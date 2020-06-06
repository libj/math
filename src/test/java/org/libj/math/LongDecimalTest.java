package org.libj.math;

import static org.junit.Assert.*;
import static org.libj.math.LongDecimal.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.libj.lang.Buffers;
import org.libj.lang.Numbers;
import org.libj.lang.Strings;

public class LongDecimalTest {
  private static final boolean debug = false;
  private static final Random random = new Random();
  private static final int numTests = 100000;
  private static final byte maxBits = 16;
  private static final MathContext precision34 = MathContext.DECIMAL128;
  private static final NumberFormat expectedFormatter = new DecimalFormat("0E0");
  private static final NumberFormat epsilonFormatter = new DecimalFormat("0E0");
  private static final long[] pow2 = new long[64];
  static final BigInteger[] minValue = new BigInteger[LongDecimal.minValue.length];
  static final BigInteger[] maxValue = new BigInteger[LongDecimal.maxValue.length];

  static {
    for (byte i = 0; i < minValue.length; ++i) {
      minValue[i] = BigInteger.valueOf(LongDecimal.minValue[i]);
      maxValue[i] = BigInteger.valueOf(LongDecimal.maxValue[i]);
    }

    expectedFormatter.setRoundingMode(RoundingMode.FLOOR);
    expectedFormatter.setMinimumFractionDigits(18);
    epsilonFormatter.setRoundingMode(RoundingMode.CEILING);
    epsilonFormatter.setMinimumFractionDigits(0);
    for (int i = 0; i < pow2.length; ++i)
      pow2[i] = (long)Math.pow(2, i);
  }

  private static BigDecimal D(final String str) {
    return new BigDecimal(str);
  }

  private static byte maxPowForBits(final byte bits) {
    return (byte)(bits == 6 ? 55 : bits == 5 ? 43 : bits == 4 ? 36 : bits == 3 ? 33 : bits == 2 ? 31 : 30);
  }

  private abstract static class Operation {
    private final String label;
    private final String operator;

    private Operation(final String label, final String operator) {
      this.label = label;
      this.operator = operator;
    }

    abstract BigDecimal[] epsilon();
    abstract BigDecimal control(BigDecimal a, BigDecimal b, long[] time);
    abstract long test(long d1, long d2, byte bits, long defaultValue, long[] time);

    final void print(final long[] time, final BigDecimal[] errors) {
      final StringBuilder builder = new StringBuilder();
      for (int i = 0; i < errors.length; ++i) {
        if (i > 0)
          builder.append(",\n");

        if (errors[i] == null)
          builder.append("null");
        else
          builder.append("D(\"" + epsilonFormatter.format(errors[i]) + "\")");
      }

      final long timeLongDecimal = time[0] / numTests;
      final long timeBigDecimal = time[1] / numTests;
      final long timePerf = ((timeBigDecimal - timeLongDecimal) * 1000) / timeLongDecimal;
      String perf = String.valueOf(timePerf);
      perf = perf.substring(0, perf.length() - 1) + "." + perf.substring(perf.length() - 1);
      if (timePerf > 0)
        perf = "+" + perf;

      System.err.println("LongDecimal." + label + "(): LongDecimal=" + timeLongDecimal + "ns, BigDecimal=" + timeBigDecimal + "ns, perf=" + perf + "%, error=\n  private static final BigDecimal[] epsilon = {\n    " + builder.toString().replace("\n", "\n    ") + "\n  };");
    }
  }

  private abstract static class AddOperation extends Operation {
    private AddOperation(final String label, final String operator) {
      super(label, operator);
    }

    private static final BigDecimal[] epsilon = {
      D("0E0"),
      D("0E0"),
      D("3E-17"),
      D("4E-16"),
      D("4E-16"),
      D("4E-13"),
      D("9E-14"),
      D("4E-13"),
      D("8E-14"),
      D("3E-12"),
      D("2E-12"),
      D("1E-13"),
      D("2E-13"),
      D("8E-12"),
      D("8E-13"),
      D("1E-13")
    };

    @Override
    final BigDecimal[] epsilon() {
      return epsilon;
    }
  }

  private static final class MulOperation extends Operation {
    private MulOperation(final String label, final String operator) {
      super(label, operator);
    }

