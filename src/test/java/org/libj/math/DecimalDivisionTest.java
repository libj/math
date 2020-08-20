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
import java.math.MathContext;

import org.junit.Test;

public class DecimalDivisionTest extends DecimalTest {
  public void testDiv(final byte scaleBits) {
    final long defaultValue = random.nextLong();
    test("div(" + scaleBits + ")", scaleBits, skip(scaleBits), BigDecimal.ZERO, null,
      d(BigDecimal.class, this::toBigDecimal, b -> toBigDecimal(nz(b)), (BigDecimal a, BigDecimal b) -> a.divide(b, MathContext.DECIMAL128), o -> o),
      d(Decimal.class, this::toDecimal, b -> toDecimal(nz(b)), (Decimal a, Decimal b) -> a.div(b), o -> o),
      d(long.class, a -> a, b -> nz(b), (long a, long b) -> Decimal.div(a, b, scaleBits, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  public void testRem(final byte scaleBits) {
    final long defaultValue = random.nextLong();
    test("rem(" + scaleBits + ")", scaleBits, skip(scaleBits), BigDecimal.ZERO, null,
      d(BigDecimal.class, this::toBigDecimal, b -> toBigDecimal(nz(b)), (BigDecimal a, BigDecimal b) -> a.remainder(b, MathContext.DECIMAL128), o -> o),
      d(Decimal.class, this::toDecimal, b -> toDecimal(nz(b)), (Decimal a, Decimal b) -> a.rem(b), o -> o),
      d(long.class, a -> a, b -> nz(b), (long a, long b) -> Decimal.rem(a, b, scaleBits, defaultValue), o -> o == defaultValue ? null : o)
    );
  }

  @Test
  public void testDiv() {
    for (byte b = Decimal.MIN_SCALE_BITS; b <= Decimal.MAX_SCALE_BITS; ++b)
      testDiv(b);
  }

  @Test
  public void testRem() {
    for (byte b = Decimal.MIN_SCALE_BITS; b <= Decimal.MAX_SCALE_BITS; ++b)
      testRem(b);
  }
}