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
public class BigIntRemainderTest extends BigIntTest {
  @Test
  public void testUnsignedRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by an unsigned `int`, setting to `this`, and returning the value as an absolute unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the returned `int` remainder result (not the `T` remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("rem(int,int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, (final int b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final int b) -> a.remainder(BigIntegers.valueOf(sig[0], b)).abs(), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.rem(sig[0], b).abs(), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> Integer.toUnsignedLong(BigInt.rem(a, sig[0], b)), String::valueOf));
  }

  @Test
  public void testUnsignedRemIntT(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by an unsigned `int`, setting to `this`, and returning the value as an absolute unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the `T` remainder result (not the returned `int` remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("rem(int,int):T").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, (final int b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final int b) -> a.remainder(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.rem(sig[0], b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> {
          BigInt.rem(a, sig[0], b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testUnsignedRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by an unsigned `long`, setting to `this`, and returning the value as an absolute unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the returned `long` remainder result (not the `T` remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("rem(int,long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, (final long b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final long b) -> a.remainder(BigIntegers.valueOf(sig[0], b)).abs(), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.rem(sig[0], b).abs(), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> BigInt.rem(a, sig[0], b), Long::toUnsignedString));
  }

  @Test
  public void testUnsignedRemLongT(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by an unsigned `long`, setting to `this`, and returning the value as an absolute unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the `T` remainder result (not the returned `int` remainder result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("rem(int,long):T").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, (final long b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final long b) -> a.remainder(BigIntegers.valueOf(sig[0], b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.rem(sig[0], b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> {
          BigInt.rem(a, sig[0], b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testSignedRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by a signed `int`, setting to `this`, and returning the value as an `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the returned `int` remainder result (not the `T` remainder result).");

    test("rem(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final int b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.rem(b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> BigInt.rem(a, b), String::valueOf));
  }

  @Test
  public void testSignedRemIntT(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by a signed `int`, setting to `this`, and returning the value as an `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the `T` remainder result (not the returned `int` remainder result).");

    test("rem(int):T").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final int b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.rem(b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> {
          BigInt.rem(a, b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testSignedRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by a signed `long`, setting to `this`, and returning the value as an `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the returned `long` remainder result (not the `T` remainder result).");

    test("rem(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final long b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.rem(b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> BigInt.rem(a, b), String::valueOf));
  }

  @Test
  public void testSignedRemLongT(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by a signed `long`, setting to `this`, and returning the value as an `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the `T` remainder result (not the returned `long` remainder result).");

    test("rem(long):T").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final long b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.rem(b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> {
          BigInt.rem(a, b);
          return a;
        }, BigInt::toString));
  }

  @Test
  public void testRemBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Remainder from division by `T`.");

    test("rem(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final String b) -> new BigInteger(nz(b)), (final BigInteger a, final BigInteger b) -> a.remainder(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final String b) -> new BigInt(nz(b)), (final BigInt a, final BigInt b) -> a.rem(b), String::valueOf),
        s(int[].class, this::scaledVal, (final String b) -> BigInt.valueOf(nz(b)), (final int[] a, final int[] b) -> BigInt.rem(a, b), BigInt::toString));
  }

  @Test
  public void testUnsignedDivRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `int`, and return the remainder as an absolute unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the remainder result (not the division result).");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("divRem(int,int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, (final int b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final int b) -> a.remainder(BigIntegers.valueOf(sig[0], b)).abs(), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> Integer.toUnsignedLong(a.divRem(sig[0], b)), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> Integer.toUnsignedLong(BigInt.divRem(a, sig[0], b)), String::valueOf));
  }

  @Test
  public void testUnsignedDivRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by an unsigned `long`, and return the remainder as an absolute unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the remainder result (not the division result).");

    final int[] sig = {0};
    test("divRem(int,long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, (final long b) -> {
          sig[0] = b % 2 == 0 ? -1 : 1;
          return nz(b);
        }, (final BigInteger a, final long b) -> a.remainder(BigIntegers.valueOf(sig[0], b)).abs(), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.divRem(sig[0], b), Long::toUnsignedString),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> BigInt.divRem(a, sig[0], b), Long::toUnsignedString));
  }

  @Test
  public void testSignedDivRemInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `int`, and return the remainder as an `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the remainder result (not the division result).");

    test("divRem(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final int b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final int b) -> a.divRem(b), String::valueOf),
        i(int[].class, this::scaledVal, this::nz, (final int[] a, final int b) -> BigInt.divRem(a, b), String::valueOf));
  }

  @Test
  public void testSignedDivRemLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by a signed `long`, and return the remainder as an `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the remainder result (not the division result).");

    test("divRem(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, this::nz, (final BigInteger a, final long b) -> a.remainder(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, this::nz, (final BigInt a, final long b) -> a.divRem(b), String::valueOf),
        l(int[].class, this::scaledVal, this::nz, (final int[] a, final long b) -> BigInt.divRem(a, b), String::valueOf));
  }

  @Test
  public void testDivRemBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Divide by `T`, and return the remainder as a new `T`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the value of the remainder result (not the division result).");

    test("divRem(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final String b) -> new BigInteger(nz(b)), (final BigInteger a, final BigInteger b) -> a.remainder(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final String b) -> new BigInt(nz(b)), (final BigInt a, final BigInt b) -> a.divRem(b), String::valueOf),
        s(int[].class, this::scaledVal, (final String b) -> BigInt.valueOf(nz(b)), (final int[] a, final int[] b) -> BigInt.divRem(a, b), BigInt::toString));
  }

  @Test
  public void testModInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Modulus by `int`.");

    test("mod(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, this::scaledBigInteger, (final int b) -> abs(nz(b)), (final BigInteger a, final int b) -> a.mod(BigInteger.valueOf(b)), String::valueOf),
        i(BigInt.class, this::scaledBigInt, (final int b) -> abs(nz(b)), (final BigInt a, final int b) -> a.mod(b), String::valueOf),
        i(int[].class, this::scaledVal, (final int b) -> abs(nz(b)), (final int[] a, final int b) -> BigInt.mod(a, b), BigInt::toString));
  }

  @Test
  public void testModLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Modulus by `long`.");

    test("mod(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, this::scaledBigInteger, (final long b) -> abs(nz(b)), (final BigInteger a, final long b) -> a.mod(BigInteger.valueOf(b)), String::valueOf),
        l(BigInt.class, this::scaledBigInt, (final long b) -> abs(nz(b)), (final BigInt a, final long b) -> a.mod(b), String::valueOf),
        l(int[].class, this::scaledVal, (final long b) -> abs(nz(b)), (final int[] a, final long b) -> BigInt.mod(a, b), BigInt::toString));
  }

  @Test
  public void testModBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Modulus by `T`.");

    test("mod(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final String b) -> new BigInteger(abs(nz(b))), (final BigInteger a, final BigInteger b) -> a.mod(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final String b) -> new BigInt(abs(nz(b))), (final BigInt a, final BigInt b) -> a.mod(b), String::valueOf),
        s(int[].class, this::scaledVal, (final String b) -> BigInt.valueOf(abs(nz(b))), (final int[] a, final int[] b) -> BigInt.mod(a, b), BigInt::toString));
  }
}