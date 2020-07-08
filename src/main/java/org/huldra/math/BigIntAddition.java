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

abstract class BigIntAddition extends BigIntMagnitude {
  private static final long serialVersionUID = 2873086066678372875L;

  /**
   * Adds the value of an {@code int} to the provided number.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param add The amount to add.
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(final int[] val, final int add) {
    return add > 0 ? add(val, 1, add) : add < 0 ? sub(val, 1, -add) : val;
  }

  /**
   * Subtracts the value of an {@code int} from the provided number.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sub The amount to subtract.
   * @return The result of the subtraction of the specified amount from the
   *         provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(final int[] val, final int sub) {
    return sub > 0 ? sub(val, 1, sub) : sub < 0 ? add(val, 1, -sub) : val;
  }

  /**
   * Adds the value of an <i>unsigned</i> {@code int} to the provided number.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sig The sign of the unsigned {@code int} to add.
   * @param add The amount to add (unsigned).
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(final int[] val, final int sig, final int add) {
    return add == 0 ? val : sig < 0 ? sub0(val, add) : add0(val, add);
  }

  private static int[] add0(int[] val, final int add) {
    int len = val[0];
    if (len == 0) {
      assign0(val.length >= 2 ? val : alloc(2), 1, add);
    }
    else if (len >= 0) {
      val = uaddVal(val, len, true, add);
    }
    else if ((len = -len) > 1 || (val[1] & LONG_INT_MASK) > (add & LONG_INT_MASK)) {
      usubVal(val, len, false, add);
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
   * Subtracts the value of an <i>unsigned</i> {@code int} from the provided
   * number.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sig The sign of the unsigned {@code int} to subtract.
   * @param sub The amount to subtract (unsigned).
   * @return The result of the subtraction of the specified amount from the
   *         provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(final int[] val, final int sig, final int sub) {
    return sub == 0 ? val : sig < 0 ? add0(val, sub) : sub0(val, sub);
  }

  private static int[] sub0(int[] val, final int sub) {
    final int len = val[0];
    if (len == 0) {
      assign0(val.length >= 2 ? val : alloc(2), -1, sub);
    }
    else if (len < 0) {
      val = uaddVal(val, -len, false, sub);
    }
    else if (len == 1 && (val[1] & LONG_INT_MASK) < (sub & LONG_INT_MASK)) {
      val[0] = -len;
      val[1] = sub - val[1];
    }
    else {
      usubVal(val, len, true, sub);
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Adds the value of a {@code long} to the provided number.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param add The amount to add.
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(final int[] val, final long add) {
    return add > 0 ? add(val, 1, add) : add < 0 ? sub(val, 1, -add) : val;
  }

  /**
   * Subtracts the value of a {@code long} from the provided number.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sub The amount to subtract.
   * @return The result of the subtraction of the specified amount from the
   *         provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(final int[] val, final long sub) {
    return sub > 0 ? sub(val, 1, sub) : sub < 0 ? add(val, 1, -sub) : val;
  }

  /**
   * Adds the value of an <i>unsigned</i> {@code long} to the provided number.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sig The sign of the unsigned {@code long} to add.
   * @param add The amount to add (unsigned).
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(final int[] val, final int sig, final long add) {
    return add == 0 ? val : sig < 0 ? sub0(val, add) : add0(val, add);
  }

  private static int[] add0(final int[] val, final long add) {
    final long addh = add >>> 32;
    if (addh == 0)
      return add0(val, (int)add);

    int len = val[0];
    if (len == 0)
      return assign0(val.length >= 3 ? val : alloc(3), 1, add, (int)addh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSub(val, len, sig, add & LONG_INT_MASK, addh, true);
  }

  /**
   * Subtracts the value of an <i>unsigned</i> {@code long} from the provided
   * number.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sig The sign of the unsigned {@code long} to subtract.
   * @param sub The amount to subtract (unsigned).
   * @return The result of the subtraction of the specified amount from the
   *         provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(final int[] val, final int sig, final long sub) {
    return sub == 0 ? val : sig < 0 ? add0(val, sub) : sub0(val, sub);
  }

  private static int[] sub0(final int[] val, final long sub) {
    final long subh = sub >>> 32;
    if (subh == 0)
      return sub0(val, (int)sub);

    int len = val[0];
    if (len == 0)
      return assign0(val.length >= 3 ? val : alloc(3), -1, sub, (int)subh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSub(val, len, sig, sub & LONG_INT_MASK, subh, false);
  }

  /**
   * Adds (or subtracts) an unsigned {@code long} to (or from) the provided number.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param addl The lower limb of the amount to add (unsigned).
   * @param addh The higher limb of the amount to add (unsigned).
   * @param addOrSub {@code true} to add, or {@code false} to subtract.
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  private static int[] uaddSub(int[] val, int len, final boolean sig, final long addl, final long addh, final boolean addOrSub) {
    if (addOrSub == sig) {
      val = uaddVal(val, len, sig, addl, addh);
    }
    else {
      if (val.length <= 2)
        val = realloc(val, 3);

      final long val0 = val[1] & LONG_INT_MASK;
      final long val1 = val[2] & LONG_INT_MASK;
      if (len > 2 || len == 2 && (val1 > addh || val1 == addh && val0 >= addl) || addh == 0 && val0 >= addl) {
        usubVal(val, len, sig, val0, val1, addl, addh);
      }
      else {
        if (len == 1)
          val[++len] = 0;

        long dif = addl - val0;
        val[1] = (int)dif;
        dif >>= 32;
        dif = addh - val1 + dif;
        val[2] = (int)dif;
        // dif >> 32 != 0 should be impossible
        if (dif == 0)
          --len;

        val[0] = addOrSub ? len : -len;
      }
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Adds the value of a value-encoded amount to the provided number.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param add The value-encoded amount to add.
   * @return The result of the addition of the specified value-encoded amount to the
   *         provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] add(final int[] val, final int[] add) {
    return addSub(val, add, true);
  }

  /**
   * Subtracts the value of a value-encoded amount from the provided number.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param sub The value-encoded amount to subtract.
   * @return The result of the subtraction of the specified value-encoded amount
   *         from the provided number.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int[] sub(final int[] val, final int[] sub) {
    return addSub(val, sub, false);
  }

  /**
   * Adds (or subtracts) a value-encoded amount to (or from) the provided
   * number.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the decrease of the provided number by the specified amount requires a
   * larger {@code int[]}.</i>
   *
   * @param val The value-encoded number.
   * @param add The value-encoded amount to add.
   * @param addOrSub {@code true} to add, or {@code false} to subtract.
   * @return The result of the addition of the specified amount to the provided
   *         number.
   * @complexity O(n)
   * @amortized O(1)
   */
  private static int[] addSub(int[] val, final int[] add, final boolean addOrSub) {
    if (isZero(add))
      return val;

    if (isZero(val)) {
      final int len = Math.abs(add[0]) + 1;
      if (len > val.length)
        val = alloc(len);

      System.arraycopy(add, 0, val, 0, len);
      if (!addOrSub)
        val[0] = -val[0];

      return val;
    }

    boolean sig = true; int len = val[0]; if (len < 0) { len = -len; sig = false; }
    int len2 = add[0]; if (len2 < 0) { len2 = -len2; }
    if (addOrSub == (sig == add[0] >= 0))
      return addVal(val, len, sig, add, len2);

    if (compareToAbs(val, add) >= 0) {
      subVal(val, len, sig, add, len2);
    }
    else {
      if (len2 >= val.length)
        val = realloc(val, len2 + 2);

      sig = !sig;
      long dif = 0;
      int i = 1;
      for (; i <= len; ++i) {
        dif = (add[i] & LONG_INT_MASK) - (val[i] & LONG_INT_MASK) + dif;
        val[i] = (int)dif;
        dif >>= 32;
      }

      if (len2 > len) {
        System.arraycopy(add, len + 1, val, len + 1, len2 - len);
        len = len2;
      }

      if (dif != 0) {
        for (; i <= len2 && val[i] == 0; ++i)
          --val[i];

        if (--val[i] == 0 && i == len)
          while (val[--len] == 0);
      }
      else if (val[len] == 0) {
        --len;
      }

      val[0] = sig ? len : -len;
    }

    _debugLenSig(val);
    return val;
  }
}