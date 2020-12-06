/* Copyright (c) 2020 Seva Safris, LibJ
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

package org.libj.math;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

abstract class BigIntegerMultiplication extends BigIntMultiplication {
  private static final long serialVersionUID = 877413432467388241L;

  /**
   * The threshold value for using 3-way Toom-Cook multiplication.
   * If the number of ints in each mag array is greater than the
   * Karatsuba threshold, and the number of ints in at least one of
   * the mag arrays is greater than this threshold, then Toom-Cook
   * multiplication will be used.
   */
  static final int TOOM_COOK_THRESHOLD = 240; // 240

  /**
   * The threshold value for using squaring code to perform multiplication
   * of a {@code BigInteger} instance by itself.  If the number of ints in
   * the number are larger than this value, {@code multiply(this)} will
   * return {@code square()}.
   */
  static final int MULTIPLY_SQUARE_THRESHOLD = 20;

  /**
   * The threshold value for using Karatsuba squaring.  If the number
   * of ints in the number are larger than this value,
   * Karatsuba squaring will be used.   This value is found
   * experimentally to work well.
   */
  static final int KARATSUBA_SQUARE_THRESHOLD = 128; // 128

  /**
   * The threshold value for using Toom-Cook squaring.  If the number
   * of ints in the number are larger than this value,
   * Toom-Cook squaring will be used.   This value is found
   * experimentally to work well.
   */
  static final int TOOM_COOK_SQUARE_THRESHOLD = 216;

  /**
   * This constant limits {@code mag.length} of BigIntegers to the supported
   * range.
   */
  static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1; // (1 << 26)

  private static final int[] ZERO = {0};

  int[] mul(int[] val, final boolean sig, final int len, final int[] mul, final int mlen, final boolean isRecursion, final int fixedLen) {
    if (mul == val && len > MULTIPLY_SQUARE_THRESHOLD)
      return square(val, 1, len, false, -1);

    if (len < TOOM_COOK_THRESHOLD && mlen < TOOM_COOK_THRESHOLD) {
      val = mulKaratsuba(mul, mlen, val, len, fixedLen > 0 ? fixedLen : len + mlen); // Swap mul and val so that multiplyKaratsuba can reuse the val array
      if (sig != val[0] >= 0)
        val[0] = -val[0];

      return val;
    }

    //
    // In "Hacker's Delight" section 2-13, p.33, it is explained
    // that if x and y are unsigned 32-bit quantities and m and n
    // are their respective numbers of leading zeros within 32 bits,
    // then the number of leading zeros within their product as a
    // 64-bit unsigned quantity is either m + n or m + n + 1. If
    // their product is not to overflow, it cannot exceed 32 bits,
    // and so the number of leading zeros of the product within 64
    // bits must be at least 32, i.e., the leftmost set bit is at
    // zero-relative position 31 or less.
    //
    // From the above there are three cases:
    //
    // m + n leftmost set bit condition
    // ----- ---------------- ---------
    // >= 32 x <= 64 - 32 = 32 no overflow
    // == 31 x >= 64 - 32 = 32 possible overflow
    // <= 30 x >= 64 - 31 = 33 definite overflow
    //
    // The "possible overflow" condition cannot be detected by
    // examining data lengths alone and requires further calculation.
    //
    // By analogy, if 'this' and 'val' have m and n as their
    // respective numbers of leading zeros within 32*MAX_MAG_LENGTH
    // bits, then:
    //
    // m + n >= 32*MAX_MAG_LENGTH no overflow
    // m + n == 32*MAX_MAG_LENGTH - 1 possible overflow
    // m + n <= 32*MAX_MAG_LENGTH - 2 definite overflow
    //
    // Note however that if the number of ints in the result
    // were to be MAX_MAG_LENGTH and mag[0] < 0, then there would
    // be overflow. As a result the leftmost bit (of mag[0]) cannot
    // be used and the constraints must be adjusted by one bit to:
    //
    // m + n > 32*MAX_MAG_LENGTH no overflow
    // m + n == 32*MAX_MAG_LENGTH possible overflow
    // m + n < 32*MAX_MAG_LENGTH definite overflow
    //
    // The foregoing leading zero-based discussion is for clarity
    // only. The actual calculations use the estimated bit length
    // of the product as this is more natural to the internal
    // array representation of the magnitude which has no leading
    // zero elements.
    //
    if (!isRecursion) {
      // The bitLength() instance method is not used here as we
      // are only considering the magnitudes as non-negative. The
      // Toom-Cook multiplication algorithm determines the sign
      // at its end from the two signum values.
      if (bitLength(val, len) + bitLength(mul, mlen) > 32L * MAX_MAG_LENGTH)
        throw new ArithmeticException("BigInteger would overflow supported range");
    }

    val = mulToomCook3(val, len, mul, mlen, OFF, fixedLen);
    return val;
  }

  /**
   * Multiplies two BigIntegers using the Karatsuba multiplication algorithm.
   * This is a recursive divide-and-conquer algorithm which is more efficient
   * for large numbers than what is commonly called the "grade-school" algorithm
   * used in multiplyToLen. If the numbers to be multiplied have length n, the
   * "grade-school" algorithm has an asymptotic complexity of O(n^2). In
   * contrast, the Karatsuba algorithm has complexity of O(n^(log2(3))), or
   * O(n^1.585). It achieves this increased performance by doing 3 multiplies
   * instead of 4 when evaluating the product. As it has some overhead, should
   * be used when both numbers are larger than a certain threshold (found
   * experimentally). See: http://en.wikipedia.org/wiki/Karatsuba_algorithm
   */
  private static int[] mulKaratsuba(final int[] x, final int xlen, final int[] y, final int ylen, int zlen) {
    zlen += OFF;
    // The number of ints in each half of the number.
    final int half = (Math.max(xlen, ylen) + 1) / 2;

    // xl and yl are the lower halves of x and y respectively,
    // xh and yh are the upper halves.
    int yUpperLen = ylen - half;
    final boolean yIsSmall = yUpperLen <= 0;
    if (yIsSmall)
      yUpperLen = half;

    int[] z;
    int[] xh;
    int xUpperLen = xlen - half;
    if (xUpperLen <= 0) {
      xUpperLen = half;
      xh = new int[xlen + OFF]; // This length is exact
      z = new int[zlen]; // This length is exact
    }
    else {
      xh = getUpper0(x, xUpperLen, yUpperLen, half);
      z = copyInPlace(xh, xUpperLen + OFF, new int[zlen]); // This length is exact
    }

    int[] yh = yIsSmall ? new int[half + OFF] : getUpper0(y, yUpperLen, xUpperLen, half);
    int[] xl = getLower(x, xlen, half, false, -1);
    final int[] yl = getLower(y, ylen, half, true, -1);

    z = mul(z, yh); // , zlen)

    xh = add(xh, xl);
    yh = add(yh, yl);

    xh = mul(xh, yh); // , xlen + yUpperLen + OFF)
    xl = mul(xl, yl); // , half + half + OFF)

    // result = p1 * 2^(32*2*half) + (p3 - p1 - p2) * 2^(32*half) + p2
    xh = sub(xh, z);
    xh = sub(xh, xl);

    z = shiftLeft(z, 32 * half);
    z = add(z, xh);
    z = shiftLeft(z, 32 * half);
    z = add(z, xl);

    // _debugLenSig(z);
    return z;
  }

  /**
   * Multiplies two BigIntegers using a 3-way Toom-Cook multiplication
   * algorithm. This is a recursive divide-and-conquer algorithm which is more
   * efficient for large numbers than what is commonly called the "grade-school"
   * algorithm used in multiplyToLen. If the numbers to be multiplied have
   * length n, the "grade-school" algorithm has an asymptotic complexity of
   * O(n^2). In contrast, 3-way Toom-Cook has a complexity of about O(n^1.465).
   * It achieves this increased asymptotic performance by breaking each number
   * into three parts and by doing 5 multiplies instead of 9 when evaluating the
   * product. Due to overhead (additions, shifts, and one division) in the
   * Toom-Cook algorithm, it should only be used when both numbers are larger
   * than a certain threshold (found experimentally). This threshold is
   * generally larger than that for Karatsuba multiplication, so this algorithm
   * is generally only used when numbers become significantly larger. The
   * algorithm used is the "optimal" 3-way Toom-Cook algorithm outlined by Marco
   * Bodrato. See: http://bodrato.it/toom-cook/
   * http://bodrato.it/papers/#WAIFI2007 "Towards Optimal Toom-Cook
   * Multiplication for Univariate and Multivariate Polynomials in
   * Characteristic 2 and 0." by Marco BODRATO; In C.Carlet and B.Sunar, Eds.,
   * "WAIFI'07 proceedings", p. 116-133, LNCS #4547. Springer, Madrid, Spain,
   * June 21-22, 2007.
   */
  private static int[] mulToomCook3(final int[] x, final int xlen, final int[] y, final int ylen, final int off, final int fixedLen) {
    final int largest = Math.max(xlen, ylen);

    // k is the size (in ints) of the lower-order slices.
    final int k = (largest + 2) / 3; // Equal to ceil(largest/3)

    // r is the size (in ints) of the highest-order slice.
    final int r = largest - 2 * k;

    // Obtain slices of the numbers. a2 and b2 are the most significant
    // bits of the numbers a and b, and a0 and b0 the least significant.
    int[] a2 = getToomSlice(x, 1, xlen, k, r, 0, largest, xlen);
    int[] a1 = getToomSlice(x, 1, xlen, k, r, 1, largest, 0);
    int[] a0 = getToomSlice(x, 1, xlen, k, r, 2, largest, 0);
    int[] b2 = getToomSlice(y, 1, ylen, k, r, 0, largest, ylen);
    int[] b1 = getToomSlice(y, 1, ylen, k, r, 1, largest, 0);
    int[] b0 = getToomSlice(y, 1, ylen, k, r, 2, largest, 0);

    int[] da1 = add(a2.clone(), a0);
    int[] db1 = add(b2.clone(), b0);
    final int[] vm1 = mul(sub(da1.clone(), a1), sub(db1.clone(), b1)); // , 0)
    da1 = add(da1, a1);
    db1 = add(db1, b1);
    int[] v1 = mul(da1.clone(), db1); // , 0)
    da1 = mul(sub(shiftLeft(add(da1, a2), 1), a0), sub(shiftLeft(add(db1, b2), 1), b0)); // , 0)

    a2 = mul(a2, b2); // , fixedLen > 0 ? fixedLen : a2.length)
    a0 = mul(a0, b0); // , 0)

    // The algorithm requires two divisions by 2 and one by 3.
    // All divisions are known to be exact, that is, they do not produce
    // remainders, and all results are positive. The divisions by 2 are
    // implemented as right shifts which are relatively efficient, leaving
    // only an exact division by 3, which is done by a specialized
    // linear-time algorithm.
    int[] t1 = sub(v1.clone(), a0);
    da1 = exactDivBy3(sub(da1, vm1), off);
    v1 = shiftRight(sub(v1, vm1), 1);
    da1 = shiftRight(sub(da1, t1), 1);
    t1 = sub(sub(t1, v1), a2);

    final int[] vinf = new int[fixedLen > 0 ? fixedLen : a2.length + k + k + k];
    System.arraycopy(a2, 0, vinf, 0, Math.abs(a2[0]) + 1);

    da1 = sub(da1, shiftLeft(a2, 1));
    v1 = sub(v1, da1);

    // Number of bits to shift left.
    final int ss = k * 32;

    final int[] result = add(shiftLeft(add(shiftLeft(add(shiftLeft(add(shiftLeft(vinf, ss), da1), ss), t1), ss), v1), ss), a0);
    // _debugLenSig(result);
    return result;
  }

  /**
   * Returns a slice of a BigInteger for use in Toom-Cook multiplication.
   *
   * @param lowerSize The size of the lower-order bit slices.
   * @param upperSize The size of the higher-order bit slices.
   * @param slice The index of which slice is requested, which must be a number
   *          from 0 to size-1. Slice 0 is the highest-order bits, and slice
   *          size-1 are the lowest-order bits. Slice 0 may be of different size
   *          than the other slices.
   * @param fullsize The size of the larger integer array, used to align slices
   *          to the appropriate position when multiplying different-sized
   *          numbers.
   */
  private static int[] getToomSlice(final int[] mag, final int off, final int len, int lowerSize, int upperSize, int slice, int fullsize, final int addSize) {
    int start, sliceSize;
    final int end;
    final int offset = fullsize - len;

    if (slice == 0) {
      start = 0 - offset;
      end = upperSize - 1 - offset;
    }
    else {
      start = upperSize + (slice - 1) * lowerSize - offset;
      end = start + lowerSize - 1;
    }

    if (start < 0)
      start = 0;

    if (end < 0)
      return new int[lowerSize + addSize];

    sliceSize = (end - start) + 1;
    if (sliceSize <= 0)
      return ZERO;

    // While performing Toom-Cook, all slices are positive and
    // the sign is adjusted when the final number is composed.
    if (start == off && sliceSize >= len)
      return abs(mag.clone()); // FIXME: Do we need to clone?

    final int[] intSlice = new int[off + sliceSize + addSize];
    System.arraycopy(mag, len - start + 1 - sliceSize, intSlice, 1, sliceSize);
    for (; intSlice[sliceSize] == 0; --sliceSize);
    intSlice[0] = sliceSize;
    // _debugLenSig(intSlice);
    return intSlice;
  }

  /**
   * Does an exact division (that is, the remainder is known to be zero) of the
   * specified number by 3. This is used in Toom-Cook multiplication. This is an
   * efficient algorithm that runs in linear time. If the argument is not
   * exactly divisible by 3, results are undefined. Note that this is expected
   * to be called with positive arguments only.
   */
  private static int[] exactDivBy3(final int[] val, final int off) {
    int len = val[0];
    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    len += off;

    long x, w, borrow = 0;
    for (int i = off; i < len; ++i) {
      x = val[i] & LONG_MASK;
      w = x - borrow;
      if (borrow > x) // Did we make the number go negative?
        borrow = 1;
      else
        borrow = 0;

      // 0xAAAAAAAB is the modular inverse of 3 (mod 2^32). Thus,
      // the effect of this is to divide by 3 (mod 2^32).
      // This is much faster than division on most architectures.
      x = (w * 0xAAAAAAABL) & LONG_MASK;
      val[i] = (int)x;

      // Now check the borrow. The second check can of course be
      // eliminated if the first fails.
      if (x >= 0x55555556L) {
        ++borrow;
        if (x >= 0xAAAAAAABL)
          ++borrow;
      }
    }

    len -= off;
    for (; val[len] == 0; --len);
    val[0] = sig ? len : -len;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Returns a new BigInteger representing n lower ints of the number. This is
   * used by Karatsuba multiplication and Karatsuba squaring.
   */
  private static int[] getLower(final int[] val, int len, int n, final boolean reuse, int fixedLen) {
    if (len <= n)
      return abs(reuse && fixedLen <= val.length ? val : val.clone());

    // First trim the length
    for (; val[n] == 0; --n);

    final int[] lowerInts;
    if (reuse && fixedLen <= val.length) {
      lowerInts = val;
    }
    else {
      if (fixedLen < 0)
        fixedLen = n + OFF;

      lowerInts = new int[fixedLen];
      System.arraycopy(val, 1, lowerInts, 1, n);
    }

    lowerInts[0] = n;
    // _debugLenSig(lowerInts);
    return lowerInts;
  }

  /**
   * Returns a new BigInteger representing mag.length-n upper ints of the
   * number. This is used by Karatsuba multiplication and Karatsuba squaring.
   * Pass both lengths to make an array that will be sufficiently sized for
   * operations to follow.
   * <p>
   * Note: upperLen2 is not checked if it's less than 0!
   */
  private static int[] getUpper(final int[] val, final int upperLen, final int upperLen2, final int n) {
    if (upperLen <= 0)
      return new int[n + upperLen2 + OFF];

    return getUpper0(val, upperLen, upperLen2, n);
  }

  private static int[] getUpper0(final int[] val, final int upperLen, int upperLen2, final int n) {
    final int[] upperInts = new int[upperLen + upperLen2 + OFF];
    System.arraycopy(val, n + OFF, upperInts, OFF, upperLen);
    upperInts[0] = upperLen;
    // _debugLenSig(upperInts);
    return upperInts;
  }

  /**
   * Returns a BigInteger whose value is {@code (this<sup>2</sup>)}. If the
   * invocation is recursive certain overflow checks are skipped.
   *
   * @param isRecursion whether this is a recursive invocation
   * @return {@code this<sup>2</sup>}
   */
  private static int[] square(final int[] val, final boolean isRecursion, final int fixedLen) {
    return square(val, 1, Math.abs(val[0]), isRecursion, fixedLen);
  }

  private static int[] square(final int[] mag, final int off, final int len, final boolean isRecursion, final int fixedLen) {
    if (len == 0)
      return mag;

    if (len < KARATSUBA_SQUARE_THRESHOLD)
      return squareToLen(mag, off, len, fixedLen);

    if (len < TOOM_COOK_SQUARE_THRESHOLD)
      return squareKaratsuba(mag, len, fixedLen);

    // For a discussion of overflow detection see multiply()
    if (!isRecursion && bitLength(mag, len) > 16L * MAX_MAG_LENGTH)
      throw new ArithmeticException("BigInteger would overflow supported range");

    return squareToomCook3(mag, OFF);
  }

  /**
   * Squares a BigInteger using the Karatsuba squaring algorithm. It should be
   * used when both numbers are larger than a certain threshold (found
   * experimentally). It is a recursive divide-and-conquer algorithm that has
   * better asymptotic performance than the algorithm used in
   * {@link #squareToLen(int[],int,int,int)}.
   */
  private static int[] squareKaratsuba(final int[] val, final int len, final int fixedLen) {
    final int half = (len + 1) / 2;

    final int upperLen = len - half;
    final int[] xh = getUpper(val, upperLen, 1, half);
    int[] xl = getLower(val, len, half, false, half + 2);

    int[] xhs = square(xh, 1, half, false, fixedLen < 0 ? (len * 2) + 2 : fixedLen);
    int[] xhs2 = xhs.clone();
    final int[] xls = square(xl, 1, half, false, -1);

    xl = add(xl, xh);
    xl = square(xl, false, -1);
    xhs2 = add(xhs2, xls);
    xl = sub(xl, xhs2);

    xhs = shiftLeft(xhs, half * 32);
    xhs = add(xhs, xl);
    xhs = shiftLeft(xhs, half * 32);
    xhs = add(xhs, xls);

    // _debugLenSig(xhs);
    return xhs;
  }

  /**
   * Squares a BigInteger using the 3-way Toom-Cook squaring algorithm. It
   * should be used when both numbers are larger than a certain threshold (found
   * experimentally). It is a recursive divide-and-conquer algorithm that has
   * better asymptotic performance than the algorithm used in squareToLen or
   * squareKaratsuba.
   */
  private static int[] squareToomCook3(final int[] val, final int off) {
    final int len = Math.abs(val[0]);

    // k is the size (in ints) of the lower-order slices.
    final int k = (len + 2) / 3; // Equal to ceil(largest/3)

    // r is the size (in ints) of the highest-order slice.
    final int r = len - 2 * k;

    // Obtain slices of the numbers. a2 is the most significant
    // bits of the number, and a0 the least significant.
    int[] a2 = getToomSlice(val, 1, len, k, r, 0, len, 3);
    final int[] a1 = getToomSlice(val, 1, len, k, r, 1, len, 0);
    int[] a0 = getToomSlice(val, 1, len, k, r, 2, len, 0);

    int[] da1 = add(a2.clone(), a0);
    final int[] vm1 = square(sub(da1.clone(), a1), true, -1);
    da1 = add(da1, a1);
    int[] v1 = square(da1, true, -1);
    int[] v2 = square(sub(shiftLeft(add(da1, a2), 1), a0), true, -1);

    a0 = square(a0, true, -1);
    a2 = square(a2, true, -1);

    // The algorithm requires two divisions by 2 and one by 3.
    // All divisions are known to be exact, that is, they do not produce
    // remainders, and all results are positive. The divisions by 2 are
    // implemented as right shifts which are relatively efficient, leaving
    // only a division by 3.
    // The division by 3 is done by an optimized algorithm for this case.
    v2 = exactDivBy3(sub(v2, vm1), off);
    int[] tm1 = shiftRight(sub(v1.clone(), vm1), 1);
    v1 = sub(v1, a0);
    v2 = shiftRight(sub(v2, v1), 1);
    v1 = sub(sub(v1, tm1), a2);
    v2 = sub(v2, shiftLeft(a2.clone(), 1));
    tm1 = sub(tm1, v2);

    // Number of bits to shift left.
    final int ss = k * 32;
    return add(shiftLeft(add(shiftLeft(add(shiftLeft(add(shiftLeft(a2, ss), v2), ss), v1), ss), tm1), ss), a0);
  }

  /**
   * Squares the contents of the int array x. The result is placed into the int
   * array z. The contents of x are not changed.
   */
  private static final int[] squareToLen(final int[] mag, final int off, final int len, final int fixedLen) {
    int zlen = len * 2;
    final int[] z = new int[fixedLen < 0 ? zlen + off : fixedLen];
    squareToLen0(mag, len, z, zlen, off);
    for (; z[zlen] == 0; --zlen);
    z[0] = zlen;
    // _debugLenSig(z);
    return z;
  }

  /**
   * The algorithm used here is adapted from Colin Plumb's C library. Technique:
   * Consider the partial products in the multiplication of "abcde" by itself:
   *
   * <pre>
   *               a  b  c  d  e
   *            *  a  b  c  d  e
   *          ==================
   *              ae be ce de ee
   *           ad bd cd dd de
   *        ac bc cc cd ce
   *     ab bb bc bd be
   *  aa ab ac ad ae
   * </pre>
   *
   * Note that everything above the main diagonal:
   *
   * <pre>
   *              ae be ce de = (abcd) * e
   *           ad bd cd       = (abc) * d
   *        ac bc             = (ab) * c
   *     ab                   = (a) * b
   * </pre>
   *
   * is a copy of everything below the main diagonal:
   *
   * <pre>
   *                       de
   *                 cd ce
   *           bc bd be
   *     ab ac ad ae
   * </pre>
   *
   * Thus, the sum is 2 * (off the diagonal) + diagonal. This is accumulated
   * beginning with the diagonal (which consist of the squares of the digits of
   * the input), which is then divided by two, the off-diagonal added, and
   * multiplied by two again. The low bit is simply a copy of the low bit of the
   * input, so it doesn't need special care.
   */
  private static final void squareToLen0(final int[] x, int xlen, final int[] z, int zlen, final int off) {
    int i, j, offset;
    long x0 = 0;

    xlen += off;
    zlen += off;

    // Store the squares, right shifted one bit (i.e., divided by 2)
    for (i = xlen - 1, j = zlen; i >= off; --i) {
      z[--j] = ((int)x0 << 31) | (int)((x0 = (x0 = x[i] & LONG_MASK) * x0) >>> 33);
      z[--j] = (int)(x0 >>> 1);
    }

    // Add in off-diagonal sums
    for (i = off, offset = off; i < xlen; ++i, offset += 2) {
      j = x[i];
      j = mulAdd(x, i + 1, xlen, j, z, offset + 1);
      addOne(z, offset, zlen, xlen - i, j);
    }

    // Shift back up and set low bit
    primitiveLeftShift(z, off, zlen, 1);
    z[off] |= x[off] & 1;
  }

  // shifts a up to len left n bits assumes no leading zeros, 0<=n<32
  static void primitiveLeftShift(final int[] a, final int start, int end, final int n) {
    if (end <= start || n == 0)
      return;

    final int n2 = 32 - n;
    int c = a[--end];
    while (end > start)
      a[end--] = (c << n) | ((c = a[end]) >>> n2);

    a[start] <<= n;
    // _debugLenSig(a);
  }

  /**
   * Multiply an array by one word k and add to result, return the carry
   */
  private static int mulAdd(final int[] in, int from, final int to, final int t, final int[] out, int offset) {
    final long tLong = t & LONG_MASK;
    long carry = 0;

    while (from < to) {
      carry += (in[from++] & LONG_MASK) * tLong + (out[offset] & LONG_MASK);
      out[offset++] = (int)carry;
      carry >>>= 32;
    }

    // _debugLenSig(out);
    return (int)carry;
  }

  /**
   * Add one word to the number a mlen words into a. Return the resulting carry.
   */
  private static int addOne(final int[] mag, int offset, final int len, int mlen, final int carry) {
    offset += mlen;
    final long t = (mag[offset] & LONG_MASK) + (carry & LONG_MASK);

    mag[offset] = (int)t;
    if ((t >>> 32) == 0)
      return 0;

    while (--mlen >= 0) {
      if (++offset == len) // Carry out of number
        return 1;

      ++mag[offset];
      if (mag[offset] != 0)
        return 0;
    }

    // _debugLenSig(mag);
    return 1;
  }

  /**
   * Calculate bitlength of contents of the first len elements an int array,
   * assuming there are no leading zero ints.
   */
  static long bitLength(final int[] val, final int len) {
    return len == 0 ? 0 : ((len - 1) << 5) + bitLengthForInt(val[0]);
  }

  private static final Method m;
  static {
    try {
      m = BigInteger.class.getDeclaredMethod("multiplyToLen", int[].class, int.class, int[].class, int.class, int[].class);
      m.setAccessible(true);
    }
    catch (final NoSuchMethodException | SecurityException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static void hackMultiplyToLen(final int[] x, final int xlen, final int[] y, final int ylen, final int[] z) {
    final int[] xx = new int[xlen];
    for (int i = 1; i <= xlen; ++i)
      xx[xlen - i] = x[i];

    final int[] yy = new int[ylen];
    for (int i = 1; i <= ylen; ++i)
      yy[ylen - i] = y[i];

    int zlen = xlen + ylen;
    try {
      m.invoke(null, xx, xlen, yy, ylen, z);
    }
    catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    z[zlen] = z[0];
    for (int i = 1; i < zlen; ++i) {
      int a = z[i];
      z[i] = z[zlen - i];
      z[zlen - i] = a;
    }
  }
}