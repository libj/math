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

import java.io.File;

abstract class BigIntMultiplication extends BigIntBinary {
  private static final boolean noNative = true;

  static {
    final String noNativeProp = System.getProperty("org.libj.math.BigInt.noNative");
//    noNative = noNativeProp != null && !noNativeProp.equals("false");
    if (!noNative)
      System.load(new File("target/libmath.so").getAbsolutePath());
  }

  private static final long serialVersionUID = -4907342078241892616L;

  /**
   * The threshold value for using Karatsuba multiplication. If the number of
   * ints in both mag arrays are greater than this number, then Karatsuba
   * multiplication will be used. This value is found experimentally to work
   * well.
   */
  static final int KARATSUBA_THRESHOLD = 110; // 110

  /**
   * The threshold value for using parallel Karatsuba multiplication. If the
   * number of ints in both mag arrays are greater than this number, then
   * Karatsuba multiplication will be used. This value is found experimentally
   * to work well.
   */
  static final int PARALLEL_KARATSUBA_THRESHOLD = 5000; // 20000

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
      val = realloc(val, len + 1, len * 2 + 1);

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
      val = realloc(val, len + 1, len * 2 + 2);

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

    val[i] = (int)carry;
    if (carry != 0 && (val[++i] = (int)(carry >>> 32)) != 0)
      ++i;

    return i - off;
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
    int len = val[0];
    if (len == 0)
      return val;

    int mlen = mul[0];
    if (mlen == 0)
      return setToZero0(val);

    boolean sig = true;
    if (len < 0) { len = -len; sig = false; }
    if (mlen < 0) { mlen = -mlen; sig = !sig; }

