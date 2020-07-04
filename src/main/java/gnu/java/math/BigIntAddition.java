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
abstract class BigIntAddition extends BigIntMagnitude {
  private static final long serialVersionUID = 2873086066678372875L;

  /**
   * Adds an unsigned {@code int} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] uadd(int[] val, final int a) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    if (signum >= 0) {
      val = uaddVal(val, a);
    }
    else if (len > 1 || (val[1] & LONG_INT_MASK) > (a & LONG_INT_MASK)) {
      usubVal(val, a);
    }
    else {
      if ((val[1] = a - val[1]) == 0)
        --len;

      val[0] = len;
    }

    _debugLenSig(val);
    return val;
  }

  static int[] uadd0(int[] val, final long a, final long ah) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    final long al = a & LONG_INT_MASK;
    // FIXME: This will fail for 0 value
    final long val0l = val[1] & LONG_INT_MASK;
    final long val0h = len <= 1 ? 0 : val[2] & LONG_INT_MASK;
    val = BigIntAddition.uadd(val, val0l, val0h, al, ah, len, signum, true);
    return val;
  }

  static int[] usub0(final int[] val, final long s, final long sh) {
    return BigIntAddition.uadd(val, s & LONG_INT_MASK, sh, false);
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param s The amount to subtract (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] usub(int[] val, final int s) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    if (signum < 0) {
      val = uaddVal(val, s);
    }
    else if (len == 1 && (val[1] & LONG_INT_MASK) < (s & LONG_INT_MASK)) {
      val[0] = -len;
      val[1] = s - val[1];
    }
    else {
      usubVal(val, s);
    }

    _debugLenSig(val);
    return val;
  }

  static int[] uadd(int[] val, final long al, final long ah, final boolean positive) {
    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }

    final long val0l = val[1] & LONG_INT_MASK;
    final long val1l = len == 1 ? 0 : val[2] & LONG_INT_MASK;
    return uadd(val, val0l, val1l, al, ah, len, signum, positive);
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uadd(int[] val, final long val0l, final long val1l, final long al, final long ah, int len, int signum, final boolean positive) {
    if (positive ? signum >= 0 : signum <= 0) {
      val = uaddVal(val, len, signum, val0l, val1l, al, ah);
    }
    else {
      if (val.length <= 2)
        val = realloc(val, 3);

      if (len > 2 || len == 2 && (val1l > ah || val1l == ah && val0l >= al) || ah == 0 && val0l >= al) {
        usubVal(val, val0l, val1l, al, ah); // FIXME: Pass len, signum, positive?
      }
      else {
        if (len == 1)
          val[++len] = 0;

        long dif = al - val0l;
        val[1] = (int)dif;
        dif >>= 32;
        dif = ah - val1l + dif;
        val[2] = (int)dif;
        // dif >> 32 != 0 should be impossible
        if (dif == 0)
          --len;

        signum = positive ? 1 : -1;
        val[0] = signum < 0 ? -len : len;
      }
    }

    _debugLenSig(val);
    return val;
  }

  public static int[] add(int[] val1, final int[] val2) {
    if (compareToAbs(val1, val2) >= 0) {
      subVal(val1, val2);
    }
    else {
      int signum1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; signum1 = -1; }
      int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }

      if (len2 >= val1.length)
        val1 = realloc(val1, len2 + 2);

      signum1 = -signum1;
      long dif = 0;
      int i = 1;
      for (; i <= len1; ++i) {
        dif = (val2[i] & LONG_INT_MASK) - (val1[i] & LONG_INT_MASK) + dif;
        val1[i] = (int)dif;
        dif >>= 32;
      }

      if (len2 > len1) {
        System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);
        len1 = len2;
      }

      if (dif != 0) {
        for (; i <= len2 && val1[i] == 0; ++i)
          --val1[i];

        if (--val1[i] == 0 && i == len1)
          while (val1[--len1] == 0);
      }
      else if (val1[len1] == 0) {
        --len1;
      }

      val1[0] = signum1 < 0 ? -len1 : len1;
    }

    _debugLenSig(val1);
    return val1;
  }

  public static int[] add(int[] val1, final int[] val2, final boolean positive) {
    if (positive == (val1[0] < 0 == val2[0] < 0))
      return addVal(val1, val2, positive);

    return add(val1, val2);
  }
}