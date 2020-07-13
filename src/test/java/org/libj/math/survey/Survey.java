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

import org.libj.math.survey.CaseTest.Case;

public abstract class Survey {
  private final Class<?> subject;
  private final int variables;
  private final int divisions;
  private final int[][] counts;
  private final long[][] times;
  private final long[][] max;
  private final long[][] min;
  private final Class<?>[] trackedClasses;
  private final int[] allocations;

  public Survey(final Case<?,?,?,?,?> cse, final AuditReport report, final int variables, final int divisions, final int warmup) {
    this.subject = report == null ? null : (Class<?>)cse.subject;
    this.trackedClasses = report == null ? null : report.getTrackedClasses();
    this.allocations = trackedClasses == null ? null : new int[trackedClasses.length];
    this.variables = variables;
    this.divisions = divisions;
    this.counts = new int[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(this.counts[v], -warmup);

    this.times = new long[variables][divisions];

    this.max = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(this.max[v], Long.MIN_VALUE);

    this.min = new long[variables][divisions];
    for (int v = 0; v < variables; ++v)
      Arrays.fill(this.min[v], Long.MAX_VALUE);
  }

  public Class<?> getSubject() {
    return this.subject;
  }

  public abstract int getDivision(int variable, Object obj);

  public void addSample(final int variable, final Object obj, final long time, final AuditReport report) {
    final int division = getDivision(variable, obj);
    if (++this.counts[variable][division] >= 0) {
      this.max[variable][division] = Math.max(this.max[variable][division], time);
      this.min[variable][division] = Math.min(this.min[variable][division], time);
      this.times[variable][division] += time;

      if (trackedClasses != null)
        for (int i = 0; i < trackedClasses.length; ++i)
          allocations[i] += report.getAllocations(trackedClasses[i]);
    }

    if (report != null)
      report.reset();
  }

  public Class<?>[] getTrackedClasses() {
    return trackedClasses;
  }

  public int getAllocs(final Class<?> subject) {
    if (trackedClasses != null)
      for (int i = 0; i < trackedClasses.length; ++i)
        if (trackedClasses[i] == subject)
          return allocations[i];

    return 0;
  }

  public long[][] getTimes() {
    return times;
  }

  public void normalize(final int count) {
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

//    for (int a = 0; a < allocations.length; ++a)
//      allocations[a] /= count;
  }

  public void reset() {
    for (int v = 0; v < variables; ++v)
      Arrays.fill(times[v], 0);

    for (int v = 0; v < variables; ++v)
      Arrays.fill(counts[v], 0);
  }
}