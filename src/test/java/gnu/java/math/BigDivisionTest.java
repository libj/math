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

public class BigDivisionTest extends AbstractTest {
  @Test
  @Ignore
  public void testSignedDivInt() {
    testRange("divRem(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> b == 0 ? 0 : a.div(b), String::valueOf)
    );
  }

  @Test
  public void testUnsignedDivInt() {
    testRange("udivRem(int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> b == 0 ? 0 : a.udiv(b), String::valueOf)
    );
  }

  @Test
  @Ignore
  public void testSignedDivLong() {
    testRange("divRem(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> b == 0 ? 0 : a.div(b), String::valueOf)
    );
  }

  @Test
  public void testUnsignedDivLong() {
    testRange("udiv(long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> b == 0 ? 0 : a.udiv(b), String::valueOf)
    );
  }

  @Test
  public void testDivBig() {
    testRange("div(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> b.isZero() ? 0 : a.div(b), String::valueOf)
    );
  }

  @Test
  public void testSignedDivRemInt() {
    testRange("divRem(int)",
      i("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> {if (b == 0) return 0; a.divRem(b); return a;}, String::valueOf)
    );
  }

  @Test
  public void testUnsignedDivRemInt() {
    testRange("udivRem(int)",
      i("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      i("BigInt", this::scaledBigInt, (BigInt a, int b) -> {if (b == 0) return 0; a.udivRem(b); return a;}, String::valueOf)
    );
  }

  @Test
  public void testSignedDivRemLong() {
    testRange("divRem(long)",
      l("BigInteger", this::scaledBigInteger, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> {if (b == 0) return 0; a.divRem(b); return a;}, String::valueOf)
    );
  }

  @Test
  public void testUnsignedDivRemLong() {
    testRange("udiv(long)",
      l("BigInteger", this::scaledBigInteger, b -> BigIntegers.valueOf(b % 2 == 0 ? -1 : 1, b), (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      l("BigInt", this::scaledBigInt, (BigInt a, long b) -> {if (b == 0) return 0; a.udivRem(b); return a;}, String::valueOf)
    );
  }

  @Test
  public void testDivRemBig() {
    testRange("div(T)",
      s("BigInteger", this::scaledBigInteger, BigInteger::new, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.divide(b), String::valueOf),
      s("BigInt", this::scaledBigInt, BigInt::new, (BigInt a, BigInt b) -> {if (b.isZero()) return 0; a.divRem(b); return a;}, String::valueOf)
    );
  }
}