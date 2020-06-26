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

  public static long urem(final int[] val, final int mod) {
    return mod < 0 ? safeUrem(val, mod) : unsafeUrem(val, mod);
  }

  public static void urem(final int[] val, final long mod) {
    final long rem = udiv(val, mod, mod >>> 32); // TODO: opt?
    val[1] = (int)rem;
    if (rem == (rem & LONG_INT_MASK)) {
      val[0] = 1;
    }
    else { // if(dig[0]==0) sign = 1;
      val[0] = 2;
      val[3] = (int)(rem >>> 32);
    }
  }

  // Assumes mod > 0.
  private static long unsafeUrem(final int[] val, final int mod) {
    final int len = val[0];
    long r = 0;
    long d = mod & LONG_INT_MASK;
    for (int i = len - 1; i >= 2; --i) {
      r <<= 32;
      r = (r + (val[i] & LONG_INT_MASK)) % d;
    }

    return r;
  }

  // Assumes mod < 0.
  private static long safeUrem(final int[] val, final int mod) {
    final int len = val[0];
    final long d = mod & LONG_INT_MASK;
    final long hbit = Long.MIN_VALUE;
    // Precompute hrem = (1<<63) % d
    // I.e. the remainder caused by the highest bit.
    long hrem = (hbit - 1) % d;
    if (++hrem == d)
      hrem = 0;

    long r = 0;
    for (int i = len - 1; i >= 2; --i) {
      r = (r << 32) + (val[i] & LONG_INT_MASK);
      // Calculate rem %= d.
      // Do this by calculating the lower 63 bits and highest bit separately.
      // The highest bit remainder only gets added if it's set.
      r = ((r & hbit - 1) + (hrem & r >> 63)) % d;
      // The addition is safe and cannot overflow.
      // Because hrem < 2^32 and there's at least one zero bit in [62,32] if bit
      // 63 is set.
    }

    return r;
  }

  /**
   * Divides the unsigned dividend in {@code val} (up to {@code len}) by the
   * unsigned {@code divisor}, and returns the remainder. The quotient will be
   * set in {@code val}.
   *
   * @param val The dividend (unsigned) as input, and quotient (unsigned) as
   *          output.
   * @param divisor The {@code int} divisor (unsigned).
   * @param len The significant length of the dividend in {@code val}.
   * @return The remainder from the division of {@code val} by {@code divisor}.
   * @throws ArithmeticException If {@code divisor} is 0.
   */
  // FIXME: Javadoc: Assumes div > 0.
  public static int udiv(final int[] val, final int divisor) {
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
   * @param divisor The amount to divide with (treated as unsigned).
   * @return The absolute value of the remainder as an unsigned long.
   * @complexity O(n)
   */
  public static long udiv(final int[] val, final long divisor, long dh) {
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

    long u2 = 0, u1 = val[len - 1] >>> 32 - s, u0 = (val[len - 1] << s | val[len - 2] >>> 32 - s) & LONG_INT_MASK;
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
    final int offset = 2;
    final int len1 = val1[0];
    int len2 = val2[0];
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
      for (i = len2 - 1; i > offset; --i)
        val2[i] = (val2[i] << s) | (val2[i - 1] >>> 32 - s);

      val2[offset] = val2[offset] << s;

      // Normalize val1
      val1[len1] = val1[len1 - 1] >>> 32 - s;
      for (i = len1 - 1; i > offset; --i)
        val1[i] = (val1[i] << s) | (val1[i - 1] >>> 32 - s);

      val1[offset] = val1[offset] << s;
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
      for (i = offset; i < len2; ++i) {
        p = qhat * (val2[i] & LONG_INT_MASK);
        t = (val1[i + j] & LONG_INT_MASK) - k - (p & LONG_INT_MASK);
        val1[i + j] = (int)t;
        k = (p >>> 32) - (t >> 32);
      }

      t = (val1[j + len2] & LONG_INT_MASK) - k;
      val1[j + len2] = (int)t;

      // Store quotient digit. If we subtracted too much, add back
      q[j + offset] = (int)qhat;
      if (t < 0) {
        q[j + offset] -= 1;
        k = 0;
        for (i = offset; i < len2; ++i) {
          t = (val1[i + j] & LONG_INT_MASK) + (val2[i] & LONG_INT_MASK) + k;
          val1[i + j] = (int)t;
          k = t >>> 32; // >>
        }

        val1[j + len2] += (int)k;
      }
    }

    if (s > 0) {
      // Unnormalize val1.
      for (i = offset; i < len2 - 1; ++i)
        val2[i] = val2[i] >>> s | val2[i + 1] << 32 - s;

      val2[len2 - 1] >>>= s;

      // Unnormalize val2
      for (i = offset; i < len1; ++i)
        val1[i] = val1[i] >>> s | val1[i + 1] << 32 - s;

      val1[len1] >>>= s;
    }

    // Set the new length of val1
    while (val1[len2--] == 0);
    val1[0] = Math.max(3, len2 += 2);
    if (isZero(val1))
      val1[1] = 0;
  }

  /**
   * Sets this number to {@code (this mod m}). This method differs from
   * {@code rem} in that it always computes * <i>non-negative</i> result
   *
   * @param div The number to use in the division causing the remainder.
   * @see #rem
   */
  public static int[] mod(int[] val1, final int[] val2) {
    final int signum2 = val2[1];
    if (signum2 <= 0)
      throw new ArithmeticException("BigInt: modulus not positive");

    val1 = rem(val1, val2);
    if (val1[1] < 0)
      val1 = add(val1, val2, true);

    val1[1] = isZero(val1) ? 0 : 1;
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
    final int len2 = val2[0];

    if (len2 == 3) {
      val1[2] = (int)urem(val1, val2[2]);
      val1[0] = 3;
      if (isZero(val1))
        val1[1] = 0;
    }
    else {
      int len1 = val1[0];
      final int c = compareAbsTo(val1, val2);
      if (c > 0) {
        final int[] q = new int[len1 - len2 + 3];
        if (len1 == val1.length)
          val1 = realloc(val1, len1 + 1); // We need an extra slot.

        div(val1, val2, q);

//        for (len1 = len2; val1[len1 - 1] == 0; --len1);
//        val1[0] = len1;
      }
      else if (c == 0) {
        setToZero(val1);
        val1[1] = 0;
      }
    }

//    val1[1] = len2 == 1 ? checkSig(val1, len1, signum1) : val1[1];
    return val1;
  }

  /**
   * Divides this number by the given BigInt. Division by zero is undefined.
   *
   * @param div The number to divide with.
   * @complexity O(n^2)
   */
  public static int[] div(int[] val1, final int[] val2) {
    final int signum = val1[1] * val2[1];
    final int len2 = val2[0];
    if (len2 == 3) {
      val1[1] = signum;
      udiv(val1, val2[2]);
      // signum = checkSig(val, len, signum); // Handled by caller, left for reference
    }
    else {
      final int c = compareAbsTo(val1, val2);
      if (c < 0) {
        setToZero(val1);
        val1[1] = 0;
      }
      else if (c == 0) {
        val1[0] = 3;
        val1[1] = signum;
        val1[2] = 1;
      }
      else {
        int len1 = val1[0];
        final int[] q = new int[len1 - len2 + 3];
        if (len1 == val1.length)
          val1 = realloc(val1, len1 + 3); // We need an extra slot.

        div(val1, val2, q);

        val1 = q;
        for (len1 = q.length; len1 > 3 && val1[len1 - 1] == 0; --len1);
        val1[0] = len1;
        val1[1] = isZero(val1) ? 0 : signum;
      }
    }

    return val1;
  }

  public static int[] divRem(int[] val1, final int[] val2) {
    if (val2[0] > 3)
      return divRem0(val1, val2);

    final int signum1 = val1[1];
    val1[1] *= val2[1];
    return uassign(new int[3], signum1, udiv(val1, val2[2]));
  }

  static int[] divRem0(int[] val1, final int[] val2) {
    final int c = compareAbsTo(val1, val2);
    if (c == 0) {
      uassign(val1, 1, val1[1] *= val2[1]);
      return setToZero(new int[3]);
    }

    if (c < 0) {
      final int[] r = val1.clone();
      setToZero(val1);
      return r;
    }

    int len1 = val1[0];
    // Prepare the q array as the replacement for val1, accounting for the extra 1 required slot
    final int[] q = new int[len1 == val1.length ? val1.length + 1 : val1.length];
    // Transfer val1 -> q
    System.arraycopy(val1, 0, q, 0, len1);

    // Do the div, with results going to val1 (which is where we want it to end up)
    BigDivision.div(q, val2, val1);

    final int signum1 = val1[1];
    int len2 = val2[0];

    // Normalize the length
//    len1 = q[0] - val2[0] + 3;
    len1 -= len2 - 3;
    for (; val1[len1 - 1] == 0; --len1);
    val1[0] = Math.max(3, len1);
    val1[1] = signum1 * val2[1];

    return q;
  }
}