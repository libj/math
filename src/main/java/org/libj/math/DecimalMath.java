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

import static org.libj.math.FixedPoint.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.libj.lang.Constants;
import org.libj.lang.Numbers;

import ch.obermuhlner.math.big.BigDecimalMath;

public final class DecimalMath {
  static boolean sqrt0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    if (significand == 0 || significand == 1 && scale == 0) {
      result.assign(significand, scale);
      return true;
    }

    if (significand < 0) {
      result.error("Complex value");
      return false;
    }

    // First see if we can get a whole square root
    double d = significand;
    short s = scale;
    if (s % 2 != 0) {
      d *= 10;
      ++s;
    }

    long v;
    if (significand % 2 == 0) {
      d = Math.sqrt(d);
      v = (long)d;
      if (v == d) {
        result.assign(v, s /= 2);
        return true;
      }
    }

    // Otherwise, use BigInt.sqrt()
    v = significand;
    s = scale;
    final int p1 = Numbers.precision(v) + Math.abs(s);
    final long ds = Long.MAX_VALUE / v;
    int e10;
    if (ds != 0) {
      e10 = Numbers.precision(ds) - 1;
      v *= FastMath.longE10[e10];
      s += e10;
    }
    else {
      e10 = 0;
    }

    e10 = MAX_PRECISION;
    final int p2 = Numbers.precision(v) + e10;

    // Adjust precision+scale so its modulus is equal to the original number
    if (p1 % 2 != p2 % 2)
      ++e10;

    int[] b = BigInt.valueOf(v);
    b = BigInt.mul(b, FastMath.longE10[e10]);
    b = BigInt.sqrt(b, rm == RoundingMode.HALF_UP || rm == RoundingMode.HALF_EVEN || rm == RoundingMode.UP || rm == RoundingMode.CEILING ? RoundingMode.DOWN : rm);
    if (b == null) {
      result.error("Rounding necessary");
      return false;
    }

    v = BigInt.longValue(b);
    s += e10;
    s /= 2;

    // Remove trailing zeroes
    final byte z = Numbers.trailingZeroes(v);
    if (z > 0) {
      // v = round(v, Long.MAX_VALUE, z, 0, rm);
      v /= FastMath.longE10[z];
      s -= z;
    }

    // Round v if necessary
    final long f = v / MAX_SIGNIFICAND;
    if (f > 0) {
      e10 = Numbers.precision(f);
      v = round(v, MAX_PRECISION, e10, rm, 0);
      if (v == 0) {
        result.error("Rounding necessary");
        return false;
      }

      s -= e10;
    }

