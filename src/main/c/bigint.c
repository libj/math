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

const jlong LONG_INT_MASK = 0xFFFFFFFFL;
const jint OFF = 1;

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMultiplyQuad(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jintArray zarr) {
  jint *x = (*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (*env)->GetPrimitiveArrayCritical(env, yarr, NULL);
  jint *z = (*env)->GetPrimitiveArrayCritical(env, zarr, NULL);

  int i, j, k;
  unsigned long carry = 0;
  jlong x0 = x[1] & LONG_INT_MASK;
  for (j = 1; j <= ylen; ++j) {
    z[j] = (jint)(carry += x0 * (y[j] & LONG_INT_MASK));
    carry >>= 32;
  }

  z[j] = (jint)carry;
  for (i = 2; i <= xlen; ++i) {
    x0 = x[i] & LONG_INT_MASK;
    for (carry = 0, j = 1, k = i; j <= ylen; ++j, ++k) {
      z[k] = (jint)(carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK));
      carry >>= 32;
    }

    z[k] = (jint)carry;
  }

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, 0);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMultiplyQuadInline(JNIEnv *env, jobject obj, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jint zlen) {
  jint *x = (*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (*env)->GetPrimitiveArrayCritical(env, yarr, NULL);

  int i, j, k, l;
  unsigned long carry = 0;
  jlong x0 = x[1] & LONG_INT_MASK;
  for (j = 1, k = 1 + zlen; j <= ylen; ++j, ++k) {
    y[j] = (jint)(carry += x0 * ((y[k] = y[j]) & LONG_INT_MASK));
    carry >>= 32;
  }

  y[k] = y[j];
  y[j] = (jint)carry;
  for (i = 2; i <= xlen; ++i) {
    x0 = x[i] & LONG_INT_MASK;
    for (carry = 0, j = 1, k = i, l = 1 + zlen; j <= ylen; ++j, ++k, ++l) {
      y[k] = (jint)(carry += x0 * (y[l] & LONG_INT_MASK) + (y[k] & LONG_INT_MASK));
      carry >>= 32;
    }

    y[k] = (jint)carry;
  }

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, 0);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKmul(JNIEnv *env, jobject obj, jintArray xarr, jintArray yarr, jint off, jint yoff, jint len, jintArray zarr, jint zoff) {
  jint *x = (*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (*env)->GetPrimitiveArrayCritical(env, yarr, NULL);
  jint *z = (*env)->GetPrimitiveArrayCritical(env, zarr, NULL);

  kmul(x, y, off, yoff, len, z, zoff);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, 0);
}

void kmul(jint *x, jint *y, jint off, jint yoff, jint len, jint *z, jint zoff) {
  int i, j, k, l;
  unsigned long carry = 0;
  jlong x0;
  const jint ooff = OFF + off;

  if (len <= 32) {
    const jint zoff2 = zoff - off, yooff = ooff + yoff, loff = len + off, lyoff = loff + yoff;

    for (x0 = x[ooff] & LONG_INT_MASK, k = yooff, j = OFF + zoff, len += zoff; j <= len; ++j, ++k) {
      z[j] = (jint)(carry += x0 * (y[k] & LONG_INT_MASK));
      carry >>= 32;
    }

    z[j] = (jint)carry;
    for (i = ooff + 1, l = len + OFF + 1; i <= loff; ++i, ++l) {
      carry = 0;
      for (x0 = x[i] & LONG_INT_MASK, j = yooff, k = i + zoff2; j <= lyoff; ++j, ++k) {
        z[k] = (jint)(carry += x0 * (y[j] & LONG_INT_MASK) + (z[k] & LONG_INT_MASK));
        carry >>= 32;
      }

      z[l] = (jint)carry;
    }
  }
  else {
    const jint b = len >> 1, b1 = b + 1, bb = b + b, lb = len - b, lblb = lb + lb, bbo = bb + OFF, lbo = lb + OFF, lbo1 = lbo + 1, lbbo1 = b + lbo1;

    jint *x2 = calloc(lbo + lbo1, sizeof(jint));
    for (i = 0, j = ooff + i, k = j + b; i < b; ++i, ++j, ++k) {
      x2[i] = (jint)(carry += (x[k] & LONG_INT_MASK) + (x[j] & LONG_INT_MASK));
      carry >>= 32;
    }

    if ((len & 1) != 0)
      x2[b] = x[off + bbo];

    if (carry != 0 && ++x2[b] == 0)
      ++x2[b1];

    carry = 0;
    for (i = lbo1, j = off + yoff + i - lbo, k = j + b; i < lbbo1; ++i, ++j, ++k) {
      x2[i] = (jint)(carry += (y[k] & LONG_INT_MASK) + (y[j] & LONG_INT_MASK));
      carry >>= 32;
    }

    if ((len & 1) != 0)
      x2[lbbo1] = y[off + bbo + yoff];

    if (carry != 0 && ++x2[lbbo1] == 0)
      ++x2[lbbo1 + 1];

    k = lb + (x2[lb] != 0 || x2[lbo + lbo] != 0 ? 1 : 0);
    const jint kk = k + k, kkbb = kk + bb;
    jint *z0 = calloc(kkbb + lblb, sizeof(jint));
    kmul(x2, x2, -OFF, lbo1, k, z0, -OFF);
    free(x2);

    kmul(x, y, off, yoff, b, z0, kk - OFF);
    kmul(x, y, off + b, yoff, lb, z0, kkbb - OFF);

    memcpy(z + (OFF + zoff), z0 + kk, (bb + lblb) * sizeof(jint));

    for (i = 0, j = b1 + zoff, k = kkbb, l = kk, x0 = 0; i < bb; ++i, ++j, ++k, ++l, x0 >>= 32)
      z[j] = (jint)(x0 += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK) - (z0[k] & LONG_INT_MASK) - (z0[l] & LONG_INT_MASK));

    for (j = i + b1 + zoff, k = i + kkbb; i < lblb; ++i, ++j, ++k, x0 >>= 32)
      z[j] = (jint)(x0 += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK) - (z0[k] & LONG_INT_MASK));

    for (j = i + b1 + zoff, len = kk - OFF; i < len; ++i, ++j, x0 >>= 32)
      z[j] = (jint)(x0 += (z[j] & LONG_INT_MASK) + (z0[i] & LONG_INT_MASK));

    free(z0);
    if (x0 != 0)
      while (++z[j++] == 0);
  }
}