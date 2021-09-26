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

import static org.libj.math.DecimalAddition.*;
import static org.libj.math.DecimalDivision.*;
import static org.libj.math.DecimalMultiplication.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.libj.lang.Numbers;

/**
 * Abstract class implementing the fixed point representation of a decimal
 * encoded in a {@code long}. A decimal is encoded into a {@code long} in 2
 * components:
 * <ul>
 * <li>significand := The significand of the decimal. The decimal's precision is
 * this component.</li>
 * <li>scale := The inverse scale by which {@code significand} is to be
 * multiplied by a power of {@code 10} (i.e.
 * <code>10<sup>-scale</sup></code>)</li>
 * </ul>
 */
public class Decimal extends FixedPoint implements Comparable<Decimal>, Cloneable {
  /**
   * Returns the absolute value of the argument (encoded with
   * {@link Decimal#valueOf(long,int,long)}.
   * <p>
   * If the argument is not negative, the argument is returned.
   * <p>
   * If the argument is negative, the negation of the argument is returned.
   *
   * @param dec The value (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The absolute value of the argument.
   */
  public static long abs(final long dec, final long defaultValue) {
    final long significand = significand(dec);
    return significand >= 0 ? dec : neg0(significand, scale(dec), defaultValue);
  }

  public Decimal abs() {
    clear();
    if (significand == Long.MIN_VALUE) {
      significand /= -10;
      --scale;
    }
    else {
      significand = Math.abs(significand);
    }

    return this;
  }

  /**
   * Returns the result of the negation of {@code d}, i.e.:
   *
   * <pre>
   * result = -d
   * </pre>
   *
   * @param dec The value (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to negate.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The result of the negation of {@code d}, i.e.: {@code - d}
   * @see #valueOf(long,int,long)
   */
  public static long neg(final long dec, final long defaultValue) {
    return neg0(significand(dec), scale(dec), defaultValue);
  }

  private static long neg0(long significand, short scale, final long defaultValue) {
    if (significand == 0)
      return significand;

    if (significand == Long.MIN_VALUE) {
      significand /= -10;
      --scale;
    }
    else {
      significand = Math.abs(significand);
    }

    return valueOf(significand, scale, defaultValue);
  }

  public static long valueOf(BigDecimal bDec, final RoundingMode rm, final long defaultValue) {
    bDec = bDec.stripTrailingZeros();
    final int pscale = bDec.scale() - bDec.precision();
    if (pscale < MIN_PSCALE || MAX_PSCALE < pscale)
      return defaultValue;

    try {
      final long value = bDec.longValueExact();
      if (value < MIN_SIGNIFICAND || MAX_SIGNIFICAND < value)
        return defaultValue;

      return encodeInPlace(value, pscale);
    }
    catch (final ArithmeticException e) {
      return rm == RoundingMode.UNNECESSARY || rm == null ? defaultValue : valueOf(bDec.toString(), rm, defaultValue);
    }
  }

  public static long valueOf(final BigDecimal bDec, final long defaultValue) {
    return valueOf(bDec, null, defaultValue);
  }

  public static Decimal valueOf(BigDecimal bDec, final RoundingMode rm) {
    bDec = bDec.stripTrailingZeros();
    final int pscale = bDec.scale() - bDec.precision();
    if (pscale < MIN_PSCALE || MAX_PSCALE < pscale)
      return null;

    try {
      final long value = bDec.longValueExact();
      if (value < MIN_SIGNIFICAND || MAX_SIGNIFICAND < value)
        return null;

      return new Decimal(value, (short)bDec.scale());
    }
    catch (final ArithmeticException e) {
      return rm == RoundingMode.UNNECESSARY || rm == null ? null : valueOf(bDec.toString(), rm);
    }
  }

  public static Decimal valueOf(final BigDecimal bDec) {
    return valueOf(bDec, null);
  }

  public static Decimal valueOfExact(final BigDecimal bDec) {
    try {
      final long value = bDec.longValue();
      final Decimal result = new Decimal();
      return checkScale(value, (byte)bDec.precision(), bDec.scale(), result) ? result : null;
    }
    catch (final ArithmeticException e) {
      return null;
    }
  }

  public static Decimal valueOf(final BigInteger bigInteger) {
    return valueOf(bigInteger, RoundingMode.DOWN);
  }

  private static Decimal valueOf(final BigInteger bigInteger, final RoundingMode rm) {
    return valueOf(bigInteger.toString(), rm); // FIXME: Can this be more efficient?
  }

  public static long valueOf(final BigInteger bigInteger, final long defaultValue) {
    return valueOf(bigInteger, defaultValue, RoundingMode.DOWN);
  }

  private static long valueOf(final BigInteger bigInteger, final long defaultValue, final RoundingMode rm) {
    return valueOf(bigInteger.toString(), rm, defaultValue); // FIXME: Can this be more efficient?
  }

  public static Decimal valueOf(final BigInt bigInt) {
    return valueOf(bigInt, RoundingMode.DOWN);
  }

  private static Decimal valueOf(final BigInt bigInt, final RoundingMode rm) {
    return valueOf(bigInt.toString(), rm); // FIXME: Can this be more efficient?
  }

  public static long valueOf(final BigInt bigInt, final long defaultValue) {
    return valueOf(bigInt, defaultValue, RoundingMode.DOWN);
  }

  private static long valueOf(final BigInt bigInt, final long defaultValue, final RoundingMode rm) {
    return valueOf(bigInt.toString(), rm, defaultValue); // FIXME: Can this be more efficient?
  }

  public static Decimal valueOf(final String str) {
    return valueOf(str, RoundingMode.DOWN);
  }

  public static Decimal valueOf(final String str, final RoundingMode rm) {
    final Decimal result = assign(new Decimal(), str, rm);
    return result == null || result.isError() ? null : result;
  }

