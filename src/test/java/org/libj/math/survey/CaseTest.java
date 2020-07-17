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
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import org.junit.internal.ArrayComparisonFailure;
import org.libj.lang.Numbers;
import org.libj.math.BigInt;
import org.libj.test.TestAide;
import org.libj.util.function.BiIntFunction;
import org.libj.util.function.BiLongFunction;
import org.libj.util.function.BiLongToLongFunction;
import org.libj.util.function.BiObjToIntFunction;
import org.libj.util.function.BiObjToLongFunction;
import org.libj.util.function.IntToIntFunction;
import org.libj.util.function.LongToLongFunction;
import org.libj.util.function.ObjIntFunction;
import org.libj.util.function.ObjIntToIntFunction;
import org.libj.util.function.ObjLongFunction;
import org.libj.util.function.ObjLongToLongFunction;

public abstract class CaseTest {
  protected static final Random random = new Random();
  protected static final int skip = 17; // 0 is "don't skip (full range)", n is "skip each n"
  protected static final int numTests = 1024;
  private static final int warmup = 100;

  @SuppressWarnings("rawtypes")
  private static int getCaseIndex(final Class<? extends Case> cls) {
    if (cls == IntCase.class)
      return 0;

    if (cls == LongCase.class)
      return 1;

    if (cls == StringCase.class)
      return 2;

    throw new UnsupportedOperationException("Unsupported type: " + cls.getName());
  }

  private boolean initialized;
  private final int[] scaleFactorFactors = {1, 1, 1};

  public boolean initialized() {
    return initialized;
  }

  @SuppressWarnings("rawtypes")
  protected void setScaleFactorFactor(final Class<? extends Case> cls, final int scaleFactorFactor) {
    this.scaleFactorFactors[getCaseIndex(cls)] = scaleFactorFactor;
  }

  @SuppressWarnings("rawtypes")
  protected int getScaleFactorFactor(final Class<? extends Case> cls) {
    return this.scaleFactorFactors[getCaseIndex(cls)];
  }

  public static String neg(final String v) {
    return !v.startsWith("-") ? "-" + v : v.substring(1);
  }

  public abstract static class Case<S,T,I,R,O> {
    final S subject;
    private final int variables;
    final Object aToA;
    final Object bToB;
    final Object test;
    final Function<R,O> out;
    Object previous;

    int scaleFactorFactorA;
    int scaleFactorFactorB;

    Case(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      this.variables = variables;
      this.subject = subject;
      this.aToA = aToA;
      this.bToB = bToB;
      this.test = test;
      this.out = out;
    }

    int variables() {
      return variables;
    }

    abstract int maxPrecision();

    final int maxPrecision(final int variable) {
      return (variable == 0 ? scaleFactorFactorA : scaleFactorFactorB) * maxPrecision();
    }

    abstract void test(final CaseTest caseTest, final String label, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys);
    abstract <I,O>void test(final CaseTest caseTest, final String label, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final T inputs);

    <I,O>void verify(final String label, final Case<?,?,I,Object,O> cse, final Object in1, final Object in2, final Object result, final int c, final long time, final Supplier<Surveys> surveys) {
//      if (time > 2000 && (in1 instanceof BigInt) && String.valueOf(in2).length() == 4) {
//        System.err.println(in1 + " " + in2);
//      }

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
                  assertArrayEquals((cse.subject instanceof Class ? ((Class<?>)cse.subject).getSimpleName() : cse.subject) + ": " + message.append('\n').toString(), (byte[])previous, (byte[])o);
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
                message.append(Arrays.toString(BigInt.valueOf((String)previous))).append('\n').append(((String)o).isEmpty() ? "" : bigIntToStringSafe((String)o));
              else if (!(o instanceof Boolean))
                throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());

