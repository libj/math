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

const jlong LONG_MASK = 0xFFFFFFFFL;
const jint OFF = 1;
const jint BIG_INT_MAX_VALUE = 2147483647;

JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuad(jint _x, jint *x, jint xlen, jint _y, jint *y, jint ylen, jint _z, jint *z) {
  jint i, j, k;

  unsigned long long carry = 0;
  jlong x0 = x[1] & LONG_MASK;

  for (j = 1; j <= ylen; ++j) { // [A]
    z[j] = (jint)(carry += x0 * (y[j] & LONG_MASK));
    carry >>= 32;
  }

  z[j] = (jint)carry;
  for (i = 2; i <= xlen; ++i) { // [A]
    x0 = x[i] & LONG_MASK;
    for (carry = 0, j = 1, k = i; j <= ylen; ++j, ++k) { // [A]
      z[k] = (jint)(carry += x0 * (y[j] & LONG_MASK) + (z[k] & LONG_MASK));
      carry >>= 32;
    }

    z[k] = (jint)carry;
  }
}

JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace(jint _x, jint *x, jint xlen, jint _y, jint *y, jint ylen, jint zlen) {
  jint i, j, k, l;

  unsigned long long carry = 0;
  jlong x0 = x[1] & LONG_MASK;
  zlen += OFF;

  // memcpy(y + zlen, y + OFF, ylen * sizeof(jint));

  for (j = OFF, l = zlen; j <= ylen; ++j, ++l) { // [A]
    y[j] = (jint)(carry += x0 * ((y[l] = y[j]) & LONG_MASK));
    carry >>= 32;
  }

  y[j] = (jint)carry;
  for (i = 2; i <= xlen; ++i) { // [A]
    x0 = x[i] & LONG_MASK;
    for (carry = 0, j = OFF, k = i, l = zlen; j <= ylen; ++j, ++k, ++l) { // [A]
      y[k] = (jint)(carry += x0 * (y[l] & LONG_MASK) + (y[k] & LONG_MASK));
      carry >>= 32;
    }

    y[k] = (jint)carry;
  }
}

typedef struct KaratsubaArgs {
  jint *x, xoff, *y, yoff, *z, zoff, zlen, zlength, off, len, parallelThreshold, parallelThresholdZ;
} KaratsubaArgs;

void* karatsubaThread(void *args) {
  KaratsubaArgs *ka = (KaratsubaArgs*)args;
  jint *x = ka->x, xoff = ka->xoff, *y = ka->y, yoff = ka->yoff, *z = ka->z, zoff = ka->zoff, zlen = ka->zlen, zlength = ka->zlength, off = ka->off, len = ka->len, parallelThreshold = ka->parallelThreshold, parallelThresholdZ = ka->parallelThresholdZ;
  free(args);
  karatsuba(x, xoff, y, yoff, z, zoff, zlen, zlength, off, len, parallelThreshold, parallelThresholdZ);
  return NULL;
}