  public static long valueOf(final String str, final RoundingMode rm, final long defaultValue) {
    final Decimal result = threadLocal.get();
    return assign(result, str, rm) == null || result.isError() ? defaultValue : result.encode(defaultValue);
  }

  public static long valueOf(final String str, final long defaultValue) {
    return valueOf(str, RoundingMode.DOWN, defaultValue);
  }

  public static Decimal valueOf(final float val) {
    return assign(new Decimal(), val);
  }

  public static long valueOf(final float val, final long defaultValue) {
    final Decimal result = threadLocal.get();
    return assign(result, val) == null || result.isError() ? defaultValue : result.encode(defaultValue);
  }

  public static Decimal valueOf(final double val) {
    return assign(new Decimal(), val, RoundingMode.DOWN);
  }

  public static Decimal valueOf(final double val, final RoundingMode rm) {
    return assign(new Decimal(), val, rm);
  }

  public static long valueOf(final double val, final RoundingMode rm, final long defaultValue) {
    final Decimal result = threadLocal.get();
    return assign(result, val, rm) == null || result.isError() ? defaultValue : result.encode(defaultValue);
  }

  public static long valueOf(final double val, final long defaultValue) {
    return valueOf(val, RoundingMode.DOWN, defaultValue);
  }

  public static Decimal assign(final Decimal dec, final float val) {
    return FloatingDecimal.toDecimal(val, dec);
  }

  public static Decimal assign(final Decimal dec, final double val) {
    return assign(dec, val, RoundingMode.DOWN);
  }

  public static Decimal assign(final Decimal dec, final double val, final RoundingMode rm) {
    return isDecimal(val) ? FloatingDecimal.toDecimal(val, rm, dec) : null;
  }

  public static Decimal assign(final Decimal dec, final String str) {
    return assign(dec, str, RoundingMode.DOWN);
  }

  private static Decimal assign(final Decimal dec, final String str, final RoundingMode rm) {
    final char[] chars = str.toCharArray();
    long significand = 0;
    short scale = 0;
    byte i = 0, p = 0, dot = 0;
    int end = chars.length;
    boolean isNeg = false;
    boolean isNegScale = false;
    boolean hasScale = false;
    boolean valLimit = false;
    boolean allZeroes = true;
    for (char ch0, ch1 = '\0'; i < chars.length; ++i, ch1 = ch0) {
      ch0 = chars[i];
      if (ch0 == '.') {
        dot = i;
      }
      else if (ch0 == '-') {
        if (hasScale) {
          isNegScale = true;
          continue;
        }
        else if (i == 0) {
          isNeg = true;
          continue;
        }

        return null;
      }
      else if (ch0 == 'e' || ch0 == 'E') {
        if (hasScale)
          return null;

        if (!valLimit) {
          end = i;
        }

        hasScale = true;
      }
      else if (ch0 == '+' && ch1 != 'e' && ch1 != 'E') {
        return null;
      }
      else if (ch0 < '0' || '9' < ch0) {
        return null;
      }
      else if (hasScale) {
        scale *= 10;
        scale += ch0 - '0';
        // Check for overflow or underflow
        if (scale < 0) {
          dec.error("Overflow");
          return dec;
        }
      }
      else if (!valLimit) {
        // Don't increment precision if the number starts with a zero
        if (!allZeroes || !(allZeroes = ch0 == '0'))
          ++p;

        significand = significand * 10 + ch0 - '0';
        if (significand > (isNeg ? -MIN_SIGNIFICAND : MAX_SIGNIFICAND)) {
          if (isNeg) {
            significand = -significand;
            isNeg = false;
          }

          significand = round(significand, MAX_PRECISION, 1, rm, 0);
          --p;
          if (significand == 0) {
            dec.error("Rouding required");
            return null;
          }

          valLimit = true;
          end = i;
          if (dot == 0)
            dot = i;
        }
      }
    }

    final int ds = dot == 0 ? 0 : end - dot - 1;
    if (!isNegScale) {
      scale *= -1;
    }

    scale += ds;
    if (isNeg) {
//      if (significand == Long.MIN_VALUE) {
//        significand = roundHalfUp10(significand);
//        --scale;
//      }

      significand = -significand;
    }

    if (allZeroes)
      p = 1;

    return checkScale(significand, p, scale, dec) ? dec : null;
  }

  long significand;
  short scale;

  // Cached values
  private int[] bigInt;
  private BigDecimal bigDecimal;
  private boolean byteValueDirty = true;
  private byte byteValue;
  private boolean shortValueDirty = true;
  private short shortValue;
  private boolean intValueDirty = true;
  private int intValue;
  private boolean longValueDirty = true;
  private long longValue;
  private boolean floatValueDirty = true;
  private float floatValue;
  private boolean doubleValueDirty = true;
  private double doubleValue;
  private boolean error;
  private String string;
  private String scientificString;

  static final ThreadLocal<Decimal> threadLocal = new ThreadLocal<Decimal>() {
    @Override
    protected Decimal initialValue() {
      return new Decimal();
    }
  };

  static final ThreadLocal<int[]> buf1 = new ThreadLocal<int[]>() {
    @Override
    protected int[] initialValue() {
      return new int[8];
    }
  };

  static final ThreadLocal<int[]> buf2 = new ThreadLocal<int[]>() {
    @Override
    protected int[] initialValue() {
      return new int[8];
    }
  };

  /**
   * Creates a new {@link Decimal} with the specified unscaled
   * {@code significand} and {@code scale}.
   *
   * @param significand The unscaled significand.
   * @param scale The scale.
   */
  public Decimal(final long significand, final short scale) {
    this.significand = significand;
    this.scale = scale;
  }