    result.assign(v, s);
    return true;
  }

  /**
   * Calculates the square root of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * <code>dec<sup>1/2</sup></code>
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The square root of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long sqrt(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return sqrt0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its square root (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>dec<sup>1/2</sup></code>
   * </pre>
   *
   * Calling this method is the equivalent of:
   *
   * <pre>
   * sqrt(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its square root (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal sqrt(final Decimal dec) {
    return sqrt0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its square root with the specified {@link RoundingMode}, or {@code null}
   * if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>dec<sup>1/2</sup></code>
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its square root with the specified {@link RoundingMode}, or {@code null}
   *         if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal sqrt(final Decimal dec, final RoundingMode rm) {
    return sqrt0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  static boolean log0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    if (significand <= 0) {
      result.error(significand == 0 ? "Negative Infinity" : "Undefined");
      return false;
    }

    return Decimal.assign(result, log0(significand, scale), rm) != null;
  }

  static boolean log0(final long significand, final short scale, final double baseLog, final RoundingMode rm, final Decimal result) {
    if (significand <= 0) {
      result.error(significand == 0 ? "Negative Infinity" : "Undefined");
      return false;
    }

    return Decimal.assign(result, log0(significand, scale, baseLog), rm) != null;
  }

  private static double log0(final long significand, final short scale) {
    return DecimalNative.nativeLog(significand, scale);
  }

  private static double log0(final long significand, final short scale, final double baseLog) {
    return DecimalNative.nativeLogBase(significand, scale, baseLog);
  }

  private static double ln0(final long significand, final short scale) {
    final int digits = Numbers.precision(significand) - scale;
    if (digits < FloatingDecimal.MAX_DIGITS_10 && digits > -FloatingDecimal.MAX_DIGITS_10)
      return Math.log(FloatingDecimal.doubleValue(significand, scale));

    return Math.log(significand) - scale * Constants.LOG_10;
  }

  /**
   * Calculates the natural logarithm of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * log(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The natural logarithm of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long log(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return log0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its natural logarithm with the specified {@link RoundingMode}, or
   * {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * log(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its natural logarithm with the specified {@link RoundingMode}, or
   *         {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log(final Decimal dec, final RoundingMode rm) {
    return log0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its natural logarithm (rounded down), or {@code null} if the result
   * cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * log(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * log(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its natural logarithm (rounded down), or {@code null} if the result
   *         cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log(final Decimal dec) {
    return log0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  /**
   * Calculates the logarithm of base {@code b} of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * <code>log<sub>base</sub>(dec)</code>
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param b The base of the logarithm function.
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The logarithm base {@code b} of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long log(final long dec, final double b, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return log0(significand(dec), scale(dec), Math.log(b), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base {@code b} with the specified {@link RoundingMode}, or
   * {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * log(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param b The base of the logarithm function.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its logarithm base {@code b} with the specified {@link RoundingMode}, or
   *         {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log(final Decimal dec, final double b, final RoundingMode rm) {
    return log0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base {@code b} (rounded down), or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * log(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * log(dec, b, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param b The base of the logarithm function.
   * @return The provided {@link Decimal} set to the value of its logarithm base {@code b} (rounded down), or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log(final Decimal dec, final double b) {
    return log0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  /**
   * Calculates the logarithm base 2 of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * <code>log<sub>2</sub>(dec)</code>
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The logarithm base 2 of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long log2(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return log0(significand(dec), scale(dec), Constants.LOG_2, rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base 2 with the specified {@link RoundingMode}, or
   * {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>log<sub>2</sub>(dec)</code>
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its logarithm base 2 with the specified {@link RoundingMode}, or
   *         {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log2(final Decimal dec, final RoundingMode rm) {
    return log0(dec.significand, dec.scale, Constants.LOG_2, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base 2 (rounded down), or {@code null} if the result
   * cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>log<sub>2</sub>(dec)</code>
   * </pre>
   *
   * Calling this method is the equivalent of:
   *
   * <pre>
   * log2(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its logarithm base 2 (rounded down), or {@code null} if the result
   *         cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log2(final Decimal dec) {
    return log0(dec.significand, dec.scale, Constants.LOG_2, RoundingMode.DOWN, dec) ? dec : null;
  }

  /**
   * Calculates the logarithm base 10 of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * <code>log<sub>10</sub>(dec)</code>
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The logarithm base 10 of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long log10(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return log0(significand(dec), scale(dec), Constants.LOG_10, rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base 10 with the specified {@link RoundingMode}, or
   * {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>log<sub>10</sub>(dec)</code>
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its logarithm base 10 with the specified {@link RoundingMode}, or
   *         {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log10(final Decimal dec, final RoundingMode rm) {
    return log0(dec.significand, dec.scale, Constants.LOG_10, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its logarithm base 10 (rounded down), or {@code null} if the result
   * cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>log<sub>10</sub>(dec)</code>
   * </pre>
   *
   * Calling this method is the equivalent of:
   *
   * <pre>
   * log10(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its logarithm base 10 (rounded down), or {@code null} if the result
   *         cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal log10(final Decimal dec) {
    return log0(dec.significand, dec.scale, Constants.LOG_10, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean sin0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    // FIXME: Very narrow range for native execution
    if (scale == 0 && Math.abs(significand) < 2000) {
      final long defaultValue = Long.MIN_VALUE;
      final long dec = DecimalNative.nativeSin(significand, scale, rm.ordinal(), defaultValue);
      if (dec == defaultValue)
        return false;

      result.assign(significand(dec), scale(dec));
      return true;
    }

    final BigDecimal x = BigDecimal.valueOf(significand, scale);
    // FIXME: The precision of MathContext should be dynamic based on the scale of x
    final BigDecimal sin = BigDecimalMath.sin(x, new MathContext(34, rm));
    result.assign(sin, rm);
    return true;
  }

  /**
   * Calculates the sin of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * sin(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The sin of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long sin(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return sin0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its sin with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * sin(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its sin with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal sin(final Decimal dec, final RoundingMode rm) {
    return sin0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its sin (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * sin(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * sin(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its sin (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal sin(final Decimal dec) {
    return cos0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean cos0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    // FIXME: Very narrow range for native execution
    if (scale == 0 && Math.abs(significand) < 2000) {
      final long defaultValue = Long.MIN_VALUE;
      final long dec = DecimalNative.nativeCos(significand, scale, rm.ordinal(), defaultValue);
      if (dec == defaultValue)
        return false;

      result.assign(significand(dec), scale(dec));
      return true;
    }

    final BigDecimal x = BigDecimal.valueOf(significand, scale);
    // FIXME: The precision of MathContext should be dynamic based on the scale of x
    final BigDecimal cos = BigDecimalMath.cos(x, new MathContext(34, rm));
    result.assign(cos, rm);
    return true;
  }

  /**
   * Calculates the cos of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * cos(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The cos of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long cos(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return cos0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its cos with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * cos(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its cos with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal cos(final Decimal dec, final RoundingMode rm) {
    return cos0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its cos (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * cos(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * cos(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its cos (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal cos(final Decimal dec) {
    return sin0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean tan0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    // FIXME: Very narrow range for native execution
    if (scale == 0 && Math.abs(significand) < 2000) {
      final long defaultValue = Long.MIN_VALUE;
      final long dec = DecimalNative.nativeTan(significand, scale, rm.ordinal(), defaultValue);
      if (dec == defaultValue)
        return false;

      result.assign(significand(dec), scale(dec));
      return true;
    }

    final BigDecimal x = BigDecimal.valueOf(significand, scale);
    // FIXME: The precision of MathContext should be dynamic based on the scale of x
    final BigDecimal tan = BigDecimalMath.tan(x, new MathContext(34, rm));
    result.assign(tan, rm);
    return true;
  }

  /**
   * Calculates the tan of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * tan(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The tan of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long tan(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return tan0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its tan with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * tan(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its tan with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal tan(final Decimal dec, final RoundingMode rm) {
    return tan0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its tan (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * tan(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * tan(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its tan (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal tan(final Decimal dec) {
    return sin0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean asin0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    final long defaultValue = Long.MIN_VALUE;
    final long dec = DecimalNative.nativeAsin(significand, scale, rm.ordinal(), defaultValue);
    if (dec == defaultValue)
      return false;

    result.assign(significand(dec), scale(dec));
    return true;
  }

  /**
   * Calculates the asin of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * asin(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The asin of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long asin(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return asin0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its asin with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * asin(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its asin with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal asin(final Decimal dec, final RoundingMode rm) {
    return asin0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its asin (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * asin(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * asin(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its asin (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal asin(final Decimal dec) {
    return sin0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean acos0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    final long defaultValue = Long.MIN_VALUE;
    final long dec = DecimalNative.nativeAcos(significand, scale, rm.ordinal(), defaultValue);
    if (dec == defaultValue)
      return false;

    result.assign(significand(dec), scale(dec));
    return true;
  }

  /**
   * Calculates the acos of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * acos(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The acos of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long acos(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return acos0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its acos with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * acos(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its acos with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal acos(final Decimal dec, final RoundingMode rm) {
    return acos0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its acos (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * acos(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * acos(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its acos (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal acos(final Decimal dec) {
    return sin0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean atan0(final long significand, final short scale, final RoundingMode rm, final Decimal result) {
    final long defaultValue = Long.MIN_VALUE;
    final long dec = DecimalNative.nativeAtan(significand, scale, rm.ordinal(), defaultValue);
    if (dec == defaultValue)
      return false;

    result.assign(significand(dec), scale(dec));
    return true;
  }

  /**
   * Calculates the atan of the provided {@code dec} with the specified {@link RoundingMode}.
   *
   * <pre>
   * atan(dec)
   * </pre>
   *
   * @param dec The decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The atan of the provided {@code dec} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long atan(final long dec, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return atan0(significand(dec), scale(dec), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its atan with the specified {@link RoundingMode}, or {@code null} if the
   * result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * atan(dec)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its atan with the specified {@link RoundingMode}, or {@code null} if the
   *         result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal atan(final Decimal dec, final RoundingMode rm) {
    return atan0(dec.significand, dec.scale, rm, dec) ? dec : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its atan (rounded down), or {@code null} if the result cannot be
   * represented in {@link Decimal} encoding.
   *
   * <pre>
   * atan(dec)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * atan(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its atan (rounded down), or {@code null} if the result cannot be
   *         represented in {@link Decimal} encoding.
   */
  public static Decimal atan(final Decimal dec) {
    return sin0(dec.significand, dec.scale, RoundingMode.DOWN, dec) ? dec : null;
  }

  private static boolean atan20(final long significandY, final short scaleY, final long significandX, final short scaleX, final RoundingMode rm, final Decimal result) {
    // FIXME: This is not really working. Need to figure out the bounds.
    if (scaleY == 0 && Math.abs(significandX) < 2000 && Math.abs(significandY) < 2000) {
      final long defaultValue = Long.MIN_VALUE;
      final long dec = DecimalNative.nativeAtan2(significandY, scaleY, significandX, scaleX, rm.ordinal(), defaultValue);
      if (dec == defaultValue)
        return false;

      result.assign(significand(dec), scale(dec));
      return true;
    }

    final BigDecimal x = BigDecimal.valueOf(significandX, scaleX);
    final BigDecimal y = BigDecimal.valueOf(significandY, scaleY);
    // FIXME: The precision of MathContext should be dynamic based on the scale of x & y
    final BigDecimal atan2 = BigDecimalMath.atan2(y, x, new MathContext(34, rm));
    result.assign(atan2, rm);
    return true;
  }

  /**
   * Calculates the atan2 of the provided {@code decY} and {@code decX} with the specified {@link RoundingMode}.
   *
   * <pre>
   * atan2(decX, decY)
   * </pre>
   *
   * @param decX The X decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param decY The Y decimal (encoded with {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The atan2 of the provided {@code decY} and {@code decX} with the specified {@link RoundingMode}.
   * @see Decimal#valueOf(long,int,long)
   */
  public static long atan2(final long decY, final long decX, final RoundingMode rm, final long defaultValue) {
    final Decimal result = new Decimal();
    return atan20(significand(decY), scale(decY), significand(decX), scale(decX), rm, result) ? result.encode(defaultValue) : defaultValue;
  }

  /**
   * Returns the provided X {@link Decimal} set to the value of its atan2 with the provided Y {@link Decimal} and specified
   * {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * decX = atan2(decX, decY)
   * </pre>
   *
   * @param decX The X {@link Decimal}.
   * @param decY The Y {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} set to the value of its atan2 with the provided Y {@link Decimal} and specified
   *         {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal atan2(final Decimal decY, final Decimal decX, final RoundingMode rm) {
    return atan20(decY.significand, decY.scale, decX.significand, decX.scale, rm, decX) ? decX : null;
  }

  /**
   * Returns the provided {@link Decimal} set to the value of its atan2 with the provided Y {@link Decimal} (rounded down), or
   * {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * decX = atan2(decX, decY)
   * </pre>
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * decX = atan2(decX, decY, RoundingMode.DOWN)
   * </pre>
   *
   * @param decX The X {@link Decimal}.
   * @param decY The Y {@link Decimal}.
   * @return The provided {@link Decimal} set to the value of its atan2 with the provided Y {@link Decimal} (rounded down), or
   *         {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal atan2(final Decimal decY, final Decimal decX) {
    return atan20(decY.significand, decY.scale, decX.significand, decX.scale, RoundingMode.DOWN, decX) ? decX : null;
  }

  /**
   * Calculates the value of the provided {@code decX} raised to the provided power {@link Decimal} {@code powY} and specified
   * {@link RoundingMode}.
   *
   * <pre>
   *  <code>decX = decX<sup>decY</sup></code>
   * </pre>
   *
   * @param decX The base.
   * @param decY The power.
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The value of the provided {@code decX} raised to the provided power {@link Decimal} {@code powY} and specified
   *         {@link RoundingMode}.
   */
  public static long pow(final long decX, final long decY, final RoundingMode rm, final long defaultValue) {
    final BigDecimal result = BigDecimalMath.pow(Decimal.toBigDecimal(decX), Decimal.toBigDecimal(decY), new MathContext(34, rm));
    return Decimal.valueOf(result, defaultValue);
  }

  /**
   * Returns the provided {@link Decimal} {@code decX} set to its value raised to the provided power {@link Decimal} {@code powY} and
   * specified {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code>decX = decX<sup>decY</sup></code>
   * </pre>
   *
   * @param decX The base {@link Decimal}.
   * @param decY The power {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} {@code decX} set to its value raised to the provided power {@link Decimal} {@code powY} and
   *         specified {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal pow(final Decimal decX, final Decimal decY, final RoundingMode rm) {
    final BigDecimal result = BigDecimalMath.pow(decX.toBigDecimal(), decY.toBigDecimal(), new MathContext(34, rm));
    return decX.assign(result);
  }

  /**
   * Returns the provided {@link Decimal} {@code decX} set to its value raised to the provided power {@link Decimal} {@code powY},
   * rounded down, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   * <code>decX = decX<sup>decY</sup></code>
   * </pre>
   *
   * Calling this method is the equivalent of:
   *
   * <pre>
   * decX = pow(decX, decY, RoundingMode.DOWN)
   * </pre>
   *
   * @param decX The base {@link Decimal}.
   * @param decY The power {@link Decimal}.
   * @return The provided {@link Decimal} {@code decX} set to its value raised to the provided power {@link Decimal} {@code powY},
   *         rounded down, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal pow(final Decimal decX, final Decimal decY) {
    return pow(decX, decY, RoundingMode.DOWN);
  }

  /**
   * Calculates the natural exponent of the provided {@code dec} and specified {@link RoundingMode}.
   *
   * <pre>
   * <code><i>e</i><sup>dec</sup></code>
   * </pre>
   *
   * @param dec The power.
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be represented in {@link Decimal} encoding.
   * @return The value of the provided {@code decX} raised to the provided power {@link Decimal} {@code powY} and specified
   *         {@link RoundingMode}.
   */
  public static long exp(final long dec, final RoundingMode rm, final long defaultValue) {
    final BigDecimal result = BigDecimalMath.exp(Decimal.toBigDecimal(dec), new MathContext(34, rm));
    return Decimal.valueOf(result, defaultValue);
  }

  /**
   * Returns the provided {@link Decimal} {@code dec} set to the value of <i>e</i> raised to the provided power of the {@link Decimal}
   * {@code dec} and specified {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code><i>e</i><sup>dec</sup></code>
   * </pre>
   *
   * @param dec The power {@link Decimal}.
   * @param rm The {@link RoundingMode}.
   * @return The provided {@link Decimal} {@code dec} set to the value of <i>e</i> raised to the provided power of the {@link Decimal}
   *         {@code dec} and specified {@link RoundingMode}, or {@code null} if the result cannot be represented in {@link Decimal}
   *         encoding.
   */
  public static Decimal exp(final Decimal dec, final RoundingMode rm) {
    final BigDecimal result = BigDecimalMath.exp(dec.toBigDecimal(), new MathContext(34, rm));
    return dec.assign(result);
  }

  /**
   * Returns the provided {@link Decimal} {@code dec} set to the value of <i>e</i> raised to the provided power of the {@link Decimal}
   * {@code dec} (rounded down), or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   *
   * <pre>
   *  <code><i>e</i><sup>dec</sup></code>
   * </pre>
   *
   * Calling this method is the equivalent of:
   *
   * <pre>
   * dec = exp(dec, RoundingMode.DOWN)
   * </pre>
   *
   * @param dec The power {@link Decimal}.
   * @return The provided {@link Decimal} {@code dec} set to the value of <i>e</i> raised to the provided power of the {@link Decimal}
   *         {@code dec} (rounded down), or {@code null} if the result cannot be represented in {@link Decimal} encoding.
   */
  public static Decimal exp(final Decimal dec) {
    return exp(dec, RoundingMode.DOWN);
  }

  private DecimalMath() {
  }
}