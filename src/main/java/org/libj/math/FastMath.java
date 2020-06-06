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

/**
 * Alternative functions supplementing those in {@link java.lang.Math} that
 * offer higher performance algorithms.
 */
public final class FastMath {
  /**
   * Returns the base 2 logarithm of positive {@code int} values, and {@code 0}
   * for negative values.
   * <p>
   * This method differentiates itself from {@link java.lang.Math#log(double)}
   * with a high performance algorithm specific to {@code int} arguments.
   *
   * @param n The value.
   * @return The base 2 logarithm of positive {@code int} values, and {@code 0}
   *         for negative values.
   */
  public static byte log2(final int n) {
    return (byte)(n <= 0 ? 0 : 31 - Integer.numberOfLeadingZeros(n - 1));
  }

  /**
   * Returns the base 2 logarithm of positive {@code long} values, and {@code 0}
   * for negative values.
   * <p>
   * This method differentiates itself from {@link java.lang.Math#log(double)}
   * with a high performance algorithm specific to {@code long} arguments.
   *
   * @param n The value.
   * @return The base 2 logarithm of positive {@code long} values, and {@code 0}
   *         for negative values.
   */
  public static byte log2(final long n) {
    return (byte)(n <= 0 ? 0 : 63 - Long.numberOfLeadingZeros(n - 1));
  }

  private FastMath() {
  }
}