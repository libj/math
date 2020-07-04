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

  /**
   * Assigns the content of the given magnitude array and the length to this
   * number. The contents of the input will be copied.
   *
   * @param source The new magnitude array content.
   * @param len The length of the content, len > 0.
   * @complexity O(n)
   */
  // FIXME: 0 or 1 min len?
  public static int[] copy(int[] target, final int[] source) {
    final int len = Math.abs(source[0]) + 1;
    return copy(target, source, len, len);
  }

  static int[] copy(int[] target, final int[] source, final int copyLength, final int arrayLength) {
    if (arrayLength >= target.length)
      target = alloc(arrayLength);

    System.arraycopy(source, 0, target, 0, copyLength);
    _debugLenSig(target);
    return target;
  }

  public static int[] assign(int[] val, final int sig, final int mag) {
    if (val.length < 2)
      val = alloc(2);

    if (mag == 0) {
      val[0] = 0;
    }
    else {
      val[0] = sig;
      val[1] = mag;
    }

    _debugLenSig(val);
    return val;
  }

  public static int[] assign(final int[] val, final boolean sig, final long mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  public static int[] assign(final int[] val, final boolean sig, final int mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  public static int[] assign(int[] val, final int mag) {
    if (val.length < 2)
      val = alloc(2);

    return mag == 0 ? setToZero(val) : mag == Integer.MIN_VALUE ? assign(val, -1, mag) : mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  public static int[] assign(int[] val, final int sig, final byte[] mag, final int len) {
    int newLen = (len + 3) / 4;
    if (val == null || newLen > val.length)
      val = alloc(newLen + 2);

    int tmp = len / 4;
    int j = 0;
    for (int i = 1; i <= tmp; ++i, j += 4)
      val[i] = mag[j + 3] << 24 | (mag[j + 2] & 0xFF) << 16 | (mag[j + 1] & 0xFF) << 8 | mag[j] & 0xFF;

    if (tmp != newLen) {
      tmp = mag[j] & 0xFF;
      if (++j < len) {
        tmp |= (mag[j] & 0xFF) << 8;
        if (++j < len)
          tmp |= (mag[j] & 0xFF) << 16;
      }

      val[newLen + 1] = tmp;
      if (tmp == 0)
        --newLen;
    }

    val[0] = sig < 0 ? -newLen : newLen;
    _debugLenSig(val);
    return val;
  }

  public static int[] assign(final int[] val, final long mag) {
    // FIXME: Long.MIN_VALUE
    return mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  public static int[] assign(int[] val, final int sig, final long mag) {
    if (mag == 0)
      return setToZero(val.length == 0 ? new int[1] : val);

    final int h = (int)(mag >>> 32);
    if (h != 0) {
      if (val.length < 3)
        val = alloc(3);

      val[0] = sig < 0 ? -2 : 2;
      val[2] = h;
    }
    else {
      if (val.length < 2)
        val = alloc(2);

      val[0] = sig;
    }

    val[1] = (int)(mag & LONG_INT_MASK);
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
    if (alloc > val.length)
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

  public static int signum(final int[] val) {
    final int v = val[0];
    return v == 0 ? 0 : v < 0 ? -1 : 1;
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
   * @param val
   * @return
   */
  public static byte[] toByteArray(long val) {
    final byte[] b = new byte[8];
    for (int j = 7; j >= 0; --j, val >>>= 8)
      b[j] = (byte)(val & 0xFF);

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

  public static byte byteValue(final int[] val) {
    return byteValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  public static byte byteValue(final int[] mag, final int off, final int sig) {
    return (byte)(sig < 0 ? ~mag[off] + 1 : mag[off]);
  }

  public static short shortValue(final int[] val) {
    return shortValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  public static short shortValue(final int[] mag, final int off, final int sig) {
    return (short)(sig < 0 ? ~mag[off] + 1 : mag[off]);
  }

  public static int intValue(final int[] val) {
    return intValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  public static int intValue(final int[] mag, final int off, final int sig) {
    return sig < 0 ? ~mag[off] + 1 : mag[off];
  }

  public static long longValue(final int[] val) {
    int signum = 1; int len = val[0]; if (len < 0) { len = -len; signum = -1; }
    return longValue(val, 1, len, signum);
  }

  public static long longValueUnsigned(final int[] val) {
    int len = val[0]; if (len < 0) { len = -len; }
    return longValue(val, 1, len);
  }

  public static long longValue(final int[] mag, final int off, final int len, final int sig) {
    final long longValue = longValue(mag, off, len);
    return sig < 0 ? ~longValue + 1 : longValue;
  }

  /**
   * To unsigned long!
   *
   * @param mag
   * @param len
   * @return
   */
  public static long longValue(final int[] mag, final int off, final int len) {
    final long val0l = mag[off] & LONG_INT_MASK;
    return len > 1 ? (long)mag[off + 1] << 32 | val0l : val0l;
  }

  public static float floatValue(final int[] val) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    return floatValue(val, 1, len, signum);
  }

  public static float floatValue(final int[] mag, final int off, final int len, final int sig) {
    final int end = len + off - 1;
    final int s = Integer.numberOfLeadingZeros(mag[end]);
    if (len == 1 && s >= 8)
      return sig < 0 ? -mag[off] : mag[off];

    final int exponent = ((len - 1) << 5) + (32 - s) - 1;
    if (exponent < Long.SIZE - 1) // FIXME: Can optimize the "- 1"
      return longValue(mag, off, len, sig);

    if (exponent > Float.MAX_EXPONENT)
      return sig < 0 ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

    int bits = mag[end]; // Mask out the 24 MSBits.
    if (s <= 8)
      bits >>>= 8 - s;
    else
      bits = bits << s - 8 | mag[end - 1] >>> 32 - (s - 8); // s-8==additional bits we need.

    bits ^= 1L << 23; // The leading bit is implicit, cancel it out.

    final int exp = (int)(((32 - s + 32L * (len - 1)) - 1 + 127) & 0xFF);
    bits |= exp << 23; // Add exponent.
    bits |= sig & (1 << 31); // Add sign-bit. // FIXME: Can this be simplified?

    return Float.intBitsToFloat(bits);
  }

  public static double doubleValue(final int[] val) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    return doubleValue(val, 1, len, sig);
  }

  public static double doubleValue(final int[] val, final int off, final int len, final int sig) {
    if (len == 1) {
      final double v = val[off] & LONG_INT_MASK;
      return sig < 0 ? -v : v;
    }

    final int end = off + len - 1;
    final int z = Integer.numberOfLeadingZeros(val[end]);
    final int exponent = ((len - 1) << 5) + (32 - z) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(val, off, len, sig < 0 ? -1 : 1);

    if (exponent > Double.MAX_EXPONENT)
      return sig < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

    if (len == 2 && 32 - z + 32 <= 53) {
      final double v = ((long)val[off + 1] << 32 | (val[off] & LONG_INT_MASK));
      return sig < 0 ? -v : v;
    }

    long bits = (long)val[end] << 32 | (val[end - 1] & LONG_INT_MASK); // Mask out
    // the 53 MSBits.
    if (z <= 11)
      bits >>>= 11 - z;
    else
      bits = bits << z - 11 | val[len - 2] >>> 32 - (z - 11); // s-11==additional bits we need.

    bits ^= 1L << 52; // The leading bit is implicit, cancel it out.

    final long exp = ((32 - z + 32L * (len - 1)) - 1 + 1023) & 0x7FF;
    bits |= exp << 52; // Add exponent.
    bits |= sig & (1L << 63); // Add sign-bit. // FIXME: Can this be simplified?

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
  public static int compareToAbs(final int[] val1, final int[] val2) {
    return compareToAbs(val1, Math.abs(val1[0]), val2, Math.abs(val2[0]));
  }

  static int compareToAbs(final int[] val1, int len1, final int[] val2, int len2) {
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
    int sig1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; sig1 = -1; } else { sig1 = 1; }

    int sig2, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; sig2 = -1; } else { sig2 = 1; }

    if (sig1 < 0)
      return sig2 < 0 ? compareToAbs(val2, len2, val1, len1) : -1;

    if (sig2 < 0)
      return 1;

    if (len2 == 0)
      return len1 == 0 ? 0 : 1;

    return len1 == 0 ? -1 : compareToAbs(val1, len1, val2, len2);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param a The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public static boolean equals(final int[] val1, final int[] val2) {
    int sig1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; sig1 = -1; } else { sig1 = 1; }

    int sig2, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; sig2 = -1; } else { sig2 = 1; }

    if (len1 != len2)
      return false;

    if (len1 == 0)
      return true;

    if ((sig1 ^ sig2) < 0)
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

    final int sig; if (len < 0) { len = -len; sig = -1; } else { sig = 1; }

    int hash = 0;
    for (; len >= 1; --len)
      hash = 31 * hash + val[len] & INT_MASK;

    return sig * hash;
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

  static final int[] emptyVal = {};

  public static int[] valueOf(final int mag) {
    return assign(emptyVal, mag);
  }

  public static int[] valueOf(final int sig, final int mag) {
    return assign(emptyVal, sig, mag);
  }

  public static int[] valueOf(final long mag) {
    return assign(emptyVal, mag);
  }

  public static int[] valueOf(final int sig, final long mag) {
    return assign(emptyVal, sig, mag);
  }

  public static int[] valueOf(final char[] s) {
    return assign(emptyVal, s);
  }

  public static int[] valueOf(final String s) {
    return assign(emptyVal, s);
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

    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

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

    if (sig < 0)
      cmag[--top] = '-';

    System.arraycopy(cpy, 0, val, 0, cpy.length);
    return new String(cmag, top, cmag.length - top);
  }

  public static int[] abs(final int[] val) {
    if (val[0] < 0)
      val[0] = -val[0];

    _debugLenSig(val);
    return val;
  }

  public static int[] max(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) > 0 ? val1 : val2;
  }

  public static int[] min(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) < 0 ? val1 : val2;
  }

  static void _debugLenSig(final int[] val) {
    if (!isZero(val) && val[Math.abs(val[0])] == 0)
      throw new IllegalStateException(Arrays.toString(val));
  }

  public static int[] to$(final int[] val) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    int[] val$ = new int[val.length + 2];
    System.arraycopy(val, 1, val$, 2, len);
    val$[0] = len + 2;
    val$[1] = sig;
    return val$;
  }

  public static int[] to$$(final int[] val) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
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

    int sig = val[1];
    final int shiftBig = shift >>> 5;
    // Special case: entire contents shifted off the end
    if (shiftBig + 2 >= val[0])
      return sig >= 0 ? setToZero(val) : assign(val, sig, 1);

    final int shiftSmall = shift & 31;
    boolean oneLost = false;
    if (sig < 0) {
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