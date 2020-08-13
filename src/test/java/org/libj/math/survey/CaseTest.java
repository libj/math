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

package org.libj.math.survey;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import org.junit.internal.ArrayComparisonFailure;
import org.libj.console.Ansi;
import org.libj.console.Ansi.Color;
import org.libj.lang.Numbers;
import org.libj.lang.Strings;
import org.libj.math.BigInt;
import org.libj.math.BigIntHuldra;
import org.libj.math.Decimal;
import org.libj.math.DecimalOperationTest;
import org.libj.test.TestAide;
import org.libj.util.CollectionUtil;
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

@SuppressWarnings("hiding")
public abstract class CaseTest {
  protected static final Random random = new Random();
  private static final BigDecimal[] e10 = new BigDecimal[63];

  private static final File errorDir = new File("target/generated-test-resources/casetest");
  private static final int warmup = 100; // Short warmup, therefore it's important to use -Xcomp to engage JIT compilation

  private boolean initialized;
  private final int[] scaleFactors = {1, 1, 1, 1};

  static {
    for (int i = 0; i < e10.length; ++i)
      e10[i] = BigDecimal.ONE.scaleByPowerOfTen(i);
  }

  @SuppressWarnings("rawtypes")
  private static int getCaseIndex(final Class<? extends Case> cls) {
    return cls == IntCase.class ? 0 : cls == LongCase.class ? 1 : cls == DecimalCase.class ? 2 : cls == StringCase.class ? 3 : 4;
  }

  private static int skip(final int precision, final int skip) {
    final int maxSkip = precision / 25; // necessary to ensure enough tests are run to get past the warmup
    return random.nextInt(Math.min(maxSkip, (int)((skip + 1) * Math.pow(precision, 1.6d) / 600000)) + 1) + 1;
  }

  public boolean initialized() {
    return initialized;
  }

  @SuppressWarnings("rawtypes")
  protected void setScaleFactor(final Class<? extends Case> cls, final int scaleFactor) {
    this.scaleFactors[getCaseIndex(cls)] = scaleFactor;
  }

  @SuppressWarnings("rawtypes")
  private int getScaleFactor(final Class<? extends Case> cls) {
    return this.scaleFactors[getCaseIndex(cls)];
  }

  public static String neg(final String num) {
    return !num.startsWith("-") ? "-" + num : num.substring(1);
  }

  public static int precision(final String num) {
    return num.startsWith("-") ? num.length() - 1 : num.length();
  }

  private static BigDecimal toBigDecimal(final Object obj) {
    if (obj instanceof BigDecimal)
      return (BigDecimal)obj;

    if (obj instanceof BigInteger)
      return new BigDecimal((BigInteger)obj);

    if (obj instanceof BigInt)
      return new BigDecimal(((BigInt)obj).toString());

    if (obj instanceof Decimal)
      return new BigDecimal(((Decimal)obj).toString());

    if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer)
      return new BigDecimal(((Number)obj).intValue());

    if (obj instanceof Long)
      return new BigDecimal(((Long)obj).longValue());

    if (obj instanceof String)
      return new BigDecimal((String)obj);

