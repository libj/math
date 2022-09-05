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

import static org.libj.lang.Strings.Align.*;
import static org.libj.math.FixedPoint.*;

import java.util.Random;

import org.junit.Test;
import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;
import org.libj.lang.Constants;
import org.libj.lang.Numbers;
import org.libj.lang.Strings;

@SuppressWarnings("unused")
public class DecimalDivisionStudy {
  private static final long DIVISOR_MAX = Long.MAX_VALUE / 10;
  private static final Random random = new Random();

  /**
   * Returns the unscaled result of the maximum precision available for an unsigned {@code long} (18 or 19 digits, depending on the
   * value) for the "long division" of {@code v1} (the dividend) by {@code v2} (the divisor).
   *
   * @param v1 The dividend.
   * @param v2 The divisor.
   * @param v The whole digits of {@code v1 / v2}.
   * @param dp Decimal precision of the result.
   * @return The unscaled result of the maximum precision available for an unsigned {@code long} (18 or 19 digits, depending on the
   *         value) for the "long division" of {@code v1} (the dividend) by {@code v2} (the divisor).
   */
  static long scaleLongDiv(long v1, final long v2, long v, byte dp) {
    long a, r;
    // Perform long division to fill all bits of signed long before overflow.
    for (int i = 0;; ++i, v = a) { // [N]
      v1 = Long.remainderUnsigned(v1, v2) * 10;
      r = Long.divideUnsigned(v1, v2);
      a = v * 10 + r;
      // Check for overflow
      if (a < 0 || i == dp) {
        v = roundHalfUp(r, v);
        break;
      }
    }

    return v;
  }

  @Test
  public void testCompareLDvsMPN() {
    final long[] buf = new long[2];
    final int[] zds = new int[4];
    final int[] x = new int[2];
    final int[] y = new int[2];
    for (int i = 0; i < 10000000; ++i) { // [N]
      long v1 = random.nextLong();
      v1 = Math.abs(v1 == Long.MIN_VALUE ? ++v1 : v1);

      long v2 = random.nextLong();
      v2 = Math.abs(v2 == Long.MIN_VALUE ? ++v2 : v2);
      if (v2 > DIVISOR_MAX)
        v2 /= 10;

//      System.err.println("[" + i + "] " + v1 + " / " + v2  + " = " + (double)v1 / v2);

      // How many bits are available until overflow long?
      final byte bp1 = (byte)(Long.numberOfLeadingZeros(v1));

      // How many decimal places are available until overflow long?
      byte dp1 = (byte)SafeMath.floor(SafeMath.log10((1L << bp1) - 1));

      // Expand the value to max precision available,
      // allowing it to use the sign bit
      if (dp1 > 0) {
        v1 = v1 * FastMath.longE10[dp1];
//        s1 += dp1;
      }

      // If v2 has trailing zeroes, remove them first
      final byte z2 = Numbers.trailingZeroes(v2);
      if (z2 > 0) {
        v2 /= FastMath.longE10[z2];
//        s2 -= z2;
      }

      test3(v1, v2, zds, x, y, buf);
    }

    System.err.println(Strings.pad("LD", RIGHT, 12) + Strings.pad("MPN", RIGHT, 12));
    final long[] sum = new long[2];
    for (int i = 0, i$ = time.length; i < i$; ++i) { // [A]
      final boolean c = time[i][0] < time[i][1];
      System.err.println(Ansi.apply(Strings.pad(String.valueOf(time[i][0]), RIGHT, 12), c ? Color.GREEN : Color.RED) + Ansi.apply(Strings.pad(String.valueOf(time[i][1]), RIGHT, 12), c ? Color.RED : Color.GREEN));
      sum[0] += time[i][0];
      sum[1] += time[i][1];
    }

    sum[0] /= time.length;
    sum[1] /= time.length;
    System.err.println(Ansi.apply(Strings.pad(String.valueOf(sum[0]), RIGHT, 12), Color.MAGENTA) + Ansi.apply(Strings.pad(String.valueOf(sum[1]), RIGHT, 12), Color.MAGENTA));
  }

