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

import java.util.Objects;

/**
 * An abstract representation of a mutable {@link Sampler} that represents a window sampler.
 */
public abstract class WindowSampler implements Sampler {
  private final int windowSize;
  private int sampleCount;

  public WindowSampler(final int windowSize) {
    this.windowSize = assertPositive(windowSize);
  }

  @Override
  public boolean accept(final double value) {
    assertFinite(value);
    return ++sampleCount <= windowSize;
  }

  /**
   * Returns the count of samples accepted by {@code this} {@link WindowSampler}.
   *
   * @return The count of samples accepted by {@code this} {@link WindowSampler}.
   */
  public int getSampleCount() {
    return sampleCount;
  }

  public int getSampleSize() {
    return Math.min(sampleCount, windowSize);
  }

  public int getWindowSize() {
    return windowSize;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(getSampleCount()) ^ Long.hashCode(getWindowSize()) ^ Objects.hashCode(getValue());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(getClass().isInstance(obj)))
      return false;

    final WindowSampler that = (WindowSampler)obj;
    return getSampleCount() == that.getSampleCount() && getWindowSize() == that.getWindowSize() && getValue() == that.getValue();
  }

  @Override
  public String toString() {
    final double value = getValue();
    return !Double.isFinite(value) ? "null" : value == 0 ? "0" : Double.toString(value);
  }
}