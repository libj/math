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

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import gnu.java.math.BigIntTest;

public class BigIntegersTest extends BigIntTest {
  @Test
  public void testInternBigInteger() {
    final BigInteger a = new BigInteger("58921");
    final BigInteger b = new BigInteger("122414");
    assertSame(a, BigIntegers.intern(a));
    assertSame(b, BigIntegers.intern(b));

    for (int i = 0; i < 100; ++i) {
      assertSame(a, BigIntegers.intern(new BigInteger("58921")));
      assertSame(b, BigIntegers.intern(new BigInteger("122414")));
    }
  }

  @Test
  public void testInternString() {
    for (int i = 0; i < 100; ++i) {
      new Thread(() -> {
        for (int j = 0; j < 1000; ++j) {
          BigIntegers.intern(String.valueOf(j));
        }
      }).start();
    }
  }

  /**
   * Unsigned long to bytes
   *
   * @param v
   * @return
   */
  public static byte[] toByteArray(long v) {
    final byte[] b = new byte[8];
    for (int j = 7; j >= 0; --j, v >>>= 8)
      b[j] = (byte)(v & 0xFF);

    return b;
  }

  public static byte[] toByteArray(int v) {
    final byte[] b = new byte[4];
    for (int j = 3; j >= 0; --j, v >>>= 8)
      b[j] = (byte)(v & 0xFF);

    return b;
  }

  @Test
  public void testUnsignedBigIntegerInt() {
    test("new unsigned BigInteger: byte[] vs shift",
      i("byte[]", a -> a % 2 == 0 ? -1 : 1, (int a, int b) -> new BigInteger(a, toByteArray(b)), String::valueOf),
      i("shift", a -> a % 2 == 0 ? -1 : 1, (int a, int b) -> BigIntegers.valueOf(a, b), String::valueOf)
    );
  }

  @Test
  public void testUnsignedBigIntegerLong() {
    test("new unsigned BigInteger: byte[] vs shift",
      l("byte[]", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> new BigInteger((int)a, toByteArray(b)), String::valueOf),
      l("shift", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> BigIntegers.valueOf((int)a, b), String::valueOf)
    );
  }

  @Test
  public void testUnsignedBigInteger2() {
    test("signum * value: '*' vs '? :'",
      l("s * v", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> a * b),
      l("s < 0 ? -v : v", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> a < 0 ? -b : b)
    );
  }
}