    if (len <= 2 || mlen <= 2) {
      if (mlen == 1) {
        if (len + 2 >= val.length)
          val = realloc(val, len + OFF, len * 2 + OFF);

        len = umul0(val, OFF, len, mul[1]);
      }
      else if (len == 1) {
        final int m = val[1];
        val = copy(mul, mlen + OFF, val, mlen + 2);
        len = umul0(val, OFF, mlen, m);
      }
      else if (mlen == 2) {
        if (len + 2 >= val.length)
          val = realloc(val, len + OFF, len * 2 + OFF);

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

    if (len < KARATSUBA_THRESHOLD || mlen < KARATSUBA_THRESHOLD)
      return mulQuad(val, len, mul, mlen, sig);

    return karatsuba(val, len, mul, mlen, sig);
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
  private static int[] mulQuad(final int[] x, final int xlen, final int[] y, final int ylen, final boolean sig) {
    int zlen = xlen + ylen + 1;
    final int[] z;
    if (x.length >= zlen + xlen) {
      if (noNative)
        javaMulQuadInline(y, ylen, x, xlen, zlen - 2);
      else
        nativeMulQuadInline(y, ylen, x, xlen, zlen - 2);

      z = x;
    }
    else {
      z = alloc(zlen);
      if (xlen < ylen) {
        if (noNative)
          javaMulQuad(x, xlen, y, ylen, z);
        else
          nativeMulQuad(x, xlen, y, ylen, z);
      }
      else {
        if (noNative)
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

    long carry = 0, x0 = x[1] & LONG_INT_MASK;
    for (j = 1; j <= ylen; ++j) {
      z[j] = (int)(carry += x0 * (y[j] & LONG_INT_MASK));
      carry >>>= 32;
    }

    z[j] = (int)carry;
    for (i = 2; i <= xlen; ++i) {
      x0 = x[i] & LONG_INT_MASK;
      for (carry = 0, j = 1, k = i; j <= ylen; ++j, ++k) {
        z[k] = (int)(carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK));
        carry >>>= 32;
      }

      z[k] = (int)carry;
    }
  }

  private static native void nativeMulQuadInline(int[] x, int xlen, int[] y, int ylen, int zlen);

  private static void javaMulQuadInline(final int[] x, final int xlen, final int[] y, final int ylen, int zlen) {
    int i, j, k, l;

    long carry = 0, x0 = x[1] & LONG_INT_MASK;
    zlen += 1;

    for (j = 1, k = zlen; j <= ylen; ++j, ++k) {
      y[j] = (int)(carry += x0 * ((y[k] = y[j]) & LONG_INT_MASK));
      carry >>>= 32;
    }

    y[k] = y[j];
    y[j] = (int)carry;
    for (i = 2; i <= xlen; ++i) {
      x0 = x[i] & LONG_INT_MASK;
      for (carry = 0, j = 1, k = i, l = zlen; j <= ylen; ++j, ++k, ++l) {
        y[k] = (int)(carry += x0 * (y[l] & LONG_INT_MASK) + (y[k] & LONG_INT_MASK));
        carry >>>= 32;
      }

      y[k] = (int)carry;
    }
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
   * @return The result of the multiplication of the provided
   *         {@linkplain BigInt#val() value-encoded numbers}.
   * @complexity O(n^1.585)
   */
  static int[] karatsuba(int[] x, int xlen, int[] y, int ylen, final boolean sig) {
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
    final int fullLen = OFF + zlen + len * 4; // FIXME: This length check is an approximation still
    if (!xNew && x.length > fullLen) {
      z = x;
      karatsuba(x, OFF, y, OFF, z, OFF, fullLen, 0, len);
    }
    else {
      z = alloc((zlen + OFF) + zlen); // (zlen + OFF) is all that's needed, but increase for optimization
      // final int[] z = alloc((zlen + OFF) * ((int)(1 + 10 * Math.random()))); // FIXME: Remove this!
      if (xlen < ylen) {
        karatsuba(x, OFF, y, OFF, z, OFF, zlen, 0, len);
      }
      else {
        karatsuba(y, OFF, x, OFF, z, OFF, zlen, 0, len);
      }
    }

    for (; z[zlen] == 0 && zlen > 0; --zlen);
    z[0] = sig ? zlen : -zlen;
    // _debugLenSig(res);
    return z;
  }

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
   * @complexity O(n^1.585)
   */
  private static void karatsuba(final int[] x, final int xoff, final int[] y, final int yoff, final int[] z, final int zoff, final int zlen, final int off, int len) {
    if (noNative)
      javaKaratsuba(x, xoff, y, yoff, z, zoff, zlen, off, len, len / PARALLEL_KARATSUBA_THRESHOLD);
    else
      nativeKaratsuba(x, xoff, y, yoff, z, zoff, zlen, off, len, len / PARALLEL_KARATSUBA_THRESHOLD);
  }

  private static native void nativeKaratsuba(final int[] x, final int xoff, final int[] y, final int yoff, final int[] z, final int zoff, final int zlen, final int off, final int len, final int parallel);

  private static void javaKaratsuba(final int[] x, final int xoff, final int[] y, final int yoff, final int[] z, final int zoff, final int zlen, final int off, final int len, final int parallel) {
    int i, j, k, l, m;
    long carry = 0;

    final int xoffoff = xoff + off, yoffoff = yoff + off;

    if (len <= 32) {
      final int yoffoffl = yoffoff + len, zoffl = zoff + len, xoffoffl = xoffoff + len;

      long x0 = x[xoffoff] & LONG_INT_MASK;
      for (k = yoffoff, j = zoff; j < zoffl; ++j, ++k) {
        z[j] = (int)(carry += x0 * (y[k] & LONG_INT_MASK));
        carry >>>= 32;
      }

      z[j] = (int)carry;
      for (i = xoffoff + 1, l = zoffl + 1, m = zoff + 1; i < xoffoffl; ++i, ++l, ++m) {
        carry = 0;
        x0 = x[i] & LONG_INT_MASK;
        for (j = yoffoff, k = m; j < yoffoffl; ++j, ++k) {
          z[k] = (int)(carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK));
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
      if (parallel == 0 && z.length >= (i = zoff + zlen) + k + 1) {
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
        tmp[i] = (int)(carry += (x[j] & LONG_INT_MASK) + (x[k] & LONG_INT_MASK));
        carry >>>= 32;
      }

      if ((len & 1) != 0)
        tmp[x2offl_b2b] = x[xoffoff + b2];

      if (carry != 0 && ++tmp[x2offl_b2b] == 0)
        ++tmp[x2offl_b2b + 1];

      carry = 0;
      for (i = y2offl_b1, j = yoffoff, k = yoffoff + b; i < y2offl_b1b; ++i, ++j, ++k) {
        tmp[i] = (int)(carry += (y[j] & LONG_INT_MASK) + (y[k] & LONG_INT_MASK));
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
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (tmp[i] & LONG_INT_MASK) - (tmp[k] & LONG_INT_MASK) - (tmp[l] & LONG_INT_MASK));
        carry >>= 32;
      }

      for (; i < tmpoffl_b2; ++i, ++j, ++k) {
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (tmp[i] & LONG_INT_MASK) - (tmp[k] & LONG_INT_MASK));
        carry >>= 32;
      }

      for (m = tmpoffrr - 1; i < m; ++i, ++j) {
        z[j] = (int)(carry += (z[j] & LONG_INT_MASK) + (tmp[i] & LONG_INT_MASK));
        carry >>= 32;
      }

      if (carry != 0)
        while (++z[j++] == 0);
    }
  }
}