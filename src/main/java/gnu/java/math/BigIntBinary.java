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

import java.util.Arrays;

@SuppressWarnings("javadoc")
abstract class BigIntBinary extends BigIntDivision {
  private static final long serialVersionUID = 6584645376198040730L;

  public static int bitCount(final int[] val) {
    int i, bits = 0;
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    // Count the bits in the magnitude
    for (i = 1; i <= len; ++i)
      bits += Integer.bitCount(val[i]);

    if (sig < 0) {
      // Count the trailing zeros in the magnitude
      for (i = 1; i <= len && val[i] == 0; ++i)
        bits += 32;

      bits += Integer.numberOfTrailingZeros(val[i]) - 1;
    }

    return bits;
  }

  /**
   * Returns the number of bits in the minimal two's-complement representation
   * of this {@link BigInt}, <em>excluding</em> a sign bit. For positive
   * {@link BigInt}s, this is equivalent to the number of bits in the ordinary
   * binary representation. For zero this method returns {@code 0}. (Computes
   * {@code (ceil(log2(this < 0 ? -this : this + 1)))}.)
   *
   * @return Number of bits in the minimal two's-complement representation of
   *         this {@link BigInt}, <em>excluding</em> a sign bit.
   */
  public static int bitLength(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 0; // offset by one to initialize

    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    // Calculate the bit length of the magnitude
    final int magBitLength = ((len - 1) << 5) + bitLengthForInt(val[len]);
    if (sig)
      return magBitLength;

    // Check if magnitude is a power of two
    boolean pow2 = Integer.bitCount(val[len]) == 1;
    for (--len; len >= 1 && pow2; --len)
      pow2 = val[len] == 0;

    return pow2 ? magBitLength - 1 : magBitLength;
  }

  /**
   * Package private method to return bit length for an integer.
   */
  static int bitLengthForInt(final int n) {
    return Integer.SIZE - Integer.numberOfLeadingZeros(n);
  }

