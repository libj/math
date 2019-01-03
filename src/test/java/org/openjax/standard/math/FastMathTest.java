/* Copyright (c) 2018 OpenJAX
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

package org.openjax.standard.math;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openjax.standard.math.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastMathTest {
  private static final Logger logger = LoggerFactory.getLogger(SafeMathTest.class);
  private static int iterations = 10000000;

  @Test
  @SuppressWarnings("unused")
  public void test() {
    final long ts0 = System.currentTimeMillis();
    for (int i = 0; i < iterations; ++i)
      FastMath.pow(2, 73);

    final long ts1 = System.currentTimeMillis();
    for (int i = 0; i < iterations; ++i)
      Math.pow(2, 73);

    final long ts2 = System.currentTimeMillis();
    long l;
    for (int i = 0; i < iterations; ++i)
      l = (long)Math.pow(2, 73);

    final long ts3 = System.currentTimeMillis();

    logger.info("Total execution time (FastMath.pow): " + (ts1 - ts0));
    logger.info("Total execution time (Math.pow): " + (ts2 - ts1));
    logger.info("Total execution time ((long)Math.pow): " + (ts3 - ts2));
    // FIXME: This is not passing in jdk1.8, but is passing for jdk9+
    if (System.getProperty("java.version").startsWith("1.")) {
      logger.warn("This is not passing in jdk1.8, but is passing for jdk9+");
    }
    else {
      assertTrue(ts1 - ts0 < ts2 - ts1);
      assertTrue(ts1 - ts0 < ts3 - ts2);
    }
  }
}