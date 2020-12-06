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
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalValueOfFloatTest extends DecimalTest {
  @Test
  public void test(final AuditReport report) {
    final MathContext mc = new MathContext(18);
    final long defaultValue = random.nextLong();
    test("valueOf(float)").withAuditReport(report).withCases(
      d(BigDecimal.class, this::toDecimalFloat, (float a) -> new BigDecimal(String.valueOf(a), mc), o -> o),
      d(Decimal.class, this::toDecimalFloat, (float a) -> Decimal.valueOf(a), o -> o),
      d(long.class, this::toDecimalFloat, (float a) -> Decimal.valueOf(a, defaultValue), (long o) -> o == defaultValue ? null : o)
    );
  }
}