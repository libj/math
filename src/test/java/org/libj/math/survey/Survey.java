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

public abstract class Survey {
  private final int variables;
  private final int divisions;
  private final int[][] counts;
  private final long[][] times;
  private final long[][] max;
  private final long[][] min;

  public Survey(final int variables, final int divisions) {
    this.variables = variables;
    this.divisions = divisions;
    this.counts = new int[variables][divisions];
    this.times = new long[variables][divisions];

    this.max = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(this.max[v], Long.MIN_VALUE);

    this.min = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(this.min[v], Long.MAX_VALUE);
  }

  public abstract int getDivision(final int variable, final Object obj);

  public void addTime(final int variable, final Object obj, final long time) {
    final int division = getDivision(variable, obj);
    this.max[variable][division] = Math.max(this.max[variable][division], time);
    this.min[variable][division] = Math.min(this.min[variable][division], time);
    this.times[variable][division] += time;
    ++this.counts[variable][division];
  }

  public long[][] getTimes() {
    return times;
  }

  public void normalize() {
    for (int v = 0; v < variables; ++v) {
      for (int d = 0; d < divisions; ++d) {
        if (counts[v][d] == 0)
          continue;

        if (counts[v][d] <= 2) {
          times[v][d] /= counts[v][d];
        }
        else {
          times[v][d] -= max[v][d];
          times[v][d] -= min[v][d];
          times[v][d] /= counts[v][d] - 2;
        }
      }
    }
  }

  public void reset() {
    for (int v = 0; v < variables; ++v)
      Arrays.fill(times[v], 0);

    for (int v = 0; v < variables; ++v)
      Arrays.fill(counts[v], 0);
  }
}