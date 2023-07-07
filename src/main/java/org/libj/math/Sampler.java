/* Copyright (c) 2021 Seva Safris, LibJ
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

public interface Sampler {
  Sampler newInstance();

  /**
   * Add the specified values, and adjust the moving average.
   *
   * @param values The values to add.
   * @throws NullPointerException If {@code values} is null.
   */
  default void accept(final double ... values) {
    for (final double value : values) // [A]
      accept(value);
  }

  /**
   * Accepts the specified {@code value}.
   *
   * @param value The value to accept.
   * @return {@code true} if the provided value changed this instance.
   */
  boolean accept(double value);

  /**
   * Returns the value.
   *
   * @return The value.
   */
  double getValue();

  /**
   * Returns the slope.
   *
   * @return The slope.
   */
  double getSlope();
}