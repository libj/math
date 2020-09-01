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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND ANY
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

abstract class BigIntMultiplication extends BigIntBinary {
  private static final long serialVersionUID = -4907342078241892616L;

  // For debugging
//  public static boolean record = true;
//  public static int[] X_Q = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_QN = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_QI = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_QIN = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_K = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_KI = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_KP = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
//  public static int[] X_KPI = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};

  /**
   * Factor to be applied Karatsuba thresholds. This factor was determined
   * experimentally to produce better results in general applications than the
   * exact factors that were determined in isolated tests.
   */
  private static final double THRESHOLD_FACTOR = 1.2;

  /**
   * The "z" threshold value for using Karatsuba multiplication. If the number
   * of ints in the output array is greater than this number, and both input
   * arrays are greater than {@link #KARATSUBA_THRESHOLD_X}, then Karatsuba
   * multiplication will be used.
   * <p>
   * This value is found experimentally to work well, but there is room for
   * further optimization. Optimizations for this threshold with magnitudes of
   * different lengths produce different results. When the two magnitudes differ
   * in length the most, the Karatsuba algorithm performs better with lower
   * values of this threshold. When the two magnitudes are equal in length, the
   * Karatsuba algorithm performs better with higher values of this threshold.
   * The difference between the optimal value for this threshold for magnitudes
   * with greatest differing lengths vs threshold for magnitudes with equal
   * lengths is ~25%. In summary, the relationship of this threshold across the
   * range of deltas between the magnitude lengths of the input arrays is not
   * linear.
   *
   * @see #KARATSUBA_THRESHOLD_X
   */
  static final int KARATSUBA_THRESHOLD_Z = (int)((NATIVE_THRESHOLD == Integer.MAX_VALUE ? 135 : 80) * THRESHOLD_FACTOR); // 135 : 80

  /**
   * The "x" threshold value for using Karatsuba multiplication. If the number
   * of ints in both input arrays is greater than this number, then
   * {@link #KARATSUBA_THRESHOLD_Z} will be evaluated to determine if Karatsuba
   * multiplication is to be used. This value is found experimentally to work
   * well.
   */
  static final int KARATSUBA_THRESHOLD_X = (int)((NATIVE_THRESHOLD == Integer.MAX_VALUE ? 70 : 50) * THRESHOLD_FACTOR); // 70 : 50

  /**
   * The threshold value for using Karatsuba squaring. If the number of ints in
   * the magnitude array is greater than this value, Karatsuba squaring will be
   * used. This value is found experimentally to work well.
   * <p>
   * Note: Values lower than {@code 30} for this threshold will result in errors
   * for in-place execution of the Karatsuba algorithm.
   */
  static final int KARATSUBA_SQUARE_THRESHOLD = (int)((NATIVE_THRESHOLD == Integer.MAX_VALUE ? 640 : 400) * THRESHOLD_FACTOR); // 640 : 400

  /**
   * Factor to be applied Parallel Karatsuba thresholds. This factor was
   * determined experimentally to produce better results in general applications
   * than the exact factors that were determined in isolated manner.
   */
  private static final double PARALLEL_THRESHOLD_FACTOR = 2;

  /**
   * The "z" threshold value for using Parallel Karatsuba multiplication. If the
   * number of ints in the output array is greater than this number, and both
   * input arrays are greater than {@link #PARALLEL_KARATSUBA_THRESHOLD_X}, then
   * Parallel Karatsuba multiplication will be used.
   * <p>
   * This value is found experimentally to work well, but there is room for
   * further optimization. Optimizations for this threshold with magnitudes of
   * different lengths produce different results. When the two magnitudes differ
   * in length the most, the Parallel Karatsuba algorithm performs better with
   * lower values of this threshold. When the two magnitudes are equal in
   * length, the Parallel Karatsuba algorithm performs better with higher values
   * of this threshold. The difference between the optimal value for this
   * threshold for magnitudes with greatest differing lengths vs threshold for
   * magnitudes with equal lengths is ~25%. In summary, the relationship of this
   * threshold across the range of deltas between the magnitude lengths of the
   * input arrays is not linear.
   *
   * @see #PARALLEL_KARATSUBA_THRESHOLD_X
   */
  static final int PARALLEL_KARATSUBA_THRESHOLD_Z = (int)((NATIVE_THRESHOLD == Integer.MAX_VALUE ? 1500 : 850) * PARALLEL_THRESHOLD_FACTOR); // 1500 : 850

