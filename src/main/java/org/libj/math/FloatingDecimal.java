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

/**
 * Provides constants for encoding of floating primitives, and encodes
 * {@link Decimal} to a double precision floating point number. Redacted from
 * {@code sun.misc.FloatConsts}, {@code sun.misc.DoubleConsts}, and
 * {@code sun.misc.FloatingDecimal}.
 */
class FloatingDecimal {
  /**
   * The number of logical bits in the significand of a {@code float} number,
   * including the implicit bit.
   */
  static final int SIGNIFICAND_WIDTH_FLOAT = 24;

  /**
   * The number of logical bits in the significand of a {@code double} number,
   * including the implicit bit.
   */
  static final int SIGNIFICAND_WIDTH_DOUBLE = 53;

  /** Bit mask to isolate the sign bit of a {@code float}. */
  static final int SIGN_BIT_MASK_FLOAT = 0x80000000;

  /** Bit mask to isolate the sign bit of a {@code double}. */
  static final long SIGN_BIT_MASK_DOUBLE = 0x8000000000000000L;

  /** Bit mask to isolate the significand field of a {@code float}. */
  static final int SIGNIF_BIT_MASK_FLOAT = 0x007FFFFF;

  /** Bit mask to isolate the significand field of a {@code double}. */
  static final long SIGNIF_BIT_MASK_DOUBLE = 0x000FFFFFFFFFFFFFL;

  /** Bias used in representing a {@code float} exponent. */
  static final short EXP_BIAS_FLOAT = 127;

  /** Bias used in representing a {@code double} exponent. */
  static final short EXP_BIAS_DOUBLE = 1023;

  // Constants of the implementation; most are IEEE-754 related.
  private static final int EXP_SHIFT = SIGNIFICAND_WIDTH_DOUBLE - 1;
  private static final long FRACT_HOB = 1L << EXP_SHIFT; // assumed High-Order bit
  private static final byte MAX_DECIMAL_DIGITS = 15;
  private static final short MAX_DECIMAL_EXPONENT = 308;
  private static final short MIN_DECIMAL_EXPONENT = -324;

  /** Bit mask to isolate the exponent field of a {@code double}. */
  private static final long EXP_BIT_MASK = 0x7FF0000000000000L;

  /**
   * All the positive powers of 10 that can be represented exactly in
   * double/float.
   */
  private static final double[] SMALL_10_POW = {1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9, 1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19, 1e20, 1e21, 1e22};

  private static final double[] BIG_10_POW = {1e16, 1e32, 1e64, 1e128, 1e256};
  private static final double[] TINY_10_POW = {1e-16, 1e-32, 1e-64, 1e-128, 1e-256};

  private static final int MAX_SMALL_TEN = SMALL_10_POW.length - 1;

