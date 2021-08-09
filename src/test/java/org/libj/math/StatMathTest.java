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

import java.util.Random;

import org.junit.Test;

public class StatMathTest {
  private static final Random random = new Random();
  private static final int arrayLen = 100;

  private static byte[] randomBytes(final int len) {
    final byte[] bytes = new byte[len];
    for (int i = 0; i < len; ++i)
      bytes[i] = (byte)random.nextInt();

    return bytes;
  }

  private static short[] randomShorts(final int len) {
    final short[] shorts = new short[len];
    for (int i = 0; i < len; ++i)
      shorts[i] = (short)random.nextInt();

    return shorts;
  }

  private static int[] randomInts(final int len) {
    final int[] ints = new int[len];
    for (int i = 0; i < len; ++i)
      ints[i] = random.nextInt();

    return ints;
  }

  private static long[] randomLongs(final int len) {
    final long[] longs = new long[len];
    for (int i = 0; i < len; ++i)
      longs[i] = random.nextLong();

    return longs;
  }

  private static float[] randomFloats(final int len) {
    final float[] floats = new float[len];
    for (int i = 0; i < len; ++i)
      floats[i] = random.nextFloat();

    return floats;
  }

  private static double[] randomDoubles(final int len) {
    final double[] doubles = new double[len];
    for (int i = 0; i < len; ++i)
      doubles[i] = random.nextDouble();

    return doubles;
  }

  @Test
  public void testRmsByte() {
    try {
      StatMath.rms((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new byte[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testRmsShort() {
    try {
      StatMath.rms((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new short[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testRmsInt() {
    try {
      StatMath.rms((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new int[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testRmsLong() {
    try {
      StatMath.rms((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new long[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testRmsFloat() {
    try {
      StatMath.rms((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new float[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testRmsDouble() {
    try {
      StatMath.rms((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.rms(new double[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.rms(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinByte() {
    try {
      StatMath.min((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new byte[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinShort() {
    try {
      StatMath.min((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new short[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinInt() {
    try {
      StatMath.min((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new int[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinLong() {
    try {
      StatMath.min((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new long[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinFloat() {
    try {
      StatMath.min((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new float[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMinDouble() {
    try {
      StatMath.min((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.min(new double[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.min(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxByte() {
    try {
      StatMath.max((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new byte[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxShort() {
    try {
      StatMath.max((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new short[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxInt() {
    try {
      StatMath.max((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new int[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxLong() {
    try {
      StatMath.max((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new long[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxFloat() {
    try {
      StatMath.max((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new float[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testMaxDouble() {
    try {
      StatMath.max((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.max(new double[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.max(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgByte() {
    try {
      StatMath.avg((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new byte[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgShort() {
    try {
      StatMath.avg((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new short[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgInt() {
    try {
      StatMath.avg((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new int[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgLong() {
    try {
      StatMath.avg((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new long[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgFloat() {
    try {
      StatMath.avg((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new float[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testAvgDouble() {
    try {
      StatMath.avg((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertTrue(Double.isNaN(StatMath.avg(new double[0])));
    for (int i = 0; i < 1000; ++i) {
      StatMath.avg(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumByte() {
    try {
      StatMath.sum((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new byte[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumShort() {
    try {
      StatMath.sum((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new short[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumInt() {
    try {
      StatMath.sum((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new int[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumLong() {
    try {
      StatMath.sum((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new long[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumFloat() {
    try {
      StatMath.sum((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new float[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testSumDouble() {
    try {
      StatMath.sum((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.sum(new double[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.sum(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevByte() {
    try {
      StatMath.stdDev((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new byte[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevShort() {
    try {
      StatMath.stdDev((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new short[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevInt() {
    try {
      StatMath.stdDev((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new int[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevLong() {
    try {
      StatMath.stdDev((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new long[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevFloat() {
    try {
      StatMath.stdDev((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new float[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testStdDevDouble() {
    try {
      StatMath.stdDev((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StatMath.stdDev(new double[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.stdDev(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeByte() {
    try {
      StatMath.normalize((byte[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomBytes(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeShort() {
    try {
      StatMath.normalize((short[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomShorts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeInt() {
    try {
      StatMath.normalize((int[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomInts(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeLong() {
    try {
      StatMath.normalize((long[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomLongs(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeFloat() {
    try {
      StatMath.normalize((float[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomFloats(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testNormalizeDouble() {
    try {
      StatMath.normalize((double[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000; ++i) {
      StatMath.normalize(randomDoubles(1 + Math.abs(random.nextInt() % arrayLen)));
    }
  }

  @Test
  public void testThresholdByte() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold((byte)random.nextInt(), (byte)random.nextInt(), (byte)random.nextInt());
    }
  }

  @Test
  public void testThresholdShort() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold((short)random.nextInt(), (short)random.nextInt(), (short)random.nextInt());
    }
  }

  @Test
  public void testThresholdInt() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold(random.nextInt(), random.nextInt(), random.nextInt());
    }
  }

  @Test
  public void testThresholdLong() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold(random.nextLong(), random.nextLong(), random.nextLong());
    }
  }

  @Test
  public void testThresholdFloat() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }
  }

  @Test
  public void testThresholdDouble() {
    for (int i = 0; i < 1000; ++i) {
      StatMath.threshold(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }
  }
}