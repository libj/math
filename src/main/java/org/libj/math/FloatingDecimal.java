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

/**
 * Provides constants for encoding of floating primitives, and encodes {@link Decimal} to a double precision floating point number.
 * Redacted from {@code sun.misc.FloatConsts}, {@code sun.misc.DoubleConsts}, and {@code sun.misc.FloatingDecimal}.
 */
class FloatingDecimal {
  // numbers greater than 10^MAX_DIGITS_10 or e^MAX_DIGITS_E are considered unsafe ('too big') for floating point operations
  static final int MAX_DIGITS_10 = 294;
  static final int MAX_DIGITS_2 = 977; // ~ MAX_DIGITS_10 * LN(10) / LN(2)
  static final int MAX_DIGITS_E = 677; // ~ MAX_DIGITS_10 * LN(10)

  /** The number of logical bits in the significand of a {@code float} number, excluding the implicit bit. */
  static final int SIGNIFICAND_BITS_FLOAT = 23;

  /** The number of logical bits in the significand of a {@code float} number, including the implicit bit. */
  static final int SIGNIFICAND_WIDTH_FLOAT = SIGNIFICAND_BITS_FLOAT + 1;

  /** The number of logical bits in the significand of a {@code double} number, excluding the implicit bit. */
  static final int SIGNIFICAND_BITS_DOUBLE = 52;

  /** The number of logical bits in the significand of a {@code double} number, including the implicit bit. */
  static final int SIGNIFICAND_WIDTH_DOUBLE = SIGNIFICAND_BITS_DOUBLE + 1;

  /** Bit mask to isolate the sign bit of a {@code float}. */
  static final int SIGN_BIT_MASK_FLOAT = 0x80000000;

  /** Bit mask to isolate the sign bit of a {@code double}. */
  static final long SIGN_BIT_MASK_DOUBLE = 0x8000000000000000L;

  /** Bit mask to isolate the significand field of a {@code float}. */
  static final int SIGNIF_BIT_MASK_FLOAT = 0x007FFFFF;

  /** Bit mask to isolate the significand field of a {@code double}. */
  static final long SIGNIF_BIT_MASK_DOUBLE = 0x000FFFFFFFFFFFFFL;

  /** The implicit 1 bit that is omitted in significands of normal doubles. */
  private static final long IMPLICIT_BIT_DOUBLE = SIGNIF_BIT_MASK_DOUBLE + 1;

  /** Bias used in representing a {@code float} exponent. */
  static final short EXP_BIAS_FLOAT = 127;

  /** Bias used in representing a {@code double} exponent. */
  static final short EXP_BIAS_DOUBLE = 1023;

  // Constants of the implementation; most are IEEE-754 related.
  private static final int EXP_SHIFT = SIGNIFICAND_WIDTH_DOUBLE - 1; // FIXME: Change to EXP_SHIFT_DOUBLE
  private static final int EXP_SHIFT_FLOAT = SIGNIFICAND_WIDTH_FLOAT - 1;

  private static final long FRACT_HOB = 1L << EXP_SHIFT; // assumed High-Order bit // FIXME: Change to FRACT_HOB_DOUBLE
  private static final int FRACT_HOB_FLOAT = 1 << EXP_SHIFT_FLOAT;

  private static final long EXP_ONE_DOUBLE = ((long)EXP_BIAS_DOUBLE) << EXP_SHIFT;

  private static final int MAX_SMALL_BIN_EXP = 62;
  private static final int MIN_SMALL_BIN_EXP = -(63 / 3);

  private static final byte MAX_DECIMAL_DIGITS = 15;
  private static final short MAX_DECIMAL_EXPONENT = 308;
  private static final short MIN_DECIMAL_EXPONENT = -324;

  /** Bit mask to isolate the exponent field of a {@code double}. */
  private static final long EXP_BIT_MASK_DOUBLE = 0x7FF0000000000000L;

  /** Bit mask to isolate the exponent field of a {@code float}. */
  private static final int EXP_BIT_MASK_FLOAT = 0x7F800000;

  /**
   * All the positive powers of 10 that can be represented exactly in double/float.
   */
  private static final double[] SMALL_10_POW = {1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9, 1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19, 1e20, 1e21, 1e22};

  private static final double[] BIG_10_POW = {1e16, 1e32, 1e64, 1e128, 1e256};
  private static final double[] TINY_10_POW = {1e-16, 1e-32, 1e-64, 1e-128, 1e-256};

