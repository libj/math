/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.libj.math;

/**
 * Performs spline interpolation given a set of control points.
 */
public class SplineInterpolator {
  private final float[] mX;
  private final float[] mY;
  private final float[] mM;

  private SplineInterpolator(float[] x, float[] y, float[] m) {
    mX = x;
    mY = y;
    mM = m;
  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The
   * spline is guaranteed to pass through each control point exactly. Moreover,
   * assuming the control points are monotonic (Y is non-decreasing or
   * non-increasing) then the interpolated values will also be monotonic. This
   * function uses the Fritsch-Carlson method for computing the spline
   * parameters. http://en.wikipedia.org/wiki/Monotone_cubic_interpolation
   *
   * @param x The X component of the control points, strictly increasing.
   * @param y The Y component of the control points
   * @return
   * @throws IllegalArgumentException If the {@code x} or {@code y} have
   *           different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static SplineInterpolator createMonotoneCubicSpline(final float[] x, final float[] y) {
    if (x.length != y.length || x.length < 2)
      throw new IllegalArgumentException("Arrays must be of equal length greater than or equal to two");

    final int n = x.length;
    final float[] d = new float[n - 1]; // could optimize this out
    final float[] m = new float[n];

    // Compute slopes of secant lines between successive points.
    for (int i = 0; i < n - 1; ++i) {
      final float h = x[i + 1] - x[i];
      if (h <= 0f)
        throw new IllegalArgumentException("Control points must all have strictly increasing X values");

      d[i] = (y[i + 1] - y[i]) / h;
    }

    // Initialize the tangents as the average of the secants.
    m[0] = d[0];
    for (int i = 1; i < n - 1; ++i)
      m[i] = (d[i - 1] + d[i]) / 2f;

    m[n - 1] = d[n - 2];

    // Update the tangents to preserve monotonicity.
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
  public float interpolate(float x) {
    // Handle the boundary cases.
    final int n = mX.length;
    if (Float.isNaN(x))
      return x;

    if (x <= mX[0])
      return mY[0];

    if (x >= mX[n - 1])
      return mY[n - 1];

    // Find the index 'i' of the last point with smaller X.
    // We know this will be within the spline due to the boundary tests.
    int i = 0;
    while (x >= mX[i + 1]) {
      i += 1;
      if (x == mX[i])
        return mY[i];
    }

    // Perform cubic Hermite spline interpolation.
    final float h = mX[i + 1] - mX[i];
    final float t = (x - mX[i]) / h;
    return (mY[i] * (1 + 2 * t) + h * mM[i] * t) * (1 - t) * (1 - t) + (mY[i + 1] * (3 - 2 * t) + h * mM[i + 1] * (t - 1)) * t * t;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0, n = mX.length; i < n; ++i) {
      if (i > 0)
        builder.append(", ");

      builder.append('(').append(mX[i]);
      builder.append(", ").append(mY[i]);
      builder.append(": ").append(mM[i]).append(')');
    }

    return builder.append(']').toString();
  }
}