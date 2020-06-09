/* Copyright (c) 2008 LibJ
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Ignore;
import org.junit.Test;

import ch.obermuhlner.math.big.BigDecimalMath;

public class SafeMathTest {
  private static final int numTests = 10000;
  private static final Random random = new Random();
  private static final MathContext mc = MathContext.DECIMAL64;

  @Test
  @Ignore("FIXME: Implement this")
  public void testNumberFunctions() {
  }

  private static double nonZero() {
    final double d = random.nextDouble();
    return d == 0 ? 0.1 + d : d;
  }

  @SuppressWarnings("unchecked")
  private static <N extends Number>N cast(final double d, final Class<N> cls) {
    if (float.class == cls)
      return (N)Float.valueOf((float)d);

    if (double.class == cls)
      return (N)Double.valueOf(d);

    if (byte.class == cls)
      return (N)Byte.valueOf((byte)(0 < d && d < 1 ? 0 : d * 100));

    if (short.class == cls)
      return (N)Short.valueOf((short)(0 < d && d < 1 ? 0 : d * 10000));

    if (int.class == cls)
      return (N)Integer.valueOf((int)(0 < d && d < 1 ? 0 : d * 100000000));

    if (long.class == cls)
      return (N)Long.valueOf((long)(0 < d && d < 1 ? 0 : d * 100000000000000000L));

    if (BigInteger.class.isAssignableFrom(cls))
      return (N)new BigInteger(String.valueOf(0 < d && d < 1 ? 0 : (long)(d * 100000000000000000L)));

    if (BigDecimal.class.isAssignableFrom(cls))
      return (N)new BigDecimal(String.valueOf(0 < d && d < 1 ? d : d * 100000000000000000L));

    throw new UnsupportedOperationException("Unsupported type: " + cls.getName());
  }

  private static <N extends Number>void test(final double value, final Class<N> cls, final Function<N,N> test, final Function<N,N> control) {
    test(value, cls, cls, test, control);
  }

  @SuppressWarnings("unused")
  private static <I extends Number,O extends Number>void test(final double value, final Class<I> in, final Class<O> out, final Function<I,O> test, final Function<I,O> control) {
    final I n = cast(value, in);
    final O expected = control.apply(n);
    final O actual = test.apply(n);
    if (expected instanceof Double)
      assertEquals(((Double)expected).doubleValue(), ((Double)actual).doubleValue(), 0.00000000000001d);
    else if (expected instanceof BigDecimal)
      assertEquals(expected + " != " + actual, 0, ((BigDecimal)expected).compareTo((BigDecimal)actual));
    else
      assertEquals(expected, actual);
  }

  @SuppressWarnings("unused")
  private static <I1 extends Number,I2 extends Number,O extends Number>void test2(final double v1, final double v2, final Class<I1> in1, final Class<I2> in2, final Class<O> out, final BiFunction<I1,I2,O> test, final BiFunction<I1,I2,O> control) {
    final I1 n1 = cast(v1, in1);
    final I2 n2 = cast(v2, in2);
    final O expected = control.apply(n1, n2);
    final O actual = test.apply(n1, n2);
    if (expected instanceof Double)
      assertEquals(((Double)expected).doubleValue(), ((Double)actual).doubleValue(), 0.0000000001d);
    else
      assertEquals(expected, actual);
  }

  @Test
  public void testAbsByte() {
    test(0, byte.class, SafeMath::abs, n -> (byte)Math.abs(n));
    test(1, byte.class, SafeMath::abs, n -> (byte)Math.abs(n));
    test(-1, byte.class, SafeMath::abs, n -> (byte)Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, byte.class, SafeMath::abs, n -> (byte)Math.abs(n));
  }

  @Test
  public void testAbsShort() {
    test(0, short.class, SafeMath::abs, n -> (short)Math.abs(n));
    test(1, short.class, SafeMath::abs, n -> (short)Math.abs(n));
    test(-1, short.class, SafeMath::abs, n -> (short)Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, short.class, SafeMath::abs, n -> (short)Math.abs(n));
  }

  @Test
  public void testAbsInt() {
    test(0, int.class, SafeMath::abs, n -> Math.abs(n));
    test(1, int.class, SafeMath::abs, n -> Math.abs(n));
    test(-1, int.class, SafeMath::abs, n -> Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, int.class, SafeMath::abs, n -> Math.abs(n));
  }

  @Test
  public void testAbsLong() {
    test(0, long.class, SafeMath::abs, n -> Math.abs(n));
    test(1, long.class, SafeMath::abs, n -> Math.abs(n));
    test(-1, long.class, SafeMath::abs, n -> Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, long.class, SafeMath::abs, n -> Math.abs(n));
  }

  @Test
  public void testAbsFloat() {
    test(0, float.class, SafeMath::abs, n -> Math.abs(n));
    test(1, float.class, SafeMath::abs, n -> Math.abs(n));
    test(-1, float.class, SafeMath::abs, n -> Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, float.class, SafeMath::abs, n -> Math.abs(n));
  }

  @Test
  public void testAbsDouble() {
    test(0, double.class, SafeMath::abs, n -> Math.abs(n));
    test(1, double.class, SafeMath::abs, n -> Math.abs(n));
    test(-1, double.class, SafeMath::abs, n -> Math.abs(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, double.class, SafeMath::abs, n -> Math.abs(n));
  }

  @Test
  public void testAbsBigInteger() {
    test(0, BigInteger.class, SafeMath::abs, n -> n.abs());
    test(1, BigInteger.class, SafeMath::abs, n -> n.abs());
    test(-1, BigInteger.class, SafeMath::abs, n -> n.abs());
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigInteger.class, SafeMath::abs, n -> n.abs());
  }

  @Test
  public void testAbsBigDecimal() {
    test(0, BigDecimal.class, SafeMath::abs, n -> n.abs());
    test(1, BigDecimal.class, SafeMath::abs, n -> n.abs());
    test(-1, BigDecimal.class, SafeMath::abs, n -> n.abs());
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigDecimal.class, SafeMath::abs, n -> n.abs());
  }

  @Test
  public void testAcosDouble() {
    test(0, double.class, SafeMath::acos, n -> Math.acos(n));
    test(1, double.class, SafeMath::acos, n -> Math.acos(n));
    test(-1, double.class, SafeMath::acos, n -> Math.acos(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::acos, n -> Math.acos(n));
  }

  @Test
  public void testAcosBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.acos(n, mc), n -> BigDecimalMath.acos(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.acos(n, mc), n -> BigDecimalMath.acos(new BigDecimal(n), mc));
  }

  @Test
  public void testAcosBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.acos(n, mc), n -> BigDecimalMath.acos(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.acos(n, mc), n -> BigDecimalMath.acos(n, mc));
  }

  @Test
  public void testAsinDouble() {
    test(0, double.class, SafeMath::asin, n -> Math.asin(n));
    test(1, double.class, SafeMath::asin, n -> Math.asin(n));
    test(-1, double.class, SafeMath::asin, n -> Math.asin(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::asin, n -> Math.asin(n));
  }

  @Test
  public void testAsinBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.asin(n, mc), n -> BigDecimalMath.asin(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.asin(n, mc), n -> BigDecimalMath.asin(new BigDecimal(n), mc));
  }

  @Test
  public void testAsinBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.asin(n, mc), n -> BigDecimalMath.asin(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.asin(n, mc), n -> BigDecimalMath.asin(n, mc));
  }

  @Test
  public void testAtanDouble() {
    test(0, double.class, SafeMath::atan, n -> Math.atan(n));
    test(1, double.class, SafeMath::atan, n -> Math.atan(n));
    test(-1, double.class, SafeMath::atan, n -> Math.atan(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::atan, n -> Math.atan(n));
  }

  @Test
  public void testAtanBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.atan(n, mc), n -> BigDecimalMath.atan(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.atan(n, mc), n -> BigDecimalMath.atan(new BigDecimal(n), mc));
  }

  @Test
  public void testAtanBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.atan(n, mc), n -> BigDecimalMath.atan(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.atan(n, mc), n -> BigDecimalMath.atan(n, mc));
  }

  @Test
  public void testAtan2Double() {
    test2(0, 0, double.class, double.class, double.class, (n1,n2) -> SafeMath.atan2(n1, n2), (n1,n2) -> Math.atan2(n1, n2));
    test2(1, 1, double.class, double.class, double.class, (n1,n2) -> SafeMath.atan2(n1, n2), (n1,n2) -> Math.atan2(n1, n2));
    test2(-1, -1, double.class, double.class, double.class, (n1,n2) -> SafeMath.atan2(n1, n2), (n1,n2) -> Math.atan2(n1, n2));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, double.class, double.class, (n1,n2) -> SafeMath.atan2(n1, n2), (n1,n2) -> Math.atan2(n1, n2));
  }

  @Test
  public void testAtan2BigDecimal() {
    test2(1, 1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.atan2(n1, n2, mc), (n1,n2) -> BigDecimalMath.atan2(n1, n2, mc));
    test2(-1, -1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.atan2(n1, n2, mc), (n1,n2) -> BigDecimalMath.atan2(n1, n2, mc));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.atan2(n1, n2, mc), (n1,n2) -> BigDecimalMath.atan2(n1, n2, mc));
  }

  @Test
  public void testCeilByte() {
    test(0, byte.class, SafeMath::ceil, n -> (byte)Math.ceil(n));
    test(1, byte.class, SafeMath::ceil, n -> (byte)Math.ceil(n));
    test(-1, byte.class, SafeMath::ceil, n -> (byte)Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, byte.class, SafeMath::ceil, n -> (byte)Math.ceil(n));
  }

  @Test
  public void testCeilShort() {
    test(0, short.class, SafeMath::ceil, n -> (short)Math.ceil(n));
    test(1, short.class, SafeMath::ceil, n -> (short)Math.ceil(n));
    test(-1, short.class, SafeMath::ceil, n -> (short)Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, short.class, SafeMath::ceil, n -> (short)Math.ceil(n));
  }

  @Test
  public void testCeilInt() {
    test(0, int.class, SafeMath::ceil, n -> (int)Math.ceil(n));
    test(1, int.class, SafeMath::ceil, n -> (int)Math.ceil(n));
    test(-1, int.class, SafeMath::ceil, n -> (int)Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, int.class, SafeMath::ceil, n -> (int)Math.ceil(n));
  }

  @Test
  public void testCeilLong() {
    test(0, long.class, SafeMath::ceil, n -> (long)Math.ceil(n));
    test(1, long.class, SafeMath::ceil, n -> (long)Math.ceil(n));
    test(-1, long.class, SafeMath::ceil, n -> (long)Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, long.class, SafeMath::ceil, n -> (long)Math.ceil(n));
  }

  @Test
  public void testCeilFloat() {
    test(0, float.class, SafeMath::ceil, n -> (float)Math.ceil(n));
    test(1, float.class, SafeMath::ceil, n -> (float)Math.ceil(n));
    test(-1, float.class, SafeMath::ceil, n -> (float)Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, float.class, SafeMath::ceil, n -> (float)Math.ceil(n));
  }

  @Test
  public void testCeilDouble() {
    test(0, double.class, SafeMath::ceil, n -> Math.ceil(n));
    test(1, double.class, SafeMath::ceil, n -> Math.ceil(n));
    test(-1, double.class, SafeMath::ceil, n -> Math.ceil(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, double.class, SafeMath::ceil, n -> Math.ceil(n));
  }

  @Test
  public void testCeilBigInteger() {
    test(0, BigInteger.class, SafeMath::ceil, n -> n);
    test(1, BigInteger.class, SafeMath::ceil, n -> n);
    test(-1, BigInteger.class, SafeMath::ceil, n -> n);
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigInteger.class, SafeMath::ceil, n -> n);
  }

  @Test
  public void testCeilBigDecimal() {
    test(0, BigDecimal.class, SafeMath::ceil, n -> n.setScale(0, RoundingMode.CEILING));
    test(1, BigDecimal.class, SafeMath::ceil, n -> n.setScale(0, RoundingMode.CEILING));
    test(-1, BigDecimal.class, SafeMath::ceil, n -> n.setScale(0, RoundingMode.CEILING));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigDecimal.class, SafeMath::ceil, n -> n.setScale(0, RoundingMode.CEILING));
  }

  @Test
  public void testCosLong() {
    test(0, long.class, double.class, SafeMath::cos, n -> Math.cos(n));
    test(1, long.class, double.class, SafeMath::cos, n -> Math.cos(n));
    test(-1, long.class, double.class, SafeMath::cos, n -> Math.cos(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), long.class, double.class, SafeMath::cos, n -> Math.cos(n));
  }

  @Test
  public void testCosDouble() {
    test(0, double.class, SafeMath::cos, n -> Math.cos(n));
    test(1, double.class, SafeMath::cos, n -> Math.cos(n));
    test(-1, double.class, SafeMath::cos, n -> Math.cos(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::cos, n -> Math.cos(n));
  }

  @Test
  public void testCosBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.cos(n, mc), n -> BigDecimalMath.cos(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.cos(n, mc), n -> BigDecimalMath.cos(new BigDecimal(n), mc));
  }

  @Test
  public void testCosBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.cos(n, mc), n -> BigDecimalMath.cos(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.cos(n, mc), n -> BigDecimalMath.cos(n, mc));
  }

  @Test
  public void testExpDouble() {
    test(0, double.class, SafeMath::exp, n -> Math.exp(n));
    test(1, double.class, SafeMath::exp, n -> Math.exp(n));
    test(-1, double.class, SafeMath::exp, n -> Math.exp(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::exp, n -> Math.exp(n));
  }

  @Test
  public void testExpBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.exp(n, mc), n -> BigDecimalMath.exp(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.exp(n, mc), n -> BigDecimalMath.exp(new BigDecimal(n), mc));
  }

  @Test
  public void testExpBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.exp(n, mc), n -> BigDecimalMath.exp(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.exp(n, mc), n -> BigDecimalMath.exp(n, mc));
  }

  @Test
  public void testFloorByte() {
    test(0, byte.class, SafeMath::floor, n -> (byte)Math.floor(n));
    test(1, byte.class, SafeMath::floor, n -> (byte)Math.floor(n));
    test(-1, byte.class, SafeMath::floor, n -> (byte)Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, byte.class, SafeMath::floor, n -> (byte)Math.floor(n));
  }

  @Test
  public void testFloorShort() {
    test(0, short.class, SafeMath::floor, n -> (short)Math.floor(n));
    test(1, short.class, SafeMath::floor, n -> (short)Math.floor(n));
    test(-1, short.class, SafeMath::floor, n -> (short)Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, short.class, SafeMath::floor, n -> (short)Math.floor(n));
  }

  @Test
  public void testFloorInt() {
    test(0, int.class, SafeMath::floor, n -> (int)Math.floor(n));
    test(1, int.class, SafeMath::floor, n -> (int)Math.floor(n));
    test(-1, int.class, SafeMath::floor, n -> (int)Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, int.class, SafeMath::floor, n -> (int)Math.floor(n));
  }

  @Test
  public void testFloorLong() {
    test(0, long.class, SafeMath::floor, n -> (long)Math.floor(n));
    test(1, long.class, SafeMath::floor, n -> (long)Math.floor(n));
    test(-1, long.class, SafeMath::floor, n -> (long)Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, long.class, SafeMath::floor, n -> (long)Math.floor(n));
  }

  @Test
  public void testFloorFloat() {
    test(0, float.class, SafeMath::floor, n -> (float)Math.floor(n));
    test(1, float.class, SafeMath::floor, n -> (float)Math.floor(n));
    test(-1, float.class, SafeMath::floor, n -> (float)Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, float.class, SafeMath::floor, n -> (float)Math.floor(n));
  }

  @Test
  public void testFloorDouble() {
    test(0, double.class, SafeMath::floor, n -> Math.floor(n));
    test(1, double.class, SafeMath::floor, n -> Math.floor(n));
    test(-1, double.class, SafeMath::floor, n -> Math.floor(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, double.class, SafeMath::floor, n -> Math.floor(n));
  }

  @Test
  public void testFloorBigInteger() {
    test(0, BigInteger.class, SafeMath::floor, n -> n);
    test(1, BigInteger.class, SafeMath::floor, n -> n);
    test(-1, BigInteger.class, SafeMath::floor, n -> n);
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigInteger.class, SafeMath::floor, n -> n);
  }

  @Test
  public void testFloorBigDecimal() {
    test(0, BigDecimal.class, SafeMath::floor, n -> n.setScale(0, RoundingMode.FLOOR));
    test(1, BigDecimal.class, SafeMath::floor, n -> n.setScale(0, RoundingMode.FLOOR));
    test(-1, BigDecimal.class, SafeMath::floor, n -> n.setScale(0, RoundingMode.FLOOR));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigDecimal.class, SafeMath::floor, n -> n.setScale(0, RoundingMode.FLOOR));
  }

  @Test
  public void testLogDouble() {
    test(0, double.class, SafeMath::log, n -> Math.log(n));
    test(1, double.class, SafeMath::log, n -> Math.log(n));
    test(-1, double.class, SafeMath::log, n -> Math.log(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::log, n -> Math.log(n));
  }

  @Test
  public void testLogBigInteger() {
    test(1, BigInteger.class, BigDecimal.class, n -> SafeMath.log(n, mc), n -> BigDecimalMath.log(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 100000), BigInteger.class, BigDecimal.class, n -> SafeMath.log(n, mc), n -> BigDecimalMath.log(new BigDecimal(n), mc));
  }

  @Test
  public void testLogBigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, n -> SafeMath.log(n, mc), n -> BigDecimalMath.log(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 10000), BigDecimal.class, BigDecimal.class, n -> SafeMath.log(n, mc), n -> BigDecimalMath.log(n, mc));
  }

  @Test
  public void testLogFloatFloat() {
    test2(0, 0, float.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, float.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, float.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), float.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleFloat() {
    test2(0, 0, float.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, float.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, float.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), float.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntFloat() {
    test2(0, 0, float.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, float.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, float.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), float.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongFloat() {
    test2(0, 0, float.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, float.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, float.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), float.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatDouble() {
    test2(0, 0, double.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, double.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, double.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleDouble() {
    test2(0, 0, double.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, double.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, double.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntDouble() {
    test2(0, 0, double.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, double.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, double.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongDouble() {
    test2(0, 0, double.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, double.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, double.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatInt() {
    test2(0, 0, int.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, int.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, int.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), int.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleInt() {
    test2(0, 0, int.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, int.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, int.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), int.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntInt() {
    test2(0, 0, int.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, int.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, int.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), int.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongInt() {
    test2(0, 0, int.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, int.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, int.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), int.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatLong() {
    test2(0, 0, long.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, long.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, long.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), long.class, float.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleLong() {
    test2(0, 0, long.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, long.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, long.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), long.class, double.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntLong() {
    test2(0, 0, long.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, long.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, long.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), long.class, int.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongLong() {
    test2(0, 0, long.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(1, 1, long.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    test2(-1, -1, long.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), long.class, long.class, double.class, (n1,n2) -> SafeMath.log(n1, n2), (n1,n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogBigDecimalBigInteger() {
    test2(1, 1, BigDecimal.class, BigInteger.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(new BigDecimal(n2), mc).divide(BigDecimalMath.log(n1, mc), mc));
    for (int i = 0; i < numTests; ++i)
      test2(Math.abs(nonZero() * 100000), Math.abs(nonZero() * 100000), BigDecimal.class, BigInteger.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(new BigDecimal(n2), mc).divide(BigDecimalMath.log(n1, mc), mc));
  }

  @Test
  public void testLogBigIntegerBigDecimal() {
    test2(1, 1, BigInteger.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(new BigDecimal(n1), mc), mc));
    for (int i = 0; i < numTests; ++i)
      test2(Math.abs(nonZero() * 100000), Math.abs(nonZero() * 100000), BigInteger.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(new BigDecimal(n1), mc), mc));
  }

  @Test
  public void testLogBigDecimalBigDecimal() {
    test2(1, 1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(n1, mc), mc));
    for (int i = 0; i < numTests; ++i)
      test2(Math.abs(nonZero() * 100000), Math.abs(nonZero() * 100000), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.log(n1, n2, mc), (n1,n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(n1, mc), mc));
  }

  @Test
  public void testPowDoubleDouble() {
    test2(0, 0, double.class, double.class, double.class, (n1,n2) -> SafeMath.pow(n1, n2), (n1,n2) -> Math.pow(n1, n2));
    test2(1, 1, double.class, double.class, double.class, (n1,n2) -> SafeMath.pow(n1, n2), (n1,n2) -> Math.pow(n1, n2));
    test2(-1, -1, double.class, double.class, double.class, (n1,n2) -> SafeMath.pow(n1, n2), (n1,n2) -> Math.pow(n1, n2));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), double.class, double.class, double.class, (n1,n2) -> SafeMath.pow(n1, n2), (n1,n2) -> Math.pow(n1, n2));
  }

  @Test
  public void testPowBigIntegerBigInteger() {
    test2(0, 0, BigInteger.class, BigInteger.class, BigInteger.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(new BigDecimal(n1), new BigDecimal(n2), mc).toBigInteger());
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), BigInteger.class, BigInteger.class, BigInteger.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(new BigDecimal(n1), new BigDecimal(n2), mc).toBigInteger());
  }

  @Test
  public void testPowBigDecimalBigInteger() {
    test2(0, 0, BigDecimal.class, BigInteger.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(n1, new BigDecimal(n2), mc));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), BigDecimal.class, BigInteger.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(n1, new BigDecimal(n2), mc));
  }

  @Test
  public void testPowBigIntegerBigDecimal() {
    test2(0, 0, BigInteger.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(new BigDecimal(n1), n2, mc));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), BigInteger.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(new BigDecimal(n1), n2, mc));
  }

  @Test
  public void testPowBigDecimalBigDecimal() {
    test2(0, 0, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(n1, n2, mc));
    for (int i = 0; i < numTests; ++i)
      test2(random.nextDouble(), random.nextDouble(), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1,n2) -> SafeMath.pow(n1, n2, mc), (n1,n2) -> BigDecimalMath.pow(n1, n2, mc));
  }

  @Test
  public void testLog10Double() {
    test(0, double.class, SafeMath::log10, n -> Math.log10(n));
    test(1, double.class, SafeMath::log10, n -> Math.log10(n));
    test(-1, double.class, SafeMath::log10, n -> Math.log10(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::log10, n -> Math.log10(n));
  }

  @Test
  public void testLog10BigInteger() {
    test(1, BigInteger.class, BigDecimal.class, n -> SafeMath.log10(n, mc), n -> BigDecimalMath.log10(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 100000), BigInteger.class, BigDecimal.class, n -> SafeMath.log10(n, mc), n -> BigDecimalMath.log10(new BigDecimal(n), mc));
  }

  @Test
  public void testLog10BigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, n -> SafeMath.log10(n, mc), n -> BigDecimalMath.log10(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 10000), BigDecimal.class, BigDecimal.class, n -> SafeMath.log10(n, mc), n -> BigDecimalMath.log10(n, mc));
  }

  @Test
  public void testLog2Double() {
    test(0, double.class, SafeMath::log2, n -> Math.log(n) / Math.log(2));
    test(1, double.class, SafeMath::log2, n -> Math.log(n) / Math.log(2));
    test(-1, double.class, SafeMath::log2, n -> Math.log(n) / Math.log(2));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::log2, n -> Math.log(n) / Math.log(2));
  }

  @Test
  public void testLog2BigInteger() {
    test(1, BigInteger.class, BigDecimal.class, n -> SafeMath.log2(n, mc), n -> BigDecimalMath.log2(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 100000), BigInteger.class, BigDecimal.class, n -> SafeMath.log2(n, mc), n -> BigDecimalMath.log2(new BigDecimal(n), mc));
  }

  @Test
  public void testLog2BigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, n -> SafeMath.log2(n, mc), n -> BigDecimalMath.log2(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(Math.abs(nonZero() * 10000), BigDecimal.class, BigDecimal.class, n -> SafeMath.log2(n, mc), n -> BigDecimalMath.log2(n, mc));
  }

  @Test
  public void testRoundByte() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, byte.class, n -> SafeMath.round(n, s), n -> n);
      test(1, byte.class, n -> SafeMath.round(n, s), n -> n);
      test(-1, byte.class, n -> SafeMath.round(n, s), n -> n);
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, byte.class, n -> SafeMath.round(n, s), n -> n);
    }
  }

  @Test
  public void testRoundShort() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, short.class, n -> SafeMath.round(n, s), n -> n);
      test(1, short.class, n -> SafeMath.round(n, s), n -> n);
      test(-1, short.class, n -> SafeMath.round(n, s), n -> n);
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, short.class, n -> SafeMath.round(n, s), n -> n);
    }
  }

  @Test
  public void testRoundInt() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, int.class, n -> SafeMath.round(n, s), n -> n);
      test(1, int.class, n -> SafeMath.round(n, s), n -> n);
      test(-1, int.class, n -> SafeMath.round(n, s), n -> n);
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, int.class, n -> SafeMath.round(n, s), n -> n);
    }
  }

  @Test
  public void testRoundLong() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, long.class, n -> SafeMath.round(n, s), n -> n);
      test(1, long.class, n -> SafeMath.round(n, s), n -> n);
      test(-1, long.class, n -> SafeMath.round(n, s), n -> n);
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, long.class, n -> SafeMath.round(n, s), n -> n);
    }
  }

  @Test
  public void testRoundFloat() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, float.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      test(1, float.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      test(-1, float.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, float.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
    }
  }

  @Test
  public void testRoundDouble() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, double.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      test(1, double.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      test(-1, double.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, double.class, n -> SafeMath.round(n, s), n -> SafeMath.round(n, s));
    }
  }

  @Test
  public void testRoundBigInteger() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, BigInteger.class, n -> SafeMath.round(n, s), n -> n);
      test(1, BigInteger.class, n -> SafeMath.round(n, s), n -> n);
      test(-1, BigInteger.class, n -> SafeMath.round(n, s), n -> n);
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, BigInteger.class, n -> SafeMath.round(n, s), n -> n);
    }
  }

  @Test
  public void testRoundBigDecimal() {
    for (int j = 0; j < 3; ++j) {
      final int s = j;
      test(0, BigDecimal.class, n -> SafeMath.round(n, s), n -> n.setScale(s, RoundingMode.HALF_UP));
      test(1, BigDecimal.class, n -> SafeMath.round(n, s), n -> n.setScale(s, RoundingMode.HALF_UP));
      test(-1, BigDecimal.class, n -> SafeMath.round(n, s), n -> n.setScale(s, RoundingMode.HALF_UP));
      for (int i = 0; i < numTests; ++i)
        test(random.nextDouble() * 10, BigDecimal.class, n -> SafeMath.round(n, s), n -> n.setScale(s, RoundingMode.HALF_UP));
    }
  }

  @Test
  public void testSignumByte() {
    test(0, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumShort() {
    test(0, short.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, short.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, short.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, short.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumInt() {
    test(0, int.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, int.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, int.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, int.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumLong() {
    test(0, long.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, long.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, long.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, long.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumFloat() {
    test(0, float.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, float.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, float.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, float.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumDouble() {
    test(0, double.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(1, double.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    test(-1, double.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, double.class, byte.class, SafeMath::signum, n -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumBigInteger() {
    test(0, BigInteger.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    test(1, BigInteger.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    test(-1, BigInteger.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigInteger.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
  }

  @Test
  public void testSignumBigDecimal() {
    test(0, BigDecimal.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    test(1, BigDecimal.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    test(-1, BigDecimal.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble() * 10, BigDecimal.class, byte.class, SafeMath::signum, n -> (byte)n.signum());
  }

  @Test
  public void testSinDouble() {
    test(0, double.class, SafeMath::sin, n -> Math.sin(n));
    test(1, double.class, SafeMath::sin, n -> Math.sin(n));
    test(-1, double.class, SafeMath::sin, n -> Math.sin(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::sin, n -> Math.sin(n));
  }

  @Test
  public void testSinBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.sin(n, mc), n -> BigDecimalMath.sin(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.sin(n, mc), n -> BigDecimalMath.sin(new BigDecimal(n), mc));
  }

  @Test
  public void testSinBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.sin(n, mc), n -> BigDecimalMath.sin(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.sin(n, mc), n -> BigDecimalMath.sin(n, mc));
  }

  @Test
  public void testSqrtDouble() {
    test(0, double.class, SafeMath::sqrt, n -> Math.sqrt(n));
    test(1, double.class, SafeMath::sqrt, n -> Math.sqrt(n));
    test(-1, double.class, SafeMath::sqrt, n -> Math.sqrt(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::sqrt, n -> Math.sqrt(n));
  }

  @Test
  public void testSqrtBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.sqrt(n, mc), n -> BigDecimalMath.sqrt(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.sqrt(n, mc), n -> BigDecimalMath.sqrt(new BigDecimal(n), mc));
  }

  @Test
  public void testSqrtBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.sqrt(n, mc), n -> BigDecimalMath.sqrt(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.sqrt(n, mc), n -> BigDecimalMath.sqrt(n, mc));
  }

  @Test
  public void testTanDouble() {
    test(0, double.class, SafeMath::tan, n -> Math.tan(n));
    test(1, double.class, SafeMath::tan, n -> Math.tan(n));
    test(-1, double.class, SafeMath::tan, n -> Math.tan(n));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), double.class, SafeMath::tan, n -> Math.tan(n));
  }

  @Test
  public void testTanBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, n -> SafeMath.tan(n, mc), n -> BigDecimalMath.tan(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigInteger.class, BigDecimal.class, n -> SafeMath.tan(n, mc), n -> BigDecimalMath.tan(new BigDecimal(n), mc));
  }

  @Test
  public void testTanBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, n -> SafeMath.tan(n, mc), n -> BigDecimalMath.tan(n, mc));
    for (int i = 0; i < numTests; ++i)
      test(random.nextDouble(), BigDecimal.class, BigDecimal.class, n -> SafeMath.tan(n, mc), n -> BigDecimalMath.tan(n, mc));
  }
}