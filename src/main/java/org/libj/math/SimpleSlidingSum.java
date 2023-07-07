/* Copyright (c) 2022 Seva Safris, LibJ
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

package org.libj.math;

public class SimpleSlidingSum extends WindowSampler {
  private double sum;
  private double slope;

  private final double[] samples;
  private int index;

  /**
   * Creates a {@link SimpleSlidingSum} with the specified window size and initial values.
   *
   * @param windowSize The size of the window.
   * @param values The initial values.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive value.
   * @throws NullPointerException If {@code values} is null.
   */
  public SimpleSlidingSum(final int windowSize, final double ... values) {
    this(windowSize);
    accept(values);
  }

  /**
   * Creates a {@link SimpleSlidingSum} with the specified window size.
   *
   * @param windowSize The size of the window.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive value.
   */
  public SimpleSlidingSum(final int windowSize) {
    super(windowSize);
    this.samples = new double[windowSize];
  }

  @Override
  public SimpleSlidingSum newInstance() {
    return new SimpleSlidingSum(getWindowSize());
  }

  @Override
  public boolean accept(final double value) {
    final double sum0 = sum;

    sum += value;
    if (!super.accept(value))
      sum -= samples[index];

    slope = sum - sum0;

    samples[index] = value;
    if (++index == samples.length)
      index = 0;

    return true;
  }

  @Override
  public double getValue() {
    return sum;
  }

  @Override
  public double getSlope() {
    return slope;
  }
}