  public Decimal(final long dec) {
    this.significand = significand(dec);
    this.scale = scale(dec);
  }

  Decimal(final Decimal copy) {
    this.significand = copy.significand;
    this.scale = copy.scale;
  }

  Decimal() {
  }

  Decimal error(final String error) {
    this.error = true;
    this.string = this.scientificString = error;
    return this;
  }

  public boolean isError() {
    return error;
  }

  /**
   * Clears the cached values in this {@link Decimal}.
   */
  public void clear() {
    this.bigInt = null;
    this.bigDecimal = null;
    this.byteValueDirty = true;
    this.shortValueDirty = true;
    this.intValueDirty = true;
    this.longValueDirty = true;
    this.floatValueDirty = true;
    this.doubleValueDirty = true;
    this.error = false;
    this.string = null;
    this.scientificString = null;
  }

  long encode(final long defaultValue) {
    return valueOf(significand, scale, defaultValue);
  }

  public Decimal assign(final Decimal copy) {
    clear();
    this.significand = copy.significand;
    this.scale = copy.scale;
    return this;
  }

  public Decimal assign(BigDecimal bDec, final RoundingMode rm) {
    bDec = bDec.stripTrailingZeros();
    final int pscale = bDec.scale() - bDec.precision();
    if (pscale < MIN_PSCALE || MAX_PSCALE < pscale)
      return null;

    try {
      final long value = bDec.longValueExact();
      if (value < MIN_SIGNIFICAND || MAX_SIGNIFICAND < value)
        return null;

      return assign(value, (short)bDec.scale());
    }
    catch (final ArithmeticException e) {
      return rm == RoundingMode.UNNECESSARY || rm == null ? null : assign(this, bDec.toString(), rm);
    }
  }

  public Decimal assign(final BigDecimal bDec) {
    return assign(bDec, null);
  }

  public Decimal assign(final long significand, final short scale) {
    clear();
    this.significand = significand;
    this.scale = scale;
    return this;
  }

  // FIXME: Is this needed?
  public static Decimal add(final long significand1, final short scale1, final long significand2, final short sign2) {
    final Decimal result = threadLocal.get();
    if (add0(significand1, scale1, significand2, sign2, false, result))
      return new Decimal(result);

    return null;
  }

  /**
   * Returns the result of the addition of {@code d1} to {@code d2}, i.e.:
   *
   * <pre>
   * result = d1 + d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The result of the addition of {@code d1} to {@code d2}, i.e.:
   *         {@code d1 + d2}
   * @see #valueOf(long,int,long)
   */
  public static long add(final long dec1, final long dec2, final long defaultValue) {
    return dec1 == 0 ? dec2 : dec2 == 0 ? dec1 : add0(dec1, dec2, defaultValue);
  }

  public static Decimal add(final Decimal dec, final Decimal add) {
    return add0(dec, add.significand, add.scale);
  }

  public Decimal add(final Decimal add) {
    return add(this, add);
  }

  public static Decimal sub(final long significand1, final short scale1, final long significand2, final short scale2) {
    final Decimal result = threadLocal.get();
    if (significand2 == Long.MIN_VALUE) {
      if (!add0(-significand1, scale1, significand2, scale2, true, result))
        return null;
    }
    else if (!add0(significand1, scale1, -significand2, scale2, false, result)) {
      return null;
    }

    return new Decimal(result);
  }

  /**
   * Returns the result of the subtraction of {@code d2} from {@code d1}, i.e.:
   *
   * <pre>
   * result = d1 - d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The result of the subtraction of {@code d2} from {@code d1}, i.e.:
   *         {@code d1 - d2}
   * @see #valueOf(long,int,long)
   */
  public static long sub(final long dec1, final long dec2, final long defaultValue) {
    if (dec2 == 0)
      return dec1;

    if (dec1 == dec2)
      return 0;

    final long significand2 = significand(dec2);
    if (significand2 == 0)
      return dec1;

    final short scale2 = scale(dec2);
    final long significand1 = significand(dec1);
    if (significand1 == 0) {
      if (scale2 == 0 && significand2 == Long.MAX_VALUE)
        return defaultValue;

      return valueOf(-significand2, scale2, defaultValue);
    }

    final short scale1 = scale(dec1);
    if (significand1 == significand2 && scale1 == scale2)
      return 0;

    final Decimal result = threadLocal.get();
    if (significand2 == MIN_SIGNIFICAND) {
      if (!add0(-significand1, scale1, significand2, scale2, true, result))
        return defaultValue;
    }
    else if (!add0(significand1, scale1, -significand2, scale2, false, result)) {
      return defaultValue;
    }

    return result.encode(defaultValue);
  }

  public static Decimal sub(final Decimal dec, final Decimal sub) {
    return add0(dec, -sub.significand, sub.scale);
  }

  public Decimal sub(final Decimal sub) {
    return sub(this, sub);
  }

  public static Decimal mul(final long significand1, final short scale1, final long significand2, final short scale2) {
    final Decimal result = threadLocal.get();
    if (mul0(significand1, scale1, significand2, scale2, result))
      return new Decimal(result);

    return null;
  }

  /**
   * Returns the result of the multiplication of {@code d1} and {@code d2},
   * i.e.:
   *
   * <pre>
   * result = d1 * d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The result of the multiplication of {@code d1} and {@code d2},
   *         i.e.: {@code d1 * d2}
   * @see #valueOf(long,int,long)
   */
  public static long mul(final long dec1, final long dec2, final long defaultValue) {
    if (dec1 == 0 || dec2 == 0)
      return 0;

    long significand1 = significand(dec1);
    if (significand1 == 0)
      return 0;

    long significand2 = significand(dec2);
    if (significand2 == 0)
      return 0;

    final short scale1 = scale(dec1);
    final short scale2 = scale(dec2);

    final Decimal result = threadLocal.get();
    if (mul0(significand1, scale1, significand2, scale2, result))
      return valueOf(result.significand, result.scale, defaultValue);

    return defaultValue;
  }

