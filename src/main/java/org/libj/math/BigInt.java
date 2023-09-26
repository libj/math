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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * An arbitrary-precision integer replacement for {@link java.math.BigInteger}, with the following differences:
 * <ol>
 * <li><b>Mutable:</b> {@link BigInt} is mutable, to allow for reuse of allocated arrays.</li>
 * <li><b>Faster arithmetic:</b> The arithmetic algorithms in {@link BigInt} are implemented with the optimization of memory (heap
 * allocation) and runtime performance in mind.</li>
 * <li><b>Faster multiplication of large numbers:</b> Support parallel multiplication algorithm for very large numbers.</li>
 * <li><b>Support for {@code int} and {@code long} parameters and return types:</b> {@link BigInt} does not require its parameters
 * or return types to be {@link BigInt}, avoiding unnecessary instantiation of transient {@link BigInt} objects.</li>
 * <li><b>No preemptive exception checking:</b> The {@link BigInt} does not preemptively check for exceptions. If a programmer
 * divides by zero he has only himself to blame. And, it is ok to have undefined behavior.</li>
 * <li><b>Support for "object-less" operation</b> All methods in {@link BigInt} are available in static form, allowing <i>bare
 * {@code int[]} {@linkplain #val() value-encoded number} arrays</i> to be used without a {@link BigInt} instance, providing further
 * reduction in heap memory allocation.</li>
 * </ol>
 *
 * @author Seva Safris
 */
public class BigInt extends BigIntMath implements Comparable<BigInt>, Cloneable {
  /**
   * The {@linkplain #val() value-encoded number}.
   *
   * @see #val()
   */
  private int[] val;

  /**
   * Returns the <i>{@linkplain #val() value-encoded number}</i>, which is an {@code int[]} with the following encoding:
   * <ol>
   * <li><b>{@code val[0]}</b>: <i>signed length</i>: <code>[-1 &lt;&lt; 26, .., -1, 0, 1, .., 1 &lt;&lt; 26]</code><br>
   * <ul>
   * <li>{@code Math.abs(val[0])}: The number of base-2<sup>32</sup> digits (limbs) in the magnitude.</li>
   * <li>{@code Math.signum(val[0])}: The sign of the magnitude ({@code -1} for negative, and {@code 1} for positive).</li>
   * <li>{@code val[0] == 0}: Magnitude is zero.</li>
   * </ul>
   * </li>
   * <li><b>{@code val[2,val[0]-1]}</b>: <i>digits</i>: {@code [Integer.MIN_VALUE, Integer.MAX_VALUE]}
   * <ul>
   * <li>The base-2<sup>32</sup> digits of the number in <i>little-endian</i> order.</li>
   * </ul>
   * </ol>
   *
   * @return The <i>{@linkplain #val() value-encoded number}</i>.
   */
  public int[] val() {
    return val;
  }

  /**
   * Creates a {@link BigInt} from the provided {@linkplain #val() value-encoded number}.
   *
   * @implNote The provided array will be used used as-is and not copied.
   * @param val The {@linkplain #val() value-encoded number}.
   * @complexity O(1)
   */
  public BigInt(final int[] val) {
    this.val = val;
  }

  /**
   * Creates a {@link BigInt} from the provided byte array containing the two's-complement binary representation of a
   * {@linkplain #val() value-encoded <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @complexity O(n^2)
   */
  public BigInt(final byte[] mag, int off, int len, final boolean littleEndian) {
    val = assign(emptyVal, mag, off, len, littleEndian);
  }

  /**
   * Creates a {@link BigInt} from the provided byte array containing the two's-complement binary representation of a
   * {@linkplain #val() value-encoded <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @complexity O(n^2)
   */
  public BigInt(final byte[] mag, final boolean littleEndian) {
    val = assign(emptyVal, mag, 0, mag.length, littleEndian);
  }

  /**
   * Creates a {@link BigInt} from the provided <i>unsigned</i> {@code int} magnitude.
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @complexity O(1)
   */
  public BigInt(final int sig, final int mag) {
    val = mag == 0 ? alloc(1) : assignInPlace(alloc(2), sig, mag);
  }

  /**
   * Creates a {@link BigInt} from the provided <i>unsigned</i> {@code long} magnitude.
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @complexity O(1)
   */
  public BigInt(final int sig, final long mag) {
    if (mag == 0) {
      val = alloc(1);
    }
    else {
      final int magh = (int)(mag >>> 32);
      val = magh != 0 ? assignInPlace(alloc(3), sig, (int)mag, magh) : assignInPlace(alloc(2), sig, (int)mag);
    }
  }

  /**
   * Creates a {@link BigInt} from the provided {@code int} magnitude.
   *
   * @param mag The magnitude.
   * @complexity O(1)
   */
  public BigInt(final int mag) {
    val = mag == 0 ? alloc(1) : mag < 0 ? assignInPlace(alloc(2), -1, -mag) : assignInPlace(alloc(2), 1, mag);
  }

  /**
   * Creates a {@link BigInt} from the provided {@code long} magnitude.
   *
   * @param mag The magnitude.
   * @complexity O(1)
   */
  public BigInt(long mag) {
    if (mag == 0) {
      val = alloc(1);
    }
    else {
      int sig = 1;
      if (mag < 0) { mag = -mag; sig = -1; }
      final int magh = (int)(mag >>> 32);
      val = magh != 0 ? assignInPlace(alloc(3), sig, (int)mag, magh) : assignInPlace(alloc(2), sig, (int)mag);
    }
  }

  /**
   * Creates a {@link BigInt} from the provided number as a string.
   *
   * @param s The number as a string.
   * @complexity O(n)
   */
  public BigInt(final String s) {
    val = assign(emptyVal, s);
  }

  /**
   * Creates a {@link BigInt} from the provided number as a {@code char[]}.
   *
   * @param s The number as a string.
   * @complexity O(n)
   */
  public BigInt(final char[] s) {
    val = assign(emptyVal, s);
  }

  /**
   * Creates a {@link BigInt} from the provided {@link BigInt}.
   *
   * @implNote This is a <i>copy constructor</i> that sets the {@linkplain #val() value-encoded number} of this {@link BigInt} to a
   *           <b>clone</b> of the {@linkplain #val() value-encoded number} of the provided {@link BigInt}.
   * @param b The {@link BigInt}.
   * @complexity O(n)
   * @see #clone()
   */
  public BigInt(final BigInt b) {
    val = b.val.clone();
  }

  /**
   * Creates a {@link BigInt} with the magnitude of the provided {@link BigInteger}.
   *
   * @param b The {@link BigInteger}.
   * @complexity O(n^2)
   */
  public BigInt(final BigInteger b) {
    val = valueOf(b);
  }

  /**
   * Assigns (copies) the value of the provided {@link BigInt} to this {@link BigInt}.
   *
   * <pre>
   * this = b
   * </pre>
   *
   * @param b The {@link BigInt}.
   * @return {@code this}
   */
  public BigInt assign(final BigInt b) {
    final int len = Math.abs(val[0]) + 1;
    copy(b.val, len, val, len);
    return this;
  }

  /**
   * Assigns the specified {@linkplain #val() value-encoded number} to this {@link BigInt}.
   *
   * <pre>
   * this = val
   * </pre>
   *
   * @implNote The provided array will be copied.
   * @param val The {@linkplain #val() value-encoded number}.
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final int[] val) {
    this.val = assign(this.val, val);
    // _debugLenSig(val);
    return this;
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation of a {@linkplain #val() value-encoded
   * <code>int[]</code>} to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param mag The two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt assign(final byte[] mag, final int off, final int len, final boolean littleEndian) {
    val = assign(val, mag, off, len, littleEndian);
    return this;
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation of a {@linkplain #val() value-encoded
   * <code>int[]</code>} to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param mag The two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt assign(final byte[] mag, final boolean littleEndian) {
    val = assign(val, mag, 0, mag.length, littleEndian);
    return this;
  }

  /**
   * Assigns the specified number as a string to this {@link BigInt}.
   *
   * <pre>
   * this = s
   * </pre>
   *
   * @param s The number as a {@code char[]}.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt assign(final String s) {
    val = assign(val, s);
    return this;
  }

  /**
   * Assigns the specified number as a {@code char[]} to this {@link BigInt}.
   *
   * <pre>
   * this = s
   * </pre>
   *
   * @param s The number as a {@code char[]}.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt assign(final char[] s) {
    val = assign(val, s);
    return this;
  }

  /**
   * Assigns an <i>unsigned</i> {@code int} magnitude to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final int sig, final int mag) {
    val = assign(val, sig, mag);
    return this;
  }

  /**
   * Assigns an <i>unsigned</i> {@code long} magnitude to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final int sig, final long mag) {
    val = assign(val, sig, mag);
    return this;
  }

  /**
   * Assigns an {@code int} magnitude to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param mag The magnitude.
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final int mag) {
    val = assign(val, mag);
    return this;
  }

  /**
   * Assigns an {@code long} magnitude to this {@link BigInt}.
   *
   * <pre>
   * this = mag
   * </pre>
   *
   * @param mag The magnitude.
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final long mag) {
    val = assign(val, mag);
    return this;
  }

  /**
   * Sets the magnitude of this {@link BigInt} to its absolute value.
   *
   * <pre>
   * this = | this |
   * </pre>
   *
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt abs() {
    abs(val);
    return this;
  }

  /**
   * Sets the magnitude of this {@link BigInt} to its negated value.
   *
   * <pre>
   * this = -this
   * </pre>
   *
   * @return {@code this}
   */
  public BigInt neg() {
    neg(val);
    return this;
  }

  /**
   * Returns the maximum of {@code this} and {@code v}.
   *
   * @param v Value with which the maximum is to be computed.
   * @return The {@link BigInt} whose value is the greater of {@code this} and {@code v}. If they are equal, {@code v} will be
   *         returned.
   */
  public BigInt max(final BigInt v) {
    return compareTo(v) > 0 ? this : v;
  }

  /**
   * Returns the minimum of {@code this} and {@code v}.
   *
   * @param v Value with which the minimum is to be computed.
   * @return The {@link BigInt} whose value is the greater of {@code this} and {@code v}. If they are equal, {@code v} will be
   *         returned.
   */
  public BigInt min(final BigInt v) {
    return compareTo(v) < 0 ? this : v;
  }

  /**
   * Returns the signum of this {@link BigInt}.
   *
   * @return -1, 0 or 1 as the value of the provided {@linkplain #val() value-encoded number} is negative, zero or positive.
   * @complexity O(1)
   */
  public int signum() {
    return signum(val);
  }

  /**
   * Tests if this {@link BigInt} is zero.
   *
   * @return {@code true} if this {@link BigInt} is zero, otherwise {@code false}.
   * @complexity O(1)
   */
  public boolean isZero() {
    return isZero(val);
  }

  /**
   * Sets this {@link BigInt} to zero.
   *
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt setToZero() {
    setToZero(val);
    return this;
  }

  /**
   * Adds an <i>unsigned</i> {@code int} to this {@link BigInt}.
   *
   * <pre>
   * this = this + add
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} to add.
   * @param add The amount to add (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt add(final int sig, final int add) {
    val = add(val, sig, add);
    return this;
  }

  /**
   * Adds an {@code int} to this {@link BigInt}.
   *
   * <pre>
   * this = this + add
   * </pre>
   *
   * @param add The amount to add.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt add(final int add) {
    val = add(val, add);
    return this;
  }

  /**
   * Adds an <i>unsigned</i> {@code long} to this {@link BigInt}.
   *
   * <pre>
   * this = this + add
   * </pre>
   *
   * @param sig The sign of the unsigned {@code long} to add.
   * @param add The amount to add (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt add(final int sig, final long add) {
    val = add(val, sig, add);
    return this;
  }

  /**
   * Adds a {@code long} to this {@link BigInt}.
   *
   * <pre>
   * this = this + add
   * </pre>
   *
   * @param add The amount to add.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt add(final long add) {
    val = add(val, add);
    return this;
  }

  /**
   * Adds a {@link BigInt} to this {@link BigInt}.
   *
   * <pre>
   * this = this + add
   * </pre>
   *
   * @param add The {@link BigInt} to add.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt add(final BigInt add) {
    val = add(val, add.val);
    return this;
  }

  /**
   * Subtracts an <i>unsigned</i> {@code int} from this {@link BigInt}.
   *
   * <pre>
   * this = this - sub
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} to subtract.
   * @param sub The amount to subtract (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt sub(final int sig, final int sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Subtracts an {@code int} from this {@link BigInt}.
   *
   * <pre>
   * this = this - sub
   * </pre>
   *
   * @param sub The amount to subtract.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt sub(final int sub) {
    val = sub(val, sub);
    return this;
  }

  /**
   * Subtracts an <i>unsigned</i> {@code long} from this {@link BigInt}.
   *
   * <pre>
   * this = this - sub
   * </pre>
   *
   * @param sig The sign of the unsigned {@code long} to subtract.
   * @param sub The amount to subtract (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt sub(final int sig, final long sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Subtracts a {@code long} from this {@link BigInt}.
   *
   * <pre>
   * this = this - sub
   * </pre>
   *
   * @param sub The amount to subtract.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt sub(final long sub) {
    val = sub(val, sub);
    return this;
  }

  /**
   * Subtracts a {@link BigInt} from this {@link BigInt}.
   *
   * <pre>
   * this = this - sub
   * </pre>
   *
   * @param sub The {@link BigInt} to subtract.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt sub(final BigInt sub) {
    val = sub(val, sub.val);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by an <i>unsigned</i> {@code int} multiplicand.
   *
   * <pre>
   * this = this * mul
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} multiplier.
   * @param mul The multiplier (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt mul(final int sig, final int mul) {
    val = mul(val, sig, mul);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by an {@code int} multiplicand.
   *
   * <pre>
   * this = this * mul
   * </pre>
   *
   * @param mul The multiplier.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt mul(final int mul) {
    val = mul(val, mul);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by an <i>unsigned</i> {@code long} multiplicand.
   *
   * <pre>
   * this = this * mul
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} multiplier.
   * @param mul The multiplier (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt mul(final int sig, final long mul) {
    val = mul(val, sig, mul);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by a {@code long} multiplicand.
   *
   * <pre>
   * this = this * mul
   * </pre>
   *
   * @param mul The multiplier.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt mul(final long mul) {
    val = mul(val, mul);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by a {@link BigInt} multiplicand.
   *
   * <pre>
   * this = this * mul
   * </pre>
   *
   * @param mul The multiplier.
   * @return {@code this}
   * @complexity O(n^2) - O(n log n)
   */
  public BigInt mul(final BigInt mul) {
    val = mul(val, mul.val);
    return this;
  }

  /**
   * Raises this {@link BigInt} to the power of the given exponent.
   *
   * <pre>
   * <code>this = this<sup>exp</sup></code>
   * </pre>
   *
   * If {@code exp < 0}, {@code this} is set to {@code 0}.
   *
   * @param exp The exponent to which this {@link BigInt} is to be raised.
   * @return <code>this<sup>exp</sup></code>
   * @complexity O(n^2 log exp) - O(n log n log exp)
   */
  public BigInt pow(final int exp) {
    val = pow(val, exp);
    return this;
  }

  /**
   * Set this {@link BigInt} to the value of its square root, rounded as per the provided {@link RoundingMode}.
   *
   * <pre>
   * <code>this = this<sup>1/2</sup></code>
   * </pre>
   *
   * @param rm The {@link RoundingMode}.
   * @return <code>this<sup>1/2</sup></code>, rounded as per the provided {@link RoundingMode}.
   * @complexity O(n^2) - O(n log n)
   */
  public BigInt sqrt(final RoundingMode rm) {
    val = sqrt(val, rm);
    return this;
  }

  /**
   * Set this {@link BigInt} to the value of its square root, rounding down.
   *
   * <pre>
   *  <code>this = this<sup>1/2</sup></code>
   * </pre>
   *
   * @return <code>this<sup>1/2</sup></code>, rounded down.
   * @complexity O(n^2) - O(n log n)
   */
  public BigInt sqrt() {
    val = sqrt(val, RoundingMode.DOWN);
    return this;
  }

  /**
   * Set this {@link BigInt} to the value of its natural logarithm, rounded as per the provided {@link RoundingMode}.
   *
   * <pre>
   * this = log(this)
   * </pre>
   *
   * @param rm The {@link RoundingMode}.
   * @return {@code log(this)}, rounded as per the provided {@link RoundingMode}.
   * @complexity O(1)
   */
  public int log(final RoundingMode rm) {
    return log(val, rm);
  }

  /**
   * Set this {@link BigInt} to the value of its natural logarithm, rounded down.
   *
   * <pre>
   * this = log(this)
   * </pre>
   *
   * @return {@code log(this)}, rounded down.
   * @complexity O(1)
   */
  public int log() {
    return log(val, RoundingMode.DOWN);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code base}, rounded as per the provided {@link RoundingMode}.
   *
   * <pre>
   *  <code>this = log<sub>base</sub>(this)</code>
   * </pre>
   *
   * @param base The base of the logarithm.
   * @param rm The {@link RoundingMode}.
   * @return <code>log<sub>base</sub>(this)</code>, rounded as per the provided {@link RoundingMode}.
   * @complexity O(1)
   */
  public int log(final double base, final RoundingMode rm) {
    return log(val, base, rm);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code base}, rounded down.
   *
   * <pre>
   * <code>this = log<sub>base</sub>(this)</code>
   * </pre>
   *
   * @param base The base of the logarithm.
   * @return <code>log<sub>base</sub>(this)</code>, rounded down.
   * @complexity O(1)
   */
  public int log(final double base) {
    return log(val, base, RoundingMode.DOWN);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code 2}, rounded as per the provided {@link RoundingMode}.
   *
   * <pre>
   *  <code>this = log<sub>2</sub>(this)</code>
   * </pre>
   *
   * @param rm The {@link RoundingMode}.
   * @return <code>log<sub>2</sub>(this)</code>, rounded as per the provided {@link RoundingMode}.
   * @complexity O(1)
   */
  public int log2(final RoundingMode rm) {
    return log2(val, rm);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code base}, rounded down.
   *
   * <pre>
   * <code>this = log<sub>2</sub>(this)</code>
   * </pre>
   *
   * @return <code>log<sub>2</sub>(this)</code>, rounded down.
   * @complexity O(1)
   */
  public int log2() {
    return log2(val, RoundingMode.DOWN);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code 2}, rounded as per the provided {@link RoundingMode}.
   *
   * <pre>
   *  <code>this = log<sub>10</sub>(this)</code>
   * </pre>
   *
   * @param rm The {@link RoundingMode}.
   * @return <code>log<sub>10</sub>(this)</code>, rounded as per the provided {@link RoundingMode}.
   * @complexity O(1)
   */
  public int log10(final RoundingMode rm) {
    return log10(val, rm);
  }

  /**
   * Set this {@link BigInt} to the value of its logarithm of base {@code base}, rounded down.
   *
   * <pre>
   * <code>this = log<sub>10</sub>(this)</code>
   * </pre>
   *
   * @return <code>log<sub>10</sub>(this)</code>, rounded down.
   * @complexity O(1)
   */
  public int log10() {
    return log10(val, RoundingMode.DOWN);
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code int} divisor.
   *
   * <pre>
   * this = this / div
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final int sig, final int div) {
    divRem(val, sig, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@code int} divisor.
   *
   * <pre>
   * this = this / div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final int div) {
    divRem(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code long} divisor.
   *
   * <pre>
   * this = this / div
   * </pre>
   *
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final int sig, final long div) {
    divRem(val, sig, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@code long} divisor.
   *
   * <pre>
   * this = this / div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final long div) {
    divRem(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@link BigInt} divisor.
   *
   * <pre>
   * this = this / div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final BigInt div) {
    val = div(val, div.val);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the specified <i>unsigned</i> {@code int} divisor, and returns the <i>absolute unsigned int</i>
   * remainder.
   *
   * <pre>
   * rem = this % div
   * this = this / div
   * return rem
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned int</i> remainder resulting from the division of this {@link BigInt} by the specified
   *         <i>unsigned</i> {@code int} divisor.
   * @complexity O(n)
   */
  public int divRem(final int sig, final int div) {
    return divRem(val, sig, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@code int} divisor, and returns the remainder.
   *
   * <pre>
   * rem = this % div
   * this = this / div
   * return rem
   * </pre>
   *
   * @param div The divisor.
   * @return The remainder resulting from the division of this {@link BigInt} by the specified {@code int} divisor.
   * @complexity O(n)
   */
  public long divRem(final int div) {
    return divRem(val, div);
  }

  /**
   * Divides this {@link BigInt} by the specified <i>unsigned</i> {@code long} divisor, and returns the <i>absolute unsigned long</i>
   * remainder.
   *
   * <pre>
   * rem = this % div
   * this = this / div
   * return rem
   * </pre>
   *
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned long</i> remainder resulting from the division of this {@link BigInt} by the specified
   *         <i>unsigned</i> {@code long} divisor.
   * @complexity O(n)
   */
  public long divRem(final int sig, final long div) {
    return divRem(val, sig, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@code long} divisor, and returns the remainder.
   *
   * <pre>
   * rem = this % div
   * this = this / div
   * return rem
   * </pre>
   *
   * @param div The divisor.
   * @return The remainder resulting from the division of this {@link BigInt} by the specified {@code long} divisor.
   * @complexity O(n)
   */
  public long divRem(final long div) {
    return divRem(val, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@link BigInt} divisor, and returns the remainder as a new {@link BigInt}.
   *
   * <pre>
   * rem = this % div
   * this = this / div
   * return rem
   * </pre>
   *
   * @param div The {@link BigInt} divisor.
   * @return A new {@link BigInt} with the remainder.
   * @complexity O(n^2)
   */
  public BigInt divRem(final BigInt div) {
    return new BigInt(divRem(val, div.val));
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code int} divisor and sets the remainder as the value of this
   * {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt rem(final int sig, final int div) {
    rem(val, sig, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@code int} divisor and sets the remainder as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt rem(final int div) {
    rem(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code long} divisor and sets the remainder as the value of this
   * {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt rem(final int sig, final long div) {
    rem(val, sig, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@code long} divisor and sets the remainder as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt rem(final long div) {
    rem(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided {@code long} divisor and sets the remainder as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt rem(final BigInt div) {
    val = rem(val, div.val);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the specified {@code int} divisor and sets the modulus as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @implNote This method differs from {@link #rem(BigInt)} in that it always returns a <i>non-negative</i> result.
   * @param div The {@link BigInt} divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt mod(final int div) {
    val = mod(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the specified {@code long} divisor and sets the modulus as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @implNote This method differs from {@link #rem(BigInt)} in that it always returns a <i>non-negative</i> result.
   * @param div The {@link BigInt} divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt mod(final long div) {
    val = mod(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the specified {@link BigInt} divisor and sets the modulus as the value of this {@link BigInt}.
   *
   * <pre>
   * this = this % div
   * </pre>
   *
   * @implNote This method differs from {@link #rem(BigInt)} in that it always returns a <i>non-negative</i> result.
   * @param div The {@link BigInt} divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt mod(final BigInt div) {
    val = mod(val, div.val);
    return this;
  }

  /**
   * Returns the number of bits in the two's complement representation of this {@link BigInt} that differ from its sign bit.
   *
   * @return Number of bits in the two's complement representation of this {@link BigInt} that differ from its sign bit.
   * @complexity O(n)
   */
  public int bitCount() {
    return bitCount(val);
  }

  /**
   * Returns the number of bits in the minimal two's-complement representation of this {@link BigInt}, <i>excluding</i> a sign bit.
   * For positive numbers, this is equivalent to the number of bits in the ordinary binary representation. For zero this method
   * returns {@code 0}.
   *
   * <pre>
   * ceil(log2(this &lt; 0 ? -this : this + 1))
   * </pre>
   *
   * @return Number of bits in the minimal two's-complement representation of this {@link BigInt}, <i>excluding</i> a sign bit.
   * @complexity O(n)
   */
  public long bitLength() {
    return bitLength(val);
  }

  /**
   * Returns the index of the rightmost (lowest-order) one bit in this {@link BigInt} (the number of zero bits to the right of the
   * rightmost one bit). Returns {@code -1} if number contains no one bits.
   *
   * <pre>
   * this == 0 ? -1 : log2(this &amp; -this)
   * </pre>
   *
   * @return The index of the rightmost (lowest-order) one bit in this {@link BigInt}.
   */
  public int getLowestSetBit() {
    return getLowestSetBit(val);
  }

  /**
   * Returns the number of digits in this {@link BigInt} (radix 10).
   *
   * @return The number of digits in this {@link BigInt} (radix 10).
   * @complexity O(n)
   */
  public int precision() {
    return precision(val);
  }

  /**
   * Shifts this {@link BigInt} left by the specified number of bits. The shift distance, {@code num}, may be negative, in which case
   * this method performs a right shift.
   *
   * <pre>
   * this = this &lt;&lt; num
   * </pre>
   *
   * @param num The amount by which to shift.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt shiftLeft(final int num) {
    val = shiftLeft(val, num);
    return this;
  }

  /**
   * Shifts this {@link BigInt} right by the specified number of bits. The shift distance, {@code num}, may be negative, in which case
   * this method performs a left shift.
   *
   * <pre>
   * this = this &gt; &gt; num
   * </pre>
   *
   * @param num The amount by which to shift.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt shiftRight(final int num) {
    val = shiftRight(val, num);
    return this;
  }

  /**
   * Tests if the specified bit is set in this {@link BigInt}.
   *
   * <pre>
   * this | (1 &lt;&lt; n)
   * </pre>
   *
   * @param bit The index of the bit to test.
   * @return {@code true} if the given bit is set in this {@link BigInt}, otherwise {@code false}.
   * @complexity O(n)
   */
  public boolean testBit(final int bit) {
    return testBit(val, bit);
  }

  /**
   * Sets the specified bit in this {@link BigInt}.
   *
   * <pre>
   * this = this | (1 &lt;&lt; n)
   * </pre>
   *
   * @param bit The bit to set.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt setBit(final int bit) {
    val = setBit(val, bit);
    return this;
  }

  /**
   * Clears the specified bit in this {@link BigInt}.
   *
   * <pre>
   * this = this &amp; ~(1 &lt;&lt; n)
   * </pre>
   *
   * @param bit The bit to clear.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt clearBit(final int bit) {
    val = clearBit(val, bit);
    return this;
  }

  /**
   * Flips the specified bit in this {@link BigInt}.
   *
   * <pre>
   * this = this ^ (1 &lt;&lt; n)
   * </pre>
   *
   * @param bit The bit to flip.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt flipBit(final int bit) {
    val = flipBit(val, bit);
    return this;
  }

  /**
   * Performs a bitwise "and" of the specified {@link BigInt} mask onto this {@link BigInt}.
   *
   * <pre>
   * this = this &amp; mask
   * </pre>
   *
   * @param mask The number with which to perform the bitwise "and".
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt and(final BigInt mask) {
    val = and(val, mask.val);
    return this;
  }

  /**
   * Performs a bitwise "or" of the specified {@link BigInt} mask onto this {@link BigInt}.
   *
   * <pre>
   * this = this | mask
   * </pre>
   *
   * @param mask The number with which to perform the bitwise "or".
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt or(final BigInt mask) {
    val = or(val, mask.val);
    return this;
  }

  /**
   * Performs a bitwise "xor" of the specified {@link BigInt} mask onto this {@link BigInt}.
   *
   * <pre>
   * this = this ^ mask
   * </pre>
   *
   * @param mask The number with which to perform the bitwise "xor".
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt xor(final BigInt mask) {
    val = xor(val, mask.val);
    return this;
  }

  /**
   * Performs a bitwise "and-not" of the specified {@link BigInt} mask onto this {@link BigInt}.
   *
   * <pre>
   * this = this &amp; ~mask
   * </pre>
   *
   * @param mask The number with which to perform the bitwise "and-not".
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt andNot(final BigInt mask) {
    val = andNot(val, mask.val);
    return this;
  }

  /**
   * Inverts the sign and all bits of this {@link BigInt}.
   *
   * <pre>
   * val = ~val
   * </pre>
   *
   * The identity {@code -val = ~val + 1} holds.
   *
   * @implNote The returned number may be a {@code new int[]} instance if the number resulting from the operation requires a larger
   *           array.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt not() {
    val = not(val);
    return this;
  }

  /**
   * Returns the value of this {@link BigInt} as a {@code byte}.
   *
   * @return The value of this {@link BigInt} as a {@code byte}.
   * @complexity O(1)
   */
  @Override
  public byte byteValue() {
    return byteValue(val);
  }

  /**
   * Returns the value of this {@link BigInt} as a {@code short}.
   *
   * @return The value of this {@link BigInt} as a {@code short}.
   * @complexity O(1)
   */
  @Override
  public short shortValue() {
    return shortValue(val);
  }

  /**
   * Returns the value of this {@link BigInt} as an {@code int}.
   *
   * @return The value of this {@link BigInt} as an {@code int}.
   * @complexity O(1)
   */
  @Override
  public int intValue() {
    return intValue(val);
  }

  /**
   * Returns the value of this {@link BigInt} as a {@code long}.
   *
   * @return The value of this {@link BigInt} as a {@code long}.
   * @complexity O(1)
   */
  @Override
  public long longValue() {
    return longValue(val);
  }

  /**
   * Returns the value of this {@link BigInt} as an <i>unsigned</i> {@code long}.
   *
   * @return The value of this {@link BigInt} as an <i>unsigned</i> {@code long}.
   * @complexity O(1)
   */
  public long longValueUnsigned() {
    return longValueUnsigned(val);
  }

  /**
   * Returns the value of this {@link BigInt} as a {@code float}.
   *
   * @return The value of this {@link BigInt} as a {@code float}.
   * @complexity O(1)
   */
  @Override
  public float floatValue() {
    return floatValue(val);
  }

  /**
   * Returns the value of this {@link BigInt} as a {@code double}.
   *
   * @return The value of this {@link BigInt} as a {@code double}.
   * @complexity O(1)
   */
  @Override
  public double doubleValue() {
    return doubleValue(val);
  }

  /**
   * Returns a byte array containing the two's-complement representation of this {@link BigInt}. The byte array will be in the endian
   * order as specified by the {@code littleEndian} argument. The array will contain the minimum number of bytes required to represent
   * this {@link BigInt}, including at least one sign bit, which is {@code (ceil((bitLength(val) + 1) / 8))}.
   *
   * @param littleEndian Whether the produced byte array is to be encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return A byte array containing the two's-complement representation of this {@link BigInt}.
   */
  public byte[] toByteArray(final boolean littleEndian) {
    return toByteArray(val, littleEndian);
  }

  /**
   * Returns a {@link BigInteger} representation of this {@link BigInt}.
   *
   * @return A {@link BigInteger} representation of this {@link BigInt}.
   */
  public BigInteger toBigInteger() {
    return toBigInteger(val);
  }

  /**
   * Returns a {@link BigDecimal} representation of this {@link BigInt}.
   *
   * @return A {@link BigDecimal} representation of this {@link BigInt}.
   */
  public BigDecimal toBigDecimal() {
    return toBigDecimal(val);
  }

  /**
   * Compares the absolute values of this {@link BigInt} to the provided {@link BigInt}, and returns one of {@code -1}, {@code 0}, or
   * {@code 1} whether the absolute value of {@code this} is less than, equal to, or greater than that of the provided {@link BigInt},
   * respectively.
   *
   * @param o The {@link BigInt} with which to compare.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the absolute value of {@code this} is less than, equal to, or greater than
   *         that of he provided {@link BigInt}, respectively.
   * @complexity O(n)
   */
  public int compareToAbs(final BigInt o) {
    return compareToAbs(val, o.val);
  }

  /**
   * Compares the values of this {@link BigInt} to the provided {@link BigInt}, and returns one of {@code -1}, {@code 0}, or {@code 1}
   * whether the value of {@code this} is less than, equal to, or greater than that of the provided {@link BigInt}, respectively.
   *
   * @param o The {@link BigInt} with which to compare.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the value of {@code this} is less than, equal to, or greater than that of
   *         he provided {@link BigInt}, respectively.
   * @complexity O(n)
   */
  @Override
  public int compareTo(final BigInt o) {
    return compareTo(val, o.val);
  }

  /**
   * Tests equality of this {@link BigInt} and the provided {@link BigInt}.
   *
   * @param n The {@link BigInt} with which to test for equality.
   * @return {@code true} if the two numbers are equal, otherwise {@code false}.
   * @complexity O(n)
   */
  public boolean equals(final BigInt n) {
    return equals(val, n.val);
  }

  /**
   * Tests equality of this {@link BigInt} and the provided object.
   *
   * @param obj The object with which to test for equality.
   * @return {@code true} if this {@link BigInt} and the provided object are equal, otherwise {@code false}.
   * @complexity O(n)
   */
  @Override
  public boolean equals(final Object obj) {
    return obj == this || obj instanceof BigInt && equals((BigInt)obj);
  }

  /**
   * Computes the hash code of this {@link BigInt}.
   *
   * @return The hash code of this {@link BigInt}.
   * @complexity O(n)
   */
  @Override
  public int hashCode() {
    return hashCode(val);
  }

  /**
   * Converts this {@link BigInt} into a string of radix 10.
   *
   * @return The string representation of this {@link BigInt} in radix 10.
   * @complexity O(n^2)
   */
  @Override
  public String toString() {
    return toString(val);
  }

  /**
   * Returns a copy of this {@link BigInt}.
   *
   * @return A copy of this {@link BigInt}.
   * @complexity O(n)
   */
  @Override
  public BigInt clone() {
    try {
      final BigInt clone = (BigInt)super.clone();
      clone.val = val.clone();
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}