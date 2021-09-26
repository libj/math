/* Copyright (c) 2020 Seva Safris, LibJ
 * Copyright (c) 2015-2016 Simon Klein, Google Inc.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of the Huldra and the LibJ projects.
 */

package org.libj.math;

import java.math.BigInteger;
import java.math.RoundingMode;

abstract class BigIntPrimitive extends BigIntBinary {
  private static final double MIN_LONG_AS_DOUBLE = -0x1p63;

  /* We cannot store Long.MAX_VALUE as a double without losing precision.
   * Instead, store Long.MAX_VALUE + 1 == -Long.MIN_VALUE, and then offset all
   * comparisons by 1. */
  private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 0x1p63;

  /**
   * Returns {@code true} if {@code x} represents a mathematical integer.
   * <p>
   * This is equivalent to, but not necessarily implemented as, the expression
   *
   * <pre>
   * !Double.isNaN(x) && !Double.isInfinite(x) && x == Math.rint(x)
   * </pre>
   */
  private static boolean isMathematicalInteger(final double x, final int exponent, final long significand) {
    return x == 0.0 || FloatingDecimal.SIGNIFICAND_BITS_DOUBLE - Long.numberOfTrailingZeros(significand) <= exponent;
  }

  /*
   * This method returns a value y such that rounding y DOWN (towards zero)
   * gives the same result as rounding x according to the specified mode.
   */
  private static double roundIntermediate(final double x, final int exponent, final long significand, final RoundingMode rm) {
    if (!Double.isFinite(x))
      return Double.NaN;

    if (rm == RoundingMode.FLOOR)
      return x >= 0.0 || isMathematicalInteger(x, exponent, significand) ? x : (long)x - 1;

    if (rm == RoundingMode.CEILING)
      return x <= 0.0 || isMathematicalInteger(x, exponent, significand) ? x : (long)x + 1;

    if (rm == RoundingMode.DOWN)
      return x;

    if (rm == RoundingMode.UP)
      return isMathematicalInteger(x, exponent, significand) ? x : (long)x + (x > 0 ? 1 : -1);

    if (rm == RoundingMode.HALF_UP) {
      final double z = Math.rint(x);
      return Math.abs(x - z) == 0.5 ? x + Math.copySign(0.5, x) : z;
    }

    if (rm == RoundingMode.HALF_DOWN) {
      final double z = Math.rint(x);
      return Math.abs(x - z) == 0.5 ? x : z;
    }

    if (rm == RoundingMode.HALF_EVEN)
      return Math.rint(x);

    return isMathematicalInteger(x, exponent, significand) ? x : Double.NaN; // RoundingMode.UNNECESSARY
  }

  /**
   * Assigns the specified {@code double} value to the provided
   * {@linkplain BigInt#val() value-encoded <code>int[]</code>}, rounded by the
   * provided {@link RoundingMode}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param mag The {@code double} value to assign.
   * @param rm The {@link RoundingMode}.
   * @return The result of assigning the specified {@code double} value to the
   *         provided {@linkplain BigInt#val() value-encoded
   *         <code>int[]</code>}, rounded by the provided {@link RoundingMode},
   *         or {@code null} if {@code mag} is not a finite value or
   *         {@code rm == RoundingMode.UNNECESSARY || rm == null} yet rounding
   *         is necessary.
   * @complexity O(n)
   */
  public static int[] assign(final int[] val, final double mag, final RoundingMode rm) {
    return assign(val, mag, rm, false);
  }

  protected static int[] assignInPlace(final int[] val, final double mag, final RoundingMode rm) {
    return assign(val, mag, rm, true);
  }

  /**
   * Assigns the specified {@code double} value to the provided
   * {@linkplain BigInt#val() value-encoded <code>int[]</code>}, assuming
   * rounding is not necessary.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the
   *           assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param mag The {@code double} value to assign.
   * @return The result of assigning the specified {@code double} value to the
   *         provided {@linkplain BigInt#val() value-encoded
   *         <code>int[]</code>}, rounded by the provided {@link RoundingMode},
   *         or {@code null} if {@code mag} is not a finite value or if rounding
   *         is necessary.
   * @complexity O(n)
   */
  public static int[] assign(final int[] val, final double mag) {
    return assign(val, mag, null, false);
  }

  protected static int[] assignInPlace(final int[] val, final double mag) {
    return assign(val, mag, null, true);
  }