  /**
   * The "x" threshold value for using Parallel Karatsuba multiplication. If the
   * number of ints in both input arrays is greater than this number, then
   * {@link #PARALLEL_KARATSUBA_THRESHOLD_Z} will be evaluated to determine if
   * Parallel Karatsuba multiplication is to be used. This value is found
   * experimentally to work well.
   */
  static final int PARALLEL_KARATSUBA_THRESHOLD_X = (int)((NATIVE_THRESHOLD == Integer.MAX_VALUE ? 120 : 100) * PARALLEL_THRESHOLD_FACTOR); // 120 : 100

  private static final int[] SMALL_5_POW = {1, 5, 5 * 5, 5 * 5 * 5, 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5, 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5};

  // Maximum size of cache of powers of 5 as BigInt
  private static final int MAX_FIVE_POW = 340;

  // Cache of big powers of 5 as BigInt
  private static final int[][] POW_5_CACHE;

  // Initialize BigInt cache of powers of 5
  static {
    int i = 0;
    for (POW_5_CACHE = new int[MAX_FIVE_POW][]; i < SMALL_5_POW.length; ++i) {
      POW_5_CACHE[i] = BigInt.assign(new int[2], SMALL_5_POW[i]);
    }

    for (int[] prev = POW_5_CACHE[i - 1]; i < MAX_FIVE_POW; ++i) {
      final int ext = 3 - (prev.length - prev[0]);
      final int[] next = ext == 0 ? prev.clone() : reallocExact(prev, prev[0] + 1, prev.length + ext);
      POW_5_CACHE[i] = prev = BigInt.mul(next, 5);
    }
  }

  /**
   * Compares the provided {@linkplain BigInt#val() value-encoded number} with
   * <code>5<sup>p5</sup> * 2<sup>p2</sup></code>, and returns one of
   * {@code -1}, {@code 0}, or {@code 1} if {@code val} is less than, equal to,
   * or greater than <code>5<sup>p5</sup> * 2<sup>p2</sup></code>, respectively.
   * <p>
   * <i><b>Note:</b> This function assumes {@code val} is positive.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to compare.
   * @param p5 The exponent of the power-of-five factor.
   * @param p2 The exponent of the power-of-two factor.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if {@code val} is less
   *         than, equal to, or greater than
   *         <code>5<sup>p5</sup> * 2<sup>p2</sup></code>, respectively.
   */
  static int compareToPow52(final int[] val, final int p5, final int p2) {
    if (p5 == 0) {
      final int wordcount = (p2 >> 5) + 1;
      final int len = val[0];
      if (len > wordcount)
        return 1;
      else if (len < wordcount)
        return -1;

      final int a = val[len];
      final int b = 1 << (p2 & 0x1f);
      return a == b ? checkZeroTail(val, len) : (a & LONG_MASK) < (b & LONG_MASK) ? -1 : 1;
    }

    // FIXME: Cache BigInt.shiftLeft(big5pow(p5).clone(), p2)
    return compareTo(val, BigInt.shiftLeft(big5pow(p5).clone(), p2));
  }

  /**
   * Determines whether all elements of a {@linkplain BigInt#val() value-encoded
   * array} are zero for all indices less than a given index.
   *
   * @param a The {@linkplain BigInt#val() value-encoded array} to be examined.
   * @param from The index strictly below which elements are to be examined.
   * @return {@code 0} if all elements below the {@code from} index (but above
   *         index of {@code 1}) are zero, {@code 1} otherwise.
   */
  private static int checkZeroTail(final int[] a, int from) {
    while (from > 1)
      if (a[--from] != 0)
        return 1;

    return 0;
  }

