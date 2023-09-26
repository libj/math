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

import static org.libj.math.survey.AuditMode.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.lang.Numbers;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = BigInteger.class, b = int[].class)
@AuditRunner.Instrument(a = BigInt.class, b = int[].class)
public class BigIntPredicateTest extends BigIntTest {
  @Test
  public void testByteValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as a `byte`.");

    test("byteValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.byteValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.byteValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.byteValue(a), o -> o));
  }

  @Test
  public void testShortValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as a `short`.");

    test("shortValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.shortValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.shortValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.shortValue(a), o -> o));
  }

  @Test
  public void testIntValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as an `int`.");

    test("intValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.intValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.intValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.intValue(a), o -> o));
  }

  @Test
  public void testLongValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as a `long`.");

    test("longValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.longValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.longValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.longValue(a), o -> o));
  }

  @Test
  public void testFloatValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as a `float`.");

    test("floatValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.floatValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.floatValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.floatValue(a), o -> o));
  }

  @Test
  public void testDoubleValue(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Value of number as a `double`.");

    test("doubleValue()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.doubleValue(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.doubleValue(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.doubleValue(a), o -> o));
  }

  @Test
  public void testToString(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "String representation of number in radix 10.");

    test("toString()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.toString(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.toString(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.toString(a), o -> o));
  }

  @Test
  public void testCompareTo(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Compare `T`.");

    test("compareTo(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.compareTo(b), o -> o),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, final BigInt b) -> a.compareTo(b), o -> o),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.compareTo(a, b), o -> o));
  }

  @Test
  public void testEquals(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Equate with `T`.");

    test("equals(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.equals(b), o -> o),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, final BigInt b) -> a.equals(b), o -> o),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.equals(a, b), o -> o));
  }

  @Test
  public void testHashCode(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Hash code of number.");

    test("hashCode()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.hashCode(), o -> Boolean.TRUE),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.hashCode(), o -> Boolean.TRUE),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.hashCode(a), o -> Boolean.TRUE));
  }

  @Test
  public void testAbs(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Absolute value of number.");

    test("abs()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.abs(), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.abs(), String::valueOf),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.abs(a), BigInt::toString));
  }

  @Test
  public void testNeg(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Negated value of number.");

    test("neg()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.negate(), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.neg(), String::valueOf),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.neg(a), BigInt::toString));
  }

  @Test
  public void testSignum(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Signum of number.");

    test("signum()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.signum(), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.signum(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.signum(a), o -> o));
  }

  @Test
  public void testPrecision(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Precision of number in radix 10.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a `precision()` method. Therefore, for this test, precision is determined with [`Numbers.precision(BigInteger)`][Numbers].");

    test("precision()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> Numbers.precision(a), o -> o),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.precision(), o -> o),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.precision(a), o -> o));
  }

  @Test
  public void testMax(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Max with `T`.");

    test("max(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.max(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, final BigInt b) -> a.max(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.max(a, b), BigInt::toString));
  }

  @Test
  public void testMin(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Min with `T`.");

    test("min(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.min(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, final BigInt b) -> a.min(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.min(a, b), BigInt::toString));
  }
}