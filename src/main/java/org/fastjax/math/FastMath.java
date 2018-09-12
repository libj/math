package org.fastjax.math;

public final class FastMath {
  /**
   * Returns the value of the first argument raised to the power of the
   * second argument.
   * <b>
   * The complexity of this implementation is O(log(b))
   * @param a
   * @param b
   * @return
   */
  public static long pow(long a, long b) {
    long p = 1;
    while (b > 0) {
      if ((b & 1) == 1)
        p *= a;

      b >>= 1;
      a *= a;
    }

    return p;
  }

  private FastMath() {
  }
}