  private static final int MAX_SMALL_TEN = SMALL_10_POW.length - 1;

  static long getSignificand(final double value, final int exponent) {
    final long bits = Double.doubleToRawLongBits(value) & SIGNIF_BIT_MASK_DOUBLE;
    return exponent == Double.MIN_EXPONENT - 1 ? bits << 1 : bits | IMPLICIT_BIT_DOUBLE;
  }

  static double doubleValue(final long value, final int scale) {
    if (scale == 0 || value == 0)
      return value;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / value))
        return value * FastMath.longE10[s];
    }

    long absVal = Math.abs(value);
    if (absVal < 1L << SIGNIFICAND_BITS_DOUBLE) {
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

    ds = nDigits - 16;
    if (ds > 0)
      absVal /= FastMath.longE10[ds];

    final int kDigits = Math.min(nDigits, MAX_DECIMAL_DIGITS + 1);

    double dValue = absVal;
    int exp2 = exp10 - kDigits;
    // absVal now contains a long integer with the value of the first kDigits
    // digits of the number. dValue contains the (double) of the same.
    if (nDigits <= MAX_DECIMAL_DIGITS) {
      /*
       * Possibly an easy case: We know that the digits can be represented exactly. If the exponent isn't too outrageous, the whole thing
       * can be done with one operation, thus one rounding error. Note that all our constructors trim all leading and trailing zeros, so
       * simple values (including zero) will always end up here.
       */
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

    /*
     * Harder cases: The sum of digits plus exponent is greater than what we think we can do with one error. Start by approximating the
     * right answer by, naively, scaling by powers of 10.
     */
    if (exp2 > 0) {
      if (exp10 > MAX_DECIMAL_EXPONENT + 1)
        return isNeg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

      if ((exp2 & 15) != 0)
        dValue *= SMALL_10_POW[exp2 & 15];

      if ((exp2 >>= 4) != 0) {
        int j;
        for (j = 0; exp2 > 1; ++j, exp2 >>= 1) // [A]
          if ((exp2 & 1) != 0)
            dValue *= BIG_10_POW[j];

        // The reason for the weird exp > 1 condition in the above loop was so
        // that the last multiply would get unrolled. We handle it here. It
        // could overflow.
        double t = dValue * BIG_10_POW[j];
        if (Double.isInfinite(t)) {
          /*
           * It did overflow. Look more closely at the result. If the exponent is just one too large, then use the maximum finite as our
           * estimate value. Else call the result infinity and punt it. (I presume this could happen because rounding forces the result here
           * to be an ULP or two larger than Double.MAX_VALUE).
           */
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
        for (j = 0; exp2 > 1; ++j, exp2 >>= 1) // [A]
          if ((exp2 & 1) != 0)
            dValue *= TINY_10_POW[j];

        // The reason for the weird exp > 1 condition in the above loop was so
        // that the last multiply would get unrolled. We handle it here. It could underflow.
        double t = dValue * TINY_10_POW[j];
        if (t == 0d) {
          /*
           * It did underflow: Look more closely at the result. If the exponent is just one too small, then use the minimum finite as our
           * estimate value. Else call the result 0 and punt it. (I presume this could happen because rounding forces the result here to be an
           * ULP or two less than Double.MIN_VALUE).
           */
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
    final int b5; // Powers of 5 in bigB
    final int d5; // Powers of 5 in bigD
    if (exp2 > 0) {
      b5 = 0;
      d5 = exp2;
    }
    else {
      b5 = -exp2;
      d5 = 0;
    }

    final int[][] buf = threadLocal.get();

    // dValue is now approximately the result
    // The hard part is adjusting it, by comparison with BigInt arithmetic.
    // Formulate the EXACT big-number result as bigD0 * 10^exp.
    final int[] bigD0 = BigInt.mulPow52InPlace(BigInt.assignInPlace(buf[0], absValNz), d5, 0);
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

      /*
       * Scale bigD, bigB appropriately for big-integer operations. Naively, we multiply by powers of ten and powers of two. What we
       * actually do is keep track of the powers of 5 and powers of 2 we would use, then factor out common divisors before doing the work.
       */
      int b2 = b5; // Powers of 2 in bigB
      int d2 = d5; // Powers of 2 in bigD
      if (bigIntExp >= 0)
        b2 += bigIntExp;
      else
        d2 -= bigIntExp;

      int dlp2 = b2; // Powers of 2 in halfUlp
      // shift bigB and bigD left by a number s. t. halfUlp is still an integer.
      final int hulpbias;
      if (binexp <= -EXP_BIAS_DOUBLE) {
        // This is going to be a denormalized number (if not actually zero). half an ULP is at 2^-(EXP_BIAS+EXP_SHIFT+1)
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
      final int[] bigB = BigInt.mulPow52InPlace(BigInt.assignInPlace(buf[4], bigBbits), b5, b2); // new int[(b2 >> 5) + 28]
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
      if (ieeeBits == 0 || ieeeBits == EXP_BIT_MASK_DOUBLE) // 0 or Double.POSITIVE_INFINITY
        break correctionLoop; // oops. Fell off end of range.

      continue; // Try again.
    }

    if (isNeg)
      ieeeBits |= SIGN_BIT_MASK_DOUBLE;

    return Double.longBitsToDouble(ieeeBits);
  }

  /* =========================================================================================== */
  /* =========================================================================================== */
  /* =========================================================================================== */

  private static final ThreadLocal<int[][]> threadLocal = new ThreadLocal<int[][]>() {
    @Override
    protected int[][] initialValue() {
      return new int[5][27];
    }
  };

  /*
   * If insignificant==(1L << ixd) i = insignificantDigitsNumber[idx] is the same as: int i; for ( i = 0; insignificant >= 10L; i++ )
   * insignificant /= 10L;
   */
  private static final int[] insignificantDigitsNumber = {
    0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 17, 17, 17, 18, 18, 18, 19
  };

  // approximately ceil( log2( long5pow[i] ) )
  private static final int[] N_5_BITS = {
    0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61,
  };

  private static final String INFINITY_REP = "Infinity";
  private static final String NAN_REP = "NaN";

  /*
   * Add one to the least significant digit. In the unlikely event there is a carry out, deal with it. Assert that this will only
   * happen where there is only one digit, e.g. (float)1e-44 seems to do it.
   */
  private static short roundup(short exp10, long value, final boolean leadingZero) {
    return leadingZero || Numbers.precision(value++) < Numbers.precision(value) ? ++exp10 : exp10;
  }

  /*
   * Estimate decimal exponent. (If it is small-ish, we could double-check.) First, scale the mantissa bits such that 1 <= d2 < 2. We
   * are then going to estimate log10(d2) ~=~ (d2-1.5)/1.5 + log(1.5) and so we can estimate log10(d) ~=~ log10(d2) + exp2 * log10(2)
   * take the floor and call it exp10.
   */
  private static short estimateExp10(final long fractBits, final int exp2) {
    final double d2 = Double.longBitsToDouble(EXP_ONE_DOUBLE | (fractBits & SIGNIF_BIT_MASK_DOUBLE));
    final double d = (d2 - 1.5D) * 0.289529654D + 0.176091259 + exp2 * 0.301029995663981;
    final long dBits = Double.doubleToRawLongBits(d); // can't be NaN here so use raw
    final short exponent = (short)(((dBits & EXP_BIT_MASK_DOUBLE) >> EXP_SHIFT) - EXP_BIAS_DOUBLE);
    final boolean isPositive = (dBits & SIGN_BIT_MASK_DOUBLE) == 0; // discover sign
    if (exponent >= 0 && exponent < 52) { // hot path
      final long mask = SIGNIF_BIT_MASK_DOUBLE >> exponent;
      final short r = (short)(((dBits & SIGNIF_BIT_MASK_DOUBLE) | FRACT_HOB) >> (EXP_SHIFT - exponent));
      return isPositive ? r : (short)((mask & dBits) == 0L ? -r : -r - 1);
    }

    return exponent >= 0 ? (short)d : (dBits & ~SIGN_BIT_MASK_DOUBLE) == 0 ? (short)0 : isPositive ? (short)0 : (short)-1;
  }

  /**
   * Calculates
   *
   * <pre>
   * insignificantDigitsForPow2(v) == insignificantDigits(1L << v)
   * </pre>
   */
  private static int insignificantDigitsForPow2(final int p2) {
    return p2 > 1 && p2 < insignificantDigitsNumber.length ? insignificantDigitsNumber[p2] : 0;
  }

  private static Decimal toDecimal(long value, short scale, final boolean isNegative, final RoundingMode rm, final Decimal result) {
    scale *= -1;
    final byte z1 = Numbers.trailingZeroes(value);
    if (z1 > 0) {
      value /= FastMath.longE10[z1];
      scale -= z1;
    }

    if (isNegative) {
      value *= -1;
      if (value < Decimal.MIN_SIGNIFICAND) {
        final byte ds = Numbers.precision(value / Decimal.MIN_SIGNIFICAND);
        if ((value = Decimal.round(value, (byte)16, ds, rm, 0)) == 0)
          return null;

        scale -= ds;
      }
    }
    else if (value > Decimal.MAX_SIGNIFICAND) {
      final byte ds = Numbers.precision(value / Decimal.MAX_SIGNIFICAND);
      if ((value = Decimal.round(value, (byte)16, ds, rm, 0)) == 0)
        return null;

      scale -= ds;
    }

    return FixedPoint.checkScale(value, Numbers.precision(value), scale, result) ? result : null;
  }

  private static Decimal toDecimal(final int exp2, long fractBits, final int nSignificantBits, final boolean isNegative, final RoundingMode rm, final Decimal result) {
    // Examine number. Determine if it is an easy case, which we can do pretty trivially using float/long conversion,
    // or whether we must do real work.
    final int tailZeros = Long.numberOfTrailingZeros(fractBits);

    // number of significant bits of fractBits;
    final int nFractBits = EXP_SHIFT + 1 - tailZeros;

    // number of significant bits to the right of the point.
    final int nTinyBits = Math.max(0, nFractBits - exp2 - 1);
    if (exp2 <= MAX_SMALL_BIN_EXP && exp2 >= MIN_SMALL_BIN_EXP) {
      // Look more closely at the number to decide if, with scaling by 10^nTinyBits, the result will fit in a long.
      if (nTinyBits < BigInt.LONG_5_POW.length && nFractBits + N_5_BITS[nTinyBits] < 64) {
        /**
         * We can do this: take the fraction bits, which are normalized. (a) nTinyBits == 0: Shift left or right appropriately to align the
         * binary point at the extreme right, i.e. where a long int point is expected to be. The integer result is easily converted to a
         * string. (b) nTinyBits > 0: Shift right by EXP_SHIFT-nFractBits, which effectively converts to long and scales by 2^nTinyBits.
         * Then multiply by 5^nTinyBits to complete the scaling. We know this won't overflow because we just counted the number of bits
         * necessary in the result. The integer you get from this can then be converted to a string pretty easily.
         */
        if (nTinyBits == 0) {
          final int insignificant = exp2 > nSignificantBits ? insignificantDigitsForPow2(exp2 - nSignificantBits - 1) : 0;
          if (exp2 >= EXP_SHIFT)
            fractBits <<= exp2 - EXP_SHIFT;
          else
            fractBits >>>= EXP_SHIFT - exp2;

          short exp10 = 0;

          /**
           * This is the easy subcase -- all the significant bits, after scaling, are held in lvalue. negSign and exp10 tell us what
           * processing and scaling has already been done. Exceptional cases have already been stripped out. In particular: lvalue is a finite
           * number (not Inf, nor NaN) lvalue > 0L (not zero, nor negative). The only reason that we develop the digits here, rather than
           * calling on Long.toString() is that we can do it a little faster, and besides want to treat trailing 0s specially. If
           * Long.toString changes, we should re-evaluate this strategy!
           */
          if (insignificant != 0) {
            // Discard non-significant low-order bits, while rounding,
            // up to insignificant value.
            final long pow10 = BigInt.LONG_5_POW[insignificant] << insignificant; // 10^i == 5^i * 2^i;
            final long residue = fractBits % pow10;
            fractBits /= pow10;
            exp10 += insignificant;
            if (residue >= pow10 >> 1)
              ++fractBits; // round up based on the low-order bits we're discarding
          }

          return toDecimal(fractBits, exp10, isNegative, rm, result);
        }

        // The following causes excess digits to be printed out in the
        // single-float case. Our manipulation of halfULP here is apparently not
        // correct. If we better understand how this works, perhaps we can use
        // this special case again. But for the time being, we do not.
        // else {
        // fractBits >>>= EXP_SHIFT+1-nFractBits;
        // fractBits//= long5pow[ nTinyBits ];
        // halfULP = long5pow[ nTinyBits ] >> (1+nSignificantBits-nFractBits);
        // developLongDigits( -nTinyBits, fractBits,
        // insignificantDigits(halfULP) );
        // return;
        // }
      }
    }

    // This is the hard case. We are going to compute large positive
    // integers B and S and integer exp10, s.t.
    // d = ( B / S )// 10^exp10
    // 1 <= B / S < 10
    // Obvious choices are:
    // exp10 = floor( log10(d) )
    // B = d// 2^nTinyBits// 10^max( 0, -exp10 )
    // S = 10^max( 0, exp10)// 2^nTinyBits
    // (noting that nTinyBits has already been forced to non-negative)
    // I am also going to compute a large positive integer
    // M = (1/2^nSignificantBits)// 2^nTinyBits// 10^max( 0, -exp10 )
    // i.e. M is (1/2) of the ULP of d, scaled like B.
    // When we iterate through dividing B/S and picking off the
    // quotient bits, we will know when to stop when the remainder
    // is <= M.
    //
    // We keep track of powers of 2 and powers of 5.
    short exp10 = estimateExp10(fractBits, exp2);
    int B2, B5; // powers of 2 and powers of 5, respectively, in B
    int S2, S5; // powers of 2 and powers of 5, respectively, in S
    int M2, M5; // powers of 2 and powers of 5, respectively, in M

    B5 = Math.max(0, -exp10);
    B2 = B5 + nTinyBits + exp2;

    S5 = Math.max(0, exp10);
    S2 = S5 + nTinyBits;

    M5 = B5;
    M2 = B2 - nSignificantBits;

    /*
     * the long integer fractBits contains the (nFractBits) interesting bits from the mantissa of d ( hidden 1 added if necessary)
     * followed by (EXP_SHIFT+1-nFractBits) zeros. In the interest of compactness, I will shift out those zeros before turning fractBits
     * into a BigInt. The resulting whole number will be d * 2^(nFractBits-1-exp2).
     */
    fractBits >>>= tailZeros;
    B2 -= nFractBits - 1;

    final int common2factor = Math.min(B2, S2);
    B2 -= common2factor;
    S2 -= common2factor;
    M2 -= common2factor;

    /*
     * HACK!! For exact powers of two, the next smallest number is only half as far away as we think (because the meaning of ULP changes
     * at power-of-two bounds) for this reason, we hack M2. Hope this works.
     */
    if (nFractBits == 1)
      M2 -= 1;

    if (M2 < 0) {
      // oops. since we cannot scale M down far enough, we must scale the other values up.
      B2 -= M2;
      S2 -= M2;
      M2 = 0;
    }

    /*
     * Construct, Scale, iterate. Some day, we'll write a stopping test that takes account of the asymmetry of the spacing of
     * floating-point numbers below perfect powers of 2 26 Sept 96 is not that day. So we use a symmetric test.
     */
    boolean low, high;
    long lowDigitDifference;
    int q;

    /**
     * Detect the special cases where all the numbers we are about to compute will fit in int or long integers. In these cases, we will
     * avoid doing BigInt arithmetic. We use the same algorithms, except that we "normalize" our BigInts before iterating. This is to
     * make division easier, as it makes our fist guess (quotient of high-order words) more accurate!
     * <p>
     * Some day, we'll write a stopping test that takes account of the asymmetry of the spacing of floating-point numbers below perfect
     * powers of 2 26 Sept 96 is not that day. So we use a symmetric test.
     * <p>
     * binary digits needed to represent B, approximation.
     */
    final int bBits = nFractBits + B2 + (B5 < N_5_BITS.length ? N_5_BITS[B5] : B5 * 3);

    // binary digits needed to represent 10*s, approximation.
    boolean leadingZero = false;
    final int s10Bits = S2 + 1 + (S5 + 1 < N_5_BITS.length ? N_5_BITS[(S5 + 1)] : (S5 + 1) * 3);
    long value = 0;
    if (bBits < 64 && s10Bits < 64) {
      if (bBits < 32 && s10Bits < 32) {
        // wa-hoo! They're all ints!
        int b = ((int)fractBits * BigInt.INT_5_POW[B5]) << B2;
        int s = BigInt.INT_5_POW[S5] << S2;
        int m = BigInt.INT_5_POW[M5] << M2;
        int tens = s * 10;

        // Unroll the first iteration. If our exp10 estimate was too high, our first quotient will be zero. In this
        // case, we discard it and decrement exp10.
        q = b / s;
        b = 10 * (b % s);
        m *= 10;
        low = b < m;
        high = b + m > tens;

        if (q == 0) { // oops. Usually ignore leading zero.
          leadingZero = high;
          --exp10;
        }
        else {
          value = value * 10 + q;
        }

        // HACK! Java spec says that we always have at least one digit after the. in either F- or E-form output. Thus
        // we will need more than one digit if we're using E-form.
        if (exp10 < -3 || exp10 >= 8)
          high = low = false;

        while (!low && !high) {
          q = b / s;
          b = 10 * (b % s);
          m *= 10;
          if (m > 0L) {
            low = b < m;
            high = b + m > tens;
          }
          else {
            // hack -- m might overflow! in this case, it is certainly > b, which won't and b+m > tens,
            // too, since that has overflowed either!
            low = true;
            high = true;
          }

          value = value * 10 + q;
        }

        lowDigitDifference = (b << 1) - tens;
      }
      else {
        // still good! they're all longs!
        final long s = BigInt.LONG_5_POW[S5] << S2;
        final long s10 = s * 10L;

        long b = fractBits * BigInt.LONG_5_POW[B5] << B2;
        long m = BigInt.LONG_5_POW[M5] << M2;

        // Unroll the first iteration. If our exp10 estimate was too high, our first quotient will be zero. In this
        // case, we discard it and decrement exp10.
        q = (int)(b / s);
        b = 10L * (b % s);
        m *= 10L;
        low = b < m;
        high = b + m > s10;

        if (q == 0) { // oops. Usually ignore leading zero.
          leadingZero = high;
          --exp10;
        }
        else {
          value = value * 10 + q;
        }

        // HACK! Java spec says that we always have at least one digit after the . in either F- or E-form output.
        // Thus we will need more than one digit if we're using
        // E-form
        if (exp10 < -3 || exp10 >= 8)
          high = low = false;

        while (!low && !high) {
          q = (int)(b / s);
          b = 10 * (b % s);
          m *= 10;
          if (m > 0L) {
            low = b < m;
            high = b + m > s10;
          }
          else {
            // hack -- m might overflow! in this case, it is certainly > b, which won't and b+m > tens, too, since that has overflowed
            // either!
            low = true;
            high = true;
          }

          value = value * 10 + q;
        }

        lowDigitDifference = (b << 1) - s10;
      }
    }
    else {
      // We really must do BigInt arithmetic.
      // Fist, construct our BigInt initial values.
      final int[][] buf = threadLocal.get();

      final int[] s = BigInt.valueOfPow52(buf[0], S5, S2);
      final int shiftBias = BigInt.getNormalizationBias(s);
      BigInt.shiftLeft(s, shiftBias); // normalize so that division works better

      final int[] s10 = BigInt.valueOfPow52(buf[1], S5 + 1, S2 + shiftBias + 1); // Sval.mult(10);

      final int[] b = BigInt.mulPow52InPlace(BigInt.assignInPlace(buf[2], fractBits), B5, B2 + shiftBias);
      final int[] m = BigInt.valueOfPow52(buf[3], M5 + 1, M2 + shiftBias + 1);

      // Unroll the first iteration. If our exp10 estimate was too high, our first quotient will be zero. In this
      // case, we discard it and decrement exp10.
      q = BigInt.quoRemIteration(b, s);
      low = BigInt.compareTo(b, m) < 0;
      high = BigInt.compareTo(s10, BigInt.addInPlace(BigInt.assignInPlace(buf[4], m, m[0]), b)) <= 0;

      if (q == 0) { // oops. Usually ignore leading zero.
        leadingZero = high;
        --exp10;
      }
      else {
        value = value * 10 + q;
      }

      // HACK! Java spec says that we always have at least one digit after the . in either F- or E-form output.
      // Thus we will need more than one digit if we're using E-form
      if (exp10 < -3 || exp10 >= 8)
        high = low = false;

      while (!low && !high) {
        q = BigInt.quoRemIteration(b, s);
        BigInt.mulInPlace(m, 10); // Mval = Mval.mult( 10 );
        low = BigInt.compareTo(b, m) < 0;

        high = BigInt.compareTo(s10, BigInt.addInPlace(BigInt.assignInPlace(buf[4], m, m[0]), b)) <= 0;
        value *= 10;
        value += q;
      }

      if (high && low) {
        BigInt.shiftLeft(b, 1);
        lowDigitDifference = BigInt.compareTo(b, s10);
      }
      else {
        lowDigitDifference = 0L; // this here only for flow analysis!
      }
    }

    ++exp10;

    // Last digit gets rounded based on stopping condition.
    if (high) {
      if (low) {
        if (lowDigitDifference == 0L) {
          // it's a tie, so choose based on which digits we like.
          if (((value % 10) & 1) != 0) {
            exp10 = roundup(exp10, value++, leadingZero);
          }
        }
        else if (lowDigitDifference > 0) {
          exp10 = roundup(exp10, value++, leadingZero);
        }
      }
      else {
        exp10 = roundup(exp10, value++, leadingZero);
      }
    }

    return toDecimal(value, exp10 -= Numbers.precision(value), isNegative, rm, result);
  }

  static Decimal toDecimal(final float f, final Decimal result) {
    final int bits = Float.floatToRawIntBits(f);
    final boolean isNegative = (bits & SIGN_BIT_MASK_FLOAT) != 0;
    int fractBits = bits & SIGNIF_BIT_MASK_FLOAT;
    int binExp = (bits & EXP_BIT_MASK_FLOAT) >> EXP_SHIFT_FLOAT;
    // Discover obvious special cases of NaN and Infinity.
    if (binExp == EXP_BIT_MASK_FLOAT >> EXP_SHIFT_FLOAT)
      return result.error(fractBits != 0L ? NAN_REP : isNegative ? "-" + INFINITY_REP : INFINITY_REP);

    // Finish unpacking
    // Normalize denormalized numbers.
    // Insert assumed high-order bit for normalized numbers.
    // Subtract exponent bias.
    int nSignificantBits;
    if (binExp != 0) {
      fractBits |= FRACT_HOB_FLOAT;
      nSignificantBits = EXP_SHIFT_FLOAT + 1;
    }
    else if (fractBits != 0) {
      final int leadingZeros = Integer.numberOfLeadingZeros(fractBits);
      final int shift = leadingZeros - (31 - EXP_SHIFT_FLOAT);
      fractBits <<= shift;
      binExp = 1 - shift;
      nSignificantBits = 32 - leadingZeros; // recall binExp is - shift count.
    }
    else {
      // not a denorm, just a 0!
      // return isNegative ? "-0" : "0";
      return result.assign(0, (short)0);
    }

    // call the routine that actually does all the hard work.
    return toDecimal(binExp - EXP_BIAS_FLOAT, (long)fractBits << EXP_SHIFT - EXP_SHIFT_FLOAT, nSignificantBits, isNegative, null, result);
  }

  static Decimal toDecimal(final double d, final RoundingMode rm, final Decimal result) {
    final long bits = Double.doubleToRawLongBits(d);
    final boolean isNegative = (bits & SIGN_BIT_MASK_DOUBLE) != 0;
    long fractBits = bits & SIGNIF_BIT_MASK_DOUBLE;
    int exp2 = (int)((bits & EXP_BIT_MASK_DOUBLE) >> EXP_SHIFT);
    // Discover obvious special cases of NaN and Infinity.
    if (exp2 == (int)(EXP_BIT_MASK_DOUBLE >> EXP_SHIFT))
      return result.error(fractBits != 0L ? NAN_REP : isNegative ? "-" + INFINITY_REP : INFINITY_REP);

    // Finish unpacking
    // Normalize denormalized numbers.
    // Insert assumed high-order bit for normalized numbers.
    // Subtract exponent bias.
    final int nSignificantBits;
    if (exp2 != 0) {
      fractBits |= FRACT_HOB;
      nSignificantBits = EXP_SHIFT + 1;
    }
    else if (fractBits != 0L) {
      final int leadingZeros = Long.numberOfLeadingZeros(fractBits);
      final int shift = leadingZeros - (63 - EXP_SHIFT);
      fractBits <<= shift;
      exp2 = 1 - shift;
      nSignificantBits = 64 - leadingZeros; // recall binExp is - shift count.
    }
    else {
      // not a denorm, just a 0!
      // return isNegative ? "-0" : "0";
      return result.assign(0, (short)0);
    }

    // call the routine that actually does all the hard work.
    return toDecimal(exp2 - EXP_BIAS_DOUBLE, fractBits, nSignificantBits, isNegative, rm, result);
  }
}