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

import java.util.Arrays;

import org.libj.lang.Numbers;
import org.libj.math.survey.CaseTest;

public abstract class NumericCaseTest extends CaseTest {
  protected static final double shouldScaleFactor = 1;
  protected static final double shouldBeEqualFactor = 0; // 0.1;
  protected static final double shouldInflateFactor = 0; // 0.2;

  protected static final boolean[] shouldScale = new boolean[3];
  protected static boolean shouldInflate;
  protected static boolean shouldBeEqual;

  static int randomInt(final int precision) {
    if (precision > 10)
      throw new IllegalArgumentException(String.valueOf(precision));

    final long min = FastMath.longE10[precision - 1];
    final long max = FastMath.longE10[precision];
    return (int)(random.nextInt((int)(max - min)) + min);
  }

  static long randomLong(final int precision) {
    if (precision < 11)
      return randomInt(precision);

    if (precision > 19)
      throw new IllegalArgumentException(String.valueOf(precision));

    long v;
    do
      v = randomInt(precision - 10) * randomInt(10);
    while (v == 0);
    while (Numbers.precision(v) < precision)
      v *= 9;

    return v;
  }

  public static String randomBig(final int len) {
    return randomBig(len, false);
  }

  public static String randomBig(int len, final boolean positive) {
    final int sign = positive ? 0 : random.nextInt(2);
    final char[] num = new char[len += sign];
    if (sign > 0)
      num[0] = '-';

    num[sign] = (char)('1' + random.nextInt(9));
    for (int i = sign + 1; i < len; ++i) // [A]
      num[i] = (char)('0' + random.nextInt(10));

    return new String(num);
  }

  @Override
  public int[] randomInputs(final int p1, final int p2, final int[] inputs) {
    if (shouldBeEqual) {
      Arrays.fill(inputs, randomInt(p1));
    }
    else {
      inputs[0] = randomInt(p1);
      inputs[1] = randomInt(p2);
      if (inputs.length > 2)
        throw new UnsupportedOperationException();
    }

    return inputs;
  }

  @Override
  public long[] randomInputs(final int p1, final int p2, final long[] inputs) {
    if (shouldBeEqual) {
      Arrays.fill(inputs, randomLong(p1));
    }
    else {
      inputs[0] = randomLong(p1);
      inputs[1] = randomLong(p2);
    }

    return inputs;
  }

  @Override
  public String[] randomInputs(final int p1, final int p2, final String[] inputs) {
    if (shouldBeEqual) {
      Arrays.fill(inputs, randomBig(p1, true));
    }
    else {
      inputs[0] = randomBig(p1, true);
      inputs[1] = randomBig(p2, true);
      if (inputs.length > 2)
        throw new UnsupportedOperationException();
    }

    return inputs;
  }

  @Override
  public void onSuccess() {
    for (int i = 0, i$ = shouldScale.length; i < i$; ++i) // [A]
      shouldScale[i] = random.nextDouble() < shouldScaleFactor;

    shouldInflate = random.nextDouble() < shouldInflateFactor;
    shouldBeEqual = random.nextDouble() < shouldBeEqualFactor;
  }
}