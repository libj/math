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

import static org.libj.lang.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.libj.util.primitive.DoubleList;

/**
 * Performs spline interpolation given a set of control points.
 *
 * @param <C> The type parameter of the coordinate object.
 */
public class SplineInterpolator<C> {
  public interface Adapter<C> {
    double get(C data, int d, int i);
    int size(C data);
  }

  private static final Adapter<float[][]> FLOAT_ARRAY = new Adapter<float[][]>() {
    @Override
    public double get(final float[][] data, final int d, final int i) {
      return data[d][i];
    }

    @Override
    public int size(final float[][] data) {
      return data[0].length;
    }
  };
  private static final Adapter<double[][]> DOUBLE_ARRAY = new Adapter<double[][]>() {
    @Override
    public double get(final double[][] data, final int d, final int i) {
      return data[d][i];
    }

    @Override
    public int size(final double[][] data) {
      return data[0].length;
    }
  };
  private static final Adapter<DoubleList[]> DOUBLE_LIST = new Adapter<DoubleList[]>() {
    @Override
    public double get(final DoubleList[] data, final int d, final int i) {
      return data[d].get(i);
    }

    @Override
    public int size(final DoubleList[] data) {
      return data[0].size();
    }
  };
  private static final Adapter<List<Double>[]> LIST_DOUBLE = new Adapter<List<Double>[]>() {
    @Override
    public double get(final List<Double>[] data, final int d, final int i) {
      return data[d].get(i);
    }

    @Override
    public int size(final List<Double>[] data) {
      return data[0].size();
    }
  };

  /**
   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
   * values will also be monotonic. This function uses the
   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
   * parameters.
   *
   * @param x The {@code x} component of the control points, strictly increasing.
   * @param y The {@code y} component of the control points.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static SplineInterpolator<?> createMonotoneCubicSpline(final float[] x, final float[] y) {
    return createMonotoneCubicSpline(new float[][] {x, y}, FLOAT_ARRAY);
  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
   * values will also be monotonic. This function uses the
   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
   * parameters.
   *
   * @param x The {@code x} component of the control points, strictly increasing.
   * @param y The {@code y} component of the control points.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static SplineInterpolator<?> createMonotoneCubicSpline(final double[] x, final double[] y) {
    return createMonotoneCubicSpline(new double[][] {x, y}, DOUBLE_ARRAY);
  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
   * values will also be monotonic. This function uses the
   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
   * parameters.
   *
   * @param x The {@code x} component of the control points, strictly increasing.
   * @param y The {@code y} component of the control points.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  @SuppressWarnings("unchecked")
  public static SplineInterpolator<?> createMonotoneCubicSpline(final List<Double> x, final List<Double> y) {
    return createMonotoneCubicSpline(new List[] {x, y}, LIST_DOUBLE);
  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
   * values will also be monotonic. This function uses the
   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
   * parameters.
   *
   * @param x The {@code x} component of the control points, strictly increasing.
   * @param y The {@code y} component of the control points.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static SplineInterpolator<?> createMonotoneCubicSpline(final DoubleList x, final DoubleList y) {
    return createMonotoneCubicSpline(new DoubleList[] {x, y}, DOUBLE_LIST);
  }

//  /**
//   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
//   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
//   * values will also be monotonic. This function uses the
//   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
//   * parameters.
//   *
//   * @param xy The array of {@code T} component of the control points, strictly increasing.
//   * @param y The {@code y} component of the control points.
//   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
//   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
//   * @throws NullPointerException If {@code x} or {@code y} is null.
//   */
//  public static <C>SplineInterpolator<C> createMonotoneCubicSpline(final C[] xy, final Adapter<C> y) {
//    return createMonotoneCubicSpline(xy, y);
//  }

