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

package org.huldra.math;

import java.util.Arrays;

@SuppressWarnings("javadoc")
abstract class BigIntValue extends Number {
  private static final long serialVersionUID = -5274535682246497862L;

  static final int INT_MASK = 0xFFFFFFFF;
  static final long LONG_INT_MASK = 0xFFFFFFFFL;

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
    return new int[len + len];
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
    return mag == 0 ? setToZero(val) : assign0(val.length > 1 ? val : alloc(2), sig, mag);
  }

  static int[] assign0(final int[] val, final int sig, final int mag) {
    val[0] = sig;
    val[1] = mag;
    _debugLenSig(val);
    return val;
  }

  public static int[] assign(int[] val, final int mag) {
    return mag == 0 ? setToZero(val) : mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  public static int[] assign(final int[] val, final boolean sig, final int mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  public static int[] assign(final int[] val, final boolean sig, final long mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  public static int[] assign(final int[] val, final long mag) {
    return mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  public static int[] assign(int[] val, final int sig, final long mag) {
    if (mag == 0)
      return setToZero(val);

    final int magh = (int)(mag >>> 32);
    if (magh != 0)
      return assign0(val.length >= 3 ? val : alloc(3), sig, mag, magh);

    return assign0(val.length >= 2 ? val : alloc(2), sig, (int)mag);
  }

  static int[] assign0(final int[] val, final int sig, final long mag, final int magh) {
    val[0] = sig < 0 ? -2 : 2;
    val[1] = (int)mag;
    val[2] = magh;
    _debugLenSig(val);
    return val;
  }

  public static int[] assign(int[] val, final byte[] mag, final boolean isLittleEndian) {
    return assign(val, mag, 0, mag.length, isLittleEndian);
  }

  public static int[] assign(int[] val, final byte[] mag, final int off, final int len, final boolean isLittleEndian) {
    if (isLittleEndian)
      return assignLittleEndian(val, mag, off, len);

    if (mag[off] < 0)
      return assignBigEndianNegative(val, mag, off, len);

    return assignBigEndianPositive(val, mag, off, len);
  }

  private static int[] assignLittleEndian(int[] val, final byte[] mag, final int off, final int len) {
    int newLen = (len + 3) / 4;
    if (val == null || newLen > val.length)
      val = alloc(newLen + 2);

    int tmp = len / 4;
    int j = off;
    for (int i = 1; i <= tmp; ++i, j += 4)
      val[i] = mag[j + 3] << 24 | (mag[j + 2] & 0xFF) << 16 | (mag[j + 1] & 0xFF) << 8 | mag[j] & 0xFF;

    if (tmp != newLen) {
      tmp = mag[j] & 0xFF;
      if (++j < len) {
        tmp |= (mag[j] & 0xFF) << 8;
        if (++j < len)
          tmp |= (mag[j] & 0xFF) << 16;
      }

      if (tmp != 0)
        val[++newLen] = tmp;
    }

    if (val[newLen] == 0)
      --newLen;

    val[0] = newLen;
    _debugLenSig(val);
    return val;
  }

  /**
   * Takes an array a representing a negative 2's-complement number and
   * returns the minimal (no leading zero bytes) unsigned whose value is -a.
   */
  private static int[] assignBigEndianNegative(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off + len;
    int keep, k;

    // Find first non-sign (0xFF) byte of input
    for (keep = off; keep < indexBound && mag[keep] == -1; ++keep);

    /*
     * Allocate output array. If all non-sign bytes are 0x00, we must allocate
     * space for one extra output byte.
     */
    for (k = keep; k < indexBound && mag[k] == 0; ++k);

    final int extraByte = k == indexBound ? 1 : 0;
    final int vlen = ((indexBound - keep + extraByte) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    /*
     * Copy one's complement of input into output, leaving extra byte (if it
     * exists) == 0x00
     */
    for (int i = 1, j, b = indexBound - 1, numBytesToTransfer, lim, mask; i <= vlen; ++i) {
      numBytesToTransfer = Math.max(0, Math.min(3, b - keep));
      val[i] = mag[b--] & 0xFF;
      for (j = 8, lim = 8 * numBytesToTransfer; j <= lim; j += 8)
        val[i] |= (mag[b--] & 0xFF) << j;

      // Mask indicates which bits must be complemented
      mask = -1 >>> (8 * (3 - numBytesToTransfer));
      val[i] = ~val[i] & mask;
    }

    // Add one to one's complement to generate two's complement
    for (int i = 1; i <= vlen; ++i) {
      val[i] = (int)((val[i] & LONG_INT_MASK) + 1);
      if (val[i] != 0)
        break;
    }

    val[0] = -vlen;
    _debugLenSig(val);;
    return val;
  }

  /**
   * Returns a copy of the input array stripped of any leading zero bytes.
   */
  private static int[] assignBigEndianPositive(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off + len;
    int keep;

    // Find first nonzero byte
    for (keep = off; keep < indexBound && mag[keep] == 0; ++keep);

    // Allocate new array and copy relevant part of input array
    final int vlen = ((indexBound - keep) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    if (vlen == 0)
      return setToZero0(val);

    for (int i = 1, j, b = indexBound - 1, bytesRemaining, bytesToTransfer; i <= vlen; ++i) {
      bytesRemaining = b - keep;
      bytesToTransfer = Math.min(3, bytesRemaining);
      val[i] = mag[b--] & 0xff;
      for (j = 8; j <= bytesToTransfer << 3; j += 8)
        val[i] |= (mag[b--] & 0xff) << j;
    }

    val[0] = vlen;
    _debugLenSig(val);;
    return val;
  }

  public static int[] assign(final int[] val, final String s) {
    return assign(val, s.toCharArray());
  }

  public static int[] assign(int[] val, final char[] s) {
    final int sig = s[0] == '-' ? -1 : 1;

    final int length = s.length;
    final int from = sig - 1 >> 1;
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
      val[0] = sig < 0 ? -toIndex : toIndex;
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
   * Reallocates the magnitude array to one of the given size.
   *
   * @param newLen The new size of the magnitude array.
   * @complexity O(n)
   */
  // FIXME: Tune this like alloc()
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
   * Sets this number to zero.
   *
   * @return The new length.
   * @complexity O(1)
   */
  public static int[] setToZero(final int[] val) {
    return setToZero0(val.length > 0 ? val : alloc(1));
  }

  static int[] setToZero0(final int[] val) {
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
    int sig = 1; int len = val[0]; if (len < 0) { len = -len; sig = -1; }
    return longValue(val, 1, len, sig);
  }

  public static long longValueUnsigned(final int[] val) {
    int len = val[0]; if (len < 0) { len = -len; }
    return longValue(val, 1, len);
  }

  public static long longValue(final int[] mag, final int off, final int len, final int sig) {
    if (len == 0)
      return 0;

    final long longValue = longValue0(mag, off, len);
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
    return len == 0 ? 0 : longValue0(mag, off, len);
  }

  static long longValue0(final int[] mag, final int off, final int len) {
    final long val0l = mag[off] & LONG_INT_MASK;
    return len > 1 ? (long)mag[off + 1] << 32 | val0l : val0l;
  }

  public static float floatValue(final int[] val) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    return floatValue(val, 1, len, sig);
  }

  public static float floatValue(final int[] mag, final int off, final int len, final int sig) {
    if (len == 0)
      return 0;

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
    if (len == 0)
      return 0;

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

    for (long v1, v2; len1 >= 1; --len1) {
      v1 = val1[len1] & LONG_INT_MASK;
      v2 = val2[len1] & LONG_INT_MASK;
      if (v1 > v2)
        return 1;

      if (v1 < v2)
        return -1;
    }

    return 0;
  }

  public static int compareTo(final int[] val1, final int[] val2) {
    int sig1 = 1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; sig1 = -1; }

    int sig2 = 1, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; sig2 = -1; }

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
    int len1 = val1[0];

    if (len1 != val2[0])
      return false;

    if (len1 == 0)
      return true;

    if (len1 < 0)
      len1 = -len1;

    for (; len1 >= 1; --len1)
      if (val1[len1] != val2[len1])
        return false;

    return true;
  }

  public static int hashCode(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 0;

    boolean sig = true; if (len < 0) { len = -len; sig = false; }

    int hash = 0;
    for (; len >= 1; --len)
      hash = 31 * hash + val[len] & INT_MASK;

    return sig ? hash : -hash;
  }

  private static final int pow5 = 1_220_703_125;
  private static final int pow2 = 1 << 13;

  /**
   * Divides the provided value-encoded number by {@code 10^13} and returns the
   * remainder. Does not change the sign of the number.
   *
   * @param val The value-encoded number.
   * @param len The count of limbs to divide.
   * @return The remainder of the division of the provided value-encode number
   *         by {@code 10^13}.
   */
  private static long toStringDiv(final int[] val, int len) {
    int q1 = 0;
    long r = 0;
    for (int q0; len > 0; --len) {
      r = (r << 32) + (val[len] & LONG_INT_MASK);
      q0 = (int)(r / pow5);
      r = r % pow5;
      val[len] = q1 | q0 >>> 13;
      q1 = q0 << 32 - 13;
    }

    r = (r << 32) + (val[0] & LONG_INT_MASK);
    final int mod2 = val[0] & pow2 - 1;
    val[0] = q1 | (int)(r / pow5 >>> 13);
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

  public static int[] valueOf(final byte[] mag, final int off, final int len, final boolean isLittleEndian) {
    return assign(emptyVal, mag, off, len, isLittleEndian);
  }

  public static int[] valueOf(final byte[] mag, final boolean isLittleEndian) {
    return assign(emptyVal, mag, 0, mag.length, isLittleEndian);
  }

  public static int[] valueOf(final char[] s) {
    return assign(emptyVal, s);
  }

  public static int[] valueOf(final String s) {
    return assign(emptyVal, s);
  }

  /**
   * Converts the provided value-encoded number into a string of radix 10.
   *
   * @param val The value-encoded number.
   * @return The string representation of the provided value-encoded number in
   *         radix 10.
   * @complexity O(n^2)
   */
  public static String toString(final int[] val) {
    if (isZero(val))
      return "0";

    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    int j, top = len * 10 + 3;
    final char[] chars = new char[top];
    Arrays.fill(chars, '0');
    final int[] mag = new int[len];
    System.arraycopy(val, 1, mag, 0, len);
    long tmp;
    while (true) {
      j = top;
      tmp = toStringDiv(mag, len - 1);
      if (mag[len - 1] == 0 && len > 1 && mag[--len - 1] == 0 && len > 1)
        --len;

      for (; tmp > 0; tmp /= 10)
        chars[--top] += tmp % 10; // TODO: Optimize.

      if (len == 1 && mag[0] == 0)
        break;

      top = j - 13;
    }

    if (sig < 0)
      chars[--top] = '-';

    return new String(chars, top, chars.length - top);
  }

  // FIXME: There must be a more efficient way to do this!
  public static int precision(final int[] val) {
    if (isZero(val))
      return 1;

    int len = val[0]; if (len < 0) { len = -len; }

    final int length = len * 10 + 3;
    int j, top = length;
    final int[] mag = new int[len];
    System.arraycopy(val, 1, mag, 0, len);
    long tmp;
    while (true) {
      j = top;
      tmp = toStringDiv(mag, len - 1);
      if (mag[len - 1] == 0 && len > 1 && mag[--len - 1] == 0 && len > 1)
        --len;

      for (; tmp > 0; tmp /= 10)
        --top;

      if (len == 1 && mag[0] == 0)
        break;

      top = j - 13;
    }

    return length - top;
  }

  /**
   * Sets the magnitude of the provided value-encoded number its absolute value.
   *
   * <pre>
   * {@code val = | val |}
   * </pre>
   *
   * @param val The value-encoded number.
   * @return The provided value-encoded number with the magnitude set to its
   *         absolute value.
   */
  public static int[] abs(final int[] val) {
    if (val[0] < 0)
      val[0] = -val[0];

    _debugLenSig(val);
    return val;
  }

  /**
   * Returns the maximum of the provided value-encoded numbers.
   *
   * @param val1 The first value-encoded number.
   * @param val2 The second value-encoded number.
   * @return The value-encoded number whose value is the greater of the provided
   *         value-encoded numbers.
   */
  public static int[] max(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) > 0 ? val1 : val2;
  }

  /**
   * Returns the minimum of the provided value-encoded numbers.
   *
   * @param val1 The first value-encoded number.
   * @param val2 The second value-encoded number.
   * @return The value-encoded number whose value is the lesser of the provided
   *         value-encoded numbers.
   */
  public static int[] min(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) < 0 ? val1 : val2;
  }

  static void _debugLenSig(final int[] val) {
    if (!isZero(val) && val[Math.abs(val[0])] == 0)
      throw new IllegalStateException(Arrays.toString(val));
  }
}