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

public class DecimalAdditionTest extends DecimalTest {
  private static final BigDecimal[] epsilon = {
    D("5E-16"),
    D("5E-16"),
    D("3E-14"),
    D("4E-15"),
    D("3E-15"),
    D("5E-15"),
    D("2E-12"),
    D("2E-12"),
    D("7E-13"),
    D("1E-11"),
    D("2E-11"),
    D("1E-12"),
    D("2E-11"),
    D("6E-12"),
    D("7E-12"),
    D("4E-12")
  };

  static abstract class AdditiveOperation extends DecimalArithmeticOperation {
    AdditiveOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    final BigDecimal epsilon(final byte bits) {
      return epsilon[bits];
    }
  }

  private static final DecimalArithmeticOperation add = new AdditiveOperation("add", long.class, "%s + %s") {
    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = add(ld1, ld2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.add(bd2, precision16);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalArithmeticOperation sub = new AdditiveOperation("sub", long.class, "%s - %s") {
    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = sub(ld1, ld2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.subtract(bd2, precision16);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  @Test
  public void testAdd() {
    test(add);
  }

  @Test
  public void testSub() {
    test(sub);
  }
}