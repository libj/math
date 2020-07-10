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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND ANY
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

package org.huldra.math;

import java.util.concurrent.ExecutionException;

/**
 * An arbitrary-precision integer replacement for {@link java.math.BigInteger},
 * with the following differences:
 * <ol>
 * <li><b>Mutable:</b> {@link BigInt} is mutable, to allow for reuse of
 * allocated arrays.</li>
 * <li><b>Generally faster arithmetic:</b> The arithmetic algorithms in
 * {@link BigInt} are implemented with optimized memory and runtime performance
 * in mind.</li>
 * <li><b>Faster multiplication of large numbers:</b> Support parallel
 * multiplication algorithm for very large numbers.</li>
 * <li><b>Support for {@code int} and {@code long} parameters and return
 * types:</b> {@link BigInt} does not require its parameters or return types to
 * be {@link BigInt}, saving from unnecessary instantiation of transient
 * {@link BigInt} objects.</li>
 * <li><b>No preemptive exception checking:</b> The {@link BigInt} does not
 * preemptively check for exceptions. If a programmer divides by zero he has
 * only himself to blame. And, it is ok to have undefined behavior.</li>
 * <li><b>Support for "object-less" operation</b> All methods in {@link BigInt}
 * are available in static form, allowing <i>bare {@code int[]}
 * {@linkplain #val() value-encoded number} arrays</i> to be used without a
 * {@link BigInt} instance, providing further reduction in heap memory
 * consumption.</li>
 * </ol>
 *
 * @author Seva Safris
 */
public class BigInt extends BigIntBinary implements Comparable<BigInt>, Cloneable {
  private static final long serialVersionUID = -4360183347203631370L;

  /**
   * The {@linkplain #val() value-encoded number}.
   *
   * @see #val()
   */
  private int[] val;

  /**
   * Returns the <i>{@linkplain #val() value-encoded number}</i>, which is an
   * {@code int[]} with the following encoding:
   * <ol>
   * <li><b>{@code val[0]}</b>: <ins>signed length</ins>:
   * <code>{Integer.MIN_VALUE - 2, .., -1, 0, 1, .., Integer.MAX_VALUE - 1}</code><br>
   * <ul>
   * <li>{@code Math.abs(val[0])}: The number of base 2^32 digits (limbs) in the
   * magnitude.</li>
   * <li>{@code Math.signum(val[0])}: The sign of the magnitude (-1 for
   * negative, and 1 for positive).</li>
   * <li>{@code val[0] == 0}: Magnitude is zero.</li>
   * </ul>
   * </li>
   * <li><b>{@code val[2,val[0]-1]}</b>: <ins>digits</ins>:
   * {@code [Integer.MIN_VALUE, Integer.MAX_VALUE]}
   * <ul>
   * <li>The base 2^32 digits of the number in <i>little-endian</i> order.</li>
   * </ul>
   * </li></li>
   * </ol>
   *
   * @return The <i>{@linkplain #val() value-encoded number}</i>.
   */
  public int[] val() {
    return val;
  }

  /**
   * Creates a {@link BigInt} from the provided {@linkplain #val() value-encoded
   * number}.
   * <p>
   * <i><b>Note:</b> The provided array will be used used as-is and not
   * copied.</i>
   *
   * @param val The {@linkplain #val() value-encoded number}.
   * @complexity O(1)
   */
  public BigInt(final int[] val) {
    assign(val);
  }

  /**
   * Creates a {@link BigInt} from the provided byte array containing the
   * two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @complexity O(1)
   */
  public BigInt(final byte[] mag, int off, int len, final boolean littleEndian) {
    val = assign(emptyVal, mag, off, len, littleEndian);
  }

  /**
   * Creates a {@link BigInt} from the provided byte array containing the
   * two's-complement binary representation of a {@linkplain #val() value-encoded <code>int[]</code>}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @complexity O(1)
   */
  public BigInt(final byte[] mag, final boolean littleEndian) {
    val = assign(emptyVal, mag, 0, mag.length, littleEndian);
  }

  /**
   * Creates a {@link BigInt} from the provided <i>unsigned</i> {@code int}
   * magnitude.
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @complexity O(1)
   */
  public BigInt(final int sig, final int mag) {
    val = mag == 0 ? setToZero0(alloc(1)) : assign0(alloc(2), sig, mag);
  }

  /**
   * Creates a {@link BigInt} from the provided <i>unsigned</i> {@code long}
   * magnitude.
   *
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @complexity O(1)
   */
  public BigInt(final int sig, final long mag) {
    if (mag == 0) {
      val = setToZero0(alloc(1));
    }
    else {
      final int magh = (int)(mag >>> 32);
      val = magh != 0 ? assign0(alloc(3), sig, mag, magh) : assign0(alloc(2), sig, (int)mag);
    }
  }

