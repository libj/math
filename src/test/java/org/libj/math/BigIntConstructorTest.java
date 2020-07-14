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
//@AuditRunner.Instrument({BigInt.class, int[].class})
//@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntConstructorTest extends BigIntTest {
  @Test
  public void testUnsignedInt(final AuditReport report) {
    test("<init>(int,int)", report,
      i(BigInteger.class, a -> a % 2 == 0 ? -1 : 1, (int a, int b) -> BigIntegers.valueOf(a, b), String::valueOf),
      i(BigInt.class, a -> a % 2 == 0 ? -1 : 1, (int a, int b) -> new BigInt(a, b), String::valueOf),
      i(int[].class, a -> a % 2 == 0 ? -1 : 1, (int a, int b) -> BigInt.valueOf(a, b), BigInt::toString)
    );
  }

  @Test
  public void testSignedInt(final AuditReport report) {
    test("<init>(int)", report,
      i(BigInteger.class, (int a, int b) -> BigInteger.valueOf(a), String::valueOf),
      i(BigInt.class, (int a, int b) -> new BigInt(a), String::valueOf),
      i(int[].class, (int a, int b) -> BigInt.valueOf(a), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedLong(final AuditReport report) {
    test("<init>(int,long)", report,
      l(BigInteger.class, a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> BigIntegers.valueOf((int)a, b), String::valueOf),
      l(BigInt.class, a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> new BigInt((int)a, b), String::valueOf),
      l(int[].class, a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> BigInt.valueOf((int)a, b), BigInt::toString)
    );
  }

  @Test
  public void testSignedLong(final AuditReport report) {
    test("<init>(long)", report,
      l(BigInteger.class, (long a) -> BigInteger.valueOf(a), String::valueOf),
      l(BigInt.class, (long a) -> new BigInt(a), String::valueOf),
      l(int[].class, (long a) -> BigInt.valueOf(a), BigInt::toString)
    );
  }

  @Test
  public void testString(final AuditReport report) {
    test("<init>(String)", report,
      s(BigInteger.class, (String a) -> new BigInteger(a), String::valueOf),
      s(BigInt.class, (String a) -> new BigInt(a), String::valueOf),
      s(int[].class, (String a) -> BigInt.valueOf(a), BigInt::toString)
    );
  }

  @Test
  public void testBytesBigEndian(final AuditReport report) {
    final byte[][] bytes = new byte[1][];
    test("<init>(byte[])", report,
      s(BigInteger.class, a -> bytes[0] = new BigInteger(a).toByteArray(), (byte[] a) -> new BigInteger(a), String::valueOf),
      s(BigInt.class, a -> bytes[0], (byte[] a) -> new BigInt(a, false), String::valueOf),
      s(int[].class, a -> bytes[0], (byte[] a) -> BigInt.valueOf(a, false), BigInt::toString)
    );
  }

  @Test
  public void testBytesLittleEndian(final AuditReport report) {
    final byte[][] bytes = new byte[2][];
    test("<init>(byte[])", report,
      s(BigInteger.class, a ->  { bytes[0] = new BigInteger(a).toByteArray(); bytes[1] = reverse(bytes[0].clone()); return bytes[0]; }, (byte[] a) -> new BigInteger(a), String::valueOf),
      s(BigInt.class, a -> bytes[1], (byte[] a) -> new BigInt(a, true), String::valueOf),
      s(int[].class, a -> bytes[1], (byte[] a) -> BigInt.valueOf(a, true), BigInt::toString)
    );
  }
}