  /**
   * Tests if the given bit in the number is set.
   *
   * @param bit The index of the bit to test.
   * @return true if the given bit is one.
   * @complexity O(n)
   */
  public static boolean testBit(final int[] val, final int bit) {
    boolean sig = true; int len = val[0]; if (len < 0) { len = -len; sig = false; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    if (bigBit > len)
      return !sig;

    if (sig)
      return (val[bigBit] & 1 << smallBit) != 0;

    int j = 1;
    for (; j <= bigBit && val[j] == 0; ++j);
    if (j > bigBit)
      return false;

    if (j < bigBit)
      return (val[bigBit] & 1 << smallBit) == 0;

    j = -val[bigBit];
    return (j & 1 << smallBit) != 0;
  }

  /**
   * Shifts this number right by the given amount.
   *
   * @param shift The amount to shift.
   * @complexity O(n)
   */
  public static int[] shiftRight(final int[] val, final int shift) {
    if (shift < 0)
      return shiftLeft(val, -shift);

    if (shift == 0 || isZero(val))
      return val;

    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    final int shiftBig = shift >>> 5;
    // Special case: entire contents shifted off the end
    if (shiftBig >= len)
      return sig >= 0 ? setToZero(val) : assign(val, sig, 1);

    final int shiftSmall = shift & 31;
    boolean oneLost = false;
    if (sig < 0) {
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

    // FIXME: What if an overflow happens? Look at Integer#javaIncrement(int[])
    if (oneLost) {
      if (val[0] == 0)
        val[0] = sig;

      ++val[1];
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Shifts this number right by the given amount (less than 32).
   *
   * @param shift The amount to shift.
   * @complexity O(n)
   */
  private static int smallShiftRight(final int[] val, int len, final int shift) {
    for (int next = val[1], i = 1; i < len; ++i)
      val[i] = next >>> shift | (next = val[i + 1]) << 32 - shift;

    if ((val[len] >>>= shift) == 0 && len >= 1)
      --len;

    return len;
  }

  /**
   * Shifts this number right by 32*shift, i.e. moves each digit shift positions
   * to the right.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  private static int bigShiftRight(final int[] val, int len, int shift) {
    System.arraycopy(val, shift + 1, val, 1, len -= shift);
    return len;
  }

  public static int[] shiftLeft(int[] val, final int shift) {
    if (shift < 0)
      return shiftRight(val, -shift);

    if (shift == 0 || isZero(val))
      return val;

    boolean sig = true; int len = val[0]; if (len < 0) { len = -len; sig = false; }
    final int shiftBig = shift >>> 5;

    if (shiftBig > 0) {
      val = bigShiftLeft(val, len, sig, shiftBig);
      sig = true; len = val[0]; if (len < 0) { len = -len; sig = false; }
    }

    final int shiftSmall = shift & 31;
    if (shiftSmall > 0) {
      val = smallShiftLeft(val, shiftBig + 1, len, sig, shiftSmall);
    }

    _debugLenSig(val);
    return val;
  }

  /**
   * Shifts this number left by 32*shift, i.e. moves each digit shift positions
   * to the left.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  private static int[] bigShiftLeft(int[] val, int len, final boolean sig, int shift) {
    ++shift;
    int newLen = len + shift;
    if (newLen > val.length) {
      final int[] tmp = alloc(newLen);
      System.arraycopy(val, 1, tmp, shift, len);
      val = tmp;
    }
    else {
      System.arraycopy(val, 1, val, shift, len);
      for (int i = 1; i < shift; ++i)
        val[i] = 0;
    }

    --newLen;
    val[0] = sig ? newLen : -newLen;
    _debugLenSig(val);
    return val;
  }

  /**
   * Shifts this number left by the given amount (less than 32) starting at the
   * given digit, i.e. the first (<len) digits are left untouched.
   *
   * @param shift The amount to shift.
   * @param off The digit to start shifting from.
   * @complexity O(n)
   */
  private static int[] smallShiftLeft(int[] val, final int off, int len, final boolean sig, final int shift) {
    int[] res = val;
    int next;
    if ((val[len] << shift >>> shift) != val[len]) { // Overflow?
      if (++len >= val.length) {
        next = 0;
        res = realloc(val, len + 1);
      }
      else {
        next = val[len];
        val[len] = 0;
      }

      res[0] = sig ? len : -len;
    }
    else {
      next = len >= val.length ? 0 : val[len];
    }

    for (; len > off; --len)
      res[len] = next << shift | (next = val[len - 1]) >>> 32 - shift;

    res[off] = next << shift;
    _debugLenSig(res);
    return res;
  }

  public static int[] setBit(int[] val, final int bit) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    if (sig >= 0) {
      if (bigBit >= val.length) {
        val = realloc(val, bigBit + 1);
        len = bigBit;
      }
      else if (bigBit > len) {
        for (; len < bigBit;)
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
          val[bigBit] ^= ((j << 1) - 1) ^ (k - 1);
          val[bigBit] |= k;
        }
      }
    }

    for (; val[len] == 0; --len);
    val[0] = sig < 0 ? -len : len;
    _debugLenSig(val);
    return val;
  }

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
        val = realloc(val, bigBit + 1);
        len = bigBit;
        val[bigBit] |= 1 << smallBit;
      }
      else if (bigBit > len) {
        for (; len < bigBit;)
          val[++len] = 0;

        val[bigBit] |= 1 << smallBit;
        len = bigBit;
      }
      else {
        int j = 1;
        for (; j <= bigBit && val[j] == 0;)
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
            val = realloc(val, j + 2);

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
    _debugLenSig(val);
    return val;
  }

  public static int[] flipBit(int[] val, final int bit) {
    int sig = 1, len = val[0]; if (len < 0) { len = -len; sig = -1; }

    final int bigBit = (bit >>> 5) + 1;
    final int smallBit = bit & 31;
    block:
    if (bigBit >= val.length) {
      val = realloc(val, bigBit + 1);
      val[bigBit] ^= 1 << smallBit;
      len = bigBit;
    }
    else if (bigBit > len) {
      for (; len < bigBit;)
        val[++len] = 0;

      val[bigBit] ^= 1 << smallBit;
      len = bigBit;
    }
    else if (sig >= 0) {
      val[bigBit] ^= 1 << smallBit;
    }
    else {
      int j = 1;
      for (; j <= bigBit && val[j] == 0;)
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
//        if (j == len && val[len] == 0)
//          val[0] = len;
      }
      else {
        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0) {
          val[bigBit] ^= ((j << 1) - 1) ^ (k - 1);
          _debugLenSig(val);
          return val;
        }

        if (j - k < 0) {
          val[bigBit] ^= k;
          _debugLenSig(val);
          return val;
        }

        j = val[bigBit];
        if (j == (-1 ^ k - 1)) { // TODO: Refactor with clearBit?
          val[bigBit] = 0;
          for (j = bigBit + 1; j <= len && val[j] == -1; ++j)
            val[j] = 0;

          if (j == val.length)
            val = realloc(val, j + 2);

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
    _debugLenSig(val);
    return val;
  }

  /**
   * Bitwise-ands this number with the given number, i.e. this &= mask.
   *
   * @param mask The number to bitwise-and with.
   * @complexity O(n)
   */
  public static int[] and(int[] val1, final int[] val2) {
    int sig1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    if (sig2 == 0 || isZero(val2)) { // FIXME: Defensive check
      setToZero(val1);
      return val1;
    }

    if (sig1 >= 0) {
      if (sig2 > 0) {
        if (len2 < len1)
          len1 = len2;

        boolean zeroes = true;
        for (int i = len1; i >= 1; --i) {
          val1[i] &= val2[i];
          if (zeroes && (zeroes = val1[i] == 0))
            --len1;
        }
      }
      else {
        final int mlen = Math.min(len1, len2);
        int a = val1[1], b = val2[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          for (val1[j - 1] = 0; j <= mlen && val2[j] == 0; ++j)
            val1[j] = 0;

          if (j <= mlen)
            val1[j] &= -val2[j];
          else
            len1 = 1;

          // FIXME: I think we can remove this
          // FIXME: This was hacked together...
          if (len1 > 1 && val1[len1] == 0)
            --len1;

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val1[j] == 0)
            ++j;
        }
        else {
          val1[j - 1] &= -b;
        }

        for (; j <= mlen; ++j)
          val1[j] &= ~val2[j];
      }
    }
    else {
      int mlen = Math.min(len1, len2); // FIXME: Make this final
      if (sig2 > 0) {
        int a = val1[1], b = val2[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val1[j], b = val2[j], ++j);

        if (a != 0 && b == 0) {
          for (val1[j - 1] = 0; j <= mlen && val2[j] == 0; ++j)
            val1[j] = 0;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val1[j] == 0)
            ++j;

          if (j <= mlen)
            val1[j] = -val1[j] & val2[j];

          ++j;
        }
        else {
          val1[j - 1] = -a & b;
        }

        for (; j <= mlen; ++j)
          val1[j] = ~val1[j] & val2[j];

        if (len2 > len1) {
          if (len2 >= val1.length)
            val1 = realloc(val1, len2 + 3);

          System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);
        }

        // FIXME: This was hacked together...
        if (len2 > 1 && val1[len2] == 0)
          --len2;

        len1 = len2;
        sig1 = 1;
      }
      else {
        if (len2 > len1) {
          if (len2 >= val1.length)
            val1 = realloc(val1, len2 + 3);

          System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);
        }

        int a = val1[1], b = val2[1], j = 2;
        for (; (a | b) == 0; a = val1[j], b = val2[j], ++j);

        if (a != 0 && b == 0) {
          for (val1[j - 1] = 0; j <= mlen && val2[j] == 0; ++j)
            val1[j] = 0;

          if (j <= mlen)
            val1[j] = -(~val1[j] & -val2[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j <= mlen && val1[j] == 0)
            ++j;

          if (j <= mlen)
            val1[j] = -(-val1[j] & ~val2[j]);

          ++j;
        }
        else {
          val1[j - 1] = -(-a & -b);
        }

        ++mlen;
        if (j <= mlen && val1[j - 1] == 0) {
          if (j < mlen)
            for (val1[j] = -~(val1[j] | val2[j]); ++j < mlen && val1[j - 1] == 0;)
              val1[j] = -~(val1[j] | val2[j]); // -(~dig[j]&~mask.dig[j])

          if (j == mlen && val1[j - 1] == 0) {
            int blen = Math.max(len1, len2);
            while (j <= blen && val1[j] == -1)
              val1[j++] = 0; // mask.dig[j]==dig[j]

            if (j <= blen) {
              val1[j] = -~val1[j];
            }
            else {
              ++blen;
              if (blen >= val1.length)
                val1 = realloc(val1, blen + 2);

              val1[blen] = 1;
              val1[0] = sig1 < 0 ? -blen : blen;
              _debugLenSig(val1);
              return val1;
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          val1[j] |= val2[j]; // ~(~dig[j]&~mask.dig[j]);

        if (len2 > len1)
          len1 = len2;
      }
    }

    if (len1 > 0)
      for (; val1[len1] == 0; --len1);

    val1[0] = sig1 < 0 ? -len1 : len1;

    _debugLenSig(val1);
    return val1;
  }

  /**
   * Bitwise-ors this number with the given number, i.e. this |= mask.
   *
   * @param mask The number to bitwise-or with.
   * @complexity O(n)
   */
  public static int[] or(int[] val1, final int[] val2) {
    if (isZero(val2))
      return val1;

    int sig1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    if (len1 == 0) // FIXME: Defensive check
      return val2;

    if (sig1 >= 0) {
      if (sig2 >= 0) {
        if (len2 > len1) {
          if (len2 >= val1.length)
            val1 = realloc(val1, len2 + 2);

          System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);
          for (int i = 1; i <= len1; ++i)
            val1[i] |= val2[i];

          len1 = len2;
        }
        else {
          for (int i = 1; i <= len2; ++i)
            val1[i] |= val2[i];
        }
      }
      else {
        if (len2 >= val1.length)
          val1 = realloc(val1, len2 + 2);

        if (len2 > len1)
          System.arraycopy(val2, len1 + 1, val1, len1 + 1, len2 - len1);

        final int mlen = Math.min(len2, len1);
        int a = val1[1], b = val2[1], j = 2;
        for (; (a | b) == 0 && j <= mlen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          val1[j - 1] = -a;
          for (; val2[j] == 0; ++j)
            val1[j] ^= -1;

          if (j <= mlen)
            val1[j] = ~(val1[j] | -val2[j]);
          else // mask.dig[j] == dig[j]
            val1[j] = ~-val1[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val1[j - 1] = b; j <= mlen && val1[j] == 0; ++j)
            val1[j] = val2[j];
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(a | -b);
        }

        for (; j <= mlen; ++j)
          val1[j] = ~val1[j] & val2[j]; // ~(dig[j]|~mask.dig[j])

        len1 = len2;
        sig1 = -1;
      }
    }
    else {
      final int mlen = Math.min(len2, len1);

      int a = val1[1], b = val2[1], j = 2;
      for (; (a | b) == 0 && j <= mlen; a = val1[j], b = val2[j], ++j);

      if (sig2 > 0) {
        if (a != 0 && b == 0) {
          for (; j <= mlen && val2[j] == 0; ++j);
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          val1[j - 1] = -b;
          for (; j <= mlen && val1[j] == 0; ++j)
            val1[j] = ~val2[j];

          if (j <= mlen) {
            val1[j] = ~(-val1[j] | val2[j]);
          }
          else {
            for (; val1[j] == 0; ++j)
              val1[j] = -1;

            val1[j] = ~-val1[j];
          }

          ++j;
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(-a | b);
        }

        for (; j <= mlen; ++j)
          val1[j] &= ~val2[j]; // ~(~dig[j]|mask.dig[j])
      }
      else {
        if (a != 0 && b == 0) {
          for (; j <= mlen && val2[j] == 0; ++j);
          if (j <= mlen)
            val1[j] = ~(~val1[j] | -val2[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val1[j - 1] = b; j <= mlen && val1[j] == 0; ++j)
            val1[j] = val2[j];

          if (j <= mlen)
            val1[j] = ~(-val1[j] | ~val2[j]);

          ++j;
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(-a | -b);
        }

        for (; j <= mlen; ++j)
          val1[j] &= val2[j]; // ~(~dig[j]|~mask.dig[j])

        len1 = mlen;
      }
    }

    if (len1 > 0)
      for (; val1[len1] == 0; --len1);

    val1[0] = sig1 < 0 ? -len1 : len1;

    _debugLenSig(val1);
    return val1;
  }

  public static void swap(final int[] a, final int[] b) {
    if (a.length < b.length) {

    }
  }

  /**
   * Bitwise-xors this number with the given number, i.e. this ^= mask.
   *
   * @param mask The number to bitwise-xor with.
   * @complexity O(n)
   */
  public static int[] xor(int[] val1, int[] val2) {
    if (isZero(val2))
      return val1;

    if (isZero(val1))
      return val1 = val2.clone();

    final int fromIndex = 1;
    int sig1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    len1 += fromIndex;
    int sig2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    len2 += fromIndex;

    if (len1 < len2 || sig1 < sig2) {
      // Copy so that the provided val2 reference does not get modified
      final int[] copy = Arrays.copyOf(val2, len2);
      val2 = val1;
      val1 = copy;
      sig1 ^= sig2;
      sig2 ^= sig1;
      sig1 ^= sig2;
      len1 ^= len2;
      len2 ^= len1;
      len1 ^= len2;
    }

    if (sig1 > 0) {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 2);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (sig2 > 0) {
        for (int i = 0; i < mlen; ++i)
          val1[i] ^= val2[i];
      }
      else {
        int a = val1[fromIndex], b = val2[fromIndex], j = 1 + fromIndex;
        for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          val1[j - 1] = -a;
          for (; val2[j] == 0; ++j)
            val1[j] ^= -1;

          if (j < len1)
            val1[j] = ~(val1[j] ^ -val2[j]);
          else
            val1[j] = ~-val2[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          val1[j - 1] = b; // -(0^-b)
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(a ^ -b);
          for (; j < mlen && val1[j - 1] == 0; ++j)
            val1[j] = -(val1[j] ^ ~val2[j]);

          if (j >= mlen && val1[j - 1] == 0) {
            final int[] tmp = j < len1 ? val1 : val2;
            final int blen = Math.max(len1, len2);
            for (; j < blen && tmp[j] == -1; ++j)
              val1[j] = 0;

            if (blen == val1.length)
              val1 = realloc(val1, blen + 2); // len==blen

            if (j == blen) {
              val1[blen] = 1;
              len1 = blen + 1;
            }
            else {
              val1[j] = -~tmp[j];
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          val1[j] ^= val2[j]; // ~(dig[j]^~dig2[j]);

        sig1 = -1;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 2);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (sig2 > 0) {
        int a = val1[fromIndex], b = val2[fromIndex], j = 1 + fromIndex;
        for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          while (j < mlen && val2[j] == 0)
            ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val1[j - 1] = -b; j < mlen && val1[j] == 0; ++j)
            val1[j] = ~val2[j];

          while (j < len1 && val1[j] == 0)
            val1[j++] = -1;

          if (j < mlen)
            val1[j] = ~(-val1[j] ^ val2[j]);
          else
            val1[j] = ~-val1[j];

          ++j;
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(-a ^ b);
        }

        for (; j < mlen; j++)
          val1[j] ^= val2[j]; // ~(~dig[j]^dig2[j]);
      }
      else {
        int a = val1[fromIndex], b = val2[fromIndex], j = 1 + fromIndex;
        for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          for (val1[j - 1] = -a; val2[j] == 0; ++j)
            val1[j] ^= -1; // ~dig[j]

          if (j < len1)
            val1[j] = ~val1[j] ^ -val2[j];
          else
            val1[j] = ~-val1[j]; // dig[j]==dig2[j], ~0^-dig2[j]

          ++j;
        }
        else if (a == 0) { // && b!=0
          for (val1[j - 1] = -b; j < len2 && val1[j] == 0; ++j)
            val1[j] = ~val2[j];

          while (val1[j] == 0)
            val1[j++] = -1;

          if (j < len2)
            val1[j] = -val1[j] ^ ~val2[j];
          else
            val1[j] = ~-val1[j]; // -dig[j]^~0

          ++j;
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -a ^ -b;
        }

        for (; j < mlen; ++j)
          val1[j] ^= val2[j]; // ~dig[j]^~dig2[j]

        sig1 = 1;
      }
    }

    if (len2 > len1)
      len1 = len2;

    len1 -= fromIndex;
    for (; len1 > 0 && val1[len1] == 0; --len1);
    val1[0] = sig1 < 0 ? -len1 : len1;

    _debugLenSig(val1);
    return val1;
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param mask The number to bitwise-and-not with.
   * @complexity O(n)
   */
  public static int[] andNot(int[] val1, final int[] val2) {
    final int fromIndex = 1;
    int sig1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    len1 += fromIndex;
    int sig2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    len2 += fromIndex;

    final int mlen = Math.min(len1, len2);
    if (sig1 > 0) {
      if (sig2 > 0) {
        for (int i = fromIndex; i < mlen; ++i)
          val1[i] &= ~val2[i];
      }
      else {
        int j = fromIndex;
        while (j < mlen && val2[j] == 0)
          ++j;

        if (j < mlen)
          for (val1[j] &= ~-val2[j]; ++j < mlen;)
            val1[j] &= val2[j]; // ~~val2[j]

        len1 = mlen;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 3);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      if (sig2 > 0) {
        int j = fromIndex;
        for (; val1[j] == 0; ++j);
        if (j < mlen) {
          val1[j] = -(-val1[j] & ~val2[j]);
          for (; ++j < mlen && val1[j - 1] == 0;) // FIXME:...
            val1[j] = -~(val1[j] | val2[j]); // -(~dig[j]&~val2[j])

          if (j == mlen && val1[j - 1] == 0) {
            final int blen = Math.max(len1, len2);
            while (j < blen && val1[j] == -1)
              val1[j++] = 0; // val2[j]==dig[j]

            if (j < blen) {
              val1[j] = -~val1[j];
            }
            else {
              if (blen >= val1.length)
                val1 = realloc(val1, len1 - 1, blen + 2);

              val1[blen] = 1;

              len1 = blen + 1;
              len1 -= fromIndex;
              val1[0] = sig1 < 0 ? -len1 : len1;
              _debugLenSig(val1);
              return val1;
            }

            ++j;
          }

          for (; j < mlen; ++j)
            val1[j] |= val2[j]; // ~(~dig[j]&~val2[j]);

          if (len2 > len1)
            len1 = len2;
        }
      }
      else {
        int a = val1[fromIndex], b = val2[fromIndex], j = 1 + fromIndex;
        for (; j < mlen && (a | b) == 0; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          val1[j - 1] = -a;
          for (; j < len2 && val2[j] == 0; ++j)
            val1[j] ^= -1;

          if (j < len1)
            val1[j] = ~(val1[j] | -val2[j]); // ~dig[j]&~-val2[j]);
          else
            val1[j] = ~-val1[j]; // dig[j]==val2[j]

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          for (; j < mlen && val1[j] == 0; ++j);
          if (j < mlen)
            val1[j] = -val1[j] & val2[j]; // ~~val2[j]

          ++j;
        }
        else {
          val1[j - 1] = -a & ~-b;
        }

        for (; j < mlen; ++j)
          val1[j] = ~val1[j] & val2[j];

        // FIXME: This was hacked together...
        if (len2 > 2 && val1[len2 - 1] == 0)
          --len2;

        len1 = len2;
        sig1 = 1;
      }
    }

    len1 -= fromIndex;
    if (len1 > 0)
      for (; val1[len1] == 0; --len1);

    val1[0] = sig1 < 0 ? -len1 : len1;
    _debugLenSig(val1);
    return val1;
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public static int[] not(int[] val) {
    if (val[0] >= 0)
      val = uaddVal(val, 1);
    else
      usubVal(val, 1);

    val[0] = -val[0];
    _debugLenSig(val);
    return val;
  }

  /**
   * Returns a byte array containing the two's-complement representation of this
   * {@link BigInt}. The byte array will be in <i>big-endian</i> byte-order: the
   * most significant byte is in the zeroth element. The array will contain the
   * minimum number of bytes required to represent this {@link BigInt},
   * including at least one sign bit, which is {@code (ceil((this.bitLength() +
   * 1)/8))}. (This representation is compatible with the {@link #BigInt(byte[])
   * (byte[])} constructor.)
   *
   * @return A byte array containing the two's-complement representation of this
   *         {@link BigInt}.
   */
  public static byte[] toByteArray(final int[] val) {
    int sig = 0, len = val[0]; if (len < 0) { len = -len; sig = -1; }
    final int nzIndex = firstNonzeroIntNum(val, 1, len);
    final int byteLen = bitLength(val) / 8 + 1;
    final byte[] bytes = new byte[byteLen];

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

    return bytes;
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

  private static int firstNonzeroIntNum(final int[] mag, int off, int len) {
    // Search for the first nonzero int
    len += off;
    for (; off < len && mag[off] == 0; ++off);
    return off;
  }
}