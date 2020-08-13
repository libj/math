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

public class DecimalAdditionTest extends DecimalTest {
  private void testAdd(final byte scaleBits) {
    test("add(" + scaleBits + ")", scaleBits, BigDecimal.ZERO, null,
      d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.add(b), o -> o),
      d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.add(b), o -> o),
      d(long.class, (long a, long b) -> Decimal.add(a, b, scaleBits, random.nextLong()))
    );
  }

  public void testSub(final byte scaleBits) {
    test("sub(" + scaleBits + ")", scaleBits, new BigDecimal("1E-18"), null,
      d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.subtract(b), o -> o),
      d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.sub(b), o -> o),
      d(long.class, (long a, long b) -> Decimal.sub(a, b, scaleBits, random.nextLong()))
    );
  }

  @Test
  public void testAdd() {
    for (byte b = 3; b <= 3; ++b)
      testAdd(b);
  }

  @Test
  public void testSub() {
  }
}