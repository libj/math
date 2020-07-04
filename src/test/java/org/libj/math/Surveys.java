package org.libj.math;

import java.util.Arrays;

import org.libj.lang.Ansi;
import org.libj.lang.Strings;
import org.libj.lang.Ansi.Color;
import org.libj.lang.Ansi.Intensity;

public abstract class Surveys {
  private final int buckets;
  private final Survey[] surveys;

  public Surveys(final int size, final int buckets) {
    this.buckets = buckets;
    surveys = new Survey[size];
    for (int i = 0; i < size; ++i) {
      surveys[i] = new Survey(buckets) {
        @Override
        public int getBucket(final Object obj) {
          return Surveys.this.getBucket(obj);
        }
      };
    }
  }

  public abstract int getBucket(final Object obj);

  public void addTime(int c, Object o, long time) {
    surveys[c].addTime(o, time);
  }

  public void reset() {
    for (int i = 0; i < surveys.length; ++i)
      surveys[i].reset();
  }

  public abstract String key(int b);

  public void print(final String label, final int count, final long ts, final String ... headings) {
    for (int i = 0; i < surveys.length; ++i)
      surveys[i].normalize();

    final long[] min = new long[buckets];
    Arrays.fill(min, Long.MAX_VALUE);

    final long[] max = new long[buckets];
    Arrays.fill(max, Long.MIN_VALUE);

    for (int i = 0; i < surveys.length; ++i) {
      for (int b = 0; b < buckets; ++b) {
        min[b] = Math.min(min[b], surveys[i].getTimes()[b]);
        max[b] = Math.max(max[b], surveys[i].getTimes()[b]);
      }
    }

    final String[][] strings = new String[surveys.length + 1][];
    strings[0] = new String[buckets + 1];
    strings[0][0] = "";
    for (int j = 0; j < buckets; ++j)
      strings[0][j + 1] = key(j);

    for (int i = 0; i < surveys.length; ++i) {
      final long[] times = surveys[i].getTimes();
      strings[i + 1] = new String[times.length + 1];
      strings[i + 1][0] = headings[i];
      for (int j = 0; j < times.length; ++j) {
        strings[i + 1][j + 1] = String.valueOf(times[j]);
        if (times[j] == min[j])
          strings[i + 1][j + 1] = Ansi.apply(strings[i + 1][j + 1], Intensity.BOLD, Color.GREEN);
        else if (surveys.length > 2 ? i >= surveys.length - 2 : i >= surveys.length - 1)
          strings[i + 1][j + 1] = Ansi.apply(strings[i + 1][j + 1], Intensity.BOLD, times[j] == max[j] ? Color.RED : Color.YELLOW);
      }
    }

    System.out.println(label + "\n  " + count + " in " + ts + "ms\n" + Strings.printTable(true, true, strings));
  }
}