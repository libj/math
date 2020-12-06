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
  static int skip(final int scaleBits) {
    return (int)Math.pow(scaleBits, scaleBits / 9d);
  }

  BigDecimal toBigDecimal(final long decimal) {
    return Decimal.toBigDecimal(decimal);
  }

  Decimal toDecimal(final long decimal) {
    return new Decimal(decimal);
  }

  long lim(final long dec, final long maxSignificand, final int maxScale) {
    return lim(dec, maxSignificand, maxScale, true);
  }

  long ulim(final long dec, final long maxSignificand, final int maxScale) {
    return lim(dec, maxSignificand, maxScale, false);
  }

  private static long lim(final long dec, final long maxSignificand, final int maxScale, final boolean signed) {
    long significand = Decimal.significand(dec);
    if (significand == 0)
      return 0;

    if (!signed)
      significand = Math.abs(significand);

    boolean changed = false;
    if (changed = (significand <= -maxSignificand || maxSignificand <= significand))
      significand = significand % maxSignificand;

    if (significand == 0)
      return 0;

    short scale = Decimal.scale(dec, significand);
    if (changed |= (scale <= -maxScale || maxScale <= scale))
      scale = (short)(maxScale == 0 ? 0 : scale % maxScale);

    return changed ? Decimal.valueOf(significand, scale, 0) : dec;
  }

  String toDecimalString(final long decimal) {
    final Decimal dec = new Decimal(decimal);
    if (shouldScale[0])
      dec.significand *= 9;

    if (shouldScale[1])
      dec.scale *= 9;

    return shouldScale[2] ? dec.toScientificString() : dec.toString();
  }

  float toDecimalFloat(final long decimal) {
    final Decimal dec = toDecimal(decimal);
    // Have to reduce the scale, otherwise floatValue() returns +/- Infinity
    dec.scale /= 12;
    return dec.floatValue();
  }

  double toDecimalDouble(final long decimal) {
    return toDecimal(decimal).doubleValue();
  }

  public long nz(final long v) {
    return v != 0 ? v : 1;
  }

  public long dnz(final long dec) {
    final long v = FixedPoint.significand(dec);
    return v != 0 ? dec : Decimal.valueOf(1, FixedPoint.scale(dec), 0);
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