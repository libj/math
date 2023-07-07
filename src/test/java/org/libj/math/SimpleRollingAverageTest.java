/* Copyright (c) 2012 Seva Safris, LibJ
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

public class SimpleRollingAverageTest {
  private static final double epsilon = 0.0001;
  private static final double[] vals = {1, 2, 4, 1, 2, 3, 7};
  private static final double[] averages = {
    1.0,
    1.5,
    2.3333333333333335,
    2.0,
    2.0,
    2.1666666666666665,
    2.857142857142857
  };

  private static void add(final SimpleRollingAverage a, final int i) {
    a.accept(vals[i]);
    assertEquals(averages[i], a.getValue(), epsilon);
  }

  private static void test(final SimpleRollingAverage a, final SimpleRollingAverage b) {
    for (int i = 0, i$ = vals.length; i < i$; ++i) { // [A]
      add(a, i);
      add(b, i);
      assertEquals(a.getValue(), b.getValue(), epsilon);
      assertEquals(a.toString(), b.toString());
    }

    assertEquals(vals.length, a.getSampleCount());
  }

  @Test
  public void testMovingAverage() {
    test(new SimpleRollingAverage(), new SimpleRollingAverage(0));
  }
}