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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = {BigDecimal.class, BigInteger.class}, b = int[].class)
@AuditRunner.Instrument(a = {Decimal.class, BigInt.class}, b = int[].class)
public class DecimalAdditionTest extends DecimalTest {
  @Test
  public void testAdd(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Add `T` to `T`.");

    final long defaultValue = random.nextLong();
    test("add").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a, final long b) -> a.add(toBigDecimal(b)), (final BigDecimal o) -> o),
        d(Decimal.class, this::toDecimal, (final Decimal a, final long b) -> a.add(toDecimal(b)), (final Decimal o) -> o),
        d(long.class, (final long a, final long b) -> Decimal.add(a, b, defaultValue), (final long o) -> o == defaultValue ? null : o));
  }

  @Test
  public void testSub(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtract `T` from `T`.");

    final long defaultValue = random.nextLong();
    test("sub").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a, final long b) -> a.subtract(toBigDecimal(b)), (final BigDecimal o) -> o),
        d(Decimal.class, this::toDecimal, (final Decimal a, final long b) -> a.sub(toDecimal(b)), (final Decimal o) -> o),
        d(long.class, (final long a, final long b) -> Decimal.sub(a, b, defaultValue), (final long o) -> o == defaultValue ? null : o));
  }
}