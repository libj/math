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

import gnu.java.math.BigInt;

/**
 * Alternative functions supplementing those in {@link java.lang.Math} that
 * offer higher performance algorithms.
 */
public final class FastMath {
  /**
   * The values of <code>10<sup>n</sup></code> for {@code n} between {@code 0}
   * and {@code 18}.
   */
  public static final long[] e10 = {1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

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
   * Divides the {@code dividend} (unsigned) by {@code divisor} (unsigned), and
   * sets the quotient in {@code result[0]} and {@code remainder} in
   * {@code result[1]}.
   *
   * @param dividend The dividend (unsigned).
   * @param divisor The divisor (unsigned)
   * @param result A {@code long} array of length 2 to receive the result.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  public static void divideUnsigned(final long dividend, final long divisor, final long[] result) {
    if (dividend == 0) {
      result[0] = 0;
      result[1] = 0;
      return;
    }

    if (divisor < 0 && Long.compareUnsigned(dividend, divisor) < 0) {
      result[0] = 0;
      result[1] = dividend;
      return;
    }

    if (dividend > 0 && divisor > 0) {
      result[0] = dividend / divisor;
      result[1] = dividend % divisor;
      return;
    }

    final long q = ((dividend >>> 1) / divisor) << 1;
    final long r = dividend - q * divisor;
    result[0] = q + (Long.compareUnsigned(r, divisor) >= 0 ? 1 : 0);
    result[1] = r - (Long.compareUnsigned(r, divisor) >= 0 ? divisor : 0);
  }

  /**
   * Returns {@code dividend} (unsigned) divided by {@code divisor} (unsigned).
   *
   * @param dividend The dividend (unsigned).
   * @param divisor The divisor (unsigned)
   * @return {@code dividend} (unsigned) divided by {@code divisor} (unsigned).
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  public static long divideUnsigned(final long dividend, final long divisor) {
    if (dividend == 0)
      return 0;

    if (divisor < 0)
      return Long.compareUnsigned(dividend, divisor) < 0 ? 0 : 1;

    if (dividend > 0)
      return dividend / divisor;

    final long q = ((dividend >>> 1) / divisor) << 1;
    final long r = dividend - q * divisor;
    return q + (Long.compareUnsigned(r, divisor) >= 0 ? 1 : 0);
  }

  /**
   * Returns the remainder of {@code dividend} (unsigned) divided by
   * {@code divisor} (unsigned).
   *
   * @param dividend The dividend (unsigned).
   * @param divisor The divisor (unsigned)
   * @return The remainder or {@code dividend} (unsigned) divided by
   *         {@code divisor} (unsigned).
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  public static long remainderUnsigned(final long dividend, final long divisor) {
    if (dividend == 0)
      return 0;

    if (divisor < 0)
      return Long.compareUnsigned(dividend, divisor) < 0 ? dividend : dividend - divisor;

    if (dividend > 0)
      return dividend % divisor;

    final long q = ((dividend >>> 1) / divisor) << 1;
    final long r = dividend - q * divisor;
    return r - (Long.compareUnsigned(r, divisor) >= 0 ? divisor : 0);
  }

  // FIXME: Implement tests and document.
  static void divideUnsigned(final int[] dividend, int len, final long divisor, final long[] result) {
    if (divisor >> 32 == 0) {
      divideUnsigned(dividend, (int)divisor, len, result);
      return;
    }

    result[1] = BigInt.udiv(dividend, len, divisor, divisor >>> 32);
    --len;
    if (len > 1 && dividend[len - 1] == 0)
      --len;

    result[0] = BigInt.longValue(dividend, len);
  }

  // FIXME: Implement tests and document.
  static long divideUnsigned(final int[] dividend, int len, final long divisor) {
    if ((int)(divisor >>> 32) == 0)
      return divideUnsigned(dividend, (int)divisor, len);

    BigInt.udiv(dividend, len, divisor, divisor >>> 32);
    --len;
    if (len > 1 && dividend[len - 1] == 0)
      --len;

    return BigInt.longValue(dividend, len);
  }

  // FIXME: Implement tests and document.
  static long remainderUnsigned(final int[] dividend, final int len, final long divisor) {
    return BigInt.udiv(dividend, len, divisor, divisor >>> 32);
  }

  private FastMath() {
  }
}