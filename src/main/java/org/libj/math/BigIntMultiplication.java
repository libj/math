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
  static final int KARATSUBA_THRESHOLD = 110; // 110

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
    final boolean vsig; int len = val[0]; if (len < 0) { len = -len; vsig = sig >= 0; } else { vsig = sig < 0; }
    if (len + 1 >= val.length)
      val = realloc(val, len + 1, len + len + 1);

    len = umul0(val, 1, len, mul);
    val[0] = vsig ? -len : len;
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
  public static int[] mul(final int[] val, final long mul) {
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
  public static int[] mul(int[] val, final int sig, final long mul) {
    int len = val[0];
    if (len == 0)
      return val;

    if (mul == 0)
      return setToZero0(val);

    final long mull = mul & LONG_INT_MASK, mulh = mul >>> 32;
    if (mulh == 0)
      return mul0(val, sig, (int)mull);

    final boolean vsig; if (len < 0) { len = -len; vsig = sig >= 0; } else { vsig = sig < 0; }
    if (len + 2 >= val.length)
      val = realloc(val, len + 1, len + len + 2);

    len = umul0(val, 1, len, mull, mulh);
    val[0] = vsig ? -len : len;

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
    return mul == 0 ? mag[0] = 0 : umul0(mag, off, len, mul);
  }

  private static int umul0(final int[] mag, final int off, int len, int mul) {
    long carry = 0, low = mul & LONG_INT_MASK;
    for (mul = off, len += off; mul < len; mag[mul] = (int)(carry += (mag[mul] & LONG_INT_MASK) * low), ++mul, carry >>>= 32);
    if (carry != 0) mag[len++] = (int)carry;
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
    return mul == 0 ? mag[0] = 0 : umul0(mag, off, len, mul);
  }

  private static int umul0(final int[] mag, final int off, final int len, final long mul) {
    final long hmul = mul >>> 32;
    return hmul == 0 ? umul0(mag, off, len, (int)mul) : umul0(mag, off, len, mul & LONG_INT_MASK, hmul);
  }

  private static int umul0(final int[] val, final int off, int len, final long mull, final long mulh) {
    long carry, low, mul;
    int i;
    for (i = off, len += off, carry = 0; i < len; ++i, carry += low * mulh) { // Could this overflow?
      val[i] = (int)((mul = (low = val[i] & LONG_INT_MASK) * mull) + carry);
      carry = (mul >>> 32) + (carry >>> 32) + ((mul & LONG_INT_MASK) + (carry & LONG_INT_MASK) >>> 32);
    }

    val[len] = (int)carry;
    if (carry != 0 && (val[++len] = (int)(carry >>> 32)) != 0)
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
  public static int[] mul(final int[] val, final int[] mul) {
    try {
      int len = val[0]; if (len == 0) return val;
      int mlen = mul[0]; if (mlen == 0) return setToZero0(val);
      boolean sig = true;
      if (len < 0) { len = -len; sig = false; }
      if (mlen < 0) { mlen = -mlen; sig = !sig; }
      if (len <= 2 || mlen <= 2) return multiplySmall(val, len, mul, mlen, sig);
      if (len < KARATSUBA_THRESHOLD || mlen < KARATSUBA_THRESHOLD) return multiplyQuad(val, len, mul, mlen, sig);
      return karatsuba(val, len, mul, mlen, sig, Math.max(len, mlen) > 20000);
    }
    finally {
    }
  }

  private static int[] multiplySmall(int[] val, int len, final int[] mul, final int mlen, final boolean sig) {
    if (mlen == 1) {
      if (len + 2 >= val.length)
        val = realloc(val, len + OFF, len + len + OFF);

      len = umul0(val, OFF, len, mul[1]);
    }
    else if (len == 1) {
      final int m = val[1];
      val = copy(mul, mlen + OFF, val, mlen + 2);
      len = umul0(val, OFF, mlen, m);
    }
    else if (mlen == 2) {
      if (len + 2 >= val.length)
        val = realloc(val, len + OFF, len + len + OFF);

      len = umul0(val, OFF, len, mul[1] & LONG_INT_MASK, mul[2] & LONG_INT_MASK);
    }
    else {
      final long ml = val[1] & LONG_INT_MASK, mh = val[2] & LONG_INT_MASK;
      val = copy(mul, mlen + OFF, val, mlen + 3);
      len = umul0(val, OFF, mlen, ml, mh);
    }

    val[0] = sig ? len : -len;
    // _debugLenSig(val);
    return val;
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
   * @param sig The sign of the result.
   * @return The {@linkplain BigInt#val() value-encoded} result array.
   * @complexity O(n^2)
   */
  private static int[] multiplyQuad(final int[] x, final int xlen, final int[] y, final int ylen, final boolean sig) {
    int zlen = xlen + ylen + 1;
    final long ts = System.nanoTime();
    final int[] z = alloc(zlen);
    multiplyQuad(x, xlen, y, ylen, z);
    time += System.nanoTime() - ts;
    if (z[--zlen] == 0) --zlen;
    z[0] = sig ? zlen : -zlen;
    // _debugLenSig(z);
    return z;
  }

  private static int multiplyQuad(final int[] x, final int xlen, final int[] y, final int ylen, final int[] z) {
    return xlen < ylen ? multiplyQuad0(x, xlen, y, ylen, z) : multiplyQuad0(y, ylen, x, xlen, z);
  }

  private static int multiplyQuad0(final int[] x, int xlen, final int[] y, final int ylen, final int[] z) {
    int i, j;
    ++xlen; // Keeping this results in a performance boost

    long carry = 0, x0 = x[1] & LONG_INT_MASK;
    for (j = 1; j <= ylen; carry >>>= 32)
      z[j] = (int)(carry += x0 * (y[j++] & LONG_INT_MASK));

    z[j] = (int)carry;
    for (i = 2; i < xlen; i -= ylen) {
      x0 = x[i] & LONG_INT_MASK;
      carry = 0;
      for (j = 1; j <= ylen; carry >>>= 32)
        z[i] = (int)(carry += x0 * (y[j++] & LONG_INT_MASK) + (z[i++] & LONG_INT_MASK));

      z[i++] = (int)carry;
    }

    // Returning 0 here actually makes this faster than if this method returned
    // void, probably due to the method right above this one, so as to be able
    // to use ternary conditional.
    return 0;
  }

  /**
   * Multiplies the provided {@linkplain BigInt#val() value-encoded numbers}
   * using the Karatsuba algorithm, and returns the result. The caller can
   * choose to use a parallel version which is more suitable for larger numbers.
   * <p>
   * <i><b>Note:</b> The size of {@code val1} and {@code val2} must be the
   * same.</i>
   *
   * @param x The first {@linkplain BigInt#val() value-encoded number}.
   * @param xlen The number of limbs in {@code val1}.
   * @param y The second {@linkplain BigInt#val() value-encoded number}.
   * @param ylen The number of limbs in {@code val2}.
   * @param sig The sign of the result.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @return The result of the multiplication of the provided
   *         {@linkplain BigInt#val() value-encoded numbers}.
   * @complexity O(n^1.585)
   */
  static int[] karatsuba(int[] x, int xlen, int[] y, int ylen, final boolean sig, final boolean parallel) {
    final int mlen = Math.max(xlen, ylen);
    ++xlen;
    ++ylen;

    boolean a = false, b = false;
    if (a = y.length < xlen)
      y = reallocExact(y, ylen, xlen);
    else if (b = x.length < ylen)
      x = reallocExact(x, xlen, ylen);

    if (!a && ylen < xlen)
      for (--ylen; ++ylen < xlen; y[ylen] = 0);
    else if (!b && xlen < ylen)
      for (--xlen; ++xlen < ylen; x[xlen] = 0);

    int zlen = mlen + mlen;
    final int[] z = new int[zlen + OFF];
    if (parallel) {
      try {
        final ExecutorService pool = Executors.newFixedThreadPool(12);
        pmul(x, y, mlen, 0, 1, z, pool);
        pool.shutdown();
      }
      catch (final ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    else if (xlen < ylen) {
      kmul(x, y, 0, 0, mlen, z, 0);
    }
    else {
      kmul(y, x, 0, 0, mlen, z, 0);
    }

    for (; z[zlen] == 0 && zlen > 0; --zlen);
    z[0] = sig ? zlen : -zlen;
    // _debugLenSig(res);
    return z;
  }

  public static long time;

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * puts the result in {@code z}. Algorithm: Karatsuba
   *
   * @param x The first magnitude array.
   * @param y The second magnitude array.
   * @param off Offset for {@code x}, {@code y} and {@code z}.
   * @param yoff Offset for {@code y}.
   * @param len The length of each of the two partial arrays.
   * @param z The array into which the result is to be put.
   * @param zoff Offset for {@code z}.
   * @complexity O(n^1.585)
   */
  static void kmul(final int[] x, final int[] y, final int off, final int yoff, int len, final int[] z, final int zoff) {
    int i, j, k, l;
    long carry = 0;
    final int ooff = OFF + off;

    // x = x1*B^m + x0
    // y = y1*B^m + y0
    // xy = z2*B^2m + z1*B^m + z0
    // z2 = x1*y1, z0 = x0*y0, z1 = (x1+x0)(y1+y0)-z2-z0
    if (len <= 32) {
      final int zoff2 = zoff - off, yooff = ooff + yoff;

      long x0;
      for (x0 = x[ooff] & LONG_INT_MASK, k = yooff, j = OFF + zoff, len += zoff; j <= len; ++j, ++k, carry >>>= 32)
        z[j] = (int)(carry += x0 * (y[k] & LONG_INT_MASK));

      z[j] = (int)carry;
      for (len -= zoff2, i = ooff + 1, l = zoff2 + len + i - off; i <= len; z[l] = (int)carry, ++i, ++l, len -= yoff)
        for (x0 = x[i] & LONG_INT_MASK, j = yooff, k = i + zoff2, carry = 0, len += yoff; j <= len; ++j, ++k, carry >>>= 32)
          z[k] = (int)(carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK));
    }
    else {
      final int b = len >>> 1, b1 = b + 1, bb = b + b, lb = len - b, lblb = lb + lb, bbo = bb + OFF, lbo = lb + OFF, lbbo1 = b + lbo + 1, lbo1 = lbo + 1;

      final int[] x2 = new int[lbo + lbo1];
      for (i = 0, j = ooff + i, k = j + b; i < b; ++i, ++j, ++k, carry >>>= 32)
        x2[i] = (int)(carry += (x[k] & LONG_INT_MASK) + (x[j] & LONG_INT_MASK));

      if ((len & 1) != 0) x2[b] = x[off + bbo];
      if (carry != 0 && ++x2[b] == 0) ++x2[b1];

      for (i = lbo1, j = off + i + yoff - lbo, k = j + b, carry = 0; i < lbbo1; ++i, ++j, ++k, carry >>>= 32)
        x2[i] = (int)(carry += (y[k] & LONG_INT_MASK) + (y[j] & LONG_INT_MASK));

      if ((len & 1) != 0) x2[lbbo1] = y[off + bbo + yoff];
      if (carry != 0 && ++x2[lbbo1] == 0) ++x2[lbbo1 + 1];

      k = lb + (x2[lb] != 0 || x2[lbo + lbo] != 0 ? 1 : 0);
      final int kk = k + k, kkbb = kk + bb;
      final int[] z0 = new int[kkbb + lblb];
      kmul(x2, x2, -OFF, lbo1, k, z0, -OFF);
      kmul(x, y, off, yoff, b, z0, kk - OFF);
      kmul(x, y, off + b, yoff, lb, z0, kkbb - OFF);

      System.arraycopy(z0, kk, z, OFF + zoff, bb + lblb);

      // Add z1
      for (i = 0, j = b1 + zoff, k = kkbb, l = kk, carry = 0; i < bb; ++i, ++j, ++k, ++l, carry >>= 32)
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK) - (z0[k] & LONG_INT_MASK) - (z0[l] & LONG_INT_MASK));

      for (j = i + b1 + zoff, k = i + kkbb; i < lblb; ++i, ++j, ++k, carry >>= 32)
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK) - (z0[k] & LONG_INT_MASK));

      for (j = i + b1 + zoff, len = kk - OFF; i < len; ++i, ++j, carry >>= 32)
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK));

      if (carry != 0)
        while (++z[j++] == 0);
    }
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
    final int toIndex = len + OFF;
    final int b = toIndex >>> 1;

    final Future<int[]> left = pool.submit(() -> {
      final int[] z = new int[2 * b];
      if (lim == 0)
        kmul(val1, val2, off, 0, b, z, 0);
      else
        pmul(val1, val2, off, b, lim - 1, z, pool);

      return z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] z = new int[2 * (toIndex - b)];
      if (lim == 0)
        kmul(val1, val2, off + b, 0, len - b, z, 0);
      else
        pmul(val1, val2, off + b, len - b, lim - 1, z, pool);

      return z;
    });

    final int[] x2 = new int[toIndex - b + 1];
    final int[] y2 = new int[toIndex - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry += (val1[OFF + b + i] & LONG_INT_MASK) + (val1[OFF + i] & LONG_INT_MASK);
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0)
      x2[b] = val1[OFF + b + b];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry += (val2[OFF + b + i] & LONG_INT_MASK) + (val2[OFF + i] & LONG_INT_MASK);
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0) {
      y2[b] = val2[OFF + b + b];
    }

    if (carry != 0 && ++y2[b] == 0)
      ++y2[b + 1];

    final Future<int[]> mid = pool.submit(() -> {
      final int l = toIndex - b + (x2[toIndex - b] != 0 || y2[toIndex - b] != 0 ? 1 : 0);
      final int[] Z = new int[2 * l];
      if (lim == 0)
        kmul(x2, y2, 0, 0, l - OFF, Z, 0);
      else
        pmul(x2, y2, 0, l - OFF, lim - 1, Z, pool);

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
}