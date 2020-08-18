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

abstract class DecimalDivision extends FixedPoint {
  private static final long serialVersionUID = 2875665225793357664L;
  private static final byte maxE10 = (byte)(FastMath.e10.length - 1);

  static boolean div0(long v1, int s1, long v2, int s2, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    final byte p1 = Numbers.precision(v1);
    byte ds1 = (byte)(Numbers.precision(maxValue / v1) - 1);

    // If v2 has trailing zeroes, remove them first.
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }
    final byte p2 = Numbers.precision(v2);

    int s;
    long v, r1, r2;
    if (p2 <= 1) {
      v1 *= FastMath.e10[ds1];
      s1 += ds1;
      s = s1 - s2;

      v = v1 / v2;
      r1 = v1 % v2;
      if (r1 != 0) {
        r1 *= 10;
        r2 = r1 / v2;
        if (r2 != 0) {
          final int ds = Numbers.precision(maxValue / v) - 1;
          if (ds > 0) {
            v *= 10;
            v += r2;
            s += 1;
            v = roundHalfUp((byte)(((r1 % v2) * 10) / v2), v);
          }
          else {
            v = roundHalfUp((byte)r2, v);
          }
        }
      }
    }
    else {
      byte p = (byte)(Numbers.precision(maxValue) + p1 + p2 -1);
      if (ds1 > p) {
        ds1 = p;
        p = 0;
      }
      else {
        p -= ds1;
      }

      v1 *= FastMath.e10[ds1];
      s1 += ds1;

      int[] val = BigInt.valueOf(v1);
      if (p > 0) {
        if (p > maxE10)
          p = maxE10;

        val = BigInt.mul(val, FastMath.e10[p]);
        s1 += p;
      }

      s = s1 - s2;

      r1 = BigInt.divRem(val, v2);

      final long dp = BigInt.longValue(BigInt.div(val.clone(), maxValue));
      if (dp == 0) {
        v = BigInt.longValue(val);
        if (r1 != 0) {
          r1 /= v2 / 100;
          if (r1 != 0) {
            final int ds = Numbers.precision(maxValue / v) - 1;
            if (ds > 0) {
              v *= 10;
              v += r1 / 10;
              s += 1;
              v = roundHalfUp((byte)(r1 % 10), v);
            }
            else {
              v = roundHalfUp((byte)(r1 / 10), v);
            }
          }
        }
      }
      else {
        final byte ds = Numbers.precision(dp);
        if (ds >= FastMath.e10.length) {
          result.error("Overflow", v1, (short)s);
          return false;
        }

        r1 = BigInt.divRem(val, FastMath.e10[ds]);
        v = BigInt.longValue(val);
        if (r1 != 0) {
          final byte rp = Numbers.precision(r1);
          final byte r = (byte)(rp < ds ? 0 : rp == 1 ? r1 : r1 / FastMath.e10[rp - 1]);
          v = roundHalfUp(r, v);
        }

        s -= ds;
      }
    }

    return checkScale(v, s, maxValue, minScale, maxScale, result);
  }
}