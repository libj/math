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

import org.libj.lang.Numbers;

abstract class DecimalAddition extends FixedPoint {
  private static final long serialVersionUID = 2903111300785124642L;

  static Decimal add0(final Decimal d1, final long v2, final short s2) {
    final Decimal result = threadLocal.get();
    if (add0(d1.value, d1.scale, v2, s2, MAX_PRECISION_D, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, false, result))
      return new Decimal(result);

    // Special situation where negation may have caused an overflow, but the result is clearly zero
    if (Decimal.compare(d1.value, d1.scale, -v2, s2) == 0)
      return ZERO;

    return null;
  };

  static long add0(long ld1, long ld2, final byte scaleBits, final long defaultValue) {
    long v1 = decodeValue(ld1, scaleBits);
    if (v1 == 0)
      return ld2;

    long v2 = decodeValue(ld2, scaleBits);
    if (v2 == 0)
      return ld1;

    final byte valueBits = valueBits(scaleBits);
    final long minValue = Decimal.minValue(valueBits);
    final long maxValue = Decimal.maxValue(valueBits);
    final short minScale = minScale(scaleBits);
    final short maxScale = maxScale(scaleBits);
    short s1 = decodeScale(ld1, scaleBits);
    short s2 = decodeScale(ld2, scaleBits);
    final byte maxPrecision = s1 == s2 ? -1 : valueBits;
    final Decimal result = threadLocal.get();
    if (add0(v1, s1, v2, s2, maxPrecision, minValue, maxValue, minScale, maxScale, false, result))
      return result.encode(scaleBits, defaultValue);

    return defaultValue;
  }

  static boolean add0(long v1, short s1, long v2, short s2, final byte maxPrecision, final long minValue, final long maxValue, final short minScale, final short maxScale, final boolean negate, final Decimal result) {
    long v;
    short s;
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

      v = v1;
      s = s1;

      // Let's try to match the scales by adjusting v1 so we don't lose precision,
      // then if the scales are not yet matched due to hitting a limit of v1's scale,
      // shift v2 (losing precision, but it's ok cause that precision is insignificant).

      // Get the number of bits unused in v1 for expansion
      byte bp1 = (byte)(maxPrecision - binaryPrecisionRequiredForValue(Math.abs(v1)));

      // This means that v1 was larger than what maxPrecision allows, which
      // can happen via sub() where it changes sign of minValue to -minValue
      if (bp1 < 0)
        bp1 = 0;

      // Change that to available decimal places (use floor, because we're calculating availability)
      // FIXME: I think we can change this to Numbers.precision()
      final byte dp1 = bp1 == 0 ? 0 : (byte)SafeMath.floor(SafeMath.log10((1L << bp1) - 1));

      // ds is always positive, and greater than 0
      int ds = s2 - s1;

      // What is the most we can shift v1 (lossless)?
      short ds1 = SafeMath.min(dp1, ds);

      // Don't go past maxScale when adjusting v1 and s1
      ds1 = SafeMath.min(ds1, maxScale - s1);

      ds -= ds1;
      s1 += ds1;

      // What is the most we can shift v2 (lossy)?
      short ds2;
      if (ds == 0) {
        ds2 = 0;
      }
      else {
        // How many decimal places can we reduce precision until it's
        // insignificant? Here we actually allow v2 to be reduced all the way
        // to 0, since ds2 accounts for all of the digits in v2. If it so
        // happens that v2 is reduced to the full size of ds2, then we still
        // have the opportunity to check the prior-to-last digit (right before
        // v2 becomes 0) if it would round up or down. If it rounds up, then we
        // add 1, otherwise the result is truly insignificant.
        ds2 = Numbers.precision(Math.abs(v2));

        // Take the lesser of shift2 and ds
        ds2 = SafeMath.min(ds2, ds);

        // Don't go past minScale when adjusting v2 and s2
        ds2 = SafeMath.min(ds2, s2 - minScale);

        ds -= ds2;
        s2 -= ds2;
      }

      // If ds accounts for the gap in scales, then let's perform the adjustments
      do {
        if (ds == 0) {
          if (s1 != s2)
            throw new IllegalStateException();

          if (ds1 > 0) {
            // Make the lossless adjustment to v1
            v1 *= FastMath.e10[ds1];
            if (v1 > maxValue)
              throw new IllegalStateException();
          }

          if (ds2 > 0) {
            // Make the lossy adjustment to v2
            final int adj = ds2 - 1; // Leave one factor for rounding
            if (adj > 0)
              v2 /= FastMath.e10[adj];

            v2 = roundDown10(v2);
            if (v2 == 0)
              break;
          }

          v = v1 + v2;
          s = s1;
        }
      }
      while (false);
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

        final long r1 = v1 % 10;
        v1 /= 10;
        if (r1 >= 5)
          v1 += 1;
        else if (r1 <= -5)
          v1 -= 1;

        final long r2 = v2 % 10;
        v2 /= 10;
        if (r2 >= 5)
          v2 += 1;
        else if (r2 <= -5)
          v2 -= 1;

        v = v1 + v2;
        if (v < 0 ? sig != -1 : v == 0 ? sig != 0 : sig != 1)
          throw new IllegalStateException("Should not happen");

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

        v /= 10;
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

      v /= 10;
      if (v < minValue || maxValue < v)
        throw new IllegalStateException("Should not happen");
    }

    result.set(v, s);
    return true;
  }
}