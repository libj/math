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

package org.libj.math.survey;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import org.huldra.math.BigInt;
import org.junit.internal.ArrayComparisonFailure;
import org.libj.lang.Numbers;
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

public abstract class CaseTest {
  protected static final int numTests = System.getProperty("fast") != null ? 1000 : 1000000;

  public static String neg(final String v) {
    return !v.startsWith("-") ? "-" + v : v.substring(1);
  }

  private final int[] scaleFactorFactors = {1, 1, 1};

  @SuppressWarnings("rawtypes")
  protected void setScaleFactorFactor(final Class<? extends Case> cls, final int scaleFactorFactor) {
    if (cls == IntCase.class)
      this.scaleFactorFactors[0] = scaleFactorFactor;
    else if (cls == LongCase.class)
      this.scaleFactorFactors[1] = scaleFactorFactor;
    else if (cls == StringCase.class)
      this.scaleFactorFactors[2] = scaleFactorFactor;
    else
      throw new UnsupportedOperationException("Unsupported type: " + cls.getName());
  }

  @SuppressWarnings("rawtypes")
  protected int getScaleFactorFactor(final Class<? extends Case> cls) {
    if (cls == IntCase.class)
      return this.scaleFactorFactors[0];

    if (cls == LongCase.class)
      return this.scaleFactorFactors[1];

    if (cls == StringCase.class)
      return this.scaleFactorFactors[2];

    throw new UnsupportedOperationException("Unsupported type: " + cls.getName());
  }

  public abstract static class Case<T,I,R,O> {
    private int count = -100;
    final String name;
    final Object aToA;
    final Object bToB;
    final Object test;
    final Function<R,O> out;
    Object previous;

    private final int variables;

    Case(final String name, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      this.variables = variables;
      this.name = name;
      this.aToA = aToA;
      this.bToB = bToB;
      this.test = test;
      this.out = out;
    }

    int variables() {
      return variables;
    }

    abstract int maxPrecision(int variable);
    abstract void test(final CaseTest caseTest, final String label, final Case<?,I,?,O>[] cases, final Supplier<Surveys> surveys);
    abstract <I,O>void test(final CaseTest caseTest, final String label, final Case<?,I,?,O>[] cases, final Supplier<Surveys> surveys, final T inputs);

    <I,O>void verify(final String label, final Case<?,I,Object,O> cse, final Object in1, final Object in2, final Object result, final int c, final long time, final Supplier<Surveys> surveys) {
      final Object o = cse.out != null ? cse.out.apply(result) : result;
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
          else {
            if (o instanceof byte[]) {
              if (!Arrays.equals((byte[])previous, (byte[])o)) {
                final StringBuilder message = new StringBuilder("\n");
                message.append(label).append('\n');
                message.append(Arrays.toString((byte[])previous)).append('\n').append(Arrays.toString((byte[])o));
                try {
                  assertArrayEquals(cse.name + ": " + message.append('\n').toString(), (byte[])previous, (byte[])o);
                }
                catch (final ArrayComparisonFailure e) {
                  // For some reason, throwing ArrayComparisonFailure causes Eclipse to
                  // fail with: java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding"
                  final AssertionError assertionError = new AssertionError(e.getMessage());
                  assertionError.setStackTrace(e.getStackTrace());
                  throw assertionError;
                }
              }
            }
            else if (!previous.equals(o)) {
              final StringBuilder message = new StringBuilder("\n");
              message.append(label).append('\n');
              if (o instanceof Byte || o instanceof Short || o instanceof Integer)
                message.append(Arrays.toString(BigInt.valueOf(((Number)previous).intValue()))).append('\n').append(Arrays.toString(BigInt.valueOf(((Number)o).intValue())));
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
      }

      surveys.get().addTime(c, 0, in1, time);
      if (cse.variables() == 2)
        surveys.get().addTime(c, 1, in2, time);

      previous = o;
    }

    void onSuccess(final CaseTest caseTest, final Supplier<Surveys> surveys) {
      caseTest.onSuccess();
      if (++count < 0)
        surveys.get().reset();
    }
  }

  public static class IntCase<A,B,R,O> extends Case<int[],Integer,R,O> {
    private static final int[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
    private static final int NUM_RANDOM = 4 * numTests;
    private final int[] inputs = {0, 0};

    IntCase(final String name, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision(final int variable) {
      return (variable == 0 ? scaleFactorFactorA : scaleFactorFactorB) * 10;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,Integer,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        caseTest.randomInputs(inputs);
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] *= -1;
        test(caseTest, label, cases, surveys, inputs);
        inputs[1] *= -1;
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] *= -1;
        test(caseTest, label, cases, surveys, inputs);
      }
    }

