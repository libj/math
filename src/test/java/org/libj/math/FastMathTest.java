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

import java.util.Random;

import org.junit.Test;

public class FastMathTest {
  private static final Random random = new Random();

  @Test
  public void testLog2Int() {
    for (int i = 0; i < 100000; ++i) {
      final int n = Math.abs(random.nextInt());
      assertEquals(String.valueOf(n), (byte)(Math.log(n) / Math.log(2)), FastMath.log2(n));
    }
  }

  @Test
  public void testLog2Long() {
    for (int i = 0; i < 100000; ++i) {
      final long n = Math.abs(random.nextLong());
      assertEquals(String.valueOf(n), (byte)(Math.log(n) / Math.log(2)), FastMath.log2(n));
    }
  }

  @Test
  public void testE10() {
    try {
      FastMath.e10((byte)-1);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    for (byte i = 0; i < 19; ++i)
      assertEquals((long)Math.pow(10, i), FastMath.e10(i));

    try {
      FastMath.e10((byte)19);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
  }
}