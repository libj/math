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

import org.junit.Ignore;
import org.junit.Test;
import org.libj.math.AbstractTest;
import org.libj.math.BigIntegers;

public class BigRemainderTest extends AbstractTest {
  private static final int[] ZERO = {0};

  @Test
  public void testUnsignedRemInt() {
    testRange("rem(int,int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> b == 0 ? 0 : a.rem(1, b), String::valueOf),
      i("int[]", this::scaledVal, (int[] a, int b) -> {if (b == 0) return ZERO; BigInt.rem(a, 1, b); return a;}, BigInt::toString)
    );
  }

  @Test
  public void testUnsignedRemLong() {
    testRange("rem(int,long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> b == 0 ? 0 : a.rem(1, b), String::valueOf),
      l("int[]", this::scaledVal, (int[] a, long b) -> {if (b == 0) return ZERO; BigInt.rem(a, 1, b); return a;}, BigInt::toString)
    );
  }

  @Test
  @Ignore
  public void testRemInt() {
    testRange("rem(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> b == 0 ? 0 : a.rem(b), String::valueOf)
    );
  }

  @Test
  @Ignore
  public void testRemLong() {
    testRange("rem(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> b == 0 ? 0 : a.rem(b), String::valueOf)
    );
  }

  @Test
  public void testRemBig() {
    testRange("rem(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> b.isZero() ? 0 : a.rem(b), String::valueOf)
    );
  }

  @Test
  public void testDivRemInt() {
    testRange("divRem(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b).longValue(), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> b == 0 ? 0 : a.divRem(b), String::valueOf)
    );
  }

  @Test
  public void testDivRemLong() {
    testRange("divRem(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b).longValue(), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> b == 0 ? 0 : a.divRem(b), String::valueOf)
    );
  }

  @Test
  public void testDivRemBig() {
    testRange("divRem(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> b.isZero() ? 0 : a.divRem(b), String::valueOf)
    );
  }
}