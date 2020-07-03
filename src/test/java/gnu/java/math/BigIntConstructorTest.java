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

public class BigIntConstructorTest extends BigIntTest {
  @Test
  public void testInt() {
    testRange("<init>(int)",
      i("BigInteger", (int a, int b) -> BigInteger.valueOf(a), String::valueOf),
      i("BigInt", (int a, int b) -> new BigInt(a), String::valueOf)
    );
  }

  @Test
  public void testLong() {
    testRange("<init>(long)",
      l("BigInteger", (long a, long b) -> BigInteger.valueOf(a), String::valueOf),
      l("BigInt", (long a, long b) -> new BigInt(a), String::valueOf)
    );
  }

  @Test
  public void testBig() {
    testRange("<init>(String)",
      s("BigInteger", (String a, String b) -> new BigInteger(a), String::valueOf),
      s("BigInt", (String a, String b) -> new BigInt(a), String::valueOf)
    );
  }
}