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

public class DecimalTranslationTest extends DecimalTest {
  private static final DecimalArithmeticOperation abs = new DecimalTranslationalOperation("abs", long.class, "| %s |") {
    @Override
    boolean lockScale() {
      return true;
    }

    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = abs(ld1, bits, defaultValue);
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
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = neg(ld1, bits, defaultValue);
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
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = encode(bd1, bits, defaultValue);
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

  private static final DecimalArithmeticOperation encodeString = new DecimalTranslationalOperation("encode", String.class, "encode(%s)") {
    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = encode(bd1.toString(), bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final String str = bd1.toString();
      final long ts = System.nanoTime();
      final BigDecimal result = new BigDecimal(str);
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
    String format2(final long value, final short scale) {
      return String.valueOf(scale);
    }

    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = setScale(ld1, decodeScale(ld2, bits), bits, defaultValue);
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
  public void testEncodeString() {
    test(encodeString);
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