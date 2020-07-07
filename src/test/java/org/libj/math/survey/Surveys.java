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
import org.libj.lang.Strings;

public abstract class Surveys {
  private final int variables;
  private final int divisions;
  private final Survey[] surveys;

  public Surveys(final int size, final int variables, final int divisions) {
    this.variables = variables;
    this.divisions = divisions;
    surveys = new Survey[size];
    for (int i = 0; i < size; ++i) {
      surveys[i] = new Survey(variables, divisions) {
        @Override
        public int getDivision(final int variable, final Object obj) {
          return Surveys.this.getDivision(variable, obj);
        }
      };
    }
  }

  public abstract int getDivision(int variable, Object obj);

  public void addTime(final int survey, final int variable, final Object obj, final long time) {
    surveys[survey].addTime(variable, obj, time);
  }

  public void reset() {
    for (int i = 0; i < surveys.length; ++i)
      surveys[i].reset();
  }

  public abstract String key(int variable, int division);

  public void print(final String label, final int count, final long ts, final String ... headings) {
    for (int s = 0; s < surveys.length; ++s)
      surveys[s].normalize();

    final long[][] min = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(min[v], Long.MAX_VALUE);

    final long[][] max = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(max[v], Long.MIN_VALUE);

    for (int s = 0; s < surveys.length; ++s) {
      for (int v = 0; v < variables; ++v) {
        for (int d = 0; d < divisions; ++d) {
          final long time = surveys[s].getTimes()[v][d];
          min[v][d] = Math.min(min[v][d], time);
          max[v][d] = Math.max(max[v][d], time);
        }
      }
    }

    final String[][] columns = new String[surveys.length + 1][];
    columns[0] = new String[variables * divisions + 1];
    columns[0][0] = "";
    for (int v = 0; v < variables; ++v)
      for (int d = 0; d < divisions; ++d)
        columns[0][v + d * variables + 1] = key(v, d);

    for (int s = 0; s < surveys.length; ++s) {
      columns[s + 1] = new String[variables * divisions + 1];
      columns[s + 1][0] = headings[s];
      final long[][] times = surveys[s].getTimes();
      for (int v = 0; v < variables; ++v) {
        for (int d = 0; d < divisions; ++d) {
          final long time = times[v][d];
          final int c = v + d * variables + 1;
          columns[s + 1][c] = String.valueOf(time);
          if (time == min[v][d])
            columns[s + 1][c] = Ansi.apply(columns[s + 1][c], Intensity.BOLD, Color.GREEN);
          else if (surveys.length > 2 ? s >= surveys.length - 2 : s >= surveys.length - 1)
            columns[s + 1][c] = Ansi.apply(columns[s + 1][c], Intensity.BOLD, time == max[v][d] ? Color.RED : Color.YELLOW);
          else
            columns[s + 1][c] = Ansi.apply(columns[s + 1][c], Intensity.BOLD, Color.WHITE);
        }
      }
    }

    System.out.println(label + "\n  " + count + " in " + ts + "ms\n" + Strings.printTable(true, true, variables, false, columns));
  }
}