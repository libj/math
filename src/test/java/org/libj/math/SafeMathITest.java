/* Copyright (c) 2008 Seva Safris, LibJ
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

import org.junit.Test;
import org.libj.lang.BigDecimals;
import org.libj.test.TestAide;

import ch.obermuhlner.math.big.BigDecimalMath;

public class SafeMathITest {
  private static final int numTests = 10000;
  private static final Random random = new Random();
  private static final MathContext mc = MathContext.DECIMAL64;

  private static double d10() {
    return (Math.random() < 0.5 ? -1 : 1) * (random.nextDouble() + 1);
  }

  private static double d0() {
    return (Math.random() < 0.5 ? -1 : 1) * random.nextDouble();
  }

  @SuppressWarnings("unchecked")
  private static <N extends Number> N cast(final boolean upscale, final double d, final Class<N> cls) {
    if (float.class == cls)
      return (N)Float.valueOf((float)d);

    if (double.class == cls)
      return (N)Double.valueOf(d);

    if (byte.class == cls)
      return (N)Byte.valueOf((byte)(0 < d && d < 1 ? 0 : upscale ? d * 100 : d));

    if (short.class == cls)
      return (N)Short.valueOf((short)(0 < d && d < 1 ? 0 : upscale ? d * 10000 : d));

    if (int.class == cls)
      return (N)Integer.valueOf((int)(0 < d && d < 1 ? 0 : upscale ? d * 100000000 : d));

    if (long.class == cls)
      return (N)Long.valueOf((long)(0 < d && d < 1 ? 0 : upscale ? d * 100000000000000000L : d));

    if (BigInteger.class.isAssignableFrom(cls))
      return (N)new BigInteger(String.valueOf(0 < d && d < 1 ? 0 : (long)(upscale ? d * 100000000000000000L : d)));

    if (BigDecimal.class.isAssignableFrom(cls))
      return (N)new BigDecimal(String.valueOf(0 < d && d < 1 ? d : upscale ? d * 100000000000000000L : d));

    throw new UnsupportedOperationException("Unsupported type: " + cls.getName());
  }

  private static <N extends Number> void test(final double value, final Class<N> cls, final Function<N,N> test, final Function<N,N> control) {
    test(value, cls, cls, test, control);
  }

  private static <I extends Number,O extends Number> void test(final double value, final Class<I> in, final Class<O> out, final Function<I,O> test, final Function<I,O> control) {
    test0(true, value, in, out, test, control);
  }

  private static <I extends Number,O extends Number> void test1(final double value, final Class<I> in, final Class<O> out, final Function<I,O> test, final Function<I,O> control) {
    test0(false, value, in, out, test, control);
  }

  @SuppressWarnings("unused")
  private static <I extends Number,O extends Number> void test0(final boolean upscale, final double value, final Class<I> in, final Class<O> out, final Function<I,O> test, final Function<I,O> control) {
    final I n = cast(upscale, value, in);
    final O expected = control.apply(n);
    final O actual = test.apply(n);
    if (expected instanceof Double)
      assertEquals(((Double)expected).doubleValue(), ((Double)actual).doubleValue(), 0.00000000000001d);
    else if (expected instanceof BigDecimal)
      assertEquals(expected + " != " + actual, 0, ((BigDecimal)expected).compareTo((BigDecimal)actual));
    else
      assertEquals(expected, actual);
  }

  private static <I1 extends Number,I2 extends Number,O extends Number> void test(final double v1, final double v2, final Class<I1> in1, final Class<I2> in2, final Class<O> out, final BiFunction<I1,I2,O> test, final BiFunction<I1,I2,O> control) {
    test0(true, v1, v2, in1, in2, out, test, control);
  }

  private static <I1 extends Number,I2 extends Number,O extends Number> void test1(final double v1, final double v2, final Class<I1> in1, final Class<I2> in2, final Class<O> out, final BiFunction<I1,I2,O> test, final BiFunction<I1,I2,O> control) {
    test0(false, v1, v2, in1, in2, out, test, control);
  }

  @SuppressWarnings("unused")
  private static <I1 extends Number,I2 extends Number,O extends Number> void test0(final boolean upscale, final double v1, final double v2, final Class<I1> in1, final Class<I2> in2, final Class<O> out, final BiFunction<I1,I2,O> test, final BiFunction<I1,I2,O> control) {
    final I1 n1 = cast(upscale, v1, in1);
    final I2 n2 = cast(upscale, v2, in2);
    final O expected = control.apply(n1, n2);
    final O actual = test.apply(n1, n2);
    if (expected instanceof Double)
      assertEquals(((Double)expected).doubleValue(), ((Double)actual).doubleValue(), 0.0000000001d);
    else
      assertEquals(expected, actual);
  }

  @Test
  public void testAbsByte() {
    test(0, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    test(1, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    test(-1, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
  }

  @Test
  public void testAbsShort() {
    test(0, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    test(1, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    test(-1, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, short.class, SafeMath::abs, (final Short n) -> (short)Math.abs(n));
  }

  @Test
  public void testAbsInt() {
    test(0, int.class, SafeMath::abs, (final Integer n) -> Math.abs(n));
    test(1, int.class, SafeMath::abs, (final Integer n) -> Math.abs(n));
    test(-1, int.class, SafeMath::abs, (final Integer n) -> Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, int.class, SafeMath::abs, (final Integer n) -> Math.abs(n));
  }

  @Test
  public void testAbsLong() {
    test(0, long.class, SafeMath::abs, (final Long n) -> Math.abs(n));
    test(1, long.class, SafeMath::abs, (final Long n) -> Math.abs(n));
    test(-1, long.class, SafeMath::abs, (final Long n) -> Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, long.class, SafeMath::abs, (final Long n) -> Math.abs(n));
  }

  @Test
  public void testAbsFloat() {
    test(0, float.class, SafeMath::abs, (final Float n) -> Math.abs(n));
    test(1, float.class, SafeMath::abs, (final Float n) -> Math.abs(n));
    test(-1, float.class, SafeMath::abs, (final Float n) -> Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, float.class, SafeMath::abs, (final Float n) -> Math.abs(n));
  }

  @Test
  public void testAbsDouble() {
    test(0, double.class, SafeMath::abs, (final Double n) -> Math.abs(n));
    test(1, double.class, SafeMath::abs, (final Double n) -> Math.abs(n));
    test(-1, double.class, SafeMath::abs, (final Double n) -> Math.abs(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, double.class, SafeMath::abs, (final Double n) -> Math.abs(n));
  }

  @Test
  public void testAbsBigInteger() {
    test(0, BigInteger.class, SafeMath::abs, (final BigInteger n) -> n.abs());
    test(1, BigInteger.class, SafeMath::abs, (final BigInteger n) -> n.abs());
    test(-1, BigInteger.class, SafeMath::abs, (final BigInteger n) -> n.abs());
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigInteger.class, SafeMath::abs, (final BigInteger n) -> n.abs());
  }

  @Test
  public void testAbsBigDecimal() {
    test(0, BigDecimal.class, SafeMath::abs, (final BigDecimal n) -> n.abs());
    test(1, BigDecimal.class, SafeMath::abs, (final BigDecimal n) -> n.abs());
    test(-1, BigDecimal.class, SafeMath::abs, (final BigDecimal n) -> n.abs());
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigDecimal.class, SafeMath::abs, (final BigDecimal n) -> n.abs());
  }

  @Test
  public void testAcosDouble() {
    test(0, double.class, SafeMath::acos, (final Double n) -> Math.acos(n));
    test(1, double.class, SafeMath::acos, (final Double n) -> Math.acos(n));
    test(-1, double.class, SafeMath::acos, (final Double n) -> Math.acos(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::acos, (final Double n) -> Math.acos(n));
  }

  @Test
  public void testAcosBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.acos(n, mc), (final BigInteger n) -> BigDecimalMath.acos(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.acos(n, mc), (final BigInteger n) -> BigDecimalMath.acos(new BigDecimal(n), mc));
  }

  @Test
  public void testAcosBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.acos(n, mc), (final BigDecimal n) -> BigDecimalMath.acos(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.acos(n, mc), (final BigDecimal n) -> BigDecimalMath.acos(n, mc));
  }

  @Test
  public void testAsinDouble() {
    test(0, double.class, SafeMath::asin, (final Double n) -> Math.asin(n));
    test(1, double.class, SafeMath::asin, (final Double n) -> Math.asin(n));
    test(-1, double.class, SafeMath::asin, (final Double n) -> Math.asin(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::asin, (final Double n) -> Math.asin(n));
  }

  @Test
  public void testAsinBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.asin(n, mc), (final BigInteger n) -> BigDecimalMath.asin(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.asin(n, mc), (final BigInteger n) -> BigDecimalMath.asin(new BigDecimal(n), mc));
  }

  @Test
  public void testAsinBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.asin(n, mc), (final BigDecimal n) -> BigDecimalMath.asin(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.asin(n, mc), (final BigDecimal n) -> BigDecimalMath.asin(n, mc));
  }

  @Test
  public void testAtanDouble() {
    test(0, double.class, SafeMath::atan, (final Double n) -> Math.atan(n));
    test(1, double.class, SafeMath::atan, (final Double n) -> Math.atan(n));
    test(-1, double.class, SafeMath::atan, (final Double n) -> Math.atan(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::atan, (final Double n) -> Math.atan(n));
  }

  @Test
  public void testAtanBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.atan(n, mc), (final BigInteger n) -> BigDecimalMath.atan(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.atan(n, mc), (final BigInteger n) -> BigDecimalMath.atan(new BigDecimal(n), mc));
  }

  @Test
  public void testAtanBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.atan(n, mc), (final BigDecimal n) -> BigDecimalMath.atan(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.atan(n, mc), (final BigDecimal n) -> BigDecimalMath.atan(n, mc));
  }

  @Test
  public void testAtan2Double() {
    test(0, 0, double.class, double.class, double.class, (n1, n2) -> SafeMath.atan2(n1, n2), (n1, n2) -> Math.atan2(n1, n2));
    test(1, 1, double.class, double.class, double.class, (n1, n2) -> SafeMath.atan2(n1, n2), (n1, n2) -> Math.atan2(n1, n2));
    test(-1, -1, double.class, double.class, double.class, (n1, n2) -> SafeMath.atan2(n1, n2), (n1, n2) -> Math.atan2(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, double.class, double.class, (n1, n2) -> SafeMath.atan2(n1, n2), (n1, n2) -> Math.atan2(n1, n2));
  }

  @Test
  public void testAtan2BigDecimal() {
    test(1, 1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.atan2(n1, n2, mc), (n1, n2) -> BigDecimalMath.atan2(n1, n2, mc));
    test(-1, -1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.atan2(n1, n2, mc), (n1, n2) -> BigDecimalMath.atan2(n1, n2, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.atan2(n1, n2, mc), (n1, n2) -> BigDecimalMath.atan2(n1, n2, mc));
  }

  @Test
  public void testCeilByte() {
    test(0, byte.class, SafeMath::ceil, (final Byte n) -> (byte)Math.ceil(n));
    test(1, byte.class, SafeMath::ceil, (final Byte n) -> (byte)Math.ceil(n));
    test(-1, byte.class, SafeMath::ceil, (final Byte n) -> (byte)Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, byte.class, SafeMath::ceil, (final Byte n) -> (byte)Math.ceil(n));
  }

  @Test
  public void testCeilShort() {
    test(0, short.class, SafeMath::ceil, (final Short n) -> (short)Math.ceil(n));
    test(1, short.class, SafeMath::ceil, (final Short n) -> (short)Math.ceil(n));
    test(-1, short.class, SafeMath::ceil, (final Short n) -> (short)Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, short.class, SafeMath::ceil, (final Short n) -> (short)Math.ceil(n));
  }

  @Test
  public void testCeilInt() {
    test(0, int.class, SafeMath::ceil, (final Integer n) -> (int)Math.ceil(n));
    test(1, int.class, SafeMath::ceil, (final Integer n) -> (int)Math.ceil(n));
    test(-1, int.class, SafeMath::ceil, (final Integer n) -> (int)Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, int.class, SafeMath::ceil, (final Integer n) -> (int)Math.ceil(n));
  }

  @Test
  public void testCeilLong() {
    test(0, long.class, SafeMath::ceil, (final Long n) -> (long)Math.ceil(n));
    test(1, long.class, SafeMath::ceil, (final Long n) -> (long)Math.ceil(n));
    test(-1, long.class, SafeMath::ceil, (final Long n) -> (long)Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, long.class, SafeMath::ceil, (final Long n) -> (long)Math.ceil(n));
  }

  @Test
  public void testCeilFloat() {
    test(0, float.class, SafeMath::ceil, (final Float n) -> (float)Math.ceil(n));
    test(1, float.class, SafeMath::ceil, (final Float n) -> (float)Math.ceil(n));
    test(-1, float.class, SafeMath::ceil, (final Float n) -> (float)Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, float.class, SafeMath::ceil, (final Float n) -> (float)Math.ceil(n));
  }

  @Test
  public void testCeilDouble() {
    test(0, double.class, SafeMath::ceil, (final Double n) -> Math.ceil(n));
    test(1, double.class, SafeMath::ceil, (final Double n) -> Math.ceil(n));
    test(-1, double.class, SafeMath::ceil, (final Double n) -> Math.ceil(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, double.class, SafeMath::ceil, (final Double n) -> Math.ceil(n));
  }

  @Test
  public void testCeilBigInteger() {
    test(0, BigInteger.class, SafeMath::ceil, (final BigInteger n) -> n);
    test(1, BigInteger.class, SafeMath::ceil, (final BigInteger n) -> n);
    test(-1, BigInteger.class, SafeMath::ceil, (final BigInteger n) -> n);
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigInteger.class, SafeMath::ceil, (final BigInteger n) -> n);
  }

  @Test
  public void testCeilBigDecimal() {
    test(0, BigDecimal.class, SafeMath::ceil, (final BigDecimal n) -> n.setScale(0, RoundingMode.CEILING));
    test(1, BigDecimal.class, SafeMath::ceil, (final BigDecimal n) -> n.setScale(0, RoundingMode.CEILING));
    test(-1, BigDecimal.class, SafeMath::ceil, (final BigDecimal n) -> n.setScale(0, RoundingMode.CEILING));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigDecimal.class, SafeMath::ceil, (final BigDecimal n) -> n.setScale(0, RoundingMode.CEILING));
  }

  @Test
  public void testCosLong() {
    test(0, long.class, double.class, SafeMath::cos, (final Long n) -> Math.cos(n));
    test(1, long.class, double.class, SafeMath::cos, (final Long n) -> Math.cos(n));
    test(-1, long.class, double.class, SafeMath::cos, (final Long n) -> Math.cos(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), long.class, double.class, SafeMath::cos, (final Long n) -> Math.cos(n));
  }

  @Test
  public void testCosDouble() {
    test(0, double.class, SafeMath::cos, (final Double n) -> Math.cos(n));
    test(1, double.class, SafeMath::cos, (final Double n) -> Math.cos(n));
    test(-1, double.class, SafeMath::cos, (final Double n) -> Math.cos(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::cos, (final Double n) -> Math.cos(n));
  }

  @Test
  public void testCosBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.cos(n, mc), (final BigInteger n) -> BigDecimalMath.cos(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.cos(n, mc), (final BigInteger n) -> BigDecimalMath.cos(new BigDecimal(n), mc));
  }

  @Test
  public void testCosBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.cos(n, mc), (final BigDecimal n) -> BigDecimalMath.cos(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.cos(n, mc), (final BigDecimal n) -> BigDecimalMath.cos(n, mc));
  }

  @Test
  public void testExpDouble() {
    test(0, double.class, SafeMath::exp, (final Double n) -> Math.exp(n));
    test(1, double.class, SafeMath::exp, (final Double n) -> Math.exp(n));
    test(-1, double.class, SafeMath::exp, (final Double n) -> Math.exp(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::exp, (final Double n) -> Math.exp(n));
  }

  @Test
  public void testExpBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.exp(n, mc), (final BigInteger n) -> BigDecimalMath.exp(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.exp(n, mc), (final BigInteger n) -> BigDecimalMath.exp(new BigDecimal(n), mc));
  }

  @Test
  public void testExpBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.exp(n, mc), (final BigDecimal n) -> BigDecimalMath.exp(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.exp(n, mc), (final BigDecimal n) -> BigDecimalMath.exp(n, mc));
  }

  @Test
  public void testFloorByte() {
    test(0, byte.class, SafeMath::floor, (final Byte n) -> (byte)Math.floor(n));
    test(1, byte.class, SafeMath::floor, (final Byte n) -> (byte)Math.floor(n));
    test(-1, byte.class, SafeMath::floor, (final Byte n) -> (byte)Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, byte.class, SafeMath::floor, (final Byte n) -> (byte)Math.floor(n));
  }

  @Test
  public void testFloorShort() {
    test(0, short.class, SafeMath::floor, (final Short n) -> (short)Math.floor(n));
    test(1, short.class, SafeMath::floor, (final Short n) -> (short)Math.floor(n));
    test(-1, short.class, SafeMath::floor, (final Short n) -> (short)Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, short.class, SafeMath::floor, (final Short n) -> (short)Math.floor(n));
  }

  @Test
  public void testFloorInt() {
    test(0, int.class, SafeMath::floor, (final Integer n) -> (int)Math.floor(n));
    test(1, int.class, SafeMath::floor, (final Integer n) -> (int)Math.floor(n));
    test(-1, int.class, SafeMath::floor, (final Integer n) -> (int)Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, int.class, SafeMath::floor, (final Integer n) -> (int)Math.floor(n));
  }

  @Test
  public void testFloorLong() {
    test(0, long.class, SafeMath::floor, (final Long n) -> (long)Math.floor(n));
    test(1, long.class, SafeMath::floor, (final Long n) -> (long)Math.floor(n));
    test(-1, long.class, SafeMath::floor, (final Long n) -> (long)Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, long.class, SafeMath::floor, (final Long n) -> (long)Math.floor(n));
  }

  @Test
  public void testFloorFloat() {
    test(0, float.class, SafeMath::floor, (final Float n) -> (float)Math.floor(n));
    test(1, float.class, SafeMath::floor, (final Float n) -> (float)Math.floor(n));
    test(-1, float.class, SafeMath::floor, (final Float n) -> (float)Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, float.class, SafeMath::floor, (final Float n) -> (float)Math.floor(n));
  }

  @Test
  public void testFloorDouble() {
    test(0, double.class, SafeMath::floor, (final Double n) -> Math.floor(n));
    test(1, double.class, SafeMath::floor, (final Double n) -> Math.floor(n));
    test(-1, double.class, SafeMath::floor, (final Double n) -> Math.floor(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, double.class, SafeMath::floor, (final Double n) -> Math.floor(n));
  }

  @Test
  public void testFloorBigInteger() {
    test(0, BigInteger.class, SafeMath::floor, (final BigInteger n) -> n);
    test(1, BigInteger.class, SafeMath::floor, (final BigInteger n) -> n);
    test(-1, BigInteger.class, SafeMath::floor, (final BigInteger n) -> n);
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigInteger.class, SafeMath::floor, (final BigInteger n) -> n);
  }

  @Test
  public void testFloorBigDecimal() {
    test(0, BigDecimal.class, SafeMath::floor, (final BigDecimal n) -> n.setScale(0, RoundingMode.FLOOR));
    test(1, BigDecimal.class, SafeMath::floor, (final BigDecimal n) -> n.setScale(0, RoundingMode.FLOOR));
    test(-1, BigDecimal.class, SafeMath::floor, (final BigDecimal n) -> n.setScale(0, RoundingMode.FLOOR));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigDecimal.class, SafeMath::floor, (final BigDecimal n) -> n.setScale(0, RoundingMode.FLOOR));
  }

  @Test
  public void testLogDouble() {
    test(0, double.class, SafeMath::log, (final Double n) -> Math.log(n));
    test(1, double.class, SafeMath::log, (final Double n) -> Math.log(n));
    test(-1, double.class, SafeMath::log, (final Double n) -> Math.log(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::log, (final Double n) -> Math.log(n));
  }

  @Test
  public void testLogBigInteger() {
    test(1, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log(n, mc), (final BigInteger n) -> BigDecimalMath.log(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log(n, mc), (final BigInteger n) -> BigDecimalMath.log(new BigDecimal(n), mc));
  }

  @Test
  public void testLogBigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log(n, mc), (final BigDecimal n) -> BigDecimalMath.log(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log(n, mc), (final BigDecimal n) -> BigDecimalMath.log(n, mc));
  }

  @Test
  public void testLogFloatFloat() {
    test(0, 0, float.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, float.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, float.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), float.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleFloat() {
    test(0, 0, float.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, float.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, float.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), float.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntFloat() {
    test(0, 0, float.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, float.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, float.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), float.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongFloat() {
    test(0, 0, float.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, float.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, float.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), float.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatDouble() {
    test(0, 0, double.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, double.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, double.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleDouble() {
    test(0, 0, double.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, double.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, double.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntDouble() {
    test(0, 0, double.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, double.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, double.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongDouble() {
    test(0, 0, double.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, double.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, double.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatInt() {
    test(0, 0, int.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, int.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, int.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), int.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleInt() {
    test(0, 0, int.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, int.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, int.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), int.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntInt() {
    test(0, 0, int.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, int.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, int.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), int.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongInt() {
    test(0, 0, int.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, int.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, int.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), int.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogFloatLong() {
    test(0, 0, long.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, long.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, long.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), long.class, float.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogDoubleLong() {
    test(0, 0, long.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, long.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, long.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), long.class, double.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogIntLong() {
    test(0, 0, long.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, long.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, long.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), long.class, int.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogLongLong() {
    test(0, 0, long.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(1, 1, long.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    test(-1, -1, long.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), long.class, long.class, double.class, (n1, n2) -> SafeMath.log(n1, n2), (n1, n2) -> Math.log(n2) / Math.log(n1));
  }

  @Test
  public void testLogBigDecimalBigInteger() {
    test(1, 1, BigDecimal.class, BigInteger.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(new BigDecimal(n2), mc).divide(BigDecimalMath.log(n1, mc), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), Math.abs(d10()), BigDecimal.class, BigInteger.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(new BigDecimal(n2), mc).divide(BigDecimalMath.log(n1, mc), mc));
  }

  @Test
  public void testLogBigIntegerBigDecimal() {
    test(1, 1, BigInteger.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(new BigDecimal(n1), mc), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), Math.abs(d10()), BigInteger.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(new BigDecimal(n1), mc), mc));
  }

  @Test
  public void testLogBigDecimalBigDecimal() {
    test(1, 1, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(n1, mc), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), Math.abs(d10()), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.log(n1, n2, mc), (n1, n2) -> BigDecimalMath.log(n2, mc).divide(BigDecimalMath.log(n1, mc), mc));
  }

  @Test
  public void testPowDoubleDouble() {
    test(0, 0, double.class, double.class, double.class, (n1, n2) -> SafeMath.pow(n1, n2), (n1, n2) -> Math.pow(n1, n2));
    test(1, 1, double.class, double.class, double.class, (n1, n2) -> SafeMath.pow(n1, n2), (n1, n2) -> Math.pow(n1, n2));
    test(-1, -1, double.class, double.class, double.class, (n1, n2) -> SafeMath.pow(n1, n2), (n1, n2) -> Math.pow(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), d0(), double.class, double.class, double.class, (n1, n2) -> SafeMath.pow(n1, n2), (n1, n2) -> Math.pow(n1, n2));
  }

  @Test
  public void testPowBigIntegerBigInteger() {
    test(0, 0, BigInteger.class, BigInteger.class, BigInteger.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(new BigDecimal(n1), new BigDecimal(n2), mc).toBigInteger());
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), d0(), BigInteger.class, BigInteger.class, BigInteger.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(new BigDecimal(n1), new BigDecimal(n2), mc).toBigInteger());
  }

  @Test
  public void testPowBigDecimalBigInteger() {
    test(0, 0, BigDecimal.class, BigInteger.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(n1, new BigDecimal(n2), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test1(d0(), d0(), BigDecimal.class, BigInteger.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(n1, new BigDecimal(n2), mc));
  }

  @Test
  public void testPowBigIntegerBigDecimal() {
    test(0, 0, BigInteger.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(new BigDecimal(n1), n2, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), Math.abs(d0()), BigInteger.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(new BigDecimal(n1), n2, mc));
  }

  @Test
  public void testPowBigDecimalBigDecimal() {
    test(0, 0, BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(n1, n2, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), Math.abs(d0()), BigDecimal.class, BigDecimal.class, BigDecimal.class, (n1, n2) -> SafeMath.pow(n1, n2, mc), (n1, n2) -> BigDecimalMath.pow(n1, n2, mc));
  }

  @Test
  public void testLog10Double() {
    test(0, double.class, SafeMath::log10, (final Double n) -> Math.log10(n));
    test(1, double.class, SafeMath::log10, (final Double n) -> Math.log10(n));
    test(-1, double.class, SafeMath::log10, (final Double n) -> Math.log10(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::log10, (final Double n) -> Math.log10(n));
  }

  @Test
  public void testLog10BigInteger() {
    test(1, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log10(n, mc), (final BigInteger n) -> BigDecimalMath.log10(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log10(n, mc), (final BigInteger n) -> BigDecimalMath.log10(new BigDecimal(n), mc));
  }

  @Test
  public void testLog10BigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log10(n, mc), (final BigDecimal n) -> BigDecimalMath.log10(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log10(n, mc), (final BigDecimal n) -> BigDecimalMath.log10(n, mc));
  }

  @Test
  public void testLog2Double() {
    test(0, double.class, SafeMath::log2, (final Double n) -> Math.log(n) / Math.log(2));
    test(1, double.class, SafeMath::log2, (final Double n) -> Math.log(n) / Math.log(2));
    test(-1, double.class, SafeMath::log2, (final Double n) -> Math.log(n) / Math.log(2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::log2, (final Double n) -> Math.log(n) / Math.log(2));
  }

  @Test
  public void testLog2BigInteger() {
    test(1, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log2(n, mc), (final BigInteger n) -> BigDecimalMath.log2(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.log2(n, mc), (final BigInteger n) -> BigDecimalMath.log2(new BigDecimal(n), mc));
  }

  @Test
  public void testLog2BigDecimal() {
    test(1, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log2(n, mc), (final BigDecimal n) -> BigDecimalMath.log2(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d10()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.log2(n, mc), (final BigDecimal n) -> BigDecimalMath.log2(n, mc));
  }

  @Test
  public void testMaxByteByte() {
    test(0, 0, byte.class, byte.class, byte.class, SafeMath::max, (n1, n2) -> (byte)Math.max(n1, n2));
    test(1, 1, byte.class, byte.class, byte.class, SafeMath::max, (n1, n2) -> (byte)Math.max(n1, n2));
    test(-1, -1, byte.class, byte.class, byte.class, SafeMath::max, (n1, n2) -> (byte)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, byte.class, byte.class, SafeMath::max, (n1, n2) -> (byte)Math.max(n1, n2));
  }

  @Test
  public void testMaxByteShort() {
    test(0, 0, byte.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(1, 1, byte.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(-1, -1, byte.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
  }

  @Test
  public void testMaxByteInt() {
    test(0, 0, byte.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(1, 1, byte.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(-1, -1, byte.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
  }

  @Test
  public void testMaxByteLong() {
    test(0, 0, byte.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, byte.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, byte.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxByteFloat() {
    test(0, 0, byte.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, byte.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, byte.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxByteDouble() {
    test(0, 0, byte.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, byte.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, byte.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxShortByte() {
    test(0, 0, short.class, byte.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(1, 1, short.class, byte.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(-1, -1, short.class, byte.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, byte.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
  }

  @Test
  public void testMaxShortShort() {
    test(0, 0, short.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(1, 1, short.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    test(-1, -1, short.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, short.class, short.class, SafeMath::max, (n1, n2) -> (short)Math.max(n1, n2));
  }

  @Test
  public void testMaxShortInt() {
    test(0, 0, short.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(1, 1, short.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(-1, -1, short.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
  }

  @Test
  public void testMaxShortLong() {
    test(0, 0, short.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, short.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, short.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxShortFloat() {
    test(0, 0, short.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, short.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, short.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxShortDouble() {
    test(0, 0, short.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, short.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, short.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxIntByte() {
    test(0, 0, int.class, byte.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(1, 1, int.class, byte.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(-1, -1, int.class, byte.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, byte.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
  }

  @Test
  public void testMaxIntShort() {
    test(0, 0, int.class, short.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(1, 1, int.class, short.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(-1, -1, int.class, short.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, short.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
  }

  @Test
  public void testMaxIntInt() {
    test(0, 0, int.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(1, 1, int.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    test(-1, -1, int.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, int.class, int.class, SafeMath::max, (n1, n2) -> (int)Math.max(n1, n2));
  }

  @Test
  public void testMaxIntLong() {
    test(0, 0, int.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, int.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, int.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxIntFloat() {
    test(0, 0, int.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, int.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, int.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxIntDouble() {
    test(0, 0, int.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, int.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, int.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxLongByte() {
    test(0, 0, long.class, byte.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, long.class, byte.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, long.class, byte.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, byte.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxLongShort() {
    test(0, 0, long.class, short.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, long.class, short.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, long.class, short.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, short.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxLongInt() {
    test(0, 0, long.class, int.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, long.class, int.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, long.class, int.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, int.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxLongLong() {
    test(0, 0, long.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(1, 1, long.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    test(-1, -1, long.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, long.class, long.class, SafeMath::max, (n1, n2) -> (long)Math.max(n1, n2));
  }

  @Test
  public void testMaxLongFloat() {
    test(0, 0, long.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, long.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, long.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxLongDouble() {
    test(0, 0, long.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, long.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, long.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatByte() {
    test(0, 0, float.class, byte.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, byte.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, byte.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, byte.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatShort() {
    test(0, 0, float.class, short.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, short.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, short.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, short.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatInt() {
    test(0, 0, float.class, int.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, int.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, int.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, int.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatLong() {
    test(0, 0, float.class, long.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, long.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, long.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, long.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatFloat() {
    test(0, 0, float.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, float.class, float.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxFloatDouble() {
    test(0, 0, float.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, float.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, float.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleByte() {
    test(0, 0, double.class, byte.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, byte.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, byte.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, byte.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleShort() {
    test(0, 0, double.class, short.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, short.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, short.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, short.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleInt() {
    test(0, 0, double.class, int.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, int.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, int.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, int.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleLong() {
    test(0, 0, double.class, long.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, long.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, long.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, long.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleFloat() {
    test(0, 0, double.class, float.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, float.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, float.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, float.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxDoubleDouble() {
    test(0, 0, double.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(1, 1, double.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    test(-1, -1, double.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, double.class, double.class, SafeMath::max, (n1, n2) -> Math.max(n1, n2));
  }

  @Test
  public void testMaxBigInteger() {
    try {
      SafeMath.max(null, BigInteger.ZERO);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    try {
      SafeMath.max(BigInteger.ZERO, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    assertTrue(SafeMath.max(BigInteger.ZERO, BigInteger.ZERO).compareTo(BigInteger.ZERO) == 0);
    assertTrue(SafeMath.max(BigInteger.ONE, BigInteger.ONE).compareTo(BigInteger.ONE) == 0);
    assertTrue(SafeMath.max(BigInteger.ONE.negate(), BigInteger.ONE.negate()).compareTo(BigInteger.ONE.negate()) == 0);
    for (int i = 0; i < numTests; ++i) { // [N]
      final long n1 = random.nextLong();
      final long n2 = random.nextLong();
      assertTrue(SafeMath.max(BigInteger.valueOf(n1), BigInteger.valueOf(n2)).compareTo(BigInteger.valueOf(Math.max(n1, n2))) == 0);
    }
  }

  @Test
  public void testMaxBigDecimal() {
    try {
      SafeMath.max(null, BigDecimal.ZERO);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    try {
      SafeMath.max(BigDecimal.ZERO, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    assertTrue(SafeMath.max(BigDecimal.ZERO, BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0);
    assertTrue(SafeMath.max(BigDecimal.ONE, BigDecimal.ONE).compareTo(BigDecimal.ONE) == 0);
    assertTrue(SafeMath.max(BigDecimal.ONE.negate(), BigDecimal.ONE.negate()).compareTo(BigDecimal.ONE.negate()) == 0);
    for (int i = 0; i < numTests; ++i) { // [N]
      final double n1 = random.nextDouble();
      final double n2 = random.nextDouble();
      assertTrue(SafeMath.max(BigDecimal.valueOf(n1), BigDecimal.valueOf(n2)).compareTo(BigDecimal.valueOf(Math.max(n1, n2))) == 0);
    }
  }

  @Test
  public void testMinByteByte() {
    test(0, 0, byte.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, byte.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, byte.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinByteShort() {
    test(0, 0, byte.class, short.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, byte.class, short.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, byte.class, short.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, short.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinByteInt() {
    test(0, 0, byte.class, int.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, byte.class, int.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, byte.class, int.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, int.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinByteLong() {
    test(0, 0, byte.class, long.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, byte.class, long.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, byte.class, long.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, long.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinByteFloat() {
    test(0, 0, byte.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, byte.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, byte.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinByteDouble() {
    test(0, 0, byte.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, byte.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, byte.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, byte.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinShortByte() {
    test(0, 0, short.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, short.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, short.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinShortShort() {
    test(0, 0, short.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(1, 1, short.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(-1, -1, short.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
  }

  @Test
  public void testMinShortInt() {
    test(0, 0, short.class, int.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(1, 1, short.class, int.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(-1, -1, short.class, int.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, int.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
  }

  @Test
  public void testMinShortLong() {
    test(0, 0, short.class, long.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(1, 1, short.class, long.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(-1, -1, short.class, long.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, long.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
  }

  @Test
  public void testMinShortFloat() {
    test(0, 0, short.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, short.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, short.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinShortDouble() {
    test(0, 0, short.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, short.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, short.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, short.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinIntByte() {
    test(0, 0, int.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, int.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, int.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinIntShort() {
    test(0, 0, int.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(1, 1, int.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(-1, -1, int.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
  }

  @Test
  public void testMinIntInt() {
    test(0, 0, int.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(1, 1, int.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(-1, -1, int.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
  }

  @Test
  public void testMinIntLong() {
    test(0, 0, int.class, long.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(1, 1, int.class, long.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(-1, -1, int.class, long.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, long.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
  }

  @Test
  public void testMinIntFloat() {
    test(0, 0, int.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, int.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, int.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinIntDouble() {
    test(0, 0, int.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, int.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, int.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, int.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinLongByte() {
    test(0, 0, long.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(1, 1, long.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    test(-1, -1, long.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, byte.class, byte.class, SafeMath::min, (n1, n2) -> (byte)Math.min(n1, n2));
  }

  @Test
  public void testMinLongShort() {
    test(0, 0, long.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(1, 1, long.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    test(-1, -1, long.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, short.class, short.class, SafeMath::min, (n1, n2) -> (short)Math.min(n1, n2));
  }

  @Test
  public void testMinLongInt() {
    test(0, 0, long.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(1, 1, long.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    test(-1, -1, long.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, int.class, int.class, SafeMath::min, (n1, n2) -> (int)Math.min(n1, n2));
  }

  @Test
  public void testMinLongLong() {
    test(0, 0, long.class, long.class, long.class, SafeMath::min, (n1, n2) -> (long)Math.min(n1, n2));
    test(1, 1, long.class, long.class, long.class, SafeMath::min, (n1, n2) -> (long)Math.min(n1, n2));
    test(-1, -1, long.class, long.class, long.class, SafeMath::min, (n1, n2) -> (long)Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, long.class, long.class, SafeMath::min, (n1, n2) -> (long)Math.min(n1, n2));
  }

  @Test
  public void testMinLongFloat() {
    test(0, 0, long.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, long.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, long.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinLongDouble() {
    test(0, 0, long.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, long.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, long.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, long.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatByte() {
    test(0, 0, float.class, byte.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, byte.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, byte.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, byte.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatShort() {
    test(0, 0, float.class, short.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, short.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, short.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, short.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatInt() {
    test(0, 0, float.class, int.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, int.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, int.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, int.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatLong() {
    test(0, 0, float.class, long.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, long.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, long.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, long.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatFloat() {
    test(0, 0, float.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, float.class, float.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinFloatDouble() {
    test(0, 0, float.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, float.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, float.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (double i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, float.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleByte() {
    test(0, 0, double.class, byte.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, byte.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, byte.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, byte.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleShort() {
    test(0, 0, double.class, short.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, short.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, short.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, short.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleInt() {
    test(0, 0, double.class, int.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, int.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, int.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, int.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleLong() {
    test(0, 0, double.class, long.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, long.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, long.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (long i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, long.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleFloat() {
    test(0, 0, double.class, float.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, float.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, float.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (float i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, float.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinDoubleDouble() {
    test(0, 0, double.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(1, 1, double.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    test(-1, -1, double.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, d0() * 10, double.class, double.class, double.class, SafeMath::min, (n1, n2) -> Math.min(n1, n2));
  }

  @Test
  public void testMinBigInteger() {
    try {
      SafeMath.min(null, BigInteger.ZERO);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    try {
      SafeMath.min(BigInteger.ZERO, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    assertTrue(SafeMath.min(BigInteger.ZERO, BigInteger.ZERO).compareTo(BigInteger.ZERO) == 0);
    assertTrue(SafeMath.min(BigInteger.ONE, BigInteger.ONE).compareTo(BigInteger.ONE) == 0);
    assertTrue(SafeMath.min(BigInteger.ONE.negate(), BigInteger.ONE.negate()).compareTo(BigInteger.ONE.negate()) == 0);
    for (int i = 0; i < numTests; ++i) { // [N]
      final long n1 = random.nextLong();
      final long n2 = random.nextLong();
      assertTrue(SafeMath.min(BigInteger.valueOf(n1), BigInteger.valueOf(n2)).compareTo(BigInteger.valueOf(Math.min(n1, n2))) == 0);
    }
  }

  @Test
  public void testMinBigDecimal() {
    try {
      SafeMath.min(null, BigDecimal.ZERO);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    try {
      SafeMath.min(BigDecimal.ZERO, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
    assertTrue(SafeMath.min(BigDecimal.ZERO, BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0);
    assertTrue(SafeMath.min(BigDecimal.ONE, BigDecimal.ONE).compareTo(BigDecimal.ONE) == 0);
    assertTrue(SafeMath.min(BigDecimal.ONE.negate(), BigDecimal.ONE.negate()).compareTo(BigDecimal.ONE.negate()) == 0);
    for (int i = 0; i < numTests; ++i) { // [N]
      final double n1 = random.nextDouble();
      final double n2 = random.nextDouble();
      assertTrue(SafeMath.min(BigDecimal.valueOf(n1), BigDecimal.valueOf(n2)).compareTo(BigDecimal.valueOf(Math.min(n1, n2))) == 0);
    }
  }

  @Test
  public void testRoundByte() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, byte.class, (final Byte n) -> SafeMath.round(n, s), (final Byte n) -> n);
      test(1, byte.class, (final Byte n) -> SafeMath.round(n, s), (final Byte n) -> n);
      test(-1, byte.class, (final Byte n) -> SafeMath.round(n, s), (final Byte n) -> n);
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, byte.class, (final Byte n) -> SafeMath.round(n, s), (final Byte n) -> n);
    }
  }

  @Test
  public void testRoundShort() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, short.class, (final Short n) -> SafeMath.round(n, s), (final Short n) -> n);
      test(1, short.class, (final Short n) -> SafeMath.round(n, s), (final Short n) -> n);
      test(-1, short.class, (final Short n) -> SafeMath.round(n, s), (final Short n) -> n);
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, short.class, (final Short n) -> SafeMath.round(n, s), (final Short n) -> n);
    }
  }

  @Test
  public void testRoundInt() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, int.class, (final Integer n) -> SafeMath.round(n, s), (final Integer n) -> n);
      test(1, int.class, (final Integer n) -> SafeMath.round(n, s), (final Integer n) -> n);
      test(-1, int.class, (final Integer n) -> SafeMath.round(n, s), (final Integer n) -> n);
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, int.class, (final Integer n) -> SafeMath.round(n, s), (final Integer n) -> n);
    }
  }

  @Test
  public void testRoundLong() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, long.class, (final Long n) -> SafeMath.round(n, s), (final Long n) -> n);
      test(1, long.class, (final Long n) -> SafeMath.round(n, s), (final Long n) -> n);
      test(-1, long.class, (final Long n) -> SafeMath.round(n, s), (final Long n) -> n);
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, long.class, (final Long n) -> SafeMath.round(n, s), (final Long n) -> n);
    }
  }

  @Test
  public void testRoundFloat() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, float.class, (final Float n) -> SafeMath.round(n, s), (final Float n) -> SafeMath.round(n, s));
      test(1, float.class, (final Float n) -> SafeMath.round(n, s), (final Float n) -> SafeMath.round(n, s));
      test(-1, float.class, (final Float n) -> SafeMath.round(n, s), (final Float n) -> SafeMath.round(n, s));
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, float.class, (final Float n) -> SafeMath.round(n, s), (final Float n) -> SafeMath.round(n, s));
    }
  }

  private static void testRoundFloatScale(final RoundingMode rm) {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, float.class, (final Float n) -> SafeMath.round(n, s, rm), (final Float n) -> SafeMath.round(n, s, rm));
      test(1, float.class, (final Float n) -> SafeMath.round(n, s, rm), (final Float n) -> SafeMath.round(n, s, rm));
      test(-1, float.class, (final Float n) -> SafeMath.round(n, s, rm), (final Float n) -> SafeMath.round(n, s, rm));
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, float.class, (final Float n) -> SafeMath.round(n, s, rm), (final Float n) -> SafeMath.round(n, s, rm));
    }
  }

  @Test
  public void testRoundFloatScaleDown() {
    testRoundFloatScale(RoundingMode.DOWN);
  }

  @Test
  public void testRoundFloatScaleUp() {
    testRoundFloatScale(RoundingMode.UP);
  }

  @Test
  public void testRoundFloatScaleFloor() {
    testRoundFloatScale(RoundingMode.FLOOR);
  }

  @Test
  public void testRoundFloatScaleCeiling() {
    testRoundFloatScale(RoundingMode.CEILING);
  }

  @Test
  public void testRoundFloatScaleHalfUp() {
    testRoundFloatScale(RoundingMode.HALF_UP);
  }

  @Test
  public void testRoundFloatScaleHalfDown() {
    testRoundFloatScale(RoundingMode.HALF_DOWN);
  }

  @Test
  public void testRoundFloatScaleHalfEven() {
    testRoundFloatScale(RoundingMode.HALF_EVEN);
  }

  @Test
  public void testRoundFloatScaleUnnecessary() {
    testRoundFloatScale(RoundingMode.UNNECESSARY);
  }

  @Test
  public void testRoundDouble() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, double.class, (final Double n) -> SafeMath.round(n, s), (final Double n) -> SafeMath.round(n, s));
      test(1, double.class, (final Double n) -> SafeMath.round(n, s), (final Double n) -> SafeMath.round(n, s));
      test(-1, double.class, (final Double n) -> SafeMath.round(n, s), (final Double n) -> SafeMath.round(n, s));
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, double.class, (final Double n) -> SafeMath.round(n, s), (final Double n) -> SafeMath.round(n, s));
    }
  }

  private static void testRoundDoubleScale(final RoundingMode rm) {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, double.class, (final Double n) -> SafeMath.round(n, s, rm), (final Double n) -> SafeMath.round(n, s, rm));
      test(1, double.class, (final Double n) -> SafeMath.round(n, s, rm), (final Double n) -> SafeMath.round(n, s, rm));
      test(-1, double.class, (final Double n) -> SafeMath.round(n, s, rm), (final Double n) -> SafeMath.round(n, s, rm));
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, double.class, (final Double n) -> SafeMath.round(n, s, rm), (final Double n) -> SafeMath.round(n, s, rm));
    }
  }

  @Test
  public void testRoundDoubleScaleDown() {
    testRoundDoubleScale(RoundingMode.DOWN);
  }

  @Test
  public void testRoundDoubleScaleUp() {
    testRoundDoubleScale(RoundingMode.UP);
  }

  @Test
  public void testRoundDoubleScaleFloor() {
    testRoundDoubleScale(RoundingMode.FLOOR);
  }

  @Test
  public void testRoundDoubleScaleCeiling() {
    testRoundDoubleScale(RoundingMode.CEILING);
  }

  @Test
  public void testRoundDoubleScaleHalfUp() {
    testRoundDoubleScale(RoundingMode.HALF_UP);
  }

  @Test
  public void testRoundDoubleScaleHalfDown() {
    testRoundDoubleScale(RoundingMode.HALF_DOWN);
  }

  @Test
  public void testRoundDoubleScaleHalfEven() {
    testRoundDoubleScale(RoundingMode.HALF_EVEN);
  }

  @Test
  public void testRoundDoubleScaleUnnecessary() {
    testRoundDoubleScale(RoundingMode.UNNECESSARY);
  }

  @Test
  public void testRoundBigInteger() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, BigInteger.class, (final BigInteger n) -> SafeMath.round(n, s), (final BigInteger n) -> n);
      test(1, BigInteger.class, (final BigInteger n) -> SafeMath.round(n, s), (final BigInteger n) -> n);
      test(-1, BigInteger.class, (final BigInteger n) -> SafeMath.round(n, s), (final BigInteger n) -> n);
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, BigInteger.class, (final BigInteger n) -> SafeMath.round(n, s), (final BigInteger n) -> n);
    }
  }

  @Test
  public void testRoundBigDecimal() {
    for (int j = 0; j < 3; ++j) { // [N]
      final int s = j;
      test(0, BigDecimal.class, (final BigDecimal n) -> SafeMath.round(n, s), (final BigDecimal n) -> BigDecimals.setScale(n, s, RoundingMode.HALF_UP));
      test(1, BigDecimal.class, (final BigDecimal n) -> SafeMath.round(n, s), (final BigDecimal n) -> BigDecimals.setScale(n, s, RoundingMode.HALF_UP));
      test(-1, BigDecimal.class, (final BigDecimal n) -> SafeMath.round(n, s), (final BigDecimal n) -> BigDecimals.setScale(n, s, RoundingMode.HALF_UP));
      for (int i = 0; i < numTests; ++i) // [N]
        test(d0() * 10, BigDecimal.class, (final BigDecimal n) -> SafeMath.round(n, s), (final BigDecimal n) -> BigDecimals.setScale(n, s, RoundingMode.HALF_UP));
    }
  }

  @Test
  public void testSignumByte() {
    test(0, byte.class, SafeMath::signum, (final Byte n) -> (byte)Math.signum(n));
    test(1, byte.class, SafeMath::signum, (final Byte n) -> (byte)Math.signum(n));
    test(-1, byte.class, SafeMath::signum, (final Byte n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, byte.class, SafeMath::signum, (final Byte n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumShort() {
    test(0, short.class, byte.class, SafeMath::signum, (final Short n) -> (byte)Math.signum(n));
    test(1, short.class, byte.class, SafeMath::signum, (final Short n) -> (byte)Math.signum(n));
    test(-1, short.class, byte.class, SafeMath::signum, (final Short n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, short.class, byte.class, SafeMath::signum, (final Short n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumInt() {
    test(0, int.class, byte.class, SafeMath::signum, (final Integer n) -> (byte)Math.signum(n));
    test(1, int.class, byte.class, SafeMath::signum, (final Integer n) -> (byte)Math.signum(n));
    test(-1, int.class, byte.class, SafeMath::signum, (final Integer n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, int.class, byte.class, SafeMath::signum, (final Integer n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumLong() {
    test(0, long.class, byte.class, SafeMath::signum, (final Long n) -> (byte)Math.signum(n));
    test(1, long.class, byte.class, SafeMath::signum, (final Long n) -> (byte)Math.signum(n));
    test(-1, long.class, byte.class, SafeMath::signum, (final Long n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, long.class, byte.class, SafeMath::signum, (final Long n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumFloat() {
    test(0, float.class, byte.class, SafeMath::signum, (final Float n) -> (byte)Math.signum(n));
    test(1, float.class, byte.class, SafeMath::signum, (final Float n) -> (byte)Math.signum(n));
    test(-1, float.class, byte.class, SafeMath::signum, (final Float n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, float.class, byte.class, SafeMath::signum, (final Float n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumDouble() {
    test(0, double.class, byte.class, SafeMath::signum, (final Double n) -> (byte)Math.signum(n));
    test(1, double.class, byte.class, SafeMath::signum, (final Double n) -> (byte)Math.signum(n));
    test(-1, double.class, byte.class, SafeMath::signum, (final Double n) -> (byte)Math.signum(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, double.class, byte.class, SafeMath::signum, (final Double n) -> (byte)Math.signum(n));
  }

  @Test
  public void testSignumBigInteger() {
    test(0, BigInteger.class, byte.class, SafeMath::signum, (final BigInteger n) -> (byte)n.signum());
    test(1, BigInteger.class, byte.class, SafeMath::signum, (final BigInteger n) -> (byte)n.signum());
    test(-1, BigInteger.class, byte.class, SafeMath::signum, (final BigInteger n) -> (byte)n.signum());
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigInteger.class, byte.class, SafeMath::signum, (final BigInteger n) -> (byte)n.signum());
  }

  @Test
  public void testSignumBigDecimal() {
    test(0, BigDecimal.class, byte.class, SafeMath::signum, (final BigDecimal n) -> (byte)n.signum());
    test(1, BigDecimal.class, byte.class, SafeMath::signum, (final BigDecimal n) -> (byte)n.signum());
    test(-1, BigDecimal.class, byte.class, SafeMath::signum, (final BigDecimal n) -> (byte)n.signum());
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0() * 10, BigDecimal.class, byte.class, SafeMath::signum, (final BigDecimal n) -> (byte)n.signum());
  }

  @Test
  public void testSinDouble() {
    test(0, double.class, SafeMath::sin, (final Double n) -> Math.sin(n));
    test(1, double.class, SafeMath::sin, (final Double n) -> Math.sin(n));
    test(-1, double.class, SafeMath::sin, (final Double n) -> Math.sin(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::sin, (final Double n) -> Math.sin(n));
  }

  @Test
  public void testSinBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.sin(n, mc), (final BigInteger n) -> BigDecimalMath.sin(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.sin(n, mc), (final BigInteger n) -> BigDecimalMath.sin(new BigDecimal(n), mc));
  }

  @Test
  public void testSinBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.sin(n, mc), (final BigDecimal n) -> BigDecimalMath.sin(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.sin(n, mc), (final BigDecimal n) -> BigDecimalMath.sin(n, mc));
  }

  @Test
  public void testSqrtDouble() {
    test(0, double.class, SafeMath::sqrt, (final Double n) -> Math.sqrt(n));
    test(1, double.class, SafeMath::sqrt, (final Double n) -> Math.sqrt(n));
    test(-1, double.class, SafeMath::sqrt, (final Double n) -> Math.sqrt(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::sqrt, (final Double n) -> Math.sqrt(n));
  }

  @Test
  public void testSqrtBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.sqrt(n, mc), (final BigInteger n) -> BigDecimalMath.sqrt(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.sqrt(n, mc), (final BigInteger n) -> BigDecimalMath.sqrt(new BigDecimal(n), mc));
  }

  @Test
  public void testSqrtBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.sqrt(n, mc), (final BigDecimal n) -> BigDecimalMath.sqrt(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.sqrt(n, mc), (final BigDecimal n) -> BigDecimalMath.sqrt(n, mc));
  }

  @Test
  public void testToDegreesDouble() {
    test(0, double.class, SafeMath::toDegrees, (final Double n) -> Math.toDegrees(n));
    test(1, double.class, SafeMath::toDegrees, (final Double n) -> Math.toDegrees(n));
    test(-1, double.class, SafeMath::toDegrees, (final Double n) -> Math.toDegrees(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::toDegrees, (final Double n) -> Math.toDegrees(n));
  }

  @Test
  public void testToDegreesBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.toDegrees(n, mc), (final BigInteger n) -> new BigDecimal(Math.toDegrees(n.doubleValue()), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.toDegrees(n, mc), (final BigInteger n) -> new BigDecimal(Math.toDegrees(n.doubleValue()), mc));
  }

  @Test
  public void testToDegreesBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.toDegrees(n, mc), (final BigDecimal n) -> new BigDecimal(Math.toDegrees(n.doubleValue()), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.toDegrees(n, mc).setScale(9, RoundingMode.DOWN), (final BigDecimal n) -> new BigDecimal(Math.toDegrees(n.doubleValue()), mc).setScale(9, RoundingMode.DOWN));
  }

  @Test
  public void testToRadiansDouble() {
    test(0, double.class, SafeMath::toRadians, (final Double n) -> Math.toRadians(n));
    test(1, double.class, SafeMath::toRadians, (final Double n) -> Math.toRadians(n));
    test(-1, double.class, SafeMath::toRadians, (final Double n) -> Math.toRadians(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::toRadians, (final Double n) -> Math.toRadians(n));
  }

  @Test
  public void testToRadiansBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.toRadians(n, mc), (final BigInteger n) -> new BigDecimal(Math.toRadians(n.doubleValue()), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.toRadians(n, mc), (final BigInteger n) -> new BigDecimal(Math.toRadians(n.doubleValue()), mc));
  }

  @Test
  public void testToRadiansBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.toRadians(n, mc), (final BigDecimal n) -> new BigDecimal(Math.toRadians(n.doubleValue()), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(Math.abs(d0()), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.toRadians(n, mc).setScale(11, RoundingMode.DOWN), (final BigDecimal n) -> new BigDecimal(Math.toRadians(n.doubleValue()), mc).setScale(11, RoundingMode.DOWN));
  }

  @Test
  public void testTanDouble() {
    test(0, double.class, SafeMath::tan, (final Double n) -> Math.tan(n));
    test(1, double.class, SafeMath::tan, (final Double n) -> Math.tan(n));
    test(-1, double.class, SafeMath::tan, (final Double n) -> Math.tan(n));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), double.class, SafeMath::tan, (final Double n) -> Math.tan(n));
  }

  @Test
  public void testTanBigInteger() {
    test(0, BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.tan(n, mc), (final BigInteger n) -> BigDecimalMath.tan(new BigDecimal(n), mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigInteger.class, BigDecimal.class, (final BigInteger n) -> SafeMath.tan(n, mc), (final BigInteger n) -> BigDecimalMath.tan(new BigDecimal(n), mc));
  }

  @Test
  public void testTanBigDecimal() {
    test(0, BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.tan(n, mc), (final BigDecimal n) -> BigDecimalMath.tan(n, mc));
    for (int i = 0; i < numTests; ++i) // [N]
      test(d0(), BigDecimal.class, BigDecimal.class, (final BigDecimal n) -> SafeMath.tan(n, mc), (final BigDecimal n) -> BigDecimalMath.tan(n, mc));
  }

  private static void testRoundFloat(final RoundingMode rm) {
    for (int i = 0; i < numTests * 100; ++i) { // [N]
      float v = i < 100 ? (i + 0.5f) * (random.nextBoolean() ? -1 : 1) : random.nextFloat();
      float r1 = 0, r2 = 0;
      final float delta = Math.ulp(v);
      int j = 0;
      do {
        if (j > 0) {
          if (TestAide.isInDebug(true))
            // TODO: Place breakpoint here to debug...
            System.console();
          else
            assertEquals(r1, r2, Math.ulp(v));
        }

        try {
          BigDecimal bd = new BigDecimal(v);
          bd = bd.setScale(0, rm);
          r2 = bd.floatValue();
        }
        catch (final ArithmeticException e) {
          r2 = Float.NaN;
        }

        r1 = SafeMath.round(v, rm);
      }
      while ((Float.isNaN(r1) ? !Float.isNaN(r2) : Float.isNaN(r2) || Math.abs(r1 - r2) > delta) && ++j < 100);
    }
  }

  @Test
  public void testRoundFloatDown() {
    testRoundFloat(RoundingMode.DOWN);
  }

  @Test
  public void testRoundFloatUp() {
    testRoundFloat(RoundingMode.UP);
  }

  @Test
  public void testRoundFloatFloor() {
    testRoundFloat(RoundingMode.FLOOR);
  }

  @Test
  public void testRoundFloatCeiling() {
    testRoundFloat(RoundingMode.CEILING);
  }

  @Test
  public void testRoundFloatHalfUp() {
    testRoundFloat(RoundingMode.HALF_UP);
  }

  @Test
  public void testRoundFloatHalfDown() {
    testRoundFloat(RoundingMode.HALF_DOWN);
  }

  @Test
  public void testRoundFloatHalfEven() {
    testRoundFloat(RoundingMode.HALF_EVEN);
  }

  @Test
  public void testRoundFloatUnnecessary() {
    testRoundFloat(RoundingMode.UNNECESSARY);
  }

  private static void testRoundDouble(final RoundingMode rm) {
    for (int i = 0; i < numTests * 100; ++i) { // [N]
      double v = i < 100 ? (i + 0.5d) * (random.nextBoolean() ? -1 : 1) : random.nextFloat();
      double r1 = 0, r2 = 0;
      final double delta = Math.ulp(v);
      int j = 0;
      do {
        if (j > 0) {
          if (TestAide.isInDebug(true))
            // TODO: Place breakpoint here to debug...
            System.err.println(r1 + " != " + r2);
          else
            assertEquals(r1, r2, Math.ulp(v));
        }

        try {
          BigDecimal bd = new BigDecimal(v);
          bd = bd.setScale(0, rm);
          r2 = bd.doubleValue();
        }
        catch (final ArithmeticException e) {
          r2 = Double.NaN;
        }

        r1 = SafeMath.round(v, rm);
      }
      while ((Double.isNaN(r1) ? !Double.isNaN(r2) : Double.isNaN(r2) || Math.abs(r1 - r2) > delta) && ++j < 100);
    }
  }

  @Test
  public void testRoundDoubleDown() {
    testRoundDouble(RoundingMode.DOWN);
  }

  @Test
  public void testRoundDoubleUp() {
    testRoundDouble(RoundingMode.UP);
  }

  @Test
  public void testRoundDoubleFloor() {
    testRoundDouble(RoundingMode.FLOOR);
  }

  @Test
  public void testRoundDoubleCeiling() {
    testRoundDouble(RoundingMode.CEILING);
  }

  @Test
  public void testRoundDoubleHalfUp() {
    testRoundDouble(RoundingMode.HALF_UP);
  }

  @Test
  public void testRoundDoubleHalfDown() {
    testRoundDouble(RoundingMode.HALF_DOWN);
  }

  @Test
  public void testRoundDoubleHalfEven() {
    testRoundDouble(RoundingMode.HALF_EVEN);
  }

  @Test
  public void testRoundDoubleUnnecessary() {
    testRoundDouble(RoundingMode.UNNECESSARY);
  }

  @Test
  public void testGcdByte() {
    try {
      SafeMath.gcd((byte)-1, (byte)0);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      SafeMath.gcd((byte)0, (byte)-1);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000000; ++i) { // [N]
      final byte a = (byte)Math.abs(random.nextInt(Byte.MAX_VALUE + 1));
      final byte b = (byte)Math.abs(random.nextInt(Byte.MAX_VALUE + 1));
      final BigInteger gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b));
      assertEquals(gcd.byteValue(), SafeMath.gcd(a, b));
    }
  }

  @Test
  public void testGcdShort() {
    try {
      SafeMath.gcd((short)-1, (short)0);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      SafeMath.gcd((short)0, (short)-1);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000000; ++i) { // [N]
      final short a = (short)Math.abs(random.nextInt(Short.MAX_VALUE + 1));
      final short b = (short)Math.abs(random.nextInt(Short.MAX_VALUE + 1));
      final BigInteger gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b));
      assertEquals(gcd.shortValue(), SafeMath.gcd(a, b));
    }
  }

  @Test
  public void testGcdInt() {
    try {
      SafeMath.gcd(-1, 0);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      SafeMath.gcd(0, -1);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000000; ++i) { // [N]
      final int a = Math.abs(random.nextInt());
      final int b = Math.abs(random.nextInt());
      final BigInteger gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b));
      assertEquals(gcd.intValue(), SafeMath.gcd(a, b));
    }
  }

  @Test
  public void testGcdLong() {
    try {
      SafeMath.gcd(-1L, 0L);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      SafeMath.gcd(0L, -1L);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    for (int i = 0; i < 1000000; ++i) { // [N]
      final long a = Math.abs(random.nextLong());
      final long b = Math.abs(random.nextLong());
      final BigInteger gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b));
      assertEquals(gcd.longValue(), SafeMath.gcd(a, b));
    }
  }
}