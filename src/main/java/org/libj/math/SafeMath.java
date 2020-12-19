/* Copyright (c) 2008 Seva Safris, LibJ
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.libj.lang.BigDecimals;

import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Utility that supplements functions in {@link Math} by providing compiler-safe
 * implementations for common math functions. Compiler-safe methods are those
 * that are linked directly by the compiler based on exact argument match.
 */
public final class SafeMath {
  /**
   * Returns the absolute value of a {@code byte} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static short abs(final byte a) {
    return a < 0 ? (short)-a : a;
  }

  /**
   * Returns the absolute value of a {@code short} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static short abs(final short a) {
    return a < 0 ? (short)-a : a;
  }

  /**
   * Returns the absolute value of an {@code int} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static int abs(final int a) {
    return Math.abs(a);
  }

  /**
   * Returns the absolute value of a {@code long} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static long abs(final long a) {
    return Math.abs(a);
  }

  /**
   * Returns the absolute value of a {@code float} value. If the argument is not
   * negative, the argument is returned. If the argument is negative, the
   * negation of the argument is returned.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is positive zero or negative zero, the result is
   * positive zero.</li>
   * <li>If the argument is infinite, the result is positive infinity.</li>
   * <li>If the argument is {@code NaN}, the result is {@code NaN}.</li>
   * </ul>
   * In other words, the result is the same as the value of the expression:
   * <p>
   * {@code Float.intBitsToFloat(0x7fffffff & Float.floatToIntBits(a))}
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static float abs(final float a) {
    return Math.abs(a);
  }

  /**
   * Returns the absolute value of a {@code double} value. If the argument is
   * not negative, the argument is returned. If the argument is negative, the
   * negation of the argument is returned.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is positive zero or negative zero, the result is
   * positive zero.</li>
   * <li>If the argument is infinite, the result is positive infinity.</li>
   * <li>If the argument is {@code NaN}, the result is {@code NaN}.</li>
   * </ul>
   * In other words, the result is the same as the value of the expression:
   * <p>
   * {@code Double.longBitsToDouble((Double.doubleToLongBits(a)<<1)>>>1)}
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static double abs(final double a) {
    return Math.abs(a);
  }

  /**
   * Returns the absolute value of a {@link BigInteger} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static BigInteger abs(final BigInteger a) {
    return a.abs();
  }

  /**
   * Returns a {@link BigDecimal} whose value is the absolute value of this
   * {@link BigDecimal}, and whose scale is {@code this.scale()}.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return A {@link BigDecimal} whose value is the absolute value of this
   *         {@link BigDecimal}, and whose scale is {@code this.scale()}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static BigDecimal abs(final BigDecimal a) {
    return a.abs();
  }

  /**
   * Returns the arc cosine of a value; the returned angle is in the range
   * {@code 0.0} through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * </ul>
   *
   * @param a The value, whose arc cosine is to be returned.
   * @return The arc cosine of the argument.
   */
  public static double acos(final double a) {
    return StrictMath.acos(a);
  }

  /**
   * Returns the arc cosine of a value; the returned angle is in the range
   * {@code 0.0} through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * </ul>
   *
   * @param a The value, whose arc cosine is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc cosine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal acos(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.acos(a, mc);
  }

  /**
   * Returns the arc cosine of a value; the returned angle is in the range
   * {@code 0.0} through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * </ul>
   *
   * @param a The value, whose arc cosine is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc cosine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal acos(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.acos(new BigDecimal(a), mc);
  }

  /**
   * Returns the arc sine of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc sine is to be returned.
   * @return The arc sine of the argument.
   */
  public static double asin(final double a) {
    return StrictMath.asin(a);
  }

  /**
   * Returns the arc sine of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc sine is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc sine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal asin(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.asin(a, mc);
  }

  /**
   * Returns the arc sine of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or its absolute value is greater than
   * {@code 1}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc sine is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc sine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal asin(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.asin(new BigDecimal(a), mc);
  }

  /**
   * Returns the arc tangent of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc tangent is to be returned.
   * @return The arc tangent of the argument.
   */
  public static double atan(final double a) {
    return StrictMath.atan(a);
  }

  /**
   * Returns the arc tangent of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc tangent is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc tangent of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal atan(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.atan(a, mc);
  }

  /**
   * Returns the arc tangent of a value; the returned angle is in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN}, then the result is {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a The value, whose arc tangent is to be returned.
   * @param mc The {@link MathContext} used for the result.
   * @return The arc tangent of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal atan(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.atan(new BigDecimal(a), mc);
  }

  /**
   * Returns the angle <i>theta</i> from the conversion of rectangular
   * coordinates ({@code x}, {@code y}) to polar coordinates (r, <i>theta</i>).
   * This method computes the phase <i>theta</i> by computing an arc tangent of
   * {@code y/x} in the range of -<i>pi</i> to <i>pi</i>.
   * <p>
   * Special cases:
   * <ul>
   * <li>If either argument is {@code NaN}, then the result is {@code NaN}.</li>
   * <li>If the first argument is positive zero and the second argument is
   * positive, or the first argument is positive and finite and the second
   * argument is positive infinity, then the result is positive zero.</li>
   * <li>If the first argument is negative zero and the second argument is
   * positive, or the first argument is negative and finite and the second
   * argument is positive infinity, then the result is negative zero.</li>
   * <li>If the first argument is positive zero and the second argument is
   * negative, or the first argument is positive and finite and the second
   * argument is negative infinity, then the result is the {@code double} value
   * closest to <i>pi</i>.</li>
   * <li>If the first argument is negative zero and the second argument is
   * negative, or the first argument is negative and finite and the second
   * argument is negative infinity, then the result is the {@code double} value
   * closest to -<i>pi</i>.</li>
   * <li>If the first argument is positive and the second argument is positive
   * zero or negative zero, or the first argument is positive infinity and the
   * second argument is finite, then the result is the {@code double} value
   * closest to <i>pi</i>/2.</li>
   * <li>If the first argument is negative and the second argument is positive
   * zero or negative zero, or the first argument is negative infinity and the
   * second argument is finite, then the result is the {@code double} value
   * closest to -<i>pi</i>/2.</li>
   * <li>If both arguments are positive infinity, then the result is the
   * {@code double} value closest to <i>pi</i>/4.</li>
   * <li>If the first argument is positive infinity and the second argument is
   * negative infinity, then the result is the {@code double} value closest to
   * 3*<i>pi</i>/4.</li>
   * <li>If the first argument is negative infinity and the second argument is
   * positive infinity, then the result is the {@code double} value closest to
   * -<i>pi</i>/4.</li>
   * <li>If both arguments are negative infinity, then the result is the
   * {@code double} value closest to -3*<i>pi</i>/4.
   * </ul>
   *
   * @param y The ordinate coordinate.
   * @param x The abscissa coordinate.
   * @return The <i>theta</i> component of the point (<i>r</i>, <i>theta</i>) in
   *         polar coordinates that corresponds to the point (<i>x</i>,
   *         <i>y</i>) in Cartesian coordinates.
   */
  public static double atan2(final double y, final double x) {
    return StrictMath.atan2(y, x);
  }

