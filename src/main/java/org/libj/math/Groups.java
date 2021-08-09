/* Copyright (c) 2014 LibJ
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

import java.lang.reflect.Array;

import org.libj.lang.Assertions;

/**
 * Utility class providing functions related to
 * <a href="https://en.wikipedia.org/wiki/Group_theory">Group Theory</a>, such
 * as <a href="https://en.wikipedia.org/wiki/Combination">combinations</a>,
 * and <a href="https://en.wikipedia.org/wiki/Permutation">permutations</a>.
 */
public final class Groups {
  /**
   * Returns an array of all ordered subset permutations of {@code k} elements
   * from a fixed set of {@code elements}.
   *
   * @param <T> The component type of the array.
   * @param elements The elements representing the fixed set.
   * @param k The number of elements representing the subsets.
   * @return An array of all ordered subset permutations of {@code k} elements
   *         from a fixed set of {@code n} elements as {@code int} values from
   *         {@code 0} to {@code n}.
   * @throws ArithmeticException If {@code elements.length} is less than
   *           {@code k}.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T>T[][] permute(final int k, final T ... elements) {
    if (elements.length < k)
      throw new ArithmeticException("n (" + elements.length + ") is less than k (" + k + ")");

    final long size = SafeMath.factorial((long)elements.length) / SafeMath.factorial((long)(elements.length - k));
    final T[][] permutations = (T[][])Array.newInstance(elements.getClass(), (int)size);
    enumerate(elements, (Class<T>)elements.getClass().getComponentType(), elements.length, k, permutations, 0);
    return permutations;
  }

  /**
   * Recursively enumerates and assigns the permutations for the given values,
   * and returns the number of enumerated permutations.
   *
   * @param a The array.
   * @param n The total number of elements.
   * @param k The subset number of elements in the permutation.
   * @param permutations The resulting array of all unordered subsets.
   * @param index The {@code permutations} element index into which the
   *          permutation is to be assigned.
   * @return The number of enumerated permutations.
   */
  @SuppressWarnings("unchecked")
  private static <T>int enumerate(final T[] a, final Class<T> componentType, final int n, final int k, final T[][] permutations, final int index) {
    if (k == 0) {
      final T[] subArray = (T[])Array.newInstance(componentType, a.length - n);
      System.arraycopy(a, n, subArray, 0, subArray.length);
      permutations[index] = subArray;
      return 1;
    }

    int depth = 0;
    for (int i = 0, n1 = n - 1, k1 = k - 1; i < n; ++i) {
      swap(a, i, n1);
      depth += enumerate(a, componentType, n1, k1, permutations, index + depth);
      swap(a, i, n1);
    }

    return depth;
  }

