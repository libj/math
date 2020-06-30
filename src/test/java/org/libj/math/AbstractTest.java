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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import org.libj.lang.Ansi;
import org.libj.lang.Ansi.Color;
import org.libj.lang.Ansi.Intensity;
import org.libj.lang.Strings;
import org.libj.test.TestAide;
import org.libj.util.function.BiIntFunction;
import org.libj.util.function.BiLongFunction;
import org.libj.util.function.BiLongToLongFunction;
import org.libj.util.function.BiObjToLongFunction;
import org.libj.util.function.ObjIntFunction;
import org.libj.util.function.ObjIntToIntFunction;
import org.libj.util.function.ObjLongFunction;
import org.libj.util.function.ObjLongToLongFunction;

import gnu.java.math.BigInt;

public abstract class AbstractTest {
  static final Random random = new Random();
  static final int numTests = 1000000;
  private static final double scaleFactor = 0.2;
  private static final double inflateFactor = 0.2;
  private static final double equalFactor = 0.1;
  private static boolean shoudlScale;
  private static boolean shouldInflate;
  private static boolean shouldBeEqual;

  public static int abs(final int a) {
    return Math.abs(a);
  }

  public static long abs(final long a) {
    return Math.abs(a);
  }

  public static String abs(final String num) {
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

  public long intScale() {
    return 1000000000000000000L;
  }

  public long longScale() {
    return 1000000000000000000L;
  }

  public String stringScale(final String a) {
    return a + a.replace("-", "");
  }

  public static String randomBig(final int len) {
    return randomBig(len, false);
  }

  public static String neg(final String v) {
    return !v.startsWith("-") ? "-" + v : v.substring(1);
  }

  public static String randomBig(final int len, final boolean positive) {
    final int sign = positive ? 0 : random.nextInt(2);
    final char[] num = new char[len + sign];
    if (sign > 0)
      num[0] = '-';

    num[sign] = (char)('1' + random.nextInt(9));
    for (int i = sign + 1; i < len + sign; i++)
      num[i] = (char)('0' + random.nextInt(10));

    return new String(num);
  }

  public static int[] randomInputs(final int[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, random.nextInt());
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = random.nextInt();
    }

    return values;
  }

  public static long[] randomInputs(final long[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, random.nextInt());
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = random.nextInt();
    }

