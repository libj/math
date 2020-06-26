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

import java.util.Arrays;
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
public class BigInt extends BigBinary implements Comparable<BigInt>, Cloneable {
  private static final long serialVersionUID = -4360183347203631370L;

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
   * @param signum The sign of the number.
   * @param val The magnitude of the number, the first position gives the least
   *          significant 8 bits.
   * @param len The (first) number of entries of v that are considered part of
   *          the number.
   * @complexity O(n)
   */
  public BigInt(final int signum, int len, final byte[] val) {
    if (signum == 0) {
      setToZero();
      return;
    }

    while (len > 1 && val[len - 1] == 0)
      --len;

    assign0(signum, len, val);
  }

  /**
   * Creates a BigInt from the given parameters. The input-value will be
   * interpreted as unsigned.
   *
   * @param signum The sign of the number.
   * @param val The magnitude of the number.
   * @complexity O(1)
   */
  public BigInt(final int signum, final int val) {
    this.val = new int[3];
    uassign(signum, val);
  }

  /**
   * Creates a BigInt from the given parameters. The input-value will be
   * interpreted as unsigned.
   *
   * @param sign The sign of the number.
   * @param val The magnitude of the number.
   * @complexity O(1)
   */
  public BigInt(final int sign, final long val) {
    this.val = new int[4];
    uassign(sign, val);
  }

  /**
   * Creates a BigInt from the given int. The input-value will be interpreted a
   * signed value.
   *
   * @param val The value of the number.
   * @complexity O(1)
   */
  public BigInt(final int val) {
    this.val = new int[3];
    assign(val);
  }

  /**
   * Creates a BigInt from the given long. The input-value will be interpreted a
   * signed value.
   *
   * @param val The value of the number.
   * @complexity O(1)
   */
  public BigInt(final long val) {
    this.val = new int[4];
    assign(val);
  }

