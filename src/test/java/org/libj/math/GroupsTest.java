/* Copyright (c) 2014 LibJ
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

import java.util.Arrays;

import org.junit.Test;
import org.libj.lang.ObjectUtil;

public class GroupsTest {
  private static String toString(final int[][] a) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < a.length; ++i) {
      if (i > 0)
        builder.append(",\n");

      builder.append(toString(a[i]));
    }

    return builder.toString();
  }

  private static String toString(final int[] a) {
    final StringBuilder builder = new StringBuilder();
    final String line = Arrays.toString(a);
    builder.append(line);
    builder.setCharAt(builder.length() - line.length(), '{');
    builder.setCharAt(builder.length() - 1, '}');
    return builder.toString();
  }

  private static final int[][] p55 = {
    {1, 2, 3, 4, 0},
    {2, 1, 3, 4, 0},
    {2, 3, 1, 4, 0},
    {3, 2, 1, 4, 0},
    {1, 3, 2, 4, 0},
    {3, 1, 2, 4, 0},
    {3, 2, 4, 1, 0},
    {2, 3, 4, 1, 0},
    {2, 4, 3, 1, 0},
    {4, 2, 3, 1, 0},
    {3, 4, 2, 1, 0},
    {4, 3, 2, 1, 0},
    {1, 3, 4, 2, 0},
    {3, 1, 4, 2, 0},
    {3, 4, 1, 2, 0},
    {4, 3, 1, 2, 0},
    {1, 4, 3, 2, 0},
    {4, 1, 3, 2, 0},
    {1, 2, 4, 3, 0},
    {2, 1, 4, 3, 0},
    {2, 4, 1, 3, 0},
    {4, 2, 1, 3, 0},
    {1, 4, 2, 3, 0},
    {4, 1, 2, 3, 0},
    {4, 2, 3, 0, 1},
    {2, 4, 3, 0, 1},
    {2, 3, 4, 0, 1},
    {3, 2, 4, 0, 1},
    {4, 3, 2, 0, 1},
    {3, 4, 2, 0, 1},
    {3, 2, 0, 4, 1},
    {2, 3, 0, 4, 1},
    {2, 0, 3, 4, 1},
    {0, 2, 3, 4, 1},
    {3, 0, 2, 4, 1},
    {0, 3, 2, 4, 1},
    {4, 3, 0, 2, 1},
    {3, 4, 0, 2, 1},
    {3, 0, 4, 2, 1},
    {0, 3, 4, 2, 1},
    {4, 0, 3, 2, 1},
    {0, 4, 3, 2, 1},
    {4, 2, 0, 3, 1},
    {2, 4, 0, 3, 1},
    {2, 0, 4, 3, 1},
    {0, 2, 4, 3, 1},
    {4, 0, 2, 3, 1},
    {0, 4, 2, 3, 1},
    {1, 4, 3, 0, 2},
    {4, 1, 3, 0, 2},
    {4, 3, 1, 0, 2},
    {3, 4, 1, 0, 2},
    {1, 3, 4, 0, 2},
    {3, 1, 4, 0, 2},
    {3, 4, 0, 1, 2},
    {4, 3, 0, 1, 2},
    {4, 0, 3, 1, 2},
    {0, 4, 3, 1, 2},
    {3, 0, 4, 1, 2},
    {0, 3, 4, 1, 2},
    {1, 3, 0, 4, 2},
    {3, 1, 0, 4, 2},
    {3, 0, 1, 4, 2},
    {0, 3, 1, 4, 2},
    {1, 0, 3, 4, 2},
    {0, 1, 3, 4, 2},
    {1, 4, 0, 3, 2},
    {4, 1, 0, 3, 2},
    {4, 0, 1, 3, 2},
    {0, 4, 1, 3, 2},
    {1, 0, 4, 3, 2},
    {0, 1, 4, 3, 2},
    {1, 2, 4, 0, 3},
    {2, 1, 4, 0, 3},
    {2, 4, 1, 0, 3},
    {4, 2, 1, 0, 3},
    {1, 4, 2, 0, 3},
    {4, 1, 2, 0, 3},
    {4, 2, 0, 1, 3},
    {2, 4, 0, 1, 3},
    {2, 0, 4, 1, 3},
    {0, 2, 4, 1, 3},
    {4, 0, 2, 1, 3},
    {0, 4, 2, 1, 3},
    {1, 4, 0, 2, 3},
    {4, 1, 0, 2, 3},
    {4, 0, 1, 2, 3},
    {0, 4, 1, 2, 3},
    {1, 0, 4, 2, 3},
    {0, 1, 4, 2, 3},
    {1, 2, 0, 4, 3},
    {2, 1, 0, 4, 3},
    {2, 0, 1, 4, 3},
    {0, 2, 1, 4, 3},
    {1, 0, 2, 4, 3},
    {0, 1, 2, 4, 3},
    {1, 2, 3, 0, 4},
    {2, 1, 3, 0, 4},
    {2, 3, 1, 0, 4},
    {3, 2, 1, 0, 4},
    {1, 3, 2, 0, 4},
    {3, 1, 2, 0, 4},
    {3, 2, 0, 1, 4},
    {2, 3, 0, 1, 4},
    {2, 0, 3, 1, 4},
    {0, 2, 3, 1, 4},
    {3, 0, 2, 1, 4},
    {0, 3, 2, 1, 4},
    {1, 3, 0, 2, 4},
    {3, 1, 0, 2, 4},
    {3, 0, 1, 2, 4},
    {0, 3, 1, 2, 4},
    {1, 0, 3, 2, 4},
    {0, 1, 3, 2, 4},
    {1, 2, 0, 3, 4},
    {2, 1, 0, 3, 4},
    {2, 0, 1, 3, 4},
    {0, 2, 1, 3, 4},
    {1, 0, 2, 3, 4},
    {0, 1, 2, 3, 4}
  };

  @Test
  public void testPermute55() {
    final int[][] a = Groups.permute(5, 5);
    assertEquals(toString(p55) + " != " + toString(a), p55.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(p55[i]) + " != " + toString(a[i]), p55[i], a[i]);

    final String[][] b = Groups.permute(5, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] p54 = {
    {2, 3, 4, 0},
    {1, 3, 4, 0},
    {3, 1, 4, 0},
    {2, 1, 4, 0},
    {3, 2, 4, 0},
    {1, 2, 4, 0},
    {2, 4, 1, 0},
    {3, 4, 1, 0},
    {4, 3, 1, 0},
    {2, 3, 1, 0},
    {4, 2, 1, 0},
    {3, 2, 1, 0},
    {3, 4, 2, 0},
    {1, 4, 2, 0},
    {4, 1, 2, 0},
    {3, 1, 2, 0},
    {4, 3, 2, 0},
    {1, 3, 2, 0},
    {2, 4, 3, 0},
    {1, 4, 3, 0},
    {4, 1, 3, 0},
    {2, 1, 3, 0},
    {4, 2, 3, 0},
    {1, 2, 3, 0},
    {2, 3, 0, 1},
    {4, 3, 0, 1},
    {3, 4, 0, 1},
    {2, 4, 0, 1},
    {3, 2, 0, 1},
    {4, 2, 0, 1},
    {2, 0, 4, 1},
    {3, 0, 4, 1},
    {0, 3, 4, 1},
    {2, 3, 4, 1},
    {0, 2, 4, 1},
    {3, 2, 4, 1},
    {3, 0, 2, 1},
    {4, 0, 2, 1},
    {0, 4, 2, 1},
    {3, 4, 2, 1},
    {0, 3, 2, 1},
    {4, 3, 2, 1},
    {2, 0, 3, 1},
    {4, 0, 3, 1},
    {0, 4, 3, 1},
    {2, 4, 3, 1},
    {0, 2, 3, 1},
    {4, 2, 3, 1},
    {4, 3, 0, 2},
    {1, 3, 0, 2},
    {3, 1, 0, 2},
    {4, 1, 0, 2},
    {3, 4, 0, 2},
    {1, 4, 0, 2},
    {4, 0, 1, 2},
    {3, 0, 1, 2},
    {0, 3, 1, 2},
    {4, 3, 1, 2},
    {0, 4, 1, 2},
    {3, 4, 1, 2},
    {3, 0, 4, 2},
    {1, 0, 4, 2},
    {0, 1, 4, 2},
    {3, 1, 4, 2},
    {0, 3, 4, 2},
    {1, 3, 4, 2},
    {4, 0, 3, 2},
    {1, 0, 3, 2},
    {0, 1, 3, 2},
    {4, 1, 3, 2},
    {0, 4, 3, 2},
    {1, 4, 3, 2},
    {2, 4, 0, 3},
    {1, 4, 0, 3},
    {4, 1, 0, 3},
    {2, 1, 0, 3},
    {4, 2, 0, 3},
    {1, 2, 0, 3},
    {2, 0, 1, 3},
    {4, 0, 1, 3},
    {0, 4, 1, 3},
    {2, 4, 1, 3},
    {0, 2, 1, 3},
    {4, 2, 1, 3},
    {4, 0, 2, 3},
    {1, 0, 2, 3},
    {0, 1, 2, 3},
    {4, 1, 2, 3},
    {0, 4, 2, 3},
    {1, 4, 2, 3},
    {2, 0, 4, 3},
    {1, 0, 4, 3},
    {0, 1, 4, 3},
    {2, 1, 4, 3},
    {0, 2, 4, 3},
    {1, 2, 4, 3},
    {2, 3, 0, 4},
    {1, 3, 0, 4},
    {3, 1, 0, 4},
    {2, 1, 0, 4},
    {3, 2, 0, 4},
    {1, 2, 0, 4},
    {2, 0, 1, 4},
    {3, 0, 1, 4},
    {0, 3, 1, 4},
    {2, 3, 1, 4},
    {0, 2, 1, 4},
    {3, 2, 1, 4},
    {3, 0, 2, 4},
    {1, 0, 2, 4},
    {0, 1, 2, 4},
    {3, 1, 2, 4},
    {0, 3, 2, 4},
    {1, 3, 2, 4},
    {2, 0, 3, 4},
    {1, 0, 3, 4},
    {0, 1, 3, 4},
    {2, 1, 3, 4},
    {0, 2, 3, 4},
    {1, 2, 3, 4}
  };

  @Test
  public void testPermute54() {
    final int[][] a = Groups.permute(4, 5);
    assertEquals(toString(p54) + " != " + toString(a), p54.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(p54[i]) + " != " + toString(a[i]), p54[i], a[i]);

    final String[][] b = Groups.permute(4, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] p53 = {
    {3, 4, 0},
    {1, 4, 0},
    {2, 4, 0},
    {4, 1, 0},
    {3, 1, 0},
    {2, 1, 0},
    {4, 2, 0},
    {1, 2, 0},
    {3, 2, 0},
    {4, 3, 0},
    {1, 3, 0},
    {2, 3, 0},
    {3, 0, 1},
    {4, 0, 1},
    {2, 0, 1},
    {0, 4, 1},
    {3, 4, 1},
    {2, 4, 1},
    {0, 2, 1},
    {4, 2, 1},
    {3, 2, 1},
    {0, 3, 1},
    {4, 3, 1},
    {2, 3, 1},
    {3, 0, 2},
    {1, 0, 2},
    {4, 0, 2},
    {0, 1, 2},
    {3, 1, 2},
    {4, 1, 2},
    {0, 4, 2},
    {1, 4, 2},
    {3, 4, 2},
    {0, 3, 2},
    {1, 3, 2},
    {4, 3, 2},
    {4, 0, 3},
    {1, 0, 3},
    {2, 0, 3},
    {0, 1, 3},
    {4, 1, 3},
    {2, 1, 3},
    {0, 2, 3},
    {1, 2, 3},
    {4, 2, 3},
    {0, 4, 3},
    {1, 4, 3},
    {2, 4, 3},
    {3, 0, 4},
    {1, 0, 4},
    {2, 0, 4},
    {0, 1, 4},
    {3, 1, 4},
    {2, 1, 4},
    {0, 2, 4},
    {1, 2, 4},
    {3, 2, 4},
    {0, 3, 4},
    {1, 3, 4},
    {2, 3, 4}
  };

  @Test
  public void testPermute53() {
    final int[][] a = Groups.permute(3, 5);
    assertEquals(toString(p53) + " != " + toString(a), p53.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(p53[i]) + " != " + toString(a[i]), p53[i], a[i]);

    final String[][] b = Groups.permute(3, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] p52 = {
    {4, 0},
    {1, 0},
    {2, 0},
    {3, 0},
    {0, 1},
    {4, 1},
    {2, 1},
    {3, 1},
    {0, 2},
    {1, 2},
    {4, 2},
    {3, 2},
    {0, 3},
    {1, 3},
    {2, 3},
    {4, 3},
    {0, 4},
    {1, 4},
    {2, 4},
    {3, 4}
  };

  @Test
  public void testPermute52() {
    final int[][] a = Groups.permute(2, 5);
    assertEquals(toString(p52) + " != " + toString(a), p52.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(p52[i]) + " != " + toString(a[i]), p52[i], a[i]);

    final String[][] b = Groups.permute(2, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] p51 = {
    {0},
    {1},
    {2},
    {3},
    {4}
  };

  @Test
  public void testPermute51() {
    final int[][] a = Groups.permute(1, 5);
    assertEquals(toString(p51) + " != " + toString(a), p51.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(p51[i]) + " != " + toString(a[i]), p51[i], a[i]);

    final String[][] b = Groups.permute(1, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] c55 = {
    {0, 1, 2, 3, 4}
  };

  @Test
  public void testCombine55() {
    final int[][] a = Groups.combine(5, 5);
    assertEquals(toString(c55) + " != " + toString(a), c55.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(c55[i]) + " != " + toString(a[i]), c55[i], a[i]);

    final String[][] b = Groups.combine(5, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] c54 = {
    {0, 1, 2, 3},
    {0, 1, 2, 4},
    {0, 1, 3, 4},
    {0, 2, 3, 4},
    {1, 2, 3, 4}
  };

  @Test
  public void testCombine54() {
    final int[][] a = Groups.combine(4, 5);
    assertEquals(toString(c54) + " != " + toString(a), c54.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(c54[i]) + " != " + toString(a[i]), c54[i], a[i]);

    final String[][] b = Groups.combine(4, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] c53 = {
    {0, 1, 2},
    {0, 1, 3},
    {0, 1, 4},
    {0, 2, 3},
    {0, 2, 4},
    {0, 3, 4},
    {1, 2, 3},
    {1, 2, 4},
    {1, 3, 4},
    {2, 3, 4}
  };

  @Test
  public void testCombine53() {
    final int[][] a = Groups.combine(3, 5);
    assertEquals(toString(c53) + " != " + toString(a), c53.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(c53[i]) + " != " + toString(a[i]), c53[i], a[i]);

    final String[][] b = Groups.combine(3, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] c52 = {
    {0, 1},
    {0, 2},
    {0, 3},
    {0, 4},
    {1, 2},
    {1, 3},
    {1, 4},
    {2, 3},
    {2, 4},
    {3, 4}
  };

  @Test
  public void testCombine52() {
    final int[][] a = Groups.combine(2, 5);
    assertEquals(toString(c52) + " != " + toString(a), c52.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(c52[i]) + " != " + toString(a[i]), c52[i], a[i]);

    final String[][] b = Groups.combine(2, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }

  private static final int[][] c51 = {
    {0},
    {1},
    {2},
    {3},
    {4}
  };

  @Test
  public void testCombine51() {
    final int[][] a = Groups.combine(1, 5);
    assertEquals(toString(c51) + " != " + toString(a), c51.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(toString(c51[i]) + " != " + toString(a[i]), c51[i], a[i]);

    final String[][] b = Groups.combine(1, "0", "1", "2", "3", "4");
    assertEquals(ObjectUtil.toString(a), ObjectUtil.toString(b));
  }
}