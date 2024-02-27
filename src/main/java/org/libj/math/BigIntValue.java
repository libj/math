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

import java.util.Arrays;

import org.libj.lang.Numbers;

abstract class BigIntValue extends Number {
  static final int NATIVE_THRESHOLD;

  static {
    final NativeMath.Mode mode = NativeMath.loadNative();
    if (mode == NativeMath.Mode.JAVA)
      NATIVE_THRESHOLD = Integer.MAX_VALUE;
    else if (mode == NativeMath.Mode.NATIVE)
      NATIVE_THRESHOLD = 15;
    else if (mode == NativeMath.Mode.CRITICAL)
      NATIVE_THRESHOLD = 0;
    else
      throw new UnsupportedOperationException("Unsupported mode: " + mode);
  }

  /**
   * The maximum supported length of {@linkplain BigInt#val() value-encoded number} arrays.
   */
  public static final int MAX_VAL_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1; // (1 << 26)

  static final long LONG_MASK = 0xFFFFFFFFL;
  static final int[] ZERO = {0};
  static final int[] emptyVal = {};
  static final int OFF = 1;

  static final LocalArray threadLocal = new LocalArray();

  static final class LocalArray extends ThreadLocal<int[]> {
    private static final int INITIAL_SIZE = 17;

    @Override
    protected final int[] initialValue() {
      return new int[INITIAL_SIZE];
    }

    int[] get(final int len) {
      int[] array = super.get();
      if (array.length < len)
        set(array = new int[len]);

      return array;
    }
  }

  /**
   * Creates a new {@code int[]} with length that is at least {@code len}.
   * <p>
   * This method can return longer arrays tuned for optimal performance.
   *
   * @param len The minimal length of the returned {@code int[]}.
   * @return A new {@code int[]} with a length that is at least {@code len}.
   * @complexity O(1)
   */
  static int[] alloc(final int len) {
    // System.err.println("alloc");
    return new int[32 + len];
  }

  /**
   * Reallocates the provided array up to <i>a minimum</i> length {@code len} to a new array of length {@code newLen}.
   * <p>
   * This method can return longer arrays tuned for optimal performance.
   *
   * @param array The array to reallocate.
   * @param len The number of elements in the original array to copy to the returned array.
   * @param newLen The length of the returned array.
   * @return A new array of length {@code newLen} with {@code len} number of elements copied from {@code array}.
   * @complexity O(n)
   */
  static int[] realloc(final int[] array, final int len, final int newLen) {
    final int[] v = new int[32 + newLen];
    System.arraycopy(array, 0, v, 0, len);
    return v;
  }

  /**
   * Reallocates the provided array up to length {@code len} to a new array of length {@code newLen}.
   *
   * @param array The array to reallocate.
   * @param len The number of elements in the original array to copy to the returned array.
   * @param newLen The length of the returned array.
   * @return A new array of length {@code newLen} with {@code len} number of elements copied from {@code array}.
   * @complexity O(n)
   */
  static int[] reallocExact(final int[] array, final int len, final int newLen) {
    final int[] v = new int[newLen];
    System.arraycopy(array, 0, v, 0, len);
    return v;
  }

  /**
   * Assigns the content of {@code copyLength} number of elements of the specified source array to the provided target array, ensuring
   * the length of the target array is at least {@code arrayLength}.
   *
   * @implNote The returned array may be a {@code new int[]} instance if the length of the provided target array not sufficient to
   *           satisfy the required {@code arrayLength}.
   * @param source The source array.
   * @param target The target array.
   * @param copyLength The number of elements to copy from source to target.
   * @param arrayLength The minimal length of the target array.
   * @return The result of copying the source array to the target array.
   * @complexity O(n)
   */
  static int[] copy(final int[] source, final int copyLength, int[] target, final int arrayLength) {
    if (arrayLength > target.length)
      target = alloc(arrayLength);

    return copyInPlace(source, copyLength, target);
  }

  protected static int[] copyInPlace(final int[] source, final int copyLength, final int[] target) {
    System.arraycopy(source, 0, target, 0, copyLength);
    // _debugLenSig(target);
    return target;
  }

  /**
   * Trims the provided {@linkplain BigInt#val() value-encoded number} to the length of its value ({@code Math.abs(val[0])}). If the
   * length of the provided array is less than or equal to the length of its value, the provided instance is returned; otherwise, a
   * new {@code int[]} is created.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to trim.
   * @return A {@code int[]} with its length less than or equal to the length of the provided {@linkplain BigInt#val() value-encoded
   *         number}. If the length of the provided array is less than or equal to {@code length}, the provided instance is returned;
   *         otherwise, a new {@code int[]} is created.
   * @complexity O(n)
   */
  public static int[] trim(final int[] val) {
    return trim(val, Math.abs(val[0]));
  }

  /**
   * Trims the provided {@linkplain BigInt#val() value-encoded number} to the given {@code len} (which is equal to
   * {@code Math.abs(val[0])}. This is merely a shortcut function to avoid having to evaluate {@code Math.abs(val[0])} if it is known
   * by the calling frame. If the length of the provided array is less than or equal to {@code len + 1}, the provided instance is
   * returned; otherwise, a new {@code int[]} is created.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number} to trim.
   * @param len The number of limbs in the {@linkplain BigInt#val() value-encoded number}.
   * @return A {@code int[]} with its length less than or equal to {@code len + 1}. If the length of the provided array is less than
   *         or equal to {@code len + 1}, the provided instance is returned; otherwise, a new {@code int[]} is created.
   * @complexity O(n)
   */
  public static int[] trim(final int[] val, int len) {
    ++len;
    if (val.length <= len)
      return val;

    final int[] res = new int[len];
    System.arraycopy(val, 0, res, 0, len);
    return res;
  }

