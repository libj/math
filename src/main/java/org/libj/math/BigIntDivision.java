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

abstract class BigIntDivision extends BigIntMultiplication {
  private static final long serialVersionUID = -4156041218135948540L;

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code int} divisor.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The provided {@linkplain BigInt#val() value-encoded dividend,}
   *         divided by the specified <i>unsigned</i> {@code int} divisor.
   * @complexity O(n)
   */
  public static int[] div(final int[] val, final int sig, final int div) {
    divRem(val, sig, div);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code int} divisor, and returns the
   * <i>absolute unsigned int</i> remainder.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned int</i> remainder resulting from the
   *         division of the provided {@linkplain BigInt#val() value-encoded
   *         dividend} by the specified <i>unsigned</i> {@code int} divisor.
   * @complexity O(n)
   */
  public static int divRem(final int[] val, final int sig, final int div) {
    int len1 = val[0];
    if (len1 == 0)
      return 0;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    return divRem0(val, len1, sig1, sig, div);
  }

  private static final long hbit = Long.MIN_VALUE;

  private static int divRem0(final int[] val, int len, final int sig, final int dsig, final int div) {
    long r = 0, rh;
    final long divl = div & LONG_MASK;
    boolean zeroes = true;
    if (div < 0) {
      long hq = (hbit - 1) / divl;
      if (hq * divl + divl == hbit)
        ++hq;

      final long rl = hbit - hq * divl;
      long q;
      for (int i = len; i >= 1; --i) {
        r = (r << 32) + (val[i] & LONG_MASK);
        rh = r >> 63;
        q = (hq & rh) + ((r & hbit - 1) + (rl & rh)) / divl;
        r -= q * divl;
        val[i] = (int)q;
        if (zeroes && (zeroes = q == 0))
          --len;
      }
    }
    else {
      for (int i = len; i >= 1; --i) {
        r <<= 32;
        r += val[i] & LONG_MASK;
        val[i] = (int)(r / divl);
        if (zeroes && (zeroes = val[i] == 0))
          --len;

        r %= divl;
      }
    }

    val[0] = sig != dsig ? -len : len;
    // _debugLenSig(val);
    return (int)r;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code int} divisor.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The provided {@linkplain BigInt#val() value-encoded dividend,}
   *         divided by the specified {@code int} divisor.
   * @complexity O(n)
   */
  public static int[] div(final int[] val, final int div) {
    divRem(val, div);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code int} divisor, and returns the remainder.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The remainder resulting from the division of the provided
   *         {@linkplain BigInt#val() value-encoded dividend} by the specified
   *         {@code int} divisor.
   * @complexity O(n)
   */
  public static int divRem(final int[] val, final int div) {
    int len = val[0];
    if (len == 0)
      return 0;

    int sig = 1; if (len < 0) { len = -len; sig = -1; }
    final int r = div < 0 ? divRem0(val, len, sig, -1, -div) : divRem0(val, len, sig, 1, div);
    return sig < 0 ? -r : r;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code long} divisor.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return The provided {@linkplain BigInt#val() value-encoded dividend,}
   *         divided by the specified <i>unsigned</i> {@code long} divisor.
   * @complexity O(n)
   */
  public static int[] div(final int[] val, final int sig, final long div) {
    int len1 = val[0];
    if (len1 == 0)
      return val;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    divRem0(val, len1, sig1, sig, div, div >>> 32);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code long} divisor, and returns the
   * <i>absolute unsigned {@code long}</i> remainder.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned {@code long}</i> remainder resulting from
   *         the division of the provided {@linkplain BigInt#val() value-encoded
   *         dividend} by the specified <i>unsigned</i> {@code long} divisor.
   * @complexity O(n)
   */
  public static long divRem(final int[] val, final int sig, final long div) {
    int len1 = val[0];
    if (len1 == 0)
      return 0;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    return divRem0(val, len1, sig1, sig, div, div >>> 32);
  }

  private static long divRem0(final int[] val, int len, final int sig, final int dsig, final long div, long divh) {
    if (divh == 0)
      return divRem0(val, len, sig, dsig, (int)div) & LONG_MASK;

    final long r;
    if (len <= 1) {
      r = val[1] & LONG_MASK;
      if (div == 1) {
        val[1] = 1;
        if (sig < 0 != dsig < 0)
          val[0] = -val[0];
      }
      else {
        setToZero0(val);
      }

      return r;
    }

    final int s = Integer.numberOfLeadingZeros((int)(divh));
    divh = div >>> 32 - s;
    final long dl = (div << s) & LONG_MASK;
    final long hbit = Long.MIN_VALUE;

    long u2 = 0;
    long u1 = val[len] >>> 32 - s;
    long u0 = (val[len] << s | val[len - 1] >>> 32 - s) & LONG_MASK;
    if (s == 0) {
      u1 = 0;
      u0 = val[len] & LONG_MASK;
    }

    long k, qhat, t, rhat, p;
    for (int j = len - 1; j >= 1; --j) {
      u2 = u1;
      u1 = u0;
      u0 = s > 0 && j > 0 ? (val[j] << s | val[j - 1] >>> 32 - s) & LONG_MASK : (val[j] << s) & LONG_MASK;

      k = (u2 << 32) + u1;
      qhat = (k >>> 1) / divh << 1;
      t = k - qhat * divh;
      if (t + hbit >= divh + hbit)
        ++qhat; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      rhat = k - qhat * divh;

      while (qhat + hbit >= (1L << 32) + hbit || qhat * dl + hbit > (rhat << 32) + u0 + hbit) { // Unsigned comparison
        --qhat;
        rhat += divh;
        if (rhat + hbit >= (1L << 32) + hbit)
          break;
      }

      // Multiply and subtract. Unfolded loop.
      p = qhat * dl;
      t = u0 - (p & LONG_MASK);
      u0 = t & LONG_MASK;
      k = (p >>> 32) - (t >> 32);
      p = qhat * divh;
      t = u1 - k - (p & LONG_MASK);
      u1 = t & LONG_MASK;
      k = (p >>> 32) - (t >> 32);
      t = u2 - k;
      u2 = t & LONG_MASK;

      val[j] = (int)qhat; // Store quotient digit. If we subtracted too much, add back.
      if (t < 0) {
        --val[j]; // Unfolded loop.
        t = u0 + dl;
        u0 = t & LONG_MASK;
        t >>>= 32;
        t = u1 + divh + t;
        u1 = t & LONG_MASK;
        t >>>= 32;
        u2 += t & LONG_MASK;
      }
    }

    val[len] = 0;
    while (val[--len] == 0);

    val[0] = sig < 0 != dsig < 0 ? -len : len;

    // _debugLenSig(val);

    final long tmp = u1 << 32 - s | u0 >>> s;
    r = s == 0 ? tmp : u2 << 64 - s | tmp;
    return r;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code long} divisor.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The provided {@linkplain BigInt#val() value-encoded dividend,}
   *         divided by the specified {@code long} divisor.
   * @complexity O(n)
   */
  public static int[] div(final int[] val, final long div) {
    divRem(val, div);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code long} divisor, and returns the {@code long} remainder.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The {@code long} remainder resulting from the division of the
   *         provided {@linkplain BigInt#val() value-encoded dividend} by the
   *         specified {@code long} divisor.
   * @complexity O(n)
   */
  public static long divRem(final int[] val, final long div) {
    int len = val[0];
    if (len == 0)
      return 0;

    int sig = 1; if (len < 0) { len = -len; sig = -1; }
    final long r = div < 0 ? divRem0(val, len, sig, -1, -div) : divRem0(val, len, sig, 1, div);
    return sig < 0 ? -r : r;
  }

  private static long divRem0(final int[] val, final int len, final int sig, final int dsig, final long div) {
    return divRem0(val, len, sig, dsig, div, div >>> 32);
  }

  /**
   * Divides the first {@linkplain BigInt#val() value-encoded dividend} by the
   * second {@linkplain BigInt#val() value-encoded divisor}, and stored the
   * quotient in {@code q}. The remainder will be stored in array of the
   * dividend ({@code val}).
   * <p>
   * <i>Hacker's Delight's implementation of Knuth's Algorithm D.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The {@linkplain BigInt#val() value-encoded divisor}.
   * @param q An array to store the quotient, which must be at least of length
   *          {@code Math.abs(val[0]) - Math.abs(div[0]) + 1}.
   * @complexity O(|val[0]|*|div[0]|)
   */
  public static void div(final int[] val, final int[] div, final int[] q) {
    int sig1 = 1, len1 = val[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = div[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    div0(val, len1, sig1, div, len2, sig2, q);
  }

  private static void div0(final int[] val, int len1, final int sig1, final int[] div, int len2, final int sig2, final int[] q) {
    ++len1;
    ++len2;

    final int fromIndex = 1;
    final long b = 1L << 32; // Number base (32 bits)
    long qhat; // Estimated quotient digit
    long rhat; // A remainder
    long p; // Product of two digits

    int s, i, j;
    long t, k;

    // Normalize by shifting v left just enough so that
    // its high-order bit is on, and shift u left the
    // same amount. We may have to append a high-order
    // digit on the dividend; we do that unconditionally.

    s = Integer.numberOfLeadingZeros(div[len2 - 1]);
    if (s > 0) { // In Java (x<<32)==(x<<0) so...
      // Normalize val2
      for (i = len2 - 1; i > fromIndex; --i)
        div[i] = (div[i] << s) | (div[i - 1] >>> 32 - s);

      div[fromIndex] = div[fromIndex] << s;

      // Normalize val1
      val[len1] = val[len1 - 1] >>> 32 - s;
      for (i = len1 - 1; i > fromIndex; --i)
        val[i] = (val[i] << s) | (val[i - 1] >>> 32 - s);

      val[fromIndex] = val[fromIndex] << s;
    }

    final long dh = div[len2 - 1] & LONG_MASK;
    final long dl = div[len2 - 2] & LONG_MASK;
    final long hbit = Long.MIN_VALUE;

    for (j = len1 - len2; j >= 0; --j) {
      // Compute estimate qhat of q[j]
      k = val[j + len2] * b + (val[j + len2 - 1] & LONG_MASK);
      qhat = (k >>> 1) / dh << 1;
      t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        ++qhat; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      rhat = k - qhat * dh;

      while (qhat + hbit >= b + hbit || qhat * dl + hbit > b * rhat + (val[j + len2 - 2] & LONG_MASK) + hbit) { // Unsigned comparison.
        qhat -= 1;
        rhat += dh;
        if (rhat + hbit >= b + hbit)
          break;
      }

      // Multiply and subtract
      k = 0;
      for (i = fromIndex; i < len2; ++i) {
        p = qhat * (div[i] & LONG_MASK);
        t = (val[i + j] & LONG_MASK) - k - (p & LONG_MASK);
        val[i + j] = (int)t;
        k = (p >>> 32) - (t >> 32);
      }

      t = (val[j + len2] & LONG_MASK) - k;
      val[j + len2] = (int)t;

      // Store quotient digit. If we subtracted too much, add back
      q[j + fromIndex] = (int)qhat;
      if (t < 0) {
        q[j + fromIndex] -= 1;
        k = 0;
        for (i = fromIndex; i < len2; ++i) {
          t = (val[i + j] & LONG_MASK) + (div[i] & LONG_MASK) + k;
          val[i + j] = (int)t;
          k = t >>> 32; // >>
        }

        val[j + len2] += (int)k;
      }
    }

    if (s > 0) {
      // Unnormalize val1
      for (i = fromIndex; i < len2 - 1; ++i)
        div[i] = div[i] >>> s | div[i + 1] << 32 - s;

      div[len2 - 1] >>>= s;

      // Unnormalize val2
      for (i = fromIndex; i < len1; ++i)
        val[i] = val[i] >>> s | val[i + 1] << 32 - s;

      val[len1] >>>= s;
    }

    int qlen = len1 - len2 + 1;
    for (; q[qlen] == 0; --qlen);
    q[0] = sig1 != sig2 ? -qlen : qlen;

    // _debugLenSig(q);

    // Set the new length of val1
    for (; val[len2] == 0; --len2);
    // Sign of remainder does not depend on the sign of the operand
    val[0] = sig1 < 0 ? -len2 : len2;

    // _debugLenSig(val);
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@linkplain BigInt#val() value-encoded divisor}.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the operation requires a larger array for the computation.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The provided {@linkplain BigInt#val() value-encoded dividend},
   *         divided by the specified {@linkplain BigInt#val() value-encoded
   *         divisor}.
   * @complexity O(n^2)
   */
  public static int[] div(int[] val, final int[] div) {
    int len1 = val[0];
    if (len1 == 0)
      return val;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = div[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    final boolean flipSig = sig1 < 0 != sig2 < 0;
    if (len2 <= 1) {
      divRem0(val, len1, sig1, sig2, div[1]);
      // FIXME: Can this be unwrapped?...
      if (val[0] < 0 != flipSig)
        val[0] = -val[0];
    }
    else {
      final int c = compareToAbs(val, len1, div, len2);
      if (c < 0) {
        setToZero0(val);
      }
      else if (c == 0) {
        val[0] = flipSig ? -1 : 1;
        val[1] = 1;
      }
      else {
        if (len1 + 1 == val.length)
          val = realloc(val, len1 + 1, len1 + 2); // We need an extra slot // FIXME: Can this extra slot be avoided?

        final int[] q = alloc(len1 - len2 + 2);
        div0(val, len1, sig1, div, len2, sig2, q);
        val = q;
      }
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@linkplain BigInt#val() value-encoded divisor}, and returns
   * the remainder.
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the operation requires a larger array for the computation.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The {@linkplain BigInt#val() value-encoded divisor}.
   * @return The remainder resulting from the division of the provided
   *         {@linkplain BigInt#val() value-encoded dividend} by the specified
   *         {@linkplain BigInt#val() value-encoded divisor}.
   * @complexity O(n^2)
   */
  public static int[] divRem(int[] val, int[] div) {
    int len1 = val[0];
    if (len1 == 0)
      return val;

    int sig1 = 1; if (len1 < 0) { len1 = -len1; sig1 = -1; }
    int sig2 = 1, len2 = div[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }
    // FIXME: Can this be unwrapped?...
    final boolean pos = val[0] < 0 == div[0] < 0;
    if (len2 > 1) {
      div = divRem0(val, div);
      if (pos != val[0] >= 0)
        val[0] = -val[0];

      val = div;
    }
    else {
      final int r = divRem0(val, len1, sig1, sig2, div[1]);
      if (pos != val[0] >= 0)
        val[0] = -val[0];

      val = assign(alloc(2), pos, r);
    }

    if (val[0] < 0 != sig1 < 0)
      val[0] = -val[0];

    return val;
  }

  private static int[] divRem0(int[] val1, final int[] val2) {
    final int c = compareToAbs(val1, val2);
    if (c == 0) {
      assign0(val1, 1, 1);
      return alloc(2);
    }

    int len1 = val1[0]; if (len1 < 0) { len1 = -len1; }
    ++len1;
    if (c < 0) {
      final int[] r = alloc(len1);
      System.arraycopy(val1, 0, r, 0, len1);
      val1.clone();
      setToZero0(val1);
      return r;
    }

    // Prepare the q array as the replacement for val1, accounting for the extra 1 required slot // FIXME: Can this extra slot be avoided?
    final int[] q = alloc(len1 == val1.length ? val1.length + 1 : val1.length);
    // Transfer val1 -> q
    System.arraycopy(val1, 0, q, 0, len1);

    // Do the div, with results going to val1 (which is where we want it to end up)
    div(q, val2, val1);
    return q;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code int} divisor, sets the dividend's
   * value to the remainder, and returns the remainder as an <i>absolute
   * unsigned {@code int}</i>.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned {@code int}</i> remainder resulting from
   *         the division of the provided {@linkplain BigInt#val() value-encoded
   *         dividend} by the specified <i>unsigned</i> {@code int} divisor.
   * @complexity O(n^2)
   */
  public static int rem(int[] val, final int sig, int div) {
    int vsig = 1, len = val[0]; if (len < 0) { len = -len; vsig = -1; }
    if (val.length < 2)
      val = realloc(val, 1, 2);

    val[1] = div = rem(val, 1, len, sig, div);
    val[0] = val[1] == 0 ? 0 : vsig;
    // _debugLenSig(val);
    return div;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code int} divisor, sets the dividend's value to the
   * remainder, and returns the remainder as an {@code int}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The {@code int} remainder resulting from the division of the
   *         provided {@linkplain BigInt#val() value-encoded dividend} by the
   *         specified {@code int} divisor.
   * @complexity O(n^2)
   */
  public static int rem(final int[] val, final int div) {
    final boolean sig = val[0] >= 0;
    final int r = div < 0 ? rem(val, -1, -div) : rem(val, 1, div);
    return sig ? r : -r;
  }

  /**
   * Divides the provided dividend in the magnitude array (starting at
   * {@code off}, with {@code len} limbs) by the specified <i>unsigned</i>
   * {@code int} divisor, sets the dividend's magnitude at {@code off} to the
   * remainder, and returns the remainder as an <i>absolute unsigned
   * {@code int}</i>.
   *
   * @param mag The dividend as a magnitude array.
   * @param off The offset of the first limb of the dividend.
   * @param len The number of limbs of the dividend.
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned {@code int}</i> remainder resulting from
   *         the division of the provided dividend magnitude array by the
   *         specified <i>unsigned</i> {@code int} divisor.
   * @complexity O(n)
   */
  public static int rem(final int[] mag, final int off, final int len, final int sig, final int div) {
    final int toIndex = len + off - 1;
    long r = 0;
    if (div < 0) {
      final long d = div & LONG_MASK;
      final long hbit = Long.MIN_VALUE;
      // Precompute hrem = (1<<63) % d
      // I.e. the remainder caused by the highest bit.
      long hrem = (hbit - 1) % d;
      if (++hrem == d)
        hrem = 0;

      for (int i = toIndex; i >= off; --i) {
        r = (r << 32) + (mag[i] & LONG_MASK);
        // Calculate rem %= d.
        // Do this by calculating the lower 63 bits and highest bit separately.
        // The highest bit remainder only gets added if it's set.
        r = ((r & hbit - 1) + (hrem & r >> 63)) % d;
        // The addition is safe and cannot overflow.
        // Because hrem < 2^32 and there's at least one zero bit in [62,32] if bit
        // 63 is set.
      }
    }
    else {
      final long d = div & LONG_MASK;
      for (int i = toIndex; i >= off; --i) {
        r <<= 32;
        r = (r + (mag[i] & LONG_MASK)) % d;
      }
    }

    return (int)r;
  }

  /**
   * Divides the provided dividend in the magnitude array (starting at
   * {@code off}, with {@code len} limbs) by the specified {@code int} divisor,
   * sets the dividend's magnitude at {@code off} to the remainder, and returns
   * the remainder as an {@code int}.
   *
   * @param mag The dividend as a magnitude array.
   * @param off The offset of the first limb of the dividend.
   * @param len The number of limbs of the dividend.
   * @param div The divisor.
   * @return The {@code int} remainder resulting from the division of the
   *         provided {@linkplain BigInt#val() value-encoded dividend} by the
   *         specified {@code int} divisor.
   * @complexity O(n)
   */
  public static int rem(final int[] mag, final int off, final int len, final int div) {
    return div < 0 ? rem(mag, off, len, -1, -div) : rem(mag, off, len, 1, div);
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified <i>unsigned</i> {@code long} divisor, sets the dividend's
   * value to the remainder, and returns the remainder as an <i>absolute
   * unsigned {@code long}</i>.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned {@code long}</i> remainder resulting from
   *         the division of the provided {@linkplain BigInt#val() value-encoded
   *         dividend} by the specified <i>unsigned</i> {@code long} divisor.
   * @complexity O(n)
   */
  public static long rem(final int[] val, final int sig, long div) {
    int len = val[0];
    if (len == 0)
      return 0;

    long modh = div >>> 32;
    if (modh == 0)
      return rem(val, sig, (int)div);

    int vsig = 1; if (len < 0) { len = -len; vsig = -1; }
    div = divRem0(val, len, vsig, sig, div, modh);
    modh = div >>> 32;
    val[1] = (int)div;
    if (modh == 0) {
      val[0] = div == 0 ? 0 : vsig < 0 ? -1 : 1;
    }
    else {
      val[0] = vsig < 0 ? -2 : 2;
      val[2] = (int)(modh);
    }

    // _debugLenSig(val);
    return div;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@code long} divisor, sets the dividend's value to the
   * remainder, and returns the remainder as a {@code long}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The divisor.
   * @return The {@code long} remainder resulting from the division of the
   *         provided {@linkplain BigInt#val() value-encoded dividend} by the
   *         specified {@code long} divisor.
   * @complexity O(n^2)
   */
  public static long rem(final int[] val, final long div) {
    final boolean sig = val[0] >= 0;
    final long r = div < 0 ? rem(val, -1, -div) : rem(val, 1, div);
    return sig ? r : -r;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@linkplain BigInt#val() value-encoded divisor}, and returns
   * the dividend array with its value replaced by the remainder.
   * <p>
   * Satisfies: {@code q * div + r = val}, where {@code q = floor(val / div)}
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the operation requires a larger array for the computation.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The {@linkplain BigInt#val() value-encoded divisor}.
   * @return The dividend array with its value replaced by the remainder
   *         resulting from the division of the provided
   *         {@linkplain BigInt#val() value-encoded dividend} by the
   *         specified {@linkplain BigInt#val() value-encoded divisor}.
   * @complexity O(n^2)
   */
  public static int[] rem(int[] val, final int[] div) {
    return isZero(val) ? val : rem0(val, div);
  }

  private static int[] rem0(int[] val, final int[] div) {
    // -7/-3 = 2, 2*-3 + -1
    // -7/3 = -2, -2*3 + -1
    // 7/-3 = -2, -2*-3 + 1
    // 7/3 = 2, 2*3 + 1
    int sig2 = 1, len2 = div[0]; if (len2 < 0) { len2 = -len2; sig2 = -1; }

    if (len2 <= 1) {
      rem(val, 1, div[1]);
      val[0] = val[1] == 0 ? 0 : val[0] < 0 ? -1 : 1;
    }
    else {
      int sig1 = 1, len1 = val[0]; if (len1 < 0) { len1 = -len1; sig1 = -1; }
      final int c = compareToAbs(val, div);
      if (c > 0) {
        if (len1 + 1 == val.length)
          val = realloc(val, len1 + 1, len1 + 2); // We need an extra slot // FIXME: Can this extra slot be avoided?

        final int[] q = threadLocal.get(len1 - len2 + 2);
        div0(val, len1, sig1, div, len2, sig2, q);
      }
      else if (c == 0) {
        setToZero0(val);
      }
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded dividend} by
   * the specified {@linkplain BigInt#val() value-encoded divisor}, and returns
   * the dividend array with its value replaced by the modulus.
   * <p>
   * <i><b>Note:</b> This method differs from {@link #rem(int[],int[])} in that
   * it always returns a <i>non-negative</i> result.</i>
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the operation requires a larger array for the computation.</i>
   *
   * @param val The {@linkplain BigInt#val() value-encoded dividend}.
   * @param div The {@linkplain BigInt#val() value-encoded divisor}.
   * @return The dividend array with its value replaced by the modulus resulting
   *         from the division of the provided {@linkplain BigInt#val()
   *         value-encoded dividend} by the specified {@linkplain BigInt#val()
   *         value-encoded divisor}.
   * @complexity O(n^2)
   */
  public static int[] mod(int[] val, final int[] div) {
    int len = val[0];
    if (len == 0)
      return val;

    val = rem0(val, div);
    len = val[0];
    if (len < 0)
      val = addSub0(val, -len, false, div, true);

    // _debugLenSig(val);
    return val;
  }
}