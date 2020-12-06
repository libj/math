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
import static org.libj.math.survey.AuditMode.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;
import org.libj.math.survey.CaseTest;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalSetScaleTest extends DecimalTest {
  public void test(final AuditReport report, final RoundingMode rm) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Set scale of `T` (RoundingMode." + rm + ").");

    // Special test for Decimal.MIN_VALUE
    System.out.println("[Decimal.MIN_VALUE]_________________________________________________________________________________");
    final int iterations = 10000;
    int progress = 0;
    String decimalString = null;
    for (int i = 0; i < iterations; ++i) {
      progress = progress(progress, i, i, iterations);

      final short newScale = CaseTest.DecimalCase.randomScale(Decimal.MIN_SIGNIFICAND);
      final Decimal decimal = new Decimal(Decimal.MIN_SIGNIFICAND, CaseTest.DecimalCase.randomScale(Decimal.MIN_SIGNIFICAND));
      final Decimal decimalOrig = decimal.clone();
      final BigDecimal bigDecimal = decimal.toBigDecimal();
      String bigDecimalString = null;
      int j = 0;
      do {
        if (j > 0) {
          System.err.println(bigDecimalString + " != " + decimalString);
          decimal.assign(decimalOrig);
        }

        try {
          final BigDecimal res = BigDecimals.setScale(bigDecimal, newScale, rm);
          bigDecimalString = res.signum() == 0 ? "0" : CaseTest.DecimalCase.format.format(res).replaceAll("\\.?0+(E[-+0-9]*)$", "$1").replaceAll("E0$", "");
        }
        catch (final ArithmeticException e) {
          bigDecimalString = null;
        }

        final Decimal result = decimal.setScale(newScale, rm);
        decimalString = result == null ? null : result.toScientificString();
      }
      while (++j < 100 && (bigDecimalString == null ? decimalString != null : !bigDecimalString.equals(decimalString)));
      assertEquals(bigDecimalString, decimalString);
    }

    System.out.println();

    final long defaultValue = random.nextLong();

    test("setScale").withAuditReport(report).withCases(
      d(BigDecimal.class, this::toBigDecimal, b -> (byte)b, (BigDecimal a, long b) -> BigDecimals.setScale(a, (byte)b, rm), o -> o),
      d(Decimal.class, this::toDecimal, b -> (byte)b, (Decimal a, long b) -> a.setScale((byte)b, rm), o -> o),
      d(long.class, a -> a, b -> (byte)b, (long a, long b) -> Decimal.setScale(a, (byte)b, rm, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  @Test
  public void testHalfUp(final AuditReport report) {
    test(report, RoundingMode.HALF_UP);
  }

  @Test
  public void testHalfDown(final AuditReport report) {
    test(report, RoundingMode.HALF_DOWN);
  }

  @Test
  public void testFloor(final AuditReport report) {
    test(report, RoundingMode.FLOOR);
  }

  @Test
  public void testCeiling(final AuditReport report) {
    test(report, RoundingMode.CEILING);
  }

  @Test
  public void testDown(final AuditReport report) {
    test(report, RoundingMode.DOWN);
  }

  @Test
  public void testUp(final AuditReport report) {
    test(report, RoundingMode.UP);
  }

  @Test
  public void testUnnecessary(final AuditReport report) {
    test(report, RoundingMode.UNNECESSARY);
  }
}