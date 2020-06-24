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

public class BigRemainderTest extends AbstractTest {
  @Override
  public double rangeCoverage() {
    return 0.00000000008;
  }

  @Test
  public void testInt() {
    testRange(
      i("BigInt", BigInt::new, (BigInt a, int b) -> b == 0 ? 0 : a.div(b), String::valueOf),
      i("BigInteger", BigInteger::valueOf, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b).longValue(), String::valueOf)
    );
  }

  @Test
  public void testLong() {
    testRange(
      l("BigInt", BigInt::new, (BigInt a, long b) -> b == 0 ? 0 : a.div(b), String::valueOf),
      l("BigInteger", BigInteger::valueOf, BigInteger::valueOf, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b).longValue(), String::valueOf)
    );
  }

  // FIXME: BigInteger is faster.
  @Test
  public void testString() {
    testRange(
      s("BigInt", BigInt::new, BigInt::new, (BigInt a, BigInt b) -> b.isZero() ? 0 : a.rem(b), String::valueOf),
      s("BigInteger", BigInteger::new, BigInteger::new, (BigInteger a, BigInteger b) -> b.signum() == 0 ? 0 : a.remainder(b), String::valueOf)
    );
  }
}