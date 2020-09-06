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

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class FastMathTest extends BigIntTest {
  private static final int numTests = 1048576;

  @Test
  public void testLog2Int() {
    for (int i = 0; i < numTests; ++i) {
      final int n = Math.abs(random.nextInt());
      assertEquals(String.valueOf(n), (byte)(Math.log(n) / Math.log(2)), FastMath.log2(n));
    }
  }

  @Test
  public void testLog2Long() {
    for (int i = 0; i < numTests; ++i) {
      final long n = Math.abs(random.nextLong());
      assertEquals(String.valueOf(n), (byte)(Math.log(n) / Math.log(2)), FastMath.log2(n));
    }
  }

  @Test
  public void testE10() {
    for (byte i = 0; i < 19; ++i)
      assertEquals((long)Math.pow(10, i), FastMath.longE10[i]);
  }

  @Test
  public void testDivideUnsignedLong() {
    test("divideUnsigned(long,long)").withEpsilon(BigDecimal.ZERO).withCases(
      l("Long", a -> a, this::nz, (long a, long b) -> Long.divideUnsigned(a, b), o -> o),
      l("FastMath", a -> a, this::nz, (long a, long b) -> FastMath.divideUnsigned(a, b), o -> o)
    );
  }

  @Test
  public void testRemainderUnsignedLong() {
    test("remainderUnsigned(long,long)").withEpsilon(BigDecimal.ZERO).withCases(
      l("Long", a -> a, this::nz, (long a, long b) -> Long.remainderUnsigned(a, b), o -> o),
      l("FastMath", a -> a, this::nz, (long a, long b) -> FastMath.remainderUnsigned(a, b), o -> o)
    );
  }

  @Test
  public void testDoubleE10() {
    test("doubleE10(int)").withEpsilon(BigDecimal.ZERO).withCases(
      i("pow(10)", a -> abs(a), (int a) -> StrictMath.pow(10, a), o -> o),
      i("doubleE10", a -> abs(a), (int a) -> FastMath.doubleE10(a), o -> o)
    );
  }
}