  /**
   * Creates a BigInt from the given string.
   *
   * @param s A string representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt(final String s) {
    assign(s);
  }

  /**
   * Creates a BigInt from the given char-array.
   *
   * @param s A char array representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt(final char[] s) {
    assign(s);
  }

  private boolean checkSetZero() {
    if (val[1] != 0)
      return false;

    val[1] = 0;
    val[0] = 3;
    return true;
  }

  private BigInt assertZeroSignum() {
    if (val[0] <= 2 || (isZero(val) ? val[1] != 0 : val[1] == 0))
      throw new IllegalStateException();

    if (val[1] != 0 && val[val[0] - 1] == 0)
      throw new IllegalStateException();

    return this;
  }

  /**
   * Creates a copy of this number.
   *
   * @return The BigInt copy.
   * @complexity O(n)
   */
  @Override
  public BigInt clone() {
    return new BigInt(Arrays.copyOf(val, val[0]));
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param The BigInt to copy/assign to this BigInt.
   * @complexity O(n)
   */
  // FIXME: This is not a clone!
  public BigInt assign(final BigInt other) {
    return assign(other.val);
  }

  /**
   * Assigns the content of the given magnitude array and the length to this
   * number. The contents of the input will be copied.
   *
   * @param val The new magnitude array content.
   * @param len The length of the content, len > 0.
   * @complexity O(n)
   */
  // FIXME: 0 or 1 min len?
  private BigInt assignCopy(final int[] val) {
    final int len = val[0]; // FIXME: What if val[0] > val.length?!?!!
    if (len > this.val.length)
      this.val = new int[len + 2]; // FIXME: Add 2 just for shits and giggles?

    System.arraycopy(val, 0, this.val, 0, len);
    return assertZeroSignum();
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
    return assertZeroSignum();
  }

  /**
   * Assigns the given BigInt parameter to this number. Assumes no leading
   * zeroes of the input-array, i.e. that v[vlen-1]!=0, except for the case when
   * vlen==1.
   *
   * @param signum The sign of the number.
   * @param dig The magnitude of the number.
   * @param len The length of the magnitude array to be used.
   * @complexity O(n)
   */
  public BigInt assign(final int signum, final int len, final byte[] dig) {
    return signum == 0 ? setToZero() : assign0(signum, len, dig);
  }

  private BigInt assign0(final int signum, final int len, final byte[] val) {
    final int newLen = (len + 3) / 4;
    if (this.val == null || newLen > this.val.length)
      this.val = new int[newLen + 4]; // FIXME: Add 2 just for shits and giggles?

    int tmp = len / 4;
    int j = 0;
    for (int i = 0; i < tmp; ++i, j += 4)
      this.val[i + 2] = val[j + 3] << 24 | (val[j + 2] & 0xFF) << 16 | (val[j + 1] & 0xFF) << 8 | val[j] & 0xFF;

    if (tmp != newLen) {
      tmp = val[j++] & 0xFF;
      if (j < len) {
        tmp |= (val[j++] & 0xFF) << 8;
        if (j < len)
          tmp |= (val[j] & 0xFF) << 16;
      }

      this.val[newLen + 1] = tmp;
    }

    this.val[0] = newLen + 2;
    this.val[1] = isZero() ? 0 : signum;
    return assertZeroSignum();
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param s A char array representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt assign(final char[] s) {
    final int signum = s[0] == '-' ? -1 : 1;

    final int length = s.length;
    final int from = signum - 1 >> 1;
    int len = length + from;
    // 3402 = bits per digit * 1024
    int alloc = len < 10 ? 1 : (int)(len * 3402L >>> 10) + 32 >>> 5;
    alloc += 2;
    if (val == null || alloc > val.length)
      val = new int[alloc];

    int j = len % 9;
    if (j == 0)
      j = 9;

    j -= from;

    val[2] = parse(s, -from, j);
    for (val[0] = 3; j < length;)
      mulAdd(val, 1_000_000_000, parse(s, j, j += 9));

    val[1] = isZero(val) ? 0 : signum;
    return assertZeroSignum();
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param s A string representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt assign(final String s) {
    return assign(s.toCharArray());
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param signum The sign of the number.
   * @param val The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt uassign(final int signum, final int val) {
    return new BigInt(uassign(this.val, signum, val));
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param signum The sign of the number.
   * @param val The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt uassign(final int signum, final long val) {
    this.val = uassign(this.val, signum, val);
    return assertZeroSignum();
  }

  /**
   * Assigns the given non-negative number to this BigInt object.
   *
   * @param val The number interpreted as unsigned.
   * @complexity O(1)
   */
  public BigInt uassign(final int val) {
    return uassign(1, val);
  }

  /**
   * Assigns the given non-negative number to this BigInt object.
   *
   * @param val The number interpreted as unsigned.
   * @complexity O(1)
   */
  public BigInt uassign(final long val) {
    return uassign(1, val);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param val The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final int val) {
    return val == 0 ? setToZero() : val == Integer.MIN_VALUE ? uassign(-1, val) : val < 0 ? uassign(-1, -val) : uassign(1, val);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param val The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final long val) {
    // FIXME: Long.MIN_VALUE
    return val < 0 ? uassign(-1, -val) : uassign(1, val);
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
    return assertZeroSignum();
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
    return assertZeroSignum();
  }

  /**
   * Adds an unsigned {@code int} to this number.
   *
   * @param a The amount to add (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt uadd(final int a) {
    return a == 0 ? this : isZero() ? uassign(a) : uadd0(a);
  }

  private BigInt uadd0(final int a) {
    val = BigAddition.uadd(val, a);
    return assertZeroSignum();
  }

  /**
   * Adds an unsigned {@code long} to this number.
   *
   * @param a The amount to add (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt uadd(final long a) {
    if (a == 0)
      return assertZeroSignum();

    if (isZero())
      return uassign(a);

    final long ah = a >>> 32;
    return ah == 0 ? uadd0((int)a) : uadd0(a, ah);
  }

  private BigInt uadd0(final long a, final long ah) {
    final long al = a & LONG_INT_MASK;
    final long val0l = val[2] & LONG_INT_MASK;
    final long val0h = val[0] == 3 ? 0 : val[3] & LONG_INT_MASK;
    val = BigAddition.uadd(val, val0l, val0h, al, ah, true);
    return assertZeroSignum();
  }

  /**
   * Subtracts an unsigned {@code int} from this number.
   *
   * @param s The amount to subtract (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt usub(final int s) {
    return s == 0 ? this : isZero() ? uassign(-1, s) : usub0(s);
  }

  private BigInt usub0(final int s) {
    val = BigAddition.usub(val, s);
    return assertZeroSignum();
  }

  /**
   * Subtracts an unsigned {@code long} from this number.
   *
   * @param s The amount to subtract (unsigned).
   * @complexity O(n)
   * @amortized O(1)
   */
  public BigInt usub(final long s) {
    if (s == 0)
      return assertZeroSignum();

    if (isZero())
      return uassign(-1, s);

    final long sh = s >>> 32;
    return sh == 0 ? usub0((int)s) : usub0(s, sh);
  }

  private BigInt usub0(final long s, final long sh) {
    final long sl = s & LONG_INT_MASK;
    val = BigAddition.uadd(val, sl, sh, false);
    return assertZeroSignum();
  }

  /**
   * Multiplies this number with an unsigned {@code int}.
   *
   * @param m The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public BigInt umul(final int m) {
    return isZero() ? this : m == 0 ? setToZero() : umul0(m);
  }

  private BigInt umul0(final int m) {
    if (val[0] == val.length)
      val = realloc(val);

    val[0] = BigMultiplication.umul(val, 2, val[0], m);
    return assertZeroSignum();
  }

  /**
   * Multiplies this number with an unsigned {@code long}.
   *
   * @param m The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public BigInt umul(final long m) {
    return isZero() ? this : m == 0 ? setToZero() : umul0(m);
  }

  private BigInt umul0(final long m) {
    final long mh = m >>> 32;
    return mh == 0 ? umul0((int)m) : umul0(m & LONG_INT_MASK, mh);
  }

  private BigInt umul0(final long ml, final long mh) {
    final int len = val[0];
    if (len + 2 >= val.length)
      val = realloc(val, 2 * len + 1);

    val[0] = BigMultiplication.umul(val, 2, val[0], ml, mh);
    return assertZeroSignum();
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
    return assertZeroSignum();
  }

  /**
   * Divides this number with an unsigned {@code int} and returns the unsigned
   * remainder.
   *
   * @param d The amount by which to divide (unsigned).
   * @return The absolute value of the remainder as an unsigned int.
   * @complexity O(n)
   */
  public int udiv(final int d) {
    if (isZero())
      return 0;

    return udiv0(d);
  }

  private int udiv0(final int d) {
    final int r = BigDivision.udiv(val, d);
    assertZeroSignum();
    return r;
  }

  /**
   * Divides this number with an unsigned {@code long} and returns the
   * remainder.
   *
   * @param d The amount by which to divide (unsigned).
   * @return The absolute value of the remainder as an unsigned long.
   * @complexity O(n)
   */
  public long udiv(final long d) {
    return isZero() ? 0 : udiv0(d);
  }

  private long udiv0(final long d) {
    final long dh = d >>> 32;
    final long r = dh == 0 ? udiv((int)d) & LONG_INT_MASK : udiv0(d, dh);
    assertZeroSignum();
    return r;
  }

  private long udiv0(final long d, final long dh) {
    final long r = BigDivision.udiv(val, d, dh);
    assertZeroSignum();
    return r;
  }

  /**
   * Applies the modulus of this number by an unsigned {@code int} (i.e.
   * {@code this = (this % mod)}).
   *
   * @param m The amount by which to modulo (unsigned).
   * @complexity O(n)
   */
  public BigInt urem(final int m) {
    val[2] = (int)BigDivision.urem(val, m);
    val[0] = 3;
    if (isZero())
      val[1] = 0;

    return assertZeroSignum();
  }

  /**
   * Applies the modulus of this number by an unsigned {@code long} (i.e.
   * {@code this = (this % mod)}).
   *
   * @param mod The amount by which to modulo (unsigned).
   * @complexity O(n)
   */
  public BigInt urem(final long mod) {
    BigDivision.urem(val, mod);
    return assertZeroSignum();
  }

  /**
   * Adds an {@code int} to this number.
   *
   * @param a The amount to add.
   * @complexity O(n)
   */
  // Has not amortized O(1) due to the risk of
  // alternating +1 -1 on continuous sequence of
  // 1-set bits.
  public BigInt add(final int a) {
    if (isZero())
      return a == 0 ? this : assign(a);

    if (a > 0)
      uadd0(a);
    else if (a < 0)
      usub0(-a);

    return assertZeroSignum();
  }

  /**
   * Adds a {@code long} to this number.
   *
   * @param a The amount to add.
   * @complexity O(n)
   */
  public BigInt add(long a) {
    if (isZero())
      return a == 0 ? this : assign(a);

    if (a > 0) {
      final long ah = a >>> 32;
      return ah == 0 ? uadd0((int)a) : uadd0(a, ah);
    }

    if (a < 0) {
      a = -a;
      final long ah = a >>> 32;
      return ah == 0 ? usub0((int)a) : usub0(a, ah);
    }

    return assertZeroSignum();
  }

  /**
   * Adds a {@link BigInt} to this number.
   *
   * @param a The number to add.
   * @complexity O(n)
   */
  public BigInt add(final BigInt a) {
    if (isZero())
      return a.isZero() ? this : assign(a);

    if (a.isZero())
      return this;

    val = BigAddition.add(val, a.val, true);
    return assertZeroSignum();
  }

  /**
   * Subtracts an {@code int} from this number.
   *
   * @param s The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final int s) {
    if (isZero())
      return s == 0 ? this : s == Integer.MIN_VALUE ? uassign(1, s) : assign(-s);

    if (s > 0)
      usub0(s);
    else if (s == Integer.MIN_VALUE)
      uadd0(-(long)s, 0);
    else if (s < 0)
      uadd0(-s);

    return assertZeroSignum();
  }

  /**
   * Subtracts a {@code long} from this number.
   *
   * @param s The amount to subtract.
   * @complexity O(n)
   */
  public BigInt sub(long s) {
    if (isZero())
      return s == 0 ? this : s == Long.MIN_VALUE ? uassign(1, s) : assign(-s);

    if (s > 0) {
      final long ah = s >>> 32;
      return ah == 0 ? usub0((int)s) : usub0(s, ah);
    }

    if (s < 0) {
      final boolean isMinInt = s == Integer.MIN_VALUE;
      s = -s;
      final long ah = s >>> 32;
      return ah == 0 && !isMinInt ? uadd0((int)s) : uadd0(s, ah);
    }

    return assertZeroSignum();
  }

  /**
   * Subtracts a {@link BigInt} from this number.
   *
   * @param s The number to subtract.
   * @complexity O(n)
   */
  public BigInt sub(final BigInt s) {
    if (isZero()) {
      if (s.isZero())
        return this;

      assignCopy(s.val);
      val[1] = -val[1];
      return this;
    }

    if (s.isZero())
      return this;

    val = BigAddition.add(val, s.val, false);
    return assertZeroSignum();
  }

  /**
   * Multiplies this number by an {@code int}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(final int m) {
    if (m == 0)
      return setToZero();

    if (isZero())
      return assertZeroSignum();

    if (m > 0)
      return umul0(m);

    val[1] = -val[1];
    if (m != Integer.MIN_VALUE)
      return umul0(-m);

    final long l = -(long)m;
    return umul0(l & LONG_INT_MASK, l >>> 32);
  }

  /**
   * Multiplies this number by a {@code long}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(long m) {
    if (m == 0)
      return setToZero();

    if (isZero())
      return assertZeroSignum();

    final long mh = m >>> 32;
    if (mh == 0)
      return umul0((int)m);

    final long ml = m & LONG_INT_MASK;
    if (m > 0)
      return umul0(ml, mh);

    val[1] = -val[1];
    if (m == Long.MIN_VALUE)
      return umul0(ml, mh);

    m = -m;
    return umul0(m & LONG_INT_MASK, m >>> 32);
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
    final int[] val1 = val;
    final int[] val2 = m.val;
    if (isZero(val2))
      return setToZero();

    if (isZero(val1))
      return assertZeroSignum();

    val = BigMultiplication.mul(val, m.val);
    return assertZeroSignum();
  }

  /**
   * Divides this number by an {@code int}.
   *
   * @param d The amount by which to divide.
   * @complexity O(n)
   * @return The signed remainder.
   */
  public int div(final int d) {
    if (isZero())
      return 0;

    final int signum = val[1];
    if (d >= 0)
      return signum * udiv0(d);

    val[1] = -signum;
    if (d == Integer.MIN_VALUE)
      return signum * (int)udiv(-(long)d);

    return signum * udiv0(-d);
  }

  /**
   * Divides this number by a {@code long}.
   *
   * @param d The amount by which to divide.
   * @complexity O(n)
   * @return The signed remainder.
   */
  public long div(final long d) {
    if (isZero())
      return 0;

    final long dh = d >>> 32;
    if (dh == 0)
      return udiv0((int)d);

    final int signum = val[1];
    if (d > 0)
      return signum * udiv0(d, dh);

    if (d == Long.MIN_VALUE) {
      if (val[0] <= 3) {
        final long r = longValue();
        setToZero();
        return r == Long.MIN_VALUE ? 0 : r;
      }

      final int[] val2 = {
        4,
        d < 0 ? -1 : 1,
        (int)(d & LONG_INT_MASK),
        (int)(d >>> 32)
      };

      final int[] r = divRem(val, val2);
      return signum * longValue(r, 2, r[0]);
    }

    val[1] = -signum;
    return signum * udiv0(-d);
  }

  /**
   * Divides this number by a {@link BigInt}.
   * <p>
   * Division by zero is undefined.
   *
   * @param d The number by which to divide.
   * @complexity O(n^2)
   */
  public BigInt div(final BigInt d) {
    if (isZero())
      return assertZeroSignum();

//    final int newSignum;
//    if (d.len == 1) {
//      newSignum = signum * d.signum;
//    }
//    else {
//      final int c = compareAbsTo(val, len, d.val, d.len);
//      if (c < 0)
//        newSignum = 0;
//      else if (c == 0)
//        newSignum = 1;
//      else
//        newSignum = signum * d.signum;
//    }

    val = BigDivision.div(val, d.val);
    return assertZeroSignum();
  }

  /**
   * Sets this number to the remainder r satisfying q*div + r = this, where q =
   * floor(this/div).
   *
   * @param d The number to use in the division causing the remainder.
   * @complexity O(n^2)
   */
  public BigInt rem(final BigInt d) {
    val = BigDivision.rem(val, d.val);
    return assertZeroSignum();
  }

  /**
   * Divides this number by a {@link BigInt} and returns the remainder.
   * <p>
   * Division by zero is undefined.
   *
   * @param d The amount by which to divide.
   * @return The remainder.
   * @complexity O(n^2)
   */
  // FIXME: I think we can just align div to return the remainder instead of this.
  public BigInt divRem(final BigInt d) {
    return new BigInt(divRem(val, d.val));
  }

  /**
   * Sets this number to {@code (this mod m}). This method differs from
   * {@link BigInt#rem(BigInt)} in that it always computes <i>non-negative</i>
   * result.
   *
   * @param d The number to use in the division causing the remainder.
   * @see #rem
   */
  public BigInt mod(final BigInt d) {
    val = BigDivision.mod(val, d.val);
    return assertZeroSignum();
  }

  // Negative numbers are imagined in their two's complement form with infinite
  // sign extension.
  // This has no effect on bit shifts, but makes implementation of other bit
  // operations a bit
  // tricky if one wants them to be as efficient as possible.

  /**
   * Shifts this number left by the given amount.
   *
   * @param s The amount to shift.
   * @complexity O(n)
   */
  public BigInt shiftLeft(final int s) {
    val = BigBinary.shiftLeft(val, s);
    return assertZeroSignum();
  }

  /**
   * Shifts this number right by the given amount.
   *
   * @param s The amount to shift.
   * @complexity O(n)
   */
  public BigInt shiftRight(final int s) {
    val = BigBinary.shiftRight(val, s);
    return assertZeroSignum();
  }

  /**
   * Tests if the bit at the given index is set.
   *
   * @param i The index of the bit to test.
   * @return {@code true} if the bit at the given index is {@code 1}, and
   *         {@code false} if {@code 0}.
   * @complexity O(n)
   */
  public boolean testBit(final int i) {
    if (i < 0)
      throw new IllegalArgumentException("index (" + i + ") must be a positive integer");

    return BigBinary.testBit(val, i);
  }

  /**
   * Sets the given bit in the number.
   *
   * @param i The index of the bit to set.
   * @complexity O(n)
   */
  public BigInt setBit(final int i) {
    if (i < 0)
      throw new IllegalArgumentException("index (" + i + ") must be a positive integer");

    val = BigBinary.setBit(val, i);
    return assertZeroSignum();
  }

  /**
   * Clears the given bit in the number.
   *
   * @param i The index of the bit to clear.
   * @complexity O(n)
   */
  public BigInt clearBit(final int i) {
    if (i < 0)
      throw new IllegalArgumentException("index (" + i + ") must be a positive integer");

    val = BigBinary.clearBit(val, i);
    return assertZeroSignum();
  }

  /**
   * Flips the given bit in the number.
   *
   * @param i The index of the bit to flip.
   * @complexity O(n)
   */
  public BigInt flipBit(final int i) {
    if (i < 0)
      throw new IllegalArgumentException("index (" + i + ") must be a positive integer");

    val = BigBinary.flipBit(val, i);
    return assertZeroSignum();
  }

  /**
   * Bitwise-ands this number with the given number, i.e. this &= mask.
   *
   * @param mask The mask with which to bitwise-"and".
   * @complexity O(n)
   */
  public BigInt and(final BigInt mask) {
    if (mask.isZero())
      return setToZero();

    val = BigBinary.and(val, mask.val);
    return assertZeroSignum();
  }

  /**
   * Bitwise-ors this number with the given number, i.e. this |= mask.
   *
   * @param mask The mask with which to bitwise-"or".
   * @complexity O(n)
   */
  public BigInt or(final BigInt mask) {
    if (mask.isZero())
      return this;

    if (isZero())
      return assign(mask);

    val = BigBinary.or(val, mask.val);
    return assertZeroSignum();
  }

  /**
   * Bitwise-xors this number with the given number, i.e. this ^= mask.
   *
   * @param mask The mask with which to bitwise-"xor".
   * @complexity O(n)
   */
  public BigInt xor(final BigInt mask) {
    if (mask.isZero())
      return this;

    if (isZero())
      return assign(mask);

    val = BigBinary.xor(val, mask.val);
    return assertZeroSignum();
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param m The mask with which to bitwise-"and-not".
   * @complexity O(n)
   */
  public BigInt andNot(final BigInt m) {
    val = BigBinary.andNot(val, m.val);
    return assertZeroSignum();
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public BigInt not() {
    val = BigBinary.not(val);
    return assertZeroSignum();
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code byte}.
   *
   * @return {@code sign * (this & 0x7F)}
   */
  @Override
  public byte byteValue() {
    return byteValue(val, 2, val[1]);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code short}.
   *
   * @return {@code sign * (this & 0x7FFF)}
   */
  @Override
  public short shortValue() {
    return shortValue(val, 2, val[1]);
  }

  /**
   * {@inheritDoc} Returns this BigInt as an {@code int}.
   *
   * @return {@code sign * (this & 0x7FFFFFFF)}
   */
  @Override
  public int intValue() {
    return intValue(val, 2);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code long}.
   *
   * @return {@code sign * (this & 0x7FFFFFFFFFFFFFFF)}
   */
  @Override
  public long longValue() {
    return longValue(val, 2, val[0], val[1]);
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
    return floatValue(val, 2, val[0], val[1]);
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
    return doubleValue(val, 2, val[0], val[1]);
  }

  /**
   * Compares the absolute value of this and the given number.
   *
   * @param o The number to be compared with.
   * @return -1 if the absolute value of this number is less, 0 if it's equal, 1
   *         if it's greater.
   * @complexity O(n)
   */
  public int compareAbsTo(final BigInt o) {
    return compareAbsTo(val, o.val);
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
   * Returns the number of bits in the two's complement representation of this
   * {@link BigInt} that differ from its sign bit. This method is useful when
   * implementing bit-vector style sets atop {@link BigInt}.
   *
   * @return The number of bits in the two's complement representation of this
   *         {@link BigInt} that differ from its sign bit.
   */
  // FIXME: Implement "dirty" state
  public int bitCount() {
    int bits = 0;
    int i;

    final int signum = val[1];
    final int len = val[0];
    // Count the bits in the magnitude
    for (i = 2; i < len; ++i)
      bits += Integer.bitCount(val[i]);

    if (signum < 0) {
      // Count the trailing zeros in the magnitude
      for (i = 2; i < len && val[i] == 0; ++i)
        bits += 32;

      bits += Integer.numberOfTrailingZeros(val[i]) - 1;
    }

    return bits;
  }
}