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

import org.libj.lang.Numbers;

/**
 * Abstract class implementing the fixed point representation of a decimal
 * encoded in a {@code long}. A decimal is encoded into a {@code long} in 2
 * components:
 * <ul>
 * <li>value := The numerical value of the decimal. The decimal's precision is
 * this component.</li>
 * <li>scale := The inverse scale by which {@code value} is to be multiplied by
 * a power of {@code 10} (i.e. <code>10<sup>-scale</sup></code>)</li>
 * </ul>
 * <p>
 * Please refer to {@link Decimals} for concrete implementations of this class
 * that expose static functions that consume or produce {@code long}-encoded
 * decimals.
 */
public abstract class Decimal extends FixedPoint implements Comparable<Decimal>, Cloneable {
  private static final long serialVersionUID = 3129168059597869867L;

  /**
   * Returns the absolute value of the argument (encoded with
   * {@link Decimal#encode(long,short,long,byte)}.
   * <p>
   * If the argument is not negative, the argument is returned.
   * <p>
   * If the argument is negative, the negation of the argument is returned.
   * <p>
   * <b>Note:</b> If the argument is equal to the most negative representable
   * value for the specified {@code scaleBits} (i.e.
   * {@link Decimal#minValue(int) minValue(bits)}), {@code defaultValue} is
   * returned.
   *
   * @param encoded The value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The absolute value of the argument.
   */
  static long abs(final long encoded, final long defaultValue, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    return value >= 0 ? encoded : neg0(value, scale(encoded, scaleBits), defaultValue, scaleBits);
  }

  public Decimal abs() {
    clear();
    if (value == Long.MIN_VALUE) {
      value /= -10;
      --scale;
    }
    else {
      value = Math.abs(value);
    }

    return this;
  }

  /**
   * Returns the result of the negation of {@code d}, i.e.:
   *
   * <pre>
   * {@code result = -d}
   * </pre>
   *
   * <b>Note:</b> If the argument is equal to the most negative representable
   * value for the specified {@code scaleBits} (i.e.
   * {@link Decimal#minValue(int) minValue(bits)}), {@code defaultValue} is
   * returned.
   *
   * @param encoded The value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to negate.
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The result of the negation of {@code d}, i.e.: {@code - d}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static long neg(final long encoded, final long defaultValue, final byte scaleBits) {
    return neg0(value(encoded, scaleBits), scale(encoded, scaleBits), defaultValue, scaleBits);
  }

  private static long neg0(long value, short scale, final long defaultValue, final byte scaleBits) {
    if (value == 0)
      return value;

    if (value == Long.MIN_VALUE) {
      value /= -10;
      --scale;
    }
    else {
      value = Math.abs(value);
    }

    return encode(value, scale, defaultValue, scaleBits);
  }

  public static Decimal valueOf(final String str) {
    final char[] chars = str.toCharArray();
    long value = 0, tmpVal;
    short scale = 0;
    int i = 0, dot = 0, end = chars.length;
    boolean isNeg = false;
    boolean isNegScale = false;
    boolean hasScale = false;
    boolean valFull = false;
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
      else if (ch0 == 'E') {
        if (hasScale)
          return null;

        if (!valFull)
          end = i;

        hasScale = true;
      }
      else if (ch0 == '+' && ch1 != 'E') {
        return null;
      }
      else if (ch0 < '0' || '9' < ch0) {
        return null;
      }
      else {
        if (hasScale) {
          scale *= 10;
          scale += ch0 - '0';
          // Check for overflow or underflow
          if (scale < 0)
            return null;
        }
        else if (!valFull) {
          tmpVal = value;
          value *= 10;
          value += ch0 - '0';
          if (value < 0) {
            end = i;
            value = tmpVal;
            valFull = true;
          }
        }
      }
    }

    final int ds = end - dot - 1;
    if (ds < 0) {
      if (Long.MAX_VALUE + ds < scale)
        return null;
    }
    else {
      if (Long.MIN_VALUE + ds > scale)
        return null;
    }

    scale -= ds;
    if (!isNegScale && scale != 0) {
      if (scale == Short.MIN_VALUE)
        return null;

      scale = (short)-scale;
    }

    if (isNeg) {
      if (value == Long.MIN_VALUE) {
        value = roundHalfUp10(value);
        --scale;
      }

      value = -value;
    }

    return new Decimals.Decimal(value, scale);
  }

  // FIXME: Make these private
  long value;
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
   * Creates a new {@link Decimal} with the specified unscaled {@code value} and
   * {@code scale}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   */
  Decimal(final long value, final short scale) {
    this.value = value;
    this.scale = scale;
  }

