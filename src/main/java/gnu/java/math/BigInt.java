/* Copyright (c) 2020 LibJ
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

package gnu.java.math;

import java.util.concurrent.ExecutionException;

/**
 * <p>
 * A class for arbitrary-precision integer arithmetic purely written in Java.
 * </p>
 * <p>
 * This class does what {@link java.math.BigInteger} doesn't.<br />
 * It is <b>faster</b>, and it is <b>mutable</b>!<br />
 * It supports <b>ints</b> and <b>longs</b> as parameters!<br />
 * It has a way faster {@link #toString()} method!<br />
 * It utilizes a faster multiplication algorithm for those nasty big numbers!
 * </p>
 * <p>
 * Get it today! Because performance matters (and we like Java).
 * </p>
 *
 * @author Simon Klein
 * @version 0.7
 */
@SuppressWarnings("javadoc")
public class BigInt extends BigIntBinary implements Comparable<BigInt>, Cloneable {
  private static final long serialVersionUID = -4360183347203631370L;

  // FIXME: Rewrite this javadoc
  /**
   * The value array with the following encoding:<br>
   * <blockquote> <b>{@code val[1]}</b>: <ins>signum</ins>:
   * <code>{-1, 0, 1}</code><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;-1 for negative, 0 for zero, or 1 for positive.<br>
   * <b>{@code val[0]}</b>: <ins>length</ins>:
   * {@code [1, Integer.MAX_VALUE - 2]}<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;The number of base 2^32 digits in the number.<br>
   * <b>{@code val[2,val[0]-1]}</b>: <ins>digits</ins>:
   * {@code [Integer.MIN_VALUE, Integer.MAX_VALUE]}<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;The base 2^32 digits of the number in
   * <i>little-endian</i> order. </blockquote>
   */
  private int[] val;

  public int[] val() {
    return val;
  }

  /**
   * Creates a BigInt from the given parameters. The input-array will be used as
   * is and not be copied.
   *
   * @param val The magnitude of the number, the first position gives the least
   *          significant 32 bits.
   * @complexity O(1)
   */
  public BigInt(final int[] val) {
    assign(val);
  }

  /**
   * Creates a BigInt from the given parameters. The contents of the input-array
   * will be copied.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number, the first position gives the least
   *          significant 8 bits.
   * @param len The (first) number of entries of v that are considered part of
   *          the number.
   * @complexity O(n)
   */
  public BigInt(final int sig, final byte[] mag, int len) {
    val = assign(emptyVal, sig, mag, len);
  }

  public BigInt(final int sig, final byte[] mag) {
    val = assign(emptyVal, sig, mag, mag.length);
  }

  /**
   * Creates a BigInt from the given parameters. The input-value will be
   * interpreted as unsigned.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number.
   * @complexity O(1)
   */
  public BigInt(final int sig, final int mag) {
    val = assign(alloc(2), sig, mag);
  }

  /**
   * Creates a BigInt from the given parameters. The input-value will be
   * interpreted as unsigned.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number.
   * @complexity O(1)
   */
  public BigInt(final int sig, final long mag) {
    val = assign(alloc(3), sig, mag);
  }

  /**
   * Creates a BigInt from the given int. The input-value will be interpreted a
   * signed value.
   *
   * @param mag The value of the number.
   * @complexity O(1)
   */
  public BigInt(final int mag) {
    val = assign(alloc(2), mag);
  }

  /**
   * Creates a BigInt from the given long. The input-value will be interpreted a
   * signed value.
   *
   * @param mag The value of the number.
   * @complexity O(1)
   */
  public BigInt(final long mag) {
    val = assign(alloc(3), mag);
  }

  /**
   * Creates a BigInt from the given string.
   *
   * @param s A string representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt(final String s) {
    val = assign(emptyVal, s);
  }

  /**
   * Creates a BigInt from the given char-array.
   *
   * @param s A char array representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt(final char[] s) {
    val = assign(emptyVal, s);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param The BigInt to copy/assign to this BigInt.
   * @complexity O(n)
   */
  // FIXME: Javadoc?!: This is not a clone!
  public BigInt assign(final BigInt b) {
    return assign(b.val);
  }

  /**
   * Assigns the given BigInt parameter to this number. The input magnitude
   * array will be used as is and not copied.
   *
   * @param sign The sign of the number.
   * @param val The magnitude of the number.
   * @param len The length of the magnitude array to be used.
   * @complexity O(1)
   */
  public BigInt assign(final int[] val) {
    this.val = val;
    _debugLenSig(val);
    return this;
  }

