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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

abstract class BigIntMultiplication extends BigIntBinary {
  private static final long serialVersionUID = -4907342078241892616L;

  /**
   * The threshold value for using Karatsuba multiplication. If the number of
   * ints in both mag arrays are greater than this number, then Karatsuba
   * multiplication will be used. This value is found experimentally to work
   * well.
   */
  private static final int KARATSUBA_THRESHOLD = 110; // 80

  /**
   * The threshold value for using 3-way Toom-Cook multiplication.
   * If the number of ints in each mag array is greater than the
   * Karatsuba threshold, and the number of ints in at least one of
   * the mag arrays is greater than this threshold, then Toom-Cook
   * multiplication will be used.
   */
  private static final int TOOM_COOK_THRESHOLD = 240;

  /**
   * The threshold value for using Karatsuba squaring.  If the number
   * of ints in the number are larger than this value,
   * Karatsuba squaring will be used.   This value is found
   * experimentally to work well.
   */
  private static final int KARATSUBA_SQUARE_THRESHOLD = 12800000; // 128


  /**
   * The threshold value for using squaring code to perform multiplication
   * of a {@code BigInteger} instance by itself.  If the number of ints in
   * the number are larger than this value, {@code multiply(this)} will
   * return {@code square()}.
   */
  private static final int MULTIPLY_SQUARE_THRESHOLD = 20;

  /**
   * This constant limits {@code mag.length} of BigIntegers to the supported
   * range.
   */
  private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1; // (1 << 26)

  /**
   * The threshold value for using Toom-Cook squaring.  If the number
   * of ints in the number are larger than this value,
   * Toom-Cook squaring will be used.   This value is found
   * experimentally to work well.
   */
  private static final int TOOM_COOK_SQUARE_THRESHOLD = 216;

  /**
   * Multiplies the provided number by an {@code int} multiplicand.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the multiplication of the provided number by the specified multiplier
   * requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded multiplicand}.
   * @param mul The multiplier.
   * @return The result of the multiplication of the provided number by the
   *         {@code int} multiplier.
   * @complexity O(n)
   */
  public static int[] mul(final int[] val, int mul) {
    return mul < 0 ? mul0(val, -1, -mul, true) : mul > 0 ? mul0(val, 1, mul, true) : setToZero0(val);
  }

  /**
   * Multiplies the provided number by an <i>unsigned</i> {@code int}
   * multiplicand.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the multiplication of the provided number by the specified multiplier
   * requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded multiplicand}.
   * @param sig The sign of the unsigned {@code int} multiplier.
   * @param mul The multiplier (unsigned).
   * @return The result of the multiplication of the provided number by the
   *         <i>unsigned</i> {@code int} multiplier.
   * @complexity O(n)
   */
  public static int[] mul(final int[] val, final int sig, final int mul) {
    return mul == 0 ? setToZero0(val) : mul0(val, sig, mul, true);
  }

  private static int[] mul0(int[] val, final int sig, final int mul, final boolean allocAllowed) {
    final boolean flipSig;
    int len = val[0];
    if (len < 0) { len = -len; flipSig = sig >= 0; } else { flipSig = sig < 0; }
    if (len + 1 >= val.length)
      val = realloc(val, len + 1, len + len + 1, allocAllowed);

    len = umul0(val, 1, len, mul);
    val[0] = flipSig ? -len : len;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Multiplies the provided number by an {@code long} multiplicand.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the multiplication of the provided number by the specified multiplier
   * requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded multiplicand}.
   * @param mul The multiplier.
   * @return The result of the multiplication of the provided number by the
   *         {@code long} multiplier.
   * @complexity O(n)
   */
  public static int[] mul(final int[] val, long mul) {
    return mul < 0 ? mul(val, -1, -mul) : mul(val, 1, mul);
  }

