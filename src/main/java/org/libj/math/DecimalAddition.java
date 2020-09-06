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

abstract class DecimalAddition extends FixedPoint {
  private static final long serialVersionUID = 2903111300785124642L;

  static Decimal add0(final Decimal d1, final long v2, final short s2) {
    if (v2 == 0)
      return d1;

    if (d1.value == 0)
      return d1.assign(v2, s2);

    if (add0(d1.value, d1.scale, v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, false, d1))
      return d1;

    // Special situation where negation may have caused an overflow,
    // but the result is clearly zero
    if (Decimal.compare(d1.value, d1.scale, -v2, s2) == 0)
      return d1.assign(0, (short)0);

    return null;
  };

  static long add0(long dec1, long dec2, final long defaultValue, final byte scaleBits) {
    long v1 = value(dec1, scaleBits);
    if (v1 == 0)
      return dec2;

    long v2 = value(dec2, scaleBits);
    if (v2 == 0)
      return dec1;

    final int valueBits = valueBits(scaleBits);
    final long minValue = Decimal.minValue(valueBits);
    final long maxValue = Decimal.maxValue(valueBits);
    final short minScale = minScale(scaleBits);
    final short maxScale = maxScale(scaleBits);
    final short s1 = scale(dec1, scaleBits);
    final short s2 = scale(dec2, scaleBits);
    final Decimal result = threadLocal.get();
    if (add0(v1, s1, v2, s2, minValue, maxValue, minScale, maxScale, false, result))
      return result.encode(scaleBits, defaultValue);

    return defaultValue;
  }

