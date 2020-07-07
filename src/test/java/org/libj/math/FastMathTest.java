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

import org.huldra.math.BigIntTest;
import org.junit.Test;
import org.libj.lang.Strings;

public class FastMathTest extends BigIntTest {
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
      assertEquals((long)Math.pow(10, i), FastMath.e10[i]);
  }

  @Test
  public void testDivideUnsignedLong() {
    test("divideUnsigned(long, long)",
      l("FastMath", (long a, long b) -> FastMath.divideUnsigned(a, b)),
      l("Long", (long a, long b) -> Long.divideUnsigned(a, b))
    );
  }

  @Test
  public void testRemainderUnsignedLong() {
    test("remainderUnsigned(long, long)",
      l("FastMath", (long a, long b) -> FastMath.remainderUnsigned(a, b)),
      l("Long", (long a, long b) -> Long.remainderUnsigned(a, b))
    );
  }

  @Test
  public void testX() {
    final Long[] time = {0L, 0L};
    for (int i = 0; i < numTests; ++i) {
      final long dividend = -7232583056709551616L;
      final long divisor = 3376106979L;
      long ts = System.nanoTime();
      final long e = Long.remainderUnsigned(dividend, divisor);
      time[0] += System.nanoTime() - ts;
      ts = System.nanoTime();
      final long q = FastMath.remainderUnsigned(dividend, divisor);
      time[1] += System.nanoTime() - ts;
      assertEquals(e, q);
    }

    time[0] /= numTests * 30;
    time[1] /= numTests * 30;
    System.out.println(Strings.printTable(true, true, time, "Long", "FastMath"));
  }
}