  /**
   * Multiplies the provided number by an <i>unsigned</i> {@code long}
   * multiplicand.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the multiplication of the provided number by the specified multiplier
   * requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded multiplicand}.
   * @param sig The sign of the unsigned {@code long} multiplier.
   * @param mul The multiplier (unsigned).
   * @return The result of the multiplication of the provided number by the
   *         <i>unsigned</i> {@code long} multiplier.
   * @complexity O(n)
   */
  public static int[] mul(int[] val, int sig, final long mul) {
    final long mull = mul & LONG_INT_MASK;
    final long mulh = mul >>> 32;
    if (mulh == 0)
      return mul(val, sig, (int)mull);

    final boolean flipSig;
    int len = val[0];
    if (len < 0) { len = -len; flipSig = sig >= 0; } else { flipSig = sig < 0; }
    if (len + 2 >= val.length)
      val = realloc(val, len + 1, len + len + 2, true);

    sig = umul0(val, 1, len, mull, mulh);
    val[0] = flipSig ? -sig : sig;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Multiplies the provided magnitude by an <i>unsigned</i> {@code int}
   * multiplicand.
   * <p>
   * <i><b>Note:</b> This method assumes that the length of the provided
   * magnitude array will accommodate for the result of the multiplication,
   * which may at most require 1 free limb.</i>
   *
   * @param mag The multiplicand (little-endian).
   * @param off The offset of the first limb of the multiplicand.
   * @param len The number of limbs of the multiplicand.
   * @param mul The multiplier (unsigned).
   * @return The result of the multiplication of the provided magnitude by the
   *         <i>unsigned</i> {@code int} multiplier.
   * @complexity O(n)
   */
  public static int umul(final int[] mag, final int off, int len, final int mul) {
    return mul == 0 ? mag[off] = 0 : umul0(mag, off, len, mul);
  }

  private static int umul0(final int[] mag, final int off, int len, final int mul) {
    long carry = 0;
    final long low = mul & LONG_INT_MASK;
    len += off;
    for (int i = off; i < len; ++i) {
      carry += (mag[i] & LONG_INT_MASK) * low;
      mag[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry == 0)
      return len - off;

    mag[len++] = (int)carry;
    return len - off;
  }

  /**
   * Multiplies the provided magnitude by an <i>unsigned</i> {@code long}
   * multiplicand.
   * <p>
   * <i><b>Note:</b> This method assumes that the length of the provided
   * magnitude array will accommodate for the result of the multiplication,
   * which may at most require 2 free limbs.</i>
   *
   * @param mag The multiplicand (little-endian).
   * @param off The offset of the first limb of the multiplicand.
   * @param len The number of limbs of the multiplicand.
   * @param mul The multiplier (unsigned).
   * @return The result of the multiplication of the provided magnitude by the
   *         <i>unsigned</i> {@code int} multiplier.
   * @complexity O(n)
   */
  public static int umul(final int[] mag, final int off, final int len, final long mul) {
    return mul == 0 ? mag[off] = 0 : umul0(mag, off, len, mul);
  }

  private static int umul0(final int[] mag, final int off, final int len, final long mul) {
    final long h2 = mul >>> 32;
    return h2 == 0 ? umul(mag, off, len, (int)mul) : umul0(mag, off, len, mul & LONG_INT_MASK, h2);
  }

  private static int umul0(final int[] val, final int off, int len, final long mull, final long mulh) {
    long carry = 0, low, mul;
    len += off;
    for (int i = off; i < len; ++i) {
      low = val[i] & LONG_INT_MASK;
      mul = low * mull;
      val[i] = (int)(mul + carry);
      carry = (mul >>> 32) + (carry >>> 32) + ((mul & LONG_INT_MASK) + (carry & LONG_INT_MASK) >>> 32);
      carry += low * mulh; // Could this overflow?
    }

    val[len] = (int)carry;
    if (carry == 0)
      return len - off;

    if ((val[++len] = (int)(carry >>> 32)) != 0)
      ++len;

    return len - off;
  }

  /**
   * Multiplies the provided number by a {@linkplain BigInt#val() value-encoded
   * multiplicand}.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the multiplication of the provided number by the specified multiplier
   * requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded multiplicand}.
   * @param mul The {@linkplain BigInt#val() value-encoded multiplier}.
   * @return The result of the multiplication of the provided
   *         {@linkplain BigInt#val() value-encoded number} by the
   *         {@linkplain BigInt#val() value-encoded multiplier}.
   * @complexity O(n^2) - O(n log n)
   */
  public static int[] mul(int[] val, final int[] mul) {
    return mul(val, mul, false, -1);
  }

  /**
   * Returns a BigInteger whose value is {@code (this * val)}. If the invocation
   * is recursive certain overflow checks are skipped.
   *
   * @param mul value to be multiplied by this BigInteger.
   * @param isRecursion whether this is a recursive invocation
   * @return {@code this * val}
   */
  private static int[] mul(int[] val, final int[] mul, final boolean isRecursion, final int exactLen) {
    int len = val[0];
    if (len == 0)
      return val;

    int mlen = mul[0];
    if (mlen == 0)
      return setToZero0(val);

    boolean sig = true;
    if (len < 0) { len = -len; sig = !sig; }
    if (mul == val && len > MULTIPLY_SQUARE_THRESHOLD)
      return square(val, 1, len, false);

    if (mlen < 0) { mlen = -mlen; sig = !sig; }
    if (len < KARATSUBA_THRESHOLD || mlen < KARATSUBA_THRESHOLD) {
      if (mlen <= 2 || len <= 2) {
        if (mlen == 1) {
          if (len + 2 >= val.length)
            val = realloc(val, len + 1, len + len, exactLen < 0);

          len = umul0(val, 1, len, mul[1]);
        }
        else if (len == 1) {
          final int m = val[1];
          val = copy(mul, val, mlen + 1, mlen + 2, exactLen < 0);
          len = umul0(val, 1, mlen, m);
        }
        else {
          final long ml;
          final long mh;
          if (mlen == 2) {
            if (len + 2 >= val.length)
              val = realloc(val, len + 1, len + len, exactLen < 0);

            ml = mul[1] & LONG_INT_MASK;
            mh = mul[2] & LONG_INT_MASK;
          }
          else {
            ml = val[1] & LONG_INT_MASK;
            mh = val[2] & LONG_INT_MASK;
            val = copy(mul, val, mlen + 1, mlen + 3, exactLen < 0);
            len = mlen;
          }

          len = umul0(val, 1, len, ml, mh);
        }

        val[0] = sig ? len : -len;
        // _debugLenSig(val);
        return val;
      }

      int zlen = len + mlen + 1;
      final int[] z = exactLen < 0 ? alloc(zlen, true) : new int[exactLen];
      zlen = multiplyToLen(val, len, mul, mlen, z, zlen, OFF);
      z[0] = sig ? zlen : -zlen;
      return z;
    }

    if (len < TOOM_COOK_THRESHOLD && mlen < TOOM_COOK_THRESHOLD) {
      val = mulKaratsuba(mul, mlen, val, len, exactLen < 0 ? len + mlen : exactLen); // Swap mul and val so that multiplyKaratsuba can reuse the val array
      if (sig != val[0] >= 0)
        val[0] = -val[0];

      return val;
    }

    if (false) {
      len = mulKaratsuba2(val, len, mul, mlen, false);
      val[0] = sig ? len : -len;
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
      if (bitLength(val, len) + bitLength(mul, mlen) > 32L * MAX_MAG_LENGTH) {
        throw new ArithmeticException("BigInteger would overflow supported range");
      }
    }

    return mulToomCook3(val, mul);
  }

  /**
   * Multiplies the provided magnitude arrays {@code x} and {@code y}, and puts
   * the result in {@code z}. Uses a quadratic algorithm which is often suitable
   * for smaller numbers.
   *
   * <pre>
   * res = val1 * val2
   * </pre>
   *
   * <i><b>Note:</b> It is expected that
   * {@code z.length >= len1 + len2 + 1}.</i>
   *
   * @param x The first magnitude array.
   * @param xlen The number of limbs in the first magnitude array {@code x}.
   * @param y The second magnitude array.
   * @param ylen The number of limbs in the second magnitude array {@code y}.
   * @param z The array into which the result is to be put.
   * @param zlen The number of limbs of the result magnitude array {@code z}.
   * @param off The offset of the first limb for each magnitude array.
   * @complexity O(n^2)
   */
  private static int multiplyToLen(final int[] x, int xlen, final int[] y, int ylen, final int[] z, int zlen, final int off) {
    if (xlen < ylen)
      return multiplyToLen0(x, xlen, y, ylen, z, zlen, off);

    return multiplyToLen0(y, ylen, x, xlen, z, zlen, off);
  }

  private static int multiplyToLen0(final int[] x, int xlen, final int[] y, int ylen, final int[] z, int zlen, final int off) {
    xlen += off;
    ylen += off;

    int i, j, k;
    long carry = 0;
    long x0 = x[off] & LONG_INT_MASK;
    for (j = off; j < ylen; ++j) {
      carry += x0 * (y[j] & LONG_INT_MASK);
      z[j] = (int)carry;
      carry >>>= 32;
    }

    z[ylen--] = (int)carry;
    for (i = off + 1; i < xlen; ++i) {
      carry = 0;
      x0 = x[i] & LONG_INT_MASK;
      for (j = off, k = i; j <= ylen; ++j, ++k) {
        carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK);
        z[k] = (int)carry;
        carry >>>= 32;
      }

      z[i + ylen] = (int)carry;
    }

    while (z[--zlen] == 0);
    // _debugLenSig(res);
    return zlen;
  }

  private static int mulKaratsuba2(int[] val, final int len, int[] mul, final int len2, final boolean allocAllowed) {
    // FIXME: This is not tested
    if (mul.length < len)
      mul = realloc(mul, len2 + 1, len, allocAllowed);
    else if (val.length < len2)
      val = realloc(val, len + 1, len2, allocAllowed);

    try {
      // FIXME: Tune thresholds
      val = karatsuba(val, len, mul, len2, Math.max(len, len2) > 20000);
    }
    catch (final ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }

    return len + len2 - 1;
  }

  /**
   * Multiplies the provided {@linkplain BigInt#val() value-encoded numbers}
   * using the Karatsuba algorithm, and returns the result. The caller can
   * choose to use a parallel version which is more suitable for larger numbers.
   * <p>
   * <i><b>Note:</b> The size of {@code val1} and {@code val2} must be the
   * same.</i>
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param len1 The number of limbs in {@code val1}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @param len2 The number of limbs in {@code val2}.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @return The result of the multiplication of the provided
   *         {@linkplain BigInt#val() value-encoded numbers}.
   * @throws ExecutionException If the computation in a worker thread threw an
   *           exception.
   * @throws InterruptedException If the current thread was interrupted while
   *           waiting.
   * @complexity O(n^1.585)
   */
  // FIXME: Not fully tested on small numbers... fix naming?
  static int[] karatsuba(int[] val1, int len1, int[] val2, int len2, final boolean parallel) throws ExecutionException, InterruptedException {
    final int mlen = Math.max(len1, len2);
    if (len2 < len1) {
      do
        val2[len1--] = 0;
      while (len1 >= len2);
    }
    else if (len1 < len2) {
      do
        val1[len2--] = 0;
      while (len2 >= len1);
    }

    final int[] res = new int[2 * (mlen + 1)]; // FIXME: Use alloc()
    res[0] = res.length - 1;
    if (val1[0] < 0 != val2[0] < 0)
      res[0] = -res[0];

    if (parallel) {
      final ExecutorService pool = Executors.newFixedThreadPool(12);
      pmul(val1, val2, 1, mlen, 1, res, pool);
      pool.shutdown();
    }
    else {
      kmul(val1, val2, 1, mlen, res);
    }

    // _debugLenSig(res);
    return res;
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * puts the result in {@code res}. Algorithm: Karatsuba
   *
   * @param val1 The first magnitude array.
   * @param val2 The second magnitude array.
   * @param off The offset of the first element.
   * @param len The length of each of the two partial arrays.
   * @param res The array into which the result is to be put.
   * @complexity O(n^1.585)
   */
  static void kmul(final int[] val1, final int[] val2, final int off, int len, final int[] res) {
    len += off;
    // x = x1*B^m + x0
    // y = y1*B^m + y0
    // xy = z2*B^2m + z1*B^m + z0
    // z2 = x1*y1, z0 = x0*y0, z1 = (x1+x0)(y1+y0)-z2-z0
    if (len <= 32) {
      long carry = 0, tmp, xi = val1[off] & LONG_INT_MASK;
      for (int j = 0; j < len; ++j) {
        tmp = xi * (val2[off + j] & LONG_INT_MASK) + carry;
        res[j] = (int)tmp;
        carry = tmp >>> 32;
      }

      res[len] = (int)carry;
      for (int i = 1; i < len; ++i) {
        xi = val1[off + i] & LONG_INT_MASK;
        carry = 0;
        for (int j = 0; j < len; ++j) {
          tmp = xi * (val2[off + j] & LONG_INT_MASK) + (res[i + j] & LONG_INT_MASK) + carry;
          res[i + j] = (int)tmp;
          carry = tmp >>> 32;
        }

        res[i + len] = (int)carry;
      }
    }
    else {
      final int b = len >>> 1;
      final int[] z2 = new int[2 * (len - b)]; // FIXME: Use alloc()
      final int[] z0 = new int[2 * b]; // FIXME: Use alloc()
      kmul(val1, val2, off + b, len - b, z2);
      kmul(val1, val2, off, b, z0);

      // FIXME: How to avoid new int[]?
      final int[] x2 = new int[len - b + 1]; // FIXME: Use alloc()
      final int[] y2 = new int[len - b + 1]; // FIXME: Use alloc()
      long carry = 0;
      for (int i = 0; i < b; ++i) {
        carry += (val1[off + b + i] & LONG_INT_MASK) + (val1[off + i] & LONG_INT_MASK);
        x2[i] = (int)carry;
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        x2[b] = val1[off + b + b];

      if (carry != 0 && ++x2[b] == 0)
        ++x2[b + 1];

      carry = 0;
      for (int i = 0; i < b; ++i) {
        carry += (val2[off + b + i] & LONG_INT_MASK) + (val2[off + i] & LONG_INT_MASK);
        y2[i] = (int)carry;
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        y2[b] = val2[off + b + b];

      if (carry != 0 && ++y2[b] == 0)
        ++y2[b + 1];

      final int l = len - b + (x2[len - b] != 0 || y2[len - b] != 0 ? 1 : 0);
      final int[] z1 = new int[2 * l]; // FIXME: Use alloc()
      kmul(x2, y2, 0, l, z1);

      // FIXME: fromIndex & toIndex?!
      System.arraycopy(z0, 0, res, 0, 2 * b); // Add z0
      System.arraycopy(z2, 0, res, b + b, 2 * (len - b)); // Add z2

      // Add z1
      carry = 0;
      int i = 0;
      for (; i < 2 * b; ++i) {
        carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK);
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      for (; i < 2 * (len - b); ++i) {
        carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK);
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      for (; i < z1.length; ++i) {
        carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK);
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      if (carry != 0)
        while (++res[i + b] == 0)
          ++i;
    }

    // _debugLenSig(res);
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * puts the result in {@code res}. Algorithm: Parallel Karatsuba
   *
   * @param val1 The first magnitude array.
   * @param val2 The second magnitude array.
   * @param off The offset of the first element.
   * @param len The length of each of the two partial arrays.
   * @param lim The recursion depth until which to spawn new threads.
   * @param pool Where spawned threads are to be added and executed.
   * @param res The array into which the result is to be put (length = 2 * n).
   * @complexity O(n^1.585)
   */
  private static void pmul(final int[] val1, final int[] val2, final int off, final int len, final int lim, final int[] res, final ExecutorService pool) throws ExecutionException, InterruptedException {
    final int toIndex = len + off;
    final int b = toIndex >>> 1;

    final Future<int[]> left = pool.submit(() -> {
      final int[] z = new int[2 * b]; // FIXME: Use alloc()
      if (lim == 0)
        kmul(val1, val2, off, b, z);
      else
        pmul(val1, val2, off, b, lim - 1, z, pool);

      return z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] z = new int[2 * (toIndex - b)]; // FIXME: Use alloc()
      if (lim == 0)
        kmul(val1, val2, off + b, len - b, z);
      else
        pmul(val1, val2, off + b, len - b, lim - 1, z, pool);

      return z;
    });

    final int[] x2 = new int[toIndex - b + 1]; // FIXME: Use alloc()
    final int[] y2 = new int[toIndex - b + 1]; // FIXME: Use alloc()
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry += (val1[off + b + i] & LONG_INT_MASK) + (val1[off + i] & LONG_INT_MASK);
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0)
      x2[b] = val1[off + b + b];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry += (val2[off + b + i] & LONG_INT_MASK) + (val2[off + i] & LONG_INT_MASK);
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0) {
      y2[b] = val2[off + b + b];
    }

    if (carry != 0 && ++y2[b] == 0)
      ++y2[b + 1];

    final Future<int[]> mid = pool.submit(() -> {
      final int l = toIndex - b + (x2[toIndex - b] != 0 || y2[toIndex - b] != 0 ? 1 : 0);
      final int[] Z = new int[2 * l]; // FIXME: Use alloc()
      if (lim == 0)
        kmul(x2, y2, 0, l - off, Z);
      else
        pmul(x2, y2, 0, l - off, lim - 1, Z, pool);

      return Z;
    });

    final int[] z0 = left.get();
    System.arraycopy(z0, 0, res, 0, 2 * b);
    final int[] z2 = right.get();
    System.arraycopy(z2, 0, res, b + b, 2 * (toIndex - b));

    final int[] z1 = mid.get();

    carry = 0;
    int i = 0;
    for (; i < 2 * b; ++i) {
      carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK);
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < 2 * (toIndex - b); ++i) {
      carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK);
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < z1.length; ++i) {
      carry += (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK);
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    if (carry != 0)
      while (++res[i + b] == 0)
        ++i;
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
      z = copy0(xh, new int[zlen], xUpperLen + OFF); // This length is exact
    }

    int[] yh = yIsSmall ? new int[half + OFF] : getUpper0(y, yUpperLen, xUpperLen, half);
    int[] xl = getLower(x, xlen, half, false);
    final int[] yl = getLower(y, ylen, half, true);

    z = mul(z, yh, false, zlen);

    xh = addSub(xh, xl, true, false);
    yh = addSub(yh, yl, true, false);

    xh = mul(xh, yh, false, xlen + yUpperLen + OFF); // This length is exact
    xl = mul(xl, yl, false, half + half + OFF); // This length is exact

    // result = p1 * 2^(32*2*half) + (p3 - p1 - p2) * 2^(32*half) + p2
    xh = addSub(xh, z, false, false);
    xh = addSub(xh, xl, false, false);

    z = shiftLeft(z, 32 * half, false);
    z = addSub(z, xh, true, false);
    z = shiftLeft(z, 32 * half, false);
    z = addSub(z, xl, true, false);

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
  private static int[] mulToomCook3(int[] x, int[] y) {
    // FIXME: This is not tested
    int xlen = Math.abs(x[0]);
    int ylen = Math.abs(y[0]);

    int largest = Math.max(xlen, ylen);

    // k is the size (in ints) of the lower-order slices.
    int k = (largest + 2) / 3; // Equal to ceil(largest/3)

    // r is the size (in ints) of the highest-order slice.
    int r = largest - 2 * k;

    // Obtain slices of the numbers. a2 and b2 are the most significant
    // bits of the numbers a and b, and a0 and b0 the least significant.
    int[] a0, a1, a2, b0, b1, b2;
    a2 = getToomSlice(x, 1, xlen, k, r, 0, largest);
    a1 = getToomSlice(x, 1, xlen, k, r, 1, largest);
    a0 = getToomSlice(x, 1, xlen, k, r, 2, largest);
    b2 = getToomSlice(y, 1, ylen, k, r, 0, largest);
    b1 = getToomSlice(y, 1, ylen, k, r, 1, largest);
    b0 = getToomSlice(y, 1, ylen, k, r, 2, largest);

    int[] v0, v1, v2, vm1, vinf, t1, t2, tm1, da1, db1;

    v0 = mul(a0, b0, true, -1);
    da1 = add(a2, a0);
    db1 = add(b2, b0);
    vm1 = mul(sub(da1, a1), sub(db1, b1), true, -1);
    da1 = add(da1, a1);
    db1 = add(db1, b1);
    v1 = mul(da1, db1, true, -1);
    v2 = mul(sub(shiftLeft(add(da1, a2), 1), a0), sub(shiftLeft(add(db1, b2), 1), b0), true, -1);
    vinf = mul(a2, b2, true, -1);

    // The algorithm requires two divisions by 2 and one by 3.
    // All divisions are known to be exact, that is, they do not produce
    // remainders, and all results are positive. The divisions by 2 are
    // implemented as right shifts which are relatively efficient, leaving
    // only an exact division by 3, which is done by a specialized
    // linear-time algorithm.
    t2 = exactDivBy3(sub(v2, vm1));
    tm1 = shiftRight(sub(v1, vm1), 1);
    t1 = sub(v1, v0);
    t2 = shiftRight(sub(t2, t1), 1);
    t1 = sub(sub(t1, tm1), vinf);
    t2 = sub(t2, shiftLeft(vinf, 1));
    tm1 = sub(tm1, t2);

    // Number of bits to shift left.
    int ss = k * 32;

    int[] result = add(shiftLeft(add(shiftLeft(add(shiftLeft(add(shiftLeft(vinf, ss), t2), ss), t1), ss), tm1), ss), v0);
    _debugLenSig(result);
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
  private static int[] getToomSlice(final int[] mag, final int off, final int len, int lowerSize, int upperSize, int slice, int fullsize) {
    int start, sliceSize;
    final int end;
    final int offset = fullsize - len;

    if (slice == 0) {
      start = off - offset;
      end = upperSize - 1 - offset;
    }
    else {
      start = off + upperSize + (slice - 1) * lowerSize - offset;
      end = start + lowerSize - 1;
    }

    if (start < off)
      start = off;

    if (end < 0)
      return new int[2]; // FIXME: Use alloc()

    sliceSize = (end - start) + 1;
    if (sliceSize <= 0)
      return new int[2]; // FIXME: Use alloc()

    // While performing Toom-Cook, all slices are positive and
    // the sign is adjusted when the final number is composed.
    if (start == off && sliceSize >= len)
      return abs(mag.clone()); // FIXME: Do we need to clone?

    final int intSlice[] = new int[sliceSize]; // FIXME: Use alloc()
    System.arraycopy(mag, start, intSlice, 0, sliceSize);
    intSlice[0] = sliceSize;
    for (; intSlice[sliceSize] == 0; --sliceSize);
    _debugLenSig(intSlice);
    return intSlice;
  }

  /**
   * Does an exact division (that is, the remainder is known to be zero) of the
   * specified number by 3. This is used in Toom-Cook multiplication. This is an
   * efficient algorithm that runs in linear time. If the argument is not
   * exactly divisible by 3, results are undefined. Note that this is expected
   * to be called with positive arguments only.
   */
  private static int[] exactDivBy3(final int[] val) {
    int len = val[0];
    boolean sig = true; if (len < 0) { len = -len; sig = false; }
    ++len;

    int[] result = new int[len]; // FIXME: Use alloc()
    long x, w, q, borrow;
    borrow = 0L;
    for (int i = len - 1; i >= 1; i--) {
      x = (val[i] & LONG_INT_MASK);
      w = x - borrow;
      if (borrow > x) { // Did we make the number go negative?
        borrow = 1L;
      }
      else {
        borrow = 0L;
      }

      // 0xAAAAAAAB is the modular inverse of 3 (mod 2^32). Thus,
      // the effect of this is to divide by 3 (mod 2^32).
      // This is much faster than division on most architectures.
      q = (w * 0xAAAAAAABL) & LONG_INT_MASK;
      result[i] = (int)q;

      // Now check the borrow. The second check can of course be
      // eliminated if the first fails.
      if (q >= 0x55555556L) {
        borrow++;
        if (q >= 0xAAAAAAABL)
          borrow++;
      }
    }

    for (; result[len] == 0; --len);
    result[0] = sig ? len : -len;
    _debugLenSig(result);
    return result;
  }

  /**
   * Returns a new BigInteger representing n lower ints of the number. This is
   * used by Karatsuba multiplication and Karatsuba squaring.
   */
  private static int[] getLower(final int[] val, int len, int n, final boolean reuse) {
    if (len <= n)
      return abs(reuse ? val : val.clone());

    // First trim the length
    for (; val[n] == 0; --n);

    final int[] lowerInts;
    if (reuse) {
      lowerInts = val;
    }
    else {
      lowerInts = new int[n + OFF];
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
   */
  private static int[] getUpper(final int[] val, final int upperLen, int upperLen2, final int n) {
    if (upperLen2 <=0)
      upperLen2 = n;

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
  private static int[] square(final int[] val, final boolean isRecursion) {
    return square(val, 1, Math.abs(val[0]), isRecursion);
  }

  private static int[] square(final int[] mag, final int off, final int len, final boolean isRecursion) {
    if (len == 0)
      return mag;

    if (len < KARATSUBA_SQUARE_THRESHOLD)
      return squareToLen(mag, off, len);

    if (len < TOOM_COOK_SQUARE_THRESHOLD)
      return squareKaratsuba(mag);

    // For a discussion of overflow detection see multiply()
    if (!isRecursion && bitLength(mag, len) > 16L * MAX_MAG_LENGTH)
      throw new ArithmeticException("BigInteger would overflow supported range");

    return squareToomCook3(mag);
  }

  /**
   * Squares a BigInteger using the Karatsuba squaring algorithm. It should be
   * used when both numbers are larger than a certain threshold (found
   * experimentally). It is a recursive divide-and-conquer algorithm that has
   * better asymptotic performance than the algorithm used in squareToLen.
   */
  private static int[] squareKaratsuba(final int[] val) {
    final int len = Math.abs(val[0]);
    final int half = (len + 1) / 2;

    final int[] xl = getLower(val, len, half, false);
    final int upperLen = len - half;
    final int[] xh = getUpper(val, upperLen, upperLen, half);

    final int[] xhs = square(xh, 1, half, false); // xhs = xh^2
    final int[] xls = square(xl, 1, half, false); // xls = xl^2

    // xh^2 << 64 + (((xl+xh)^2 - (xh^2 + xl^2)) << 32) + xl^2

    final int[] z = add(shiftLeft(add(shiftLeft(xhs, half * 32), sub(square(add(xl, xh), false), add(xhs, xls))), half * 32), xls);
    _debugLenSig(z);
    return z;
  }

  /**
   * Squares a BigInteger using the 3-way Toom-Cook squaring algorithm. It
   * should be used when both numbers are larger than a certain threshold (found
   * experimentally). It is a recursive divide-and-conquer algorithm that has
   * better asymptotic performance than the algorithm used in squareToLen or
   * squareKaratsuba.
   */
  private static int[] squareToomCook3(final int[] val) {
    final int len = Math.abs(val[0]);

    // k is the size (in ints) of the lower-order slices.
    int k = (len + 2) / 3; // Equal to ceil(largest/3)

    // r is the size (in ints) of the highest-order slice.
    int r = len - 2 * k;

    // Obtain slices of the numbers. a2 is the most significant
    // bits of the number, and a0 the least significant.
    int[] a0, a1, a2;
    a2 = getToomSlice(val, 1, len, k, r, 0, len);
    a1 = getToomSlice(val, 1, len, k, r, 1, len);
    a0 = getToomSlice(val, 1, len, k, r, 2, len);
    int[] v0, v1, v2, vm1, vinf, t1, t2, tm1, da1;

    v0 = square(a0, true);
    da1 = add(a2, a0);
    vm1 = square(sub(da1, a1), true);
    da1 = add(da1, a1);
    v1 = square(da1, true);
    vinf = square(a2, true);
    v2 = square(sub(shiftLeft(add(da1, a2), 1), a0), true);

    // The algorithm requires two divisions by 2 and one by 3.
    // All divisions are known to be exact, that is, they do not produce
    // remainders, and all results are positive. The divisions by 2 are
    // implemented as right shifts which are relatively efficient, leaving
    // only a division by 3.
    // The division by 3 is done by an optimized algorithm for this case.
    t2 = exactDivBy3(sub(v2, vm1));
    tm1 = shiftRight(sub(v1, vm1), 1);
    t1 = sub(v1, v0);
    t2 = shiftRight(sub(t2, t1), 1);
    t1 = sub(sub(t1, tm1), vinf);
    t2 = sub(t2, shiftLeft(vinf, 1));
    tm1 = sub(tm1, t2);

    // Number of bits to shift left.
    int ss = k * 32;

    return add(shiftLeft(add(shiftLeft(add(shiftLeft(add(shiftLeft(vinf, ss), t2), ss), t1), ss), tm1), ss), v0);
  }

  /**
   * Squares the contents of the int array x. The result is placed into the int
   * array z. The contents of x are not changed.
   */
  private static final int[] squareToLen(final int[] mag, final int off, final int len) {
    int zlen = len << 1;
    final int[] z = new int[zlen + off];
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
      z[--j] = ((int)x0 << 31) | (int)((x0 = (x0 = x[i] & LONG_INT_MASK) * x0) >>> 33);
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
    final long tLong = t & LONG_INT_MASK;
    long carry = 0;

    while (from < to) {
      carry += (in[from++] & LONG_INT_MASK) * tLong + (out[offset] & LONG_INT_MASK);
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
    final long t = (mag[offset] & LONG_INT_MASK) + (carry & LONG_INT_MASK);

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
  private static int bitLength(final int[] val, final int len) {
    return len == 0 ? 0 : ((len - 1) << 5) + bitLengthForInt(val[0]);
  }
}