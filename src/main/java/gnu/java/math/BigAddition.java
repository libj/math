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
  public static int[] uadd(int[] val, final int a) {
    final int len = val[0];
    final int signum = val[1];
    if (signum >= 0) {
      val = uaddMag(val, a);
    }
    else if (len > 3 || (val[2] & LONG_INT_MASK) > (a & LONG_INT_MASK)) {
      usubMag(val, a);
    }
    else {
      val[1] = 1;
      val[2] = a - val[2];
    }

    return val;
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param s The amount to subtract (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] usub(int[] val, final int s) {
    final int len = val[0];
    final int signum = val[1];
    if (signum < 0) {
//      if (len == 3 && (val[2] & LONG_INT_MASK) == (s & LONG_INT_MASK))
//        setToZero(val);
//      else
      val = uaddMag(val, s);
    }
    else if (len == 3 && (val[2] & LONG_INT_MASK) < (s & LONG_INT_MASK)) {
      val[1] = -1;
      val[2] = s - val[2];
    }
    else {
      usubMag(val, s);
    }

    return val;
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uadd(int[] val, final long a, final boolean positive) {
    final long al = a & LONG_INT_MASK;
    final long ah = a >>> 32;
    return uadd(val, al, ah, positive);
  }

  static int[] uadd(int[] val, final long al, final long ah, final boolean positive) {
    final int len = val[0];

    final long val0l = val[2] & LONG_INT_MASK;
    final long val1l = len == 3 ? 0 : val[3] & LONG_INT_MASK;
    return uadd(val, val0l, val1l, al, ah, positive);
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uadd(int[] val, final long val0l, final long val1l, final long al, final long ah, final boolean positive) {
    final int signum = val[1];
    if (positive ? signum >= 0 : signum <= 0) {
      val = uaddMag(val, val0l, val1l, al, ah);
    }
    else {
      if (val.length == 3)
        val = realloc(val, 4);

      int len = val[0];
      if (len > 4 || len == 4 && (val1l > ah || val1l == ah && val0l >= al) || ah == 0 && val0l >= al) {
        usubMag(val, val0l, val1l, al, ah);
      }
      else {
        if (len == 3) {
          val[len++] = 0;
          val[0] = len;
        }

        long dif = al - val0l;
        val[2] = (int)dif;
        dif >>= 32;
        dif = ah - val1l + dif;
        val[3] = (int)dif;
        // dif >> 32 != 0 should be impossible
        if (dif == 0)
          val[0] = len - 1;

        val[1] = positive ? 1 : -1;
      }
    }

    return val;
  }

  public static int[] add(int[] val1, final int[] val2) {
    if (compareAbsTo(val1, val2) >= 0) {
      subMag(val1, val2);
      // if(len==1 && dig[0]==0) sign = 1;
    }
    else {
      int len1 = val1[0];
      final int len2 = val2[0];
      final int[] v = val2; // FIXME: Why create v?
      final int vlen = len2;
      if (val1.length < vlen)
        val1 = realloc(val1, vlen + 1);

      val1[1] = -val1[1];
      long dif = 0;
      int i = 2;
      for (; i < len1; ++i) {
        dif = (v[i] & LONG_INT_MASK) - (val1[i] & LONG_INT_MASK) + dif;
        val1[i] = (int)dif;
        dif >>= 32;
      }

      if (vlen > len1) {
        System.arraycopy(v, len1, val1, len1, vlen - len1);
        val1[0] = len1 = vlen;
      }

      if (dif != 0) {
        for (; i < vlen && val1[i] == 0; ++i)
          --val1[i];

        if (--val1[i] == 0 && i + 1 == len1)
          --len1;

        val1[0] = len1;
      }
      else if (val1[len1 - 1] == 0) {
        val1[0] = len1 - 1;
      }
    }

    // if(i==vlen) should be impossible
    return val1;
  }

  public static int[] add(int[] val1, final int[] val2, final boolean positive) {
    final int signum1 = val1[1];
    final int signum2 = val2[1];
    if (positive ? signum1 == signum2 : signum1 != signum2)
      return addMag(val1, val2, positive);

    return add(val1, val2);
  }
}