  /**
   * Assigns the given BigInt parameter to this number. Assumes no leading
   * zeroes of the input-array, i.e. that v[vlen-1]!=0, except for the case when
   * vlen==1.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number.
   * @param len The length of the magnitude array to be used.
   * @complexity O(n)
   */
  public BigInt assign(final int sig, final byte[] mag, final int len) {
    val = assign(val, sig, mag, len);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param s A char array representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt assign(final char[] s) {
    val = assign(val, s);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param s A string representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt assign(final String s) {
    val = assign(val, s);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt assign(final int sig, final int mag) {
    val = assign(val, sig, mag);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param sig The sign of the number.
   * @param mag The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt assign(final int sig, final long mag) {
    val = assign(val, sig, mag);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param mag The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final int mag) {
    val = assign(val, mag);
    return this;
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param mag The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final long mag) {
    val = assign(val, mag);
    return this;
  }

  /**
   * Sets to this {@link BigInt} the absolute value of its magnitude.
   *
   * <pre>
   * {@code v = | v |}
   * </pre>
   *
   * @return This {@link BigInt}.
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

  public int signum() {
    return signum(val);
  }

  public boolean isZero() {
    return isZero(val);
  }

  /**
   * Sets this number to zero.
   *
   * @complexity O(1)
   */
  public BigInt setToZero() {
    setToZero(val);
    return this;
  }

  /**
   * Adds an unsigned {@code int} to this number.
   *
   * @param add The amount to add (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt add(final int sig, final int add) {
    val = add(val, sig, add);
    return this;
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param sub The amount to subtract (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt usub(final int sig, final int sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Subtracts an unsigned {@code long} from this number.
   *
   * @param sub The amount to subtract (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt usub(final int sig, final long sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Adds an {@code int} to this number.
   *
   * @param add The amount to add.
   * @complexity O(n)
   */
  // Has not amortized O(1) due to the risk of
  // alternating +1 -1 on continuous sequence of
  // 1-set bits.
  public BigInt add(final int add) {
    val = add(val, add);
    return this;
  }

  /**
   * Adds a {@code long} to this number.
   *
   * @param add The amount to add.
   * @complexity O(n)
   */
  public BigInt add(final long add) {
    val = add(val, add);
    return this;
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param add The amount to add (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt add(final int sig, final long add) {
    val = add(val, sig, add);
    return this;
  }

  /**
   * Adds a {@link BigInt} to this number.
   *
   * @param add The number to add.
   * @complexity O(n)
   */
  public BigInt add(final BigInt add) {
    val = add(val, add.val);
    return this;
  }

  /**
   * Subtracts an {@code int} from this number.
   *
   * @param sub The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final int sub) {
    val = sub(val, sub);
    return this;
  }

  /**
   * Subtracts an {@code int} from this number.
   *
   * @param sub The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final int sig, final int sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Subtracts a {@code long} from this number.
   *
   * @param s The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final long sub) {
    val = sub(val, sub);
    return this;
  }

  /**
   * Subtracts a {@code long} from this number.
   *
   * @param s The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final int sig, final long sub) {
    val = sub(val, sig, sub);
    return this;
  }

  /**
   * Subtracts a {@link BigInt} from this number.
   *
   * @param sub The number to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final BigInt sub) {
    val = sub(val, sub.val);
    return this;
  }

  /**
   * Multiplies this number with an unsigned {@code int}.
   *
   * @param mul The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public BigInt mul(final int sig, final int mul) {
    val = mul(val, sig, mul);
    return this;
  }

  /**
   * Multiplies this number with an unsigned {@code long}.
   *
   * @param mul The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public BigInt mul(final int sig, final long mul) {
    val = mul(val, sig, mul);
    return this;
  }

  /**
   * Multiplies this number by an {@code int}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(final int m) {
    val = mul(val, m);
    return this;
  }

  /**
   * Multiplies this number by a {@code long}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(long m) {
    val = mul(val, m);
    return this;
  }

  /**
   * Multiplies this number by a {@link BigInt}.
   * <p>
   * Chooses the appropriate algorithm with regards to the size of the numbers.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n^2) - O(n log n)
   */
  public BigInt mul(final BigInt m) {
    val = mul(val, m.val);
    return this;
  }

  /**
   * Multiplies this number by the given {@link BigInt} using the Karatsuba
   * algorithm.
   * <p>
   * NOTE: Size of mag1 and mag2 must be the same!
   *
   * @param m The amount to multiply.
   * @param p Whether to attempt to use the parallel algorithm.
   */
  BigInt karatsuba(final BigInt m, final boolean p) throws ExecutionException, InterruptedException {
    val = karatsuba(val, m.val, p);
    return this;
  }

  /**
   * Divides this number by an unsigned {@code int}.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final int sig, final int div) {
    divRem(val, sig, div);
    return this;
  }

  /**
   * Divides this number by an unsigned {@code long}.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final int sig, final long div) {
    divRem(val, sig, div);
    return this;
  }

  /**
   * Divides this number by an {@code int}.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final int div) {
    divRem(val, div);
    return this;
  }

  /**
   * Divides this number by a {@code long}.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final long div) {
    divRem(val, div);
    return this;
  }

  /**
   * Divides this number by a {@link BigInt}.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final BigInt div) {
    val = div(val, div.val);
    return this;
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param div The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public BigInt rem(final int div) {
    rem(val, div);
    return this;
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param div The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public BigInt rem(final long div) {
    rem(val, div);
    return this;
  }

  /**
   * Applies the modulus of this number by an unsigned {@code int} (i.e.
   * {@code this = (this % div)}).
   *
   * @param div The amount by which to modulo (unsigned).
   * @complexity O(n)
   */
  public BigInt rem(final int sig, final int div) {
    rem(val, sig, div);
    return this;
  }

  /**
   * Applies the modulus of this number by an unsigned {@code long} (i.e.
   * {@code this = (this % mod)}).
   *
   * @param div The amount by which to modulo (unsigned).
   * @complexity O(n)
   */
  public BigInt rem(final int sig, final long div) {
    rem(val, sig, div);
    return this;
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param div The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public BigInt rem(final BigInt div) {
    val = rem(val, div.val);
    return this;
  }

  /**
   * Divides this number by a {@link BigInt} and returns the remainder.
   * <p>
   * Division by zero is undefined.
   *
   * @param div The amount by which to divide.
   * @return The remainder.
   * @complexity O(n^2)
   */
  // FIXME: I think we can just align div to return the remainder instead of this.
  public BigInt divRem(final BigInt div) {
    return new BigInt(divRem(val, div.val));
  }

  /**
   * Divides this number with an unsigned {@code int} and returns the unsigned
   * remainder.
   *
   * @param div The amount by which to divide (unsigned).
   * @return The absolute value of the remainder as an unsigned int.
   * @complexity O(n)
   */
  public long divRem(final int sig, final int div) {
    return divRem(val, sig, div);
  }

  public long divRem(final int div) {
    return divRem(val, div);
  }

  /**
   * Divides this number with an unsigned {@code long} and returns the
   * remainder.
   *
   * @param div The amount by which to divide (unsigned).
   * @return The absolute value of the remainder as an unsigned long.
   * @complexity O(n)
   */
  public long divRem(final int sig, final long div) {
    return divRem(val, sig, div);
  }

  public long divRem(final long div) {
    return divRem(val, div);
  }

  /**
   * Sets this number to {@code (this mod m}). This method differs from
   * {@link BigInt#rem(BigInt)} in that it always computes <i>non-negative</i>
   * result.
   *
   * @param div The number to use in the division causing the remainder.
   * @see #rem
   */
  public BigInt mod(final BigInt div) {
    val = mod(val, div.val);
    return this;
  }

  // Negative numbers are imagined in their two's complement form with infinite
  // sign extension.
  // This has no effect on bit shifts, but makes implementation of other bit
  // operations a bit
  // tricky if one wants them to be as efficient as possible.

  /**
   * Returns the number of bits in the two's complement representation of this
   * {@link BigInt} that differ from its sign bit. This method is useful when
   * implementing bit-vector style sets atop {@link BigInt}.
   *
   * @return The number of bits in the two's complement representation of this
   *         {@link BigInt} that differ from its sign bit.
   */
  public int bitCount() {
    return bitCount(val);
  }

  public int precision() {
    return precision(val);
  }

  /**
   * Shifts this number left by the given amount.
   *
   * @param s The amount to shift.
   * @complexity O(n)
   */
  public BigInt shiftLeft(final int s) {
    val = shiftLeft(val, s);
    return this;
  }

  /**
   * Shifts this number right by the given amount.
   *
   * @param s The amount to shift.
   * @complexity O(n)
   */
  public BigInt shiftRight(final int s) {
    val = shiftRight(val, s);
    return this;
  }

  /**
   * Tests if the bit at the given index is set.
   * <p>
   * NOT DEFINED FOR NEGATIVE INDEXES
   *
   * @param i The index of the bit to test.
   * @return {@code true} if the bit at the given index is {@code 1}, and
   *         {@code false} if {@code 0}.
   * @complexity O(n)
   */
  public boolean testBit(final int i) {
    return testBit(val, i);
  }

  /**
   * Sets the given bit in the number.
   * <p>
   * NOT DEFINED FOR NEGATIVE INDEXES
   *
   * @param i The index of the bit to set.
   * @complexity O(n)
   */
  public BigInt setBit(final int i) {
    val = setBit(val, i);
    return this;
  }

  /**
   * Clears the given bit in the number.
   * <p>
   * NOT DEFINED FOR NEGATIVE INDEXES
   *
   * @param i The index of the bit to clear.
   * @complexity O(n)
   */
  public BigInt clearBit(final int i) {
    val = clearBit(val, i);
    return this;
  }

  /**
   * Flips the given bit in the number.
   * <p>
   * NOT DEFINED FOR NEGATIVE INDEXES
   *
   * @param i The index of the bit to flip.
   * @complexity O(n)
   */
  public BigInt flipBit(final int i) {
    val = flipBit(val, i);
    return this;
  }

  /**
   * Bitwise-ands this number with the given number, i.e. this &= mask.
   *
   * @param mask The mask with which to bitwise-"and".
   * @complexity O(n)
   */
  public BigInt and(final BigInt mask) {
    val = and(val, mask.val);
    return this;
  }

  /**
   * Bitwise-ors this number with the given number, i.e. this |= mask.
   *
   * @param mask The mask with which to bitwise-"or".
   * @complexity O(n)
   */
  public BigInt or(final BigInt mask) {
    val = or(val, mask.val);
    return this;
  }

  /**
   * Bitwise-xors this number with the given number, i.e. this ^= mask.
   *
   * @param mask The mask with which to bitwise-"xor".
   * @complexity O(n)
   */
  public BigInt xor(final BigInt mask) {
    val = xor(val, mask.val);
    return this;
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param mask The mask with which to bitwise-"and-not".
   * @complexity O(n)
   */
  public BigInt andNot(final BigInt mask) {
    val = andNot(val, mask.val);
    return this;
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public BigInt not() {
    val = not(val);
    return this;
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code byte}.
   *
   * @return {@code sign * (this & 0x7F)}
   */
  @Override
  public byte byteValue() {
    return byteValue(val);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code short}.
   *
   * @return {@code sign * (this & 0x7FFF)}
   */
  @Override
  public short shortValue() {
    return shortValue(val);
  }

  /**
   * {@inheritDoc} Returns this BigInt as an {@code int}.
   *
   * @return {@code sign * (this & 0x7FFFFFFF)}
   */
  @Override
  public int intValue() {
    return intValue(val);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code long}.
   *
   * @return {@code sign * (this & 0x7FFFFFFFFFFFFFFF)}
   */
  @Override
  public long longValue() {
    return longValue(val);
  }

  public long longValueUnsigned() {
    return longValueUnsigned(val);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code float}.
   *
   * @return the most significant 24 bits in the mantissa (the highest order bit
   *         obviously being implicit), the exponent value which will be
   *         consistent for {@code BigInt}s up to 128 bits (should it not fit
   *         it'll be calculated modulo 256), and the sign bit set if this
   *         number is negative.
   */
  @Override
  public float floatValue() {
    return floatValue(val);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code double}.
   *
   * @return the most significant 53 bits in the mantissa (the highest order bit
   *         obviously being implicit), the exponent value which will be
   *         consistent for {@code BigInt}s up to 1024 bits (should it not fit
   *         it'll be calculated modulo 2048), and the sign bit set if this
   *         number is negative.
   */
  @Override
  public double doubleValue() {
    return doubleValue(val);
  }

  /**
   * Compares the absolute value of this and the given number.
   *
   * @param o The number to be compared with.
   * @return -1 if the absolute value of this number is less, 0 if it's equal, 1
   *         if it's greater.
   * @complexity O(n)
   */
  public int compareToAbs(final BigInt o) {
    return compareToAbs(val, o.val);
  }

  /**
   * Compares the value of this and the given number.
   *
   * @param o The number to be compared with.
   * @return -1 if the value of this number is less, 0 if it's equal, 1 if it's
   *         greater.
   * @complexity O(n)
   */
  @Override
  public int compareTo(final BigInt o) {
    return compareTo(val, o.val);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param n The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public boolean equals(final BigInt n) {
    return equals(val, n.val);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || obj instanceof BigInt && equals((BigInt)obj);
  }

  @Override
  public int hashCode() {
    return hashCode(val);
  }

  /**
   * Converts this number into a string of radix 10.
   *
   * @return The string representation of this number in decimal.
   * @complexity O(n^2)
   */
  @Override
  public String toString() {
    return toString(val);
  }

  /**
   * Creates a copy of this number.
   *
   * @return The BigInt copy.
   * @complexity O(n)
   */
  @Override
  public BigInt clone() {
    return new BigInt(val.clone());
  }
}