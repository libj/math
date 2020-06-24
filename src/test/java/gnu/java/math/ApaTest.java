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

public class ApaTest extends AbstractTest {
  @Test
  public void testMulLong() {
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[2];
    testRange(
      l("BigInt", (long a, long b) -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        BigMultiplication.umul(x, 2, b);
        return BigNumber.longValue(x, 2);
      }),
      l("MPN", (long a, long b) -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        MPN.mul(zds, x, 2, y, 2);
        return BigNumber.longValue(zds, 3);
      })
    );
  }

  @Test
  public void testMulInt() {
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[1];
    testRange(
      l("BigInt", (long a, long b) -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        BigMultiplication.umul(x, 2, (int)b);
        return BigNumber.longValue(x, 2);
      }),
        l("MPN", (long a, long b) -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        y[0] = (int)b;
        MPN.mul(zds, x, 2, y, 1);
        return BigNumber.longValue(zds, 3);
      })
    );
  }
}