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

import static ch.obermuhlner.math.big.BigDecimalMath.acos;
import static org.libj.math.DecimalMath.acos;
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

@Ignore
@RunWith(AuditRunner.class)
@AuditRunner.Execution(UNINSTRUMENTED)
@AuditRunner.Instrument(a = {BigDecimal.class, BigInteger.class}, b = int[].class)
@AuditRunner.Instrument(a = {Decimal.class, BigInt.class}, b = int[].class)
public class DecimalAcosTest extends DecimalTest {
  // FIXME: Scale is limited here to 2, as a stop-gap.
  private void test(final AuditReport report, final RoundingMode rm) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Acos of `T` (RoundingMode." + rm + ").");
    setRoundingMode(rm);
    final MathContext mc = new MathContext(34, rm);
    final long defaultValue = random.nextLong();
    test("acos(" + rm + ")").withEpsilon(null)
      .withAuditReport(report)
      .withCases(
        d(BigDecimal.class, (final long a) -> toBigDecimal(lim(a, 1, 0)), (final BigDecimal a) -> acos(a, mc), (final BigDecimal o) -> o),
        d(Decimal.class, (final long a) -> toDecimal(lim(a, 1, 0)), (final Decimal a) -> acos(a, rm), (final Decimal o) -> o),
        d(long.class, (final long a) -> lim(a, 1, 0), (final long a) -> acos(a, rm, defaultValue), (final long o) -> o == defaultValue ? null : o));
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
  public void testHalfDown(final AuditReport report) {
    test(report, RoundingMode.HALF_DOWN);
  }

  @Test
  public void testHalfUp(final AuditReport report) {
    test(report, RoundingMode.HALF_UP);
  }

  @Test
  public void testHalfEven(final AuditReport report) {
    test(report, RoundingMode.HALF_EVEN);
  }

  @Test
  public void testUnnecessary(final AuditReport report) {
    test(report, RoundingMode.UNNECESSARY);
  }
}