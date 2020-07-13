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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.libj.lang.Numbers;
import org.libj.math.survey.CaseTest;

public abstract class BigIntTest extends CaseTest {
  protected static final Random random = new Random();
  protected static final int IRRELEVANT = 1;
  protected static final int[] ZERO = {0};

  private static final double scaleFactor = 0.2;
  private static final double inflateFactor = 0.2;
  private static final double equalFactor = 0.1;

  private static boolean shoudlScale;
  private static boolean shouldInflate;
  private static boolean shouldBeEqual;

  public static byte[] reverse(final byte[] bytes) {
    if (bytes == null || bytes.length <= 1)
      return bytes;

    for (int i = 0; i < bytes.length / 2; ++i) {
      bytes[i] ^= bytes[bytes.length - 1 - i];
      bytes[bytes.length - 1 - i] ^= bytes[i];
      bytes[i] ^= bytes[bytes.length - 1 - i];
    }

    return bytes;
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
      setScaleFactorFactor(IntCase.class, scaleFactorFactor());

    if (shoudlScale)
      return new BigInteger(stringScale(String.valueOf(a)));

    return BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final long a) {
    if (!initialized())
      setScaleFactorFactor(LongCase.class, scaleFactorFactor());

    if (shoudlScale)
      return new BigInteger(stringScale(String.valueOf(a)));

    return BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final String a) {
    if (!initialized())
      setScaleFactorFactor(StringCase.class, scaleFactorFactor());

    return new BigInteger(shoudlScale ? stringScale(a) : a);
  }

  public int[] scaledVal(final int a) {
    if (!initialized())
      setScaleFactorFactor(IntCase.class, scaleFactorFactor());

    final int[] val = BigInt.assign(newVal(2), a);
    if (shoudlScale)
      return BigInt.valueOf(stringScale(BigInt.toString(val)));

    return val;
  }

  public int[] scaledVal(final long a) {
    if (!initialized())
      setScaleFactorFactor(LongCase.class, scaleFactorFactor());

    final int[] val = BigInt.assign(newVal(2), a);
    if (shoudlScale)
      return BigInt.valueOf(stringScale(BigInt.toString(val)));

    return val;
  }

  public int[] scaledVal(final String a) {
    if (!initialized())
      setScaleFactorFactor(StringCase.class, scaleFactorFactor());

    if (shoudlScale)
      return BigInt.valueOf(stringScale(a));

    return BigInt.valueOf(a);
  }

  public BigInt scaledBigInt(final int a) {
    if (!initialized())
      setScaleFactorFactor(IntCase.class, scaleFactorFactor());

    if (shoudlScale)
      return newBigInt().assign(stringScale(String.valueOf(a)));

    return newBigInt().assign(a);
  }

  public BigInt scaledBigInt(final long a) {
    if (!initialized())
      setScaleFactorFactor(LongCase.class, scaleFactorFactor());

    if (shoudlScale)
      return newBigInt().assign(stringScale(String.valueOf(a)));

    return newBigInt().assign(a);
  }

  public BigInt scaledBigInt(final String a) {
    if (!initialized())
      setScaleFactorFactor(StringCase.class, scaleFactorFactor());

    if (shoudlScale)
      return newBigInt().assign(stringScale(a));

    return newBigInt().assign(a);
  }

  private static BigInt newBigInt() {
//    AuditReport.foo = true;
//    System.err.println("-->");
    final BigInt x;
    if (shouldInflate) {
//      System.err.println("1");
      final int[] val = new int[random.nextInt(1024) + 1];
//      System.err.println("2");
      x = new BigInt(val);
//      System.err.println("3");
    }
    else {
//      System.err.println("4");
      x = new BigInt(0);
//      System.err.println("5");
    }
//    AuditReport.foo = false;
//    System.err.println("<--");
    return x;
  }

  private static int[] newVal(final int size) {
    return shouldInflate ? new int[random.nextInt(1024) + 1] : new int[size];
  }

  static int randomInt(final int precision) {
    final long min = FastMath.e10[precision - 1];
    final long max = FastMath.e10[precision];
    return (int)(random.nextInt((int)(max - min)) + min);
  }

  static long randomLong(final int precision) {
    if (precision < 10)
      return randomInt(precision);

    long v = randomInt(precision - 9) * randomInt(9);
    while (Numbers.precision(v) < precision)
      v *= 9;

    return v;
  }

  @Override
  public int[] randomInputs(final int precision, final int[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomInt(precision));
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = randomInt(precision);
    }

    return values;
  }

  @Override
  public long[] randomInputs(final int precision, final long[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomLong(precision));
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = randomLong(precision);
    }

    return values;
  }

  @Override
  public String[] randomInputs(final int precision, final String[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomBig(precision, true));
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = randomBig(precision, true);
    }

    return values;
  }

  @Override
  public void onSuccess() {
    shoudlScale = random.nextDouble() < scaleFactor;
    shouldInflate = random.nextDouble() < inflateFactor;
    shouldBeEqual = random.nextDouble() < equalFactor;
  }

  private static int scaleFactorFactor() {
    return 2;
  }

  public String stringScale(final String a) {
    if (scaleFactorFactor() == 1 || a.charAt(0) == '0')
      return a;

    final char ch = a.charAt(a.length() - 1);
    final StringBuilder builder = new StringBuilder(a);
    for (int i = 1, p; i < scaleFactorFactor(); ++i) {
      p = builder.length();
      builder.append(a);
      builder.setCharAt(p, ch);
    }

    return builder.toString();
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

    final String x = new String(num);
    if (x.length() == 2049)
        System.console();
    return x;
  }
}