  /**
   * Creates a monotone cubic spline from a given set of control points. The spline is guaranteed to pass through each control point
   * exactly. Moreover, assuming the control points are monotonic (Y is non-decreasing or non-increasing) then the interpolated
   * values will also be monotonic. This function uses the
   * <a href= "http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">Fritsch-Carlson</a> method for computing the spline
   * parameters.
   *
   * @param <C> The type parameter of the coordinate object.
   * @param xy The control points, strictly increasing.
   * @param adapter The {@link Adapter} for conversion of {@code xy} control points of type {@code &lt;T&gt;} to {@code double}.
   * @return A new {@link SplineInterpolator} of a monotone cubic spline from the given set of control points.
   * @throws IllegalArgumentException If {@code x} or {@code y} have different lengths or fewer than 2 values.
   * @throws NullPointerException If {@code x} or {@code y} is null.
   */
  public static <C>SplineInterpolator<C> createMonotoneCubicSpline(final C xy, final Adapter<C> adapter) {
    final int n = adapter.size(xy), n1 = n - 1, n2 = n - 2;
    if (adapter.size(xy) < 2)
      throw new IllegalArgumentException("Arrays must be of equal length greater than or equal to two");

    final double[] d = new double[n1]; // could optimize this out
    final double[] m = new double[n];

    // Compute slopes of secant lines between successive points
    for (int i = 0; i < n1; ++i) { // [A]
      final double h = adapter.get(xy, 0, i + 1) - adapter.get(xy, 0, i);
      if (h <= 0)
        throw new IllegalArgumentException("Control points must all have strictly increasing x values");

      d[i] = (adapter.get(xy, 1, i + 1) - adapter.get(xy, 1, i)) / h;
    }

    // Initialize the tangents as the average of the secants
    m[0] = d[0];
    for (int i = 1; i < n1; ++i) // [A]
      m[i] = (d[i - 1] + d[i]) / 2;

    m[n1] = d[n2];

    // Update the tangents to preserve monotonicity
    for (int i = 0; i < n1; ++i) { // [A]
      if (d[i] == 0) { // successive Y values are equal
        m[i] = 0;
        m[i + 1] = 0;
      }
      else {
        final double a = m[i] / d[i];
        final double b = m[i + 1] / d[i];
        final double h = Math.hypot(a, b);
        if (h > 9) {
          final double t = 3 / h;
          m[i] = t * a * d[i];
          m[i + 1] = t * b * d[i];
        }
      }
    }

    return new SplineInterpolator<>(adapter, xy, m);
  }

  private final Adapter<C> adapter;
  private final C _xy;
  private final double[] _m;

  private SplineInterpolator(final Adapter<C> adapter, final C xy, final double[] m) {
    this.adapter = Objects.requireNonNull(adapter);
    _xy = Objects.requireNonNull(xy);
    assertPositive(m.length);
    _m = m;
  }

  /**
   * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of the spline.
   *
   * @param x The X value.
   * @return The interpolated Y = f(X) value.
   */
  public double interpolate(final double x) {
    // Handle the boundary cases.
    final int n = adapter.size(_xy), n1 = n - 1;
    if (Double.isNaN(x))
      return x;

    if (x <= adapter.get(_xy, 0, 0))
      return adapter.get(_xy, 1, 0);

    if (x >= adapter.get(_xy, 0, n1))
      return adapter.get(_xy, 1, n1);

    // Find the index 'i' of the last point with smaller x
    // We know this will be within the spline due to the boundary tests
    int i = 0;
    while (x >= adapter.get(_xy, 0, i + 1))
      if (x == adapter.get(_xy, 0, ++i))
        return adapter.get(_xy, 1, i);

    // Perform cubic Hermite spline interpolation
    final double h = adapter.get(_xy, 0, i + 1) - adapter.get(_xy, 0, i);
    final double t = (x - adapter.get(_xy, 0, i)) / h;
    return (adapter.get(_xy, 1, i) * (1 + 2 * t) + h * _m[i] * t) * (1 - t) * (1 - t) + (adapter.get(_xy, 1, i + 1) * (3 - 2 * t) + h * _m[i + 1] * (t - 1)) * t * t;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0, i$ = adapter.size(_xy); i < i$; ++i) { // [A]
      if (i > 0)
        builder.append(", ");

      builder.append('(').append(adapter.get(_xy, 0, i));
      builder.append(", ").append(adapter.get(_xy, 1, i));
      builder.append(": ").append(_m[i]).append(')');
    }

    return builder.append(']').toString();
  }
}