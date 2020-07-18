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

import java.util.function.Function;

import org.junit.Test;
import org.libj.lang.Numbers;

public class BigIntStudy extends BigIntTest {
  /**
   * Tests which is faster:
   * <ol>
   * <li><ins>Regular: {@code int[] {len, sig, ..}}</ins><br>
   * Length and sig separated, thus sig can be -1, 0, or 1. A length of 0
   * means sig must be 0, which means code has to check for this
   * alignment.</li>
   * <li><ins>Compound: {@code int[] {sigLen, ..}}</ins><br>
   * Length and sig are combined, whereby a positive length means a sig =
   * 1, and a negative length a sig = -1. A length of 0 means the value is
   * zero, and sig ends up as 1. No alignment checks are necessary.</li>
   * </ol>
   */
  @Test
  public void testLengthSignum() {
    final int[] v = new int[10];
    test("length signum: regular vs compound",
      i("Regular", a -> { v[0] = Math.abs(a / 2); v[1] = Integer.compare(a, 0); return v; }, (int[] a) -> {
        int len = a[0];
        int sig = a[1];

        return len * sig;
      }, Integer::valueOf),
      i("Compound", a -> { v[0] = a / 2; return v; }, (int[] a) -> {
        int sig, len = a[0];
        if (len < 0) {
          len = -len;
          sig = -1;
        }
        else {
          sig = 1;
        }

        return len * sig;
      }, Integer::valueOf)
    );
  }

  @Test
  public void testNewVsClone() {
    test("new vs clone",
      i("new", a -> new int[Math.abs(a / 100000)], (int[] a) -> new int[a.length], a -> a.length),
      i("clone", a -> new int[Math.abs(a / 100000)], (int[] a) -> a.clone(), a -> a.length)
    );
  }

  @Test
  public void testIpp() {
    test("++i vs i++",
      i("++i", (int a) -> ++a, a -> 0),
      i("i++", (int a) -> a++, a -> 0)
    );
  }

  private static final Function<Object,Object> function = f -> f;

  @Test
  public void testFunction() {
    final Object object = new Object();
    final int[] array = new int[10];

    test("object vs array",
      i("object", (int a) -> function.apply(object), a -> 0),
      i("array", (int a) -> function.apply(array), a -> 0)
    );
  }

  @Test
  public void testRandomIntPrecision() {
    for (int i = 0; i < 1000; ++i) {
      for (int j = 1; j < 10; ++j) {
        final int random = randomInt(j);
        assertEquals(String.valueOf(random), j, Numbers.precision(random));
      }
    }
  }

  @Test
  public void testRandomLongPrecision() {
    for (int i = 0; i < 1000; ++i) {
      for (int j = 1; j < 19; ++j) {
        final long random = randomLong(j);
        assertEquals(j + " " + String.valueOf(random), j, Numbers.precision(random));
      }
    }
  }
}