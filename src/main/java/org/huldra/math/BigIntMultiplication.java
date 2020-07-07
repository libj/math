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

package org.huldra.math;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("javadoc")
abstract class BigIntMultiplication extends BigIntAddition {
  private static final long serialVersionUID = -4907342078241892616L;

  public static int[] mul(final int[] val, final int mul) {
    if (mul > 0)
      return mul(val, 1, mul);

    return mul(val, -1, -mul);
  }

  public static int[] mul(int[] val, final int sig, final int mul) {
    final boolean flipSig; int len = val[0]; if (len < 0) { len = -len; flipSig = sig >= 0; } else { flipSig = sig < 0; }
    if (len + 1 >= val.length)
      val = realloc(val);

    val[0] = umul(val, 1, len, mul);
    if (flipSig)
      val[0] = -val[0];

    _debugLenSig(val);
    return val;
  }

  public static int[] mul(final int[] val, long mul) {
    int sig = 1; if (mul < 0) { sig = -1; mul = -mul; }
    return mul(val, sig, mul);
  }

  public static int[] mul(int[] val, final int sig, final long mul) {
    final long ml = mul & LONG_INT_MASK;
    final long mh = mul >>> 32;
    if (mh == 0)
      return mul(val, sig, (int)ml);

    final boolean flipSig; int len = val[0]; if (len < 0) { len = -len; flipSig = sig >= 0; } else { flipSig = sig < 0; }
    if (len + 2 >= val.length)
      val = realloc(val, 2 * (len + 1));

    val[0] = umul(val, 1, len, ml, mh);
    if (flipSig)
      val[0] = -val[0];

    _debugLenSig(val);
    return val;
  }

  /**
   * Returns {@code val} after the multiplication of the unsigned multiplicand
   * in {@code val} of (of length {@code len}) by the unsigned
   * {@code multiplier}.
   *
   * @param val The multiplicand (unsigned) as input, and result (unsigned) as
   *          output.
   * @param multiplier The amount to multiply (unsigned).
   * @param len The significant length of the multiplicand in {@code val}.
   * @return {@code val} after the multiplication of the unsigned multiplicand
   *         in {@code val} of (of length {@code len}) by the unsigned
   *         {@code multiplier}.
   * @complexity O(n)
   */
  /**
   * Multiplies this number with an unsigned int.
   * <p>
   * NOTE: Does not check for zero!
   *
   * @param mul The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  // FIXME: Where should these methods go?
  public static int umul(final int[] mag, final int off, int len, final int mul) {
    if (mul == 0) {
      setToZero(mag);
      return 0;
    }

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
   * Returns {@code val} after the multiplication of the unsigned multiplicand
   * in {@code val} (of (of length {@code len})) by the unsigned {@code multiplier}.
   *
   * @param val The multiplicand (unsigned) as input, and result (unsigned) as
   *          output.
   * @param multiplier The amount to multiply (unsigned).
   * @param len The significant length of the multiplicand in {@code val}.
   * @return {@code val} after the multiplication of the unsigned multiplicand
   *         in {@code val} of (of (of length {@code len})) by the unsigned
   *         {@code multiplier}.
   * @complexity O(n)
   */
  /**
   * Multiplies this number with an unsigned long.
   * <p>
   * NOTE: val size must be at least len + 2
   * NOTE: Does not check for zero!
   *
   * @param mul The amount to multiply (unsigned).
   * @complexity O(n)
   */
  // FIXME: Where should these methods go?
  public static int umul(final int[] mag, final int off, final int len, final long mul) {
    final long h2 = mul >>> 32;
    return h2 == 0 ? umul(mag, off, len, (int)mul) : umul(mag, off, len, mul & LONG_INT_MASK, h2);
  }

  // FIXME: Where should these methods go?
  private static int umul(final int[] val, final int off, int len, final long ml, final long mh) {
    long carry = 0;
    long h0 = 0;
    long l0;
    long vl;
    len += off;
    for (int i = off; i < len; ++i) {
      carry += h0; // Could this overflow?
      vl = val[i] & LONG_INT_MASK;
      l0 = vl * ml;
      h0 = vl * mh;
      val[i] = (int)(l0 + carry);
      carry = (l0 >>> 32) + (carry >>> 32) + ((l0 & LONG_INT_MASK) + (carry & LONG_INT_MASK) >>> 32);
    }

    carry += h0;
    val[len] = (int)carry;
    if (carry == 0)
      return len - off;

    if ((val[++len] = (int)(carry >>> 32)) != 0)
      ++len;

    return len - off;
  }

