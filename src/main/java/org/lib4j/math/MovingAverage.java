/* Copyright (c) 2012 lib4j
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

package org.lib4j.math;

public final class MovingAverage extends Number {
  private static final long serialVersionUID = 1631326433117019519L;

  private volatile double average = 0;
  private volatile long count = 0;

  public MovingAverage(final double ... values) {
    add(values);
  }

  public MovingAverage(final double value) {
    this.average = value;
  }

  public MovingAverage() {
  }

  public void add(final double ... values) {
    for (final double value : values)
      average += (value - average) / ++count;
  }

  public long getCount() {
    return count;
  }

  @Override
  public int intValue() {
    return (int)average;
  }

  @Override
  public long longValue() {
    return (long)average;
  }

  @Override
  public float floatValue() {
    return (float)average;
  }

  @Override
  public double doubleValue() {
    return average;
  }

  @Override
  public String toString() {
    return String.valueOf(average);
  }
}