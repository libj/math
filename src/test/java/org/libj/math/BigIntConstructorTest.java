/* Copyright (c) 2020 Seva Safris, LibJ
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

import static org.libj.math.survey.AuditMode.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.lang.BigIntegers;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;
import org.libj.util.ArrayUtil;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument(a = BigInteger.class, b = int[].class)
@AuditRunner.Instrument(a = BigInt.class, b = int[].class)
public class BigIntConstructorTest extends BigIntTest {
  @Test
  public void testUnsignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the [`BigIntegers.valueOf(int)`][BigIntegers] utility method is used to provide this missing function.");

    test("<init>(int,int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, (final int a) -> a % 2 == 0 ? -1 : 1, (final int a, final int b) -> BigIntegers.valueOf(a, b), String::valueOf),
        i(BigInt.class, (final int a) -> a % 2 == 0 ? -1 : 1, (final int a, final int b) -> new BigInt(a, b), String::valueOf),
        i(int[].class, (final int a) -> a % 2 == 0 ? -1 : 1, (final int a, final int b) -> BigInt.valueOf(a, b), BigInt::toString));
  }

  @Test
  public void testSignedInt(final AuditReport report) {
    test("<init>(int)").withAuditReport(report)
      .withCases(
        i(BigInteger.class, (final int a, final int b) -> BigInteger.valueOf(a), String::valueOf),
        i(BigInt.class, (final int a, final int b) -> new BigInt(a), String::valueOf),
        i(int[].class, (final int a, final int b) -> BigInt.valueOf(a), BigInt::toString));
  }

  @Test
  public void testUnsignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the [`BigIntegers.valueOf(long)`][BigIntegers] utility method is used to provide this missing function.");

    test("<init>(int,long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, (final long a) -> a % 2 == 0 ? -1 : 1, (final long a, final long b) -> BigIntegers.valueOf((int)a, b), String::valueOf),
        l(BigInt.class, (final long a) -> a % 2 == 0 ? -1 : 1, (final long a, final long b) -> new BigInt((int)a, b), String::valueOf),
        l(int[].class, (final long a) -> a % 2 == 0 ? -1 : 1, (final long a, final long b) -> BigInt.valueOf((int)a, b), BigInt::toString));
  }

  @Test
  public void testSignedLong(final AuditReport report) {
    test("<init>(long)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, (final long a) -> BigInteger.valueOf(a), String::valueOf),
        l(BigInt.class, (final long a) -> new BigInt(a), String::valueOf),
        l(int[].class, (final long a) -> BigInt.valueOf(a), BigInt::toString));
  }

  private static double strip(final double v, final String[] y) {
    String str = String.valueOf(v);
    str = str.replace(".", "").replace("E-", "E");
    y[0] = str;
    return Double.valueOf(str);
  }

  @Test
  // FIXME: Create a test case for double
  public void testDouble(final AuditReport report) {
    final double[] x = new double[1];
    final String[] y = new String[1];
    test("<init>(double)").withAuditReport(report)
      .withCases(
        l(BigInteger.class, (final long a) -> {
          x[0] = strip(random.nextDouble() * random.nextLong(), y);
          return y[0];
        }, (final String a) -> new BigInteger(a), (final BigInteger o) -> o == null ? null : String.valueOf(o)),
        l(int[].class, (final long a) -> BigInt.valueOf(x[0]), (final int[] o) -> o == null ? null : BigInt.toString(o)));
  }

  @Test
  public void testString(final AuditReport report) {
    test("<init>(String)").withAuditReport(report)
      .withCases(
        s(BigInteger.class, (final String a) -> new BigInteger(a), String::valueOf),
        s(BigInt.class, (final String a) -> new BigInt(a), String::valueOf),
        s(int[].class, (final String a) -> BigInt.valueOf(a), BigInt::toString));
  }

  @Test
  public void testBytesBigEndian(final AuditReport report) {
    final byte[][] bytes = new byte[1][];
    test("<init>(byte[])").withAuditReport(report)
      .withCases(
        s(BigInteger.class, (final String a) -> bytes[0] = new BigInteger(a).toByteArray(), (final byte[] a) -> new BigInteger(a), String::valueOf),
        s(BigInt.class, (final String a) -> bytes[0], (final byte[] a) -> new BigInt(a, false), String::valueOf),
        s(int[].class, (final String a) -> bytes[0], (final byte[] a) -> BigInt.valueOf(a, false), BigInt::toString));
  }

  @Test
  public void testBytesLittleEndian(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not support little endian `byte[]` encoding. Therefore, for this test, the input array is reversed just for `BigInteger`. The time for the array reversal _is not_ included in the runtime measure.");

    final byte[][] bytes = new byte[2][];
    test("<init>(byte[])").withAuditReport(report)
      .withCases(
        s(BigInteger.class, (final String a) -> {
          bytes[0] = new BigInteger(a).toByteArray();
          bytes[1] = ArrayUtil.reverse(bytes[0].clone());
          return bytes[0];
        }, (final byte[] a) -> new BigInteger(a), String::valueOf),
        s(BigInt.class, (final String a) -> bytes[1], (final byte[] a) -> new BigInt(a, true), String::valueOf),
        s(int[].class, (final String a) -> bytes[1], (final byte[] a) -> BigInt.valueOf(a, true), BigInt::toString));
  }
}