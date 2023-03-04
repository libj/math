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
import static org.libj.lang.Strings.Align.*;
import static org.libj.math.FixedPoint.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.libj.lang.BigDecimals;
import org.libj.lang.Buffers;
import org.libj.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedPointTest extends DecimalOperationTest {
  private static final Logger logger = LoggerFactory.getLogger(FixedPointTest.class);
  private static final boolean debug = false;

  private static void testEncodeDecode(final long significand, final short scale, final long[] time) {
    final boolean expectOverflow = significand < 0 ? significand < MIN_SIGNIFICAND : significand > MAX_SIGNIFICAND;

    if (debug && logger.isInfoEnabled()) {
      logger.info("Value: " + Buffers.toString(significand) + " " + significand);
      logger.info("Scale: " + Buffers.toString(scale).substring(Buffers.toString(scale).length() - SCALE_BITS) + " " + scale + " " + SCALE_BITS);
    }

    final long defaultValue = random.nextLong();
    long ts = System.nanoTime();
    final long encoded = valueOf(significand, scale, defaultValue);
    time[0] += System.nanoTime() - ts;
    if (expectOverflow) {
      if (encoded != defaultValue)
        fail("Expected IllegalArgumentException: " + significand + ", " + scale + ", " + SCALE_BITS);

      return;
    }

    if (debug && logger.isInfoEnabled()) logger.info("Encod: " + Buffers.toString(encoded));

    ts = System.nanoTime();
    final long decodedValue = significand(encoded);
    final short decodedScale = scale(encoded);
    time[1] += System.nanoTime() - ts;

    if (debug && logger.isInfoEnabled()) {
      logger.info("DeVal: " + Buffers.toString(decodedValue));
      logger.info("DeSca: " + Buffers.toString(decodedScale));
    }

    assertEquals("significand=" + significand + ", scale=" + scale + ", bits=" + SCALE_BITS, significand, decodedValue);
    assertEquals("significand=" + significand + ", scale=" + scale + ", bits=" + SCALE_BITS, scale, decodedScale);
  }

  private static String formatOverflowPoint(final BigDecimal val, final int cut) {
    final String str = val.toString();
    if (str.length() <= cut)
      return str;

    final String a = str.substring(0, cut);
    final String b = str.substring(cut);
    return a + "." + b + " " + b.length() + " " + (FastMath.log2(b.length()) + 2);
  }

  @Test
  public void testMulOverflowPoints() {
    boolean flip = false;
    for (int i = 63, j = 63; i >= 1;) { // [N]
      final BigDecimal a = BigDecimals.TWO.pow(i);
      final BigDecimal b = BigDecimals.TWO.pow(j);
      if (logger.isInfoEnabled()) logger.info(Strings.pad(i + " + " + j, LEFT, 8) + " " + Strings.pad(String.valueOf(i + j), LEFT, 4) + " " + formatOverflowPoint(a.multiply(b), i <= 55 ? 18 : 17));
      if (flip)
        --i;
      else
        --j;

      flip = !flip;
    }
  }

  @Test
  public void testEncodeDecode() {
    final long[] time = new long[2];
    final long significand = 20769187434139310L;
    final short scale = -18;
    testEncodeDecode(significand, scale, time);

    int count = 0;
    for (short s = 0; s <= MAX_PSCALE; ++s) // [N]
      for (int i = 0; i < numTests / 10; ++i, ++count) // [N]
        testEncodeDecode(random.nextLong(), s, time);

    if (logger.isInfoEnabled()) logger.info("testEncodeDecode(): encode=" + (time[0] / count) + "ns, decode=" + (count / time[1]) + "/ns");
  }

  private static double print(final String label, final long significand, final short pscale) {
    final long dec = encodeInPlace(significand, pscale);
    final String str = Decimal.toScientificString(dec);
//    Math.nextUp(dbl)
    System.out.println(label + "   " + Buffers.toString(dec) + " " + str + " " + significand + " " + pscale);
    // assertEquals(significand, Decimal.significand(dec));
    // assertEquals(scale, Decimal.scale(dec));
    return Double.valueOf(str);
  }

  @Test
  public void testMinMax() {
    System.out.println("Decimal max value: " + MAX_SIGNIFICAND);
    System.out.println("Decimal min value: " + MIN_SIGNIFICAND);
    System.out.println("duble min normal: " + Double.MIN_NORMAL);
    System.out.println("duble min  value: " + Double.MIN_VALUE);
    System.out.println("duble max  value: " + Double.MAX_VALUE);
    System.out.println(print("min pos ", MAX_SIGNIFICAND, MAX_PSCALE));
    System.out.println(print("max pos ", MAX_SIGNIFICAND, MIN_PSCALE));
    System.out.println(print("min neg ", MIN_SIGNIFICAND, MAX_PSCALE));
    System.out.println(print("max neg ", MIN_SIGNIFICAND, MIN_PSCALE));

    assertTrue(isDecimal(maxPos));
    assertFalse(isDecimal(Math.nextUp(maxPos)));
    assertTrue(isDecimal(minPos));
    assertFalse(isDecimal(Math.nextUp(minPos)));
    assertTrue(isDecimal(minNeg));
    assertFalse(isDecimal(Math.nextDown(minNeg)));
    assertTrue(isDecimal(maxNeg));
    assertFalse(isDecimal(Math.nextDown(maxNeg)));

    assertFalse(isDecimal(Double.NaN));
    assertFalse(isDecimal(Double.POSITIVE_INFINITY));
    assertFalse(isDecimal(Double.NEGATIVE_INFINITY));

    assertFalse(isDecimal(Double.MAX_VALUE));
    assertFalse(isDecimal(Double.MIN_VALUE));

    boolean hasPass = false;
    boolean hasFail = false;

    // General test
    for (int i = 0; i < 100000; ++i) { // [N]
      final double sig = random.nextDouble();
      final byte exp = (byte)((byte)random.nextInt() * 0.25);
      final double x = Math.pow(sig, exp);
      assertTrue(x + " = " + sig + "^" + exp, isDecimal(x));
    }

    // Test maximum positive
    double v = maxPos;
    for (int i = 0; i < 1000; ++i) // [N]
      v = Math.nextDown(v);

    for (int i = 0; i < 100000; ++i) { // [N]
      final boolean pass = (v = Math.nextUp(v)) <= maxPos;
      assertEquals("" + i, pass, isDecimal(v));
      if (pass)
        hasPass = true;
      else
        hasFail = true;
    }

    assertTrue(hasPass);
    assertTrue(hasFail);
    hasPass = hasFail = false;

    // Test maximum negative
    v = maxNeg;
    for (int i = 0; i < 1000; ++i) // [N]
      v = Math.nextUp(v);

    for (int i = 0; i < 100000; ++i) { // [N]
      final boolean pass = (v = Math.nextDown(v)) >= maxNeg;
//      assertEquals("" + i, pass, isDecimal(v));
      if (pass)
        hasPass = true;
      else
        hasFail = true;
    }

    assertTrue(hasPass);
    assertTrue(hasFail);
    hasPass = hasFail = false;

    // Test minimum positive
    v = minPos;
    for (int i = 16; i >= 1; --i) { // [N]
      v = SafeMath.round(v, 256 + i, RoundingMode.DOWN);
      assertTrue("" + v, isDecimal(v));
    }

    // Test minimum negative
    v = minNeg;
    for (int i = 16; i >= 1; --i) { // [N]
      v = SafeMath.round(v, 256 + i, RoundingMode.DOWN);
      assertTrue("" + v, isDecimal(v));
    }
  }

  public static boolean isOk(final double val) {
    return val < 0 ? val <= minNeg && maxNeg < val : val <= maxPos && minPos < val;
  }

  private static final int count = 2000000;

  @Test
  public void testSwap() {
    long a = 0L;
    long b = 0L;

    long ts;
    long tmp;

    long time1 = 0;
    long time2 = 0;
    for (int j = 0; j < 20; ++j) { // [N]
      if (j % 2 == 0) {
        a = random.nextLong();
        b = random.nextLong();
      }

      for (int i = 0; i < count; ++i) { // [N]
        if (j % 2 == 0) {
          ts = System.nanoTime();
          tmp = a;
          a = b;
          b = tmp;
          time1 += System.nanoTime() - ts;
        }
        else {
          ts = System.nanoTime();
          a ^= b;
          b ^= a;
          a ^= b;
          time2 += System.nanoTime() - ts;
        }
      }

      if (j < 6) {
        time1 = 0;
        time2 = 0;
      }
    }

    if (logger.isInfoEnabled()) logger.info("tmp: " + time1);
    if (logger.isInfoEnabled()) logger.info("xor: " + time2);
    // assertTrue(time1 < time2);
  }

  @Test
  public void testBinaryPrecisionRequiredForValue() {
    assertEquals(0, bitLength(0));
    for (int i = 1; i < Long.SIZE; ++i) // [N]
      assertEquals(String.valueOf(i), i, bitLength((long)Math.pow(2, i - 1)));
  }
}