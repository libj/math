/* Copyright (c) 2020 LibJ
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
import java.math.RoundingMode;

import org.libj.lang.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fixed point representation of a decimal encoded in a {@code long}.
 */
public final class Decimal extends FixedPoint implements Comparable<Decimal> {
  private static final long serialVersionUID = 3129168059597869867L;
  private static final Logger logger = LoggerFactory.getLogger(Decimal.class);

  /**
   * Returns the minimum scale that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,byte,long)} with the specified number of
   * scale {@code bits}.
   *
   * @param scale The number of bits reserved for the scale.
   * @return The minimum scale that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,byte,long)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,byte,long)
   * @see #minValue(byte)
   */
  public static byte minScaleBits(short scale) {
    if (scale == MIN_SCALE)
      return 16;

    if (scale < 0)
      scale = (short)(-scale);

    if (scale < 128) {
      if (scale < 8) {
        if (scale < 2) {
          if (scale < 1)
            return 0;

          return 2;
        }

        if (scale < 4)
          return 3;

        return 4;
      }

      if (scale < 32) {
        if (scale < 16)
          return 5;

        return 6;
      }

      if (scale < 64)
        return 7;

      return 8;
    }

    if (scale < 2048) {
      if (scale < 512) {
        if (scale < 256)
          return 9;

        return 10;
      }

      if (scale < 1024)
        return 11;

      return 12;
    }

    if (scale < 8192) {
      if (scale < 4096)
        return 13;

      return 14;
    }

    if (scale < 16384)
      return 15;

    return 16;
  }

  /**
   * Returns the string representation of the fixed point decimal specified by
   * the provided {@code value} and {@code scale}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   * @return The string representation of the fixed point decimal specified by
   *         the provided {@code value} and {@code scale}.
   */
  public static String toString(long value, short scale) {
    if (scale == 0)
      return String.valueOf(value);

    final byte precision = Numbers.precision(value);
    int dot;
    if (scale > 0) {
      if (scale >= precision) {
        dot = -1;
      }
      else {
        dot = precision - scale + 1;
        scale = 0;
      }
    }
    else {
      dot = 2;
      scale -= (precision - 1);
    }

    final int scaleLen;
    final int scalePart;
    final boolean scaleNeg;
    if (scale > 0) {
      scaleLen = Numbers.precision(scale);
      scalePart = scaleLen + 2;
      scaleNeg = false;
    }
    else if (scale < 0) {
      scaleLen = Numbers.precision(scale);
      scalePart = scaleLen + 1;
      scaleNeg = true;
      scale = (short)-scale;
    }
    else {
      scaleLen = 0;
      scalePart = 0;
      scaleNeg = false;
    }

    final char[] buf = new char[precision + (dot <= 0 ? 0 : 1) + (value < 0 ? 1 : 0) + scalePart];
    final int lim;
    if (value < 0) {
      lim = 1;
      ++dot;
      buf[0] = '-';
      value = -value;
    }
    else {
      lim = 0;
    }

    int i = buf.length;
    if (scale != 0) {
      for (int j = 0; j < scaleLen; ++j, scale /= 10)
        buf[--i] = (char)(scale % 10 + '0');

      if (!scaleNeg)
        buf[--i] = '-';

      buf[--i] = 'E';
    }

    if (dot > 0) {
      for (; i > dot; value /= 10)
        buf[--i] = (char)(value % 10 + '0');

      buf[--i] = '.';
    }

    for (; i > lim; value /= 10)
      buf[--i] = (char)(value % 10 + '0');

    return new String(buf);
  }

  /**
   * Returns the string representation of the provided
   * {@link Decimal#encode(long,short,byte,long) encoded} value.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The string representation of the provided
   *         {@link Decimal#encode(long,short,byte,long) encoded} value.
   */
  public static String toString(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return toString(value, scale);
  }

  /**
   * Returns a new {@link BigDecimal} instance with the value of the provided
   * {@link Decimal#encode(long,short,byte,long) encoded} value and and sign
   * {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return A new {@link BigDecimal} instance with the value of the provided
   *         {@link Decimal#encode(long,short,byte,long) encoded} value and and
   *         sign {@code bits}.
   */
  public static BigDecimal toBigDecimal(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return BigDecimal.valueOf(value, scale);
  }

