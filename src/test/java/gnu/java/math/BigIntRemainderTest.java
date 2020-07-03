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

public class BigIntRemainderTest extends BigIntTest {
  @Test
  public void testUnsignedRemInt() {
    testRange("rem(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(IRRELEVANT, nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, this::nz, (BigInt a, int b) -> a.rem(IRRELEVANT, b), String::valueOf),
      i("int[]", this::scaledVal, this::nz, (int[] a, int b) -> {BigInt.rem(a, IRRELEVANT, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testUnsignedRemLong() {
    testRange("rem(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(IRRELEVANT, nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      l("BigInt", this::scaledBigInt, this::nz, (BigInt a, long b) -> a.rem(IRRELEVANT, b), String::valueOf),
      l("int[]", this::scaledVal, this::nz, (int[] a, long b) -> {BigInt.rem(a, IRRELEVANT, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testSignedRemInt() {
    testRange("rem(int)",
      i("BigInteger", this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, this::nz, (BigInt a, int b) -> a.rem(b), String::valueOf),
      i("int[]", this::scaledVal, this::nz, (int[] a, int b) -> {BigInt.rem(a, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testSignedRemLong() {
    testRange("rem(long)",
      l("BigInteger", this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      l("BigInt", this::scaledBigInt, this::nz, (BigInt a, long b) -> a.rem(b), String::valueOf),
      l("int[]", this::scaledVal, this::nz, (int[] a, long b) -> {BigInt.rem(a, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testRemBig() {
    testRange("rem(T)",
      s("BigInteger", this::scaledBigInteger, b -> new BigInteger(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      s("BigInt", this::scaledBigInt, b -> new BigInt(nz(b)), (BigInt a, BigInt b) -> b.isZero() ? 0 : a.rem(b), String::valueOf),
      s("int[]", this::scaledVal, b -> BigInt.fromString(nz(b)), (int[] a, int[] b) -> BigInt.isZero(b) ? ZERO : BigInt.rem(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedDivRemInt() {
    final int[] signum = {0};
    testRange("divRem(int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(signum[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, this::nz, (BigInt a, int b) -> a.divRem(signum[0], b), String::valueOf),
      i("int[]", this::scaledVal, this::nz, (int[] a, int b) -> BigInt.divRem(a, signum[0], b), String::valueOf)
    );
  }

  @Test
  public void testUnsignedDivRemLong() {
    final int[] signum = {0};
    testRange("divRem(long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(signum[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b).abs(), String::valueOf),
      l("BigInt", this::scaledBigInt, this::nz, (BigInt a, long b) -> a.divRem(signum[0], b), Long::toUnsignedString),
      l("int[]", this::scaledVal, this::nz, (int[] a, long b) -> BigInt.divRem(a, signum[0], b), Long::toUnsignedString)
    );
  }

  @Test
  public void testSignedDivRemInt() {
    testRange("divRem(int)",
      i("BigInteger", this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, this::nz, (BigInt a, int b) -> a.divRem(b), String::valueOf),
      i("int[]", this::scaledVal, this::nz, (int[] a, int b) -> BigInt.divRem(a, b), String::valueOf)
    );
  }

  @Test
  public void testSignedDivRemLong() {
    testRange("divRem(long)",
      l("BigInteger", this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      l("BigInt", this::scaledBigInt, this::nz, (BigInt a, long b) -> a.divRem(b), String::valueOf),
      l("int[]", this::scaledVal, this::nz, (int[] a, long b) -> BigInt.divRem(a, b), String::valueOf)
    );
  }

  @Test
  public void testDivRemBig() {
    testRange("divRem(T)",
      s("BigInteger", this::scaledBigInteger, b -> new BigInteger(nz(b)), (BigInteger a, BigInteger b) -> a.remainder(b), String::valueOf),
      s("BigInt", this::scaledBigInt, b -> new BigInt(nz(b)), (BigInt a, BigInt b) -> b.isZero() ? 0 : a.divRem(b), String::valueOf),
      s("int[]", this::scaledVal, b -> BigInt.fromString(nz(b)), (int[] a, int[] b) -> BigInt.isZero(b) ? ZERO : BigInt.divRem(a, b), BigInt::toString)
    );
  }
}