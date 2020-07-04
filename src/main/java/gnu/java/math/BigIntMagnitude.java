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
abstract class BigIntMagnitude extends BigIntValue {
  private static final long serialVersionUID = 734086338662551150L;

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static void usubVal(final int[] val, final long val0, final long val1, final long sl, final long sh) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }

    long dif = val0 - sl;
    val[1] = (int)dif;
    dif >>= 32;
    dif = val1 - sh + dif;
    val[2] = (int)dif;

    // Subtract remainder of longer number while borrow propagates
    boolean borrow = dif >> 32 != 0;
    for (int i = 2; i <= len && borrow;)
      borrow = --val[++i] == -1;

    // FIXME: This can be optimized to not require a dedicated loop
    while (val[len] == 0)
      --len;

    val[0] = signum < 0 ? -len : len;
    _debugLenSig(val);
  }

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static void usubVal(final int[] val, final int s) {
    if (val[0] == 0) {
      val[0] = -1;
      val[1] = s;
    }
    else {
      int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
      long dif = (val[1] & LONG_INT_MASK) - (s & LONG_INT_MASK);
      val[1] = (int)dif;
      if ((dif >> 32) != 0) {
        int i = 2;
        for (; val[i] == 0; ++i)
          --val[i];

        if (--val[i] != 0 || i != len) {
          _debugLenSig(val);
          return;
        }
      }
      else if (val[len] != 0) {
        _debugLenSig(val);
        return;
      }

      --len;
      val[0] = signum < 0 ? -len : len;
    }

    _debugLenSig(val);
  }

  /**
   * Decreases the magnitude of this number by the given magnitude array.
   * Behavior is undefined if u > |this|.
   *
   * @param val2 The magnitude array of the decrease.
   * @param vlen The length (number of digits) of the decrease.
   * @complexity O(n)
   */
  static void subVal(final int[] val1, final int[] val2) {
    int signum1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; signum1 = -1; }
    int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }

    final int[] v = val1; // ulen <= vlen // FIXME: Why v?!

    // Assumes vlen=len and v=dig
    long dif = 0;
    int i = 1;
    for (; i <= len2; ++i) {
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

    while (val1[len1] == 0 && len1 >= 1)
      --len1;

    val1[0] = signum1 < 0 ? -len1 : len1;
    _debugLenSig(val1);
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddVal(int[] val, final int a) {
    if (val[0] == 0) {
      val[0] = 1;
      val[1] = a;
    }
    else {
      final long tmp = (val[1] & LONG_INT_MASK) + (a & LONG_INT_MASK);
      val[1] = (int)tmp;
      if ((tmp >>> 32) != 0) {
        int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
        ++len;
        int i = 2;
        for (; i < len && ++val[i] == 0; ++i);
        if (i == len) {
          if (len == val.length)
            val = realloc(val, len + 1);

          val[len] = 1;
          val[0] = signum < 0 ? -len : len;
        }
      }
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddVal(int[] val, int len, final int signum, final long val0l, final long val1l, final long al, final long ah) {
    // FIXME: Change int signum to boolean signum
    if (val.length <= 3)
      val = realloc(val, 4); // FIXME: 4 or 3?

    long carry = val0l + al;
    val[1] = (int)carry;
    carry >>>= 32;
    carry = val1l + ah + carry;
    val[2] = (int)carry;
    if (carry != 0 && len < 2)
      ++len;

    if ((carry >> 32) != 0) {
      int i = 3;
      for (; i <= len && ++val[i] == 0; ++i);
      if (i == len + 1) { // FIXME: Change this to < ?
        len = i;
        if (len == val.length)
          val = realloc(val, len);

        val[len] = 1;
      }
    }
    else if (val[len] == 0) {
      --len;
    }

    val[0] = signum < 0 ? -len : len;
    _debugLenSig(val);
    return val;
  }

  /**
   * Increases the magnitude of this number by the given magnitude array.
   *
   * @param val2 The magnitude array of the increase.
   * @param len2 The length (number of digits) of the increase.
   * @complexity O(n)
   */
  static int[] addVal(int[] val1, int[] val2, final boolean positive) {
    int signum1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; signum1 = -1; }
    int signum2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; signum2 = -1; }

    int len0 = len1;
    int[] val0 = val1; // ulen <= vlen
    if (len2 < len0) {
      val0 = val2;
      val2 = val1;
      len0 = len2;
      len2 = len1;
    }

    if (len2 >= val1.length)
      val1 = realloc(val1, len2 + 2);

    long carry = 0;
    int i = 1;
    for (; i <= len0; ++i) {
      carry = (val0[i] & LONG_INT_MASK) + (val2[i] & LONG_INT_MASK) + carry;
      val1[i] = (int)carry;
      carry >>>= 32;
    }

    if (len2 > len1) {
      System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);
      len1 = len2;
      val1[0] = signum1 < 0 ? -len1 : len1;
    }

    if (carry != 0) { // carry==1
      for (; i <= len1 && ++val1[i] == 0; ++i);
      if (i > len1) { // vlen==len
        if (i == val1.length)
          val1 = realloc(val1, i + 1);

        val1[++len1] = 1;
        val1[0] = signum1 < 0 ? -len1 : len1;
      }
    }

    _debugLenSig(val1);
    return val1;
  }
}