  static boolean add0(long v1, short s1, long v2, short s2, final long minValue, final long maxValue, final short minScale, final short maxScale, final boolean negate, final Decimal result) {
    long v;
    short s;
    long r = 0;
    if (s1 == s2) {
      v = v1 + v2;
      s = s1;
    }
    else {
      long r0 = 0;
      short ds2 = 0;
      // If v1 has trailing zeroes, remove them first.
      byte z1 = Numbers.trailingZeroes(v1);
      if (z1 > 0) {
        v1 /= FastMath.longE10[z1];
        s1 -= z1;
      }

      // If v2 has trailing zeroes, remove them first.
      byte z2 = Numbers.trailingZeroes(v2);
      if (z2 > 0) {
        v2 /= FastMath.longE10[z2];
        s2 -= z2;
      }

      // Make the first argument the larger one
      if (s1 > s2) {
        z1 ^= z2;
        z2 ^= z1;
        z1 ^= z2;

        v1 ^= v2;
        v2 ^= v1;
        v1 ^= v2;

        s1 ^= s2;
        s2 ^= s1;
        s1 ^= s2;
      }

      final int p1 = Numbers.precision(v1) - s1;
      final int p2 = Numbers.precision(v2) - s2;
      if (p1 - p2 > Numbers.precision(maxValue)) {
        if (z1 > 0) {
          v1 *= FastMath.longE10[z1];
          s1 += z1;
        }

        result.assign(v1, s1);
        return true;
      }

      v = v1;
      s = s1;

      // Let's try to match the scales by up-scaling v1 so we don't lose
      // precision, then if the scales still don't match due to the limit of
      // v1's scale, down-scale v2 and do rounding (losing precision, but it's
      // ok cause that precision is insignificant).

      // How many multiples of 10 until overflow?
      final int dp1 = Numbers.precision(minValue / v1) - 1;

      // ds is always positive, and greater than 0
      int ds = s2 - s1;

      // What is the most we can up-scale v1?
      int ds1 = SafeMath.min(dp1, ds);

      // Don't go past maxScale when adjusting v1 and s1
      ds1 = SafeMath.min(ds1, maxScale - s1);

      ds -= ds1;
      s1 += ds1;

      // What is the most we can down-scale v2?
      if (ds != 0) {
        // How many decimal places can we reduce precision until it's
        // insignificant? Here we actually allow v2 to be reduced all the way
        // to 0, since ds2 accounts for all of the digits in v2. If it so
        // happens that v2 is reduced to the full size of ds2, then we still
        // have the opportunity to check the prior-to-last digit (right before
        // v2 becomes 0) if it would round up or down. If it rounds up, then we
        // add 1, otherwise the result is insignificant.
        ds2 = Numbers.precision(v2);

        // Take the lesser of ds2 and ds
        ds2 = SafeMath.min(ds2, ds);

        // Don't go past minScale when adjusting v2 and s2
        ds2 = SafeMath.min(ds2, s2 - minScale);

        ds -= ds2;
        s2 -= ds2;
      }

      // If ds accounts for the gap in scales, then let's perform the adjustments
      if (ds == 0) {
        if (ds1 > 0) {
          // Make the lossless adjustment to v1
          v1 *= FastMath.longE10[ds1];
        }

        if (ds2 > 0) {
          final int[] val1 = BigInt.assign(Decimal.buf1.get(), v1);
          int sig = 1, len = val1[0];
          if (len < 0) { len = -len; sig *= -1; }

          long f = FastMath.longE10[ds2];
          if (ds2 < 10)
            len = BigInt.umul0(val1, BigInt.OFF, len, (int)f);
          else
            len = BigInt.umul0(val1, BigInt.OFF, len, f & BigInt.LONG_MASK, f >>> 32);

          val1[0] = len * sig;
          BigInt.add(val1, v2);

          if (f <= 100 && BigInt.equals(BigInt.assign0(Decimal.buf2.get(), v = BigInt.longValue(val1)), val1)) {
            s1 += f == 100 ? 2 : 1;
            final long p = v / minValue;
            if (p != 0 && (ds = Numbers.precision(p)) > 0) {
              s1 -= ds;
              if (--ds > 0) {
                f = FastMath.longE10[ds];
                r0 = v % f;
                v /= f;
              }

              if (v != (v < 0 ? minValue : maxValue) || ds == 0 || roundHalfUp(ds == 1 ? r0 : r0 / FastMath.longE10[ds - 1]) != 0)
                v = roundHalfUp10(v);
              else
                ++s1;
            }
          }
          else {
            long r1;
            boolean looped = false;
            do {
              r1 = r0 = BigInt.divRem(val1, f);
              f /= 10;
              r0 /= f;
              r = roundHalfUp(r0);
              v = BigInt.longValue(val1) + r;
              if (v < 0 == val1[0] < 0)
                break;

              f = 10;
              looped = true;
              --s1;
            }
            while (true);

            if (!looped && Numbers.precision(minValue / v) == 2) { // We can get 1 more digit of precision
              f /= 10;
              if (f > 1)
                r1 /= f;

              v = (v - r) * 10 + r0 + roundHalfUp(r1 % 10);
              ++s1;
            }
          }

          s = s1;
        }
        else {
          v = v1 + v2;
          s = s1;
          if (s1 == s2) {
            final int sig;
            if (v1 == Long.MIN_VALUE) {
              sig = -1;
            }
            else if (v1 == Long.MAX_VALUE) {
              sig = v2 == Long.MIN_VALUE ? -1 : v2 == Long.MIN_VALUE + 1 ? 0 : 1;
            }
            else if (v1 < 0) {
              final long av1 = Math.abs(v1);
              sig = av1 > v2 ? -1 : av1 == v2 ? 0 : 1;
            }
            else if (v2 < 0) {
              final long av2 = Math.abs(v2);
              sig = av2 > v1 ? -1 : av2 == v1 ? 0 : 1;
            }
            else {
              sig = 1;
            }

            if (v < 0 ? sig != -1 : v == 0 ? sig != 0 : sig != 1) {
              // overflow can only be off by a factor of 10,
              // since this is addition/subtraction
              if (--s < minScale) {
                result.error("Overflow");
                return false;
              }

              v1 = roundHalfUp10(v1);
              v2 = roundHalfUp10(v2);
              result.assign(v1 + v2, s);
              return true;
            }
          }
        }
      }
    }

    if (v < minValue || maxValue < v) {
      // overflow can only be off by a factor of 10, since this is
      // addition/subtraction
      if (--s < minScale) {
        result.error("Overflow");
        return false;
      }

      v = roundHalfUp10(v - r);
    }

    if (negate) {
      if (v == minValue) {
        if (--s < minScale) {
          result.error("Overflow");
          return false;
        }

        v = roundHalfUp10(v - r);
      }

      v = -v;
    }

    return checkScale(v, s, maxValue, minScale, maxScale, result);
  }
}