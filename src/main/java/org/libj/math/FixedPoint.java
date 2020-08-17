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

import org.libj.lang.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class FixedPoint extends Number {
  private static final long serialVersionUID = -2783163338875335475L;
  private static final Logger logger = LoggerFactory.getLogger(FixedPoint.class);

  static final boolean highPrecision = System.getProperty("org.libj.math.Decimal.highPrecision") != null && !"false".equals(System.getProperty("org.libj.math.Decimal.highPrecision"));

  static final long LONG_INT_MASK = 0xFFFFFFFFL;

  /** The minimum allowed number of scale bits (inclusive). */
  public static final byte MIN_SCALE_BITS = 3;
  /** The maximum allowed number of scale bits (inclusive). */
  public static final byte MAX_SCALE_BITS = 16;

  static final byte MAX_PRECISION_D = 19;
  static final byte MAX_PRECISION_B = 63;

  static final short MIN_SCALE = Short.MIN_VALUE;
  static final short MAX_SCALE = Short.MAX_VALUE;

  private static final byte noScaleBits = MAX_SCALE_BITS + 1;
  private static final long[] pow2 = new long[noScaleBits];
  static final short[] minScale = new short[noScaleBits];
  static final short[] maxScale = new short[noScaleBits];

  static {
    for (byte b = 0; b < noScaleBits; ++b) {
      pow2[b] = (long)Math.pow(2, b);
      if (b > 2) {
        final long scale = pow2[b - 1];
        minScale[b] = (short)-scale;
        maxScale[b] = (short)(scale - 1);
      }
    }

    minScale[2] = (short)-1;
    maxScale[2] = (short)0;
  }

  static final ThreadLocal<Decimal> threadLocal = new ThreadLocal<Decimal>() {
    @Override
    protected Decimal initialValue() {
      return new Decimal();
    }
  };

  public static final Decimal ZERO = new Decimal(0, (short)0);

  /**
   * Returns the result of {@code v / 10} rounded half up.
   *
   * @param v The value.
   * @return The result of {@code v / 10} rounded half up.
   */
  static long roundHalfUp10(final long v) {
    return roundHalfUp((byte)(v % 10), v / 10);
  }

  static long roundCeil10(final long v) {
    return roundCeil((byte)(v % 10), v / 10);
  }

  static long roundHalfDown10(final long v) {
    return roundHalfDown((byte)(v % 10), v / 10);
  }

  static long roundCeil(final byte r, final long v) {
    return r < 0 ? v - 1 : r > 0 ? v + 1 : v;
  }

  static long roundHalfUp(final byte r, final long v) {
    return r <= -5 ? v - 1 : r >= 5 ? v + 1 : v;
  }

  static long roundHalfDown(final byte r, final long v) {
    return r < -5 ? v - 1 : r > 5 ? v + 1 : v;
  }

  static long unroundHalfUp(final byte r, final long v) {
    return r <= -5 ? v + 1 : r >= 5 ? v - 1 : v;
  }

  static long unroundHalfDown(final byte r, final long v) {
    return r < -5 ? v + 1 : r > 5 ? v - 1 : v;
  }

  static int roundHalfUp(final byte r) {
    return r <= -5 ? -1 : r >= 5 ? 1 : 0;
  }

  /**
   * Returns the number of bits of precision required for the representation of
   * the specified value.
   * <p>
   * <b>Note:</b> It is assumed the specified value is positive, as the
   * {@link Long#numberOfLeadingZeros(long)} returns 0 for all negative values.
   *
   * @param val The value for which to return the number of bits of precision.
   * @return The number of bits of precision required for the representation of
   *         the specified value.
   */
  static byte bitLength(final long val) {
    return (byte)(Long.SIZE - Long.numberOfLeadingZeros(val));
  }

  /**
   * Returns the number of value bits for the specified scale bits.
   *
   * @param scaleBits The number of scale bits.
   * @return the number of value bits for the specified scale bits.
   */
  public static byte valueBits(final byte scaleBits) {
    return (byte)(63 - scaleBits);
  }

  /**
   * Returns the number of scale bits for the specified value bits.
   *
   * @param valueBits The number of value bits.
   * @return the number of scale bits for the specified value bits.
   */
  public static byte scaleBits(final byte valueBits) {
    return (byte)(63 - valueBits);
  }

  /**
   * Returns the minimum value that can be represented with the specified number
   * of {@code bits}.
   *
   * @param bits The number of bits.
   * @return The minimum value that can be represented with the specified number
   *         of {@code bits}.
   */
  public static long minValue(final byte bits) {
    return -1L << bits;
  }

  /**
   * Returns the maximum value that can be represented with the specified number
   * of {@code bits}.
   *
   * @param bits The number of bit.
   * @return The maximum value that can be represented with the specified number
   *         of {@code bits}.
   */
  public static long maxValue(final byte bits) {
    return (1L << bits) - 1;
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
    return Decimal.minScale[bits];
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
    return Decimal.maxScale[bits];
  }

  private static long mask(final byte bits) {
    // Leave the first bit untouched, as that's the sign bit
    return (0xFFFFL << 63 - bits) & 0x7fffffffffffffffL;
  }

  /**
   * Encodes the provided {@link BigDecimal} {@code value} into a {@code long}
   * with the given number of sign {@code bits}.
   *
   * @param value The string representation of a decimal.
   * @param bits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final BigDecimal value, final byte bits, final long defaultValue) {
    final int scale = value.scale();
    final int minScale = FixedPoint.minScale[bits];
    final int maxScale = FixedPoint.maxScale[bits];
    if (scale < minScale || maxScale < scale) {
      if (logger.isDebugEnabled())
        logger.debug(value + " cannot be represented with " + bits + " scale bits");

      return defaultValue;
    }

    return encode(value.unscaledValue().longValue(), (short)scale, bits, defaultValue);
  }

  /**
   * Encodes the provided string representation of a decimal {@code value} into
   * a {@code long} with the given number of sign {@code bits}.
   *
   * @param value The string representation of a decimal.
   * @param scaleBits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final String value, final byte scaleBits, final long defaultValue) {
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
          val *= FastMath.e10[f];
      }

      val += Long.parseLong(value.substring(dot + 1, e));
    }

    final short maxScale = FixedPoint.maxScale[scaleBits];
    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.precision(val)) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");

        return defaultValue;
      }

      for (int i; adj > 0; adj -= i) {
        i = Math.min(adj, FastMath.e10.length - 1);
        val /= FastMath.e10[i];
        s -= i;
      }

      if (val == 0) {
        if (logger.isDebugEnabled())
          logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");

        return defaultValue;
      }
    }
    else {
      final int minScale = FixedPoint.minScale[scaleBits];
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.precision(val)) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");

          return defaultValue;
        }

        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.e10.length - 1);
          val *= FastMath.e10[i];
          s += i;
        }

        final byte valueBits = valueBits(scaleBits);
        final long minValue = FixedPoint.minValue(valueBits);
        final long maxValue = FixedPoint.maxValue(valueBits);
        if (val < 0 ? val < minValue : maxValue < val) {
          if (logger.isDebugEnabled())
            logger.debug("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");

          return defaultValue;
        }
      }
    }

    return encode(val, (short)s, scaleBits, defaultValue);
  }

  /**
   * Encodes the provided {@code value} and {@code scale} into a {@code long}
   * with the given number of sign {@code bits}.
   *
   * @param value The numeric component.
   * @param scale The scale component.
   * @param scaleBits The number of bits reserved for the scale.
   * @param defaultValue The value to be returned if the result cannot be
   *          represented in {@link Decimal} encoding with the provided
   *          {@code bits}.
   * @return A {@code long} encoded with the provided {@code value} and
   *         {@code scale} with the given number of sign {@code bits}.
   * @see #minValue(byte)
   * @see #maxValue(byte)
   */
  public static long encode(final long value, final short scale, final byte scaleBits, final long defaultValue) {
    if (scaleBits == 0)
      return scale == 0 ? value : defaultValue;

    final byte valueBits = valueBits(scaleBits);
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);
    if (value < 0 ? value < minValue : maxValue < value) {
      if (logger.isDebugEnabled())
        logger.debug("Value " + value + " is outside permitted min(" + minValue(valueBits) + ") max(" + maxValue(valueBits) + ")");

      return defaultValue;
    }

    if (scale < 0 ? scale < FixedPoint.minScale[scaleBits] : FixedPoint.maxScale[scaleBits] < scale) {
      if (logger.isDebugEnabled())
        logger.debug("Scale " + scale + " is greater than bits allow: " + (scale < 0 ? FixedPoint.minScale[scaleBits] : FixedPoint.maxScale[scaleBits]));

      return defaultValue;
    }

    final long scaleMask = (scale & ((1L << scaleBits) - 1)) << (63 - scaleBits);

    // System.out.println("value: " + Buffers.toString(value));
    final long encoded = value < 0 ? value ^ scaleMask : value | scaleMask;
    if (encoded == defaultValue)
      throw new IllegalArgumentException("Encoded value (" + encoded + ") conflicts with defaultValue (" + defaultValue + ")");

    return encoded;
  }

  /**
   * Decodes the value component from the
   * {@link Decimal#encode(long,short,byte,long) encoded} value with the given
   * number of sign {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,byte,long) encoded}
   *          value.
   * @param bits The number of bits reserved for the scale.
   * @return The value component from the
   *         {@link Decimal#encode(long,short,byte,long) encoded} value with the
   *         given number of sign {@code bits}.
   * @see #encode(long,short,byte,long)
   * @see #decodeScale(long,byte)
   */
  public static long decodeValue(final long encoded, final byte bits) {
    if (bits == 0)
      return encoded;

    final long scaleMask = mask(bits);
    // System.out.println("encoded: " + Buffers.toString(encoded));
    // System.out.println("sleMask: " + Buffers.toString(scaleMask));
    return encoded < 0 ? encoded | scaleMask : encoded & ~scaleMask;
  }

  /**
   * Decodes the scale component from the
   * {@link Decimal#encode(long,short,byte,long) encoded} value with the given
   * number of sign {@code bits}.
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
  public static short decodeScale(final long encoded, final byte bits) {
    if (bits == 0)
      return 0;

    final long scaleMask = mask(bits);
    final short scale = (short)(encoded < 0 ? ~((encoded | ~scaleMask) >> 63 - bits) : (encoded & scaleMask) >> 63 - bits);
    // System.out.println("SMask: " + Buffers.toString(scaleMask) + " " + scale);
    final int sign = scale & ((byte)1 << bits - 1);
    return sign == 0 ? scale : (short)(-((~scale + 1) & ((1 << bits) - 1)));
  }

  static boolean checkScale(long v, int s, final long minValue, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    if (s > maxScale) {
      final int ds = s - maxScale;
      final int p = Numbers.precision(v);
      if (p <= ds) {
        result.error("Underflow", v, (short)s);
        return false;
      }

      final long e10 = FastMath.e10[ds];
      long r1 = v % e10;
      v /= e10;
      s -= ds;
      if (r1 != 0) {
        final byte rp = Numbers.precision(r1);
        final byte r = (byte)(rp < ds ? 0 : rp == 1 ? r1 : r1 / FastMath.e10[rp - 1]);
        v = roundHalfUp(r, v);
      }
    }
    else if (s < minScale) {
      final int ds = minScale - s;
      final int fac = Numbers.precision((v < 0 ? minValue : maxValue) / v);
      if (fac <= ds) {
        result.error("Overflow", v, (short)s);
        return false;
      }

      v *= FastMath.e10[ds];
      s += ds;
    }

    result.assign(v, (short)s);
    return true;
  }
}