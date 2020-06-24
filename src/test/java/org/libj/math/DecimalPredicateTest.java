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

import static org.junit.Assert.*;
import static org.libj.math.Decimal.*;

import java.math.BigDecimal;

import org.junit.Test;

public class DecimalPredicateTest extends DecimalTest {
  private static final DecimalOperation<Short,Short> precision = new DecimalOperation<Short,Short>("precision", long.class, "precision(%s)") {
    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final Short expected, final Short actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected.compareTo(actual) == 0 ? null : BigDecimal.ONE;
    }

    @Override
    Short test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final short result = Decimal.precision(ld1, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Short control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final short result = (short)bd1.precision();
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalOperation<BigDecimal,BigDecimal> toBigDecimal = new DecimalOperation<BigDecimal,BigDecimal>("toBigDecimal", long.class, "toBigDecimal(%s)") {
    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final BigDecimal expected, final BigDecimal actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected.compareTo(actual) == 0 ? null : BigDecimal.ONE;
    }

    @Override
    BigDecimal test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = Decimal.toBigDecimal(ld1, bits);
      time[0] += System.nanoTime() - ts;
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

  private static final DecimalOperation<Double,Double> doubleValue = new DecimalOperation<Double,Double>("doubleValue", long.class, "doubleValue(%s)") {
    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final Double expected, final Double actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected.compareTo(actual) == 0 ? null : BigDecimal.ONE;
    }

    @Override
    Double test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final double result = Decimal.doubleValue(ld1, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Double control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final double result = bd1.doubleValue();
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalOperation<Integer,Integer> compare = new DecimalOperation<Integer,Integer>("compare", long.class, "compare(%s, %s)") {
    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final Integer expected, final Integer actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected == actual ? null : BigDecimal.ONE;
    }

    @Override
    Integer test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final int result = compare(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Integer control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final int result = bd1.compareTo(bd2);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private abstract static class PredicateOperation extends DecimalOperation<Long,BigDecimal> {
    private PredicateOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final BigDecimal expected, final Long actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected.compareTo(toBigDecimal(actual, bits)) == 0 ? null : BigDecimal.ONE;
    }
  }

  private static final PredicateOperation min = new PredicateOperation("min", long.class, "<min>") {
    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final long result = min(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.min(bd2);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final PredicateOperation max = new PredicateOperation("max", long.class, "<max>") {
    @Override
    Long test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final long result = max(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    BigDecimal control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final BigDecimal result = bd1.max(bd2);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private abstract static class InequalityOperation extends DecimalOperation<Boolean,Boolean> {
    private InequalityOperation(final String label, final Class<?> arg, final String operator) {
      super(label, arg, operator);
    }

    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final Boolean expected, final Boolean actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return expected == actual ? null : BigDecimal.ONE;
    }
  }

  private static final InequalityOperation eq = new InequalityOperation("eq", long.class, "%s = %s") {
    @Override
    Boolean test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = eq(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Boolean control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = bd1.equals(bd2);
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final InequalityOperation lt = new InequalityOperation("lt", long.class, "%s < %s") {
    @Override
    Boolean test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = lt(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Boolean control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = bd1.compareTo(bd2) < 0;
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final InequalityOperation lte = new InequalityOperation("lte", long.class, "%s <= %s") {
    @Override
    Boolean test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = lte(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Boolean control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = bd1.compareTo(bd2) <= 0;
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final InequalityOperation gt = new InequalityOperation("gt", long.class, "%s > %s") {
    @Override
    Boolean test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = gt(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Boolean control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = bd1.compareTo(bd2) > 0;
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final InequalityOperation gte = new InequalityOperation("gte", long.class, "%s >= %s") {
    @Override
    Boolean test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = gte(ld1, ld2, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    Boolean control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final boolean result = bd1.compareTo(bd2) >= 0;
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  private static final DecimalOperation<String,String> toString = new DecimalOperation<String,String>("toString", long.class, "toString(%s)") {
    @Override
    BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final String expected, final String actual, final byte bits, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
      return new BigDecimal(expected).compareTo(new BigDecimal(actual)) == 0 ? null : BigDecimal.ONE;
    }

    @Override
    String test(final long ld1, final long ld2, final BigDecimal bd1, final BigDecimal bd2, final byte bits, final long defaultValue, final long[] time) {
      final long ts = System.nanoTime();
      final String result = Decimal.toString(ld1, bits);
      time[0] += System.nanoTime() - ts;
      return result;
    }

    @Override
    String control(final BigDecimal bd1, final BigDecimal bd2, final long[] time) {
      final long ts = System.nanoTime();
      final String result = bd1.toString();
      time[1] += System.nanoTime() - ts;
      return result;
    }
  };

  @Test
  public void testMin() {
    test(min);
  }

  @Test
  public void testMax() {
    test(max);
  }

  @Test
  public void testEq() {
    test(eq);
  }

  @Test
  public void testCompare() {
    test(compare);
  }

  @Test
  public void testLt() {
    test(lt);
  }

  @Test
  public void testLte() {
    test(lte);
  }

  @Test
  public void testGt() {
    test(gt);
  }

  @Test
  public void testGte() {
    test(gte);
  }

  @Test
  public void testDoubleValue() {
    test(doubleValue);
  }

  @Test
  public void testPrecision() {
    test(precision);
  }

  @Test
  public void testToBigDecimal() {
    test(toBigDecimal);
  }

  @Test
  public void testToString() {
    final byte scaleBits = 7;
    assertEquals("10000000000E-43", Decimal.toString(3098476553630901248L, scaleBits));
    assertEquals("72057594037927935E-40", Decimal.toString(2954361355555045375L, scaleBits));
    test(toString);
  }
}