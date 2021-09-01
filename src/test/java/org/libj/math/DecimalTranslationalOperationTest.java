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
import java.math.RoundingMode;

import org.junit.Test;
import org.libj.lang.BigDecimals;

public class DecimalTranslationalOperationTest extends DecimalOperationTest {
  private static final DecimalArithmeticOperation abs = new DecimalTranslationalOperation("abs", long.class, "| %s |") {
    @Override
    boolean lockScale() {
      return true;
    }

    @Override
    Long test(final long d1, final long d2, final BigDecimal bd1, final BigDecimal bd2, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = abs(d1, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.abs();
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalArithmeticOperation neg = new DecimalTranslationalOperation("neg", long.class, "- %s") {
    @Override
    boolean lockScale() {
      return true;
    }

    @Override
    Long test(final long d1, final long d2, final BigDecimal bd1, final BigDecimal bd2, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = neg(d1, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.negate();
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalArithmeticOperation encodeBigDecimal = new DecimalTranslationalOperation("encode", BigDecimal.class, "encode(%s)") {
    @Override
    Long test(final long d1, final long d2, final BigDecimal bd1, final BigDecimal bd2, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = Decimal.valueOf(bd1, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final String str = bd1.toString();
      final long ts = System.nanoTime();
      final BigDecimal result = BigDecimals.intern(str);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalArithmeticOperation setScale = new DecimalTranslationalOperation("setScale", int.class, "setScale(%s, %s)") {
    @Override
    boolean lockScale() {
      return true;
    }

    @Override
    String format2(final long significand, final short scale) {
      return String.valueOf(scale);
    }

    @Override
    Long test(final long d1, final long d2, final BigDecimal bd1, final BigDecimal bd2, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = setScale(d1, scale(d2), RoundingMode.HALF_UP, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.setScale(bd2.scale(), RoundingMode.HALF_UP);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  @Test
  public void testEncodeBigDecimal() {
    test(encodeBigDecimal);
  }

  @Test
  public void testAbs() {
    test(abs);
  }

  @Test
  public void testNeg() {
    test(neg);
  }

  @Test
  public void testSetScale() {
    test(setScale);
  }
}