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

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
@AuditRunner.Instrument({BigInt.class, int[].class})
@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntBitwiseTest extends BigIntTest {
  private static long randomBit(final String seed, final int bitCount) {
    return Math.abs(seed.hashCode() % (bitCount + 1));
  }

  @Test
  public void testBitCount(final AuditReport report) {
    test("bitCount()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.bitCount(), Integer::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.bitCount(), Integer::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.bitCount(a), Integer::valueOf)
    );
  }

  @Test
  public void testBitLength(final AuditReport report) {
    test("bitLength()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.bitLength(), Integer::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.bitLength(), Integer::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.bitLength(a), Integer::valueOf)
    );
  }

  @Test
  public void testToByteArrayBigEndian(final AuditReport report) {
    test("toByteArray()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.toByteArray()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.toByteArray(false)),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.toByteArray(a, false))
    );
  }

  @Test
  public void testToByteArrayLittleEndian(final AuditReport report) {
    test("toByteArray()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> reverse(a.toByteArray())),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.toByteArray(true)),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.toByteArray(a, true))
    );
  }

  @Test
  public void testTestBit(final AuditReport report) {
    test("testBit(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.testBit(a, (int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testSetBit(final AuditReport report) {
    test("setBit(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.setBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testFlipBit(final AuditReport report) {
    test("flipBit(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.flipBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testClearBit(final AuditReport report) {
    test("clearBit(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.clearBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testShiftLeft(final AuditReport report) {
    test("shiftLeft(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.shiftLeft((int)b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.shiftLeft((int)b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.shiftLeft(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testShiftRight(final AuditReport report) {
    test("shiftRight(int)", report,
      s(BigInteger.class, this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.shiftRight((int)b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.shiftRight((int)b), String::valueOf),
      s(int[].class, this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.shiftRight(a, (int)b), BigInt::toString)
    );
  }
}