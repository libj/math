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

  /** The {@link BigInteger} constant zero ({@code 0}). */
  public static final BigInteger ZERO = init("0", BigInteger.ZERO);

  /** The {@link BigInteger} constant one ({@code 1}). */
  public static final BigInteger ONE = init("1", BigInteger.ONE);

  /** The {@link BigInteger} constant two ({@code 2}). */
  public static final BigInteger TWO = init("2", BigInteger.valueOf(2));

  /** The {@link BigInteger} constant ten ({@code 10}). */
  public static final BigInteger TEN = init("10", BigInteger.TEN);

  private static BigInteger init(final String str, final BigInteger val) {
    instances.put(str, val);
    return val;
  }

  /**
   * Returns a canonical representation of the {@link BigInteger} object
   * representing the specified string value.
   *
   * @param val The value of the desired {@link BigInteger} instance.
   * @return A canonical representation of the {@link BigInteger} object
   *         representing the specified string value.
   * @throws NullPointerException If the specified string value is null.
   */
  public static BigInteger intern(final String val) {
    final BigInteger intern = instances.get(Objects.requireNonNull(val));
    return intern != null ? intern : init(val, new BigInteger(val));
  }

  /**
   * Returns a canonical representation for the {@link BigInteger} object.
   *
   * @param n The {@link BigInteger} to intern.
   * @return A {@link BigInteger} that has the same contents as the specified
   *         {@link BigInteger}, but is guaranteed to be from a pool of unique
   *         instances.
   * @throws NullPointerException If {@code n} is null.
   */
  public static BigInteger intern(final BigInteger n) {
    final BigInteger instance = instances.putIfAbsent(n.toString(), n);
    return instance != null ? instance : n;
  }

  /**
   * Return a BigInteger equal to the unsigned value of the
   * argument.
   */
  public static BigInteger valueOf(final int signum, final int v) {
    final long signed = Integer.toUnsignedLong(v);
    return BigInteger.valueOf(signum < 0 ? -signed : signed);
  }

  /**
   * Return a BigInteger equal to the unsigned value of the
   * argument.
   */
  public static BigInteger valueOf(final int signum, final long v) {
    if (signum < -1 || signum > 1)
      throw new NumberFormatException("Invalid signum value");

    if (signum == 0) {
      if (v != 0)
        throw new NumberFormatException("signum-magnitude mismatch");

      return BigInteger.ZERO;
    }

    if (v >= 0L)
      return BigInteger.valueOf(signum == -1 ? -v : v);

    // (upper << 32) + lower
    final int upper = (int)(v >>> 32);
    final int lower = (int)v;

    if (signum == -1)
      return BigInteger.valueOf(-Integer.toUnsignedLong(upper)).shiftLeft(32).add(BigInteger.valueOf(-Integer.toUnsignedLong(lower)));

    return BigInteger.valueOf(Integer.toUnsignedLong(upper)).shiftLeft(32).add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
  }

  private BigIntegers() {
  }
}