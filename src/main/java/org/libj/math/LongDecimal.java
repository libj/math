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

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.libj.lang.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fixed point representation of a decimal encoded in a {@code long}.
 */
public final class LongDecimal {
  private static final Logger logger = LoggerFactory.getLogger(LongDecimal.class);
  static final byte maxScaleBits = 17;
  private static final long[] pow2 = new long[maxScaleBits];
  static final short[] minScale = new short[maxScaleBits];
  static final short[] maxScale = new short[maxScaleBits];
  static final byte[] minDigits = new byte[maxScaleBits];
  static final byte[] maxDigits = new byte[maxScaleBits];

  static {
    for (byte b = 0; b < maxScaleBits; ++b) {
      pow2[b] = (long)Math.pow(2, b);
      if (b <= 1) {
        minScale[b] = 0;
        maxScale[b] = 0;
      }
      else {
        final long scale = pow2[b - 2];
        minScale[b] = (short)-scale;
        maxScale[b] = (short)(scale - 1);
      }

      minDigits[b] = Numbers.digits(minValue(b));
      maxDigits[b] = Numbers.digits(maxValue(b));
    }
  }

  private static long mask(final byte bits) {
    return (0xFFFFL << 63 - bits) & 0x7fffffffffffffffL; // Leave the first bit untouched, as that's the sign bit
  }

  private static boolean willProductOverflow(final long x, final long y) {
    return (double)x * (double)y >= Long.MAX_VALUE;
  }

  /**
   * Returns the minimum value that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,byte,long)} with the specified number of
   * scale {@code bits}.
   *
   * @param bits The number of bits reserved for the scale.
   * @return The minimum value that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,byte,long)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,byte,long)
   * @see #minScale(byte)
   */
  public static long minValue(final byte bits) {
    return -2L << 62 - bits;
  }

  /**
   * Returns the maximum value that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,byte,long)} with the specified number of
   * scale {@code bits}.
   *
   * @param bits The number of bits reserved for the scale.
   * @return The maximum value that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,byte,long)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,byte,long)
   * @see #maxScale(byte)
   */
  public static long maxValue(final byte bits) {
    return (2L << 62 - bits) - 1;
  }

  /**
   * Returns the minimum scale that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,byte,long)} with the specified number of
   * scale {@code bits}.
   *
   * @param bits The number of bits reserved for the scale.
   * @return The minimum scale that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,byte,long)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,byte,long)
   * @see #minValue(byte)
   */
  public static short minScale(final byte bits) {
    return LongDecimal.minScale[bits];
  }

  /**
   * Returns the maximum scale that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,byte,long)} with the specified number of
   * scale {@code bits}.
   *
   * @param bits The number of bits reserved for the scale.
   * @return The maximum scale that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,byte,long)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,byte,long)
   * @see #maxValue(byte)
   */
  public static short maxScale(final byte bits) {
    return LongDecimal.maxScale[bits];
  }

