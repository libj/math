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

package gnu.java.math;

import java.math.BigInteger;

import org.junit.Test;
import org.libj.math.AbstractTest;

public class BigBitwiseTest extends AbstractTest {
  @Override
  public double rangeCoverage() {
    return 0.00000000008;
  }

  private static int randomBit(final int seed, final int bitCount) {
    return Math.abs((seed + 1) % (bitCount + 1));
  }

  private static long randomBit(final long seed, final int bitCount) {
    return Math.abs((seed + 1) % (bitCount + 1));
  }

  private static long randomBit(final String seed, final int bitCount) {
    return Math.abs(seed.hashCode() % (bitCount + 1));
  }

  @Test
  public void testShiftLeft() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> b % 512, (BigInteger a, int b) -> a.shiftLeft(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> b % 512, (BigInt a, int b) -> a.shiftLeft(b), String::valueOf)
    );
  }

  @Test
  public void testShiftRight() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> b % 512, (BigInteger a, int b) -> a.shiftRight(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> b % 512, (BigInt a, int b) -> a.shiftRight(b), String::valueOf)
    );
  }

  @Test
  public void testClearBitInt() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.clearBit(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.clearBit(b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitInt() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.flipBit(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.flipBit(b), String::valueOf)
    );
  }

  @Test
  public void testSetBitInt() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.setBit(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.setBit(b), String::valueOf)
    );
  }

  @Test
  public void testTestBitInt() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.testBit(b), Boolean::valueOf),
      i("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.testBit(b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountInt() {
    testRange(
      i("BigInteger", this::scaledBigInteger, (BigInteger a, int b) -> a.bitCount(), Integer::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.bitCount(), Integer::valueOf)
    );
  }

  @Test
  public void testClearBitLong() {
    testRange(
      l("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf),
      l("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitLong() {
    testRange(
      l("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf),
      l("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testSetBitLong() {
    testRange(
      l("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf),
      l("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testTestBitLong() {
    testRange(
      l("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf),
      l("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountLong() {
    testRange(
      l("BigInteger", this::scaledBigInteger, (BigInteger a, long b) -> a.bitCount(), Integer::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.bitCount(), Integer::valueOf)
    );
  }

  @Test
  public void testClearBitString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testSetBitString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testTestBitString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.bitCount(), Integer::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.bitCount(), Integer::valueOf)
    );
  }
}