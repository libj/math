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
  /**
   * Returns the product of {@code v1 * v2}. If the result overflows {@code long}, or is outside the range of {@code MIN_VALUE} and
   * {@code MAX_VALUE}, this method returns {@code 0}.
   *
   * @param v1 The multiplier.
   * @param v2 The multiplicand.
   * @return the product of {@code v1 * v2}. If the result overflows {@code long}, or is outside the range of {@code MIN_VALUE} and
   *         {@code MAX_VALUE}, this method returns {@code 0}.
   */
  private static long mulNonZero(final long v1, final long v2) {
    final long product = v1 * v2;
    final long av1 = Math.abs(v1);
    final long av2 = Math.abs(v2);
    if (((av1 | av2) >>> 31 == 0 || v2 == 0 || product / v2 == v1) && (v1 < 0 == v2 < 0 ? product <= MAX_SIGNIFICAND : MIN_SIGNIFICAND <= product))
      return product;

    return 0;
  }

  static boolean mul0(long significand1, short scale1, long significand2, short scale2, final Decimal result) {
    // If significand1 has trailing zeroes, remove them first.
    final byte z1 = Numbers.trailingZeroes(significand1);
    if (z1 > 0) {
      significand1 /= FastMath.longE10[z1];
      scale1 -= z1;
    }

    // If v2 has trailing zeroes, remove them first.
    final byte z2 = Numbers.trailingZeroes(significand2);
    if (z2 > 0) {
      significand2 /= FastMath.longE10[z2];
      scale2 -= z2;
    }

    int s = scale1 + scale2;

    // Check if we can do simple multiplication
    long v = mulNonZero(significand1, significand2);
    if (v == 0) {
      final int[] val = BigInt.assignInPlace(Decimal.buf1.get(), significand1);
      BigInt.mulInPlace(val, significand2);
      final int[] val2 = BigInt.copyInPlace(val, Math.abs(val[0]) + 1, Decimal.buf2.get());
      final long dp = BigInt.longValue(BigInt.div(val2, MAX_SIGNIFICAND));
      if (dp == 0) {
        v = BigInt.longValue(val);
      }
      else {
        final byte ds = Numbers.precision(dp);
        if (ds >= FastMath.longE10.length) {
          result.error("Overflow");
          return false;
        }

        final long rem = BigInt.divRem(val, FastMath.longE10[ds]);
        v = BigInt.longValue(val);
        if (rem != 0) {
          final byte rp = Numbers.precision(rem);
          final long r = rp < ds ? 0 : rp == 1 ? rem : rem / FastMath.longE10[rp - 1];
          v = roundHalfUp(r, v);
        }

        s -= ds;
      }
    }

    return checkScale(v, Numbers.precision(v), s, result);
  }
}