  static double doubleValue(final long value, final int scale) {
    if (scale == 0 || value == 0)
      return value;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MAX_VALUE / value))
        return value * FastMath.longE10[s];
    }

    long absVal = Math.abs(value);
    if (absVal < 1L << 52) {
      if (scale > 0 && scale < SMALL_10_POW.length)
        return value / SMALL_10_POW[scale];

      if (scale < 0 && scale > -SMALL_10_POW.length)
        return value * SMALL_10_POW[-scale];
    }

    if (value == Long.MIN_VALUE)
      absVal = Long.MAX_VALUE;

    final boolean isNeg = value < 0;
    final int exp10 = Numbers.precision(absVal) - scale;
    if (exp10 > MAX_DECIMAL_EXPONENT + 1)
      return isNeg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

    if (exp10 < MIN_DECIMAL_EXPONENT)
      return 0d;

    int ds = Numbers.trailingZeroes(absVal);
    if (ds > 0)
      absVal /= FastMath.longE10[ds];

    final long absValNz = absVal;
    final int nDigits = Numbers.precision(absVal);

    ds = Numbers.precision(absVal) - 16;
    if (ds > 0)
      absVal /= FastMath.longE10[ds];

    final int kDigits = Math.min(nDigits, MAX_DECIMAL_DIGITS + 1);

    double dValue = absVal;
    int exp2 = exp10 - kDigits;
    // absVal now contains a long integer with the value of the first kDigits
    // digits of the number. dValue contains the (double) of the same.
    if (nDigits <= MAX_DECIMAL_DIGITS) {
      // Possibly an easy case:
      // We know that the digits can be represented exactly.
      // If the exponent isn't too outrageous, the whole thing can be done with
      // one operation, thus one rounding error. Note that all our constructors
      // trim all leading and trailing zeros, so simple values (including zero)
      // will always end up here.
      if (exp2 == 0 || dValue == 0d)
        return isNeg ? -dValue : dValue; // small floating integer

      if (exp2 >= 0) {
        if (exp2 <= MAX_SMALL_TEN) {
          // Can get the answer with one operation, thus one roundoff.
          final double rValue = dValue * SMALL_10_POW[exp2];
          return isNeg ? -rValue : rValue;
        }

        final int slop = MAX_DECIMAL_DIGITS - kDigits;
        if (exp2 <= MAX_SMALL_TEN + slop) {
          // We can multiply dValue by 10^(slop) and it is still "small" and exact.
          // Then we can multiply by 10^(exp-slop) with one rounding.
          dValue *= SMALL_10_POW[slop];
          final double rValue = dValue * SMALL_10_POW[exp2 - slop];
          return isNeg ? -rValue : rValue;
        }
      }
      else if (exp2 >= -MAX_SMALL_TEN) {
        // We have a hard case with a positive exp:
        // Can get the answer in one division.
        final double rValue = dValue / SMALL_10_POW[-exp2];
        return isNeg ? -rValue : rValue;
      }
    }

    // We have a hard case with a negative exp:

    // Harder cases:
    // The sum of digits plus exponent is greater than what we think we can do
    // with one error. Start by approximating the right answer by, naively,
    // scaling by powers of 10.
    if (exp2 > 0) {
      if (exp10 > MAX_DECIMAL_EXPONENT + 1)
        return isNeg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

      if ((exp2 & 15) != 0)
        dValue *= SMALL_10_POW[exp2 & 15];

      if ((exp2 >>= 4) != 0) {
        int j;
        for (j = 0; exp2 > 1; ++j, exp2 >>= 1)
          if ((exp2 & 1) != 0)
            dValue *= BIG_10_POW[j];

        // The reason for the weird exp > 1 condition in the above loop was so
        // that the last multiply would get unrolled. We handle it here. It
        // could overflow.
        double t = dValue * BIG_10_POW[j];
        if (Double.isInfinite(t)) {
          // It did overflow. Look more closely at the result. If the exponent
          // is just one too large, then use the maximum finite as our estimate
          // value. Else call the result infinity and punt it. (I presume this
          // could happen because rounding forces the result here to be an ULP
          // or two larger than Double.MAX_VALUE).
          t = dValue / 2d;
          t *= BIG_10_POW[j];
          if (Double.isInfinite(t))
            return isNeg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

          t = Double.MAX_VALUE;
        }

        dValue = t;
      }
    }
    else if (exp2 < 0) {
      exp2 = -exp2;
      if (exp10 < MIN_DECIMAL_EXPONENT - 1)
        return isNeg ? -0d : 0d;

      if ((exp2 & 15) != 0)
        dValue /= SMALL_10_POW[exp2 & 15];

      if ((exp2 >>= 4) != 0) {
        int j;
        for (j = 0; exp2 > 1; ++j, exp2 >>= 1)
          if ((exp2 & 1) != 0)
            dValue *= TINY_10_POW[j];

        // The reason for the weird exp > 1 condition in the above loop was so
        // that the last multiply would get unrolled. We handle it here. It could underflow.
        double t = dValue * TINY_10_POW[j];
        if (t == 0d) {
          // It did underflow:
          // Look more closely at the result. If the exponent is just one too
          // small, then use the minimum finite as our estimate value. Else call
          // the result 0 and punt it. (I presume this could happen because
          // rounding forces the result here to be an ULP or two less than
          // Double.MIN_VALUE).
          t = dValue * 2d;
          t *= TINY_10_POW[j];
          if (t == 0d)
            return isNeg ? -0d : 0d;

          t = Double.MIN_VALUE;
        }

        dValue = t;
      }
    }

    exp2 = exp10 - nDigits;

    long ieeeBits = Double.doubleToRawLongBits(dValue); // IEEE-754 bits of double candidate
    final int b5 = Math.max(0, -exp2); // Powers of 5 in bigB
    final int d5 = Math.max(0, exp2); // Powers of 5 in bigD

    // dValue is now approximately the result
    // The hard part is adjusting it, by comparison with BigInt arithmetic.
    // Formulate the EXACT big-number result as bigD0 * 10^exp.
    int[] bigD0 = BigInt.mulPow52(BigInt.assign(new int[27], absValNz), d5, 0);
    int[] bigD = null;
    int prevD2 = 0;

    correctionLoop:
    while (true) {
      // here ieeeBits can't be NaN, Infinity or zero
      int binexp = (int)(ieeeBits >>> EXP_SHIFT);
      long bigBbits = ieeeBits & SIGNIF_BIT_MASK_DOUBLE;
      if (binexp > 0) {
        bigBbits |= FRACT_HOB;
      }
      else {
        // Normalize denormalized numbers.
        final int leadingZeros = Long.numberOfLeadingZeros(bigBbits);
        final int shift = leadingZeros - (63 - EXP_SHIFT);
        bigBbits <<= shift;
        binexp = 1 - shift;
      }

      binexp -= EXP_BIAS_DOUBLE;
      final int lowOrderZeros = Long.numberOfTrailingZeros(bigBbits);
      bigBbits >>>= lowOrderZeros;
      final int bigIntExp = binexp - EXP_SHIFT + lowOrderZeros;
      final int bigIntNBits = EXP_SHIFT + 1 - lowOrderZeros;

      // Scale bigD, bigB appropriately for big-integer operations.
      // Naively, we multiply by powers of ten and powers of two. What we
      // actually do is keep track of the powers of 5 and powers of 2 we would
      // use, then factor out common divisors before doing the work.
      int b2 = b5; // Powers of 2 in bigB
      int d2 = d5; // Powers of 2 in bigD
      int dlp2; // Powers of 2 in halfUlp
      if (bigIntExp >= 0)
        b2 += bigIntExp;
      else
        d2 -= bigIntExp;

      dlp2 = b2;
      // shift bigB and bigD left by a number s. t. halfUlp is still an integer.
      final int hulpbias;
      if (binexp <= -EXP_BIAS_DOUBLE) {
        // This is going to be a denormalized number (if not actually zero).
        // half an ULP is at 2^-(EXP_BIAS+EXP_SHIFT+1)
        hulpbias = binexp + lowOrderZeros + EXP_BIAS_DOUBLE;
      }
      else {
        hulpbias = 1 + lowOrderZeros;
      }

      b2 += hulpbias;
      d2 += hulpbias;

      // if there are common factors of 2, we might just as well
      // factor them out, as they add nothing useful.
      final int common2 = Math.min(b2, Math.min(d2, dlp2));
      b2 -= common2;
      d2 -= common2;
      dlp2 -= common2;

      // do multiplications by powers of 5 and 2
      int[] bigB = BigInt.mulPow52(BigInt.assign(new int[(b2 >> 5) + 28], bigBbits), b5, b2);
      if (bigD == null || prevD2 != d2) {
        bigD = BigInt.shiftLeft(bigD0, d2).clone();
        prevD2 = d2;
      }

      // to recap:
      // bigB is the scaled-big-int version of our floating-point candidate.
      // bigD is the scaled-big-int version of the exact value as we understand it.
      // halfUlp is 1/2 an ulp of bigB, except for special cases of exact powers of 2.
      // The plan is to compare bigB with bigD, and if the difference is less
      // than halfUlp, then we're satisfied. Otherwise, use the ratio of
      // difference to halfUlp to calculate a fudge factor to add to the
      // floating value, then go 'round again.
      int cmpResult = BigInt.compareTo(bigB, bigD);
      final int overvalue;
      if (cmpResult > 0) {
        overvalue = -1; // our candidate is too big.
        BigInt.sub(bigB, bigD);
        if (bigIntNBits == 1 && bigIntExp > 1 - EXP_BIAS_DOUBLE) {
          // candidate is a normalized exact power of 2 and is too big (larger
          // than Double.MIN_NORMAL). We will be subtracting. For our purposes,
          // ulp is the ulp of the next smaller range.
          if (--dlp2 < 0) {
            // Cannot de-scale ulp this far, so must scale diff in other direction.
            dlp2 = 0;
            BigInt.shiftLeft(bigB, 1);
          }
        }
      }
      else if (cmpResult < 0) {
        overvalue = 1; // our candidate is too small.
        BigInt.add(BigInt.neg(bigB), bigD);
      }
      else {
        // The candidate is exactly right! This happens with surprising frequency.
        break correctionLoop;
      }

      cmpResult = BigInt.compareToPow52(bigB, b5, dlp2);
      if (cmpResult < 0)
        break correctionLoop; // Difference is small, so it's close enough.

      if (cmpResult == 0) {
        // Difference is exactly half an ULP round to some other value maybe, then finish.
        if ((ieeeBits & 1) != 0) // Half ties to even
          ieeeBits += overvalue; // nextDown or nextUp

        break correctionLoop;
      }

      // Difference is non-trivial.
      // Could scale addend by ratio of difference to halfUlp here, if we bothered to compute that difference.
      // Most of the time (I hope) it is about 1 anyway.
      ieeeBits += overvalue; // nextDown or nextUp
      if (ieeeBits == 0 || ieeeBits == EXP_BIT_MASK) // 0 or Double.POSITIVE_INFINITY
        break correctionLoop; // oops. Fell off end of range.

      continue; // Try again.
    }

    if (isNeg)
      ieeeBits |= SIGN_BIT_MASK_DOUBLE;

    return Double.longBitsToDouble(ieeeBits);
  }
}