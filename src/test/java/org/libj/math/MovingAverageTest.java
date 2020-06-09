/* Copyright (c) 2012 LibJ
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

public class MovingAverageTest {
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

  private static void add(final MovingAverage a, final int i) {
    a.add(vals[i]);
    assertEquals(averages[i], a.doubleValue(), epsilon);
  }

  private static void test(final MovingAverage a, final MovingAverage b) {
    for (int i = 0; i < vals.length; ++i) {
      add(a, i);
      add(b, i);
      assertEquals(a.byteValue(), b.byteValue());
      assertEquals(a.shortValue(), b.shortValue());
      assertEquals(a.intValue(), b.intValue());
      assertEquals(a.longValue(), b.longValue());
      assertEquals(a.floatValue(), b.floatValue(), epsilon);
      assertEquals(a.toString(), b.toString());
    }

    assertEquals(vals.length, a.getCount());
  }

  @Test
  public void testMovingAverage() {
    test(new MovingAverage(), new MovingAverage(0));
  }
}