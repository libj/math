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
abstract class BigBinary extends BigDivision {
  private static final long serialVersionUID = 6584645376198040730L;

  /**
   * Tests if the given bit in the number is set.
   *
   * @param bit The index of the bit to test.
   * @return true if the given bit is one.
   * @complexity O(n)
   */
  public static boolean testBit(final int[] mag, final int len, final int signum, final int bit) {
    final int bitBig = bit >>> 5;
    final int bitSmall = bit & 31;
    if (bitBig >= len)
      return signum < 0;

    if (signum > 0)
      return (mag[bitBig] & 1 << bitSmall) != 0;

    int j = 0;
    for (; j <= bitBig && mag[j] == 0; ++j);
    if (j > bitBig)
      return false;

    if (j < bitBig)
      return (mag[bitBig] & 1 << bitSmall) == 0;

    j = -mag[bitBig];
    return (j & 1 << bitSmall) != 0;
  }

  /**
   * Shifts this number right by the given amount (less than 32).
   *
   * @param shift The amount to shift.
   * @complexity O(n)
   */
  static int smallShiftRight(final int[] mag, int len, final int shift) {
    for (int next = mag[0], i = 0; i < len - 1; ++i)
      mag[i] = next >>> shift | (next = mag[i + 1]) << 32 - shift;

    if ((mag[len - 1] >>>= shift) == 0 && len > 1)
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
  static int bigShiftRight(final int[] mag, int len, final int shift) {
    System.arraycopy(mag, shift, mag, 0, len -= shift);
    return len;
  }

  /**
   * Shifts this number right by the given amount.
   *
   * @param shift The amount to shift.
   * @complexity O(n)
   */
  public static int shiftRight(final int[] mag, int len, final int shift) {
    int s = shift >>> 5;
    if (s > 0)
      len = bigShiftRight(mag, len, s);

    s = shift & 31;
    if (s > 0)
      len = smallShiftRight(mag, len, s);

    return len;
  }

  /**
   * Shifts this number left by 32*shift, i.e. moves each digit shift positions
   * to the left.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  static int[] bigShiftLeft(int[] mag, final int len, final int shift) {
    final int newLen = len + shift;
    if (newLen > mag.length) {
      final int[] tmp = new int[newLen + 1];
      System.arraycopy(mag, 0, tmp, shift, len);
      mag = tmp;
    }
    else {
      System.arraycopy(mag, 0, mag, shift, len);
      for (int i = 0; i < shift; ++i)
        mag[i] = 0;
    }

    return mag;
  }

  /**
   * Shifts this number left by the given amount (less than 32) starting at the
   * given digit, i.e. the first (<len) digits are left untouched.
   *
   * @param shift The amount to shift.
   * @param fromIndex The digit to start shifting from.
   * @complexity O(n)
   */
  static int[] smallShiftLeft(int[] mag, int len, final int shift, final int fromIndex) {
    if ((mag[len - 1] << shift >>> shift) != mag[len - 1]) { // Overflow?
      if (++len > mag.length)
        mag = realloc(mag, len, len+1);
      else
        mag[len - 1] = 0;
    }

    int next = len > mag.length ? 0 : mag[len - 1];
    for (int i = len - 1; i > fromIndex; --i)
      mag[i] = next << shift | (next = mag[i - 1]) >>> 32 - shift;

    mag[fromIndex] = next << shift;

    clear(mag, len);
    return mag;
  }

  public static int[] shiftLeft(int[] mag, int len, final int shift) {
    final int shiftBig = shift >>> 5;
    if (shiftBig > 0) {
      mag = bigShiftLeft(mag, len, shiftBig);
      len += shiftBig;
    }

    final int shiftSmall = shift & 31;
    if (shiftSmall > 0) {
      mag = smallShiftLeft(mag, len, shiftSmall, shiftBig);
    }

    return mag;
  }

  public static int[] setBit(int[] mag, int len, final int signum, final int bit) {
    final int bigBit = bit >>> 5, smallBit = bit & 31;
    if (signum >= 0) {
      if (bigBit >= mag.length) {
        mag = realloc(mag, len, bigBit + 1);
        len = bigBit + 1;
      }
      else if (bigBit >= len) {
        for (; len <= bigBit; ++len)
          mag[len] = 0;
        // len = bigBit+1;
      }

      mag[bigBit] |= 1 << smallBit;
    }
    else {
      if (bigBit >= len)
        return mag;

      int j = 0;
      for (; j <= bigBit && mag[j] == 0;)
        ++j;

      if (j > bigBit) {
        mag[bigBit] = -1 << smallBit;
        for (; mag[j] == 0; ++j)
          mag[j] = -1;

        mag[j] = ~-mag[j];
        if (j == len - 1 && mag[len - 1] == 0)
          --len;
      }
      else if (j < bigBit) {
        mag[bigBit] &= ~(1 << smallBit);
      }
      else {
        j = Integer.lowestOneBit(mag[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (k - j > 0) {
          mag[bigBit] &= ~k; // Unsigned compare.
        }
        else {
          mag[bigBit] ^= ((j << 1) - 1) ^ (k - 1);
          mag[bigBit] |= k;
        }
      }
    }

    return mag;
  }

  public static int[] clearBit(int[] mag, int len, final int signum, final int bit) {
    final int bigBit = bit >>> 5;
    final int smallBit = bit & 31;
    if (signum >= 0) {
      if (bigBit < len) {
        mag[bigBit] &= ~(1 << smallBit);
      }
    }
    else {
      if (bigBit >= mag.length) {
        mag = realloc(mag, len, bigBit + 1);
        len = bigBit + 1;
        mag[bigBit] |= 1 << smallBit;
      }
      else if (bigBit >= len) {
        for (; len <= bigBit; ++len)
          mag[len] = 0;

        mag[bigBit] |= 1 << smallBit;
      }
      else {
        int j = 0;
        for (; j <= bigBit && mag[j] == 0;)
          ++j;

        if (j > bigBit)
          return mag;

        if (j < bigBit) {
          mag[bigBit] |= 1 << smallBit;
          return mag;
        }

        j = Integer.lowestOneBit(mag[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0)
          return mag; // Unsigned compare

        if (j - k < 0) {
          mag[bigBit] |= k;
          return mag;
        }

        j = mag[bigBit];
        if (j == (-1 ^ k - 1)) {
          mag[bigBit] = 0;
          for (j = bigBit + 1; j < len && mag[j] == -1; ++j)
            mag[j] = 0;

          if (j == mag.length)
            mag = realloc(mag, len, j + 2);

          if (j == len) {
            mag[len++] = 1;
          }
          else {
            mag[j] = -~mag[j];
          }
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          mag[bigBit] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    return mag;
  }

  public static int[] flipBit(int[] mag, int len, final int signum, final int bit) {
    final int bigBit = bit >>> 5, smallBit = bit & 31;
    block:
    if (bigBit >= mag.length) {
      mag = realloc(mag, len, bigBit + 1);
      len = bigBit + 1;
      mag[bigBit] ^= 1 << smallBit;
    }
    else if (bigBit >= len) {
      for (; len <= bigBit; ++len)
        mag[len] = 0;

      mag[bigBit] ^= 1 << smallBit;
    }
    else if (signum >= 0) {
      mag[bigBit] ^= 1 << smallBit;
    }
    else {
      int j = 0;
      for (; j <= bigBit && mag[j] == 0;)
        ++j;

      if (j < bigBit) {
        mag[bigBit] ^= 1 << smallBit;
        break block;
      }

      if (j > bigBit) { // TODO: Refactor with setBit?
        mag[bigBit] = -1 << smallBit;
        for (; mag[j] == 0; ++j)
          mag[j] = -1;

        mag[j] = ~-mag[j];
        if (j == len - 1 && mag[len - 1] == 0)
          --len;
      }
      else {
        j = Integer.lowestOneBit(mag[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0) {
          mag[bigBit] ^= ((j << 1) - 1) ^ (k - 1);
          return mag;
        }

        if (j - k < 0) {
          mag[bigBit] ^= k;
          return mag;
        }

        j = mag[bigBit];
        if (j == (-1 ^ k - 1)) { // TODO: Refactor with clearBit?
          mag[bigBit] = 0;
          for (j = bigBit + 1; j < len && mag[j] == -1; ++j)
            mag[j] = 0;
          if (j == mag.length)
            mag = realloc(mag, len, j + 2);
          if (j == len) {
            mag[len++] = 1;
          }
          else {
            mag[j] = -~mag[j];
          }
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          mag[bigBit] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    return mag;
  }

  /**
   * Bitwise-ands this number with the given number, i.e. this &= mask.
   *
   * @param mask The number to bitwise-and with.
   * @complexity O(n)
   */
  public static int[] and(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum2 == 0 || isZero(mag2, len2)) { // Defensive check
      len1 = setToZero(mag1);
    }
    else if (signum1 >= 0) {
      if (signum2 > 0) {
        if (len2 < len1)
          len1 = len2;

        for (int i = 0; i < len1; ++i)
          mag1[i] &= mag2[i];
      }
      else {
        final int mlen = Math.min(len1, len2);
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mlen; a = mag1[j], b = mag2[j], ++j);
        if (a != 0 && b == 0) {
          for (mag1[j - 1] = 0; j < mlen && mag2[j] == 0; ++j)
            mag1[j] = 0;

          if (j < mlen)
            mag1[j] &= -mag2[j];
          else if (j == len1)
            len1 = 1;

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j < mlen && mag1[j] == 0)
            ++j;
        }
        else {
          mag1[j - 1] &= -b;
        }

        for (; j < mlen; ++j)
          mag1[j] &= ~mag2[j];
      }
    }
    else {
      final int mlen = Math.min(len1, len2);
      if (signum2 > 0) {
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mlen; a = mag1[j], b = mag2[j], ++j);

        if (a != 0 && b == 0) {
          for (mag1[j - 1] = 0; j < mlen && mag2[j] == 0; ++j)
            mag1[j] = 0;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j < mlen && mag1[j] == 0)
            ++j;

          if (j < mlen)
            mag1[j] = -mag1[j] & mag2[j];

          ++j;
        }
        else {
          mag1[j - 1] = -a & b;
        }

        for (; j < mlen; ++j)
          mag1[j] = ~mag1[j] & mag2[j];

        if (len2 > len1) {
          if (len2 > mag1.length)
            mag1 = realloc(mag1, len1, len2 + 2);

          System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
        }

        // signum = 1; // Handled by caller, left for reference
        len1 = len2;
      }
      else {
        if (len2 > len1) {
          if (len2 > mag1.length)
            mag1 = realloc(mag1, len1, len2 + 2);

          System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
        }

        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0; a = mag1[j], b = mag2[j], ++j);

        if (a != 0 && b == 0) {
          for (mag1[j - 1] = 0; j < mlen && mag2[j] == 0; ++j)
            mag1[j] = 0;

          if (j < mlen)
            mag1[j] = -(~mag1[j] & -mag2[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          while (j < mlen && mag1[j] == 0)
            ++j;

          if (j < mlen)
            mag1[j] = -(-mag1[j] & ~mag2[j]);

          ++j;
        }
        else {
          mag1[j - 1] = -(-a & -b);
        }

        if (j <= mlen && mag1[j - 1] == 0) {
          if (j < mlen)
            for (mag1[j] = -~(mag1[j] | mag2[j]); ++j < mlen && mag1[j - 1] == 0;)
              mag1[j] = -~(mag1[j] | mag2[j]); // -(~dig[j]&~mask.dig[j])

          if (j == mlen && mag1[j - 1] == 0) {
            final int blen = Math.max(len1, len2);
            while (j < blen && mag1[j] == -1)
              mag1[j++] = 0; // mask.dig[j]==dig[j]

            if (j < blen) {
              mag1[j] = -~mag1[j];
            }
            else {
              if (blen >= mag1.length)
                mag1 = realloc(mag1, len1, blen + 2);

              mag1[blen] = 1;
              return mag1;
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          mag1[j] |= mag2[j]; // ~(~dig[j]&~mask.dig[j]);

        if (len2 > len1)
          len1 = len2;
      }
    }

    clear(mag1, len1);
    return mag1;
  }

  /**
   * Bitwise-ors this number with the given number, i.e. this |= mask.
   *
   * @param mask The number to bitwise-or with.
   * @complexity O(n)
   */
  public static int[] or(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum1 == 0 || isZero(mag1, len1)) // Defensive
      return mag2;

    if (signum1 >= 0) {
      if (signum2 >= 0) {
        if (len2 > len1) {
          if (len2 > mag1.length)
            mag1 = realloc(mag1, len1, len2 + 1);

          System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
          for (int i = 0; i < len1; ++i)
            mag1[i] |= mag2[i];

          len1 = len2;
        }
        else {
          for (int i = 0; i < len2; ++i)
            mag1[i] |= mag2[i];
        }
      }
      else {
        if (len2 > mag1.length)
          mag1 = realloc(mag1, len1, len2 + 1);

        if (len2 > len1)
          System.arraycopy(mag2, len1, mag1, len1, len2 - len1);

        final int mLen = Math.min(len2, len1);
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mLen; a = mag1[j], b = mag2[j], ++j);
        if (a != 0 && b == 0) {
          mag1[j - 1] = -a;
          for (; mag2[j] == 0; ++j)
            mag1[j] ^= -1;

          if (j < mLen)
            mag1[j] = ~(mag1[j] | -mag2[j]);
          else // mask.dig[j] == dig[j]
            mag1[j] = ~-mag1[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (mag1[j - 1] = b; j < mLen && mag1[j] == 0; ++j)
            mag1[j] = mag2[j];
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -(a | -b);
        }

        for (; j < mLen; ++j)
          mag1[j] = ~mag1[j] & mag2[j]; // ~(dig[j]|~mask.dig[j])

        // signum = -1; // Handled by caller, left for reference
        len1 = len2;
      }
    }
    else {
      final int mLen = Math.min(len2, len1);

      int a = mag1[0], b = mag2[0], j = 1;
      for (; (a | b) == 0 && j < mLen; a = mag1[j], b = mag2[j], ++j);

      if (signum2 > 0) {
        if (a != 0 && b == 0) {
          for (; j < mLen && mag2[j] == 0; ++j);
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          mag1[j - 1] = -b;
          for (; j < mLen && mag1[j] == 0; ++j)
            mag1[j] = ~mag2[j];

          if (j < mLen) {
            mag1[j] = ~(-mag1[j] | mag2[j]);
          }
          else {
            for (; mag1[j] == 0; ++j)
              mag1[j] = -1;

            mag1[j] = ~-mag1[j];
          }

          ++j;
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -(-a | b);
        }

        for (; j < mLen; ++j)
          mag1[j] &= ~mag2[j]; // ~(~dig[j]|mask.dig[j])
      }
      else {
        if (a != 0 && b == 0) {
          for (; j < mLen && mag2[j] == 0; ++j);
          if (j < mLen)
            mag1[j] = ~(~mag1[j] | -mag2[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (mag1[j - 1] = b; j < mLen && mag1[j] == 0; ++j)
            mag1[j] = mag2[j];

          if (j < mLen)
            mag1[j] = ~(-mag1[j] | ~mag2[j]);

          ++j;
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -(-a | -b);
        }

        for (; j < mLen; ++j)
          mag1[j] &= mag2[j]; // ~(~dig[j]|~mask.dig[j])

        len1 = mLen;
      }
    }

    clear(mag1, len1);
    return mag1;
  }

  /**
   * Bitwise-xors this number with the given number, i.e. this ^= mask.
   *
   * @param mask The number to bitwise-xor with.
   * @complexity O(n)
   */
  public static int[] xor(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum1 >= 0) {
      if (len2 > len1) {
        if (len2 > mag1.length)
          mag1 = realloc(mag1, len1, len2 + 2);

        System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (signum2 >= 0) {
        for (int i = 0; i < mlen; ++i)
          mag1[i] ^= mag2[i];
      }
      else {
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mlen; a = mag1[j], b = mag2[j], ++j);
        if (a != 0 && b == 0) {
          mag1[j - 1] = -a;
          for (; mag2[j] == 0; ++j)
            mag1[j] ^= -1;

          if (j < len1)
            mag1[j] = ~(mag1[j] ^ -mag2[j]);
          else
            mag1[j] = ~-mag2[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          mag1[j - 1] = b; // -(0^-b)
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -(a ^ -b);
          for (; j < mlen && mag1[j - 1] == 0; ++j)
            mag1[j] = -(mag1[j] ^ ~mag2[j]);
          if (j >= mlen && mag1[j - 1] == 0) {
            final int[] tmp = j < len1 ? mag1 : mag2;
            final int blen = Math.max(len1, len2);
            for (; j < blen && tmp[j] == -1; ++j)
              mag1[j] = 0;

            if (blen == mag1.length)
              mag1 = realloc(mag1, len1, blen + 2); // len==blen

            if (j == blen) {
              mag1[blen] = 1;
              len1 = blen + 1;
            }
            else {
              mag1[j] = -~tmp[j];
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          mag1[j] ^= mag2[j]; // ~(dig[j]^~mask.dig[j]);

        // signum = -1; // Handled by caller, left for reference
      }

      // if (len2 > len) len = len2; // Handled by caller, left for reference
    }
    else {
      if (len2 > len1) {
        if (len2 > mag1.length)
          mag1 = realloc(mag1, len1, len2 + 2);

        System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (signum2 >= 0) {
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mlen; a = mag1[j], b = mag2[j], ++j);
        if (a != 0 && b == 0) {
          while (j < mlen && mag2[j] == 0)
            ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (mag1[j - 1] = -b; j < mlen && mag1[j] == 0; ++j)
            mag1[j] = ~mag2[j];

          while (j < len1 && mag1[j] == 0)
            mag1[j++] = -1;

          if (j < mlen)
            mag1[j] = ~(-mag1[j] ^ mag2[j]);
          else
            mag1[j] = ~-mag1[j];

          ++j;
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -(-a ^ b);
        }

        for (; j < mlen; ++j)
          mag1[j] ^= mag2[j]; // ~(~dig[j]^mask.dig[j]);
      }
      else {
        int a = mag1[0], b = mag2[0], j = 1;
        for (; (a | b) == 0 && j < mlen; a = mag1[j], b = mag2[j], ++j);

        if (a != 0 && b == 0) {
          for (mag1[j - 1] = -a; mag2[j] == 0; ++j)
            mag1[j] ^= -1; // ~dig[j]

          if (j < len1)
            mag1[j] = ~mag1[j] ^ -mag2[j];
          else
            mag1[j] = ~-mag1[j]; // dig[j]==mask.dig[j], ~0^-mask.dig[j]

          ++j;
        }
        else if (a == 0) { // && b!=0
          for (mag1[j - 1] = -b; j < len2 && mag1[j] == 0; ++j)
            mag1[j] = ~mag2[j];

          while (mag1[j] == 0)
            mag1[j++] = -1;

          if (j < len2)
            mag1[j] = -mag1[j] ^ ~mag2[j];
          else
            mag1[j] = ~-mag1[j]; // -dig[j]^~0

          ++j;
        }
        else { // a!=0 && b!=0
          mag1[j - 1] = -a ^ -b;
        }

        for (; j < mlen; ++j)
          mag1[j] ^= mag2[j]; // ~dig[j]^~mask.dig[j]

        // signum = 1; // Handled by caller, left for reference
      }

      // if (len2 > len) len = len2; // Handled by caller, left for reference
    }

    return mag1;
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param mask The number to bitwise-and-not with.
   * @complexity O(n)
   */
  public static int[] andNot(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    final int mlen = Math.min(len1, len2);
    if (signum1 >= 0) {
      if (signum2 >= 0) {
        for (int i = 0; i < mlen; ++i)
          mag1[i] &= ~mag2[i];
      }
      else {
        int j = 0;
        while (j < mlen && mag2[j] == 0)
          ++j;

        if (j < mlen) {
          for (mag1[j] &= ~-mag2[j]; ++j < mlen;)
            mag1[j] &= mag2[j]; // ~~mask.dig[j]
        }

        len1 = mlen;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > mag1.length)
          mag1 = realloc(mag1, len1, len2 + 2);

        System.arraycopy(mag2, len1, mag1, len1, len2 - len1);
      }

      if (signum2 >= 0) {
        int j = 0;
        while (mag1[j] == 0)
          ++j;

        if (j < mlen) {
          mag1[j] = -(-mag1[j] & ~mag2[j]);
          for (; ++j < mlen && mag1[j - 1] == 0;)
            mag1[j] = -~(mag1[j] | mag2[j]); // -(~dig[j]&~mask.dig[j])

          if (j == mlen && mag1[j - 1] == 0) {
            final int blen = Math.max(len1, len2);
            while (j < blen && mag1[j] == -1)
              mag1[j++] = 0; // mask.dig[j]==dig[j]

            if (j < blen) {
              mag1[j] = -~mag1[j];
            }
            else {
              if (blen >= mag1.length)
                mag1 = realloc(mag1, len1, blen + 2);

              mag1[blen] = 1;
              // len = blen + 1; // Handled by caller, left for reference
              return mag1;
            }

            ++j;
          }

          for (; j < mlen; ++j)
            mag1[j] |= mag2[j]; // ~(~dig[j]&~mask.dig[j]);

          if (len2 > len1)
            len1 = len2;
        }
      }
      else {
        int a = mag1[0], b = mag2[0], j = 1;
        for (; j < mlen && (a | b) == 0; a = mag1[j], b = mag2[j], ++j);
        if (a != 0 && b == 0) {
          mag1[j - 1] = -a;
          for (; j < len2 && mag2[j] == 0; ++j)
            mag1[j] ^= -1;

          if (j < len1)
            mag1[j] = ~(mag1[j] | -mag2[j]); // ~dig[j]&~-mask.dig[j]);
          else
            mag1[j] = ~-mag1[j]; // dig[j]==mask.dig[j]

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          for (; j < mlen && mag1[j] == 0; ++j);
          if (j < mlen)
            mag1[j] = -mag1[j] & mag2[j]; // ~~mask.dig[j]

          ++j;
        }
        else {
          mag1[j - 1] = -a & ~-b;
        }

        for (; j < mlen; ++j)
          mag1[j] = ~mag1[j] & mag2[j];

        len1 = len2;
        // signum = 1; // Handled by caller, left for reference
      }
    }

    clear(mag1, len1);
    return mag1;
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public static int[] not(int[] mag, int len, final int signum) {
    if (signum >= 0) {
      final int i = uaddMag(mag, len, 1);
      if (i == len) {
        if (len == mag.length)
          mag = realloc(mag, len);

        mag[len++] = 1;
      }
    }
    else {
      len = usubMag(mag, len, 1);
    }

    return mag;
  }
}