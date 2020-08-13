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

import static org.libj.math.Decimal.*;

import java.math.BigDecimal;

import org.junit.Test;

public class DecimalDivisionTest extends DecimalOperationTest {
  private static final BigDecimal[] epsilon = {
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("8E-13"),
    D("7E-13"),
    D("2E-13"),
    D("4E-14"),
    D("8E-15"),
    D("1E-14"),
    D("3E-15"),
    D("5E-15"),
    D("9E-15"),
    D("2E-14"),
    D("3E-14"),
    D("5E-14")
  };

  private static final DecimalArithmeticOperation div = new DecimalMultiplicativeOperation("div", long.class, "%s / %s") {
    @Override
    final BigDecimal epsilon(final byte bits) {
      return epsilon[bits];
    }

    @Override
    Long test(final long d1, final long d2, final BigDecimal bd1, final BigDecimal bd2, final byte scaleBits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = div(d1, d2, scaleBits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      if (bd2.signum() == 0)
        return null;

      final long ts = System.nanoTime();
      final BigDecimal result = bd1.divide(bd2, precision16);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  @Test
  public void testDiv() {
    test(div);
  }
}