              assertEquals((cse.subject instanceof Class ? ((Class<?>)cse.subject).getSimpleName() : cse.subject) + ": " + message.append('\n').toString(), previous, o);
            }
          }
        }
      }

      surveys.get().addSample(c, 0, in1, time);
      if (cse.variables() == 2)
        surveys.get().addSample(c, 1, in2, time);

      previous = o;
    }

    void onSuccess(final CaseTest caseTest, final Surveys surveys) {
      caseTest.onSuccess();
      surveys.onSuccess();
    }
  }

  private static String bigIntToStringSafe(final String str) {
    try {
      return Arrays.toString(BigInt.valueOf(str));
    }
    catch (final ArrayIndexOutOfBoundsException e) {
      return str;
    }
  }

  public static class IntCase<S,A,B,R,O> extends Case<S,int[],Integer,R,O> {
    private static final int[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE + 1, Short.MIN_VALUE, Short.MAX_VALUE + 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
    private static final int NUM_RANDOM = 9 * numTests;
    private final int[] inputs = {0, 0};
    private static final int MAX_PRECISION = 10;

    IntCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,?,Integer,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        inputs[0] = SPECIAL[i];
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; i += (random.nextInt(skip) + 1)) {
        for (int j = 0; j < NUM_RANDOM; j += (random.nextInt(skip) + 1)) {
          caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] *= -1;
          test(caseTest, label, cases, surveys, inputs);
          inputs[1] *= -1;
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] *= -1;
          test(caseTest, label, cases, surveys, inputs);
        }
      }
    }

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final int[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,?,I,Object,O> cse = (Case<?,?,I,Object,O>)cases[c];
          final IntCase castCase = (IntCase)cse;

          Object in1, in2 = null;
          int a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactorFactor(IntCase.class, 1);

          if (castCase.aToA instanceof IntToIntFunction)
            a = ((IntToIntFunction)castCase.aToA).applyAsInt(a);
          else if (castCase.aToA instanceof IntFunction)
            a0 = ((IntFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactorFactor(IntCase.class);

          int b = inputs[1];
          Object b0 = b;

          if (!caseTest.initialized())
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

          if (!caseTest.initialized())
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
            in1 = CaseTest.clone(a0);
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = CaseTest.clone(a0);

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

      onSuccess(caseTest, surveys.get());
    }
  }

  public static class LongCase<S,A,B,R,O> extends Case<S,long[],Long,R,O> {
    private static final long[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE + 1, Short.MIN_VALUE, Short.MAX_VALUE + 1, Integer.MIN_VALUE, Integer.MAX_VALUE + 1, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int NUM_RANDOM = 7 * numTests;
    private final long[] inputs = {0, 0};
    private static final int MAX_PRECISION = 19;

    LongCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,?,Long,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        inputs[0] = SPECIAL[i];
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; i += (random.nextInt(skip) + 1)) {
        for (int j = 0; j < NUM_RANDOM; j += (random.nextInt(skip) + 1)) {
          caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] *= -1;
          test(caseTest, label, cases, surveys, inputs);
          inputs[1] *= -1;
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] *= -1;
          test(caseTest, label, cases, surveys, inputs);
        }
      }
    }

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final long[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,?,I,Object,O> cse = (Case<?,?,I,Object,O>)cases[c];
          final LongCase castCase = (LongCase)cse;

          Object in1, in2 = null;
          long a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactorFactor(LongCase.class, 1);

          if (castCase.aToA instanceof LongToLongFunction)
            a = ((LongToLongFunction)castCase.aToA).applyAsLong(a);
          else if (castCase.aToA instanceof LongFunction)
            a0 = ((LongFunction)castCase.aToA).apply(a);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactorFactor(LongCase.class);

          long b = inputs[1];
          Object b0 = b;

          if (!caseTest.initialized())
            caseTest.setScaleFactorFactor(LongCase.class, 1);

          if (castCase.bToB instanceof ObjLongToLongFunction)
            b = ((ObjLongToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB instanceof LongToLongFunction)
            b = ((LongToLongFunction)castCase.bToB).applyAsLong(b);
          else if (castCase.bToB instanceof LongFunction)
            b0 = ((LongFunction)castCase.bToB).apply(b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          if (!caseTest.initialized())
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
            in1 = CaseTest.clone(a0);
            in2 = b;

            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

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
            in1 = CaseTest.clone(a0);

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

      onSuccess(caseTest, surveys.get());
    }
  }

  public static class StringCase<S,A,B,R,O> extends Case<S,String[],String,R,O> {
    private static final String[] SPECIAL = {"0", "-1", "1", String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE), String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE + 1), String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE + 1), String.valueOf(Long.MIN_VALUE), "9223372036854775808", "-18446744073709551616", "18446744073709551615", "-79228162514264337593543950336", "79228162514264337593543950335", "-340282366920938463463374607431768211456", "340282366920938463463374607431768211455", "-1461501637330902918203684832716283019655932542976", "1461501637330902918203684832716283019655932542975", "-6277101735386680763835789423207666416102355444464034512896", "6277101735386680763835789423207666416102355444464034512895", "-26959946667150639794667015087019630673637144422540572481103610249216", "26959946667150639794667015087019630673637144422540572481103610249215", "-115792089237316195423570985008687907853269984665640564039457584007913129639936", "115792089237316195423570985008687907853269984665640564039457584007913129639935"};
    private static final int NUM_RANDOM = numTests;
    private static final int MAX_PRECISION = 1024;

    private final String[] inputs = {null, null};

    StringCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final Case<?,?,String,?,O>[] cases, final Supplier<Surveys> surveys) {
      for (int i = 0; i < SPECIAL.length; ++i) {
        inputs[0] = SPECIAL[i];
        for (int j = 0; j < SPECIAL.length; ++j) {
          inputs[1] = SPECIAL[j];
          test(caseTest, label, cases, surveys, inputs);
        }
      }

      for (int i = 0; i < NUM_RANDOM; i += (random.nextInt(skip) + 1)) {
        for (int j = 0; j < NUM_RANDOM; j += (random.nextInt(skip) + 1)) {
          caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] = neg(inputs[0]);
          test(caseTest, label, cases, surveys, inputs);
          inputs[1] = neg(inputs[1]);
          test(caseTest, label, cases, surveys, inputs);
          inputs[0] = neg(inputs[0]);
          test(caseTest, label, cases, surveys, inputs);
        }
      }
    }

    @Override
    <I,O>void test(final CaseTest caseTest, final String label, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final String[] inputs) {
      for (int c = 0; c < cases.length; ++c) {
        try {
          final Case<?,?,I,Object,O> cse = (Case<?,?,I,Object,O>)cases[c];
          final StringCase castCase = (StringCase)cse;

          Object in1, in2 = null;
          final String a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactorFactor(StringCase.class, 1);

          if (castCase.aToA instanceof Function)
            a0 = ((Function)castCase.aToA).apply(a0);
          else if (castCase.aToA != null)
            throw new UnsupportedOperationException(castCase.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactorFactor(StringCase.class);

          final String b = inputs[1];
          Object b0 = b;
          int b1 = Integer.MIN_VALUE;
          long b2 = Long.MIN_VALUE;

          if (!caseTest.initialized())
            caseTest.setScaleFactorFactor(StringCase.class, 1);

          if (castCase.bToB instanceof Function)
            b0 = ((Function)castCase.bToB).apply(b);
          else if (castCase.bToB instanceof BiObjToIntFunction)
            b1 = ((BiObjToIntFunction)castCase.bToB).applyAsInt(a0, b);
          else if (castCase.bToB instanceof BiObjToLongFunction)
            b2 = ((BiObjToLongFunction)castCase.bToB).applyAsLong(a0, b);
          else if (castCase.bToB != null)
            throw new UnsupportedOperationException(castCase.bToB.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorB = caseTest.getScaleFactorFactor(StringCase.class);

          final Object result;
          long time;
          if (cse.test instanceof ObjIntFunction) {
            final ObjIntFunction test = (ObjIntFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b1;
            if (b1 == Integer.MIN_VALUE)
              throw new IllegalArgumentException();

            time = System.nanoTime();
            result = test.apply(a0, b1);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b2;
            if (b2 == Long.MIN_VALUE)
              throw new IllegalArgumentException();

            time = System.nanoTime();
            result = test.apply(a0, b2);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)castCase.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)castCase.test;
            in1 = CaseTest.clone(a0);

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

      onSuccess(caseTest, surveys.get());
    }
  }

  private static Object clone(final Object obj) {
    if (obj instanceof BigInt)
      return ((BigInt)obj).clone();

    if (obj instanceof int[])
      return ((int[])obj).clone();

    return obj;
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntFunction<A> aToA, final IntFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntFunction<A> aToA, final ObjIntToIntFunction<A> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntFunction<A> aToA, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntFunction<A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,R,O>IntCase<S,A,?,R,O> i(final S subject, final IntFunction<A> aToA, final IntToIntFunction bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntToIntFunction aToA, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>IntCase<S,A,B,R,O> i(final S subject, final IntToIntFunction aToA, final IntToIntFunction test, final Function<R,O> out) {
    return new IntCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,R,O>IntCase<S,Integer,Integer,R,O> i(final S subject, final BiIntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 2, null, null, test, out);
  }

  public static <S,R,O>IntCase<S,Integer,Integer,R,O> i(final S subject, final IntFunction<R> test, final Function<R,O> out) {
    return new IntCase<>(subject, 1, null, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final LongToLongFunction bToB, final BiLongFunction<R> test) {
    return new LongCase<>(subject, 2, aToA, bToB, test, null);
  }

  public static <S,R,O>LongCase<S,Long,Long,R,O> l(final S subject, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, null, null, test, out);
  }

  public static <S,R,O>LongCase<S,Long,Long,R,O> l(final S subject, final LongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 1, null, null, test, out);
  }

  public static <S,R,O>LongCase<S,Long,Long,R,O> l(final S subject, final LongToLongFunction aToA, final BiLongFunction<R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final LongToLongFunction bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final BiLongToLongFunction test, final Function<R,O> out) {
    return new LongCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final BiLongToLongFunction test) {
    return new LongCase<>(subject, 2, aToA, null, test, null);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final BiLongToLongFunction test) {
    return new LongCase<>(subject, 2, null, null, test, null);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final Function<String,B> bToB, final BiFunction<A,B,R> test) {
    return new StringCase<>(subject, 2, aToA, bToB, test, null);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final BiFunction<A,B,R> test) {
    return new StringCase<>(subject, 2, aToA, null, test, null);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final Function<A,R> test) {
    return new StringCase<>(subject, 1, aToA, null, test, null);
  }

  public static <S,R,O>StringCase<S,String,String,R,O> s(final S subject, final BiFunction<String,String,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 2, null, null, test, out);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final BiObjToLongFunction<A,String> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>StringCase<S,A,B,R,O> s(final S subject, final Function<String,A> aToA, final BiObjToIntFunction<A,String> bToB, final ObjIntFunction<A,R> test, final Function<R,O> out) {
    return new StringCase<>(subject, 2, aToA, bToB, test, out);
  }

  protected abstract int[] randomInputs(int p1, int p2, int[] inputs);
  protected abstract long[] randomInputs(int p1, int p2, long[] inputs);
  protected abstract String[] randomInputs(int p1, int p2, String[] inputs);
  protected abstract void onSuccess();

  @SafeVarargs
  public final <O>void test(final String label, final IntCase<String,?,?,?,O> ... cases) {
    exec(label, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final LongCase<String,?,?,?,O> ... cases) {
    exec(label, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final StringCase<String,?,?,?,O> ... cases) {
    exec(label, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final AuditReport report, final IntCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final AuditReport report, final LongCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final AuditReport report, final StringCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, report, cases);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private final <I,O>void exec(final String label, final AuditReport report, final Case<?,?,I,?,O>[] cases) {
    final Case prototype = cases[0];
    final int divisions = 11;
    final int variables = prototype.variables();

    final String[] headings = heading(cases);

    long ts = System.currentTimeMillis();
    prototype.test(this, label, cases, () -> {
      if (initialized)
        return CaseTest.this.surveys;

      initialized = true;
      return CaseTest.this.surveys = new Surveys(cases, variables, divisions, warmup, report) {
        @Override
        public String key(final int variable, final int division) {
          final int maxPrec = prototype.maxPrecision(variable);
          final double width = 2d * maxPrec / (divisions - 1);
          return String.valueOf((int)(division * width - maxPrec));
        }

        @Override
        public int getDivision(final int variable, final Object obj) {
          if (obj instanceof Boolean)
            return ((Boolean)obj).booleanValue() ? 0 : 1;

          final int maxPrecision = prototype.maxPrecision(variable);
          final double width = 2d * maxPrecision / divisions;
          int dig;
          if (obj instanceof Integer) {
            final int val = (Integer)obj;
            dig = Numbers.precision(val) * Numbers.signum(val);
          }
          else if (obj instanceof Long) {
            final long val = (Long)obj;
            dig = Numbers.precision(val) * Numbers.signum(val);
          }
          else if (obj instanceof BigInteger) {
            final BigInteger val = (BigInteger)obj;
            dig = Numbers.precision(val) * val.signum();
          }
          else if (obj instanceof BigInt) {
            final BigInt val = (BigInt)obj;
            dig = val.precision() * val.signum();
          }
          else if (obj instanceof int[]) {
            final int[] val = (int[])obj;
            final int len = val.length == 0 ? -1 : Math.abs(val[0]);
            // Is this a BigInt value array?
            if (len != 0 && len >= val.length)
              throw new IllegalArgumentException("Expected BigInt value array: " + Arrays.toString(val));

            if (len < 0)
              dig = 0;
            else
              dig = BigInt.precision(val) * BigInt.signum(val);
          }
          else if (obj instanceof byte[]) {
            final byte[] val = (byte[])obj;
            // FIXME: This is incorrect! But, whatever!
            dig = val.length % (divisions * 2);
//            final int bits = (val.length - 1) * 8;
//            dig = (int)Math.floor(Math.log10((1L << bits) - 1));
          }
          else if (obj instanceof String) {
            final String val = (String)obj;
            dig = val.startsWith("-") ? 1 - val.length() : val.length();
          }
          else {
            throw new UnsupportedOperationException(obj.getClass().getName());
          }

          // Special case for unsigned long, because signed long has max precision of 19, but unsigned long is 20
          if (dig == 20 && maxPrecision == 19)
            dig = 19;

          final int division = (int)((maxPrecision + dig - 1) / width);
          if (0 > division || division > divisions - 1)
            throw new IllegalStateException();

          return division;
        }
      };
    });

    ts = System.currentTimeMillis() - ts;
    surveys.print(label, ts, headings);
  }

  private Surveys surveys;

  private static String[] heading(final Case<?,?,?,?,?>[] cases) {
    final String[] heading = new String[cases.length];
    for (int i = 0; i < heading.length; ++i) {
      final Case<?,?,?,?,?> cse = cases[i];
      heading[i] = cse.subject instanceof Class ? ((Class<?>)cse.subject).getSimpleName() : String.valueOf(cse.subject);
    }

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