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

import gnu.java.math.MPN;

@RunWith(AuditRunner.class)
//@AuditRunner.Instrument({BigInt.class, int[].class})
//@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntMultiplicationTest extends BigIntTest {
  @Test
  public void testUnsignedInt(final AuditReport report) {
    final int[] sig = {0};
    test("mul(int,int)", report,
      i(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, int b) -> a.multiply(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.mul(sig[0], b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedInt(final AuditReport report) {
    test("mul(int)", report,
      i(BigInteger.class, this::scaledBigInteger, (BigInteger a, int b) -> a.multiply(BigInteger.valueOf(b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.mul(b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedLong(final AuditReport report) {
    final int[] sig = {0};
    test("mul(int,long)", report,
      l(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, long b) -> a.multiply(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.mul(sig[0], b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedLong(final AuditReport report) {
    test("mul(long)", report,
      l(BigInteger.class, this::scaledBigInteger, (BigInteger a, long b) -> a.multiply(BigInteger.valueOf(b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.mul(b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testBig(final AuditReport report) {
    test("mul(T)", report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.mul(b), String::valueOf),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testBig3(final AuditReport report) {
    test("mul(T)", report,
      s(BigInteger.class, this::scaledBigInteger, this::scaledBigInteger, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s(BigIntHuldra.class, this::scaledBigIntHuldra, this::scaledBigIntHuldra, (BigIntHuldra a, BigIntHuldra b) -> { a.mul(b); return a; }, String::valueOf),
      s(BigInt.class, this::scaledBigInt, this::scaledBigInt, (BigInt a, BigInt b) -> a.mul(b), String::valueOf)
//      s(int[].class, this::scaledVal, this::scaledVal, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );

//    System.out.println(BigIntHuldra.time + "\n" + BigIntMultiplication.time + "\n" + ((double)BigIntHuldra.time / BigIntMultiplication.time));
//    System.out.println(BigIntMultiplication.MAX);
  }

  @Test
  public void testVeryBig3(final AuditReport report) {
    test("mul(T)", report,
      s(BigInteger.class, a -> scaledBigInteger(a, 8), this::scaledBigInteger, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s(BigIntHuldra.class, a -> scaledBigIntHuldra(a, 8), this::scaledBigIntHuldra, (BigIntHuldra a, BigIntHuldra b) -> { a.mul(b); return a; }, String::valueOf),
      s(BigInt.class, a -> scaledBigInt(a, 8), this::scaledBigInt, (BigInt a, BigInt b) -> a.mul(b), String::valueOf)
//      s(int[].class, this::scaledVal, this::scaledVal, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );

//    System.out.println(BigIntHuldra.time + "\n" + BigIntMultiplication.time + "\n" + ((double)BigIntHuldra.time / BigIntMultiplication.time));
//    System.out.println(BigIntMultiplication.MAX);
  }

  @Test
  public void testVeryBig(final AuditReport report) {
    test("mul(TT)", report,
      s(BigInteger.class, a -> scaledBigInteger(a, 8), BigInteger::new, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s(BigInt.class, a -> scaledBigInt(a, 8), BigInt::new, (BigInt a, BigInt b) -> a.mul(b), String::valueOf),
      s(int[].class, a -> scaledVal(a, 8), BigInt::valueOf, (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSquareBig(final AuditReport report) {
    test("mul(T,T)", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a, String b) -> a.multiply(a), String::valueOf),
      s(int[].class, this::scaledVal, (int[] a, String b) -> BigInt.mul(a, a), BigInt::toString)
    );
  }

  @Test
  public void testSquareVeryBig(final AuditReport report) {
    test("mul(TT,TT)", report,
      s(BigInteger.class, a -> scaledBigInteger(a, 8), (BigInteger a, String b) -> a.multiply(a), String::valueOf),
      s(int[].class, a -> scaledVal(a, 8), (int[] a, String b) -> BigInt.mul(a, a), BigInt::toString)
    );
  }

  @Test
  public void testUInt(final AuditReport report) {
    final int[] zds = new int[3];
    final int[] x = new int[3];
    final int[] y = new int[1];
    final int[] val = new int[4];
    test("umul(int[],int)", report,
      l(MPN.class, a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)b;
        MPN.mul(zds, x, 2, y, 1);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 2)),
      l(BigInt.class, a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], (int)b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }

  @Test
  public void testULong(final AuditReport report) {
    final int[] zds = new int[4];
    final int[] x = new int[3];
    final int[] y = new int[2];
    final int[] val = new int[5];
    test("umul(int[],long)", report,
      l(MPN.class, a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        MPN.mul(zds, x, 2, y, 2);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 3)),
      l(BigInt.class, a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }
}