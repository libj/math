/* Copyright (c) 2020 Seva Safris, LibJ
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

import java.util.Arrays;

/**
 * Alternative functions supplementing those in {@link java.lang.Math} that
 * offer higher performance algorithms.
 */
public final class FastMath {
  /**
   * The values of <code>10<sup>n</sup></code> for {@code n} between {@code 0}
   * and {@code 18}.
   */
  public static final long[] E10 = {1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
  private static volatile double[] doubleE10 = {1};

  /** This value was tuned with {@link FastMathTest#testDoubleE10} */
  @SuppressWarnings("javadoc")
  private static final int MAX_E10_DOUBLE = 1 << 15;

  private static volatile int[][] bigIntE10 = {new int[] {1, 1}};

  static {
    doubleE10(1024);
    E10(16);
  }

  /**
   * Return <code>10<sup>n</sup></code>, as a {@code double}, expanding the
   * underlying {@link #doubleE10} array if necessary.
   *
   * @param n The power of ten to be returned (>= 0).
   * @return A {@linkplain BigInt#val() value-encoded number} with the value
   *         (10<sup>n</sup>).
   * @throws ArrayIndexOutOfBoundsException If {@code n} is negative.
   */
  public static double doubleE10(final int n) {
    if (n >= MAX_E10_DOUBLE)
      return StrictMath.pow(10, n);

    double[] pows = doubleE10;
    if (n < pows.length)
      return pows[n];

    synchronized (doubleE10) {
      int curLen = pows.length;
      // The following comparison and the above synchronized statement is
      // to prevent multiple threads from expanding the same array.
      if (curLen <= n) {
        int newLen;
        if (n < 256) {
          newLen = curLen * 2;
          while (newLen <= n)
            newLen *= 2;
        }
        else {
          newLen = n + 64;
        }

        pows = Arrays.copyOf(pows, newLen);
        for (; curLen < newLen; ++curLen)
          pows[curLen] = StrictMath.pow(10, curLen);

        // Based on the following facts:
        // 1. pows is a private local variable;
        // 2. the following store is a volatile store.
        // Thus the newly created array elements can be safely published.
        doubleE10 = pows;
      }

      return pows[n];
    }
  }

  /**
   * Return <code>10<sup>n</sup></code>, as a {@linkplain BigInt#val()
   * value-encoded number}, expanding the underlying {@link #bigIntE10} array if
   * necessary.
   *
   * @param n The power of ten to be returned (>= 0).
   * @return A {@linkplain BigInt#val() value-encoded number} with the value
   *         (10<sup>n</sup>).
   * @throws ArrayIndexOutOfBoundsException If {@code n} is negative.
   */
  public static int[] E10(final int n) {
    int[][] pows = bigIntE10;
    if (n < pows.length)
      return pows[n];

    synchronized (bigIntE10) {
      int curLen = pows.length;
      // The following comparison and the above synchronized statement is
      // to prevent multiple threads from expanding the same array.
      if (curLen <= n) {
        int newLen;
        if (n < 256) {
          newLen = curLen * 2;
          while (newLen <= n)
            newLen *= 2;
        }
        else {
          newLen = n + 64;
        }

        int[] val;
        pows = Arrays.copyOf(pows, newLen);
        for (int len; curLen < newLen; ++curLen) {
          val = pows[curLen - 1];
          len = val[0];
          val = len + 2 >= val.length ? BigInt.reallocExact(val, len + 1, len + 2) : val.clone();
          val[0] = BigIntMultiplication.umul0(pows[curLen] = val, 1, len, 10);
        }

        // Based on the following facts:
        // 1. pows is a private local variable;
        // 2. the following store is a volatile store.
        // Thus the newly created array elements can be safely published.
        bigIntE10 = pows;
      }

      return pows[n];
    }
  }

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

    final long q = ((dividend >>> 1) / divisor) * 2;
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

    final long q = ((dividend >>> 1) / divisor) * 2;
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

    final long q = ((dividend >>> 1) / divisor) * 2;
    final long r = dividend - q * divisor;
    return r - (Long.compareUnsigned(r, divisor) >= 0 ? divisor : 0);
  }

  private FastMath() {
  }
}