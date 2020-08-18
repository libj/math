/* Copyright (c) 2020 Seva Safris, LibJ
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

package org.libj.math;

import static org.junit.Assert.*;

import org.junit.Test;
import org.libj.lang.Buffers;
import org.libj.lang.Numbers;

public class DecimalMultiplicationStudy {
  private static final long v1 = 72057594037927935L;
  private static final short s1 = 22;
  private static final long v2 = 72057594037927935L;
  private static final short s2 = 37;
  private static final byte valueBits = 56;

  private static final long[] d = {0b00000000000000001111111111111111, 0b11111111111111111111111111111111, 0b11111110000000000000000000000000, 0b00000000000000000000000000000001};
  private static final long[] e = {0b0000000000000000111111111111111111111111111111111111111111111111L, 0b1111111000000000000000000000000000000000000000000000000000000001L};
  private static final long expected = 51922968585348274L;

  @Test
  public void test0() {
    final long[] d = DecimalMultiplicationStudy.d.clone();
    div(d, 1000000000);
    div(d, 100000000);
    final long result = com(d);
    assertEquals(expected, result);
  }

  @Test
  public void test1() {
    final long[] d = DecimalMultiplicationStudy.d.clone();
    final long result = divf(d, 17);
    assertEquals(expected, result);
  }

  @Test
  public void test2() {
    final long[] e = DecimalMultiplicationStudy.e.clone();
    final long result = divf2(e, 17);
    assertEquals(expected, result);
  }

  @Test
  public void test3() {
    final long[] e = DecimalMultiplicationStudy.e.clone();
    final long result = div128e10(e[0], e[1], 17);
    assertEquals(expected, result);
  }

  @Test
  public void test4() {
    final int errorFactor = 1;
    final long result = mulMod(v1, s1, v2, s2, valueBits);
    assertEquals(expected / errorFactor, result / errorFactor);
  }

  private static long com(long[] y) {
    long s = 0;
    for (int i = 0; i < y.length; ++i) {
      s <<= 32;
      s |= y[i];
    }

    return s;
  }

  private static long divf(long[] y, int factor) {
    if (factor <= 9) {
      div(y, (int)FastMath.e10[factor]);
    }
    else {
      final int high = (int)FastMath.e10[factor -= 9];
      final int low = (int)FastMath.e10[9];
      div(y, low);
      div(y, high);
    }

    return com(y);
  }

  private static long divf2(long[] two, int factor) {
    if (factor <= 9) {
      div2(two, (int)FastMath.e10[factor]);
    }
    else {
      final int high = (int)FastMath.e10[factor -= 9];
      final int low = (int)FastMath.e10[9];
      div2(two, high);
      div2(two, low);
    }

    return two[1];
  }

  private static final long LOW_32 = (1L << 32) - 1;

  private static void div2(final long[] two, final int divisor) {
    final long high = two[0];
    final long low = two[1];

    long a;
    long m;
    long r = 0;

    a = (high >> 32) & LOW_32;
    long d0 = Long.divideUnsigned(a, divisor);
    System.err.println("d0: " + Buffers.toString(d0));
    m = Long.remainderUnsigned(a, divisor);
    r |= m;
    r <<= 32;

    a = high & LOW_32;
    a |= r;
    long d1 = Long.divideUnsigned(a, divisor);
    System.err.println("d1: " + Buffers.toString(d1));
    m = Long.remainderUnsigned(a, divisor);
    r |= m;
    r <<= 32;

    a = (low >> 32) & LOW_32;
    a |= r;
    long d2 = Long.divideUnsigned(a, divisor);
    System.err.println("d2: " + Buffers.toString(d2));
    m = Long.remainderUnsigned(a, divisor);
    r |= m;
    r <<= 32;

    a = low & LOW_32;
    a |= r;
    long d3 = Long.divideUnsigned(a, divisor);
    System.err.println("d3: " + Buffers.toString(d3));

    two[0] = (d0 << 32) | d1;
    two[1] = (d2 << 32) | d3;
    System.err.println();
  }

  private static void div(final long[] dividend, final int d) {
    long r = 0;
    System.err.println("d: " + Buffers.toString(d));
    for (int i = 0; i < dividend.length; ++i) {
      long a = dividend[i] & ((1L << 32) - 1);
      System.err.println("a" + i + ": " + Buffers.toString(a) + " a = " + a);

      a |= r;
      System.err.println("a" + i + ": " + Buffers.toString(a) + " a = a | r = " + a);

      long v = Long.divideUnsigned(a, d);
      System.err.println("v" + i + ": " + Buffers.toString(v) + " v = a / d = " + v);
      long m = Long.remainderUnsigned(a, d);
      System.err.println("m" + i + ": " + Buffers.toString(m) + " m = a % d = " + m);
      r |= m;
      System.err.println("r" + i + ": " + Buffers.toString(r) + " r = r | m = " + r);
      r <<= 32;
      System.err.println("r" + i + ": " + Buffers.toString(r) + " r = r << 32 = " + r);

      dividend[i] = (int)v;
      System.err.println();
    }
  }

  /**
   * Returns the result of the division of a 128-bit number represented by
   * {@code high} and {@code low} by {@code 10^factor}.
   *
   * @param high The high 64-bits of the 128-bit value.
   * @param low The low 64-bits of the 128-bit value.
   * @param factor The decimal factor to be converted to:
   *          {@code divisor = 10^factor}.
   * @return The result of the division of a 128-bit number represented by
   *         {@code high} and {@code low} by {@code 10^factor}.
   */
  private static long div128e10(long high, long low, int factor) {
    long a, m, r = 0;
    long v0, v1, v2, v3;
    int d, f;
    do {
      f = Math.min(factor, 9);
      d = (int)FastMath.e10[f];
      factor -= f;

      System.err.println("d: " + Buffers.toString(d));

      a = (high >> 32) & LOW_32;
      System.err.println("a0: " + Buffers.toString(a) + " a = " + a);
      System.err.println("a0: " + Buffers.toString(a) + " a = a | r = " + a);
      v0 = Long.divideUnsigned(a, d);
      System.err.println("v0: " + Buffers.toString(v0) + " v = a / d = " + v0);
      m = Long.remainderUnsigned(a, d);
      System.err.println("m0: " + Buffers.toString(m) + " m = a % d = " + m);
      r |= m;
      System.err.println("r0: " + Buffers.toString(r) + " r = r | m = " + r);
      r <<= 32;
      System.err.println("r0: " + Buffers.toString(r) + " r = r << 32 = " + r);
      System.err.println();

      a = high & LOW_32;
      System.err.println("a1: " + Buffers.toString(a) + " a = " + a);
      a |= r;
      System.err.println("a1: " + Buffers.toString(a) + " a = a | r = " + a);
      v1 = Long.divideUnsigned(a, d);
      System.err.println("v1: " + Buffers.toString(v1) + " v = a / d = " + v1);
      m = Long.remainderUnsigned(a, d);
      System.err.println("m1: " + Buffers.toString(m) + " m = a % d = " + m);
      r |= m;
      System.err.println("r1: " + Buffers.toString(r) + " r = r | m = " + r);
      r <<= 32;
      System.err.println("r1: " + Buffers.toString(r) + " r = r << 32 = " + r);
      System.err.println();

      a = (low >> 32) & LOW_32;
      System.err.println("a2: " + Buffers.toString(a) + " a = " + a);
      a |= r;
      System.err.println("a2: " + Buffers.toString(a) + " a = a | r = " + a);
      v2 = Long.divideUnsigned(a, d);
      System.err.println("v2: " + Buffers.toString(v2) + " v = a / d = " + v2);
      m = Long.remainderUnsigned(a, d);
      System.err.println("m2: " + Buffers.toString(m) + " m = a % d = " + m);
      r |= m;
      System.err.println("r2: " + Buffers.toString(r) + " r = r | m = " + r);
      r <<= 32;
      System.err.println("r2: " + Buffers.toString(r) + " r = r << 32 = " + r);
      System.err.println();

      a = low & LOW_32;
      System.err.println("a3: " + Buffers.toString(a) + " a = " + a);
      a |= r;
      System.err.println("a3: " + Buffers.toString(a) + " a = a | r = " + a);
      v3 = Long.divideUnsigned(a, d);
      System.err.println("v3: " + Buffers.toString(v3) + " v = a / d = " + v3);
      System.err.println();

      low = (v2 << 32) | (v3 & LOW_32);
      if (factor == 0)
        return low;

      high = (v0 << 32) | (v1 & LOW_32);
    }
    while (true);
  }

  /**
   * A <b>low error</b> approach for {@code v1 * v2} via:
   *
   * <pre>
   * <code>
   * v = v1 * v2 = (v1h + v1l) * (v2h + v2l)
   * </code>
   * </pre>
   */
  private static long mulMod(final long v1, long s1, final long v2, long s2, final byte valueBits) {
    byte bp1 = FixedPoint.bitLength(v1);
    byte bp2 = FixedPoint.bitLength(v2);

    // How many bits are available until we hit the max (valueBits)?
    byte bp = (byte)(bp1 + bp2 - valueBits);
    final byte dp = Numbers.precision((1L << bp) - 1);

    // Approach 2: "64-bit mod split"
    // v = v1 * v2 = (v1h + v1l) * (v2h + v2l)

    // We need to reduce the sizes of v1 and v2 by bp so that the product
    // doesn't overflow. First allocate as much of bp as we can to bring bp1
    // and bp2 closer to each other. This ensures that the least significant
    // digits are de-scaled first.
    final boolean bias = bp1 > bp2;
    if (bias) {
      bp1 = SafeMath.min(bp1 - bp2, bp);
      bp2 = 0;
      bp -= bp1;
    }
    else if (bp1 < bp2) {
      bp2 = SafeMath.min(bp2 - bp1, bp);
      bp1 = 0;
      bp -= bp2;
    }
    else {
      bp1 = 0;
      bp2 = 0;
    }

    // If bp is not fully consumed, split the difference of bp between v1
    // and v2, reducing the larger of v1 and v2 if bp is odd.
    if (bp > 0) {
      final byte half = (byte)(bp / 2);
      if (bias) {
        bp1 += (byte)(bp - half);
        bp2 += half;
      }
      else {
        bp1 += half;
        bp2 += (byte)(bp - half);
      }
    }

    // Switch from binary to decimal to calculate the de-scaling factors.
    final int dp1, dp2;
    if (bias) {
      dp2 = Numbers.precision((1L << bp2) - 1);
      dp1 = dp - dp2;
    }
    else {
      dp1 = Numbers.precision((1L << bp1) - 1);
      dp2 = dp - dp1;
    }

    long v = 0;
    long hv1 = 0;
    long lv1 = 0;
    long f1 = 0;
    long hv2 = 0;
    long lv2 = 0;
    long f2 = 0;
    if (dp1 > 0) {
      f1 = FastMath.e10[dp1];
      lv1 = v1 % 1000000000;
      hv1 = (v1 - lv1) / f1;
      if (dp2 == 0)
        v = (hv1 * v2) + (lv1 * v2) / f1;
    }

    if (dp2 > 0) {
      f2 = FastMath.e10[dp2];
      lv2 = v2 % 1000000000;
      hv2 = (v2 - lv2) / f2;
      if (dp1 == 0)
        v = (hv2 * v1) + (lv2 * v1) / f2;
    }

    if (dp1 > 0 && dp2 > 0)
      v = (hv1 * hv2) + ((hv1 * lv2) / f2) + ((hv2 * lv1) / f1) + ((lv1 * lv2) / (f1 * f2));

    return v;
  }

  /**
   * Returns as a {@code long} the most significant 64 bits of the 128-bit
   * product of two 64-bit factors.
   *
   * @param x The first value.
   * @param y The second value.
   * @return the result.
   */
  static long multiplyHigh(final long x, final long y) {
    final long x2 = x & 0xFFFFFFFFL;
    final long y2 = y & 0xFFFFFFFFL;
    if (x < 0 || y < 0) {
      // Use technique from section 8-2 of Henry S. Warren, Jr.,
      // Hacker's Delight (2nd ed.) (Addison Wesley, 2013), 173-174.
      final long x1 = x >> 32;
      final long y1 = y >> 32;
      final long z2 = x2 * y2;
      final long t = x1 * y2 + (z2 >>> 32);
      final long z0 = t >> 32;
      final long z1 = (t & 0xFFFFFFFFL) + x2 * y1;
      return x1 * y1 + z0 + (z1 >> 32);
    }

    // Use Karatsuba technique with two base 2^32 digits.
    final long x1 = x >>> 32;
    final long y1 = y >>> 32;
    final long a = x1 * y1;
    final long b = x2 * y2;
    final long c = (x1 + x2) * (y1 + y2);
    final long k = c - a - b;
    return (((b >>> 32) + k) >>> 32) + a;
  }

  /**
   * A <b>medium error</b> approach for {@code v1 * v2} via
   * {@link DecimalMultiplication#multiplyHigh(long,long)} that tries to shift {@code high}
   * and {@code low} into a single 64-bit {@code long}, perform the division by
   * the specified {@code factor}, and then shift back to the original position.
   */
  private static long mulHigh(final long v1, final long v2, int factor) {
    long h = multiplyHigh(v1, v2);
    long l = v1 * v2;

    final long f = FastMath.e10[factor];

    final int shift = Long.numberOfLeadingZeros(h);
    long shiftHigh = h << shift;
    long shiftLow = (l >> (64 - shift)) & ((1L << shift) - 1);
    shiftHigh |= shiftLow;

    h = Long.divideUnsigned(shiftHigh, f);
    long r = Long.remainderUnsigned(shiftHigh, f);

    h <<= (64 - shift);
    r >>= shift & ((1L << (64 - shift)) - 1);

    h |= r;
    return h;
  }

  /**
   * A <b>high error</b> approach for {@code v1 * v2} by just factoring down
   * {@code v1} and {@code v2} individually.
   */
  private static long mulDirect(long v1, long s1, long v2, long s2, final byte bp1, final byte bp2) {
    // Need to scale down if we have high order bits
    if (bp1 > 0) {
      final int dp1 = Numbers.precision((1 << bp1) - 1);
      v1 /= FastMath.e10[dp1];
      s1 -= dp1; // NOTE: s1 is reduced, and this info needs to be used
    }

    // Need to scale down if we have high order bits
    if (bp2 > 0) {
      final int dp2 = Numbers.precision((1 << bp2) - 1);
      v2 /= FastMath.e10[dp2];
      s2 -= dp2; // NOTE: s2 is reduced, and this info needs to be used
    }

    return v1 * v2;
  }
}