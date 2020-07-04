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

public class BigIntPredicateTest extends BigIntTest {
  @Test
  public void testByteValue() {
    testRange("byeValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.byteValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.byteValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.byteValue(a))
    );
  }

  @Test
  public void testShortValue() {
    testRange("shortValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.shortValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.shortValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.shortValue(a))
    );
  }

  @Test
  public void testIntValue() {
    testRange("intValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.intValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.intValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.intValue(a))
    );
  }

  @Test
  public void testLongValue() {
    testRange("longValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.longValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.longValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.longValue(a))
    );
  }

  @Test
  public void testFloatValue() {
    testRange("floatValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.floatValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.floatValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.floatValue(a))
    );
  }

  @Test
  public void testDoubleValue() {
    testRange("doubleValue()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.doubleValue()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.doubleValue()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.doubleValue(a))
    );
  }

  @Test
  public void testToString() {
    testRange("toString()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.toString()),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.toString()),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.toString(a))
    );
  }

  @Test
  public void testCompareTo() {
    testRange("compareTo(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.compareTo(b)),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.compareTo(b)),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.compareTo(a, b))
    );
  }

  @Test
  public void testEquals() {
    testRange("equals(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.equals(b)),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.equals(b)),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.equals(a, b))
    );
  }

  @Test
  public void testHashCode() {
    testRange("hashCode()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.hashCode(), o -> Boolean.TRUE),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.hashCode(), o -> Boolean.TRUE),
      s("int[]", this::scaledVal, (int[] a, String b) -> BigInt.hashCode(a), o -> Boolean.TRUE)
    );
  }

  @Test
  public void testAbs() {
    testRange("abs()",
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.abs(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.abs(), String::valueOf),
      s("int[]", this::scaledVal, (int[] a, String b) -> BigInt.abs(a), BigInt::toString)
    );
  }

  @Test
  public void testMax() {
    testRange("max(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.max(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.max(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.max(a, b), BigInt::toString)
    );
  }

  @Test
  public void testMin() {
    testRange("min(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.min(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.min(b), String::valueOf),
      s("int[]", this::scaledVal, BigInt::valueOf, (int[] a, int[] b) -> BigInt.min(a, b), BigInt::toString)
    );
  }
}