  private static int[] assign(int[] val, double mag, final RoundingMode rm, final boolean inPlace) {
    if (!Double.isFinite(mag))
      return null;

    int exponent = Math.getExponent(mag);
    long significand = FloatingDecimal.getSignificand(mag, exponent);
    mag = roundIntermediate(mag, exponent, significand, rm);
    if (MIN_LONG_AS_DOUBLE - mag < 1 & mag < MAX_LONG_AS_DOUBLE_PLUS_ONE)
      return inPlace ? assignInPlace(val, (long)mag) : assign(val, (long)mag);

    exponent = Math.getExponent(mag);
    significand = FloatingDecimal.getSignificand(mag, exponent);
    val = shiftLeft(inPlace ? assignInPlace(val, significand) : assign(val, significand), exponent - FloatingDecimal.SIGNIFICAND_BITS_DOUBLE);
    return mag < 0 ? BigInt.neg(val) : val;
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as an {@code double}, rounded by the given
   * {@link RoundingMode}.
   *
   * @param mag The magnitude.
   * @param rm The {@link RoundingMode}.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as an {@code double}, rounded by the given
   *         {@link RoundingMode}.
   * @complexity O(1)
   */
  public static int[] valueOf(final double mag, final RoundingMode rm) {
    return assign(emptyVal, mag, rm, false);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as an {@code double}, rounded down.
   * <p>
   * This is equivalent to:
   *
   * <pre>
   * BigInt.valueOf(mag, RoundingMode.DOWN);
   * </pre>
   *
   * @param mag The magnitude.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as an {@code double}, rounded down.
   * @complexity O(1)
   */
  public static int[] valueOf(final double mag) {
    return assign(emptyVal, mag, null, false);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as an {@code int}.
   *
   * @param mag The magnitude.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as an {@code int}.
   * @complexity O(1)
   */
  public static int[] valueOf(final int mag) {
    return assign(emptyVal, mag);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as an <i>unsigned</i> {@code int}.
   *
   * @param sig The sign of the magnitude.
   * @param mag The magnitude (unsigned).
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as an <i>unsigned</i> {@code int}.
   * @complexity O(1)
   */
  public static int[] valueOf(final int sig, final int mag) {
    return assign(emptyVal, sig, mag);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as a {@code long}.
   *
   * @param mag The magnitude.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as a {@code long}.
   * @complexity O(1)
   */
  public static int[] valueOf(final long mag) {
    return assign(emptyVal, mag);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as an <i>unsigned</i> {@code long}.
   *
   * @param sig The sign of the magnitude.
   * @param mag The magnitude (unsigned).
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         provided magnitude as an <i>unsigned</i> {@code long}.
   * @complexity O(1)
   */
  public static int[] valueOf(final int sig, final long mag) {
    return assign(emptyVal, sig, mag);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * magnitude of the provided byte array containing the two's-complement binary
   * representation of a {@linkplain BigInt#val() value-encoded
   * <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         magnitude of the provided byte array containing the
   *         two's-complement binary representation of a
   *         {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] valueOf(final byte[] mag, final int off, final int len, final boolean littleEndian) {
    return assign(emptyVal, mag, off, len, littleEndian);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * magnitude of the provided byte array containing the two's-complement binary
   * representation of a {@linkplain BigInt#val() value-encoded
   * <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         magnitude of the provided byte array containing the
   *         two's-complement binary representation of a
   *         {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] valueOf(final byte[] mag, final boolean littleEndian) {
    return assign(emptyVal, mag, 0, mag.length, littleEndian);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * magnitude of the provided {@link BigInteger}.
   *
   * @param b The {@link BigInteger}.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         magnitude of the provided {@link BigInteger}.
   * @complexity O(n^2)
   */
  public static int[] valueOf(final BigInteger b) {
    return valueOf(b.toByteArray(), false);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as a {@code char[]}.
   *
   * @param s The magnitude.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         magnitude of the provided {@code long}.
   * @complexity O(n)
   */
  public static int[] valueOf(final char[] s) {
    return assign(emptyVal, s);
  }

  /**
   * Returns a new {@linkplain BigInt#val() value-encoded number} with the
   * provided magnitude as a {@code String}.
   *
   * @param s The magnitude.
   * @return A new {@linkplain BigInt#val() value-encoded number} with the
   *         magnitude of the provided {@code long}.
   * @complexity O(n)
   */
  public static int[] valueOf(final String s) {
    return assign(emptyVal, s);
  }
}