  /**
   * Returns the angle <i>theta</i> from the conversion of rectangular
   * coordinates ({@code x}, {@code y}) to polar coordinates (r, <i>theta</i>).
   * This method computes the phase <i>theta</i> by computing an arc tangent of
   * {@code y/x} in the range of -<i>pi</i> to <i>pi</i>.
   * <p>
   * Special cases:
   * <ul>
   * <li>If either argument is {@code NaN}, then the result is {@code NaN}.</li>
   * <li>If the first argument is positive zero and the second argument is
   * positive, or the first argument is positive and finite and the second
   * argument is positive infinity, then the result is positive zero.</li>
   * <li>If the first argument is negative zero and the second argument is
   * positive, or the first argument is negative and finite and the second
   * argument is positive infinity, then the result is negative zero.</li>
   * <li>If the first argument is positive zero and the second argument is
   * negative, or the first argument is positive and finite and the second
   * argument is negative infinity, then the result is the {@code double} value
   * closest to <i>pi</i>.</li>
   * <li>If the first argument is negative zero and the second argument is
   * negative, or the first argument is negative and finite and the second
   * argument is negative infinity, then the result is the {@code double} value
   * closest to -<i>pi</i>.</li>
   * <li>If the first argument is positive and the second argument is positive
   * zero or negative zero, or the first argument is positive infinity and the
   * second argument is finite, then the result is the {@code double} value
   * closest to <i>pi</i>/2.</li>
   * <li>If the first argument is negative and the second argument is positive
   * zero or negative zero, or the first argument is negative infinity and the
   * second argument is finite, then the result is the {@code double} value
   * closest to -<i>pi</i>/2.</li>
   * <li>If both arguments are positive infinity, then the result is the
   * {@code double} value closest to <i>pi</i>/4.</li>
   * <li>If the first argument is positive infinity and the second argument is
   * negative infinity, then the result is the {@code double} value closest to
   * 3*<i>pi</i>/4.</li>
   * <li>If the first argument is negative infinity and the second argument is
   * positive infinity, then the result is the {@code double} value closest to
   * -<i>pi</i>/4.</li>
   * <li>If both arguments are negative infinity, then the result is the
   * {@code double} value closest to -3*<i>pi</i>/4.</li>
   * </ul>
   *
   * @param y The ordinate coordinate.
   * @param x The abscissa coordinate.
   * @param mc The {@link MathContext} used for the result.
   * @return The <i>theta</i> component of the point (<i>r</i>, <i>theta</i>) in
   *         polar coordinates that corresponds to the point (<i>x</i>,
   *         <i>y</i>) in Cartesian coordinates.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code y}, {@code x}, or the
   *           {@link MathContext} is null.
   */
  public static BigDecimal atan2(final BigDecimal y, final BigDecimal x, final MathContext mc) {
    return BigDecimalMath.atan2(y, x, mc);
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code byte} value that
   * is greater than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) byte value that is
   *         greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static byte ceil(final byte a) {
    return a;
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code short} value
   * that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) short value that is
   *         greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static short ceil(final short a) {
    return a;
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code int} value that
   * is greater than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) int value that is
   *         greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static int ceil(final int a) {
    return a;
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code long} value that
   * is greater than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) long value that is
   *         greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static long ceil(final long a) {
    return a;
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code double} value
   * that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is {@code NaN} or an infinity or positive zero or
   * negative zero, then the result is the same as the argument.</li>
   * <li>If the argument value is less than zero but greater than -1.0, then the
   * result is negative zero.</li>
   * </ul>
   * Note that the value of {@code StrictMath.ceil(x)} is exactly the value of
   * {@code -StrictMath.floor(-x)}.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) floating-point value
   *         that is greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static float ceil(final float a) {
    return (float)StrictMath.ceil(a);
  }

  /**
   * Returns the smallest (closest to negative infinity) {@code double} value
   * that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is {@code NaN} or an infinity or positive zero or
   * negative zero, then the result is the same as the argument.</li>
   * <li>If the argument value is less than zero but greater than -1.0, then the
   * result is negative zero.</li>
   * </ul>
   * Note that the value of {@code StrictMath.ceil(x)} is exactly the value of
   * {@code -StrictMath.floor(-x)}.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) floating-point value
   *         that is greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static double ceil(final double a) {
    return StrictMath.ceil(a);
  }

  /**
   * Returns the smallest (closest to negative infinity) {@link BigInteger}
   * value that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) BigInteger value that
   *         is greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static BigInteger ceil(final BigInteger a) {
    return a;
  }

  /**
   * Returns the {@link Decimal} set to the smallest value (closest to negative
   * infinity) that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   *
   * @param a The value.
   * @return The {@link Decimal} set to the smallest value (closest to negative
   *         infinity) that is greater than or equal to the argument and is
   *         equal to a mathematical integer.
   */
  public static Decimal ceil(final Decimal a) {
    return a.setScale((short)0, RoundingMode.CEILING);
  }

  /**
   * Returns the smallest (closest to negative infinity) {@link BigDecimal}
   * value that is greater than or equal to the argument and is equal to a
   * mathematical integer.
   *
   * @param a The value.
   * @return The smallest (closest to negative infinity) BigDecimal value that
   *         is greater than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static BigDecimal ceil(final BigDecimal a) {
    return a.setScale(0, RoundingMode.CEILING);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or an infinity, then the result is
   * {@code NaN}.</li>
   * </ul>
   *
   * @param a An angle, in radians.
   * @return The cosine of the argument.
   */
  public static double cos(final double a) {
    return StrictMath.cos(a);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The cosine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal cos(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.cos(a, mc);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The cosine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal cos(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.cos(new BigDecimal(a), mc);
  }

  /**
   * Returns Euler's number <i>e</i> raised to the power of a {@code double}
   * value.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN}, the result is {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is negative infinity, then the result is positive
   * zero.</li>
   * </ul>
   *
   * @param a The exponent to raise <i>e</i> to.
   * @return The value <i>e</i><sup>{@code a}</sup>, where <i>e</i> is the base
   *         of the natural logarithms.
   */
  public static double exp(final double a) {
    return StrictMath.exp(a);
  }

  /**
   * Returns Euler's number <i>e</i> raised to the power of a {@link BigDecimal}
   * value.
   * <p>
   * Special cases:
   *
   * @param a The exponent to raise <i>e</i> to.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <i>e</i><sup>{@code a}</sup>, where <i>e</i> is the base
   *         of the natural logarithms.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal exp(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.exp(a, mc);
  }

  /**
   * Returns Euler's number <i>e</i> raised to the power of a {@link BigInteger}
   * value.
   * <p>
   * Special cases:
   *
   * @param a The exponent to raise <i>e</i> to.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <i>e</i><sup>{@code a}</sup>, where <i>e</i> is the base
   *         of the natural logarithms.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the value or {@link MathContext} is null.
   */
  public static BigDecimal exp(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.exp(new BigDecimal(a), mc);
  }

  /**
   * Returns the largest (closest to positive infinity) {@code byte} value that
   * is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) byte value that less
   *         than or equal to the argument and is equal to a mathematical
   *         integer.
   */
  public static byte floor(final byte a) {
    return a;
  }

  /**
   * Returns the largest (closest to positive infinity) {@code short} value that
   * is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) short value that less
   *         than or equal to the argument and is equal to a mathematical
   *         integer.
   */
  public static short floor(final short a) {
    return a;
  }

  /**
   * Returns the largest (closest to positive infinity) {@code int} value that
   * is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) int value that less than
   *         or equal to the argument and is equal to a mathematical integer.
   */
  public static int floor(final int a) {
    return a;
  }

  /**
   * Returns the largest (closest to positive infinity) {@code long} value that
   * is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) long value that less
   *         than or equal to the argument and is equal to a mathematical
   *         integer.
   */
  public static long floor(final long a) {
    return a;
  }

  /**
   * Returns the largest (closest to positive infinity) {@code double} value
   * that is less than or equal to the argument and is equal to a mathematical
   * integer.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is {@code NaN} or an infinity or positive zero or
   * negative zero, then the result is the same as the argument.</li>
   * </ul>
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) floating-point value
   *         that less than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static float floor(final float a) {
    return (float)StrictMath.floor(a);
  }

  /**
   * Returns the largest (closest to positive infinity) {@code double} value
   * that is less than or equal to the argument and is equal to a mathematical
   * integer.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is {@code NaN} or an infinity or positive zero or
   * negative zero, then the result is the same as the argument.</li>
   * </ul>
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) floating-point value
   *         that less than or equal to the argument and is equal to a
   *         mathematical integer.
   */
  public static double floor(final double a) {
    return StrictMath.floor(a);
  }

  /**
   * Returns the provided {@link Decimal} set to the largest value (closest to
   * positive infinity) that is less than or equal to the argument and is equal
   * to a mathematical integer.
   *
   * @param a The value.
   * @return The provided {@link Decimal} set to the largest value (closest to
   *         positive infinity) that is less than or equal to the argument and
   *         is equal to a mathematical integer.
   */
  public static Decimal floor(final Decimal a) {
    return a.setScale((short)0, RoundingMode.FLOOR);
  }

  /**
   * Returns the largest (closest to positive infinity) {@link BigDecimal} value
   * that is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) BigDecimal value that
   *         less than or equal to the argument and is equal to a mathematical
   *         integer.
   */
  public static BigDecimal floor(final BigDecimal a) {
    return a.setScale(0, RoundingMode.FLOOR);
  }

  /**
   * Returns the largest (closest to positive infinity) {@link BigInteger} value
   * that is less than or equal to the argument and is equal to a mathematical
   * integer.
   *
   * @param a The value.
   * @return The largest (closest to positive infinity) BigInteger value that
   *         less than or equal to the argument and is equal to a mathematical
   *         integer.
   */
  public static BigInteger floor(final BigInteger a) {
    return a;
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the second argument is positive or negative zero, then the result is
   * 1.0.</li>
   * <li>If the second argument is {@code 1.0}, then the result is the same as
   * the first argument.</li>
   * <li>If the second argument is {@code NaN}, then the result is
   * {@code NaN}.</li>
   * <li>If the first argument is {@code NaN} and the second argument is
   * nonzero, then the result is {@code NaN}.</li>
   * <li>If
   * <ul>
   * <li>the absolute value of the first argument is greater than {@code 1} and
   * the second argument is positive infinity, or</li>
   * <li>the absolute value of the first argument is less than {@code 1} and the
   * second argument is negative infinity,</li>
   * </ul>
   * then the result is positive infinity.</li>
   * <li>If
   * <ul>
   * <li>the absolute value of the first argument is greater than {@code 1} and
   * the second argument is negative infinity, or</li>
   * <li>the absolute value of the first argument is less than {@code 1} and the
   * second argument is positive infinity,</li>
   * </ul>
   * then the result is positive zero.</li>
   * <li>If the absolute value of the first argument equals {@code 1} and the
   * second argument is infinite, then the result is {@code NaN}.</li>
   * <li>If
   * <ul>
   * <li>the first argument is positive zero and the second argument is greater
   * than zero, or</li>
   * <li>the first argument is positive infinity and the second argument is less
   * than zero,</li>
   * </ul>
   * then the result is positive zero.</li>
   * <li>If
   * <ul>
   * <li>the first argument is positive zero and the second argument is less
   * than zero, or</li>
   * <li>the first argument is positive infinity and the second argument is
   * greater than zero,</li>
   * </ul>
   * then the result is positive infinity.</li>
   * <li>If
   * <ul>
   * <li>the first argument is negative zero and the second argument is greater
   * than zero but not a finite odd integer, or</li>
   * <li>the first argument is negative infinity and the second argument is less
   * than zero but not a finite odd integer,</li>
   * </ul>
   * then the result is positive zero.</li>
   * <li>If
   * <ul>
   * <li>the first argument is negative zero and the second argument is a
   * positive finite odd integer, or</li>
   * <li>the first argument is negative infinity and the second argument is a
   * negative finite odd integer,</li>
   * </ul>
   * then the result is negative zero.</li>
   * <li>If
   * <ul>
   * <li>the first argument is negative zero and the second argument is less
   * than zero but not a finite odd integer, or</li>
   * <li>the first argument is negative infinity and the second argument is
   * greater than zero but not a finite odd integer,</li>
   * </ul>
   * then the result is positive infinity.</li>
   * <li>If
   * <ul>
   * <li>the first argument is negative zero and the second argument is a
   * negative finite odd integer, or</li>
   * <li>the first argument is negative infinity and the second argument is a
   * positive finite odd integer,</li>
   * </ul>
   * then the result is negative infinity.</li>
   * <li>If the first argument is finite and less than zero
   * <ul>
   * <li>if the second argument is a finite even integer, the result is equal to
   * the result of raising the absolute value of the first argument to the power
   * of the second argument</li>
   * <li>if the second argument is a finite odd integer, the result is equal to
   * the negative of the result of raising the absolute value of the first
   * argument to the power of the second argument</li>
   * <li>if the second argument is finite and not an integer, then the result is
   * {@code NaN}.</li>
   * </ul>
   * <li>If both arguments are integers, then the result is exactly equal to the
   * mathematical result of raising the first argument to the power of the
   * second argument if that result can in fact be represented exactly as a
   * {@code double} value.</li>
   * </ul>
   * <p>
   * (In the foregoing descriptions, a floating-point value is considered to be
   * an integer if and only if it is finite and a fixed point of the method
   * {@link #ceil} or, equivalently, a fixed point of the method {@link #floor}.
   * A value is a fixed point of a one-argument method if and only if the result
   * of applying the method to the value is equal to the value.)
   *
   * @param a The base.
   * @param b The exponent.
   * @return The value {@code a}<sup>{@code b}</sup>.
   */
  public static double pow(final double a, final double b) {
    return StrictMath.pow(a, b);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a The base.
   * @param b The exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   * @throws ArithmeticException If {@code b} is negative and the result is
   *           inexact but the rounding mode is {@link RoundingMode#UNNECESSARY}
   *           or {@code mc.precision == 0} and the quotient has a
   *           non-terminating decimal expansion.
   * @throws ArithmeticException If the rounding mode is
   *           {@link RoundingMode#UNNECESSARY} and the {@link BigDecimal}
   *           operation would require rounding.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal pow(final BigDecimal a, final BigDecimal b, final MathContext mc) {
    return BigDecimalMath.pow(a, b, mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a The base.
   * @param b The exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   * @throws ArithmeticException If {@code b} is negative and the result is
   *           inexact but the rounding mode is {@link RoundingMode#UNNECESSARY}
   *           or {@code mc.precision == 0} and the quotient has a
   *           non-terminating decimal expansion.
   * @throws ArithmeticException If the rounding mode is
   *           {@link RoundingMode#UNNECESSARY} and the {@link BigDecimal}
   *           operation would require rounding.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal pow(final BigInteger a, final BigDecimal b, final MathContext mc) {
    return BigDecimalMath.pow(new BigDecimal(a), b, mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a The base.
   * @param b The exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   * @throws ArithmeticException If {@code b} is negative and the result is
   *           inexact but the rounding mode is {@link RoundingMode#UNNECESSARY}
   *           or {@code mc.precision == 0} and the quotient has a
   *           non-terminating decimal expansion.
   * @throws ArithmeticException If the rounding mode is
   *           {@link RoundingMode#UNNECESSARY} and the {@link BigDecimal}
   *           operation would require rounding.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal pow(final BigDecimal a, final BigInteger b, final MathContext mc) {
    return BigDecimalMath.pow(a, new BigDecimal(b), mc);
  }

  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   *
   * @param a The base.
   * @param b The exponent.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code a}<sup>{@code b}</sup>.
   * @throws ArithmeticException If {@code b} is negative and the result is
   *           inexact but the rounding mode is {@link RoundingMode#UNNECESSARY}
   *           or {@code mc.precision == 0} and the quotient has a
   *           non-terminating decimal expansion.
   * @throws ArithmeticException If the rounding mode is
   *           {@link RoundingMode#UNNECESSARY} and the {@link BigDecimal}
   *           operation would require rounding.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigInteger pow(final BigInteger a, final BigInteger b, final MathContext mc) {
    return BigDecimalMath.pow(new BigDecimal(a), new BigDecimal(b), mc).toBigInteger();
  }

  /**
   * Returns the rounded value of the specified {@code float} based on the
   * rounding policy of the provided {@link RoundingMode}.
   *
   * @param a A floating-point value to be rounded to the scaled {@code float}.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@code float} based on the
   *         rounding policy of the provided {@link RoundingMode}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static float round(float a, final RoundingMode rm) {
    final int b = (int)a;
    if (rm == RoundingMode.HALF_UP) {
      if (b == a)
        return a;

      a -= b;
      return a <= -.5 ? b - 1 : a >= .5 ? b + 1 : b;
    }

    if (rm == RoundingMode.DOWN)
      return b;

    if (rm == RoundingMode.FLOOR)
      return a > 0 || b == a ? b : b - 1;

    if (rm == RoundingMode.UP)
      return b == a ? b : a < 0 ? b - 1 : b + 1;

    if (rm == RoundingMode.CEILING)
      return a < 0 || b == a ? b : b + 1;

    if (rm == RoundingMode.HALF_DOWN) {
      if (b == a)
        return a;

      a -= b;
      return a < -.5 ? b - 1 : a > .5 ? b + 1 : b;
    }

    if (rm == RoundingMode.HALF_EVEN) {
      if (b == a)
        return a;

      a -= b;
      if (a == -.5)
        return b % 2 == 0 ? b : b - 1;

      if (a == .5)
        return b % 2 == 0 ? b : b + 1;

      return a < -.5 ? b - 1 : a > .5 ? b + 1 : b;
    }

    if ((rm == RoundingMode.UNNECESSARY || rm == null) && b != a)
      return Float.NaN;

    return b;
  }

  /**
   * Returns the rounded value of the specified {@code float} based on the
   * rounding policy of the provided {@link RoundingMode} with ties at the given
   * {@code scale}.
   *
   * @param a A floating-point value to be rounded to the scaled float.
   * @param scale The number of digits after the decimal at which to round.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@code float} based on the
   *         rounding policy of the provided {@link RoundingMode} with ties at
   *         the given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static float round(final float a, final int scale, final RoundingMode rm) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return round(a, rm);

    final double pow = FastMath.doubleE10(scale);
    return (float)(round(a * pow, rm) / pow);
  }

  /**
   * Returns the rounded value of the specified {@code float} based on the
   * rounding policy of {@link RoundingMode#HALF_UP} with ties at the given
   * {@code scale}.
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * SafeMath.round(a, scale, RoundingMode.HALF_UP)
   * </pre>
   *
   * @param a A floating-point value to be rounded to the scaled float.
   * @param scale The number of digits after the decimal at which to round.
   * @return The rounded value of the specified {@code float} based on the
   *         rounding policy of {@link RoundingMode#HALF_UP} with ties at the
   *         given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static float round(final float a, final int scale) {
    return round(a, scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the rounded value of the specified {@code double} based on the
   * rounding policy of the provided {@link RoundingMode}.
   *
   * @param a A floating-point value to be rounded to the scaled {@code double}.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@code double} based on the
   *         rounding policy of the provided {@link RoundingMode}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static double round(double a, final RoundingMode rm) {
    final long c = (long)a;
    if (rm == RoundingMode.HALF_UP) {
      if (c == a)
        return a;

      a -= c;
      return a <= -.5 ? c - 1 : a >= .5 ? c + 1 : c;
    }

    if (rm == RoundingMode.DOWN)
      return c;

    if (rm == RoundingMode.FLOOR)
      return a > 0 || c == a ? c : c - 1;

    if (rm == RoundingMode.UP)
      return c == a ? c : a < 0 ? c - 1 : c + 1;

    if (rm == RoundingMode.CEILING)
      return a < 0 || c == a ? c : c + 1;

    if (rm == RoundingMode.HALF_DOWN) {
      if (c == a)
        return a;

      a -= c;
      return a < -.5 ? c - 1 : a > .5 ? c + 1 : c;
    }

    if (rm == RoundingMode.HALF_EVEN) {
      if (c == a)
        return a;

      a -= c;
      if (a == -.5)
        return c % 2 == 0 ? c : c - 1;

      if (a == .5)
        return c % 2 == 0 ? c : c + 1;

      return a < -.5 ? c - 1 : a > .5 ? c + 1 : c;
    }

    if ((rm == RoundingMode.UNNECESSARY || rm == null) && c != a)
      return Double.NaN;

    return c;
  }

  /**
   * Returns the rounded value of the specified {@code double} based on the
   * rounding policy of the provided {@link RoundingMode} with ties at the given
   * {@code scale}.
   *
   * @param a A floating-point value to be rounded to the scaled double.
   * @param scale The number of digits after the decimal at which to round.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@code double} based on the
   *         rounding policy of the provided {@link RoundingMode} with ties at
   *         the given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static double round(final double a, final int scale, final RoundingMode rm) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return round(a, rm);

    final double pow = FastMath.doubleE10(scale);
    return round(a * pow, rm) / pow;
  }

  /**
   * Returns the rounded value of the specified {@code double} based on the
   * rounding policy of {@link RoundingMode#HALF_UP} with ties at the given
   * {@code scale}.
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * SafeMath.round(a, scale, RoundingMode.HALF_UP)
   * </pre>
   *
   * @param a A floating-point value to be rounded to the scaled double.
   * @param scale The number of digits after the decimal at which to round.
   * @return The rounded value of the specified {@code double} based on the
   *         rounding policy of {@link RoundingMode#HALF_UP} with ties at the
   *         given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static double round(final double a, final int scale) {
    return round(a, scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the closest {@code byte} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A byte value to be rounded to the scaled byte.
   * @param scale Ignored for byte type.
   * @return The value of the argument rounded to the nearest {@code byte} value
   *         at {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static byte round(final byte a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code short} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A short value to be rounded to the scaled short.
   * @param scale Ignored for short type.
   * @return The value of the argument rounded to the nearest {@code short}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static short round(final short a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code int} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A int value to be rounded to the scaled int.
   * @param scale Ignored for int type.
   * @return The value of the argument rounded to the nearest {@code int} value
   *         at {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static int round(final int a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@code long} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A long value to be rounded to the scaled long.
   * @param scale Ignored for long type.
   * @return The value of the argument rounded to the nearest {@code long} value
   *         at {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static long round(final long a, final int scale) {
    return a;
  }

  /**
   * Returns the rounded value of the specified {@link Decimal} based on the
   * rounding policy of the provided {@link RoundingMode} with ties at the given
   * {@code scale}.
   *
   * @param a The {@link Decimal} value to be rounded.
   * @param scale The number of digits after the decimal at which to round.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@link Decimal} based on the
   *         rounding policy of the provided {@link RoundingMode} with ties at
   *         the given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static Decimal round(final Decimal a, final short scale, final RoundingMode rm) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    return a.setScale(scale, rm);
  }

  /**
   * Returns the provided {@link Decimal} based on the rounding policy of
   * {@link RoundingMode#HALF_UP} with ties at the given {@code scale}.
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * SafeMath.round(a, scale, RoundingMode.HALF_UP)
   * </pre>
   *
   * @param a The {@link Decimal} value to be rounded.
   * @param scale The number of digits after the decimal at which to round.
   * @return The provided {@link Decimal} based on the rounding policy of
   *         {@link RoundingMode#HALF_UP} with ties at the given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   * @throws NullPointerException If {@code a} is null.
   */
  public static Decimal round(final Decimal a, final short scale) {
    return round(a, scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the rounded value of the specified {@link BigDecimal} based on the
   * rounding policy of the provided {@link RoundingMode} with ties at the given
   * {@code scale}.
   *
   * @param a The {@link BigDecimal} value to be rounded.
   * @param scale The number of digits after the decimal at which to round.
   * @param rm The {@link RoundingMode} to be used for rounding.
   * @return The rounded value of the specified {@link BigDecimal} based on the
   *         rounding policy of the provided {@link RoundingMode} with ties at
   *         the given {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   */
  public static BigDecimal round(final BigDecimal a, final int scale, final RoundingMode rm) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    return BigDecimals.setScale(a, scale, rm);
  }

  /**
   * Returns the closest {@link BigDecimal} to the argument, based on the
   * rounding policy of {@link RoundingMode#HALF_UP} with ties at the given
   * {@code scale}.
   *
   * @param a A {@link BigDecimal} value to be rounded to the scaled
   *          {@link BigDecimal}.
   * @param scale The number of digits after the decimal at which to round.
   * @return The closest {@link BigDecimal} to the argument, based on the
   *         rounding policy of {@link RoundingMode#HALF_UP} with ties at the given
   *         {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
   * @throws NullPointerException If {@code a} is null.
   */
  public static BigDecimal round(final BigDecimal a, final int scale) {
    return round(a, scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the closest {@link BigInteger} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A BigInteger value to be rounded to the scaled BigInteger.
   * @param scale Ignored for BigInteger type.
   * @return The value of the argument rounded to the nearest {@link BigInteger}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is negative.
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
   * @param a The byte value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final byte a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The short value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final short a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The int value whose signum is to be returned.
   * @return The signum function of the argument.
   */
  public static byte signum(final int a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The long value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final long a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The floating-point value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final float a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The floating-point value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final double a) {
    return a < 0 ? -1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument {@link BigInteger}.
   *
   * @param a The {@link BigInteger} value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final BigInteger a) {
    return (byte)a.signum();
  }

  /**
   * Returns the signum function of the argument {@link BigDecimal}.
   *
   * @param a The {@link BigDecimal} value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static byte signum(final BigDecimal a) {
    return (byte)a.signum();
  }

  /**
   * Returns the trigonometric sine of an angle.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or an infinity, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a An angle, in radians.
   * @return The sine of the argument.
   */
  public static double sin(final double a) {
    return StrictMath.sin(a);
  }

  /**
   * Returns the trigonometric sine of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The sine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified angle or {@link MathContext}
   *           is null.
   */
  public static BigDecimal sin(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.sin(a, mc);
  }

  /**
   * Returns the trigonometric sine of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The sine of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified angle or {@link MathContext}
   *           is null.
   */
  public static BigDecimal sin(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.sin(new BigDecimal(a), mc);
  }

  /**
   * Returns the correctly rounded positive square root of a {@code double}
   * value.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * the same as the argument.</li>
   * </ul>
   * Otherwise, the result is the {@code double} value closest to the true
   * mathematical square root of the argument value.
   *
   * @param a The value.
   * @return The positive square root of {@code a}.
   */
  public static double sqrt(final double a) {
    return StrictMath.sqrt(a);
  }

  /**
   * Returns the correctly rounded positive square root of a {@link BigDecimal}
   * value.
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The positive square root of {@code a}.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal sqrt(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.sqrt(a, mc);
  }

  /**
   * Returns the correctly rounded positive square root of a {@link BigInteger}
   * value.
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The positive square root of {@code a}.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal sqrt(final BigInteger a, final MathContext mc) {
    return sqrt(new BigDecimal(a), mc);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or an infinity, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is zero, then the result is a zero with the same sign
   * as the argument.</li>
   * </ul>
   *
   * @param a An angle, in radians.
   * @return The tangent of the argument.
   */
  public static double tan(final double a) {
    return StrictMath.tan(a);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The tangent of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified angle or {@link MathContext}
   *           is null.
   */
  public static BigDecimal tan(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.tan(a, mc);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   *
   * @param a An angle, in radians.
   * @param mc The {@link MathContext} used for the result.
   * @return The tangent of the argument.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified angle or {@link MathContext}
   *           is null.
   */
  public static BigDecimal tan(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.tan(new BigDecimal(a), mc);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final int b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final long b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final float b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final double b, final int a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final int b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final long b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final float b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final double b, final long a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final int b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final long b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final float b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final double b, final float a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final int b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final long b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final float b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   */
  public static double log(final double b, final double a) {
    return b < 0 || a < 0 || b == 0 && a == 0 || b == 1 && a == 1 ? Double.NaN : StrictMath.log(a) / StrictMath.log(b);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal log(final BigInteger b, final BigInteger a, final MathContext mc) {
    return BigDecimalMath.log(new BigDecimal(a), mc).divide(BigDecimalMath.log(new BigDecimal(b), mc), mc);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal log(final BigInteger b, final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log(a, mc).divide(BigDecimalMath.log(new BigDecimal(b), mc), mc);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal log(final BigDecimal b, final BigInteger a, final MathContext mc) {
    return BigDecimalMath.log(new BigDecimal(a), mc).divide(BigDecimalMath.log(b, mc), mc);
  }

  /**
   * Returns the value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are {@code 0} or {@code 1}, then the result
   * is {@code NaN}.</li>
   * <li>If {@code a} is {@code 0}, then the result is negative infinity.</li>
   * <li>If {@code b} is {@code 1}, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is {@code 1}.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>b</sub>(a)</code> (logarithm base {@code b}
   *         of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal log(final BigDecimal b, final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log(a, mc).divide(BigDecimalMath.log(b, mc), mc);
  }

  /**
   * Returns the value <code>log<sub>e</sub>(a)</code> (i.e. the natural
   * logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>e</sub>(a)</code> (i.e. the natural
   *         logarithm of {@code a}).
   */
  public static double log(final float a) {
    return StrictMath.log(a);
  }

  /**
   * Returns the value <code>log<sub>e</sub>(a)</code> (i.e. the natural
   * logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>e</sub>(a)</code> (i.e. the natural
   *         logarithm of {@code a}).
   */
  public static double log(final double a) {
    return StrictMath.log(a);
  }

  /**
   * Returns value <code>log<sub>e</sub>(a)</code>, (i.e. the natural logarithm
   * of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>e</sub>(a)</code>, (i.e. the natural
   *         logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.log(new BigDecimal(a), mc);
  }

  /**
   * Returns the value <code>log<sub>e</sub>(a)</code>, (i.e. the natural
   * logarithm of {@code a})
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>e</sub>(a)</code>, (i.e. the natural
   *         logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log(a, mc);
  }

  /**
   * Returns the value <code>log<sub>10</sub>(a)</code> (i.e. the base
   * {@code 10} logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 10}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>10</sub>(a)</code> (i.e. the base
   *         {@code 10} logarithm of {@code a}).
   */
  public static double log10(final float a) {
    return StrictMath.log10(a);
  }

  /**
   * Returns the value <code>log<sub>10</sub>(a)</code> (i.e. the base
   * {@code 10} logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 10}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>10</sub>(a)</code> (i.e. the base
   *         {@code 10} logarithm of {@code a}).
   */
  public static double log10(final double a) {
    return StrictMath.log10(a);
  }

  /**
   * Returns the base {@code 10} logarithm of a {@link BigDecimal} value.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 10}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>10</sub>(a)</code>, (i.e. the base
   *         {@code 10} logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log10(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.log10(new BigDecimal(a), mc);
  }

  /**
   * Returns the base {@code 10} logarithm of a {@link BigDecimal} value.
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 10}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>10</sub>(a)</code>, (i.e. the base
   *         {@code 10} logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log10(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log10(a, mc);
  }

  /**
   * Returns the value <code>log<sub>2</sub>(a)</code> (i.e. the base {@code 2}
   * logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 2}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>2</sub>(a)</code> (i.e. the base {@code 2}
   *         logarithm of {@code a}).
   */
  public static double log2(final float a) {
    return StrictMath.log(a) / StrictMath.log(2);
  }

  /**
   * Returns the value <code>log<sub>2</sub>(a)</code> (i.e. the base {@code 2}
   * logarithm of {@code a}).
   * <p>
   * Special cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 2}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @return The value <code>log<sub>2</sub>(a)</code> (i.e. the base {@code 2}
   *         logarithm of {@code a}).
   */
  public static double log2(final double a) {
    return StrictMath.log(a) / StrictMath.log(2);
  }

  /**
   * Returns the base {@code 2} logarithm of a {@link BigDecimal} value. Special
   * cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 2}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>2</sub>(a)</code>, (i.e. the base {@code 2}
   *         logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log2(final BigInteger a, final MathContext mc) {
    return BigDecimalMath.log2(new BigDecimal(a), mc);
  }

  /**
   * Returns the base {@code 2} logarithm of a {@link BigDecimal} value. Special
   * cases:
   * <ul>
   * <li>If the argument is {@code NaN} or less than zero, then the result is
   * {@code NaN}.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * <li>If the argument is equal to {@code 2}<sup><i>n</i></sup> for integer
   * <i>n</i>, then the result is <i>n</i>.
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value <code>log<sub>2</sub>(a)</code>, (i.e. the base {@code 2}
   *         logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log2(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log2(a, mc);
  }

  /**
   * Returns the larger of two {@code byte} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static byte max(final byte a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code byte} and a {@code short}.
   *
   * @param a A {@code byte}.
   * @param b A {@code short}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static short max(final byte a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code byte} and an {@code int}.
   *
   * @param a A {@code byte}.
   * @param b An {@code int}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static int max(final byte a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code byte} and a {@code long}.
   *
   * @param a A {@code byte}.
   * @param b A {@code long}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final byte a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code byte} and a {@code float}.
   *
   * @param a A {@code byte}.
   * @param b A {@code float}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final byte a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code byte} and a {@code double}.
   *
   * @param a A {@code byte}.
   * @param b A {@code double}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final byte a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code short} and a {@code byte}.
   *
   * @param a A {@code short}.
   * @param b A {@code byte}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static short max(final short a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of two {@code short} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static short max(final short a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code short} and an {@code int}.
   *
   * @param a A {@code short}.
   * @param b An {@code int}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static int max(final short a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code short} and a {@code long}.
   *
   * @param a A {@code short}.
   * @param b A {@code long}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final short a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code short} and a {@code float}.
   *
   * @param a A {@code short}.
   * @param b A {@code float}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final short a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code short} and a {@code double}.
   *
   * @param a A {@code short}.
   * @param b A {@code double}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final short a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of an {@code int} and an {@code byte}.
   *
   * @param a An {@code int}.
   * @param b A {@code byte}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static int max(final int a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of an {@code int} and a {@code short}.
   *
   * @param a An {@code int}.
   * @param b A {@code short}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static int max(final int a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of two {@code int} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static int max(final int a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of an {@code int} and a {@code long}.
   *
   * @param a An {@code int}.
   * @param b A {@code long}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final int a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of an {@code int} and a {@code float}.
   *
   * @param a An {@code int}.
   * @param b A {@code float}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final int a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of an {@code int} and a {@code double}.
   *
   * @param a An {@code int}.
   * @param b A {@code double}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final int a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code long} and a {@code byte}.
   *
   * @param a A {@code long}.
   * @param b A {@code byte}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final long a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code long} and a {@code short}.
   *
   * @param a A {@code long}.
   * @param b A {@code short}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final long a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code long} and an {@code int}.
   *
   * @param a A {@code long}.
   * @param b An {@code int}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final long a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of two {@code long} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static long max(final long a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code long} and a {@code float}.
   *
   * @param a A {@code long}.
   * @param b A {@code float}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final long a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code long} and a {@code double}.
   *
   * @param a A {@code long}.
   * @param b A {@code double}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final long a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code float} and a {@code byte}.
   *
   * @param a A {@code float}.
   * @param b A {@code byte}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final float a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code float} and a {@code short}.
   *
   * @param a A {@code float}.
   * @param b A {@code short}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final float a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code float} and an {@code int}.
   *
   * @param a A {@code float}.
   * @param b An {@code int}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final float a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code float} and a {@code long}.
   *
   * @param a A {@code float}.
   * @param b A {@code long}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final float a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of two {@code float} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static float max(final float a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code float} and a {@code double}.
   *
   * @param a A {@code float}.
   * @param b A {@code double}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final float a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code double} and a {@code byte}.
   *
   * @param a A {@code double}.
   * @param b A {@code byte}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final byte b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code double} and a {@code short}.
   *
   * @param a A {@code double}.
   * @param b A {@code short}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final short b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code double} and an {@code int}.
   *
   * @param a A {@code double}.
   * @param b An {@code int}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final int b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code double} and a {@code long}.
   *
   * @param a A {@code double}.
   * @param b A {@code long}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final long b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of a {@code double} and a {@code float}.
   *
   * @param a A {@code double}.
   * @param b A {@code float}.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final float b) {
    return a > b ? a : b;
  }

  /**
   * Returns the larger of two {@code double} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The larger of {@code a} and {@code b}.
   */
  public static double max(final double a, final double b) {
    return a > b ? a : b;
  }

  /**
   * Returns the smaller of two {@code byte} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final byte a, final byte b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code byte} and a {@code short}.
   *
   * @param a A {@code byte}.
   * @param b A {@code short}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final byte a, final short b) {
    return a <= b ? a : (byte)b;
  }

  /**
   * Returns the smaller of a {@code byte} and an {@code int}.
   *
   * @param a A {@code byte}.
   * @param b An {@code int}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final byte a, final int b) {
    return a <= b ? a : (byte)b;
  }

  /**
   * Returns the smaller of a {@code byte} and a {@code long}.
   *
   * @param a A {@code byte}.
   * @param b A {@code long}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final byte a, final long b) {
    return a <= b ? a : (byte)b;
  }

  /**
   * Returns the smaller of a {@code byte} and a {@code float}.
   *
   * @param a A {@code byte}.
   * @param b A {@code float}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final byte a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code byte} and a {@code double}.
   *
   * @param a A {@code byte}.
   * @param b A {@code double}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final byte a, final double b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code short} and a {@code byte}.
   *
   * @param a A {@code short}.
   * @param b A {@code byte}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final short a, final byte b) {
    return a < b ? (byte)a : b;
  }

  /**
   * Returns the smaller of two {@code short} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static short min(final short a, final short b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code short} and an {@code int}.
   *
   * @param a A {@code short}.
   * @param b An {@code int}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static short min(final short a, final int b) {
    return a <= b ? a : (short)b;
  }

  /**
   * Returns the smaller of a {@code short} and a {@code long}.
   *
   * @param a A {@code short}.
   * @param b A {@code long}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static short min(final short a, final long b) {
    return a <= b ? a : (short)b;
  }

  /**
   * Returns the smaller of a {@code short} and a {@code float}.
   *
   * @param a A {@code short}.
   * @param b A {@code float}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final short a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code short} and a {@code double}.
   *
   * @param a A {@code short}.
   * @param b A {@code double}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final short a, final double b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of an {@code int} and an {@code byte}.
   *
   * @param a An {@code int}.
   * @param b A {@code byte}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final int a, final byte b) {
    return a < b ? (byte)a : b;
  }

  /**
   * Returns the smaller of an {@code int} and a {@code short}.
   *
   * @param a An {@code int}.
   * @param b A {@code short}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static short min(final int a, final short b) {
    return a < b ? (short)a : b;
  }

  /**
   * Returns the smaller of two {@code int} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static int min(final int a, final int b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of an {@code int} and a {@code long}.
   *
   * @param a An {@code int}.
   * @param b A {@code long}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static int min(final int a, final long b) {
    return a <= b ? a : (int)b;
  }

  /**
   * Returns the smaller of an {@code int} and a {@code float}.
   *
   * @param a An {@code int}.
   * @param b A {@code float}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final int a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of an {@code int} and a {@code double}.
   *
   * @param a An {@code int}.
   * @param b A {@code double}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final int a, final double b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code long} and a {@code byte}.
   *
   * @param a A {@code long}.
   * @param b A {@code byte}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static byte min(final long a, final byte b) {
    return a < b ? (byte)a : b;
  }

  /**
   * Returns the smaller of a {@code long} and a {@code short}.
   *
   * @param a A {@code long}.
   * @param b A {@code short}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static short min(final long a, final short b) {
    return a < b ? (short)a : b;
  }

  /**
   * Returns the smaller of a {@code long} and an {@code int}.
   *
   * @param a A {@code long}.
   * @param b An {@code int}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static int min(final long a, final int b) {
    return a < b ? (int)a : b;
  }

  /**
   * Returns the smaller of two {@code long} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static long min(final long a, final long b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code long} and a {@code float}.
   *
   * @param a A {@code long}.
   * @param b A {@code float}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final long a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code long} and a {@code double}.
   *
   * @param a A {@code long}.
   * @param b A {@code double}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final long a, final double b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code float} and a {@code byte}.
   *
   * @param a A {@code float}.
   * @param b A {@code byte}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final float a, final byte b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code float} and a {@code short}.
   *
   * @param a A {@code float}.
   * @param b A {@code short}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final float a, final short b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code float} and an {@code int}.
   *
   * @param a A {@code float}.
   * @param b An {@code int}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final float a, final int b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code float} and a {@code long}.
   *
   * @param a A {@code float}.
   * @param b A {@code long}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final float a, final long b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of two {@code float} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static float min(final float a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code float} and a {@code double}.
   *
   * @param a A {@code float}.
   * @param b A {@code double}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final float a, final double b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code double} and a {@code byte}.
   *
   * @param a A {@code double}.
   * @param b A {@code byte}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final byte b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code double} and a {@code short}.
   *
   * @param a A {@code double}.
   * @param b A {@code short}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final short b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code double} and an {@code int}.
   *
   * @param a A {@code double}.
   * @param b An {@code int}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final int b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code double} and a {@code long}.
   *
   * @param a A {@code double}.
   * @param b A {@code long}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final long b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of a {@code double} and a {@code float}.
   *
   * @param a A {@code double}.
   * @param b A {@code float}.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final float b) {
    return a < b ? a : b;
  }

  /**
   * Returns the smaller of two {@code double} values.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The smaller of {@code a} and {@code b}.
   */
  public static double min(final double a, final double b) {
    return a < b ? a : b;
  }

  private SafeMath() {
  }
}