  public Decimal mul(final Decimal mul) {
    if (significand == 0)
      return this;

    final long significand2 = mul.significand;
    if (significand2 == 0)
      return assign(0, (short)0);

    if (mul0(significand, scale, significand2, mul.scale, this))
      return this;

    return null;
  }
  /**
   * Returns the result of the division of {@code d1} by {@code d2}, i.e.:
   *
   * <pre>
   * result = d1 / d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The result of the multiplication of {@code d1} and {@code d2},
   *         i.e. {@code d1 * d2}
   * @see #valueOf(long,int,long)
   */
  public static long div(final long dec1, final long dec2, final RoundingMode rm, final long defaultValue) {
    long significand2 = significand(dec2);
    // Division by zero
    if (significand2 == 0)
      return defaultValue;

    long significand1 = significand(dec1);
    // Division of zero
    if (significand1 == 0)
      return 0;

    final short scale1 = scale(dec1);
    final short scale2 = scale(dec2);

    final Decimal result = threadLocal.get();
    if (div0(significand1, scale1, significand2, scale2, result, rm))
      return encodeInPlace(result.significand, result.scale - Numbers.precision(result.significand));

    return defaultValue;
  }

  private static Decimal div(final Decimal dec, final Decimal div, final RoundingMode rm) {
    long significand1 = dec.significand;
    if (significand1 == 0)
      return dec;

    long significand2 = div.significand;
    if (significand2 == 0)
      return dec.assign(0, (short)0);

    if (div0(significand1, dec.scale, significand2, div.scale, dec, rm))
      return dec;

    return null;
  }

  public Decimal div(final Decimal div, final RoundingMode rm) {
    return div(this, div, rm);
  }

  public static long rem(final long dec1, final long dec2, final long defaultValue) {
    final long significand2 = significand(dec2);
    // Division by zero
    if (significand2 == 0)
      return defaultValue;

    final long significand1 = significand(dec1);
    // Division of zero
    if (significand1 == 0)
      return 0;

    final short scale1 = scale(dec1);
    final short scale2 = scale(dec2);

    final Decimal result = threadLocal.get();
    if (rem0(significand1, scale1, significand2, scale2, result))
      return valueOf(result.significand, result.scale, defaultValue);

    return defaultValue;
  }

  public static Decimal rem(final Decimal dec, final Decimal div) {
    final long significand1 = dec.significand;
    // Division by zero
    if (significand1 == 0)
      return dec;

    // Division of zero
    final long significand2 = div.significand;
    if (significand2 == 0)
      return dec.assign(0, (short)0);

    if (rem0(significand1, dec.scale, significand2, div.scale, dec))
      return dec;

    return null;
  }

  public Decimal rem(final Decimal div) {
    return rem(this, div);
  }

  public static byte signum(long dec) {
    dec = significand(dec);
    return dec < 0 ? -1 : dec == 0 ? 0 : (byte)1;
  }

  public byte signum() {
    return significand < 0 ? -1 : significand == 0 ? 0 : (byte)1;
  }

  /**
   * Returns the <i>precision</i> of the specified
   * {@link Decimal#valueOf(long,int,long) encoded} value. (The precision
   * is the number of digits in the significand.)
   * <p>
   * The precision of a zero value is {@code 1}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return The scale component from the
   *         {@link Decimal#valueOf(long,int,long) encoded} value with the
   *         given number of sign {@code scaleBits}.
   * @see #valueOf(long,int,long)
   */
  public static byte precision(final long dec) {
    return Numbers.precision(significand(dec));
  }

  public byte precision() {
    return Numbers.precision(significand);
  }

  public long significand() {
    return significand;
  }

  public short scale() {
    return scale;
  }

  /**
   * Returns the {@link Decimal#valueOf(long,int,long) encoded} value with its
   * scale set to {@code scale}. The significand is determined by multiplying or
   * dividing the significand of {@code encoded} by the appropriate power of ten
   * to maintain its overall value. If the scale is reduced by the operation,
   * the significand must be divided (rather than multiplied), and the value may
   * be changed; in this case, the value will be rounded down.
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * setScale(encoded, newScale, RoundingMode.DOWN, defaultValue)
   * </pre>
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded} value.
   * @param newScale The scale component.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The {@link Decimal#valueOf(long,int,long) encoded} value with its
   *         scale set to {@code scale}.
   * @see #valueOf(long,int,long)
   * @see #scale(long)
   */
  public static long setScale(final long dec, short newScale, final long defaultValue) {
    return setScale(dec, newScale, RoundingMode.DOWN, defaultValue);
  }

  /**
   * Returns the {@link Decimal#valueOf(long,int,long) encoded} value with its
   * scale set to {@code scale}. The significand is determined by multiplying or
   * dividing the significand of {@code encoded} by the appropriate power of ten
   * to maintain its overall value. If the scale is reduced by the operation,
   * the significand must be divided (rather than multiplied), and the value may
   * be changed; in this case, the value will be rounded down.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded} value.
   * @param newScale The scale component.
   * @param rm The {@link RoundingMode}.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding.
   * @return The {@link Decimal#valueOf(long,int,long) encoded} value with its
   *         scale set to {@code scale}.
   * @see #valueOf(long,int,long)
   * @see #scale(long)
   */
  public static long setScale(final long dec, short newScale, final RoundingMode rm, final long defaultValue) {
    long significand = significand(dec);
    if (significand == 0)
      return dec;

    byte precision = Numbers.precision(significand);
    final short scale = scale(dec, precision);
    if (scale == newScale)
      return dec;

    int ds = scale - newScale;
    if (ds < 0)
      return dec;

    significand = round(significand, precision, ds, rm, defaultValue);
    if (significand == defaultValue || significand == 0)
      return significand;

    newScale -= Numbers.precision(significand);
    if (newScale > 0)
      return newScale <= MAX_PSCALE ? encodeInPlace(significand, newScale) : defaultValue;

    if (MIN_PSCALE <= newScale)
      return encodeInPlace(significand, newScale);

    ds = MIN_PSCALE - newScale;
    // How many multiples of 10 until overflow?
    final int dp1 = Numbers.precision(MIN_SIGNIFICAND / significand) - 1; // FIXME: Remove -1 and ds >= dp1
    if (ds > dp1)
      return defaultValue;

    significand *= FastMath.longE10[ds];
    newScale += ds;
    return encodeInPlace(significand, newScale);
  }

