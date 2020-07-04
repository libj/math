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
import org.libj.util.function.IntToIntFunction;
import org.libj.util.function.LongToLongFunction;
import org.libj.util.function.ObjIntFunction;
import org.libj.util.function.ObjIntToIntFunction;
import org.libj.util.function.ObjLongFunction;
import org.libj.util.function.ObjLongToLongFunction;

import gnu.java.math.BigInt;

public abstract class AbstractTest {
  protected static final Random random = new Random();

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

  public abstract static class Case<T,I,R,O> {
    private int count = -100;
    final String name;
    final Object aToA;
    final Object bToB;
    final Object test;
    final Function<R,O> out;
    Object previous;

    Case(final String name, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      this.name = name;
      this.aToA = aToA;
      this.bToB = bToB;
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
              message.append(Arrays.toString(BigInt.valueOf(((Integer)previous).intValue()))).append('\n').append(Arrays.toString(BigInt.valueOf(((Integer)o).intValue())));
            else if (o instanceof Long)
              message.append(Arrays.toString(BigInt.valueOf(((Long)previous).longValue()))).append('\n').append(Arrays.toString(BigInt.valueOf(((Long)o).longValue())));
            else if (o instanceof String)
              message.append(Arrays.toString(BigInt.valueOf((String)previous))).append('\n').append(((String)o).isEmpty() ? "" : Arrays.toString(BigInt.valueOf((String)o)));
            else if (!(o instanceof Boolean))
              throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());