  /**
   * Multiplies the provided {@linkplain BigInt#val() value-encoded number} by
   * <code>5<sup>p5</sup> * 2<sup>p2</sup></code>.
   * <p>
   * <i><b>Note:</b> This function assumes the provided array is big enough for
   * the operation to be performed in place.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to multiply.
   * @param p5 The exponent of the power-of-five factor.
   * @param p2 The exponent of the power-of-two factor.
   * @return The provided {@code val} reference after its multiplication by
   *         <code>5<sup>p5</sup> * 2<sup>p2</sup></code>.
   */
  static int[] mulPow52(final int[] val, final int p5, final int p2) {
    if (val[0] == 0)
      return val;

    if (p5 != 0) {
      if (p5 < SMALL_5_POW.length)
        BigInt.mul(val, SMALL_5_POW[p5]);
      else
        BigInt.mul(val, big5pow(p5));
    }

    BigInt.shiftLeft(val, p2);
    return val;
  }

  /**
   * Returns the value of <code>5<sup>p</sup></code> as a
   * {@linkplain BigInt#val() value-encoded number}.
   *
   * @param p The exponent of {@code 5}.
   * @return The value of <code>5<sup>p</sup></code> as a
   *         {@linkplain BigInt#val() value-encoded number}.
   */
  private static int[] big5pow(final int p) {
    return p < MAX_FIVE_POW ? POW_5_CACHE[p] : big5powRec(p);
  }

  /**
   * Recursive function that computes the value of <code>5<sup>p</sup></code> as
   * a {@linkplain BigInt#val() value-encoded number}.
   *
   * @param p The exponent of {@code 5}.
   * @return The value of <code>5<sup>p</sup></code> as a
   *         {@linkplain BigInt#val() value-encoded number}.
   */
  private static int[] big5powRec(final int p) {
    if (p < MAX_FIVE_POW)
      return POW_5_CACHE[p];

    // Construct the value recursively.
    // In order to compute 5^p, compute its square root, 5^(p/2) and square.
    // Or, let q = p / 2, r = p -q, then 5^p = 5^(q+r) = 5^q * 5^r
    final int q = p >> 1;
    final int r = p - q;
    final int[] bigq = big5powRec(q);
    return r < SMALL_5_POW.length ? BigInt.mul0(bigq, 1, SMALL_5_POW[r], true) : BigInt.mul(bigq, big5powRec(r), true);
  }

