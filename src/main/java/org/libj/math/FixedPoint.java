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

abstract class FixedPoint extends Number {
  private static final long serialVersionUID = -2783163338875335475L;

  /** The minimum allowed number of scale bits (inclusive). */
  public static final byte MIN_SCALE_BITS = 3;
  /** The maximum allowed number of scale bits (inclusive). */
  public static final byte MAX_SCALE_BITS = 16;

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
      return new Decimals.Decimal();
    }
  };

  /**
   * Returns the result of {@code v / 10} rounded half up.
   *
   * @param v The value.
   * @return The result of {@code v / 10} rounded half up.
   */
  static long roundHalfUp10(final long v) {
    return roundHalfUp((byte)(v % 10), v / 10);
  }

  static long roundHalfUp(final byte r, final long v) {
    return r <= -5 ? v - 1 : r >= 5 ? v + 1 : v;
  }

  static long unroundHalfUp(final byte r, final long v) {
    return r <= -5 ? v + 1 : r >= 5 ? v - 1 : v;
  }

  static long roundCeil10(final long v) {
    return roundCeil((byte)(v % 10), v / 10);
  }

  static long roundCeil(final byte r, final long v) {
    return r < 0 ? v - 1 : r > 0 ? v + 1 : v;
  }

  static long roundHalfDown10(final long v) {
    return roundHalfDown((byte)(v % 10), v / 10);
  }

  static long roundHalfDown(final byte r, final long v) {
    return r < -5 ? v - 1 : r > 5 ? v + 1 : v;
  }

  static long unroundHalfDown(final byte r, final long v) {
    return r < -5 ? v + 1 : r > 5 ? v - 1 : v;
  }

  /**
   * Returns the number of bits of precision required for the representation of
   * the specified value.
   * <p>
   * <b>Note:</b> It is assumed the specified value is positive, as the
   * {@link Long#numberOfLeadingZeros(long)} returns 0 for all negative values.
   *
   * @param value The value for which to return the number of bits of precision.
   * @return The number of bits of precision required for the representation of
   *         the specified value.
   */
  static byte bitLength(final long value) {
    return (byte)(Long.SIZE - Long.numberOfLeadingZeros(value));
  }

  /**
   * Returns the number of value bits for the specified scale bits.
   *
   * @param scaleBits The number of scale bits.
   * @return the number of value bits for the specified scale bits.
   */
  static byte valueBits(final byte scaleBits) {
    return (byte)(63 - scaleBits);
  }

  /**
   * Returns the number of scale bits for the specified value bits.
   *
   * @param valueBits The number of value bits.
   * @return the number of scale bits for the specified value bits.
   */
  static byte scaleBits(final byte valueBits) {
    return (byte)(63 - valueBits);
  }

  /**
   * Returns the minimum value that can be represented with the specified number
   * of {@code bits}.
   *
   * @param valueBits The number of bits.
   * @return The minimum value that can be represented with the specified number
   *         of {@code bits}.
   */
  static long minValue(final byte valueBits) {
    return -1L << valueBits;
  }

  /**
   * Returns the maximum value that can be represented with the specified number
   * of {@code bits}.
   *
   * @param valueBits The number of bit.
   * @return The maximum value that can be represented with the specified number
   *         of {@code bits}.
   */
  static long maxValue(final byte valueBits) {
    return (1L << valueBits) - 1;
  }

  /**
   * Returns the minimum scale that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,long,byte)} with the specified number of
   * scale {@code bits}.
   *
   * @param scaleBits The number of bits reserved for the scale.
   * @return The minimum scale that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,long,byte)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,long,byte)
   * @see #minValue(byte)
   */
  static short minScale(final byte scaleBits) {
    return Decimal.minScale[scaleBits];
  }

  /**
   * Returns the maximum scale that can be represented in a {@code long} encoded
   * with {@link #encode(long,short,long,byte)} with the specified number of
   * scale {@code bits}.
   *
   * @param scaleBits The number of bits reserved for the scale.
   * @return The maximum scale that can be represented in a {@code long} encoded
   *         with {@link #encode(long,short,long,byte)} with the specified
   *         number of scale {@code bits}.
   * @throws ArrayIndexOutOfBoundsException If {@code bits} is negative or
   *           greater than {@code 16}.
   * @see #encode(long,short,long,byte)
   * @see #maxValue(byte)
   */
  static short maxScale(final byte scaleBits) {
    return Decimal.maxScale[scaleBits];
  }

  private static long mask(final byte scaleBits) {
    // Leave the first bit untouched, as that's the sign bit
    return (0xFFFFL << 63 - scaleBits) & 0x7fffffffffffffffL;
  }

  /**
   * Encodes the provided {@link BigDecimal} {@code value} into a {@code long}
   * with the given number of sign {@code bits}.
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
  static long encode(final BigDecimal value, final long defaultValue, final byte scaleBits) {
    final int scale = value.scale();
    final int minScale = FixedPoint.minScale[scaleBits];
    final int maxScale = FixedPoint.maxScale[scaleBits];
    if (scale < minScale || maxScale < scale) {
      // System.err.println(value + " cannot be represented with " + bits + " scale bits");
      return defaultValue;
    }

    return encode(value.unscaledValue().longValue(), (short)scale, defaultValue, scaleBits);
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
  static long encode(final String value, final long defaultValue, final byte scaleBits) {
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
          val *= FastMath.E10[f];
      }

      val += Long.parseLong(value.substring(dot + 1, e));
    }

    final short maxScale = FixedPoint.maxScale[scaleBits];
    // Make sure we don't overflow the scale bits
    if (s > maxScale) {
      int adj = s - maxScale;
      if (adj >= Numbers.precision(val)) {
        // System.err.println("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");
        return defaultValue;
      }

      for (int i; adj > 0; adj -= i) {
        i = Math.min(adj, FastMath.E10.length - 1);
        val /= FastMath.E10[i];
        s -= i;
      }

      if (val == 0) {
        // System.err.println("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");
        return defaultValue;
      }
    }
    else {
      final int minScale = FixedPoint.minScale[scaleBits];
      if (s < minScale) {
        int adj = minScale - s;
        if (adj >= 20 - Numbers.precision(val)) {
          // System.err.println("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");
          return defaultValue;
        }

        for (int i; adj > 0; adj -= i) {
          i = Math.min(adj, FastMath.E10.length - 1);
          val *= FastMath.E10[i];
          s += i;
        }

        final byte valueBits = valueBits(scaleBits);
        final long minValue = FixedPoint.minValue(valueBits);
        final long maxValue = FixedPoint.maxValue(valueBits);
        if (val < 0 ? val < minValue : maxValue < val) {
          // System.err.println("value=" + val + " scale=" + s + " cannot be represented with " + scaleBits + " scale bits");
          return defaultValue;
        }
      }
    }

    return encode(val, (short)s, defaultValue, scaleBits);
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
  static long encode(final long value, final short scale, final long defaultValue, final byte scaleBits) {
    if (scaleBits == 0)
      return scale == 0 ? value : defaultValue;

    final byte valueBits = valueBits(scaleBits);
    final long minValue = FixedPoint.minValue(valueBits);
    final long maxValue = FixedPoint.maxValue(valueBits);
    if (value < 0 ? value < minValue : maxValue < value) {
      // System.err.println("Value " + value + " is outside permitted min(" + minValue(valueBits) + ") max(" + maxValue(valueBits) + ")");
      return defaultValue;
    }

    if (scale < 0 ? scale < FixedPoint.minScale[scaleBits] : FixedPoint.maxScale[scaleBits] < scale) {
      // System.err.println("Scale " + scale + " is greater than bits allow: " + (scale < 0 ? FixedPoint.minScale[scaleBits] : FixedPoint.maxScale[scaleBits]));
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
   * {@link Decimal#encode(long,short,long,byte) encoded} value with the given
   * number of sign {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The value component from the
   *         {@link Decimal#encode(long,short,long,byte) encoded} value with the
   *         given number of sign {@code bits}.
   * @see #encode(long,short,long,byte)
   * @see #scale(long,byte)
   */
  static long value(final long encoded, final byte scaleBits) {
    if (scaleBits == 0)
      return encoded;

    final long scaleMask = mask(scaleBits);
    // System.out.println("encoded: " + Buffers.toString(encoded));
    // System.out.println("sleMask: " + Buffers.toString(scaleMask));
    return encoded < 0 ? encoded | scaleMask : encoded & ~scaleMask;
  }

  /**
   * Decodes the scale component from the
   * {@link Decimal#encode(long,short,long,byte) encoded} value with the given
   * number of sign {@code bits}.
   *
   * @param encoded The {@link Decimal#encode(long,short,long,byte) encoded}
   *          value.
   * @param scaleBits The number of bits reserved for the scale.
   * @return The scale component from the
   *         {@link Decimal#encode(long,short,long,byte) encoded} value with the
   *         given number of sign {@code bits}.
   * @see #encode(long,short,long,byte)
   * @see #value(long,byte)
   */
  static short scale(final long encoded, final byte scaleBits) {
    if (scaleBits == 0)
      return 0;

    final long scaleMask = mask(scaleBits);
    final short scale = (short)(encoded < 0 ? ~((encoded | ~scaleMask) >> 63 - scaleBits) : (encoded & scaleMask) >> 63 - scaleBits);
    // System.out.println("SMask: " + Buffers.toString(scaleMask) + " " + scale);
    final int sign = scale & ((byte)1 << scaleBits - 1);
    return sign == 0 ? scale : (short)(-((~scale + 1) & ((1 << scaleBits) - 1)));
  }

  static boolean checkScale(long value, int scale, final long maxValue, final short minScale, final short maxScale, final Decimal result) {
    if (scale > maxScale) {
      final int ds = scale - maxScale;
      final int p = Numbers.precision(value);
      if (p <= ds) {
        result.error("Underflow");
        return false;
      }

      final long e10 = FastMath.E10[ds];
      long r1 = value % e10;
      value /= e10;
      scale -= ds;
      if (r1 != 0) {
        final byte rp = Numbers.precision(r1);
        final byte r = (byte)(rp < ds ? 0 : rp == 1 ? r1 : r1 / FastMath.E10[rp - 1]);
        value = roundHalfUp(r, value);
      }
    }
    else if (scale < minScale) {
      final int ds = minScale - scale;
      final int fac = Numbers.precision(maxValue / value);
      if (fac <= ds) {
        result.error("Overflow");
        return false;
      }

      value *= FastMath.E10[ds];
      scale += ds;
    }

    result.assign(value, (short)scale);
    return true;
  }
}