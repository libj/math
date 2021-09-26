/* Copyright (c) 2012 Seva Safris, LibJ
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

import static org.libj.lang.Assertions.*;

/**
 * A mutable {@link Number} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average">moving average</a>
 * algorithm.
 */
public class MovingAverage extends Number {
  private double average;
  protected long count;

  /**
   * Creates a {@link MovingAverage} with the specified initial values.
   *
   * @param values The initial values.
   * @throws IllegalArgumentException If {@code values} is null.
   */
  public MovingAverage(final double ... values) {
    add(values);
  }

  /**
   * Creates a {@link MovingAverage} with the specified initial value.
   *
   * @param value The initial value.
   */
  public MovingAverage(final double value) {
    this.average = value;
  }

  /**
   * Creates a {@link MovingAverage} with an initial value of {@code 0}.
   */
  public MovingAverage() {
  }

  /**
   * Add the specified {@code value} to this instance, and adjust the moving
   * average.
   *
   * @param value The value to add.
   * @return {@code this} instance.
   */
  public MovingAverage add(final double value) {
    average += (value - average) / ++count;
    return this;
  }

  /**
   * Add the specified values to this instance, and adjust the moving average.
   *
   * @param values The values to add.
   * @return {@code this} instance.
   * @throws IllegalArgumentException If {@code values} is null.
   */
  public MovingAverage add(final double ... values) {
    for (final double value : assertNotNull(values))
      add(value);

    return this;
  }

  /**
   * Returns the count of values in this {@link MovingAverage}.
   *
   * @return The count of values in this {@link MovingAverage}.
   */
  public long getCount() {
    return count;
  }

  /**
   * Returns the value of this {@link MovingAverage} as an {@code int}.
   *
   * @return The value of this {@link MovingAverage} as an {@code int}.
   */
  @Override
  public int intValue() {
    return (int)average;
  }

  /**
   * Returns the value of this {@link MovingAverage} as a {@code long}.
   *
   * @return The value of this {@link MovingAverage} as a {@code long}.
   */
  @Override
  public long longValue() {
    return (long)average;
  }

  /**
   * Returns the value of this {@link MovingAverage} as a {@code float}.
   *
   * @return The value of this {@link MovingAverage} as a {@code float}.
   */
  @Override
  public float floatValue() {
    return (float)average;
  }

  /**
   * Returns the value of this {@link MovingAverage} as a {@code double}.
   *
   * @return The value of this {@link MovingAverage} as a {@code double}.
   */
  @Override
  public double doubleValue() {
    return average;
  }

  @Override
  public String toString() {
    return String.valueOf(average);
  }
}