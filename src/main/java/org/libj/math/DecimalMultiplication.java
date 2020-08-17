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

import org.libj.lang.Numbers;

abstract class DecimalMultiplication extends FixedPoint {
  private static final long serialVersionUID = -6383926671393192287L;

  private static boolean checkValue(final long value, final boolean isPositive, final long minValue, final long maxValue) {
    return isPositive ? value <= maxValue : minValue <= value;
  }

  /**
   * Returns the product of {@code v1 * v2}. If the result overflows
   * {@code long}, or is outside the range of {@code minValue} and
   * {@code maxValue}, this method returns {@code 0}.
   *
   * @param v1 The multiplier.
   * @param v2 The multiplicand.
   * @param minValue The minimum value lower than which is considered an
   *          underflow.
   * @param maxValue The maximum value higher than which is considered an
   *          overflow.
   * @return the product of {@code v1 * v2}. If the result overflows
   *         {@code long}, or is outside the range of {@code minValue} and
   *         {@code maxValue}, this method returns {@code 0}.
   */
  static long multiplyNonZero(final long v1, final long v2, final long minValue, final long maxValue) {
    final long product = v1 * v2;
    final long ax = Math.abs(v1);
    final long ay = Math.abs(v2);
    if (((ax | ay) >>> 31 == 0 || v2 == 0 || product / v2 == v1) && checkValue(product, v1 < 0 == v2 < 0, minValue, maxValue))
      return product;

    return 0;
  }

  /**
   * Returns as a {@code long} the most significant 64 bits of the 128-bit
   * product of two 64-bit factors.
   *
   * @param x The first value.
   * @param y The second value.
   * @return the result.
   */
  static long multiplyHigh(final long x, final long y) {
    final long x2 = x & 0xFFFFFFFFL;
    final long y2 = y & 0xFFFFFFFFL;
    if (x < 0 || y < 0) {
      // Use technique from section 8-2 of Henry S. Warren, Jr.,
      // Hacker's Delight (2nd ed.) (Addison Wesley, 2013), 173-174.
      final long x1 = x >> 32;
      final long y1 = y >> 32;
      final long z2 = x2 * y2;
      final long t = x1 * y2 + (z2 >>> 32);
      final long z0 = t >> 32;
      final long z1 = (t & 0xFFFFFFFFL) + x2 * y1;
      return x1 * y1 + z0 + (z1 >> 32);
    }

    // Use Karatsuba technique with two base 2^32 digits.
    final long x1 = x >>> 32;
    final long y1 = y >>> 32;
    final long a = x1 * y1;
    final long b = x2 * y2;
    final long c = (x1 + x2) * (y1 + y2);
    final long k = c - a - b;
    return (((b >>> 32) + k) >>> 32) + a;
  }

  /**
   * Returns the result of the division of a 128-bit number represented by
   * {@code high} and {@code low} by {@code 10^factor}.
   *
   * @param high The high 64-bits of the 128-bit value.
   * @param low The low 64-bits of the 128-bit value.
   * @param factor The decimal factor to be converted to:
   *          {@code divisor = 10^factor}.
   * @param buf A buffer to use for division ({@code long[2]}).
   * @return The result of the division of a 128-bit number represented by
   *         {@code high} and {@code low} by {@code 10^factor}.
   */
  static long div128e10(long high, long low, int factor, final long[] buf) {
    long a, r = 0;
    long v0, v1, v2, v3;
    int d, f;
    do {
      f = Math.min(factor, 9);
      d = (int)FastMath.e10[f];
      factor -= f;

      a = (high >> 32) & LONG_INT_MASK;
      FastMath.divideUnsigned(a, d, buf);
      v0 = buf[0];
      r = buf[1];
      r <<= 32;

      a = high & LONG_INT_MASK;
      a |= r;
      FastMath.divideUnsigned(a, d, buf);
      v1 = buf[0];
      r |= buf[1];
      r <<= 32;

      a = (low >> 32) & LONG_INT_MASK;
      a |= r;
      FastMath.divideUnsigned(a, d, buf);
      v2 = buf[0];
      r |= buf[1];
      r <<= 32;

      a = low & LONG_INT_MASK;
      a |= r;
      FastMath.divideUnsigned(a, d, buf);
      v3 = buf[0];

      low = (v2 << 32) | v3;
      if (factor == 0)
        return low;

      high = (v0 << 32) | v1;
    }
    while (true);
  }

  static boolean mul0(long v1, short s1, long v2, short s2, final long minValue, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    // If v1 has trailing zeroes, remove them first.
    short z1 = Numbers.trailingZeroes(v1);
    if (z1 > 0) {
      // Make sure we don't go below (minScale) // FIXME: Do we need this?
      if (s1 < 0)
        z1 = SafeMath.min(z1, (short)(s1 - minScale));

      v1 /= FastMath.e10[z1];
      s1 -= z1;
    }

    // If v2 has trailing zeroes, remove them first.
    short z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      // Make sure we don't go below (minScale) // FIXME: Do we need this?
      if (s2 < 0)
        z2 = SafeMath.min(z2, (short)(s2 - minScale));

      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    final byte sig = (byte)(v1 < 0 == v2 < 0 ? 1 : -1);
    int s = s1 + s2;

    // Check if we can do simple multiplication
    long v = multiplyNonZero(v1, v2, minValue, maxValue);
    if (v == 0) {
      int[] val = BigInt.valueOf(v1);
      val = BigInt.mul(val, v2);
      final int[] factor = val.clone();
      BigInt.div(factor, sig < 0 ? minValue : maxValue);
      final long f = BigInt.longValue(factor);
      if (f == 0) {
        v = BigInt.longValue(val);
      }
      else {
        final byte p = Numbers.precision(f);
        if (p >= FastMath.e10.length) {
          result.error("Overflow", v, (short)s);
          return false;
        }

        final long e10 = FastMath.e10[p];
        final long rem = BigInt.divRem(val, e10);
        v = BigInt.longValue(val);
        if (rem != 0) {
          final byte rp = Numbers.precision(rem);
          final byte r = (byte)(rp < p ? 0 : rp == 1 ? rem : rem / FastMath.e10[rp - 1]);
          v = roundHalfUp(r, v);
        }

        s -= p;
      }
    }

    return checkScale(v, s, minValue, maxValue, minScale, maxScale, result);
  }
}