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
 * A mutable {@link WindowSampler} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average">Exponential Moving Average</a> algorithm.
 */
public class EmaSampler extends WindowSampler {
  protected final double alpha;
  protected double ema = Double.NaN;
  protected double slope = Double.NaN;

  /**
   * Creates a {@link EmaSampler} with an initial value of {@code 0}.
   *
   * @param smoothing The "smoothing factor".
   */
  public EmaSampler(final double smoothing, final int windowSize) {
    this(windowSize, smoothing / (windowSize + 1));
  }

  private EmaSampler(final int windowSize, final double alpha) {
    super(windowSize);
    this.alpha = alpha;
  }

  @Override
  public EmaSampler newInstance() {
    return new EmaSampler(getWindowSize(), alpha);
  }

  /**
   * Add the specified {@code value} to this instance, and adjust the moving average.
   *
   * @param value The value to add.
   */
  @Override
  public boolean accept(final double value) {
    super.accept(value);
    if (Double.isNaN(ema)) {
      ema = value;
    }
    else {
      final double ema0 = ema;
      ema = ((value - ema0) * alpha) + ema0;

      slope = ema - ema0;
    }

    return true;
  }

  @Override
  public double getValue() {
    return ema;
  }

  @Override
  public double getSlope() {
    return slope;
  }
}