  /**
   * Multiplies the provided number by an {@code int} multiplicand.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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
    return mul < 0 ? mul0(val, -1, -mul, false) : mul > 0 ? mul0(val, 1, mul, false) : setToZero0(val);
  }

  /**
   * Multiplies the provided number by an <i>unsigned</i> {@code int}
   * multiplicand.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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
    return mul == 0 ? setToZero0(val) : mul0(val, sig, mul, false);
  }

  static int[] mul0(int[] val, int sig, final int mul, final boolean allocExact) {
    int len = val[0]; if (len < 0) { len = -len; sig *= -1; }
    if (len + 2 >= val.length)
      val = allocExact ? reallocExact(val, len + OFF, len + 2) : realloc(val, len + OFF, len + 2);

    len = umul0(val, OFF, len, mul);
    val[0] = sig * len;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Multiplies the provided number by an {@code long} multiplicand.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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

    final long mulh = mul >>> 32;
    if (mulh == 0)
      return mul0(val, sig, (int)mul, false);

    if (len < 0) { len = -len; sig *= -1; }
    if (len + 3 >= val.length)
      val = realloc(val, len + OFF, len + 3);

    len = umul0(val, 1, len, mul & LONG_MASK, mulh);
    val[0] = sig * len;

    // _debugLenSig(val);
    return val;
  }

  /**
   * Multiplies the provided magnitude by an <i>unsigned</i> {@code int}
   * multiplicand.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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

  static int umul0(final int[] mag, final int off, int len, int mul) {
    long carry = 0, longMul = mul & LONG_MASK;
    for (mul = off, len += off; mul < len; mag[mul] = (int)(carry += (mag[mul++] & LONG_MASK) * longMul), carry >>>= 32);
    if (carry != 0) mag[len++] = (int)carry;
    return len - off;
  }

  /**
   * Multiplies the provided magnitude by an <i>unsigned</i> {@code long}
   * multiplicand.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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

  private static int umul0(final int[] mag, final int off, int len, final long mull, final long mulh) {
    long carry = 0, mul;
    int i = off;
    len += off;
    for (long v0; i < len; ++i) { // Could this overflow?
      mag[i] = (int)((mul = (v0 = mag[i] & LONG_MASK) * mull) + carry);
      carry = (mul >>> 32) + (carry >>> 32) + ((mul & LONG_MASK) + (carry & LONG_MASK) >>> 32) + v0 * mulh;
    }

    mag[i] = (int)carry;
    if (carry != 0 && (mag[++i] = (int)(carry >>> 32)) != 0)
      ++i;

    return i - off;
  }

  /**
   * Multiplies the provided number by a {@linkplain BigInt#val() value-encoded
   * multiplicand}.
   *
   * <pre>
   * val = val * mul
   * </pre>
   *
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
    return mul(val, mul, false);
  }

  static int[] mul(int[] val, int[] mul, final boolean allocExact) {
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
          val = allocExact ? reallocExact(val, len + OFF, len + 2) : realloc(val, len + OFF, len + 2);

        len = umul0(val, OFF, len, mul[1]);
      }
      else if (len == 1) {
        final int m = val[1];
        val = copy(mul, mlen + OFF, val, mlen + 2);
        len = umul0(val, OFF, mlen, m);
      }
      else if (mlen == 2) {
        if (len + 3 >= val.length)
          val = allocExact ? reallocExact(val, len + OFF, len + 3) : realloc(val, len + OFF, len + 3);

        len = umul0(val, OFF, len, mul[1] & LONG_MASK, mul[2] & LONG_MASK);
      }
      else {
        final long ml = val[1] & LONG_MASK, mh = val[2] & LONG_MASK;
        val = copy(mul, mlen + OFF, val, mlen + 3);
        len = umul0(val, OFF, mlen, ml, mh);
      }

      val[0] = sig ? len : -len;
      // _debugLenSig(val);
      return val;
    }

    final int zlen = len + mlen + 1;
    if (len < KARATSUBA_THRESHOLD_X || mlen < KARATSUBA_THRESHOLD_X || zlen < KARATSUBA_THRESHOLD_Z)
      return mulQuad(val, len, mul, mlen, zlen, sig, allocExact);

    return karatsuba(val, len, mul, mlen, zlen, sig);
  }

  private static int[] karatsuba(int[] x, int xlen, int[] y, int ylen, int zlen, final boolean sig) {
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

    final int[] z;
    final int inlineLen = zlen * 2 + 1;
//    final boolean PARALLEL = len > PARALLEL_KARATSUBA_THRESHOLD_X && zlen > PARALLEL_KARATSUBA_THRESHOLD_Z;
    if (!xNew && x.length > inlineLen) {
//      if (record) { final int X[] = PARALLEL ? X_KPI : X_KI; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2]; }

      z = x;
      z[zlen] = 0;
      karatsuba(x, y, z, inlineLen, len);
    }
    else {
//      if (record) { final int X[] = PARALLEL ? X_KP : X_K; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2]; }

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

  private static int[] mulQuad(final int[] x, int xlen, final int[] y, int ylen, int zlen, final boolean sig, final boolean allocExact) {
    final int[] z;
    if (x.length >= zlen + xlen) {
//      if (record) { final int X[] = xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD ? X_QI : X_QIN; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2]; }

      z = x;
      if (xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD)
        javaMulQuadInPlace(y, ylen, z, xlen, zlen - 2);
      else
        nativeMulQuadInPlace(y, ylen, z, xlen, zlen - 2);
    }
    else {
//      if (record) { final int X[] = xlen < NATIVE_THRESHOLD || ylen < NATIVE_THRESHOLD ? X_Q : X_QN; X[0] = Math.min(X[0], zlen); X[1] = Math.max(X[1], zlen); ++X[2]; }

      z = allocExact ? new int[zlen] : alloc(zlen);
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
      javaKaratsuba(x, OFF, y, OFF, z, OFF, zlen, 0, len, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
    else
      nativeKaratsuba(x, OFF, y, OFF, z, OFF, zlen, z.length, 0, len, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
  }

  private static native void nativeKaratsuba(int[] x, int xoff, int[] y, int yoff, int[] z, int zoff, int zlen, int zlength, int off, int len, int parallelThreshold, int parallelThresholdZ);

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
   * @param parallelThreshold Threshold of {@code len} for parallel execution.
   * @param parallelThresholdZ Threshold of {@code zlen} for parallel execution.
   * @complexity O(n^1.585)
   */
  private static void javaKaratsuba(final int[] x, final int xoff, final int[] y, final int yoff, final int[] z, final int zoff, final int zlen, final int off, final int len, final int parallelThreshold, final int parallelThresholdZ) {
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
      final boolean parallel = len > parallelThreshold && zlen > parallelThresholdZ;
      final int b = len >> 1, b2 = b * 2, ll = len * 2, l_b = len - b, l_b2 = l_b * 2;
      final int tmpoff, x2offl_b2, y2offl_b2;
      final int[] tmp;

      j = ll + l_b2 + 2; // length needed for (x2) computation
      k = j + l_b2 + 1;  // length needed for (y2) computation
      if (!parallel && z.length >= (i = zoff + zlen) + k + 1) {
        tmpoff = i;
        x2offl_b2 = j + i;
        y2offl_b2 = k + i;
        tmp = z;
      }
      else {
        tmpoff = 0;
        x2offl_b2 = j;
        y2offl_b2 = k;
        tmp = new int[y2offl_b2 + 1];
      }

      final int x2offl_b2b = x2offl_b2 + b, y2offl_b = x2offl_b2 + l_b, y2offl_b1 = y2offl_b + 1, y2offl_b1b = y2offl_b1 + b;
      tmp[x2offl_b2b] = tmp[y2offl_b1b] = tmp[y2offl_b] = tmp[y2offl_b2] = 0;

      for (i = x2offl_b2, j = xoffoff, k = xoffoff + b; i < x2offl_b2b; ++i, ++j, ++k) {
        tmp[i] = (int)(carry += (x[j] & LONG_MASK) + (x[k] & LONG_MASK));
        carry >>>= 32;
      }

      if ((len & 1) != 0) {
        tmp[x2offl_b2b] = x[xoffoff + b2];
      }

      if (carry != 0 && ++tmp[x2offl_b2b] == 0) {
        ++tmp[x2offl_b2b + 1];
      }

      carry = 0;
      for (i = y2offl_b1, j = yoffoff, k = yoffoff + b; i < y2offl_b1b; ++i, ++j, ++k) {
        tmp[i] = (int)(carry += (y[j] & LONG_MASK) + (y[k] & LONG_MASK));
        carry >>>= 32;
      }

      if ((len & 1) != 0) {
        tmp[y2offl_b1b] = y[yoffoff + b2];
      }

      if (carry != 0 && ++tmp[y2offl_b1b] == 0) {
        ++tmp[y2offl_b1b + 1];
      }

      final int tmpoffl_b2 = tmpoff + l_b2;
      final int tmplen = tmpoffl_b2 + l_b2 + 4;
      final int r = l_b + (tmp[y2offl_b] != 0 || tmp[y2offl_b2] != 0 ? 1 : 0);

      final int tmpoffrr = tmpoff + r * 2, tmpoffbb = tmpoff + b2, tmpoffrrbb = tmpoffrr + b2;
      if (!parallel) {
        javaKaratsuba(tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, 0, r, Integer.MAX_VALUE, Integer.MAX_VALUE);
        javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrr, tmplen, off, b, Integer.MAX_VALUE, Integer.MAX_VALUE);
        javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, off + b, l_b, Integer.MAX_VALUE, Integer.MAX_VALUE);
      }
      else {
        final Thread t1 = new Thread() {
          @Override
          public void run() {
            // System.err.print(".");
            javaKaratsuba(tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, 0, r, parallelThreshold * 2, parallelThresholdZ * 2);
          }
        };

        final Thread t2 = new Thread() {
          @Override
          public void run() {
            javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrr, tmplen, off, b, parallelThreshold * 2, parallelThresholdZ * 2);
          }
        };

        final Thread t3 = new Thread() {
          @Override
          public void run() {
            javaKaratsuba(x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, off + b, l_b, parallelThreshold * 2, parallelThresholdZ * 2);
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

      if (carry != 0) {
        while (++z[j++] == 0);
      }
    }
  }

  static int[] square(final int[] x, final int len) {
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
          javaSquareKaratsuba(x, len, x, fullLen, true, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
        else
          nativeSquareKaratsuba(x, len, x, fullLen, x.length, true, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
      }
      else {
        z = alloc(OFF + zlen * 2); // (OFF + zlen) is all that's needed, but increase to potentially reuse the original array
        // z = alloc((OFF + zlen) * ((int)(1 + 10 * Math.random()))); // FIXME: Remove this!
        if (len < NATIVE_THRESHOLD)
          javaSquareKaratsuba(x, len, z, zlen, false, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
        else
          nativeSquareKaratsuba(x, len, z, zlen, z.length, false, PARALLEL_KARATSUBA_THRESHOLD_X, PARALLEL_KARATSUBA_THRESHOLD_Z);
      }
    }

    for (; z[zlen] == 0 && zlen > 0; --zlen);
    z[0] = zlen;

    // _debugLenSig(z);
    return z;
  }

  private static native void nativeSquareKaratsuba(int[] x, int len, int[] z, int zlen, int zlength, boolean yCopy, int parallelThreshold, int parallelThresholdZ);

  private static void javaSquareKaratsuba(final int[] x, final int len, final int[] z, final int zlen, final boolean yCopy, final int parallelThreshold, final int parallelThresholdZ) {
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

    javaKaratsuba(x, OFF, y, OFF, z, OFF, zlen, 0, len, parallelThreshold, parallelThresholdZ);
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
      k = mulAdd(x, ++i, xlen, k, z, off + 1);
      addOne(z, off, zlen, j, k);
    }

    // Shift back up and set low bit
    primitiveLeftShift(z, zoff, zlen, 1);
    z[zoff] |= x[xoff] & 1;
    // _debugLenSig(z);
  }

  // shifts a up to len left n bits assumes no leading zeros, 0<=n<32
  private static void primitiveLeftShift(final int[] a, final int start, int end, final int n) {
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

  private static final int[][] e10 = new int[16384][];


  /**
   * Returns the provided {@linkplain BigInt#val() value-encoded number} raised
   * to the power of the given exponent.
   *
   * <pre>
   * val = val<sup>exp</sup>
   * </pre>
   *
   * This method returns {@code 0} in the following situations:
   * <ol>
   * <li>If {@code exp} is negative.</li>
   * <li>If the length of the resulting value array is greater than the
   * {@link BigInt#MAX_VAL_LENGTH}.</li>
   * </ol>
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the operation results in a number that requires a larger array.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to raise to
   *          the power of the given {@code exponent}.
   * @param exp The exponent to which {@code val} is to be raised.
   * @return <code>val<sup>exponent</sup></code>
   * @complexity O(n^2 exp log exp) - O(n log n exp log exp)
   */
  public static int[] pow(int[] val, final int exp) {
    if (exp <= 0)
      return exp == 0 ? assign(val, 1, 1) : setToZero(val);

    int len = val[0];
    if (len == 0)
      return val;

    if (len == 1 && val[1] == 10)
      return FastMath.E10(exp).clone();

    boolean sig = true; if (len < 0) { val[0] = len = -len; sig = false; }
    int[] res = val;

    // Factor out powers of two from the base, as the exponentiation of these
    // can be done by left shifts only. The remaining part can then be
    // exponentiated faster. Powers of two will be multiplied back at the end.
    final int powersOfTwo = getLowestSetBit(res);
    final long bitsToShiftLong = (long)powersOfTwo * exp;
    if (bitsToShiftLong > Integer.MAX_VALUE)
      return setToZero(val);

    final int bitsToShift = (int)bitsToShiftLong;
    final int remainingBits;

    // Factor the powers of two out quickly by shifting right, if needed.
    if (powersOfTwo > 0) {
      res = shiftRight(res, powersOfTwo);
      remainingBits = bitLength(res);
      if (remainingBits == 1) // Nothing left but +/- 1?
        return shiftLeft(assign(val, sig || (exp & 1) != 1 ? 1 : -1), bitsToShift);
    }
    else {
      remainingBits = bitLength(res);
      if (remainingBits == 1) // Nothing left but +/- 1?
        return assign(val, sig || (exp & 1) != 1 ? 1 : -1);
    }

    // This is a quick way to approximate the size of the result,
    // similar to doing log2[n] * exponent. This will give an upper bound
    // of how big the result can be, and which algorithm to use.
    final long scaleFactor = (long)remainingBits * exp;

    // Use slightly different algorithms for small and large operands.
    // See if the result will safely fit into a long. (Largest 2^63-1)
    if (len == 1 && scaleFactor <= 62) {
      // Small number algorithm. Everything fits into a long.
      final int newSign = sig || (exp & 1) != 1 ? 1 : -1;
      long result = 1;
      long baseToPow2 = res[len] & LONG_MASK;

      int workingExponent = exp;

      // Perform exponentiation using repeated squaring trick
      while (workingExponent != 0) {
        if ((workingExponent & 1) == 1)
          result *= baseToPow2;

        if ((workingExponent >>>= 1) != 0)
          baseToPow2 *= baseToPow2;
      }

      // Multiply back the powers of two (quickly, by shifting left)
      if (powersOfTwo > 0) {
        if (bitsToShift + scaleFactor <= 62) // Fits in long?
          return assign(val, (result << bitsToShift) * newSign);

        return shiftLeft(assign(val, result * newSign), bitsToShift);
      }

      return assign(val, result * newSign);
    }

    final long newLen = (long)bitLengthPos(val, len) * exp / Integer.SIZE;
    if (newLen > MAX_VAL_LENGTH)
      return setToZero(val);

    // Large number algorithm. This is basically identical to the algorithm
    // above, but calls multiply() and square() which may use more efficient
    // algorithms for large numbers.
    int[] answer = new int[Math.min(MAX_VAL_LENGTH, 4 * (int)newLen + (bitsToShift >> 5))];
    answer[0] = answer[1] = 1;

    int workingExponent = exp;
    // Perform exponentiation using repeated squaring trick
    while (workingExponent != 0) {
      if ((workingExponent & 1) == 1)
        answer = mul(answer, res);

      if ((workingExponent >>>= 1) != 0)
        res = square(res, res[0]);
    }

    // Multiply back the (exponentiated) powers of two (quickly, by shifting left)
    if (powersOfTwo > 0)
      answer = shiftLeft(answer, bitsToShift);

    return sig || (exp & 1) != 1 ? answer : neg(answer);
  }
}