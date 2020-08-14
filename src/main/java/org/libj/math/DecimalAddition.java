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
      return d1.set(v2, s2);

    if (add0(d1.value, d1.scale, v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, false, d1))
      return d1;

    // Special situation where negation may have caused an overflow, but the result is clearly zero
    if (Decimal.compare(d1.value, d1.scale, -v2, s2) == 0)
      return ZERO;

    return null;
  };

  static long add0(long d1, long d2, final byte scaleBits, final long defaultValue) {
    long v1 = decodeValue(d1, scaleBits);
    if (v1 == 0)
      return d2;

    long v2 = decodeValue(d2, scaleBits);
    if (v2 == 0)
      return d1;

    final byte valueBits = valueBits(scaleBits);
    final long minValue = Decimal.minValue(valueBits);
    final long maxValue = Decimal.maxValue(valueBits);
    final short minScale = minScale(scaleBits);
    final short maxScale = maxScale(scaleBits);
    final short s1 = decodeScale(d1, scaleBits);
    final short s2 = decodeScale(d2, scaleBits);
    final Decimal result = threadLocal.get();
    if (add0(v1, s1, v2, s2, minValue, maxValue, minScale, maxScale, false, result))
      return result.encode(scaleBits, defaultValue);

    return defaultValue;
  }

  static boolean add0(long v1, short s1, long v2, short s2, final long minValue, final long maxValue, final short minScale, final short maxScale, final boolean negate, final Decimal result) {
    long v;
    short s;
    byte r = 0;
    short ds2 = 0;
    if (s1 == s2) {
      v = v1 + v2;
      s = s1;
    }
    else {
      // Make the first argument the larger one
      if (s1 > s2) {
        v1 ^= v2;
        v2 ^= v1;
        v1 ^= v2;

        s1 ^= s2;
        s2 ^= s1;
        s1 ^= s2;
      }

//      final int p1 = Numbers.precision(v1) - s1;
//      final int p2 = Numbers.precision(v2) - s2;
//      if (Math.abs(p1 - p2) > Numbers.precision(maxValue)) {
//        result.set(v1, s1);
//        return true;
//      }

      v = v1;
      s = s1;

      // Let's try to match the scales by up-scaling v1 so we don't lose
      // precision, then if the scales still don't match due to the limit of
      // v1's scale, down-scale v2 and do rounding (losing precision, but it's
      // ok cause that precision is insignificant).

      // Calculate the "overflow factor" -- factor multiple past which there will be an overflow
//      final long min = minValue == Long.MIN_VALUE ? Long.MIN_VALUE : minValue * 10;
//      final long max = maxValue == Long.MAX_VALUE ? Long.MAX_VALUE : maxValue * 10;
      long of = (v1 < 0 ? minValue : maxValue) / v1;

      // How many multiples of 10 until overflow?
      final byte dp1 = (byte)Math.max(Numbers.precision(of) - 1, 0);

      // ds is always positive, and greater than 0
      int ds = s2 - s1;

      // What is the most we can up-scale v1 (lossless)?
      short ds1 = SafeMath.min(dp1, ds);

      // Don't go past maxScale when adjusting v1 and s1
      ds1 = SafeMath.min(ds1, maxScale - s1);

      ds -= ds1;
      s1 += ds1;

      // What is the most we can down-scale v2 (lossy)?
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
        do {
          if (ds1 > 0) {
            // Make the lossless adjustment to v1
            v1 *= FastMath.e10[ds1];
          }

//          if (Numbers.precision(v2) < ds2)
//            break;

          long rf = 0;
          long r0 = 0;
          final boolean signsEqual = v1 < 0 == v2 < 0;
          if (ds2 > 0) {
            // Make the lossy adjustment to v2
            final short adj = --ds2; // Leave one factor for rounding
            if (adj > 0) {
              // Check if there is a rounding carry
              rf = FastMath.e10[adj];
              if ((r0 = v2 % rf) == 0)
                ds2 = 0;

              v2 /= rf;
            }

            r = (byte)(v2 % 10);
            v2 /= 10;
            v2 = signsEqual || ds2 > 0 ? roundHalfUp(r, v2) : roundHalfDown(r, v2);
            if (v2 == 0)
              break;
          }

          v = v1 + v2;
          s = s1;

          byte ur = r;
          if (r0 != 0) {
            rf /= 10;
            ur = (byte)(r0 / rf);
            ur = (byte)(signsEqual ? roundHalfUp(ur, r) : roundHalfDown(ur, r));
          }

          // If we set a remainder value, then v has been scaled down.
          // See if there's room for it to be scaled back up.
          if (ur != 0) {
            of = (v < 0 ? minValue : maxValue) / v;
            if (of >= 10) {
              // Undo the rounding adjustment
              v = signsEqual || ds2 > 0 ? unroundHalfUp(r, v) : unroundHalfDown(r, v);
              v *= 10;
              v += ur;
              ++s;
            }
          }
        }
        while (false);
      }
    }

    if (s1 == s2) {
      int sig;
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

        v = v1 + v2;
//        if (v < 0 ? sig != -1 : v == 0 ? sig != 0 : sig != 1)
//          throw new IllegalStateException("Should not happen");

        result.set(v, s);
        return true;
      }
    }

    if (negate) {
      if (v == Long.MIN_VALUE) {
        if (--s < minScale) {
          result.error("Overflow");
          return false;
        }

        final boolean signsEqual = v1 < 0 == v2 < 0;
        v = signsEqual || ds2 > 0 ? unroundHalfUp(r, v) : unroundHalfDown(r, v);
        v = roundHalfUp10(v);
      }

      v = -v;
    }

    if (v < minValue || maxValue < v) {
      // overflow can only be off by a factor of 10, since this is
      // addition/subtraction
      if (--s < minScale) {
        result.error("Overflow");
        return false;
      }

      final boolean signsEqual = v1 < 0 == v2 < 0;
      v = signsEqual || ds2 > 0 ? unroundHalfUp(r, v) : unroundHalfDown(r, v);
      v = roundHalfUp10(v);
//      if (v < minValue || maxValue < v)
//        throw new IllegalStateException("Should not happen");
    }

    result.set(v, s);
    return true;
  }
}