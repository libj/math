/* Copyright (c) 2020 Seva Safris, LibJ
 * Copyright (c) 2015-2016 Simon Klein, Google Inc.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of the Huldra and the LibJ projects.
 */

package org.libj.math;

abstract class BigIntAddition extends BigIntPrimitive {
  private static final long serialVersionUID = 2873086066678372875L;

  /**
   * Adds an {@code int} to the provided {@linkplain BigInt#val() value-encoded
   * addend}.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified {@code int}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded addend}.
   * @param add The amount to add.
   * @return The result of the addition of the specified amount to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  // Has not amortized O(1) due to the risk of
  // alternating +1 -1 on continuous sequence of
  // 1-set bits.
  public static int[] add(final int[] val, final int add) {
    return add > 0 ? add0(val, add) : add < 0 ? sub0(val, -add) : val;
  }

  /**
   * Subtracts an {@code int} subtrahend from the provided
   * {@linkplain BigInt#val() value-encoded minuend}.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           decrease of the provided minuend by the specified {@code int}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded minuend}.
   * @param sub The subtrahend.
   * @return The result of the subtraction of the specified amount from the
   *         provided {@linkplain BigInt#val() value-encoded minuend}.
   * @complexity O(n)
   */
  public static int[] sub(final int[] val, final int sub) {
    return sub > 0 ? sub0(val, sub) : sub < 0 ? add0(val, -sub) : val;
  }

  /**
   * Adds an <i>unsigned</i> {@code int} to the provided
   * {@linkplain BigInt#val() value-encoded addend}.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified {@code int}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded addend}.
   * @param sig The sign of the unsigned {@code int} to add.
   * @param add The amount to add (unsigned).
   * @return The result of the addition of the specified amount to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  public static int[] add(final int[] val, final int sig, final int add) {
    return add == 0 ? val : sig < 0 ? sub0(val, add) : add0(val, add);
  }

  private static int[] add0(int[] val, final int add) {
    int len = val[0];
    if (len == 0) {
      val = assignInPlace(val.length >= 2 ? val : alloc(2), 1, add);
    }
    else if (len > 0) {
      val = uaddVal(val, len, true, add);
    }
    else if ((len = -len) > 1 || (val[1] & LONG_MASK) > (add & LONG_MASK)) {
      usubVal(val, len, false, add);
    }
    else {
      if ((val[1] = add - val[1]) == 0)
        --len;

      val[0] = len;
    }

    // _debugLenSig(val);
    return val;
  }

  private static int[] addInPlace(final int[] val, final int add) {
    int len = val[0];
    if (len == 0) {
      assignInPlace(val, 1, add);
    }
    else if (len > 0) {
      uaddValInPlace(val, len, true, add);
    }
    else if ((len = -len) > 1 || (val[1] & LONG_MASK) > (add & LONG_MASK)) {
      usubVal(val, len, false, add);
    }
    else {
      if ((val[1] = add - val[1]) == 0)
        --len;

      val[0] = len;
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Subtracts an <i>unsigned</i> {@code int} subtrahend from the provided
   * {@linkplain BigInt#val() value-encoded minuend}.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           decrease of the provided minuend by the specified {@code int}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded minuend}.
   * @param sig The sign of the unsigned {@code int} to subtract.
   * @param sub The subtrahend (unsigned).
   * @return The result of the subtraction of the specified amount from the
   *         provided {@linkplain BigInt#val() value-encoded minuend}.
   * @complexity O(n)
   */
  public static int[] sub(final int[] val, final int sig, final int sub) {
    return sub == 0 ? val : sig < 0 ? add0(val, sub) : sub0(val, sub);
  }

  private static int[] sub0(int[] val, final int sub) {
    final int len = val[0];
    if (len == 0) {
      val = assignInPlace(val.length >= 2 ? val : alloc(2), -1, sub);
    }
    else if (len < 0) {
      val = uaddVal(val, -len, false, sub);
    }
    else if (len == 1 && (val[1] & LONG_MASK) < (sub & LONG_MASK)) {
      val[0] = -len;
      val[1] = sub - val[1];
    }
    else {
      usubVal(val, len, true, sub);
    }

    // _debugLenSig(val);
    return val;
  }

