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