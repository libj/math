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

#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>
#include <jni.h>
#include "emyg_dtoa.h"

//#ifdef __INTEL_COMPILER
//typedef _Quad float128;
//typedef _Quad float128;
//#else
//#include <quadmath.h>
//typedef __float128 float128;
//typedef __float128 float128;
//#endif

#ifndef _Included_org_libj_math_decimal
#define _Included_org_libj_math_decimal
#ifdef __cplusplus
extern "C" {
#else
typedef unsigned char boolean;
#define false 0
#define true 1
#endif

#define LN2 = 0.6931471805599453094172321214581765680755001343602552541L;
#define LN10 = 2.302585092994045684017991454684364207601101488628772976L;

enum RoundingMode {UP = 0, DOWN = 1, CEILING = 2, FLOOR = 3, HALF_UP = 4, HALF_DOWN = 5, HALF_EVEN = 6, UNNECESSARY = 7};

long double toLongDouble(long long signif, short scale);
long long toDecimal(long double x, enum RoundingMode rm, long long defaultValue);

JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeDoubleValue(jlong v, jshort s);
JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeLog(jlong v, jshort s);
JNIEXPORT jdouble JNICALL JavaCritical_org_libj_math_DecimalNative_nativeLogBase(jlong v, jshort s, jdouble b);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeSin(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeCos(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeTan(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAsin(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAcos(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAtan(jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL JavaCritical_org_libj_math_DecimalNative_nativeAtan2(jlong vY, jshort sY, jlong vX, jshort sX, jint rm, jlong defaultValue);

#ifndef CRITICAL_NATIVE

JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeDoubleValue(JNIEnv *env, jclass clazz, jlong v, jshort s);
JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeLog(JNIEnv *env, jclass clazz, jlong v, jshort s);
JNIEXPORT jdouble JNICALL Java_org_libj_math_DecimalNative_nativeLogBase(JNIEnv *env, jclass clazz, jlong v, jshort s, jdouble b);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeSin(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeCos(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeTan(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAsin(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAcos(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAtan(JNIEnv *env, jclass clazz, jlong v, jshort s, jint rm, jlong defaultValue);
JNIEXPORT jlong JNICALL Java_org_libj_math_DecimalNative_nativeAtan2(JNIEnv *env, jclass clazz, jlong v1, jshort s1, jlong v2, jshort s2, jint rm, jlong defaultValue);






JNIEXPORT jstring JNICALL Java_org_libj_math_DecimalNative_nativeD2A(JNIEnv *env, jclass clazz, jdouble v);

#endif

#ifdef __cplusplus
}
#endif
#endif