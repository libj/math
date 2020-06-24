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

  /** -1 for negative, 0 for zero, or 1 for positive. */
  private int signum;

  /** The magnitude in <i>little-endian</i> order. */
  private int[] mag;

  /** The number of digits of the number (in base 2^32). */
  private int len;

  /**
   * Creates a BigInt from the given parameters. The input-array will be used as
   * is and not be copied.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number, the first position gives the least
   *          significant 32 bits.
   * @param len The (first) number of entries of v that are considered part of
   *          the number.
   * @complexity O(1)
   */
  public BigInt(final int signum, final int[] mag, final int len) {
    assign(signum, mag, len);
  }

  /**
   * Creates a BigInt from the given parameters. The contents of the input-array
   * will be copied.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number, the first position gives the least
   *          significant 8 bits.
   * @param len The (first) number of entries of v that are considered part of
   *          the number.
   * @complexity O(n)
   */
  public BigInt(final int signum, final byte[] mag, int len) {
    if (signum == 0) {
      setToZero();
      return;
    }

    while (len > 1 && mag[len - 1] == 0)
      --len;

    this.mag = new int[(len + 3) / 4];
    assign0(signum, mag, len);
  }

  /**
   * Creates a BigInt from the given parameters. The input-value will be
   * interpreted as unsigned.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number.
   * @complexity O(1)
   */
  public BigInt(final int signum, final int mag) {
    this.mag = new int[1];
    uassign(signum, mag);
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
    mag = new int[2];
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
    mag = new int[1];
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
    mag = new int[2];
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
    if (mag[0] != 0)
      return false;

    len = 1;
    signum = 0;
    return true;
  }

  private BigInt assertZeroSignum() {
    if (len == 0 || (isZero(mag, len) ? signum != 0 : signum == 0))
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
    return new BigInt(signum, Arrays.copyOf(mag, len), len);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param The BigInt to copy/assign to this BigInt.
   * @complexity O(n)
   */
  public BigInt assign(final BigInt other) {
    signum = other.signum;
    return assign(other.mag, other.len);
  }

  /**
   * Assigns the content of the given magnitude array and the length to this
   * number. The contents of the input will be copied.
   *
   * @param v The new magnitude array content.
   * @param vlen The length of the content, vlen > 0.
   * @complexity O(n)
   */
  private BigInt assign(final int[] v, final int vlen) {
    if (vlen > mag.length)
      mag = new int[vlen + 2];

    System.arraycopy(v, 0, mag, 0, len = vlen);
    return assertZeroSignum();
  }

  /**
   * Assigns the given BigInt parameter to this number. The input magnitude
   * array will be used as is and not copied.
   *
   * @param sign The sign of the number.
   * @param mag The magnitude of the number.
   * @param len The length of the magnitude array to be used.
   * @complexity O(1)
   */
  public BigInt assign(final int sign, final int[] mag, final int len) {
    this.signum = sign;
    this.len = len;
    this.mag = mag;
    return assertZeroSignum();
  }

  /**
   * Assigns the given BigInt parameter to this number. Assumes no leading
   * zeroes of the input-array, i.e. that v[vlen-1]!=0, except for the case when
   * vlen==1.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number.
   * @param vlen The length of the magnitude array to be used.
   * @complexity O(n)
   */
  public BigInt assign(final int signum, final byte[] mag, final int vlen) {
    return signum == 0 ? setToZero() : assign0(signum, mag, vlen);
  }

  private BigInt assign0(final int signum, final byte[] mag, final int len) {
    this.len = (len + 3) / 4;
    if (this.len > this.mag.length)
      this.mag = new int[this.len + 2];

    int tmp = len / 4;
    int j = 0;
    for (int i = 0; i < tmp; ++i, j += 4)
      this.mag[i] = mag[j + 3] << 24 | (mag[j + 2] & 0xFF) << 16 | (mag[j + 1] & 0xFF) << 8 | mag[j] & 0xFF;

    if (tmp != this.len) {
      tmp = mag[j++] & 0xFF;
      if (j < len) {
        tmp |= (mag[j++] & 0xFF) << 8;
        if (j < len)
          tmp |= (mag[j] & 0xFF) << 16;
      }

      this.mag[this.len - 1] = tmp;
    }

    this.signum = isZero() ? 0 : signum;
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
   * @param s A char array representing the number in decimal.
   * @complexity O(n^2)
   */
  public BigInt assign(final char[] s) {
    signum = s[0] == '-' ? -1 : 1;

    final int from = signum - 1 >> 1;
    len = s.length + from;
    // 3402 = bits per digit * 1024
    final int alloc = len < 10 ? 1 : (int)(len * 3402L >>> 10) + 32 >>> 5;
    if (mag == null || alloc > mag.length)
      mag = new int[alloc];

    int j = len % 9;
    if (j == 0)
      j = 9;

    j -= from;
    mag[0] = parse(s, -from, j);
    for (len = 1; j < s.length;)
      len = mulAdd(mag, len, 1_000_000_000, parse(s, j, j += 9));

    clear(mag, len);
    if (isZero())
      signum = 0;

    return assertZeroSignum();
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt uassign(final int signum, final int mag) {
    this.signum = mag == 0 ? 0 : signum;
    this.mag[0] = mag;
    this.len = 1;
    return assertZeroSignum();
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param signum The sign of the number.
   * @param mag The magnitude of the number (will be interpreted as unsigned).
   * @complexity O(1)
   */
  public BigInt uassign(final int signum, final long mag) {
    if (mag == 0)
      return setToZero();

    this.signum = signum;
    this.mag[0] = (int)(mag & LONG_INT_MASK); // OLD: (int)(val & LONG_INT_MASK)
    final int mh = (int)(mag >>> 32);
    if (mh != 0) {
      if (this.mag.length < 2)
        this.mag = realloc(this.mag, this.len, 2);
      else
        clear(this.mag, 2);

      this.mag[1] = mh;
      this.len = 2;
    }
    else {
      this.len = 1;
    }

    return assertZeroSignum();
  }

  /**
   * Assigns the given non-negative number to this BigInt object.
   *
   * @param mag The number interpreted as unsigned.
   * @complexity O(1)
   */
  public BigInt uassign(final int mag) {
    return uassign(1, mag);
  }

  /**
   * Assigns the given non-negative number to this BigInt object.
   *
   * @param mag The number interpreted as unsigned.
   * @complexity O(1)
   */
  public BigInt uassign(final long mag) {
    return uassign(1, mag);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param mag The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final int mag) {
    return mag < 0 ? uassign(-1, -mag) : uassign(1, mag);
  }

  /**
   * Assigns the given number to this BigInt object.
   *
   * @param mag The number to be assigned.
   * @complexity O(1)
   */
  public BigInt assign(final long mag) {
    return mag < 0 ? uassign(-1, -mag) : uassign(1, mag);
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
    if (signum == -1)
      signum = 1;

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
    return isZero(mag, len);
  }

  /**
   * Sets this number to zero.
   *
   * @complexity O(1)
   */
  public BigInt setToZero() {
    len = setToZero(mag);
    signum = 0;
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
    final int newSignum = signum >= 0 || (len > 1 || (mag[0] & LONG_INT_MASK) > (a & LONG_INT_MASK)) ? signum : 1;
    mag = BigAddition.uadd(mag, len, signum, a);
    len = getLen(mag);
    signum = newSignum;
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
    final long mag0l = mag[0] & LONG_INT_MASK;
    final long mag0h = len == 1 ? 0 : mag[1] & LONG_INT_MASK;
    final int newSignum = signum >= 0 || (len > 2 || len == 2 && (mag0h > ah || mag0h == ah && mag0l >= al) || ah == 0 && mag0l >= al) ? signum : 1;
    mag = BigAddition.uadd(mag, mag0l, mag0h, len, signum, al, ah, true);
    len = getLen(mag);
    signum = isZero() ? 0 : newSignum;
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
    final int newSignum = signum >= 0 && len == 1 && (mag[0] & LONG_INT_MASK) < (s & LONG_INT_MASK) ? -1 : signum;
    mag = BigAddition.usub(mag, len, signum, s);
    len = getLen(mag);
    signum = isZero() ? 0 : newSignum;
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
    final long al = s & LONG_INT_MASK;
    final int newSignum = signum < 0 || (len > 2 || len == 2 && ((mag[1] & LONG_INT_MASK) > sh || (mag[1] & LONG_INT_MASK) == sh && (mag[0] & LONG_INT_MASK) >= al) || sh == 0 && (mag[0] & LONG_INT_MASK) >= al) ? signum : -1;
    mag = BigAddition.uadd(mag, len, signum, s, false);
    len = getLen(mag);
    signum = isZero() ? 0 : newSignum;
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
    if (len == mag.length)
      mag = realloc(mag, len);

    len = BigMultiplication.umul(mag, len, m);
    return assertZeroSignum();
  }

  /**
   * Multiplies this number with an unsigned {@code long}.
   *
   * @param m The amount by which to multiply (unsigned).
   * @complexity O(n)
   */
  public BigInt umul(final long m) {
    if (isZero())
      return this;

    if (m == 0) {
      setToZero();
      return assertZeroSignum();
    }

    final long mh = m >>> 32;
    return mh == 0 ? umul0((int)m) : umul0(m, mh);
  }

  private BigInt umul0(final long m, final long mh) {
    if (len + 2 >= mag.length)
      mag = realloc(mag, len, 2 * len + 1);

    len = BigMultiplication.umul(mag, len, m & LONG_INT_MASK, mh);
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
    mag = karatsuba(mag, len, m.mag, m.len, p);
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

    final int r = BigDivision.udiv(mag, len, d);
    len = trimLen(mag, len);
    signum = checkSig(mag, len, signum);
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
    if (isZero())
      return 0;

    final long dh = d >>> 32;
    return dh == 0 ? udiv((int)d) & LONG_INT_MASK : udiv0(d, dh);
  }

  private long udiv0(final long d, final long dh) {
    final long r = BigDivision.udiv(mag, len, d, dh);
    if (!checkSetZero())
      len = trimLen(mag, len);

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
    mag[0] = (int)BigDivision.urem(mag, len, m);
    len = 1;
    signum = checkSig(mag, len, signum);
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
    len = BigDivision.urem(mag, len, mod);
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
      assign(a);
    else if (a > 0)
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
      return assign(a);

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
      return assign(a);

    if (a.isZero())
      return this;

    final int c;
    final int newSignum = signum == a.signum || (c = compareAbsTo(mag, len, a.mag, a.len)) > 0 ? signum : c == 0 ? 0 : -signum;
    mag = BigAddition.add(mag, len, signum, a.mag, a.len, a.signum, true);
    len = getLen(mag);
    signum = isZero() ? 0 : newSignum;
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
      return s == Integer.MIN_VALUE ? uassign(1, s) : assign(-s);

    if (s > 0)
      usub0(s);
    else if (s < 0) {
      if (s == Integer.MIN_VALUE) {
        long l = -(long)s;
        uadd0(l, 0);
      }
      else {
        uadd0(-s);
      }
    }

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
      return s == Long.MIN_VALUE ? uassign(1, s) : assign(-s);

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
    if (s.isZero())
      return this;

    if (isZero())
      return assign(-s.signum, s.mag, s.len);

    final int c;
    final int newSignum = signum != s.signum || (c = compareAbsTo(mag, len, s.mag, s.len)) > 0 ? signum : c == 0 ? 0 : -signum;
    mag = BigAddition.add(mag, len, signum, s.mag, s.len, s.signum, false);
    len = getLen(mag);
    signum = newSignum;
    return assertZeroSignum();
  }

  /**
   * Multiplies this number by an {@code int}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(int m) {
    if (m == 0) {
      setToZero();
      return assertZeroSignum();
    }

    if (isZero())
      return assertZeroSignum();

    if (m < 0) {
      m = -m;
      signum = -signum;
    }

    return umul0(m);
  }

  /**
   * Multiplies this number by a {@code long}.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n)
   */
  public BigInt mul(long m) {
    if (m == 0) {
      setToZero();
      return assertZeroSignum();
    }

    if (isZero())
      return assertZeroSignum();

    if (m < 0) {
      m = -m;
      signum = -signum;
    }

    return umul0(m, m >>> 32);
  }

  /**
   * Multiplies this number by a {@link BigInt}.
   * <p>
   * Chooses the appropriate algorithm with regards to the size of the numbers.
   *
   * @param m The amount by which to multiply.
   * @complexity O(n^2) - O(n log n)
   */
  // FIXME:
  public BigInt mul(final BigInt m) {
    if (isZero(m.mag, m.len)) {
      setToZero();
      return assertZeroSignum();
    }

    if (isZero(mag, len))
      return assertZeroSignum();

    if (m.len <= 2 || len <= 2) {
      signum *= m.signum;
      if (m.len == 1) {
        umul(m.mag[0]);
      }
      else if (len == 1) {
        final int tmp = mag[0];
        assign(m.mag, m.len);
        umul(tmp);
      }
      else if (m.len == 2) {
        umul((long)m.mag[1] << 32 | (m.mag[0] & LONG_INT_MASK));
      }
      else {
        final long tmp = (long)mag[1] << 32 | (mag[0] & LONG_INT_MASK);
        assign(m.mag, m.len);
        umul(tmp);
      }
    }
    else if (len < 128 || m.len < 128 || (long)len * m.len < 1_000_000) {
      final int[] res = new int[len + m.len];
      mag = BigMultiplication.smallMul(res, mag, len, m.mag, m.len); // Remove overhead?
      if (!checkSetZero()) {
        len = getLen(mag); // FIXME: Or trimlen?
        signum *= m.signum;
      }
    }
    else {
      if (m.mag.length < len)
        m.mag = realloc(mag, len, len);
      else if (mag.length < m.len)
        mag = realloc(mag, len, m.len);

      try {
        if (Math.max(len, m.len) < 20000)
          mag = BigMultiplication.karatsuba(mag, len, m.mag, m.len, false); // Tune thresholds and remove hardcode.
        else
          mag = BigMultiplication.karatsuba(mag, len, m.mag, m.len, true);
      }
      catch (final ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }

      len += m.len;
      signum *= m.signum;
    }

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

    if (d >= 0)
      return signum * udiv(d);

    signum = -signum;
    return -signum * udiv(-d);
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

    if (d >= 0)
      return signum * udiv(d);

    signum = -signum;
    return -signum * udiv(-d);
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

    final int newSignum;
    if (d.len == 1) {
      newSignum = signum * d.signum;
    }
    else {
      final int c = compareAbsTo(mag, len, d.mag, d.len);
      if (c < 0)
        newSignum = 0;
      else if (c == 0)
        newSignum = 1;
      else
        newSignum = signum * d.signum;
    }

    mag = BigDivision.div(mag, len, signum, d.mag, d.len, d.signum);
    len = getLen(mag);
    signum = d.len == 1 ? checkSig(mag, len, newSignum) : newSignum;
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
    final int newSignum = d.len != 1 && compareAbsTo(mag, len, d.mag, d.len) == 0 ? 0 : signum;
    mag = BigDivision.rem(mag, len, d.mag, d.len);
    len = getLen(mag);
    signum = d.len == 1 ? checkSig(mag, len, signum) : newSignum;
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
  // FIXME:
  public BigInt divRem(final BigInt d) {
    int tmp = signum;
    if (d.len == 1) {
      signum *= d.signum;
      return new BigInt(tmp, udiv(d.mag[0]));
    }

    tmp = compareAbsTo(mag, len, d.mag, d.len);
    if (tmp < 0) {
      final BigInt cpy = new BigInt(signum, mag, len);
      mag = new int[2];
      len = 1; // setToZero()
      return cpy;
    }

    if (tmp == 0) {
      uassign(1, signum *= d.signum);
      return new BigInt(1, 0);
    }

    final int[] q = new int[len - d.len + 1];
    if (len == mag.length)
      mag = realloc(mag, len, len + 1); // We need an extra slot.

    BigDivision.div(mag, d.mag, len, d.len, q);

    final int[] r = mag;
    mag = q;
    for (len = q.length; len > 1 && mag[len - 1] == 0; --len);

    tmp = d.len;
    while (tmp > 1 && r[tmp - 1] == 0)
      --tmp;

    signum *= d.signum;
    return new BigInt(signum / d.signum, r, tmp);
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
    mag = BigDivision.mod(mag, len, signum, d.mag, d.len, d.signum);
    len = getLen(mag);
    signum = isZero() ? 0 : 1;
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
    mag = BigBinary.shiftLeft(mag, len, s);
    len = getLen(mag);
    return assertZeroSignum();
  }

  /**
   * Shifts this number right by the given amount.
   *
   * @param s The amount to shift.
   * @complexity O(n)
   */
  public BigInt shiftRight(final int s) {
    len = BigBinary.shiftRight(mag, len, s);
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

    return BigBinary.testBit(mag, len, signum, i);
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

    mag = BigBinary.setBit(mag, len, signum, i);
    len = getLen(mag);
    if (signum == 0)
      signum = 1;

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

    mag = BigBinary.clearBit(mag, len, signum, i);
    len = getLen(mag);
    if (isZero())
      signum = 0;

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

    mag = BigBinary.flipBit(mag, len, signum, i);
    len = getLen(mag);
    if (signum == 0)
      signum = 1;
    else if (isZero())
      signum = 0;

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

    mag = BigBinary.and(mag, len, signum, mask.mag, mask.len, mask.signum);
    if (mask.signum == 0)
      return setToZero();

    len = getLen(mag);
    if (isZero())
      signum = 0;
    else if (signum <= 0 && mask.signum > 0)
      signum = 1;

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

    mag = BigBinary.or(mag, len, signum, mask.mag, mask.len, mask.signum);
    len = getLen(mag);
    if (signum >= 0 && mask.signum <= 0)
      signum = -1;

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

    mag = BigBinary.xor(mag, len, signum, mask.mag, mask.len, mask.signum);
    len = getLen(mag);
    if (isZero())
      signum = 0;
    else if (mask.signum <= 0)
      signum = signum >= 0 ? -1 : 1;
    else if (signum == 0)
      signum = 1;

    return assertZeroSignum();
  }

  /**
   * Bitwise-and-nots this number with the given number, i.e. this &= ~mask.
   *
   * @param m The mask with which to bitwise-"and-not".
   * @complexity O(n)
   */
  public BigInt andNot(final BigInt m) {
    mag = BigBinary.andNot(mag, len, signum, m.mag, m.len, m.signum);
    len = getLen(mag);
    if (isZero())
      signum = 0;
    else if (signum <= 0 && m.signum < 0)
      signum = 1;

    return assertZeroSignum();
  }

  /**
   * Inverts sign and all bits of this number, i.e. this = ~this. The identity
   * -this = ~this + 1 holds.
   *
   * @complexity O(n)
   */
  public BigInt not() {
    mag = BigBinary.not(mag, len, signum);
    len = getLen(mag);
    signum = signum == 0 ? -1 : isZero() ? 0 : -signum;
    return assertZeroSignum();
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code byte}.
   *
   * @return {@code sign * (this & 0x7F)}
   */
  @Override
  public byte byteValue() {
    return byteValue(mag, signum);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code short}.
   *
   * @return {@code sign * (this & 0x7FFF)}
   */
  @Override
  public short shortValue() {
    return shortValue(mag, signum);
  }

  /**
   * {@inheritDoc} Returns this BigInt as an {@code int}.
   *
   * @return {@code sign * (this & 0x7FFFFFFF)}
   */
  @Override
  public int intValue() {
    return intValue(mag, signum);
  }

  /**
   * {@inheritDoc} Returns this BigInt as a {@code long}.
   *
   * @return {@code sign * (this & 0x7FFFFFFFFFFFFFFF)}
   */
  @Override
  public long longValue() {
    return longValue(mag, len, signum);
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
    return floatValue(mag, len, signum);
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
    return doubleValue(mag, len, signum);
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
    return compareAbsTo(mag, len, o.mag, o.len);
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
    return compareTo(mag, len, signum, o.mag, o.len, o.signum);
  }

  /**
   * Tests equality of this number and the given one.
   *
   * @param n The number to be compared with.
   * @return true if the two numbers are equal, false otherwise.
   * @complexity O(n)
   */
  public boolean equals(final BigInt n) {
    return equals(mag, len, signum, n.mag, n.len, n.signum);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || obj instanceof BigInt && equals((BigInt)obj);
  }

  @Override
  public int hashCode() {
    return hashCode(mag, len, signum);
  }

  /**
   * Converts this number into a string of radix 10.
   *
   * @return The string representation of this number in decimal.
   * @complexity O(n^2)
   */
  @Override
  public String toString() {
    return toString(mag, len, signum);
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

    // Count the bits in the magnitude
    for (i = 0; i < len; ++i)
      bits += Integer.bitCount(mag[i]);

    if (signum < 0) {
      // Count the trailing zeros in the magnitude
      for (i = 0; i < len && mag[i] == 0; ++i)
        bits += 32;

      bits += Integer.numberOfTrailingZeros(mag[i]) - 1;
    }

    return bits;
  }
}