  /**
   * Returns a {@code float} representation of the provided
   * {@link Decimal#encode(long,short,byte,long) encoded} value and and sign
   * {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return A {@code float} representation of the provided
   *         {@link Decimal#encode(long,short,byte,long) encoded} value and and
   *         sign {@code bits}.
   */
  public static float floatValue(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return floatValue(value, scale);
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
    return (float)doubleValue(value, scale);
  }

  /**
   * Returns a {@code double} representation of the provided
   * {@link Decimal#encode(long,short,byte,long) encoded} value and and sign
   * {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return A {@code double} representation of the provided
   *         {@link Decimal#encode(long,short,byte,long) encoded} value and and
   *         sign {@code bits}.
   */
  public static double doubleValue(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return doubleValue(value, scale);
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
    return value * StrictMath.pow(10, -scale);
  }

  /**
   * Returns the {@link Decimal#encode(long,short,byte,long) encoded} value with
   * its scale set to {@code scale}. The unscaled value is determined by
   * multiplying or dividing this the unscaled value of {@code encoded} by the
   * appropriate power of ten to maintain its overall value. If the scale is
   * reduced by the operation, the unscaled value must be divided (rather than
   * multiplied), and the value may be changed; in this case, the value will be
   * rounded down.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param scale The scale component.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The {@link Decimal#encode(long,short,byte,long) encoded} value with
   *         its scale set to {@code scale}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long setScale(final long encoded, final short scale, final byte bits, final long defaultValue) {
    return setScale(encoded, scale, bits, defaultValue, null);
  }

  /**
   * Returns the {@link Decimal#encode(long,short,byte,long) encoded} value with
   * its scale set to {@code scale}. The unscaled value is determined by
   * multiplying or dividing this the unscaled value of {@code encoded} by the
   * appropriate power of ten to maintain its overall value. If the scale is
   * reduced by the operation, the unscaled value must be divided (rather than
   * multiplied), and the value may be changed; in this case, the value will be
   * rounded down.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param scale The scale component.
   * @param scaleBits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @param roundingMode The rounding mode to apply.
   * @return The {@link Decimal#encode(long,short,byte,long) encoded} value with
   *         its scale set to {@code scale}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long setScale(final long encoded, final short scale, final byte scaleBits, final long defaultValue, final RoundingMode roundingMode) {
    // FIXME: RoundingMode is not being considered
    final short s = decodeScale(encoded, scaleBits);
    if (s == scale)
      return encoded;

    final int minScale = Decimal.minScale[scaleBits];
    final int maxScale = Decimal.maxScale[scaleBits];
    if (scale < minScale || maxScale < scale) {
      if (logger.isDebugEnabled())
        logger.debug("The scale of " + toString(encoded, scaleBits) + " cannot be set to " + scale + " with " + scaleBits + " scale bits");

      return defaultValue;
    }

    long v = decodeValue(encoded, scaleBits);
    if (v == 0)
      return encode(v, scale, scaleBits, defaultValue);

    final byte precision = Numbers.precision(v);
    if (scale < s - precision)
      return encode(0, scale, scaleBits, defaultValue);

    final int ds = s - scale;
    if (ds > precision || ds < -18 || precision - ds > 19) {
      if (logger.isDebugEnabled())
        logger.debug("The scale of " + toString(encoded, scaleBits) + " cannot be set to " + scale + " with " + scaleBits + " scale bits");

      return defaultValue;
    }

    final byte valueBits = valueBits(scaleBits);
    final boolean isPositive = v >= 0;
    final long minValue = Decimal.minValue(valueBits);
    final long maxValue = Decimal.maxValue(valueBits);
    if (ds < 0) {
      v *= FastMath.e10[-ds];
      if (isPositive ? v > maxValue : v < minValue) {
        if (logger.isDebugEnabled())
          logger.debug("The scale of " + toString(encoded, scaleBits) + " cannot be set to " + scale + " with " + scaleBits + " scale bits");

        return defaultValue;
      }
    }
    else {
      final int adj = ds - 1; // Leave one factor for rounding
      if (adj > 0)
        v /= FastMath.e10[adj];

      v = roundDown10(v);
    }

    return encode(v, scale, scaleBits, defaultValue);
  }

  /**
   * Returns the <i>precision</i> of the specified
   * {@link Decimal#encode(long,short,byte,long) encoded} value. (The precision
   * is the number of digits in the unscaled value.)
   * <p>
   * The precision of a zero value is {@code 1}.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The scale component from the
   *         {@link Decimal#encode(long,short,byte,long) encoded} value with the
   *         given number of sign {@code bits}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static short precision(final long encoded, final byte bits) {
    return encoded == 0 ? 1 : Numbers.precision(decodeValue(encoded, bits));
  }

  /**
   * Returns the smaller of two {@link Decimal#encode(long,short,byte,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param ld1 The first value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param ld2 The second value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param bits The number of bits reserved for the scale.
   * @return The smaller of {@code ld1} and {@code ld2}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static long min(final long ld1, final long ld2, final byte bits) {
    return lte(ld1, ld2, bits) ? ld1 : ld2;
  }

  /**
   * Returns the larger of two {@link Decimal#encode(long,short,byte,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param ld1 The first value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param ld2 The second value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param bits The number of bits reserved for the scale.
   * @return The larger of {@code ld1} and {@code ld2}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static long max(final long ld1, final long ld2, final byte bits) {
    return gte(ld1, ld2, bits) ? ld1 : ld2;
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#encode(long,short,byte,long)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = ld1 == ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if the argument (encoded with
   *         {@link Decimal#encode(long,short,byte,long)})s are equal to each
   *         other and {@code false} otherwise.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean eq(final long ld1, final long ld2, final byte bits) {
    if (ld1 == ld2)
      return true;

    final long v1 = decodeValue(ld1, bits);
    final long v2 = decodeValue(ld2, bits);
    final short s1 = decodeScale(ld1, bits);
    final short s2 = decodeScale(ld2, bits);
    return v1 == v2 && s1 == s2;
  }

  /**
   * Returns {@code true} if the arguments (encoded with
   * {@link Decimal#encode(long,short,byte,long)} are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = ld1 == ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if the argument (encoded with
   *         {@link Decimal#encode(long,short,byte,long)})s are equal to each
   *         other and {@code false} otherwise.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean eq(final long v1, final short s1, final long v2, final short s2) {
    return v1 == v2 && s1 == s2;
  }

  /**
   * Compares two value (encoded with
   * {@link Decimal#encode(long,short,byte,long)})s numerically.
   *
   * @param ld1 The first value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param ld2 The second value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return The value {@code 0} if {@code ld1 == ld2}; a value less than
   *         {@code 0} if {@code ld1 < ld2}; and a value greater than {@code 0}
   *         if {@code ld1 > ld2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static int compare(final long ld1, final long ld2, final byte bits) {
    long v1 = decodeValue(ld1, bits);
    long v2 = decodeValue(ld2, bits);
    if (v1 == 0)
      return v2 > 0 ? -1 : v2 == 0 ? 0 : 1;

    if (v2 == 0)
      return v1 < 0 ? -1 : v1 == 0 ? 0 : 1;

    short s1 = decodeScale(ld1, bits);
    short s2 = decodeScale(ld2, bits);
    if (s1 == s2 || v1 < 0 != v2 < 0)
      return v1 < v2 ? -1 : 1;

    return compare0(v1, s1, v2, s2);
  }

  /**
   * Compares two value (encoded with
   * {@link Decimal#encode(long,short,byte,long)})s numerically.
   *
   * @param ld1 The first value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param ld2 The second value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to compare.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return The value {@code 0} if {@code ld1 == ld2}; a value less than
   *         {@code 0} if {@code ld1 < ld2}; and a value greater than {@code 0}
   *         if {@code ld1 > ld2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static int compare(long v1, short s1, long v2, short s2) {
    if (v1 == 0)
      return v2 > 0 ? -1 : v2 == 0 ? 0 : 1;

    if (v2 == 0)
      return v1 < 0 ? -1 : v1 == 0 ? 0 : 1;

    if (s1 == s2 || v1 < 0 != v2 < 0)
      return v1 < v2 ? -1 : 1;

    return compare0(v1, s1, v2, s2);
  }

  private static int compare0(long v1, short s1, long v2, short s2) {
    s1 -= Numbers.precision(v1);
    s2 -= Numbers.precision(v2);
    if (s1 != s2)
      return (v1 < 0 ? s1 < s2 : s1 > s2) ? -1 : 1;

    final int ds = s1 - s2;
    if (ds > 0)
      v1 /= FastMath.e10[ds];
    else
      v2 /= FastMath.e10[-ds];

    return v1 < v2 ? -1 : v1 == v2 ? 0 : 1;
  }

  /**
   * Returns {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = ld1 < ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean lt(final long ld1, final long ld2, final byte bits) {
    return compare(ld1, ld2, bits) < 0;
  }

  /**
   * Returns {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = ld1 < ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean lte(final long ld1, final long ld2, final byte bits) {
    return compare(ld1, ld2, bits) <= 0;
  }

  /**
   * Returns {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = ld1 < ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean gt(final long ld1, final long ld2, final byte bits) {
    return compare(ld1, ld2, bits) > 0;
  }

  /**
   * Returns {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   * {@code false}.
   *
   * <pre>
   * {@code result = ld1 < ld2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if {@code ld1} is less than {@code ld2}, otherwise
   *         {@code false}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static boolean gte(final long ld1, final long ld2, final byte bits) {
    return compare(ld1, ld2, bits) >= 0;
  }

  /**
   * Returns the absolute value of the argument (encoded with
   * {@link Decimal#encode(long,short,byte,long)}.
   * <p>
   * If the argument is not negative, the argument is returned.
   * <p>
   * If the argument is negative, the negation of the argument is returned.
   * <p>
   * <b>Note:</b> If the argument is equal to the most negative representable
   * value for the specified {@code bits} (i.e. {@link Decimal#minValue(byte)
   * minValue(bits)}), {@code defaultValue} is returned.
   *
   * @param d The value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The absolute value of the argument.
   */
  public static long abs(final long d, final byte bits, final long defaultValue) {
    final long value = decodeValue(d, bits);
    return value >= 0 ? d : neg0(value, decodeScale(d, bits), bits, defaultValue);
  }

