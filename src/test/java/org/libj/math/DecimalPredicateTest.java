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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;
import org.libj.math.survey.CaseTest;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = {BigDecimal.class, BigInteger.class}, b = int[].class)
@AuditRunner.Instrument(a = {Decimal.class, BigInt.class}, b = int[].class)
public class DecimalPredicateTest extends DecimalTest {
  @Test
  public void testEquals(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Equate `T` with `T`.");

    test("eq").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.equals(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.equals(b), o -> o),
        d(long.class, (final long a, final long b) -> Decimal.eq(a, b), o -> o));
  }

  @Test
  public void testCompareTo(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Compare `T` to `T`.");

    test("compareTo").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.compareTo(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.compareTo(b), o -> o),
        d(long.class, (final long a, final long b) -> Decimal.compare(a, b), (final long o) -> o));
  }

  @Test
  public void testLt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` < `T`.");

    test("lt").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.compareTo(b) < 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.compareTo(b) < 0, o -> o),
        d(long.class, (final long a, final long b) -> Decimal.lt(a, b), o -> o));
  }

  @Test
  public void testGt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` > `T`.");

    test("gt").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.compareTo(b) > 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.compareTo(b) > 0, o -> o),
        d(long.class, (final long a, final long b) -> Decimal.gt(a, b), o -> o));
  }

  @Test
  public void testLte(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` <= `T`.");

    test("lte").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.compareTo(b) <= 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.compareTo(b) <= 0, o -> o),
        d(long.class, (final long a, final long b) -> Decimal.lte(a, b), o -> o));
  }

  @Test
  public void testGte(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` >= `T`.");

    test("gte").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.compareTo(b) >= 0, o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.compareTo(b) >= 0, o -> o),
        d(long.class, (final long a, final long b) -> Decimal.gte(a, b), o -> o));
  }

  @Test
  public void testMax(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Maximum of `T` and `T`.");

    test("max").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.max(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.max(b), o -> o),
        d(long.class, (final long a, final long b) -> Decimal.max(a, b), (final long o) -> o));
  }

  @Test
  public void testMin(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Minimum of `T` and `T`.");

    test("min").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, this::toBigDecimal, (final BigDecimal a, final BigDecimal b) -> a.min(b), o -> o),
        d(Decimal.class, this::toDecimal, this::toDecimal, (final Decimal a, final Decimal b) -> a.min(b), o -> o),
        d(long.class, (final long a, final long b) -> Decimal.min(a, b), (final long o) -> o));
  }

  @Test
  public void testPrecision(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Precision of `T`.");

    test("precision").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.precision(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.precision(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.precision(a), String::valueOf));
  }

  @Test
  public void testHashCode(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Hash code of `T`.");

    test("hashCode").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.hashCode(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.hashCode(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.hashCode(a), String::valueOf));
  }

  @Test
  public void testScale(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Scale of `T`.");

    test("scale").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.scale(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.scale(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.scale(a), String::valueOf));
  }

  @Test
  public void testSignum(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Signum of `T`.");

    test("signum").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.signum(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.signum(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.signum(a), String::valueOf));
  }

  @Test
  public void testByteValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`byte` valye of `T`.");

    test("byteValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.byteValue(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.byteValue(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.byteValue(a), String::valueOf));
  }

  @Test
  public void testShortValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`short` valye of `T`.");

    test("shortValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.shortValue(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.shortValue(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.shortValue(a), String::valueOf));
  }

  @Test
  public void testIntValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`int` valye of `T`.");

    test("intValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.intValue(), (final long a) -> String.valueOf(a)),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.intValue(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.intValue(a), String::valueOf));
  }

  @Test
  public void testLongValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`long` valye of `T`.");

    test("longValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, (final long a) -> toBigDecimal(a).longValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.longValue(), (final long a) -> String.valueOf(a)),
        d(long.class, (final long a) -> Decimal.longValue(a), String::valueOf));
  }

  @Test
  public void testFloatValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`float` valye of `T`.");

    // Special test for Decimal.MIN_VALUE
    System.out.println("[Decimal.MIN_VALUE]_________________________________________________________________________________");
    final int iterations = 100000;
    int progress = 0;
    for (int i = 0; i < iterations; ++i) { // [N]
      final Decimal minDecimal = new Decimal(Decimal.MIN_SIGNIFICAND, CaseTest.DecimalCase.randomScale(Decimal.MIN_SIGNIFICAND));
      final BigDecimal minBigDecimal = minDecimal.toBigDecimal();
      assertEquals(minBigDecimal.floatValue(), minDecimal.floatValue(), 0);
      progress = progress(progress, i, i, iterations);
    }

    test("floatValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.floatValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.floatValue(), String::valueOf),
        d(long.class, (final long a) -> Decimal.floatValue(a), String::valueOf));
  }

  @Test
  public void testDoubleValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`double` valye of `T`.");

    // Special test for Decimal.MIN_VALUE
    System.out.println("[Decimal.MIN_VALUE]_________________________________________________________________________________");
    final int iterations = 100000;
    int progress = 0;
    for (int i = 0; i < iterations; ++i) { // [N]
      final Decimal minDecimal = new Decimal(Decimal.MIN_SIGNIFICAND, CaseTest.DecimalCase.randomScale(Decimal.MIN_SIGNIFICAND));
      final BigDecimal minBigDecimal = minDecimal.toBigDecimal();
      assertEquals(minBigDecimal.doubleValue(), minDecimal.doubleValue(), 0);
      progress = progress(progress, i, i, iterations);
    }

    test("doubleValue").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.doubleValue(), String::valueOf),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.doubleValue(), String::valueOf),
        d(long.class, (final long a) -> Decimal.doubleValue(a), String::valueOf));
  }

  @Test
  public void testToBigInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`T` to `BigInt` or `BigInteger`.");

    test("toBigInt").withAuditReport(report)
      .withCases(
        d(BigDecimal.class, this::toBigDecimal, (final BigDecimal a) -> a.toBigInteger(), String::valueOf),
        d(Decimal.class, this::toDecimal, (final Decimal a) -> a.toBigInt(), BigInt::toString),
        d(long.class, (final long a) -> Decimal.toBigInt(a), BigInt::toString));
  }
}