    return values;
  }

  public static String[] randomInputs(final int len, final String[] values) {
    if (shouldBeEqual) {
      Arrays.fill(values, randomBig(random.nextInt(len + 1) + 1, true));
    }
    else {
      for (int i = 0; i < values.length; ++i)
        values[i] = randomBig(random.nextInt(len + 1) + 1, true);
    }

    return values;
  }

  public abstract static class Case<T,I,R,O> {
    private int count = -100;
    final String name;
    final Object test;
    final Function<R,O> out;
    Object previous;

    Case(final String name, final Object test, final Function<R,O> out) {
      this.name = name;
      this.test = test;
      this.out = out;
    }

    abstract void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times);
    abstract <I,O>void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times, final T inputs);

    void verify(final String label, final Case cse, final Object t, final int c, final long time, final Long[] times) {
      final Object o = cse.out != null ? cse.out.apply(t) : t;
      if (previous != null) {
        if (c > 0) {
          if (o instanceof Float) {
            final float delta = Math.ulp((Float)previous);
            assertEquals((Float)previous, (Float)o, delta);
          }
          else if (o instanceof Double) {
            final double delta = Math.ulp((Double)previous);
            assertEquals((Double)previous, (Double)o, delta);
          }
          else if (!previous.equals(o)) {
            final StringBuilder message = new StringBuilder("\n");
            message.append(label).append('\n');
            if (o instanceof Integer)
              message.append(Arrays.toString(BigInt.assign(null, ((Integer)previous).intValue()))).append('\n').append(Arrays.toString(BigInt.assign(null, ((Integer)o).intValue())));
            else if (o instanceof Long)
              message.append(Arrays.toString(BigInt.assign(null, ((Long)previous).longValue()))).append('\n').append(Arrays.toString(BigInt.assign(null, ((Long)o).longValue())));
            else if (o instanceof String)
              message.append(Arrays.toString(BigInt.assign(null, (String)previous))).append('\n').append(((String)o).isEmpty() ? "" : Arrays.toString(BigInt.assign(null, (String)o)));
            else if (!(o instanceof Boolean))
              throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());

            assertEquals(message.append('\n').toString(), previous, o);
          }
        }

        if (++count > 0)
          times[c] += time;
      }

      previous = o;
    }
  }

  public static class IntCase<A,B,R,O> extends Case<int[],Integer,R,O> {
    private static final int[] SPECIAL = {0, -1, 1, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
    private static final int NUM_RANDOM = 4000000;
    private final int[] inputs = {0, 0};

    final IntFunction<A> aToA;
    final Object bToB;

    IntCase(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = null;
    }

    IntCase(final String name, final BiIntFunction<R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = null;
      this.bToB = null;
    }

    IntCase(final String name, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    IntCase(final String name, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    @Override
    final void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(abstractTest, label, cases, times, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        randomInputs(inputs);
        test(abstractTest, label, cases, times, inputs);
        inputs[0] *= -1;
        test(abstractTest, label, cases, times, inputs);
        inputs[1] *= -1;
        test(abstractTest, label, cases, times, inputs);
        inputs[0] *= -1;
        test(abstractTest, label, cases, times, inputs);
      }
    }

    @Override
    <I,O>void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times, final int[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          int a = inputs[0];
          int b = inputs[1];
          final Object t;
          long time;
          final IntCase intCase = (IntCase)cse;
          if (cse.test instanceof BiIntFunction) {
            final BiIntFunction test = (BiIntFunction)cse.test;

            time = System.nanoTime();
            t = test.apply(a, b);
          }
          else {
            final Object a0 = intCase.aToA == null ? Integer.valueOf(a) : intCase.aToA.apply(a);
            if (cse.test instanceof ObjIntFunction) {
              final ObjIntFunction test = (ObjIntFunction)cse.test;
              if (intCase.bToB != null)
                b = ((ObjIntToIntFunction)intCase.bToB).applyAsInt(a0, b);

              time = System.nanoTime();
              t = test.apply(a0, b);
            }
            else {
              final Object b0 = intCase.bToB == null ? b : ((IntFunction)intCase.bToB).apply(b);
              final BiFunction test = (BiFunction)cse.test;

              time = System.nanoTime();
              t = test.apply(a0, b0);
            }
          }

          time = System.nanoTime() - time;
          verify(label, cse, t, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      abstractTest.onSuccess();
    }
  }

  public static class LongCase<A,B,R,O> extends Case<long[],Long,R,O> {
    private static final long[] SPECIAL = {0, -1, 1, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int NUM_RANDOM = 3000000;
    private final long[] inputs = {0, 0};

    final LongFunction<A> aToA;
    final Object bToB;

    LongCase(final String name, final LongFunction<A> aToA, final BiLongToLongFunction test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = null;
    }

    LongCase(final String name, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = null;
    }

    LongCase(final String name, final BiLongFunction<R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = null;
      this.bToB = null;
    }

    LongCase(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    LongCase(final String name, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    @Override
    final void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(abstractTest, label, cases, times, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        randomInputs(inputs);
        test(abstractTest, label, cases, times, inputs);
        inputs[0] *= -1;
        test(abstractTest, label, cases, times, inputs);
        inputs[1] *= -1;
        test(abstractTest, label, cases, times, inputs);
        inputs[0] *= -1;
        test(abstractTest, label, cases, times, inputs);
      }
    }

    @Override
    <I,O>void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times, final long[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          final long a = inputs[0];
          long b = inputs[1];
          final Object t;
          long time;
          final LongCase longCase = (LongCase)cse;
          if (cse.test instanceof BiLongToLongFunction) {
            final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;

            time = System.nanoTime();
            t = test.applyAsLong(a, b);
          }
          else if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;

            time = System.nanoTime();
            t = test.apply(a, b);
          }
          else {
            final Object a0 = longCase.aToA == null ? a : longCase.aToA.apply(a);
            if (cse.test instanceof ObjLongFunction) {
              final ObjLongFunction test = (ObjLongFunction)cse.test;
              if (longCase.bToB != null)
                b = ((ObjLongToLongFunction)longCase.bToB).applyAsLong(a0, b);

              time = System.nanoTime();
              t = test.apply(a0, b);
            }
            else {
              final Object b0 = longCase.bToB == null ? b : ((LongFunction)longCase.bToB).apply(b);
              final BiFunction test = (BiFunction)this.test;

              time = System.nanoTime();
              t = test.apply(a0, b0);
            }
          }

          time = System.nanoTime() - time;
          verify(label, cse, t, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      abstractTest.onSuccess();
    }
  }

  public static class StringCase<A,B,R,O> extends Case<String[],String,R,O> {
    private static final String[] SPECIAL = {"0", "-1", "1", String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE), String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE), String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), String.valueOf(Long.MIN_VALUE), String.valueOf(Long.MAX_VALUE), "-18446744073709551616", "18446744073709551615", "-79228162514264337593543950336", "79228162514264337593543950335", "-340282366920938463463374607431768211456", "340282366920938463463374607431768211455", "-1461501637330902918203684832716283019655932542976", "1461501637330902918203684832716283019655932542975", "-6277101735386680763835789423207666416102355444464034512896", "6277101735386680763835789423207666416102355444464034512895", "-26959946667150639794667015087019630673637144422540572481103610249216", "26959946667150639794667015087019630673637144422540572481103610249215", "-115792089237316195423570985008687907853269984665640564039457584007913129639936", "115792089237316195423570985008687907853269984665640564039457584007913129639935"};
    private static final int NUM_RANDOM = 50000;
    private static final int MAX_LENGTH = 1024;

    private final String[] inputs = {null, null};

    final Function<String,A> aToA;
    final Object bToB;

    StringCase(final String name, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    StringCase(final String name, final Function<String,A> aToA, final BiObjToLongFunction<A,String> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
      this.bToB = bToB;
    }

    @Override
    final void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(abstractTest, label, cases, times, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        randomInputs(i % MAX_LENGTH, inputs);
        test(abstractTest, label, cases, times, inputs);
        inputs[0] = neg(inputs[0]);
        test(abstractTest, label, cases, times, inputs);
        inputs[1] = neg(inputs[1]);
        test(abstractTest, label, cases, times, inputs);
        inputs[0] = neg(inputs[0]);
        test(abstractTest, label, cases, times, inputs);
      }
    }

    @Override
    <I,O>void test(final AbstractTest abstractTest, final String label, final Case[] cases, final Long[] times, final String[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          final String a0 = inputs[0];
          final String b0 = inputs[1];
          final Object t;
          long time;
          final StringCase stringCase = (StringCase)cse;
          final Object a = stringCase.aToA == null ? a0 : stringCase.aToA.apply(a0);
          if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            final long b = ((BiObjToLongFunction)stringCase.bToB).applyAsLong(a, b0);

            time = System.nanoTime();
            t = test.apply(a, b);
          }
          else {
            final BiFunction test = (BiFunction)stringCase.test;
            final Object b = stringCase.bToB == null ? b0 : ((Function)stringCase.bToB).apply(b0);

            time = System.nanoTime();
            t = test.apply(a, b);
          }

          time = System.nanoTime() - time;
          verify(label, cse, t, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      abstractTest.onSuccess();
    }
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public void onSuccess() {
    shoudlScale = random.nextDouble() < scaleFactor;
    shouldInflate = random.nextDouble() < inflateFactor;
    shouldBeEqual = random.nextDouble() < equalFactor;
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, test, out);
  }

  public static <R,O>IntCase<Integer,Integer,R,O> i(final String name, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final BiLongToLongFunction test) {
    return new LongCase<>(name, null, test, null);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(name, aToA, null, test, out);
  }

  public static <R,O>StringCase<String,String,R,O> s(final String name, final BiFunction<String,String,R> test, final Function<R,O> out) {
    return new StringCase<>(name, null, null, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiObjToLongFunction<A,String> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new StringCase<>(name, aToA, bToB, test, out);
  }

  @SafeVarargs
  public final <O>void testRange(final String label, final IntCase<?,?,?,O> ... cases) {
    execTests(label, cases);
  }

  @SafeVarargs
  public final <O>void testRange(final String label, final LongCase<?,?,?,O> ... cases) {
    execTests(label, cases);
  }

  @SafeVarargs
  public final <O>void testRange(final String label, final StringCase<?,?,?,O> ... cases) {
    execTests(label, cases);
  }

  private final <I,O>void execTests(final String label, final Case<?,I,?,O>[] cases) {
    final String[] headings = heading(cases);
    final Long[] times = new Long[cases.length];
    Arrays.fill(times, 0L);
    final Case prototype = cases[0];
    long ts = System.currentTimeMillis();
    prototype.test(this, label, cases, times);
    ts = System.currentTimeMillis() - ts;

    if (prototype.count > 0) {
      times[0] /= prototype.count;
      times[1] /= prototype.count;
    }

    print(label, prototype.count, ts, times, headings);
  }

  private static String[] heading(final Case<?,?,?,?>[] cases) {
    final String[] array = new String[cases.length];
    for (int i = 0; i < array.length; ++i)
      array[i] = cases[i].name;

    return array;
  }

  private static void print(final String label, final int count, final long ts, final Long[] times, final String ... headings) {
    int min = 0;
    for (int i = 0; i < times.length; ++i)
      if (times[i] < times[min])
        min = i;

    final String[] strings = new String[times.length];
    for (int i = 0; i < times.length; ++i) {
      strings[i] = String.valueOf(times[i]);
      if (i == min)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, Color.GREEN);
      else if (i == times.length - 1)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, Color.RED);
    }

    System.out.println(label + "\n  " + count + " in " + ts + "ms\n" + Strings.printTable(true, true, strings, headings));
  }

  private static <T extends Throwable>void checkDebug(final Throwable t) throws T {
    if (!TestAide.isInDebug())
      throw (T)t;

    TestAide.printStackTrace(System.err, t);
    System.console();
  }
}