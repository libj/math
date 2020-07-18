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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class BigIntegerHack {
  private static final Method multiplyToomCook3;
  private static final Field mag;

  static {
    try {
      multiplyToomCook3 = BigInteger.class.getDeclaredMethod("multiplyToomCook3", BigInteger.class, BigInteger.class);
      multiplyToomCook3.setAccessible(true);

      mag = BigInteger.class.getDeclaredField("mag");
      mag.setAccessible(true);
    }
    catch (final NoSuchMethodException | NoSuchFieldException | SecurityException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  static BigInteger invoke(final BigInteger a, final BigInteger b) {
    try {
      if (((int[])mag.get(a)).length < BigIntegerMultiplication.TOOM_COOK_THRESHOLD)
        return a.multiply(b);

      return (BigInteger)multiplyToomCook3.invoke(null, a, b);
    }
    catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}