    private int scaleFactorFactorA;
    private int scaleFactorFactorB;

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,I,?,O>[] cases, final Supplier<Surveys> surveys, final int[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          final IntCase castCase = (IntCase)cse;

          Object in1, in2 = null;
          int a = inputs[0];
          Object a0 = a;

          caseTest.setScaleFactorFactor(IntCase.class, 1);

          if (castCase.aToA instanceof IntToIntFunction)
            a = ((IntToIntFunction)castCase.aToA).applyAsInt(a);
          else if (castCase.aToA instanceof IntFunction)
            a0 = ((IntFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          scaleFactorFactorA = caseTest.getScaleFactorFactor(IntCase.class);

          int b = inputs[1];
          Object b0 = b;

          caseTest.setScaleFactorFactor(IntCase.class, 1);

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

          scaleFactorFactorB = caseTest.getScaleFactorFactor(IntCase.class);

          final Object result;
          long time;
          if (cse.test instanceof BiIntFunction) {
            final BiIntFunction test = (BiIntFunction)cse.test;
            in1 = a;
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof IntFunction) {
            final IntFunction test = (IntFunction)cse.test;
            in1 = a;

            time = System.nanoTime();
            result = test.apply(a);
          }
          else if (cse.test instanceof ObjIntFunction) {
            final ObjIntFunction test = (ObjIntFunction)cse.test;
            in1 = a0;
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = a0;
            in2 = b0;

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = a0;

            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, in1, in2, result, c, time, surveys);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys);
    }
  }

  public static class LongCase<A,B,R,O> extends Case<long[],Long,R,O> {
    private static final long[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int NUM_RANDOM = 3 * numTests;
    private final long[] inputs = {0, 0};

    LongCase(final String name, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision(final int variable) {
      return (variable == 0 ? scaleFactorFactorA : scaleFactorFactorB) * 19;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,Long,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        caseTest.randomInputs(inputs);
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] *= -1;
        test(caseTest, label, cases, surveys, inputs);
        inputs[1] *= -1;
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] *= -1;
        test(caseTest, label, cases, surveys, inputs);
      }
    }

    private int scaleFactorFactorA;
    private int scaleFactorFactorB;

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,I,?,O>[] cases, final Supplier<Surveys> surveys, final long[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          final LongCase castCase = (LongCase)cse;

          Object in1, in2 = null;
          long a = inputs[0];
          Object a0 = a;

          caseTest.setScaleFactorFactor(LongCase.class, 1);

          if (castCase.aToA instanceof LongToLongFunction)
            a = ((LongToLongFunction)castCase.aToA).applyAsLong(a);
          else if (castCase.aToA instanceof LongFunction)
            a0 = ((LongFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          scaleFactorFactorA = caseTest.getScaleFactorFactor(LongCase.class);

          long b = inputs[1];
          Object b0 = b;

          caseTest.setScaleFactorFactor(LongCase.class, 1);

          if (castCase.bToB instanceof ObjLongToLongFunction)
            b = ((ObjLongToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB instanceof LongToLongFunction)
            b = ((LongToLongFunction)castCase.bToB).applyAsLong(b);
          else if (castCase.bToB instanceof LongFunction)
            b0 = ((LongFunction)castCase.bToB).apply(b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          scaleFactorFactorB = caseTest.getScaleFactorFactor(LongCase.class);

          final Object result;
          long time;
          if (cse.test instanceof BiLongToLongFunction) {
            final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;
            in1 = a;
            in2 = b;

            time = System.nanoTime();
            result = test.applyAsLong(a, b);
          }
          else if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;
            in1 = a;
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof LongFunction) {
            final LongFunction test = (LongFunction)cse.test;
            in1 = a;

            time = System.nanoTime();
            result = test.apply(a);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = a0;
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = a0;
            in2 = b0;

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;
            in1 = a;
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = a0;

            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, in1, in2, result, c, time, surveys);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys);
    }
  }

  public static class StringCase<A,B,R,O> extends Case<String[],String,R,O> {
    private static final String[] SPECIAL = {"0", "-1", "1", String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE), String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE), String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), String.valueOf(Long.MIN_VALUE), String.valueOf(Long.MAX_VALUE), "-18446744073709551616", "18446744073709551615", "-79228162514264337593543950336", "79228162514264337593543950335", "-340282366920938463463374607431768211456", "340282366920938463463374607431768211455", "-1461501637330902918203684832716283019655932542976", "1461501637330902918203684832716283019655932542975", "-6277101735386680763835789423207666416102355444464034512896", "6277101735386680763835789423207666416102355444464034512895", "-26959946667150639794667015087019630673637144422540572481103610249216", "26959946667150639794667015087019630673637144422540572481103610249215", "-115792089237316195423570985008687907853269984665640564039457584007913129639936", "115792089237316195423570985008687907853269984665640564039457584007913129639935"};
    private static final int NUM_RANDOM = 5 * numTests / 100;
    private static final int MAX_LENGTH = 1024;