void karatsuba(jint *x, jint xoff, jint *y, jint yoff, jint *z, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ) {
  jint i, j, k, l, m;
  jlong x0;

  unsigned long long carry = 0;
  const jint xoffoff = xoff + off, yoffoff = yoff + off;

  if (len <= 32) {
    const jint yoffoffl = yoffoff + len, zoffl = zoff + len, xoffoffl = xoffoff + len;

    jlong x0 = x[xoffoff] & LONG_MASK;
    for (k = yoffoff, j = zoff; j < zoffl; ++j, ++k) { // [A]
      z[j] = (jint)(carry += x0 * (y[k] & LONG_MASK));
      carry >>= 32;
    }

    z[j] = (jint)carry;
    for (i = xoffoff + 1, l = zoffl + 1, m = zoff + 1; i < xoffoffl; ++i, ++l, ++m) { // [A]
      carry = 0;
      x0 = x[i] & LONG_MASK;
      for (j = yoffoff, k = m; j < yoffoffl; ++j, ++k) { // [A]
        z[k] = (jint)(carry += x0 * (y[j] & LONG_MASK) + (z[k] & LONG_MASK));
        carry >>= 32;
      }

      z[l] = (jint)carry;
    }
  }
  else {
    const bool parallel = len > parallelThreshold && zlen > parallelThresholdZ;
    const jint b = len >> 1, b2 = b * 2, ll = len * 2, l_b = len - b, l_b2 = l_b * 2;
    jint tmpoff, x2offl_b2, y2offl_b2;
    jint *tmp;
    bool allocated;

    j = ll + l_b2 + 2; // length needed for `x2` computation
    k = j + l_b2 + 1;  // length needed for `y2` computation
    if (!parallel && zlength >= (i = zoff + zlen) + k + 1) {
      tmpoff = i;
      x2offl_b2 = j + i;
      y2offl_b2 = k + i;
      tmp = z;
      allocated = false;
    }
    else {
      tmpoff = 0;
      x2offl_b2 = j;
      y2offl_b2 = k;
      zlength = y2offl_b2 + 1;
      tmp = (jint*)calloc(zlength, sizeof(jint));
      allocated = true;
    }

    const jint x2offl_b2b = x2offl_b2 + b, y2offl_b = x2offl_b2 + l_b, y2offl_b1 = y2offl_b + 1, y2offl_b1b = y2offl_b1 + b;
    tmp[x2offl_b2b] = tmp[y2offl_b1b] = tmp[y2offl_b] = tmp[y2offl_b2] = 0;

    for (i = x2offl_b2, j = xoffoff, k = xoffoff + b; i < x2offl_b2b; ++i, ++j, ++k) { // [A]
      tmp[i] = (jint)(carry += (x[j] & LONG_MASK) + (x[k] & LONG_MASK));
      carry >>= 32;
    }

    if ((len & 1) != 0) {
      tmp[x2offl_b2b] = x[xoffoff + b2];
    }

    if (carry != 0 && ++tmp[x2offl_b2b] == 0) {
      ++tmp[x2offl_b2b + 1];
    }

    carry = 0;
    for (i = y2offl_b1, j = yoffoff, k = yoffoff + b; i < y2offl_b1b; ++i, ++j, ++k) { // [A]
      tmp[i] = (jint)(carry += (y[j] & LONG_MASK) + (y[k] & LONG_MASK));
      carry >>= 32;
    }

    if ((len & 1) != 0) {
      tmp[y2offl_b1b] = y[yoffoff + b2];
    }

    if (carry != 0 && ++tmp[y2offl_b1b] == 0) {
      ++tmp[y2offl_b1b + 1];
    }

    const jint tmpoffl_b2 = tmpoff + l_b2;
    const jint tmplen = tmpoffl_b2 + l_b2 + 3;
    const jint r = l_b + (tmp[y2offl_b] != 0 || tmp[y2offl_b2] != 0 ? 1 : 0);
    const jint tmpoffrr = tmpoff + r * 2, tmpoffbb = tmpoff + b2, tmpoffrrbb = tmpoffrr + b2;
    if (!parallel) {
      karatsuba(tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, zlength, 0, r, BIG_INT_MAX_VALUE, BIG_INT_MAX_VALUE);
      karatsuba(x, xoff, y, yoff, tmp, tmpoffrr, tmplen, zlength, off, b, BIG_INT_MAX_VALUE, BIG_INT_MAX_VALUE);
      karatsuba(x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, zlength, off + b, l_b, BIG_INT_MAX_VALUE, BIG_INT_MAX_VALUE);
    }
    else {
      KaratsubaArgs *args1 = (KaratsubaArgs*)malloc(sizeof(KaratsubaArgs));
      *args1 = (KaratsubaArgs){ tmp, x2offl_b2, tmp, y2offl_b1, tmp, tmpoff, tmplen, zlength, 0, r, parallelThreshold * 2, parallelThresholdZ * 2 };

      KaratsubaArgs *args2 = (KaratsubaArgs*)malloc(sizeof(KaratsubaArgs));
      *args2 = (KaratsubaArgs){ x, xoff, y, yoff, tmp, tmpoffrr, tmplen, zlength, off, b, parallelThreshold * 2, parallelThresholdZ * 2 };

      KaratsubaArgs *args3 = (KaratsubaArgs*)malloc(sizeof(KaratsubaArgs));
      *args3 = (KaratsubaArgs){ x, xoff, y, yoff, tmp, tmpoffrrbb, tmplen, zlength, off + b, l_b, parallelThreshold * 2, parallelThresholdZ * 2 };

      pthread_t t1;
      int s1 = pthread_create(&t1, NULL, karatsubaThread, args1);

      pthread_t t2;
      int s2 = pthread_create(&t2, NULL, karatsubaThread, args2);

      pthread_t t3;
      int s3 = pthread_create(&t3, NULL, karatsubaThread, args3);

      pthread_join(t1, NULL);
      pthread_join(t2, NULL);
      pthread_join(t3, NULL);
    }

    memcpy(z + zoff, tmp + tmpoffrr, ll * sizeof(jint));

    x0 = 0;
    for (i = tmpoff, j = zoff + b, k = tmpoffrrbb, l = tmpoffrr, m = tmpoffbb; i < m; ++i, ++j, ++k, ++l) { // [A]
      z[j] = (jint)(x0 += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK) - (tmp[k] & LONG_MASK) - (tmp[l] & LONG_MASK));
      x0 >>= 32;
    }

    for (; i < tmpoffl_b2; ++i, ++j, ++k) { // [A]
      z[j] = (jint)(x0 += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK) - (tmp[k] & LONG_MASK));
      x0 >>= 32;
    }

    for (m = tmpoffrr - 1; i < m; ++i, ++j) { // [A]
      z[j] = (jint)(x0 += (z[j] & LONG_MASK) + (tmp[i] & LONG_MASK));
      x0 >>= 32;
    }

    if (allocated)
      free(tmp);

    if (x0 != 0) {
      while (++z[j++] == 0);
    }
  }
}

JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeKaratsuba(jint _x, jint *x, jint xoff, jint _y, jint *y, jint yoff, jint _z, jint *z, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ) {
  karatsuba(x, xoff, y, yoff, z, zoff, zlen, zlength, off, len, parallelThreshold, parallelThresholdZ);
}

JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(jint _x, jint *x, jint len, jint _z, jint *z, jint zlen, jint zlength, jboolean yCopy, jint parallelThreshold, jint parallelThresholdZ) {
  if (yCopy) {
    // InPlace computation for `mag` requires a copy for `y`, otherwise we're reading and writing from the same array for `x` `y` and `z`
    jint *y = (jint*)calloc(len + OFF, sizeof(jint));
    memcpy(y, x, (len + OFF) * sizeof(jint));
    karatsuba(x, OFF, y, OFF, z, OFF, zlen, zlength, 0, len, parallelThreshold, parallelThresholdZ);
    free(y);
  }
  else {
    karatsuba(x, OFF, x, OFF, z, OFF, zlen, zlength, 0, len, parallelThreshold, parallelThresholdZ);
  }
}

// shifts a up to len left n bits assumes no leading zeros, 0<=n<32
void primitiveLeftShift(jint *a, jint start, jint end, jint n) {
  // if (end <= start || n == 0)
  //   return;

  jint i, n2 = 32 - n;
  jint c = a[--end];
  while (end > start) {
    i = c << n;
    c = a[end - 1];
    a[end--] = i | ((unsigned int)c >> n2);
  }

  a[start] <<= n;
}

/**
 * Add one word to the number a mlen words into a. Return the resulting carry.
 */
jint addOne(jint *x, jint xoff, jint xlen, jint mlen, jint carry) {
  xoff += mlen;
  const unsigned long long t = (x[xoff] & LONG_MASK) + (carry & LONG_MASK);

  x[xoff] = (int)t;
  if (((t) >> 32) == 0)
    return 0;

  while (--mlen >= 0) {
    if (++xoff == xlen) // Carry out of number
      return 1;

    ++x[xoff];
    if (x[xoff] != 0)
      return 0;
  }

  return 1;
}

/**
 * Multiply an array by one word k and add to result, return the carry
 */
jint mulAdd(jint *x, jint from, jint to, jint mul, jint *z, jint zoff) {
  const jlong tLong = mul & LONG_MASK;
  unsigned long long carry = 0;

  while (from < to) {
    carry += (x[from++] & LONG_MASK) * tLong + (z[zoff] & LONG_MASK);
    z[zoff++] = (jint)carry;
    carry >>= 32;
  }

  return (jint)carry;
}

/**
 * The algorithm used here is adapted from Colin Plumb's C library.
 */
