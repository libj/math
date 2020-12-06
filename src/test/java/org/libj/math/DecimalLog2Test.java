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

import static org.libj.math.DecimalMath.*;
import static org.libj.math.survey.AuditMode.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

import ch.obermuhlner.math.big.BigDecimalMath;

@Ignore
@RunWith(AuditRunner.class)
@AuditRunner.Execution(UNINSTRUMENTED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalLog2Test extends DecimalTest {
  private void test(final AuditReport report, final RoundingMode rm, final BigDecimal epsilon) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Log base 2 of `T` (RoundingMode." + rm + ").");
    setRoundingMode(rm);
    final MathContext mc = new MathContext(26, rm);
    final long defaultValue = random.nextLong();
    test("log2(" + rm + ")").withEpsilon(epsilon).withAuditReport(report).withCases(
      d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> BigDecimalMath.log2(a, mc), o -> o),
      d(Decimal.class, this::toDecimal, (Decimal a) -> log2(a, rm), o -> o),
      d(long.class, a -> a, (long a) -> log2(a, rm, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  @Test
  public void testFloor(final AuditReport report) {
    test(report, RoundingMode.FLOOR, new BigDecimal("1E-24"));
  }

  @Test
  public void testCeiling(final AuditReport report) {
    test(report, RoundingMode.CEILING, new BigDecimal("1E-24"));
  }

  @Test
  public void testDown(final AuditReport report) {
    test(report, RoundingMode.DOWN, new BigDecimal("1E-24"));
  }

  @Test
  public void testUp(final AuditReport report) {
    test(report, RoundingMode.UP, new BigDecimal("2E-25"));
  }

  @Test
  public void testHalfDown(final AuditReport report) {
    test(report, RoundingMode.HALF_DOWN, new BigDecimal("2E-24"));
  }

  @Test
  public void testHalfUp(final AuditReport report) {
    test(report, RoundingMode.HALF_UP, new BigDecimal("2E-25"));
  }

  @Test
  public void testHalfEven(final AuditReport report) {
    test(report, RoundingMode.HALF_EVEN, new BigDecimal("2E-25"));
  }

  @Test
  public void testHalfUnnecessary(final AuditReport report) {
    test(report, RoundingMode.UNNECESSARY, null);
  }
}