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

public class GroupsTest {
  private static String print(final int[][] a) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < a.length; ++i) {
      if (i > 0)
        builder.append(",\n");

      builder.append(print(a[i]));
    }

    return builder.toString();
  }

  private static String print(final int[] a) {
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
    assertEquals(print(p55) + " != " + print(a), p55.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(p55[i]) + " != " + print(a[i]), p55[i], a[i]);
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
    final int[][] a = Groups.permute(5, 4);
    assertEquals(print(p54) + " != " + print(a), p54.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(p54[i]) + " != " + print(a[i]), p54[i], a[i]);
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
    final int[][] a = Groups.permute(5, 3);
    assertEquals(print(p53) + " != " + print(a), p53.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(p53[i]) + " != " + print(a[i]), p53[i], a[i]);
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
    final int[][] a = Groups.permute(5, 2);
    assertEquals(print(p52) + " != " + print(a), p52.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(p52[i]) + " != " + print(a[i]), p52[i], a[i]);
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
    final int[][] a = Groups.permute(5, 1);
    assertEquals(print(p51) + " != " + print(a), p51.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(p51[i]) + " != " + print(a[i]), p51[i], a[i]);
  }

  private static final int[][] c55 = {
    {0, 1, 2, 3, 4}
  };

  @Test
  public void testCombine55() {
    final int[][] a = Groups.combine(5, 5);
    assertEquals(print(c55) + " != " + print(a), c55.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(c55[i]) + " != " + print(a[i]), c55[i], a[i]);
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
    final int[][] a = Groups.combine(5, 4);
    assertEquals(print(c54) + " != " + print(a), c54.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(c54[i]) + " != " + print(a[i]), c54[i], a[i]);
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
    final int[][] a = Groups.combine(5, 3);
    assertEquals(print(c53) + " != " + print(a), c53.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(c53[i]) + " != " + print(a[i]), c53[i], a[i]);
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
    final int[][] a = Groups.combine(5, 2);
    assertEquals(print(c52) + " != " + print(a), c52.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(c52[i]) + " != " + print(a[i]), c52[i], a[i]);
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
    final int[][] a = Groups.combine(5, 1);
    assertEquals(print(c51) + " != " + print(a), c51.length, a.length);
    for (int i = 0; i < a.length; ++i)
      assertArrayEquals(print(c51[i]) + " != " + print(a[i]), c51[i], a[i]);
  }
}