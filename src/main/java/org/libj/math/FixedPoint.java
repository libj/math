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

abstract class FixedPoint extends Number {
  private static final long serialVersionUID = -2783163338875335475L;

  static final byte SCALE_BITS = 9;
  private static final byte VALUE_BITS = 63 - SCALE_BITS;
  private static final byte LONG_SHORT_SCALE_SHIFT = 63 - Short.SIZE;
  private static final byte SHORT_SCALE_SHIFT = Short.SIZE - SCALE_BITS;

  private static final long SCALE_MASK = (0xffffL << VALUE_BITS) & 0x7fffffffffffffffL;

  public static final long MIN_SIGNIFICAND = -(2L << 62 - SCALE_BITS);
  public static final long MAX_SIGNIFICAND = -MIN_SIGNIFICAND - 1;
  public static final byte MAX_PRECISION = Numbers.precision(MAX_SIGNIFICAND);

  public static final short MIN_PSCALE = -2 << SCALE_BITS - 2;
  public static final short MAX_PSCALE = -MIN_PSCALE - 1;

  static final double maxPos = 1.8014398509481983E255;        // encodeInPlace(MAX_VALUE, MIN_PSCALE, 0)
  static final double maxNeg = -1.8014398509481984E255;       // encodeInPlace(MIN_VALUE, MIN_PSCALE, 0)

  static final double minPos = 1.8014398509481983E-256;       // encodeInPlace(MAX_VALUE, MAX_PSCALE, 0)
  static final double minNeg = -1.8014398509481984E-256;      // encodeInPlace(MIN_VALUE, MAX_PSCALE, 0)
  private static final double minMan2 = 0.6762169998536515;   // Frexp.frexp(minPos).mantissa
  private static final int minExp2 = -849;                    // Frexp.frexp(minPos).exponent

  /**
   * Returns {@code true} if the specified {@code long} value can be
   * represented as a {@link Decimal}, otherwise {@code false}.
   *
   * @param significand The {@code long} value to test for representability as a
   *          {@link Decimal}.
   * @return {@code true} if the specified {@code double} value can be
   *         represented as a {@link Decimal}, otherwise {@code false}.
   */
  public static boolean isDecimal(final long significand) {
    return MIN_SIGNIFICAND <= significand && significand <= MAX_SIGNIFICAND;
  }

  /**
   * Returns {@code true} if the specified {@code double} value can be
   * represented as a {@link Decimal}, otherwise {@code false}.
   *
   * @param val The {@code double} value to test for representability as a
   *          {@link Decimal}.
   * @return {@code true} if the specified {@code double} value can be
   *         represented as a {@link Decimal}, otherwise {@code false}.
   */
  public static boolean isDecimal(final double val) {
    if (Double.isNaN(val) || Double.isInfinite(val))
      return false;

    if (val == 0 || Math.getExponent(val) == 0)
      return true;

    final long bits = Double.doubleToLongBits(val);
    int exp = (int)((bits >> FloatingDecimal.SIGNIFICAND_BITS_DOUBLE) & 0x7ffL);
    if (exp <= minExp2 + FloatingDecimal.EXP_BIAS_DOUBLE) {
      if (exp < minExp2 - 1 + FloatingDecimal.EXP_BIAS_DOUBLE)
        return false;

      long significand = bits & FloatingDecimal.SIGNIF_BIT_MASK_DOUBLE;
      if (exp == 0)
        ++exp;
      else
        significand |= 1L << FloatingDecimal.SIGNIFICAND_BITS_DOUBLE;

      exp -= FloatingDecimal.EXP_BIAS_DOUBLE + FloatingDecimal.SIGNIFICAND_BITS_DOUBLE;
      double mantissa = significand;

      // normalize
      while (mantissa > 1) {
        significand >>= 1;
        mantissa /= 2;
        ++exp;
      }

      if (exp == minExp2)
        return mantissa <= minMan2;

      if (exp == minExp2 - 1)
        return mantissa <= 1;
    }

    return val < 0 ? val <= minNeg && maxNeg <= val : val <= maxPos && minPos <= val;
  }

  /**
   * Returns the result of {@code v / 10} rounded half up.
   *
   * @param v The value.
   * @return The result of {@code v / 10} rounded half up.
   */
  static long roundHalfUp10(final long v) {
    return roundHalfUp(v % 10, v / 10);
  }

  static long roundHalfUp(final long r, final long v) {
    return r <= -5 ? v - 1 : r >= 5 ? v + 1 : v;
  }

  static long roundHalfUp(final long r) {
    return r <= -5 ? -1 : r >= 5 ? 1 : 0;
  }

  static long roundFloor10(long v) {
    final long r = v % 10;
    v /= 10;
    return r < 0 ? v - 1 : v;
  }

  static long roundCeiling10(long v) {
    final long r = v % 10;
    v /= 10;
    return r > 0 ? v + 1 : v;
  }

  static long roundUp10(long v) {
    final long r = v % 10;
    v /= 10;
    return r < 0 ? v - 1 : r > 0 ? v + 1 : v;
  }

  static long roundHalfDown10(long v) {
    final long r = v % 10;
    v /= 10;
    return r < -5 ? v - 1 : r > 5 ? v + 1 : v;
  }

  static long roundHalfEven10(long v) {
    final long r = v % 10;
    v /= 10;

    if (r == -5)
      return v % 2 == 0 ? v : v - 1;

    if (r == 5)
      return v % 2 == 0 ? v : v + 1;

    return r < -5 ? v - 1 : r > 5 ? v + 1 : v;
  }

