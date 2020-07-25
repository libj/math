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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

abstract class BigIntMultiplication extends BigIntBinary {
  private static final long serialVersionUID = -4907342078241892616L;

  /**
   * The threshold value for using Karatsuba multiplication. If the number of
   * ints in both mag arrays are greater than this number, then Karatsuba
   * multiplication will be used. This value is found experimentally to work
   * well.
   */
  static final int KARATSUBA_THRESHOLD = 120; // 120

  /**
   * The threshold value for using parallel Karatsuba multiplication. If the
   * number of ints in both mag arrays are greater than this number, then
   * Karatsuba multiplication will be used. This value is found experimentally
   * to work well.
   */
  static final int PARALLEL_KARATSUBA_THRESHOLD = 5000; // 20000

  /**
   * The threshold value for using Karatsuba squaring.  If the number
   * of ints in the number are larger than this value,
   * Karatsuba squaring will be used.   This value is found
   * experimentally to work well.
   */
  static final int KARATSUBA_SQUARE_THRESHOLD = 128; // 128

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
  public static int[] mul(final int[] val, final int mul) {
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

  static int[] mul0(int[] val, int sig, final int mul) {
    int len = val[0]; if (len < 0) { len = -len; sig *= -1; }
    if (len + 1 >= val.length)
      val = realloc(val, len + OFF, len + 1);

    len = umul0(val, OFF, len, mul);
    val[0] = sig * len;
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
  public static int[] mul(int[] val, int sig, final long mul) {
    int len = val[0];
    if (len == 0)
      return val;

    if (mul == 0)
      return setToZero0(val);

    final long mull = mul & LONG_MASK, mulh = mul >>> 32;
    if (mulh == 0)
      return mul0(val, sig, (int)mull); // FIXME: Optimize?

    if (len < 0) { len = -len; sig *= -1; }
    if (len + 3 >= val.length)
      val = realloc(val, len + OFF, len + 3);

    len = umul0(val, 1, len, mull, mulh);
    val[0] = sig * len;

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

  private static native int nativeUmulInt(int[] mag, int off, int len, int mul);

  private static int umul0(final int[] mag, final int off, int len, int mul) {
    long carry = 0, longMul = mul & LONG_MASK;
    for (mul = off, len += off; mul < len; mag[mul] = (int)(carry += (mag[mul++] & LONG_MASK) * longMul), carry >>>= 32);
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
    return hmul == 0 ? umul0(mag, off, len, (int)mul) : umul0(mag, off, len, mul & LONG_MASK, hmul);
  }

  private static native int nativeUmulLong(int[] val, int off, int len, long mull, long mulh);

  private static int umul0(final int[] val, final int off, int len, final long mull, final long mulh) {
    long carry = 0, mul;
    int i = off;
    len += off;
    for (long v0; i < len; ++i) { // Could this overflow?
      val[i] = (int)((mul = (v0 = val[i] & LONG_MASK) * mull) + carry);
      carry = (mul >>> 32) + (carry >>> 32) + ((mul & LONG_MASK) + (carry & LONG_MASK) >>> 32) + v0 * mulh;
    }

    val[i] = (int)carry;
    if (carry != 0 && (val[++i] = (int)(carry >>> 32)) != 0)
      ++i;

    return i - off;
  }

  public static int[] X_Q = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
  public static int[] X_QN = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
  public static int[] X_QI = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
  public static int[] X_QIN = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
  public static int[] X_K = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
  public static int[] X_KI = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};

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
    int len = val[0];
    if (len == 0)
      return val;

    int mlen = mul[0];
    if (mlen == 0)
      return setToZero0(val);

    boolean sig = true;
    if (len < 0) { len = -len; sig = false; }

    if (val == mul)
      return square(val, len);

    if (mlen < 0) { mlen = -mlen; sig = !sig; }

    if (len <= 2 || mlen <= 2) {
      if (mlen == 1) {
        if (len + 2 >= val.length)
          val = realloc(val, len + OFF, len + 2);

        if (NATIVE_THRESHOLD == 0)
          len = nativeUmulInt(val, OFF, len, mul[1]);
        else
          len = umul0(val, OFF, len, mul[1]);
      }
      else if (len == 1) {
        final int m = val[1];
        val = copy(mul, mlen + OFF, val, mlen + 2);
        if (NATIVE_THRESHOLD == 0)
          len = nativeUmulInt(val, OFF, mlen, m);
        else
          len = umul0(val, OFF, mlen, m);
      }
      else if (mlen == 2) {
        if (len + 3 >= val.length)
          val = realloc(val, len + OFF, len + 3);

        if (NATIVE_THRESHOLD == 0)
          len = nativeUmulLong(val, OFF, len, mul[1] & LONG_MASK, mul[2] & LONG_MASK);
        else
          len = umul0(val, OFF, len, mul[1] & LONG_MASK, mul[2] & LONG_MASK);
      }
      else {
        final long ml = val[1] & LONG_MASK, mh = val[2] & LONG_MASK;
        val = copy(mul, mlen + OFF, val, mlen + 3);
        if (NATIVE_THRESHOLD == 0)
          len = nativeUmulLong(val, OFF, mlen, ml, mh);
        else
          len = umul0(val, OFF, mlen, ml, mh);
      }

      val[0] = sig ? len : -len;
      // _debugLenSig(val);
      return val;
    }

    if (len < KARATSUBA_THRESHOLD || mlen < KARATSUBA_THRESHOLD)
      return mulQuad(val, len, mul, mlen, sig);

    return karatsuba(val, len, mul, mlen, sig);
  }

  private static int[] karatsuba(int[] x, int xlen, int[] y, int ylen, final boolean sig) {
    final int len = Math.max(xlen, ylen);
    ++xlen;
    ++ylen;

    boolean yNew = false, xNew = false;
    if (yNew = y.length < xlen)
      y = reallocExact(y, ylen, xlen);
    else if (xNew = x.length < ylen)
      x = reallocExact(x, xlen, ylen);

    if (!yNew && ylen < xlen)
      for (--ylen; ++ylen < xlen; y[ylen] = 0);
    else if (!xNew && xlen < ylen)
      for (--xlen; ++xlen < ylen; x[xlen] = 0);

    int zlen = len * 2;
    final int[] z;
    final int fullLen = zlen * 2;
    if (!xNew && x.length > fullLen) {
      final int X[] = X_KI; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2];

      z = x;
      karatsuba(x, y, z, fullLen, len);
    }
    else {
      final int X[] = X_K; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2];

      z = alloc(OFF + zlen * 2); // (OFF + zlen) is all that's needed, but increase to potentially reuse the original array
      // z = alloc((OFF + zlen) * ((int)(1 + 10 * Math.random()))); // FIXME: Remove this!
      if (xlen < ylen) {
        karatsuba(x, y, z, zlen, len);
      }
      else {
        karatsuba(y, x, z, zlen, len);
      }
    }

