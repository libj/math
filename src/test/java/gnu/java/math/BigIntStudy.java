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

package gnu.java.math;

import org.junit.Test;
import org.libj.math.AbstractTest;

public class BigIntStudy extends AbstractTest {
  /**
   * Tests which is faster:
   * <ol>
   * <li><ins>Regular: {@code int[] {len, signum, ..}}</ins><br>
   * Length and signum separated, thus signum can be -1, 0, or 1. A length of 0
   * means signum must be 0, which means code has to check for this
   * alignment.</li>
   * <li><ins>Compound: {@code int[] {sigLen, ..}}</ins><br>
   * Length and signum are combined, whereby a positive length means a signum =
   * 1, and a negative length a signum = -1. A length of 0 means the value is
   * zero, and signum ends up as 1. No alignment checks are necessary.</li>
   * </ol>
   */
  @Test
  public void testLengthSignum() {
    final int[] v = new int[10];
    testRange("length signum: regular vs compound",
      i("Regular", a -> {v[0] = Math.abs(a / 2); v[1] = Integer.compare(a, 0); return v;}, (int[] a, int b) -> {
        int len = a[0];
        int signum = a[1];

        return len * signum;
      }, Integer::valueOf),
      i("Compound", a -> {v[0] = a / 2; return v;}, (int[] a, int b) -> {
        int signum, len = a[0];
        if (len < 0) {
          len = -len;
          signum = -1;
        }
        else {
          signum = 1;
        }

        return len * signum;
      }, Integer::valueOf)
    );
  }
}