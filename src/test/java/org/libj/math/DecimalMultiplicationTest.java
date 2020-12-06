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
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalMultiplicationTest extends DecimalTest {
  @Test
  public void testMul(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Multiply `T` by `T`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "`BigDecimal` outperforms `Decimal` in runtime performance due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.");
    report.addComment(UNINSTRUMENTED.ordinal(), "`Decimal` outperforms `long` in runtime performance in lieu of the need to encode and decode the decimal value from a `long`-encoded number.");
    report.addComment(UNINSTRUMENTED.ordinal(), "`Decimal` outperform `BigDecimal` in heap allocation by requiring no more than the single `Decimal` instance and zero `int[]` instances for all operations.");
    report.addComment(UNINSTRUMENTED.ordinal(), "`long` outperform `Decimal` and `BigDecimal` in heap allocation by avoiding heap allocation entirely for all operations.");

    final long defaultValue = random.nextLong();
    test("mul").withAuditReport(report).withCases(
      d(BigDecimal.class, (long a, long b) -> toBigDecimal(a).multiply(toBigDecimal(b)), o -> o),
      d(Decimal.class, (long a, long b) -> toDecimal(a).mul(toDecimal(b)), o -> o),
      d(long.class, (long a, long b) -> Decimal.mul(a, b, defaultValue), (long o) -> o == defaultValue ? null : o)
    );
  }
}