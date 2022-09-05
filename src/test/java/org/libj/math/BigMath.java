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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Provides some mathematical operations on {@code BigDecimal} and {@code BigInteger}. Static methods.
 */
class BigMath {
  private static final double LOG_2 = Math.log(2.0);
  private static final double LOG_10 = Math.log(10.0);

  // numbers greater than 10^MAX_DIGITS_10 or e^MAX_DIGITS_E are considered
  // unsafe ('too big') for floating point operations
  private static final int MAX_DIGITS_10 = 294;
  private static final int MAX_DIGITS_2 = 977; // ~ MAX_DIGITS_10 * LN(10)/LN(2)
  private static final int MAX_DIGITS_E = 677; // ~ MAX_DIGITS_10 * LN(10)

  /**
   * Computes the natural logarithm of the provided {@link BigInteger}.
   *
   * @param val The {@link BigInteger}.
   * @param rm The {@link RoundingMode}.
   * @return Natural logarithm, as in {@link java.lang.Math#log(double)}, or {@code NaN} if the argument is negative, or
   *         {@code NEGATIVE_INFINITY} if the argument is zero.
   */
  public static double logBigInteger(BigInteger val, final RoundingMode rm) {
    if (val.signum() < 1)
      return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;

    final int blex = val.bitLength() - MAX_DIGITS_2; // any value in 60..1023 works here
    if (blex > 0)
      val = val.shiftRight(blex);

    double res = Math.log(val.doubleValue());
    res = blex > 0 ? res + blex * LOG_2 : res;
    return rm == null ? res : SafeMath.round(res, rm);
  }

  /**
   * Computes the natural logarithm of a {@link BigDecimal}.
   *
   * @param val The {@link BigDecimal}.
   * @param rm The {@link RoundingMode}.
   * @return Natural logarithm, as in {@link java.lang.Math#log(double)}, or {@code NaN} if the argument is negative, or
   */
  public static double logBigDecimal(final BigDecimal val, final RoundingMode rm) {
    if (val.signum() < 1)
      return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;

    int digits = val.precision() - val.scale();
    if (digits < MAX_DIGITS_10 && digits > -MAX_DIGITS_10)
      return Math.log(val.doubleValue());

    return SafeMath.round(logBigInteger(val.unscaledValue(), null) - val.scale() * LOG_10, rm);
  }

  /**
   * Computes the exponential function, returning a {@link BigDecimal} (precision ~ 16).
   *
   * @param exponent Any finite value (infinite or {@code NaN} throws {@code IllegalArgumentException}).
   * @return The value of <i>e</i> raised to the given {@code exponent}, as in {@link java.lang.Math#exp(double)}.
   */
  public static BigDecimal expBig(double exponent) {
    if (!Double.isFinite(exponent))
      throw new IllegalArgumentException("Infinite not accepted: " + exponent);

    // e^b = e^(b2+c) = e^b2 2^t with e^c = 2^t
    final double bc = MAX_DIGITS_E;
    if (exponent < bc && exponent > -bc)
      return new BigDecimal(Math.exp(exponent), MathContext.DECIMAL64);

    final boolean neg;
    if (neg = (exponent < 0))
      exponent = -exponent;

    double b2 = bc;
    double c = exponent - bc;
    int t = (int)Math.ceil(c / LOG_10);
    c = t * LOG_10;
    b2 = exponent - c;
    if (neg) {
      b2 = -b2;
      t = -t;
    }

    return new BigDecimal(Math.exp(b2), MathContext.DECIMAL64).movePointRight(t);
  }

  /**
   * Same as {@link java.lang.Math#pow(double,double)} but returns a {@link BigDecimal} (precision ~ 16).
   * <p>
   * Works even for outputs that fall outside the {@code double} range.
   * <p>
   * The only limitation is that {@code b * log(a)} cannot exceed the {@code double} range.
   *
   * @param base The non-negative base.
   * @param exp The finite exponent.
   * @return Returns the value of {@code base} raised to the power of {@code exp}.
   */
  public static BigDecimal powBig(final double base, final double exp) {
    if (!(Double.isFinite(base) && Double.isFinite(exp)))
      throw new IllegalArgumentException(Double.isFinite(exp) ? "base not finite: a=" + base : "exponent not finite: b=" + exp);

    if (exp == 0)
      return BigDecimal.ONE;

    if (exp == 1)
      return BigDecimal.valueOf(base);

    if (base <= 0) {
      if (base == 0) {
        if (exp >= 0)
          return BigDecimal.ZERO;

        throw new IllegalArgumentException("0**negative = infinite b=" + exp);
      }

      throw new IllegalArgumentException("negative base a=" + base);
    }

    final double x = exp * Math.log(base);
    if (Math.abs(x) < MAX_DIGITS_E)
      return BigDecimal.valueOf(Math.pow(base, exp));

    return expBig(x);
  }
}