    throw new UnsupportedOperationException("Unsupported type: " + obj.getClass().getName());
  }

  public abstract static class Case<S,T,I,R,O> {
    private final File errorFile = new File(errorDir, getClass().getSimpleName());

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

    final void clearErrorFile() {
      errorFile.delete();
    }

    BigDecimal checkError(final Object a, final Object b) {
      if (a.equals(b))
        return null;

      final BigDecimal bd1 = toBigDecimal(a);
      final BigDecimal bd2 = toBigDecimal(b);
      return bd1.subtract(bd2).abs().divide(e10[bd1.precision()]);
    }

    @SuppressWarnings("unchecked")
    final T readErrorFile(final T defaultInputs) {
      if (!errorFile.exists())
        return defaultInputs;

      try (final ObjectInputStream in = new ObjectInputStream(Files.newInputStream(errorFile.toPath()))) {
        return (T)in.readObject();
      }
      catch (final ClassNotFoundException | IOException e) {
        throw new RuntimeException(e);
      }
    }

    public S getSubject() {
      return this.subject;
    }

    int variables() {
      return variables;
    }

    abstract int maxPrecision();

    final int maxPrecision(final int variable) {
      return (variable == 0 ? scaleFactorFactorA : scaleFactorFactorB) * maxPrecision();
    }

    abstract void test(CaseTest caseTest, String label, int skip, BigDecimal epsilon, AuditReport report, Case<?,?,I,?,O>[] cases, Supplier<Surveys> surveys);
    abstract <I,O>int test(CaseTest caseTest, String label, BigDecimal epsilon, Case<?,?,I,?,O>[] cases, Supplier<Surveys> surveys, T inputs);

    <I,O>int verify(final String label, final Case<?,?,I,Object,O> cse, final BigDecimal epsilon, final Object in1, final Object in2, final Object result, final int c, final long time, final Supplier<Surveys> surveys) {
      final Object o = cse.out != null ? cse.out.apply(result) : result;
      final BigDecimal error;
      if (previous == null || c == 0) {
        previous = o;
        error = null;
      }
      else if (o instanceof Float) {
        final float delta = epsilon != null && epsilon.signum() != 0 ? epsilon.floatValue() : Math.ulp((Float)previous);
        final float o1 = (Float)previous;
        final float o2 = (Float)o;
        assertEquals(o1, o2, delta);
        error = BigDecimal.valueOf(Math.abs(o1 - o2));
      }
      else if (o instanceof Double) {
        final double delta = epsilon != null && epsilon.signum() != 0 ? epsilon.doubleValue() : Math.ulp((Double)previous);
        final double o1 = (Double)previous;
        final double o2 = (Double)o;
        assertEquals(o1, o2, delta);
        error = BigDecimal.valueOf(Math.abs(o1 - o2));
      }
      else {
        if (o instanceof byte[]) {
          error = null;
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
        else {
          error = (epsilon == null || epsilon.scale() == 0) && previous.equals(o) ? null : checkError(previous, o);
          if (error != null && error.compareTo(epsilon) > 0) {
            final StringBuilder message = new StringBuilder("\n");
            message.append(label).append('\n');
            message.append("in1: ").append(toString(in1, in2)).append('\n');
            message.append("in2: ").append(toString(in2, in1)).append('\n');
            message.append(toDetailString(previous, o)).append('\n').append(toDetailString(o, previous)).append("\nerror: ").append(error);
            assertEquals((cse.subject instanceof Class ? ((Class<?>)cse.subject).getSimpleName() : cse.subject) + ": " + message.append('\n').toString(), toDetailString(previous, o), toDetailString(o, previous));
          }
        }
      }

      int precision = precision(in1);
      surveys.get().addSample(c, 0, in1, time, error);
      if (cse.variables() == 2) {
        precision = Math.max(precision, precision(in2));
        surveys.get().addSample(c, 1, in2, time, error);
      }

      return precision;
    }

    protected String toString(final Object o1, final Object o2) {
      return o1 == null ? "null" : o1 instanceof int[] ? BigInt.toString((int[])o1) : o1.toString();
    }

    protected String toDetailString(final Object o1, final Object o2) {
      if (o1 instanceof Byte || o1 instanceof Short || o1 instanceof Integer)
        return Arrays.toString(BigInt.valueOf(((Number)o1).intValue()));

      if (o1 instanceof Long)
        return Arrays.toString(BigInt.valueOf(((Long)o1).longValue()));

      if (o1 instanceof String)
        return bigIntToStringSafe((String)o1);

      if (o1 instanceof Boolean)
        return o1.toString();

      throw new UnsupportedOperationException("Unsupported type: " + o1.getClass().getName());
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
    private static final int MAX_PRECISION = 10;
    private static final int MIN_ITERATIONS = 2000;

    private final int[] inputs = readErrorFile(new int[2]);
    private boolean specialDone;

    IntCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final Case<?,?,Integer,?,O>[] cases, final Supplier<Surveys> surveys) {
      if (!specialDone) {
        if (inputs[0] != 0 || inputs[1] != 0)
          test(caseTest, label, epsilon, cases, surveys, inputs);

        for (int i = 0; i < SPECIAL.length; ++i) {
          inputs[0] = SPECIAL[i];
          for (int j = 0; j < SPECIAL.length; ++j) {
            inputs[1] = SPECIAL[j];
            test(caseTest, label, epsilon, cases, surveys, inputs);
          }
        }

        specialDone = true;
      }

      final int minIterations = report == null ? MIN_ITERATIONS : report.minIterations(MIN_ITERATIONS);
      int precision = MAX_PRECISION;
      for (int i = 0; i < precision; i += skip(precision, skip)) {
        for (int j = 0; j < precision; j += skip(precision, skip)) {
          for (int k = 0; k < Math.max(minIterations, warmup / precision); ++k) {
            caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);

            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[1] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
          }
        }
      }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    <I,O>int test(final CaseTest caseTest, final String label, final BigDecimal epsilon, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final int[] inputs) {
      int precision = 0;
      for (int c = 0; c < cases.length; ++c) {
        final IntCase cse = (IntCase)cases[c];
        try {
          int a = inputs[0];
          Object a0 = a;
          Object in1, in2 = null;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(IntCase.class, 1);

          if (cse.aToA instanceof IntToIntFunction)
            a = ((IntToIntFunction)cse.aToA).applyAsInt(a);
          else if (cse.aToA instanceof IntFunction)
            a0 = ((IntFunction)cse.aToA).apply(a);
          else if (cse.aToA != null)
            throw new UnsupportedOperationException(cse.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactor(IntCase.class);

          int b = inputs[1];
          Object b0 = b;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(IntCase.class, 1);

          if (cse.bToB instanceof IntToIntFunction)
            b = ((IntToIntFunction)cse.bToB).applyAsInt(b);
          else if (cse.bToB instanceof ObjIntToIntFunction)
            b = ((ObjIntToIntFunction)cse.bToB).applyAsInt(a0, b);
          else if (cse.bToB instanceof IntFunction)
            b0 = ((IntFunction)cse.bToB).apply(b);
          else if (cse.bToB instanceof ObjIntFunction)
            b0 = ((ObjIntFunction)cse.bToB).apply(a0, b);
          else if (cse.bToB != null)
            throw new UnsupportedOperationException(cse.bToB.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorB = caseTest.getScaleFactor(IntCase.class);

          final Object result;
          final long overhead;
          long time;
          if (cse.test instanceof BiIntFunction) {
            final BiIntFunction test = (BiIntFunction)cse.test;
            in1 = a;
            in2 = b;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof IntFunction) {
            final IntFunction test = (IntFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a);
          }
          else if (cse.test instanceof IntToIntFunction) {
            final IntToIntFunction test = (IntToIntFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.applyAsInt(a);
          }
          else if (cse.test instanceof ObjIntFunction) {
            final ObjIntFunction test = (ObjIntFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b;

            overhead = 30;
            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = CaseTest.clone(a0);

            overhead = 31;
            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time - overhead;
          precision = Math.max(precision, verify(label, cse, epsilon, in1, in2, result, c, time, surveys));
        }
        catch (final Throwable t) {
          checkDebug(t, cse, inputs);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys.get());
      return precision;
    }
  }

  public static class LongCase<S,A,B,R,O> extends Case<S,long[],Long,R,O> {
    private static final long[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE + 1, Short.MIN_VALUE, Short.MAX_VALUE + 1, Integer.MIN_VALUE, Integer.MAX_VALUE + 1, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int MAX_PRECISION = 19;
    private static final int MIN_ITERATIONS = 200;

    private final long[] inputs = readErrorFile(new long[2]);
    private boolean specialDone;

    LongCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final Case<?,?,Long,?,O>[] cases, final Supplier<Surveys> surveys) {
      if (!specialDone) {
        if (inputs[0] != 0 || inputs[1] != 0)
          test(caseTest, label, epsilon, cases, surveys, inputs);

        for (int i = 0; i < SPECIAL.length; ++i) {
          inputs[0] = SPECIAL[i];
          for (int j = 0; j < SPECIAL.length; ++j) {
            inputs[1] = SPECIAL[j];
            test(caseTest, label, epsilon, cases, surveys, inputs);
          }
        }

        specialDone = true;
      }

      final int minIterations = report == null ? MIN_ITERATIONS : report.minIterations(MIN_ITERATIONS);
      int precision = MAX_PRECISION;
      for (int i = 0; i < precision; i += skip(precision, skip)) {
        for (int j = 0; j < precision; j += skip(precision, skip)) {
          for (int k = 0; k < Math.max(minIterations, warmup / precision); ++k) {
            caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);

            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[1] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
          }
        }
      }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    <I,O>int test(final CaseTest caseTest, final String label, final BigDecimal epsilon, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final long[] inputs) {
      int precision = 0;
      for (int c = 0; c < cases.length; ++c) {
        final LongCase cse = (LongCase)cases[c];
        try {
          Object in1, in2 = null;
          long a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(LongCase.class, 1);

          if (cse.aToA instanceof LongToLongFunction)
            a = ((LongToLongFunction)cse.aToA).applyAsLong(a);
          else if (cse.aToA instanceof LongFunction)
            a0 = ((LongFunction)cse.aToA).apply(a);
          else if (cse.aToA != null)
            throw new UnsupportedOperationException(cse.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactor(LongCase.class);

          long b = inputs[1];
          Object b0 = b;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(LongCase.class, 1);

          if (cse.bToB instanceof ObjLongToLongFunction)
            b = ((ObjLongToLongFunction)cse.bToB).applyAsLong(a0, b);
          else if (cse.bToB instanceof LongToLongFunction)
            b = ((LongToLongFunction)cse.bToB).applyAsLong(b);
          else if (cse.bToB instanceof LongFunction)
            b0 = ((LongFunction)cse.bToB).apply(b);
          else if (cse.bToB != null)
            throw new UnsupportedOperationException(cse.bToB.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorB = caseTest.getScaleFactor(LongCase.class);

          final Object result;
          final long overhead;
          long time;
          if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;
            in1 = a;
            in2 = b;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof BiLongToLongFunction) {
            final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;
            in1 = a;
            in2 = b;

            overhead = 31;
            time = System.nanoTime();
            result = test.applyAsLong(a, b);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b;

            overhead = 31;
            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof LongFunction) {
            final LongFunction test = (LongFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a);
          }
          else if (cse.test instanceof LongToLongFunction) {
            final LongToLongFunction test = (LongToLongFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.applyAsLong(a);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = CaseTest.clone(a0);

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time - overhead;
          precision = Math.max(precision, verify(label, cse, epsilon, in1, in2, result, c, time, surveys));
        }
        catch (final Throwable t) {
          checkDebug(t, cse, inputs);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys.get());
      return precision;
    }
  }

  public static class DecimalCase<S,A,B,R,O> extends Case<S,long[],Decimal,R,O> {
    public static final ThreadLocal<Byte> scaleBitsLocal = new ThreadLocal<>();

    static long randomValue(final byte maxValuePower) {
      final long maxValue = DecimalOperationTest.pow2[maxValuePower];
      return (long)((Math.random() < 0.5 ? -1 : 1) * random.nextDouble() * maxValue);
    }

    static short randomScale(final byte bits) {
      if (bits <= 1)
        return 0;

      if (bits == 2)
        return (short)(Math.random() < 0.5 ? -1 : 0);

      final short maxScale = Decimal.maxScale(bits);
      final double scale = random.nextDouble() * maxScale;
      return (short)((Math.random() < 0.5 ? -1 : 1) * scale);
    }

    static long randomEncoded(final byte scaleBits) {
      final long defaultValue = random.nextLong();
      final long value = randomValue(Decimal.valueBits(scaleBits));
      final short scale = randomScale(scaleBits);
      final long decimal = Decimal.encode(value, scale, scaleBits, defaultValue);
      if (decimal == defaultValue)
        throw new IllegalStateException();

      return decimal;
    }

    private static final long[] SPECIAL = {0, -1, 1, -2, 2, -4, 4, -8, 8, -16, 16, -32, 32, -64, 64, Byte.MIN_VALUE, Byte.MAX_VALUE + 1, Short.MIN_VALUE, Short.MAX_VALUE + 1, Integer.MIN_VALUE, Integer.MAX_VALUE + 1, Long.MIN_VALUE, Long.MAX_VALUE};
    private static final int MIN_ITERATIONS = 1000;

    private final long[] inputs = readErrorFile(new long[3]);
    private boolean specialDone;

    private byte scaleBits;
    private byte valueBits;
    private long minValue;
    private long maxValue;
    private int MAX_PRECISION;

    DecimalCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    void init() {
      this.scaleBits = inputs[2] != 0 ? (byte)inputs[2] : scaleBitsLocal.get();
      this.valueBits = Decimal.valueBits(scaleBits);
      this.minValue = Decimal.minValue(valueBits);
      this.maxValue = Decimal.maxValue(valueBits);
      this.MAX_PRECISION = Numbers.precision(this.minValue);
    }

    @Override
    protected String toString(final Object o1, final Object o2) {
      if (o1 instanceof BigDecimal)
        return format((BigDecimal)o1, o2);

      if (o1 instanceof Decimal) {
        if (!(o2 instanceof Long))
          return ((Decimal)o1).toScientificString();

        final BigDecimal a = new BigDecimal(((Decimal)o1).toString());
        return format(a, o2);
      }

      if (o1 instanceof Long)
        return Decimal.toScientificString((Long)o1, scaleBits);

      return super.toString(o1, o2);
    }

    @Override
    protected String toDetailString(final Object o1, final Object o2) {
      if (o1 instanceof Long)
        return Decimal.toScientificString((Long)o1, scaleBits);

      if (o1 instanceof BigDecimal || o1 instanceof Decimal)
        return toString(o1, o2);

      return super.toDetailString(o1, o2);
    }

    private static final DecimalFormat format = new DecimalFormat("0E0");

    static {
      format.setRoundingMode(RoundingMode.HALF_UP);
      format.setMinimumFractionDigits(20);
      format.setPositivePrefix("");
    }

    private String format(final BigDecimal o1, final Object o2) {
      final BigInteger min;
      final BigInteger max;
      if (o2 instanceof Decimal) {
        min = BigInteger.valueOf(Long.MIN_VALUE);
        max = BigInteger.valueOf(Long.MAX_VALUE);
      }
      else if (o2 instanceof Long) {
        min = BigInteger.valueOf(minValue);
        max = BigInteger.valueOf(maxValue);
      }
      else {
        throw new UnsupportedOperationException(o2.getClass().getName());
      }

      if (o1.signum() == 0)
        return "0";

      if (o1.precision() == 1 && o1.scale() == 0)
        return o1.toString();

      int scale = o1.scale();
      BigDecimal y = o1.scaleByPowerOfTen(o1.scale());
      BigInteger bi;
      for (int i = 1; (bi = y.toBigInteger()).signum() < 0 ? bi.compareTo(min) < 0 : bi.compareTo(max) > 0; ++i, --scale)
        y = o1.scaleByPowerOfTen(o1.scale()).divide(e10[i], RoundingMode.HALF_UP);

      y = y.setScale(0, RoundingMode.HALF_UP);
      y = y.scaleByPowerOfTen(-scale);

      return format.format(y).replaceAll("\\.?0+(E[-+0-9]*)$", "$1");
    }

    private BigDecimal format(final Object o1, final Object o2) {
      if (o1 instanceof BigDecimal)
        return new BigDecimal(format((BigDecimal)o1, o2));

      if (o1 instanceof Decimal) {
        final BigDecimal a = new BigDecimal(((Decimal)o1).toString());
        return o2 instanceof Long ? new BigDecimal(format(a, o2)) : a;
      }

      if (o1 instanceof Long)
        return Decimal.toBigDecimal((Long)o1, scaleBits);

      throw new UnsupportedOperationException(o1.getClass().getName());
    }

    @Override
    BigDecimal checkError(final Object a, final Object b) {
      BigDecimal o1 = format(a, b);
      BigDecimal o2 = format(b, a);
      final int scale = Math.min(o1.scale(), o2.scale());
      if (scale < 0) {
        o1 = o1.scaleByPowerOfTen(scale);
        o2 = o2.scaleByPowerOfTen(scale);
      }

      return o1.subtract(o2).abs().divide(e10[o1.precision()]);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    boolean setInput(final long[] inputs, final int index, long value) {
      boolean changed = false;
      if (changed = (value <= minValue))
        value = minValue + 1;
      else if (changed = (value >= maxValue))
        value = maxValue - 1;

      inputs[index] = value;
      return changed;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final Case<?,?,Decimal,?,O>[] cases, final Supplier<Surveys> surveys) {
      if (!specialDone) {
        init();
        if (inputs[0] != 0 || inputs[1] != 0)
          test(caseTest, label, epsilon, cases, surveys, inputs);

        for (int i = 0; i < SPECIAL.length; ++i) {
          if (setInput(inputs, 0, SPECIAL[i]))
            i = SPECIAL.length;

          for (int j = 0; j < SPECIAL.length; ++j) {
            if (setInput(inputs, 1, SPECIAL[j]))
              j = SPECIAL.length;

            test(caseTest, label, epsilon, cases, surveys, inputs);
          }
        }

        specialDone = true;
      }

      final long defaultValue = random.nextLong();
      final int minIterations = report == null ? MIN_ITERATIONS : report.minIterations(MIN_ITERATIONS);
      int precision = MAX_PRECISION;
      for (int i = 0; i < precision; i += skip(precision, skip)) {
        for (int j = 0; j < precision; j += skip(precision, skip)) {
          for (int k = 0; k < Math.max(minIterations, warmup / precision); ++k) {
            caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);

            setInput(inputs, 0, inputs[0]);
            inputs[0] = Decimal.encode(inputs[0], randomScale(scaleBits), scaleBits, defaultValue);
            if (inputs[0] == defaultValue)
              throw new IllegalStateException();

            setInput(inputs, 1, inputs[1]);
            inputs[1] = Decimal.encode(inputs[1], randomScale(scaleBits), scaleBits, defaultValue);
            if (inputs[0] == defaultValue)
              throw new IllegalStateException();

            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[1] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] *= -1;
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
          }
        }
      }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    <I,O>int test(final CaseTest caseTest, final String label, final BigDecimal epsilon, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final long[] inputs) {
      int precision = 0;
      for (int c = 0; c < cases.length; ++c) {
        final DecimalCase cse = (DecimalCase)cases[c];
        try {
          Object in1, in2 = null;
          long a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(DecimalCase.class, 1);

          if (cse.aToA instanceof LongToLongFunction)
            a = ((LongToLongFunction)cse.aToA).applyAsLong(a);
          else if (cse.aToA instanceof LongFunction)
            a0 = ((LongFunction)cse.aToA).apply(a);
          else if (cse.aToA != null)
            throw new UnsupportedOperationException(cse.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactor(DecimalCase.class);

          long b = inputs[1];
          Object b0 = b;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(DecimalCase.class, 1);

          if (cse.bToB instanceof ObjLongToLongFunction)
            b = ((ObjLongToLongFunction)cse.bToB).applyAsLong(a0, b);
          else if (cse.bToB instanceof LongToLongFunction)
            b = ((LongToLongFunction)cse.bToB).applyAsLong(b);
          else if (cse.bToB instanceof LongFunction)
            b0 = ((LongFunction)cse.bToB).apply(b);
          else if (cse.bToB != null)
            throw new UnsupportedOperationException(cse.bToB.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorB = caseTest.getScaleFactor(DecimalCase.class);

          final Object result;
          final long overhead;
          long time;
          if (cse.test instanceof BiLongFunction) {
            final BiLongFunction test = (BiLongFunction)cse.test;
            in1 = a;
            in2 = b;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a, b);
          }
          else if (cse.test instanceof BiLongToLongFunction) {
            final BiLongToLongFunction test = (BiLongToLongFunction)cse.test;
            in1 = a;
            in2 = b;

            overhead = 31;
            time = System.nanoTime();
            result = test.applyAsLong(a, b);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b;

            overhead = 31;
            time = System.nanoTime();
            result = test.apply(a0, b);
          }
          else if (cse.test instanceof LongFunction) {
            final LongFunction test = (LongFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.apply(a);
          }
          else if (cse.test instanceof LongToLongFunction) {
            final LongToLongFunction test = (LongToLongFunction)cse.test;
            in1 = a;

            overhead = 32;
            time = System.nanoTime();
            result = test.applyAsLong(a);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = CaseTest.clone(a0);

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time - overhead;
          precision = Math.max(precision, verify(label, cse, epsilon, in1, in2, result, c, time, surveys));
        }
        catch (final Throwable t) {
          checkDebug(t, cse, inputs);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys.get());
      return precision;
    }
  }

  public static class StringCase<S,A,B,R,O> extends Case<S,String[],String,R,O> {
    private static final String[] SPECIAL = {"0", "-1", "1", String.valueOf(Byte.MIN_VALUE), String.valueOf(Byte.MAX_VALUE), String.valueOf(Short.MIN_VALUE), String.valueOf(Short.MAX_VALUE + 1), String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE + 1), String.valueOf(Long.MIN_VALUE), "9223372036854775808", "-18446744073709551616", "18446744073709551615", "-79228162514264337593543950336", "79228162514264337593543950335", "-340282366920938463463374607431768211456", "340282366920938463463374607431768211455", "-1461501637330902918203684832716283019655932542976", "1461501637330902918203684832716283019655932542975", "-6277101735386680763835789423207666416102355444464034512896", "6277101735386680763835789423207666416102355444464034512895", "-26959946667150639794667015087019630673637144422540572481103610249216", "26959946667150639794667015087019630673637144422540572481103610249215", "-115792089237316195423570985008687907853269984665640564039457584007913129639936", "115792089237316195423570985008687907853269984665640564039457584007913129639935"};
    private static final int MAX_PRECISION = 64;
    private static final int MIN_ITERATIONS = 2;

    private final String[] inputs = readErrorFile(new String[2]);
    private boolean specialDone;

    StringCase(final S subject, final int variables, final Object aToA, final Object bToB, final Object test, final Function<R,O> out) {
      super(subject, variables, aToA, bToB, test, out);
    }

    @Override
    int maxPrecision() {
      return MAX_PRECISION;
    }

    @Override
    final void test(final CaseTest caseTest, final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final Case<?,?,String,?,O>[] cases, final Supplier<Surveys> surveys) {
      if (!specialDone) {
        if (inputs[0] != null || inputs[1] != null)
          test(caseTest, label, epsilon, cases, surveys, inputs);

        for (int i = 0; i < SPECIAL.length; ++i) {
          inputs[0] = SPECIAL[i];
          for (int j = 0; j < SPECIAL.length; ++j) {
            inputs[1] = SPECIAL[j];
            test(caseTest, label, epsilon, cases, surveys, inputs);
          }
        }

        specialDone = true;
      }

      final int minIterations = report == null ? MIN_ITERATIONS : report.minIterations(MIN_ITERATIONS);
      int precision = MAX_PRECISION;
      for (int i = 0; i < precision; i += skip(precision, skip)) {
        for (int j = 0; j < precision; j += skip(precision, skip)) {
          for (int k = 0; k < Math.max(minIterations, warmup / precision); ++k) {
            caseTest.randomInputs(i % MAX_PRECISION + 1, j % MAX_PRECISION + 1, inputs);

            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] = neg(inputs[0]);
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[1] = neg(inputs[1]);
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
            inputs[0] = neg(inputs[0]);
            precision = Math.max(precision, test(caseTest, label, epsilon, cases, surveys, inputs));
          }
        }
      }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    <I,O>int test(final CaseTest caseTest, final String label, final BigDecimal epsilon, final Case<?,?,I,?,O>[] cases, final Supplier<Surveys> surveys, final String[] inputs) {
      int precision = 0;
      for (int c = 0; c < cases.length; ++c) {
        final StringCase cse = (StringCase)cases[c];
        try {
          Object in1, in2 = null;
          final String a = inputs[0];
          Object a0 = a;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(StringCase.class, 1);

          if (cse.aToA instanceof Function)
            a0 = ((Function)cse.aToA).apply(a0);
          else if (cse.aToA != null)
            throw new UnsupportedOperationException(cse.aToA.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorA = caseTest.getScaleFactor(StringCase.class);

          final String b = inputs[1];
          Object b0 = b;
          int b1 = Integer.MIN_VALUE;
          long b2 = Long.MIN_VALUE;

          if (!caseTest.initialized())
            caseTest.setScaleFactor(StringCase.class, 1);

          if (cse.bToB instanceof Function)
            b0 = ((Function)cse.bToB).apply(b);
          else if (cse.bToB instanceof BiObjToIntFunction)
            b1 = ((BiObjToIntFunction)cse.bToB).applyAsInt(a0, b);
          else if (cse.bToB instanceof BiObjToLongFunction)
            b2 = ((BiObjToLongFunction)cse.bToB).applyAsLong(a0, b);
          else if (cse.bToB != null)
            throw new UnsupportedOperationException(cse.bToB.getClass().getName());

          if (!caseTest.initialized())
            scaleFactorFactorB = caseTest.getScaleFactor(StringCase.class);

          final Object result;
          final long overhead;
          long time;
          if (cse.test instanceof ObjIntFunction) {
            final ObjIntFunction test = (ObjIntFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b1;
            if (b1 == Integer.MIN_VALUE)
              throw new IllegalArgumentException();

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0, b1);
          }
          else if (cse.test instanceof ObjLongFunction) {
            final ObjLongFunction test = (ObjLongFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = b2;
            if (b2 == Long.MIN_VALUE)
              throw new IllegalArgumentException();

            overhead = 33;
            time = System.nanoTime();
            result = test.apply(a0, b2);
          }
          else if (cse.test instanceof BiFunction) {
            final BiFunction test = (BiFunction)cse.test;
            in1 = CaseTest.clone(a0);
            in2 = CaseTest.clone(b0);

            overhead = 38;
            time = System.nanoTime();
            result = test.apply(a0, b0);
          }
          else {
            final Function test = (Function)cse.test;
            in1 = CaseTest.clone(a0);

            overhead = 35;
            time = System.nanoTime();
            result = test.apply(a0);
          }

          time = System.nanoTime() - time - overhead;
          precision = Math.max(precision, verify(label, cse, epsilon, in1, in2, result, c, time, surveys));
        }
        catch (final Throwable t) {
          checkDebug(t, cse, inputs);
          c = -1;
        }
      }

      onSuccess(caseTest, surveys.get());
      return precision;
    }
  }

  private static int precision(final Object obj) {
    if (obj instanceof Integer)
      return Numbers.precision((Integer)obj);

    if (obj instanceof Long)
      return Numbers.precision((Long)obj);

    if (obj instanceof BigInteger)
      return Numbers.precision((BigInteger)obj);

    if (obj instanceof BigDecimal)
      return ((BigDecimal)obj).precision();

    if (obj instanceof Decimal)
      return ((Decimal)obj).precision();

    if (obj instanceof BigIntHuldra)
      return ((BigIntHuldra)obj).precision();

    if (obj instanceof BigInt)
      return ((BigInt)obj).precision();

    if (obj instanceof int[])
      return BigInt.precision((int[])obj);

    // FIXME: This is incorrect! But, whatever!
    if (obj instanceof byte[])
      return 25 * ((byte[])obj).length;

    throw new UnsupportedOperationException("Unsupported type: " + obj.getClass().getName());
  }

  private static Object clone(final Object obj) {
    if (obj instanceof BigInt)
      return ((BigInt)obj).clone();

    if (obj instanceof Decimal)
      return ((Decimal)obj).clone();

    if (obj instanceof BigIntHuldra)
      return ((BigIntHuldra)obj).clone();

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

  public static <S,R,O>IntCase<S,Integer,Integer,R,O> i(final S subject, final BiIntFunction<R> test) {
    return new IntCase<>(subject, 2, null, null, test, null);
  }

  public static <S,R,O>IntCase<S,Integer,Integer,R,O> i(final S subject, final IntFunction<R> test) {
    return new IntCase<>(subject, 1, null, null, test, null);
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

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongFunction<A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new LongCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final LongToLongFunction test, final Function<R,O> out) {
    return new LongCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final LongToLongFunction bToB, final BiLongFunction<R> test) {
    return new LongCase<>(subject, 2, aToA, bToB, test, null);
  }

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final LongToLongFunction aToA, final BiLongFunction<R> test) {
    return new LongCase<>(subject, 2, aToA, null, test, null);
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

  public static <S,A,B,R,O>LongCase<S,A,B,R,O> l(final S subject, final BiLongToLongFunction test) {
    return new LongCase<>(subject, 2, null, null, test, null);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiFunction<A,B,R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final LongFunction<B> bToB, final BiLongFunction<R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final Function<A,R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongToLongFunction aToA, final LongToLongFunction test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 1, aToA, null, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongToLongFunction aToA, final LongToLongFunction bToB, final BiLongFunction<R> test) {
    return new DecimalCase<>(subject, 2, aToA, bToB, test, null);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongToLongFunction aToA, final BiLongFunction<R> test) {
    return new DecimalCase<>(subject, 2, aToA, null, test, null);
  }

  public static <S,R,O>DecimalCase<S,Long,Long,R,O> d(final S subject, final BiLongFunction<R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, null, null, test, out);
  }

  public static <S,R,O>DecimalCase<S,Long,Long,R,O> d(final S subject, final LongFunction<R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 1, null, null, test, out);
  }

  public static <S,R,O>DecimalCase<S,Long,Long,R,O> d(final S subject, final LongToLongFunction aToA, final BiLongFunction<R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final ObjLongToLongFunction<A> bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongFunction<A> aToA, final LongToLongFunction bToB, final ObjLongFunction<A,R> test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, bToB, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final LongToLongFunction aToA, final BiLongToLongFunction test, final Function<R,O> out) {
    return new DecimalCase<>(subject, 2, aToA, null, test, out);
  }

  public static <S,A,B,R,O>DecimalCase<S,A,B,R,O> d(final S subject, final BiLongToLongFunction test) {
    return new DecimalCase<>(subject, 2, null, null, test, null);
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
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final IntCase<String,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final IntCase<String,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final LongCase<String,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final LongCase<String,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final StringCase<String,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final StringCase<String,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, null, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final IntCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final AuditReport report, final IntCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final LongCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final AuditReport report, final LongCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int scaleBits, final int skip, final BigDecimal epsilon, final AuditReport report, final DecimalCase<Class<?>,?,?,?,O> ... cases) {
    DecimalCase.scaleBitsLocal.set((byte)scaleBits);
    exec(label, skip, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int scaleBits, final BigDecimal epsilon, final AuditReport report, final DecimalCase<Class<?>,?,?,?,O> ... cases) {
    DecimalCase.scaleBitsLocal.set((byte)scaleBits);
    exec(label, 0, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final StringCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, skip, epsilon, report, cases);
  }

  @SafeVarargs
  public final <O>void test(final String label, final BigDecimal epsilon, final AuditReport report, final StringCase<Class<?>,?,?,?,O> ... cases) {
    exec(label, 0, epsilon, report, cases);
  }

  public abstract Ansi.Color getColor(Case<?,?,?,?,?> cse);

  @SuppressWarnings({"rawtypes", "unchecked"})
  private final <I,O>void exec(final String label, final int skip, final BigDecimal epsilon, final AuditReport report, final Case<?,?,I,?,O>[] cases) {
    final Case prototype = cases[0];
    final int divisions = 11;
    final int variables = prototype.variables();

    final String[] headings = heading(cases);

    final long ts = System.currentTimeMillis();
    prototype.test(this, label, skip, epsilon, report, cases, () -> {
      if (initialized)
        return CaseTest.this.surveys;

      initialized = true;
      return CaseTest.this.surveys = new Surveys(cases, variables, divisions, warmup, report) {
        @Override
        public Color getColor(final Case<?,?,?,?,?> cse) {
          return CaseTest.this.getColor(cse);
        }

        @Override
        public String getLabel(final int index, final int variable, final int division) {
          final int maxPrecision = prototype.maxPrecision(variable);
          final double width = 2d * maxPrecision / (divisions - 1);
          final int precision = (int)(division * width - maxPrecision);
          return String.valueOf(index == 0 ? BigInt.valueOf("9" + Strings.repeat('9', Math.abs(precision))).length : precision);
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
          else if (obj instanceof BigDecimal) {
            final BigDecimal val = (BigDecimal)obj;
            dig = val.precision() * val.signum();
          }
          else if (obj instanceof Decimal) {
            final Decimal val = (Decimal)obj;
            dig = val.precision() * val.signum();
          }
          else if (obj instanceof BigInt) {
            final BigInt val = (BigInt)obj;
            dig = val.precision() * val.signum();
          }
          else if (obj instanceof BigIntHuldra) {
            final BigIntHuldra val = (BigIntHuldra)obj;
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
            dig = 25 * val.length % (divisions * 2);
//            final int bits = (val.length - 1) * 8;
//            dig = (int)Math.floor(Math.log10((1L << bits) - 1));
          }
          else if (obj instanceof String) {
            final String val = (String)obj;
            dig = val.startsWith("-") ? 1 - val.length() : val.length();
          }
          else if (obj != null && obj.getClass() == Object.class) {
            // Special case for testFunctionOverhead()
            return 0;
          }
          else {
            throw new UnsupportedOperationException(obj == null ? null : obj.getClass().getName());
          }

          // Special case for unsigned long, because signed long
          // has max precision of 19, but unsigned long is 20
          if (dig == 20 && maxPrecision == 19)
            dig = 19;

          int division = (int)((maxPrecision + dig - 1) / width);
          if (0 > division || division > divisions - 1)
            division = Math.max(0, Math.min(10, division));
//            throw new IllegalStateException(String.valueOf(obj));

          return division;
        }
      };
    });

    final int categories = report != null && report.getMode() == 1 ? 2 : variables;
    final String[] summary = new String[cases.length * categories];
    final String result = surveys.print(label, System.currentTimeMillis() - ts, summary, headings);
    if (report != null)
      report.submit(label, result, summary, categories, CollectionUtil.asCollection(new ArrayList<>(), headings));
    else
      System.out.println(result);

    initialized = false;
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
  private static <T extends Throwable>void checkDebug(final Throwable t, final Case<?,?,?,?,?> cse, final Object inputs) throws T {
    TestAide.printStackTrace(System.err, t);

    try {
      errorDir.mkdirs();
      final Path path = new File(errorDir, cse.getClass().getSimpleName()).toPath();
      try (final ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
        out.writeObject(inputs);
      }
    }
    catch (final IOException e) {
      throw (T)e;
    }

    if (!TestAide.isInDebug())
      throw (T)t;

    // TODO: Place breakpoint here to debug...
    cse.clearErrorFile();
  }
}