            assertEquals(cse.name + ": " + message.append('\n').toString(), previous, o);
          }
        }
      }

      times[c] += time;
      previous = o;
    }

    void onSuccess(final AbstractTest abstractTest, final Long[] times) {
      abstractTest.onSuccess();
      if (++count < 0)
        Arrays.fill(times, 0L);
    }
  }

  public static class IntCase<A,B,R,O> extends Case<int[],Integer,R,O> {
    private static final int[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
    private static final int NUM_RANDOM = 4000000;
    private final int[] inputs = {0, 0};

    IntCase(final String name, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, aToA, bToB, test, out);
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
        abstractTest.randomInputs(inputs);
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
          final IntCase castCase = (IntCase)cse;

          int a = inputs[0];
          Object a0 = a;
          if (castCase.aToA instanceof IntToIntFunction)
            a = ((IntToIntFunction)castCase.aToA).applyAsInt(a);
          else if (castCase.aToA instanceof IntFunction)
            a0 = ((IntFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          int b = inputs[1];
          Object b0 = b;
          if (castCase.bToB instanceof IntToIntFunction)
            b = ((IntToIntFunction)castCase.bToB).applyAsInt(b);
          else if (castCase.bToB instanceof ObjIntToIntFunction)
            b = ((ObjIntToIntFunction)castCase.bToB).applyAsInt(a0, b);
          else if (castCase.bToB instanceof IntFunction)
            b0 = ((IntFunction)castCase.bToB).apply(b);
          else if (castCase.bToB instanceof ObjIntFunction)
            b0 = ((ObjIntFunction)castCase.bToB).apply(a0, b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          final Object result;
          long time;
          if (cse.test instanceof BiIntFunction) {
            final BiIntFunction test = (BiIntFunction)cse.test;

            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof ObjIntFunction) {
            final ObjIntFunction test = (ObjIntFunction)cse.test;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else {
            final BiFunction test = (BiFunction)cse.test;

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, result, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(abstractTest, times);
    }
  }

  public static class LongCase<A,B,R,O> extends Case<long[],Long,R,O> {
    private static final long[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int NUM_RANDOM = 3000000;
    private final long[] inputs = {0, 0};

    LongCase(final String name, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, aToA, bToB, test, out);
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
        abstractTest.randomInputs(inputs);
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
          final LongCase castCase = (LongCase)cse;

          long a = inputs[0];
          Object a0 = a;
          if (castCase.aToA instanceof LongToLongFunction)
            a = ((LongToLongFunction)castCase.aToA).applyAsLong(a);
          else if (castCase.aToA instanceof LongFunction)
            a0 = ((LongFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          long b = inputs[1];
          Object b0 = b;
          if (castCase.bToB instanceof ObjLongToLongFunction)
            b = ((ObjLongToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB instanceof LongToLongFunction)
            b = ((LongToLongFunction)castCase.bToB).applyAsLong(b);
          else if (castCase.bToB instanceof LongFunction)
            b0 = ((LongFunction)castCase.bToB).apply(b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          final Object result;
          long time;
          if (cse.test instanceof BiLongToLongFunction) {
            final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;

            time = System.nanoTime();
            result = test.applyAsLong(a, b);
          }
          else if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;

            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else {
            final BiFunction test = (BiFunction)this.test;

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, result, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(abstractTest, times);
    }
  }

  public static class StringCase<A,B,R,O> extends Case<String[],String,R,O> {
    private static final String[] SPECIAL = {"0", "-1", "1", String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE), String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE), String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), String.valueOf(Long.MIN_VALUE), String.valueOf(Long.MAX_VALUE), "-18446744073709551616", "18446744073709551615", "-79228162514264337593543950336", "79228162514264337593543950335", "-340282366920938463463374607431768211456", "340282366920938463463374607431768211455", "-1461501637330902918203684832716283019655932542976", "1461501637330902918203684832716283019655932542975", "-6277101735386680763835789423207666416102355444464034512896", "6277101735386680763835789423207666416102355444464034512895", "-26959946667150639794667015087019630673637144422540572481103610249216", "26959946667150639794667015087019630673637144422540572481103610249215", "-115792089237316195423570985008687907853269984665640564039457584007913129639936", "115792089237316195423570985008687907853269984665640564039457584007913129639935"};
    private static final int NUM_RANDOM = 50000;
    private static final int MAX_LENGTH = 1024;

    private final String[] inputs = {null, null};

    StringCase(final String name, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, aToA, bToB, test, out);
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
        abstractTest.randomInputs(i % MAX_LENGTH, inputs);
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
          final StringCase castCase = (StringCase)cse;

          final String a = inputs[0];
          Object a0 = a;
          if (castCase.aToA instanceof Function)
            a0 = ((Function)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          final String b = inputs[1];
          Object b0 = b;
          long b1 = Long.MIN_VALUE;

          if (castCase.bToB instanceof Function)
            b0 = ((Function)castCase.bToB).apply(b);
          else if (castCase.bToB instanceof BiObjToLongFunction)
            b1 = ((BiObjToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          final Object t;
          long time;
          if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;

            time = System.nanoTime();
            t = test.apply(a0, b1);
          }
          else {
            final BiFunction test = (BiFunction)castCase.test;

            time = System.nanoTime();
            t = test.apply(a0, b0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, t, c, time, times);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(abstractTest, times);
    }
  }


  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  protected abstract String[] randomInputs(int i, String[] inputs);
  protected abstract long[] randomInputs(long[] inputs);
  protected abstract int[] randomInputs(int[] inputs);
  protected abstract void onSuccess();

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, null, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final IntToIntFunction bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntToIntFunction aToA, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, null, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntToIntFunction aToA, final IntToIntFunction test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, null, test, out);
  }

  public static <R,O>IntCase<Integer,Integer,R,O> i(final String name, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, null, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, null, null, test, out);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final LongToLongFunction aToA, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongToLongFunction bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongToLongFunction aToA, final BiLongToLongFunction test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongToLongFunction aToA, final BiLongToLongFunction test) {
    return new LongCase<>(name, aToA, null, test, null);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final BiLongToLongFunction test) {
    return new LongCase<>(name, null, null, test, null);
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

    if (prototype.count > 0)
      for (int i = 0; i < cases.length; ++i)
        times[i] /= prototype.count;

    print(label, prototype.count, ts, times, headings);
  }

  private static String[] heading(final Case<?,?,?,?>[] cases) {
    final String[] array = new String[cases.length];
    for (int i = 0; i < array.length; ++i)
      array[i] = cases[i].name;

    return array;
  }

  private static void print(final String label, final int count, final long ts, final Long[] times, final String ... headings) {
    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;
    for (int i = 0; i < times.length; ++i) {
      min = Math.min(min, times[i]);
      max = Math.max(max, times[i]);
    }

    final String[] strings = new String[times.length];
    for (int i = 0; i < times.length; ++i) {
      strings[i] = String.valueOf(times[i]);
      if (times[i] == min)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, Color.GREEN);
      else if (times.length > 2 ? i >= times.length - 2 : i >= times.length - 1)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, times[i] == max ? Color.RED : Color.YELLOW);
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