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

import java.math.BigInteger;

import org.junit.Test;
import org.libj.math.AbstractTest;

public class BigMultiplicationTest extends AbstractTest {
  @Test
  public void testInt() {
    testRange("mul(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> a.mul(b), String::valueOf)
    );
  }

  @Test
  public void testLong() {
    testRange("mul(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> a.mul(b), String::valueOf)
    );
  }

  // FIXME: BigInteger is faster.
  @Test
  public void testBig() {
    testRange("mul(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> a.mul(b), String::valueOf)
    );
  }

  @Test
  public void testUInt() {
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[1];
    testRange("umul(int[],int[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, b -> {
        y[0] = (int)b;
        return y;
      }, (int[] a, int[] b) -> {
        MPN.mul(zds, a, 2, b, 1);
        return zds;
      }, a -> BigNumber.longValue(a, 0, 3)),
      l("BigInt", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, (int[] a, long b) -> {
        BigMultiplication.umul(x, 0, 2, (int)b);
        return x;
      }, a -> BigNumber.longValue(a, 0, 2))
    );
  }

  @Test
  public void testULong() {
    final int[] zds = new int[4];
    final int[] x = new int[4];
    final int[] y = new int[2];
    testRange("umul(long[],long[])",
      l("MPN", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, b -> {
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        return y;
      }, (int[] a, int[] b) -> {
        MPN.mul(zds, a, 2, b, 2);
        return zds;
      }, a -> BigNumber.longValue(a, 0, 3)),
      l("BigInt", a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return x;
      }, (int[] a, long b) -> {
        BigMultiplication.umul(x, 0, 2, b);
        return x;
      }, a -> BigNumber.longValue(a, 0, 2))
    );
  }
}