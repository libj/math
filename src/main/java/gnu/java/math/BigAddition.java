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
abstract class BigAddition extends BigMagnitude {
  private static final long serialVersionUID = 2873086066678372875L;

  /**
   * Adds an unsigned {@code int} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uadd(int[] mag, int len, final int signum, final int a) {
    if (signum >= 0) {
      final int i = uaddMag(mag, len, a);
      if (i == len) {
        if (len == mag.length)
          mag = realloc(mag, len);

        mag[len++] = 1;
      }
    }
    else if (len > 1 || (mag[0] & LONG_INT_MASK) > (a & LONG_INT_MASK)) {
      len = usubMag(mag, len, a);
    }
    else {
      // signum = 1; // Handled by caller, left for reference
      mag[0] = a - mag[0];
    }

    clear(mag, len);
    return mag;
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param s The amount to subtract (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] usub(int[] mag, int len, final int signum, final int s) {
    if (signum < 0) {
      final int i = uaddMag(mag, len, s);
      if (i == len) {
        if (len == mag.length)
          mag = realloc(mag, len);

        mag[len++] = 1;
      }
    }
    else if (len == 1 && (mag[0] & LONG_INT_MASK) < (s & LONG_INT_MASK)) {
      // signum = -1; // Handled by caller, left for reference
      mag[0] = s - mag[0];
    }
    else {
      len = usubMag(mag, len, s);
    }

    clear(mag, len);
    return mag;
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uadd(int[] mag, int len, final int signum, final long a, final boolean positive) {
    final long al = a & LONG_INT_MASK;
    final long ah = a >>> 32;
    final long mag0l = mag[0] & LONG_INT_MASK;
    final long mag1l = len == 1 ? 0 : mag[1] & LONG_INT_MASK;
    return uadd(mag, mag0l, mag1l, len, signum, al, ah, positive);
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uadd(int[] mag, final long mag0l, final long mag1l, int len, final int signum, final long al, final long ah, final boolean positive) {
    if (positive ? signum >= 0 : signum <= 0) {
      mag = uaddMag(mag, mag0l, mag1l, len, al, ah);
    }
    else {
      if (mag.length == 1)
        mag = realloc(mag, len, 2);

      if (len > 2 || len == 2 && (mag1l > ah || mag1l == ah && mag0l >= al) || ah == 0 && mag0l >= al) {
        len = usubMag(mag, mag0l, mag1l, len, al, ah);
      }
      else {
        if (len == 1)
          mag[len++] = 0;

        long dif = al - mag0l;
        mag[0] = (int)dif;
        dif >>= 32;
        dif = ah - mag1l + dif;
        mag[1] = (int)dif;
        // dif >> 32 != 0 should be impossible
        // if (dif == 0) --len; // Handled by caller, left for reference
        // signum = positive ? 1 : -1; // Handled by caller, left for reference
      }
    }

    return mag;
  }

  public static int[] add(int[] mag1, int len1, final int[] mag2, final int len2) {
    if (compareAbsTo(mag1, len1, mag2, len2) >= 0) {
      len1 = subMag(mag1, len1, mag2, len2);
      // if(len==1 && dig[0]==0) sign = 1;
    }
    else {
      final int[] v = mag2;
      final int vlen = len2;
      if (mag1.length < vlen)
        mag1 = realloc(mag1, len1, vlen + 1);

      // signum1 = -signum1; // Handled by caller, left for reference
      long dif = 0;
      int i = 0;
      for (; i < len1; ++i) {
        dif = (v[i] & LONG_INT_MASK) - (mag1[i] & LONG_INT_MASK) + dif;
        mag1[i] = (int)dif;
        dif >>= 32;
      }

      if (vlen > len1) {
        System.arraycopy(v, len1, mag1, len1, vlen - len1);
        len1 = vlen;
      }

      if (dif != 0) {
        for (; i < vlen && mag1[i] == 0; ++i)
          --mag1[i];

        if (--mag1[i] == 0 && i + 1 == len1)
          --len1;
      }
    }

    // if(i==vlen) should be impossible
    return mag1;
  }

  public static int[] add(int[] mag1, final int len1, final int signum1, final int[] mag2, final int len2, final int signum2, final boolean positive) {
    if (positive ? signum1 == signum2 : signum1 != signum2)
      return addMag(mag1, len1, mag2, len2);

    return add(mag1, len1, mag2, len2);
  }
}