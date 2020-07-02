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

@SuppressWarnings("javadoc")
abstract class BigDivision extends BigMultiplication {
  private static final long serialVersionUID = -4156041218135948540L;

  public static int rem(final int[] val, final int signum, int mod) {
    int signum1, len = val[0]; if (len < 0) { len = -len; signum1 = -1; } else { signum1 = 1; }
    val[1] = mod = urem(val, 1, len, mod);
    val[0] = val[1] == 0 ? 0 : signum1;
    _debugLenSig(val);
    return mod;
  }

  public static int urem(final int[] mag, final int off, final int len, final int mod) {
    final int toIndex = len + off - 1;
    long r = 0;
    if (mod < 0) {
      final long d = mod & LONG_INT_MASK;
      final long hbit = Long.MIN_VALUE;
      // Precompute hrem = (1<<63) % d
      // I.e. the remainder caused by the highest bit.
      long hrem = (hbit - 1) % d;
      if (++hrem == d)
        hrem = 0;

      for (int i = toIndex; i >= off; --i) {
        r = (r << 32) + (mag[i] & LONG_INT_MASK);
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
      final long d = mod & LONG_INT_MASK;
      for (int i = toIndex; i >= off; --i) {
        r <<= 32;
        r = (r + (mag[i] & LONG_INT_MASK)) % d;
      }
    }

    return (int)r;
  }

  public static long rem(final int[] val, final int signum, long mod) {
    long modh = mod >>> 32;
    if (modh == 0)
      return rem(val, 1, (int)mod);

    final boolean signum1 = val[0] >= 0;
    mod = udiv(val, mod, modh);
    modh = mod >>> 32;
    val[1] = (int)mod;
    if (modh == 0) {
      val[0] = mod == 0 ? 0 : signum1 ? 1 : -1;
    }
    else {
      val[0] = signum1 ? 2 : -2;
      val[2] = (int)(modh);
    }

    _debugLenSig(val);
    return mod;
  }

  /**
   * Divides the unsigned dividend in {@code val} (up to {@code len}) by the
   * unsigned {@code divisor}, and returns the remainder. The quotient will be
   * set in {@code val}.
   *
   * @param val The dividend (unsigned) as input, and quotient (unsigned) as
   *          output.
   * @param d The {@code int} divisor (unsigned).
   * @param len The significant length of the dividend in {@code val}.
   * @return The remainder from the division of {@code val} by {@code divisor}.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  // FIXME: Javadoc: Assumes div > 0.
  public static int udiv(final int[] val, final int d) {
    int[] val$ = to$(val);
    final long x = udiv$(val$, d);

    int len = val[0]; if (len < 0) { len = -len; }
    long r = 0;
    final long dl = d & LONG_INT_MASK;
    boolean zeroes = true;
    if (d < 0) {
      final long hbit = Long.MIN_VALUE;
      long hq = (hbit - 1) / dl;
      if (hq * dl + dl == hbit)
        ++hq;

      final long rh = hbit - hq * dl;
      long q;
      for (int i = len; i >= 1; --i) {
        r = (r << 32) + (val[i] & LONG_INT_MASK);
        q = (hq & r >> 63) + ((r & hbit - 1) + (rh & r >> 63)) / dl;
        r -= q * dl;
        val[i] = (int)q;
        if (zeroes && (zeroes = q == 0))
          --len;
      }
    }
    else {
      for (int i = len; i >= 1; --i) {
        r <<= 32;
        r += val[i] & LONG_INT_MASK;
        val[i] = (int)(r / dl);
        if (zeroes && (zeroes = val[i] == 0))
          --len;

        r %= dl;
      }
    }

    val[0] = val[0] < 0 ? -len : len;
    _debugLenSig(val);
    return (int)r;
  }

  public static int udiv$(final int[] val, final int divisor) {
    return divisor < 0 ? safeUdiv(val, divisor) : unsafeUdiv(val, divisor);
  }

  // Assumes div > 0.
  private static int unsafeUdiv(final int[] val, final int d) {
    int len = val[0];
    final long dl = d & LONG_INT_MASK;
    long r = 0;
    for (int i = len - 1; i >= 2; --i) {
      r <<= 32;
      r += val[i] & LONG_INT_MASK;
      if ((val[i] = (int)(r / dl)) == 0)
        --len;

      r = r % dl;
    }

    val[0] = Math.max(3, len);
    if (isZero(val))
      val[1] = 0;

    return (int)r;
  }

  // Assumes div < 0.
  private static int safeUdiv(final int[] val, final int d) {
    final long dl = d & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;
    long hq = (hbit - 1) / dl;
    if (hq * dl + dl == hbit)
      ++hq;

    long r = 0;
    final long rh = hbit - hq * dl;
    int len = val[0];
    boolean zeroes = true;
    for (int i = len - 1; i >= 2; --i) {
      r = (r << 32) + (val[i] & LONG_INT_MASK);
      final long q = (hq & r >> 63) + ((r & hbit - 1) + (rh & r >> 63)) / dl;
      r -= q * dl;
      val[i] = (int)q;
      if (zeroes && (zeroes = q == 0))
        --len;
    }

    val[0] = Math.max(3, len);
    if (isZero(val))
      val[1] = 0;

    return (int)r;
  }

  /**
   * Divides the unsigned dividend in {@code val} (up to {@code len}) by the
   * unsigned {@code divisor}, and returns the remainder. The quotient will be
   * set in {@code val}.
   *
   * @param val The dividend (unsigned) as input, and quotient (unsigned) as
   *          output.
   * @param divisor The {@code long} divisor (unsigned).
   * @param len The significant length of the dividend in {@code val}.
   * @return The remainder from the division of {@code val} by {@code divisor}.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  /**
   * Divides this number with an unsigned long and returns the remainder.
   *
   * @param d The amount to divide with (treated as unsigned).
   * @return The absolute value of the remainder as an unsigned long.
   * @complexity O(n)
   */
  public static long udiv(final int[] val, final long d, long dh) {
    if (d == (d & LONG_INT_MASK))
      return udiv(val, (int)d) & LONG_INT_MASK;

//    int[] val$ = to$(val);
//    final long x = udiv$(val$, d, dh);

    int len = val[0]; if (len < 0) { len = -len; }
    if (len <= 1) {
      final long vl = val[1] & LONG_INT_MASK;
      if (d == 1)
        val[1] = 1;
      else
        setToZero(val);

      return vl;
    }

    final int s = Integer.numberOfLeadingZeros((int)(dh));
    dh = d >>> 32 - s;
    final long dl = (d << s) & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;

    long u2 = 0;
    long u1 = val[len] >>> 32 - s;
    long u0 = (val[len] << s | val[len - 1] >>> 32 - s) & LONG_INT_MASK;
    if (s == 0) {
      u1 = 0;
      u0 = val[len] & LONG_INT_MASK;
    }

    long k, qhat, t, rhat, p;
    for (int j = len - 1; j >= 1; j--) {
      u2 = u1;
      u1 = u0;
      u0 = s > 0 && j > 0 ? (val[j] << s | val[j - 1] >>> 32 - s) & LONG_INT_MASK : (val[j] << s) & LONG_INT_MASK;

      k = (u2 << 32) + u1;
      qhat = (k >>> 1) / dh << 1;
      t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        ++qhat; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      rhat = k - qhat * dh;

      while (qhat + hbit >= (1L << 32) + hbit || qhat * dl + hbit > (rhat << 32) + u0 + hbit) { // Unsigned comparison
        --qhat;
        rhat = rhat + dh;
        if (rhat + hbit >= (1L << 32) + hbit)
          break;
      }

      // Multiply and subtract. Unfolded loop.
      p = qhat * dl;
      t = u0 - (p & LONG_INT_MASK);
      u0 = t & LONG_INT_MASK;
      k = (p >>> 32) - (t >> 32);
      p = qhat * dh;
      t = u1 - k - (p & LONG_INT_MASK);
      u1 = t & LONG_INT_MASK;
      k = (p >>> 32) - (t >> 32);
      t = u2 - k;
      u2 = t & LONG_INT_MASK;

      val[j] = (int)qhat; // Store quotient digit. If we subtracted too much, add back.
      if (t < 0) {
        --val[j]; // Unfolded loop.
        t = u0 + dl;
        u0 = t & LONG_INT_MASK;
        t >>>= 32;
        t = u1 + dh + t;
        u1 = t & LONG_INT_MASK;
        t >>>= 32;
        u2 += t & LONG_INT_MASK;
      }
    }

    val[len] = 0;
    while (val[--len] == 0);

    val[0] = val[0] < 0 ? -len : len;

    _debugLenSig(val);

    final long tmp = u1 << 32 - s | u0 >>> s;
    return s == 0 ? tmp : u2 << 64 - s | tmp;
  }

  public static long udiv$(final int[] val, final long divisor, long dh) {
    int len = val[0];
    if (len == 3) {
      final long val0l = val[2] & LONG_INT_MASK;
      setToZero(val);
      return val0l;
    }

    final int s = Integer.numberOfLeadingZeros((int)(dh));
    dh = divisor >>> 32 - s;
    final long dl = (divisor << s) & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;

    long u2 = 0, u1 = val[len - 1] >>> 32 - s;
    long u0 = (val[len - 1] << s | val[len - 2] >>> 32 - s) & LONG_INT_MASK;
    if (s == 0) {
      u1 = 0;
      u0 = val[len - 1] & LONG_INT_MASK;
    }

    for (int j = len - 2; j >= 2; j--) {
      u2 = u1;
      u1 = u0;
      u0 = s > 0 && j > 0 ? (val[j] << s | val[j - 1] >>> 32 - s) & LONG_INT_MASK : (val[j] << s) & LONG_INT_MASK;

      long k = (u2 << 32) + u1;
      long qhat = (k >>> 1) / dh << 1;
      long t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        qhat++; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      long rhat = k - qhat * dh;

      while (qhat + hbit >= (1L << 32) + hbit || qhat * dl + hbit > (rhat << 32) + u0 + hbit) { // Unsigned comparison
        --qhat;
        rhat = rhat + dh;
        if (rhat + hbit >= (1L << 32) + hbit)
          break;
      }

      // Multiply and subtract. Unfolded loop.
      long p = qhat * dl;
      t = u0 - (p & LONG_INT_MASK);
      u0 = t & LONG_INT_MASK;
      k = (p >>> 32) - (t >> 32);
      p = qhat * dh;
      t = u1 - k - (p & LONG_INT_MASK);
      u1 = t & LONG_INT_MASK;
      k = (p >>> 32) - (t >> 32);
      t = u2 - k;
      u2 = t & LONG_INT_MASK;

      val[j] = (int)qhat; // Store quotient digit. If we subtracted too much, add back.
      if (t < 0) {
        --val[j]; // Unfolded loop.
        t = u0 + dl;
        u0 = t & LONG_INT_MASK;
        t >>>= 32;
        t = u1 + dh + t;
        u1 = t & LONG_INT_MASK;
        t >>>= 32;
        u2 += t & LONG_INT_MASK;
      }
    }

    --len;
    val[len] = 0;
    if (len > 3 && val[len - 1] == 0)
      --len;

    val[0] = len;
    if (isZero(val))
      val[1] = 0;

    final long tmp = u1 << 32 - s | u0 >>> s;
    return s == 0 ? tmp : u2 << 64 - s | tmp;
  }

  /**
   * Divides the first magnitude u[0..m) by v[0..n) and stores the resulting
   * quotient in q. The remainder will be stored in u, so u will be destroyed.
   * u[] must have room for an additional element, i.e. u[m] is a legal access.
   *
   * @param val1 The first magnitude array, the dividend.
   * @param val2 The second magnitude array, the divisor.
   * @param m The length of the first array.
   * @param n The length of the second array.
   * @param q An array of length at least n-m+1 where the quotient will be
   *          stored.
   * @complexity O(m*n)
   */
  // Hacker's Delight's implementation of Knuth's Algorithm D
  public static void div(final int[] val1, final int[] val2, final int[] q) {
    int signum1, len1 = val1[0]; if (len1 < 0) { len1 = -len1; signum1 = -1; } else { signum1 = 1; }
    ++len1;
    int signum2, len2 = val2[0]; if (len2 < 0) { len2 = -len2; signum2 = -1; } else { signum2 = 1; }
    ++len2;

    final int fromIndex = 1;
    final long b = 1L << 32; // Number base (32 bits).
    long qhat; // Estimated quotient digit.
    long rhat; // A remainder.
    long p; // Product of two digits.

    int s, i, j;
    long t, k;

    // Normalize by shifting v left just enough so that
    // its high-order bit is on, and shift u left the
    // same amount. We may have to append a high-order
    // digit on the dividend; we do that unconditionally.

    s = Integer.numberOfLeadingZeros(val2[len2 - 1]);
    if (s > 0) { // In Java (x<<32)==(x<<0) so...
      // Normalize val2
      for (i = len2 - 1; i > fromIndex; --i)
        val2[i] = (val2[i] << s) | (val2[i - 1] >>> 32 - s);

      val2[fromIndex] = val2[fromIndex] << s;

      // Normalize val1
      val1[len1] = val1[len1 - 1] >>> 32 - s;
      for (i = len1 - 1; i > fromIndex; --i)
        val1[i] = (val1[i] << s) | (val1[i - 1] >>> 32 - s);

      val1[fromIndex] = val1[fromIndex] << s;
    }

    final long dh = val2[len2 - 1] & LONG_INT_MASK;
    final long dl = val2[len2 - 2] & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;

    for (j = len1 - len2; j >= 0; --j) {
      // Compute estimate qhat of q[j].
      k = val1[j + len2] * b + (val1[j + len2 - 1] & LONG_INT_MASK);
      qhat = (k >>> 1) / dh << 1;
      t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        ++qhat; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      rhat = k - qhat * dh;

      while (qhat + hbit >= b + hbit || qhat * dl + hbit > b * rhat + (val1[j + len2 - 2] & LONG_INT_MASK) + hbit) { // Unsigned comparison.
        qhat -= 1;
        rhat += dh;
        if (rhat + hbit >= b + hbit)
          break;
      }

      // Multiply and subtract
      k = 0;
      for (i = fromIndex; i < len2; ++i) {
        p = qhat * (val2[i] & LONG_INT_MASK);
        t = (val1[i + j] & LONG_INT_MASK) - k - (p & LONG_INT_MASK);
        val1[i + j] = (int)t;
        k = (p >>> 32) - (t >> 32);
      }

      t = (val1[j + len2] & LONG_INT_MASK) - k;
      val1[j + len2] = (int)t;

      // Store quotient digit. If we subtracted too much, add back
      q[j + fromIndex] = (int)qhat;
      if (t < 0) {
        q[j + fromIndex] -= 1;
        k = 0;
        for (i = fromIndex; i < len2; ++i) {
          t = (val1[i + j] & LONG_INT_MASK) + (val2[i] & LONG_INT_MASK) + k;
          val1[i + j] = (int)t;
          k = t >>> 32; // >>
        }

        val1[j + len2] += (int)k;
      }
    }

    if (s > 0) {
      // Unnormalize val1.
      for (i = fromIndex; i < len2 - 1; ++i)
        val2[i] = val2[i] >>> s | val2[i + 1] << 32 - s;

      val2[len2 - 1] >>>= s;

      // Unnormalize val2
      for (i = fromIndex; i < len1; ++i)
        val1[i] = val1[i] >>> s | val1[i + 1] << 32 - s;

      val1[len1] >>>= s;
    }

    int qlen = len1 - len2 + 1;
    for (; q[qlen] == 0; --qlen);
    q[0] = signum1 != signum2 ? -qlen : qlen;

    _debugLenSig(q);

    // Set the new length of val1
    for (; val1[len2] == 0; --len2);
    // Sign of remainder does not depend on the sign of the operand
    val1[0] = signum1 < 0 ? -len2 : len2;

    _debugLenSig(val1);
  }

  /**
   * Sets this number to {@code (this mod m}). This method differs from
   * {@code rem} in that it always computes * <i>non-negative</i> result
   *
   * @param div The number to use in the division causing the remainder.
   * @see #rem
   */
  public static int[] mod(int[] val1, final int[] val2) {
    if (val2[0] <= 0)
      throw new ArithmeticException("BigInt: modulus not positive");

    val1 = rem(val1, val2);
    if (val1[0] < 0)
      val1 = add(val1, val2, true);

    _debugLenSig(val1);
    return val1;
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param div The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public static int[] rem(int[] val1, final int[] val2) {
    // -7/-3 = 2, 2*-3 + -1
    // -7/3 = -2, -2*3 + -1
    // 7/-3 = -2, -2*-3 + 1
    // 7/3 = 2, 2*3 + 1
    int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }

    if (len2 <= 1) {
      rem(val1, 1, val2[1]);
      val1[0] = val1[1] == 0 ? 0 : val1[0] < 0 ? -1 : 1;
    }
    else {
      int len1 = val1[0]; if (len1 < 0) { len1 = -len1; }
      final int c = compareAbsTo(val1, val2);
      if (c > 0) {
        ++len1;
        if (len1 == val1.length)
          val1 = realloc(val1, len1 + 1); // We need an extra slot.

        final int[] q = alloc(len1 - len2 + 1);
        div(val1, val2, q);
      }
      else if (c == 0) {
        setToZero(val1);
      }
    }

    _debugLenSig(val1);
    return val1;
  }

  /**
   * Divides this number by the given BigInt. Division by zero is undefined.
   *
   * @param div The number to divide with.
   * @complexity O(n^2)
   */
  public static int[] div(int[] val1, final int[] val2) {
    final boolean flipSignum = val1[0] < 0 != val2[0] < 0;
    int len1 = val1[0]; if (len1 < 0) { len1 = -len1; }
    int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }
    if (len2 <= 1) {
      udiv(val1, val2[1]);
      if (val1[0] < 0 != flipSignum)
        val1[0] = -val1[0];
    }
    else {
      final int c = compareAbsTo(val1, val2);
      if (c < 0) {
        setToZero(val1);
      }
      else if (c == 0) {
        val1[0] = flipSignum ? -1 : 1;
        val1[1] = 1;
      }
      else {
        ++len1;
        if (len1 == val1.length)
          val1 = realloc(val1, len1 + 1); // We need an extra slot.

        final int[] q = alloc(len1 - len2 + 1);
        div(val1, val2, q);
        val1 = q;
      }
    }

    _debugLenSig(val1);
    return val1;
  }