  /**
   * Returns the result of the negation of {@code d}, i.e.:
   *
   * <pre>
   * {@code result = -d}
   * </pre>
   *
   * <b>Note:</b> If the argument is equal to the most negative representable
   * value for the specified {@code bits} (i.e. {@link Decimal#minValue(byte)
   * minValue(bits)}), {@code defaultValue} is returned.
   *
   * @param d The value (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}) to negate.
   * @param scaleBits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the negation of {@code d}, i.e.: {@code - d}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long neg(final long d, final byte scaleBits, final long defaultValue) {
    return neg0(decodeValue(d, scaleBits), decodeScale(d, scaleBits), scaleBits, defaultValue);
  }

  // FIXME: It's possible that the negation of `value` will exceed `maxValue`
  // FIXME: Should we reduce in precision to accommodate?!
  private static long neg0(final long value, final short scale, final byte scaleBits, final long defaultValue) {
    if (value == 0)
      return value;

    final long neg = -value;
    if (neg == value)
      return defaultValue;

    return encode(neg, scale, scaleBits, defaultValue);
  }

  /**
   * Returns the result of the subtraction of {@code ld2} from {@code ld1},
   * i.e.:
   *
   * <pre>
   * {@code result = d1 - d2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param scaleBits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the subtraction of {@code ld2} from {@code ld1},
   *         i.e.: {@code d1 - d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long sub(final long ld1, final long ld2, final byte scaleBits, final long defaultValue) {
    if (ld2 == 0)
      return ld1;

    if (ld1 == ld2)
      return 0;

    final long v2 = decodeValue(ld2, scaleBits);
    if (v2 == 0)
      return ld1;

    final short s2 = decodeScale(ld2, scaleBits);
    final long v1 = decodeValue(ld1, scaleBits);
    final short s1 = decodeScale(ld1, scaleBits);
    if (v1 == v2 && s1 == s2)
      return 0;

    final byte valueBits = valueBits(scaleBits);
    final long minValue = Decimal.minValue(valueBits);
    final long maxValue = Decimal.maxValue(valueBits);
    final short minScale = minScale(scaleBits);
    final short maxScale = maxScale(scaleBits);
    final byte maxPrecision = s1 == s2 ? -1 : valueBits;
    final Decimal result = threadLocal.get();
    if (v2 == minValue) {
      if (!add0(-v1, s1, v2, s2, maxPrecision, minValue, maxValue, minScale, maxScale, true, result))
        return defaultValue;
    }
    else if (!add0(v1, s1, -v2, s2, maxPrecision, minValue, maxValue, minScale, maxScale, false, result)) {
      return defaultValue;
    }

    return result.encode(scaleBits, defaultValue);
  }

  /**
   * Returns the result of the addition of {@code ld1} to {@code ld2}, i.e.:
   *
   * <pre>
   * {@code result = d1 + d2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the addition of {@code ld1} to {@code ld2}, i.e.:
   *         {@code d1 + d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long add(final long ld1, final long ld2, final byte bits, final long defaultValue) {
    return ld1 == 0 ? ld2 : ld2 == 0 ? ld1 : add0(ld1, ld2, bits, defaultValue);
  }

  public static Decimal add(final Decimal d1, final Decimal d2) {
    return add0(d1, d2.value, d2.scale);
  }

  public static Decimal sub(final Decimal d1, final Decimal d2) {
    return add0(d1, -d2.value, d2.scale);
  }

  /**
   * Returns the result of the multiplication of {@code ld1} and {@code ld2},
   * i.e.:
   *
   * <pre>
   * {@code result = d1 * d2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param scaleBits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the multiplication of {@code ld1} and {@code ld2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long mul(final long ld1, final long ld2, final byte scaleBits, final long defaultValue) {
    if (ld1 == 0 || ld2 == 0)
      return 0;

    if (scaleBits == 0) {
      final long v = multiplyNonZero(ld1, ld2, Long.MIN_VALUE, Long.MAX_VALUE);
      if (v != 0)
        return v;

      if (logger.isDebugEnabled())
        logger.debug("result cannot be represented with " + scaleBits + " scale bits");

      return defaultValue;
    }

    long v1 = decodeValue(ld1, scaleBits);
    if (v1 == 0)
      return 0;

    long v2 = decodeValue(ld2, scaleBits);
    if (v2 == 0)
      return 0;

    short s1 = decodeScale(ld1, scaleBits);
    short s2 = decodeScale(ld2, scaleBits);

    // if v1 has trailing zeroes, remove them first
    final byte z1 = Numbers.trailingZeroes(v1);
    if (z1 > 0) {
      v1 /= FastMath.e10[z1];
      s1 -= z1;
    }

    // if v2 has trailing zeroes, remove them first
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    // FIXME: Check this logic here... is this necessary considering the beefed up mul0?!
    final long av1 = Math.abs(v1);
    final long av2 = Math.abs(v2);

    // Preliminary check to make sure the product of the values
    // can be represented with the given number of bits
    final byte p1 = FastMath.log2(av1);
    final byte p2 = FastMath.log2(av2);
    final int pow = p1 + p2;
    final byte bitsRequired;
    if (pow > 110) // 55 + 55
      bitsRequired = 6;
    else if (pow > 86) // 43 + 43
      bitsRequired = 5;
    else if (pow > 73) // 37 + 36
      bitsRequired = 4;
    else if (pow > 66) // 33 + 33
      bitsRequired = 3;
    else if (pow > 63) // 32 + 31
      bitsRequired = 2;
    else
      bitsRequired = 0;

    if (scaleBits < bitsRequired) {
      if (logger.isDebugEnabled())
        logger.debug("result cannot be represented with less than " + bitsRequired + " scale bits");

      return defaultValue;
    }

    final Decimal result = threadLocal.get();
    if (mul0(v1, s1, v2, s2, valueBits(scaleBits), result))
      return encode(result.value, result.scale, scaleBits, defaultValue);

    if (logger.isDebugEnabled())
      logger.debug("value=" + result.value + " scale=" + result.scale + " cannot be represented with " + scaleBits + " scale bits");

    return defaultValue;
  }

  public static Decimal mul(final Decimal d1, final Decimal d2) {
    long v1 = d1.value;
    short s1 = d1.scale;
    long v2 = d2.value;
    short s2 = d2.scale;

    // if v1 has trailing zeroes, remove them first
    final byte z1 = Numbers.trailingZeroes(v1);
    if (z1 > 0) {
      v1 /= FastMath.e10[z1];
      s1 -= z1;
    }

    // if v2 has trailing zeroes, remove them first
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    return mul(v1, s1, v2, s2);
  }

  public static Decimal mul(long v1, short s1, long v2, short s2) {
    // if v1 has trailing zeroes, remove them first
    final byte z1 = Numbers.trailingZeroes(v1);
    if (z1 > 0) {
      v1 /= FastMath.e10[z1];
      s1 -= z1;
    }

    // if v2 has trailing zeroes, remove them first
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    final Decimal result = threadLocal.get();
    if (mul0(v1, s1, v2, s2, MAX_PRECISION_B, result))
      return new Decimal(result);

    return null;
  }


  /**
   * Returns the result of the division of {@code ld1} by {@code ld2}, i.e.:
   *
   * <pre>
   * {@code result = d1 / d2}
   * </pre>
   *
   * @param ld1 The first argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param ld2 The second argument (encoded with
   *          {@link Decimal#encode(long,short,byte,long)}).
   * @param scaleBits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the multiplication of {@code ld1} and {@code ld2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long div(final long ld1, final long ld2, final byte scaleBits, final long defaultValue) {
    // Division by zero
    if (ld2 == 0)
      return defaultValue;

    long v2 = decodeValue(ld2, scaleBits);
    // Division by zero
    if (v2 == 0)
      return defaultValue;

    // Division of zero
    if (ld1 == 0)
      return 0;

    long v1 = decodeValue(ld1, scaleBits);
    // Division of zero
    if (v1 == 0)
      return 0;

    short s1 = decodeScale(ld1, scaleBits);
    short s2 = decodeScale(ld2, scaleBits);

    final Decimal result = threadLocal.get();
    if (div0(v1, s1, v2, s2, valueBits(scaleBits), result))
      return encode(result.value, result.scale, scaleBits, defaultValue);

    if (logger.isDebugEnabled())
      logger.debug("value=" + result.value + " scale=" + result.scale + " cannot be represented with " + scaleBits + " scale bits");

    return defaultValue;
  }

  public static Decimal div(final Decimal d1, final Decimal d2) {
    final Decimal result = threadLocal.get();
    if (div0(d1.value, d1.scale, d2.value, d2.scale, MAX_PRECISION_B, result))
      return new Decimal(result);

    return null;
  }

  final long[] buf = new long[2];
  final int[] zds = new int[5];
  // FIXME: Make these private
  long value;
  short scale;
  String error;
  private float floatValue = Float.NaN;
  private double doubleValue = Double.NaN;

  /**
   * Creates a new {@link Decimal} with the specified unscaled {@code value} and
   * {@code scale}.
   *
   * @param value The unscaled value.
   * @param scale The scale.
   */
  public Decimal(final long value, final short scale) {
    this.value = value;
    this.scale = scale;
  }

