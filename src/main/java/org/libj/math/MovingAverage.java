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

/**
 * A mutable {@link Number} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average">moving average</a>
 * algorithm.
 */
public class MovingAverage extends Number {
  private static final long serialVersionUID = 1631326433117019519L;

  private double average;
  private long count;

  /**
   * Creates a {@link MovingAverage} with the specified initial values.
   *
   * @param values The initial values.
   * @throws NullPointerException If {@code values} is null.
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
   * Add the specified values to this instance, and adjust the moving average.
   *
   * @param values The values to add.
   * @throws NullPointerException If {@code values} is null.
   */
  public void add(final double ... values) {
    for (final double value : values)
      average += (value - average) / ++count;
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