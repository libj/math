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

import java.math.BigInteger;
import java.util.Arrays;

import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;
import org.libj.lang.Numbers;
import org.libj.math.survey.CaseTest;
import org.libj.util.ArrayUtil;

public abstract class BigIntTest extends CaseTest {
  protected static final int[] ZERO = {0};

  private static final int defaultScaleFactor = 2;

  private static final double shouldScaleFactor = 1;
  private static final double shouldBeEqualFactor = 0; // 0.1;
  private static final double shouldInflateFactor = 0; // 0.2;

  private static boolean shoudlScale;
  private static boolean shouldInflate;
  private static boolean shouldBeEqual;

  private static int getInflatedSize() {
    return random.nextInt(2048) + 1;
  }

  public int nz(final int a) {
    return a == 0 ? 1 : a;
  }

  public long nz(final long a) {
    return a == 0 ? 1 : a;
  }

  public String nz(final String a) {
    return "0".equals(a) ? "1" : a;
  }

  public int abs(final int a) {
    return Math.abs(a);
  }

  public long abs(final long a) {
    return Math.abs(a);
  }

  public String abs(final String num) {
    return num.startsWith("-") ? num.substring(1) : num;
  }

  public BigInteger scaledBigInteger(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    return shoudlScale ? new BigInteger(stringScale(String.valueOf(a))) : BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    return shoudlScale ? new BigInteger(stringScale(String.valueOf(a))) : BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    return new BigInteger(shoudlScale ? stringScale(a, factor) : a);
  }

  public BigInteger scaledBigInteger(final String a) {
    return scaledBigInteger(a, defaultScaleFactor);
  }

  public int[] scaledVal(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    final int[] val = newMag(2);
    return shoudlScale ? BigInt.assign(val, stringScale(String.valueOf(a))) : BigInt.assign(val, a);
  }

  public int[] scaledVal(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    final int[] val = newMag(3);
    return shoudlScale ? BigInt.assign(val, stringScale(String.valueOf(a))) : BigInt.assign(val, a);
  }

  public int[] scaledVal(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    int[] val = BigInt.valueOf(a);
    val = newMag(val.length * factor);
    return shoudlScale ? BigInt.assign(val, stringScale(a, factor)) : BigInt.assign(val, a);
  }

  public int[] scaledVal(final String a) {
    return scaledVal(a, defaultScaleFactor);
  }

  public BigInt scaledBigInt(final int a) {
    return new BigInt(scaledVal(a));
  }

  public BigInt scaledBigInt(final long a) {
    return new BigInt(scaledVal(a));
  }

  public BigInt scaledBigInt(final String a) {
    return new BigInt(scaledVal(a));
  }

  public BigInt scaledBigInt(final String a, final int factor) {
    return new BigInt(scaledVal(a, factor));
  }

  public BigIntHuldra scaledBigIntHuldra(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    final BigIntHuldra v = newBigIntHuldra();
    return shoudlScale ? v.assign(stringScale(String.valueOf(a))) : v.assign(a);
  }

  public BigIntHuldra scaledBigIntHuldra(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    final BigIntHuldra v = newBigIntHuldra();
    return shoudlScale ? v.assign(stringScale(String.valueOf(a))) : v.assign(a);
  }

  public BigIntHuldra scaledBigIntHuldra(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    final BigIntHuldra v = newBigIntHuldra();
    return shoudlScale ? v.assign(stringScale(String.valueOf(a), factor)) : v.assign(a);
  }

  public BigIntHuldra scaledBigIntHuldra(final String a) {
    return scaledBigIntHuldra(a, defaultScaleFactor);
  }

  private static BigIntHuldra newBigIntHuldra() {
    return new BigIntHuldra(newMag(0));
  }

  private static int[] newMag(final int size) {
    return shouldInflate ? new int[Math.max(size, getInflatedSize())] : new int[size];
  }

  @Override
  public int[] randomInputs(final int p1, final int p2, final int[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomInt(p1));
    }
    else {
      values[0] = randomInt(p1);
      values[1] = randomInt(p2);
      if (values.length > 2)
        throw new UnsupportedOperationException();
    }

    return values;
  }

  @Override
  public long[] randomInputs(final int p1, final int p2, final long[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomLong(p1));
    }
    else {
      values[0] = randomLong(p1);
      values[1] = randomLong(p2);
      if (values.length > 2)
        throw new UnsupportedOperationException();
    }

    return values;
  }

  @Override
  public String[] randomInputs(final int p1, final int p2, final String[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomBig(p1, true));
    }
    else {
      values[0] = randomBig(p1, true);
      values[1] = randomBig(p2, true);
      if (values.length > 2)
        throw new UnsupportedOperationException();
    }

    return values;
  }

  @Override
  public void onSuccess() {
    shoudlScale = random.nextDouble() < shouldScaleFactor;
    shouldInflate = random.nextDouble() < shouldInflateFactor;
    shouldBeEqual = random.nextDouble() < shouldBeEqualFactor;
  }

  public String stringScale(final String a) {
    return stringScale(a, defaultScaleFactor);
  }

  public String stringScale(final String a, final int factor) {
    if (factor == 1 || a.charAt(0) == '0')
      return a;

    final StringBuilder builder = new StringBuilder(a);
    final boolean isNegative = builder.charAt(0) == '-';
    for (int i = 1, len = a.length(); i < factor; ++i)
      builder.append(a, isNegative ? 1 : 0, len);

    return builder.toString();
  }

  static int randomInt(final int precision) {
    if (precision > 10)
      throw new IllegalArgumentException(String.valueOf(precision));

    final long min = FastMath.e10[precision - 1];
    final long max = FastMath.e10[precision];
    return (int)(random.nextInt((int)(max - min)) + min);
  }

  static long randomLong(final int precision) {
    if (precision < 11)
      return randomInt(precision);

    if (precision > 19)
      throw new IllegalArgumentException(String.valueOf(precision));

    long v = randomInt(precision - 10) * randomInt(10);
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
    for (int i = sign + 1; i < len; ++i)
      num[i] = (char)('0' + random.nextInt(10));

    return new String(num);
  }

  static int[] randomVal(final int length) {
    final int[] val = new int[length + 1];
    val[0] = length;
    for (int i = 1; i <= length; ++i)
      val[i] = random.nextInt();

    return val;
  }

  @Override
  public Color getColor(final Case<?,?,?,?,?> cse) {
    if (cse.getSubject() == BigInteger.class)
      return Ansi.Color.CYAN;

    if (cse.getSubject() == BigInt.class)
      return Ansi.Color.YELLOW;

    if (cse.getSubject() == int[].class)
      return Ansi.Color.GREEN;

    return Ansi.Color.DEFAULT;
  }
}