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

#include "decimal.h"

#define SCALE_BITS 9
#define MIN_VALUE (-(2ull << 62ull - SCALE_BITS))
#define MAX_VALUE (-MIN_VALUE - 1ull)
#define MIN_SCALE ((short)(-(2 << SCALE_BITS - 2)))
#define MAX_SCALE ((short)(-MIN_SCALE - 1))

#define VALUE_BITS ((char)(63 - SCALE_BITS))
#define SCALE_MASK ((0xffffull << VALUE_BITS) & 0x7fffffffffffffffull)

long double toLongDouble(long long signif, short scale) {
  long double value = (long double)signif;
  if (scale < 0)
    value *= powl(10.0L, -scale);
  else if (scale > 0)
    value /= powl(10.0L, scale);

  return value;
}

long long round10(const char r, const long long v, const enum RoundingMode rm) {
  switch (rm) {
    case UP:
      return r < 0 ? v - 1 : r > 0 ? v + 1 : v;

    case DOWN:
      return v;

    case CEILING:
      return r > 0 ? v + 1 : v;

    case FLOOR:
      return r < 0 ? v - 1 : v;

    case HALF_UP:
      return r <= -5 ? v - 1 : r >= 5 ? v + 1 : v;

    case HALF_DOWN:
      return r < -5 ? v - 1 : r > 5 ? v + 1 : v;

    case HALF_EVEN:
      if (r == -5)
        return v % 2 == 0 ? v : v - 1;

      if (r == 5)
        return v % 2 == 0 ? v : v + 1;

      return r < -5 ? v - 1 : r > 5 ? v + 1 : v;

    default:
      return r == 0 ? v : 0;
  }
}

unsigned char precision(long long n) {
  n = labs(n);
  if (n < 1000000000L) {
    if (n < 10000L) {
      if (n < 100L) {
        if (n < 10L) {
          // Special case for Long.MIN_VALUE, because Math.abs() keeps it negative
          if (n < 0L)
            return 19;

          return 1;
        }

        return 2;
      }

      if (n < 1000L)
        return 3;

      return 4;
    }

    if (n < 10000000L) {
      if (n < 100000L)
        return 5;

      if (n < 1000000L)
        return 6;

      return 7;
    }

    if (n < 100000000L)
      return 8;

    return 9;
  }

  if (n < 1000000000000000L) {
    if (n < 1000000000000L) {
      if (n < 10000000000L)
        return 10;

      if (n < 100000000000L)
        return 11;

      return 12;
    }

    if (n < 10000000000000L)
      return 13;

    if (n < 100000000000000L)
      return 14;

    return 15;
  }

  if (n < 100000000000000000L) {
    if (n < 10000000000000000L)
      return 16;

    return 17;
  }

  if (n < 1000000000000000000L)
    return 18;

  return 19;
}

long long encodeInPlace(const long long value, const long long pscale) {
  const long long scaleMask = pscale << VALUE_BITS & SCALE_MASK;
  const long long dec = value < 0 ? value ^ scaleMask : value | scaleMask;
  // System.out.println("    scale: " + Buffers.toString(scale));
  // System.out.println("scaleMask: " + Buffers.toString(scaleMask));
  // System.out.println("    value: " + Buffers.toString(value));
  // System.out.println("  decimal: " + Buffers.toString(dec));
  return dec;
}

boolean getSignifScale(const long double x, const enum RoundingMode rm, long long *signif, short *scale) {
  if (isnan(x) || isinf(x))
    return false;

  if (x == 0) {
    *signif = 0;
    *scale = 0;
    return true;
  }

  *scale = (short)(floorl(log10l(fabsl(x))) - 17);
  *signif = x * powl(10.0L, -(*scale));
  long long y = *signif;

  char r;
  if (x < 0) {
    for (; *signif < MIN_VALUE; ++*scale) { // [A]
      *signif = round10(r = y % 10, y /= 10, rm);
    }
  }
  else {
    for (; *signif > MAX_VALUE; ++*scale) { // [A]
      *signif = round10(r = y % 10, y /= 10, rm);
    }
  }

  // printf(" Decimal: %lld %d\n\n", signif, scale);
  return true;
}

long long toDecimal(long double x, enum RoundingMode rm, long long defaultValue) {
  long long signif;
  short scale;
  return getSignifScale(x, rm, &signif, &scale) ? encodeInPlace(signif, -scale - precision(signif)) : defaultValue;
}

JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeDoubleValue(jlong v, jshort s) {
  const long double value = toLongDouble(v, s);
  return (jdouble)value;
}

JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeLogBase(jlong v, jshort s, jdouble b) {
  long double value = toLongDouble(v, s);
  value = logl(value) / b;
  return (jdouble)value;
}

JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeLog(jlong v, jshort s) {
  long double value = toLongDouble(v, s);
  value = logl(value);
  return (jdouble)value;
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeSin(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = sinl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeCos(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = cosl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeTan(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = tanl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAsin(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = asinl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAcos(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = acosl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAtan(jlong v, jshort s, jint rm, jlong defaultValue) {
  long double value = toLongDouble(v, s);
  value = atanl(value);
  return toDecimal(value, rm, defaultValue);
}

JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAtan2(jlong vY, jshort sY, jlong vX, jshort sX, jint rm, jlong defaultValue) {
  long double valueY = toLongDouble(vY, sY);
  long double valueX = toLongDouble(vX, sX);
  valueY = atan2l(valueY, valueX);
  return toDecimal(valueY, rm, defaultValue);
}

#ifndef CRITICAL_NATIVE

JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeDoubleValue(JNIEnv *env, jclass clazz, jlong v, jshort s) {
  return JavaCritical_org_libj_math_DecimalNative_nativeDoubleValue(v, s);
}

JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeLogBase(JNIEnv *env, jclass clazz, jlong v, jshort s, jdouble b) {
  return JavaCritical_org_libj_math_DecimalNative_nativeLogBase(v, s, b);
}

JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeLog(JNIEnv *env, jclass clazz, jlong v, jshort s) {
  return JavaCritical_org_libj_math_DecimalNative_nativeLog(v, s);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeSin(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeSin(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeCos(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeCos(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeTan(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeTan(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAsin(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeAsin(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAcos(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeAcos(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAtan(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeAtan(v, s, rm, defaultValue);
}

JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAtan2(JNIEnv *env, jclass clazz, jlong v1, jshort s1, jlong v2, jshort s2, jint rm, jlong defaultValue) {
  return JavaCritical_org_libj_math_DecimalNative_nativeAtan2(v1, s1, v2, s2, rm, defaultValue);
}






JNIEXPORT jstring JNICALL Java_org_libj_math_DecimalNative_nativeD2A(JNIEnv *env, jclass clazz, jdouble v) {
  char str[22];
  emyg_dtoa(v, str);
  return (*env)->NewStringUTF(env, str);
}

#endif