    for (; z[zlen] == 0 && zlen > 0; --zlen);
    z[0] = sig ? zlen : -zlen;
    // _debugLenSig(res);
    return z;
  }

  private static int[] mulQuad(final int[] x, int xlen, final int[] y, int ylen, final boolean sig) {
    int zlen = xlen + ylen + 1;
    final int[] z;
    if (x.length >= zlen + xlen) {
      final int X[] = xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD ? X_QI : X_QIN; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2];

      z = x;
      if (xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD)
        javaMulQuadInPlace(y, ylen, z, xlen, zlen - 2);
      else
        nativeMulQuadInPlace(y, ylen, z, xlen, zlen - 2);
    }
    else {
      final int X[] = xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD ? X_Q : X_QN; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2];

      z = alloc(zlen);
      if (xlen < ylen) {
        if (xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD)
          javaMulQuad(x, xlen, y, ylen, z);
        else
          nativeMulQuad(x, xlen, y, ylen, z);
      }
      else {
        if (xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD)
          javaMulQuad(y, ylen, x, xlen, z);
        else
          nativeMulQuad(y, ylen, x, xlen, z);
      }
    }

    if (z[--zlen] == 0) --zlen;
    z[0] = sig ? zlen : -zlen;
    // _debugLenSig(z);
    return z;
  }

  private static native void nativeMulQuad(int[] x, int xlen, int[] y, int ylen, int[] z);

  private static void javaMulQuad(final int[] x, final int xlen, final int[] y, final int ylen, final int[] z) {
    int i, j, k;

    long carry = 0, x0 = x[1] & LONG_MASK;
    for (j = 1; j <= ylen; ++j) {
      z[j] = (int)(carry += x0 * (y[j] & LONG_MASK));
      carry >>>= 32;
    }

    z[j] = (int)carry;
    for (i = 2; i <= xlen; ++i) {
      x0 = x[i] & LONG_MASK;
      for (carry = 0, j = 1, k = i; j <= ylen; ++j, ++k) {
        z[k] = (int)(carry += x0 * (y[j] & LONG_MASK) + (z[k] & LONG_MASK));
        carry >>>= 32;
      }

      z[k] = (int)carry;
    }
  }

  private static native void nativeMulQuadInPlace(int[] x, int xlen, int[] y, int ylen, int zlen);

  private static void javaMulQuadInPlace(final int[] x, final int xlen, final int[] y, final int ylen, int zlen) {
    int i, j, k, l;

    long carry = 0, x0 = x[1] & LONG_MASK;
    zlen += OFF;

    for (j = OFF, l = zlen; j <= ylen; ++j, ++l) {
      y[j] = (int)(carry += x0 * ((y[l] = y[j]) & LONG_MASK));
      carry >>>= 32;
    }

    y[j] = (int)carry;
    for (i = 2; i <= xlen; ++i) {
      x0 = x[i] & LONG_MASK;
      for (carry = 0, j = OFF, k = i, l = zlen; j <= ylen; ++j, ++k, ++l) {
        y[k] = (int)(carry += x0 * (y[l] & LONG_MASK) + (y[k] & LONG_MASK));
        carry >>>= 32;
      }

      y[k] = (int)carry;
    }
  }

  private static void karatsuba(final int[] x, final int[] y, final int[] z, final int zlen, int len) {
    if (len < NATIVE_THRESHOLD)
      javaKaratsuba(x, OFF, y, OFF, z, OFF, zlen, 0, len, len / PARALLEL_KARATSUBA_THRESHOLD);
    else
      nativeKaratsuba(x, OFF, y, OFF, z, OFF, zlen, z.length, 0, len, len / PARALLEL_KARATSUBA_THRESHOLD);
  }

  private static native void nativeKaratsuba(int[] x, int xoff, int[] y, int yoff, int[] z, int zoff, int zlen, int zlength, int off, int len, int parallel);

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * puts the result in {@code z}. Algorithm: Karatsuba
   *
   * @param x The first magnitude array.
   * @param xoff Offset for {@code x}.
   * @param y The second magnitude array.
   * @param yoff Offset for {@code y}.
   * @param z The array into which the result is to be put.
   * @param zoff Offset for {@code z}.
   * @param zlen Length of {@code z}.
   * @param off Offset for {@code x}, {@code y} and {@code z}.
   * @param len The length of each of the two partial arrays.
   * @param parallel Count of parallel execution depths.
   * @complexity O(n^1.585)
   */
  private static void javaKaratsuba(final int[] x, final int xoff, final int[] y, final int yoff, final int[] z, final int zoff, final int zlen, final int off, final int len, final int parallel) {
    int i, j, k, l, m;

    final int xoffoff = xoff + off, yoffoff = yoff + off;
    long carry = 0;

    if (len <= 32) {
      final int yoffoffl = yoffoff + len, zoffl = zoff + len, xoffoffl = xoffoff + len;

      long x0 = x[xoffoff] & LONG_MASK;
      for (k = yoffoff, j = zoff; j < zoffl; ++j, ++k) {
        z[j] = (int)(carry += x0 * (y[k] & LONG_MASK));
        carry >>>= 32;
      }

      z[j] = (int)carry;
      for (i = xoffoff + 1, l = zoffl + 1, m = zoff + 1; i < xoffoffl; ++i, ++l, ++m) {
        carry = 0;
        x0 = x[i] & LONG_MASK;
        for (j = yoffoff, k = m; j < yoffoffl; ++j, ++k) {
          z[k] = (int)(carry += x0 * (y[j] & LONG_MASK) + (z[k] & LONG_MASK));
          carry >>>= 32;
        }

        z[l] = (int)carry;
      }
    }
    else {
      final int b = len >> 1, b2 = b * 2, ll = len * 2, l_b = len - b, l_b2 = l_b * 2;
      final int tmpoff, x2offl_b2, y2offl_b2;
      final int[] tmp;

      j = ll + l_b2 + 2; // length needed for (x2) computation
      k = j + l_b2;      // length needed for (y2) computation
      if (parallel == 0 && z.length >= (i = zoff + zlen) + k + 2) {
        tmpoff = i;
        x2offl_b2 = j + i;
        y2offl_b2 = k + i;
        tmp = z;
      }
      else {
        tmpoff = 0;
        x2offl_b2 = j;
        y2offl_b2 = k;
        tmp = new int[y2offl_b2 + 2];
      }

      final int x2offl_b2b = x2offl_b2 + b, y2offl_b = x2offl_b2 + l_b, y2offl_b1 = y2offl_b + 1, y2offl_b1b = y2offl_b1 + b;

      for (i = x2offl_b2, j = xoffoff, k = xoffoff + b; i < x2offl_b2b; ++i, ++j, ++k) {
        tmp[i] = (int)(carry += (x[j] & LONG_MASK) + (x[k] & LONG_MASK));
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        tmp[x2offl_b2b] = x[xoffoff + b2];

      if (carry != 0 && ++tmp[x2offl_b2b] == 0)
        ++tmp[x2offl_b2b + 1];

      carry = 0;
      for (i = y2offl_b1, j = yoffoff, k = yoffoff + b; i < y2offl_b1b; ++i, ++j, ++k) {
        tmp[i] = (int)(carry += (y[j] & LONG_MASK) + (y[k] & LONG_MASK));
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        tmp[y2offl_b1b] = y[yoffoff + b2];

      if (carry != 0 && ++tmp[y2offl_b1b] == 0)
        ++tmp[y2offl_b1b + 1];

      final int tmpoffl_b2 = tmpoff + l_b2;
      final int tmplen = tmpoffl_b2 + l_b2 + 3;
      final int r = l_b + (tmp[y2offl_b] != 0 || tmp[y2offl_b2] != 0 ? 1 : 0);
      final int tmpoffrr = tmpoff + r * 2, tmpoffbb = tmpoff + b2, tmpoffrrbb = tmpoffrr + b2;
      if (parallel == 0) {
        javaKaratsuba(tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, 0, r, 0);
        javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrr, tmplen, off, b, 0);
        javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, off + b, l_b, 0);
      }
      else {
        final Thread t1 = new Thread() {
          @Override
          public void run() {
            // System.err.print(".");
            javaKaratsuba(tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, 0, r, parallel - 1);
          }
        };

        final Thread t2 = new Thread() {
          @Override
          public void run() {
            javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrr, tmplen, off, b, parallel - 1);
          }
        };

        final Thread t3 = new Thread() {
          @Override
          public void run() {
            javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, off + b, l_b, parallel - 1);
          }
        };

        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();

        try {
          t1.join();
          t2.join();
          t3.join();
        }
        catch (final InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      System.arraycopy(tmp, tmpoffrr, z, zoff, ll);

      carry = 0;
      for (i = tmpoff, j = zoff + b, k = tmpoffrrbb, l = tmpoffrr, m = tmpoffbb; i < m; ++i, ++j, ++k, ++l) {
        z[j] = (int)(carry += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK) - (tmp[k] & LONG_MASK) - (tmp[l] & LONG_MASK));
        carry >>= 32;
      }

      for (; i < tmpoffl_b2; ++i, ++j, ++k) {
        z[j] = (int)(carry += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK) - (tmp[k] & LONG_MASK));
        carry >>= 32;
      }

      for (m = tmpoffrr - 1; i < m; ++i, ++j) {
        z[j] = (int)(carry += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK));
        carry >>= 32;
      }

      if (carry != 0)
        while (++z[j++] == 0);
    }
  }

  private static int[] square(final int[] x, final int len) {
    final int[] z;
    int zlen = len * 2;
    if (len < KARATSUBA_SQUARE_THRESHOLD) {
      final int xoff;
      if (x.length >= len + zlen + OFF) {
        xoff = zlen + OFF;
        System.arraycopy(x, OFF, x, xoff, len);
        z = x;
      }
      else {
        xoff = OFF;
        z = new int[zlen + xoff];
      }

      if (len < NATIVE_THRESHOLD)
        javaSquareQuad(x, xoff, len, z, OFF, zlen);
      else
        nativeSquareQuad(x, xoff, len, z, OFF, zlen);
    }
    else {
      final int fullLen = zlen * 2;
      if (x.length > fullLen) {
        z = x;

        if (len < NATIVE_THRESHOLD)
          javaSquareKaratsuba(x, len, x, fullLen, true);
        else
          nativeSquareKaratsuba(x, len, x, fullLen, x.length, len / PARALLEL_KARATSUBA_THRESHOLD, true);
      }
      else {
        z = alloc(OFF + zlen * 2); // (OFF + zlen) is all that's needed, but increase to potentially reuse the original array
        // z = alloc((OFF + zlen) * ((int)(1 + 10 * Math.random()))); // FIXME: Remove this!
        if (len < NATIVE_THRESHOLD)
          javaSquareKaratsuba(x, len, z, zlen, false);
        else
          nativeSquareKaratsuba(x, len, z, zlen, z.length, len / PARALLEL_KARATSUBA_THRESHOLD, false);
      }
    }

    for (; z[zlen] == 0 && zlen > 0; --zlen);
    z[0] = zlen;
    // _debugLenSig(z);
    return z;
  }

  private static native void nativeSquareKaratsuba(int[] x, int len, int[] z, int zlen, int zlength, int parallel, boolean yCopy);

  private static void javaSquareKaratsuba(final int[] x, final int len, final int[] z, final int zlen, final boolean yCopy) {
    final int[] y;
    if (yCopy) {
      // "In place" computation for (mag) requires a copy for (y), otherwise
      // we're reading and writing from the same array for (x) (y) and (z)
      y = new int[len + OFF];
      System.arraycopy(x, 0, y, 0, len + OFF);
    }
    else {
      y = x;
    }

    javaKaratsuba(x, OFF, y, OFF, z, OFF, zlen, 0, len, len / PARALLEL_KARATSUBA_THRESHOLD);
  }

  private static native void nativeSquareQuad(int[] x, int xoff, int xlen, int[] z, int zoff, int zlen);

  /**
   * The algorithm used here is adapted from Colin Plumb's C library.
   */
  private static void javaSquareQuad(final int[] x, final int xoff, int xlen, final int[] z, final int zoff, int zlen) {
    int i, j, k, off;
    long x0 = 0;

    xlen += xoff;
    zlen += zoff;

    // Store the squares, right shifted one bit (i.e., divided by 2)
    for (i = xlen - 1, j = zlen; i >= xoff; --i) {
      z[--j] = ((int)x0 << 31) | (int)((x0 = (x0 = x[i] & LONG_MASK) * x0) >>> 33);
      z[--j] = (int)(x0 >>> 1);
    }

    // Add in off-diagonal sums
    for (i = xoff, j = xlen - xoff, off = zoff; i < xlen; --j, off += 2) {
      k = x[i];
      k = mulAdd(x, ++i, xlen, k, z, off + 1); // FIXME: Optimize? (k = off), ++off...
      addOne(z, off, zlen, j, k);
    }

    // Shift back up and set low bit
    primitiveLeftShift(z, zoff, zlen, 1);
    z[zoff] |= x[xoff] & 1;
    // _debugLenSig(z);
  }

  // shifts a up to len left n bits assumes no leading zeros, 0<=n<32
  static void primitiveLeftShift(final int[] a, final int start, int end, final int n) {
//    if (end <= start || n == 0)
//      return;

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
  private static int mulAdd(final int[] x, int from, final int to, final int mul, final int[] z, int zoff) {
    final long tLong = mul & LONG_MASK;
    long carry = 0;

    while (from < to) {
      carry += (x[from++] & LONG_MASK) * tLong + (z[zoff] & LONG_MASK);
      z[zoff++] = (int)carry;
      carry >>>= 32;
    }

    // _debugLenSig(out);
    return (int)carry;
  }

  /**
   * Add one word to the number a mlen words into a. Return the resulting carry.
   */
  private static int addOne(final int[] x, int xoff, final int xlen, int mlen, final int carry) {
    xoff += mlen;
    final long t = (x[xoff] & LONG_MASK) + (carry & LONG_MASK);

    x[xoff] = (int)t;
    if ((t >>> 32) == 0)
      return 0;

    while (--mlen >= 0) {
      if (++xoff == xlen) // Carry out of number
        return 1;

      ++x[xoff];
      if (x[xoff] != 0)
        return 0;
    }

    // _debugLenSig(mag);
    return 1;
  }

  private static final Method m;
  static {
    try {
      m = BigInteger.class.getDeclaredMethod("multiplyToLen", int[].class, int.class, int[].class, int.class, int[].class);
      m.setAccessible(true);
    }
    catch (NoSuchMethodException | SecurityException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static void hackMultiplyToLen(final int[] x, final int xlen, final int[] y, final int ylen, final int[] z) {
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
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    z[zlen] = z[0];
    for (int i = 1; i < zlen; ++i) {
      int a = z[i];
      z[i] = z[zlen - i];
      z[zlen - i] = a;
    }
  }

  /**
   * Multiplies partial magnitude arrays x[off..off+n) and y[off...off+n) and
   * puts the result in {@code z}. Algorithm: Parallel Karatsuba
   *
   * @param x The first magnitude array.
   * @param y The second magnitude array.
   * @param off The offset of the first element.
   * @param len The length of each of the two partial arrays.
   * @param lim The recursion depth until which to spawn new threads.
   * @param pool Where spawned threads are to be added and executed.
   * @param z The array into which the result is to be put (length = 2 * n).
   * @complexity O(n^1.585)
   */
  private static void javaKmul(final int[] x, final int[] y, final int yoff, final int[] z, final int zoff, final int off, int len) {
    int i, j, k, l;
    long carry = 0;
    final int ooff = OFF + off;
    final int b = len >> 1, b1 = b + 1, bb = b + b, lb = len - b, lblb = lb + lb, bbo = bb + OFF, lbo = lb + OFF, lbo1 = lbo + 1, lbbo1 = b + lbo1;

    final int[] x2 = new int[lbo + lbo1];
    for (i = 0, j = ooff + i, k = j + b; i < b; ++i, ++j, ++k) {
      x2[i] = (int)(carry += (x[k] & LONG_MASK) + (x[j] & LONG_MASK));
      carry >>>= 32;
    }

    if ((len & 1) != 0)
      x2[b] = x[off + bbo];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b1];

    carry = 0;
    for (i = lbo1, j = yoff + off + i - lbo, k = j + b; i < lbbo1; ++i, ++j, ++k) {
      x2[i] = (int)(carry += (y[k] & LONG_MASK) + (y[j] & LONG_MASK));
      carry >>>= 32;
    }

    if ((len & 1) != 0)
      x2[lbbo1] = y[off + bbo + yoff];

    if (carry != 0 && ++x2[lbbo1] == 0)
      ++x2[lbbo1 + 1];

    final int len2 = lb + (x2[lb] != 0 || x2[lbo + lbo] != 0 ? 1 : 0);
    final int kk = len2 + len2, kkbb = kk + bb;
    final int[] z0 = new int[kkbb + lblb];

    javaKmul(x2, x2, lbo1, z0, -OFF, -OFF, len2);
    javaKmul(x, y, yoff, z0, kk - OFF, off, b);
    javaKmul(x, y, yoff, z0, kkbb - OFF, off + b, lb);

    System.arraycopy(z0, kk, z, OFF + zoff, bb + lblb);

    // Add z1
    for (i = 0, j = b1 + zoff, k = kkbb, l = kk, carry = 0; i < bb; ++i, ++j, ++k, ++l, carry >>= 32)
      z[j] = (int)(carry += (z[j] & LONG_MASK) + (z0[i] & LONG_MASK) - (z0[k] & LONG_MASK) - (z0[l] & LONG_MASK));

    for (j = i + b1 + zoff, k = i + kkbb; i < lblb; ++i, ++j, ++k, carry >>= 32)
      z[j] = (int)(carry += (z[j] & LONG_MASK) + (z0[i] & LONG_MASK) - (z0[k] & LONG_MASK));

    for (j = i + b1 + zoff, len = kk - OFF; i < len; ++i, ++j, carry >>= 32)
      z[j] = (int)(carry += (z[j] & LONG_MASK) + (z0[i] & LONG_MASK));

    if (carry != 0)
      while (++z[j++] == 0);

    // _debugLenSig(z);
  }
}