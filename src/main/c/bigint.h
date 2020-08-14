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
#include <pthread.h>
#include <string.h>
#include <math.h>
#include <jni.h>

#ifndef _Included_org_libj_math_BigInt
#define _Included_org_libj_math_BigInt
#ifdef __cplusplus
extern "C" {
#endif

void karatsuba(jint *x, jint xoff, jint *y, jint yoff, jint *z, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ);

#ifdef CRITICAL_NATIVE

JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuad(jint _x, jint *x, jint xlen, jint _y, jint *y, jint ylen, jint _z, jint *z);
JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace(jint _x, jint *x, jint xlen, jint _y, jint *y, jint ylen, jint zlen);
JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeKaratsuba(jint _x, jint *x, jint xoff, jint _y, jint *y, jint yoff, jint _z, jint *z, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ);
JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(jint _x, jint *x, jint len, jint _z, jint *z, jint zlen, jint zlength, jboolean yCopy, jint parallelThreshold, jint parallelThresholdZ);
JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareQuad(jint _x, jint *x, jint xoff, jint xlen, jint _z, jint *z, jint zoff, jint zlen);

#else

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuad(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jintArray zarr);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jint zlen);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKaratsuba(JNIEnv *env, jobject obj, jintArray xarr, jint xoff, jintArray yarr, jint yoff, jintArray zarr, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(JNIEnv *env, jobject obj, jintArray xarr, jint len, jintArray zarr, jint zlen, jint zlength, jboolean yCopy, jint parallelThreshold, jint parallelThresholdZ);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareQuad(JNIEnv *env, jobject obj, jintArray xarr, jint xoff, jint xlen, jintArray zarr, jint zoff, jint zlen);

#endif

#ifdef __cplusplus
}
#endif
#endif