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
import org.libj.math.AbstractTest;

public class BigPredicateTest extends AbstractTest {
  @Override
  public double rangeCoverage() {
    return 0.00000000008;
  }

  @Test
  public void testByteValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.byteValue(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.byteValue(), String::valueOf)
    );
  }

  @Test
  public void testShortValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.shortValue(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.shortValue(), String::valueOf)
    );
  }

  @Test
  public void testIntValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.intValue(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.intValue(), String::valueOf)
    );
  }

  @Test
  public void testLongValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.longValue(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.longValue(), String::valueOf)
    );
  }

  // FIXME: BigInteger is faster.
  @Test
  public void testFloatValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.floatValue(), null),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.floatValue(), null)
    );
  }

  @Test
  public void testDoubleValue() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.doubleValue(), null),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.doubleValue(), null)
    );
  }

  @Test
  public void testToString() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.toString(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.toString(), String::valueOf)
    );
  }

  @Test
  public void testCompareTo() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.compareTo(new BigInteger(b)), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.compareTo(new BigInt(b)), String::valueOf)
    );
  }

  @Test
  public void testEquals() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.equals(new BigInteger(b)), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.equals(new BigInt(b)), String::valueOf)
    );
  }

  @Test
  public void testHashCode() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> {a.hashCode(); return 0;}, String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> {a.hashCode(); return 0;}, String::valueOf)
    );
  }

  @Test
  public void testAbs() {
    testRange(
      s("BigInteger", this::scaledBigInteger, (BigInteger a, String b) -> a.abs(), String::valueOf),
      s("BigInt", this::scaledBigInt, (BigInt a, String b) -> a.abs(), String::valueOf)
    );
  }

  // FIXME: BigInteger is faster.
  @Test
  public void testMax() {
    testRange(
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.max(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.max(b), String::valueOf)
    );
  }

  // FIXME: BigInteger is faster.
  @Test
  public void testMin() {
    testRange(
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.min(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.min(b), String::valueOf)
    );
  }
}