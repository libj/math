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

package gnu.java.math;

import java.util.Arrays;

@SuppressWarnings("javadoc")
abstract class BigNumber extends Number {
  private static final long serialVersionUID = -5274535682246497862L;

  public static final int INT_MASK = 0xFFFFFFFF;
  public static final long LONG_INT_MASK = 0xFFFFFFFFL;

  static int checkSig(final int[] mag, final int len, final int signum) {
    return len == 1 && mag[0] == 0 ? 0 : signum;
  }

  static int trimLen(final int[] mag, int len) {
    while (len > 1 && mag[len - 1] == 0)
      --len;

    return len;
  }

  static int getLen(final int[] mag) {
    for (int i = mag.length - 1; i >= 0; --i)
      if (mag[i] != 0)
        return i + 1;

    return 1;
  }

  static void clear(final int[] mag, final int fromIndex) {
    for (int i = fromIndex; i < mag.length; ++i)
      mag[i] = 0;
  }

  /**
   * Parses a part of a char array as an unsigned decimal number.
   *
   * @param s A char array representing the number in decimal.
   * @param from The index (inclusive) where we start parsing.
   * @param to The index (exclusive) where we stop parsing.
   * @return The parsed number.
   * @complexity O(n)
   */
  static int parse(final char[] s, int from, final int to) {
    int res = s[from] - '0';
    while (++from < to)
      res = res * 10 + s[from] - '0';

    return res;
  }