  /**
   * Sets the scale of this {@link Decimal} to the provided {@code newScale}.
   * The significand is determined by multiplying or dividing the significand of
   * {@code encoded} by the appropriate power of ten to maintain its overall
   * value. If the scale is reduced by the operation, the significand must be
   * divided (rather than multiplied), and the value may be changed; in this
   * case, the value will be rounded down.
   * <p>
   * Calling this method is the equivalent of:
   *
   * <pre>
   * setScale(newScale, RoundingMode.DOWN)
   * </pre>
   *
   * @param newScale The new scale to which this {@link Decimal} is to be set.
   * @return {@code this} {@link Decimal} with its scale set to
   *         {@code newScale}.
   */
  public Decimal setScale(final short newScale) {
    return setScale(newScale, RoundingMode.DOWN);
  }

  /**
   * Sets the scale of this {@link Decimal} to the provided {@code newScale}.
   * The significand is determined by multiplying or dividing the significand of
   * {@code encoded} by the appropriate power of ten to maintain its overall
   * value. If the scale is reduced by the operation, the significand must be
   * divided (rather than multiplied), and the value may be changed; in this
   * case, the value will be rounded down.
   *
   * @param newScale The new scale to which this {@link Decimal} is to be set.
   * @param rm The {@link RoundingMode}.
   * @return {@code this} {@link Decimal} with its scale set to
   *         {@code newScale}.
   */
  public Decimal setScale(final short newScale, final RoundingMode rm) {
    if (scale == newScale || significand == 0)
      return this;

    final int ds = scale - newScale;
    if (ds < 0)
      return this;

    clear();
    final long defaultValue = -significand;
    significand = round(significand, MAX_PRECISION, ds, rm, defaultValue);
    if (significand == defaultValue)
      return null;

    scale = newScale;
    return this;
  }

  /**
   * Compares two decimals (encoded with
   * {@link Decimal#valueOf(long,int,long)})s numerically.
   *
   * @param dec1 The first decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @param dec2 The second decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @return The value {@code 0} if {@code d1 == d2}; a value less than
   *         {@code 0} if {@code d1 < d2}; and a value greater than {@code 0} if
   *         {@code d1 > d2}
   * @see #valueOf(long,int,long)
   */
  public static int compare(final long dec1, final long dec2) {
    long significand1 = significand(dec1);
    long significand2 = significand(dec2);
    if (significand1 == 0)
      return significand2 > 0 ? -1 : significand2 == 0 ? 0 : 1;

    if (significand2 == 0)
      return significand1 < 0 ? -1 : significand1 == 0 ? 0 : 1;

    final short scale1 = scale(dec1);
    final short scale2 = scale(dec2);

    if (scale1 == scale2 || significand1 < 0 != significand2 < 0)
      return significand1 < significand2 ? -1 : significand1 == significand2 ? 0 : 1;

    return compare0(significand1, scale1, significand2, scale2);
  }

  /**
   * Compares two decimals (encoded with {@link Decimal#valueOf(long,int,long)})s
   * numerically.
   *
   * @param significand1 The significand of the first argument to equate.
   * @param scale1 The scale of the first argument to equate.
   * @param significand2 The significand of the second argument to equate.
   * @param scale2 The scale of the second argument to equate.
   * @return The value {@code 0} if the two arguments are equal; a value less
   *         than {@code 0} if the first argument is less than the second
   *         argument; and a value greater than {@code 0} if the first argument
   *         is greater than the second argument.
   * @see #valueOf(long,int,long)
   */
  public static int compare(final long significand1, final short scale1, final long significand2, final short scale2) {
    if (significand1 == 0)
      return significand2 > 0 ? -1 : significand2 == 0 ? 0 : 1;

    if (significand2 == 0)
      return significand1 < 0 ? -1 : significand1 == 0 ? 0 : 1;

    if (scale1 == scale2 || significand1 < 0 != significand2 < 0)
      return significand1 < significand2 ? -1 : significand1 == significand2 ? 0 : 1;

    return compare0(significand1, scale1, significand2, scale2);
  }

  private static int compare0(long significand1, short scale1, long significand2, short scale2) {
    final int p1 = Numbers.precision(significand1) - scale1;
    final int p2 = Numbers.precision(significand2) - scale2;
    if (p1 < p2)
      return significand1 < 0 ? 1 : -1;

    if (p1 > p2)
      return significand1 < 0 ? -1 : 1;

    if (scale1 < scale2) {
      final int ds = scale2 - scale1;
      int ds1 = Numbers.precision(Long.MIN_VALUE / significand1) - 1;
      if (ds < ds1)
        ds1 = ds;

      significand1 *= FastMath.longE10[ds1];
      scale1 += ds1;
    }
    else if (scale1 > scale2) {
      final int ds = scale1 - scale2;
      int ds2 = Numbers.precision(Long.MIN_VALUE / significand2) - 1;
      if (ds < ds2)
        ds2 = ds;

      significand2 *= FastMath.longE10[ds2];
      scale2 += ds2;
    }

    return significand1 < significand2 ? -1 : significand1 == significand2 ? 0 : 1;
  }

