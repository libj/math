/* Copyright (c) 2012 LibJ
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
 * A mutable class that represents the moving normal algorithm.
 */
public class MovingNormal {
  protected double mean = 0d;
  protected double sum = 0d;
  protected double sumSq = 0d;
  protected double scale = 1d;
  protected double count = 0d;

  /**
   * Normalizes the specified array of {@code double} values between the
   * provided {@code fromIndex} and {@code toIndex} into this
   * {@link MovingNormal}.
   *
   * @param values The values to normalize.
   * @param fromIndex The from index.
   * @param toIndex The to index.
   * @throws ArrayIndexOutOfBoundsException If the given {@code fromIndex} or
   *           {@code toIndex} is out of range.
   * @throws IllegalArgumentException If {@code fromIndex > toIndex}.
   * @throws NullPointerException If {@code values} is null.
   */
  public void normalize(final int fromIndex, final int toIndex, final double ... values) {
    Assertions.assertRange(fromIndex, toIndex, values.length);

    for (int i = fromIndex; i < toIndex; ++i, ++count) {
      sum += values[i];
      sumSq += values[i] * values[i];
    }

    mean = sum / count;
    scale = StrictMath.sqrt((sumSq - sum * mean) / count);
    if (scale == 0d)
      scale = 1d;

    for (int i = fromIndex; i < toIndex; ++i)
      values[i] = (values[i] - mean) / scale;
  }

  /**
   * Returns the mean value of this {@link MovingNormal}.
   *
   * @return The mean value of this {@link MovingNormal}.
   */
  public double getMean() {
    return mean;
  }

  /**
   * Returns the scale value of this {@link MovingNormal}.
   *
   * @return The scale value of this {@link MovingNormal}.
   */
  public double getScale() {
    return scale;
  }
}