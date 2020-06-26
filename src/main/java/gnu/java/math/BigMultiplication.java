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
   * @param m The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public static int umul(final int[] val, final int idx, final int len, final int m) {
    long carry = 0;
    final long l = m & LONG_INT_MASK;
    for (int i = idx; i < len; ++i) {
      carry += (val[i] & LONG_INT_MASK) * l;
      val[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry == 0)
      return len;

    val[len] = (int)carry;
    return len + 1;
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
   * @param multiplier The amount to multiply (unsigned).
   * @complexity O(n)
   */
  public static int umul(final int[] val, final int idx, final int len, final long multiplier) {
    final long h2 = multiplier >>> 32;
    return h2 == 0 ? umul(val, idx, len, (int)multiplier) : umul(val, idx, len, multiplier & LONG_INT_MASK, h2);
  }

  public static int umul(final int[] val, final int idx, int len, final long l2, final long h2) {
    long carry = 0;
    long h1 = 0;
    long l1;
    long magl;
    for (int i = idx; i < len; ++i) {
      carry += h1; // Could this overflow?
      magl = val[i] & LONG_INT_MASK;
      l1 = magl * l2;
      h1 = magl * h2;
      val[i] = (int)(l1 + carry);
      carry = (l1 >>> 32) + (carry >>> 32) + ((l1 & LONG_INT_MASK) + (carry & LONG_INT_MASK) >>> 32);
    }

    carry += h1;
    val[len++] = (int)carry;
    if ((val[len] = (int)(carry >>> 32)) != 0)
      ++len;

    return len;
  }

  /**
   * Multiplies this number by the given (suitably small) BigInt. Uses a
   * quadratic algorithm which is often suitable for smaller numbers.
   *
   * @param mul The number to multiply with.
   * @complexity O(n^2)
   */
  static void mulQuad(final int[] res, final int[] val1, final int len1, final int[] val2, final int len2, final int fromIndex) {
    if (val1[0] < val2[0])
      mulQuad0(res, val1, len1, val2, len2, fromIndex);
    else
      mulQuad0(res, val2, len2, val1, len1, fromIndex);
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
  private static void mulQuad0(final int[] res, final int[] val1, final int len1, final int[] val2, final int len2, final int fromIndex) {
    int i, j, k, l;
    long v, r = 0;
    long val1l = val1[fromIndex] & LONG_INT_MASK;
    for (j = fromIndex; j < len2; ++j) {
      v = val1l * (val2[j] & LONG_INT_MASK) + r;
      res[j] = (int)v;
      r = v >>> 32;
    }

    res[len2] = (int)r;
    for (i = fromIndex + 1; i < len1; ++i) {
      val1l = val1[i] & LONG_INT_MASK;
      r = 0;
      k = i - fromIndex;
      for (j = fromIndex; j < len2; ++j) {
        l = j + k;
        v = val1l * (val2[j] & LONG_INT_MASK) + (res[l] & LONG_INT_MASK) + r;
        res[l] = (int)v;
        r = v >>> 32;
      }

      res[k + len2] = (int)r;
    }
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * returns the result. Algorithm: Karatsuba
   *
   * @param x The first magnitude array.
   * @param y The second magnitude array.
   * @param offset The offset, where the first element is residing.
   * @param n The length of each of the two partial arrays.
   * @complexity O(n^1.585)
   */
  static void kmul(final int[] z, final int[] x, final int[] y, final int offset, final int n) {
    // x = x1*B^m + x0
    // y = y1*B^m + y0
    // xy = z2*B^2m + z1*B^m + z0
    // z2 = x1*y1, z0 = x0*y0, z1 = (x1+x0)(y1+y0)-z2-z0
    if (n <= 32) {
      long carry = 0, tmp, xi = x[offset] & LONG_INT_MASK;
      for (int j = 0; j < n; ++j) {
        tmp = xi * (y[offset + j] & LONG_INT_MASK) + carry;
        z[j] = (int)tmp;
        carry = tmp >>> 32;
      }

      z[n] = (int)carry;
      for (int i = 1; i < n; ++i) {
        xi = x[offset + i] & LONG_INT_MASK;
        carry = 0;
        for (int j = 0; j < n; ++j) {
          tmp = xi * (y[offset + j] & LONG_INT_MASK) + (z[i + j] & LONG_INT_MASK) + carry;
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
    kmul(z2, x, y, offset + b, n - b);
    kmul(z0, x, y, offset, b);

    // FIXME: How to avoid new int[]?
    final int[] x2 = new int[n - b + 1];
    final int[] y2 = new int[n - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (x[offset + b + i] & LONG_INT_MASK) + (x[offset + i] & LONG_INT_MASK) + carry;
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      x2[b] = x[offset + b + b];

    if (carry != 0)
      if (++x2[b] == 0)
        ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (y[offset + b + i] & LONG_INT_MASK) + (y[offset + i] & LONG_INT_MASK) + carry;
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      y2[b] = y[offset + b + b];

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
   * @param offset The offset, where the first element is residing.
   * @param n The length of each of the two partial arrays.
   * @param lim The recursion depth up until which we will spawn new threads.
   * @param pool Where spawn threads will be added and executed.
   * @throws Various thread related exceptions.
   * @complexity O(n^1.585)
   */
  private static void pmul(final int[] z, final int[] x, final int[] y, final int offset, final int n, final int lim, final ExecutorService pool) throws ExecutionException, InterruptedException {
    final int b = n >>> 1;

    final Future<int[]> left = pool.submit(() -> {
      final int[] Z = new int[2 * b];
      if (lim == 0)
        kmul(Z, x, y, offset, b);
      else
        pmul(Z, x, y, offset, b, lim - 1, pool);

      return Z;
    });

    final Future<int[]> right = pool.submit(() -> {
      final int[] Z = new int[2 * (n - b)];
      if (lim == 0)
        kmul(Z, x, y, offset + b, n - b);
      else
        pmul(Z, x, y, offset + b, n - b, lim - 1, pool);

      return Z;
    });

    final int[] x2 = new int[n - b + 1];
    final int[] y2 = new int[n - b + 1];
    long carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (x[offset + b + i] & LONG_INT_MASK) + (x[offset + i] & LONG_INT_MASK) + carry;
      x2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0)
      x2[b] = x[offset + b + b];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b + 1];

    carry = 0;
    for (int i = 0; i < b; ++i) {
      carry = (y[offset + b + i] & LONG_INT_MASK) + (y[offset + i] & LONG_INT_MASK) + carry;
      y2[i] = (int)carry;
      carry >>>= 32;
    }

    if ((n & 1) != 0) {
      y2[b] = y[offset + b + b];
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
   * NOTE: Size of val1 and val2 must be the same!
   *
   * @param mul The number to multiply with.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @complexity O(n^1.585)
   */
  // Not fully tested on small numbers... fix naming?
  static int[] karatsuba(int[] val1, int[] val2, final boolean parallel) throws ExecutionException, InterruptedException {
    final int len1 = val1[0];
    final int len2 = val2[0];
    if (len2 < len1) {
      for (int i = len2; i < len1; ++i)
        val2[i] = 0;
    }

    if (len1 < len2) {
      for (int i = len1; i < len2; ++i)
        val1[i] = 0;
    }

    final int mlen = Math.max(len1, len2);
    final int[] z = new int[2 * mlen];
    z[0] = val1[1] * val2[1];
    z[1] = z.length;

    if (parallel) {
      final ExecutorService pool = Executors.newFixedThreadPool(12);
      pmul(z, val1, val2, 0, mlen, 1, pool);
      pool.shutdown();
    }
    else {
      kmul(z, val1, val2, 0, mlen);
    }

    return z;
  }

  private static int[] assignCopy(int[] target, final int[] source) {
    final int len = source[0]; // FIXME: What if val[0] > val.length?!?!!
    if (len >= target.length)
      target = new int[len + 2]; // FIXME: Add 2 just for shits and giggles?

    System.arraycopy(source, 0, target, 0, len);
    return target;
  }

  /**
   * NOTE: Does not guarantee proper signum for zero result. It is expected that
   * zero result is determined before execution of this method.
   */
  static int[] mul(int[] val1, int[] val2) {
    // FIXME: Determine the actual size necessary for the result before the
    // FIXME: execution of this method, and pass the result array into here.
    final int len1 = val1[0];
    final int len2 = val2[0];
    if (len2 <= 4 || len1 <= 4) {
      final int signum = val1[1] * val2[1];
      if (len2 == 3) {
        if (val1[0] == val1.length)
          val1 = realloc(val1);

        val1[0] = BigMultiplication.umul(val1, 2, val1[0], val2[2]);
      }
      else if (len1 == 3) {
        final int val10 = val1[2];
        val1 = val2.clone();
        val1 = assignCopy(val1, val2);
        val1[0] = BigMultiplication.umul(val1, 2, val1[0], val10);
      }
      else if (len2 == 4) {
        if (len1 + 2 >= val1.length)
          val1 = realloc(val1, 2 * len1 + 1);

        val1[0] = BigMultiplication.umul(val1, 2, val1[0], val2[2] & LONG_INT_MASK, val2[3] & LONG_INT_MASK);
      }
      else {
        final long val10 = val1[2] & LONG_INT_MASK;
        final long val11 = val1[3] & LONG_INT_MASK;
        val1 = assignCopy(val1, val2);
        val1[0] = BigMultiplication.umul(val1, 2, val1[0], val10, val11);
      }

      val1[1] = signum;
    }
    else if (len1 - 2 < 128 || len2 - 2 < 128 || (long)len1 * len2 < 1_000_000) {
      final int[] res = new int[len1 + len2 - 2];
      BigMultiplication.mulQuad(res, val1, val1[0], val2, val2[0], 2);
      res[0] = res[res.length - 1] == 0 ? res.length - 1 : res.length;
      res[1] = val1[1] * val2[1];
      return res;
    }
    else {
      final int signum = val1[1] * val2[1];
      if (val2.length < len1)
        val2 = realloc(val2, len1);
      else if (val1.length < len2)
        val1 = realloc(val1, len1);

      try {
        // FIXME: Tune thresholds
        if (Math.max(len1, len2) < 20000)
          val1 = BigMultiplication.karatsuba(val1, val2, false);
        else
          val1 = BigMultiplication.karatsuba(val1, val2, true);
      }
      catch (final ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }

      val1[0] = len1 + len2 - 2;
      val1[1] = signum;
    }

    return val1;
  }
}