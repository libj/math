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

  public static int[] add(final int[] val, final int add) {
    if (add > 0)
      return add(val, 1, add);

    if (add < 0)
      return sub(val, 1, -add);

    return val;
  }

  public static int[] sub(final int[] val, final int sub) {
    if (sub > 0)
      return sub(val, 1, sub);

    if (sub < 0)
      return add(val, 1, -sub);

    return val;
  }

  /**
   * Adds an unsigned {@code int} to this number.
   *
   * @param add The amount to add (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(int[] val, final int sig, final int add) {
    if (add == 0)
      return val;

    if (sig < 0)
      return sub(val, 1, add);

    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    if (signum >= 0) {
      val = uaddVal(val, add);
    }
    else if (len > 1 || (val[1] & LONG_INT_MASK) > (add & LONG_INT_MASK)) {
      usubVal(val, add);
    }
    else {
      if ((val[1] = add - val[1]) == 0)
        --len;

      val[0] = len;
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param sub The amount to subtract (treated as unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(int[] val, final int sig, final int sub) {
    if (sig < 0)
      return add(val, 1, sub);

    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    if (signum < 0) {
      val = uaddVal(val, sub);
    }
    else if (len == 1 && (val[1] & LONG_INT_MASK) < (sub & LONG_INT_MASK)) {
      val[0] = -len;
      val[1] = sub - val[1];
    }
    else {
      usubVal(val, sub);
    }

    _debugLenSig(val);
    return val;
  }

  public static int[] add(final int[] val, final long add) {
    if (add > 0)
      return add(val, 1, add);

    if (add < 0)
      return sub(val, 1, -add);

    return val;
  }

  public static int[] sub(final int[] val, final long sub) {
    if (sub > 0)
      return sub(val, 1, sub);

    if (sub < 0)
      return add(val, 1, -sub);

    return val;
  }

  public static int[] add(final int[] val, final int sig, final long add) {
    if (isZero(val))
      return assign(val, sig, add);

    if (sig < 0)
      return sub(val, 1, add);

    final long addh = add >>> 32;
    if (addh == 0)
      return add(val, sig, (int)add);

    int signum = 1, len = val[0]; if (len < 0) { len = -len; signum = -1; }
    final long al = add & LONG_INT_MASK;
    // FIXME: This will fail for 0 value
    final long val0l = val[1] & LONG_INT_MASK;
    final long val0h = len <= 1 ? 0 : val[2] & LONG_INT_MASK;
    return uadd(val, val0l, val0h, al, addh, len, signum, true);
  }

  public static int[] sub(final int[] val, final int sig, final long sub) {
    if (sig < 0)
      return add(val, 1, sub);

    final long subh = sub >>> 32;
    if (subh == 0)
      return sub(val, sig, (int)sub);

    return add(val, sub & LONG_INT_MASK, subh, false);
  }

  public static int[] add(int[] val1, final int[] val2) {
    return add(val1, val2, true);
  }

  public static int[] sub(int[] val1, final int[] val2) {
    return add(val1, val2, false);
  }

  private static int[] add(int[] val, final long al, final long ah, final boolean positive) {
    if (isZero(val)) {
      if (val.length <= 2)
        val = realloc(val, 3);

      int len = 1;
      val[1] = (int)al;
      val[2] = (int)ah;
      if (ah != 0)
        ++len;

      val[0] = positive ? len : -len;
      _debugLenSig(val);
      return val;
    }

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
  private static int[] uadd(int[] val, final long val0l, final long val1l, final long al, final long ah, int len, int signum, final boolean positive) {
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

  private static int[] add(int[] val1, final int[] val2, final boolean positive) {
    if (isZero(val1)) {
      if (isZero(val2))
        return val1;

      final int len = Math.abs(val2[0]) + 1;
      if (len > val1.length)
        val1 = alloc(len);

      System.arraycopy(val2, 0, val1, 0, len);
      if (!positive)
        val1[0] = -val1[0];

      return val1;
    }

    if (isZero(val2))
      return val1;

    if (positive == (val1[0] < 0 == val2[0] < 0))
      return addVal(val1, val2, positive);

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
}