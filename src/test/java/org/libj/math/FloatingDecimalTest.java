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

import java.math.RoundingMode;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.libj.test.TestAide;

public class FloatingDecimalTest extends DecimalNative {
  private static final Random r = new Random();
  private static final int tests = 1000000;

  private static final float[] SINGLE_SMALL_10_POW = {
    1.0e0f,
    1.0e1f, 1.0e2f, 1.0e3f, 1.0e4f, 1.0e5f,
    1.0e6f, 1.0e7f, 1.0e8f, 1.0e9f, 1.0e10f
  };

  private static final float randomFloat() {
    if (r.nextBoolean())
      return Math.scalb(r.nextBoolean() ? r.nextInt() : r.nextFloat(), Double.MIN_EXPONENT + (int)(r.nextDouble() * (Float.MAX_EXPONENT - Float.MIN_EXPONENT)));

    if (r.nextBoolean())
      return SINGLE_SMALL_10_POW[(int)(SINGLE_SMALL_10_POW.length * r.nextDouble())];

    if (r.nextBoolean())
      return Float.NaN;

    if (r.nextBoolean())
      return Float.MAX_VALUE;

    if (r.nextBoolean())
      return Float.MAX_VALUE;

    if (r.nextBoolean())
      return Float.MIN_VALUE;

    if (r.nextBoolean())
      return Float.MIN_NORMAL;

    return r.nextBoolean() ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
  }

  private static final double[] SMALL_10_POW = {
    1.0e0,
    1.0e1, 1.0e2, 1.0e3, 1.0e4, 1.0e5,
    1.0e6, 1.0e7, 1.0e8, 1.0e9, 1.0e10,
    1.0e11, 1.0e12, 1.0e13, 1.0e14, 1.0e15,
    1.0e16, 1.0e17, 1.0e18, 1.0e19, 1.0e20,
    1.0e21, 1.0e22
  };

  private static final double randomDouble() {
    if (r.nextBoolean())
      return Math.scalb(r.nextBoolean() ? r.nextInt() : r.nextDouble(), Double.MIN_EXPONENT + (int)(r.nextDouble() * (Double.MAX_EXPONENT - Double.MIN_EXPONENT)));

    if (r.nextBoolean())
      return SMALL_10_POW[(int)(SMALL_10_POW.length * r.nextDouble())];

    if (r.nextBoolean())
      return Double.NaN;

    if (r.nextBoolean())
      return Double.MAX_VALUE;

    if (r.nextBoolean())
      return Double.MAX_VALUE;

    if (r.nextBoolean())
      return Double.MIN_VALUE;

    if (r.nextBoolean())
      return Double.MIN_NORMAL;

    return r.nextBoolean() ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
  }

  private float defaultFloat = Float.NaN;

  @Test
  public void testFloat() {
    final Decimal decimal = new Decimal();
    String expectedStr = null;
    String actualStr = null;
    boolean error = !Float.isNaN(defaultFloat);
    float x = defaultFloat;
    for (int i = 0; i < tests; ++i) { // [N]
      try {
        if (!error)
          x = randomFloat();

        expectedStr = String.valueOf(x);
        actualStr = FloatingDecimal.toDecimal(x, decimal).toScientificString();
        final float expected = Float.parseFloat(expectedStr);
        final float actual = Float.parseFloat(actualStr);
        assertEquals(expected, actual, Math.ulp(x));
        assertEquals(String.valueOf(expected == 0 ? 0 : expected), String.valueOf(actual));
      }
      catch (final Throwable e) {
        System.out.println("expected: " + expectedStr);
        System.out.println("  actual: " + actualStr);
        if (!TestAide.isInDebug(true))
          throw e;

        error = true;
        e.printStackTrace();
      }
    }
  }

  private double defaultDouble = Double.NaN;

  @Test
  public void testDouble() {
    final Decimal decimal = new Decimal();
    Decimal expectedDecimal;
    Decimal actualDecimal;
    String expectedStr = null;
    String actualStr = null;
    boolean error = !Double.isNaN(defaultDouble);
    double x = defaultDouble;
    for (int i = 0; i < tests; ++i) { // [N]
      try {
        if (!error)
          x = randomDouble();

        expectedDecimal = Decimal.valueOf(String.valueOf(x), RoundingMode.DOWN);
        actualDecimal = FloatingDecimal.toDecimal(x, RoundingMode.DOWN, decimal);
        if (actualDecimal == null || actualDecimal.isError()) {
          assertNull(expectedDecimal);
          continue;
        }

        assertNotNull(expectedDecimal);

        expectedStr = expectedDecimal.toScientificString();
        actualStr = actualDecimal.toScientificString();

        final double expected = Double.parseDouble(expectedStr);
        final double actual = Double.parseDouble(actualStr);

        assertEquals(expected, actual, Math.ulp(x));
        assertEquals(String.valueOf(expected == 0 ? 0 : expected), String.valueOf(actual));
      }
      catch (final Throwable e) {
        System.out.println("expected: " + expectedStr);
        System.out.println("  actual: " + actualStr);
        if (!TestAide.isInDebug(true))
          throw e;

        error = true;
        e.printStackTrace();
      }
    }
  }

  @Test
  @Ignore
  public void testNative() {
    String expectedStr = null;
    String actualStr = null;
    boolean error = !Double.isNaN(defaultDouble);
    double x = defaultDouble;
    for (int i = 0; i < tests; ++i) { // [N]
      try {
        if (!error)
          x = randomDouble();

        expectedStr = String.valueOf(x);
        actualStr = DecimalNative.nativeD2A(x);
        assertEquals(expectedStr, actualStr);
      }
      catch (final Throwable e) {
        System.out.println("expected: " + expectedStr);
        System.out.println("  actual: " + actualStr);
        if (!TestAide.isInDebug(true))
          throw e;

        error = true;
        e.printStackTrace();
      }
    }
  }
}