  /**
   * Multiplies this number and then adds something to it. I.e. sets this =
   * this*mul + add.
   *
   * @param mul The value we multiply our number with, mul < 2^31.
   * @param add The value we add to our number, add < 2^31.
   * @complexity O(n)
   */
  static int mulAdd(final int[] mag, int len, final int mul, final int add) {
    long carry = 0;
    for (int i = 0; i < len; ++i) {
      carry = mul * (mag[i] & LONG_INT_MASK) + carry;
      mag[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry != 0)
      mag[len++] = (int)carry;

    carry = (mag[0] & LONG_INT_MASK) + add;
    mag[0] = (int)carry;
    if ((carry >>> 32) != 0) {
      int i = 1;
      for (; i < len && ++mag[i] == 0; ++i);
      if (i == len)
        mag[len++] = 1; // TODO: realloc() for general case?
    }

    return len;
  }

  /**
   * Reallocates the magnitude array to one twice its size.
   *
   * @complexity O(n)
   */
  static int[] realloc(final int[] mag, final int len) {
    return realloc(mag, len, mag.length * 2);
  }

  /**
   * Reallocates the magnitude array to one of the given size.
   *
   * @param newLen The new size of the magnitude array.
   * @complexity O(n)
   */
  static int[] realloc(final int[] mag, final int len, final int newLen) {
    final int[] res = new int[newLen];
    System.arraycopy(mag, 0, res, 0, len);
    return res;
  }

  /**
   * Tells whether this number is zero or not.
   *
   * @return true if this number is zero, false otherwise
   * @complexity O(1)
   */
  public static boolean isZero(final int[] mag, final int len) {
    return len == 1 && mag[0] == 0;
  }

  /**
   * Sets this number to zero.
   *
   * @return The new length.
   * @complexity O(1)
   */
  public static int setToZero(final int[] mag) {
    mag[0] = 0;
    return 1;
  }

  public static byte byteValue(final int[] mag, final int signum) {
    return (byte)(signum < 0 ? ~mag[0] + 1 : mag[0]);
  }

  public static short shortValue(final int[] mag, final int signum) {
    return (short)(signum < 0 ? ~mag[0] + 1 : mag[0]);
  }

  public static int intValue(final int[] mag, final int signum) {
    return signum < 0 ? ~mag[0] + 1 : mag[0];
  }

  public static long longValue(final int[] mag, final int len, final int signum) {
    final long longValue = longValue(mag, len);
    return signum < 0 ? ~longValue + 1 : longValue;
  }

  /**
   * To unsigned long!
   *
   * @param mag
   * @param len
   * @return
   */
  public static long longValue(final int[] mag, final int len) {
    final long mag0l = mag[0] & LONG_INT_MASK;
    return len == 1 ? mag0l : (long)mag[1] << 32 | mag0l;
  }

  public static float floatValue(final int[] mag, final int len, final int signum) {
    final int s = Integer.numberOfLeadingZeros(mag[len - 1]);
    if (len == 1 && s >= 8)
      return signum * mag[0];

    final int exponent = ((len - 1) << 5) + (32 - s) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(mag, len, signum);

    if (exponent > Float.MAX_EXPONENT)
      return signum > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;

    int bits = mag[len - 1]; // Mask out the 24 MSBits.
    if (s <= 8)
      bits >>>= 8 - s;
    else
      bits = bits << s - 8 | mag[len - 2] >>> 32 - (s - 8); // s-8==additional bits we need.

    bits ^= 1L << 23; // The leading bit is implicit, cancel it out.

    final int exp = (int)(((32 - s + 32L * (len - 1)) - 1 + 127) & 0xFF);
    bits |= exp << 23; // Add exponent.
    bits |= signum & (1 << 31); // Add sign-bit.

    return Float.intBitsToFloat(bits);
  }

  static int bitLengthForInt(int n) {
    return 32 - Integer.numberOfLeadingZeros(n);
  }

  public static double doubleValue(final int[] mag, final int len, final int signum) {
    if (len == 1)
      return signum * (mag[0] & LONG_INT_MASK);

    final int z = Integer.numberOfLeadingZeros(mag[len - 1]);
    final int exponent = ((len - 1) << 5) + (32 - z) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(mag, len, signum);

    if (exponent > Double.MAX_EXPONENT)
      return signum > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

    if (len == 2 && 32 - z + 32 <= 53)
      return signum * ((long)mag[1] << 32 | (mag[0] & LONG_INT_MASK));

    long bits = (long)mag[len - 1] << 32 | (mag[len - 2] & LONG_INT_MASK); // Mask out
    // the 53 MSBits.
    if (z <= 11)
      bits >>>= 11 - z;
    else
      bits = bits << z - 11 | mag[len - 3] >>> 32 - (z - 11); // s-11==additional bits we need.

    bits ^= 1L << 52; // The leading bit is implicit, cancel it out.

    final long exp = ((32 - z + 32L * (len - 1)) - 1 + 1023) & 0x7FF;
    bits |= exp << 52; // Add exponent.
    bits |= signum & (1L << 63); // Add sign-bit.

    return Double.longBitsToDouble(bits);
  }

  // Divides the number by 10^13 and returns the remainder.
  // Does not change the sign of the number.
  private static long toStringDiv(final int[] mag, final int len) {
    final int pow5 = 1_220_703_125, pow2 = 1 << 13;
    int nextq = 0;
    long rem = 0;
    for (int i = len - 1; i > 0; --i) {
      rem = (rem << 32) + (mag[i] & LONG_INT_MASK);
      final int q = (int)(rem / pow5);
      rem = rem % pow5;
      mag[i] = nextq | q >>> 13;
      nextq = q << 32 - 13;
    }

    rem = (rem << 32) + (mag[0] & LONG_INT_MASK);
    final int mod2 = mag[0] & pow2 - 1;
    mag[0] = nextq | (int)(rem / pow5 >>> 13);
    rem = rem % pow5;
    // Applies the Chinese Remainder Theorem. -67*5^13 + 9983778*2^13 = 1
    final long pow10 = (long)pow5 * pow2;
    rem = (rem - pow5 * (mod2 - rem) % pow10 * 67) % pow10;
    if (rem < 0)
      rem += pow10;

    return rem;
  }

  /**
   * Converts this number into a string of radix 10.
   *
   * @return The string representation of this number in decimal.
   * @complexity O(n^2)
   */
  // FIXME: Performance is really bad!
  public String toString(final int[] mag, int len, final int signum) {
    if (isZero(mag, len))
      return "0";

    int top = len * 10 + 1;
    final char[] cmag = new char[top];
    Arrays.fill(cmag, '0');
    final int[] cpy = Arrays.copyOf(mag, len);
    while (true) {
      final int j = top;
      long tmp = toStringDiv(mag, len);
      if (mag[len - 1] == 0 && len > 1 && mag[--len - 1] == 0 && len > 1)
        --len;

      for (; tmp > 0; tmp /= 10)
        cmag[--top] += tmp % 10; // TODO: Optimize.

      if (len == 1 && mag[0] == 0)
        break;

      top = j - 13;
    }

    if (signum < 0)
      cmag[--top] = '-';

    System.arraycopy(cpy, 0, mag, 0, len = cpy.length);
    return new String(cmag, top, cmag.length - top);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param a The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public static boolean equals(final int[] mag1, final int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (len1 != len2)
      return false;

    if (isZero(mag1, len1) && isZero(mag2, len2))
      return true;

    if ((signum1 ^ signum2) < 0)
      return false; // In case definition of sign would change...

    for (int i = 0; i < len1; ++i)
      if (mag1[i] != mag2[i])
        return false;

    return true;
  }

  public static int hashCode(final int[] mag, final int len, final int signum) {
    if (signum == 0)
      return 0;

    int hash = 0;
    for (int i = 0; i < len; ++i)
      hash = 31 * hash + mag[i] & INT_MASK;

    return signum * hash;
  }

  /**
   * Compares the absolute value of this and the given number.
   *
   * @param a The number to be compared with.
   * @return -1 if the absolute value of this number is less, 0 if it's equal, 1
   *         if it's greater.
   * @complexity O(n)
   */
  public static int compareAbsTo(final int[] mag1, final int len1, final int[] mag2, final int len2) {
    if (len1 > len2)
      return 1;

    if (len1 < len2)
      return -1;

    long mag1l, mag2l;
    for (int i = len1 - 1; i >= 0; --i) {
      mag1l = mag1[i] & LONG_INT_MASK;
      mag2l = mag2[i] & LONG_INT_MASK;
      if (mag1l > mag2l)
        return 1;

      if (mag1l < mag2l)
        return -1;
    }

    return 0;
  }

  public static int compareTo(final int[] mag1, final int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum1 < 0)
      return signum2 < 0 ? compareAbsTo(mag2, len2, mag1, len1) : -1;

    if (signum1 > 0)
      return signum2 > 0 ? compareAbsTo(mag1, len1, mag2, len2) : 1;

    return signum2 < 0 ? 1 : signum2 == 0 ? 0 : -1;
  }
}