  /**
   * Assigns an {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mag The magnitude.
   * @return The result of assigning an {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final int mag) {
    return mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  /**
   * Assigns an <i>unsigned</i> {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @return The result of assigning an <i>unsigned</i> {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded
   *         number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final boolean sig, final int mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  /**
   * Assigns an <i>unsigned</i> {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param sig The sign of the unsigned {@code int}.
   * @param mag The magnitude (unsigned).
   * @return The result of assigning an <i>unsigned</i> {@code int} magnitude to the provided {@linkplain BigInt#val() value-encoded
   *         number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final int sig, final int mag) {
    return mag == 0 ? setToZero(val) : assignInPlace(val.length > 1 ? val : alloc(2), sig, mag);
  }

  protected static int[] assignInPlace(final int[] val, final int sig, final int mag) {
    val[0] = sig;
    val[1] = mag;
    // _debugLenSig(val);
    return val;
  }

  protected static int[] assignInPlace(final int[] val, final boolean sig, final int mag) {
    return assignInPlace(val, sig ? 1 : -1, mag);
  }

  /**
   * Assigns an {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param mag The magnitude.
   * @return The result of assigning an {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final long mag) {
    return mag < 0 ? assign(val, -1, -mag) : assign(val, 1, mag);
  }

  protected static int[] assignInPlace(final int[] val, final long mag) {
    return mag < 0 ? assignInPlace(val, -1, -mag) : assignInPlace(val, 1, mag);
  }

  /**
   * Assigns an <i>unsigned</i> {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param sig The sign of the unsigned {@code long}.
   * @param mag The magnitude (unsigned).
   * @return The result of assigning an <i>unsigned</i> {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded
   *         number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final boolean sig, final long mag) {
    return assign(val, sig ? 1 : -1, mag);
  }

  /**
   * Assigns an <i>unsigned</i> {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param sig The sign of the unsigned {@code long}.
   * @param mag The magnitude (unsigned).
   * @return The result of assigning an <i>unsigned</i> {@code long} magnitude to the provided {@linkplain BigInt#val() value-encoded
   *         number}.
   * @complexity O(1)
   */
  public static int[] assign(final int[] val, final int sig, final long mag) {
    if (mag == 0)
      return setToZero(val);

    final int magh = (int)(mag >>> 32);
    if (magh != 0)
      return assignInPlace(val.length >= 3 ? val : alloc(3), sig, (int)mag, magh);

    return assignInPlace(val.length >= 2 ? val : alloc(2), sig, (int)mag);
  }

  protected static int[] assignInPlace(final int[] val, final int sig, final long mag) {
    if (mag == 0)
      return setToZeroInPlace(val);

    final int magh = (int)(mag >>> 32);
    if (magh != 0)
      return assignInPlace(val, sig, (int)mag, magh);

    return assignInPlace(val, sig, (int)mag);
  }

  static int[] assignInPlace(final int[] val, final int sig, final int mag, final int magh) {
    val[0] = sig < 0 ? -2 : 2;
    val[1] = mag;
    val[2] = magh;
    // _debugLenSig(val);
    return val;
  }

  /**
   * Assigns the specified {@linkplain BigInt#val() value-encoded number} {@code val} to the contents of {@linkplain BigInt#val()
   * value-encoded number} {@code src}.
   *
   * <pre>
   * this = val
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The {@linkplain BigInt#val() value-encoded number} to receive the contents of {@code src}.
   * @param src The {@linkplain BigInt#val() value-encoded number} to provide its contents to {@code val}.
   * @return {@code this}
   * @complexity O(n)
   */
  public static int[] assign(int[] val, final int[] src) {
    final int len = Math.abs(src[0]);
    if (val.length <= len)
      val = alloc(len);

    return assignInPlace(val, src, len);
  }

  static int[] assignInPlace(final int[] val, final int[] src, final int len) {
    System.arraycopy(src, 0, val, 0, len + 1);
    return val;
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation of a {@linkplain BigInt#val() value-encoded
   * <code>int[]</code>} into a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param mag The two's-complement binary representation of a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return The result of assigning a byte array containing the two's-complement binary representation of a {@linkplain BigInt#val()
   *         value-encoded <code>int[]</code>} into a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] assign(final int[] val, final byte[] mag, final boolean littleEndian) {
    return assign(val, mag, 0, mag.length, littleEndian);
  }

