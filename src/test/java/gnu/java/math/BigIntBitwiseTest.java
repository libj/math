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

public class BigIntBitwiseTest extends BigIntTest {
  private static long randomBit(final String seed, final int bitCount) {
    return Math.abs(seed.hashCode() % (bitCount + 1));
  }

  @Test
  public void testBitCount() {
    testRange("bitCount()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.bitCount(), Integer::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.bitCount(), Integer::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.bitCount(a), Integer::valueOf)
    );
  }

  @Test
  public void testTestBit() {
    testRange("testBit(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.testBit(a, (int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testSetBit() {
    testRange("setBit(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.setBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testFlipBit() {
    testRange("flipBit(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.flipBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testClearBit() {
    testRange("clearBit(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.clearBit(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testShiftLeft() {
    testRange("shiftLeft(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.shiftLeft((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.shiftLeft((int)b), String::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.shiftLeft(a, (int)b), BigInt::toString)
    );
  }

  @Test
  public void testShiftRight() {
    testRange("shiftRight(int)",
      s("BigInteger", this::scaledBigInteger, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.shiftRight((int)b), String::valueOf),
      s("BigInt", this::scaledBigInt, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.shiftRight((int)b), String::valueOf),
      s("int[]", this::scaledVal, (a, b) -> randomBit(b, BigInt.bitCount(a)), (int[] a, long b) -> BigInt.shiftRight(a, (int)b), BigInt::toString)
    );
  }
}