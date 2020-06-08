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

package org.libj.math;

import static org.libj.math.LongDecimal.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import org.libj.test.TestAide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LongDecimalTest {
  static final BigDecimal DEFAULT = new BigDecimal("0.0");
  static final Logger logger = LoggerFactory.getLogger(LongDecimalTest.class);
  static byte testBits = -1;
  static long testLD1 = -1;
  static long testLD2 = -1;

  static {
//    testBits = 9;
//    testLD1 = 18014398509481984L;
//    testLD2 = 18014398509481984L;
  }

  static final boolean debug = false;
  static final Random random = new Random();
  static final int numTests = 100000;
  static final byte maxBits = 16;
  static final MathContext precision16 = MathContext.DECIMAL64;
  static final NumberFormat expectedFormatter = new DecimalFormat("0E0");
  static final NumberFormat epsilonFormatter = new DecimalFormat("0E0");
  static final long[] pow2 = new long[64];
  static final BigInteger[] minValue = new BigInteger[maxScaleBits];
  static final BigInteger[] maxValue = new BigInteger[maxScaleBits];

  static {
    for (byte i = 0; i < minValue.length; ++i) {
      minValue[i] = BigInteger.valueOf(LongDecimal.minValue(i));
      maxValue[i] = BigInteger.valueOf(LongDecimal.maxValue(i));
    }

    expectedFormatter.setRoundingMode(RoundingMode.FLOOR);
    expectedFormatter.setMinimumFractionDigits(18);
    epsilonFormatter.setRoundingMode(RoundingMode.CEILING);
    epsilonFormatter.setMinimumFractionDigits(0);
    for (int i = 0; i < pow2.length; ++i)
      pow2[i] = (long)Math.pow(2, i);
  }

  private static final String ENCODE_START = "\033[";
  private static final String ENCODE_END = "m";
  private static final String RESET = "0;39";

  static String color(final String str, final boolean failure) {
    final int color = failure ? 1 : 2;
    final int strength = failure ? 1 : 0;
    final StringBuilder builder = new StringBuilder();
    builder.append(ENCODE_START).append(strength);
    builder.append(';').append(3).append(color);
    return builder.append(ENCODE_END).append(str).append(ENCODE_START).append(RESET).append(ENCODE_END).toString();
  }

  static BigDecimal D(final String str) {
    return new BigDecimal(str);
  }

  static long randomValue(final byte maxValuePower) {
    long maxValue = pow2[maxValuePower];
    return (long)((Math.random() < 0.5 ? -1 : 1) * random.nextDouble() * maxValue);
  }

  private static final long boundedMin = -100000000;
  private static final long boundedMax = 100000000;
  private static final short boundedScale = 8;
  private static final byte boundedBits = 9;
  private static final long[] preliminary = {0, 1, -1, 10, -10, 100, -100, Long.MAX_VALUE, Long.MIN_VALUE};

  static void test(final Operation<?,?> operation) {
    final long[] time = new long[2];
    final boolean[] failures = new boolean[16];
    final BigDecimal[] errors = new BigDecimal[16];
    int count = test(operation, true, time, errors, failures);
    count += test(operation, false, time, errors, failures);
    operation.print(count, time, errors, failures);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static int test(final Operation<?,?> operation, final boolean bounded, final long[] time, final BigDecimal[] errors, final boolean[] failures) {
    int count = 0;
    for (int i = 0; i < numTests; ++i) {
      for (byte b = 0; b < maxBits; ++b) {
        final long defaultValue = random.nextLong();
        final long ld1;
        final long ld2;
        if (testLD1 != -1) {
          ld1 = testLD1;
          ld2 = testLD2;
        }
        else if (bounded) {
          ld1 = operation.randomBounded(boundedMin, boundedMax, boundedScale, boundedBits);
          ld2 = operation.randomBounded(boundedMin, boundedMax, boundedScale, boundedBits);
        }
        else if (i < preliminary.length) {
          long v1 = preliminary[i];
          long v2 = preliminary[i];
          final short s1, s2;
          if (v1 == Long.MAX_VALUE || v1 == Long.MIN_VALUE) {
            s1 = 1;
            s2 = 1;
          }
          else {
            s1 = (short)(Math.random() * 5);
            v1 *= FastMath.e10[s1];
            s2 = (short)(Math.random() * 5);
            v2 *= FastMath.e10[s2];
          }

          ld1 = operation.randomBounded(v1, v1, s1, boundedBits);
          ld2 = operation.randomBounded(v2, v2, s2, boundedBits);
        }
        else {
          ld1 = operation.randomEncoded(b);
          ld2 = operation.randomEncoded(b);
        }

        final BigDecimal bd1 = toBidDecimal(ld1, b);
        final BigDecimal bd2 = toBidDecimal(ld2, b);
        Object actual = defaultValue;
        Object expected = defaultValue;
        BigDecimal error = BigDecimal.ZERO;
        try {
          actual = operation.test(ld1, ld2, bd1, bd2, b, defaultValue, time);
          expected = operation.control(bd1, bd2, time);
          error = ((Operation)operation).run(bd1, bd2, expected, actual, testBits != -1 ? testBits : b, defaultValue, errors, failures);
        }
        catch (final Exception e) {
          e.printStackTrace();
        }

        if (error == DEFAULT)
          continue;

        ++count;
        if (TestAide.isInDebug() && error != null) {
          for (int j = 0; j < 100; ++j) {
            logger.info(color("----------------------------------------\nd1: " + ld1 + " (" + decodeScale(ld1, b) + ") d2: " + ld2 + " (" + decodeScale(ld2, b) + ") bits: " + b, true));
            logger.info(color("Expected: " + bd1 + " " + operation.operator + " " + bd2 + " = " + expected + "\n  Actual: " + bd1 + " " + operation.operator + " " + bd2 + " = " + (!(actual instanceof Long) ? actual : ((Long)actual).longValue() == defaultValue ? "err" : LongDecimal.toString((Long)actual, b)) + "\n Error: " + error, true));
            actual = operation.test(ld1, ld2, bd1, bd2, b, defaultValue, time);
            ((Operation)operation).run(bd1, bd2, expected, actual, testBits != -1 ? testBits : b, defaultValue, errors, failures);
          }
        }
      }
    }

    return count;
  }
}