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
  private static long mulNonZero(final long v1, final long v2, final long minValue, final long maxValue) {
    final long product = v1 * v2;
    final long av1 = Math.abs(v1);
    final long av2 = Math.abs(v2);
    if (((av1 | av2) >>> 31 == 0 || v2 == 0 || product / v2 == v1) && (v1 < 0 == v2 < 0 ? product <= maxValue : minValue <= product))
      return product;

    return 0;
  }

  static boolean mul0(long v1, short s1, long v2, short s2, final long minValue, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    // If v1 has trailing zeroes, remove them first.
    final byte z1 = Numbers.trailingZeroes(v1);
    if (z1 > 0) {
      v1 /= FastMath.e10[z1];
      s1 -= z1;
    }

    // If v2 has trailing zeroes, remove them first.
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    int s = s1 + s2;

    // Check if we can do simple multiplication
    long v = mulNonZero(v1, v2, minValue, maxValue);
    if (v == 0) {
      int[] val = BigInt.valueOf(v1);
      val = BigInt.mul(val, v2);
      final long dp = BigInt.longValue(BigInt.div(val.clone(), maxValue));
      if (dp == 0) {
        v = BigInt.longValue(val);
      }
      else {
        final byte ds = Numbers.precision(dp);
        if (ds >= FastMath.e10.length) {
          result.error("Overflow", v, (short)s);
          return false;
        }

        final long e10 = FastMath.e10[ds];
        final long rem = BigInt.divRem(val, e10);
        v = BigInt.longValue(val);
        if (rem != 0) {
          final byte rp = Numbers.precision(rem);
          final byte r = (byte)(rp < ds ? 0 : rp == 1 ? rem : rem / FastMath.e10[rp - 1]);
          v = roundHalfUp(r, v);
        }

        s -= ds;
      }
    }

    return checkScale(v, s, maxValue, minScale, maxScale, result);
  }
}