  /**
   * Returns the string representation of the provided
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The string representation of the provided
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value.
   */
  public static String toString(final long encoded, final byte bits) {
    long value = decodeValue(encoded, bits);
    short scale = decodeScale(encoded, bits);
    if (scale == 0)
      return String.valueOf(value);

    final byte digits = Numbers.digits(value);
    int dot;
    if (scale > 0) {
      if (scale >= digits) {
        dot = -1;
      }
      else {
        dot = digits - scale + 1;
        scale = 0;
      }
    }
    else {
      dot = 2;
      scale -= (digits - 1);
    }

    final int scaleLen;
    final int scalePart;
    final boolean scaleNeg;
    if (scale > 0) {
      scaleLen = Numbers.digits(scale);
      scalePart = scaleLen + 2;
      scaleNeg = false;
    }
    else if (scale < 0) {
      scaleLen = Numbers.digits(scale);
      scalePart = scaleLen + 1;
      scaleNeg = true;
      scale = (short)-scale;
    }
    else {
      scaleLen = 0;
      scalePart = 0;
      scaleNeg = false;
    }

    final char[] buf = new char[digits + (dot <= 0 ? 0 : 1) + (value < 0 ? 1 : 0) + scalePart];
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
   * Returns a new {@link BigDecimal} instance with the value of the provided
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value and and sign
   * {@code bits}.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return A new {@link BigDecimal} instance with the value of the provided
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value and
   *         and sign {@code bits}.
   */
  public static BigDecimal toBigDecimal(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return BigDecimal.valueOf(value, scale);
  }

  /**
   * Returns a {@code double} representation of the provided
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value and and sign
   * {@code bits}.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return A {@code double} representation of the provided
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value and
   *         and sign {@code bits}.
   */
  public static double toDouble(final long encoded, final byte bits) {
    final long value = decodeValue(encoded, bits);
    final short scale = decodeScale(encoded, bits);
    return value * StrictMath.pow(10, -scale);
  }

  /**
   * Encodes the provided {@link BigDecimal} {@code value} into a {@code long}
   * with the given number of sign {@code bits}.
   *
   * @param value The string representation of a decimal.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final BigDecimal value, final byte bits, final long defaultValue) {
    final int scale = value.scale();
    final int minScale = LongDecimal.minScale[bits];
    final int maxScale = LongDecimal.maxScale[bits];
    if (scale < minScale || maxScale < scale) {
      if (logger.isDebugEnabled())
        logger.debug(value + " cannot be represented with " + bits + " scale bits");

      return defaultValue;
    }

    return encode(value.unscaledValue().longValue(), (short)scale, bits, defaultValue);
  }

  /**
   * Encodes the provided string representation of a decimal {@code value} into a {@code long}
   * with the given number of sign {@code bits}.
   *
   * @param value The string representation of a decimal.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final String value, final byte bits, final long defaultValue) {
    int e = value.indexOf('e');
    if (e < 0)
      e = value.indexOf('E');

    final int dot = value.indexOf('.');
    final int len = value.length();
    int s = e < 0 ? 0 : -Integer.parseInt(value.substring(e + 1));
    if (e < 0)
      e = len;

    if (dot > -1)
      s += e - dot - 1;

    long val = 0;
    if (dot < 0 && e == len) {
      val = Long.parseLong(value);
    }
    else {
      if (dot > 0)
        val = Long.parseLong(value.substring(0, dot));

      if (val != 0) {
        // FIXME: What if: f > 18 ?!
        final byte f = (byte)(e - dot - 1);
        if (e - dot - 1 >= 0)
          val *= FastMath.e10(f);
      }

      val += Long.parseLong(value.substring(dot + 1, e));
    }

    final short maxScale = LongDecimal.maxScale[bits];
    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.digits(val)) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }

      for (int i; adj > 0; adj -= i) {
        i = Math.min(adj, FastMath.e10.length - 1);
        val /= FastMath.e10[i];
        s -= i;
      }

      if (val == 0) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }
    }
    else {
      final int minScale = LongDecimal.minScale[bits];
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.digits(val)) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }

        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.e10.length - 1);
          val *= FastMath.e10[i];
          s += i;
        }

        final long minValue = LongDecimal.minValue(bits);
        final long maxValue = LongDecimal.maxValue(bits);
        if (val < 0 ? val < minValue : maxValue < val) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }
      }
    }

    return encode(val, (short)s, bits, defaultValue);
  }

  /**
   * Encodes the provided {@code value} and {@code scale} into a {@code long}
   * with the given number of sign {@code bits}.
   *
   * @param value The numeric component.
   * @param scale The scale component.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final long value, final short scale, final byte bits, final long defaultValue) {
    if (bits == 0)
      return scale == 0 ? value : defaultValue;

    final long minValue = LongDecimal.minValue(bits);
    final long maxValue = LongDecimal.maxValue(bits);
    if (value < 0 ? value < minValue : maxValue < value) {
      if (logger.isDebugEnabled())
        logger.debug("Value " + value + " is outside permitted min(" + minValue(bits) + ") max(" + maxValue(bits) + ")");

      return defaultValue;
    }

    if (scale < 0 ? scale < LongDecimal.minScale[bits] : LongDecimal.maxScale[bits] < scale) {
      if (logger.isDebugEnabled())
        logger.debug("Scale " + scale + " is greater than bits allow: " + (scale < 0 ? LongDecimal.minScale[bits] : LongDecimal.maxScale[bits]));

      return defaultValue;
    }

    final long scaleMask;
    if (scale < 0)
      scaleMask = ((long)-scale << (63 - bits)) | 1L << 62; // FIXME: Look this over
    else
      scaleMask = ((long)scale << (63 - bits));

//    System.out.println("value: " + Buffers.toString(value));
    final long encoded = value < 0 ? value ^ scaleMask : value | scaleMask;
    if (encoded == defaultValue)
      throw new IllegalArgumentException("Encoded value (" + encoded + ") conflicts with defaultValue (" + defaultValue + ")");

    return encoded;
  }

  /**
   * Decodes the value component from the
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value with the
   * given number of sign {@code bits}.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The value component from the
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value with
   *         the given number of sign {@code bits}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long decodeValue(final long encoded, final byte bits) {
    if (bits == 0)
      return encoded;

    final long scaleMask = mask(bits);
//    System.out.println("encoded: " + Buffers.toString(encoded));
//    System.out.println("sleMask: " + Buffers.toString(scaleMask));
    return encoded < 0 ? encoded | scaleMask : encoded & ~scaleMask;
  }

  /**
   * Decodes the scale component from the
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value with the
   * given number of sign {@code bits}.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The scale component from the
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value with
   *         the given number of sign {@code bits}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static short decodeScale(final long encoded, final byte bits) {
    if (bits == 0)
      return 0;

    final long scaleMask = mask(bits);
    final short scale = (short)(encoded < 0 ? ~((encoded | ~scaleMask) >> 63 - bits) : (encoded & scaleMask) >> 63 - bits);
//    System.out.println("SMask: " + Buffers.toString(scaleMask) + " " + scale);
    final int sign = scale & ((byte)1 << bits - 1);
    return sign == 0 ? scale : (short)-(scale & ~(1 << bits - 1));
  }

  /**
   * Returns the {@link LongDecimal#encode(long,short,byte,long) encoded} value
   * with its scale set to {@code scale}. The unscaled value is determined by
   * multiplying or dividing this the unscaled value of {@code encoded} by the
   * appropriate power of ten to maintain its overall value. If the scale is
   * reduced by the operation, the unscaled value must be divided (rather than
   * multiplied), and the value may be changed; in this case, the value will be
   * rounded down.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param scale The scale component.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return The {@link LongDecimal#encode(long,short,byte,long) encoded} value
   *         with its scale set to {@code scale}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long setScale(final long encoded, final short scale, final byte bits, final long defaultValue) {
    return setScale(encoded, scale, bits, defaultValue, null);
  }

  /**
   * Returns the {@link LongDecimal#encode(long,short,byte,long) encoded} value
   * with its scale set to {@code scale}. The unscaled value is determined by
   * multiplying or dividing this the unscaled value of {@code encoded} by the
   * appropriate power of ten to maintain its overall value. If the scale is
   * reduced by the operation, the unscaled value must be divided (rather than
   * multiplied), and the value may be changed; in this case, the value will be
   * rounded down.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param scale The scale component.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @param roundingMode The rounding mode to apply.
   * @return The {@link LongDecimal#encode(long,short,byte,long) encoded} value
   *         with its scale set to {@code scale}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long setScale(final long encoded, final short scale, final byte bits, final long defaultValue, final RoundingMode roundingMode) {
    // FIXME: RoundingMode is not being considered
    final short s = decodeScale(encoded, bits);
    if (s == scale)
      return encoded;

    final int minScale = LongDecimal.minScale[bits];
    final int maxScale = LongDecimal.maxScale[bits];
    if (scale < minScale || maxScale < scale) {
      if (logger.isDebugEnabled())
        logger.debug("The scale of " + toString(encoded, bits) + " cannot be set to " + scale + " with " + bits + " scale bits");

      return defaultValue;
    }

    long val = decodeValue(encoded, bits);
    if (val == 0)
      return encode(val, scale, bits, defaultValue);

    final byte digits = Numbers.digits(val);
    if (scale < s - digits + 1)
      return encode(0, scale, bits, defaultValue);

    final int diff = s - scale;
    if (diff > digits || diff < -18 || digits - diff > 19) {
      if (logger.isDebugEnabled())
        logger.debug("The scale of " + toString(encoded, bits) + " cannot be set to " + scale + " with " + bits + " scale bits");

      return defaultValue;
    }

    final boolean isPositive = val >= 0;
    final long minValue = LongDecimal.minValue(bits);
    final long maxValue = LongDecimal.maxValue(bits);
    if (diff < 0) {
      val *= FastMath.e10[-diff];
      if (isPositive ? val > maxValue : val < minValue) {
        if (logger.isDebugEnabled())
          logger.debug("The scale of " + toString(encoded, bits) + " cannot be set to " + scale + " with " + bits + " scale bits");

        return defaultValue;
      }
    }
    else {
      val /= FastMath.e10[diff];
      if (val == 0)
        throw new IllegalStateException("Should not happen");
    }

    return encode(val, scale, bits, defaultValue);
  }

  /**
   * Returns the <i>precision</i> of the specified
   * {@link LongDecimal#encode(long,short,byte,long) encoded} value. (The
   * precision is the number of digits in the unscaled value.)
   * <p>
   * The precision of a zero value is {@code 1}.
   *
   * @param encoded The {@link LongDecimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The scale component from the
   *         {@link LongDecimal#encode(long,short,byte,long) encoded} value with
   *         the given number of sign {@code bits}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static short precision(final long encoded, final byte bits) {
    return encoded == 0 ? 1 : Numbers.digits(decodeValue(encoded, bits));
  }

  /**
   * Returns the smaller of two {@link LongDecimal#encode(long,short,byte,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param bits The number of bits reserved for the scale.
   * @return The smaller of {@code ld1} and {@code ld2}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static long min(final long ld1, final long ld2, final byte bits) {
    return lte(ld1, ld2, bits) ? ld1 : ld2;
  }

  /**
   * Returns the larger of two {@link LongDecimal#encode(long,short,byte,long)
   * encoded} values. If the arguments have the same value, the result is that
   * same value.
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param bits The number of bits reserved for the scale.
   * @return The larger of {@code ld1} and {@code ld2}.
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   */
  public static long max(final long ld1, final long ld2, final byte bits) {
    return gte(ld1, ld2, bits) ? ld1 : ld2;
  }

  /**
   * Returns {@code true} if the {@link LongDecimal#encode(long,short,byte,long)
   * LongDecimal}-encoded arguments are equal to each other and
   * {@code false} otherwise.
   *
   * <pre>
   * {@code result = ld1 == ld2}
   * </pre>
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return {@code true} if the {@link LongDecimal#encode(long,short,byte,long)
   *         LongDecimal}-encoded arguments are equal to each other and
   *         {@code false} otherwise.
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
   * Compares two {@link LongDecimal#encode(long,short,byte,long)
   * LongDecimal}-encoded values numerically.
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded value to compare.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @return The value {@code 0} if {@code ld1 == ld2}; a value less than {@code 0}
   *         if {@code ld1 < ld2}; and a value greater than {@code 0} if
   *         {@code ld1 > ld2}
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

    s1 -= Numbers.digits(v1);
    s2 -= Numbers.digits(v2);
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
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
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
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
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
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
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
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
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
   * Returns the result of the negation of {@code d}, i.e.:
   *
   * <pre>
   * {@code result = -d}
   * </pre>
   *
   * @param d The value to negate.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the negation of {@code d}, i.e.: {@code - d}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long neg(final long d, final byte bits, final long defaultValue) {
    return encode(-decodeValue(d, bits), decodeScale(d, bits), bits, defaultValue);
  }

  /**
   * Returns the result of the subtraction of {@code ld2} from {@code ld1}, i.e.:
   *
   * <pre>
   * {@code result = d1 - d2}
   * </pre>
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the subtraction of {@code ld2} from {@code ld1}, i.e.:
   *         {@code d1 - d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long sub(final long ld1, final long ld2, final byte bits, final long defaultValue) {
    final long nld2 = neg(ld2, bits, defaultValue);
    if (nld2 == defaultValue)
      return ld1 == ld2 ? 0 : defaultValue;

    return ld1 == 0 ? nld2 : ld2 == 0 ? ld1 : add0(ld1, nld2, bits, defaultValue);
  }

  /**
   * Returns the result of the addition of {@code ld1} to {@code ld2}, i.e.:
   *
   * <pre>
   * {@code result = d1 + d2}
   * </pre>
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
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

  private static long add0(long ld1, long ld2, final byte bits, final long defaultValue) {
    long v1 = decodeValue(ld1, bits);
    if (v1 == 0)
      return ld2;

    long v2 = decodeValue(ld2, bits);
    if (v2 == 0)
      return ld1;

    final long minValue = LongDecimal.minValue(bits);
    final long maxValue = LongDecimal.maxValue(bits);
    short s1 = decodeScale(ld1, bits);
    short s2 = decodeScale(ld2, bits);
    short s = 0;
    if (s1 == s2) {
      s = s1;
    }
    else {
      if (s1 > s2) {
        ld1 ^= ld2;
        ld2 ^= ld1;
        ld1 ^= ld2;

        v1 ^= v2;
        v2 ^= v1;
        v1 ^= v2;

        s1 ^= s2;
        s2 ^= s1;
        s1 ^= s2;
      }

      int diff = s2 - s1;
      byte avail = (byte)(maxDigits[bits] - Numbers.digits(v1));
      // Test for overflow (exclude if avail already == 0?)
      final long test = v1 * FastMath.e10[avail];
      if (v1 > 0 ? 0 > test || test > maxValue : 0 < test || test < minValue)
        --avail;

      if (diff > Numbers.digits(maxValue))
        return ld1;

      if (diff > avail) {
        final short x = (short)(diff - avail);
        short adj = x;
        for (int i; adj > 0;) {
          i = Math.min(adj, FastMath.e10.length - 1);
          v2 /= FastMath.e10[i];
          adj -= i;
          if (v2 == 0)
            return ld1;
        }

        diff = avail;
        s = (short)(s2 - x);
      }
      else if (diff < 0) {
        return ld1;
      }
      else {
        s = s2;
      }

      if (diff > 0)
        v1 *= FastMath.e10[diff];
    }

    long val = v1 + v2;
    if (v1 > 0 != v2 > 0) {
      if (val < minValue || maxValue < val)
        throw new IllegalStateException("Should not happen");
    }
    else if (v2 > 0 ? 0 > val : 0 < val) {
      // overflow can only be off by a factor of 10, since this is addition/subtraction
      --s;
      v1 /= 10;
      v2 /= 10;
      val = v1 + v2;
      if (v2 > 0 ? 0 > val : 0 < val)
        throw new IllegalStateException("Should not happen");
    }
    else if (v2 > 0 ? val > maxValue : val < minValue) {
      // overflow can only be off by a factor of 10, since this is addition/subtraction
      --s;
      val /= 10;
      if (v2 > 0 ? val > maxValue : val < minValue)
        throw new IllegalStateException("Should not happen");
    }

    return encode(val, s, bits, defaultValue);
  }

  /**
   * Returns the result of the multiplication of {@code ld1} and {@code ld2},
   * i.e.:
   *
   * <pre>
   * {@code result = d1 * d2}
   * </pre>
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the multiplication of {@code ld1} and {@code ld2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long mul(final long ld1, final long ld2, final byte bits, final long defaultValue) {
    if (bits == 0) {
      if (willProductOverflow(Math.abs(ld1), Math.abs(ld2))) {
        if (logger.isDebugEnabled())
          logger.debug("result cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }

      return ld1 * ld2;
    }

    if (ld1 == 0 || ld2 == 0)
      return 0;

    long v1 = decodeValue(ld1, bits);
    if (v1 == 0)
      return 0;

    long v2 = decodeValue(ld2, bits);
    if (v2 == 0)
      return 0;

    short s1 = decodeScale(ld1, bits);
    short s2 = decodeScale(ld2, bits);
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

    if (bits < bitsRequired) {
      if (logger.isDebugEnabled())
        logger.debug("result cannot be represented with less than " + bitsRequired + " scale bits");

      return defaultValue;
    }

    final boolean expectedPositive = v1 < 0 ? v2 < 0 : v2 > 0;
    long val;
    int s = s1 + s2;
    // How many digits are available before overflow?
    // If not enough, then degrade precision of each
    // value, degrading higher precision first.
    final byte l1 = Numbers.digits(v1);
    final byte l2 = Numbers.digits(v2);
    byte avail = (byte)(19 - l1 - l2);
    if (avail >= 0) {
      while (expectedPositive != (val = v1 * v2) > 0) {
        if (v1 > v2)
          v1 /= 10;
        else
          v2 /= 10;

        --s;
      }
    }
    else {
      avail = (byte)-avail;
      // If there is no space in the long for a direct multiplication, then
      // 1) Figure out the scale-back points first, scaling back the arg with more digits
      // 2) Test the multiplication against willProductOverflow(long,long,long) to see if an overflow will occur
      final int dl = Math.abs(l1 - l2);
      short ds1 = 0, ds2 = 0;
      if (avail < dl) {
        if (l1 >= l2)
          ds1 += avail;
        else
          ds2 += avail;
      }
      else {
        avail -= dl;
        final int andOne = avail % 2 == 0 ? 0 : 1;
        avail /= 2;
        if (l1 >= l2) {
          ds1 += avail + andOne + dl;
          ds2 += avail;
        }
        else {
          ds1 += avail;
          ds2 += avail + andOne + dl;
        }
      }

      long f1 = FastMath.e10[ds1];
      long f2 = FastMath.e10[ds2];
      long t1 = av1 / f1;
      long t2 = av2 / f2;
      if (willProductOverflow(t1, t2)) {
        if (l1 >= l2) {
          f1 = FastMath.e10[++ds1];
          t1 /= 10;
        }
        else {
          f2 = FastMath.e10[++ds2];
          t2 /= 10;
        }

        if (willProductOverflow(t1, t2))
          throw new IllegalStateException("There's an error in the precision reduction algorithm");
      }

      final long lv1 = v1 % 100000000;
      final long hv1 = (v1 - lv1) / f1;

      final long lv2 = v2 % 100000000;
      final long hv2 = (v2 - lv2) / f2;

      val = (hv1 * hv2) + ((hv1 * lv2) / f2) + ((hv2 * lv1) / f1) + ((lv1 * lv2) / (f1 * f2));
      s -= ds1 + ds2;

      // Approach using Math.multiplyHigh
//      avail = (byte)(l1 + l2 - 19);
//      final long high = Math.multiplyHigh(v1, v2);
//      final int p = (int)(Math.log(high) / Math.log(2)) + 1;
//      final long a = e10[avail];
//      final long u = high << (64-p);
//      final long r = Long.remainderUnsigned(u, a);
//      final long h = Long.divideUnsigned(u, a) << p;
//      final long low = Long.divideUnsigned(v1 * v2, a);
//      final long res = h + low - Long.MIN_VALUE;
//      if (!String.valueOf(res).substring(0, 2).equals(String.valueOf(val).substring(0, 2)))
//        throw new IllegalStateException();
//      val = res;
    }

    // We may still have overflowed the LongDecimal encoding
    final long maxValue = LongDecimal.maxValue(bits);
    final long minValue = LongDecimal.minValue(bits);
    while (val < 0 ? val < minValue : maxValue < val) {
      --s;
      val /= 10;
      if (val == 0)
        return defaultValue;
    }

    final short maxScale = LongDecimal.maxScale[bits];
    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.digits(val)) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }

      for (int i; adj > 0; adj -= i) {
        i = Math.min(adj, FastMath.e10.length - 1);
        val /= FastMath.e10[i];
        s -= i;
      }

      if (val == 0) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }
    }
    else {
      final int minScale = LongDecimal.minScale[bits];
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.digits(val)) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }

        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.e10.length - 1);
          val *= FastMath.e10[i];
          s += i;
        }

        if (val < 0 ? val < minValue : maxValue < val) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }
      }
    }

    return encode(val, (short)s, bits, defaultValue);
  }

  /**
   * Returns the result of the division of {@code ld1} by {@code ld2}, i.e.:
   *
   * <pre>
   * {@code result = d1 / d2}
   * </pre>
   *
   * @param ld1 The first {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param ld2 The second {@link LongDecimal#encode(long,short,byte,long)
   *          LongDecimal}-encoded argument.
   * @param bits The number of bits in the encoded {@code long} values reserved
   *          for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link LongDecimal} encoding with the provided
   *          {@code bits}.
   * @return The result of the multiplication of {@code ld1} and {@code ld2},
   *         i.e.: {@code d1 * d2}
   * @see #encode(long,short,byte,long)
   * @see #decodeValue(long,byte)
   * @see #decodeScale(long,byte)
   */
  public static long div(final long ld1, final long ld2, final byte bits, final long defaultValue) {
    // Division by zero
    if (ld2 == 0)
      return defaultValue;

    long v2 = decodeValue(ld2, bits);
    // Division by zero
    if (v2 == 0)
      return defaultValue;

    // Division of zero
    if (ld1 == 0)
      return 0;

    long v1 = decodeValue(ld1, bits);
    // Division of zero
    if (v1 == 0)
      return 0;

    short s1 = decodeScale(ld1, bits);
    short s2 = decodeScale(ld2, bits);

    // How many decimals do we have until overflow of long?
    byte l1 = (byte)(19 - Numbers.digits(v1));
    // Expand the value to max precision available
    if (l1 > 0) {
      long test = v1 * FastMath.e10[l1];
      // Check if we've overflowed long
      if (v1 < 0 != test < 0)
        test = v1 * FastMath.e10[--l1];

      v1 = test;
      s1 += l1;
    }

    // If v2 has trailing zeroes, remove them first
    final byte z2 = Numbers.trailingZeroes(v2);
    if (z2 > 0) {
      v2 /= FastMath.e10[z2];
      s2 -= z2;
    }

    int s = s1 - s2;
    long val = v1 / v2;

    // val has definitely not overflowed long, but it may have
    // overflowed the max/min value of LongDecimal encoding
    final long maxValue = LongDecimal.maxValue(bits);
    final long minValue = LongDecimal.minValue(bits);
    while (val < 0 ? val < minValue : maxValue < val) {
      --s;
      val /= 10;
    }

    final int maxScale = LongDecimal.maxScale[bits];
    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.digits(val)) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }

      for (int i; adj > 0; adj -= i) {
        i = Math.min(adj, FastMath.e10.length - 1);
        val /= FastMath.e10[i];
        s -= i;
      }

      if (val == 0) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

        return defaultValue;
      }
    }
    else {
      final int minScale = LongDecimal.minScale[bits];
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.digits(val)) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }

        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.e10.length - 1);
          val *= FastMath.e10[i];
          s += i;
        }

        if (val < 0 ? val < minValue : maxValue < val) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + bits + " scale bits");

          return defaultValue;
        }
      }
    }

    return encode(val, (short)s, bits, defaultValue);
  }

  private LongDecimal() {
  }
}