  private static long[][] time = new long[19][];

  static {
    for (int i = 0, i$ = time.length; i < i$; ++i) // [A]
      time[i] = new long[2];
  }

  /**
   * Returns the result of <code>v1 * 10<sup>dp</sup> / v2</code>.
   *
   * @param v1 The dividend (unsigned).
   * @param v2 The divisor (unsigned).
   * @param dp The decimal precision "factor" by which to scale {@code v1}.
   * @param q An array for the quotient ({@code int[4]}).
   * @param buf An array for the rounding buffer ({@code long[2]}).
   * @return The result of <code>v1 * 10<sup>dp</sup> / v2</code>.
   */
  static long scaleDiv(long v1, final long v2, byte dp, int[] q, final long[] buf) {
    final long f = FastMath.longE10[dp];

    BigInt.assign(q, 1, v1);
    if (BigInt.mul(q, f) != q)
      throw new IllegalStateException("q is not big enough");

    long remainder = BigInt.divRem(q, v2);

    // Put the result in v1
    v1 = BigInt.longValue(q, 1, 3);

    // If v1 is bigger than the signed limit, scale it down
    if (v1 < 0) {
      FastMath.divideUnsigned(v1, 10, buf);
      v1 = FixedPoint.roundHalfUp(buf[1], buf[0]);
    }
    else {
      remainder *= 10;
      final long round = FastMath.divideUnsigned(remainder, v2);
      v1 = FixedPoint.roundHalfUp(round, v1);
    }

    return v1;
  }

  private void test3(final long v1, final long v2, final int[] zds, final int[] x, final int[] y, final long[] buf) {
    // Regular division
    long ts = System.nanoTime();
    long r1 = FastMath.divideUnsigned(v1, v2);
    final byte bp = (byte)Long.numberOfLeadingZeros(r1);
    final byte dp = (byte)(bp == 64 ? 18 : SafeMath.floor(SafeMath.log10((1L << bp) - 1)));
    r1 = scaleLongDiv(v1, v2, r1, dp);
    time[dp][0] += System.nanoTime() - ts;

    final byte ds = (byte)(Numbers.precision(v1) - Numbers.precision(v2));
    final byte p = Numbers.precision(r1);
    final short s1 = (short)(p - ds);
//    System.err.println("r1: " + r1 + "E" + (-s1));

    // MPN
    ts = System.nanoTime();

    final long r2 = scaleDiv(v1, v2, dp, zds, buf);

    time[dp][1] += System.nanoTime() - ts;

    if (r1 != r2) {
      System.err.println("v1 / v2: " + Long.toUnsignedString(v1) + " / " + v2);
      System.err.println(" r1: " + r1);
      System.err.println(" r2: " + r2);
      test3(v1, v2, zds, x, y, buf);
    }
  }

  public static double getMantissa(final double x, final int exp) {
    return x / Math.pow(2, exp);
  }

  @Test
  public void testFloatingDecimal() {
    for (int i = 0; i < 10; ++i) { // [N]
      final double v = random.nextDouble() / random.nextLong();
      final int exp2 = Math.getExponent(v);

      final double exp = exp2 * Constants.LOG_2 / Constants.LOG_10;
      final double exp10f = Math.floor(exp);
      final double rem10 = exp - exp10f;
      final int exp10 = (int)exp10f;

      long x = 1000000000000000000L;
      final double man2 = getMantissa(v, exp2);
      final double man10 = man2 * Math.exp((exp10f + rem10 - exp10) * Constants.LOG_10);

      System.err.println(v);
      System.err.println(man2 * Math.pow(2, exp2) + " " + man2 + " " + man2);
      System.err.println(man10 + " " + exp10);
      System.err.println("----");
    }
  }
}