  /**
   * Assigns a byte array containing the two's-complement binary representation of a {@linkplain BigInt#val() value-encoded
   * <code>int[]</code>} into a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   *
   * <pre>
   * val = mag
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param mag The two's-complement binary representation of a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @param off The start offset of the binary representation.
   * @param len The number of bytes to use.
   * @param littleEndian Whether the specified byte array is encoded in <i>little-endian</i> ({@code true}), or <i>big-endian</i>
   *          ({@code false}).
   * @return The result of assigning a byte array containing the two's-complement binary representation of a {@linkplain BigInt#val()
   *         value-encoded <code>int[]</code>} into a {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] assign(final int[] val, final byte[] mag, final int off, final int len, final boolean littleEndian) {
    return littleEndian ? assignLittleEndian(val, mag, off, len) : assignBigEndian(val, mag, off, len);
  }

  private static int[] assignLittleEndian(final int[] val, final byte[] mag, final int off, final int len) {
    return mag[off + len - 1] < 0 ? assignNegativeLittleEndian(val, mag, off, len) : assignPositiveLittleEndian(val, mag, off, len);
  }

  private static int[] assignBigEndian(final int[] val, final byte[] mag, final int off, final int len) {
    return mag[off] < 0 ? assignNegativeBigEndian(val, mag, off, len) : assignPositiveBigEndian(val, mag, off, len);
  }

  /**
   * Takes a little-endian array a representing a negative 2's-complement number and returns the minimal (no leading zero bytes)
   * unsigned whose value is -a.
   */
  private static int[] assignNegativeLittleEndian(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off;
    int keep, k;

    // Find first non-sign (0xFF) byte of input
    for (keep = off + len - 1; keep >= indexBound && mag[keep] == -1; --keep);

    /*
     * Allocate output array. If all non-sign bytes are 0x00, we must allocate space for one extra output byte.
     */
    for (k = keep; k >= indexBound && mag[k] == 0; --k);

    final int extraByte = k == indexBound - 1 ? 1 : 0;
    final int vlen = ((keep - (indexBound - 1) + extraByte) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    /*
     * Copy one's complement of input into output, leaving extra byte (if it exists) == 0x00
     */
    for (int i = 1, j, b = indexBound, numBytesToTransfer, lim, mask; i <= vlen; ++i) { // [A]
      numBytesToTransfer = Math.max(0, Math.min(3, keep - b));
      val[i] = mag[b++] & 0xFF;
      for (j = 8, lim = 8 * numBytesToTransfer; j <= lim; j += 8) // [A]
        val[i] |= (mag[b++] & 0xFF) << j;

      // Mask indicates which bits must be complemented
      mask = -1 >>> (8 * (3 - numBytesToTransfer));
      val[i] = ~val[i] & mask;
    }

    // Add one to one's complement to generate two's complement
    for (int i = 1; i <= vlen; ++i) { // [A]
      val[i] = (int)((val[i] & LONG_MASK) + 1);
      if (val[i] != 0)
        break;
    }

    val[0] = -vlen;
    // _debugLenSig(val);;
    return val;
  }

  /**
   * Returns a copy of the little-endian input array stripped of any leading zero bytes.
   */
  private static int[] assignPositiveLittleEndian(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off;
    int keep;

    // Find first nonzero byte
    for (keep = off + len - 1; keep >= indexBound && mag[keep] == 0; --keep);

    // Allocate new array and copy relevant part of input array
    final int vlen = ((keep - (indexBound - 1)) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    if (vlen == 0)
      return setToZeroInPlace(val);

    for (int i = 1, j, b = indexBound, bytesRemaining, bytesToTransfer; i <= vlen; ++i) { // [A]
      bytesRemaining = keep - b;
      bytesToTransfer = Math.min(3, bytesRemaining);
      val[i] = mag[b++] & 0xff;
      for (j = 8; j <= bytesToTransfer << 3; j += 8) // [A]
        val[i] |= (mag[b++] & 0xff) << j;
    }

    val[0] = vlen;
    // _debugLenSig(val);;
    return val;
  }

  /**
   * Takes a big-endian array a representing a negative 2's-complement number and returns the minimal (no leading zero bytes) unsigned
   * whose value is -a.
   */
  private static int[] assignNegativeBigEndian(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off + len;
    int keep, k;

    // Find first non-sign (0xFF) byte of input
    for (keep = off; keep < indexBound && mag[keep] == -1; ++keep);

    /*
     * Allocate output array. If all non-sign bytes are 0x00, we must allocate space for one extra output byte.
     */
    for (k = keep; k < indexBound && mag[k] == 0; ++k);

    final int extraByte = k == indexBound ? 1 : 0;
    final int vlen = ((indexBound - keep + extraByte) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    /*
     * Copy one's complement of input into output, leaving extra byte (if it exists) == 0x00
     */
    for (int i = 1, j, b = indexBound - 1, numBytesToTransfer, lim, mask; i <= vlen; ++i) { // [A]
      numBytesToTransfer = Math.max(0, Math.min(3, b - keep));
      val[i] = mag[b--] & 0xFF;
      for (j = 8, lim = 8 * numBytesToTransfer; j <= lim; j += 8) // [A]
        val[i] |= (mag[b--] & 0xFF) << j;

      // Mask indicates which bits must be complemented
      mask = -1 >>> (8 * (3 - numBytesToTransfer));
      val[i] = ~val[i] & mask;
    }

    // Add one to one's complement to generate two's complement
    for (int i = 1; i <= vlen; ++i) { // [A]
      val[i] = (int)((val[i] & LONG_MASK) + 1);
      if (val[i] != 0)
        break;
    }

    val[0] = -vlen;
    // _debugLenSig(val);;
    return val;
  }

  /**
   * Returns a copy of the big-endian input array stripped of any leading zero bytes.
   */
  private static int[] assignPositiveBigEndian(int[] val, final byte[] mag, final int off, final int len) {
    final int indexBound = off + len;
    int keep;

    // Find first nonzero byte
    for (keep = off; keep < indexBound && mag[keep] == 0; ++keep);

    // Allocate new array and copy relevant part of input array
    final int vlen = ((indexBound - keep) + 3) >>> 2;
    if (val.length <= vlen)
      val = alloc(vlen + 1);

    if (vlen == 0)
      return setToZeroInPlace(val);

    for (int i = 1, j, b = indexBound - 1, bytesRemaining, bytesToTransfer; i <= vlen; ++i) { // [A]
      bytesRemaining = b - keep;
      bytesToTransfer = Math.min(3, bytesRemaining);
      val[i] = mag[b--] & 0xff;
      for (j = 8; j <= bytesToTransfer << 3; j += 8) // [A]
        val[i] |= (mag[b--] & 0xff) << j;
    }

    val[0] = vlen;
    // _debugLenSig(val);;
    return val;
  }

  /**
   * Assigns the specified number as a {@code char[]} to the provided {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   *
   * <pre>
   * val = s
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param s The number as a {@code char[]}.
   * @return The result of assigning the specified number as a {@code char[]} to the provided {@linkplain BigInt#val() value-encoded
   *         <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] assign(int[] val, final char[] s) {
    final int sig = s[0] == '-' ? -1 : 1;

    final int length = s.length;
    final int from = sig - 1 >> 1;
    final int len = length + from;
    // 3402 = bits per digit * 1024
    final int alloc = (len < 10 ? 1 : (int)(len * 3402L >>> 10) + 32 >>> 5) + 1;
    if (alloc > val.length)
      val = alloc(alloc);

    int j = len % 9;
    if (j == 0)
      j = 9;

    j -= from;

    if ((val[1] = parse(s, -from, j)) != 0) {
      int toIndex = 2;
      while (j < length)
        toIndex = mulAdd(val, 1, toIndex, 1_000_000_000, parse(s, j, j += 9));

      --toIndex;
      val[0] = sig < 0 ? -toIndex : toIndex;
    }
    else {
      val[0] = 0;
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Assigns the specified number as a string to the provided {@linkplain BigInt#val() value-encoded <code>int[]</code>}.
   *
   * <pre>
   * val = s
   * </pre>
   *
   * @implNote The returned number may be a {@code new int[]} instance if the assignment requires a larger array.
   * @param val The target array of the assignment.
   * @param s The number as a string.
   * @return The result of assigning the specified number as a string to the provided {@linkplain BigInt#val() value-encoded
   *         <code>int[]</code>}.
   * @complexity O(n^2)
   */
  public static int[] assign(int[] val, final String s) {
    final int sig = s.charAt(0) == '-' ? -1 : 1;

    final int length = s.length();
    final int from = sig - 1 >> 1;
    final int len = length + from;
    // 3402 = bits per digit * 1024
    final int alloc = (len < 10 ? 1 : (int)(len * 3402L >>> 10) + 32 >>> 5) + 1;
    if (alloc > val.length)
      val = alloc(alloc);

    int j = len % 9;
    if (j == 0)
      j = 9;

    j -= from;

    if ((val[1] = parse(s, -from, j)) != 0) {
      int toIndex = 2;
      while (j < length)
        toIndex = mulAdd(val, 1, toIndex, 1_000_000_000, parse(s, j, j += 9));

      --toIndex;
      val[0] = sig < 0 ? -toIndex : toIndex;
    }
    else {
      val[0] = 0;
    }

    // _debugLenSig(val);
    return val;
  }

  /**
   * Parses a part of a char array as an unsigned number radix 10.
   *
   * @param s A char array representing the number in decimal.
   * @param fromIndex The index (inclusive) where we start parsing.
   * @param toIndex The index (exclusive) where we stop parsing.
   * @return The parsed {@code int}.
   * @complexity O(n)
   */
  private static int parse(final char[] s, int fromIndex, final int toIndex) {
    int v = s[fromIndex] - '0';
    while (++fromIndex < toIndex)
      v = v * 10 + s[fromIndex] - '0';

    return v;
  }

  /**
   * Parses a part of a char array as an unsigned number radix 10.
   *
   * @param s A char array representing the number in decimal.
   * @param fromIndex The index (inclusive) where we start parsing.
   * @param toIndex The index (exclusive) where we stop parsing.
   * @return The parsed {@code int}.
   * @complexity O(n)
   */
  private static int parse(final CharSequence s, int fromIndex, final int toIndex) {
    int v = s.charAt(fromIndex) - '0';
    while (++fromIndex < toIndex)
      v = v * 10 + s.charAt(fromIndex) - '0';

    return v;
  }

  /**
   * Multiplies the provided {@linkplain BigInt#val() value-encoded number} with {@code mul}, adds {@code add}, and returns the carry.
   *
   * <pre>
   * val = val * mul + add
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param fromIndex The starting index in the val array.
   * @param toIndex The ending index in the val array.
   * @param mul The value we multiply our number with, mul < 2^31.
   * @param add The value we add to our number, add < 2^31.
   * @return The carry.
   * @complexity O(n)
   */
  private static int mulAdd(final int[] val, final int fromIndex, int toIndex, final int mul, final int add) {
    long carry = 0;
    int i = fromIndex;
    for (; i < toIndex; ++i) { // [A]
      carry += mul * (val[i] & LONG_MASK);
      val[i] = (int)carry;
      carry >>>= 32;
    }

    if (carry != 0)
      val[toIndex++] = (int)carry;

    carry = (val[fromIndex] & LONG_MASK) + add;
    val[fromIndex] = (int)carry;
    if ((carry >>> 32) != 0) {
      i = fromIndex + 1;
      for (; i < toIndex && ++val[i] == 0; ++i);
      if (i == toIndex)
        val[toIndex++] = 1;
    }

    return toIndex;
  }

  /**
   * Returns the signum of the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return -1, 0 or 1 as the value of the provided {@linkplain BigInt#val() value-encoded number} is negative, zero or positive.
   * @complexity O(1)
   */
  public static int signum(final int[] val) {
    final int v = val[0];
    return v < 0 ? -1 : v > 0 ? 1 : 0;
  }

  /**
   * Tests if the provided {@linkplain BigInt#val() value-encoded number} is zero.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return {@code true} if the provided {@linkplain BigInt#val() value-encoded number} is zero, otherwise {@code false}.
   * @complexity O(1)
   */
  public static boolean isZero(final int[] val) {
    return val[0] == 0;
  }

  /**
   * Sets the provided {@linkplain BigInt#val() value-encoded number} to zero.
   *
   * @implNote The returned array will be a {@code new int[]} instance if the length of the provided array is zero.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The provided {@linkplain BigInt#val() value-encoded number} set to zero.
   * @complexity O(1)
   */
  public static int[] setToZero(final int[] val) {
    return setToZeroInPlace(val.length > 0 ? val : alloc(1));
  }

  protected static int[] setToZeroInPlace(final int[] val) {
    val[0] = 0;
    return val;
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code byte}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code byte}.
   * @complexity O(1)
   */
  public static byte byteValue(final int[] val) {
    return byteValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code byte}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided magnitude array as a {@code byte}.
   * @complexity O(1)
   */
  public static byte byteValue(final int[] mag, final int off, final int sig) {
    return (byte)(sig < 0 ? ~mag[off] + 1 : mag[off]);
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code short}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code short}.
   * @complexity O(1)
   */
  public static short shortValue(final int[] val) {
    return shortValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code short}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided magnitude array as a {@code short}.
   * @complexity O(1)
   */
  public static short shortValue(final int[] mag, final int off, final int sig) {
    return (short)(sig < 0 ? ~mag[off] + 1 : mag[off]);
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code int}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code long}.
   * @complexity O(1)
   */
  public static int intValue(final int[] val) {
    return intValue(val, 1, val[0] < 0 ? -1 : 1);
  }