  static long round(long value, final byte precision, final int ds, final RoundingMode rm, final long defaultValue) {
    if (rm == RoundingMode.UNNECESSARY || rm == null) {
      if (ds > precision)
        return value != 0 ? defaultValue : 0;

      final long f = FastMath.longE10[ds];
      return value % f != 0 ? defaultValue : value / f;
    }

    if (ds > precision)
      return 0;

    if (ds > 1)
      value /= FastMath.longE10[ds - 1];

    if (rm == RoundingMode.DOWN)
      return value / 10;

    if (rm == RoundingMode.UP)
      return roundUp10(value);

    if (rm == RoundingMode.FLOOR)
      return roundFloor10(value);

    if (rm == RoundingMode.CEILING)
      return roundCeiling10(value);

    if (rm == RoundingMode.HALF_UP)
      return roundHalfUp10(value);

    if (rm == RoundingMode.HALF_DOWN)
      return roundHalfDown10(value);

    if (rm == RoundingMode.HALF_EVEN)
      return roundHalfEven10(value);

    return value;
  }

  /**
   * Returns the number of bits of precision required for the representation of
   * the specified value.
   * <p>
   * <b>Note:</b> It is assumed the specified value is positive, as the
   * {@link Long#numberOfLeadingZeros(long)} returns 0 for all negative values.
   *
   * @param v The value for which to return the number of bits of precision.
   * @return The number of bits of precision required for the representation of
   *         the specified value.
   */
  static int bitLength(final long v) {
    return Long.SIZE - Long.numberOfLeadingZeros(v);
  }

  /**
   * Encodes the provided {@code significand} and {@code scale} into a
   * {@code long} with the given number of {@code scaleBits}.
   *
   * @param significand The significand.
   * @param scale The scale component.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return A {@code long} encoded decimal with the provided
   *         {@code significand} and {@code scale} with the given number of
   *         {@code scaleBits}.
   */
  public static long valueOf(final long significand, final int scale, final long defaultValue) {
    return encode(significand, Numbers.precision(significand), scale, defaultValue);
  }

  static long encode(final long significand, final byte precision, int scale, final long defaultValue) {
    if (significand < MIN_SIGNIFICAND || MAX_SIGNIFICAND < significand) {
      // System.err.println("Significand " + significand + " is outside permitted min(" + MIN_VALUE + ") max(" + MAX_VALUE + ")");
      return defaultValue;
    }

    scale -= precision;

    if (scale < MIN_PSCALE || MAX_PSCALE < scale) {
      // System.err.println("Scale " + scale + " is greater than bits allow: " + (scale < 0 ? MIN_SCALE : MAX_SCALE));
      return defaultValue;
    }

    final long dec = encodeInPlace(significand, scale);
    if (dec == defaultValue)
      throw new IllegalArgumentException("Encoded decimal (" + dec + ") conflicts with defaultValue (" + defaultValue + ")");

    return dec;
  }

  static long encodeInPlace(final long significand, final long pscale) {
    final long scaleMask = pscale << VALUE_BITS & SCALE_MASK;
    final long dec = significand < 0 ? significand ^ scaleMask : significand | scaleMask;
    // System.out.println("      scale: " + Buffers.toString(scale));
    // System.out.println("  scaleMask: " + Buffers.toString(scaleMask));
    // System.out.println("significand: " + Buffers.toString(significand));
    // System.out.println("    decimal: " + Buffers.toString(dec));
    return dec;
  }

  /**
   * Decodes the significand from the {@link Decimal#valueOf(long,int,long)
   * encoded} decimal with the given number of {@code scaleBits}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded} decimal.
   * @return The significand from the {@link Decimal#valueOf(long,int,long)
   *         encoded} decimal with the given number of {@code scaleBits}.
   * @see #valueOf(long,int,long)
   * @see #scale(long)
   */
  public static long significand(final long dec) {
    // System.out.println("decimal: " + Buffers.toString(dec));
    // System.out.println("sleMask: " + Buffers.toString(scaleMask));
    return dec < 0 ? dec | SCALE_MASK : dec & ~SCALE_MASK;
  }

  /**
   * Decodes the scale from the {@link Decimal#valueOf(long,int,long) encoded}
   * decimal with the given number of {@code scaleBits}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded} value.
   * @return The scale from the {@link Decimal#valueOf(long,int,long) encoded}
   *         decimal with the given number of {@code scaleBits}.
   * @see #valueOf(long,int,long)
   * @see #significand(long)
   */
  public static short scale(final long dec) {
    return scale(dec, significand(dec));
  }

  static short scale(final long dec, final long significand) {
    return scale(dec, Numbers.precision(significand));
  }

  static short scale(final long dec, final byte precision) {
    short scale = (short)((dec < 0 ? ~(dec | ~SCALE_MASK) : (dec & SCALE_MASK)) >> LONG_SHORT_SCALE_SHIFT);
    scale >>= SHORT_SCALE_SHIFT;
    // System.out.println("~ encoded: " + Buffers.toString(dec));
    // System.out.println("~   scale:                                                 " + Buffers.toString(scale) + " " + scale);
    return scale += precision;
  }

  static boolean checkScale(final long significand, final byte precision, final int scale, final Decimal result) {
    final int pscale = scale - precision;
    if (pscale < MIN_PSCALE) {
      result.error("Underflow");
      return false;
    }

    if (pscale > MAX_PSCALE) {
      result.error("Overflow");
      return false;
    }

    result.assign(significand, (short)scale);
    return true;
  }
}