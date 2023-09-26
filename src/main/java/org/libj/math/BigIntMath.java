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

import java.math.RoundingMode;

import org.libj.lang.Constants;

abstract class BigIntMath extends BigIntDivision {
  /**
   * Returns the square root of the specified {@code int}, rounded with the provided {@link RoundingMode}; or {@code -1} if
   * {@code x <= 0} or {@code rm == RoundingMode.UNNECESSARY} and rounding is necessary.
   *
   * @return The square root of the specified {@code int}, rounded with the provided {@link RoundingMode}; or {@code -1} if
   *         {@code x <= 0} or {@code rm == RoundingMode.UNNECESSARY} and rounding is necessary.
   */
  private static int sqrt(final int x, final RoundingMode rm) {
    if (x <= 0)
      return -1;

    final int sqrtFloor = (int)Math.sqrt(x);
    if (rm == RoundingMode.FLOOR || rm == RoundingMode.DOWN)
      return sqrtFloor;

    if (rm == RoundingMode.CEILING || rm == RoundingMode.UP)
      return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);

    if (rm == RoundingMode.UNNECESSARY)
      return sqrtFloor * sqrtFloor == x ? sqrtFloor : -1;

    final int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
    /*
     * Test whether x <= (sqrtFloor + 0.5)^2 = halfSquare + 0.25. Since both x and halfSquare are integers, this is equivalent to
     * testing whether x <= halfSquare. (We have to deal with overflow, though). If we treat halfSquare as an unsigned int, we know that
     * sqrtFloor^2 <= x < (sqrtFloor + 1)^2 halfSquare - sqrtFloor <= x < halfSquare + sqrtFloor + 1 so |x - halfSquare| <= sqrtFloor.
     * Therefore, it's safe to treat x - halfSquare as a signed int, so lessThanBranchFree is safe for use.
     */
    return sqrtFloor + lessThanBranchFree(halfSquare, x);
  }

  /**
   * Returns the square root of the specified {@code long}, rounded with the provided {@link RoundingMode}; or {@code -1} if
   * {@code x <= 0} or {@code rm == RoundingMode.UNNECESSARY} and rounding is necessary.
   *
   * @return The square root of the specified {@code long}, rounded with the provided {@link RoundingMode}; or {@code -1} if
   *         {@code x <= 0} or {@code rm == RoundingMode.UNNECESSARY} and rounding is necessary.
   */
  private static long sqrt(final long x, final RoundingMode rm) {
    if (x <= 0)
      return -1;

    if ((int)x == x)
      return sqrt((int)x, rm);

    /*
     * Let k be the true value of floor(sqrt(x)), so that k * k <= x < (k + 1) * (k + 1) (double)(k * k) <= (double)x <= (double)((k +
     * 1) * (k + 1)) since casting to double is nondecreasing. Note that the right-hand inequality is no longer strict. sqrt(k * k) <=
     * sqrt(x) <= sqrt((k + 1) * (k + 1)) since sqrt() is monotonic. (long)sqrt(k * k) <= (long)sqrt(x) <= (long)sqrt((k + 1) * (k + 1))
     * since casting to long is monotonic k <= (long)sqrt(x) <= k + 1 since (long) sqrt(k * k) == k, as checked exhaustively in {@link
     * LongMathTest#testSqrtOfPerfectSquareAsDoubleIsPerfect}
     */
    final long guess = (long)Math.sqrt(x);
    // Note: guess is always <= FLOOR_SQRT_MAX_LONG.
    final long guessSq = guess * guess;

    // Note (2013-2-26): benchmarks indicate that, inscrutably enough, using if
    // statements is faster here than using lessThanBranchFree.
    if (rm == RoundingMode.FLOOR || rm == RoundingMode.DOWN)
      return x < guessSq ? guess - 1 : guess;

    if (rm == RoundingMode.CEILING || rm == RoundingMode.UP)
      return x > guessSq ? guess + 1 : guess;

    if (rm == RoundingMode.UNNECESSARY)
      return guessSq == x ? guess : -1;

    final long sqrtFloor = guess - (x < guessSq ? 1 : 0);
    final long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;

    /*
     * Test whether x <= (sqrtFloor + 0.5)^2 = halfSquare + 0.25. Since both x and halfSquare are integers, this is equivalent to
     * testing whether x <= halfSquare. (We have to deal with overflow, though). If we treat halfSquare as an unsigned int, we know that
     * sqrtFloor^2 <= x < (sqrtFloor + 1)^2 halfSquare - sqrtFloor <= x < halfSquare + sqrtFloor + 1 so |x - halfSquare| <= sqrtFloor.
     * Therefore, it's safe to treat x - halfSquare as a signed int, so lessThanBranchFree is safe for use.
     */
    return sqrtFloor + lessThanBranchFree(halfSquare, x);
  }

  /**
   * Computes the square root of the provided {@linkplain BigInt#val() value-encoded number}, rounded by {@link RoundingMode#DOWN}.
   *
   * <pre>
   *  <code>val = ⌊val<sup>1/2</sup>⌋</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @implNote The implementation is based on the material in Henry S. Warren, Jr., <i>Hacker's Delight (2nd ed.)</i> (Addison Wesley,
   *           2013), 279-282.
   * @return {@code val} with its value replaced with its integer square root, or {@code null} if {@code val} is negative.
   * @complexity O(n^2) - O(n log n)
   */
  public static int[] sqrt(final int[] val) {
    return sqrt(val, null);
  }

  /**
   * Computes the square root of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode}.
   *
   * <pre>
   *  <code>val = |val<sup>1/2</sup>|</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param rm The {@link RoundingMode}.
   * @implNote The implementation is based on the material in Henry S. Warren, Jr., <i>Hacker's Delight (2nd ed.)</i> (Addison Wesley,
   *           2013), 279-282.
   * @return {@code val} with its value replaced with its rounded square root, or {@code null} if {@code val} is negative, or
   *         {@code rm == RoundingMode.UNNECESSARY || rm == null} yet rounding is necessary.
   * @complexity O(n^2) - O(n log n)
   */
  public static int[] sqrt(final int[] val, final RoundingMode rm) {
    final int len = val[0];
    if (len == 0)
      return new int[] {0};

    if (len < 0)
      return null;

    if (fitsInLong(val, len))
      return assign(val, sqrt(longValue(val), rm));

    final int[] sqrtApprox = sqrtApprox(val, rm);
    if (rm == RoundingMode.FLOOR || rm == RoundingMode.DOWN)
      return sqrtApprox;

    if (rm == RoundingMode.CEILING || rm == RoundingMode.UP) {
      final int sqrtFloorInt = intValue(sqrtApprox);
      if (sqrtFloorInt * sqrtFloorInt != intValue(val)) // fast check mod 2^32
        return addInPlace(sqrtApprox, 1, 1);

      final int[] tmp = sqrtApprox.clone();
      return equals(mulInPlace(tmp, tmp), val) ? sqrtApprox : addInPlace(sqrtApprox, 1, 1); // slow exact check
    }

    final int[] tmp = sqrtApprox.clone();
    mulInPlace(tmp, tmp);
    if (rm == RoundingMode.UNNECESSARY || rm == null)
      return equals(tmp, val) ? sqrtApprox : null;

    final int[] halfSquare = addInPlace(tmp, sqrtApprox);
    /*
     * Test whether or not x <= (sqrtFloor + 0.5)^2 = halfSquare + 0.25. Since both x and halfSquare are integers, this is equivalent to
     * testing whether or not x <= halfSquare.
     */
    return compareToAbs(halfSquare, val) >= 0 ? sqrtApprox : addInPlace(sqrtApprox, 1, 1);
  }

  /**
   * Adapted from Hacker's Delight, Figure 11-1. Using DoubleUtils.bigToDouble, getting a double approximation of x is extremely fast,
   * and then we can get a double approximation of the square root. Then, we iteratively improve this guess with an application of
   * Newton's method, which sets guess := (guess + (x / guess)) / 2. This iteration has the following two properties: a) every
   * iteration (except potentially the first) has guess >= floor(sqrt(x)). This is because guess' is the arithmetic mean of guess and
   * x / guess, sqrt(x) is the geometric mean, and the arithmetic mean is always higher than the geometric mean. b) this iteration
   * converges to floor(sqrt(x)). In fact, the number of correct digits doubles with each iteration, so this algorithm takes
   * O(log(digits)) iterations. We start out with a double-precision approximation, which may be higher or lower than the true value.
   * Therefore, we perform at least one Newton iteration to get a guess that's definitely >= floor(sqrt(x)), and then continue the
   * iteration until we reach a fixed point.
   */
  // FIXME: Perhaps it's possible to yet remove more realloc(s). Use ThreadLocal.
  private static int[] sqrtApprox(final int[] val, final RoundingMode rm) {
    int[] res;
    final int log2 = log2(val, RoundingMode.FLOOR);
    if (log2 < Double.MAX_EXPONENT) {
      res = sqrtApproxDoubleInPlace(val.clone(), rm);
    }
    else {
      final int shift = (log2 - FloatingDecimal.SIGNIFICAND_BITS_DOUBLE) & ~1; // even!
      /*
       * We have that x / 2^shift < 2^54. Our initial approximation to sqrtFloor(x) will be 2^(shift/2) * sqrtApproxWithDoubles(x /
       * 2^shift).
       */
      res = shiftLeft(sqrtApproxDoubleInPlace(shiftRight(val.clone(), shift), rm), shift >> 1);
    }

    int[] tmp = shiftRight(addInPlace(div(val.clone(), res), res), 1);
    if (equals(res, tmp))
      return res;

    do {
      res = tmp;
      tmp = shiftRight(addInPlace(div(val.clone(), res), res), 1);
    }
    while (compareToAbs(tmp, res) < 0);
    return res;
  }

  private static int[] sqrtApproxDoubleInPlace(final int[] val, final RoundingMode rm) {
    return assignInPlace(val, Math.sqrt(doubleValue(val)), rm);
  }

  /**
   * Returns {@code 1} if {@code x < y} as unsigned longs, and 0 otherwise. Assumes that x - y fits into a signed long. The
   * implementation is branch-free, and benchmarks suggest it is measurably faster than the straightforward ternary expression.
   */
  private static int lessThanBranchFree(final long x, final long y) {
    // Returns the sign bit of x - y.
    return (int)(~~(x - y) >>> (Long.SIZE - 1));
  }

  /** maxLog10ForLeadingZeros[i] == floor(log10(2^(Long.SIZE - i))) */
  private static final byte[] maxLog10ForLeadingZeros = {19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};

  private static int log10Floor(final long x) {
    /*
     * Based on Hacker's Delight Fig. 11-5, the two-table-lookup, branch-free implementation. The key idea is that based on the number
     * of leading zeros (equivalently, floor(log2(x))), we can narrow the possible floor(log10(x)) values to two. For example, if
     * floor(log2(x)) is 6, then 64 <= x < 128, so floor(log10(x)) is either 1 or 2.
     */
    final int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
    /*
     * y is the higher of the two possible values of floor(log10(x)). If x < 10^y, then we want the lower of the two possible values, or
     * y - 1, otherwise, we want y.
     */
    return y - lessThanBranchFree(x, FastMath.longE10[y]);
  }

  private static int log10(final long x, final RoundingMode rm) {
    if (x <= 0)
      return -1;

    if ((rm == RoundingMode.UNNECESSARY || rm == null) && x != FastMath.longE10[log10Floor(x)])
      return -1;

    return (int)SafeMath.round(Math.log10(x), rm);
  }

  /**
   * Returns {@code true} if
   *
   * <pre>
   * equals(BigInt.valueOf(longValue(x)), x)
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The number of limbs in the provided {@linkplain BigInt#val() value-encoded number} (i.e. {@code Math.abs(val[0])}).
   * @return {@code true} if the provided {@linkplain BigInt#val() value-encoded number} fits in a {@code long} (i.e. 64 signed bits).
   */
  // FIXME: This should be used in other places too?
  static boolean fitsInLong(final int[] val, final int len) {
    return bitLength(val, len) < Long.SIZE;
  }

  /**
   * Computes the base 10 logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode#DOWN}.
   *
   * <pre>
   *  <code>val = ⌊log<sub>10</sub>(val)⌋</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The integer value of the log base 10 of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log10(final int[] val) {
    return log10(val, null);
  }

  /**
   * Computes the base 10 logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode}.
   *
   * <pre>
   *  <code>val = |log<sub>10</sub>(val)|</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param rm The {@link RoundingMode}.
   * @return The rounded value of the log base 10 of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log10(final int[] val, final RoundingMode rm) {
    final int len = val[0];
    if (len <= 0)
      return -1;

    if (fitsInLong(val, len))
      return log10(longValue(val), rm);

    return (int)SafeMath.round(log0(val, Constants.LOG_10), rm);
  }

  /** Returns {@code true} if {@code x} represents a power of two. */
  private static boolean isPowerOfTwo(final int[] val) {
    return signum(val) > 0 && getLowestSetBit(val) == bitLength(val) - 1;
  }

  /**
   * Computes the base 2 logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode#DOWN}.
   *
   * <pre>
   *  <code>val = ⌊log<sub>2</sub>(val)⌋</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The integer value of the log base 2 of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log2(final int[] val) {
    return log2(val, null);
  }

  /**
   * Computes the base 2 logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode}.
   *
   * <pre>
   *  <code>val = |log<sub>2</sub>(val)|</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param rm The {@link RoundingMode}.
   * @return The rounded value of the log base 2 of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log2(final int[] val, final RoundingMode rm) {
    if (val[0] <= 0)
      return -1;

    if (rm == RoundingMode.UNNECESSARY && !isPowerOfTwo(val))
      return -1;

    if (rm == RoundingMode.DOWN || rm == RoundingMode.FLOOR)
      return (int)(bitLength(val) - 1);

    return (int)SafeMath.round(log0(val, Constants.LOG_2), rm);
  }

  /**
   * Computes the natural logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode#DOWN}.
   *
   * <pre>
   * val = ⌊log(val)⌋
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The integer value of the natural log of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log(final int[] val) {
    return (int)SafeMath.round(log0(val), null);
  }

  /**
   * Computes the natural logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode}.
   *
   * <pre>
   * val = log(val)
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param rm The {@link RoundingMode}.
   * @return The rounded value of the natural log of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1} if
   *         {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log(final int[] val, final RoundingMode rm) {
    return (int)SafeMath.round(log0(val), rm);
  }

  /**
   * Computes the base {@code b} logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode#DOWN}.
   *
   * <pre>
   *  <code>val = ⌊log<sub>b</sub>(val)⌋</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param b The base of the logarithm function.
   * @return The integer value of the log base {@code b} of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1}
   *         if {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log(final int[] val, final double b) {
    return log(val, b, null);
  }

  /**
   * Computes the base {@code b} logarithm of the provided {@linkplain BigInt#val() value-encoded number}, rounded by the given
   * {@link RoundingMode}.
   *
   * <pre>
   *  <code>val = log<sub>b</sub>(val)</code>
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param b The base of the logarithm function.
   * @param rm The {@link RoundingMode}.
   * @return The rounded value of the log base {@code b} of the provided {@linkplain BigInt#val() value-encoded number}, or {@code -1}
   *         if {@code val} is not positive.
   * @complexity O(1)
   */
  public static int log(final int[] val, final double b, final RoundingMode rm) {
    return (int)SafeMath.round(log0(val, Math.log(b)), rm);
  }

  private static double log0(final int[] val) {
    final int len = val[0];
    return len <= 0 ? -1 : log0(val, len);
  }

  private static double log0(final int[] val, final double baseLog) {
    final int len = val[0];
    return len <= 0 ? -1 : log0(val, len) / baseLog;
  }

  private static double log0(final int[] val, final int len) {
    final int blex = (int)(bitLength(val, len) - FloatingDecimal.MAX_DIGITS_2);
    final double res = Math.log(doubleValue(val, OFF, len, 1));
    return blex > 0 ? res + blex * Constants.LOG_2 : res;
  }
}