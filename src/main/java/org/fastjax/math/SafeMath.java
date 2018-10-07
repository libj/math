/* Copyright (c) 2008 FastJAX
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

package org.fastjax.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;

public final class SafeMath {

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a base.
   * @param b the exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static BigDecimal pow(final BigDecimal a, final BigDecimal b, final MathContext mc) {
    return BigDecimalMath.pow(a, b, mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a base.
   * @param b the exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static BigDecimal pow(final BigInteger a, final BigDecimal b, final MathContext mc) {
    return BigDecimalMath.pow(new BigDecimal(a), b, mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a base.
   * @param b the exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static BigDecimal pow(final BigDecimal a, final BigInteger b, final MathContext mc) {
    return BigDecimalMath.pow(a, new BigDecimal(b), mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a base.
   * @param b the exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static BigInteger pow(final BigInteger a, final BigInteger b, final MathContext mc) {
    return BigDecimalMath.pow(new BigDecimal(a), new BigDecimal(b), mc).toBigInteger();
  }

  /**
   * Returns the closest {@code float} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a floating-point value to be rounded to the scaled float.
   * @param scale Number of digits after the decimal point at which to round.
   * @return The value of the argument rounded to the nearest {@code float}
   *         value at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static float round(final float a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return Math.round(a);

    final float pow = (float)Math.pow(10, scale);
    return Math.round(a * pow) / pow;
  }

  /**
   * Returns the closest {@code double} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a floating-point value to be rounded to the scaled double.
   * @param scale Number of digits after the decimal point at which to round.
   * @return The value of the argument rounded to the nearest {@code double}
   *         value at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static double round(final double a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return Math.round(a);

    final double pow = Math.pow(10, scale);
    return Math.round(a * pow) / pow;
  }

  /**
   * Returns the closest {@code byte} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a byte value to be rounded to the scaled byte.
   * @param scale Ignored for byte type.
   * @return The value of the argument rounded to the nearest {@code byte} value
   *         at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static byte round(final byte a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code short} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a short value to be rounded to the scaled short.
   * @param scale Ignored for short type.
   * @return The value of the argument rounded to the nearest {@code short}
   *         value at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static short round(final short a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code int} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a int value to be rounded to the scaled int.
   * @param scale Ignored for int type.
   * @return The value of the argument rounded to the nearest {@code int} value
   *         at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static int round(final int a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code long} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a long value to be rounded to the scaled long.
   * @param scale Ignored for long type.
   * @return The value of the argument rounded to the nearest {@code long} value
   *         at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static long round(final long a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code BigDecimal} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a BigDecimal value to be rounded to the scaled BigDecimal.
   * @param scale Number of digits after the decimal point at which to round.
   * @return The value of the argument rounded to the nearest {@code BigDecimal}
   *         value at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static BigDecimal round(final BigDecimal a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    return a.setScale(scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the closest {@code BigInteger} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a a BigInteger value to be rounded to the scaled BigInteger.
   * @param scale Ignored for BigInteger type.
   * @return The value of the argument rounded to the nearest {@code BigInteger}
   *         value at {@code scale}.
   * @throws IllegalArgumentException if scale is less than 0
   */
  public static BigInteger round(final BigInteger a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    return a;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the byte value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static byte signum(final byte a) {
    return a < 0 ? (byte)-1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the short value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static short signum(final short a) {
    return a < 0 ? (short)-1 : a == 0 ? 0 : (short)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the int value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static int signum(final int a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the long value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static long signum(final long a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the floating-point value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static float signum(final float a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a the floating-point value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static double signum(final double a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument {@code BigInteger}.
   *
   * @param a the {@code BigInteger} value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static int signum(final BigInteger a) {
    return a.signum();
  }

  /**
   * Returns the signum function of the argument {@code BigDecimal}.
   *
   * @param a the {@code BigDecimal} value whose signum is to be returned
   * @return The signum function of the argument
   */
  public static int signum(final BigDecimal a) {
    return a.signum();
  }

  /**
   * Returns the trigonometric sine of an angle. Special cases:
   * <ul>
   * <li>If the argument is NaN or an infinity, then the result is NaN.
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.
   * </ul>
   *
   * @param a an angle, in radians.
   * @return The sine of the argument.
   */
  public static double sin(final double a) {
    return StrictMath.sin(a);
  }

  /**
   * Returns the trigonometric sine of an angle.
   *
   * @param a an angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The sine of the argument.
   */
  public static BigDecimal sin(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.sin(a, mc);
  }

  /**
   * Returns the trigonometric sine of an angle.
   *
   * @param a an angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The sine of the argument.
   */
  public static BigDecimal sin(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.sin(new BigDecimal(a), mc);
  }

  /**
   * Returns the correctly rounded positive square root of a {@code double}
   * value. Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.
   * <li>If the argument is positive zero or negative zero, then the result is
   * the same as the argument.
   * </ul>
   * Otherwise, the result is the {@code double} value closest to the true
   * mathematical square root of the argument value.
   *
   * @param a a value.
   * @return The positive square root of {@code a}.
   */
  public static double sqrt(final double a) {
    return StrictMath.sqrt(a);
  }

  /**
   * Returns the correctly rounded positive square root of a {@code BigDecimal}
   * value.
   *
   * @param a a value.
   * @param mc The {@link MathContext} used for the result.
   * @return The positive square root of {@code a}.
   */
  public static BigDecimal sqrt(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.sqrt(a, mc);
  }

  /**
   * Returns the correctly rounded positive square root of a {@code BigInteger}
   * value.
   *
   * @param a a value.
   * @param mc The {@link MathContext} used for the result.
   * @return The positive square root of {@code a}.
   */
  public static BigDecimal sqrt(final BigInteger a, final MathContext mc) {
    return sqrt(new BigDecimal(a), mc);
  }

  /**
   * Returns the trigonometric tangent of an angle. Special cases:
   * <ul>
   * <li>If the argument is NaN or an infinity, then the result is NaN.
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.
   * </ul>
   *
   * @param a an angle, in radians.
   * @return The tangent of the argument.
   */
  public static double tan(final double a) {
    return StrictMath.tan(a);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   *
   * @param a an angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The tangent of the argument.
   */
  public static BigDecimal tan(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.tan(a, mc);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   *
   * @param a an angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The tangent of the argument.
   */
  public static BigDecimal tan(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.tan(new BigDecimal(a), mc);
  }

  /**
   * Returns the natural logarithm (base <i>e</i>) of a {@code double} value.
   * Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.
   * </ul>
   *
   * @param a a value
   * @return The value ln&nbsp;{@code a}, the natural logarithm of {@code a}.
   */
  public static double log(final double a) {
    return StrictMath.log(a);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final int b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final long b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final float b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final double b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final int b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final long b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final float b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final double b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final int b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final long b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final float b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final double b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final int b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final long b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final float b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static double log(final double b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the natural logarithm (base <i>e</i>) of a {@code BigDecimal}
   * value. Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.
   * </ul>
   *
   * @param a The value.
   * @param mathContext The {@link MathContext} used for the result.
   * @return The value ln&nbsp;{@code a}, the natural logarithm of {@code a}.
   */
  public static BigDecimal log(final BigDecimal a, final MathContext mathContext) {
    return BigDecimalMath.log(a, mathContext);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is NaN.
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.
   * <li>If {@code a} is 0, then the result is negative infinity.
   * <li>If {@code b} is 1, then the result is positive infinity.
   * <li>If {@code b} is equal to {@code a}, then the result is 1.
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mathContext The {@link MathContext} used for the result.
   * @return The value logarithm base {@code b} of {@code a}.
   */
  public static BigDecimal log(final BigDecimal b, final BigDecimal a, final MathContext mathContext) {
    return BigDecimalMath.log(a, mathContext).divide(BigDecimalMath.log(b, mathContext), mathContext);
  }

  private SafeMath() {
  }
}