    private final String[] inputs = {null, null};

    StringCase(final String name, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(name, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision(final int variable) {
      return (variable == 0 ? scaleFactorFactorA : scaleFactorFactorB) * MAX_LENGTH;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,String,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[0] = SPECIAL[i];
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; ++i) {
        caseTest.randomInputs(i % (MAX_LENGTH + 1), inputs);
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] = neg(inputs[0]);
        test(caseTest, label, cases, surveys, inputs);
        inputs[1] = neg(inputs[1]);
        test(caseTest, label, cases, surveys, inputs);
        inputs[0] = neg(inputs[0]);
        test(caseTest, label, cases, surveys, inputs);
      }
    }

    private int scaleFactorFactorA;
    private int scaleFactorFactorB;

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,I,?,O>[] cases, final Supplier<Surveys> surveys, final String[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,I,Object,O> cse = (Case<?,I,Object,O>)cases[c];
          final StringCase castCase = (StringCase)cse;

          Object in1, in2 = null;
          final String a = inputs[0];
          Object a0 = a;

          caseTest.setScaleFactorFactor(StringCase.class, 1);

          if (castCase.aToA instanceof Function)
            a0 = ((Function)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          scaleFactorFactorA = caseTest.getScaleFactorFactor(StringCase.class);

          final String b = inputs[1];
          Object b0 = b;
          long b1 = Long.MIN_VALUE;

          caseTest.setScaleFactorFactor(StringCase.class, 1);

          if (castCase.bToB instanceof Function)
            b0 = ((Function)castCase.bToB).apply(b);
          else if (castCase.bToB instanceof BiObjToLongFunction)
            b1 = ((BiObjToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          scaleFactorFactorB = caseTest.getScaleFactorFactor(StringCase.class);

          final Object result;
          long time;
          if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = a0;
            in2 = b1;

            time = System.nanoTime();
            result = test.apply(a0, b1);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)castCase.test;
            in1 = a0;
            in2 = b0;

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)castCase.test;
            in1 = a0;

            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time;
          verify(label, cse, in1, in2, result, c, time, surveys);
        }
        catch (final Throwable t) {
          checkDebug(t);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys);
    }
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntFunction<A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, 1, aToA, null, test, out);
  }

  public static <A,R,O>IntCase<A,?,R,O> i(final String name, final IntFunction<A> aToA, final IntToIntFunction bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntToIntFunction aToA, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>IntCase<A,B,R,O> i(final String name, final IntToIntFunction aToA, final IntToIntFunction test, final Function<R,O> out) {
    return new IntCase<>(name, 1, aToA, null, test, out);
  }

  public static <R,O>IntCase<Integer,Integer,R,O> i(final String name, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, 2, null, null, test, out);
  }

  public static <R,O>IntCase<Integer,Integer,R,O> i(final String name, final IntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(name, 1, null, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongToLongFunction aToA, final LongToLongFunction bToB, final BiLongFunction<R> test) {
    return new LongCase<>(name, 2, aToA, bToB, test, null);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, null, null, test, out);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final LongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, 1, null, null, test, out);
  }

  public static <R,O>LongCase<Long,Long,R,O> l(final String name, final LongToLongFunction aToA, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongFunction<A> aToA, final LongToLongFunction bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongToLongFunction aToA, final BiLongToLongFunction test, final Function<R,O> out) {
    return new LongCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final LongToLongFunction aToA, final BiLongToLongFunction test) {
    return new LongCase<>(name, 2, aToA, null, test, null);
  }

  public static <A,B,R,O>LongCase<A,B,R,O> l(final String name, final BiLongToLongFunction test) {
    return new LongCase<>(name, 2, null, null, test, null);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(name, 2, aToA, bToB, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test) {
    return new StringCase<>(name, 2, aToA, bToB, test, null);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(name, 2, aToA, null, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new StringCase<>(name, 1, aToA, null, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiFunction<A,B,R> test) {
    return new StringCase<>(name, 2, aToA, null, test, null);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final Function<A,R> test) {
    return new StringCase<>(name, 1, aToA, null, test, null);
  }

  public static <R,O>StringCase<String,String,R,O> s(final String name, final BiFunction<String,String,R> test, final Function<R,O> out) {
    return new StringCase<>(name, 2, null, null, test, out);
  }

  public static <A,B,R,O>StringCase<A,B,R,O> s(final String name, final Function<String,A> aToA, final BiObjToLongFunction<A,String> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new StringCase<>(name, 2, aToA, bToB, test, out);
  }

  protected abstract String[] randomInputs(int i, String[] inputs);
  protected abstract long[] randomInputs(long[] inputs);
  protected abstract int[] randomInputs(int[] inputs);
  protected abstract void onSuccess();

  @SafeVarargs
  public final <O>void test(final String label, final IntCase<?,?,?,O> ... cases) {
    exec(label, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final LongCase<?,?,?,O> ... cases) {
    exec(label, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final StringCase<?,?,?,O> ... cases) {
    exec(label, cases);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private final <I,O>void exec(final String label, final Case<?,I,?,O>[] cases) {
    final Case prototype = cases[0];
    final int divisions = 11;
    final int variables = prototype.variables();

    final String[] headings = heading(cases);

    long ts = System.currentTimeMillis();
    prototype.test(this, label, cases, () -> {
      if (CaseTest.this.surveys != null)
        return CaseTest.this.surveys;

      return CaseTest.this.surveys = new Surveys(cases.length, variables, divisions) {
        @Override
        public String key(final int variable, final int division) {
          final int maxPrec = prototype.maxPrecision(variable);
          final double width = 2d * maxPrec / (divisions - 1);
          return Strings.padLeft(String.valueOf((int)(division * width - maxPrec)), Numbers.precision(maxPrec) + 1);
        }

        @Override
        public int getDivision(final int variable, final Object obj) {
          if (obj instanceof Boolean)
            return ((Boolean)obj).booleanValue() ? 0 : 1;

          final int maxPrec = prototype.maxPrecision(variable);
          final double width = 2d * maxPrec / (divisions - 1);
          if (obj instanceof Integer) {
            final int val = (Integer)obj;
            final int dig = Numbers.precision(val);
            return (int)((maxPrec + (val < 0 ? -dig : dig)) / width);
          }

          if (obj instanceof Long) {
            final long val = (Long)obj;
            final int dig = Numbers.precision(val);
            return (int)((maxPrec + (val < 0 ? -dig : dig)) / width);
          }

          final int division;
          if (obj instanceof BigInteger) {
            final BigInteger val = (BigInteger)obj;
            final int dig = Numbers.precision(val) * val.signum();
            division = (int)((maxPrec + dig) / width);
          }
          else if (obj instanceof BigInt) {
            final BigInt val = (BigInt)obj;
            final int dig = val.precision() * val.signum();
            division = (int)((maxPrec + dig) / width);
          }
          else if (obj instanceof int[]) {
            final int[] val = (int[])obj;
            final int len = Math.abs(val[0]);
            // Is this a BigInt value array?
            if (len != 0 && len >= val.length)
              throw new IllegalArgumentException("Expected BigInt value array: " + Arrays.toString(val));

            final int dig = BigInt.precision(val) * BigInt.signum(val);
            division = (int)((maxPrec + dig) / width);
          }
          else if (obj instanceof byte[]) {
            final byte[] val = (byte[])obj;
            // FIXME: This is incorrect!
            final int bits = (val.length - 1) * 8;
            final int dig = (int)Math.floor(Math.log10((1L << bits) - 1));
            division = (int)((maxPrec + dig) / width);
          }
          else if (obj instanceof String) {
            final String val = (String)obj;
            final int dig = val.startsWith("-") ? 1 - val.length() : val.length();
            division = (int)((maxPrec + dig) / width);
          }
          else {
            throw new UnsupportedOperationException(obj.getClass().getName());
          }

          return Math.min(Math.max(0, division), divisions - 1);
        }
      };
    });

    ts = System.currentTimeMillis() - ts;
    surveys.print(label, prototype.count, ts, headings);
  }

  private Surveys surveys;

  private static String[] heading(final Case<?,?,?,?>[] cases) {
    final String[] heading = new String[cases.length];
    for (int i = 0; i < heading.length; ++i)
      heading[i] = cases[i].name;

    return heading;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable>void checkDebug(final Throwable t) throws T {
    TestAide.printStackTrace(System.err, t);
    if (!TestAide.isInDebug())
      throw (T)t;

    // TODO: Place breakpoint here to debug...
    System.console();
  }
}