  /**
   * Creates a {@link BigInt} from the provided {@code int} magnitude.
   *
   * @param mag The magnitude.
   * @complexity O(1)
   */
  public BigInt(final int mag) {
    val = mag == 0 ? setToZero0(alloc(1)) : mag < 0 ? assign0(alloc(2), -1, -mag) : assign0(alloc(2), 1, mag);
  }

  /**
   * Creates a {@link BigInt} from the provided {@code long} magnitude.
   *
   * @param mag The magnitude.
   * @complexity O(1)
   */
  public BigInt(long mag) {
    if (mag == 0) {
      val = setToZero0(alloc(1));
    }
    else {
      int sig = 1; if (mag < 0) { mag = -mag; sig = -1; }
      final int magh = (int)(mag >>> 32);
      val = magh != 0 ? assign0(alloc(3), sig, mag, magh) : assign0(alloc(2), sig, (int)mag);
    }
  }

  /**
   * Creates a {@link BigInt} from the provided number as a string.
   *
   * @param s The number as a string.
   * @complexity O(1)
   */
  public BigInt(final String s) {
    val = assign(emptyVal, s);
  }

  /**
   * Creates a {@link BigInt} from the provided number as a {@code char[]}.
   *
   * @param s The number as a string.
   * @complexity O(1)
   */
  public BigInt(final char[] s) {
    val = assign(emptyVal, s);
  }

  /**
   * Creates a {@link BigInt} from the provided {@link BigInt}.
   * <p>
   * <i><b>Note:</b> This is a <i>copy constructor</i> that sets the
   * {@linkplain #val() value-encoded number} of this {@link BigInt} to a
   * <b>clone</b> of the {@linkplain #val() value-encoded number} of the
   * provided {@link BigInt}.</i>
   *
   * @param b The {@link BigInt}.
   * @complexity O(n)
   * @see #clone()
   */
  public BigInt(final BigInt b) {
    val = b.val.clone();
  }

  /**
   * Assigns (copies) the value of the provided {@link BigInt} to this
   * {@link BigInt}.
   *
   * @param b The {@link BigInt}.
   * @return {@code this}
   */
  public BigInt assign(final BigInt b) {
    final int len = Math.abs(val[0]) + 1;
    copy(val, b.val, len, len);
    return assign(b.val);
  }

  /**
   * Assigns the specified {@linkplain #val() value-encoded number} to this
   * {@link BigInt}.
   * <p>
   * <i><b>Note:</b> The provided array will be used used as-is and not
   * copied.</i>
   *
   * @param val The {@linkplain #val() value-encoded number}.
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final int[] val) {
    // FIXME: Should this assert that val.length != 0?
    this.val = val;
    _debugLenSig(val);
    return this;
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation
   * of a {@linkplain #val() value-encoded <code>int[]</code>} to this
   * {@link BigInt}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final byte[] mag, final int off, final int len, final boolean littleEndian) {
    val = assign(val, mag, off, len, littleEndian);
    return this;
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation
   * of a {@linkplain #val() value-encoded <code>int[]</code>} to this
   * {@link BigInt}.
   *
   * @param mag The two's-complement binary representation of a
   *          {@linkplain #val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return {@code this}
   * @complexity O(1)
   */
  public BigInt assign(final byte[] mag, final boolean littleEndian) {
    val = assign(val, mag, 0, mag.length, littleEndian);
    return this;
  }

  /**
   * Assigns the specified number as a string to this {@link BigInt}.
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
   * {@code this = | this |}
   * </pre>
   *
   * @return {@code this}
   */
  public BigInt abs() {
    abs(val);
    return this;
  }

  /**
   * Returns the maximum of {@code this} and {@code v}.
   *
   * @param v Value with which the maximum is to be computed.
   * @return The {@link BigInt} whose value is the greater of {@code this} and
   *         {@code v}. If they are equal, {@code v} will be returned.
   */
  public BigInt max(final BigInt v) {
    return compareTo(v) > 0 ? this : v;
  }

  /**
   * Returns the minimum of {@code this} and {@code v}.
   *
   * @param v Value with which the minimum is to be computed.
   * @return The {@link BigInt} whose value is the greater of {@code this} and
   *         {@code v}. If they are equal, {@code v} will be returned.
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
   * @return {@code true} if this {@link BigInt} is zero, otherwise
   *         {@code false}.
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
   * Multiplies this {@link BigInt} by an <i>unsigned</i> {@code int}
   * multiplicand.
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
   * @param mul The multiplier.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt mul(final int mul) {
    val = mul(val, mul);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by an <i>unsigned</i> {@code long}
   * multiplicand.
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
   * @param mul The multiplier.
   * @return {@code this}
   * @complexity O(n^2) - O(n log n)
   */
  public BigInt mul(final BigInt mul) {
    val = mul(val, mul.val);
    return this;
  }

