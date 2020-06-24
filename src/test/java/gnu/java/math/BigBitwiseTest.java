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
  public void testClearBitInt() {
    testRange(
      i("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.clearBit(b), String::valueOf),
      i("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.clearBit(b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitInt() {
    testRange(
      i("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.flipBit(b), String::valueOf),
      i("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.flipBit(b), String::valueOf)
    );
  }

  @Test
  public void testSetBitInt() {
    testRange(
      i("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.setBit(b), String::valueOf),
      i("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.setBit(b), String::valueOf)
    );
  }

  @Test
  public void testTestBitInt() {
    testRange(
      i("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, int b) -> a.testBit(b), Boolean::valueOf),
      i("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, int b) -> a.testBit(b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountInt() {
    testRange(
      i("BigInt", BigInt::new, (BigInt a, int b) -> a.bitCount(), Integer::valueOf),
      i("BigInteger", BigInteger::valueOf, (BigInteger a, int b) -> a.bitCount(), Integer::valueOf)
    );
  }

  @Test
  public void testClearBitLong() {
    testRange(
      l("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf),
      l("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitLong() {
    testRange(
      l("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf),
      l("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testSetBitLong() {
    testRange(
      l("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf),
      l("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testTestBitLong() {
    testRange(
      l("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf),
      l("BigInteger", BigInteger::valueOf, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountLong() {
    testRange(
      l("BigInt", BigInt::new, (BigInt a, long b) -> a.bitCount(), Integer::valueOf),
      l("BigInteger", BigInteger::valueOf, (BigInteger a, long b) -> a.bitCount(), Integer::valueOf)
    );
  }

  @Test
  public void testClearBitString() {
    testRange(
      s("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.clearBit((int)b), String::valueOf),
      s("BigInteger", BigInteger::new, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.clearBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testFlipBitString() {
    testRange(
      s("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.flipBit((int)b), String::valueOf),
      s("BigInteger", BigInteger::new, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.flipBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testSetBitString() {
    testRange(
      s("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.setBit((int)b), String::valueOf),
      s("BigInteger", BigInteger::new, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.setBit((int)b), String::valueOf)
    );
  }

  @Test
  public void testTestBitString() {
    testRange(
      s("BigInt", BigInt::new, (a, b) -> randomBit(b, a.bitCount()), (BigInt a, long b) -> a.testBit((int)b), Boolean::valueOf),
      s("BigInteger", BigInteger::new, (a, b) -> randomBit(b, a.bitCount()), (BigInteger a, long b) -> a.testBit((int)b), Boolean::valueOf)
    );
  }

  @Test
  public void testBitCountString() {
    testRange(
      s("BigInt", BigInt::new, (BigInt a, String b) -> a.bitCount(), Integer::valueOf),
      s("BigInteger", BigInteger::new, (BigInteger a, String b) -> a.bitCount(), Integer::valueOf)
    );
  }
}