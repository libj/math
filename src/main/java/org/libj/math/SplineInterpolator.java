/* Copyright (c) 2020 Seva Safris, LibJ
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
 * Performs spline interpolation given a set of control points.
 */
public class SplineInterpolator {
  private final float[] _x;
  private final float[] _y;
  private final float[] _m;

  private SplineInterpolator(final float[] x, final float[] y, final float[] m) {
    _x = x;
    _y = y;
    _m = m;
  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The
   * spline is guaranteed to pass through each control point exactly. Moreover,
   * assuming the control points are monotonic (Y is non-decreasing or
   * non-increasing) then the interpolated values will also be monotonic. This
   * function uses the <a href=
   * "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a>
   * method for computing the spline parameters.
   *
   * @param x The {@code x} component of the control points, strictly
   *          increasing.
   * @param y The {@code y} component of the control points.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from
   *         the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different
   *           lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static SplineInterpolator createMonotoneCubicSpline(final float[] x, final float[] y) {
    if (x.length != y.length || x.length < 2)
      throw new IllegalArgumentException("Arrays must be of equal length greater than or equal to two");

    final int n = x.length;
    final float[] d = new float[n - 1]; // could optimize this out
    final float[] m = new float[n];

    // Compute slopes of secant lines between successive points
    for (int i = 0; i < n - 1; ++i) {
      final float h = x[i + 1] - x[i];
      if (h <= 0f)
        throw new IllegalArgumentException("Control points must all have strictly increasing x values");

      d[i] = (y[i + 1] - y[i]) / h;
    }

    // Initialize the tangents as the average of the secants
    m[0] = d[0];
    for (int i = 1; i < n - 1; ++i)
      m[i] = (d[i - 1] + d[i]) / 2f;

    m[n - 1] = d[n - 2];

    // Update the tangents to preserve monotonicity
    for (int i = 0; i < n - 1; ++i) {
      if (d[i] == 0f) { // successive Y values are equal
        m[i] = 0f;
        m[i + 1] = 0f;
      }
      else {
        final float a = m[i] / d[i];
        final float b = m[i + 1] / d[i];
        final float h = (float)Math.hypot(a, b);
        if (h > 9f) {
          final float t = 3f / h;
          m[i] = t * a * d[i];
          m[i + 1] = t * b * d[i];
        }
      }
    }

    return new SplineInterpolator(x, y, m);
  }

  /**
   * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of
   * the spline.
   *
   * @param x The X value.
   * @return The interpolated Y = f(X) value.
   */
  public float interpolate(final float x) {
    // Handle the boundary cases.
    final int n = _x.length;
    if (Float.isNaN(x))
      return x;

    if (x <= _x[0])
      return _y[0];

    if (x >= _x[n - 1])
      return _y[n - 1];

    // Find the index 'i' of the last point with smaller x
    // We know this will be within the spline due to the boundary tests
    int i = 0;
    while (x >= _x[i + 1])
      if (x == _x[++i])
        return _y[i];

    // Perform cubic Hermite spline interpolation
    final float h = _x[i + 1] - _x[i];
    final float t = (x - _x[i]) / h;
    return (_y[i] * (1 + 2 * t) + h * _m[i] * t) * (1 - t) * (1 - t) + (_y[i + 1] * (3 - 2 * t) + h * _m[i + 1] * (t - 1)) * t * t;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0, n = _x.length; i < n; ++i) {
      if (i > 0)
        builder.append(", ");

      builder.append('(').append(_x[i]);
      builder.append(", ").append(_y[i]);
      builder.append(": ").append(_m[i]).append(')');
    }

    return builder.append(']').toString();
  }
}