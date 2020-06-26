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

@SuppressWarnings("javadoc")
abstract class BigMagnitude extends BigNumber {
  private static final long serialVersionUID = 734086338662551150L;

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static void usubMag(final int[] val, final long s) {
    final long sl = s & LONG_INT_MASK;
    final long sh = s >>> 32;
    final long val0l = val[1] & LONG_INT_MASK;
    final long val1l = val[0] & LONG_INT_MASK;
    usubMag(val, val0l, val1l, sl, sh);
  }

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static void usubMag(final int[] val, final long val0, final long val1, final long sl, final long sh) {
    int len = val[0];
    long dif = val0 - sl;
    val[2] = (int)dif;
    dif >>= 32;
    dif = val1 - sh + dif;
    val[3] = (int)dif;

    // Subtract remainder of longer number while borrow propagates
    boolean borrow = dif >> 32 != 0;
    for (int i = 3; i < len && borrow;)
      borrow = --val[++i] == -1;

    if (len == 4 && val[3] == 0)
      --len;

    val[0] = len;
    if (len == 3 && val[2] == 0)
      val[1] = 0;
  }

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static void usubMag(final int[] val, final int s) {
    long dif = (val[2] & LONG_INT_MASK) - (s & LONG_INT_MASK);
    val[2] = (int)dif;
    if ((dif >> 32) == 0) {
      if (isZero(val))
        val[1] = 0;

      return;
    }

    int len = val[0];
    int i = 3;
    for (; val[i] == 0; ++i)
      --val[i];

    if (--val[i] == 0 && i + 1 == len)
      --len;

    val[0] = len;
  }

  /**
   * Decreases the magnitude of this number by the given magnitude array.
   * Behavior is undefined if u > |this|.
   *
   * @param val2 The magnitude array of the decrease.
   * @param vlen The length (number of digits) of the decrease.
   * @complexity O(n)
   */
  public static int subMag(final int[] val1, final int[] val2) {
    int len1 = val1[0];
    final int len2 = val2[0];
    final int[] v = val1; // ulen <= vlen // FIXME: Why v?!

    // Assumes vlen=len and v=dig
    long dif = 0;
    int i = 2;
    for (; i < len2; ++i) {
      dif = (v[i] & LONG_INT_MASK) - (val2[i] & LONG_INT_MASK) + dif;
      val1[i] = (int)dif;
      dif >>= 32;
    }

    if (dif != 0) {
      for (; val1[i] == 0; ++i)
        --val1[i];

      if (--val1[i] == 0 && i + 1 == len1)
        len1 = len2;
    }

    while (len1 > 3 && val1[len1 - 1] == 0)
      --len1;

    val1[0] = len1;
    if (len1 == 3 && val1[2] == 0)
      val1[1] = 0;

    return len1;
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uaddMag(int[] val, final int a) {
    long tmp = (val[2] & LONG_INT_MASK) + (a & LONG_INT_MASK);
    val[2] = (int)tmp;
    if ((tmp >>> 32) != 0) {
      int len = val[0];
      int i = 3;
      for (; i < len && ++val[i] == 0; ++i);
      if (i == len) {
        if (len == val.length)
          val = realloc(val);

        val[len] = 1;
        val[0] = len + 1;
      }
    }

    return val;
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uaddMag(int[] val, final long a) {
    if (val.length <= 2) {
      val = realloc(val, 3);
      val[0] = 4; // FIXME: Why is this being set here?!
    }

    final long al = a & LONG_INT_MASK;
    final long ah = a >>> 32;
    final long val0l = val[1] & LONG_INT_MASK;
    final long val1l = val[0] & LONG_INT_MASK;
    return uaddMag(val, val0l, val1l, al, ah);
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddMag(int[] val, final long val0l, final long val1l, final long al, final long ah) {
    int len = val[0];
    if (val.length <= 4)
      val = realloc(val, 5);

    long carry = val0l + al;
    val[2] = (int)carry;
    carry >>>= 32;
    carry = val1l + ah + carry;
    val[3] = (int)carry;
    if (len == 3 && val[3] != 0)
      len = 4; // KL(m) change (new line)

    if ((carry >> 32) != 0) {
      int i = 4;
      for (; i < len && ++val[i] == 0; ++i);
      if (i == len) {
        if (len == val.length)
          val = realloc(val, len);

        val[len++] = 1;
      }
    }
    else if (len == 2 && val[3] == 0) {
      --len;
    }

    val[0] = len;
    return val;
  }

  /**
   * Increases the magnitude of this number by the given magnitude array.
   *
   * @param val2 The magnitude array of the increase.
   * @param len2 The length (number of digits) of the increase.
   * @complexity O(n)
   */
  public static int[] addMag(int[] val1, int[] val2, final boolean positive) {
    int len1 = val1[0];
    int len2 = val2[0];

    int len0 = len1;
    int[] val0 = val1; // ulen <= vlen
    if (len2 < len0) {
      val0 = val2;
      val2 = val1;
      len0 = len2;
      len2 = len1;
    }

    if (len2 > val1.length)
      val1 = realloc(val1, len2 + 1);

    long carry = 0;
    int i = 2;
    for (; i < len0; ++i) {
      carry = (val0[i] & LONG_INT_MASK) + (val2[i] & LONG_INT_MASK) + carry;
      val1[i] = (int)carry;
      carry >>>= 32;
    }

    if (len2 > len1) {
      System.arraycopy(val2, len1, val1, len1, len2 - len1);
      val1[0] = len1 = len2;
    }

    if (carry != 0) { // carry==1
      for (; i < len1 && ++val1[i] == 0; ++i);
      if (i == len1) { // vlen==len
        if (len1 == val1.length)
          val1 = realloc(val1);

        val1[len1] = 1;
        val1[0] = len1 + 1;
      }
    }

    if (val1[1] == 0)
      val1[1] = positive ? val2[1] : -val2[1];

    return val1;
  }
}