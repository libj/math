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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument({BigInt.class, int[].class})
@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntAdditionTest extends BigIntTest {
  @Test
  public void testAddUnsignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Addition of unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("add(int,int)", BigDecimal.ZERO, report,
      i(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, int b) -> a.add(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.add(sig[0], b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.add(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testAddSignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Addition of signed `int`.");

    test("add(int)", BigDecimal.ZERO, report,
      i(BigInteger.class, this::scaledBigInteger, (BigInteger a, int b) -> a.add(BigInteger.valueOf(b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.add(b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testAddUnsignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Addition of unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("add(int,long)", BigDecimal.ZERO, report,
      l(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, long b) -> a.add(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.add(sig[0], b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.add(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testAddSignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Addition of signed `long`.");

    test("add(long)", BigDecimal.ZERO, report,
      l(BigInteger.class, this::scaledBigInteger, (BigInteger a, long b) -> a.add(BigInteger.valueOf(b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.add(b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testAddBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Addition of `T`.");

    test("add(T)", BigDecimal.ZERO, report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.add(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.add(b), String::valueOf),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubUnsignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtraction of unsigned `int`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("sub(int,int)", BigDecimal.ZERO, report,
      i(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, int b) -> a.subtract(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.sub(sig[0], b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.sub(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSubSignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtraction of signed `int`.");

    test("sub(int)", BigDecimal.ZERO, report,
      i(BigInteger.class, this::scaledBigInteger, (BigInteger a, int b) -> a.subtract(BigInteger.valueOf(b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.sub(b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubUnsignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtraction of unsigned `long`.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");

    final int[] sig = {0};
    test("sub(int,long)", BigDecimal.ZERO, report,
      l(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, long b) -> a.subtract(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.sub(sig[0], b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.sub(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSubSignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtraction of signed `long`.");

    test("sub(long)", BigDecimal.ZERO, report,
      l(BigInteger.class, this::scaledBigInteger, (BigInteger a, long b) -> a.subtract(BigInteger.valueOf(b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.sub(b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Subtraction of `T`.");

    test("sub(T)", BigDecimal.ZERO, report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.subtract(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.sub(b), String::valueOf),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }
}