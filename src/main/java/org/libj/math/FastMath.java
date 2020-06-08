/* Copyright (c) 2020 LibJ
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
 * Alternative functions supplementing those in {@link java.lang.Math} that
 * offer higher performance algorithms.
 */
public final class FastMath {
  static final long[] e10 = {1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

  /**
   * Returns the base 2 logarithm of positive {@code int} values, and {@code 0}
   * for negative values.
   * <p>
   * This method differentiates itself from {@link java.lang.Math#log(double)}
   * with a high performance algorithm specific to {@code int} arguments.
   *
   * @param n The value.
   * @return The base 2 logarithm of positive {@code int} values, and {@code 0}
   *         for negative values.
   */
  public static byte log2(final int n) {
    return (byte)(n <= 0 ? 0 : 31 - Integer.numberOfLeadingZeros(n - 1));
  }

  /**
   * Returns the base 2 logarithm of positive {@code long} values, and {@code 0}
   * for negative values.
   * <p>
   * This method differentiates itself from {@link java.lang.Math#log(double)}
   * with a high performance algorithm specific to {@code long} arguments.
   *
   * @param n The value.
   * @return The base 2 logarithm of positive {@code long} values, and {@code 0}
   *         for negative values.
   */
  public static byte log2(final long n) {
    return (byte)(n <= 0 ? 0 : 63 - Long.numberOfLeadingZeros(n - 1));
  }

  /**
   * Returns the value of <code>1x10<sup>n</sup></code> for {@code n} between
   * {@code 0} and {@code 18}.
   *
   * @param n The value.
   * @return The value of <code>1x10<sup>n</sup></code> for {@code n} between
   *         {@code 0} and {@code 18}.
   * @throws ArrayIndexOutOfBoundsException If {@code n} is smaller than
   *           {@code 0} or greater than {@code 18}.
   */
  public static long e10(final byte n) {
    return e10[n];
  }

  private FastMath() {
  }
}