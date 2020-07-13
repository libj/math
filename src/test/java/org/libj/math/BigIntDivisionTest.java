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
public class BigIntDivisionTest extends BigIntTest {
  @Test
  public void testUnsignedDivInt(final AuditReport report) {
    final int[] sig = {0};
    test("udivRem(int)", report,
      i(BigInteger.class, this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      i(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, int b) -> a.div(sig[0], b), String::valueOf),
      i(int[].class, this::scaledVal, this::nz, (int[] a, int b) -> b == 0 ? ZERO : BigInt.div(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedDivLong(final AuditReport report) {
    final int[] sig = {0};
    test("udiv(long)", report,
      l(BigInteger.class, this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      l(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, long b) -> a.div(sig[0], b), String::valueOf),
      l(int[].class, this::scaledVal, this::nz, (int[] a, long b) -> b == 0 ? ZERO : BigInt.div(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedDivInt(final AuditReport report) {
    test("divRem(int)", report,
      i(BigInteger.class, this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      i(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, int b) -> a.div(b), String::valueOf),
      i(int[].class, this::scaledVal, this::nz, (int[] a, int b) -> b == 0 ? ZERO : BigInt.div(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSignedDivLong(final AuditReport report) {
    test("divRem(long)", report,
      l(BigInteger.class, this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      l(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, long b) -> a.div(b), String::valueOf),
      l(int[].class, this::scaledVal, this::nz, (int[] a, long b) -> b == 0 ? ZERO : BigInt.div(a, b), BigInt::toString)
    );
  }

  @Test
  public void testDivBig(final AuditReport report) {
    test("div(T)", report,
      s(BigInteger.class, this::scaledBigInteger, b -> new BigInteger(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, b -> new BigInt(nz(b)), (BigInt a, BigInt b) -> a.div(b), String::valueOf),
      s(int[].class, this::scaledVal, b -> BigInt.valueOf(nz(b)), (int[] a, int[] b) -> BigInt.div(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedDivRemInt(final AuditReport report) {
    final int[] sig = {0};
    test("udivRem(int)", report,
      i(BigInteger.class, this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      i(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, int b) -> {a.divRem(sig[0], b); return a;}, String::valueOf),
      i(int[].class, this::scaledVal, this::nz, (int[] a, int b) -> {BigInt.divRem(a, sig[0], b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testUnsignedDivRemLong(final AuditReport report) {
    final int[] sig = {0};
    test("udiv(long)", report,
      l(BigInteger.class, this::scaledBigInteger, b -> BigIntegers.valueOf(sig[0] = b % 2 == 0 ? -1 : 1, nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      l(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, long b) -> {a.divRem(sig[0], b); return a;}, String::valueOf),
      l(int[].class, this::scaledVal, this::nz, (int[] a, long b) -> {BigInt.divRem(a, sig[0], b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testSignedDivRemInt(final AuditReport report) {
    test("divRem(int)", report,
      i(BigInteger.class, this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      i(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, int b) -> {a.divRem(b); return a;}, String::valueOf),
      i(int[].class, this::scaledVal, this::nz, (int[] a, int b) -> {BigInt.divRem(a, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testSignedDivRemLong(final AuditReport report) {
    test("divRem(long)", report,
      l(BigInteger.class, this::scaledBigInteger, b -> BigInteger.valueOf(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      l(BigInt.class, this::scaledBigInt, this::nz, (BigInt a, long b) -> {a.divRem(b); return a;}, String::valueOf),
      l(int[].class, this::scaledVal, this::nz, (int[] a, long b) -> {BigInt.divRem(a, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testDivRemBig(final AuditReport report) {
    test("div(T)", report,
      s(BigInteger.class, this::scaledBigInteger, b -> new BigInteger(nz(b)), (BigInteger a, BigInteger b) -> a.divide(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, b -> new BigInt(nz(b)), (BigInt a, BigInt b) -> {a.divRem(b); return a;}, String::valueOf),
      s(int[].class, this::scaledVal, b -> BigInt.valueOf(nz(b)), (int[] a, int[] b) -> {BigInt.divRem(a, b); return a;}, BigInt::toString)
    );
  }
}