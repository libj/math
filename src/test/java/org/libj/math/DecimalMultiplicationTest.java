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

import java.math.BigDecimal;

import org.junit.Test;

public class DecimalMultiplicationTest extends DecimalTest {
  public void testMul(final byte scaleBits) {
    final long defaultValue = random.nextLong();
    test("mul(" + scaleBits + ")", scaleBits, skip(scaleBits), BigDecimal.ZERO, null,
      d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.multiply(b), o -> o),
      d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.mul(b), o -> o),
      d(long.class, (long a, long b) -> Decimal.mul(a, b, scaleBits, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  @Test
  public void testMul() {
    for (byte b = Decimal.MIN_SCALE_BITS; b <= Decimal.MAX_SCALE_BITS; ++b)
      testMul(b);
  }
}