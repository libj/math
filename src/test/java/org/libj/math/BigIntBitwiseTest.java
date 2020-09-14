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
import org.libj.util.ArrayUtil;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a=BigInteger.class, b=int[].class)
@AuditRunner.Instrument(a=BigInt.class, b=int[].class)
public class BigIntBitwiseTest extends BigIntTest {
  private static int randomBit(final String seed, final int bitCount) {
    return Math.abs(seed.hashCode() % (bitCount + 1));
  }

  @Test
  public void testBitCount(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The number of bits in the two's complement representation that differ from the sign bit.");

    test("bitCount()").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.bitCount(), Integer::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.bitCount(), Integer::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.bitCount(a), Integer::valueOf)
    );
  }

  @Test
  public void testBitLength(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The number of bits in the minimal two's-complement representation excluding the sign bit.");

    test("bitLength()").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.bitLength(), Integer::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.bitLength(), Integer::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.bitLength(a), Integer::valueOf)
    );
  }

  @Test
  public void testLowestSetBit(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The number of bits in the minimal two's-complement representation excluding the sign bit.");

    test("bitLength()").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.getLowestSetBit(), Integer::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.getLowestSetBit(), Integer::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.getLowestSetBit(a), Integer::valueOf)
    );
  }

  @Test
  public void testToByteArrayBigEndian(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Convert the number to a `byte` array in big-endian order.");

    test("toByteArray() BE").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.toByteArray(), o -> o),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.toByteArray(false), o -> o),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.toByteArray(a, false), o -> o)
    );
  }

  @Test
  public void testToByteArrayLittleEndian(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Convert the number to a `byte` array in little-endian order.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not support little endian `byte[]` encoding. Therefore, for this test, the output array is reversed just for `BigInteger`. The time for the array reversal _is_ included in the runtime measure.");

    test("toByteArray() LE").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> ArrayUtil.reverse(a.toByteArray()), o -> o),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.toByteArray(true), o -> o),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.toByteArray(a, true), o -> o)
    );
  }

  @Test
  public void testTestBit(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Tests whether the provided bit is set.");

    test("testBit(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.testBit(b), Boolean::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.testBit(b), Boolean::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.testBit(a, b), Boolean::valueOf)
    );
  }

  @Test
  public void testSetBit(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Sets the provided bit.");

    test("setBit(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.setBit(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.setBit(b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.setBit(a, b), BigInt::toString)
    );
  }

  @Test
  public void testFlipBit(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Flips the provided bit.");

    test("flipBit(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.flipBit(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.flipBit(b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.flipBit(a, b), BigInt::toString)
    );
  }

  @Test
  public void testClearBit(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Clears the provided bit.");

    test("clearBit(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.clearBit(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.clearBit(b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.clearBit(a, b), BigInt::toString)
    );
  }

  @Test
  public void testShiftLeft(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Shifts the bits in the number left by the given amount.");

    test("shiftLeft(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.shiftLeft(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.shiftLeft(b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.shiftLeft(a, b), BigInt::toString)
    );
  }

  @Test
  public void testShiftRight(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "Shifts the bits in the number right by the given amount.");

    test("shiftRight(int)").withAuditReport(report).withCases(
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.shiftRight(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.shiftRight(b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, int b) -> BigInt.shiftRight(a, b), BigInt::toString)
    );
  }
}