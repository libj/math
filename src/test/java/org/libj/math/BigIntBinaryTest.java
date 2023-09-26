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
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = BigInteger.class, b = int[].class)
@AuditRunner.Instrument(a = BigInt.class, b = int[].class)
public class BigIntBinaryTest extends BigIntTest {
  @Test
  public void testAnd(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`and` with `T`.");

    test("and(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.and(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, BigInt b) -> a.and(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.and(a, b), BigInt::toString));
  }

  @Test
  public void testOr(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`or` with `T`.");

    test("or(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.or(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, BigInt b) -> a.or(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.or(a, b), BigInt::toString));
  }

  @Test
  public void testXor(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`xor` with `T`.");

    test("xor(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.xor(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, BigInt b) -> a.xor(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.xor(a, b), BigInt::toString));
  }

  @Test
  public void testNot(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`not` with `T`.");

    test("not()").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, (final BigInteger a) -> a.not(), String::valueOf),
        s(BigInt.class, this::scaledBigInt, (final BigInt a) -> a.not(), String::valueOf),
        s(int[].class, this::scaledVal, (final int[] a) -> BigInt.not(a), BigInt::toString));
  }

  @Test
  public void testAndNot(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "`andNot` with `T`.");

    test("andNot(T)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (final BigInteger a, final BigInteger b) -> a.andNot(b), String::valueOf),
        s(BigInt.class, this::scaledBigInt, BigInt::new, (final BigInt a, BigInt b) -> a.andNot(b), String::valueOf),
        s(int[].class, this::scaledVal, BigInt::valueOf, (final int[] a, final int[] b) -> BigInt.andNot(a, b), BigInt::toString));
  }
}