/* Copyright (c) 2021 Seva Safris, LibJ
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

import org.junit.Test;

@SuppressWarnings("unused")
public class SlidingWindowAverageTest {
  @Test
  public void testNegativeWindowSize1() {
    try {
      new SmaSampler(-5);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testNegativeWindowSize2() {
    try {
      new SmaSampler(-5, 5);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testNullVlaues() {
    try {
      new SmaSampler(5, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
  }

  @Test
  public void testSlidingWindowAverage() {
    final SmaSampler avg = new SmaSampler(4);
    avg.accept(1);
    assertEquals(1, avg.getValue(), Math.ulp(avg.getValue()));
    avg.accept(3);
    assertEquals(2, avg.getValue(), Math.ulp(avg.getValue()));
    avg.accept(5);
    assertEquals(3, avg.getValue(), Math.ulp(avg.getValue()));
    avg.accept(7);
    assertEquals(4, avg.getValue(), Math.ulp(avg.getValue()));
    avg.accept(5);
    assertEquals(5, avg.getValue(), Math.ulp(avg.getValue()));
    avg.accept(7);
    assertEquals(6, avg.getValue(), Math.ulp(avg.getValue()));
  }
}