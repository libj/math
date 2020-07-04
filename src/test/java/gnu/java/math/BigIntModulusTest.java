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

public class BigIntModulusTest extends BigIntTest {
  @Test
  public void testBig() {
    testRange("mod(T)",
      s("BigInteger", this::scaledBigInteger, b -> new BigInteger(abs(nz(b))), (BigInteger a, BigInteger b) -> a.mod(b), String::valueOf),
      s("BigInt", this::scaledBigInt, b -> new BigInt(abs(nz(b))), (BigInt a, BigInt b) -> a.mod(b), String::valueOf)
    );
  }
}