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
import org.libj.lang.Numbers;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

@RunWith(AuditRunner.class)
//@AuditRunner.Instrument({BigInt.class, int[].class})
//@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntPredicateTest extends BigIntTest {
  @Test
  public void testByteValue(final AuditReport report) {
    test("byeValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.byteValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.byteValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.byteValue(a))
    );
  }

  @Test
  public void testShortValue(final AuditReport report) {
    test("shortValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.shortValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.shortValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.shortValue(a))
    );
  }

  @Test
  public void testIntValue(final AuditReport report) {
    test("intValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.intValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.intValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.intValue(a))
    );
  }

  @Test
  public void testLongValue(final AuditReport report) {
    test("longValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.longValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.longValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.longValue(a))
    );
  }

  @Test
  public void testFloatValue(final AuditReport report) {
    test("floatValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.floatValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.floatValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.floatValue(a))
    );
  }

  @Test
  public void testDoubleValue(final AuditReport report) {
    test("doubleValue()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.doubleValue()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.doubleValue()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.doubleValue(a))
    );
  }

  @Test
  public void testToString(final AuditReport report) {
    test("toString()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.toString()),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.toString()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.toString(a))
    );
  }

  @Test
  public void testCompareTo(final AuditReport report) {
    test("compareTo(T)", report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.compareTo(b)),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.compareTo(b)),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.compareTo(a, b))
    );
  }

  @Test
  public void testEquals(final AuditReport report) {
    test("equals(T)", report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.equals(b)),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.equals(b)),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.equals(a, b))
    );
  }

  @Test
  public void testHashCode(final AuditReport report) {
    test("hashCode()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.hashCode(), o -> Boolean.TRUE),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.hashCode(), o -> Boolean.TRUE),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.hashCode(a), o -> Boolean.TRUE)
    );
  }

  @Test
  public void testAbs(final AuditReport report) {
    test("abs()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> a.abs(), String::valueOf),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.abs(), String::valueOf),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.abs(a), BigInt::toString)
    );
  }

  @Test
  public void testPrecision(final AuditReport report) {
    test("precision()", report,
      s(BigInteger.class, this::scaledBigInteger, (BigInteger a) -> Numbers.precision(a)),
      s(BigInt.class, this::scaledBigInt, (BigInt a) -> a.precision()),
      s(int[].class, this::scaledVal, (int[] a) -> BigInt.precision(a))
    );
  }

  @Test
  public void testMax(final AuditReport report) {
    test("max(T)", report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.max(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.max(b), String::valueOf),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.max(a, b), BigInt::toString)
    );
  }

  @Test
  public void testMin(final AuditReport report) {
    test("min(T)", report,
      s(BigInteger.class, this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.min(b), String::valueOf),
      s(BigInt.class, this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.min(b), String::valueOf),
      s(int[].class, this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.min(a, b), BigInt::toString)
    );
  }
}