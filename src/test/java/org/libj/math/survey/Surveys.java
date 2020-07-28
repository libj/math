/* Copyright (c) 2020 LibJ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.math.survey;

import java.util.Arrays;

import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;
import org.libj.console.Ansi.Intensity;
import org.libj.console.Tables;
import org.libj.console.drawille.Canvas;
import org.libj.lang.Classes;
import org.libj.lang.Strings;
import org.libj.lang.Strings.Align;
import org.libj.math.SplineInterpolator;
import org.libj.math.survey.CaseTest.Case;

public abstract class Surveys {
  private final int variables;
  private final int divisions;
  private final Survey[] surveys;
  private final AuditReport report;
  private int count;

  public Surveys(final Case<?,?,?,?,?>[] cases, final int variables, final int divisions, final int warmup, final AuditReport report) {
    this.variables = variables;
    this.divisions = divisions;
    this.report = report;
    this.count = 0;
    surveys = new Survey[cases.length];
    for (int i = 0; i < cases.length; ++i) {
      surveys[i] = new Survey(cases[i], report, variables, divisions, warmup) {
        @Override
        public int getDivision(final int variable, final Object obj) {
          return Surveys.this.getDivision(variable, obj);
        }
      };
    }
  }

  public abstract int getDivision(int variable, Object obj);

  public void addSample(final int survey, final int variable, final Object obj, final long time) {
    surveys[survey].addSample(variable, obj, time, report);
  }

  public void onSuccess() {
    ++count;
  }

  public void reset() {
    for (int s = 0; s < surveys.length; ++s)
      surveys[s].reset();
  }

  public abstract String getLabel(int index, int variable, int division);

  public void print(final String label, final long ts, final String ... headings) {
    for (int s = 0; s < surveys.length; ++s)
      surveys[s].normalize();

    final long[][] min = new long[variables][divisions + 1];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(min[v], 0, divisions + 1, Long.MAX_VALUE);

    final long[][] max = new long[variables][divisions + 1];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(max[v], 0, divisions + 1, Long.MIN_VALUE);

    long absMin = Integer.MAX_VALUE;
    long absMax = Integer.MIN_VALUE;

    for (int s = 0; s < surveys.length; ++s) {
      for (int v = 0; v < variables; ++v) {
        for (int d = 0; d < divisions; ++d) {
          final long time = surveys[s].getTimes()[v][d];
          min[v][d] = Math.min(min[v][d], time);
          max[v][d] = Math.max(max[v][d], time);
          absMin = Math.min(absMin, time);
          absMax = Math.max(absMax, time);
        }
      }
    }

    final Canvas canvas;
    final String[][] columns = new String[3 + surveys.length][];
    final int[][] counts = surveys[0].getCounts();
    String[] rows = columns[0] = new String[1 + variables * divisions];
    rows[0] = "length";
    for (int v = 0; v < variables; ++v)
      for (int d = 0; d < divisions; ++d)
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = getLabel(0, v, d);

    rows = columns[1] = new String[1 + variables * divisions];
    rows[0] = "precision";
    for (int v = 0; v < variables; ++v)
      for (int d = 0; d < divisions; ++d)
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = getLabel(1, v, d);

    rows = columns[2] = new String[1 + variables * divisions];
    rows[0] = "count";
    for (int v = 0; v < variables; ++v)
      for (int d = 0; d < divisions; ++d)
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = String.valueOf(counts[v][d]);

    if (report != null) {
      canvas = null;
      for (int s = 0, c = 0; s < surveys.length; ++s) {
        final Survey survey = surveys[s];
        rows = columns[s + 3] = new String[1 + divisions];
        rows[0] = getColor(survey.getCase()).apply(headings[s]) + "\n" + getColor(survey.getCase()).apply("    T │ " + Classes.getProperSimpleName(int[].class));
        for (int d = 0; d < divisions; ++d) {
          c = 1 + d * variables;
          if (variables == 1) {
            rows[c] = survey.getAllocs(survey.getSubject(), d) + " │ " + Strings.pad(String.valueOf(survey.getAllocs(int[].class, d)), Align.RIGHT, 5);
          }
          else {
            rows[c] = String.valueOf(survey.getAllocs(survey.getSubject(), d));
            rows[c + 1] = String.valueOf(survey.getAllocs(int[].class, d));
          }
        }
      }
    }
    else {
      canvas = new Canvas(90, 20);
      rows[rows.length - 1 - variables] = Ansi.apply("sum:", Intensity.BOLD, Color.CYAN);
      rows[rows.length - 1] = Ansi.apply("+%:", Intensity.BOLD, Color.CYAN);
      final float[] x = new float[divisions];
      final float[] y = new float[divisions];
      final int[][] sums = new int[surveys.length][variables];
      for (int s = 0, c = 0; s < surveys.length; ++s) {
        final Survey survey = surveys[s];
        rows = columns[s + 3] = new String[1 + 2 * variables + variables * divisions];
        rows[0] = getColor(survey.getCase()).apply(headings[s]);
        final long[][] times = survey.getTimes();
        for (int d = 0; d < divisions; ++d) {
          long avgTime = 0;
          for (int v = 0; v < variables; ++v) {
            final long time = times[v][d];
            avgTime += time;
            sums[s][v] += time;
            c = 1 + v + d * variables;
            rows[c] = String.valueOf(time);
            color(rows, c, time, min[v][d], max[v][d], s, surveys.length);
          }

          avgTime /= 2;
          x[d] = (float)d * canvas.getWidth() / divisions;
          y[d] = ((float)avgTime - absMin) / (absMax - absMin) * (canvas.getHeight() - 8); // Padding of 8
        }

        final SplineInterpolator spline = SplineInterpolator.createMonotoneCubicSpline(x, y);
        for (int u = 0; u < canvas.getWidth(); ++u)
          canvas.set(u, (int)Math.min(canvas.getHeight() - 1, Math.max(0, spline.interpolate(u))), getColor(survey.getCase()));
      }

      // Set the overall min and max for all surveys
      for (int s = 0; s < surveys.length; ++s) {
        for (int v = 0; v < variables; ++v) {
          min[v][divisions] = Math.min(min[v][divisions], sums[s][v]);
          max[v][divisions] = Math.max(max[v][divisions], sums[s][v]);
        }
      }

      for (int s = 0, c = 0; s < surveys.length; ++s) {
        rows = columns[s + 3];
        for (int v = 0; v < variables; ++v) {
          c = 1 + v + divisions * variables;

          // Output the "sum" row
          c = 1 + v + divisions * variables;
          rows[c] = String.valueOf(sums[s][v]);
          color(rows, c, sums[s][v], min[v][divisions], max[v][divisions], s, surveys.length);

          // Output the "+%" row
          final int percent = (int)(100d * max[v][divisions] / sums[s][v] - 100);
          rows[c + variables] = String.valueOf(percent >= 0 ? "+" + percent : percent);
          color(rows, c + variables, sums[s][v], min[v][divisions], max[v][divisions], s, surveys.length);
        }
      }
    }

    System.out.println(label + "\n  " + count + " in " + ts + "ms\n" + Tables.printTable(true, Align.RIGHT, variables, false, columns));
    if (canvas != null) {
      System.out.println();
      canvas.render();
    }
  }

  private static void color(final String[] rows, final int c, final long val, final long min, final long max, final int s, final int len) {
    if (val == min)
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, Color.GREEN);
    else if (len > 2 ? s >= len - 2 : s >= len - 1)
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, val == max ? Color.RED : Color.YELLOW);
    else
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, Color.WHITE);
  }

  public abstract Ansi.Color getColor(Case<?,?,?,?,?> cse);
}