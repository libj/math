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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND ANY
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

abstract class BigIntMagnitude extends BigIntValue {
  private static final long serialVersionUID = 734086338662551150L;

  /**
   * Increases the magnitude of the provided number by the specified amount.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger array.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param add The amount by which to increase (unsigned).
   * @return The provided number increased by the specified amount.
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddVal(int[] val, int len, final boolean sig, final int add) {
    final long sum = (val[1] & LONG_INT_MASK) + (add & LONG_INT_MASK);
    val[1] = (int)sum;
    if ((sum >>> 32) != 0) {
      int i = 2;
      for (; i <= len && ++val[i] == 0; ++i);
      if (i > len) {
        if (++len == val.length)
          val = realloc(val, len - 1, len + 1);

        val[len] = 1;
        val[0] = sig ? len : -len;
      }
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Decreases the magnitude of the provided number by the specified amount.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param sub The amount by which to decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static void usubVal(final int[] val, int len, final boolean sig, final int sub) {
    final long dif = (val[1] & LONG_INT_MASK) - (sub & LONG_INT_MASK);
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
    val[0] = sig ? len : -len;
    _debugLenSig(val);
  }

  /**
   * Increases the magnitude of the provided number by the specified amount.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase of the provided number by the specified amount requires a
   * larger array.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param addl The lower limb of the amount by which to increase (unsigned).
   * @param addh The higher limb of the amount by which to increase (unsigned).
   * @return The provided number increased by the specified amount.
   * @complexity O(n)
   * @amortized O(1)
   */
  static int[] uaddVal(int[] val, int len, final boolean sig, final long addl, final long addh) {
    if (val.length <= 3)
      val = realloc(val, len, 4);

    final long val0 = val[1] & LONG_INT_MASK;
    final long val1 = val[2] & LONG_INT_MASK;
    long carry = val0 + addl;
    val[1] = (int)carry;
    carry >>>= 32;
    carry = val1 + addh + carry;
    val[2] = (int)carry;
    if (carry != 0 && len < 2)
      ++len;

    if ((carry >> 32) != 0) {
      int i = 3;
      for (; i <= len && ++val[i] == 0; ++i);
      if (i > len) {
        len = i;
        if (len == val.length)
          val = realloc(val, len, len);

        val[len] = 1;
      }
    }
    else if (val[len] == 0) {
      --len;
    }

    val[0] = sig ? len : -len;
    _debugLenSig(val);
    return val;
  }

  /**
   * Decreases the magnitude of the provided number by the specified amount.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param val0 The value of {@code val[1]}.
   * @param val1 The value of {@code val[2]}.
   * @param subl The lower limb of the amount by which to decrease (unsigned).
   * @param subh The higher limb of the amount by which to decrease (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  static void usubVal(final int[] val, int len, final boolean sig, final long val0, final long val1, final long subl, final long subh) {
    long dif = val0 - subl;
    val[1] = (int)dif;
    dif >>= 32;
    dif = val1 - subh + dif;
    val[2] = (int)dif;

    // Subtract remainder of longer number while borrow propagates
    boolean borrow = dif >> 32 != 0;
    for (int i = 2; i <= len && borrow;)
      borrow = --val[++i] == -1;

    while (val[len] == 0)
      --len;

    val[0] = sig ? len : -len;
    _debugLenSig(val);
  }

  /**
   * Increases (or decreased) the magnitude of the provided number by the
   * specified amount.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the increase (or decrease) of the provided number by the specified amount
   * requires a larger array.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param add The {@linkplain BigInt#val() value-encoded amount} by which to
   *          increase (or decrease).
   * @param alen The count of limbs in the number by which to increase.
   * @return The provided number increased (or decreased) by the specified
   *         amount.
   * @complexity O(n)
   */
  static int[] addVal(int[] val, int len, final boolean sig, int[] add, int alen) {
    int[] val1 = val; // len <= len2
    int len1 = len;
    if (alen < len) {
      val1 = add;
      add = val;
      len1 = alen;
      alen = len;
    }

    if (alen >= val.length)
      val = realloc(val, len, alen + 2);

    long carry = 0;
    int i = 1;
    for (; i <= len1; ++i) {
      carry = (val1[i] & LONG_INT_MASK) + (add[i] & LONG_INT_MASK) + carry;
      val[i] = (int)carry;
      carry >>>= 32;
    }

    if (alen > len) {
      System.arraycopy(add, len + 1, val, len + 1, alen - len);
      len = alen;
      val[0] = sig ? len : -len;
    }

    if (carry != 0) { // carry == 1
      for (; i <= len && ++val[i] == 0; ++i);
      if (i > len) { // len == len2
        if (i == val.length)
          val = realloc(val, len, i + 1);

        val[++len] = 1;
        val[0] = sig ? len : -len;
      }
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Decreased the magnitude of the provided number by the specified amount.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs in the number.
   * @param sig The sign of the number.
   * @param sub The {@linkplain BigInt#val() value-encoded amount} by which to
   *          decrease.
   * @param slen The count of limbs in the number by which to decrease.
   * @complexity O(n)
   */
  static void subVal(final int[] val, int len, final boolean sig, final int[] sub, final int slen) {
    // Assumes len == len2 and v == dig
    long dif = 0;
    int i = 1;
    for (; i <= slen; ++i) {
      dif += (val[i] & LONG_INT_MASK) - (sub[i] & LONG_INT_MASK);
      val[i] = (int)dif;
      dif >>= 32;
    }

    if (dif != 0) {
      for (; val[i] == 0; ++i)
        --val[i];

      if (--val[i] == 0 && i + 1 == len)
        len = slen;
    }

    while (val[len] == 0)
      if (--len == 0)
        break;

    val[0] = sig ? len : -len;
    _debugLenSig(val);
  }
}