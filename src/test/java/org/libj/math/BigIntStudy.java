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

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.function.Function;

import org.junit.Test;
import org.libj.lang.Numbers;
import org.libj.lang.Strings;

public class BigIntStudy extends BigIntTest {
  /**
   * Tests which is faster:
   * <ol>
   * <li><ins>Regular: {@code int[] {len, sig, ..}}</ins><br>
   * Length and sig separated, thus sig can be -1, 0, or 1. A length of 0
   * means sig must be 0, which means code has to check for this
   * alignment.</li>
   * <li><ins>Compound: {@code int[] {sigLen, ..}}</ins><br>
   * Length and sig are combined, whereby a positive length means a sig =
   * 1, and a negative length a sig = -1. A length of 0 means the value is
   * zero, and sig ends up as 1. No alignment checks are necessary.</li>
   * </ol>
   */
  @Test
  public void testLengthSignum() {
    final int[] v = new int[10];
    test("length signum: regular vs compound").withCases(
      i("Regular", a -> { v[0] = Math.abs(a / 2); v[1] = Integer.compare(a, 0); return v; }, (int[] a) -> {
        int len = a[0];
        int sig = a[1];

        return len * sig;
      }, Integer::valueOf),
      i("Compound", a -> { v[0] = a / 2; return v; }, (int[] a) -> {
        int sig, len = a[0];
        if (len < 0) {
          len = -len;
          sig = -1;
        }
        else {
          sig = 1;
        }

        return len * sig;
      }, Integer::valueOf)
    );
  }

  @Test
  public void testNewVsClone() {
    test("new vs clone").withCases(
      i("new", a -> new int[Math.abs(a / 100000)], (int[] a) -> new int[a.length], o -> o.length),
      i("clone", a -> new int[Math.abs(a / 100000)], (int[] a) -> a.clone(), o -> o.length)
    );
  }

  @Test
  public void testIpp() {
    test("++i vs i++").withCases(
      i("++i", (int a) -> ++a, (int o) -> 0),
      i("i++", (int a) -> a++, (int o) -> 0)
    );
  }

  @Test
  public void testPlus() {
    test("++i vs i++").withCases(
      i("++a", (int a, int b) -> ++a, o -> 0),
      i("a += b", (int a, int b) -> a += b, o -> 0)
    );
  }

  private static final Function<Object,Object> function = a -> a;

  @Test
  public void testFunctionOverhead() {
    final Object object = new Object();
    final int[] array = new int[10];

    test("int function overhead").withCases(
      i("BiIntFunction", (int a, int b) -> a, o -> 0),
      i("ObjIntFunction", (int a) -> Integer.valueOf(a), (Object a, int b) -> a, o -> 0),
      i("BiFunction(Object)", (int a) -> object, (int b) -> Integer.valueOf(b), (Object a, Object b) -> a, o -> 0),
      i("BiFunction(int[])", (int a) -> array, (int b) -> array, (int[] a, int[] b) -> a, o -> 0),
      i("IntFunction", (int a) -> a, (int o) -> 0),
      i("IntToIntFunction", (int a) -> a, (int o) -> 0),
      i("Function(Object)", (int a) -> object, (Object a) -> a, o -> 0),
      i("Function(int[])", (int a) -> array, (Object a) -> a, o -> 0)
    );

    test("long function overhead").withCases(
      l("BiLongFunction", (long a, long b) -> a, (long o) -> 0L),
      l("BiLongToLongFunction", (long a, long b) -> a, (long a) -> 0L),
      l("ObjLongFunction", (long a) -> Long.valueOf(a), (Object a, long b) -> a, o -> 0L),
      l("BiFunction(Object)", (long a) -> object, (long b) -> object, (Object a, Object b) -> a, o -> 0L),
      l("BiFunction(int[])", (long a) -> array, (long b) -> array, (int[] a, int[] b) -> a, o -> 0L),
      l("LongFunction", (long a) -> a, o -> 0L),
      l("LongToLongFunction", (long a) -> a, (long a) -> a, o -> 0L),
      l("Function(Object)", (long a) -> object, (Object a) -> a, o -> 0L),
      l("Function(int[])", (long a) -> array, (Object a) -> a, o -> 0L)
    );

    test("string function overhead").withCases(
      s("ObjIntFunction", (String a) -> a, (String a, String b) -> 0, (String a, int b) -> a, o -> 0),
      s("ObjLongFunction", (String a) -> a, (String a, String b) -> 0L, (String a, long b) -> a, o -> 0),
      s("BiFunction(Object)", (String a) -> object, (String b) -> object, (Object a, Object b) -> a, o -> 0),
      s("BiFunction(int[])", (String a) -> array, (String b) -> array, (int[] a, int[] b) -> a, o -> 0),
      s("Function(Object)", (String a) -> object, (Object a) -> a, o -> 0),
      s("Function(int[])", (String a) -> array, (Object a) -> a, o -> 0)
    );
  }

