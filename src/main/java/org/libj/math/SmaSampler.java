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

/**
 * A mutable {@link SimpleSlidingSum} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average#Simple_moving_average">Simple Moving Average</a> algorithm.
 */
public class SmaSampler extends SimpleSlidingSum {
  /**
   * Creates a {@link SmaSampler} with the specified window size and initial values.
   *
   * @param windowSize The size of the window.
   * @param values The initial values.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive value.
   * @throws NullPointerException If {@code values} is null.
   */
  public SmaSampler(final int windowSize, final double ... values) {
    super(windowSize, values);
  }

  /**
   * Creates a {@link SmaSampler} with the specified window size.
   *
   * @param windowSize The size of the window.
   * @throws IllegalArgumentException If {@code windowSize} is not a positive value.
   */
  public SmaSampler(final int windowSize) {
    super(windowSize);
  }

  @Override
  public double getValue() {
    return super.getValue() / getSampleSize();
  }
}