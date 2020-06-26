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

  // FIXME: Probably don't need this
  static int checkSig(final int[] val, final int signum) {
    return val[0] == 3 && val[2] == 0 ? 0 : signum;
  }

  // FIXME: Probably don't need this
  static int trimLen(final int[] val, int len) {
    while (len > 1 && val[len - 1] == 0)
      --len;

    return len;
  }

  // FIXME: Probably don't need this
  static int getLen(final int[] val) {
    for (int i = val.length - 1; i >= 0; --i)
      if (val[i] != 0)
        return i + 1;

    return 1;
  }

  // FIXME: Probably don't need this
  static void clear(final int[] val, final int fromIndex) {
    for (int i = fromIndex; i < val.length; ++i)
      val[i] = 0;
  }

  public static int[] uassign(final int[] val, final int signum, final int v) {
    val[0] = 3;
    val[1] = v == 0 ? 0 : signum;
    val[2] = v;
    return val;
  }

  public static int[] uassign(int[] val, final int signum, final long v) {
    if (v == 0)
      return setToZero(val);

    final int h = (int)(v >>> 32);
    if (h != 0) {
      if (val.length < 4)
        val = new int[4];

      val[0] = 4;
      val[3] = h;
    }
    else {
      val[0] = 3;
    }

    val[1] = signum;
    val[2] = (int)(v & LONG_INT_MASK);
    return val;
  }

  /**
   * Parses a part of a char array as an unsigned decimal number.
   *
   * @param s A char array representing the number in decimal.
   * @param fromIndex The index (inclusive) where we start parsing.
   * @param toIndex The index (exclusive) where we stop parsing.
   * @return The parsed number.
   * @complexity O(n)
   */
  static int parse(final char[] s, int fromIndex, final int toIndex) {
    int res = s[fromIndex] - '0';
    while (++fromIndex < toIndex)
      res = res * 10 + s[fromIndex] - '0';

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
  static void mulAdd(final int[] val, final int mul, final int add) {
    int len = val[0];
    long carry = 0;
    for (int i = 2; i < len; ++i) {
      carry = mul * (val[i] & LONG_INT_MASK) + carry;
      val[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry != 0)
      val[len++] = (int)carry;

    carry = (val[2] & LONG_INT_MASK) + add;
    val[2] = (int)carry;
    if ((carry >>> 32) != 0) {
      int i = 3;
      for (; i < len && ++val[i] == 0; ++i);
      if (i == len)
        val[len++] = 1; // TODO: realloc() for general case?
    }

    val[0] = len;
  }

  /**
   * Reallocates the magnitude array to one twice its size.
   *
   * @complexity O(n)
   */
  static int[] realloc(final int[] val) {
    return realloc(val, val.length * 2);
  }

  /**
   * Reallocates the magnitude array to one of the given size.
   *
   * @param newLen The new size of the magnitude array.
   * @complexity O(n)
   */
  static int[] realloc(final int[] val, final int newLen) {
    return realloc(val, val[0], newLen);
  }

  static int[] realloc(final int[] val, final int len, final int newLen) {
    // FIXME: Switch length and signum?!??!!?!
    final int[] res = new int[newLen];
    System.arraycopy(val, 0, res, 0, len);
    val[0] = newLen;
    return res;
  }

  /**
   * Tells whether this number is zero or not.
   *
   * @return true if this number is zero, false otherwise
   * @complexity O(1)
   */
  public static boolean isZero(final int[] val) {
    return val[0] == 3 && val[2] == 0;
  }

  /**
   * Sets this number to zero.
   *
   * @return The new length.
   * @complexity O(1)
   */
  public static int[] setToZero(final int[] val) {
    val[0] = 3;
    val[1] = 0;
    val[2] = 0;
    return val;
  }

  public static byte byteValue(final int[] val, final int fromIndex, final int signum) {
    return (byte)(signum < 0 ? ~val[fromIndex] + 1 : val[fromIndex]);
  }

  public static short shortValue(final int[] val, final int fromIndex, final int signum) {
    return (short)(signum < 0 ? ~val[fromIndex] + 1 : val[fromIndex]);
  }

  public static int intValue(final int[] val, final int fromIndex) {
    return val[1] < 0 ? ~val[fromIndex] + 1 : val[fromIndex];
  }

  public static long longValue(final int[] val, final int fromIndex, final int toIndex, final int signum) {
    final long longValue = longValue(val, fromIndex, toIndex);
    return signum < 0 ? ~longValue + 1 : longValue;
  }

  /**
   * To unsigned long!
   *
   * @param val
   * @param len
   * @return
   */
  public static long longValue(final int[] val, int fromIndex, final int toIndex) {
    final long val0l = val[fromIndex] & LONG_INT_MASK;
    return ++fromIndex < toIndex ? (long)val[fromIndex] << 32 | val0l : val0l;
  }

  public static float floatValue(final int[] val, final int fromIndex, final int toIndex, final int signum) {
    final int len = toIndex;
    final int s = Integer.numberOfLeadingZeros(val[len - 1]);
    if (len == fromIndex + 1 && s >= 8)
      return signum * val[fromIndex];

    final int exponent = ((len - (fromIndex + 1)) << 5) + (32 - s) - 1;
    if (exponent < Long.SIZE - 1) // FIXME: Can optimize the "- 1"
      return longValue(val, fromIndex, toIndex, signum);

    if (exponent > Float.MAX_EXPONENT)
      return signum > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;

    int bits = val[len - 1]; // Mask out the 24 MSBits.
    if (s <= 8)
      bits >>>= 8 - s;
    else
      bits = bits << s - 8 | val[len - 2] >>> 32 - (s - 8); // s-8==additional bits we need.

    bits ^= 1L << 23; // The leading bit is implicit, cancel it out.

    final int exp = (int)(((32 - s + 32L * (len - (fromIndex + 1))) - 1 + 127) & 0xFF);
    bits |= exp << 23; // Add exponent.
    bits |= signum & (1 << 31); // Add sign-bit.

    return Float.intBitsToFloat(bits);
  }

  static int bitLengthForInt(int n) {
    return 32 - Integer.numberOfLeadingZeros(n);
  }

  public static double doubleValue(final int[] val, final int fromIndex, final int toIndex, final int signum) {
    final int len = toIndex;
    if (len == fromIndex + 1)
      return signum * (val[fromIndex] & LONG_INT_MASK);

    final int z = Integer.numberOfLeadingZeros(val[len - 1]);
    final int exponent = ((len - (fromIndex + 1)) << 5) + (32 - z) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(val, fromIndex, toIndex, signum);

    if (exponent > Double.MAX_EXPONENT)
      return signum > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

    if (len == (fromIndex + 2) && 32 - z + 32 <= 53)
      return signum * ((long)val[fromIndex + 1] << 32 | (val[fromIndex] & LONG_INT_MASK));

    long bits = (long)val[len - 1] << 32 | (val[len - 2] & LONG_INT_MASK); // Mask out
    // the 53 MSBits.
    if (z <= 11)
      bits >>>= 11 - z;
    else
      bits = bits << z - 11 | val[len - (fromIndex + 3)] >>> 32 - (z - 11); // s-11==additional bits we need.

    bits ^= 1L << 52; // The leading bit is implicit, cancel it out.

    final long exp = ((32 - z + 32L * (len - (fromIndex + 1))) - 1 + 1023) & 0x7FF;
    bits |= exp << 52; // Add exponent.
    bits |= signum & (1L << 63); // Add sign-bit.

    return Double.longBitsToDouble(bits);
  }

  // Divides the number by 10^13 and returns the remainder.
  // Does not change the sign of the number.
  private static long toStringDiv(final int[] val) {
    final int pow5 = 1_220_703_125, pow2 = 1 << 13;
    int nextq = 0;
    long rem = 0;
    for (int i = val[0] - 1; i > 2; --i) {
      rem = (rem << 32) + (val[i] & LONG_INT_MASK);
      final int q = (int)(rem / pow5);
      rem = rem % pow5;
      val[i] = nextq | q >>> 13;
      nextq = q << 32 - 13;
    }

    rem = (rem << 32) + (val[2] & LONG_INT_MASK);
    final int mod2 = val[2] & pow2 - 1;
    val[2] = nextq | (int)(rem / pow5 >>> 13);
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
  public static String toString(final int[] val) {
    if (isZero(val))
      return "0";

    int len = val[0];
    final int signum = val[1];
    int top = (len - 2) * 10 + 3;
    final char[] cmag = new char[top];
    Arrays.fill(cmag, '0');
    // FIXME: Why are we copying?
    final int[] cpy = Arrays.copyOf(val, len);
    while (true) {
      final int j = top;
      long tmp = toStringDiv(val);
      if (val[len - 1] == 0 && len > 3 && val[--len - 1] == 0 && len > 3)
        --len;

      for (; tmp > 0; tmp /= 10)
        cmag[--top] += tmp % 10; // TODO: Optimize.

      if (len == 3 && val[2] == 0)
        break;

      top = j - 13;
    }

    if (signum < 0)
      cmag[--top] = '-';

    System.arraycopy(cpy, 0, val, 0, cpy.length);
    return new String(cmag, top, cmag.length - top);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param a The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public static boolean equals(final int[] val1, final int[] val2) {
    final int len1 = val1[0];
    final int len2 = val2[0];
    if (len1 != len2)
      return false;

    if (isZero(val1) && isZero(val2))
      return true;

    final int signum1 = val1[1];
    final int signum2 = val2[1];
    if ((signum1 ^ signum2) < 0)
      return false; // In case definition of sign would change...

    for (int i = 2; i < len1; ++i)
      if (val1[i] != val2[i])
        return false;

    return true;
  }

  public static int hashCode(final int[] val) {
    final int signum = val[1];
    if (signum == 0)
      return 0;

    final int len = val[0];
    int hash = 0;
    for (int i = 0; i < len; ++i)
      hash = 31 * hash + val[i] & INT_MASK;

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
  public static int compareAbsTo(final int[] val1, final int[] val2) {
    final int len1 = val1[0];
    final int len2 = val2[0];
    if (len1 > len2)
      return 1;

    if (len1 < len2)
      return -1;

    long val1l, val2l;
    for (int i = len1 - 1; i >= 0; --i) {
      val1l = val1[i] & LONG_INT_MASK;
      val2l = val2[i] & LONG_INT_MASK;
      if (val1l > val2l)
        return 1;

      if (val1l < val2l)
        return -1;
    }

    return 0;
  }

  public static int compareTo(final int[] val1, final int[] val2) {
    final int signum1 = val1[1];
    final int signum2 = val2[1];
    if (signum1 < 0)
      return signum2 < 0 ? compareAbsTo(val2, val1) : -1;

    if (signum1 > 0)
      return signum2 > 0 ? compareAbsTo(val1, val2) : 1;

    return signum2 < 0 ? 1 : signum2 == 0 ? 0 : -1;
  }

  public static void abs(final int[] val) {
    if (val[1] != 0)
      val[1] = 1;
  }
}