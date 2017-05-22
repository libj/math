/* Copyright (c) 2012 lib4j
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

package org.safris.commons.math;

import org.junit.Assert;
import org.junit.Test;

public class MovingAverageTest {
  @Test
  public void testMovingAverage() {
    final double[] vals = new double[] {1, 2, 4, 1, 2, 3, 7};
    final double[] averages = new double[] {
      1.0,
      1.5,
      2.3333333333333335,
      2.0,
      2.0,
      2.1666666666666665,
      2.857142857142857
    };

    final MovingAverage movingAverage = new MovingAverage();
    for (int i = 0; i < vals.length; i++) {
      movingAverage.add(vals[i]);
      Assert.assertEquals(averages[i], movingAverage.getAverage(), 0.0001d);
    }
  }
}