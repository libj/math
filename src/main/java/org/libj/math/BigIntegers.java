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

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility functions for operations pertaining to {@link BigInteger}.
 */
public final class BigIntegers {
  private static final ConcurrentHashMap<String,BigInteger> instances = new ConcurrentHashMap<>();

  /** The {@link BigInteger} constant two. */
  public static final BigInteger TWO = init("2", BigInteger.valueOf(2));

  /**
   * Returns a cached reference to the {@link BigInteger} object representing
   * the specified string value.
   *
   * @param val The value of the desired {@link BigInteger} instance.
   * @return A cached reference to the {@link BigInteger} object representing
   *         the specified string value.
   */
  public static BigInteger of(final String val) {
    BigInteger instance = instances.get(Objects.requireNonNull(val));
    if (instance != null)
      return instance;

    synchronized (val.intern()) {
      instance = instances.get(val);
      if (instance != null)
        return instance;

      init(val, instance = new BigInteger(val));
    }

    return instance;
  }

  private static BigInteger init(final String str, final BigInteger val) {
    instances.put(str, val);
    return val;
  }

  private static final ConcurrentHashMap<String,BigInteger> interns = new ConcurrentHashMap<>();

  /**
   * Returns a canonical representation for the {@link BigInteger} object.
   *
   * @param n The {@link BigInteger} to intern.
   * @return A {@link BigInteger} that has the same contents as the specified
   *         {@link BigInteger}, but is guaranteed to be from a pool of unique
   *         instances.
   */
  public static BigInteger intern(final BigInteger n) {
    final BigInteger intern = interns.putIfAbsent(n.toString(), n);
    return intern != null ? intern : n;
  }

  private BigIntegers() {
  }
}