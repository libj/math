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

package org.huldra.math;

import java.math.BigInteger;

import org.junit.Test;
import org.libj.math.BigIntegers;

public class BigIntMultiplicationTest extends BigIntTest {
  @Test
  public void testUnsignedInt() {
    final int[] sig = {0};
    test("mul(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.mul(sig[0], b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedInt() {
    test("mul(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.mul(b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedLong() {
    final int[] sig = {0};
    test("mul(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.mul(sig[0], b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedLong() {
    test("mul(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.mul(b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testBig() {
    test("mul(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.mul(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUInt() {
    final int[] zds = new int[3];
    final int[] x = new int[3];
    final int[] y = new int[1];
    final int[] val = new int[4];
    test("umul(int[],int[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)b;
        MPN.mul(zds, x, 2, y, 1);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 2)),
      l("BigInt", a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], (int)b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }

  @Test
  public void testULong() {
    final int[] zds = new int[4];
    final int[] x = new int[3];
    final int[] y = new int[2];
    final int[] val = new int[5];
    test("umul(int[],int[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        MPN.mul(zds, x, 2, y, 2);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 3)),
      l("BigInt", a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }
}