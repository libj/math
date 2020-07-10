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
import org.libj.lang.Numbers;

public class BigIntPredicateTest extends BigIntTest {
  @Test
  public void testByteValue() {
    test("byeValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.byteValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.byteValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.byteValue(a))
    );
  }

  @Test
  public void testShortValue() {
    test("shortValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.shortValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.shortValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.shortValue(a))
    );
  }

  @Test
  public void testIntValue() {
    test("intValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.intValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.intValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.intValue(a))
    );
  }

  @Test
  public void testLongValue() {
    test("longValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.longValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.longValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.longValue(a))
    );
  }

  @Test
  public void testFloatValue() {
    test("floatValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.floatValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.floatValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.floatValue(a))
    );
  }

  @Test
  public void testDoubleValue() {
    test("doubleValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.doubleValue()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.doubleValue()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.doubleValue(a))
    );
  }

  @Test
  public void testToString() {
    test("toString()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.toString()),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.toString()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.toString(a))
    );
  }

  @Test
  public void testCompareTo() {
    test("compareTo(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.compareTo(b)),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.compareTo(b)),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.compareTo(a, b))
    );
  }

  @Test
  public void testEquals() {
    test("equals(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.equals(b)),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.equals(b)),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.equals(a, b))
    );
  }

  @Test
  public void testHashCode() {
    test("hashCode()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.hashCode(), o -> Boolean.TRUE),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.hashCode(), o -> Boolean.TRUE),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.hashCode(a), o -> Boolean.TRUE)
    );
  }

  @Test
  public void testAbs() {
    test("abs()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> a.abs(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.abs(), String::valueOf),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.abs(a), BigInt::toString)
    );
  }

  @Test
  public void testPrecision() {
    test("precision()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a) -> Numbers.precision(a)),
      s("BigInt", this::scaledBigInt, (BigInt a) -> a.precision()),
      s("int[]", this::scaledVal, (int[] a) -> BigInt.precision(a))
    );
  }

  @Test
  public void testMax() {
    test("max(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.max(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.max(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.max(a, b), BigInt::toString)
    );
  }

  @Test
  public void testMin() {
    test("min(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.min(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.min(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.min(a, b), BigInt::toString)
    );
  }
}