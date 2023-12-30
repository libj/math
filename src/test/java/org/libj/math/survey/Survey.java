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
import java.math.RoundingMode;
import java.util.Arrays;

import org.libj.math.survey.CaseTest.Case;

public abstract class Survey {
  private final Case<?,?,?,?,?> cse;
  private final Class<?> subject;
  private final int variables;
  private final int divisions;
  private final int[][] counts;
  private final long[][] times;
  private final long[][] max;
  private final long[][] min;
  private final BigDecimal[][] error;
  private boolean hasError;
  private final Class<?>[] trackedClasses;
  private final int[][] allocations;

  public Survey(final Case<?,?,?,?,?> cse, final AuditReport report, final int variables, final int divisions, final int warmup) {
    this.cse = cse;
    this.subject = cse.subject instanceof Class ? (Class<?>)cse.subject : null;
    this.trackedClasses = report == null ? null : report.getTrackedClasses();
    this.allocations = trackedClasses == null ? null : new int[trackedClasses.length][divisions];
    this.variables = variables;
    this.divisions = divisions;
    this.counts = new int[variables][divisions];
    if (trackedClasses == null)
      for (int v = 0; v < variables; ++v) // [A]
        Arrays.fill(this.counts[v], -warmup);

    this.times = new long[variables][divisions];

    this.max = new long[variables][divisions];
    for (int v = 0; v < variables; ++v) // [A]
      Arrays.fill(this.max[v], Long.MIN_VALUE);

    this.min = new long[variables][divisions];
    for (int v = 0; v < variables; ++v) // [A]
      Arrays.fill(this.min[v], Long.MAX_VALUE);

    this.error = new BigDecimal[variables][divisions];
    for (int v = 0; v < variables; ++v) // [A]
      Arrays.fill(this.error[v], BigDecimal.ZERO);
  }

  public Class<?> getSubject() {
    return subject;
  }

  public Case<?,?,?,?,?> getCase() {
    return cse;
  }

  public abstract int getDivision(int variable, Object obj);

  public void addSample(final int variable, final Object obj, final long time, BigDecimal error, final AuditReport report) {
    final int division = getDivision(variable, obj);
    if (++this.counts[variable][division] >= 0) {
      this.max[variable][division] = Math.max(this.max[variable][division], time);
      this.min[variable][division] = Math.min(this.min[variable][division], time);
      this.times[variable][division] += time;
      if (error != null && error.signum() != 0) {
        hasError = true;
        error = error.setScale(1 + error.scale() - error.precision(), RoundingMode.CEILING);
        if (error.compareTo(this.error[variable][division]) > 0)
          this.error[variable][division] = error;
      }

      if (report != null && report.getMode() == 1) {
        for (int c = 0, c$ = trackedClasses.length; c < c$; ++c) // [A]
          allocations[c][division] += report.getAllocations(trackedClasses[c]);

        report.reset();
      }
    }
  }

  public Class<?>[] getTrackedClasses() {
    return trackedClasses;
  }

  public int getAllocs(final Class<?> subject, final int division) {
    if (trackedClasses != null)
      for (int c = 0, c$ = trackedClasses.length; c < c$; ++c) // [A]
        if (trackedClasses[c] == subject)
          return allocations[c][division];

    return 0;
  }

  public int[][] getCounts() {
    return counts;
  }

  public long[][] getTimes() {
    return times;
  }

  public BigDecimal[][] getError() {
    return error;
  }

  public boolean hasError() {
    return hasError;
  }

  public void normalize() {
    for (int v = 0; v < variables; ++v) { // [A]
      for (int d = 0; d < divisions; ++d) { // [A]
        if (counts[v][d] == 0)
          continue;

        if (counts[v][d] <= 2) {
          times[v][d] /= counts[v][d];
        }
        else {
          times[v][d] -= max[v][d] + min[v][d];
          times[v][d] /= counts[v][d] - 2;
        }
      }
    }

    if (trackedClasses != null)
      for (int d = 0; d < divisions; ++d) // [A]
        for (int c = 0, c$ = allocations.length; c < c$; ++c) // [A]
          if (counts[0][d] != 0)
            allocations[c][d] = (int)Math.round((double)allocations[c][d] / counts[0][d]);
  }

  public void reset() {
    for (int v = 0; v < variables; ++v) { // [A]
      Arrays.fill(times[v], 0);
      Arrays.fill(counts[v], 0);
    }
  }
}