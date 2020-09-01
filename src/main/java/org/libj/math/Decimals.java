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

public final class Decimals {
  /**
   * Fixed point representation of a decimal exposing static functions that
   * consume or produce {@code long}-encoded decimals with a variable
   * {@code scaleBits} reserved for the scale.
   * <p>
   * The ranges for the {@code value} and {@code scale} in {@code long}-encoded
   * decimals produced by this class are as follows:
   *
   * <pre>
   * value := 2<sup>64-scaleBits</sup> [-2<sup>64-scaleBits-1</sup>, 2<sup>64-scaleBits-1</sup> - 1]
   * scale := 2<sup>scaleBits</sup> [-2<sup>scaleBits-1</sup>, 2<sup>scaleBits-1</sup> - 1]
   * </pre>
   */
  public static class Decimal extends org.libj.math.Decimal {
    private static final long serialVersionUID = -6843116448466339806L;

    public Decimal(final long value, final short scale) {
      super(value, scale);
    }

    public Decimal(final long dec, final byte scaleBits) {
      super(dec, scaleBits);
    }

    public Decimal(final org.libj.math.Decimal copy) {
      super(copy);
    }

    Decimal() {
    }

    public static BigDecimal toBigDecimal(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
    }

    public static boolean eq(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
    }

    public static boolean gt(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
    }

    public static boolean gte(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
    }

    public static boolean lt(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
    }

    public static boolean lte(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
    }

    public static byte byteValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.byteValue(dec, scaleBits);
    }

    public static byte precision(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.precision(dec, scaleBits);
    }

    public static byte signum(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.signum(dec, scaleBits);
    }

