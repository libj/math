/* Copyright (c) 2008 LibJ
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
  public static byte abs(final byte a) {
    return a;
  }

  /**
   * Returns the absolute value of a {@code short} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static short abs(final short a) {
    return a;
  }

  /**
   * Returns the absolute value of a {@code int} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static int abs(final int a) {
    return a;
  }

  /**
   * Returns the absolute value of a {@code long} value.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static long abs(final long a) {
    return a;
  }

  /**
   * Returns the absolute value of a {@code float} value. If the argument is not
   * negative, the argument is returned. If the argument is negative, the
   * negation of the argument is returned. Special cases:
   * <ul>
   * <li>If the argument is positive zero or negative zero, the result is
   * positive zero.</li>
   * <li>If the argument is infinite, the result is positive infinity.</li>
   * <li>If the argument is NaN, the result is NaN.</li>
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
   * negation of the argument is returned. Special cases:
   * <ul>
   * <li>If the argument is positive zero or negative zero, the result is
   * positive zero.</li>
   * <li>If the argument is infinite, the result is positive infinity.</li>
   * <li>If the argument is NaN, the result is NaN.</li>
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
    return a;
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
   * Returns the arc cosine of a value; the returned angle is in the range 0.0
   * through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
   * </ul>
   *
   * @param a The value, whose arc cosine is to be returned.
   * @return The arc cosine of the argument.
   */
  public static double acos(final double a) {
    return StrictMath.acos(a);
  }

  /**
   * Returns the arc cosine of a value; the returned angle is in the range 0.0
   * through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
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
   * Returns the arc cosine of a value; the returned angle is in the range 0.0
   * through <i>pi</i>. Special case:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN or its absolute value is greater than 1, then
   * the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN, then the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN, then the result is NaN.</li>
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
   * -<i>pi</i>/2 through <i>pi</i>/2. Special cases:
   * <ul>
   * <li>If the argument is NaN, then the result is NaN.</li>
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
   * {@code y/x} in the range of -<i>pi</i> to <i>pi</i>. Special cases:
   * <ul>
   * <li>If either argument is NaN, then the result is NaN.</li>
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
   * {@code y/x} in the range of -<i>pi</i> to <i>pi</i>. Special cases:
   * <ul>
   * <li>If either argument is NaN, then the result is NaN.</li>
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
    if (y.signum() == 1)
      return BigDecimals.TWO.multiply(BigDecimalMath.atan(x.divide(BigDecimalMath.sqrt(y.multiply(y).add(x.multiply(x)), mc).add(y), mc), mc));

    if (y.signum() <= 0 && x.signum() != 0)
      return BigDecimals.TWO.multiply(BigDecimalMath.atan(BigDecimalMath.sqrt(y.multiply(y).add(x.multiply(x)), mc).subtract(y).divide(x, mc), mc));

    if (y.signum() == -1 && x.signum() == 0)
      return BigDecimals.PI;

    return null;
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
   * mathematical integer. Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is NaN or an infinity or positive zero or negative
   * zero, then the result is the same as the argument.</li>
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
   * mathematical integer. Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is NaN or an infinity or positive zero or negative
   * zero, then the result is the same as the argument.</li>
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
   * Returns the trigonometric cosine of an angle. Special cases:
   * <ul>
   * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
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
   * value. Special cases:
   * <ul>
   * <li>If the argument is NaN, the result is NaN.</li>
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
   * value. Special cases:
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
   * value. Special cases:
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
   * integer. Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is NaN or an infinity or positive zero or negative
   * zero, then the result is the same as the argument.</li>
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
   * integer. Special cases:
   * <ul>
   * <li>If the argument value is already equal to a mathematical integer, then
   * the result is the same as the argument.</li>
   * <li>If the argument is NaN or an infinity or positive zero or negative
   * zero, then the result is the same as the argument.</li>
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
   * argument. Special cases:
   * <ul>
   * <li>If the second argument is positive or negative zero, then the result is
   * 1.0.</li>
   * <li>If the second argument is 1.0, then the result is the same as the first
   * argument.</li>
   * <li>If the second argument is NaN, then the result is NaN.</li>
   * <li>If the first argument is NaN and the second argument is nonzero, then
   * the result is NaN.</li>
   * <li>If
   * <ul>
   * <li>the absolute value of the first argument is greater than 1 and the
   * second argument is positive infinity, or</li>
   * <li>the absolute value of the first argument is less than 1 and the second
   * argument is negative infinity,</li>
   * </ul>
   * then the result is positive infinity.</li>
   * <li>If
   * <ul>
   * <li>the absolute value of the first argument is greater than 1 and the
   * second argument is negative infinity, or</li>
   * <li>the absolute value of the first argument is less than 1 and the second
   * argument is positive infinity,</li>
   * </ul>
   * then the result is positive zero.</li>
   * <li>If the absolute value of the first argument equals 1 and the second
   * argument is infinite, then the result is NaN.</li>
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
   * NaN.</li>
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
   * @throws NullPointerException If the {@code a}, {@code b}, or
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
   * @throws NullPointerException If the {@code a}, {@code b}, or
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
   * @throws NullPointerException If the {@code a}, {@code b}, or
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
   * @throws NullPointerException If the {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigInteger pow(final BigInteger a, final BigInteger b, final MathContext mc) {
    return BigDecimalMath.pow(new BigDecimal(a), new BigDecimal(b), mc).toBigInteger();
  }

  /**
   * Returns the closest {@code float} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A floating-point value to be rounded to the scaled float.
   * @param scale The number of digits after the decimal at which to round.
   * @return The value of the argument rounded to the nearest {@code float}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is less than 0.
   */
  public static float round(final float a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return Math.round(a);

    final float pow = (float)StrictMath.pow(10, scale);
    return Math.round(a * pow) / pow;
  }

  /**
   * Returns the closest {@code double} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A floating-point value to be rounded to the scaled double.
   * @param scale The number of digits after the decimal at which to round.
   * @return The value of the argument rounded to the nearest {@code double}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is less than 0.
   */
  public static double round(final double a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    if (scale == 0)
      return Math.round(a);

    final double pow = StrictMath.pow(10, scale);
    return Math.round(a * pow) / pow;
  }

  /**
   * Returns the closest {@code byte} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A byte value to be rounded to the scaled byte.
   * @param scale Ignored for byte type.
   * @return The value of the argument rounded to the nearest {@code byte} value
   *         at {@code scale}.
   * @throws IllegalArgumentException If scale is less than 0.
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
   * @throws IllegalArgumentException If scale is less than 0.
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
   * @throws IllegalArgumentException If scale is less than 0.
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
   * @throws IllegalArgumentException If scale is less than 0.
   */
  public static long round(final long a, final int scale) {
    return a;
  }

  /**
   * Returns the closest {@link BigDecimal} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A BigDecimal value to be rounded to the scaled BigDecimal.
   * @param scale The number of digits after the decimal at which to round.
   * @return The value of the argument rounded to the nearest {@link BigDecimal}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is less than 0.
   * @throws NullPointerException If {@code a} is null.
   */
  public static BigDecimal round(final BigDecimal a, final int scale) {
    if (scale < 0)
      throw new IllegalArgumentException("scale < 0: " + scale);

    return a.setScale(scale, RoundingMode.HALF_UP);
  }

  /**
   * Returns the closest {@link BigInteger} to the argument, with ties after
   * {@code scale} digits after the decimal rounding to positive infinity.
   *
   * @param a A BigInteger value to be rounded to the scaled BigInteger.
   * @param scale Ignored for BigInteger type.
   * @return The value of the argument rounded to the nearest {@link BigInteger}
   *         value at {@code scale}.
   * @throws IllegalArgumentException If scale is less than 0.
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
    return a < 0 ? (byte)-1 : a == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The short value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static short signum(final short a) {
    return a < 0 ? (short)-1 : a == 0 ? 0 : (short)1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The int value whose signum is to be returned.
   * @return The signum function of the argument.
   */
  public static int signum(final int a) {
    return Integer.compare(a, 0);
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The long value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static long signum(final long a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The floating-point value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static float signum(final float a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero,
   * 1 if the argument is greater than zero, -1 if the argument is less than
   * zero.
   *
   * @param a The floating-point value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static double signum(final double a) {
    return a < 0 ? -1 : a == 0 ? 0 : 1;
  }

  /**
   * Returns the signum function of the argument {@link BigInteger}.
   *
   * @param a The {@link BigInteger} value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static int signum(final BigInteger a) {
    return a.signum();
  }

  /**
   * Returns the signum function of the argument {@link BigDecimal}.
   *
   * @param a The {@link BigDecimal} value whose signum is to be returned
   * @return The signum function of the argument.
   */
  public static int signum(final BigDecimal a) {
    return a.signum();
  }

  /**
   * Returns the trigonometric sine of an angle. Special cases:
   * <ul>
   * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
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
   * value. Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
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
   * Returns the trigonometric tangent of an angle. Special cases:
   * <ul>
   * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
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
   * Returns the natural logarithm (base <i>e</i>) of a {@code double} value.
   * Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @return The value {@code ln(a)} (i.e. the natural logarithm of {@code a}).
   */
  public static double log(final double a) {
    return StrictMath.log(a);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
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
   * Returns the natural logarithm (base <i>e</i>) of a {@link BigDecimal}
   * value. Special cases:
   * <ul>
   * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
   * <li>If the argument is positive infinity, then the result is positive
   * infinity.</li>
   * <li>If the argument is positive zero or negative zero, then the result is
   * negative infinity.</li>
   * </ul>
   *
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value {@code ln(a)}, (i.e. the natural logarithm of {@code a}).
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the specified value or {@link MathContext}
   *           is null.
   */
  public static BigDecimal log(final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log(a, mc);
  }

  /**
   * Returns the logarithm base {@code b} of value {@code a}. Special cases:
   * <ul>
   * <li>If {@code b} or {@code a} is less than zero, then the result is
   * NaN.</li>
   * <li>If {@code b} and {@code a} are 0 or 1, then the result is NaN.</li>
   * <li>If {@code a} is 0, then the result is negative infinity.</li>
   * <li>If {@code b} is 1, then the result is positive infinity.</li>
   * <li>If {@code b} is equal to {@code a}, then the result is 1.</li>
   * </ul>
   * <p>
   * The computed result must be within 1 ulp of the exact result. Results must
   * be semi-monotonic.
   *
   * @param b The base.
   * @param a The value.
   * @param mc The {@link MathContext} used for the result.
   * @return The value logarithm base {@code b} of {@code a}.
   * @throws UnsupportedOperationException If the {@link MathContext} has
   *           unlimited precision.
   * @throws NullPointerException If the {@code a}, {@code b}, or
   *           {@link MathContext} is null.
   */
  public static BigDecimal log(final BigDecimal b, final BigDecimal a, final MathContext mc) {
    return BigDecimalMath.log(a, mc).divide(BigDecimalMath.log(b, mc), mc);
  }

  private SafeMath() {
  }
}