  /**
   * Returns the value of the the provided magnitude array as an {@code int}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided magnitude array as an {@code int}.
   * @complexity O(1)
   */
  public static int intValue(final int[] mag, final int off, final int sig) {
    return sig < 0 ? ~mag[off] + 1 : mag[off];
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code long}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code long}.
   * @complexity O(1)
   */
  public static long longValue(final int[] val) {
    int sig = 1;
    int len = val[0];
    if (len < 0) { len = -len; sig = -1; }
    return longValue(val, 1, len, sig);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code long}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param len The number of magnitude elements to use.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided magnitude array as a {@code long}.
   * @complexity O(1)
   */
  public static long longValue(final int[] mag, final int off, final int len, final int sig) {
    if (len == 0)
      return 0;

    final long longValue = longValue0(mag, off, len);
    return sig < 0 ? ~longValue + 1 : longValue;
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as an <i>unsigned</i> {@code long}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as an <i>unsigned</i> {@code long}.
   * @complexity O(1)
   */
  public static long longValueUnsigned(final int[] val) {
    final int len = val[0];
    return longValue(val, 1, len < 0 ? -len : len);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code long}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param len The number of magnitude elements to use.
   * @return The value of the the provided magnitude array as a {@code long}.
   * @complexity O(1)
   */
  public static long longValue(final int[] mag, final int off, final int len) {
    return len == 0 ? 0 : longValue0(mag, off, len);
  }

  private static long longValue0(final int[] mag, final int off, final int len) {
    final long low0 = mag[off] & LONG_MASK;
    return len > 1 ? (long)mag[off + 1] << 32 | low0 : low0;
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code float}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code float}.
   * @complexity O(1)
   */
  public static float floatValue(final int[] val) {
    int sig = 1, len = val[0];
    if (len < 0) { len = -len; sig = -1; }
    return floatValue(val, 1, len, sig);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code float}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param len The number of magnitude elements to use.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code float}.
   * @complexity O(1)
   */
  public static float floatValue(final int[] mag, final int off, final int len, final int sig) {
    if (len == 0)
      return 0;

    final int end = len + off - 1;
    final int s = Integer.numberOfLeadingZeros(mag[end]);
    if (len == 1 && s >= 8)
      return sig < 0 ? -mag[off] : mag[off];

    final int exponent = ((end - 1) << 5) + bitLengthForInt(mag[end]) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(mag, off, len, sig);

    if (exponent > Float.MAX_EXPONENT)
      return sig > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;

    /*
     * We need the top SIGNIFICAND_WIDTH bits, including the "implicit" one bit. To make rounding easier, we pick out the top
     * SIGNIFICAND_WIDTH + 1 bits, so we have one to help us round up or down. twiceSignifFloor will contain the top SIGNIFICAND_WIDTH +
     * 1 bits, and signifFloor the top SIGNIFICAND_WIDTH. It helps to consider the real number signif = abs(this) * 2^(SIGNIFICAND_WIDTH
     * - 1 - exponent).
     */
    final int shift = exponent - FloatingDecimal.SIGNIFICAND_WIDTH_FLOAT;

    int twiceSignifFloor;
    // twiceSignifFloor will be == abs().shiftRight(shift).intValue()
    // We do the shift into an int directly to improve performance.

    final int nBits = shift & 0x1f;
    if (nBits == 0) {
      twiceSignifFloor = mag[end];
    }
    else {
      twiceSignifFloor = mag[end] >>> nBits;
      if (twiceSignifFloor == 0)
        twiceSignifFloor = (mag[end] << (32 - nBits)) | (mag[end - 1] >>> nBits);
    }

    final int signifFloor = twiceSignifFloor >> 1 & FloatingDecimal.SIGNIF_BIT_MASK_FLOAT; // remove the implied bit

    /*
     * We round up if either the fractional part of signif is strictly greater than 0.5 (which is true if the 0.5 bit is set and any
     * lower bit is set), or if the fractional part of signif is >= 0.5 and signifFloor is odd (which is true if both the 0.5 bit and
     * the 1 bit are set). This is equivalent to the desired HALF_EVEN rounding.
     */
    final boolean increment = (twiceSignifFloor & 1) != 0 && ((signifFloor & 1) != 0 || getLowestSetBit(mag) < shift);
    final int signifRounded = increment ? signifFloor + 1 : signifFloor;
    final int bits = ((exponent + FloatingDecimal.EXP_BIAS_FLOAT) << (FloatingDecimal.SIGNIFICAND_WIDTH_FLOAT - 1)) + signifRounded;

    /*
     * If signifRounded == 2^24, we'd need to set all of the significand bits to zero and add 1 to the exponent. This is exactly the
     * behavior we get from just adding signifRounded to bits directly. If the exponent is Float.MAX_EXPONENT, we round up (correctly)
     * to Float.POSITIVE_INFINITY.
     */
    return Float.intBitsToFloat(bits | sig & FloatingDecimal.SIGN_BIT_MASK_FLOAT);
  }

  /**
   * Returns the value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code double}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code double}.
   * @complexity O(1)
   */
  public static double doubleValue(final int[] val) {
    int sig = 1, len = val[0];
    if (len < 0) { len = -len; sig = -1; }
    return doubleValue(val, 1, len, sig);
  }

  /**
   * Returns the value of the the provided magnitude array as a {@code double}.
   *
   * @param mag The magnitude array.
   * @param off The start index in the magnitude array.
   * @param len The number of magnitude elements to use.
   * @param sig The sign of the magnitude.
   * @return The value of the the provided {@linkplain BigInt#val() value-encoded number} as a {@code double}.
   * @complexity O(1)
   */
  public static double doubleValue(final int[] mag, final int off, final int len, final int sig) {
    if (len == 0)
      return 0;

    if (len == 1) {
      final double v = mag[off] & LONG_MASK;
      return sig < 0 ? -v : v;
    }

    final int end = off + len - 1;
    int exponent = ((end - 1) << 5) + bitLengthForInt(mag[end]) - 1;
    if (exponent < Long.SIZE - 1)
      return longValue(mag, off, len, sig < 0 ? -1 : 1);

    if (exponent > Double.MAX_EXPONENT)
      return sig > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

    /*
     * We need the top SIGNIFICAND_WIDTH bits, including the "implicit" one bit. To make rounding easier, we pick out the top
     * SIGNIFICAND_WIDTH + 1 bits, so we have one to help us round up or down. twiceSignifFloor will contain the top SIGNIFICAND_WIDTH +
     * 1 bits, and signifFloor the top SIGNIFICAND_WIDTH. It helps to consider the real number signif = abs(this) * 2^(SIGNIFICAND_WIDTH
     * - 1 - exponent).
     */
    final int shift = exponent - FloatingDecimal.SIGNIFICAND_WIDTH_DOUBLE;

    // twiceSignifFloor will be == abs().shiftRight(shift).longValue()
    // We do the shift into a long directly to improve performance.

    final int nBits = shift & 0x1f;

    int highBits;
    int lowBits;
    if (nBits == 0) {
      highBits = mag[end];
      lowBits = mag[end - 1];
    }
    else {
      final int nBits2 = 32 - nBits;
      highBits = mag[end] >>> nBits;
      lowBits = (mag[end] << nBits2) | (mag[end - 1] >>> nBits);
      if (highBits == 0) {
        highBits = lowBits;
        lowBits = (mag[end - 1] << nBits2) | (mag[end - 2] >>> nBits);
      }
    }

    final long twiceSignifFloor = ((highBits & LONG_MASK) << 32) | (lowBits & LONG_MASK);
    final long signifFloor = twiceSignifFloor >> 1 & FloatingDecimal.SIGNIF_BIT_MASK_DOUBLE; // remove the implied bit

    /*
     * We round up if either the fractional part of signif is strictly greater than 0.5 (which is true if the 0.5 bit is set and any
     * lower bit is set), or if the fractional part of signif is >= 0.5 and signifFloor is odd (which is true if both the 0.5 bit and
     * the 1 bit are set). This is equivalent to the desired HALF_EVEN rounding.
     */
    final boolean increment = (twiceSignifFloor & 1) != 0 && ((signifFloor & 1) != 0 || getLowestSetBit(mag) < shift);
    final long signifRounded = increment ? signifFloor + 1 : signifFloor;
    final long bits = ((long)(exponent + FloatingDecimal.EXP_BIAS_DOUBLE) << (FloatingDecimal.SIGNIFICAND_WIDTH_DOUBLE - 1)) + signifRounded;

    /*
     * If signifRounded == 2^53, we'd need to set all of the significand bits to zero and add 1 to the exponent. This is exactly the
     * behavior we get from just adding signifRounded to bits directly. If the exponent is Double.MAX_EXPONENT, we round up (correctly)
     * to Double.POSITIVE_INFINITY.
     */
    return Double.longBitsToDouble(bits | sig & FloatingDecimal.SIGN_BIT_MASK_DOUBLE);
  }

  /**
   * Compares the absolute values of the provided {@linkplain BigInt#val() value-encoded numbers}, and returns one of {@code -1},
   * {@code 0}, or {@code 1} whether the first number's absolute value is less than, equal to, or greater than that of the second
   * argument, respectively.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the first number's absolute value is less than, equal to, or greater than
   *         that of the second argument, respectively.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int compareToAbs(final int[] val1, final int[] val2) {
    return compareToAbs(val1, Math.abs(val1[0]), val2, Math.abs(val2[0]));
  }

  /**
   * Compares the absolute values of the provided {@linkplain BigInt#val() value-encoded numbers}, and returns one of {@code -1},
   * {@code 0}, or {@code 1} whether the first number's absolute value is less than, equal to, or greater than that of the second
   * argument, respectively.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param len1 The length of the first number.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @param len2 The length of the second number.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the first number's absolute value is less than, equal to, or greater than
   *         that of the second argument, respectively.
   * @complexity O(n)
   * @amortized O(1)
   */
  static int compareToAbs(final int[] val1, int len1, final int[] val2, final int len2) {
    if (len1 > len2)
      return 1;

    if (len1 < len2)
      return -1;

    for (long v1, v2; len1 >= 1; --len1) { // [A]
      v1 = val1[len1] & LONG_MASK;
      v2 = val2[len1] & LONG_MASK;
      if (v1 > v2)
        return 1;

      if (v1 < v2)
        return -1;
    }

    return 0;
  }

  /**
   * Compares the values of the provided {@linkplain BigInt#val() value-encoded numbers}, and returns one of {@code -1}, {@code 0}, or
   * {@code 1} whether the first number's value is less than, equal to, or greater than that of the second argument, respectively.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @return One of {@code -1}, {@code 0}, or {@code 1} if the first number's value is less than, equal to, or greater than that of
   *         the second argument, respectively.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int compareTo(final int[] val1, final int[] val2) {
    int sig1 = 1, len1 = val1[0];
    if (len1 < 0) { len1 = -len1; sig1 = -1; }

    int sig2 = 1, len2 = val2[0];
    if (len2 < 0) { len2 = -len2; sig2 = -1; }

    if (sig1 < 0)
      return sig2 < 0 ? compareToAbs(val2, len2, val1, len1) : -1;

    if (sig2 < 0)
      return 1;

    if (len2 == 0)
      return len1 == 0 ? 0 : 1;

    return len1 == 0 ? -1 : compareToAbs(val1, len1, val2, len2);
  }

  /**
   * Sets the provided {@linkplain BigInt#val() value-encoded number} to its absolute value.
   *
   * <pre>
   * val = | val |
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The provided {@linkplain BigInt#val() value-encoded number} set to its absolute value.
   * @complexity O(1)
   */
  public static int[] abs(final int[] val) {
    if (val[0] < 0)
      val[0] = -val[0];

    // _debugLenSig(val);
    return val;
  }

  /**
   * Sets the provided {@linkplain BigInt#val() value-encoded number} to its negated value.
   *
   * <pre>
   * val = -val
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The provided {@linkplain BigInt#val() value-encoded number} set to its negated value.
   * @complexity O(1)
   */
  public static int[] neg(final int[] val) {
    val[0] = -val[0];
    // _debugLenSig(val);
    return val;
  }

  /**
   * Returns the maximum of the provided {@linkplain BigInt#val() value-encoded numbers}.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @return The {@linkplain BigInt#val() value-encoded number} whose value is the greater of the provided {@linkplain BigInt#val()
   *         value-encoded numbers}.
   * @complexity O(n)
   */
  public static int[] max(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) > 0 ? val1 : val2;
  }

  /**
   * Returns the minimum of the provided {@linkplain BigInt#val() value-encoded numbers}.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @return The {@linkplain BigInt#val() value-encoded number} whose value is the lesser of the provided {@linkplain BigInt#val()
   *         value-encoded numbers}.
   * @complexity O(n)
   */
  public static int[] min(final int[] val1, final int[] val2) {
    return compareTo(val1, val2) < 0 ? val1 : val2;
  }

  /**
   * Tests the provided {@linkplain BigInt#val() value-encoded numbers} for equality.
   *
   * @param val1 The first {@linkplain BigInt#val() value-encoded number}.
   * @param val2 The second {@linkplain BigInt#val() value-encoded number}.
   * @return {@code true} if the two provided numbers are equal, otherwise {@code false}.
   * @complexity O(n)
   */
  public static boolean equals(final int[] val1, final int[] val2) {
    int len1 = val1[0];

    if (len1 != val2[0])
      return false;

    if (len1 == 0)
      return true;

    if (len1 < 0)
      len1 = -len1;

    for (; len1 >= 1; --len1) // [A]
      if (val1[len1] != val2[len1])
        return false;

    return true;
  }

  /**
   * Computes the hash code of the provided {@linkplain BigInt#val() value-encoded number}.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The hash code of the provided {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   */
  public static int hashCode(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 0;

    boolean sig = true;
    if (len < 0) { len = -len; sig = false; }

    int hashCode = 0;
    for (; len >= 1; --len) // [A]
      hashCode = (int)(31 * hashCode + (val[len] & LONG_MASK));

    return sig ? hashCode : -hashCode;
  }

  private static final int pow5 = 1_220_703_125;
  private static final int pow2 = 1 << 13;

  /**
   * Divides the provided {@linkplain BigInt#val() value-encoded number} by {@code 10^13} and returns the remainder. Does not change
   * the sign of the number.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The count of limbs to divide.
   * @return The remainder of the division of the provided value-encode number by {@code 10^13}.
   * @complexity O(n)
   */
  private static long toStringDiv(final int[] val, int len) {
    int q1 = 0;
    long r = 0;
    for (int q0; len > 0; --len) { // [A]
      r = (r << 32) + (val[len] & LONG_MASK);
      q0 = (int)(r / pow5);
      r %= pow5;
      val[len] = q1 | q0 >>> 13;
      q1 = q0 << 32 - 13;
    }

    r = (r << 32) + (val[0] & LONG_MASK);
    final int mod2 = val[0] & pow2 - 1;
    val[0] = q1 | (int)(r / pow5 >>> 13);
    r %= pow5;

    // Applies the Chinese Remainder Theorem. -67*5^13 + 9983778*2^13 = 1
    final long pow10 = (long)pow5 * pow2;
    r = (r - pow5 * (mod2 - r) % pow10 * 67) % pow10;
    if (r < 0)
      r += pow10;

    return r;
  }

  /**
   * Converts the provided {@linkplain BigInt#val() value-encoded number} into a string of radix 10.
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The string representation of the provided {@linkplain BigInt#val() value-encoded number} in radix 10.
   * @complexity O(n^2)
   */
  public static String toString(final int[] val) {
    if (val == null)
      return "null";

    if (isZero(val))
      return "0";

    int sig = 1, len = val[0];
    if (len < 0) { len = -len; sig = -1; }

    int j, top = len * 10 + 3;
    final char[] chars = new char[top];
    Arrays.fill(chars, '0');
    final int[] mag = new int[len];
    System.arraycopy(val, 1, mag, 0, len);
    long tmp;
    while (true) {
      j = top;
      tmp = toStringDiv(mag, len - 1);
      if (mag[len - 1] == 0 && len > 1 && mag[--len - 1] == 0 && len > 1)
        --len;

      for (; tmp > 0; tmp /= 10) // [A]
        chars[--top] += tmp % 10; // TODO: Optimize

      if (len == 1 && mag[0] == 0)
        break;

      top = j - 13;
    }

    if (sig < 0)
      chars[--top] = '-';

    return new String(chars, top, chars.length - top);
  }

  /**
   * Returns the number of bits in the minimal two's-complement representation of the provided {@linkplain BigInt#val() value-encoded
   * number}, <em>excluding</em> a sign bit. For positive numbers, this is equivalent to the number of bits in the ordinary binary
   * representation. For zero this method returns {@code 0}.
   *
   * <pre>
   * ceil(log2(val < 0 ? -val : val + 1))
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return Number of bits in the minimal two's-complement representation of the provided {@linkplain BigInt#val() value-encoded
   *         number}, <em>excluding</em> a sign bit.
   * @complexity O(n)
   */
  static long bitLength(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 0;

    if (len < 0) { len = -len; }
    return len * 32L - Integer.numberOfLeadingZeros(val[len]);
  }

  /**
   * Returns the number of bits in the minimal two's-complement representation of the provided {@linkplain BigInt#val() value-encoded
   * number}, <em>excluding</em> a sign bit. For positive numbers, this is equivalent to the number of bits in the ordinary binary
   * representation. For zero this method returns {@code 0}.
   *
   * <pre>
   * ceil(log2(val < 0 ? -val : val + 1))
   * </pre>
   *
   * @implNote This function returns a {@code long} result because the maximum numbers of bits representable with a
   *           {@linkplain BigInt#val() value-encoded number} is <code>32 * {@link #MAX_VAL_LENGTH} = 2147483648</code>.
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @param len The number of significant limbs in the number <b>(assumed to be positive)</b>.
   * @return Number of bits in the minimal two's-complement representation of the provided {@linkplain BigInt#val() value-encoded
   *         number}, <em>excluding</em> a sign bit.
   * @complexity O(1)
   */
  static long bitLength(final int[] val, final int len) {
    return len == 0 ? 0 : len * 32L - Integer.numberOfLeadingZeros(val[len]);
  }

  /**
   * Returns the index of the rightmost (lowest-order) one bit in the provided {@linkplain BigInt#val() value-encoded number} (the
   * number of zero bits to the right of the rightmost one bit). Returns {@code -1} if number contains no one bits.
   *
   * <pre>
   * val == 0 ? -1 : log2(val &amp; -val)
   * </pre>
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The index of the rightmost (lowest-order) one bit in the provided {@linkplain BigInt#val() value-encoded number}.
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int getLowestSetBit(final int[] val) {
    if (val[0] == 0)
      return -1;

    // Search for lowest order nonzero int
    int i, b;
    for (i = 1; (b = val[i]) == 0; ++i);
    return ((i - 1) << 5) + Integer.numberOfTrailingZeros(b);
  }

  /**
   * Returns the bit length of the provided integer.
   *
   * @param n The integer whose bit length to return.
   * @return The bit length of the provided integer.
   * @complexity O(1)
   */
  static int bitLengthForInt(final int n) {
    return Integer.SIZE - Integer.numberOfLeadingZeros(n);
  }

  /**
   * Returns the number of digits in the provided {@linkplain BigInt#val() value-encoded number} (radix 10).
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The number of digits in the provided {@linkplain BigInt#val() value-encoded number} (radix 10).
   * @complexity O(n)
   * @amortized O(1)
   */
  public static int precision(final int[] val) {
    int len = val[0];
    if (len == 0)
      return 1;

    if (len < 0) { len = -len; }
    if (len == 1)
      return Numbers.precision(BigInt.longValue(val));

    final int p = (int)(((1L + len * 32L - Integer.numberOfLeadingZeros(val[len])) * 646456993L) >>> 31);
    return compareToAbs(val, FastMath.E10(p)) < 0 ? p : p + 1;
  }

  /**
   * Returns the normalization bias of the provided {@linkplain BigInt#val() value-encoded number}. The normalization bias is a left
   * shift such that after it the highest word of the value will have the 4 highest bits equal to zero:
   * {@code (highestWord & 0xf0000000) == 0}, but the next bit should be 1 ({@code (highestWord & 0x08000000) != 0}).
   * <p>
   * This assumes: {@code val != 0}
   *
   * @param val The {@linkplain BigInt#val() value-encoded number}.
   * @return The normalization bias of the provided {@linkplain BigInt#val() value-encoded number}.
   */
  static int getNormalizationBias(final int[] val) {
    final int zeros = Integer.numberOfLeadingZeros(val[Math.abs(val[0])]);
    return zeros < 4 ? 28 + zeros : zeros - 4;
  }

  static void _debugLenSig(final int[] val) {
    final int len;
    if (!isZero(val) && ((len = Math.abs(val[0])) > val.length || val[len] == 0))
      throw new IllegalStateException(Arrays.toString(val));
  }
}