/* Copyright (c) 2020 Seva Safris, LibJ
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

import java.math.BigDecimal;

import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;

abstract class DecimalTest extends NumericCaseTest {
  static final int MAX_SCALE_BITS = isCI ? 13 : Decimal.MAX_SCALE_BITS;

  static int skip(final byte scaleBits) {
    return (int)Math.pow(scaleBits, scaleBits / 9d);
  }

  BigDecimal toBigDecimal(final long decimal) {
    return Decimal.toBigDecimal(decimal, DecimalCase.scaleBitsLocal.get());
  }

  Decimal toDecimal(final long decimal) {
    return new Decimals.Decimal(decimal, DecimalCase.scaleBitsLocal.get());
  }

  public long nz(final long d) {
    return d != 0 ? d : 1;
  }

  @Override
  public Color getColor(final Case<?,?,?,?,?> cse) {
    if (cse.getSubject() == BigDecimal.class)
      return Ansi.Color.CYAN;

    if (cse.getSubject() == Decimal.class)
      return Ansi.Color.YELLOW;

    if (cse.getSubject() == long.class)
      return Ansi.Color.GREEN;

    return Ansi.Color.DEFAULT;
  }
}