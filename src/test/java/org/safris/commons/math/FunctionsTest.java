/* Copyright (c) 2008 lib4j
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

public class FunctionsTest {
  @Test
  public void testRound() {
    try {
      Assert.assertEquals(12d, Functions.round(12.45, -1), 0);
    }
    catch (final IllegalArgumentException e) {
      if (!"digits < 0".equals(e.getMessage()))
        throw e;
    }

    Assert.assertEquals(12d, Functions.round(12.45, 0), 0);
    Assert.assertEquals(12.5d, Functions.round(12.45, 1), 0);
    Assert.assertEquals(12.45d, Functions.round(12.45, 2), 0);
  }

  @Test
  public void testBinaryClosestSearch() {
    final int[] sorted = new int[] {1, 3, 5, 9};
    Assert.assertEquals(0, Functions.binaryClosestSearch(sorted, 0, 0, sorted.length));
    Assert.assertEquals(1, Functions.binaryClosestSearch(sorted, 2, 0, sorted.length));
    Assert.assertEquals(2, Functions.binaryClosestSearch(sorted, 4, 0, sorted.length));
    Assert.assertEquals(3, Functions.binaryClosestSearch(sorted, 6, 0, sorted.length));
    Assert.assertEquals(3, Functions.binaryClosestSearch(sorted, 9, 0, sorted.length));
    Assert.assertEquals(4, Functions.binaryClosestSearch(sorted, 10, 0, sorted.length));
  }

  @Test
  public void testLog() {
    Assert.assertEquals(0d, Functions.log(0, 2), 0d);
    Assert.assertEquals(0d, Functions.log(2, 1), 0d);
    Assert.assertEquals(2d, Functions.log(2, 4), 0d);
    Assert.assertEquals(Double.POSITIVE_INFINITY, Functions.log(1, 2), 0d);
    Assert.assertEquals(Double.NEGATIVE_INFINITY, Functions.log(1, 0), 0d);
  }
}