JNIEXPORT void JNICALL JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareQuad(jint _x, jint *x, jint xoff, jint xlen, jint _z, jint *z, jint zoff, jint zlen) {
  jint i, j, k, off;
  long long x0 = 0;

  xlen += xoff;
  zlen += zoff;

  // Store the squares, right shifted one bit (i.e., divided by 2)
  for (i = xlen - 1, j = zlen; i >= xoff; --i) { // [A]
    k = (jint)x0 << 31;
    x0 = x[i] & LONG_MASK;
    x0 *= x0;
    z[--j] = k | (jint)((unsigned long long)x0 >> 33);
    z[--j] = (jint)(x0 >> 1);
  }

  // Add in off-diagonal sums
  for (i = xoff, j = xlen - xoff, off = zoff; i < xlen; --j, off += 2) { // [A]
    k = x[i];
    k = mulAdd(x, ++i, xlen, k, z, off + 1);
    addOne(z, off, zlen, j, k);
  }

  // Shift back up and set low bit
  primitiveLeftShift(z, zoff, zlen, 1);
  z[zoff] |= x[xoff] & 1;
}

#ifndef CRITICAL_NATIVE

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuad(JNIEnv *env, jclass clazz, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jintArray zarr) {
  jboolean zcopy;
  jint *x = (jint*)(*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (jint*)(*env)->GetPrimitiveArrayCritical(env, yarr, NULL);
  jint *z = (jint*)(*env)->GetPrimitiveArrayCritical(env, zarr, &zcopy);

  JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuad(0, x, xlen, 0, y, ylen, 0, z);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, zcopy ? 0 : JNI_ABORT);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace(JNIEnv *env, jclass clazz, jintArray xarr, jint xlen, jintArray yarr, jint ylen, jint zlen) {
  jboolean ycopy;
  jint *x = (jint*)(*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (jint*)(*env)->GetPrimitiveArrayCritical(env, yarr, &ycopy);

  JavaCritical_org_libj_math_BigIntMultiplication_nativeMulQuadInPlace(0, x, xlen, 0, y, ylen, zlen);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, ycopy ? 0 : JNI_ABORT);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeKaratsuba(JNIEnv *env, jclass clazz, jintArray xarr, jint xoff, jintArray yarr, jint yoff, jintArray zarr, jint zoff, jint zlen, jint zlength, jint off, jint len, jint parallelThreshold, jint parallelThresholdZ) {
  jboolean zcopy;
  jint *x = (jint*)(*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *y = (jint*)(*env)->GetPrimitiveArrayCritical(env, yarr, NULL);
  jint *z = (jint*)(*env)->GetPrimitiveArrayCritical(env, zarr, &zcopy);

  JavaCritical_org_libj_math_BigIntMultiplication_nativeKaratsuba(0, x, xoff, 0, y, yoff, 0, z, zoff, zlen, zlength, off, len, parallelThreshold, parallelThresholdZ);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, yarr, y, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, zcopy ? 0 : JNI_ABORT);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(JNIEnv *env, jclass clazz, jintArray xarr, jint len, jintArray zarr, jint zlen, jint zlength, jboolean yCopy, jint parallelThreshold, jint parallelThresholdZ) {
  jboolean zcopy;
  jint *x = (jint*)(*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *z = (jint*)(*env)->GetPrimitiveArrayCritical(env, zarr, &zcopy);

  JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareKaratsuba(0, x, len, 0, z, zlen, zlength, yCopy, parallelThreshold, parallelThresholdZ);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, zcopy ? 0 : JNI_ABORT);
}

JNIEXPORT void JNICALL Java_org_libj_math_BigIntMultiplication_nativeSquareQuad(JNIEnv *env, jclass clazz, jintArray xarr, jint xoff, jint xlen, jintArray zarr, jint zoff, jint zlen) {
  jboolean zcopy;
  jint *x = (jint*)(*env)->GetPrimitiveArrayCritical(env, xarr, NULL);
  jint *z = (jint*)(*env)->GetPrimitiveArrayCritical(env, zarr, &zcopy);

  JavaCritical_org_libj_math_BigIntMultiplication_nativeSquareQuad(0, x, xoff, xlen, 0, z, zoff, zlen);

  (*env)->ReleasePrimitiveArrayCritical(env, xarr, x, JNI_ABORT);
  (*env)->ReleasePrimitiveArrayCritical(env, zarr, z, zcopy ? 0 : JNI_ABORT);
}

#endif