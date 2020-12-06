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

import java.math.RoundingMode;

import org.libj.lang.Numbers;

abstract class DecimalDivision extends FixedPoint {
  private static final long serialVersionUID = 2875665225793357664L;
  private static final byte maxE10 = (byte)(FastMath.longE10.length - 1);

  static boolean div0(long significand1, int scale1, long significand2, int scale2, final Decimal result, final RoundingMode rm) {
    if (rm != RoundingMode.HALF_UP)
      throw new IllegalArgumentException("Only RoundingMode.HALF_UP is supported");

    final byte p1 = Numbers.precision(significand1);
    int ds1 = Numbers.precision(MIN_SIGNIFICAND / significand1) - 1;

    // If significand2 has trailing zeroes, remove them first.
    final byte z2 = Numbers.trailingZeroes(significand2);
    if (z2 > 0) {
      significand2 /= FastMath.longE10[z2];
      scale2 -= z2;
    }
    final byte p2 = Numbers.precision(significand2);

    int s;
    long v, r1, r2;
    if (p2 == 1) {
      significand1 *= FastMath.longE10[ds1];
      scale1 += ds1;
      s = scale1 - scale2;

      v = significand1 / significand2;
      r1 = significand1 % significand2;

      if (r1 != 0) {
        r1 *= 10;
        r2 = r1 / significand2;
        if (r2 != 0) {
          final int ds = Numbers.precision(MIN_SIGNIFICAND / v) - 1;
          if (ds > 0) {
            v *= 10;
            v += r2;
            s += 1;
            v = roundHalfUp(((r1 % significand2) * 10) / significand2, v);
          }
          else {
            v = roundHalfUp(r2, v);
          }
        }
      }
    }
    else {
      int p = Numbers.precision(MAX_SIGNIFICAND) + p1 + p2 - 1;
      if (ds1 > p) {
        ds1 = p;
        p = 0;
      }
      else {
        p -= ds1;
      }

      significand1 *= FastMath.longE10[ds1];
      scale1 += ds1;

      final int[] val = BigInt.assignInPlace(Decimal.buf1.get(), significand1);
      if (p > 0) {
        if (p > maxE10)
          p = maxE10;

        BigInt.mulInPlace(val, FastMath.longE10[p]);
        scale1 += p;
      }

      s = scale1 - scale2;

      r1 = BigInt.divRem(val, significand2);
      final int[] val2 = BigInt.copyInPlace(val, Math.abs(val[0]) + 1, Decimal.buf2.get());
      final long dp = BigInt.longValue(BigInt.div(val2, MAX_SIGNIFICAND));
      if (dp == 0) {
        v = BigInt.longValue(val);
        if (r1 != 0) {
          r1 /= significand2 / 100;
          if (r1 != 0) {
            final int ds = Numbers.precision(MIN_SIGNIFICAND / v) - 1;
            if (ds > 0) {
              v *= 10;
              v += r1 / 10;
              s += 1;
              v = roundHalfUp(r1 % 10, v);
            }
            else {
              v = roundHalfUp(r1 / 10, v);
            }
          }
        }
      }
      else {
        final byte ds = Numbers.precision(dp);
        if (ds >= FastMath.longE10.length) {
          result.error("Overflow");
          return false;
        }

        r1 = BigInt.divRem(val, FastMath.longE10[ds]);
        v = BigInt.longValue(val);
        if (r1 != 0) {
          final byte rp = Numbers.precision(r1);
          final long r = rp < ds ? 0 : rp == 1 ? r1 : r1 / FastMath.longE10[rp - 1];
          v = roundHalfUp(r, v);
        }

        s -= ds;
      }
    }

    return checkScale(v, Numbers.precision(v), s, result);
  }

  static boolean rem0(long significand1, int scale1, long significand2, int scale2, final Decimal result) {
    final byte p1 = Numbers.precision(significand1);
    final byte p2 = Numbers.precision(significand2);
    if (p2 - scale2 > p1 - scale1) {
      result.assign(significand1, (short)scale1);
      return true;
    }

    if (scale1 < scale2) {
      int ds = scale2 - scale1;
      int ds1 = Numbers.precision(Long.MIN_VALUE / significand1) - 1;
      if (ds < ds1)
        ds1 = ds;

      significand1 *= FastMath.longE10[ds1];
      scale1 += ds1;
      ds -= ds1;

      if (ds > 0) {
        int z2 = Numbers.trailingZeroes(significand2);
        if (z2 != 0) {
          if (ds < z2)
            z2 = ds;

          significand2 /= FastMath.longE10[z2];
          scale2 -= z2;
          ds -= z2;
        }
      }

      if (ds > 0) {
        if (ds > 36) {
          result.assign(0, (short)0);
          return true;
        }

        final int[] val1 = BigInt.assignInPlace(Decimal.buf1.get(), significand1);
        if (ds > 18) {
          BigInt.mulInPlace(val1, FastMath.longE10[18]);
          ds -= 18;
        }

        BigInt.mulInPlace(val1, FastMath.longE10[ds]);
        final long rem = BigInt.rem(val1, significand2);
        result.assign(rem, (short)scale2);

        return true;
      }
    }
    else {
      significand2 *= FastMath.longE10[scale1 - scale2];
    }

    result.assign(significand1 % significand2, (short)scale1);
    return true;
  }
}