/* Copyright (c) 2021 Seva Safris, LibJ
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

import org.libj.lang.Assertions;

/**
 * A mutable {@link Number} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average">moving average</a>
 * algorithm with sliding window.
 */
public class SlidingWindowAverage extends MovingAverage {
  private static final long serialVersionUID = -2727156410200819678L;

  private double total;

  private final double[] samples;
  private int index;

  /**
   * Creates a {@link SlidingWindowAverage} with the specified window size and
   * initial values.
   *
   * @param windowSize The size of the window.
   * @param values The initial values.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive
   *           value, or {@code values} is null.
   */
  public SlidingWindowAverage(final int windowSize, final double ... values) {
    this(windowSize);
    add(values);
  }

  /**
   * Creates a {@link SlidingWindowAverage} with the specified window size.
   *
   * @param windowSize The size of the window.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive
   *           value.
   */
  public SlidingWindowAverage(final int windowSize) {
    this.samples = new double[Assertions.assertPositive(windowSize)];
  }

  /**
   * Add the specified {@code value} to this instance, and adjust the moving average.
   *
   * @param value The value to add.
   * @return {@code this} instance.
   */
  @Override
  public SlidingWindowAverage add(final double value) {
    if (count < samples.length) {
      ++count;
      total += value;
    }
    else {
      total += value - samples[index];
    }

    samples[index] = value;
    if (++index == samples.length)
      index = 0;

    return this;
  }

  @Override
  public SlidingWindowAverage add(final double ... values) {
    return (SlidingWindowAverage)super.add(values);
  }

  /**
   * Returns the count of values in this {@link SlidingWindowAverage}.
   *
   * @return The count of values in this {@link SlidingWindowAverage}.
   */
  @Override
  public long getCount() {
    return count;
  }

  /**
   * Returns the value of this {@link SlidingWindowAverage} as an {@code int}.
   *
   * @return The value of this {@link SlidingWindowAverage} as an {@code int}.
   */
  @Override
  public int intValue() {
    return (int)doubleValue();
  }

  /**
   * Returns the value of this {@link SlidingWindowAverage} as a {@code long}.
   *
   * @return The value of this {@link SlidingWindowAverage} as a {@code long}.
   */
  @Override
  public long longValue() {
    return (long)doubleValue();
  }

  /**
   * Returns the value of this {@link SlidingWindowAverage} as a {@code float}.
   *
   * @return The value of this {@link SlidingWindowAverage} as a {@code float}.
   */
  @Override
  public float floatValue() {
    return (float)doubleValue();
  }

  /**
   * Returns the value of this {@link SlidingWindowAverage} as a {@code double}.
   *
   * @return The value of this {@link SlidingWindowAverage} as a {@code double}.
   */
  @Override
  public double doubleValue() {
    return total / count;
  }

  @Override
  public String toString() {
    return String.valueOf(doubleValue());
  }
}