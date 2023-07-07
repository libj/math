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
 * A mutable {@link RollingSampler} that represents the
 * <a href="https://en.wikipedia.org/wiki/Moving_average#Simple_moving_average">Simple Moving Average</a> algorithm.
 */
public class SimpleRollingAverage extends RollingSampler {
  private double avg;
  private double slope;

  /**
   * Creates a {@link SimpleRollingAverage} with the specified initial values.
   *
   * @param values The initial values.
   * @throws NullPointerException If {@code values} is null.
   */
  public SimpleRollingAverage(final double ... values) {
    accept(values);
  }

  /**
   * Creates a {@link SimpleRollingAverage} with the specified initial value.
   *
   * @param value The initial value.
   */
  public SimpleRollingAverage(final double value) {
    this.avg = value;
  }

  @Override
  public SimpleRollingAverage newInstance() {
    return new SimpleRollingAverage();
  }

  /**
   * Creates a {@link SimpleRollingAverage} with an initial value of {@code 0}.
   */
  public SimpleRollingAverage() {
  }

  @Override
  public boolean accept(final double value) {
    super.accept(value);
    final double avg0 = avg;
    avg += (value - avg) / getSampleCount();
    slope = avg - avg0;
    return true;
  }

  @Override
  public double getValue() {
    return avg;
  }

  @Override
  public double getSlope() {
    return slope;
  }
}