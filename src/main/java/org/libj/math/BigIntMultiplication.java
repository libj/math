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

abstract class BigIntMultiplication extends BigIntAddition {
  private static final long serialVersionUID = -4907342078241892616L;

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
    return mul < 0 ? mul0(val, -1, -mul) : mul > 0 ? mul0(val, 1, mul) : setToZero0(val);
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
    return mul == 0 ? setToZero0(val) : mul0(val, sig, mul);
  }

  private static int[] mul0(int[] val, final int sig, final int mul) {
    final boolean flipSig;
    int len = val[0];
    if (len < 0) { len = -len; flipSig = sig >= 0; } else { flipSig = sig < 0; }
    if (len + 1 >= val.length)
      val = realloc(val, len + 1, len + len + 1);

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
      val = realloc(val, len + 1, len + len + 2);

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
  public static int[] mul(int[] val, int[] mul) {
    if (isZero(val))
      return val;

    if (isZero(mul))
      return setToZero0(val);

    int len1 = val[0];
    int len2 = mul[0];
    final boolean flipSig = len1 < 0 != len2 < 0;
    if (len1 < 0) { len1 = -len1; }
    if (len2 < 0) { len2 = -len2; }

    if (len2 <= 2 || len1 <= 2) {
      if (len2 == 1) {
        if (len1 + 2 >= val.length)
          val = realloc(val, len1 + 1, len1 + len1);

        len1 = umul0(val, 1, len1, mul[1]);
      }
      else if (len1 == 1) {
        final int m = val[1];
        val = copy(val, mul, len2 + 1, len2 + 2);
        len1 = umul0(val, 1, len2, m);
      }
      else {
        final long ml;
        final long mh;
        if (len2 == 2) {
          if (len1 + 2 >= val.length)
            val = realloc(val, len1 + 1, len1 + len1);

          ml = mul[1] & LONG_INT_MASK;
          mh = mul[2] & LONG_INT_MASK;
        }
        else {
          ml = val[1] & LONG_INT_MASK;
          mh = val[2] & LONG_INT_MASK;
          val = copy(val, mul, len2 + 1, len2 + 3);
          len1 = len2;
        }

        len1 = umul0(val, 1, len1, ml, mh);
      }
    }
    else if (len1 - 1 < 128 || len2 - 1 < 128 || (long)len1 * len2 < 1_000_000) {
      final int[] res = new int[len1 + len2 + 1];
      mulQuad(val, len1 + 1, mul, len2 + 1, 1, res);
      val = res;

      len1 = val[val.length - 1] == 0 ? val.length - 2 : val.length - 1;
    }
    else {
      if (mul.length < len1)
        mul = realloc(mul, len2 + 1, len1);
      else if (val.length < len2)
        val = realloc(val, len1 + 1, len2);

      try {
        // FIXME: Tune thresholds
        val = karatsuba(val, mul, Math.max(len1, len2) > 20000);
      }
      catch (final ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }

      len1 += len2 - 1;
    }

    val[0] = flipSig ? -len1 : len1;
    // _debugLenSig(val);
    return val;
  }

  /**
   * @param mul The number to multiply with.
   */
  /**
   * Multiplies the provided magnitude arrays, and puts the result in
   * {@code res}. Uses a quadratic algorithm which is often suitable for smaller
   * numbers.
   *
   * <pre>
   * res = val1 * val2
   * </pre>
   *
   * <i><b>Note:</b> It is expected that
   * {@code res.length >= len1 + len2 + 1}.</i>
   *
   * @param mag1 The first magnitude array.
   * @param len1 The number of limbs of the first number.
   * @param mag2 The second magnitude array.
   * @param len2 The number of limbs of the second number.
   * @param off The offset of the first limb for the first and second numbers,
   *          as well as the result array.
   * @param res The array into which the result is to be put.
   * @complexity O(n^2)
   */
  private static void mulQuad(final int[] mag1, final int len1, final int[] mag2, final int len2, final int off, final int[] res) {
    if (len1 < len2)
      mulQuad0(mag1, len1, mag2, len2, off, res);
    else
      mulQuad0(mag2, len2, mag1, len1, off, res);
  }

  private static void mulQuad0(final int[] mag1, final int len1, final int[] mag2, final int len2, final int off, final int[] res) {
    int i, j, k, l;
    long v, r = 0;
    long val1l = mag1[off] & LONG_INT_MASK;
    for (j = off; j < len2; ++j) {
      v = val1l * (mag2[j] & LONG_INT_MASK) + r;
      res[j] = (int)v;
      r = v >>> 32;
    }

    res[len2] = (int)r;
    for (i = off + 1; i < len1; ++i) {
      val1l = mag1[i] & LONG_INT_MASK;
      r = 0;
      k = i - off;
      for (j = off; j < len2; ++j) {
        l = j + k;
        v = val1l * (mag2[j] & LONG_INT_MASK) + (res[l] & LONG_INT_MASK) + r;
        res[l] = (int)v;
        r = v >>> 32;
      }

      res[k + len2] = (int)r;
    }

    // _debugLenSig(res);
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
      final int[] z2 = new int[2 * (len - b)];
      final int[] z0 = new int[2 * b];
      kmul(val1, val2, off + b, len - b, z2);
      kmul(val1, val2, off, b, z0);

      // FIXME: How to avoid new int[]?
      final int[] x2 = new int[len - b + 1];
      final int[] y2 = new int[len - b + 1];
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
      final int[] z1 = new int[2 * l];
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
      final int[] z = new int[2 * b];
      if (lim == 0)
        kmul(val1, val2, off, b, z);
      else
        pmul(val1, val2, off, b, lim - 1, z, pool);

      return z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] z = new int[2 * (toIndex - b)];
      if (lim == 0)
        kmul(val1, val2, off + b, len - b, z);
      else
        pmul(val1, val2, off + b, len - b, lim - 1, z, pool);

      return z;
    });

    final int[] x2 = new int[toIndex - b + 1];
    final int[] y2 = new int[toIndex - b + 1];
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
      final int[] Z = new int[2 * l];
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
   * Multiplies the provided {@linkplain BigInt#val() value-encoded numbers}
   * using the Karatsuba algorithm, and returns the result. The caller can
   * choose to use a parallel version which is more suitable for larger numbers.
   * <p>
   * <i><b>Note:</b> The size of {@code val1} and {@code val2} must be the
   * same.</i>
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
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
  static int[] karatsuba(int[] val1, int[] val2, final boolean parallel) throws ExecutionException, InterruptedException {
    int len1 = val1[0];
    if (len1 < 0) { len1 = -len1; }

    int len2 = val2[0];
    if (len2 < 0) { len2 = -len2; }

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

    final int[] res = new int[2 * (mlen + 1)];
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
}