  // FIXME: Make this private
  Decimal(final Decimal copy) {
    this.value = copy.value;
    this.scale = copy.scale;
  }

  // FIXME: Make this private
  Decimal() {
  }

  // FIXME: Make this private
  Decimal error(final String error) {
    this.error = error;
    return this;
  }

  // FIXME: Make this private
  Decimal set(final Decimal copy) {
    this.value = copy.value;
    this.scale = copy.scale;
    return this;
  }

  // FIXME: Make this private
  Decimal set(final long value, final short scale) {
    this.error = null;
    this.value = value;
    this.scale = scale;
    return this;
  }

  public Decimal mul(final Decimal multiplicand) {
    return mul(this, multiplicand);
  }

  @Override
  public int intValue() {
    return (int)value;
  }

  @Override
  public long longValue() {
    return value;
  }

  @Override
  public float floatValue() {
    return Float.isNaN(floatValue) ? floatValue = floatValue(value, scale) : floatValue;
  }

  @Override
  public double doubleValue() {
    return Double.isNaN(doubleValue) ? doubleValue = doubleValue(value, scale) : doubleValue;
  }

  public long encode(final byte bits, final long defaultValue) {
    return encode(value, scale, bits, defaultValue);
  }

  @Override
  public int compareTo(final Decimal o) {
    return compare(value, scale, o.value, o.scale);
  }

  @Override
  public int hashCode() {
    return 31 * Long.hashCode(value) + Short.hashCode(scale);
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

  @Override
  public String toString() {
    return toString(value, scale);
  }
}