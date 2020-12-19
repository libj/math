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

import java.math.BigDecimal;
import java.math.BigInteger;

abstract class BigIntBinary extends BigIntMagnitude {
  private static final long serialVersionUID = 6584645376198040730L;

  /**
   * Returns the number of bits in the two's complement representation of the
   * provided {@linkplain BigInt#val() value-encoded number} that differ from
   * its sign bit.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return Number of bits in the two's complement representation of the
   *         provided {@linkplain BigInt#val() value-encoded number} that differ
   *         from its sign bit.
   * @complexity O(n)
   */
  public static int bitCount(final int[] val) {
    int i, bits = 0;
    boolean sig = true; int len = val[0]; if (len < 0) { len = -len; sig = false; }
    // Count the bits in the magnitude
    for (i = 1; i <= len; ++i)
      bits += Integer.bitCount(val[i]);

    if (!sig) {
      // Count the trailing zeros in the magnitude
      for (i = 1; i <= len && val[i] == 0; ++i)
        bits += 32;

      bits += Integer.numberOfTrailingZeros(val[i]) - 1;
    }

    return bits;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} right by
   * the specified number of bits. The shift distance, {@code num}, may be
   * negative, in which case this method performs a left shift.
   *
   * <pre>
   * val = val &gt;&gt; num
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the shift requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param num The amount by which to shift.
   * @return The result of shifting the provided {@linkplain BigInt#val()
   *         value-encoded number}} right by the specified number of bits.
   * @complexity O(n)
   */
  public static int[] shiftRight(final int[] val, final int num) {
    return shiftRight(val, num, false);
  }

  static int[] shiftRight(final int[] val, final int num, final boolean inPlace) {
    if (num == 0)
      return val;

    int len = val[0];
    if (len == 0)
      return val;

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return num < 0 ? shiftLeft0(val, len, sig, -num, inPlace) : shiftRight0(val, len, sig, num);
  }

  private static int[] shiftRight0(final int[] val, int len, final boolean sig, final int num) {
    final int shiftBig = num >>> 5;
    // Special case: entire contents shifted off the end
    if (shiftBig >= len)
      return sig ? setToZeroInPlace(val) : assignInPlace(val, sig, 1);

    final int shiftSmall = num & 31;
    boolean oneLost = false;
    if (!sig) {
      // Find out whether any one-bits will be shifted off the end
      final int j = shiftBig + 1;
      for (int i = 1; i < j && !(oneLost = val[i] != 0); ++i);
      if (!oneLost && shiftSmall != 0)
        oneLost = val[j] << (32 - shiftSmall) != 0;
    }

    if (shiftBig > 0) {
      len = bigShiftRight(val, len, shiftBig);
      val[0] = val[0] < 0 ? -len : len;
    }

    if (shiftSmall > 0) {
      len = smallShiftRight(val, len, shiftSmall);
      val[0] = val[0] < 0 ? -len : len;
    }

    if (oneLost)
      uaddVal(val, len, sig, 1);

    // _debugLenSig(val);
    return val;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} by
   * {@code 32 * num} bits to the right.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The number of limbs of the number to shift.
   * @param num The number of {@code bits / 32} by which to shift.
   * @return The length of the number, which may have changed due to the shift.
   * @complexity O(n)
   */
  private static int bigShiftRight(final int[] val, int len, final int num) {
    System.arraycopy(val, num + 1, val, 1, len -= num);
    return len;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} right by
   * the specified number of bits (less than 32).
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The number of limbs of the number to shift.
   * @param num The number of bits by which to shift.
   * @return The length of the number, which may have changed due to the shift.
   * @complexity O(n)
   */
  private static int smallShiftRight(final int[] val, int len, final int num) {
    for (int next = val[1], i = 1; i < len; ++i)
      val[i] = next >>> num | (next = val[i + 1]) << 32 - num;

    if ((val[len] >>>= num) == 0 && len >= 1)
      --len;

    return len;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} left by
   * the specified number of bits. The shift distance, {@code num}, may be
   * negative, in which case this method performs a right shift.
   *
   * <pre>
   * val = val &lt;&lt; num
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the shift requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param num The amount by which to shift.
   * @return The result of shifting the provided {@linkplain BigInt#val()
   *         value-encoded number} left by the specified number of bits.
   * @complexity O(n)
   */
  public static int[] shiftLeft(final int[] val, final int num) {
    return shiftLeft(val, num, false);
  }

  static int[] shiftLeft(final int[] val, final int num, final boolean inPlace) {
    if (num == 0)
      return val;

    int len = val[0];
    if (len == 0)
      return val;

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    return num < 0 ? shiftRight0(val, len, sig, -num) : shiftLeft0(val, len, sig, num, inPlace);
  }

  private static int[] shiftLeft0(int[] val, int len, boolean sig, final int num, final boolean inPlace) {
    final int shiftBig = (num >>> 5) + 1;
    if (shiftBig > 1) {
      val = bigShiftLeft(val, len, sig, shiftBig, inPlace);
      sig = true; len = val[0]; if (len < 0) { len = -len; sig = false; }
    }

    final int shiftSmall = num & 31;
    if (shiftSmall > 0) {
      val = smallShiftLeft(val, shiftBig, len, sig, shiftSmall);
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} by
   * {@code 32 * num} bits to the right.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to shift.
   * @param len The number of limbs of the number to shift.
   * @param sig The sign of the number to shift.
   * @param num The number of {@code bits / 32} by which to shift.
   * @param inPlace Whether the operation should be performed in-place.
   * @return The length of the number, which may have changed due to the shift.
   * @complexity O(n)
   */
  private static int[] bigShiftLeft(int[] val, final int len, final boolean sig, final int num, final boolean inPlace) {
    int newLen = len + num;
    if (!inPlace && newLen > val.length) {
      final int[] tmp = alloc(newLen);
      System.arraycopy(val, 1, tmp, num, len);
      val = tmp;
    }
    else {
      System.arraycopy(val, 1, val, num, len);
      for (int i = 1; i < num; ++i)
        val[i] = 0;
    }

    newLen -= OFF;
    val[0] = sig ? newLen : -newLen;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Shifts the provided {@linkplain BigInt#val() value-encoded number} left by
   * the specified number of bits (less than 32) starting at the given offset
   * ({@code off}).
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the shift requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to shift.
   * @param off The limb at which to start shifting.
   * @param len The number of limbs of the number to shift.
   * @param sig The sign of the number to shift.
   * @param num The number of bits by which to shift.
   * @return The result of shifting the provided {@linkplain BigInt#val()
   *         value-encoded number} left by the specified number of bits.
   * @complexity O(n)
   */
  private static int[] smallShiftLeft(int[] val, final int off, int len, final boolean sig, final int num) {
    int[] res = val;
    int next = 0;
    if ((val[len] << num >>> num) != val[len]) { // Overflow?
      if (++len >= val.length) {
        res = realloc(val, len, len + 1);
      }
      else {
        val[len] = 0;
      }

      res[0] = sig ? len : -len;
    }
    else {
      next = len >= val.length ? 0 : val[len];
    }

    for (; len > off; --len)
      res[len] = next << num | (next = val[len - 1]) >>> 32 - num;

    res[off] = next << num;
    // _debugLenSig(res);
    return res;
  }

  /**
   * Tests if the specified bit is set in the provided {@linkplain BigInt#val()
   * value-encoded number}.
   * <p>
   * Computes:
   *
   * <pre>
   * val | (1 &lt;&lt; n)
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param bit The index of the bit to test.
   * @return {@code true} if the given bit is set in the provided
   *         {@linkplain BigInt#val() value-encoded number,} otherwise
   *         {@code false}.
   * @complexity O(n)
   */
  public static boolean testBit(final int[] val, final int bit) {
    boolean sig = true; int len = val[0]; if (len < 0) { len = -len; sig = false; }

    final int bigBit = (bit >>> 5) + 1;
    if (bigBit > len)
      return !sig;

    if (sig) {
      final int smallBit = bit & 31;
      return (val[bigBit] & 1 << smallBit) != 0;
    }

    int j = 1;
    for (; j <= bigBit && val[j] == 0; ++j);
    if (j > bigBit)
      return false;

    final int smallBit = bit & 31;
    if (j < bigBit)
      return (val[bigBit] & 1 << smallBit) == 0;

    j = -val[bigBit];
    return (j & 1 << smallBit) != 0;
  }

  /**
   * Sets the specified bit in the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val | (1 &lt;&lt; n)
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param bit The bit to set.
   * @return The result of setting the specified bit in the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] setBit(int[] val, final int bit) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    if (sig >= 0) {
      if (bigBit >= val.length) {
        val = realloc(val, len + 1, bigBit + 1);
        len = bigBit;
      }
      else if (bigBit > len) {
        while (len < bigBit)
          val[++len] = 0;

        len = bigBit;
      }

      val[bigBit] |= 1 << smallBit;
    }
    else {
      if (bigBit > len)
        return val;

      int j = 1;
      while (j <= bigBit && val[j] == 0)
        ++j;

      if (j > bigBit) {
        val[bigBit] = -1 << smallBit;
        for (; val[j] == 0; ++j)
          val[j] = -1;

        val[j] = ~-val[j];
        if (j == len && val[len] == 0)
          --len;
      }
      else if (j < bigBit) {
        val[bigBit] &= ~(1 << smallBit);
      }
      else {
        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (k - j > 0) {
          val[bigBit] &= ~k; // Unsigned compare.
        }
        else {
          val[bigBit] ^= ((j * 2) - 1) ^ (k - 1);
          val[bigBit] |= k;
        }
      }
    }

    for (; val[len] == 0; --len);
    val[0] = sig < 0 ? -len : len;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Clears the specified bit in the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val &amp; ~(1 &lt;&lt; n)
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param bit The bit to clear.
   * @return The result of clearing the specified bit in the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] clearBit(int[] val, final int bit) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    if (sig >= 0) {
      if (bigBit <= len) {
        val[bigBit] &= ~(1 << smallBit);
      }
    }
    else {
      if (bigBit >= val.length) {
        val = realloc(val, len + 1, bigBit + 1);
        len = bigBit;
        val[bigBit] |= 1 << smallBit;
      }
      else if (bigBit > len) {
        while (len < bigBit)
          val[++len] = 0;

        val[bigBit] |= 1 << smallBit;
        len = bigBit;
      }
      else {
        int j = 1;
        while (j <= bigBit && val[j] == 0)
          ++j;

        if (j > bigBit)
          return val;

        if (j < bigBit) {
          val[bigBit] |= 1 << smallBit;
          return val;
        }

        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0)
          return val; // Unsigned compare

        if (j - k < 0) {
          val[bigBit] |= k;
          return val;
        }

        j = val[bigBit];
        if (j == (-1 ^ k - 1)) {
          val[bigBit] = 0;
          for (j = bigBit + 1; j <= len && val[j] == -1; ++j)
            val[j] = 0;

          if (j == val.length)
            val = realloc(val, len + 1, j + 2);

          if (j <= len) {
            val[j] = -~val[j];
          }
          else {
            val[++len] = 1;
          }
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          val[bigBit] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    if (len > 0)
      for (; val[len] == 0; --len);

    val[0] = sig < 0 ? -len : len;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Flips the specified bit in the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val ^ (1 &lt;&lt; n)
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param bit The bit to flip.
   * @return The result of flipping the specified bit in the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] flipBit(int[] val, final int bit) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    block:
    if (bigBit >= val.length) {
      val = realloc(val, len + 1, bigBit + 1);
      val[bigBit] ^= 1 << smallBit;
      len = bigBit;
    }
    else if (bigBit > len) {
      while (len < bigBit)
        val[++len] = 0;

      val[bigBit] ^= 1 << smallBit;
      len = bigBit;
    }
    else if (sig >= 0) {
      val[bigBit] ^= 1 << smallBit;
    }
    else {
      int j = 1;
      while (j <= bigBit && val[j] == 0)
        ++j;

      if (j < bigBit) {
        val[bigBit] ^= 1 << smallBit;
        break block;
      }

      if (j > bigBit) { // TODO: Refactor with setBit?
        val[bigBit] = -1 << smallBit;
        for (; val[j] == 0; ++j)
          val[j] = -1;

        val[j] = ~-val[j];
      }
      else {
        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0) {
          val[bigBit] ^= ((j * 2) - 1) ^ (k - 1);
          // _debugLenSig(val);
          return val;
        }

        if (j - k < 0) {
          val[bigBit] ^= k;
          // _debugLenSig(val);
          return val;
        }

        j = val[bigBit];
        if (j == (-1 ^ k - 1)) { // TODO: Refactor with clearBit?
          val[bigBit] = 0;
          for (j = bigBit + 1; j <= len && val[j] == -1; ++j)
            val[j] = 0;

          if (j == val.length)
            val = realloc(val, len + 1, j + 2);

          if (j <= len)
            val[j] = -~val[j];
          else
            val[++len] = 1;
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          val[bigBit] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    for (; val[len] == 0; --len);
    val[0] = sig < 0 ? -len : len;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Performs a bitwise "and" of the specified {@linkplain BigInt#val()
   * value-encoded mask} onto the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val &amp; mask
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mask The number with which to perform the bitwise "and".
   * @return The result of the bitwise "and" of the specified
   *         {@linkplain BigInt#val() value-encoded mask} onto the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] and(int[] val, final int[] mask) {
    int len2 = mask[0];
    if (len2 == 0)
      return setToZeroInPlace(val);

    int sig1 = 1, len1 = val[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    if (sig1 >= 0) {
      if (sig2 > 0) {
        if (len2 < len1)
          len1 = len2;

        boolean zeroes = true;
        for (int i = len1; i >= 1; --i) {
          val[i] &= mask[i];
          if (zeroes && (zeroes = val[i] == 0))
            --len1;
        }
      }
      else {
        final int mlen = Math.min(len1, len2);
        int a = val[1], b = mask[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          for (val[j - 1] = 0; j <= mlen && mask[j] == 0; ++j)
            val[j] = 0;

          if (j <= mlen)
            val[j] &= -mask[j];
          else
            len1 = 1;

          // FIXME: I think we can remove this
          // FIXME: This was hacked together...
          if (len1 > 1 && val[len1] == 0)
            --len1;

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val[j] == 0)
            ++j;
        }
        else {
          val[j - 1] &= -b;
        }

        for (; j <= mlen; ++j)
          val[j] &= ~mask[j];
      }
    }
    else {
      int mlen = Math.min(len1, len2); // FIXME: Make this final
      if (sig2 > 0) {
        int a = val[1], b = mask[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val[j], b = mask[j], ++j);

        if (a != 0 && b == 0) {
          for (val[j - 1] = 0; j <= mlen && mask[j] == 0; ++j)
            val[j] = 0;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val[j] == 0)
            ++j;

          if (j <= mlen)
            val[j] = -val[j] & mask[j];

          ++j;
        }
        else {
          val[j - 1] = -a & b;
        }

        for (; j <= mlen; ++j)
          val[j] = ~val[j] & mask[j];

        if (len2 > len1) {
          if (len2 >= val.length)
            val = realloc(val, len1 + 1, len2 + 3);

          System.arraycopy(mask, len1 + 1, val, len1 + 1, len2 - len1);
        }

        // FIXME: This was hacked together...
        if (len2 > 1 && val[len2] == 0)
          --len2;

        len1 = len2;
        sig1 = 1;
      }
      else {
        if (len2 > len1) {
          if (len2 >= val.length)
            val = realloc(val, len1 + 1, len2 + 3);

          System.arraycopy(mask, len1 + 1, val, len1 + 1, len2 - len1);
        }

        int a = val[1], b = mask[1], j = 2;
        for (; (a | b) == 0; a = val[j], b = mask[j], ++j);

        if (a != 0 && b == 0) {
          for (val[j - 1] = 0; j <= mlen && mask[j] == 0; ++j)
            val[j] = 0;

          if (j <= mlen)
            val[j] = -(~val[j] & -mask[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val[j] == 0)
            ++j;

          if (j <= mlen)
            val[j] = -(-val[j] & ~mask[j]);

          ++j;
        }
        else {
          val[j - 1] = -(-a & -b);
        }

        ++mlen;
        if (j <= mlen && val[j - 1] == 0) {
          if (j < mlen)
            for (val[j] = -~(val[j] | mask[j]); ++j < mlen && val[j - 1] == 0;)
              val[j] = -~(val[j] | mask[j]); // -(~dig[j]&~mask.dig[j])

          if (j == mlen && val[j - 1] == 0) {
            int blen = Math.max(len1, len2);
            while (j <= blen && val[j] == -1)
              val[j++] = 0; // mask.dig[j]==dig[j]

            if (j <= blen) {
              val[j] = -~val[j];
            }
            else {
              ++blen;
              if (blen >= val.length)
                val = realloc(val, len1 + 1, blen + 2);

              val[blen] = 1;
              val[0] = sig1 < 0 ? -blen : blen;
              // _debugLenSig(val);
              return val;
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          val[j] |= mask[j]; // ~(~dig[j]&~mask.dig[j]);

        if (len2 > len1)
          len1 = len2;
      }
    }

    if (len1 > 0)
      for (; val[len1] == 0; --len1);

    val[0] = sig1 < 0 ? -len1 : len1;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Performs a bitwise "or" of the specified {@linkplain BigInt#val()
   * value-encoded mask} onto the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val | mask
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mask The number with which to perform the bitwise "or".
   * @return The result of the bitwise "or" of the specified
   *         {@linkplain BigInt#val() value-encoded mask} onto the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] or(int[] val, final int[] mask) {
    int len2 = mask[0];
    if (len2 == 0)
      return val;

    int len1 = val[0];
    if (len1 == 0)
      return mask;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    if (sig1 >= 0) {
      if (sig2 >= 0) {
        if (len2 > len1) {
          if (len2 >= val.length)
            val = realloc(val, len1 + 1, len2 + 2);

          System.arraycopy(mask, len1 + 1, val, len1 + 1, len2 - len1);
          for (int i = 1; i <= len1; ++i)
            val[i] |= mask[i];

          len1 = len2;
        }
        else {
          for (int i = 1; i <= len2; ++i)
            val[i] |= mask[i];
        }
      }
      else {
        if (len2 >= val.length)
          val = realloc(val, len1 + 1, len2 + 2);

        if (len2 > len1)
          System.arraycopy(mask, len1 + 1, val, len1 + 1, len2 - len1);

        final int mlen = Math.min(len2, len1);
        int a = val[1], b = mask[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          val[j - 1] = -a;
          for (; mask[j] == 0; ++j)
            val[j] ^= -1;

          if (j <= mlen)
            val[j] = ~(val[j] | -mask[j]);
          else // mask.dig[j] == dig[j]
            val[j] = ~-val[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val[j - 1] = b; j <= mlen && val[j] == 0; ++j)
            val[j] = mask[j];
        }
        else { // a!=0 && b!=0
          val[j - 1] = -(a | -b);
        }

        for (; j <= mlen; ++j)
          val[j] = ~val[j] & mask[j]; // ~(dig[j]|~mask.dig[j])

        len1 = len2;
        sig1 = -1;
      }
    }
    else {
      final int mlen = Math.min(len2, len1);

      int a = val[1], b = mask[1], j = 2;
      for (; (a | b) == 0 && j <= mlen; a = val[j], b = mask[j], ++j);

      if (sig2 > 0) {
        if (a != 0 && b == 0) {
          for (; j <= mlen && mask[j] == 0; ++j);
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          val[j - 1] = -b;
          for (; j <= mlen && val[j] == 0; ++j)
            val[j] = ~mask[j];

          if (j <= mlen) {
            val[j] = ~(-val[j] | mask[j]);
          }
          else {
            for (; val[j] == 0; ++j)
              val[j] = -1;

            val[j] = ~-val[j];
          }

          ++j;
        }
        else { // a!=0 && b!=0
          val[j - 1] = -(-a | b);
        }

        for (; j <= mlen; ++j)
          val[j] &= ~mask[j]; // ~(~dig[j]|mask.dig[j])
      }
      else {
        if (a != 0 && b == 0) {
          for (; j <= mlen && mask[j] == 0; ++j);
          if (j <= mlen)
            val[j] = ~(~val[j] | -mask[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val[j - 1] = b; j <= mlen && val[j] == 0; ++j)
            val[j] = mask[j];

          if (j <= mlen)
            val[j] = ~(-val[j] | ~mask[j]);

          ++j;
        }
        else { // a!=0 && b!=0
          val[j - 1] = -(-a | -b);
        }

        for (; j <= mlen; ++j)
          val[j] &= mask[j]; // ~(~dig[j]|~mask.dig[j])

        len1 = mlen;
      }
    }

    if (len1 > 0)
      for (; val[len1] == 0; --len1);

    val[0] = sig1 < 0 ? -len1 : len1;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Performs a bitwise "xor" of the specified {@linkplain BigInt#val()
   * value-encoded mask} onto the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val ^ mask
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mask The number with which to perform the bitwise "xor".
   * @return The result of the bitwise "xor" of the specified
   *         {@linkplain BigInt#val() value-encoded mask} onto the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] xor(int[] val, int[] mask) {
    int len2 = mask[0];
    if (len2 == 0)
      return val;

    int len1 = val[0];
    if (len1 == 0)
      return copy(mask, len2 = Math.abs(len2) + 1, val, len2);

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    final int off = 1;
    len1 += off;
    len2 += off;

//    if (len1 < len2 || sig1 < sig2) {
//      // Copy so that the provided val2 reference does not get modified
//      final int[] copy = Arrays.copyOf(mask, len2);
//      mask = val;
//      val = copy;
//      sig1 ^= sig2;
//      sig2 ^= sig1;
//      sig1 ^= sig2;
//      len1 ^= len2;
//      len2 ^= len1;
//      len1 ^= len2;
//    }

    if (sig1 > 0) {
      if (len2 > len1) {
        if (len2 > val.length)
          val = realloc(val, len1 - off + 1, len2 + 2);

        System.arraycopy(mask, len1, val, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (sig2 > 0) {
        for (int i = 0; i < mlen; ++i)
          val[i] ^= mask[i];
      }
      else {
        int a = val[off], b = mask[off], j = 1 + off;
        for (; (a | b) == 0 && j < mlen; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          val[j - 1] = -a;
          for (; mask[j] == 0; ++j)
            val[j] ^= -1;

          if (j < len1)
            val[j] = ~(val[j] ^ -mask[j]);
          else
            val[j] = ~-mask[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          val[j - 1] = b; // -(0^-b)
        }
        else { // a!=0 && b!=0
          val[j - 1] = -(a ^ -b);
          for (; j < mlen && val[j - 1] == 0; ++j)
            val[j] = -(val[j] ^ ~mask[j]);

          if (j >= mlen && val[j - 1] == 0) {
            final int[] tmp = j < len1 ? val : mask;
            final int blen = Math.max(len1, len2);
            for (; j < blen && tmp[j] == -1; ++j)
              val[j] = 0;

            if (blen == val.length)
              val = realloc(val, len1 - off + 1, blen + 2); // len==blen

            if (j == blen) {
              val[blen] = 1;
              len1 = blen + 1;
            }
            else {
              val[j] = -~tmp[j];
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          val[j] ^= mask[j]; // ~(dig[j]^~dig2[j]);

        sig1 = -1;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > val.length)
          val = realloc(val, len1 - off + 1, len2 + 2);

        System.arraycopy(mask, len1, val, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (sig2 > 0) {
        int a = val[off], b = mask[off], j = 1 + off;
        for (; (a | b) == 0 && j < mlen; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          while (j < mlen && mask[j] == 0)
            ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val[j - 1] = -b; j < mlen && val[j] == 0; ++j)
            val[j] = ~mask[j];

          while (j < len1 && val[j] == 0)
            val[j++] = -1;

          if (j < mlen)
            val[j] = ~(-val[j] ^ mask[j]);
          else
            val[j] = ~-val[j];

          ++j;
        }
        else { // a!=0 && b!=0
          val[j - 1] = -(-a ^ b);
          if (val[j - 1] == 0) { // Perform carry.
            for (; val[j - 1] == 0 && j < mlen; j++) {
              val[j] ^= mask[j];
              ++val[j];
            }

            final int blen = Math.max(len1, len2);
            for (; val[j - 1] == 0 && j < blen; j++)
              ++val[j];

            if (j == val.length)
              val = realloc(val, len1, j + 1);

            if (val[j - 1] == 0) {
              val[j] = 1;
              len1 = j;
              val[0] = sig1 < 0 ? -len1 : len1;

              // _debugLenSig(val);
              return val;
            }
          }
        }

        for (; j < mlen; j++)
          val[j] ^= mask[j]; // ~(~dig[j]^dig2[j]);
      }
      else {
        int a = val[off], b = mask[off], j = 1 + off;
        for (; (a | b) == 0 && j < mlen; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          for (val[j - 1] = -a; mask[j] == 0; ++j)
            val[j] ^= -1; // ~dig[j]

          if (j < len1)
            val[j] = ~val[j] ^ -mask[j];
          else
            val[j] = ~-val[j]; // dig[j]==dig2[j], ~0^-dig2[j]

          ++j;
        }
        else if (a == 0) { // && b!=0
          for (val[j - 1] = -b; j < len2 && val[j] == 0; ++j)
            val[j] = ~mask[j];

          while (val[j] == 0)
            val[j++] = -1;

          if (j < len2)
            val[j] = -val[j] ^ ~mask[j];
          else
            val[j] = ~-val[j]; // -dig[j]^~0

          ++j;
        }
        else { // a!=0 && b!=0
          val[j - 1] = -a ^ -b;
        }

        for (; j < mlen; ++j)
          val[j] ^= mask[j]; // ~dig[j]^~dig2[j]

        sig1 = 1;
      }
    }

    if (len2 > len1)
      len1 = len2;

    len1 -= off;
    for (; len1 > 0 && val[len1] == 0; --len1);
    val[0] = sig1 < 0 ? -len1 : len1;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Performs a bitwise "and-not" of the specified {@linkplain BigInt#val()
   * value-encoded mask} onto the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = val &amp; ~mask
   * </pre>
   *
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mask The number with which to perform the bitwise "and-not".
   * @return The result of the bitwise "and-not" of the specified value-encoded
   *         mask onto the provided {@linkplain BigInt#val() value-encoded
   *         number}.
   * @complexity O(n)
   */
  public static int[] andNot(int[] val, final int[] mask) {
    int sig1 = 1, len1 = val[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = mask[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    final int off = 1;
    len1 += off;
    len2 += off;

    final int mlen = Math.min(len1, len2);
    if (sig1 > 0) {
      if (sig2 > 0) {
        for (int i = off; i < mlen; ++i)
          val[i] &= ~mask[i];
      }
      else {
        int j = off;
        while (j < mlen && mask[j] == 0)
          ++j;

        if (j < mlen)
          for (val[j] &= ~-mask[j]; ++j < mlen;)
            val[j] &= mask[j]; // ~~val2[j]

        len1 = mlen;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > val.length)
          val = realloc(val, len1 - off + 1, len2 + 3);

        System.arraycopy(mask, len1, val, len1, len2 - len1);
      }

      if (sig2 > 0) {
        int j = off;
        for (; val[j] == 0; ++j);
        if (j < mlen) {
          val[j] = -(-val[j] & ~mask[j]);
          while (++j < mlen && val[j - 1] == 0) // FIXME:...
            val[j] = -~(val[j] | mask[j]); // -(~dig[j]&~val2[j])

          if (j == mlen && val[j - 1] == 0) {
            final int blen = Math.max(len1, len2);
            while (j < blen && val[j] == -1)
              val[j++] = 0; // val2[j]==dig[j]

            if (j < blen) {
              val[j] = -~val[j];
            }
            else {
              if (blen >= val.length)
                val = realloc(val, len1, blen + 2);

              val[blen] = 1;

              len1 = blen + 1;
              len1 -= off;
              val[0] = sig1 < 0 ? -len1 : len1;
              // _debugLenSig(val);
              return val;
            }

            ++j;
          }

          for (; j < mlen; ++j)
            val[j] |= mask[j]; // ~(~dig[j]&~val2[j]);

          if (len2 > len1)
            len1 = len2;
        }
      }
      else {
        int a = val[off], b = mask[off], j = 1 + off;
        for (; j < mlen && (a | b) == 0; a = val[j], b = mask[j], ++j);
        if (a != 0 && b == 0) {
          val[j - 1] = -a;
          for (; j < len2 && mask[j] == 0; ++j)
            val[j] ^= -1;

          if (j < len1)
            val[j] = ~(val[j] | -mask[j]); // ~dig[j]&~-val2[j]);
          else
            val[j] = ~-val[j]; // dig[j]==val2[j]

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          for (; j < mlen && val[j] == 0; ++j);
          if (j < mlen)
            val[j] = -val[j] & mask[j]; // ~~val2[j]

          ++j;
        }
        else {
          val[j - 1] = -a & ~-b;
        }

        for (; j < mlen; ++j)
          val[j] = ~val[j] & mask[j];

        // FIXME: This was hacked together...
        if (len2 > 2 && val[len2 - 1] == 0)
          --len2;

        len1 = len2;
        sig1 = 1;
      }
    }

    len1 -= off;
    if (len1 > 0)
      for (; val[len1] == 0; --len1);

    val[0] = sig1 < 0 ? -len1 : len1;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Inverts the sign and all bits of the provided {@linkplain BigInt#val()
   * value-encoded number}.
   *
   * <pre>
   * val = ~val
   * </pre>
   *
   * The identity {@code -val = ~val + 1} holds.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The result of the bitwise "not" of the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int[] not(int[] val) {
    final int len = val[0];
    if (len == 0) {
      val[0] = -1;
      val[1] = 1;
      // _debugLenSig(val);
      return val;
    }

    if (len < 0)
      usubVal(val, -len, false, 1);
    else
      val = uaddVal(val, len, true, 1);

    val[0] = -val[0];
    // _debugLenSig(val);
    return val;
  }

  /**
   * Returns a byte array containing the two's-complement representation of the
   * provided {@linkplain BigInt#val() value-encoded number}. The byte array
   * will be in the endian order as specified by the {@code littleEndian}
   * argument. The array will contain the minimum number of bytes required to
   * represent the provided number, including at least one sign bit, which is
   * {@code (ceil((bitLength(val) + 1) / 8))}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param littleEndian Whether the produced byte array is to be encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return A byte array containing the two's-complement representation of the
   *         provided {@linkplain BigInt#val() value-encoded number}.
   */
  public static byte[] toByteArray(final int[] val, final boolean littleEndian) {
    int len = val[0];
    if (len == 0)
      return new byte[] {0};

    int sig = 0; if (len < 0) { len = -len; sig = -1; }
    final int byteLen = (int)(bitLength(val, len) / 8L + 1L);
    final int nzIndex = firstNonzeroIntNum(val, 1, len);
    final byte[] bytes = new byte[byteLen];

    if (littleEndian) {
      for (int i = 0, bytesCopied = 4, nextInt = 0, intIndex = 1; i < byteLen; ++i) {
        if (bytesCopied == 4) {
          nextInt = getInt(sig, len, val, nzIndex, intIndex++);
          bytesCopied = 1;
        }
        else {
          nextInt >>>= 8;
          ++bytesCopied;
        }

        bytes[i] = (byte)nextInt;
      }
    }
    else {
      for (int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 1; i >= 0; --i) {
        if (bytesCopied == 4) {
          nextInt = getInt(sig, len, val, nzIndex, intIndex++);
          bytesCopied = 1;
        }
        else {
          nextInt >>>= 8;
          ++bytesCopied;
        }

        bytes[i] = (byte)nextInt;
      }
    }

    return bytes;
  }

  /**
   * Returns a {@link BigInteger} representation of the provided
   * {@linkplain BigInt#val() value-encoded number}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return A {@link BigInteger} representation of the provided
   *         {@linkplain BigInt#val() value-encoded number}.
   */
  public static BigInteger toBigInteger(final int[] val) {
    return new BigInteger(toByteArray(val, false));
  }

  /**
   * Returns a {@link BigDecimal} representation of the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return A {@link BigDecimal} representation of the provided {@linkplain BigInt#val() value-encoded number}.
   */
  public static BigDecimal toBigDecimal(final int[] val) {
    return new BigDecimal(toBigInteger(val));
  }

  /**
   * Returns the specified int of the little-endian two's complement
   * representation (int 0 is the least significant). The {@code int} number can
   * be arbitrarily high (values are logically preceded by infinitely many sign
   * {@code int}s).
   */
  private static int getInt(final int sig, final int len, final int[] val, final int nzIndex, final int n) {
    if (n < 1)
      return 0;

    if (n > len)
      return sig;

    final int v = val[n];
    return sig < 0 ? n <= nzIndex ? -v : ~v : v;
  }

  /**
   * Returns the index of the first non-zero limb.
   *
   * @param mag The magnitude.
   * @param off The offset.
   * @param len The length.
   * @return The index of the first non-zero limb.
   */
  private static int firstNonzeroIntNum(final int[] mag, int off, int len) {
    len += off;
    for (; off < len && mag[off] == 0; ++off); // FIXME: Make more efficient
    return off;
  }
}