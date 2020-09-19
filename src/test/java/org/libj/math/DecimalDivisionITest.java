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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.Decimals.Decimal;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalDivisionITest extends DecimalTest {
  @Test
  public void testDiv(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide `T` by `T`.");

    final long defaultValue = random.nextLong();
    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("div(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a, long b) -> a.divide(toBigDecimal(nz(b)), MathContext.DECIMAL128), o -> o),
        d(Decimal.class, this::toDecimal, (Decimal a, long b) -> a.div(toDecimal(nz(b))), o -> o),
        d(long.class, a -> a, b -> nz(b), (long a, long b) -> Decimal.div(a, b, defaultValue, scaleBits), o -> o == defaultValue ? null : o)
      );
    }
  }

  @Test
  public void testRem(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder of `T` divided by `T`.");

    final long defaultValue = random.nextLong();
    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("rem(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a, long b) -> a.remainder(toBigDecimal(nz(b)), MathContext.DECIMAL128), o -> o),
        d(Decimal.class, this::toDecimal, (Decimal a, long b) -> a.rem(toDecimal(nz(b))), o -> o),
        d(long.class, a -> a, b -> nz(b), (long a, long b) -> Decimal.rem(a, b, defaultValue, scaleBits), o -> o == defaultValue ? null : o)
      );
    }
  }
}