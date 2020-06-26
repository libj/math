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
  public static boolean testBit(final int[] val, final int bit) {
    final int signum = val[1];
    final int len = val[0];

    final int bitBig = (bit >>> 5) + 2;
    final int bitSmall = bit & 31;
    if (bitBig >= len)
      return signum < 0;

    if (signum > 0)
      return (val[bitBig] & 1 << bitSmall) != 0;

    int j = 2;
    for (; j <= bitBig && val[j] == 0; ++j);
    if (j > bitBig)
      return false;

    if (j < bitBig)
      return (val[bitBig] & 1 << bitSmall) == 0;

    j = -val[bitBig];
    return (j & 1 << bitSmall) != 0;
  }

  /**
   * Shifts this number right by the given amount (less than 32).
   *
   * @param shiftSmall The amount to shift.
   * @complexity O(n)
   */
  static void smallShiftRight(final int[] val, final int shiftSmall) {
    int len = val[0];
    for (int next = val[2], i = 2; i < len - 1; ++i)
      val[i] = next >>> shiftSmall | (next = val[i + 1]) << 32 - shiftSmall;

    if ((val[len - 1] >>>= shiftSmall) == 0 && len > 3)
      val[0] = --len;
  }

  /**
   * Shifts this number right by 32*shift, i.e. moves each digit shift positions
   * to the right.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  static void bigShiftRight(final int[] val, int shift) {
    System.arraycopy(val, shift + 2, val, 2, (val[0] -= shift) - 1);
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

    int signum = val[1];
    final int shiftBig = shift >>> 5;
    // Special case: entire contents shifted off the end
    if (shiftBig + 2 >= val[0])
      return signum >= 0 ? setToZero(val) : uassign(val, signum, 1);

    final int shiftSmall = shift & 31;
    boolean oneLost = false;
    if (signum < 0) {
      // Find out whether any one-bits will be shifted off the end
      final int j = 2 + shiftBig;
      for (int i = 2; i < j && !(oneLost = val[i] != 0); ++i);
      if (!oneLost && shiftSmall != 0)
        oneLost = val[j] << (32 - shiftSmall) != 0;
    }

    if (shiftBig > 0)
      bigShiftRight(val, shiftBig);

    if (shiftSmall > 0)
      smallShiftRight(val, shiftSmall);

    // FIXME: What if an overflow happens? Look at Integer#javaIncrement(int[])
    if (oneLost)
      ++val[2];

    if (isZero(val))
      val[1] = 0;

    return val;
  }

  /**
   * Shifts this number left by 32*shift, i.e. moves each digit shift positions
   * to the left.
   *
   * @param shift The number of positions to move each digit.
   * @complexity O(n)
   */
  static int[] bigShiftLeft(int[] val, int shift) {
    final int len = val[0] - 2;
    shift += 2;
    final int newLen = len + shift;
    if (newLen > val.length) {
      final int[] tmp = new int[newLen];
      System.arraycopy(val, 2, tmp, shift, len);
      tmp[1] = val[1];
      val = tmp;
    }
    else {
      System.arraycopy(val, 2, val, shift, len);
      for (int i = 2; i < shift; ++i)
        val[i] = 0;
    }

    val[0] = newLen;
    return val;
  }

  /**
   * Shifts this number left by the given amount (less than 32) starting at the
   * given digit, i.e. the first (<len) digits are left untouched.
   *
   * @param shift The amount to shift.
   * @param fromIndex The digit to start shifting from.
   * @complexity O(n)
   */
  static int[] smallShiftLeft(int[] val, final int shift, final int fromIndex) {
    int len = val[0];
    if ((val[len - 1] << shift >>> shift) != val[len - 1]) { // Overflow?
      if (++len > val.length)
        val = realloc(val, len + 1);
      else
        val[len - 1] = 0;

      val[0] = len;
    }

    int next = len > val.length ? 0 : val[len - 1];
    for (int i = len - 1; i > fromIndex; --i)
      val[i] = next << shift | (next = val[i - 1]) >>> 32 - shift;

    val[fromIndex] = next << shift;
    return val;
  }

  public static int[] shiftLeft(int[] val, final int shift) {
    if (shift < 0)
      return shiftRight(val, -shift);

    if (shift == 0 || isZero(val))
      return val;

    final int shiftBig = shift >>> 5;
    if (shiftBig > 0)
      val = bigShiftLeft(val, shiftBig);

    final int shiftSmall = shift & 31;
    if (shiftSmall > 0)
      val = smallShiftLeft(val, shiftSmall, shiftBig + 2);

    return val;
  }

  public static int[] setBit(int[] val, final int bit) {
    final int signum = val[1];
    int len = val[0];

    final int bitBig = (bit >>> 5) + 2;
    final int bitSmall = bit & 31;
    if (signum >= 0) {
      if (bitBig >= val.length) {
        val = realloc(val, bitBig + 1);
        val[0] = bitBig + 1;
      }
      else if (bitBig >= len) {
        for (; len <= bitBig; ++len)
          val[len] = 0;

        val[0] = bitBig + 1;
      }

      val[bitBig] |= 1 << bitSmall;
    }
    else {
      if (bitBig >= len)
        return val;

      int j = 2;
      for (; j <= bitBig && val[j] == 0;)
        ++j;

      if (j > bitBig) {
        val[bitBig] = -1 << bitSmall;
        for (; val[j] == 0; ++j)
          val[j] = -1;

        val[j] = ~-val[j];
        if (j == len - 1 && val[len - 1] == 0)
          val[0] = len;
      }
      else if (j < bitBig) {
        val[bitBig] &= ~(1 << bitSmall);
      }
      else {
        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << bitSmall;
        if (k - j > 0) {
          val[bitBig] &= ~k; // Unsigned compare.
        }
        else {
          val[bitBig] ^= ((j << 1) - 1) ^ (k - 1);
          val[bitBig] |= k;
        }
      }
    }

    if (signum == 0)
      val[1] = 1;

    return val;
  }

  public static int[] clearBit(int[] val, final int bit) {
    final int signum = val[1];
    int len = val[0];

    final int bitBig = (bit >>> 5) + 2;
    final int bitSmall = bit & 31;
    if (signum >= 0) {
      if (bitBig < len) {
        val[bitBig] &= ~(1 << bitSmall);
      }
    }
    else {
      if (bitBig >= val.length) {
        val = realloc(val, bitBig + 1);
        val[0] = bitBig + 1;
        val[bitBig] |= 1 << bitSmall;
      }
      else if (bitBig >= len) {
        for (; len <= bitBig; ++len)
          val[len] = 0;

        val[bitBig] |= 1 << bitSmall;
        val[0] = bitBig + 1;
      }
      else {
        int j = 2;
        for (; j <= bitBig && val[j] == 0;)
          ++j;

        if (j > bitBig)
          return val;

        if (j < bitBig) {
          val[bitBig] |= 1 << bitSmall;
          return val;
        }

        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << bitSmall;
        if (j - k > 0)
          return val; // Unsigned compare

        if (j - k < 0) {
          val[bitBig] |= k;
          return val;
        }

        j = val[bitBig];
        if (j == (-1 ^ k - 1)) {
          val[bitBig] = 0;
          for (j = bitBig + 1; j < len && val[j] == -1; ++j)
            val[j] = 0;

          if (j == val.length)
            val = realloc(val, j + 2);

          if (j == len) {
            val[len] = 1;
            val[0] = len + 1;
          }
          else {
            val[j] = -~val[j];
          }
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          val[bitBig] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    // FIXME: This was hacked together...
    while (val[--len] == 0);
    val[0] = Math.max(3, len + 1);
    if (isZero(val))
      val[1] = 0;

    return val;
  }

  public static int[] flipBit(int[] val, final int bit) {
    final int signum = val[1];
    int len = val[0];

    final int bigBit = (bit >>> 5) + 2;
    final int smallBit = bit & 31;
    block:
    if (bigBit >= val.length) {
      val = realloc(val, bigBit + 1);
      val[0] = bigBit + 1;
      val[bigBit] ^= 1 << smallBit;
    }
    else if (bigBit >= len) {
      for (; len <= bigBit; ++len)
        val[len] = 0;

      val[bigBit] ^= 1 << smallBit;
      val[0] = bigBit + 1;
    }
    else if (signum >= 0) {
      val[bigBit] ^= 1 << smallBit;
    }
    else {
      int j = 2;
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
        if (j == len - 1 && val[len - 1] == 0)
          val[0] = len;
      }
      else {
        j = Integer.lowestOneBit(val[j]); // more efficient than numberOfTrailingZeros
        final int k = 1 << smallBit;
        if (j - k > 0) {
          val[bigBit] ^= ((j << 1) - 1) ^ (k - 1);
          return val;
        }

        if (j - k < 0) {
          val[bigBit] ^= k;
          return val;
        }

        j = val[bigBit];
        if (j == (-1 ^ k - 1)) { // TODO: Refactor with clearBit?
          val[bigBit] = 0;
          for (j = bigBit + 1; j < len && val[j] == -1; ++j)
            val[j] = 0;

          if (j == val.length)
            val = realloc(val, j + 2);

          if (j == len) {
            val[len] = 1;
            val[0] = ++len;
          }
          else {
            val[j] = -~val[j];
          }
        }
        else {
          j = Integer.lowestOneBit(j ^ (-1 ^ k - 1));
          val[bigBit] ^= j | (j - 1) ^ (k - 1);
        }
      }
    }

    // FIXME: This was hacked together...
    while (val[--len] == 0);
    val[0] = Math.max(3, len + 1);

    // FIXME: Eek!!!...
    if (isZero(val))
      val[1] = 0;
    else if (val[1] == 0 && val[0] > 3 || val[2] != 0)
      val[1] = signum != 0 ? signum : 1;

    return val;
  }

  /**
   * Bitwise-ands this number with the given number, i.e. this &= mask.
   *
   * @param mask The number to bitwise-and with.
   * @complexity O(n)
   */
  public static int[] and(int[] val1, final int[] val2) {
    final int signum1 = val1[1];
    int len1 = val1[0];
    final int signum2 = val2[1];
    final int len2 = val2[0];

    final int[] x = new int[val1.length - 2];
    System.arraycopy(val1, 2, x, 0, x.length);
    final int[] y = new int[val2.length - 2];
    System.arraycopy(val2, 2, y, 0, y.length);


    try {
      if (signum2 == 0 || isZero(val2)) { // Defensive check
        setToZero(val1);
        return val1;
      }

      if (signum1 >= 0) {
        if (signum2 > 0) {
          if (len2 < len1)
            len1 = val1[0] = len2;

          boolean ok = true;
          for (int i = len1 - 1; i >= 2; --i) {
            if ((val1[i] &= val2[i]) == 0) {
              if (ok)
                --len1;
            }
            else {
              ok = false;
            }
          }

          val1[0] = Math.max(3, len1);
        }
        else {
          final int mlen = Math.min(len1, len2);
          int a = val1[2], b = val2[2], j = 3;
          for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);
          if (a != 0 && b == 0) {
            for (val1[j - 1] = 0; j < mlen && val2[j] == 0; ++j)
              val1[j] = 0;

            if (j < mlen)
              val1[j] &= -val2[j];
            else if (j == len1)
              val1[0] = len1 = 3;

            // FIXME: This was hacked together...
            if (len1 > 3 && val1[len1 - 1] == 0)
              val1[0] = len1 - 1;

            ++j;
          }
          else if (a == 0) { // && (b!=0 || j==mlen)
            while (j < mlen && val1[j] == 0)
              ++j;
          }
          else {
            val1[j - 1] &= -b;
          }

          for (; j < mlen; ++j)
            val1[j] &= ~val2[j];
        }
      }
      else {
        final int mlen = Math.min(len1, len2);
        if (signum2 > 0) {
          int a = val1[2], b = val2[2], j = 3;
          for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);

          if (a != 0 && b == 0) {
            for (val1[j - 1] = 0; j < mlen && val2[j] == 0; ++j)
              val1[j] = 0;
          }
          else if (a == 0) { // && (b!=0 || j==mlen)
            while (j < mlen && val1[j] == 0)
              ++j;

            if (j < mlen)
              val1[j] = -val1[j] & val2[j];

            ++j;
          }
          else {
            val1[j - 1] = -a & b;
          }

          for (; j < mlen; ++j)
            val1[j] = ~val1[j] & val2[j];

          if (len2 > len1) {
            if (len2 > val1.length)
              val1 = realloc(val1, len2 + 2);

            System.arraycopy(val2, len1, val1, len1, len2 - len1);
          }

          // FIXME: This was hacked together...
          val1[0] = len2 > 3 && val1[len2 - 1] == 0 ? len2 - 1 : len2;
          val1[1] = 1;
        }
        else {
          if (len2 > len1) {
            if (len2 > val1.length)
              val1 = realloc(val1, len2 + 2);

            System.arraycopy(val2, len1, val1, len1, len2 - len1);
          }

          int a = val1[2], b = val2[2], j = 3;
          for (; (a | b) == 0; a = val1[j], b = val2[j], ++j);

          if (a != 0 && b == 0) {
            for (val1[j - 1] = 0; j < mlen && val2[j] == 0; ++j)
              val1[j] = 0;

            if (j < mlen)
              val1[j] = -(~val1[j] & -val2[j]);

            ++j;
          }
          else if (a == 0) { // && (b!=0 || j==mlen)
            while (j < mlen && val1[j] == 0)
              ++j;

            if (j < mlen)
              val1[j] = -(-val1[j] & ~val2[j]);

            ++j;
          }
          else {
            val1[j - 1] = -(-a & -b);
          }

          if (j <= mlen && val1[j - 1] == 0) {
            if (j < mlen)
              for (val1[j] = -~(val1[j] | val2[j]); ++j < mlen && val1[j - 1] == 0;)
                val1[j] = -~(val1[j] | val2[j]); // -(~dig[j]&~mask.dig[j])

            if (j == mlen && val1[j - 1] == 0) {
              final int blen = Math.max(len1, len2);
              while (j < blen && val1[j] == -1)
                val1[j++] = 0; // mask.dig[j]==dig[j]

              if (j < blen) {
                val1[j] = -~val1[j];
              }
              else {
                if (blen >= val1.length)
                  val1 = realloc(val1, blen + 2);

                val1[blen] = 1;
                val1[0] = blen + 1;
                return val1;
              }

              ++j;
            }
          }

          for (; j < mlen; ++j)
            val1[j] |= val2[j]; // ~(~dig[j]&~mask.dig[j]);

          if (len2 > len1)
            val1[0] = len2;
        }
      }

      // FIXME: This was hacked together...
      int len = val1[0];
      while (val1[--len] == 0);
      val1[0] = Math.max(3, len + 1);

      if (isZero(val1))
        val1[1] = 0;

      return val1;
    }
    finally {
      final int[] z = and(x, len1 - 2, signum1, y, len2 - 2, signum2);
    }
  }

  public static int[] and(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum2 == 0 || (len2 == 1 && mag2[0] == 0)) { // Defensive check
      mag1[0] = 0;
      len1 = 1;
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
  public static int[] or(int[] val1, final int[] val2) {
    final int signum1 = val1[1];
    final int len1 = val1[0];
    final int signum2 = val2[1];
    final int len2 = val2[0];

    if (signum1 == 0 || isZero(val1)) // Defensive
      return val2;

    if (signum1 >= 0) {
      if (signum2 >= 0) {
        if (len2 > len1) {
          if (len2 > val1.length)
            val1 = realloc(val1, len2 + 1);

          System.arraycopy(val2, len1, val1, len1, len2 - len1);
          for (int i = 2; i < len1; ++i)
            val1[i] |= val2[i];

          val1[0] = len2;
        }
        else {
          for (int i = 2; i < len2; ++i)
            val1[i] |= val2[i];
        }
      }
      else {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 1);

        if (len2 > len1)
          System.arraycopy(val2, len1, val1, len1, len2 - len1);

        final int mLen = Math.min(len2, len1);
        int a = val1[2], b = val2[2], j = 3;
        for (; (a | b) == 0 && j < mLen; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          val1[j - 1] = -a;
          for (; val2[j] == 0; ++j)
            val1[j] ^= -1;

          if (j < mLen)
            val1[j] = ~(val1[j] | -val2[j]);
          else // mask.dig[j] == dig[j]
            val1[j] = ~-val1[j];

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val1[j - 1] = b; j < mLen && val1[j] == 0; ++j)
            val1[j] = val2[j];
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(a | -b);
        }

        for (; j < mLen; ++j)
          val1[j] = ~val1[j] & val2[j]; // ~(dig[j]|~mask.dig[j])

        val1[1] = -1;
        val1[0] = len2;
      }
    }
    else {
      final int mLen = Math.min(len2, len1);

      int a = val1[2], b = val2[2], j = 3;
      for (; (a | b) == 0 && j < mLen; a = val1[j], b = val2[j], ++j);

      if (signum2 > 0) {
        if (a != 0 && b == 0) {
          for (; j < mLen && val2[j] == 0; ++j);
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          val1[j - 1] = -b;
          for (; j < mLen && val1[j] == 0; ++j)
            val1[j] = ~val2[j];

          if (j < mLen) {
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

        for (; j < mLen; ++j)
          val1[j] &= ~val2[j]; // ~(~dig[j]|mask.dig[j])
      }
      else {
        if (a != 0 && b == 0) {
          for (; j < mLen && val2[j] == 0; ++j);
          if (j < mLen)
            val1[j] = ~(~val1[j] | -val2[j]);

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mLen)
          for (val1[j - 1] = b; j < mLen && val1[j] == 0; ++j)
            val1[j] = val2[j];

          if (j < mLen)
            val1[j] = ~(-val1[j] | ~val2[j]);

          ++j;
        }
        else { // a!=0 && b!=0
          val1[j - 1] = -(-a | -b);
        }

        for (; j < mLen; ++j)
          val1[j] &= val2[j]; // ~(~dig[j]|~mask.dig[j])

        val1[0] = mLen;
      }
    }

    // FIXME: This was hacked together...
    int len = val1[0];
    while (val1[--len] == 0);
    val1[0] = Math.max(3, len + 1);

    return val1;
  }

  /**
   * Bitwise-xors this number with the given number, i.e. this ^= mask.
   *
   * @param mask The number to bitwise-xor with.
   * @complexity O(n)
   */
  public static int[] xor(int[] val1, final int[] val2) {
    final int signum1 = val1[1];
    final int len1 = val1[0];
    final int signum2 = val2[1];
    final int len2 = val2[0];

    if (signum1 >= 0) {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 2);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (signum2 >= 0) {
        for (int i = 2; i < mlen; ++i)
          val1[i] ^= val2[i];
      }
      else {
        int a = val1[2], b = val2[2], j = 3;
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
              val1[0] = blen + 1; // FIXME: In the for loop??!
            }
            else {
              val1[j] = -~tmp[j];
            }

            ++j;
          }
        }

        for (; j < mlen; ++j)
          val1[j] ^= val2[j]; // ~(dig[j]^~mask.dig[j]);

        val1[1] = -1;
      }

      if (len2 > len1)
        val1[0] = len2;
    }
    else {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 2);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      final int mlen = Math.min(len1, len2);
      if (signum2 >= 0) {
        int a = val1[2], b = val2[2], j = 3;
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

        for (; j < mlen; ++j)
          val1[j] ^= val2[j]; // ~(~dig[j]^mask.dig[j]);
      }
      else {
        int a = val1[2], b = val2[2], j = 3;
        for (; (a | b) == 0 && j < mlen; a = val1[j], b = val2[j], ++j);

        if (a != 0 && b == 0) {
          for (val1[j - 1] = -a; val2[j] == 0; ++j)
            val1[j] ^= -1; // ~dig[j]

          if (j < len1)
            val1[j] = ~val1[j] ^ -val2[j];
          else
            val1[j] = ~-val1[j]; // dig[j]==mask.dig[j], ~0^-mask.dig[j]

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
          val1[j] ^= val2[j]; // ~dig[j]^~mask.dig[j]

        val1[1] = 1;
      }

      if (len2 > len1)
        val1[0] = len2;
    }

    // FIXME: This was hacked together...
    int len = val1[0];
    while (val1[--len] == 0);
    val1[0] = Math.max(3, len + 1);

    if (isZero(val1))
      val1[1] = 0;

    return val1;
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param mask The number to bitwise-and-not with.
   * @complexity O(n)
   */
  public static int[] andNot(int[] val1, final int[] val2) {
    final int signum1 = val1[1];
    final int len1 = val1[0];
    final int signum2 = val2[1];
    final int len2 = val2[0];

    final int mlen = Math.min(len1, len2);
    if (signum1 >= 0) {
      if (signum2 >= 0) {
        for (int i = 2; i < mlen; ++i)
          val1[i] &= ~val2[i];
      }
      else {
        int j = 2;
        while (j < mlen && val2[j] == 0)
          ++j;

        if (j < mlen) {
          for (val1[j] &= ~-val2[j]; ++j < mlen;)
            val1[j] &= val2[j]; // ~~mask.dig[j]
        }

        val1[0] = mlen;
      }
    }
    else {
      if (len2 > len1) {
        if (len2 > val1.length)
          val1 = realloc(val1, len2 + 2);

        System.arraycopy(val2, len1, val1, len1, len2 - len1);
      }

      if (signum2 >= 0) {
        int j = 2;
        while (val1[j] == 0)
          ++j;

        if (j < mlen) {
          val1[j] = -(-val1[j] & ~val2[j]);
          for (; ++j < mlen && val1[j - 1] == 0;)
            val1[j] = -~(val1[j] | val2[j]); // -(~dig[j]&~mask.dig[j])

          if (j == mlen && val1[j - 1] == 0) {
            final int blen = Math.max(len1, len2);
            while (j < blen && val1[j] == -1)
              val1[j++] = 0; // mask.dig[j]==dig[j]

            if (j < blen) {
              val1[j] = -~val1[j];
            }
            else {
              if (blen >= val1.length)
                val1 = realloc(val1, blen + 2);

              val1[blen] = 1;
              val1[0] = blen + 1;
              return val1;
            }

            ++j;
          }

          for (; j < mlen; ++j)
            val1[j] |= val2[j]; // ~(~dig[j]&~mask.dig[j]);

          if (len2 > len1)
            val1[0] = len2;
        }
      }
      else {
        int a = val1[2], b = val2[2], j = 3;
        for (; j < mlen && (a | b) == 0; a = val1[j], b = val2[j], ++j);
        if (a != 0 && b == 0) {
          val1[j - 1] = -a;
          for (; j < len2 && val2[j] == 0; ++j)
            val1[j] ^= -1;

          if (j < len1)
            val1[j] = ~(val1[j] | -val2[j]); // ~dig[j]&~-mask.dig[j]);
          else
            val1[j] = ~-val1[j]; // dig[j]==mask.dig[j]

          ++j;
        }
        else if (a == 0) { // && (b!=0 || j==mlen)
          for (; j < mlen && val1[j] == 0; ++j);
          if (j < mlen)
            val1[j] = -val1[j] & val2[j]; // ~~mask.dig[j]

          ++j;
        }
        else {
          val1[j - 1] = -a & ~-b;
        }

        for (; j < mlen; ++j)
          val1[j] = ~val1[j] & val2[j];

        // FIXME: This was hacked together...
        val1[0] = len2 > 3 && val1[len2 - 1] == 0 ? len2 - 1 : len2;
        val1[1] = 1;
      }
    }

    // FIXME: This was hacked together...
    int len = val1[0];
    while (val1[--len] == 0);
    val1[0] = Math.max(3, len + 1);

    if (isZero(val1))
      val1[1] = 0;

    return val1;
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public static int[] not(int[] val) {
    final int signum = val[1];
    if (signum >= 0) {
      val = uaddMag(val, 1);
      val[1] = isZero(val) ? 0 : -1;
    }
    else {
      usubMag(val, 1);
      val[1] = isZero(val) ? 0 : 1;
    }

    return val;
  }
}