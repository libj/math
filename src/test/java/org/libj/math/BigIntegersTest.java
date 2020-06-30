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

public class BigIntegersTest extends AbstractTest {
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

  // FIXME: BigInteger is faster.
  @Test
  public void testUnsignedBigInteger() {
    testRange("unsigned BigInteger via byte[] vs shift",
      l("byte[]", (long a, long b) -> new BigInteger(1, toByteArray(a)), String::valueOf),
      l("shift", (long a, long b) -> BigIntegers.toUnsigned(a), String::valueOf)
    );
  }
}