  private static int[] subInPlace(final int[] val, final int sub) {
    final int len = val[0];
    if (len == 0) {
      assignInPlace(val, -1, sub);
    }
    else if (len < 0) {
      uaddValInPlace(val, -len, false, sub);
    }
    else if (len == 1 && (val[1] & LONG_MASK) < (sub & LONG_MASK)) {
      val[0] = -len;
      val[1] = sub - val[1];
    }
    else {
      usubVal(val, len, true, sub);
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Adds a {@code long} to the provided {@linkplain BigInt#val() value-encoded
   * addend}.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified {@code long}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded addend}.
   * @param add The amount to add.
   * @return The result of the addition of the specified amount to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  public static int[] add(final int[] val, final long add) {
    return add > 0 ? add0(val, add) : add < 0 ? sub0(val, -add) : val;
  }

  protected static int[] addInPlace(final int[] val, final long add) {
    return add > 0 ? addUnsafe0(val, add) : add < 0 ? subUnsafe0(val, -add) : val;
  }

  /**
   * Subtracts a {@code long} subtrahend from the provided value-encoded
   * minuend.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           decrease of the provided minuend by the specified {@code long}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded minuend}.
   * @param sub The subtrahend.
   * @return The result of the subtraction of the specified amount from the
   *         provided {@linkplain BigInt#val() value-encoded minuend}.
   * @complexity O(n)
   */
  public static int[] sub(final int[] val, final long sub) {
    return sub > 0 ? sub0(val, sub) : sub < 0 ? add0(val, -sub) : val;
  }

  /**
   * Adds an <i>unsigned</i> {@code long} to the provided
   * {@linkplain BigInt#val() value-encoded addend}.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified {@code long}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded addend}.
   * @param sig The sign of the unsigned {@code long} to add.
   * @param add The amount to add (unsigned).
   * @return The result of the addition of the specified amount to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  public static int[] add(final int[] val, final int sig, final long add) {
    return add == 0 ? val : sig < 0 ? sub0(val, add) : add0(val, add);
  }

  protected static int[] addInPlace(final int[] val, final int sig, final long add) {
    return add == 0 ? val : sig < 0 ? subUnsafe0(val, add) : addUnsafe0(val, add);
  }

  private static int[] add0(final int[] val, final long add) {
    final long addh = add >>> 32;
    if (addh == 0)
      return add0(val, (int)add);

    int len = val[0];
    if (len == 0)
      return assignInPlace(val.length >= 3 ? val : alloc(3), 1, (int)add, (int)addh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSub(val, len, sig, add & LONG_MASK, addh, true);
  }

  private static int[] addUnsafe0(final int[] val, final long add) {
    final long addh = add >>> 32;
    if (addh == 0)
      return addInPlace(val, (int)add);

    int len = val[0];
    if (len == 0)
      return assignInPlace(val, 1, (int)add, (int)addh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSubInPlace(val, len, sig, add & LONG_MASK, addh, true);
  }

  /**
   * Subtracts an <i>unsigned</i> {@code long} subtrahend from the provided
   * {@linkplain BigInt#val() value-encoded minuend}.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           decrease of the provided minuend by the specified {@code long}
   *           requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded minuend}.
   * @param sig The sign of the unsigned {@code long} to subtract.
   * @param sub The subtrahend (unsigned).
   * @return The result of the subtraction of the specified amount from the
   *         provided {@linkplain BigInt#val() value-encoded minuend}.
   * @complexity O(n)
   */
  public static int[] sub(final int[] val, final int sig, final long sub) {
    return sub == 0 ? val : sig < 0 ? add0(val, sub) : sub0(val, sub);
  }

  protected static int[] subInPlace(final int[] val, final int sig, final long sub) {
    return sub == 0 ? val : sig < 0 ? addUnsafe0(val, sub) : subUnsafe0(val, sub);
  }

  private static int[] sub0(final int[] val, final long sub) {
    final long subh = sub >>> 32;
    if (subh == 0)
      return sub0(val, (int)sub);

    int len = val[0];
    if (len == 0)
      return assignInPlace(val.length >= 3 ? val : alloc(3), -1, (int)sub, (int)subh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSub(val, len, sig, sub & LONG_MASK, subh, false);
  }

  private static int[] subUnsafe0(final int[] val, final long sub) {
    final long subh = sub >>> 32;
    if (subh == 0)
      return subInPlace(val, (int)sub);

    int len = val[0];
    if (len == 0)
      return assignInPlace(val, -1, (int)sub, (int)subh);

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return uaddSubInPlace(val, len, sig, sub & LONG_MASK, subh, false);
  }

  /**
   * Adds (or subtracts) an unsigned {@code long} to (or from) the provided
   * {@linkplain BigInt#val() value-encoded addend}.
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase (or decrease) of the provided addend (or minuend) by the
   *           specified {@code long} requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded addend} (or minuend).
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param addl The lower limb of the amount to add (unsigned).
   * @param addh The higher limb of the amount to add (unsigned).
   * @param addOrSub {@code true} to add, or {@code false} to subtract.
   * @return The result of the addition of the specified amount to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  private static int[] uaddSub(int[] val, int len, final boolean sig, final long addl, final long addh, final boolean addOrSub) {
    if (addOrSub == sig) {
      if (val.length <= 3)
        val = realloc(val, len + OFF, 4);

      val = uaddVal(val, len, sig, addl, addh);
    }
    else {
      if (val.length <= 2)
        val = realloc(val, len + 1, 3);

      final long val0 = val[1] & LONG_MASK;
      final long val1 = val[2] & LONG_MASK;
      if (len > 2 || len == 2 && (val1 > addh || val1 == addh && val0 >= addl) || addh == 0 && val0 >= addl) {
        usubVal(val, len, sig, val0, val1, addl, addh);
      }
      else {
        if (len == 1)
          val[++len] = 0;

        long dif = addl - val0;
        val[1] = (int)dif;
        dif >>= 32;
        dif += addh - val1;
        val[2] = (int)dif;
        // dif >> 32 != 0 should be impossible
        if (dif == 0)
          --len;

        val[0] = addOrSub ? len : -len;
      }
    }

    // _debugLenSig(val);
    return val;
  }

  private static int[] uaddSubInPlace(final int[] val, int len, final boolean sig, final long addl, final long addh, final boolean addOrSub) {
    if (addOrSub == sig) {
      uaddVal(val, len, sig, addl, addh);
    }
    else {
      final long val0 = val[1] & LONG_MASK;
      final long val1 = val[2] & LONG_MASK;
      if (len > 2 || len == 2 && (val1 > addh || val1 == addh && val0 >= addl) || addh == 0 && val0 >= addl) {
        usubVal(val, len, sig, val0, val1, addl, addh);
      }
      else {
        if (len == 1)
          val[++len] = 0;

        long dif = addl - val0;
        val[1] = (int)dif;
        dif >>= 32;
        dif += addh - val1;
        val[2] = (int)dif;
        // dif >> 32 != 0 should be impossible
        if (dif == 0)
          --len;

        val[0] = addOrSub ? len : -len;
      }
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Adds a {@linkplain BigInt#val() value-encoded number} to the provided
   * {@linkplain BigInt#val() value-encoded addend}.
   *
   * <pre>
   * val = val + add
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified
   *           {@linkplain BigInt#val() value-encoded number} requires a larger
   *           array.
   * @param val The {@linkplain BigInt#val() value-encoded addend}.
   * @param add The {@linkplain BigInt#val() value-encoded amount} to add.
   * @return The result of the addition of the specified
   *         {@linkplain BigInt#val() value-encoded number} to the provided
   *         {@linkplain BigInt#val() value-encoded addend}.
   * @complexity O(n)
   */
  public static int[] add(final int[] val, final int[] add) {
    return addSub(val, add, true, false);
  }

  protected static int[] addInPlace(final int[] val, final int[] add) {
    return addSub(val, add, true, true);
  }

  /**
   * Subtracts a {@linkplain BigInt#val() value-encoded subtrahend} from the
   * provided {@linkplain BigInt#val() value-encoded minuend}.
   *
   * <pre>
   * val = val - sub
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           decrease of the provided minuend by the specified
   *           {@linkplain BigInt#val() value-encoded subtrahend} requires a
   *           larger array.
   * @param val The {@linkplain BigInt#val() value-encoded minuend}.
   * @param sub The {@linkplain BigInt#val() value-encoded subtrahend}.
   * @return The result of the subtraction of the specified
   *         {@linkplain BigInt#val() value-encoded subtrahend} from the
   *         provided {@linkplain BigInt#val() value-encoded minuend}.
   * @complexity O(n)
   */
  public static int[] sub(final int[] val, final int[] sub) {
    return addSub(val, sub, false, false);
  }

  protected static int[] subInPlace(final int[] val, final int[] sub) {
    return addSub(val, sub, false, true);
  }

  /**
   * Adds (or subtracts) a {@linkplain BigInt#val() value-encoded number} to (or
   * from) the provided {@linkplain BigInt#val() value-encoded addend} (or
   * minuend).
   * <p>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           increase of the provided addend by the specified
   *           {@linkplain BigInt#val() value-encoded number} requires a larger
   *           array.
   * @param val The {@linkplain BigInt#val() value-encoded addend} (or minuend).
   * @param add The {@linkplain BigInt#val() value-encoded amount} to add.
   * @param addOrSub {@code true} to add, or {@code false} to subtract.
   * @param inPlace Whether the operation should be performed in-place.
   * @return The result of the addition (or subtraction) of the specified amount
   *         to (or from) the provided {@linkplain BigInt#val() value-encoded
   *         addend} (or minuend).
   * @complexity O(n)
   */
  private static int[] addSub(int[] val, final int[] add, final boolean addOrSub, final boolean inPlace) {
    int len = val[0];
    if (len == 0) {
      len = Math.abs(add[0]);
      if (!inPlace && len >= val.length)
        val = alloc(len + 1);

      System.arraycopy(add, 0, val, 0, len + 1);
      if (!addOrSub)
        val[0] = -val[0];

      return val;
    }

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return addSub0(val, len, sig, add, addOrSub, inPlace);
  }

  static int[] addSub0(int[] val, int len, boolean sig, final int[] add, final boolean addOrSub, final boolean inPlace) {
    int len2 = add[0]; if (len2 < 0) { len2 = -len2; }
    if (addOrSub == (sig == add[0] >= 0))
      return addVal(val, len, sig, add, len2);

    if (compareToAbs(val, add) >= 0) {
      subVal(val, len, sig, add, len2);
    }
    else {
      if (!inPlace && len2 >= val.length)
        val = realloc(val, len + 1, len2 + 2);

      sig = !sig;
      long dif = 0;
      int i = 1;
      for (; i <= len; ++i) {
        val[i] = (int)(dif += (add[i] & LONG_MASK) - (val[i] & LONG_MASK));
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
      else {
        while (val[len] == 0)
          --len;
      }

      val[0] = sig ? len : -len;
    }

    // _debugLenSig(val);
    return val;
  }
}