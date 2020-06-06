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

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalsTest {
  @Test
  public void testIntern() {
    final BigDecimal a = new BigDecimal("589.21");
    final BigDecimal b = new BigDecimal("12.2414");
    assertSame(a, BigDecimals.intern(a));
    assertSame(b, BigDecimals.intern(b));

    for (int i = 0; i < 100; ++i) {
      assertSame(a, BigDecimals.intern(new BigDecimal("589.21")));
      assertSame(b, BigDecimals.intern(new BigDecimal("12.2414")));
    }
  }
}