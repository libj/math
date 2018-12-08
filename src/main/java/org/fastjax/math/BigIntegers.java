/* Copyright (c) 2018 FastJAX
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

package org.fastjax.math;

import java.math.BigInteger;
import java.util.HashMap;

public final class BigIntegers {
  private static final HashMap<String,BigInteger> instances = new HashMap<>();

  public static BigInteger instance(final String val) {
    BigInteger instance = instances.get(val);
    if (instance == null)
      instances.put(val, instance = new BigInteger(val));

    return instance;
  }

  public static final BigInteger TWO = BigInteger.valueOf(2);

  private BigIntegers() {
  }
}