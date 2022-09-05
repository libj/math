/* Copyright (c) 2020 Seva Safris, LibJ
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

public class DecimalNative {
  static {
    NativeMath.loadNative();
  }

  static native double nativeDoubleValue(long significand, short scale);
  static native double nativeLog(long significand, short scale);
  static native double nativeLogBase(long significand, short scale, double base);

  static native long nativeSin(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeCos(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeTan(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeAsin(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeAcos(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeAtan(long significand, short scale, int rm, long defaultValue); // FIXME: Very limited range
  static native long nativeAtan2(long significand1, short scale1, long significand2, short scale2, int rm, long defaultValue); // FIXME: Very limited range

  // FIXME: Work in progress
  static native String nativeD2A(double v);
}