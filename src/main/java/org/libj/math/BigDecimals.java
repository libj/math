/* Copyright (c) 2017 LibJ
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

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility functions for operations pertaining to {@link BigDecimal}.
 */
public final class BigDecimals {
  private static final ConcurrentHashMap<String,BigDecimal> instances = new ConcurrentHashMap<>();

  /** The value 2, with a scale of 0. */
  public static final BigDecimal TWO = init("2", BigDecimal.valueOf(2L));

  /** The value {@code E}, with a scale of 15. */
  public static final BigDecimal E = init(String.valueOf(Math.E), BigDecimal.valueOf(Math.E));

  /** The value {@code PI}, with a scale of 15. */
  public static final BigDecimal PI = init(String.valueOf(Math.PI), BigDecimal.valueOf(Math.PI));

  /** The value {@code log(2)}, with a scale of 15. */
  public static final BigDecimal LOG_2 = init(String.valueOf(Constants.LOG_2), BigDecimal.valueOf(Constants.LOG_2));

  /** The value {@code log(10)}, with a scale of 15. */
  public static final BigDecimal LOG_10 = init(String.valueOf(Constants.LOG_10), BigDecimal.valueOf(Constants.LOG_10));

  /**
   * Returns a cached reference to the {@link BigDecimal} object representing
   * the specified string value.
   *
   * @param val The value of the desired {@link BigDecimal} instance.
   * @return A cached reference to the {@link BigDecimal} object representing
   *         the specified string value.
   * @throws NullPointerException If the specified string value is null.
   */
  public static BigDecimal of(final String val) {
    BigDecimal instance = instances.get(Objects.requireNonNull(val));
    if (instance != null)
      return instance;

    synchronized (val.intern()) {
      instance = instances.get(val);
      if (instance != null)
        return instance;

      init(val, instance = new BigDecimal(val));
    }

    return instance;
  }

  private static BigDecimal init(final String str, final BigDecimal val) {
    instances.put(str, val);
    return val;
  }

  private BigDecimals() {
  }
}