    private static final BigDecimal[] epsilon = {
      D("0E0"),
      D("0E0"),
      D("9E-18"),
      D("9E-18"),
      D("2E-17"),
      D("1E-16"),
      D("2E-16"),
      D("2E-16"),
      D("3E-16"),
      D("6E-16"),
      D("2E-15"),
      D("3E-15"),
      D("5E-15"),
      D("9E-15"),
      D("2E-14"),
      D("4E-14")
    };

    @Override
    BigDecimal[] epsilon() {
      return epsilon;
    }

    @Override
    BigDecimal control(final BigDecimal a, final BigDecimal b, final long[] time) {
      long ts = System.nanoTime();
      final BigDecimal result = a.multiply(b, precision34);
      ts = System.nanoTime() - ts;
      time[1] += ts;
      return result;
    }

    @Override
    long test(final long d1, final long d2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = mul(d1, d2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }
  }

  private static final class DivOperation extends Operation {
    private DivOperation(final String label, final String operator) {
      super(label, operator);
    }

    private static final BigDecimal[] epsilon = {
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("5E-1"),
      D("2E-1"),
      D("9E-2"),
      D("1E-1"),
      D("2E-2"),
      D("7E-6"),
      D("8E-9"),
      D("2E-9"),
      D("2E-9")
    };

    @Override
    final BigDecimal[] epsilon() {
      return epsilon;
    }

    @Override
    BigDecimal control(final BigDecimal a, final BigDecimal b, final long[] time) {
      if (b.signum() == 0)
        return null;

      long ts = System.nanoTime();
      final BigDecimal result = a.divide(b, precision34);
      ts = System.nanoTime() - ts;
      time[1] += ts;
      return result;
    }

    @Override
    long test(final long d1, final long d2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = div(d1, d2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }
  }

