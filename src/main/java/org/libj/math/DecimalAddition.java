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
  static Decimal add0(final Decimal dec, final long significand2, final short scale2) {
    if (significand2 == 0)
      return dec;

    if (dec.significand == 0)
      return dec.assign(significand2, scale2);

    if (add0(dec.significand, dec.scale, significand2, scale2, false, dec))
      return dec;

    // Special situation where negation may have caused an overflow,
    // but the result is clearly zero
    if (Decimal.compare(dec.significand, dec.scale, -significand2, scale2) == 0)
      return dec.assign(0, (short)0);

    return null;
  };

  static long add0(long dec1, long dec2, final long defaultValue) {
    long significand1 = significand(dec1);
    if (significand1 == 0)
      return dec2;

    final long significand2 = significand(dec2);
    if (significand2 == 0)
      return dec1;

    final short scale1 = scale(dec1);
    final short scale2 = scale(dec2);
    final Decimal result = Decimal.threadLocal.get();
    if (add0(significand1, scale1, significand2, scale2, false, result))
      return result.encode(defaultValue);

    return defaultValue;
  }

  static boolean add0(long significand1, short scale1, long significand2, short scale2, final boolean negate, final Decimal result) {
    long v;
    short s;
    long r = 0;
    if (scale1 == scale2) {
      v = significand1 + significand2;
      s = scale1;
    }
    else {
      // If significand1 has trailing zeroes, remove them first
      byte z1 = Numbers.trailingZeroes(significand1);
      if (z1 > 0) {
        significand1 /= FastMath.longE10[z1];
        scale1 -= z1;
      }

      // If significand2 has trailing zeroes, remove them first
      byte z2 = Numbers.trailingZeroes(significand2);
      if (z2 > 0) {
        significand2 /= FastMath.longE10[z2];
        scale2 -= z2;
      }

      // Make the first argument the larger one
      if (scale1 > scale2) {
        z1 ^= z2;
        z2 ^= z1;
        z1 ^= z2;

        significand1 ^= significand2;
        significand2 ^= significand1;
        significand1 ^= significand2;

        scale1 ^= scale2;
        scale2 ^= scale1;
        scale1 ^= scale2;
      }

      final int p1 = Numbers.precision(significand1);
      final int p2 = Numbers.precision(significand2);
      if ((p1 - scale1) - (p2 - scale2) > Numbers.precision(MAX_SIGNIFICAND)) {
        if (z1 > 0) {
          significand1 *= FastMath.longE10[z1];
          scale1 += z1;
        }

        result.assign(significand1, scale1);
        return true;
      }

      v = significand1;
      s = scale1;

      // Let's try to match the scales by up-scaling significand1 so we don't lose
      // precision, then if the scales still don't match due to the limit of
      // significand1's scale, down-scale significand2 and do rounding (losing precision,
      // but it's ok cause that precision is insignificant).

      // How many multiples of 10 until overflow?
      final int dp1 = Numbers.precision(MIN_SIGNIFICAND / significand1) - 1;

      // ds is always positive, and greater than 0
      int ds = scale2 - scale1;

      // What is the most we can up-scale significand1?
      int ds1 = Math.min(dp1, ds);

      // Don't go past MAX_SCALE when adjusting significand1 and scale1
      // ds1 = Math.min(ds1, MAX_PSCALE - (scale1 - p1)); // FIXME: Is the `p1` here correct?

      ds -= ds1;
      scale1 += ds1;

      long r0 = 0;
      int ds2 = 0;

      // What is the most we can down-scale significand2?
      if (ds != 0) {
        // How many decimal places can we reduce precision until it's
        // insignificant? Here we actually allow significand2 to be reduced all
        // the way to 0, since ds2 accounts for all of the digits in significand2.
        // If it so happens that significand2 is reduced to the full size of ds2,
        // then we still have the opportunity to check the prior-to-last digit (right
        // before significand2 becomes 0) if it would round up or down. If it rounds
        // up, then we add 1, otherwise the result is insignificant.
        ds2 = p2;

        // Take the lesser of ds2 and ds
        ds2 = Math.min(ds2, ds);

        // Don't go past MIN_SCALE when adjusting significand2 and scale2
        // ds2 = Math.min(ds2, (scale2 - p2) - MIN_PSCALE); // FIXME: Is the `p2` here correct?
        ds -= ds2;
        scale2 -= ds2;
      }

      // If ds accounts for the gap in scales, then let's perform the adjustments
      if (ds == 0) {
        if (ds1 > 0) {
          // Make the lossless adjustment to significand1
          significand1 *= FastMath.longE10[ds1];
        }

        if (ds2 > 0) {
          final int[] val1 = BigInt.assignInPlace(Decimal.buf1.get(), significand1);
          int len = val1[0];
          final int sig;
          if (len < 0) { len = -len; sig = -1; } else sig = 1;

          long f = FastMath.longE10[ds2];
          if (ds2 < 10)
            len = BigInt.umul0(val1, BigInt.OFF, len, (int)f);
          else
            len = BigInt.umul0(val1, BigInt.OFF, len, f & BigInt.LONG_MASK, f >>> 32);

          val1[0] = len * sig;
          BigInt.addInPlace(val1, significand2);

          if (f <= 100 && BigInt.equals(BigInt.assignInPlace(Decimal.buf2.get(), v = BigInt.longValue(val1)), val1)) {
            scale1 += f == 100 ? 2 : 1;
            final long p = v / MIN_SIGNIFICAND;
            if (p != 0 && (ds = Numbers.precision(p)) > 0) {
              scale1 -= ds;
              if (--ds > 0) {
                f = FastMath.longE10[ds];
                r0 = v % f;
                v /= f;
              }

              if (v != (v < 0 ? MIN_SIGNIFICAND : MAX_SIGNIFICAND) || ds == 0 || roundHalfUp(ds == 1 ? r0 : r0 / FastMath.longE10[ds - 1]) != 0)
                v = roundHalfUp10(v);
              else
                ++scale1;
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
              --scale1;
            }
            while (true);

            if (!looped && Numbers.precision(MIN_SIGNIFICAND / v) == 2) { // We can get 1 more digit of precision
              f /= 10;
              if (f > 1)
                r1 /= f;

              v = (v - r) * 10 + r0 + roundHalfUp(r1 % 10);
              ++scale1;
            }
          }

          s = scale1;
        }
        else {
          v = significand1 + significand2;
          s = scale1;
          if (scale1 == scale2) {
            final int sig;
            if (significand1 == Long.MIN_VALUE) {
              sig = -1;
            }
            else if (significand1 == Long.MAX_VALUE) {
              sig = significand2 == Long.MIN_VALUE ? -1 : significand2 == Long.MIN_VALUE + 1 ? 0 : 1;
            }
            else if (significand1 < 0) {
              final long as1 = Math.abs(significand1);
              sig = as1 > significand2 ? -1 : as1 == significand2 ? 0 : 1;
            }
            else if (significand2 < 0) {
              final long as2 = Math.abs(significand2);
              sig = as2 > significand1 ? -1 : as2 == significand1 ? 0 : 1;
            }
            else {
              sig = 1;
            }

            if (v < 0 ? sig != -1 : v == 0 ? sig != 0 : sig != 1) {
              // overflow can only be off by a factor of 10,
              // since this is addition/subtraction
              if (--s < MIN_PSCALE) {
                result.error("Overflow");
                return false;
              }

              significand1 = roundHalfUp10(significand1);
              significand2 = roundHalfUp10(significand2);
              result.assign(significand1 + significand2, s);
              return true;
            }
          }
        }
      }
    }

    byte p = Numbers.precision(v);
    if (v < MIN_SIGNIFICAND || MAX_SIGNIFICAND < v) {
      // overflow can only be off by a factor of 10, since this is
      // addition/subtraction
      if (--s - --p < MIN_PSCALE) {
        result.error("Overflow");
        return false;
      }

      v = roundHalfUp10(v - r);
    }

    if (negate) {
      if (v == MIN_SIGNIFICAND) {
        if (--s - --p < MIN_PSCALE) {
          result.error("Overflow");
          return false;
        }

        v = roundHalfUp10(v - r);
      }

      v = -v;
    }

    return checkScale(v, p, s, result);
  }
}