  /**
   * Multiplies this {@link BigInt} by the given {@link BigInt} using the
   * Karatsuba algorithm.
   * <p>
   * <i><b>Note:</b> Size of mag1 and mag2 must be the same.</i>
   *
   * @param mul The amount to multiply.
   * @param parallel Whether to attempt to use the parallel algorithm.
   * @return {@code this}
   * @throws ExecutionException If the computation in a worker thread threw an
   *           exception.
   * @throws InterruptedException If the current thread was interrupted while
   *           waiting.
   * @complexity O(n^1.585)
   */
  BigInt karatsuba(final BigInt mul, final boolean parallel) throws ExecutionException, InterruptedException {
    val = karatsuba(val, mul.val, parallel);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code int}
   * divisor.
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
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final int div) {
    divRem(val, div);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code long}
   * divisor.
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
   * @param div The divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt div(final BigInt div) {
    val = div(val, div.val);
    return this;
  }

  /**
   * Divides this {@link BigInt} by the specified <i>unsigned</i> {@code int}
   * divisor, and returns the <i>absolute unsigned int</i> remainder.
   *
   * @param sig The sign of the unsigned {@code int} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned int</i> remainder resulting from the
   *         division of this {@link BigInt} by the specified <i>unsigned</i>
   *         {@code int} divisor.
   * @complexity O(n)
   */
  public int divRem(final int sig, final int div) {
    return divRem(val, sig, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@code int} divisor, and
   * returns the remainder.
   *
   * @param div The divisor.
   * @return The remainder resulting from the division of this {@link BigInt} by
   *         the specified {@code int} divisor.
   * @complexity O(n)
   */
  public long divRem(final int div) {
    return divRem(val, div);
  }

  /**
   * Divides this {@link BigInt} by the specified <i>unsigned</i> {@code long}
   * divisor, and returns the <i>absolute unsigned long</i> remainder.
   *
   * @param sig The sign of the unsigned {@code long} divisor.
   * @param div The divisor (unsigned).
   * @return The <i>absolute unsigned long</i> remainder resulting from the
   *         division of this {@link BigInt} by the specified <i>unsigned</i>
   *         {@code long} divisor.
   * @complexity O(n)
   */
  public long divRem(final int sig, final long div) {
    return divRem(val, sig, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@code long} divisor, and
   * returns the remainder.
   *
   * @param div The divisor.
   * @return The remainder resulting from the division of this {@link BigInt} by
   *         the specified {@code long} divisor.
   * @complexity O(n)
   */
  public long divRem(final long div) {
    return divRem(val, div);
  }

  /**
   * Divides this {@link BigInt} by the specified {@link BigInt} divisor, and
   * returns the remainder as a new {@link BigInt}.
   *
   * @param div The {@link BigInt} divisor.
   * @return A new {@link BigInt} with the remainder.
   * @complexity O(n^2)
   */
  public BigInt divRem(final BigInt div) {
    return new BigInt(divRem(val, div.val));
  }

  /**
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code int}
   * divisor and sets the remainder as the value of this {@link BigInt}.
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
   * Divides this {@link BigInt} by the provided {@code int} divisor and sets
   * the remainder as the value of this {@link BigInt}.
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
   * Divides this {@link BigInt} by the provided <i>unsigned</i> {@code long}
   * divisor and sets the remainder as the value of this {@link BigInt}.
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
   * Divides this {@link BigInt} by the provided {@code long} divisor and sets
   * the remainder as the value of this {@link BigInt}.
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
   * Divides this {@link BigInt} by the provided {@code long} divisor and sets
   * the remainder as the value of this {@link BigInt}.
   * <p>
   * Satisfies: {@code q * div + r = this}, where {@code q = floor(this / div)}
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
   * Divides this {@link BigInt} by the specified {@link BigInt} divisor and sets
   * the modulus as the value of this {@link BigInt}.
   * <p>
   * <i><b>Note:</b> This method differs from {@link #rem(BigInt)} in that it
   * always returns a <i>non-negative</i> result.</i>
   * <p>
   *
   * @param div The {@link BigInt} divisor.
   * @return {@code this}
   * @complexity O(n^2)
   */
  public BigInt mod(final BigInt div) {
    val = mod(val, div.val);
    return this;
  }

  /**
   * Returns the number of bits in the two's complement representation of this
   * {@link BigInt} that differ from its sign bit.
   *
   * @return Number of bits in the two's complement representation of this
   *         {@link BigInt} that differ from its sign bit.
   * @complexity O(n)
   */
  public int bitCount() {
    return bitCount(val);
  }

  /**
   * Returns the number of bits in the minimal two's-complement representation
   * of this {@link BigInt}, <em>excluding</em> a sign bit. For positive
   * numbers, this is equivalent to the number of bits in the ordinary binary
   * representation. For zero this method returns {@code 0}.
   * <p>
   * Computes:
   *
   * <pre>
   * ceil(log2(this < 0 ? -this : this + 1))
   * </pre>
   *
   * @return Number of bits in the minimal two's-complement representation of
   *         this {@link BigInt}, <em>excluding</em> a sign bit.
   * @complexity O(n)
   */
  public int bitLength() {
    return bitLength(val);
  }

  /**
   * Returns the number of digits in this {@link BigInt} (radix 10).
   *
   * @return The number of digits in this {@link BigInt} (radix 10).
   * @complexity O(n^2) // FIXME: There must be a more efficient way to do this!
   */
  public int precision() {
    return precision(val);
  }

  /**
   * Shifts this {@link BigInt} left by the specified number of bits. The shift
   * distance, {@code num}, may be negative, in which case this method performs
   * a right shift.
   *
   * <pre>
   * this << num
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
   * Shifts this {@link BigInt} right by the specified number of bits. The shift
   * distance, {@code num}, may be negative, in which case this method performs
   * a left shift.
   *
   * <pre>
   * this >> num
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
   * <p>
   * Computes:
   *
   * <pre>
   * this | (1 << n)
   * </pre>
   *
   * @param bit The index of the bit to test.
   * @return {@code true} if the given bit is set in this {@link BigInt},
   *         otherwise {@code false}.
   * @complexity O(n)
   */
  public boolean testBit(final int bit) {
    return testBit(val, bit);
  }

  /**
   * Sets the specified bit in this {@link BigInt}.
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
   * @param bit The bit to flip.
   * @return {@code this}
   * @complexity O(n)
   */
  public BigInt flipBit(final int bit) {
    val = flipBit(val, bit);
    return this;
  }

  /**
   * Performs a bitwise "and" of the specified {@link BigInt} mask onto this
   * {@link BigInt}.
   *
   * <pre>
   * this = this & mask
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
   * Performs a bitwise "or" of the specified {@link BigInt} mask onto this
   * {@link BigInt}.
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
   * Performs a bitwise "xor" of the specified {@link BigInt} mask onto this
   * {@link BigInt}.
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
   * Performs a bitwise "and-not" of the specified {@link BigInt} mask onto this
   * {@link BigInt}.
   *
   * <pre>
   * this = this & ~mask
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
   * <p>
   * <i><b>Note:</b> The returned number may be a {@code new int[]} instance if
   * the number resulting from the operation requires a larger array.</i>
   *
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
   * Returns a byte array containing the two's-complement representation of this
   * {@link BigInt}. The byte array will be in the endian order as specified by
   * the {@code littleEndian} argument. The array will contain the minimum
   * number of bytes required to represent this {@link BigInt}, including at
   * least one sign bit, which is {@code (ceil((bitLength(val) + 1) / 8))}.
   *
   * @param littleEndian Whether the produced byte array is to be encoded in
   *          <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return A byte array containing the two's-complement representation of this
   *         {@link BigInt}.
   */
  public byte[] toByteArray(final boolean littleEndian) {
    return toByteArray(val, littleEndian);
  }

  /**
   * Compares the absolute values of this {@link BigInt} to the provided
   * {@link BigInt}, and returns one of {@code -1}, {@code 0}, or {@code 1}
   * whether the absolute value of {@code this} is less than, equal to, or
   * greater than that of the provided {@link BigInt}, respectively.
   *
   * @param o The {@link BigInt} with which to compare.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the absolute value of
   *         {@code this} is less than, equal to, or greater than that of he
   *         provided {@link BigInt}, respectively.
   * @complexity O(n)
   */
  public int compareToAbs(final BigInt o) {
    return compareToAbs(val, o.val);
  }

  /**
   * Compares the values of this {@link BigInt} to the provided {@link BigInt},
   * and returns one of {@code -1}, {@code 0}, or {@code 1} whether the value of
   * {@code this} is less than, equal to, or greater than that of the provided
   * {@link BigInt}, respectively.
   *
   * @param o The {@link BigInt} with which to compare.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the value of
   *         {@code this} is less than, equal to, or greater than that of he
   *         provided {@link BigInt}, respectively.
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
   * @return {@code true} if this {@link BigInt} and the provided object are
   *         equal, otherwise {@code false}.
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