  @Override
  public int compareTo(final Decimal dec) {
    return compare(significand, scale, dec.significand, dec.scale);
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * result = d1 &lt; d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #valueOf(long,int,long)
   */
  public static boolean gt(final long dec1, final long dec2) {
    return compare(dec1, dec2) > 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * result = d1 &lt; d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #valueOf(long,int,long)
   */
  public static boolean gte(final long dec1, final long dec2) {
    return compare(dec1, dec2) >= 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * result = d1 &lt; d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #valueOf(long,int,long)
   */
  public static boolean lt(final long dec1, final long dec2) {
    return compare(dec1, dec2) < 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * result = d1 &lt; d2
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #valueOf(long,int,long)
   */
  public static boolean lte(final long dec1, final long dec2) {
    return compare(dec1, dec2) <= 0;
  }

  /**
   * Returns the larger of two {@link Decimal#valueOf(long,int,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param dec1 The first decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @param dec2 The second decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @return The larger of {@code d1} and {@code d2}.
   * @see #valueOf(long,int,long)
   */
  public static long max(final long dec1, final long dec2) {
    return gte(dec1, dec2) ? dec1 : dec2;
  }

  public Decimal max(final Decimal dec) {
    return compare(significand, scale, dec.significand, dec.scale) > 0 ? this : dec;
  }

  /**
   * Returns the smaller of two {@link Decimal#valueOf(long,int,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param dec1 The first decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @param dec2 The second decimal (encoded with
   *          {@link Decimal#valueOf(long,int,long)}) to compare.
   * @return The smaller of {@code d1} and {@code d2}.
   * @see #valueOf(long,int,long)
   */
  public static long min(final long dec1, final long dec2) {
    return lte(dec1, dec2) ? dec1 : dec2;
  }

  public Decimal min(final Decimal dec) {
    return compare(significand, scale, dec.significand, dec.scale) < 0 ? this : dec;
  }

  public static byte byteValue(final long significand, final short scale) {
    return (byte)longValue(significand, scale);
  }

  public static byte byteValue(final long dec) {
    return byteValue(significand(dec), scale(dec));
  }

  @Override
  public byte byteValue() {
    if (!byteValueDirty)
      return byteValue;

    byteValueDirty = false;
    return byteValue = byteValue(significand, scale);
  }

  public static short shortValue(final long significand, final short scale) {
    return (short)longValue(significand, scale);
  }

  public static short shortValue(final long dec) {
    return shortValue(significand(dec), scale(dec));
  }

  @Override
  public short shortValue() {
    if (!shortValueDirty)
      return shortValue;

    shortValueDirty = false;
    return shortValue = shortValue(significand, scale);
  }

  public static int intValue(final long significand, final short scale) {
    return (int)longValue(significand, scale);
  }

  public static int intValue(final long dec) {
    return intValue(significand(dec), scale(dec));
  }

  @Override
  public int intValue() {
    if (!intValueDirty)
      return intValue;

    intValueDirty = false;
    return intValue = intValue(significand, scale);
  }

  public static long longValue(final long significand, final short scale) {
    if (significand == 0)
      return 0;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / significand))
        return significand * FastMath.longE10[s];

      final int[] val = BigInt.assignInPlace(buf1.get(), significand);
      if (s < 19)
        return BigInt.longValue(BigInt.mulInPlace(val, FastMath.longE10[s]));

      final boolean sig = significand >= 0;
      final int len = sig ? val[0] : -val[0];
      final int[] mul = FastMath.E10(s);
      final int mlen = mul[0];
      return BigInt.longValue(BigInt.mulQuad(val, len, mul, mlen, len + mlen + 1, sig, true));
    }
    else if (scale > 0) {
      return scale > 18 ? 0 : significand / FastMath.longE10[scale];
    }

    return significand;
  }

  public static long longValue(final long dec) {
    return longValue(significand(dec), scale(dec));
  }

  @Override
  public long longValue() {
    if (!longValueDirty)
      return longValue;

    longValueDirty = false;
    return longValue = longValue(significand, scale);
  }

  /**
   * Returns a {@code float} representation of the provided significand
   * {@code long} and scale {@code short}.
   *
   * @param significand The significand.
   * @param scale The scale.
   * @return A {@code float} representation of the provided significand
   *         {@code long} and scale {@code short}.
   */
  public static float floatValue(final long significand, final short scale) {
    if (scale == 0 || significand == 0)
      return significand;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / significand))
        return significand * FastMath.longE10[s];
    }

    return (float)(scale > 0 ? significand / FastMath.doubleE10(scale) : significand * FastMath.doubleE10(-scale));
  }

  /**
   * Returns a {@code float} representation of the provided
   * {@link Decimal#valueOf(long,int,long) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return A {@code float} representation of the provided
   *         {@link Decimal#valueOf(long,int,long) encoded} value and
   *         sign {@code scaleBits}.
   */
  public static float floatValue(final long dec) {
    return floatValue(significand(dec), scale(dec));
  }

  @Override
  public float floatValue() {
    if (!floatValueDirty)
      return floatValue;

    floatValueDirty = false;
    return floatValue = floatValue(significand, scale);
  }

  /**
   * Returns a {@code double} representation of the provided significand
   * {@code long} and scale {@code short}.
   *
   * @param value The significand.
   * @param scale The scale.
   * @return A {@code double} representation of the provided significand
   *         {@code long} and scale {@code short}.
   */
  public static double doubleValue(final long value, final short scale) {
    return FloatingDecimal.doubleValue(value, scale);
  }

  /**
   * Returns a {@code double} representation of the provided
   * {@link Decimal#valueOf(long,int,long) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return A {@code double} representation of the provided
   *         {@link Decimal#valueOf(long,int,long) encoded} value and
   *         sign {@code scaleBits}.
   */
  public static double doubleValue(final long dec) {
    return doubleValue(significand(dec), scale(dec));
  }

  @Override
  public double doubleValue() {
    if (!doubleValueDirty)
      return doubleValue;

    doubleValueDirty = false;
    return doubleValue = doubleValue(significand, scale);
  }

  public int[] toBigInt() {
    return bigInt == null ? bigInt = toBigInt(significand, scale) : bigInt;
  }

  public static int[] toBigInt(final long dec) {
    return toBigInt(significand(dec), scale(dec));
  }

  public static int[] toBigInt(long significand, final short scale) {
    if (significand == 0)
      return new int[] {0};

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / significand)) {
        significand *= FastMath.longE10[s];
      }
      else {
        final int[] val = BigInt.assignInPlace(new int[5], significand);
        if (s < 19)
          return BigInt.mulInPlace(val, FastMath.longE10[s]);

        final boolean sig = significand >= 0;
        final int len = sig ? val[0] : -val[0];
        final int[] mul = FastMath.E10(s);
        final int mlen = mul[0];
        return BigInt.mulQuad(val, len, mul, mlen, len + mlen + 1, sig, true);
      }
    }
    else if (scale > 0) {
      if (scale > 18)
        return new int[] {0};

      significand /= FastMath.longE10[scale];
    }

    return BigInt.assignInPlace(new int[4], significand);
  }

  /**
   * Returns a new {@link BigDecimal} instance with the value of the provided
   * {@link Decimal#valueOf(long,int,long) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return A new {@link BigDecimal} instance with the value of the provided
   *         {@link Decimal#valueOf(long,int,long) encoded} value and
   *         sign {@code scaleBits}.
   */
  public static BigDecimal toBigDecimal(final long dec) {
    return BigDecimal.valueOf(significand(dec), scale(dec));
  }

  public BigDecimal toBigDecimal() {
    return bigDecimal == null ? bigDecimal = BigDecimal.valueOf(significand).scaleByPowerOfTen(-scale) : bigDecimal;
  }

  public long toLong() {
    // FIXME: This code assumes that Decimal can never exist in an invalid state.
    return encode(significand, Numbers.precision(significand), scale, 0);
  }

  public static int hashCode(final long significand, final short scale) {
    final int temp;
    if (significand < 0) {
      final long abs = -significand;
      temp = -(int)((int)(abs >>> 32) * 31 + (abs & BigInt.LONG_MASK));
    }
    else {
      final long abs = significand;
      temp = (int)((int)(abs >>> 32) * 31 + (abs & BigInt.LONG_MASK));
    }

    return 31 * temp + scale;
  }

  public static int hashCode(final long dec) {
    return hashCode(significand(dec), scale(dec));
  }

  @Override
  public int hashCode() {
    return hashCode(significand, scale);
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#valueOf(long,int,long)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = d1 == d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#valueOf(long,int,long)}).
   * @return {@code true} if the arguments (encoded with
   *         {@link Decimal#valueOf(long,int,long)}) are equal to each
   *         other and {@code false} otherwise.
   * @see #valueOf(long,int,long)
   */
  public static boolean eq(final long dec1, final long dec2) {
    return dec1 == dec2;
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#valueOf(long,int,long)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = d1 == d2}
   * </pre>
   *
   * @param significand1 The significand of the first argument to equate.
   * @param scale1 The scale of the first argument to equate.
   * @param significand2 The significand of the second argument to equate.
   * @param scale2 The scale of the second argument to equate.
   * @return {@code true} if the first argument's significand and scale equals
   *         the second argument's significand and scale.
   * @see #valueOf(long,int,long)
   */
  public static boolean eq(final long significand1, final short scale1, final long significand2, final short scale2) {
    return significand1 == significand2 && scale1 == scale2;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Decimal))
      return false;

    final Decimal that = (Decimal)obj;
    return significand == that.significand && scale == that.scale;
  }

  /**
   * Returns the string representation (in
   * <a href="https://en.wikipedia.org/wiki/Scientific_notation">scientific
   * notation</a>) of the fixed point decimal specified by the provided
   * significand and scale.
   *
   * @param significand The significand.
   * @param scale The scale.
   * @return The string representation (in <a href=
   *         "https://en.wikipedia.org/wiki/Scientific_notation">scientific
   *         notation</a>) of the fixed point decimal specified by the provided
   *         {@code significand} and {@code scale}.
   * @complexity O(n)
   */
  public static String toScientificString(long significand, final short scale) {
    if (significand == 0)
      return "0";

    int s = scale;
    final byte precision = Numbers.precision(significand);
    int dot;
    if (precision > 1) {
      dot = significand < 0 ? 3 : 2;
      s -= precision - 1;
    }
    else {
      dot = 0;
    }

    final int scaleLen;
    final int scalePart;
    final boolean scaleNeg;
    if (s > 0) {
      scaleLen = Numbers.precision(s);
      scalePart = scaleLen + 2;
      scaleNeg = true;
    }
    else if (s < 0) {
      scaleLen = Numbers.precision(s);
      scalePart = scaleLen + 1;
      scaleNeg = false;
      s = -s;
    }
    else {
      scaleLen = 0;
      scalePart = 0;
      scaleNeg = true;
    }

    final boolean isNeg = significand < 0;
    final char[] buf = new char[precision + (dot <= 0 ? 0 : 1) + (isNeg ? 1 : 0) + scalePart];

    int i = buf.length;
    if (s != 0) {
      for (int j = 0; j < scaleLen; ++j, s /= 10)
        buf[--i] = (char)(s % 10 + '0');

      if (scaleNeg)
        buf[--i] = '-';

      buf[--i] = 'E';
    }

    if (isNeg) {
      if (dot > 0) {
        boolean zeroesStripped = false;
        final int j = i;
        for (long dig; i > dot; significand /= 10) {
          dig = -(significand % 10);
          if (zeroesStripped || (zeroesStripped = (dig != 0)))
            buf[--i] = (char)(dig + '0');
          else
            ++dot;
        }

        if (j != i)
          buf[--i] = '.';
      }

      buf[--i] = (char)(-(significand % 10) + '0');
      if (isNeg)
        buf[--i] = '-';
    }
    else {
      if (dot > 0) {
        boolean zeroesStripped = false;
        final int j = i;
        for (long dig; i > dot; significand /= 10) {
          dig = significand % 10;
          if (zeroesStripped || (zeroesStripped = (dig != 0)))
            buf[--i] = (char)(dig + '0');
          else
            ++dot;
        }

        if (j != i)
          buf[--i] = '.';
      }

      buf[--i] = (char)((significand % 10) + '0');
      if (isNeg)
        buf[--i] = '-';
    }

    return new String(buf, i, buf.length - i);
  }

  /**
   * Returns the string representation (in
   * <a href="https://en.wikipedia.org/wiki/Scientific_notation">scientific
   * notation</a>) of the provided {@link Decimal#valueOf(long,int,long)
   * encoded} value.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return The string representation (in <a href=
   *         "https://en.wikipedia.org/wiki/Scientific_notation">scientific
   *         notation</a>) of the provided
   *         {@link Decimal#valueOf(long,int,long) encoded} value.
   */
  public static String toScientificString(final long dec) {
    return toScientificString(significand(dec), scale(dec));
  }

  /**
   * Returns the string representation (in
   * <a href="https://en.wikipedia.org/wiki/Scientific_notation">scientific
   * notation</a>) this {@link Decimal}.
   *
   * @return The string representation (in <a href=
   *         "https://en.wikipedia.org/wiki/Scientific_notation">scientific
   *         notation</a>) of this {@link Decimal}.
   * @complexity O(n)
   * @amortized O(1)
   */
  public String toScientificString() {
    return scientificString == null ? scientificString = toScientificString(significand, scale) : scientificString;
  }

  /**
   * Returns the string representation of the fixed point decimal specified by
   * the provided {@code significand} and {@code scale}.
   *
   * @param significand The significand.
   * @param scale The scale.
   * @return The string representation of the fixed point decimal specified by
   *         the provided {@code significand} and {@code scale}.
   * @complexity O(n)
   */
  public static String toString(long significand, final short scale) {
    if (significand == 0)
      return "0";

    int s = scale;
    if (s == 0)
      return String.valueOf(significand);

    final byte precision = Numbers.precision(significand);
    int dot;
    if (s > 0) {
      if (s >= precision) {
        dot = -1;
      }
      else {
        dot = precision - s + 1;
        s = 0;
      }
    }
    else if (s < 0 && precision > 1) {
      dot = 2;
      s -= precision - 1;
    }
    else {
      dot = -1;
    }

    final int scaleLen;
    final int scalePart;
    final boolean scaleNeg;
    if (s > 0) {
      scaleLen = Numbers.precision(s);
      scalePart = scaleLen + 2;
      scaleNeg = true;
    }
    else if (s < 0) {
      scaleLen = Numbers.precision(s);
      scalePart = scaleLen + 1;
      scaleNeg = false;
      s = -s;
    }
    else {
      scaleLen = 0;
      scalePart = 0;
      scaleNeg = true;
    }

    final boolean isNeg = significand < 0;
    final char[] buf = new char[precision + (dot <= 0 ? 0 : 1) + (isNeg ? 1 : 0) + scalePart];
    final int lim;
    if (isNeg) {
      lim = 1;
      ++dot;
      buf[0] = '-';
    }
    else {
      lim = 0;
    }

    int i = buf.length;
    if (s != 0) {
      for (int j = 0; j < scaleLen; ++j, s /= 10)
        buf[--i] = (char)(s % 10 + '0');

      if (scaleNeg)
        buf[--i] = '-';

      buf[--i] = 'E';
    }

    if (isNeg) {
      if (dot > 0) {
        for (; i > dot; significand /= 10)
          buf[--i] = (char)(-(significand % 10) + '0');

        buf[--i] = '.';
      }

      for (; i > lim; significand /= 10)
        buf[--i] = (char)(-(significand % 10) + '0');
    }
    else {
      if (dot > 0) {
        for (; i > dot; significand /= 10)
          buf[--i] = (char)((significand % 10) + '0');

        buf[--i] = '.';
      }

      for (; i > lim; significand /= 10)
        buf[--i] = (char)((significand % 10) + '0');
    }

    return new String(buf);
  }

  /**
   * Returns the string representation of the provided
   * {@link Decimal#valueOf(long,int,long) encoded} value.
   *
   * @param dec The {@link Decimal#valueOf(long,int,long) encoded}
   *          value.
   * @return The string representation of the provided
   *         {@link Decimal#valueOf(long,int,long) encoded} value.
   */
  public static String toString(final long dec) {
    return toString(significand(dec), scale(dec));
  }

  /**
   * Returns the string representation this {@link Decimal}.
   *
   * @return The string representation of this {@link Decimal}.
   * @complexity O(n)
   * @amortized O(1)
   */
  @Override
  public String toString() {
    return string == null ? string = toString(significand, scale) : string;
  }

  /**
   * Returns a copy of this {@link Decimal}.
   *
   * @return A copy of this {@link Decimal}.
   * @complexity O(n)
   */
  @Override
  public Decimal clone() {
    try {
      return (Decimal)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}