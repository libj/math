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

#include "bigint.h"
#include "decimal.h"

#ifdef CRITICAL_NATIVE

static JNINativeMethod bigIntMethods[] = {
  { "nativeKaratsuba", "([II[II[IIIIIIII)V", (void*)JavaCritical_org_libj_math_BigIntMultiplication_nativeKaratsuba },
  { "nativeMulQuad", "([II[II[I)V", (void*)JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuad },
  { "nativeMulQuadInPlace", "([II[III)V", (void*)JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace },
  { "nativeSquareKaratsuba", "([II[IIIZII)V", (void*)JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba },
  { "nativeSquareQuad", "([III[III)V", (void*)JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareQuad }
};

static JNINativeMethod decimalMethods[] = {
  { "nativeDoubleValue", "(JS)D", (void*)JavaCritical_org_libj_math_DecimalNative_nativeDoubleValue },
  { "nativeLog", "(JS)D", (void*)JavaCritical_org_libj_math_DecimalNative_nativeLog },
  { "nativeLogBase", "(JSD)D", (void*)JavaCritical_org_libj_math_DecimalNative_nativeLogBase },
  { "nativeSin", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeSin },
  { "nativeCos", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeCos },
  { "nativeTan", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeTan },
  { "nativeAsin", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeAsin },
  { "nativeAcos", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeAcos },
  { "nativeAtan", "(JSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeAtan },
  { "nativeAtan2", "(JSJSIJ)J", (void*)JavaCritical_org_libj_math_DecimalNative_nativeAtan2 }
};

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  if ((*vm)->GetEnv(vm, (void*)&env, JNI_VERSION_1_6) != JNI_OK)
    return JNI_ERR;

  jclass clazz = (*env)->FindClass(env, "org/libj/math/BigIntMultiplication");
  if (!clazz)
    return JNI_ERR;

  jint ret = (*env)->RegisterNatives(env, clazz, bigIntMethods, sizeof(bigIntMethods) / sizeof(bigIntMethods[0]));
  (*env)->DeleteLocalRef(env, clazz);
  if (ret != 0)
    return JNI_ERR;

  clazz = (*env)->FindClass(env, "org/libj/math/DecimalNative");
  if (!clazz)
    return JNI_ERR;

  ret = (*env)->RegisterNatives(env, clazz, decimalMethods, sizeof(decimalMethods) / sizeof(decimalMethods[0]));
  (*env)->DeleteLocalRef(env, clazz);

  return ret == 0 ? JNI_VERSION_1_6 : JNI_ERR;
}

#endif