  Decimal(final long encoded, final byte scaleBits) {
    this.value = value(encoded, scaleBits);
    this.scale = scale(encoded, scaleBits);;
  }

  Decimal(final Decimal copy) {
    this.value = copy.value;
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

  long encode(final byte scaleBits, final long defaultValue, final long minValue, final long maxValue, final short minScale, final short maxScale) {
    return encode(value, scale, defaultValue, scaleBits, minValue, maxValue, minScale, maxScale);
  }

  public Decimal assign(final Decimal copy) {
    clear();
    this.value = copy.value;
    this.scale = copy.scale;
    return this;
  }

  public Decimal assign(final long value, final short scale) {
    clear();
    this.value = value;
    this.scale = scale;
    return this;
  }

  public static Decimal add(final long v1, final short s1, final long v2, final short s2) {
    final Decimal result = threadLocal.get();
    if (add0(v1, s1, v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, false, result))
      return new Decimals.Decimal(result);

    return null;
  }

  /**
   * Returns the result of the addition of {@code d1} to {@code d2}, i.e.:
   *
   * <pre>
   * {@code result = d1 + d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The result of the addition of {@code d1} to {@code d2}, i.e.:
   *         {@code d1 + d2}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static long add(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
    return dec1 == 0 ? dec2 : dec2 == 0 ? dec1 : add0(dec1, dec2, defaultValue, scaleBits);
  }

  public static Decimal add(final Decimal dec, final Decimal add) {
    return add0(dec, add.value, add.scale);
  }

  public Decimal add(final Decimal add) {
    return add(this, add);
  }

  public static Decimal sub(final long v1, final short s1, final long v2, final short s2) {
    final Decimal result = threadLocal.get();
    if (v2 == Long.MIN_VALUE) {
      if (!add0(-v1, s1, v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, true, result))
        return null;
    }
    else if (!add0(v1, s1, -v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, false, result)) {
      return null;
    }

    return new Decimals.Decimal(result);
  }

  /**
   * Returns the result of the subtraction of {@code d2} from {@code d1}, i.e.:
   *
   * <pre>
   * {@code result = d1 - d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The result of the subtraction of {@code d2} from {@code d1}, i.e.:
   *         {@code d1 - d2}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static long sub(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
    if (dec2 == 0)
      return dec1;

    if (dec1 == dec2)
      return 0;

    final long v2 = value(dec2, scaleBits);
    if (v2 == 0)
      return dec1;

    final short s2 = scale(dec2, scaleBits);
    final long v1 = value(dec1, scaleBits);
    if (v1 == 0) {
      if (s2 == 0 && v2 == Long.MAX_VALUE)
        return defaultValue;

      return encode(-v2, s2, defaultValue, scaleBits);
    }

    final short s1 = scale(dec1, scaleBits);
    if (v1 == v2 && s1 == s2)
      return 0;

    final int valueBits = valueBits(scaleBits);
    final long minValue = minValue(valueBits);
    final long maxValue = maxValue(valueBits);
    final short minScale = minScale(scaleBits);
    final short maxScale = maxScale(scaleBits);
    final Decimal result = threadLocal.get();
    if (v2 == minValue) {
      if (!add0(-v1, s1, v2, s2, minValue, maxValue, minScale, maxScale, true, result))
        return defaultValue;
    }
    else if (!add0(v1, s1, -v2, s2, minValue, maxValue, minScale, maxScale, false, result)) {
      return defaultValue;
    }

    return result.encode(scaleBits, defaultValue, minValue, maxValue, minScale, maxScale);
  }

  public static Decimal sub(final Decimal dec, final Decimal sub) {
    return add0(dec, -sub.value, sub.scale);
  }

  public Decimal sub(final Decimal sub) {
    return sub(this, sub);
  }

  public static Decimal mul(final long v1, final short s1, final long v2, final short s2) {
    final Decimal result = threadLocal.get();
    if (mul0(v1, s1, v2, s2, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, result))
      return new Decimals.Decimal(result);

    return null;
  }

  /**
   * Returns the result of the multiplication of {@code d1} and {@code d2},
   * i.e.:
   *
   * <pre>
   * {@code result = d1 * d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The result of the multiplication of {@code d1} and {@code d2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static long mul(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
    if (dec1 == 0 || dec2 == 0)
      return 0;

    long v1 = value(dec1, scaleBits);
    if (v1 == 0)
      return 0;

    long v2 = value(dec2, scaleBits);
    if (v2 == 0)
      return 0;

    short s1 = scale(dec1, scaleBits);
    short s2 = scale(dec2, scaleBits);

    final short minScale = FixedPoint.minScale[scaleBits];
    final short maxScale = FixedPoint.maxScale[scaleBits];
    final int valueBits = valueBits(scaleBits);
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);
    final Decimal result = threadLocal.get();
    if (mul0(v1, s1, v2, s2, minValue, maxValue, minScale, maxScale, result))
      return encode(result.value, result.scale, defaultValue, scaleBits);

    return defaultValue;
  }

  public Decimal mul(final Decimal mul) {
    if (value == 0)
      return this;

    final long v2 = mul.value;
    if (v2 == 0)
      return assign(0, (short)0);

    if (mul0(value, scale, v2, mul.scale, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, this))
      return this;

    return null;
  }
  /**
   * Returns the result of the division of {@code d1} by {@code d2}, i.e.:
   *
   * <pre>
   * {@code result = d1 / d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The result of the multiplication of {@code d1} and {@code d2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static long div(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
    long v2 = value(dec2, scaleBits);
    // Division by zero
    if (v2 == 0)
      return defaultValue;

    long v1 = value(dec1, scaleBits);
    // Division of zero
    if (v1 == 0)
      return 0;

    short s1 = scale(dec1, scaleBits);
    short s2 = scale(dec2, scaleBits);

    final short minScale = FixedPoint.minScale[scaleBits];
    final short maxScale = FixedPoint.maxScale[scaleBits];
    final int valueBits = valueBits(scaleBits);
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);
    final Decimal result = threadLocal.get();
    if (div0(v1, s1, v2, s2, minValue, maxValue, minScale, maxScale, result))
      return encodeInPlace(result.value, result.scale, defaultValue, scaleBits);

    return defaultValue;
  }

  public static Decimal div(final Decimal dec, final Decimal div) {
    long v1 = dec.value;
    if (v1 == 0)
      return dec;

    long v2 = div.value;
    if (v2 == 0)
      return dec.assign(0, (short)0);

    if (div0(v1, dec.scale, v2, div.scale, Long.MIN_VALUE, Long.MAX_VALUE, MIN_SCALE, MAX_SCALE, dec))
      return dec;

    return null;
  }

  public Decimal div(final Decimal div) {
    return div(this, div);
  }

  static long rem(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
    final long v2 = value(dec2, scaleBits);
    // Division by zero
    if (v2 == 0)
      return defaultValue;

    final long v1 = value(dec1, scaleBits);
    // Division of zero
    if (v1 == 0)
      return 0;

    final short s1 = scale(dec1, scaleBits);
    final short s2 = scale(dec2, scaleBits);
    final Decimal result = threadLocal.get();
    if (rem0(v1, s1, v2, s2, result))
      return encode(result.value, result.scale, defaultValue, scaleBits);

    return defaultValue;
  }

  public static Decimal rem(final Decimal dec, final Decimal div) {
    final long v1 = dec.value;
    // Division by zero
    if (v1 == 0)
      return dec;

    // Division of zero
    final long v2 = div.value;
    if (v2 == 0)
      return dec.assign(0, (short)0);

    if (rem0(v1, dec.scale, v2, div.scale, dec))
      return dec;

    return null;
  }

  public Decimal rem(final Decimal div) {
    return rem(this, div);
  }

  public static byte signum(final long value) {
    return value < 0 ? -1 : value == 0 ? 0 : (byte)1;
  }

  static byte signum(final long encoded, final byte scaleBits) {
    // FIXME: I think we can just do < 0
    return signum(value(encoded, scaleBits));
  }

  public byte signum() {
    return signum(value);
  }

  /**
   * Returns the <i>precision</i> of the specified
   * {@link Decimal#encode(long,short,long,byte) encoded} value. (The precision
   * is the number of digits in the unscaled value.)
   * <p>
   * The precision of a zero value is {@code 1}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The scale component from the
   *         {@link Decimal#encode(long,short,long,byte) encoded} value with the
   *         given number of sign {@code scaleBits}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   */
  static byte precision(final long encoded, final byte scaleBits) {
    return Numbers.precision(value(encoded, scaleBits));
  }

  public byte precision() {
    return Numbers.precision(value);
  }

  public short scale() {
    return scale;
  }

  /**
   * Returns the {@link Decimal#encode(long,short,long,byte) encoded} value with
   * its scale set to {@code scale}. The unscaled value is determined by
   * multiplying or dividing this the unscaled value of {@code encoded} by the
   * appropriate power of ten to maintain its overall value. If the scale is
   * reduced by the operation, the unscaled value must be divided (rather than
   * multiplied), and the value may be changed; in this case, the value will be
   * rounded down.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @param newScale The scale component.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code scaleBits}.
   * @return The {@link Decimal#encode(long,short,long,byte) encoded} value with
   *         its scale set to {@code scale}.
   * @see #encode(long,short,long,byte)
   * @see #scale(long,byte)
   */
  static long setScale(final long encoded, short newScale, final long defaultValue, final byte scaleBits) {
    final short scale = scale(encoded, scaleBits);
    if (scale == newScale)
      return encoded;

    int ds = scale - newScale;
    if (ds < 0)
      return encoded;

    long value = value(encoded, scaleBits);
    if (value == 0)
      return encoded;

    if (ds > Numbers.precision(value))
      return 0;

    if (--ds > 0) // Leave one factor for rounding
      value /= FastMath.longE10[ds];

    // FIXME: RoundingMode is not being considered
    value = roundHalfUp10(value);
    if (value == 0)
      return 0;

    if (newScale > 0)
      return newScale <= maxScale(scaleBits) ? encode(value, newScale, defaultValue, scaleBits) : defaultValue;

    final short minScale = minScale(scaleBits);
    if (minScale <= newScale)
      return encode(value, newScale, defaultValue, scaleBits);

    final int valueBits = valueBits(scaleBits);
    final long minValue = minValue(valueBits);

    ds = minScale - newScale;
    // How many multiples of 10 until overflow?
    final int dp1 = Numbers.precision(minValue / value) - 1;
    if (ds > dp1)
      return defaultValue;

    value *= FastMath.longE10[ds];
    newScale += ds;
    return encode(value, newScale, defaultValue, scaleBits, minValue, maxValue(valueBits), minScale, maxScale(scaleBits));
  }

  /**
   * Sets the scale of this {@link Decimal} to the provided {@code newScale}.
   * The unscaled value is determined by multiplying or dividing this the
   * unscaled value of {@code encoded} by the appropriate power of ten to
   * maintain its overall value. If the scale is reduced by the operation, the
   * unscaled value must be divided (rather than multiplied), and the value may
   * be changed; in this case, the value will be rounded down.
   *
   * @param newScale The new scale to which this {@link Decimal} is to be set.
   * @return {@code this} {@link Decimal} with its scale set to
   *         {@code newScale}.
   */
  public Decimal setScale(final short newScale) {
    if (scale == newScale || value == 0)
      return this;

    int ds = scale - newScale;
    if (ds < 0)
      return this;

    clear();
    if (ds > Numbers.precision(value)) {
      this.value = 0;
    }
    else {
      if (--ds > 0) // Leave one factor for rounding
        value /= FastMath.longE10[ds];

      // FIXME: RoundingMode is not being considered
      value = roundHalfUp10(value);
    }

    scale = newScale;
    return this;
  }

  /**
   * Compares two value (encoded with
   * {@link Decimal#encode(long,short,long,byte)})s numerically.
   *
   * @param dec1 The first value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param dec2 The second value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return The value {@code 0} if {@code d1 == d2}; a value less than
   *         {@code 0} if {@code d1 < d2}; and a value greater than {@code 0} if
   *         {@code d1 > d2}
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static int compare(final long dec1, final long dec2, final byte scaleBits) {
    long v1 = value(dec1, scaleBits);
    long v2 = value(dec2, scaleBits);
    if (v1 == 0)
      return v2 > 0 ? -1 : v2 == 0 ? 0 : 1;

    if (v2 == 0)
      return v1 < 0 ? -1 : v1 == 0 ? 0 : 1;

    short s1 = scale(dec1, scaleBits);
    short s2 = scale(dec2, scaleBits);
    if (s1 == s2 || v1 < 0 != v2 < 0)
      return v1 < v2 ? -1 : v1 == v2 ? 0 : 1;

    return compare0(v1, s1, v2, s2);
  }

  /**
   * Compares two value (encoded with
   * {@link Decimal#encode(long,short,long,byte)})s numerically.
   *
   * @param v1 The magnitude value of the first argument to equate.
   * @param s1 The scale of the first argument to equate.
   * @param v2 The magnitude value of the second argument to equate.
   * @param s2 The scale of the second argument to equate.
   * @return The value {@code 0} if the two arguments are equal; a value less
   *         than {@code 0} if the first argument is less than the second
   *         argument; and a value greater than {@code 0} if the first argument
   *         is greater than the second argument.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  public static int compare(long v1, short s1, long v2, short s2) {
    if (v1 == 0)
      return v2 > 0 ? -1 : v2 == 0 ? 0 : 1;

    if (v2 == 0)
      return v1 < 0 ? -1 : v1 == 0 ? 0 : 1;

    if (s1 == s2 || v1 < 0 != v2 < 0)
      return v1 < v2 ? -1 : v1 == v2 ? 0 : 1;

    return compare0(v1, s1, v2, s2);
  }

  private static int compare0(long v1, short s1, long v2, short s2) {
    final int p1 = Numbers.precision(v1) - s1;
    final int p2 = Numbers.precision(v2) - s2;
    if (p1 < p2)
      return v1 < 0 ? 1 : -1;

    if (p1 > p2)
      return v1 < 0 ? -1 : 1;

    if (s1 < s2) {
      final int ds = s2 - s1;
      int ds1 = Numbers.precision(Long.MIN_VALUE / v1) - 1;
      if (ds < ds1)
        ds1 = ds;

      v1 *= FastMath.longE10[ds1];
      s1 += ds1;
    }
    else if (s1 > s2) {
      final int ds = s1 - s2;
      int ds2 = Numbers.precision(Long.MIN_VALUE / v2) - 1;
      if (ds < ds2)
        ds2 = ds;

      v2 *= FastMath.longE10[ds2];
      s2 += ds2;
    }

    return v1 < v2 ? -1 : v1 == v2 ? 0 : 1;
  }

  @Override
  public int compareTo(final Decimal dec) {
    return compare(value, scale, dec.value, dec.scale);
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = d1 < d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static boolean gt(final long dec1, final long dec2, final byte scaleBits) {
    return compare(dec1, dec2, scaleBits) > 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = d1 < d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static boolean gte(final long dec1, final long dec2, final byte scaleBits) {
    return compare(dec1, dec2, scaleBits) >= 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = d1 < d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static boolean lt(final long dec1, final long dec2, final byte scaleBits) {
    return compare(dec1, dec2, scaleBits) < 0;
  }

  /**
   * Returns {@code true} if {@code d1} is less than {@code d2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = d1 < d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return {@code true} if {@code d1} is less than {@code d2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static boolean lte(final long dec1, final long dec2, final byte scaleBits) {
    return compare(dec1, dec2, scaleBits) <= 0;
  }

  /**
   * Returns the larger of two {@link Decimal#encode(long,short,long,byte)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param dec1 The first value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param dec2 The second value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The larger of {@code d1} and {@code d2}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   */
  static long max(final long dec1, final long dec2, final byte scaleBits) {
    return gte(dec1, dec2, scaleBits) ? dec1 : dec2;
  }

  public Decimal max(final Decimal dec) {
    return compare(value, scale, dec.value, dec.scale) > 0 ? this : dec;
  }

  /**
   * Returns the smaller of two {@link Decimal#encode(long,short,long,byte)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param dec1 The first value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param dec2 The second value (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}) to compare.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The smaller of {@code d1} and {@code d2}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   */
  static long min(final long dec1, final long dec2, final byte scaleBits) {
    return lte(dec1, dec2, scaleBits) ? dec1 : dec2;
  }

  public Decimal min(final Decimal dec) {
    return compare(value, scale, dec.value, dec.scale) < 0 ? this : dec;
  }

  public static byte byteValue(final long value, final short scale) {
    return (byte)longValue(value, scale);
  }

  static byte byteValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return byteValue(value, scale);
  }

