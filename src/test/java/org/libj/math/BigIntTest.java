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

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;

public abstract class BigIntTest extends NumericCaseTest {
  protected static final int[] ZERO = {0};

  private static final int defaultScaleFactor = 2;

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
    return a == Integer.MIN_VALUE ? Integer.MAX_VALUE : Math.abs(a);
  }

  public long abs(final long a) {
    return a == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(a);
  }

  public String abs(final String num) {
    return num.startsWith("-") ? num.substring(1) : num;
  }

  public BigInteger scaledBigInteger(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    return shouldScale[0] ? new BigInteger(stringScale(String.valueOf(a))) : BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    return shouldScale[0] ? new BigInteger(stringScale(String.valueOf(a))) : BigInteger.valueOf(a);
  }

  public BigInteger scaledBigInteger(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    return new BigInteger(shouldScale[0] ? stringScale(a, factor) : a);
  }

  public BigInteger scaledBigInteger(final String a) {
    return scaledBigInteger(a, defaultScaleFactor);
  }

  public int[] scaledVal(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    final int[] val = newMag(2);
    return shouldScale[0] ? BigInt.assign(val, stringScale(String.valueOf(a))) : BigInt.assign(val, a);
  }

  public int[] scaledVal(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    final int[] val = newMag(3);
    return shouldScale[0] ? BigInt.assign(val, stringScale(String.valueOf(a))) : BigInt.assign(val, a);
  }

  public int[] scaledVal(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    int[] val = BigInt.valueOf(a);
    val = newMag(val.length * factor);
    return shouldScale[0] ? BigInt.assign(val, stringScale(a, factor)) : BigInt.assign(val, a);
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

  public org.huldra.math.BigInt scaledBigIntHuldra(final int a) {
    if (!initialized())
      setScaleFactor(IntCase.class, defaultScaleFactor);

    final org.huldra.math.BigInt v = newBigIntHuldra();
    return shouldScale[0] ? v.assign(stringScale(String.valueOf(a))) : v.assign(a);
  }

  public org.huldra.math.BigInt scaledBigIntHuldra(final long a) {
    if (!initialized())
      setScaleFactor(LongCase.class, defaultScaleFactor);

    final org.huldra.math.BigInt v = newBigIntHuldra();
    return shouldScale[0] ? v.assign(stringScale(String.valueOf(a))) : v.assign(a);
  }

  public org.huldra.math.BigInt scaledBigIntHuldra(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    final org.huldra.math.BigInt v = newBigIntHuldra();
    return shouldScale[0] ? v.assign(stringScale(String.valueOf(a), factor)) : v.assign(a);
  }

  public org.huldra.math.BigInt scaledBigIntHuldra(final String a) {
    return scaledBigIntHuldra(a, defaultScaleFactor);
  }

  private static org.huldra.math.BigInt newBigIntHuldra() {
    return new org.huldra.math.BigInt(newMag(0));
  }

  public String scaledString(final String a, final int factor) {
    if (!initialized())
      setScaleFactor(StringCase.class, factor);

    return shouldScale[0] ? stringScale(a, factor) : a;
  }

  private static int[] newMag(final int size) {
    return shouldInflate ? new int[Math.max(size, getInflatedSize())] : new int[size];
  }

  public String stringScale(final String a) {
    return stringScale(a, defaultScaleFactor);
  }

  public String stringScale(final String a, final int factor) {
    if (factor == 1 || a.charAt(0) == '0')
      return a;

    final StringBuilder builder = new StringBuilder(a);
    final boolean isNegative = builder.charAt(0) == '-';
    for (int i = 1, i$ = a.length(); i < factor; ++i) // [N]
      builder.append(a, isNegative ? 1 : 0, i$);

    return builder.toString();
  }

  static int[] randomVal(final int length) {
    final int[] val = new int[length + 1];
    val[0] = length;
    for (int i = 1; i <= length; ++i) // [A]
      val[i] = random.nextInt();

    return val;
  }

  static BigInteger newMaxBigInteger(final int byteLength) {
    final int[] mag = new int[byteLength];
    for (int i = 0, i$ = mag.length; i < i$; ++i) // [A]
      mag[i] = -1;

    try {
      final Constructor<BigInteger> c = BigInteger.class.getDeclaredConstructor(int[].class, int.class);
      c.setAccessible(true);
      return c.newInstance(mag, 1);
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
  }

  static int[] newMaxValue(final int byteLength) {
    final int[] val = new int[byteLength + 1];
    val[0] = val.length - 1;
    for (int i = 1, i$ = val.length; i < i$; ++i) // [A]
      val[i] = -1;

    return val;
  }

  @Override
  public Color getColor(final Case<?,?,?,?,?> cse) {
    if (cse.getSubject() == BigInteger.class || cse.getSubject() == BigDecimal.class)
      return Ansi.Color.CYAN;

    if (cse.getSubject() == BigInt.class || cse.getSubject() == Decimal.class)
      return Ansi.Color.YELLOW;

    if (cse.getSubject() == int[].class || cse.getSubject() == long.class)
      return Ansi.Color.GREEN;

    return Ansi.Color.DEFAULT;
  }
}