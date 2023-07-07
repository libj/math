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

public class DemaSampler extends WindowSampler {
  private final double alpha;

  private double ema = Double.NaN;
  private double emaEma;
  private double dema;

  private double slope;

  /**
   * Creates a {@link DemaSampler} with an initial value of {@code 0}.
   *
   * @param smoothing The "smoothing factor".
   */
  public DemaSampler(final double smoothing, final int windowSize) {
    super(windowSize);
    this.alpha = smoothing / (windowSize + 1);
  }

  private DemaSampler(final int windowSize, final double alpha) {
    super(windowSize);
    this.alpha = alpha;
  }

  @Override
  public DemaSampler newInstance() {
    return new DemaSampler(getWindowSize(), alpha);
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
      ema = emaEma = value;
    }
    else {
      final double ema0 = ema;
      ema = ((value - ema0) * alpha) + ema0;

      final double emaEma0 = emaEma;
      emaEma = ((ema - emaEma0) * alpha) + emaEma0;

      dema = 2 * ema - emaEma;

      slope = ema - ema0;
    }

    return true;
  }

  @Override
  public double getValue() {
    return dema;
  }

  @Override
  public double getSlope() {
    return slope;
  }
}