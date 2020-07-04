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
import org.libj.math.BigIntegers;

public class BigIntMultiplicationTest extends BigIntTest {
  @Test
  public void testUnsignedInt() {
    final int[] signum = {0};
    testRange("mul(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(signum[0] = b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.mul(signum[0], b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> b == 0 ? ZERO : BigInt.mul(a, signum[0], b), BigInt::toString)
    );
  }

  @Test
  public void testInt() {
    testRange("mul(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.mul(b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> b == 0 ? ZERO : BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedLong() {
    final int[] signum = {0};
    testRange("mul(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(signum[0] = b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.mul(signum[0], b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> b == 0 ? ZERO : BigInt.mul(a, signum[0], b), BigInt::toString)
    );
  }

  @Test
  public void testLong() {
    testRange("mul(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.mul(b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> b == 0 ? ZERO : BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testBig() {
    testRange("mul(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.mul(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUInt() {
    final int[] len = {0};
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[1];
    testRange("umul(int[],int[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, b -> {
        y[0] = (int)b;
        return y;
      }, (int[] a, int[] b) -> {
        MPN.mul(zds, a, 2, b, 1);
        return zds;
      }, a -> BigIntValue.longValue(a, 0, 3)),
      l("BigInt", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, (int[] a, long b) -> {
        len[0] = BigIntMultiplication.umul(x, 0, 2, (int)b);
        return x;
      }, a -> BigIntValue.longValue(a, 0, len[0]))
    );
  }

  @Test
  public void testULong() {
    final int[] len = {0};
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[2];
    testRange("umul(long[],long[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, b -> {
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        return y;
      }, (int[] a, int[] b) -> {
        MPN.mul(zds, a, 2, b, 2);
        return zds;
      }, a -> BigIntValue.longValue(a, 0, 3)),
      l("BigInt", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, (int[] a, long b) -> {
        len[0] = BigIntMultiplication.umul(x, 0, 2, b);
        return x;
      }, a -> BigIntValue.longValue(a, 0, len[0]))
    );
  }
}