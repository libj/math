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

import static org.junit.Assert.*;
import static org.libj.math.survey.AuditMode.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;
import org.libj.math.survey.CaseTest;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a={BigDecimal.class, BigInteger.class}, b=int[].class)
@AuditRunner.Instrument(a={Decimal.class, BigInt.class}, b=int[].class)
public class DecimalPredicateITest extends DecimalTest {
  @Test
  public void testEquals(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Equate `T` with `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("eq(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.equals(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.equals(b), o -> o),
        d(long.class, (long a, long b) -> Decimal.eq(a, b, scaleBits), o -> o)
      );
    }
  }

  @Test
  public void testSetScale(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Set scale of `T`.");

    // Special test for Long.MIN_VALUE
    System.out.println("[Long.MIN_VALUE]____________________________________________________________________________________");
    final int iterations = 10000;
    int progress = 0;
    for (int i = 0; i < iterations; ++i) {
      progress = progress(progress, i, i, iterations);

      final short newScale = CaseTest.DecimalCase.randomScale((byte)(i % Decimal.MAX_SCALE_BITS));
      final Decimal decimal = new Decimals.Decimal(Long.MIN_VALUE, CaseTest.DecimalCase.randomScale((byte)(i % Decimal.MAX_SCALE_BITS)));
      final BigDecimal bigDecimal = decimal.toBigDecimal();
      String bigDecimalString = null;
      String decimalString = null;
      int j = 0;
      do {
        if (j > 0) {
          System.err.println(bigDecimalString + " != " + decimalString);
          decimal.clear();
        }

        try {
          final BigDecimal res = bigDecimal.setScale(newScale, RoundingMode.HALF_UP).stripTrailingZeros();
          bigDecimalString = res.signum() == 0 ? "0" : CaseTest.DecimalCase.format.format(res).replaceAll("\\.?0+(E[-+0-9]*)$", "$1").replaceAll("E0$", "");
        }
        catch (final ArithmeticException e) {
          bigDecimalString = null;
        }

        decimalString = decimal.setScale(newScale).toScientificString();
      }
      while (++j < 100 && (bigDecimalString == null ? decimalString != null : !bigDecimalString.equals(decimalString)));
      assertEquals(bigDecimalString, decimal.setScale(newScale).toScientificString());
    }

    System.out.println();

    final long defaultValue = random.nextLong();
    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("setScale(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, b -> (byte)b, (BigDecimal a, long b) -> a.setScale((byte)b, RoundingMode.HALF_UP), o -> o),
        d(Decimal.class, this::toDecimal, b -> (byte)b, (Decimal a, long b) -> a.setScale((byte)b), o -> o),
        d(long.class, a -> a, b -> (byte)b, (long a, long b) -> Decimal.setScale(a, (byte)b, defaultValue, scaleBits), o -> o == defaultValue ? null : o)
      );
    }
  }

  @Test
  public void testCompareTo(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Compare `T` to `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("compare(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.compareTo(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.compareTo(b), o -> o),
        d(long.class, (long a, long b) -> Decimal.compare(a, b, scaleBits), (long o) -> o)
      );
    }
  }

  @Test
  public void testLt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` < `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("lt(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.compareTo(b) < 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.compareTo(b) < 0, o -> o),
        d(long.class, (long a, long b) -> Decimal.lt(a, b, scaleBits), o -> o)
      );
    }
  }

  @Test
  public void testGt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` > `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("gt(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.compareTo(b) > 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.compareTo(b) > 0, o -> o),
        d(long.class, (long a, long b) -> Decimal.gt(a, b, scaleBits), o -> o)
      );
    }
  }

  @Test
  public void testLte(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` <= `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("lte(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.compareTo(b) <= 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.compareTo(b) <= 0, o -> o),
        d(long.class, (long a, long b) -> Decimal.lte(a, b, scaleBits), o -> o)
      );
    }
  }

  @Test
  public void testGte(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` >= `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("gte(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.compareTo(b) >= 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.compareTo(b) >= 0, o -> o),
        d(long.class, (long a, long b) -> Decimal.gte(a, b, scaleBits), o -> o)
      );
    }
  }

  @Test
  public void testMax(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Maximum of `T` and `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("max(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.max(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.max(b), o -> o),
        d(long.class, (long a, long b) -> Decimal.max(a, b, scaleBits), (long o) -> o)
      );
    }
  }

  @Test
  public void testMin(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Minimum of `T` and `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("min(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (BigDecimal a, BigDecimal b) -> a.min(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (Decimal a, Decimal b) -> a.min(b), o -> o),
        d(long.class, (long a, long b) -> Decimal.min(a, b, scaleBits), (long o) -> o)
      );
    }
  }

  @Test
  public void testPrecision(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Precision of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("precision(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.precision(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.precision(), String::valueOf),
        d(long.class, (long a) -> Decimal.precision(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testHashCode(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Hash code of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("hashCode(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.hashCode(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.hashCode(), String::valueOf),
        d(long.class, (long a) -> Decimal.hashCode(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testScale(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Scale of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("scale(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.scale(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.scale(), String::valueOf),
        d(long.class, (long a) -> Decimal.scale(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testSignum(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Signum of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("signum(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.signum(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.signum(), String::valueOf),
        d(long.class, (long a) -> Decimal.signum(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testByteValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`byte` valye of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("byteValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.byteValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.byteValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.byteValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testShortValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`short` valye of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("shortValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.shortValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.shortValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.shortValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testIntValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`int` valye of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("intValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.intValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.intValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.intValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testLongValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`long` valye of `T`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("longValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, (long a) -> toBigDecimal(a).longValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.longValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.longValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testFloatValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`float` valye of `T`.");

    // Special test for Long.MIN_VALUE
    System.out.println("[Long.MIN_VALUE]____________________________________________________________________________________");
    final int iterations = 100000;
    int progress = 0;
    for (int i = 0; i < iterations; ++i) {
      final Decimal minDecimal = new Decimals.Decimal(Long.MIN_VALUE, CaseTest.DecimalCase.randomScale((byte)(i % Decimal.MAX_SCALE_BITS)));
      final BigDecimal minBigDecimal = minDecimal.toBigDecimal();
      assertEquals(minBigDecimal.floatValue(), minDecimal.floatValue(), 0);
      progress = progress(progress, i, i, iterations);
    }

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("floatValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.floatValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.floatValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.floatValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testDoubleValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`double` valye of `T`.");

    // Special test for Long.MIN_VALUE
    System.out.println("[Long.MIN_VALUE]____________________________________________________________________________________");
    final int iterations = 100000;
    int progress = 0;
    for (int i = 0; i < iterations; ++i) {
      final Decimal minDecimal = new Decimals.Decimal(Long.MIN_VALUE, CaseTest.DecimalCase.randomScale((byte)(i % Decimal.MAX_SCALE_BITS)));
      final BigDecimal minBigDecimal = minDecimal.toBigDecimal();
      assertEquals(minBigDecimal.doubleValue(), minDecimal.doubleValue(), 0);
      progress = progress(progress, i, i, iterations);
    }

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("doubleValue(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.doubleValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.doubleValue(), String::valueOf),
        d(long.class, (long a) -> Decimal.doubleValue(a, scaleBits), String::valueOf)
      );
    }
  }

  @Test
  public void testToBigInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` to `BigInt` or `BigInteger`.");

    for (byte i = Decimal.MIN_SCALE_BITS; i <= MAX_SCALE_BITS; ++i) {
      final byte scaleBits = i;
      test("toBigInt(" + scaleBits + ")").withSkip(skip(scaleBits)).withAuditReport(report).withCases(scaleBits,
        d(BigDecimal.class, this::toBigDecimal, (BigDecimal a) -> a.toBigInteger(), String::valueOf),
        d(Decimal.class, this::toDecimal, (Decimal a) -> a.toBigInt(), BigInt::toString),
        d(long.class, (long a) -> Decimal.toBigInt(a, scaleBits), BigInt::toString)
      );
    }
  }
}