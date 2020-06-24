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
  public static int usubMag(final int[] mag, final int len, final long s) {
    final long sl = s & LONG_INT_MASK;
    final long sh = s >>> 32;
    final long mag0l = mag[0] & LONG_INT_MASK;
    final long mag1l = mag[1] & LONG_INT_MASK;
    return usubMag(mag, mag0l, mag1l, len, sl, sh);
  }

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int usubMag(final int[] mag, final long mag0l, final long mag1l, int len, final long sl, final long sh) {
    long dif = mag0l - sl;
    mag[0] = (int)dif;
    dif >>= 32;
    dif = mag1l - sh + dif;
    mag[1] = (int)dif;
    if ((dif >> 32) != 0) {
      int i = 2;
      for (; mag[i] == 0; ++i)
        --mag[i];

      if (--mag[i] == 0 && i + 1 == len)
        --len;
    }

    if (len == 2 && mag[1] == 0)
      --len;

    return len;
  }

  /**
   * Decreases the magnitude of this number. If s > this behavior is undefined.
   *
   * @param s The amount of the decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int usubMag(final int[] mag, int len, final int s) {
    long dif = (mag[0] & LONG_INT_MASK) - (s & LONG_INT_MASK);
    mag[0] = (int)dif;
    if ((dif >> 32) == 0)
      return len;

    int i = 1;
    for (; mag[i] == 0; ++i)
      --mag[i];

    if (--mag[i] == 0 && i + 1 == len)
      --len;

    return len;
  }

  /**
   * Decreases the magnitude of this number by the given magnitude array.
   * Behavior is undefined if u > |this|.
   *
   * @param u The magnitude array of the decrease.
   * @param vlen The length (number of digits) of the decrease.
   * @complexity O(n)
   */
  public static int subMag(final int[] mag, int len, final int[] u, final int ulen) {
    final int[] v = mag; // ulen <= vlen

    // Assumes vlen=len and v=dig
    long dif = 0;
    int i = 0;
    for (; i < ulen; ++i) {
      dif = (v[i] & LONG_INT_MASK) - (u[i] & LONG_INT_MASK) + dif;
      mag[i] = (int)dif;
      dif >>= 32;
    }
    if (dif != 0) {
      for (; mag[i] == 0; ++i)
        --mag[i];
      if (--mag[i] == 0 && i + 1 == len)
        len = ulen;
    }

    while (len > 1 && mag[len - 1] == 0)
      --len;

    return len;
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int uaddMag(final int[] mag, final int len, final int a) {
    long tmp = (mag[0] & LONG_INT_MASK) + (a & LONG_INT_MASK);
    mag[0] = (int)tmp;
    if ((tmp >>> 32) == 0)
      return 0;

    int i = 1;
    for (; i < len && ++mag[i] == 0; ++i);
    return i;
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uaddMag(int[] mag, int len, final long a) {
    if (mag.length <= 2) {
      mag = realloc(mag, len, 3);
      len = 2;
    }

    final long al = a & LONG_INT_MASK;
    final long ah = a >>> 32;
    final long mag0l = mag[0] & LONG_INT_MASK;
    final long mag1l = mag[1] & LONG_INT_MASK;
    return uaddMag(mag, mag0l, mag1l, len, al, ah);
  }

  /**
   * Increases the magnitude of this number.
   *
   * @param a The amount of the increase (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddMag(int[] mag, final long mag0l, final long mag1l, int len, final long al, final long ah) {
    if (mag.length <= 2) {
      mag = realloc(mag, len, 3);
      len = 2;
    }

    long carry = mag0l + al;
    mag[0] = (int)carry;
    carry >>>= 32;
    carry = mag1l + ah + carry;
    mag[1] = (int)carry;
    if (len == 1 && mag[1] != 0)
      len = 2; // KL(m) change (new line)

    if ((carry >> 32) != 0) {
      int i = 2;
      for (; i < len && ++mag[i] == 0; ++i);
      if (i == len) {
        if (len == mag.length)
          mag = realloc(mag, len);

        mag[len++] = 1;
      }
    }
    else if (len == 2 && mag[1] == 0) {
      --len;
    }

    return mag;
  }

  /**
   * Increases the magnitude of this number by the given magnitude array.
   *
   * @param v The magnitude array of the increase.
   * @param vlen The length (number of digits) of the increase.
   * @complexity O(n)
   */
  public static int[] addMag(int[] mag, int len, int[] v, int vlen) {
    int ulen = len;
    int[] u = mag; // ulen <= vlen
    if (vlen < ulen) {
      u = v;
      v = mag;
      ulen = vlen;
      vlen = len;
    }

    if (vlen > mag.length)
      mag = realloc(mag, len, vlen + 1);

    long carry = 0;
    int i = 0;
    for (; i < ulen; ++i) {
      carry = (u[i] & LONG_INT_MASK) + (v[i] & LONG_INT_MASK) + carry;
      mag[i] = (int)carry;
      carry >>>= 32;
    }

    if (vlen > len) {
      System.arraycopy(v, len, mag, len, vlen - len);
      len = vlen;
    }

    if (carry != 0) { // carry==1
      for (; i < len && ++mag[i] == 0; ++i);
      if (i == len) { // vlen==len
        if (len == mag.length)
          mag = realloc(mag, len);

        mag[len++] = 1;
      }
    }

    return mag;
  }
}