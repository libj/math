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

import static gnu.java.math.BigIntValue.*;

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

  static boolean mul0(long v1, short s1, long v2, short s2, final byte valueBits, final Decimal result) {
    if (v1 == 0 || v2 == 0) {
      result.set(ZERO);
      return true;
    }

    final byte scaleBits = valueBits(valueBits);
    final short minScale = FixedPoint.minScale[scaleBits];
    final short maxScale = FixedPoint.maxScale[scaleBits];
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);

    // First check that we can do the simplest possible multiplication, without any adjustments
    long v = multiplyNonZero(v1, v2, minValue, maxValue);
    final byte signum;
    if (v != 0) {
      signum = 1;
    }
    else {
      signum = (byte)(v1 < 0 == v2 < 0 ? 1 : -1);
      // Have to make adjustments, so let's try to reduce the error as much as possible
      v1 = Math.abs(v1);
      v2 = Math.abs(v2);

      // If v1 has trailing zeroes, remove them first.
      short z1 = Numbers.trailingZeroes(v1);
      if (z1 > 0) {
        // Make sure we don't go below (minScale)
        if (s1 < 0) {
          // FIXME: Create SafeMath method for min(short,short)
          z1 = SafeMath.min(z1, (short)(s1 - minScale));
        }

        v1 /= FastMath.e10[z1];
        s1 -= z1;
      }

      // If v2 has trailing zeroes, remove them first.
      short z2 = Numbers.trailingZeroes(v2);
      if (z2 > 0) {
        // Make sure we don't go below minScale
        if (s2 < 0) {
          // FIXME: Create SafeMath method for min(short,short)
          z2 = SafeMath.min(z2, (short)(s2 - minScale));
        }

        v2 /= FastMath.e10[z2];
        s2 -= z2;
      }

      // Try again if trailing spaces have been removed
      v = z1 == 0 && z2 == 0 ? 0 : multiplyNonZero(v1, v2, minValue, maxValue);

      // Scale down v1 and v2 so the product will fit within (valueBits).
      if (v == 0) {
        // Get the number of bits required to represent v1.
        byte bp1 = binaryPrecisionRequiredForValue(v1);

        // Get the number of bits required to represent v2.
        byte bp2 = binaryPrecisionRequiredForValue(v2);

        // How many bits are available until overflow (valueBits)?
        // It is possible for (bp1 + bp2 - valueBits) to be 65,
        // if v1 and v2 are both Long.MIN_VALUE.
        byte bp = SafeMath.min(bp1 + bp2 - valueBits, MAX_PRECISION_B);
        if (bp <= 0)
          throw new IllegalStateException();

        // How many decimal places are available until overflow (maxValue)?
        final byte dp = Numbers.precision((1L << bp) - 1);

        // The scale of the result will have to be reduced by the number of
        // decimals just before we overflow. It doesn't matter whether s1 or s2
        // is reduced here, as it's added to s = s1 + s2 later.
        s1 -= dp;

        // The following is a "128-bit long division" approach to multiply v1 *
        // v2 and de-scale the result to fit within valueBits. Though it
        // produces the lowest error, it is also expensive. The alternative
        // "64-bit mod split" approach fits within in the same error profile,
        // but is much less expensive.

        if (highPrecision) {
          // Approach 1: "128-bit long division"
          // v (128-bit) = v1 * v2 => v (128-bit) / 10e(dp) => v (64-bit)
          final long h = multiplyHigh(v1, v2);
          final long l = v1 * v2;
          v = div128e10(h, l, dp, result.buf);
        }
        else {
          // Approach 2: "64-bit mod split"
          // v = v1 * v2 = (v1h + v1l) * (v2h + v2l)

          // We need to reduce the sizes of v1 and v2 by bp so that the product
          // doesn't overflow. First allocate as much of bp as we can to bring bp1
          // and bp2 closer to each other. This ensures that the least significant
          // digits are de-scaled first.
          final boolean bias = bp1 > bp2;
          if (bias) {
            bp1 = SafeMath.min(bp1 - bp2, bp);
            bp2 = 0;
            bp -= bp1;
          }
          else if (bp1 < bp2) {
            bp2 = SafeMath.min(bp2 - bp1, bp);
            bp1 = 0;
            bp -= bp2;
          }
          else {
            bp1 = 0;
            bp2 = 0;
          }

          // If bp is not zero, split the difference of bp between v1 and v2,
          // reducing the larger of v1 and v2 if bp is odd.
          if (bp > 0) {
            final byte half = (byte)(bp / 2);
            if (bias) {
              bp1 += (byte)(bp - half);
              bp2 += half;
            }
            else {
              bp1 += half;
              bp2 += (byte)(bp - half);
            }
          }

          // Switch from binary to decimal to calculate the de-scaling factors.
          final int dp1, dp2;
          if (bias) {
            dp2 = Numbers.precision((1L << bp2) - 1);
            dp1 = dp - dp2;
          }
          else {
            dp1 = Numbers.precision((1L << bp1) - 1);
            dp2 = dp - dp1;
          }

          long hv1 = 0;
          long lv1 = 0;
          long f1 = 0;
          long hv2 = 0;
          long lv2 = 0;
          long f2 = 0;
          if (dp1 > 0) {
            f1 = FastMath.e10[dp1];
            lv1 = v1 % 1000000000;
            hv1 = (v1 - lv1) / f1;
            // FIXME: How do we do rounding here?
            if (dp2 == 0)
              v = (hv1 * v2) + (lv1 * v2) / f1;
          }

          if (dp2 > 0) {
            f2 = FastMath.e10[dp2];
            lv2 = v2 % 1000000000;
            hv2 = (v2 - lv2) / f2;
            // FIXME: How do we do rounding here?
            if (dp1 == 0)
              v = (hv2 * v1) + (lv2 * v1) / f2;
          }

          // FIXME: How do we do rounding here?
          if (dp1 > 0 && dp2 > 0) {
            v = (hv1 * hv2) + ((hv1 * lv2) / f2) + ((hv2 * lv1) / f1) + ((lv1 * lv2) / (f1 * f2));
          }
        }
      }
    }

    int s = s1 + s2;

    // Make sure we don't overflow the scale bits.
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.precision(v)) {
        // FIXME: Should create a pattern to set a value and error message together
        result.set(v, (short)s);
        return false;
      }

      s -= adj;
      --adj; // Leave one factor for rounding
      v /= FastMath.e10[adj];
      v = roundDown10(v);
      if (v == 0) {
        result.set(v, (short)s);
        return false;
      }
    }
    else if (s < minScale) {
      int ds = minScale - s;
      // Get the number of bits unused in v1 for expansion
      // Need the abs() cause of multiplyNonZero(v1, v2, minValue, maxValue) path above
      byte bp = (byte)(valueBits - binaryPrecisionRequiredForValue(Math.abs(v)));
      // This can happen if multiplyNonZero(v1, v2, minValue, maxValue) returns
      // a non-zero value, leading to it having to be reduced later (here).
      if (bp < 0)
        bp = 0;

      // Change that to available decimal places (use floor, because we're calculating availability)
      // FIXME: Floor or ceil?!?!
      final byte dp = bp == 0 ? 0 : (byte)SafeMath.ceil(SafeMath.log10((1L << bp) - 1));
      if (ds > dp) {
        // FIXME: Should create a pattern to set a value and error message together
        result.set(v, (short)s);
        return false;
      }

      v *= FastMath.e10[ds];
      s += ds;

      if (v < 0 ? v < minValue : maxValue < v) {
        // FIXME: Should create a pattern to set a value and error message together
        result.set(v, (short)s);
        return false;
      }
    }

    result.set(signum * v, (short)s);
    return true;
  }
}