    public static double doubleValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.doubleValue(dec, scaleBits);
    }

    public static float floatValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.floatValue(dec, scaleBits);
    }

    public static int compare(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
    }

    public static int hashCode(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.hashCode(dec, scaleBits);
    }

    public static int intValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.intValue(dec, scaleBits);
    }

    public static int[] toBigInt(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.toBigInt(dec, scaleBits);
    }

    public static long abs(final long dec, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
    }

    public static long add(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
    }

    public static long div(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
    }

    public static long longValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.longValue(dec, scaleBits);
    }

    public static long max(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
    }

    public static long min(final long dec1, final long dec2, final byte scaleBits) {
      return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
    }

    public static long mul(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
    }

    public static long neg(final long dec, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
    }

    public static long rem(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
    }

    public static long setScale(final long dec, short newScale, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
    }

    public static long sub(final long dec1, final long dec2, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
    }

    public static short shortValue(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.shortValue(dec, scaleBits);
    }

    public static String toScientificString(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.toScientificString(dec, scaleBits);
    }

    public static String toString(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.toString(dec, scaleBits);
    }

    public static long encode(final BigDecimal value, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
    }

    public static long encode(final long value, final short scale, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
    }

    public static long encode(final String value, final long defaultValue, final byte scaleBits) {
      return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
    }

    public static long value(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.value(dec, scaleBits);
    }

    public static short scale(final long dec, final byte scaleBits) {
      return org.libj.math.Decimal.scale(dec, scaleBits);
    }

    public static byte valueBits(final byte scaleBits) {
      return org.libj.math.Decimal.valueBits(scaleBits);
    }

    public static byte scaleBits(final byte valueBits) {
      return org.libj.math.Decimal.scaleBits(valueBits);
    }

    public static long maxValue(final byte valueBits) {
      return org.libj.math.Decimal.maxValue(valueBits);
    }

    public static long minValue(final byte valueBits) {
      return org.libj.math.Decimal.minValue(valueBits);
    }

    public static short maxScale(final byte scaleBits) {
      return org.libj.math.Decimal.maxScale(scaleBits);
    }

    public static short minScale(final byte scaleBits) {
      return org.libj.math.Decimal.minScale(scaleBits);
    }

    /**
     * Returns a copy of this {@link Decimal}.
     *
     * @return A copy of this {@link Decimal}.
     * @complexity O(n)
     */
    @Override
    public Decimal clone() {
      return (Decimal)super.clone();
    }
  }

  public static final class D3 {
    private static final byte scaleBits = 3;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 3} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>61</sup> [-1152921504606846976, 1152921504606846975]
     * scale := 2<sup>3</sup> [-4, 3]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -7884111796895814126L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D4 {
    private static final byte scaleBits = 4;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 4} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>60</sup> [-576460752303423488, 576460752303423487]
     * scale := 2<sup>4</sup> [-8, 7]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 4218529208566850961L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D5 {
    private static final byte scaleBits = 5;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 5} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>59</sup> [-288230376151711744, 288230376151711743]
     * scale := 2<sup>5</sup> [-16, 15]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 7977020322347615378L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D6 {
    private static final byte scaleBits = 6;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 6} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>58</sup> [-144115188075855872, 144115188075855871]
     * scale := 2<sup>6</sup> [-32, 31]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 3000625493300637694L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D7 {
    private static final byte scaleBits = 7;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 7} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>57</sup> [-72057594037927936, 72057594037927935]
     * scale := 2<sup>7</sup> [-64, 63]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 5928580159604762476L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D8 {
    private static final byte scaleBits = 8;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 8} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>56</sup> [-36028797018963968, 36028797018963967]
     * scale := 2<sup>8</sup> [-128, 127]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 2621573607890760026L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D9 {
    private static final byte scaleBits = 9;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 9} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>55</sup> [-18014398509481984, 18014398509481983]
     * scale := 2<sup>9</sup> [-256, 255]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -2640303942721232713L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D10 {
    private static final byte scaleBits = 10;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 10} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>54</sup> [-9007199254740992, 9007199254740991]
     * scale := 2<sup>10</sup> [-512, 511]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -3217234000632971553L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D11 {
    private static final byte scaleBits = 11;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 11} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>53</sup> [-4503599627370496, 4503599627370496]
     * scale := 2<sup>11</sup> [-1024, 1023]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -401722815320147828L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D12 {
    private static final byte scaleBits = 12;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 12} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>52</sup> [-2251799813685248, 2251799813685247]
     * scale := 2<sup>12</sup> [-2048, 2047]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -8445154790750951770L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D13 {
    private static final byte scaleBits = 13;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 13} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>51</sup> [-1125899906842624, 1125899906842623]
     * scale := 2<sup>13</sup> [-4096, 4095]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 7397500384274668548L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D14 {
    private static final byte scaleBits = 14;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 14} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>50</sup> [-562949953421312, 562949953421311]
     * scale := 2<sup>14</sup> [-8192, 8191]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 7953933942236252056L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D15 {
    private static final byte scaleBits = 15;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 15} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>49</sup> [-281474976710656, 281474976710655]
     * scale := 2<sup>15</sup> [-16384, 16383]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = 256459874560621654L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  public static final class D16 {
    private static final byte scaleBits = 16;

    /**
     * Fixed point representation of a decimal exposing static functions that
     * consume or produce {@code long}-encoded decimals with {@code 16} bits
     * reserved for the scale.
     * <p>
     * The ranges for the {@code value} and {@code scale} in
     * {@code long}-encoded decimals produced by this class are as follows:
     *
     * <pre>
     * value := 2<sup>48</sup> [-140737488355328, 140737488355327]
     * scale := 2<sup>16</sup> [-32768, 32767]
     * </pre>
     */
    public static class Decimal extends org.libj.math.Decimal {
      private static final long serialVersionUID = -3688436161055212847L;

      public Decimal(final long value, final short scale) {
        super(value, scale);
      }

      public Decimal(final long dec) {
        super(dec, scaleBits);
      }

      public Decimal(final org.libj.math.Decimal copy) {
        super(copy);
      }

      public static BigDecimal toBigDecimal(final long dec) {
        return org.libj.math.Decimal.toBigDecimal(dec, scaleBits);
      }

      public static boolean eq(final long dec1, final long dec2) {
        return org.libj.math.Decimal.eq(dec1, dec2, scaleBits);
      }

      public static boolean gt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gt(dec1, dec2, scaleBits);
      }

      public static boolean gte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.gte(dec1, dec2, scaleBits);
      }

      public static boolean lt(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lt(dec1, dec2, scaleBits);
      }

      public static boolean lte(final long dec1, final long dec2) {
        return org.libj.math.Decimal.lte(dec1, dec2, scaleBits);
      }

      public static byte byteValue(final long dec) {
        return org.libj.math.Decimal.byteValue(dec, scaleBits);
      }

      public static byte precision(final long dec) {
        return org.libj.math.Decimal.precision(dec, scaleBits);
      }

      public static byte signum(final long dec) {
        return org.libj.math.Decimal.signum(dec, scaleBits);
      }

      public static double doubleValue(final long dec) {
        return org.libj.math.Decimal.doubleValue(dec, scaleBits);
      }

      public static float floatValue(final long dec) {
        return org.libj.math.Decimal.floatValue(dec, scaleBits);
      }

      public static int compare(final long dec1, final long dec2) {
        return org.libj.math.Decimal.compare(dec1, dec2, scaleBits);
      }

      public static int hashCode(final long dec) {
        return org.libj.math.Decimal.hashCode(dec, scaleBits);
      }

      public static int intValue(final long dec) {
        return org.libj.math.Decimal.intValue(dec, scaleBits);
      }

      public static int[] toBigInt(final long dec) {
        return org.libj.math.Decimal.toBigInt(dec, scaleBits);
      }

      public static long abs(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.abs(dec, defaultValue, scaleBits);
      }

      public static long add(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.add(dec1, dec2, defaultValue, scaleBits);
      }

      public static long div(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.div(dec1, dec2, defaultValue, scaleBits);
      }

      public static long longValue(final long dec) {
        return org.libj.math.Decimal.longValue(dec, scaleBits);
      }

      public static long max(final long dec1, final long dec2) {
        return org.libj.math.Decimal.max(dec1, dec2, scaleBits);
      }

      public static long min(final long dec1, final long dec2) {
        return org.libj.math.Decimal.min(dec1, dec2, scaleBits);
      }

      public static long mul(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.mul(dec1, dec2, defaultValue, scaleBits);
      }

      public static long neg(final long dec, final long defaultValue) {
        return org.libj.math.Decimal.neg(dec, defaultValue, scaleBits);
      }

      public static long rem(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.rem(dec1, dec2, defaultValue, scaleBits);
      }

      public static long setScale(final long dec, short newScale, final long defaultValue) {
        return org.libj.math.Decimal.setScale(dec, newScale, defaultValue, scaleBits);
      }

      public static long sub(final long dec1, final long dec2, final long defaultValue) {
        return org.libj.math.Decimal.sub(dec1, dec2, defaultValue, scaleBits);
      }

      public static short shortValue(final long dec) {
        return org.libj.math.Decimal.shortValue(dec, scaleBits);
      }

      public static String toScientificString(final long dec) {
        return org.libj.math.Decimal.toScientificString(dec, scaleBits);
      }

      public static String toString(final long dec) {
        return org.libj.math.Decimal.toString(dec, scaleBits);
      }

      public static long encode(final BigDecimal value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long encode(final long value, final short scale, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, scale, defaultValue, scaleBits);
      }

      public static long encode(final String value, final long defaultValue) {
        return org.libj.math.Decimal.encode(value, defaultValue, scaleBits);
      }

      public static long value(final long dec) {
        return org.libj.math.Decimal.value(dec, scaleBits);
      }

      public static short scale(final long dec) {
        return org.libj.math.Decimal.scale(dec, scaleBits);
      }

      /**
       * Returns a copy of this {@link Decimal}.
       *
       * @return A copy of this {@link Decimal}.
       * @complexity O(n)
       */
      @Override
      public Decimal clone() {
        return (Decimal)super.clone();
      }
    }
  }

  private Decimals() {
  }
}