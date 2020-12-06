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

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalDivisionTest extends DecimalTest {
  private void testDiv(final AuditReport report, final RoundingMode rm) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide `T` by `T`.");

    final long defaultValue = random.nextLong();
    test("div(" + rm + ")").withAuditReport(report).withCases(
      d(BigDecimal.class, this::toBigDecimal, (BigDecimal a, long b) -> a.divide(toBigDecimal(dnz(b)), MathContext.DECIMAL128), o -> o),
      d(Decimal.class, this::toDecimal, (Decimal a, long b) -> a.div(toDecimal(dnz(b)), rm), o -> o),
      d(long.class, a -> a, b -> dnz(b), (long a, long b) -> Decimal.div(a, b, rm, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivDown(final AuditReport report) {
    testDiv(report, RoundingMode.DOWN);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivUp(final AuditReport report) {
    testDiv(report, RoundingMode.UP);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivFloor(final AuditReport report) {
    testDiv(report, RoundingMode.FLOOR);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivCeiling(final AuditReport report) {
    testDiv(report, RoundingMode.CEILING);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivHalfDown(final AuditReport report) {
    testDiv(report, RoundingMode.HALF_DOWN);
  }

  @Test
  public void testDivHalfUp(final AuditReport report) {
    testDiv(report, RoundingMode.HALF_UP);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivHalfEven(final AuditReport report) {
    testDiv(report, RoundingMode.HALF_EVEN);
  }

  @Test
  @Ignore("Not supported yet")
  //FIXME: Support this RoundingMode
  public void testDivUnnecessary(final AuditReport report) {
    testDiv(report, RoundingMode.UNNECESSARY);
  }

  @Test
  public void testRem(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder of `T` divided by `T`.");

    final long defaultValue = random.nextLong();
    test("rem").withAuditReport(report).withCases(
      d(BigDecimal.class, this::toBigDecimal, (BigDecimal a, long b) -> a.remainder(toBigDecimal(dnz(b)), MathContext.DECIMAL128), o -> o),
      d(Decimal.class, this::toDecimal, (Decimal a, long b) -> a.rem(toDecimal(dnz(b))), o -> o),
      d(long.class, a -> a, b -> dnz(b), (long a, long b) -> Decimal.rem(a, b, defaultValue), o -> o == defaultValue ? null : o)
    );
  }
}