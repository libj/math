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

import static org.libj.math.survey.AuditMode.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.survey.AuditReport;
import org.libj.math.survey.AuditRunner;

import gnu.java.math.MPN;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument({BigInt.class, int[].class})
@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BigIntMultiplicationTest extends BigIntTest {
  @Test
  public void testUnsignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys the long multiplication algorithm for both `BigInt` and `BigInteger`.");

    final int[] sig = {0};
    test("mul(int,int)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      i(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, int b) -> a.multiply(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.mul(sig[0], b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys the long multiplication algorithm for both `BigInt` and `BigInteger`.");

    test("mul(int)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      i(BigInteger.class, this::scaledBigInteger, (BigInteger a, int b) -> a.multiply(BigInteger.valueOf(b)), String::valueOf),
      // i(BigIntHuldra.class, this::scaledBigIntHuldra, (BigIntHuldra a, int b) -> { a.mul(b); return a; }, String::valueOf),
      i(BigInt.class, this::scaledBigInt, (BigInt a, int b) -> a.mul(b), String::valueOf),
      i(int[].class, this::scaledVal, (int[] a, int b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  @Test
  public void testUnsignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.");
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys the long multiplication algorithm for both `BigInt` and `BigInteger`.");

    final int[] sig = {0};
    test("mul(int,long)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      l(BigInteger.class, this::scaledBigInteger, b -> { sig[0] = b % 2 == 0 ? -1 : 1; return b; }, (BigInteger a, long b) -> a.multiply(BigIntegers.valueOf(sig[0], b)), String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.mul(sig[0], b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.mul(a, sig[0], b), BigInt::toString)
    );
  }

  @Test
  public void testSignedLong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys the long multiplication algorithm for both `BigInt` and `BigInteger`.");

    test("mul(long)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      l(BigInteger.class, this::scaledBigInteger, (BigInteger a, long b) -> a.multiply(BigInteger.valueOf(b)), String::valueOf),
      // l(BigIntHuldra.class, this::scaledBigIntHuldra, (BigIntHuldra a, long b) -> { a.mul(b); return a; }, String::valueOf),
      l(BigInt.class, this::scaledBigInt, (BigInt a, long b) -> a.mul(b), String::valueOf),
      l(int[].class, this::scaledVal, (int[] a, long b) -> BigInt.mul(a, b), BigInt::toString)
    );
  }

  public void testBig(final AuditReport report, final int scale, final int skip) {
    test("mul(T): " + scale).withSkip(skip).withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      s(BigInteger.class, a -> scaledBigInteger(a, scale), a -> scaledBigInteger(a, scale), (BigInteger a, BigInteger b) -> a.multiply(b), String::valueOf),
      // s(BigIntHuldra.class, a -> scaledBigIntHuldra(a, scale), a -> scaledBigIntHuldra(a, scale), (BigIntHuldra a, BigIntHuldra b) -> a.mul(b), String::valueOf),
      s(BigInt.class, a -> scaledBigInt(a, scale), a -> scaledBigInt(a, scale), (BigInt a, BigInt b) -> a.mul(b), String::valueOf),
      s(int[].class, a -> scaledVal(a, scale), a -> scaledVal(a, scale), (int[] a, int[] b) -> BigInt.mul(a, b), BigInt::toString)
    );

    printAlgoReport();
  }

  private static void printAlgoReport() {
//    if (BigIntMultiplication.X_Q[0] != Integer.MAX_VALUE)
//      System.out.println("                      quad: " + Arrays.toString(BigIntMultiplication.X_Q));
//    if (BigIntMultiplication.X_QI[0] != Integer.MAX_VALUE)
//      System.out.println("              quad inplace: " + Arrays.toString(BigIntMultiplication.X_QI));
//
//    if (BigIntMultiplication.X_QN[0] != Integer.MAX_VALUE)
//      System.out.println("               native quad: " + Arrays.toString(BigIntMultiplication.X_QN));
//    if (BigIntMultiplication.X_QIN[0] != Integer.MAX_VALUE)
//      System.out.println("       native quad inplace: " + Arrays.toString(BigIntMultiplication.X_QIN));
//
//    if (BigIntMultiplication.X_K[0] != Integer.MAX_VALUE)
//      System.out.println("                 karatsuba: " + Arrays.toString(BigIntMultiplication.X_K));
//    if (BigIntMultiplication.X_KI[0] != Integer.MAX_VALUE)
//      System.out.println("         karatsuba inplace: " + Arrays.toString(BigIntMultiplication.X_KI));
//    if (BigIntMultiplication.X_KP[0] != Integer.MAX_VALUE)
//      System.out.println("        parallel karatsuba: " + Arrays.toString(BigIntMultiplication.X_KP));
//    if (BigIntMultiplication.X_KPI[0] != Integer.MAX_VALUE)
//      System.out.println("karatsuba parallel inplace: " + Arrays.toString(BigIntMultiplication.X_KPI));
//
//    System.out.println();
  }

  public void testSquareBig(final AuditReport report, final int scale, final int skip) {
    test("mul(T,T): " + scale).withSkip(skip).withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      s(BigInteger.class, a -> scaledBigInteger(a, scale), (BigInteger a) -> a.multiply(a), String::valueOf),
      // s(BigIntHuldra.class, a -> scaledBigIntHuldra(a, scale), (BigIntHuldra a) -> a.mul(a), String::valueOf),
      s(BigInt.class, a -> scaledBigInt(a, scale), (BigInt a) -> a.mul(a), String::valueOf),
      s(int[].class, a -> scaledVal(a, scale), (int[] a) -> BigInt.mul(a, a), BigInt::toString)
    );

    printAlgoReport();
  }

  @Test
  public void testBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys all multiplication algorithms in `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.");
    report.addComment(UNINSTRUMENTED.ordinal(), "For \"small sized\" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.");
    report.addComment(UNINSTRUMENTED.ordinal(), "For \"medium sized\" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.");
    report.addComment(UNINSTRUMENTED.ordinal(), "For \"large sized\" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.");
    report.addComment(UNINSTRUMENTED.ordinal(), "For \"very large sized\" numbers, `BigInt` outperforms `BigInteger` in lieu of its Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.");
    final int maxPrecision = isCI ? 8 : 128;
    for (int i = 1; i <= maxPrecision; i *= 2)
      testBig(report, i, 80);
  }

  @Test
  @Ignore
  public void testHuge(final AuditReport report) {
    for (int i = 256; i <= 1024; i *= 2)
      testBig(report, i, 100);
  }

  @Test
  public void testSquareBig(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys all multiplication algorithms in `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.");
    report.addComment(UNINSTRUMENTED.ordinal(), "The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the \"square\" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.");
    final int maxPrecision = isCI ? 8 : 128;
    for (int i = 1; i <= maxPrecision; i *= 2)
      testSquareBig(report, i, 80);
  }

  @Test
  @Ignore
  public void testSquareHuge(final AuditReport report) {
    for (int i = 256; i <= 1024; i *= 2)
      testSquareBig(report, i, 100);
  }

  @Test
  public void testPow(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test surveys the power algorithm for both `BigInt` and `BigInteger`. Note that the variable representing the power is limited to `2048`, otherwise with power values larger than 127 result in unreasonably long tests.");

    test("pow(int)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      i(BigInteger.class, this::scaledBigInteger, b -> abs(b) % 256, (BigInteger a, int b) -> a.pow(b), o -> o == null ? null : String.valueOf(o)),
      i(BigInt.class, this::scaledBigInt, b -> abs(b) % 256, (BigInt a, int b) -> a.pow(b), o -> o == null ? null : String.valueOf(o)),
      i(int[].class, this::scaledVal, b -> abs(b) % 256, (int[] a, int b) -> BigInt.pow(a, b), o -> o == null ? null : BigInt.toString(o))
    );
  }

  @Test
  public void testUInt(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the correctness of `[BigIntMultiplication.umul(int)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.");

    final int[] zds = new int[3];
    final int[] x = new int[3];
    final int[] y = new int[1];
    final int[] val = new int[4];
    test("umul(int[],int)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      l(MPN.class, a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)b;
        MPN.mul(zds, x, 2, y, 1);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 2)),
      l(BigInt.class, a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], (int)b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }

  @Test
  public void testULong(final AuditReport report) {
    report.addComment(UNINSTRUMENTED.ordinal(), "This test validates the correctness of `[BigIntMultiplication.umul(long)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.");

    final int[] zds = new int[4];
    final int[] x = new int[3];
    final int[] y = new int[2];
    final int[] val = new int[5];
    test("umul(int[],long)").withEpsilon(BigDecimal.ZERO).withAuditReport(report).withCases(
      l(MPN.class, a -> {
        x[0] = (int)(a & 0xFFFFFFFFL);
        x[1] = (int)(a >>> 32);
        return a;
      }, (long a, long b) -> {
        y[0] = (int)(b & 0xFFFFFFFFL);
        y[1] = (int)(b >>> 32);
        MPN.mul(zds, x, 2, y, 2);
        return zds;
      }, o -> BigIntValue.longValue(o, 0, 3)),
      l(BigInt.class, a -> {
        BigInt.assign(val, 1, a);
        return a;
      }, (long a, long b) -> {
        val[0] = BigIntMultiplication.umul(val, 1, val[0], b);
        return val;
      }, o -> BigIntValue.longValue(o, 1, o[0]))
    );
  }

  @Test
  @Ignore("Used for tuning")
  public void testKaratsubaThreshold() {
    final ThresholdTest test = new ThresholdTest(2, 500000) {
      @Override
      void beforeTest(final int len1, final int len2) {
        // final int zlen = len1 + len2;
        // BigIntMultiplication.record = true;
        // BigIntMultiplication.KARATSUBA_THRESHOLD_X = Math.min(len1, len2) - 5;
        // BigIntMultiplication.KARATSUBA_THRESHOLD_Z = zlen - 5;
      }

      @Override
      void test(final BigInt v1, final BigInt v2) {
        v1.mul(v2);
      }

      @Override
      void beforeControl() {
        // BigIntMultiplication.record = false;
        // BigIntMultiplication.KARATSUBA_THRESHOLD_X = Integer.MAX_VALUE;
        // BigIntMultiplication.KARATSUBA_THRESHOLD_Z = Integer.MAX_VALUE;
      }
    };

    if (BigInt.NATIVE_THRESHOLD == Integer.MAX_VALUE) {
      // KARATSUBA_THRESHOLD_Z0=135
      test.runAB(65, 70);
      // KARATSUBA_THRESHOLD_X0=70
      test.runA(70, 63, 75);
      test.runB(70, 63, 75);
    }
    else {
      // KARATSUBA_THRESHOLD_Z0=80
      test.runAB(35, 50);
      // KARATSUBA_THRESHOLD_X0=50
      test.runA(50, 43, 55);
      test.runB(50, 43, 55);
    }

    printAlgoReport();
  }

  @Test
  @Ignore("Used for tuning")
  public void testKaratsubaSquareThreshold() {
    final ThresholdTest test = new ThresholdTest(2, 10000) {
      @Override
      void beforeTest(final int len1, final int len2) {
        // BigIntMultiplication.record = true;
        // BigIntMultiplication.KARATSUBA_SQUARE_THRESHOLD = len1 - 5;
      }

      @Override
      void test(final BigInt v1, final BigInt v2) {
        v1.mul(v1);
      }

      @Override
      void beforeControl() {
        // BigIntMultiplication.record = false;
        // BigIntMultiplication.KARATSUBA_SQUARE_THRESHOLD = Integer.MAX_VALUE;
      }
    };

    if (BigInt.NATIVE_THRESHOLD == Integer.MAX_VALUE) {
      // KARATSUBA_SQUARE_THRESHOLD=400
      test.runAB(630, 650);
    }
    else {
      // KARATSUBA_SQUARE_THRESHOLD=400
      test.runAB(190, 210);
    }

    printAlgoReport();
  }

  @Test
  @Ignore("Used for tuning")
  public void testParallelKaratsubaThreshold() {
    final ThresholdTest test = new ThresholdTest(5, 10000) {
      @Override
      void beforeTest(final int len1, final int len2) {
        // final int zlen = len1 + len2;
        // BigIntMultiplication.record = true;
        // BigIntMultiplication.PARALLEL_KARATSUBA_THRESHOLD_X = Math.min(len1, len2) - 5;
        // BigIntMultiplication.PARALLEL_KARATSUBA_THRESHOLD_Z = zlen - 5;
      }

      @Override
      void test(final BigInt v1, final BigInt v2) {
        v1.mul(v2);
      }

      @Override
      void beforeControl() {
        // BigIntMultiplication.record = false;
        // BigIntMultiplication.PARALLEL_KARATSUBA_THRESHOLD_X = Integer.MAX_VALUE;
        // BigIntMultiplication.PARALLEL_KARATSUBA_THRESHOLD_Z = Integer.MAX_VALUE;
      }
    };

    if (BigInt.NATIVE_THRESHOLD == Integer.MAX_VALUE) {
      // KARATSUBA_THRESHOLD_Z0=1500
      test.runAB(745, 765);
      // KARATSUBA_THRESHOLD_X0=120, but raise it a bit cause threads are generally expensive
      test.runA(120, 700, 720);
      test.runB(120, 700, 720);
    }
    else {
      // KARATSUBA_THRESHOLD_Z0=850
      test.runAB(415, 435);
      // KARATSUBA_THRESHOLD_X0=100, but raise it a bit cause threads are generally expensive
      test.runA(90, 400, 450);
      test.runB(90, 400, 450);
    }

    printAlgoReport();
  }

  private abstract static class ThresholdTest {
    private final int step;
    private final int iterations;

    private ThresholdTest(final int step, final int iterations) {
      this.step = step;
      this.iterations = iterations;
    }

    private void runA(final int a, final int start, final int end) {
      System.out.println("- [" + a + ",?] _____________");
      for (int len = start; len <= end; len += step)
        calc(a, len);
    }

    private void runB(final int b, final int start, final int end) {
      System.out.println("- [?," + b + "] _____________");
      for (int len = start; len <= end; len += step)
        calc(len, b);
    }

    private void runAB(final int start, final int end) {
      System.out.println("- [?,?] _____________");
      for (int len = start; len <= end; len += step)
        calc(len, len);
    }

    abstract void beforeTest(int len1, int len2);
    abstract void test(BigInt v1, BigInt v2);
    abstract void beforeControl();

    private void calc(final int len1, final int len2) {
      long ts, a = 0, b = 0;
      final int[] v1 = randomVal(len1);
      final int[] v2 = randomVal(len2);
      BigInt t1, t2;
      for (int i = 0; i < iterations; ++i) {
        t1 = new BigInt(v1);
        t2 = new BigInt(v2);

        beforeTest(len1, len2);
        ts = System.nanoTime();
        test(t1, t2);
        a += System.nanoTime() - ts;

        t1 = new BigInt(v1);
        t2 = new BigInt(v2);

        beforeControl();
        ts = System.nanoTime();
        test(t1, t2);
        b += System.nanoTime() - ts;
      }

      final int zlen = len1 + len2;
      final double c = (double)a / (double)b;
      System.out.println("[" + len1 + "," + len2 + "] " + zlen + " " + c);
    }
  }
}