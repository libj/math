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

import org.huldra.math.BigInt;
import org.libj.lang.Numbers;

abstract class DecimalDivision extends FixedPoint {
  private static final long serialVersionUID = 2875665225793357664L;
  static final long DIVISOR_MAX = Long.MAX_VALUE / 10;

  /**
   * Returns the result of <code>v1 * 10<sup>dp</sup> / v2</code>.
   *
   * @param v1 The dividend (unsigned).
   * @param v2 The divisor (unsigned).
   * @param dp The decimal precision "factor" by which to scale {@code v1}.
   * @param q An array for the quotient ({@code int[4]}).
   * @param buf An array for the rounding buffer ({@code long[2]}).
   * @return The result of <code>v1 * 10<sup>dp</sup> / v2</code>.
   */
  static long scaleDiv(long v1, final long v2, byte dp, int[] q, final long[] buf) {
    final long f = FastMath.e10[dp];

    BigInt.assign(q, 1, v1);
    if (BigInt.mul(q, f) != q)
      throw new IllegalStateException("q is not big enough");

    long remainder = BigInt.divRem(q, v2);

    // Put the result in v1
    v1 = BigInt.longValue(q, 1, 3);

    // If v1 is bigger than the signed limit, scale it down
    if (v1 < 0) {
      FastMath.divideUnsigned(v1, 10, buf);
      v1 = FixedPoint.round((byte)buf[1], buf[0]);
    }
    else {
      remainder *= 10;
      final byte round = (byte)FastMath.divideUnsigned(remainder, v2);
      v1 = FixedPoint.round(round, v1);
    }

    return v1;
  }

  static boolean div0(long v1, short s1, long v2, short s2, final byte valueBits, final Decimal result) {
    final byte scaleBits = valueBits(valueBits);
    final short minScale = FixedPoint.minScale[scaleBits];
    final short maxScale = FixedPoint.maxScale[scaleBits];
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);

    final byte sig = (byte)(v1 < 0 == v2 < 0 ? 1 : -1);
    v1 = Math.abs(v1);
    v2 = Math.abs(v2);

    // How many bits are available until overflow long?
    final byte bp1 = (byte)(Long.numberOfLeadingZeros(v1));

    // How many decimal places are available until overflow long?
    byte dp1 = (byte)SafeMath.floor(SafeMath.log10((1L << bp1) - 1));

    // Expand the value to max precision available,
    // allowing it to use the sign bit
    if (dp1 > 0) {
      v1 = v1 * FastMath.e10[dp1];
      s1 += dp1;
    }

    // If v2 has trailing zeroes, remove them first
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    int s = s1 - s2;

    long v;
    if (v2 == 1) {
      // v1 is an unscaled long, so dividing it by 1 will result in a negative
      // number. Therefore, add 9 and divide by 10, which brings v1 back to the
      // signed space, and also rounds.
      v = FastMath.divideUnsigned(v1 + 9, 10);
      --s;
    }
    else {
      // Don't allow v2 to be greater than Long.MAX_VALUE / 10, because the
      // unsigned division and remainder algorithms break down in that range.
      if (v2 > DIVISOR_MAX) {
        v2 /= 10;
        ++s;
      }

      // Get the LHS from the decimal
      v = FastMath.divideUnsigned(v1, v2);

      // Record its precision
      final byte d = Numbers.precision(v);

      // How many bits are available to expand v?
      final byte bp = (byte)(Long.numberOfLeadingZeros(v));
      // How many decimals are available to expand v?
      final byte dp = (byte)(bp == 64 ? 18 : SafeMath.floor(SafeMath.log10((1L << bp) - 1)));

      // Scale v1 by dp and divide by v2.
      v = scaleDiv(v1, v2, dp, result.zds, result.buf);

      // Adjust the scale by the number of decimal points we got from div(...).
      s += (byte)(Numbers.precision(v) - d);
    }

    // v has not overflowed long, but it may have overflowed minValue/maxValue
    // By how many bits have we overflowed?
    final byte bp = (byte)(binaryPrecisionRequiredForValue(v) - valueBits);
    if (bp > 0) {
      byte dp = Numbers.precision((1L << bp) - 1);
      s -= dp;
      --dp; // Leave one factor for rounding
      if (dp > 0)
        v /= FastMath.e10[dp];

      v = roundDown10(v);
    }

    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.precision(v)) {
        result.set(sig * v, (short)s);
        return false;
      }

      s -= adj;
      --adj; // Leave one factor for rounding
      v /= FastMath.e10[adj];
      v = roundDown10(v);
      if (v == 0) {
        result.set(sig * v, (short)s);
        return false;
      }
    }
    else {
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.precision(v)) {
          result.set(sig * v, (short)s);
          return false;
        }

        s += adj;
        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.e10.length - 1);
          v *= FastMath.e10[i];
        }

        if (v < 0 ? v < minValue : maxValue < v) {
          result.set(sig * v, (short)s);
          return false;
        }
      }
    }

    result.set(sig * v, (short)s);
    return true;
  }
}