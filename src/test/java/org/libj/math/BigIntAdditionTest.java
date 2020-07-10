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

public class BigIntAdditionTest extends BigIntTest {
  @Test
  public void testAddUnsignedInt() {
    final BigInteger[] big = {null};
    final int[] sig = {0};
    test("add(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> { big[0] = BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b); return b; }, (BigInteger a, int b) -> a.add(big[0]), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.add(sig[0], b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.add(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testAddSignedInt() {
    test("add(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.add(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.add(b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testAddUnsignedLong() {
    final BigInteger[] big = {null};
    final int[] sig = {0};
    test("add(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> { big[0] = BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b); return b; }, (BigInteger a, long b) -> a.add(big[0]), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.add(sig[0], b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.add(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testAddSignedLong() {
    test("add(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.add(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.add(b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testAddBig() {
    test("add(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.add(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.add(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.add(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubUnsignedInt() {
    final BigInteger[] big = {null};
    final int[] sig = {0};
    test("sub(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> { big[0] = BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b); return b; }, (BigInteger a, int b) -> a.subtract(big[0]), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.sub(sig[0], b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.sub(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSubSignedInt() {
    test("sub(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.subtract(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.sub(b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubUnsignedLong() {
    final BigInteger[] big = {null};
    final int[] sig = {0};
    test("sub(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> { big[0] = BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, b); return b; }, (BigInteger a, long b) -> a.subtract(big[0]), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.sub(sig[0], b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.sub(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSubSignedLong() {
    test("sub(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.subtract(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.sub(b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSubBig() {
    test("sub(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.subtract(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.sub(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.sub(a, b), BigInt::toString)
    );
  }
}