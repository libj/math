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

  public static long urem(final int[] mag, int len, final int mod) {
    return mod < 0 ? safeUrem(mag, len, mod) : unsafeUrem(mag, len, mod);
  }

  public static int urem(final int[] mag, int len, final long mod) {
    final long rem = udiv(mag, len, mod, mod >>> 32); // todo: opt?
    len = 2;
    mag[0] = (int)rem;
    if (rem == (rem & LONG_INT_MASK)) {
      --len;
    }
    else { // if(dig[0]==0) sign = 1;
      mag[1] = (int)(rem >>> 32);
    }

    return len;
  }

  // Assumes mod > 0.
  private static long unsafeUrem(final int[] mag, int len, final int mod) {
    long rem = 0, d = mod & LONG_INT_MASK;
    for (int i = len - 1; i >= 0; --i) {
      rem <<= 32;
      rem = (rem + (mag[i] & LONG_INT_MASK)) % d;
    }

    return rem;
  }

  // Assumes mod < 0.
  private static long safeUrem(final int[] mag, int len, final int mod) {
    final long d = mod & LONG_INT_MASK, hbit = Long.MIN_VALUE;
    // Precompute hrem = (1<<63) % d
    // I.e. the remainder caused by the highest bit.
    long hrem = (hbit - 1) % d;
    if (++hrem == d)
      hrem = 0;
    long rem = 0;
    for (int i = len - 1; i >= 0; --i) {
      rem = (rem << 32) + (mag[i] & LONG_INT_MASK);
      // Calculate rem %= d.
      // Do this by calculating the lower 63 bits and highest bit separately.
      // The highest bit remainder only gets added if it's set.
      rem = ((rem & hbit - 1) + (hrem & rem >> 63)) % d;
      // The addition is safe and cannot overflow.
      // Because hrem < 2^32 and there's at least one zero bit in [62,32] if bit
      // 63 is set.
    }

    return rem;
  }

  /**
   * Divides the unsigned dividend in {@code mag} (up to {@code len}) by the
   * unsigned {@code divisor}, and returns the remainder. The quotient will be
   * set in {@code mag}.
   *
   * @param mag The dividend (unsigned) as input, and quotient (unsigned) as
   *          output.
   * @param divisor The {@code int} divisor (unsigned).
   * @param len The significant length of the dividend in {@code mag}.
   * @return The remainder from the division of {@code mag} by {@code divisor}.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  // FIXME: Javadoc: Assumes div > 0.
  public static int udiv(final int[] mag, final int len, final int divisor) {
    return divisor < 0 ? safeUdiv(mag, len, divisor) : unsafeUdiv(mag, len, divisor);
  }

  // Assumes div > 0.
  private static int unsafeUdiv(final int[] mag, final int len, final int div) {
    final long d = div & LONG_INT_MASK;
    long rem = 0;
    for (int i = len - 1; i >= 0; --i) {
      rem <<= 32;
      rem += (mag[i] & LONG_INT_MASK);
      mag[i] = (int)(rem / d); // Todo: opt?
      rem = rem % d;
    }

    return (int)rem;
  }

  // Assumes div < 0.
  private static int safeUdiv(final int[] mag, final int len, final int div) {
    final long d = div & LONG_INT_MASK, hbit = Long.MIN_VALUE;
    long hq = (hbit - 1) / d;
    if (hq * d + d == hbit)
      ++hq;

    final long hrem = hbit - hq * d;
    long rem = 0;
    for (int i = len - 1; i >= 0; --i) {
      rem = (rem << 32) + (mag[i] & LONG_INT_MASK);
      final long q = (hq & rem >> 63) + ((rem & hbit - 1) + (hrem & rem >> 63)) / d;
      rem -= q * d;
      mag[i] = (int)q;
    }

    return (int)rem;
  }

  /**
   * Divides the unsigned dividend in {@code mag} (up to {@code len}) by the
   * unsigned {@code divisor}, and returns the remainder. The quotient will be
   * set in {@code mag}.
   *
   * @param mag The dividend (unsigned) as input, and quotient (unsigned) as
   *          output.
   * @param divisor The {@code long} divisor (unsigned).
   * @param len The significant length of the dividend in {@code mag}.
   * @return The remainder from the division of {@code mag} by {@code divisor}.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  /**
   * Divides this number with an unsigned long and returns the remainder.
   *
   * @param divisor The amount to divide with (treated as unsigned).
   * @return The absolute value of the remainder as an unsigned long.
   * @complexity O(n)
   */
  public static long udiv(final int[] mag, int len, final long divisor, long dh) {
    if (len == 1) {
      final long mag0l = mag[0] & LONG_INT_MASK;
      setToZero(mag);
      return mag0l;
    }

    final int s = Integer.numberOfLeadingZeros((int)(dh));
    dh = divisor >>> 32 - s;
    final long dl = (divisor << s) & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;

    long u2 = 0, u1 = mag[len - 1] >>> 32 - s, u0 = (mag[len - 1] << s | mag[len - 2] >>> 32 - s) & LONG_INT_MASK;
    if (s == 0) {
      u1 = 0;
      u0 = mag[len - 1] & LONG_INT_MASK;
    }
    for (int j = len - 2; j >= 0; j--) {
      u2 = u1;
      u1 = u0;
      u0 = s > 0 && j > 0 ? (mag[j] << s | mag[j - 1] >>> 32 - s) & LONG_INT_MASK : (mag[j] << s) & LONG_INT_MASK;

      long k = (u2 << 32) + u1; // Unsigned division is a pain in the ass! ='(
      long qhat = (k >>> 1) / dh << 1;
      long t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        qhat++; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];
      long rhat = k - qhat * dh;

      while (qhat + hbit >= (1L << 32) + hbit || qhat * dl + hbit > (rhat << 32) + u0 + hbit) // Unsigned
                                                                                              // comparison.
      {
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

      mag[j] = (int)qhat; // Store quotient digit. If we subtracted too much,
                          // add back.
      if (t < 0) {
        --mag[j]; // Unfolded loop.
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
    mag[len] = 0;
    if (len > 1 && mag[len - 1] == 0)
      --len;

    final long tmp = u1 << 32 - s | u0 >>> s;
    return s == 0 ? tmp : u2 << 64 - s | tmp;
  }

  /**
   * Divides the first magnitude u[0..m) by v[0..n) and stores the resulting
   * quotient in q. The remainder will be stored in u, so u will be destroyed.
   * u[] must have room for an additional element, i.e. u[m] is a legal access.
   *
   * @param u The first magnitude array, the dividend.
   * @param v The second magnitude array, the divisor.
   * @param m The length of the first array.
   * @param n The length of the second array.
   * @param q An array of length at least n-m+1 where the quotient will be
   *          stored.
   * @complexity O(m*n)
   */
  // Hacker's Delight's implementation of Knuth's Algorithm D
  public static void div(final int[] u, final int[] v, final int m, final int n, final int[] q) {
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

    s = Integer.numberOfLeadingZeros(v[n - 1]);
    if (s > 0) { // In Java (x<<32)==(x<<0) so...
      for (i = n - 1; i > 0; --i)
        v[i] = (v[i] << s) | (v[i - 1] >>> 32 - s);

      v[0] = v[0] << s;
      u[m] = u[m - 1] >>> 32 - s;
      for (i = m - 1; i > 0; --i)
        u[i] = (u[i] << s) | (u[i - 1] >>> 32 - s);

      u[0] = u[0] << s;
    }

    final long dh = v[n - 1] & LONG_INT_MASK, dl = v[n - 2] & LONG_INT_MASK, hbit = Long.MIN_VALUE;

    for (j = m - n; j >= 0; --j) { // Main loop
      // Compute estimate qhat of q[j].
      k = u[j + n] * b + (u[j + n - 1] & LONG_INT_MASK);
      // in the ass! ='(
      qhat = (k >>> 1) / dh << 1;
      t = k - qhat * dh;
      if (t + hbit >= dh + hbit)
        ++qhat; // qhat = (u[j+n]*b + u[j+n-1])/v[n-1];

      rhat = k - qhat * dh;

      while (qhat + hbit >= b + hbit || qhat * dl + hbit > b * rhat + (u[j + n - 2] & LONG_INT_MASK) + hbit) { // Unsigned comparison.
        qhat -= 1;
        rhat += dh;
        if (rhat + hbit >= b + hbit)
          break;
      }

      // Multiply and subtract.
      k = 0;
      for (i = 0; i < n; ++i) {
        p = qhat * (v[i] & LONG_INT_MASK);
        t = (u[i + j] & LONG_INT_MASK) - k - (p & LONG_INT_MASK);
        u[i + j] = (int)t;
        k = (p >>> 32) - (t >> 32);
      }

      t = (u[j + n] & LONG_INT_MASK) - k;
      u[j + n] = (int)t;

      q[j] = (int)qhat; // Store quotient digit. If we subtracted too much, add back.
      if (t < 0) {
        q[j] -= 1;
        k = 0;
        for (i = 0; i < n; ++i) {
          t = (u[i + j] & LONG_INT_MASK) + (v[i] & LONG_INT_MASK) + k;
          u[i + j] = (int)t;
          k = t >>> 32; // >>
        }

        u[j + n] += (int)k;
      }
    }

    if (s > 0) {
      // Unnormalize v[].
      for (i = 0; i < n - 1; ++i)
        v[i] = v[i] >>> s | v[i + 1] << 32 - s;
      v[n - 1] >>>= s;

      // Unnormalize u[].
      for (i = 0; i < m; ++i)
        u[i] = u[i] >>> s | u[i + 1] << 32 - s;

      u[m] >>>= s;
    }
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param div The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public static int[] rem(int[] mag1, int len1, final int[] mag2, final int len2) {
    // -7/-3 = 2, 2*-3 + -1
    // -7/3 = -2, -2*3 + -1
    // 7/-3 = -2, -2*-3 + 1
    // 7/3 = 2, 2*3 + 1
    if (len2 == 1) {
      mag1[0] = (int)urem(mag1, len1, mag2[0]);
      len1 = 1;
      // signum = checkSig(mag1, len1, signum1); // Handled by caller, left for reference
    }
    else {
      final int c = compareAbsTo(mag1, len1, mag2, len2);
      if (c > 0) {
        final int[] q = new int[len1 - len2 + 1];
        if (len1 == mag1.length)
          mag1 = realloc(mag1, len1, len1 + 1); // We need an extra slot.

        div(mag1, mag2, len1, len2, q);

        for (len1 = len2; mag1[len1 - 1] == 0; --len1);
      }
      else if (c == 0) {
        len1 = setToZero(mag1);
        // signum = 0; // Handled by caller, left for reference
      }
    }

    clear(mag1, len1);
    return mag1;
  }

  /**
   * Sets this number to {@code (this mod m}). This method differs from
   * {@code rem} in that it always computes * <i>non-negative</i> result
   *
   * @param div The number to use in the division causing the remainder.
   * @see #rem
   */
  public static int[] mod(int[] mag1, int len1, int signum1, final int[] mag2, final int len2, final int signum2) {
    if (signum2 <= 0)
      throw new ArithmeticException("BigInt: modulus not positive");

    int newSignum = len2 != 1 && compareAbsTo(mag1, len1, mag2, len2) == 0 ? 0 : signum1;
    mag1 = rem(mag1, len1, mag2, len2);
    len1 = getLen(mag1);
    signum1 = len2 == 1 ? checkSig(mag1, len1, signum1) : newSignum;
    if (signum1 < 0)
      mag1 = add(mag1, len1, signum1, mag2, len2, signum2, true);

    return mag1;
  }

  /**
   * Divides this number by the given BigInt. Division by zero is undefined.
   *
   * @param div The number to divide with.
   * @complexity O(n^2)
   */
  public static int[] div(int[] mag1, int len1, final int signum1, final int[] mag2, final int len2, final int signum2) {
    if (len2 == 1) {
      // signum *= signum2; // Handled by caller, left for reference
      udiv(mag1, len1, mag2[0]);
      // signum = checkSig(mag, len, signum); // Handled by caller, left for reference
    }
    else {
      final int c = compareAbsTo(mag1, len1, mag2, len2);
      if (c < 0) {
        len1 = setToZero(mag1);
        // signum = 0; // Handled by caller, left for reference
      }
      else if (c == 0) {
        len1 = 1;
        // signum = 1; // Handled by caller, left for reference
        mag1[0] = signum1 * signum2;
      }
      else {
        final int[] q = new int[len1 - len2 + 1];
        if (len1 == mag1.length)
          mag1 = realloc(mag1, len1, len1 + 1); // We need an extra slot.

        div(mag1, mag2, len1, len2, q);

        mag1 = q;
        for (len1 = q.length; len1 > 1 && mag1[len1 - 1] == 0; --len1);
        // signum *= signum2; // Handled by caller, left for reference
      }
    }

    clear(mag1, len1);
    return mag1;
  }
}