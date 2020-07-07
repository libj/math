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

package org.huldra.math;

import org.junit.Test;

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
      i("Regular", a -> {v[0] = Math.abs(a / 2); v[1] = Integer.compare(a, 0); return v;}, (int[] a) -> {
        int len = a[0];
        int sig = a[1];

        return len * sig;
      }, Integer::valueOf),
      i("Compound", a -> {v[0] = a / 2; return v;}, (int[] a) -> {
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
}