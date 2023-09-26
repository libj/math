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
import org.libj.lang.BigIntegers;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = BigInteger.class, b = int[].class)
@AuditRunner.Instrument(a = BigInt.class, b = int[].class)
public class BigIntDivisionTest extends BigIntTest {
  @Test
  public void testUnsignedDivInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("div(int,int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, b -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final int b) -> a.divide(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.div(sig[0], b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> b == 0 ? ZERO : BigInt.div(a, sig[0], b), BigInt::toString));
  }

  @Test
  public void testUnsignedDivLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("div(int,long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, b -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final long b) -> a.divide(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.div(sig[0], b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> b == 0 ? ZERO : BigInt.div(a, sig[0], b), BigInt::toString));
  }

  @Test
  public void testSignedDivInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `int`.");
    test("div(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final int b) -> a.divide(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.div(b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> b == 0 ? ZERO : BigInt.div(a, b), BigInt::toString));
  }

  @Test
  public void testSignedDivLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `long`.");
    test("div(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final long b) -> a.divide(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.div(b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> b == 0 ? ZERO : BigInt.div(a, b), BigInt::toString));
  }

  @Test
  public void testDivBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by `T`.");
    test("div(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, b -> new BigInteger(nz(b)), (final BigInteger a, final BigInteger b) -> a.divide(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, b -> new BigInt(nz(b)), (final BigInt a, final BigInt b) -> a.div(b), String::valueOf),
        s(int[].class, this::scaledVal, b -> BigInt.valueOf(nz(b)), (final int[] a, final int[] b) -> BigInt.div(a, b), BigInt::toString));
  }

  @Test
  public void testUnsignedDivRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `int`, and return the remainder as an absolute unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the division result (not the remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("divRem(int,int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, b -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final int b) -> a.divide(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> {
          a.divRem(sig[0], b);
          return a;
        }, String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> {
          BigInt.divRem(a, sig[0], b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testUnsignedDivRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `long`, and return the remainder as an absolute unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the division result (not the remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("divRem(int,long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, b -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final long b) -> a.divide(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> {
          a.divRem(sig[0], b);
          return a;
        }, String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> {
          BigInt.divRem(a, sig[0], b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testSignedDivRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `int`, and return the remainder as a signed `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the division result (not the remainder result).");

    test("divRem(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final int b) -> a.divide(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> {
          a.divRem(b);
          return a;
        }, String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> {
          BigInt.divRem(a, b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testSignedDivRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `long`, and return the remainder as a signed `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the division result (not the remainder result).");

    test("divRem(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final long b) -> a.divide(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> {
          a.divRem(b);
          return a;
        }, String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> {
          BigInt.divRem(a, b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testDivRemBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by `T`, and return the remainder as a new `T`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the division result (not the remainder result).");

    test("divRem(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, b -> new BigInteger(nz(b)), (final BigInteger a, final BigInteger b) -> a.divide(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, b -> new BigInt(nz(b)), (final BigInt a, final BigInt b) -> {
          a.divRem(b);
          return a;
        }, String::valueOf),
        s(int[].class, this::scaledVal, b -> BigInt.valueOf(nz(b)), (final int[] a, final int[] b) -> {
          BigInt.divRem(a, b);
          return a;
        }, BigInt::toString));
  }
}