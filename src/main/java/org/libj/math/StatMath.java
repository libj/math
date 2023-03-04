/* Copyright (c) 2008 Seva Safris, LibJ
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

import java.math.BigInteger;

/**
 * Utility that supplements functions in {@link Math}, providing implementations of functions that pertain to statistics.
 */
public final class StatMath {
  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final byte ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final short ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final int ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final long ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final float ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the root mean square of an array of values.
   *
   * @param values The values.
   * @return The rms.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double rms(final double ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double rms = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      rms += values[i] * values[i];

    return Math.sqrt(rms / values.length);
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static byte min(final byte ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    byte min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static short min(final short ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    short min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int min(final int ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    int min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static long min(final long ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    long min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static float min(final float ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    float min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the minimum value of an array of values.
   *
   * @param values The values.
   * @return The minimum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double min(final double ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double min = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] < min)
        min = values[i];

    return min;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static byte max(final byte ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    byte max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static short max(final short ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    short max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int max(final int ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    int max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static long max(final long ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    long max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static float max(final float ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    float max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Calculate the maximum value of an array of values.
   *
   * @param values The values.
   * @return The maximum value.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double max(final double ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values.length == 0");

    double max = values[0];
    for (int i = 1, i$ = values.length; i < i$; ++i) // [A]
      if (values[i] > max)
        max = values[i];

    return max;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static byte threshold(final byte value, final byte min, final byte max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static short threshold(final short value, final short min, final short max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static int threshold(final int value, final int min, final int max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static long threshold(final long value, final long min, final long max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static float threshold(final float value, final float min, final float max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Test if a value is within the threshold of a min and a max. If the value is less than min, this method will return min. If the
   * value is greater than max, this method will return max.
   *
   * @param value The value to test.
   * @param min The minimum accepted value.
   * @param max The maximum accepted value.
   * @return The value complying to the threshold.
   */
  public static double threshold(final double value, final double min, final double max) {
    return value < min ? min : max < value ? max : value;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final byte ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / (double)values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final short ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / (double)values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final int ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / (double)values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final long ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / (double)values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final float ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / (double)values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The vararg array of values.
   * @return The average value, or {@code Double.NaN} if length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   */
  public static double avg(final double ... values) {
    if (values.length == 0)
      return Double.NaN;

    return sum(values) / values.length;
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final byte[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (double)(toIndex - fromIndex);
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final short[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (double)(toIndex - fromIndex);
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final int[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (double)(toIndex - fromIndex);
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final long[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (double)(toIndex - fromIndex);
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final float[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (double)(toIndex - fromIndex);
  }

  /**
   * Compute the average of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The average value, or {@code Double.NaN} if the selected length of {@code values} is zero.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double avg(final double[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0 || fromIndex == toIndex)
      return Double.NaN;

    assertRangeArray(fromIndex, toIndex, values.length);
    return sum(values, fromIndex, toIndex) / (toIndex - fromIndex);
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int sum(final byte ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    int sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int sum(final short ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    int sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static long sum(final int ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    long sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static long sum(final long ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    long sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static float sum(final float ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    float sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array.
   *
   * @param values The vararg array of values.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double sum(final double ... values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static int sum(final byte[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    int sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static int sum(final short[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    int sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static int sum(final int[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    int sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static long sum(final long[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    long sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static float sum(final float[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    float sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of the members of the argument array.
   *
   * @param values The array of values.
   * @param fromIndex Start index from which to aggregate.
   * @param toIndex End index to which to aggregate.
   * @return The sum.
   * @throws NullPointerException If {@code values} is null, is empty, or if {@code fromIndex >= toIndex}.
   * @throws IndexOutOfBoundsException If {@code fromIndex} or {@code toIndex} are out of range
   *           ({@code fromIndex < 0 || values.length < toIndex}).
   */
  public static double sum(final double[] values, final int fromIndex, final int toIndex) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    assertRangeArray(fromIndex, toIndex, values.length);

    int sum = 0;
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      sum += values[i];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array. This method accepts a 2 dimensional array, allowing one to iterate through a
   * specific dimension, 0 or 1.
   *
   * @param values The 2 dimensional array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int sum(final short[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    int sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i][dimension];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array. This method accepts a 2 dimensional array, allowing one to iterate through a
   * specific dimension, 0 or 1.
   *
   * @param values The 2 dimensional array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static int sum(final int[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    int sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i][dimension];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array. This method accepts a 2 dimensional array, allowing one to iterate through a
   * specific dimension, 0 or 1.
   *
   * @param values The 2 dimensional array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static long sum(final long[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    long sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i][dimension];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array. This method accepts a 2 dimensional array, allowing one to iterate through a
   * specific dimension, 0 or 1.
   *
   * @param values The 2 dimensional array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static float sum(final float[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    float sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i][dimension];

    return sum;
  }

  /**
   * Compute the sum of values in the argument array. This method accepts a 2 dimensional array, allowing one to iterate through a
   * specific dimension, 0 or 1.
   *
   * @param values The 2 dimensional array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The sum of values in the argument array.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double sum(final double[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += values[i][dimension];

    return sum;
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final byte[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final short[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final int[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final long[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final float[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array.
   *
   * @param values The array of values.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final double[] values) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i] - u) * (values[i] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array. This method accepts a 2 dimensional array, allowing one
   * to iterate through a specific dimension, 0 or 1.
   *
   * @param values The array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final short[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values, dimension) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i][dimension] - u) * (values[i][dimension] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array. This method accepts a 2 dimensional array, allowing one
   * to iterate through a specific dimension, 0 or 1.
   *
   * @param values The array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final int[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values, dimension) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i][dimension] - u) * (values[i][dimension] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array. This method accepts a 2 dimensional array, allowing one
   * to iterate through a specific dimension, 0 or 1.
   *
   * @param values The array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final long[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values, dimension) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i][dimension] - u) * (values[i][dimension] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array. This method accepts a 2 dimensional array, allowing one
   * to iterate through a specific dimension, 0 or 1.
   *
   * @param values The array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final float[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values, dimension) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i][dimension] - u) * (values[i][dimension] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * Calculate the standard deviation of the values in the argument array. This method accepts a 2 dimensional array, allowing one
   * to iterate through a specific dimension, 0 or 1.
   *
   * @param values The array of values.
   * @param dimension The index of dimension that should be aggregated.
   * @return The standard deviation of the values.
   * @throws NullPointerException If {@code values} is null.
   * @throws IllegalArgumentException If {@code values} is empty.
   */
  public static double stdDev(final double[][] values, final int dimension) {
    if (values.length == 0)
      throw new IllegalArgumentException("values is empty");

    final double u = sum(values, dimension) / values.length;
    double sum = 0;
    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      sum += (values[i][dimension] - u) * (values[i][dimension] - u);

    return Math.sqrt(sum / values.length);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final byte[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (byte)((values[i] - mean) / scale);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final short[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (short)((values[i] - mean) / scale);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final int[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (int)((values[i] - mean) / scale);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final long[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (long)((values[i] - mean) / scale);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final float[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (float)((values[i] - mean) / scale);
  }

  /**
   * In-place normalization of argument values.
   *
   * @param values The values to normalize.
   * @throws NullPointerException If {@code values} is null.
   */
  public static void normalize(final double[] values) {
    if (values.length <= 1)
      return;

    double sumLinear = 0d;
    double sumSquares = 0d;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      sumLinear += values[i];
      sumSquares += values[i] * values[i];
    }

    final double length = values.length;
    final double mean = sumLinear / length;
    double scale = Math.sqrt((sumSquares - sumLinear * mean) / length);
    if (scale == 0d)
      scale = 1d;

    for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
      values[i] = (values[i] - mean) / scale;
  }

  /**
   * Calculate the binomial coefficient of the expression {@code (n choose k)}.
   *
   * @param n Pool size.
   * @param k Selection size.
   * @return The binomial coefficient of {@code (n choose k)}.
   */
  public static BigInteger binomial(final int n, final int k) {
    BigInteger binomial = BigInteger.ONE;
    for (int i = 0; i < k; ++i) // [N]
      binomial = binomial.multiply(BigInteger.valueOf(n - i)).divide(BigInteger.valueOf(i + 1));

    return binomial;
  }

  private StatMath() {
  }
}