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
 * An abstract representation of a mutable {@link Sampler} that represents a rolling sampler.
 */
public abstract class RollingSampler implements Sampler {
  private int size;

  @Override
  public boolean accept(final double value) {
    ++size;
    return true;
  }

  /**
   * Returns the count of samples accepted by {@code this} {@link RollingSampler}.
   *
   * @return The count of samples accepted by {@code this} {@link RollingSampler}.
   */
  public int getSampleCount() {
    return size;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(getSampleCount()) ^ Double.hashCode(getValue());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(getClass().isInstance(obj)))
      return false;

    final RollingSampler that = (RollingSampler)obj;
    return getSampleCount() == that.getSampleCount() && getValue() == that.getValue();
  }

  @Override
  public String toString() {
    return String.valueOf(getValue());
  }
}