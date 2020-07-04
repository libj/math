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
import java.util.Arrays;

import org.libj.math.AbstractTest;

public abstract class BigIntTest extends AbstractTest {
  public static final int IRRELEVANT = 1;
  public static final int[] ZERO = {0};
  static final int numTests = 1000000;
  private static final double scaleFactor = 0.2;
  private static final double inflateFactor = 0.2;
  private static final double equalFactor = 0.1;
  private static boolean shoudlScale;
  private static boolean shouldInflate;
  private static boolean shouldBeEqual;

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
    final BigInteger b = BigInteger.valueOf(a);
    return shoudlScale ? b.multiply(BigInteger.valueOf(intScale())) : b;
  }

  public BigInteger scaledBigInteger(final long a) {
    final BigInteger b = BigInteger.valueOf(a);
    return shoudlScale ? b.multiply(BigInteger.valueOf(longScale())) : b;
  }

  public BigInteger scaledBigInteger(final String a) {
    return new BigInteger(shoudlScale ? stringScale(a) : a);
  }

  private static BigInt newBigInt() {
    return shouldInflate ? new BigInt(new int[random.nextInt(1024) + 1]) : new BigInt(0);
  }

  private static int[] newVal(final int size) {
    return shouldInflate ? new int[random.nextInt(1024) + 1] : new int[size];
  }

  public int[] scaledVal(final int a) {
    final int[] b = BigInt.assign(newVal(2), a);
    return shoudlScale ? BigInt.mul(b, intScale()) : b;
  }

  public int[] scaledVal(final long a) {
    final int[] b = BigInt.assign(newVal(2), a);
    return shoudlScale ? BigInt.mul(b, intScale()) : b;
  }

  public int[] scaledVal(final String a) {
    return BigInt.valueOf(shoudlScale ? stringScale(a) : a);
  }

  public BigInt scaledBigInt(final int a) {
    final BigInt b = newBigInt().assign(a);
    return shoudlScale ? b.mul(intScale()) : b;
  }

  public BigInt scaledBigInt(final long a) {
    final BigInt b = newBigInt().assign(a);
    return shoudlScale ? b.mul(longScale()) : b;
  }

  public BigInt scaledBigInt(final String a) {
    return newBigInt().assign(shoudlScale ? stringScale(a) : a);
  }

  @Override
  public int[] randomInputs(final int[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, random.nextInt());
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = random.nextInt();
    }

    return values;
  }

  @Override
  public long[] randomInputs(final long[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, random.nextInt());
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = random.nextInt();
    }

    return values;
  }

  @Override
  public String[] randomInputs(final int len, final String[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomBig(random.nextInt(len + 1) + 1, true));
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = randomBig(random.nextInt(len + 1) + 1, true);
    }

    return values;
  }

  @Override
  public void onSuccess() {
    shoudlScale = random.nextDouble() < scaleFactor;
    shouldInflate = random.nextDouble() < inflateFactor;
    shouldBeEqual = random.nextDouble() < equalFactor;
  }
}