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

public class MovingNormalTest {
  private static final double epsilon = 0.0001;
  private static final double[] vals = {1, 2, 4, 1, 2, 3, 4, 2};
  private static final double[] normals = {
    -1.0,
    1.0,
    1.6329931618554523,
    -0.8164965809277261,
    -0.15617376188860588,
    0.7808688094430303,
    1.462614271203831,
    -0.3375263702778072
  };

  @Test
  public void testMovingNormal() {
    StatMath.normalize(vals);
    final MovingNormal movingNormal = new MovingNormal();
    movingNormal.normalize(0, 2, vals);
    movingNormal.normalize(2, 4, vals);
    movingNormal.normalize(4, 6, vals);
    movingNormal.normalize(6, 8, vals);
    for (int i = 0, i$ = vals.length; i < i$; ++i) { // [A]
      assertEquals(normals[i], vals[i], epsilon);
      assertNotEquals(0, movingNormal.getMean());
      assertNotEquals(0, movingNormal.getScale());
    }
  }
}