  public static int[] divRem(int[] val1, int[] val2) {
    final boolean signum1 = val1[0] >= 0;
    int len2 = val2[0]; if (len2 < 0) { len2 = -len2; }
    final boolean pos = val1[0] < 0 == val2[0] < 0;
    if (len2 > 1) {
      val2 = divRem0(val1, val2);
      if (pos != val1[0] >= 0)
        val1[0] = -val1[0];

      val1 = val2;
    }
    else {
      final int r = udiv(val1, val2[1]);
      if (pos != val1[0] >= 0)
        val1[0] = -val1[0];

      val1 = uassign(alloc(2), pos, r);
    }

    if (val1[0] >= 0 != signum1)
      val1[0] = -val1[0];

    return val1;
  }

  static int[] divRem0(int[] val1, final int[] val2) {
    final int c = compareAbsTo(val1, val2);
    if (c == 0) {
      uassign(val1, 1, 1);
      return alloc(2);
    }

    if (c < 0) {
      final int[] r = val1.clone();
      setToZero(val1);
      return r;
    }

    int len1 = val1[0]; if (len1 < 0) { len1 = -len1; }
    ++len1;

    // Prepare the q array as the replacement for val1, accounting for the extra 1 required slot
    final int[] q = alloc(len1 == val1.length ? val1.length + 1 : val1.length);
    // Transfer val1 -> q
    System.arraycopy(val1, 0, q, 0, len1);

    // Do the div, with results going to val1 (which is where we want it to end up)
    BigDivision.div(q, val2, val1);
    return q;
  }
}