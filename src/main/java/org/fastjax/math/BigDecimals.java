/* Copyright (c) 2017 FastJAX
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

import java.math.BigDecimal;
import java.util.HashMap;

public final class BigDecimals {
  private static final HashMap<String,BigDecimal> instances = new HashMap<>();

  public static BigDecimal TWO = BigDecimal.valueOf(2l);
  public static BigDecimal PI = BigDecimal.valueOf(Math.PI);

  public static BigDecimal instance(final String val) {
    BigDecimal instance = instances.get(val);
    if (instance == null)
      instances.put(val, instance = new BigDecimal(val));

    return instance;
  }

  private BigDecimals() {
  }
}