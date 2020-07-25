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

import org.libj.lang.Ansi;
import org.libj.lang.Ansi.Color;
import org.libj.lang.Ansi.Intensity;
import org.libj.lang.Classes;
import org.libj.lang.Strings;
import org.libj.lang.Strings.Align;
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
      surveys[s].normalize(count);

    final long[][] min = new long[variables][divisions + 1];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(min[v], 0, divisions + 1, Long.MAX_VALUE);

    final long[][] max = new long[variables][divisions + 1];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(max[v], 0, divisions + 1, Long.MIN_VALUE);

    for (int s = 0; s < surveys.length; ++s) {
      for (int v = 0; v < variables; ++v) {
        for (int d = 0; d < divisions; ++d) {
          final long time = surveys[s].getTimes()[v][d];
          min[v][d] = Math.min(min[v][d], time);
          max[v][d] = Math.max(max[v][d], time);
        }
      }
    }

    final String[][] columns = new String[3 + surveys.length][];
    if (report != null) {
      String[] rows = columns[0] = new String[1 + 2];
      rows[0] = "";
      rows[1] = "T";
      rows[2] = Classes.getProperSimpleName(int[].class);

      for (int s = 0; s < surveys.length; ++s) {
        final Survey survey = surveys[s];
        rows = columns[1 + s] = new String[1 + 2];
        rows[0] = headings[s];
        rows[1] = String.valueOf(survey.getAllocs(survey.getSubject()));
        rows[2] = String.valueOf(survey.getAllocs(int[].class));
      }
    }
    else {
      final int[][] counts = surveys[0].getCounts();
      String[] rows = columns[0] = new String[1 + 2 * variables + variables * divisions];
      rows[0] = "length";
      for (int v = 0; v < variables; ++v)
        for (int d = 0; d < divisions; ++d)
          rows[1 + v + d * variables] = getLabel(0, v, d);

      rows = columns[1] = new String[1 + 2 * variables + variables * divisions];
      rows[0] = "precision";
      for (int v = 0; v < variables; ++v)
        for (int d = 0; d < divisions; ++d)
          rows[1 + v + d * variables] = getLabel(1, v, d);

      rows = columns[2] = new String[1 + 2 * variables + variables * divisions];
      rows[0] = "count";
      for (int v = 0; v < variables; ++v)
        for (int d = 0; d < divisions; ++d)
          rows[1 + v + d * variables] = String.valueOf(counts[v][d]);

      rows[rows.length - 1 - variables] = Ansi.apply("sum:", Intensity.BOLD, Color.CYAN);
      rows[rows.length - 1] = Ansi.apply("% fstr:", Intensity.BOLD, Color.CYAN);
      final int[][] sums = new int[surveys.length][variables];
      for (int s = 0, c = 0; s < surveys.length; ++s) {
        final Survey survey = surveys[s];
        rows = columns[s + 3] = new String[1 + 2 * variables + variables * divisions];
        rows[0] = headings[s];
        final long[][] times = survey.getTimes();
        for (int v = 0; v < variables; ++v) {
          for (int d = 0; d < divisions; ++d) {
            final long time = times[v][d];
            sums[s][v] += time;
            c = 1 + v + d * variables;
            rows[c] = String.valueOf(time);
            color(rows, c, time, min[v][d], max[v][d], s, surveys.length);
          }
        }
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

          // Output the "% fstr" row
          final int percent = (int)(100d * max[v][divisions] / sums[s][v] - 100);
          rows[c + variables] = String.valueOf(percent >= 0 ? "+" + percent : percent);
          color(rows, c + variables, sums[s][v], min[v][divisions], max[v][divisions], s, surveys.length);
        }
      }
    }

    System.out.println(label + "\n  " + count + " in " + ts + "ms\n" + Strings.printTable(true, Align.RIGHT, variables, false, columns));
  }

  private static void color(final String[] rows, final int c, final long val, final long min, final long max, final int s, final int len) {
    if (val == min)
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, Color.GREEN);
    else if (len > 2 ? s >= len - 2 : s >= len - 1)
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, val == max ? Color.RED : Color.YELLOW);
    else
      rows[c] = Ansi.apply(rows[c], Intensity.BOLD, Color.WHITE);
  }
}