  @Override
  public byte byteValue() {
    if (!byteValueDirty)
      return byteValue;

    byteValueDirty = false;
    return byteValue = byteValue(value, scale);
  }

  public static short shortValue(final long value, final short scale) {
    return (short)longValue(value, scale);
  }

  static short shortValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return shortValue(value, scale);
  }

  @Override
  public short shortValue() {
    if (!shortValueDirty)
      return shortValue;

    shortValueDirty = false;
    return shortValue = shortValue(value, scale);
  }

  public static int intValue(final long value, final short scale) {
    return (int)longValue(value, scale);
  }

  static int intValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return intValue(value, scale);
  }

  @Override
  public int intValue() {
    if (!intValueDirty)
      return intValue;

    intValueDirty = false;
    return intValue = intValue(value, scale);
  }

  public static long longValue(final long value, final short scale) {
    if (value == 0)
      return 0;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / value))
        return value * FastMath.longE10[s];

      final int[] val = BigInt.assignInPlace(buf1.get(), value);
      if (s < 19)
        return BigInt.longValue(BigInt.mulInPlace(val, FastMath.longE10[s]));

      final boolean sig = value >= 0;
      final int len = sig ? val[0] : -val[0];
      final int[] mul = FastMath.E10(s);
      final int mlen = mul[0];
      return BigInt.longValue(BigInt.mulQuad(val, len, mul, mlen, len + mlen + 1, sig, true));
    }
    else if (scale > 0) {
      return scale > 18 ? 0 : value / FastMath.longE10[scale];
    }

    return value;
  }

  static long longValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return longValue(value, scale);
  }

  @Override
  public long longValue() {
    if (!longValueDirty)
      return longValue;

    longValueDirty = false;
    return longValue = longValue(value, scale);
  }

  /**
   * Returns a {@code float} representation of the provided unscaled value
   * {@code long} and scale {@code short}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   * @return A {@code float} representation of the provided unscaled value
   *         {@code long} and scale {@code short}.
   */
  public static float floatValue(final long value, final short scale) {
    if (scale == 0 || value == 0)
      return value;

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / value))
        return value * FastMath.longE10[s];
    }

    return (float)(scale > 0 ? value / FastMath.doubleE10(scale) : value * FastMath.doubleE10(-scale));
  }

  /**
   * Returns a {@code float} representation of the provided
   * {@link Decimal#encode(long,short,long,byte) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return A {@code float} representation of the provided
   *         {@link Decimal#encode(long,short,long,byte) encoded} value and
   *         sign {@code scaleBits}.
   */
  static float floatValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return floatValue(value, scale);
  }

  @Override
  public float floatValue() {
    if (!floatValueDirty)
      return floatValue;

    floatValueDirty = false;
    return floatValue = floatValue(value, scale);
  }

  /**
   * Returns a {@code double} representation of the provided unscaled value
   * {@code long} and scale {@code short}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   * @return A {@code double} representation of the provided unscaled value
   *         {@code long} and scale {@code short}.
   */
  public static double doubleValue(final long value, final short scale) {
    return FloatingDecimal.doubleValue(value, scale);
  }

  /**
   * Returns a {@code double} representation of the provided
   * {@link Decimal#encode(long,short,long,byte) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return A {@code double} representation of the provided
   *         {@link Decimal#encode(long,short,long,byte) encoded} value and
   *         sign {@code scaleBits}.
   */
  static double doubleValue(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return doubleValue(value, scale);
  }

  @Override
  public double doubleValue() {
    if (!doubleValueDirty)
      return doubleValue;

    doubleValueDirty = false;
    return doubleValue = doubleValue(value, scale);
  }

  public int[] toBigInt() {
    return bigInt == null ? bigInt = toBigInt(value, scale) : bigInt;
  }

  static int[] toBigInt(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return toBigInt(value, scale);
  }

  public static int[] toBigInt(long value, final short scale) {
    if (value == 0)
      return new int[] {0};

    if (scale < 0) {
      final int s = -scale;
      if (s < Numbers.precision(Long.MIN_VALUE / value)) {
        value *= FastMath.longE10[s];
      }
      else {
        final int[] val = BigInt.assignInPlace(new int[5], value);
        if (s < 19)
          return BigInt.mulInPlace(val, FastMath.longE10[s]);

        final boolean sig = value >= 0;
        final int len = sig ? val[0] : -val[0];
        final int[] mul = FastMath.E10(s);
        final int mlen = mul[0];
        return BigInt.mulQuad(val, len, mul, mlen, len + mlen + 1, sig, true);
      }
    }
    else if (scale > 0) {
      if (scale > 18)
        return new int[] {0};

      value /= FastMath.longE10[scale];
    }

    return BigInt.assignInPlace(new int[4], value);
  }

  /**
   * Returns a new {@link BigDecimal} instance with the value of the provided
   * value and scale.
   *
   * @param value The value.
   * @param scale The scale.
   * @return A new {@link BigDecimal} instance with the value of the provided
   *         value and scale.
   */
  public static BigDecimal toBigDecimal(final long value, final short scale) {
    return BigDecimal.valueOf(value, scale);
  }

  /**
   * Returns a new {@link BigDecimal} instance with the value of the provided
   * {@link Decimal#encode(long,short,long,byte) encoded} value and sign
   * {@code scaleBits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return A new {@link BigDecimal} instance with the value of the provided
   *         {@link Decimal#encode(long,short,long,byte) encoded} value and
   *         sign {@code scaleBits}.
   */
  static BigDecimal toBigDecimal(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return toBigDecimal(value, scale);
  }

  public BigDecimal toBigDecimal() {
    return bigDecimal == null ? bigDecimal = BigDecimal.valueOf(value).scaleByPowerOfTen(-scale) : bigDecimal;
  }

  public static int hashCode(final long value, final short scale) {
    final int temp;
    if (value < 0) {
      final long abs = -value;
      temp = -(int)((int)(abs >>> 32) * 31 + (abs & BigInt.LONG_MASK));
    }
    else {
      final long abs = value;
      temp = (int)((int)(abs >>> 32) * 31 + (abs & BigInt.LONG_MASK));
    }

    return 31 * temp + scale;
  }

  static int hashCode(final long encoded, final byte scaleBits) {
    return hashCode(value(encoded, scaleBits), scale(encoded, scaleBits));
  }

  @Override
  public int hashCode() {
    return hashCode(value, scale);
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#encode(long,short,long,byte)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = d1 == d2}
   * </pre>
   *
   * @param dec1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param dec2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,long,byte)}).
   * @param scaleBits The number of bits in the encoded {@code long} values
   *          reserved for the scale.
   * @return {@code true} if the arguments (encoded with
   *         {@link Decimal#encode(long,short,long,byte)}) are equal to each
   *         other and {@code false} otherwise.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  static boolean eq(final long dec1, final long dec2, final byte scaleBits) {
    return dec1 == dec2;
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#encode(long,short,long,byte)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = d1 == d2}
   * </pre>
   *
   * @param v1 The magnitude value of the first argument to equate.
   * @param s1 The scale of the first argument to equate.
   * @param v2 The magnitude value of the second argument to equate.
   * @param s2 The scale of the second argument to equate.
   * @return {@code true} if the first argument's value and scale equals the
   *         second argument's value and scale.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   * @see #scale(long,byte)
   */
  public static boolean eq(final long v1, final short s1, final long v2, final short s2) {
    return v1 == v2 && s1 == s2;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Decimal))
      return false;

    final Decimal that = (Decimal)obj;
    return value == that.value && scale == that.scale;
  }

  /**
   * Returns the string representation (in
   * <a href="https://en.wikipedia.org/wiki/Scientific_notation">scientific
   * notation</a>) of the fixed point decimal specified by the provided value
   * and scale.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   * @return The string representation (in <a href=
   *         "https://en.wikipedia.org/wiki/Scientific_notation">scientific
   *         notation</a>) of the fixed point decimal specified by the provided
   *         {@code value} and {@code scale}.
   * @complexity O(n)
   */
  public static String toScientificString(long value, final short scale) {
    if (value == 0)
      return "0";

    int s = scale;
    final byte precision = Numbers.precision(value);
    int dot;
    if (precision > 1) {
      dot = value < 0 ? 3 : 2;
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

    final boolean isNeg = value < 0;
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
        for (long dig; i > dot; value /= 10) {
          dig = -(value % 10);
          if (zeroesStripped || (zeroesStripped = (dig != 0)))
            buf[--i] = (char)(dig + '0');
          else
            ++dot;
        }

        if (j != i)
          buf[--i] = '.';
      }

      buf[--i] = (char)(-(value % 10) + '0');
      if (isNeg)
        buf[--i] = '-';
    }
    else {
      if (dot > 0) {
        boolean zeroesStripped = false;
        final int j = i;
        for (long dig; i > dot; value /= 10) {
          dig = value % 10;
          if (zeroesStripped || (zeroesStripped = (dig != 0)))
            buf[--i] = (char)(dig + '0');
          else
            ++dot;
        }

        if (j != i)
          buf[--i] = '.';
      }

      buf[--i] = (char)((value % 10) + '0');
      if (isNeg)
        buf[--i] = '-';
    }

    return new String(buf, i, buf.length - i);
  }

  /**
   * Returns the string representation (in
   * <a href="https://en.wikipedia.org/wiki/Scientific_notation">scientific
   * notation</a>) of the provided {@link Decimal#encode(long,short,long,byte)
   * encoded} value.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The string representation (in <a href=
   *         "https://en.wikipedia.org/wiki/Scientific_notation">scientific
   *         notation</a>) of the provided
   *         {@link Decimal#encode(long,short,long,byte) encoded} value.
   */
  static String toScientificString(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return toScientificString(value, scale);
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
    return scientificString == null ? scientificString = toScientificString(value, scale) : scientificString;
  }

  /**
   * Returns the string representation of the fixed point decimal specified by
   * the provided {@code value} and {@code scale}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   * @return The string representation of the fixed point decimal specified by
   *         the provided {@code value} and {@code scale}.
   * @complexity O(n)
   */
  public static String toString(long value, final short scale) {
    if (value == 0)
      return "0";

    int s = scale;
    if (s == 0)
      return String.valueOf(value);

    final byte precision = Numbers.precision(value);
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

    final boolean isNeg = value < 0;
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
        for (; i > dot; value /= 10)
          buf[--i] = (char)(-(value % 10) + '0');

        buf[--i] = '.';
      }

      for (; i > lim; value /= 10)
        buf[--i] = (char)(-(value % 10) + '0');
    }
    else {
      if (dot > 0) {
        for (; i > dot; value /= 10)
          buf[--i] = (char)((value % 10) + '0');

        buf[--i] = '.';
      }

      for (; i > lim; value /= 10)
        buf[--i] = (char)((value % 10) + '0');
    }

    return new String(buf);
  }

  /**
   * Returns the string representation of the provided
   * {@link Decimal#encode(long,short,long,byte) encoded} value.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The string representation of the provided
   *         {@link Decimal#encode(long,short,long,byte) encoded} value.
   */
  static String toString(final long encoded, final byte scaleBits) {
    final long value = value(encoded, scaleBits);
    final short scale = scale(encoded, scaleBits);
    return toString(value, scale);
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
    return string == null ? string = toString(value, scale) : string;
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