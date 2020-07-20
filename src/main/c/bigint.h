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
#include <math.h>
#include <jni.h>

#ifndef _Included_org_libj_math_BigInt
#define _Included_org_libj_math_BigInt
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMultiplyQuad(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jintArray z);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMultiplyQuadInline(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jint zlen);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKmul(JNIEnv *env, jobject obj, jintArray xarr, jintArray yarr, jint off, jint yoff, jint len, jintArray zarr, jint zoff);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKmul1(JNIEnv *env, jobject obj, jintArray xarr, jintArray yarr, jint off, jint yoff, jint len, jintArray zarr, jint zoff);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKmul2(JNIEnv *env, jobject obj, jintArray xarr, jintArray yarr, jint off, jint yoff, jint len, jintArray x2arr);
JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKmul3(JNIEnv *env, jobject obj, jintArray z0arr, jint off, jint k, jint len, jintArray zarr, jint zoff);
void kmul1(jint *x, jint *y, jint off, jint yoff, jint len, jint *z, jint zoff);
void kmul(jint *xarr, jint *yarr, jint off, jint yoff, jint len, jint *z, jint zoff);

#ifdef __cplusplus
}
#endif
#endif