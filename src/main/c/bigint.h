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
#include <string.h>
#include <pthread.h>
#include <math.h>
#include <stdbool.h>
#include <jni.h>

#ifndef _Included_org_libj_math_BigInt
#define _Included_org_libj_math_BigInt
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuad(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jintArray z);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuadInline(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jint zlen);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKaratsuba(JNIEnv *env, jobject obj, jintArray xarr, jint xoff, jintArray yarr, jint yoff, jintArray zarr, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallel);
void karatsuba(jint *x, jint xoff, jint *y, jint yoff, jint *z, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallel);
void* karatsubaThread(void *args);

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(JNIEnv *env, jobject obj, jintArray x, jint len, jintArray z, jint zlen, jint zlength, jint parallel, jboolean yCopy);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareToLen(JNIEnv *env, jobject obj, jintArray x, jint xoff, jint xlen, jintArray z, jint zoff, jint zlen);
void squareToLen(jint *x, jint xoff, jint xlen, jint *z, jint zoff, jint zlen);
void primitiveLeftShift(jint *a, jint start, jint end, jint n);
jint mulAdd(jint *x, jint from, jint to, jint mul, jint *z, jint zoff);
jint addOne(jint *x, jint xoff, jint xlen, jint mlen, jint carry);

#ifdef __cplusplus
}
#endif
#endif