  @Test
  public void testFunctionObjectVsArray() {
    final Object object = new Object();
    final int[] array = new int[10];

    test("object vs array").withCases(
      i("object", (int a) -> function.apply(object), o -> 0),
      i("array", (int a) -> function.apply(array), o -> 0)
    );
  }

  @Test
  public void testRandomIntPrecision() {
    for (int i = 0; i < 1000; ++i) {
      for (int j = 1; j < 10; ++j) {
        final int random = randomInt(j);
        assertEquals(String.valueOf(random), j, Numbers.precision(random));
      }
    }
  }

  @Test
  public void testRandomLongPrecision() {
    for (int i = 0; i < 1000; ++i) {
      for (int j = 1; j < 19; ++j) {
        final long random = randomLong(j);
        assertEquals(j + " " + String.valueOf(random), j, Numbers.precision(random));
      }
    }
  }

  @Test
  public void testMulVsCond() {
    test("sig * value: '*' vs '? :'").withCases(
      l("s * v", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> a * b, (long o) -> o),
      l("s < 0 ? -v : v", a -> a % 2 == 0 ? -1 : 1, (long a, long b) -> a < 0 ? -b : b, (long o) -> o)
    );
  }

  @Test
  public void testMulVsPlus() {
    test("sig * value: '*' vs '? :'").withCases(
      l("s * 2", (long a, long b) -> a * 2, (long o) -> o),
      l("s << 1", (long a, long b) -> a << 1, (long o) -> o),
      l("s + s", (long a, long b) -> a + a, (long o) -> o)
    );
  }

  @Test
  public void testArrayVsBuffer2() {
    for (int i = 1; i < 10000; i += 10) {
      final int[] x = BigInt.valueOf("9" + Strings.repeat('9', i));
      System.out.println(i + " " + x.length + " " + i / x.length);
    }
  }

  @Test
  public void testArrayVsBuffer() {
    test("array vs buffer").withCases(
      l("array", (long a, long b) -> { final int[] x = new int[100]; return x.length; }, (long o) -> o),
      l("buffer", (long a, long b) -> { final ByteBuffer x = ByteBuffer.allocateDirect(100); return x.capacity(); }, (long o) -> o)
    );
  }

  @Test
  public void testCast() {
    test("direct vs cast").withCases(
      i("object", a -> (byte)a, (int a) -> a, (int o) -> o),
      i("array", a -> (byte)a, (int a) -> (byte)a, (int o) -> o)
    );
  }

  private static byte min1(final byte a, final short b) {
    return (byte)(a < b ? a : b);
  }

  private static byte min2(final byte a, final short b) {
    if (a < b) return a; return (byte)b;
  }

  @Test
  public void testCast2() {
    long ts;
    byte a;
    short b;
    long time1 = 0;
    long time2 = 0;
    for (int i = 0; i < 100000000; ++i) {
      a = (byte)random.nextInt();
      b = (short)random.nextInt();

      ts = System.nanoTime();
      min1(a, b);
      ts = System.nanoTime() - ts;
      time1 += ts;

      ts = System.nanoTime();
      min2(a, b);
      ts = System.nanoTime() - ts;
      time2 += ts;
    }

    System.out.println(time1 + "\n" + time2);
  }
}