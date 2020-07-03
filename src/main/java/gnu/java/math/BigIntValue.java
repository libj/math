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
abstract class BigIntValue extends Number {
  private static final long serialVersionUID = -5274535682246497862L;

  public static final int INT_MASK = 0xFFFFFFFF;
  public static final long LONG_INT_MASK = 0xFFFFFFFFL;

  public static int[] uassign(int[] val, final int signum, final int v) {
    if (v == 0) {
      val[0] = 0;
    }
    else {
      if (val.length < 2)
        val = alloc(2);

      val[0] = signum;
      val[1] = v;
    }

    _debugLenSig(val);
    return val;
  }

  public static int[] uassign(final int[] val, final boolean signum, final long v) {
    return uassign(val, signum ? 1 : -1, v);
  }

  public static int[] uassign(final int[] val, final boolean signum, final int v) {
    return uassign(val, signum ? 1 : -1, v);
  }

  /**
   * Creates a new {@code int[]} that is at least length = {@code len}.
   * <p>
   * This method can return longer arrays in order to tune for optimal
   * performance.
   *
   * @param len The minimal length of the returned {@code int[]}.
   * @return A new {@code int[]} that is at least length = {@code len}.
   */
  static int[] alloc(final int len) {
    return new int[len];
  }

  public static int[] assign(final int[] val, final int v) {
    return v == 0 ? setToZero(val) : v == Integer.MIN_VALUE ? uassign(val, -1, v) : v < 0 ? uassign(val, -1, -v) : uassign(val, 1, v);
  }

  public static int[] assign(int[] val, final int signum, final byte[] v, final int len) {
    int newLen = (len + 3) / 4;
    if (val == null || newLen > val.length)
      val = alloc(newLen + 2);

    int tmp = len / 4;
    int j = 0;
    for (int i = 1; i <= tmp; ++i, j += 4)
      val[i] = v[j + 3] << 24 | (v[j + 2] & 0xFF) << 16 | (v[j + 1] & 0xFF) << 8 | v[j] & 0xFF;

    if (tmp != newLen) {
      tmp = v[j] & 0xFF;
      if (++j < len) {
        tmp |= (v[j] & 0xFF) << 8;
        if (++j < len)
          tmp |= (v[j] & 0xFF) << 16;
      }

      val[newLen + 1] = tmp;
      if (tmp == 0)
        --newLen;
    }

    val[0] = signum < 0 ? -newLen : newLen;
    _debugLenSig(val);
    return val;
  }

  public static int[] assign(final int[] val, final long v) {
    // FIXME: Long.MIN_VALUE
    return v < 0 ? uassign(val, -1, -v) : uassign(val, 1, v);
  }

  public static int[] uassign(int[] val, final int signum, final long v) {
    if (v == 0)
      return setToZero(val);

    final int h = (int)(v >>> 32);
    if (h != 0) {
      if (val.length < 3)
        val = alloc(3);

      val[0] = signum < 0 ? -2 : 2;
      val[2] = h;
    }
    else {
      if (val.length < 2)
        val = alloc(2);

      val[0] = signum;
    }

    val[1] = (int)(v & LONG_INT_MASK);
    _debugLenSig(val);
    return val;
  }

  public static int[] assign(final int[] val, final String s) {
    return assign(val, s.toCharArray());
  }