  private static <T>void swap(final T[] a, final int i, final int j) {
    final T temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  /**
   * Returns an array of all ordered subset permutations of {@code k} elements
   * from a fixed set of {@code n} elements as {@code int} values from {@code 0}
   * to {@code n}.
   *
   * @param k The number of elements representing the subsets.
   * @param n The number of elements representing the fixed set.
   * @return An array of all ordered subset permutations of {@code k} elements
   *         from a fixed set of {@code n} elements as {@code int} values from
   *         {@code 0} to {@code n}.
   * @throws ArithmeticException If {@code n} is less than {@code k}.
   */
  public static int[][] permute(final int k, final int n) {
    if (n < k)
      throw new ArithmeticException("n (" + n + ") is less than k (" + k + ")");

    final int[] a = new int[n];
    for (int i = 0; i < n; ++i)
      a[i] = i;

    final long size = SafeMath.factorial((long)n) / SafeMath.factorial((long)(n - k));
    final int[][] permutations = new int[(int)size][];
    enumerate(a, n, k, permutations, 0);
    return permutations;
  }

  /**
   * Recursively enumerates and assigns the permutations for the given values,
   * and returns the number of enumerated permutations.
   *
   * @param a The array.
   * @param n The total number of elements.
   * @param k The subset number of elements in the permutation.
   * @param permutations The resulting array of all unordered subsets.
   * @param index The {@code permutations} element index into which the
   *          permutation is to be assigned.
   * @return The number of enumerated permutations.
   */
  private static int enumerate(final int[] a, final int n, final int k, final int[][] permutations, final int index) {
    if (k == 0) {
      final int[] subArray = new int[a.length - n];
      System.arraycopy(a, n, subArray, 0, subArray.length);
      permutations[index] = subArray;
      return 1;
    }

    int depth = 0;
    for (int i = 0, n1 = n - 1, k1 = k - 1; i < n; ++i) {
      swap(a, i, n1);
      depth += enumerate(a, n1, k1, permutations, index + depth);
      swap(a, i, n1);
    }

    return depth;
  }

  private static void swap(final int[] a, final int i, final int j) {
    final int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  /**
   * Returns an array of all unordered subset combinations of {@code k} elements
   * from a fixed set of {@code elements}.
   *
   * @param <T> The component type of the array.
   * @param elements The elements representing the fixed set.
   * @param k The number of elements representing the subsets.
   * @return An array of all unordered subset combinations of {@code k} elements
   *         from a fixed set of {@code n} elements as {@code int} values from
   *         {@code 0} to {@code n}.
   * @throws ArithmeticException If {@code elements.length} is less than {@code k}.
   * @throws IllegalArgumentException If {@code elements} is null.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T>T[][] combine(final int k, final T ... elements) {
    if (Assertions.assertNotNull(elements).length < k)
      throw new ArithmeticException("elements.length (" + elements.length + ") is less than k (" + k + ")");

    final long size = SafeMath.factorial((long)elements.length) / (SafeMath.factorial((long)k) * SafeMath.factorial((long)(elements.length - k)));
    final T[][] permutations = (T[][])Array.newInstance(elements.getClass(), (int)size);
    combine(elements, k, 0, (T[])Array.newInstance(elements.getClass().getComponentType(), k), permutations, 0);
    return permutations;
  }

  private static <T>int combine(final T[] a, final int k, final int start, final T[] combination, final T[][] combinations, final int index) {
    if (k == 0) {
      combinations[index] = combination.clone();
      return 1;
    }

    int depth = 0;
    for (int i = start, len = a.length - k; i <= len; ++i) {
      combination[combination.length - k] = a[i];
      depth += combine(a, k - 1, i + 1, combination, combinations, index + depth);
    }

    return depth;
  }

  /**
   * Returns an array of all unordered subset combinations of {@code k} elements
   * from a fixed set of {@code n} elements as {@code int} values from {@code 0}
   * to {@code n}.
   *
   * @param k The number of elements representing the subsets.
   * @param n The number of elements representing the fixed set.
   * @return An array of all unordered subset combinations of {@code k} elements
   *         from a fixed set of {@code n} elements as {@code int} values from
   *         {@code 0} to {@code n}.
   * @throws ArithmeticException If {@code n} is less than {@code k}.
   */
  public static int[][] combine(final int k, final int n) {
    if (n < k)
      throw new ArithmeticException("n (" + n + ") is less than k (" + k + ")");

    final int[] a = new int[n];
    for (int i = 0; i < n; ++i)
      a[i] = i;

    final long size = SafeMath.factorial((long)n) / (SafeMath.factorial((long)k) * SafeMath.factorial((long)(n - k)));
    final int[][] permutations = new int[(int)size][];
    combine(a, k, 0, new int[k], permutations, 0);
    return permutations;
  }

  private static int combine(final int[] a, final int k, final int start, final int[] combination, final int[][] combinations, final int index) {
    if (k == 0) {
      combinations[index] = combination.clone();
      return 1;
    }

    int depth = 0;
    for (int i = start, len = a.length - k; i <= len; ++i) {
      combination[combination.length - k] = a[i];
      depth += combine(a, k - 1, i + 1, combination, combinations, index + depth);
    }

    return depth;
  }

  /**
   * Permutes all subsets of elements in the specified 2-dimensional array,
   * where:
   * <ul>
   * <li>{@code n} = the total number of elements in the 2-dimensional
   * array.</li>
   * <li>{@code r} = the length of the first dimension of the 2-dimensional
   * array.</li>
   * </ul>
   * Time Complexity: {@code O(n choose r)}
   *
   * @param <T> The component type of the array.
   * @param a The 2-dimensional array.
   * @return A 2-dimensional array of combination sets for {@code a}.
   * @throws ArrayIndexOutOfBoundsException If {@code a.length == 0}.
   * @throws IllegalArgumentException If {@code a} or any array member of
   *           {@code a} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[][] permute(final T[][] a) {
    int total = Assertions.assertNotNull(a)[0].length;
    for (int i = 1; i < a.length; ++i)
      total *= Assertions.assertNotNull(a[i]).length;

    final Class<?> componentType1 = a.getClass().getComponentType();
    final T[][] combinations = (T[][])Array.newInstance(componentType1, total);
    final Class<?> componentType2 = componentType1.getComponentType();
    for (; total > 0; --total) {
      final T[] currentSet = (T[])Array.newInstance(componentType2, a.length);
      int position = total;

      // Pick the required element from each list, and add it to the set
      for (int i = 0, len; i < a.length; ++i) {
        len = a[i].length;
        currentSet[i] = a[i][position % len];
        position /= len;
      }

      combinations[total - 1] = currentSet;
    }

    return combinations;
  }

  private Groups() {
  }
}