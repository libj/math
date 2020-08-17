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
  static final long DIVISOR_MAX = Long.MAX_VALUE / 10;
  private static final byte maxE10 = (byte)(FastMath.e10.length - 1);

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
      v1 = FixedPoint.roundHalfUp((byte)buf[1], buf[0]);
    }
    else {
      remainder *= 10;
      final byte round = (byte)FastMath.divideUnsigned(remainder, v2);
      v1 = FixedPoint.roundHalfUp(round, v1);
    }

    return v1;
  }

  static boolean div0(long v1, int s1, long v2, int s2, final long minValue, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    final byte sig = (byte)(v1 < 0 == v2 < 0 ? 1 : -1);

    final byte p1 = Numbers.precision(v1);
    final long f1 = (v1 < 0 ? minValue : maxValue) / v1;
    byte ds1 = (byte)(Numbers.precision(f1) - 1);

    // If v2 has trailing zeroes, remove them first.
    short z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      // Make sure we don't go below (minScale) // FIXME: Do we need this?
      if (s2 < 0)
        z2 = SafeMath.min(z2, (short)(s2 - minScale));

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
        r2 = (r1 * 10) / v2;
        if (r2 != 0) {
          final int pmax = Numbers.precision((v < 0 ? minValue : maxValue) / v) - 1;
          if (pmax > 0) {
            v *= 10;
            v += r2;
            s += 1;
            v = roundHalfUp((byte)((((r1 * 10) % v2) * 10) / v2), v);
          }
          else {
            v = roundHalfUp((byte)r2, v);
          }
        }
      }
    }
    else {
      byte precision = (byte)(Numbers.precision(maxValue) + p1 + p2 -1);
      if (ds1 > precision) {
        ds1 = precision;
        precision = 0;
      }
      else {
        precision -= ds1;
      }

      v1 *= FastMath.e10[ds1];
      s1 += ds1;

      int[] val = BigInt.valueOf(v1);
      if (precision > 0) {
        if (precision > maxE10)
          precision = maxE10;

        val = BigInt.mul(val, FastMath.e10[precision]);
        s1 += precision;
      }

      s = s1 - s2;

      r1 = BigInt.divRem(val, v2);

      final long smax = BigInt.longValue(BigInt.div(val.clone(), sig < 0 ? minValue : maxValue));
      if (smax == 0) {
        v = BigInt.longValue(val);
        if (r1 != 0) {
          r2 = r1 / (v2 / 100);
          if (r2 != 0) {
            final int pmax = Numbers.precision((v < 0 ? minValue : maxValue) / v) - 1;
            if (pmax > 0) {
              v *= 10;
              v += (r2 / 10);
              s += 1;
              v = roundHalfUp((byte)(r2 % 10), v);
            }
            else {
              r2 /= 10;
              v = roundHalfUp((byte)r2, v);
            }
          }
        }
      }
      else {
        final byte pmax = Numbers.precision(smax);
        if (pmax >= FastMath.e10.length) {
          result.error("Overflow", v1, (short)s);
          return false;
        }

        final long e10 = FastMath.e10[pmax];
        r1 = BigInt.divRem(val, e10);
        v = BigInt.longValue(val);
        if (r1 != 0) {
          final byte rp = Numbers.precision(r1);
          final byte r = (byte)(rp < pmax ? 0 : rp == 1 ? r1 : r1 / FastMath.e10[rp - 1]);
          v = roundHalfUp(r, v);
        }

        s -= pmax;
      }
    }

    return checkScale(v, s, minValue, maxValue, minScale, maxScale, result);
  }
}