  public static int[] assign(int[] val, final char[] s) {
    final int signum = s[0] == '-' ? -1 : 1;

    final int length = s.length;
    final int from = signum - 1 >> 1;
    final int len = length + from;
    // 3402 = bits per digit * 1024
    final int alloc = (len < 10 ? 1 : (int)(len * 3402L >>> 10) + 32 >>> 5) + 1;
    if (val == null || alloc > val.length)
      val = alloc(alloc);

    int j = len % 9;
    if (j == 0)
      j = 9;

    j -= from;

    if ((val[1] = parse(s, -from, j)) != 0) {
      int toIndex = 2;
      while (j < length)
        toIndex = mulAdd(val, 1, toIndex, 1_000_000_000, parse(s, j, j += 9));

      --toIndex;
      val[0] = signum < 0 ? -toIndex : toIndex;
    }
    else {
      val[0] = 0;
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Parses a part of a char array as an unsigned decimal number.
   *
   * @param s A char array representing the number in decimal.
   * @param fromIndex The index (inclusive) where we start parsing.
   * @param toIndex The index (exclusive) where we stop parsing.
   * @return The parsed {@code int}.
   * @complexity O(n)
   */
  private static int parse(final char[] s, int fromIndex, final int toIndex) {
    int v = s[fromIndex] - '0';
    while (++fromIndex < toIndex)
      v = v * 10 + s[fromIndex] - '0';

    return v;
  }

  /**
   * Multiplies this number and then adds something to it. I.e. sets this =
   * this*mul + add.
   *
   * @param mul The value we multiply our number with, mul < 2^31.
   * @param add The value we add to our number, add < 2^31.
   * @complexity O(n)
   */
  private static int mulAdd(final int[] val, final int fromIndex, int toIndex, final int mul, final int add) {
    long carry = 0;
    int i = fromIndex;
    for (; i < toIndex; ++i) {
      carry = mul * (val[i] & LONG_INT_MASK) + carry;
      val[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry != 0)
      val[toIndex++] = (int)carry;

    carry = (val[fromIndex] & LONG_INT_MASK) + add;
    val[fromIndex] = (int)carry;
    if ((carry >>> 32) != 0) {
      i = fromIndex + 1;
      for (; i < toIndex && ++val[i] == 0; ++i);
      if (i == toIndex)
        val[toIndex++] = 1; // TODO: realloc() for general case?
    }

    return toIndex;
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
    int len = val[0]; if (len < 0) len = -len;
    return realloc(val, len, newLen);
  }

  static int[] realloc(final int[] val, final int len, final int newLen) {
    final int[] v = new int[newLen];
    System.arraycopy(val, 0, v, 0, len + 1);
    return v;
  }

  /**
   * Tells whether this number is zero or not.
   *
   * @return true if this number is zero, false otherwise
   * @complexity O(1)
   */
  public static boolean isZero(final int[] val) {
    return val[0] == 0;
  }

  /**
   * Unsigned long to bytes
   *
   * @param v
   * @return
   */
  public static byte[] toByteArray(long v) {
    final byte[] b = new byte[8];
    for (int j = 7; j >= 0; --j, v >>>= 8)
      b[j] = (byte)(v & 0xFF);

    return b;
  }

  /**
   * Sets this number to zero.
   *
   * @return The new length.
   * @complexity O(1)
   */
  public static int[] setToZero(final int[] val) {
    val[0] = 0;
    return val;
  }

  public static byte byteValue(final int[] val, final int fromIndex, final boolean signum) {
    return (byte)(!signum ? ~val[fromIndex] + 1 : val[fromIndex]);
  }

  public static short shortValue(final int[] val, final int fromIndex, final boolean signum) {
    return (short)(!signum ? ~val[fromIndex] + 1 : val[fromIndex]);
  }

  public static int intValue(final int[] val, final int fromIndex, final boolean signum) {
    return !signum ? ~val[fromIndex] + 1 : val[fromIndex];
  }

  public static long longValue(final int[] val, final int fromIndex, final int toIndex, final boolean signum) {
    final long longValue = longValue(val, fromIndex, toIndex);
    return !signum ? ~longValue + 1 : longValue;
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

  public static float floatValue(final int[] val, final int fromIndex, final int toIndex, final boolean signum) {
    final int len = toIndex;
    final int s = Integer.numberOfLeadingZeros(val[len - 1]);
    if (len == fromIndex + 1 && s >= 8)
      return signum ? val[fromIndex] : -val[fromIndex];

    final int exponent = ((len - (fromIndex + 1)) << 5) + (32 - s) - 1;
    if (exponent < Long.SIZE - 1) // FIXME: Can optimize the "- 1"
      return longValue(val, fromIndex, toIndex, signum);

    if (exponent > Float.MAX_EXPONENT)
      return signum ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;

    int bits = val[len - 1]; // Mask out the 24 MSBits.
    if (s <= 8)
      bits >>>= 8 - s;
    else
      bits = bits << s - 8 | val[len - 2] >>> 32 - (s - 8); // s-8==additional bits we need.

    bits ^= 1L << 23; // The leading bit is implicit, cancel it out.

    final int exp = (int)(((32 - s + 32L * (len - (fromIndex + 1))) - 1 + 127) & 0xFF);
    bits |= exp << 23; // Add exponent.
    bits |= (signum ? 1 : -1) & (1 << 31); // Add sign-bit. // FIXME: Can this be simplified?

    return Float.intBitsToFloat(bits);
  }

  public static double doubleValue(final int[] val, final int fromIndex, final int toIndex, final boolean signum) {
    final int len = toIndex;
    if (len == fromIndex + 1) {
      final double v = val[fromIndex] & LONG_INT_MASK;
      return signum ? v : -v;
    }

    final int z = Integer.numberOfLeadingZeros(val[len - 1]);
    final int exponent = ((len - (fromIndex + 1)) << 5) + (32 - z) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(val, fromIndex, toIndex, signum);

    if (exponent > Double.MAX_EXPONENT)
      return signum ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

    if (len == (fromIndex + 2) && 32 - z + 32 <= 53) {
      final double v = ((long)val[fromIndex + 1] << 32 | (val[fromIndex] & LONG_INT_MASK));
      return signum ? v : -v;
    }

    long bits = (long)val[len - 1] << 32 | (val[len - 2] & LONG_INT_MASK); // Mask out
    // the 53 MSBits.
    if (z <= 11)
      bits >>>= 11 - z;
    else
      bits = bits << z - 11 | val[len - (fromIndex + 3)] >>> 32 - (z - 11); // s-11==additional bits we need.

    bits ^= 1L << 52; // The leading bit is implicit, cancel it out.

    final long exp = ((32 - z + 32L * (len - (fromIndex + 1))) - 1 + 1023) & 0x7FF;
    bits |= exp << 52; // Add exponent.
    bits |= (signum ? 1 : -1) & (1L << 63); // Add sign-bit. // FIXME: Can this be simplified?

    return Double.longBitsToDouble(bits);
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
    return compareAbsTo(val1, Math.abs(val1[0]), val2, Math.abs(val2[0]));
  }

  static int compareAbsTo(final int[] val1, int len1, final int[] val2, int len2) {
    if (len1 > len2)
      return 1;

    if (len1 < len2)
      return -1;

    for (long val1l, val2l; len1 >= 1; --len1) {
      val1l = val1[len1] & LONG_INT_MASK;
      val2l = val2[len1] & LONG_INT_MASK;
      if (val1l > val2l)
        return 1;

      if (val1l < val2l)
        return -1;
    }

    return 0;
  }

  public static int compareTo(final int[] val1, final int[] val2) {
    int signum1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; signum1 = -1; } else { signum1 = 1; }

    int signum2, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; signum2 = -1; } else { signum2 = 1; }

    if (signum1 < 0)
      return signum2 < 0 ? compareAbsTo(val2, len2, val1, len1) : -1;

    if (signum2 < 0)
      return 1;

    if (len2 == 0)
      return len1 == 0 ? 0 : 1;

    return len1 == 0 ? -1 : compareAbsTo(val1, len1, val2, len2);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param a The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public static boolean equals(final int[] val1, final int[] val2) {
    int signum1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; signum1 = -1; } else { signum1 = 1; }

    int signum2, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; signum2 = -1; } else { signum2 = 1; }

    if (len1 != len2)
      return false;

    if (len1 == 0)
      return true;

    if ((signum1 ^ signum2) < 0)
      return false;

    for (; len1 >= 1; --len1)
      if (val1[len1] != val2[len1])
        return false;

    return true;
  }

  public static int hashCode(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 0;

    final int signum; if (len < 0) { len = -len; signum = -1; } else { signum = 1; }

    int hash = 0;
    for (; len >= 1; --len)
      hash = 31 * hash + val[len] & INT_MASK;

    return signum * hash;
  }

  // Divides the number by 10^13 and returns the remainder.
  // Does not change the sign of the number.
  private static long toStringDiv(final int[] val, int len) {
    // FIXME: Static...
    final int pow5 = 1_220_703_125;
    final int pow2 = 1 << 13;
    int q1 = 0;
    long r = 0;
    for (int q0; len > 1; --len) {
      r = (r << 32) + (val[len] & LONG_INT_MASK);
      q0 = (int)(r / pow5);
      r = r % pow5;
      val[len] = q1 | q0 >>> 13;
      q1 = q0 << 32 - 13;
    }

    r = (r << 32) + (val[1] & LONG_INT_MASK);
    final int mod2 = val[1] & pow2 - 1;
    val[1] = q1 | (int)(r / pow5 >>> 13);
    r %= pow5;

    // Applies the Chinese Remainder Theorem. -67*5^13 + 9983778*2^13 = 1
    final long pow10 = (long)pow5 * pow2;
    r = (r - pow5 * (mod2 - r) % pow10 * 67) % pow10;
    if (r < 0)
      r += pow10;

    return r;
  }

  public static int[] fromString(final char[] s) {
    return assign(null, s);
  }

  public static int[] fromString(final String s) {
    return assign(null, s);
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

    int signum, len = val[0]; if (len < 0) { len = -len; signum = -1; } else { signum = 1; }

    int top = len * 10 + 3;
    final char[] cmag = new char[top];
    Arrays.fill(cmag, '0');
    // FIXME: Why are we copying?
    final int[] cpy = Arrays.copyOf(val, len + 1);
    while (true) {
      final int j = top;
      long tmp = toStringDiv(val, len);
      if (val[len] == 0 && len > 1 && val[--len] == 0 && len > 1)
        --len;

      for (; tmp > 0; tmp /= 10)
        cmag[--top] += tmp % 10; // TODO: Optimize.

      if (len == 1 && val[1] == 0)
        break;

      top = j - 13;
    }

    if (signum < 0)
      cmag[--top] = '-';

    System.arraycopy(cpy, 0, val, 0, cpy.length);
    return new String(cmag, top, cmag.length - top);
  }

  public static void abs(final int[] val) {
    if (val[0] < 0)
      val[0] = -val[0];

    _debugLenSig(val);
  }

  static void _debugLenSig(final int[] val) {
    if (!isZero(val) && val[Math.abs(val[0])] == 0)
      throw new IllegalStateException(Arrays.toString(val));
  }

  public static int[] to$(final int[] val) {
    int signum, len = val[0]; if (len < 0) { len = -len; signum = -1; } else { signum = 1; }
    int[] val$ = new int[val.length + 2];
    System.arraycopy(val, 1, val$, 2, len);
    val$[0] = len + 2;
    val$[1] = signum;
    return val$;
  }

  public static int[] to$$(final int[] val) {
    int signum, len = val[0]; if (len < 0) { len = -len; signum = -1; } else { signum = 1; }
    int[] val$$ = new int[val.length - 1];
    System.arraycopy(val, 1, val$$, 0, len);
    return val$$;
  }

  static int[] bigShiftLeft$(int[] val, int shift) {
    final int len = val[0] - 2;
    shift += 2;
    final int newLen = len + shift;
    if (newLen > val.length) {
      final int[] tmp = new int[newLen];
      System.arraycopy(val, 2, tmp, shift, len);
      tmp[1] = val[1];
      val = tmp;
    }
    else {
      System.arraycopy(val, 2, val, shift, len);
      for (int i = 2; i < shift; ++i)
        val[i] = 0;
    }

    val[0] = newLen;
    return val;
  }

  static int[] smallShiftLeft$(int[] val, final int shift, final int fromIndex) {
    int len = val[0];
    if ((val[len - 1] << shift >>> shift) != val[len - 1]) { // Overflow?
      if (++len > val.length)
        val = realloc(val, val[0] - 1, len + 2);
      else
        val[len - 1] = 0;

      val[0] = len;
    }

    int next = len > val.length ? 0 : val[len - 1];
    for (int i = len - 1; i > fromIndex; --i)
      val[i] = next << shift | (next = val[i - 1]) >>> 32 - shift;

    val[fromIndex] = next << shift;
    return val;
  }

  public static int[] shiftRight$(final int[] val, final int shift) {
//    if (shift < 0)
//      return shiftLeft$(val, -shift);

    if (shift == 0 || val[0] == 3 && val[2] == 0)
      return val;

    int signum = val[1];
    final int shiftBig = shift >>> 5;
    // Special case: entire contents shifted off the end
    if (shiftBig + 2 >= val[0])
      return signum >= 0 ? setToZero(val) : uassign(val, signum, 1);

    final int shiftSmall = shift & 31;
    boolean oneLost = false;
    if (signum < 0) {
      // Find out whether any one-bits will be shifted off the end
      final int j = 2 + shiftBig;
      for (int i = 2; i < j && !(oneLost = val[i] != 0); ++i);
      if (!oneLost && shiftSmall != 0)
        oneLost = val[j] << (32 - shiftSmall) != 0;
    }

    if (shiftBig > 0)
      bigShiftRight$(val, shiftBig);

    if (shiftSmall > 0)
      smallShiftRight$(val, shiftSmall);

    // FIXME: What if an overflow happens? Look at Integer#javaIncrement(int[])
    if (oneLost)
      ++val[2];

    if (val[0] == 3 && val[2] == 0)
      val[1] = 0;

    return val;
  }

  static void smallShiftRight$(final int[] val, final int shiftSmall) {
    int len = val[0];
    for (int next = val[2], i = 2; i < len - 1; ++i)
      val[i] = next >>> shiftSmall | (next = val[i + 1]) << 32 - shiftSmall;

    if ((val[len - 1] >>>= shiftSmall) == 0 && len > 3)
      val[0] = --len;
  }

  /**
   * Shifts this number right by 32*shift, i.e. moves each digit shift positions
   * to the right.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  static void bigShiftRight$(final int[] val, int shift) {
    System.arraycopy(val, shift + 2, val, 2, (val[0] -= shift) - 1);
  }
}