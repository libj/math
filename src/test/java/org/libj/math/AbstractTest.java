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
import org.libj.util.function.BiLongToLongFunction;
import org.libj.util.function.BiObjToLongFunction;
import org.libj.util.function.ObjIntFunction;
import org.libj.util.function.ObjIntToIntFunction;
import org.libj.util.function.ObjLongFunction;
import org.libj.util.function.ObjLongToLongFunction;

public abstract class AbstractTest {
  static final Random random = new Random();
  static final int numTests = 1000000;
  private static final double specialFactor = 0.02;

  private static final String[] specialStrings = {"0", String.valueOf(Integer.MIN_VALUE), String.valueOf(Long.MIN_VALUE)};
  private static final long[] specialLongs = {0, Integer.MIN_VALUE, Long.MIN_VALUE};

  public static String abs(final String num) {
    return num.startsWith("-") ? num.substring(1) : num;
  }

  private static String randomBig(final int len) {
    if (random.nextDouble() < specialFactor)
      return specialStrings[random.nextInt(3)];

    final int sign = random.nextInt(2);
    final char[] num = new char[len + sign];
    if (sign > 0)
      num[0] = '-';

    num[sign] = (char)('1' + random.nextInt(9));
    for (int i = sign + 1; i < len + sign; i++)
      num[i] = (char)('0' + random.nextInt(10));

    return new String(num);
  }

  public double rangeCoverage() {
    return 0.0000000001;
  }

  @FunctionalInterface
  public interface LongIntToLongFunction {
    /**
     * Applies this function to the given arguments.
     *
     * @param t The first function argument.
     * @param u The second function argument.
     * @return The function result.
     */
    long applyAsLong(long t, int u);
  }

  public abstract static class Case<I,R,O> {
    final String name;
    final Object test;
    final Function<R,O> out;

    Case(final String name, final Object test, final Function<R,O> out) {
      this.name = name;
      this.test = test;
      this.out = out;
    }
  }

  public static class IntCase<A,B,R,O> extends Case<Integer,R,O> {
    final IntFunction<A> aToA;
    final Object bToB;

    IntCase(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
      super(name, test, out);
      this.aToA = aToA;
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
  }

  public static class LongCase<A,B,R,O> extends Case<Long,R,O> {
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
  }

  public static class StringCase<A,B,R,O> extends Case<String,R,O> {
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
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, aToA, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new LongCase<>(name, aToA, bToB, test, out);
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

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiObjToLongFunction<A,String> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new StringCase<>(name, aToA, bToB, test, out);
  }

  @SafeVarargs
  public final <O>void testRange(final IntCase<?,?,?,O> ... cases) {
    execTests(IntCase.class, cases);
  }

  @SafeVarargs
  public final <O>void testRange(final LongCase<?,?,?,O> ... cases) {
    execTests(LongCase.class, cases);
  }

  @SafeVarargs
  public final <O>void testRange(final StringCase<?,?,?,O> ... cases) {
    execTests(StringCase.class, cases);
  }

  private final <I,O>void execTests(final Class<? extends Case> cls, final Case<I,?,O>[] cases) {
    final Long[] times = new Long[cases.length];
    Arrays.fill(times, 0L);
    final String[] headings = heading(cases);
    final double skipFactor = 1000000D / rangeCoverage();

    int count = -100;
    final long ts = System.currentTimeMillis();
    if (cls == StringCase.class) {
      final long tests = (long)(100000000000000D * rangeCoverage());
      for (long i = 0; i < tests; ++i) {
        final int len = (int)(i % 4095 + 1);
        final String a = randomBig(len);
        final String b = randomBig(len);
        execTests(a, b, count, times, cases);
        ++count;
      }
    }
    else {
      for (long a = Long.MIN_VALUE, i = -1; i < 2 || (i = -1) == 0; a += (long)(random.nextDouble() * skipFactor), i += (a < 0 == i < 0 ? 0 : 2)) {
        for (long b = Long.MIN_VALUE, j = -1; j < 2 || (j = -1) == 0; b += (long)(random.nextDouble() * skipFactor), j += (b < 0 == j < 0 ? 0 : 2)) {
          execTests(random.nextDouble() < specialFactor ? specialLongs[random.nextInt(3)] : a, random.nextDouble() < specialFactor ? specialLongs[random.nextInt(3)] : b, count, times, cases);
          ++count;
        }
      }
    }

    times[0] /= count;
    times[1] /= count;

    print(count, ts, times, headings);
  }

