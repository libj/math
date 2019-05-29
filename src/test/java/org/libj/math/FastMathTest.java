/* Copyright (c) 2018 LibJ
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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class FastMathTest {
  private static final Logger logger = LoggerFactory.getLogger(SafeMathTest.class);
  private static int iterations = 20000000;

  @Test
  public void testPow() {
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
      assertTrue((ts1 - ts0) + " < " + (ts2 - ts1), ts1 - ts0 < ts2 - ts1);
      assertTrue((ts1 - ts0) + " < " + (ts3 - ts2), ts1 - ts0 < ts3 - ts2);
    }
  }

  private static void testIsDigit(final int radix) {
    for (int r = 0; r < radix; ++r) {
      if (r < 10) {
        assertTrue(String.valueOf(r), FastMath.isDigit((char)('0' + r), radix));
      }
      else {
        assertTrue(String.valueOf(r), FastMath.isDigit((char)('a' + r - 10), radix));
        assertTrue(String.valueOf(r), FastMath.isDigit((char)('A' + r - 10), radix));
      }
    }

    assertFalse("-1", FastMath.isDigit((char)('0' - 1), radix));
    if (radix <= 10) {
      assertFalse(String.valueOf(radix), FastMath.isDigit((char)('0' + radix), radix));
    }
    else {
      assertFalse(String.valueOf(radix), FastMath.isDigit((char)('a' + radix - 10), radix));
      // NOTE: Can only check a max of radix=31, because at 32, the ASCII table crosses
      // NOTE: form lower-case latin characters to upper case
      if (radix < 32)
        assertFalse(String.valueOf(radix), FastMath.isDigit((char)('A' + radix - 10), radix));
    }
  }

  @Test
  public void testIsDigit() {
    for (int r = Character.MIN_RADIX; r <= Character.MAX_RADIX; ++r)
      testIsDigit(r);
  }

  @SuppressWarnings("unchecked")
  public <T extends Number>void testParse(final Function<char[],T> function, T ... numbers) {
    for (final T number : numbers) {
      final char[] chars = String.valueOf(number).toCharArray();
      assertEquals(number, function.apply(chars));
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Number>void testParseRadix(final BiFunction<char[],Integer,T> function, int radix, T ... numbers) {
    for (final T number : numbers) {
      final char[] chars = Long.toString(number.longValue(), radix).toCharArray();
      assertEquals(number, function.apply(chars, radix));
    }
  }

  @Test
  public void testParseInt() {
    try {
      FastMath.parseInt(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      FastMath.parseInt(new char[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      FastMath.parseInt(new char[] {'-'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseInt(new char[] {':'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseInt(new char[] {'/'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    testParse(FastMath::parseInt, -323, 3923, -7932, 38229, -732938, 83928384, -382983985);

    try {
      FastMath.parseInt(new char[] {'1'}, (byte)1);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseInt(new char[] {'-'}, 10);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseInt(new char[] {'3'}, 2);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    for (byte r = Character.MIN_RADIX; r <= Character.MAX_RADIX; ++r)
      testParseRadix(FastMath::parseInt, r, -323, 3923, -7932, 38229, -732938, 83928384, -382983985);

    for (int i = 0; i < 1000; ++i) {
      for (int r = Character.MIN_RADIX; r <= Character.MAX_RADIX; ++r) {
        final int random = (int)(Integer.MIN_VALUE * Math.random() + Integer.MAX_VALUE * Math.random());
        assertEquals(random, Integer.parseInt(Integer.toString(random, r), r));
        assertEquals(random, FastMath.parseInt(Integer.toString(random, r).toCharArray(), r));
      }
    }
  }

  @Test
  public void testParseLong() {
    try {
      FastMath.parseLong(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      FastMath.parseLong(new char[0]);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      FastMath.parseLong(new char[] {'-'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseLong(new char[] {':'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseLong(new char[] {'/'});
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    testParse(FastMath::parseLong, 323l, -3923l, 7932l, -38229l, 732938l, -83928384l, 382983985l, -8434893285l, 38434893285l, -938434893285l, 1938434893285l, -21938434893285l, 921938434893285l, -9921938434893285l, 79921938434893285l, -279921938434893285l, 8279921938434893285l);

    try {
      FastMath.parseLong(new char[] {'1'}, (byte)1);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseLong(new char[] {'-'}, 10);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    try {
      FastMath.parseLong(new char[] {'3'}, 2);
      fail("Expected NumberFormatException");
    }
    catch (final NumberFormatException e) {
    }

    for (byte r = Character.MIN_RADIX; r <= Character.MAX_RADIX; ++r)
      testParseRadix(FastMath::parseLong, r, 323l, -3923l, 7932l, -38229l, 732938l, -83928384l, 382983985l, -8434893285l, 38434893285l, -938434893285l, 1938434893285l, -21938434893285l, 921938434893285l, -9921938434893285l, 79921938434893285l, -279921938434893285l, 8279921938434893285l);

    for (int i = 0; i < 1000; ++i) {
      for (int r = Character.MIN_RADIX; r <= Character.MAX_RADIX; ++r) {
        final long random = (long)(Long.MIN_VALUE * Math.random() + Long.MAX_VALUE * Math.random());
        assertEquals(random, Long.parseLong(Long.toString(random, r), r));
        assertEquals(random, FastMath.parseLong(Long.toString(random, r).toCharArray(), r));
      }
    }
  }
}