package org.libj.math;

import java.util.Arrays;

public abstract class Survey {
  private final int buckets;
  private final int[] counts;
  private final long[] times;

  public Survey(final int buckets) {
    this.buckets = buckets;
    this.times = new long[buckets];
    this.counts = new int[buckets];
  }

  public abstract int getBucket(final Object obj);

  public void addTime(final Object obj, final long time) {
    final int i = getBucket(obj);
    this.times[i] += time;
    ++this.counts[i];
  }

  public long[] getTimes() {
    return times;
  }

  public void reset() {
    Arrays.fill(times, 0);
    Arrays.fill(counts, 0);
  }

  public void normalize() {
    for (int i = 0; i < buckets; ++i)
      if (counts[i] != 0)
        times[i] /= counts[i];
  }
}