  private static void print(final int count, final long ts, final Long[] times, final String ... headings) {
    int min = 0;
    for (int i = 0; i < times.length; ++i)
      if (times[i] < times[min])
        min = i;

    final String[] strings = new String[times.length];
    for (int i = 0; i < times.length; ++i) {
      strings[i] = String.valueOf(times[i]);
      if (i == min)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, Color.GREEN);
      else if (i == 0)
        strings[i] = Ansi.apply(strings[i], Intensity.BOLD, Color.RED);
    }

    System.out.println(Thread.currentThread().getStackTrace()[4].getMethodName() + "()\n  " + count + " in " + (System.currentTimeMillis() - ts) + "ms\n" + Strings.printTable(true, true, strings, headings));
  }

  private static <I,O>void execTests(final Object a, final Object b, final int count, final Long[] times, final Case<I,?,O>[] cases) {
    O previous = null;
    for (int c = 0; c < cases.length; ++c) {
      final Case<?,Object,O> cse = (Case<I,Object,O>)cases[c];
      try {
        previous = execTest(cse, a, b, count, c, times, previous);
      }
      catch (final Throwable t) {
        if (TestAide.isInDebug()) {
          c = -1;
          continue;
        }

        throw t;
      }
    }
  }

  private static <I,O>O execTest(final Case<I,Object,O> cse, final Object a0, Object b0, final int count, final int c, final Long[] times, final O previous) {
    final Object t;
    long time;
    if (cse instanceof IntCase) {
      final IntCase intCase = (IntCase)cse;
      final Object a = intCase.aToA.apply(((Long)a0).intValue());
      int b1 = ((Long)b0).intValue();
      if (cse.test instanceof ObjIntFunction) {
        final ObjIntFunction test = (ObjIntFunction)cse.test;
        if (intCase.bToB != null)
          b1 = ((ObjIntToIntFunction)intCase.bToB).applyAsInt(a, b1);

        time = System.nanoTime();
        t = test.apply(a, b1);
      }
      else {
        final Object b = intCase.bToB == null ? b1 : ((IntFunction)intCase.bToB).apply(b1);
        final BiFunction test = (BiFunction)cse.test;

        time = System.nanoTime();
        t = test.apply(a, b);
      }
    }
    else if (cse instanceof LongCase) {
      final LongCase longCase = (LongCase)cse;
      long b1 = ((Long)b0).longValue();
      final Object a = longCase.aToA == null ? (Long)a0 : longCase.aToA.apply(((Long)a0).longValue());
      if (cse.test instanceof BiLongToLongFunction) {
        final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;
        final long a1 = ((Long)a0).longValue();

        time = System.nanoTime();
        t = test.applyAsLong(a1, b1);
      }
      else if (cse.test instanceof ObjLongFunction) {
        final ObjLongFunction test = (ObjLongFunction)cse.test;
        if (longCase.bToB != null)
          b1 = ((ObjLongToLongFunction)longCase.bToB).applyAsLong(a, b1);

        time = System.nanoTime();
        t = test.apply(a, b1);
      }
      else {
        final Object b = longCase.bToB == null ? b1 : ((LongFunction)longCase.bToB).apply(b1);
        final BiFunction test = (BiFunction)cse.test;

        time = System.nanoTime();
        t = test.apply(a, b);
      }
    }
    else if (cse instanceof StringCase) {
      final StringCase stringCase = (StringCase)cse;
      final Object a = stringCase.aToA.apply(a0);
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
    }
    else {
      throw new UnsupportedOperationException();
    }

    time = System.nanoTime() - time;

    final O o = cse.out != null ? cse.out.apply(t) : (O)t;
    if (c > 0) {
      if (o instanceof Float) {
        final float delta = Math.ulp((Float)previous);
        assertEquals((Float)previous, (Float)o, delta);
      }
      else if (o instanceof Double) {
        final double delta = Math.ulp((Double)previous);
        assertEquals((Double)previous, (Double)o, delta);
      }
      else {
        assertEquals(previous, o);
      }
    }

    if (count > 0)
      times[c] += time;

    return o;
  }

  private static String[] heading(final Case<?,?,?>[] cases) {
    final String[] array = new String[cases.length];
    for (int i = 0; i < array.length; ++i)
      array[i] = cases[i].name;

    return array;
  }
}