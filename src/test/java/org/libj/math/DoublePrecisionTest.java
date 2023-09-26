package org.libj.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DoublePrecisionTest {
  static final AtomicInteger c = new AtomicInteger(0);
  static final AtomicInteger cd = new AtomicInteger(0);
  static final Random r = new Random();

  @Test
  public void test() throws InterruptedException {
    final int scale = 8;
    final BigDecimal _z = BigDecimal.valueOf(7, scale);
    final double _d = _z.doubleValue();
    assertEquals(0, _z.compareTo(BigDecimal.valueOf(_d)));

    final int step = 20;
    final int threads = 720 / step;
    final ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int from = -360; from <= 360 - step; from += step) { // [N]
      final int fromFinal = from;
      executor.execute(() -> test(scale, fromFinal, fromFinal + step, _z, _d));
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    System.err.println("Error @ 10^-" + scale + " for DOUBLE: " + (int)(100d * cd.get() / c.get()) + "%");
  }

  private static void test(final int scale, final int from, final int to, final BigDecimal _z, final double _d) {
    BigDecimal z = BigDecimal.valueOf(from);
    for (double d = from; d < to; c.getAndIncrement()) { // [N]
      z = z.add(_z);

      // double
      d += _d;
      d = SafeMath.round(d, scale);
      final BigDecimal zd = BigDecimal.valueOf(d);
      d = zd.doubleValue();
      if (zd.compareTo(z) != 0) {
        // System.err.println("double: " + z + " != " + zd);
        cd.getAndIncrement();
      }
    }
  }
}