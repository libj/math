package org.libj.math;

import java.util.Arrays;

public abstract class Survey {
  private final int variables;
  private final int divisions;
  private final int[][] counts;
  private final long[][] times;

  public Survey(final int variables, final int divisions) {
    this.variables = variables;
    this.divisions = divisions;
    this.times = new long[variables][divisions];
    this.counts = new int[variables][divisions];
  }

  public abstract int getDivision(final int variable, final Object obj);

  public void addTime(final int variable, final Object obj, final long time) {
    final int division = getDivision(variable, obj);
    this.times[variable][division] += time;
    ++this.counts[variable][division];
  }

  public long[][] getTimes() {
    return times;
  }

  public void normalize() {
    for (int i = 0; i < variables; ++i)
      for (int j = 0; j < divisions; ++j)
        if (counts[i][j] != 0)
          times[i][j] /= counts[i][j];
  }

  public void reset() {
    for (int i = 0; i < variables; ++i)
      Arrays.fill(times[i], 0);

    for (int i = 0; i < variables; ++i)
      Arrays.fill(counts[i], 0);
  }
}