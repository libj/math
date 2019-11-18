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

/**
 * Utility that supplements functions in {@link Math}.
 */
public final class FastMath {
  /**
   * Returns the value of the first argument raised to the power of the second
   * argument.
   * <p>
   * Note this method only works for exponents that are non-negative.
   * <p>
   * The complexity of this implementation is {@code O(log(b))}.
   *
   * @param a The base.
   * @param b The exponent.
   * @return The value {@code a}<sup>{@code b}</sup>.
   * @throws IllegalArgumentException If the exponent is negative.
   */
  public static long pow(long a, long b) {
    if (b <= 0)
      throw new IllegalArgumentException("Exponent must be non-negative: " + b);

    long p = 1;
    while (b > 0) {
      if ((b & 1) == 1)
        p *= a;

      b >>= 1;
      a *= a;
    }

    return p;
  }

  /**
   * Parses an array of characters with digits to an {@code int}. A leading
   * {@code '-'} character is allowed.
   *
   * @param chars The array of characters with digits.
   * @return An {@code int} equivalent to the array of characters with digits.
   * @throws IllegalArgumentException If the specified array is empty.
   * @throws NullPointerException If the specified array is null.
   */
  public static int parseInt(final char[] chars) {
    if (chars.length == 0)
      throw new IllegalArgumentException("Empty array");

    int result = 0;
    final int len = chars.length - 1;
    for (int i = len; i >= 0; --i) {
      final char ch = chars[i];
      if (i == 0 && ch == '-') {
        if (len == 0)
          throw new NumberFormatException(new String(chars));

        result *= -1;
        break;
      }

      if (ch < '0' || '9' < ch)
        throw new NumberFormatException("Not a digit: '" + ch + "'");

      int digit = ch & 0xF;
      for (int j = i; j < len; ++j)
        digit *= 10;

      result += digit;
    }

    return result;
  }

  /**
   * Assert the specified radix is within legal range.
   *
   * @param radix The radix to assert.
   * @throws NumberFormatException If the specified radix is outside the range
   *           of legal values.
   */
  private static void assertRadix(final int radix) {
    if (radix < Character.MIN_RADIX)
      throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");

    if (radix > Character.MAX_RADIX)
      throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
  }

  /**
   * Parses an array of characters with digits to an {@code int}. A leading
   * {@code '-'} character is allowed.
   *
   * @param chars The array of characters with digits.
   * @param radix The radix to be used while parsing the array.
   * @return An {@code int} equivalent to the array of characters with digits.
   * @throws NumberFormatException If the specified radix is outside the range
   *           of legal values, or if the array does not contain a parsable
   *           {@code int}.
   * @throws IllegalArgumentException If the specified array is empty.
   * @throws NullPointerException If the specified array is null.
   */
  public static int parseInt(final char[] chars, final int radix) {
    if (chars.length == 0)
      throw new IllegalArgumentException("Empty array");

    assertRadix(radix);

    final int len = chars.length - 1;
    int result = 0;
    for (int i = len; i >= 0; --i) {
      final char ch = chars[i];
      if (i == 0 && ch == '-') {
        if (len == 0)
          throw new NumberFormatException(new String(chars));

        result *= -1;
        break;
      }

      if (!isDigit(ch, radix))
        throw new NumberFormatException("Not a digit: '" + ch + "'");

      int digit = Character.digit(ch, radix);
      for (int j = i; j < len; ++j)
        digit *= radix;

      result += digit;
    }

    return result;
  }

  /**
   * Determines if the specified character is a digit in the provided radix.
   *
   * @param digit The character to test.
   * @param radix The radix to test against.
   * @return {@code true} if the character is a digit in the provided radix;
   *         {@code false} otherwise.
   * @throws NumberFormatException If the specified radix is outside the range
   *           of legal values.
   */
  public static boolean isDigit(final char digit, final int radix) {
    final int val = digit(digit, radix);
    return 0 <= val && val < radix;
  }

  /**
   * Returns the numeric value of the specified character representing a digit.
   * The specified character must be within the following ranges:
   * <ol>
   * <li>{@code '0' <= digit && digit <= '9'}</li>
   * <li>{@code 'a' <= digit && digit <= 'a'}</li>
   * <li>{@code 'A' <= digit && digit <= 'Z'}</li>
   * </ol>
   * <p>
   * If the specified character is outside these ranges, the value
   * {@code -digit} is returned.
   *
   * @param digit The character representing a digit.
   * @param radix The radix to be used to transform the character.
   * @return The numeric value of the specified character representing a digit.
   * @throws NumberFormatException If the specified radix is outside the range
   *           of legal values.
   */
  public static int digit(final char digit, final int radix) {
    assertRadix(radix);
    if ('0' <= digit && digit <= '9')
      return digit - '0';

    if ('a' <= digit && digit <= 'z')
      return digit + 10 - 'a';

    if ('A' <= digit && digit <= 'Z')
      return digit + 10 - 'A';

    return -digit;
  }

  /**
   * Parses an array of characters with digits to a {@code long}. A leading
   * {@code '-'} character is allowed.
   *
   * @param chars The array of characters with digits.
   * @return An {@code long} equivalent to the array of characters with digits.
   * @throws IllegalArgumentException If the specified array is empty.
   * @throws NullPointerException If the specified array is null.
   */
  public static long parseLong(final char[] chars) {
    if (chars.length == 0)
      throw new IllegalArgumentException("Empty array");

    long result = 0;
    final int len = chars.length - 1;
    for (int i = len; i >= 0; --i) {
      final char ch = chars[i];
      if (i == 0 && ch == '-') {
        if (len == 0)
          throw new NumberFormatException(new String(chars));

        result *= -1;
        break;
      }

      if (ch < '0' || '9' < ch)
        throw new NumberFormatException("Not a digit: '" + ch + "'");

      long digit = ch & 0xF;
      for (int j = i; j < len; ++j)
        digit *= 10;

      result += digit;
    }

    return result;
  }

  /**
   * Parses an array of characters with digits to a {@code long}. A leading
   * {@code '-'} character is allowed.
   *
   * @param chars The array of characters with digits.
   * @param radix The radix to be used while parsing the array.
   * @return An {@code long} equivalent to the array of characters with digits.
   * @throws NumberFormatException If the specified radix is outside the range
   *           of legal values, or if the array does not contain a parsable
   *           {@code long}.
   * @throws IllegalArgumentException If the specified array is empty.
   * @throws NullPointerException If the specified array is null.
   */
  public static long parseLong(final char[] chars, final int radix) {
    if (chars.length == 0)
      throw new IllegalArgumentException("Empty array");

    assertRadix(radix);

    final int len = chars.length - 1;
    long result = 0;
    for (int i = len; i >= 0; --i) {
      final char ch = chars[i];
      if (i == 0 && ch == '-') {
        if (len == 0)
          throw new NumberFormatException(new String(chars));

        result *= -1;
        break;
      }

      if (!isDigit(ch, radix))
        throw new NumberFormatException("Not a digit: '" + ch + "'");

      long digit = Character.digit(ch, radix);
      for (int j = i; j < len; ++j)
        digit *= radix;

      result += digit;
    }

    return result;
  }

  private FastMath() {
  }
}