  /**
   * NOTE: Does not guarantee proper signum for zero result. It is expected that
   * zero result is determined before execution of this method.
   */
  public static int[] mul(int[] val1, int[] val2) {
    // FIXME: Determine the actual size necessary for the result before the
    // FIXME: execution of this method, and pass the result array into here.
    int sig1 = 1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = val2[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    if (isZero(val1))
      return val1;

    if (isZero(val2)) {
      setToZero(val1);
      return val1;
    }

    if (len2 <= 2 || len1 <= 2) {
      final boolean flipSig = sig1 != sig2;
      if (len2 == 1) {
        if (len1 + 2 >= val1.length)
          val1 = realloc(val1, 2 * len1 + 1);

        len1 = umul(val1, 1, len1, val2[1]);
      }
      else if (len1 == 1) {
        final int m = val1[1];
        val1 = val2.clone();
        val1 = copy(val1, val2, len2 + 1, len2 + 2);
        len1 = umul(val1, 1, len2, m);
      }
      else {
        final long ml;
        final long mh;
        if (len2 == 2) {
          if (len1 + 2 >= val1.length)
            val1 = realloc(val1, 2 * len1 + 1);

          ml = val2[1] & LONG_INT_MASK;
          mh = val2[2] & LONG_INT_MASK;
        }
        else {
          ml = val1[1] & LONG_INT_MASK;
          mh = val1[2] & LONG_INT_MASK;
          val1 = copy(val1, val2, len2 + 1, len2 + 3);
          len1 = len2;
        }

        len1 = umul(val1, 1, len1, ml, mh);
      }

      val1[0] = flipSig ? -len1 : len1;
    }
    else if (len1 - 1 < 128 || len2 - 1 < 128 || (long)len1 * len2 < 1_000_000) {
      final int[] res = new int[len1 + len2 + 1];
      mulQuad(res, val1, len1 + 1, val2, len2 + 1, 1);
      val1 = res;

      len1 = val1[val1.length - 1] == 0 ? val1.length - 2 : val1.length - 1;
      val1[0] = sig1 != sig2 ? -len1 : len1;
    }
    else {
      final boolean flipLenSig = sig1 != sig2;
      if (val2.length < len1)
        val2 = realloc(val2, len1);
      else if (val1.length < len2)
        val1 = realloc(val1, len1);

      try {
        // FIXME: Tune thresholds
        val1 = karatsuba(val1, val2, Math.max(len1, len2) > 20000);
      }
      catch (final ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }

      len1 += len2 - 1;
      val1[0] = flipLenSig ? -len1 : len1;
    }

    _debugLenSig(val1);
    return val1;
  }

  /**
   * Multiplies this number by the given (suitably small) BigInt. Uses a
   * quadratic algorithm which is often suitable for smaller numbers.
   *
   * @param mul The number to multiply with.
   * @complexity O(n^2)
   */
  private static void mulQuad(final int[] res, final int[] val1, final int len1, final int[] val2, final int len2, final int off) {
    if (len1 < len2)
      mulQuad0(res, val1, len1, val2, len2, off);
    else
      mulQuad0(res, val2, len2, val1, len1, off);
  }

  /**
   * Multiplies two magnitude arrays and returns the result.
   * <p>
   * res must be int[] of size len1 + len2.
   *
   * @param val1 The first magnitude array.
   * @param len1 The length of the first array.
   * @param val2 The second magnitude array.
   * @param len2 The length of the second array.
   * @complexity O(n^2)
   */
  private static void mulQuad0(final int[] res, final int[] val1, final int len1, final int[] val2, final int len2, final int off) {
    int i, j, k, l;
    long v, r = 0;
    long val1l = val1[off] & LONG_INT_MASK;
    for (j = off; j < len2; ++j) {
      v = val1l * (val2[j] & LONG_INT_MASK) + r;
      res[j] = (int)v;
      r = v >>> 32;
    }

    res[len2] = (int)r;
    for (i = off + 1; i < len1; ++i) {
      val1l = val1[i] & LONG_INT_MASK;
      r = 0;
      k = i - off;
      for (j = off; j < len2; ++j) {
        l = j + k;
        v = val1l * (val2[j] & LONG_INT_MASK) + (res[l] & LONG_INT_MASK) + r;
        res[l] = (int)v;
        r = v >>> 32;
      }

      res[k + len2] = (int)r;
    }

    _debugLenSig(res);
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * returns the result. Algorithm: Karatsuba
   *
   * @param val1 The first magnitude array.
   * @param val2 The second magnitude array.
   * @param off The offset, where the first element is residing.
   * @param len The length of each of the two partial arrays.
   * @complexity O(n^1.585)
   */
  static void kmul(final int[] res, final int[] val1, final int[] val2, final int off, int len) {
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
      kmul(z2, val1, val2, off + b, len - b);
      kmul(z0, val1, val2, off, b);

      // FIXME: How to avoid new int[]?
      final int[] x2 = new int[len - b + 1];
      final int[] y2 = new int[len - b + 1];
      long carry = 0;
      for (int i = 0; i < b; ++i) {
        carry = (val1[off + b + i] & LONG_INT_MASK) + (val1[off + i] & LONG_INT_MASK) + carry;
        x2[i] = (int)carry;
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        x2[b] = val1[off + b + b];

      if (carry != 0)
        if (++x2[b] == 0)
          ++x2[b + 1];

      carry = 0;
      for (int i = 0; i < b; ++i) {
        carry = (val2[off + b + i] & LONG_INT_MASK) + (val2[off + i] & LONG_INT_MASK) + carry;
        y2[i] = (int)carry;
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        y2[b] = val2[off + b + b];

      if (carry != 0)
        if (++y2[b] == 0)
          ++y2[b + 1];

      final int l = len - b + (x2[len - b] != 0 || y2[len - b] != 0 ? 1 : 0);
      final int[] z1 = new int[2 * l];
      kmul(z1, x2, y2, 0, l);

      // FIXME: fromIndex & toIndex?!
      System.arraycopy(z0, 0, res, 0, 2 * b); // Add z0
      System.arraycopy(z2, 0, res, b + b, 2 * (len - b)); // Add z2

      // Add z1
      carry = 0;
      int i = 0;
      for (; i < 2 * b; ++i) {
        carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK) + carry;
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      for (; i < 2 * (len - b); ++i) {
        carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) + carry;
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      for (; i < z1.length; ++i) {
        carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) + carry;
        res[i + b] = (int)carry;
        carry >>= 32;
      }

      if (carry != 0)
        while (++res[i + b] == 0)
          ++i;
    }

    _debugLenSig(res);
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * returns the result. Algorithm: Parallel Karatsuba
   *
   * @param res Must be int[2 *n]
   * @param val1 The first magnitude array.
   * @param val2 The second magnitude array.
   * @param off The offset, where the first element is residing.
   * @param toIndex The length of each of the two partial arrays.
   * @param lim The recursion depth up until which we will spawn new threads.
   * @param pool Where spawn threads will be added and executed.
   * @throws Various thread related exceptions.
   * @complexity O(n^1.585)
   */
  private static void pmul(final int[] res, final int[] val1, final int[] val2, final int off, final int len, final int lim, final ExecutorService pool) throws ExecutionException, InterruptedException {
    final int toIndex = len + off;
    final int b = toIndex >>> 1;

    final Future<int[]> left = pool.submit(() -> {
      final int[] z = new int[2 * b];
      if (lim == 0)
        kmul(z, val1, val2, off, b);
      else
        pmul(z, val1, val2, off, b, lim - 1, pool);

      return z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] z = new int[2 * (toIndex - b)];
      if (lim == 0)
        kmul(z, val1, val2, off + b, len - b);
      else
        pmul(z, val1, val2, off + b, len - b, lim - 1, pool);

      return z;
    });

    final int[] x2 = new int[toIndex - b + 1];
    final int[] y2 = new int[toIndex - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (val1[off + b + i] & LONG_INT_MASK) + (val1[off + i] & LONG_INT_MASK) + carry;
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0)
      x2[b] = val1[off + b + b];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (val2[off + b + i] & LONG_INT_MASK) + (val2[off + i] & LONG_INT_MASK) + carry;
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((toIndex & 1) != 0) {
      y2[b] = val2[off + b + b];
    }

    if (carry != 0)
      if (++y2[b] == 0)
        ++y2[b + 1];

    final Future<int[]> mid = pool.submit(() -> {
      final int l = toIndex - b + (x2[toIndex - b] != 0 || y2[toIndex - b] != 0 ? 1 : 0);
      final int[] Z = new int[2 * l];
      if (lim == 0)
        kmul(Z, x2, y2, 0, l - off);
      else
        pmul(Z, x2, y2, 0, l - off, lim - 1, pool);

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
      carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK) + carry;
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < 2 * (toIndex - b); ++i) {
      carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) + carry;
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < z1.length; ++i) {
      carry = (res[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) + carry;
      res[i + b] = (int)carry;
      carry >>= 32;
    }

    if (carry != 0)
      while (++res[i + b] == 0)
        ++i;
  }

  /**
   * Multiplies this number by the given BigInt using the Karatsuba algorithm.
   * The caller can choose to use a parallel version which is more suitable for
   * larger numbers.
   * <p>
   * NOTE: Size of val1 and val2 must be the same!
   *
   * @param mul The number to multiply with.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @complexity O(n^1.585)
   */
  // FIXME: Not fully tested on small numbers... fix naming?
  static int[] karatsuba(int[] val1, int[] val2, final boolean parallel) throws ExecutionException, InterruptedException {
    int len1 = val1[0]; if (len1 < 0) { len1 = -len1; }
    int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }
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
      pmul(res, val1, val2, 1, mlen, 1, pool);
      pool.shutdown();
    }
    else {
      kmul(res, val1, val2, 1, mlen);
    }

    _debugLenSig(res);
    return res;
  }
}