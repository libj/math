/* Copyright (c) 2020 LibJ
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

import static org.libj.math.LongDecimal.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;
import org.libj.lang.Numbers;

public class LongDecimalArithmeticTest extends LongDecimalTest {
  private static final BigDecimal[] epsilonAdd = {
    D("5E-16"),
    D("5E-16"),
    D("6E-15"),
    D("5E-16"),
    D("2E-15"),
    D("5E-15"),
    D("2E-12"),
    D("2E-12"),
    D("7E-13"),
    D("1E-11"),
    D("2E-11"),
    D("5E-13"),
    D("2E-11"),
    D("6E-12"),
    D("7E-12"),
    D("4E-12")
  };

  private static final BigDecimal[] epsilonDiv = {
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("3E-16"),
    D("4E-11"),
    D("2E-10"),
    D("9E-10"),
    D("2E-10"),
    D("3E-10"),
    D("3E-10"),
    D("3E-10"),
    D("2E-10"),
    D("2E-10"),
    D("9E-11")
  };

  private static final BigDecimal[] epsilonMul = {
    D("5E-16"),
    D("5E-16"),
    D("5E-16"),
    D("5E-16"),
    D("1E-8"),
    D("5E-16"),
    D("6E-16"),
    D("5E-16"),
    D("5E-16"),
    D("6E-16"),
    D("2E-15"),
    D("3E-15"),
    D("5E-15"),
    D("9E-15"),
    D("2E-14"),
    D("4E-14")
  };

  private abstract static class AdditiveOperation extends ArithmeticOperation {
    private AdditiveOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    final BigDecimal epsilon(final byte bits) {
      return epsilonAdd[bits];
    }
  }

  private abstract static class MultiplicativeOperation extends ArithmeticOperation {
    private MultiplicativeOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    byte maxValuePower(final byte bits) {
      // For multiply and divide, the maxValue should not be so big as to cause the result to overrun max/min value
      return (byte)(bits == 6 ? 55 : bits == 5 ? 43 : bits == 4 ? 36 : bits == 3 ? 33 : bits == 2 ? 31 : 30);
    }

    @Override
    short randomScale(final byte bits) {
      final short scale = super.randomScale(bits);
      final short abs = SafeMath.abs(scale);
      return (short)(abs < 4 ? scale : scale / 2);
    }
  }

  private static final class MulOperation extends MultiplicativeOperation {
    private MulOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    BigDecimal epsilon(final byte bits) {
      return epsilonMul[bits];
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.multiply(bd2, precision16);
      time[1] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = mul(ld1, ld2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }
  }

  private abstract static class ArithmeticOperation extends Operation<Long,BigDecimal> {
    private ArithmeticOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    abstract BigDecimal epsilon(byte bits);

    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final BigDecimal expected, final Long result, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      if (expected == null)
        return result == defaultValue ? DEFAULT : BigDecimal.ONE;

      if (expected.signum() == 0)
        return compare(0, result, bits) == 0 ? DEFAULT : BigDecimals.TWO;

      final BigInteger minValue = LongDecimalArithmeticTest.minValue[bits];
      final BigInteger maxValue = LongDecimalArithmeticTest.maxValue[bits];
      BigInteger unscaled = expected.unscaledValue();
      int scale = expected.scale();
      int len;
      if (!lockScale()) {
        len = Numbers.digits(unscaled);
        if (len > 19) {
          final int negOffset = unscaled.signum() < 0 ? 1 : 0;
          unscaled = new BigInteger(unscaled.toString().substring(negOffset, 19 + negOffset));
          scale -= len - 19;
        }
        else {
          unscaled = unscaled.multiply(BigInteger.TEN.pow(20 - len));
          scale += 20 - len;
        }

        // Turn the unscaled value to be highest precision available for the LongDecimal of the provided bits
        while (unscaled.signum() < 0 ? unscaled.compareTo(minValue) < 0 : unscaled.compareTo(maxValue) > 0) {
          unscaled = unscaled.divide(BigInteger.TEN);
          --scale;
        }
      }

      final boolean expectDefaultValue;
      if (unscaled.signum() < 0 ? unscaled.compareTo(minValue) < 0 : unscaled.compareTo(maxValue) > 0)
        expectDefaultValue = true;
      else {
        final int minScale = LongDecimal.minScale[bits];
        final int maxScale = LongDecimal.maxScale[bits];
        if (minScale <= scale && scale <= maxScale) {
          expectDefaultValue = false;
        }
        else {
          len = Numbers.digits(unscaled);
          final int diffScale = scale - (scale < 0 ? minScale : maxScale);
          expectDefaultValue = diffScale < 0 || len - diffScale <= 0;
        }
      }

      if (expectDefaultValue)
        return result == defaultValue ? DEFAULT : BigDecimal.TEN;

      if (result == defaultValue)
        return BigDecimal.TEN;

      final short s = decodeScale(result, bits);
      final BigDecimal expectedScaled = expected.setScale(s, RoundingMode.FLOOR);

      final BigDecimal actual = toBigDecimal(result, bits);
      final BigDecimal error = expectedScaled.subtract(actual).abs().divide(expected, precision16);
      errors[bits] = errors[bits] == null ? error : errors[bits].max(error);

      final boolean pass = error.signum() == 0 || error.compareTo(epsilon(bits)) <= 0;
      failures[bits] |= !pass;
      return pass ? null : error;
    }
  }

  private static final class DivOperation extends MultiplicativeOperation {
    private DivOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    final BigDecimal epsilon(final byte bits) {
      return epsilonDiv[bits];
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

    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = div(ld1, ld2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }
  }

  private static abstract class FunctionOperation extends ArithmeticOperation {
    private FunctionOperation(final String label, final Class<?> arg) {
      super(label, arg, "~");
    }

    @Override
    final BigDecimal epsilon(final byte bits) {
      return BigDecimal.ZERO;
    }
  }

  private static final ArithmeticOperation neg = new FunctionOperation("neg", long.class) {
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

  private static final ArithmeticOperation encodeBigDecimal = new FunctionOperation("encode", BigDecimal.class) {
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
      final BigDecimal result = new BigDecimal(str);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final ArithmeticOperation encodeString = new FunctionOperation("encode", String.class) {
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

  private static final ArithmeticOperation setScale = new FunctionOperation("setScale", int.class) {
    @Override
    boolean lockScale() {
      return true;
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
      final BigDecimal result = bd1.setScale(bd2.scale(), RoundingMode.FLOOR);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final ArithmeticOperation add = new AdditiveOperation("add", long.class, "+") {
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

  private static final ArithmeticOperation sub = new AdditiveOperation("sub", long.class, "-") {
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

  private static final ArithmeticOperation mul = new MulOperation("mul", long.class, "*");
  private static final ArithmeticOperation div = new DivOperation("div", long.class, "/");

  @Test
  public void testEncodeBigDecimal() {
    test(encodeBigDecimal);
  }

  @Test
  public void testEncodeString() {
    test(encodeString);
  }

  @Test
  public void testNeg() {
    test(neg);
  }

  @Test
  public void testSetScale() {
    test(setScale);
  }

  @Test
  public void testAdd() {
    test(add);
  }

  @Test
  public void testSub() {
    test(sub);
  }

  @Test
  public void testMul() {
    test(mul);
  }

  @Test
  public void testDiv() {
    test(div);
  }
}