  private static final Operation add = new AddOperation("add", "+") {
    @Override
    long test(final long d1, final long d2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = add(d1, d2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal a, final BigDecimal b, final long[] time) {
      long ts = System.nanoTime();
      final BigDecimal result = a.add(b, precision34);
      ts = System.nanoTime() - ts;
      time[1] += ts;
      return result;
    }
  };

  private static final Operation sub = new AddOperation("sub", "-") {
    @Override
    long test(final long d1, final long d2, final byte bits, final long defaultValue, final long[] time) {
      long ts = System.nanoTime();
      final long result = sub(d1, d2, bits, defaultValue);
      ts = System.nanoTime() - ts;
      if (result != defaultValue)
        time[0] += ts;

      return result;
    }

    @Override
    BigDecimal control(final BigDecimal a, final BigDecimal b, final long[] time) {
      long ts = System.nanoTime();
      final BigDecimal result = a.subtract(b, precision34);
      ts = System.nanoTime() - ts;
      time[1] += ts;
      return result;
    }
  };

  private static final Operation mul = new MulOperation("mul", "*");
  private static final Operation div = new DivOperation("div", "/");

  private static void testEncodeDecode(final long value, final short scale, final byte bits, final long[] time) {
    final long maxValue = pow2[63 - bits];
    final boolean expectOverflow = value < 0 ? value < -maxValue : value > maxValue;

    if (debug) {
      System.out.println("Value: " + Buffers.toString(value) + " " + value);
      System.out.println("Scale: " + Buffers.toString(scale).substring(Buffers.toString(scale).length() - bits) + " " + scale + " " + bits);
    }

    final long defaultValue = random.nextLong();
    long ts = System.nanoTime();
    final long encoded = encode(value, scale, bits, defaultValue);
    time[0] += System.nanoTime() - ts;
    if (expectOverflow) {
      if (encoded != defaultValue)
        fail("Expected IllegalArgumentException: " + value + ", " + scale + ", " + bits);

      return;
    }

    if (debug)
      System.out.println("Encod: " + Buffers.toString(encoded));

    ts = System.nanoTime();
    final long decodedValue = decodeValue(encoded, bits);
    final short decodedScale = decodeScale(encoded, bits);
    time[1] += System.nanoTime() - ts;

    if (debug) {
      System.out.println("DeVal: " + Buffers.toString(decodedValue));
      System.out.println("DeSca: " + Buffers.toString(decodedScale));
    }

    assertEquals("value=" + value + ", scale=" + scale + ", bits=" + bits, value, decodedValue);
    assertEquals("value=" + value + ", scale=" + scale + ", bits=" + bits, scale, decodedScale);
  }

  private static boolean testOperation(final long ld1, final long ld2, final byte bits, final Operation operation, final long[] time, final BigDecimal[] errors) {
    final BigDecimal bd1 = toBidDecimal(ld1, bits);
    final BigDecimal bd2 = toBidDecimal(ld2, bits);
    final BigDecimal expected = operation.control(bd1, bd2, time);

    final long defaultValue = random.nextLong();
    long result = Long.MAX_VALUE;
    try {
      result = operation.test(ld1, ld2, bits, defaultValue, time);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }

    if (result == defaultValue)
      return false;

    final BigDecimal actual = toBidDecimal(result, bits);
    if (expected == null || expected.signum() == 0)
      return false;

    BigInteger unscaled = expected.unscaledValue();
    int scale = -expected.scale();
    final String str = unscaled.toString();
    if (str.length() > 19) {
      unscaled = new BigInteger(str.substring(0, 19));
      scale += str.length() - 19;
    }

    final BigInteger minValue = LongDecimalTest.minValue[bits];
    final BigInteger maxValue = LongDecimalTest.maxValue[bits];
    // Turn the unscaled value to be highest precision available for the LongDecimal of the provided bits
    while (unscaled.signum() < 0 ? unscaled.compareTo(minValue) <= 0 : unscaled.compareTo(maxValue) >= 0) {
      unscaled = unscaled.divide(BigInteger.TEN);
      --scale;
    }

    final int maxScale = bits == 0 ? 0 : (int)pow2[bits - 1];
    final int len = unscaled.toString().length();
    final boolean expectDefaultValue = scale > len && scale - len >= maxScale;

    boolean pass = expectDefaultValue == (result == defaultValue);
    if (!pass)
      operation.test(ld1, ld2, bits, defaultValue, time);

    final BigDecimal error = expected.subtract(actual).abs().divide(expected, precision34);
    errors[bits] = errors[bits] == null ? error : errors[bits].max(error);

    pass &= error.signum() == 0 || error.compareTo(operation.epsilon()[bits]) < 0;
    if (!pass) {
      System.err.println("----------------------------------------\nd1: " + ld1 + " (" + decodeScale(ld1, bits) + ") d2: " + ld2 + " (" + decodeScale(ld2, bits) + ") bits: " + bits);
      System.err.println("Expected: " + bd1 + " " + operation.operator + " " + bd2 + " = " + expectedFormatter.format(expected) + "\n  Actual: " + bd1 + " " + operation.operator + " " + bd2 + " = " + actual + "\n Error: " + error);
      operation.test(ld1, ld2, bits, defaultValue, time);
//          assertTrue(pass);
    }

    return true;
  }

  private static long randomValue(final byte bits, final boolean forMul) {
    // For multiply and divide, the maxValue should not be so big as to cause the result to overrun max/min value
    final long maxValue = pow2[forMul ? maxPowForBits(bits) : 63 - bits];
    return (long)(random.nextDouble() * maxValue);
  }

  private static short randomScale(final byte bits, final int maxScale) {
    if (bits == 0)
      return 0;

    if (bits == 1)
      return (short)(Math.random() < 0.5 ? -1 : 0);

    double scale = random.nextDouble() * (pow2[bits - 1] - 1);
    if (maxScale != -1 && Math.abs(scale) > maxScale)
      scale /= maxScale;

    return (short)((Math.random() < 0.5 ? -1 : 1) * scale);
  }

  private static long randomEncoded(final byte bits, final int maxScale, final long defaultValue, final boolean forMul) {
    return encode(randomValue(bits, forMul), randomScale(bits, maxScale), bits, defaultValue);
  }

  @Test
  @Ignore
  public void testEncodeDecode() {
    final long[] time = new long[2];
    final byte bits = 3;
    final long value = 128936733099190773L;
    final short scale = -1;
    testEncodeDecode(value, scale, bits, time);

    int count = 0;
    for (byte b = 0; b < 16; ++b)
      for (short s = 0; s < Math.pow(2, Math.max(0, b - 1)); s += Math.random() * 10)
        for (int i = 0; i < numTests / 100; ++i, ++count)
          testEncodeDecode(random.nextLong(), s, b, time);

    System.err.println("LongDecimal.testEncodeDecode(): encode=" + (time[0] / count) + "ns, decode=" + (count / time[1]) + "/ns");
  }

  @Test
  public void testAdd() {
    final long[] time = new long[2];
    final BigDecimal[] errors = new BigDecimal[16];
    for (int i = 0; i < numTests; ++i) {
      for (byte b = 0; b < maxBits; ++b) {
        final long defaultValue = random.nextLong();
        final long d1 = randomEncoded(b, -1, defaultValue, false);
        final long d2 = randomEncoded(b, -1, defaultValue, false);
        testOperation(d1, d2, b, add, time, errors);
      }
    }

    add.print(time, errors);
  }

  @Test
  public void testSub() {
    final long[] time = new long[2];
    final BigDecimal[] errors = new BigDecimal[16];
    for (int i = 0; i < numTests; ++i) {
      for (byte b = 0; b < maxBits; ++b) {
        final long defaultValue = random.nextLong();
        final long d1 = randomEncoded(b, -1, defaultValue, false);
        final long d2 = randomEncoded(b, -1, defaultValue, false);
        testOperation(d1, d2, b, sub, time, errors);
      }
    }

    sub.print(time, errors);
  }

  @Test
  public void testMul() {
    final long[] time = new long[2];
    final BigDecimal[] errors = new BigDecimal[maxBits];
    for (int i = 0; i < numTests; ++i) {
      for (byte b = 0; b < maxBits; ++b) {
        final int maxBits = b == 0 ? 0 : (int)pow2[b - 1] / 2;
        final long defaultValue = random.nextLong();
        final long d1 = randomEncoded(b, maxBits, defaultValue, true);
        final long d2 = randomEncoded(b, maxBits, defaultValue, true);
        testOperation(d1, d2, b, mul, time, errors);
      }
    }

    mul.print(time, errors);
  }

  @Test
  public void testDiv() {
    final long[] time = new long[2];
    final BigDecimal[] errors = new BigDecimal[maxBits];
    for (int i = 0; i < numTests; ++i) {
      for (byte b = 0; b < maxBits; ++b) {
        final int maxBits = b == 0 ? 0 : (int)pow2[b - 1] / 2;
        final long defaultValue = random.nextLong();
        final long d1 = randomEncoded(b, maxBits, defaultValue, true);
        final long d2 = randomEncoded(b, maxBits, defaultValue, true);
        testOperation(d1, d2, b, div, time, errors);
      }
    }

    div.print(time, errors);
  }

  @Test
  @Ignore
  public void testSwap() {
    long a = 4831452529031252205L;
    long b = 4971988804943465940L;

    final int count = 10000000;
    long tmpTime = 0;
    long ts;
    long tmp;
    for (int i = 0; i < count; ++i) {
      ts = System.nanoTime();
      tmp = a;
      a = b;
      b = tmp;
      tmpTime += System.nanoTime() - ts;
    }

    System.out.println("tmp: " + tmpTime);
    long xorTime = 0;
    for (int i = 0; i < count; ++i) {
      ts = System.nanoTime();
      a ^= b;
      b ^= a;
      a ^= b;
      xorTime += System.nanoTime() - ts;
    }

    System.out.println("xor: " + xorTime);
    assertTrue(xorTime < tmpTime);
  }

  private static String formatOverrunPoint(final BigDecimal val, final int cut) {
    final String str = val.toString();
    if (str.length() <= cut)
      return str;

    final String a = str.substring(0, cut);
    final String b = str.substring(cut);
    return a + "." + b + " " + b.length() + " " + (FastMath.log2(b.length()) + 2);
  }

  @Test
  @Ignore
  public void testMulOverrunPoints() {
    boolean flip = false;
    for (int i = 63, j = 63; i >= 1;) {
      final BigDecimal a = BigDecimals.TWO.pow(i);
      final BigDecimal b = BigDecimals.TWO.pow(j);
      System.out.println(Strings.padLeft(i + " + " + j, 8) + " " + Strings.padLeft(String.valueOf(i + j), 4) + " " + formatOverrunPoint(a.multiply(b), i <= 55 ? 18 : 17));
      if (flip)
        --i;
      else
        --j;

      flip = !flip;
    }
  }
}