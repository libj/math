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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("javadoc")
abstract class BigMultiplication extends BigAddition {
  private static final long serialVersionUID = -4907342078241892616L;

  /**
   * Returns {@code mag} after the multiplication of the unsigned multiplicand
   * in {@code mag} of (of length {@code len}) by the unsigned
   * {@code multiplier}.
   *
   * @param mag The multiplicand (unsigned) as input, and result (unsigned) as
   *          output.
   * @param multiplier The amount to multiply (unsigned).
   * @param len The significant length of the multiplicand in {@code mag}.
   * @return {@code mag} after the multiplication of the unsigned multiplicand
   *         in {@code mag} of (of length {@code len}) by the unsigned
   *         {@code multiplier}.
   * @complexity O(n)
   */
  /**
   * Multiplies this number with an unsigned int.
   * <p>
   * NOTE: Does not check for zero!
   *
   * @param multiplier The amount to multiply (unsigned).
   * @complexity O(n)
   */
  public static int umul(final int[] mag, int len, final int multiplier) {
    long carry = 0;
    final long l = multiplier & LONG_INT_MASK;
    for (int i = 0; i < len; ++i) {
      carry += (mag[i] & LONG_INT_MASK) * l;
      mag[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry == 0)
      return len;

    mag[len++] = (int)carry;
    return len;
  }

  /**
   * Returns {@code mag} after the multiplication of the unsigned multiplicand
   * in {@code mag} (of (of length {@code len})) by the unsigned {@code multiplier}.
   *
   * @param mag The multiplicand (unsigned) as input, and result (unsigned) as
   *          output.
   * @param multiplier The amount to multiply (unsigned).
   * @param len The significant length of the multiplicand in {@code mag}.
   * @return {@code mag} after the multiplication of the unsigned multiplicand
   *         in {@code mag} of (of (of length {@code len})) by the unsigned
   *         {@code multiplier}.
   * @complexity O(n)
   */
  /**
   * Multiplies this number with an unsigned long.
   * <p>
   * NOTE: mag size must be at least len + 2
   * NOTE: Does not check for zero!
   *
   * @param multiplier The amount to multiply (unsigned).
   * @complexity O(n)
   */
  public static int umul(final int[] mag, int len, final long multiplier) {
    final long h2 = multiplier >>> 32;
    if (h2 == 0)
      return umul(mag, len, (int)multiplier);

    return umul(mag, len, multiplier & LONG_INT_MASK, h2);
  }

  public static int umul(final int[] mag, int len, final long l2, final long h2) {
    long carry = 0;
    long h1 = 0;
    long l1;
    long magl;
    for (int i = 0; i < len; ++i) {
      carry += h1; // Could this overflow?
      magl = mag[i] & LONG_INT_MASK;
      l1 = magl * l2;
      h1 = magl * h2;
      mag[i] = (int)(l1 + carry);
      carry = (l1 >>> 32) + (carry >>> 32) + ((l1 & LONG_INT_MASK) + (carry & LONG_INT_MASK) >>> 32);
    }

    carry += h1;
    mag[len++] = (int)carry;
    mag[len++] = (int)(carry >>> 32);
    return len;
  }

  /**
   * Multiplies this number by the given (suitably small) BigInt. Uses a
   * quadratic algorithm which is often suitable for smaller numbers.
   *
   * @param mul The number to multiply with.
   * @complexity O(n^2)
   */
  static int[] smallMul(final int[] res, final int[] mag1, final int len1, final int[] mag2, final int len2) {
    if (isZero(mag1, len1))
      return mag1; // Remove?

    if (isZero(mag2, len2)) {
      setToZero(mag1);
      return mag1;
    }

    int ulen = len1, vlen = len2;
    int[] u = mag1, v = mag2; // ulen <= vlen
    if (vlen < ulen) {
      u = v;
      v = mag1;
      ulen = vlen;
      vlen = len1;
    }

    naiveMul(res, u, ulen, v, vlen); // TODO: remove function overhead.
    return res;
  }

  /**
   * Multiplies two magnitude arrays and returns the result.
   *
   * @param u The first magnitude array.
   * @param ulen The length of the first array.
   * @param v The second magnitude array.
   * @param vlen The length of the second array.
   * @return A ulen+vlen length array containing the result.
   * @complexity O(n^2)
   */
  static void naiveMul(final int[] res, final int[] u, final int ulen, final int[] v, final int vlen) {
    long carry = 0, tmp, ui = u[0] & LONG_INT_MASK;
    for (int j = 0; j < vlen; ++j) {
      tmp = ui * (v[j] & LONG_INT_MASK) + carry;
      res[j] = (int)tmp;
      carry = tmp >>> 32;
    }
    res[vlen] = (int)carry;
    for (int i = 1; i < ulen; ++i) {
      ui = u[i] & LONG_INT_MASK;
      carry = 0;
      for (int j = 0; j < vlen; ++j) {
        tmp = ui * (v[j] & LONG_INT_MASK) + (res[i + j] & LONG_INT_MASK) + carry;
        res[i + j] = (int)tmp;
        carry = tmp >>> 32;
      }
      res[i + vlen] = (int)carry;
    }
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * returns the result. Algorithm: Karatsuba
   *
   * @param x The first magnitude array.
   * @param y The second magnitude array.
   * @param off The offset, where the first element is residing.
   * @param n The length of each of the two partial arrays.
   * @complexity O(n^1.585)
   */
  static void kmul(final int[] z, final int[] x, final int[] y, final int off, final int n) {
    // x = x1*B^m + x0
    // y = y1*B^m + y0
    // xy = z2*B^2m + z1*B^m + z0
    // z2 = x1*y1, z0 = x0*y0, z1 = (x1+x0)(y1+y0)-z2-z0
    if (n <= 32) {
      long carry = 0, tmp, xi = x[off] & LONG_INT_MASK;
      for (int j = 0; j < n; ++j) {
        tmp = xi * (y[off + j] & LONG_INT_MASK) + carry;
        z[j] = (int)tmp;
        carry = tmp >>> 32;
      }

      z[n] = (int)carry;
      for (int i = 1; i < n; ++i) {
        xi = x[off + i] & LONG_INT_MASK;
        carry = 0;
        for (int j = 0; j < n; ++j) {
          tmp = xi * (y[off + j] & LONG_INT_MASK) + (z[i + j] & LONG_INT_MASK) + carry;
          z[i + j] = (int)tmp;
          carry = tmp >>> 32;
        }

        z[i + n] = (int)carry;
      }

      return;
    }

    final int b = n >>> 1;
    final int[] z2 = new int[2 * (n - b)];
    final int[] z0 = new int[2 * b];
    kmul(z2, x, y, off + b, n - b);
    kmul(z0, x, y, off, b);

    // FIXME: How to avoid new int[]?
    final int[] x2 = new int[n - b + 1];
    final int[] y2 = new int[n - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (x[off + b + i] & LONG_INT_MASK) + (x[off + i] & LONG_INT_MASK) + carry;
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      x2[b] = x[off + b + b];

    if (carry != 0)
      if (++x2[b] == 0)
        ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (y[off + b + i] & LONG_INT_MASK) + (y[off + i] & LONG_INT_MASK) + carry;
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      y2[b] = y[off + b + b];

    if (carry != 0)
      if (++y2[b] == 0)
        ++y2[b + 1];

    final int l = n - b + (x2[n - b] != 0 || y2[n - b] != 0 ? 1 : 0);
    final int[] z1 = new int[2 * l];
    kmul(z1, x2, y2, 0, l);

    System.arraycopy(z0, 0, z, 0, 2 * b); // Add z0
    System.arraycopy(z2, 0, z, b + b, 2 * (n - b)); // Add z2

    // Add z1
    carry = 0;
    int i = 0;
    for (; i < 2 * b; ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < 2 * (n - b); ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < z1.length; ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    if (carry != 0)
      while (++z[i + b] == 0)
        ++i;
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * returns the result. Algorithm: Parallel Karatsuba
   *
   * @param z Must be int[2 *n]
   * @param x The first magnitude array.
   * @param y The second magnitude array.
   * @param off The offset, where the first element is residing.
   * @param n The length of each of the two partial arrays.
   * @param lim The recursion depth up until which we will spawn new threads.
   * @param pool Where spawn threads will be added and executed.
   * @throws Various thread related exceptions.
   * @complexity O(n^1.585)
   */
  private static void pmul(final int[] z, final int[] x, final int[] y, final int off, final int n, final int lim, final ExecutorService pool) throws ExecutionException, InterruptedException {
    final int b = n >>> 1;

    final Future<int[]> left = pool.submit(() -> {
      final int[] Z = new int[2 * b];
      if (lim == 0)
        kmul(Z, x, y, off, b);
      else
        pmul(Z, x, y, off, b, lim - 1, pool);

      return Z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] Z = new int[2 * (n - b)];
      if (lim == 0)
        kmul(Z, x, y, off + b, n - b);
      else
        pmul(Z, x, y, off + b, n - b, lim - 1, pool);

      return Z;
    });

    final int[] x2 = new int[n - b + 1];
    final int[] y2 = new int[n - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (x[off + b + i] & LONG_INT_MASK) + (x[off + i] & LONG_INT_MASK) + carry;
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      x2[b] = x[off + b + b];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (y[off + b + i] & LONG_INT_MASK) + (y[off + i] & LONG_INT_MASK) + carry;
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0) {
      y2[b] = y[off + b + b];
    }

    if (carry != 0)
      if (++y2[b] == 0)
        ++y2[b + 1];

    final Future<int[]> mid = pool.submit(() -> {
      final int l = n - b + (x2[n - b] != 0 || y2[n - b] != 0 ? 1 : 0);
      final int[] Z = new int[2 * l];
      if (lim == 0)
        kmul(Z, x2, y2, 0, l);
      else
        pmul(Z, x2, y2, 0, l, lim - 1, pool);

      return Z;
    });

    final int[] z0 = left.get();
    System.arraycopy(z0, 0, z, 0, 2 * b);
    final int[] z2 = right.get();
    System.arraycopy(z2, 0, z, b + b, 2 * (n - b));

    final int[] z1 = mid.get();

    carry = 0;
    int i = 0;
    for (; i < 2 * b; ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) - (z0[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < 2 * (n - b); ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) - (z2[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    for (; i < z1.length; ++i) {
      carry = (z[i + b] & LONG_INT_MASK) + (z1[i] & LONG_INT_MASK) + carry;
      z[i + b] = (int)carry;
      carry >>= 32;
    }

    if (carry != 0)
      while (++z[i + b] == 0)
        ++i;
  }

  /**
   * Multiplies this number by the given BigInt using the Karatsuba algorithm.
   * The caller can choose to use a parallel version which is more suitable for
   * larger numbers.
   * <p>
   * NOTE: Size of mag1 and mag2 must be the same!
   *
   * @param mul The number to multiply with.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @complexity O(n^1.585)
   */
  // Not fully tested on small numbers... fix naming?
  static int[] karatsuba(int[] mag1, final int len1, int[] mag2, final int len2, final boolean parallel) throws ExecutionException, InterruptedException {
    if (len2 < len1) {
      for (int i = len2; i < len1; ++i)
        mag2[i] = 0;
    }

    if (len1 < len2) {
      for (int i = len1; i < len2; ++i)
        mag1[i] = 0;
    }

    final int mlen = Math.max(len1, len2);
    final int[] z = new int[2 * mlen];
    if (parallel) {
      final ExecutorService pool = Executors.newFixedThreadPool(12);
      pmul(z, mag1, mag2, 0, mlen, 1, pool);
      pool.shutdown();
    }
    else {
      kmul(z, mag1, mag2, 0, mlen);
    }

    return z;
  }
}