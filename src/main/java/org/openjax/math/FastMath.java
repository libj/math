/* Copyright (c) 2018 OpenJAX
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

package org.openjax.math;

/**
 * Utility that supplements functions in {@link Math}.
 */
public final class FastMath {
  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   * <p>
   * Note this method only works for exponents that are non-negative.
   * <p>
   * The complexity of this implementation is {@code O(log(b))}.
   *
   * @param a The base.
   * @param b The exponent.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static long pow(long a, long b) {
    if (b <= 0)
      throw new IllegalArgumentException("Exponent must be non-negative: " + b);

    long p = 1;
    while (b > 0) {
      if ((b & 1) == 1)
        p *= a;

      b >>= 1;
      a *= a;
    }

    return p;
  }

  private FastMath() {
  }
}