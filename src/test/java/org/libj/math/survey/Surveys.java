/* Copyright (c) 2020 Seva Safris, LibJ
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

import java.math.BigDecimal;
import java.util.Arrays;

import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;
import org.libj.console.Ansi.Intensity;
import org.libj.console.Tables;
import org.libj.console.drawille.Canvas;
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
    this.surveys = new Survey[cases.length];
    for (int i = 0, i$ = cases.length; i < i$; ++i) { // [A]
      this.surveys[i] = new Survey(cases[i], report, variables, divisions, warmup) {
        @Override
        public int getDivision(final int variable, final Object obj) {
          return Surveys.this.getDivision(variable, obj);
        }
      };
    }
  }

  public abstract int getDivision(int variable, Object obj);

  public void addSample(final int survey, final int variable, final Object obj, final long time, final BigDecimal error) {
    surveys[survey].addSample(variable, obj, time, error, report);
  }

  public void onSuccess() {
    ++count;
  }

  public void reset() {
    for (int s = 0, s$ = surveys.length; s < s$; ++s) // [A]
      surveys[s].reset();
  }

  public boolean hasError() {
    for (int s = 0, s$ = surveys.length; s < s$; ++s) // [A]
      if (surveys[s].hasError())
        return true;

    return false;
  }

  public abstract String getLabel(int index, int variable, int division);

  public String print(final String label, final long runTime, final String[] summary, final String ... headings) {
    for (int s = 0, s$ = surveys.length; s < s$; ++s) // [A]
      surveys[s].normalize();

    final int categories = summary.length / surveys.length;

    final long[][] min = new long[categories][divisions + 1];
    for (int v = 0; v < categories; ++v) // [A]
      Arrays.fill(min[v], 0, divisions + 1, Long.MAX_VALUE);

    final long[][] max = new long[categories][divisions + 1];
    for (int v = 0; v < categories; ++v) // [A]
      Arrays.fill(max[v], 0, divisions + 1, Long.MIN_VALUE);

    long absMin = Integer.MAX_VALUE;
    long absMax = Integer.MIN_VALUE;

    final int extraRows = 1 + 2 * variables;
    final Canvas canvas;
    final String[][] columns = new String[3 + surveys.length][];
    final int[][] counts = surveys[0].getCounts();
    String[] rows = columns[0] = new String[extraRows + variables * divisions];
    rows[0] = "length";
    for (int v = 0; v < variables; ++v) // [A]
      for (int d = 0; d < divisions; ++d) // [A]
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = getLabel(0, v, d);

    rows = columns[1] = new String[extraRows + variables * divisions];
    rows[0] = "precision";
    for (int v = 0; v < variables; ++v) // [A]
      for (int d = 0; d < divisions; ++d) // [A]
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = getLabel(1, v, d);

    rows = columns[2] = new String[extraRows + variables * divisions];
    rows[0] = "count";
    for (int v = 0; v < variables; ++v) // [A]
      for (int d = 0; d < divisions; ++d) // [A]
        if (counts[v][d] > 0)
          rows[1 + v + d * variables] = String.valueOf(counts[v][d]);

    rows[rows.length - 1 - variables] = Ansi.apply("sum:", Intensity.BOLD, Color.CYAN);
    rows[rows.length - 1] = Ansi.apply("+%:", Intensity.BOLD, Color.CYAN);

    final int[][] sums = new int[surveys.length][categories];
    if (report != null && report.getMode() == 1) {
      for (int s = 0, s$ = surveys.length; s < s$; ++s) { // [A]
        final Survey survey = surveys[s];
        for (int d = 0; d < divisions; ++d) { // [A]
          final int subject = survey.getAllocs(survey.getSubject(), d);
          final int array = survey.getAllocs(int[].class, d);
          min[0][d] = Math.min(min[0][d], subject);
          min[1][d] = Math.min(min[1][d], array);
          max[0][d] = Math.max(max[0][d], subject);
          max[1][d] = Math.max(max[1][d], array);
          absMin = Math.min(absMin, subject);
          absMin = Math.min(absMin, array);
          absMax = Math.max(absMax, subject);
          absMax = Math.max(absMax, array);
          sums[s][0] += subject;
          sums[s][1] += array;
        }
      }

      // Heading
      canvas = null;
      for (int s = 0, r = 0; s < surveys.length; ++s) { // [A]
        final Survey survey = surveys[s];
        rows = columns[s + 3] = new String[extraRows + variables * divisions];
        rows[0] = headings[s] = getColor(survey.getCase()).apply(headings[s]) + "\n" + getColor(survey.getCase()).apply("    T │ " + int[].class.getCanonicalName());
        for (int d = 0; d < divisions; ++d) { // [A]
          r = 1 + d * variables;
          final int subject = survey.getAllocs(survey.getSubject(), d);
          final int array = survey.getAllocs(int[].class, d);
          if (variables == 1) {
            rows[r] = subject + " │ " + Strings.pad(String.valueOf(array), Align.RIGHT, 5);
            rows[r] = color(rows[r], subject, min[0][d], max[0][d], s, surveys.length);
          }
          else {
            rows[r] = String.valueOf(subject);
            rows[r] = color(rows[r], subject, min[0][d], max[0][d], s, surveys.length);
            rows[r + 1] = String.valueOf(array);
            rows[r + 1] = color(rows[r + 1], subject, min[1][d], max[1][d], s, surveys.length);
          }
        }
      }
    }
    else {
      for (int s = 0, s$ = surveys.length; s < s$; ++s) { // [A]
        for (int c = 0; c < categories; ++c) { // [A]
          for (int d = 0; d < divisions; ++d) { // [A]
            final long time = surveys[s].getTimes()[c][d];
            min[c][d] = Math.min(min[c][d], time);
            max[c][d] = Math.max(max[c][d], time);
            absMin = Math.min(absMin, time);
            absMax = Math.max(absMax, time);
          }
        }
      }

      canvas = new Canvas(90, 20, Color.DEFAULT);
      final float[] x = new float[divisions];
      final float[] y = new float[divisions];
      for (int s = 0, r = 0; s < surveys.length; ++s) { // [A]
        final Survey survey = surveys[s];
        rows = columns[s + 3] = new String[extraRows + variables * divisions];
        rows[0] = headings[s] = getColor(survey.getCase()).apply(headings[s]);
        final long[][] times = survey.getTimes();
        for (int d = 0; d < divisions; ++d) { // [A]
          long avgTime = 0, avgMin = absMin * 2, avgMax = absMax * 2;
          for (int v = 0; v < variables; ++v) { // [A]
            final long time = times[v][d];
            avgTime += time;
            sums[s][v] += time;
            avgMin += min[v][d];
            avgMax += max[v][d];
            r = 1 + v + d * variables;
            rows[r] = time == 0 ? "" : String.valueOf(time);
            rows[r] = color(rows[r], time, min[v][d], max[v][d], s, surveys.length);
          }

          avgTime /= 2;
          avgMin /= 4; // Find the middle between the local min and the absolute min
          avgMax /= 4; // Find the middle between the local max and the absolute max
          x[d] = (float)d * canvas.getWidth() / divisions;
          y[d] = ((float)avgTime - avgMin) / (avgMax - avgMin) * (canvas.getHeight() - 8); // Padding of 8
        }

        final SplineInterpolator<?> spline = SplineInterpolator.createMonotoneCubicSpline(x, y);
        for (int u = 0, u$ = canvas.getWidth(); u < u$; ++u) // [N]
          canvas.set(u, (int)Math.min(canvas.getHeight() - 1, Math.max(0, spline.interpolate(u))), getColor(survey.getCase()));
      }
    }

    // Set the overall min and max for all surveys
    for (int s = 0, s$ = surveys.length; s < s$; ++s) { // [A]
      for (int c = 0; c < categories; ++c) { // [A]
        min[c][divisions] = Math.min(min[c][divisions], sums[s][c]);
        max[c][divisions] = Math.max(max[c][divisions], sums[s][c]);
      }
    }

    for (int s = 0, r = 0; s < surveys.length; ++s) { // [A]
      rows = columns[s + 3];
      for (int c = 0; c < categories; ++c) { // [A]
        r = 1 + c + divisions * variables;

        final int percent = (int)(100d * max[c][divisions] / sums[s][c] - 100);

        String r0 = String.valueOf(sums[s][c]);
        String r1 = String.valueOf(percent == Integer.MAX_VALUE ? "+∞" : percent >= 0 ? "+" + percent : percent);
        r0 = color(r0, sums[s][c], min[c][divisions], max[c][divisions], s, surveys.length);
        r1 = color(r1, sums[s][c], min[c][divisions], max[c][divisions], s, surveys.length);

        // Output the "sum" and "+%" rows
        if (categories == variables || c == 0) {
          rows[r] = r0;
          rows[r + variables] = r1;
        }
        else {
          r -= variables;
          rows[r] += " │" + Strings.pad(" " + r0, Align.RIGHT, 6, true);
          rows[r + variables] += " │" + Strings.pad(" " + r1, Align.RIGHT, 6, true);
        }

        summary[s * categories + c] = r1;
      }
    }

    final StringBuilder builder = new StringBuilder();
    builder.append(label + "\n  " + count + " in " + runTime + "ms\n" + Tables.printTable(true, Align.CENTER, Align.RIGHT, variables, false, columns));

    builder.append('\n');
    final String[][] errors = new String[surveys.length][1 + variables * divisions];
    for (int s = 0, s$ = surveys.length; s < s$; ++s) { // [A]
      final Survey survey = surveys[s];
      errors[s][0] = getColor(survey.getCase()).apply(headings[s]);
      final BigDecimal[][] error = survey.getError();
      for (int d = 0; d < divisions; ++d) // [A]
        for (int v = 0; v < variables; ++v) // [A]
          errors[s][d * variables + v + 1] = error[v][d].toString();
    }

    StringBuilder out;
    if (hasError()) {
      final String[] tables = {builder.toString(), "\nerror profile\n" + Tables.printTable(true, Align.CENTER, Align.RIGHT, variables, false, errors)};
      out = new StringBuilder(Tables.printTable(false, Align.LEFT, Align.LEFT, 1, false, tables));
    }
    else {
      out = builder;
    }

    if (canvas != null)
      out.append('\n').append(canvas);

    // Remove redundant Color RESET->SET
    Strings.replace(out, "\033[0;39m\033[", "\033[");
    return out.toString();
  }

  private static String color(final String row, final long val, final long min, final long max, final int s, final int len) {
    if (val == min)
      return Ansi.apply(row, Intensity.BOLD, Color.GREEN);

    if (len > 2 ? s >= len - 2 : s >= len - 1)
      return Ansi.apply(row, Intensity.BOLD, val == max ? Color.RED : Color.YELLOW);

    return Ansi.apply(row, Intensity.BOLD, Color.WHITE);
  }

  public abstract Ansi.Color getColor(Case<?,?,?,?,?> cse);
}