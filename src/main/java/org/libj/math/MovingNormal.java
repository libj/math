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

/**
 * A mutable class that represents the moving normal algorithm.
 */
public class MovingNormal {
  private volatile double mean = 0d;
  private volatile double sum = 0d;
  private volatile double sumSq = 0d;
  private volatile double scale = 1d;
  private volatile double count = 0d;

  /**
   * Normalizes the specified array of {@code double} values between the
   * provided {@code fromIndex} and {@code toIndex} into this
   * {@code MovingNormal}.
   *
   * @param values The values to normalize.
   * @param fromIndex The from index.
   * @param toIndex The to index.
   */
  public void normalize(final double[] values, final int fromIndex, final int toIndex) {
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
   * @return The mean value of this {@code MovingNormal}.
   */
  public double getMean() {
    return mean;
  }

  /**
   * @return The scale value of this {@code MovingNormal}.
   */
  public double getScale() {
    return scale;
  }
}