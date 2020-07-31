# LibJ Math

[![Build Status](https://travis-ci.org/libj/math.svg?1)](https://travis-ci.org/libj/math)
[![Coverage Status](https://coveralls.io/repos/github/libj/math/badge.svg?branch=master)](https://coveralls.io/github/libj/math?branch=master)
[![Javadocs](https://www.javadoc.io/badge/org.libj/math.svg?1)](https://www.javadoc.io/doc/org.libj/math)
[![Released Version](https://img.shields.io/maven-central/v/org.libj/math.svg?1)](https://mvnrepository.com/artifact/org.libj/math)
![Snapshot Version](https://img.shields.io/nexus/s/org.libj/math?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

<ins>LibJ Math</ins> is an extension to the `java.math` Java API. The classes in <ins>LibJ Math</ins> can be split in two categories:

1. Higher-performance alternatives to functionalities present in `java.math`.
1. Supplementary functionalities missing in `java.math`.

## Higher-performance alternatives

### `BigInt`

#### Introduction

An arbitrary-precision integer replacement for `java.math.BigInteger`, with the following differences:

1. **Mutable:** `BigInt` is mutable, to allow for reuse of allocated arrays.
1. **Faster arithmetic:** The arithmetic algorithms in `BigInt` are implemented with the optimization of memory (heap allocation) and runtime performance in mind.
1. **Faster multiplication of large numbers:** Support parallel multiplication algorithm for large numbers.
1. **Support for `int` and `long` parameters and return types:** `BigInt` does not require its parameters or return types to be `BigInt`, avoiding unnecessary instantiation of transient `BigInt` objects.
1. **No preemptive exception checking:** The `BigInt` does not preemptively check for exceptions. If a programmer divides by zero he has only himself to blame. And, it is ok to have undefined behavior.
1. **Support for "object-less" operation** All methods in `BigInt` are available in static form, allowing [_bare `int[]` <ins>value-encoded number</ins> arrays_](#bare-int-value-encoded-number-arrays) to be used without a `BigInt` instance, providing further reduction in heap memory allocation.

##### Bare `int[]` <ins>value-encoded number</ins> arrays

The `BigInt` architecture exposes the underlying `int[]` array, and provides static function equivalents for all of its arithmetic instance methods. The bare `int[]` array can therefore be used as a feature-equivalent replacement for `BigInt`, with one notable difference: no `BigInt` instance is required.

#### Function Matrix

| | `BigInteger` | `BigInt` | `int[]` |
|-|:-------------:|:------------:|:------------:|
| `abs()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `neg()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `max(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `min(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `signum()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `add(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `add(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `add(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `add(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `add(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `sub(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `sub(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `sub(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `sub(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `sub(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `mul(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `mul(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `mul(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `mul(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `mul(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `div(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `div(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `div(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `div(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `div(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `divRem(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `divRem(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `divRem(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `divRem(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `divRem(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `rem(int)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `rem(int)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `rem(long)`<sup>unsigned</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `rem(long)`<sup>signed</sup> | :x: | :white_check_mark: | :white_check_mark: |
| `rem(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `mod(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `modInverse(T)` | :white_check_mark: | :x: | :x: |
| `modPow(T)` | :white_check_mark: | :x: | :x: |
| `gcd(T)` | :white_check_mark: | :x: | :x: |
| `getLowestSetBit(T)` | :white_check_mark: | :x: | :x: |
| `isProbablePrime(T)` | :white_check_mark: | :x: | :x: |
| `nextProbablePrime()` | :white_check_mark: | :x: | :x: |
| `pow(T)` | :white_check_mark: | :x: | :x: |
| `bitCount()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `bitLength()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `precision()` | :x: | :white_check_mark: | :white_check_mark: |
| `testBit(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `setBit(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `clearBit(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `flipBit(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `shiftLeft(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `shiftRight(int)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `and(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `or(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `xor(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `andNot(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `not(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `byteValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `byteValueExact()` | :white_check_mark: | :x: | :x: |
| `shortValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `shortValueExact()` | :white_check_mark: | :x: | :x: |
| `intValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `intValueExact()` | :white_check_mark: | :x: | :x: |
| `longValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `longValueExact()` | :white_check_mark: | :x: | :x: |
| `longValueUnsigned()` | :x: | :white_check_mark: | :white_check_mark: |
| `floatValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `doubleValue()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `toByteArray()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `compareTo(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `compareToAbs()` | :x: | :white_check_mark: | :white_check_mark: |
| `equals(T)` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `hashCode()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `toString()` | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `toString(int)` | :white_check_mark: | :x: | :x: |
| `clone()` | :x: | :white_check_mark: | :white_check_mark: |

#### Benchmarks

The **`BigInt`** implementation is accompanied by a custom test framework that provides:

1. Assert correctness of function results.
1. Compare runtime performance.
1. Compare heap memory allocation.

The following section provides benchmarks that compare the performance of **`BigInteger`**, **`BigInt`**, and [bare **`int[]`** <ins>value-encoded number</ins> arrays](#bare-int-value-encoded-number-arrays).

The benchmark results provide <ins>runtime</ins> and <ins>memory heap allocation</ins> performance comparisons in the following kinds of tables and charts:

1. **Summary of <ins>runtime performance</ins><br><br>**
This table shows the relative performance of each [function](#function-matrix) tested in the relevant test class ([`BigIntAdditionTest`][BigIntAdditionTest], [`BigIntMultiplicationTest`][BigIntMultiplicationTest], etc.). The values in this table represent the **"percent <ins>less time spent</ins> in the function being tested"**, as compared to the baseline, which is the test subject (`BigInteger`, `BigInt`, or `int[]`) containing the `0` value (i.e. that test subject spends 0% less time than the baseline (itself) for the test).

![runtime summary](https://user-images.githubusercontent.com/1258414/89029429-a2322700-d358-11ea-9a37-1a4c2eb32492.jpg)

1. **Summary of <ins>heap memory allocation</ins><br><br>**
This table shows the relative heap allocation performance of each [function](#function-matrix) tested in the relevant test class ([`BigIntAdditionTest`][BigIntAdditionTest], [`BigIntMultiplicationTest`][BigIntMultiplicationTest], etc.). The values in this table represent the **"percent <ins>less number of instances</ins> allocated on the heap during the execution of the function being tested"**, as compared to the baseline, which is the test subject (`BigInteger`, `BigInt`, or `int[]`) containing the `0` value (i.e. that test subject allocates 0% less instances than the baseline (itself) for the test). The columns for <ins>heap allocation</ins> results provide 2 values: `T` and `int[]`. Here, `T` represents the type of the test subject (`BigInteger`, `BigInt`, or `int[]`), and `int[]` represents the `int[]` type itself, as both `BigInteger` and `BigInt` use `int[]` as the underlying representation of the number's magnitude. Therefore, for [bare **`int[]`** <ins>value-encoded number</ins> arrays](#bare-int-value-encoded-number-arrays), `T` and `int[]` refer to the same thing.

![heap summary](https://user-images.githubusercontent.com/1258414/89029423-a100fa00-d358-11ea-909a-e8013000b784.jpg)

1. **Detailed <ins>runtime performance</ins><br><br>**
This table provides a detailed view of the <ins>runtime performance</ins> test results as delineated by the <ins>precision of the input(s) being tested</ins>. This table shows 3 meta-columns:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **Length**: The length of the underlying `int[]` (i.e. `int[].length`).<br>
&nbsp;&nbsp;&nbsp;&nbsp;b. **Precision**: The precision (number of digits) of the number -- positive precision is for positive values, and negative precision is for negative values.<br>
&nbsp;&nbsp;&nbsp;&nbsp;c. **Count**: Number of tests that were run.<br><br>
The values in the columns of the test subject (`BigInteger`, `BigInt`, or `int[]`) represent the <ins>mean time spent</ins> (nanoseconds) in the test method. If the test method being tested applies to one input (like `BigInteger.abs()`, where the one input is `this`), then the table will provide a single <ins>mean time spent</ins> value. If the test method being tested applies to two inputs (like `BigInteger.add(BigInteger)`, where the first input is `this` and the second is some other `BigInteger` instance), then the table will provide two <ins>mean time spent</ins> values -- one for each input. This feature of the test framework allows one to more easily identify performance discrepancies between "when a particular value of a particular precision is the first argument vs if it's the second argument".<br><br>
The table provides 2 additional rows at the bottom:<br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **Sum**: The sum of the <ins>mean time spent</ins> in all precision sections.<br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **+%**: The relative improvement in performance, as represented by the **"percent <ins>less time spent</ins> in the function being tested"** by evaluating the sum <ins>mean time spent</ins> in all precision sections. Like in the table for the **Summary of <ins>runtime performance</ins>**, this percentage is compared to the baseline, which is the test subject (`BigInteger`, `BigInt`, or `int[]`) containing the `0` value (i.e. that test subject spends 0% less time than the baseline (itself) for the test).

![runtime](https://user-images.githubusercontent.com/1258414/89029425-a1999080-d358-11ea-9e9b-7dad44a0f44d.jpg)

1. **Runtime performance chart**<br><br>
This chart provides a visual representation of the **Detailed <ins>runtime performance</ins>** table. Note that the **y** values are inverted, whereby the higher **y** values in the graph are better than lower.

![chart](https://user-images.githubusercontent.com/1258414/89029417-9d6d7300-d358-11ea-8626-9d5c49f9a2e5.jpg)

1. **Detailed <ins>heap memory alloction</ins><br><br>**
This table provides a detailed view of the <ins>heap memory alloction</ins> test results as delineated by the <ins>precision of the input(s) being tested</ins>. This table shows 3 meta-columns:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **Length**: The length of the underlying `int[]` (i.e. `int[].length`).<br>
&nbsp;&nbsp;&nbsp;&nbsp;b. **Precision**: The precision (number of digits) of the number -- positive precision is for positive values, and negative precision is for negative values.<br>
&nbsp;&nbsp;&nbsp;&nbsp;c. **Count**: Number of tests that were run.<br><br>
The values in the columns of the test subject (`BigInteger`, `BigInt`, or `int[]`) represent the <ins>total number of instances</ins> allocated during the execution of the tested function. Like in the table for the **Summary of <ins>heap memory allocation</ins>**, the columns for <ins>heap allocation</ins> results provide 2 values: `T` and `int[]`. Here, `T` represents the type of the test subject (`BigInteger`, `BigInt`, or `int[]`), and `int[]` represents the `int[]` type itself, as both `BigInteger` and `BigInt` use `int[]` as the underlying representation of the number's magnitude. Therefore, for [bare **`int[]`** <ins>value-encoded number</ins> arrays](#bare-int-value-encoded-number-arrays), `T` and `int[]` refer to the same thing.<br><br>
The table provides 2 additional rows at the bottom:<br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **Sum**: The sum of the <ins>total number of instances</ins> in all precision sections.<br>
&nbsp;&nbsp;&nbsp;&nbsp;a. **+%**: The relative improvement in performance, as represented by the **"percent <ins>less number of instances</ins> allocated on the heap during the execution of the function being tested"** by evaluating the sum <ins>total number of instances</ins> in all precision sections. Like in the table for the **Summary of <ins>heap memory allocation</ins>**, this percentage is compared to the baseline, which is the test subject (`BigInteger`, `BigInt`, or `int[]`) containing the `0` value (i.e. that test subject allocates 0% less instances than the baseline (itself) for the test). Similarly, the columns for <ins>heap allocation</ins> results provide 2 values: `T` and `int[]`. Here, `T` represents the type of the test subject (`BigInteger`, `BigInt`, or `int[]`), and `int[]` represents the `int[]` type itself, as both `BigInteger` and `BigInt` use `int[]` as the underlying representation of the number's magnitude. Therefore, for [bare **`int[]`** <ins>value-encoded number</ins> arrays](#bare-int-value-encoded-number-arrays), `T` and `int[]` refer to the same thing.

![heap](https://user-images.githubusercontent.com/1258414/89029420-9fcfcd00-d358-11ea-8e14-53157a4d497a.jpg)

#### Addition and subtraction ([`BigIntAdditionTest`][BigIntAdditionTest])

**Summary**

```
[runtime performance]
╔═══════════════╦═════════════╦═════════════╦═════════════╗
║               ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║      add(int) ║   +0 │   +0 ║ +199 │ +197 ║ +248 │ +236 ║
║     add(long) ║   +0 │   +0 ║ +202 │ +209 ║ +200 │ +209 ║
║  add(int,int) ║   +0 │   +0 ║ +120 │ +114 ║ +121 │ +118 ║
║ add(int,long) ║   +0 │   +0 ║ +166 │ +166 ║ +212 │ +212 ║
║        add(T) ║   +0 │   +0 ║   +1 │   +4 ║  +25 │  +31 ║
║      sub(int) ║   +0 │   +0 ║ +213 │ +208 ║ +291 │ +285 ║
║     sub(long) ║   +0 │   +0 ║ +133 │ +139 ║ +225 │ +235 ║
║  sub(int,int) ║   +0 │   +0 ║ +125 │ +123 ║ +152 │ +154 ║
║ sub(int,long) ║   +0 │   +0 ║ +254 │ +258 ║ +316 │ +321 ║
║        sub(T) ║   +0 │   +0 ║  +22 │  +22 ║  +48 │  +48 ║
╚═══════════════╩══════╧══════╩══════╧══════╩══════╧══════╝
```

```
[heap allocation]
╔═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║               ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║      add(int) ║    +0 │    +0 ║  +863 │  +200 ║  +265 │  +200 ║
║     add(long) ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
║  add(int,int) ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
║ add(int,long) ║    +0 │    +0 ║ +2136 │  +546 ║  +778 │  +546 ║
║        add(T) ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
║      sub(int) ║    +0 │    +0 ║  +863 │  +200 ║  +265 │  +200 ║
║     sub(long) ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
║  sub(int,int) ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
║ sub(int,long) ║    +0 │    +0 ║ +2127 │  +539 ║  +775 │  +539 ║
║        sub(T) ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
╚═══════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `add(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_addition of signed `int`_</sup>

```
[runtime performance]
add(int)
 2640441 in 4665ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   39 │   37 ║   13 │   12 ║   12 │   11 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   34 │   35 ║   12 │   11 ║   11 │   10 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   38 │   34 ║   14 │   11 ║   12 │   10 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   33 │   36 ║   11 │   11 ║    8 │   11 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   30 │   33 ║   10 │   13 ║    8 │   11 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   31 │   35 ║   10 │   13 ║    8 │   11 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   29 │   34 ║    9 │   11 ║    7 │   10 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   38 │   34 ║   13 │   11 ║   11 │    9 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   35 │   34 ║   11 │   11 ║    9 │   10 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   37 │   36 ║   13 │   12 ║   12 │   10 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   39 │   39 ║   12 │   14 ║   12 │   12 ║
║    │    ║     │     ║        │   sum: ║  383 │  387 ║  128 │  130 ║  110 │  115 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +199 │ +197 ║ +248 │ +236 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣏⣑⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣉⣉⣁⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣉⣉⣑⣒⣒⡒⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠒⠒⠒⠒⠤⢄⣀⣀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
add(int)
 8361 in 217ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    11 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +200 ║  +265 │  +200 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `add(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_addition of signed `long`_</sup>

```
[runtime performance]
add(long)
 946929 in 2222ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   41 │   39 ║   11 │   14 ║   11 │   15 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   40 │   40 ║   11 │   15 ║   11 │   14 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   35 │   38 ║   12 │   12 ║   11 │   11 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   37 │   35 ║   13 │    9 ║   15 │    9 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   30 │   36 ║   12 │   11 ║   12 │   10 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   30 │   36 ║   13 │   10 ║   13 │   11 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   34 │   34 ║   14 │    9 ║   14 │   10 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   34 │   36 ║   14 │    9 ║   13 │    9 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   36 │   38 ║   10 │   13 ║   11 │   14 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   42 │   37 ║   11 │   14 ║   11 │   14 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   40 │   37 ║   11 │   15 ║   11 │   14 ║
║    │    ║     │     ║        │   sum: ║  399 │  406 ║  132 │  131 ║  133 │  131 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +202 │ +209 ║ +200 │ +209 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠭⠭⠭⣉⣉⣉⡩⠭⠭⠭⠭⠭⢭⣭⣭⠭⠭⠭⠭⣭⣭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣭⣭⡭⠭⠭⠭⠭⣭⣭⡭⠭⠭⠭⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡯⠵⠶⠖⠶⠶⠶⠭⠭⠭⠥⠤⠤⠔⠒⠒⠊⠉⠉⠁⠀⠈⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠉⠉⠉⠉⠙⠛⠛⠛⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
add(long)
 13117 in 513ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    13 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    24 │    18 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    32 │    23 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    35 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   225 │   159 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `add(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_addition of signed `int`_</sup>

```
[runtime performance]
add(int,int)
 2640441 in 4759ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   37 │   35 ║   18 │   15 ║   18 │   14 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   37 │   39 ║   18 │   19 ║   18 │   18 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   37 │   37 ║   18 │   20 ║   18 │   19 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   31 │   36 ║   15 │   20 ║   15 │   19 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   35 │   37 ║   14 │   22 ║   14 │   21 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   39 │   32 ║   15 │   15 ║   15 │   16 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   30 │   35 ║   12 │   14 ║   12 │   15 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   35 │   35 ║   17 │   15 ║   17 │   15 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   35 │   36 ║   17 │   15 ║   16 │   15 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   39 │   35 ║   18 │   14 ║   18 │   14 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   40 │   34 ║   17 │   13 ║   17 │   13 ║
║    │    ║     │     ║        │   sum: ║  395 │  391 ║  179 │  182 ║  178 │  179 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +120 │ +114 ║ +121 │ +118 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⢍⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⢤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣠⡤⠤⠒⠒⠋⠉⠁⠀⠀⠀⠀⠀⠀⠈⠉⠙⠒⠲⠤⠤⢤⣤⡤⠤⠤⠤⠤⠔⠤⠤⣢⣢⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠉⠉⠉⠑⠒⠲⠶⢖⣒⡒⠒⠒⠒⠒⣒⣒⠶⠶⠖⠒⠒⠶⠶⢖⣒⣒⡒⠒⣉⣉⠭⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠉⠉⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠤⠤⠤⠤⠔⠤⠤⠔⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠊⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
add(int,int)
 8361 in 237ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    89 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `add(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_addition of unsigned `long`_</sup>

```
[runtime performance]
add(int,long)
 946929 in 3196ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   82 │  125 ║   22 │   34 ║   20 │   31 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  100 │  133 ║   26 │   35 ║   23 │   29 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  101 │  138 ║   34 │   41 ║   28 │   33 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   77 │  134 ║   35 │   38 ║   31 │   34 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  107 │  167 ║   46 │   57 ║   41 │   47 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║  243 │   81 ║  128 │   49 ║   98 │   40 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   99 │   61 ║   39 │   27 ║   35 │   22 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   74 │   60 ║   29 │   26 ║   26 │   22 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  104 │   65 ║   32 │   35 ║   30 │   28 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  102 │   63 ║   25 │   34 ║   23 │   32 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   83 │   58 ║   23 │   31 ║   20 │   29 ║
║    │    ║     │     ║        │   sum: ║ 1172 │ 1085 ║  439 │  407 ║  375 │  347 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +166 │ +166 ║ +212 │ +212 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡿⠿⠿⠿⠿⠿⢟⣛⣛⣛⣛⡛⠛⠛⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠝⣛⣛⠯⠭⠭⠭⠿⠿⠿⢟⣛⣛⡫⠭⠭⠭⠭⠭⠭⠭⣭⣭⣭⣭⣝⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⢿
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠑⠒⠒⠒⠬⠭⣉⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⢁⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⢄⠀⠉⠒⠤⢄⣀⣀⠤⠒⠁⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⡀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠉⠉⠉⠉⠒⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⣀⣀⣀⣀⣀⡠⠤⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠈⠉⠑⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
add(int,long)
 13117 in 1170ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    39 │    25 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    37 │    25 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    12 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    12 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     9 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     9 │     9 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║    10 │    10 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    26 │    20 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    34 │    25 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    37 │    26 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   246 │   181 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2136 │  +546 ║  +778 │  +546 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `add(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_addition of `T`_</sup>

```
[runtime performance]
add(T)
 107377 in 683ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   35 │   46 ║  30 │  44 ║  22 │  34 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   36 │   43 ║  26 │  43 ║  21 │  35 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   40 │   42 ║  35 │  41 ║  32 │  32 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   54 │   37 ║  53 │  36 ║  38 │  32 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   43 │   39 ║  47 │  31 ║  38 │  26 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   56 │   36 ║  77 │  37 ║  65 │  27 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   43 │   39 ║  48 │  31 ║  44 │  25 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   50 │   42 ║  54 │  40 ║  42 │  31 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   40 │   42 ║  33 │  40 ║  27 │  32 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   36 │   42 ║  29 │  42 ║  22 │  37 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   35 │   44 ║  28 │  46 ║  23 │  33 ║
║    │    ║      │     ║       │  sum: ║  468 │  452 ║ 460 │ 431 ║ 374 │ 344 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +1 │  +4 ║ +25 │ +31 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠤⠤⠤⣀⣀⣀⣀⠤⠔⠒⠉⠉⠉⠉⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⣀⣀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠉⠉⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠤⠔⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠒⠒⠒⠢⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⠀⠀⠀⠈⠢⡀⠀⠀⠀⢀⠌⠀⠀⠀⠠⠤⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⢀⣈⣄⠤⠤⠤⠤⠬⠶⠖⠚⠓⠒⠒⠒⡒⠥⠤⣀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠤⢄⡉⠒⠤⠤⠤⠔⢉⡠⠤⠒⠉⠁⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠉⠒⢕⡄⠀⠀⠀⠀⠀⢀⠊⢀⣀⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⣒⠤⣤⠖⠓⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⣀⣀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
add(T)
 54001 in 4558ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    71 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    44 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    69 │    43 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    38 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    20 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    12 │    10 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    40 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    44 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   596 │   384 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `sub(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_subtraction of signed `int`_</sup>

```
[runtime performance]
sub(int)
 2640441 in 4675ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   41 │   38 ║   12 │   12 ║   10 │   10 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   37 │   36 ║   12 │   12 ║    9 │   10 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   37 │   36 ║   13 │   12 ║   11 │    9 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   33 │   40 ║   10 │   13 ║    8 │   10 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   36 │   36 ║   13 │   14 ║   10 │   11 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   42 │   37 ║   16 │   13 ║   12 │    9 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   31 │   40 ║    9 │   11 ║    7 │    9 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   37 │   36 ║   12 │   11 ║   10 │    9 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   37 │   37 ║   10 │   11 ║    8 │    9 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   39 │   38 ║   12 │   12 ║   10 │   10 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   41 │   39 ║   12 │   13 ║   10 │   11 ║
║    │    ║     │     ║        │   sum: ║  411 │  413 ║  131 │  134 ║  105 │  107 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +213 │ +208 ║ +291 │ +285 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⣉⣉⡩⠭⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⢉⣉⣉⠭⠭⠭⠭⠭⠭⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠁⢀⣀⣀⡠⠤⣀⣀⡀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
sub(int)
 8361 in 252ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    11 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +200 ║  +265 │  +200 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `sub(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_subtraction of signed `long`_</sup>

```
[runtime performance]
sub(long)
 946929 in 2447ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   47 │   42 ║   18 │   20 ║   12 │   15 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   47 │   43 ║   18 │   21 ║   12 │   16 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   37 │   42 ║   15 │   18 ║   10 │   12 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   39 │   41 ║   17 │   15 ║   14 │   10 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   34 │   40 ║   17 │   17 ║   12 │   11 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   34 │   40 ║   18 │   14 ║   15 │   11 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   35 │   39 ║   16 │   14 ║   13 │    9 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   38 │   40 ║   18 │   14 ║   14 │    9 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   36 │   43 ║   14 │   18 ║    9 │   14 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   47 │   41 ║   19 │   19 ║   13 │   14 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   48 │   42 ║   19 │   19 ║   12 │   14 ║
║    │    ║     │     ║        │   sum: ║  442 │  453 ║  189 │  189 ║  136 │  135 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +133 │ +139 ║ +225 │ +235 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠭⠭⠭⠭⣉⣉⡉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠒⠒⠒⠤⠤⠤⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠤⠤⣀⣀⣀⣀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠔⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠒⠒⠒⠤⠤⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
sub(long)
 13117 in 510ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    13 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    24 │    18 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    32 │    23 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    35 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   225 │   159 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `sub(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_subtraction of unsigned `int`_</sup>

```
[runtime performance]
sub(int,int)
 2640441 in 5113ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   43 │   51 ║   18 │   18 ║   17 │   16 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   48 │   45 ║   21 │   21 ║   19 │   20 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   51 │   45 ║   24 │   23 ║   20 │   20 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   44 │   45 ║   21 │   24 ║   19 │   22 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   46 │   52 ║   18 │   30 ║   17 │   25 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   56 │   47 ║   23 │   24 ║   19 │   18 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   40 │   43 ║   16 │   17 ║   14 │   15 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   47 │   44 ║   23 │   16 ║   22 │   15 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   48 │   41 ║   23 │   17 ║   20 │   15 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   41 │   43 ║   19 │   17 ║   17 │   15 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   44 │   43 ║   19 │   16 ║   17 │   15 ║
║    │    ║     │     ║        │   sum: ║  508 │  499 ║  225 │  223 ║  201 │  196 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +125 │ +123 ║ +152 │ +154 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡯⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠝⠛⠉⠉⠉⠉⣉⣉⠛⠫⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡧⠤⣀⣈⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⡠⠔⠉⠉⠀⠀⠉⠉⠑⠒⠪⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⣀⡠⠤⠤⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⣀⣀⣀⣀⠤⠤⢄⣀⣀⠀⠀⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣸
⣇⣀⣀⠤⠤⠤⠔⠒⠒⠒⠤⠤⠤⣀⣀⣀⣀⣀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
sub(int,int)
 8361 in 316ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    89 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `sub(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_subtraction of unsigned `long`_</sup>

```
[runtime performance]
sub(int,long)
 946929 in 2334ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   70 │   95 ║   15 │   19 ║   13 │   16 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   69 │   95 ║   15 │   18 ║   13 │   16 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   73 │  100 ║   17 │   22 ║   16 │   18 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   71 │   98 ║   20 │   22 ║   17 │   19 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   66 │  100 ║   19 │   27 ║   16 │   22 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   72 │   47 ║   38 │   18 ║   29 │   15 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   68 │   41 ║   23 │   14 ║   21 │   12 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   68 │   41 ║   23 │   14 ║   19 │   12 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   74 │   43 ║   17 │   18 ║   15 │   16 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   69 │   43 ║   15 │   18 ║   13 │   16 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   70 │   43 ║   15 │   18 ║   13 │   15 ║
║    │    ║     │     ║        │   sum: ║  770 │  746 ║  217 │  208 ║  185 │  177 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +254 │ +258 ║ +316 │ +321 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⣏⣉⣉⣉⣉⠭⠭⠭⠭⠽⢟⣛⣛⣭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠝⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣻
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⢄⣀⣀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠊⠉⠀⡠⠔⠊⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
sub(int,long)
 13117 in 643ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    39 │    25 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    37 │    24 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    12 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     9 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     9 │     9 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║    10 │    10 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    26 │    20 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    34 │    24 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    37 │    26 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   245 │   179 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2127 │  +539 ║  +775 │  +539 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `add(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_subtraction of `T`_</sup>

```
[runtime performance]
sub(T)
 107377 in 677ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   39 │   42 ║  30 │  37 ║  23 │  30 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   41 │   41 ║  30 │  35 ║  22 │  30 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   36 │   40 ║  28 │  32 ║  24 │  25 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   40 │   37 ║  32 │  29 ║  27 │  24 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   51 │   35 ║  41 │  28 ║  33 │  24 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   42 │   36 ║  40 │  31 ║  39 │  27 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   52 │   37 ║  43 │  29 ║  33 │  22 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   37 │   41 ║  33 │  32 ║  26 │  27 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   33 │   43 ║  28 │  34 ║  25 │  28 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   38 │   46 ║  32 │  36 ║  26 │  30 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   40 │   43 ║  30 │  36 ║  24 │  30 ║
║    │    ║      │     ║       │  sum: ║  449 │  441 ║ 367 │ 359 ║ 302 │ 297 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +22 │ +22 ║ +48 │ +48 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⣀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⣀⡠⠤⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠊⠉⠉⠁⠈⠉⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⢄⣀⣀⠀⢀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠉⠉⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠉⠁⠀⠈⠉⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⠒⠢⠤⣀⣀⣀⣀⡠⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⠤⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⡀⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
sub(T)
 54001 in 4488ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    71 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    44 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    69 │    43 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    38 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    20 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    12 │    10 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    40 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    44 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   596 │   384 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

#### Division ([`BigIntDivisionTest`][BigIntDivisionTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═════════════╗
║                  ║ BigInteger  ║   BigInt    ║    int[]    ║
╠══════════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║        divRem(T) ║   +5 │   +3 ║   +0 │   +0 ║  +16 │  +14 ║
║  divRem(int,int) ║   +0 │   +0 ║  +89 │  +93 ║  +90 │  +94 ║
║     divRem(long) ║   +0 │   +0 ║ +113 │ +113 ║ +117 │ +116 ║
║         div(int) ║   +0 │   +0 ║ +136 │ +139 ║ +129 │ +133 ║
║ divRem(int,long) ║   +0 │   +0 ║ +169 │ +160 ║ +176 │ +164 ║
║     div(int,int) ║   +0 │   +0 ║  +82 │  +92 ║  +71 │  +78 ║
║    div(int,long) ║   +0 │   +0 ║ +174 │ +162 ║ +178 │ +166 ║
║           div(T) ║   +0 │   +0 ║  +12 │  +12 ║  +17 │  +17 ║
║      divRem(int) ║   +0 │   +0 ║ +147 │ +148 ║ +131 │ +131 ║
║        div(long) ║   +0 │   +0 ║ +124 │ +122 ║ +126 │ +125 ║
╚══════════════════╩══════╧══════╩══════╧══════╩══════╧══════╝
```

```
[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║        divRem(T) ║    +0 │    +0 ║ +2559 │  +646 ║ +1070 │  +646 ║
║  divRem(int,int) ║    +0 │    +0 ║  +709 │  +196 ║  +256 │  +196 ║
║     divRem(long) ║    +0 │    +0 ║ +1827 │  +572 ║  +863 │  +572 ║
║         div(int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║ divRem(int,long) ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
║     div(int,int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║    div(int,long) ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
║           div(T) ║    +0 │    +0 ║ +5200 │  +809 ║ +1321 │  +809 ║
║      divRem(int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║        div(long) ║    +0 │    +0 ║ +1827 │  +572 ║  +863 │  +572 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `divRem(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by `T`, returning remainder_</sup>

```
[runtime performance]
divRem(T)
  107377 in 991ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  396 │  233 ║  399 │  328 ║  374 │  277 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  422 │  300 ║  394 │  352 ║  353 │  299 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  433 │  352 ║  346 │  376 ║  303 │  327 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  310 │  372 ║  364 │  389 ║  307 │  352 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  104 │  343 ║  212 │  315 ║  170 │  284 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  136 │  297 ║  304 │  226 ║  236 │  203 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  110 │  404 ║  221 │  366 ║  172 │  319 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  353 │  436 ║  403 │  413 ║  332 │  350 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  467 │  390 ║  346 │  393 ║  302 │  343 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  443 │  324 ║  414 │  354 ║  369 │  302 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  430 │  267 ║  414 │  323 ║  370 │  279 ║
║    │    ║      │     ║       │  sum: ║ 3604 │ 3718 ║ 3817 │ 3835 ║ 3288 │ 3335 ║
║    │    ║      │     ║       │   +%: ║   +5 │   +3 ║   +0 │   +0 ║  +16 │  +14 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⡪⠕⠉⠉⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠛⠲⣒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⢀⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⣀⠀⢀⣀⡠⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⣀⣀⡠⠴⠃⠀⠀⠀⡠⠊⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠤⠤⢄⡀⠀⠀⠀⠐⠄⠀⠀⠑⠢⠤⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠉⠙⠥⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⡀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠉⠒⢄⠀⣀⠤⠒⠊⠉⠉⠑⠢⢄⠀⡠⠊⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⢂⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠤⠤⠒⠒⠊⠉⠢⣀⠀⠀⠀⠀⠀⠀⡠⠛⠤⣀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠡⡀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⣀⣠⠒⣉⣀⣀⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠤⠤⠒⠉⠀⠀⠀⠀⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠈⠢⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠈⠒⢄⡀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(T)
  54001 in 4268ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     2 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     2 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     2 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     2 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    39 ║     2 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    41 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     2 │     5 ║     5 │     5 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    39 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │     ║      │ sum: ║   585 │   373 ║    22 │    50 ║    50 │    50 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +2559 │  +646 ║ +1070 │  +646 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `int`, returning remainder_</sup>

```
[runtime performance]
divRem(int,int)
  2640441 in 4803ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   76 │   62 ║  41 │  41 ║  42 │  41 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   76 │   62 ║  40 │  41 ║  41 │  41 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   64 │   63 ║  35 │  42 ║  36 │  42 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   50 │   65 ║  25 │  43 ║  26 │  43 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   56 │   69 ║  31 │  48 ║  29 │  47 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   62 │   78 ║  34 │  33 ║  32 │  33 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   59 │   76 ║  24 │  30 ║  23 │  30 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   52 │   71 ║  30 │  28 ║  31 │  28 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   76 │   68 ║  41 │  27 ║  39 │  27 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   77 │   66 ║  40 │  27 ║  40 │  26 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   77 │   64 ║  41 │  25 ║  41 │  25 ║
║    │    ║     │     ║        │   sum: ║  725 │  744 ║ 382 │ 385 ║ 380 │ 383 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +89 │ +93 ║ +90 │ +94 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⣔⡲⠭⠓⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠲⠦⢤⣤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢔⠮⠓⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⣔⡲⠮⠛⠉⠁⠀⠀⠀⠀⠉⠛⠶⣒⡤⠤⢤⣒⡪⠕⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠈⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠔⠒⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⠒⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int,int)
  8361 in 249ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    10 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║     9 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     8 │     7 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    89 │    74 ║    11 │    25 ║    25 │    25 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +709 │  +196 ║  +256 │  +196 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by signed `long`, returning remainder_</sup>

```
[runtime performance]
divRem(long)
  946929 in 2370ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  145 │  113 ║   71 │   51 ║   71 │   49 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  130 │  132 ║   60 │   52 ║   59 │   51 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  106 │   98 ║   40 │   46 ║   39 │   45 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   71 │   87 ║   33 │   45 ║   32 │   44 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   37 │   89 ║   18 │   47 ║   18 │   46 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   43 │   86 ║   30 │   44 ║   28 │   45 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   42 │   83 ║   22 │   44 ║   20 │   43 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   83 │   79 ║   35 │   42 ║   35 │   42 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  112 │  120 ║   45 │   49 ║   45 │   48 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  134 │  121 ║   64 │   51 ║   64 │   50 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  145 │  102 ║   72 │   49 ║   71 │   50 ║
║    │    ║     │     ║        │   sum: ║ 1048 │ 1110 ║  490 │  520 ║  482 │  513 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +113 │ +113 ║ +117 │ +116 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡤⠤⠤⠤⠤⢤⣀⣀⣀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣤⡤⠶⠶⠒⠛⠛⠛⠛⠛⠛⠛⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠙⠛⠛⠛⠓⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠶⠶⢤⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣤⣤⣤⡤⠴⠶⠖⠚⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠓⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡧⠤⠤⢄⣀⣀⣀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(long)
  13117 in 466ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    33 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    18 │    12 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     6 │     5 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     7 │     7 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    23 │    17 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    32 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   212 │   148 ║    11 │    22 ║    22 │    22 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1827 │  +572 ║  +863 │  +572 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `div(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by signed `int`_</sup>

```
[runtime performance]
div(int)
  2640441 in 4582ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   82 │   59 ║   33 │   26 ║   34 │   26 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   74 │   64 ║   31 │   26 ║   33 │   27 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   64 │   68 ║   26 │   27 ║   28 │   28 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   41 │   73 ║   17 │   27 ║   17 │   28 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   35 │   64 ║   18 │   27 ║   18 │   29 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   34 │   64 ║   19 │   28 ║   19 │   28 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   39 │   65 ║   16 │   26 ║   16 │   26 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   54 │   61 ║   21 │   26 ║   23 │   26 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   74 │   58 ║   31 │   26 ║   32 │   26 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   75 │   58 ║   31 │   25 ║   31 │   26 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   82 │   55 ║   34 │   24 ║   34 │   25 ║
║    │    ║     │     ║        │   sum: ║  654 │  689 ║  277 │  288 ║  285 │  295 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +136 │ +139 ║ +129 │ +133 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⠤⠤⠤⣒⡲⠖⠛⠉⠉⠉⠉⠉⠙⠛⠛⠫⠭⠭⠵⠶⠶⠶⠦⠤⠤⠤⠤⠒⠒⠒⠋⠉⠉⠉⠉⠉⠛⠛⠳⠶⠶⣒⣢⡤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠶⠮⠭⠭⠭⠭⠭⠭⠥⠤⠤⠤⠤⠤⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠚⠛⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠓⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
div(int)
  8361 in 191ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║     9 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     8 │     7 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    91 │    74 ║    11 │    25 ║    25 │    25 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `long`, returning remainder_</sup>

```
[runtime performance]
divRem(int,long)
  946929 in 2162ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  171 │  130 ║   72 │   42 ║   71 │   42 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  149 │  130 ║   56 │   42 ║   54 │   41 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  104 │  134 ║   38 │   44 ║   37 │   44 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   68 │  135 ║   30 │   45 ║   28 │   45 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   51 │  144 ║    9 │   50 ║    8 │   48 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   72 │   91 ║   18 │   46 ║   16 │   46 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   53 │   79 ║   14 │   37 ║   14 │   37 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   74 │   76 ║   30 │   37 ║   30 │   37 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  125 │  115 ║   45 │   45 ║   43 │   43 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  155 │  114 ║   60 │   46 ║   61 │   45 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  174 │   98 ║   72 │   44 ║   71 │   43 ║
║    │    ║     │     ║        │   sum: ║ 1196 │ 1246 ║  444 │  478 ║  433 │  471 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +169 │ +160 ║ +176 │ +164 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣠⣤⡤⠤⠤⠤⣤⣤⣀⣀⣀⣀⣀⣀⣀⣀⣠⠤⠤⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⣀⣀⣀⢤⣤⣤⣤⠴⠶⠶⠶⠒⠒⠒⠒⠒⠒⠛⠛⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠲⠶⠦⣤⣤⣤⡤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠚⠛⠛⠛⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠓⠒⠒⠒⠒⠦⠤⠤⠤⣤⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠒⠉⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⣀⡠⠤⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int,long)
  13117 in 483ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    14 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     7 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     8 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     9 │     8 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    19 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    23 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    37 │    25 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   231 │   166 ║    11 │    22 ║    22 │    22 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `div(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `int`_</sup>

```
[runtime performance]
div(int,int)
  2640441 in 4360ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   71 │   52 ║  37 │  38 ║  40 │  39 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   70 │   52 ║  36 │  38 ║  39 │  39 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   58 │   56 ║  31 │  38 ║  34 │  39 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   34 │   54 ║  22 │  38 ║  23 │  39 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   33 │   54 ║  21 │  37 ║  21 │  39 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   31 │   62 ║  21 │  24 ║  20 │  27 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   33 │   64 ║  20 │  23 ║  20 │  27 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   47 │   61 ║  27 │  23 ║  30 │  26 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   70 │   58 ║  35 │  22 ║  39 │  25 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   70 │   58 ║  36 │  22 ║  39 │  25 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   74 │   54 ║  37 │  22 ║  40 │  25 ║
║    │    ║     │     ║        │   sum: ║  591 │  625 ║ 323 │ 325 ║ 345 │ 350 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +82 │ +92 ║ +71 │ +78 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⢝⣛⣛⣛⣛⣛⡛⠛⠛⠛⠛⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢒⠥⠊⠁⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠤⢄⣀⠀⠉⠉⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⢀⣀⠀⠀⠀⡀⠀⢀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢔⡩⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠉⠉⠉⠈⠉⠁⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠊⢉⣁⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣀⣀⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⣀⡀⣀⠀⠀⡀⡀⠀⢀⢀⢀⣀⠀⠀⠀⡀⠀⢀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠉⠉⠈⠈⠉⠁⠁⠁⠀⠉⠉⠉⠈⠉⠁⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
div(int,int)
  8361 in 192ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║     9 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     8 │     7 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    91 │    74 ║    11 │    25 ║    25 │    25 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `div(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `long`_</sup>

```
[runtime performance]
div(int,long)
  946929 in 2140ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  172 │  127 ║   72 │   41 ║   72 │   40 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  149 │  130 ║   55 │   43 ║   54 │   42 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  102 │  133 ║   36 │   43 ║   36 │   43 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   67 │  132 ║   29 │   45 ║   28 │   44 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   50 │  138 ║    8 │   47 ║    7 │   47 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   54 │   85 ║   10 │   44 ║   12 │   42 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   52 │   80 ║   12 │   37 ║   12 │   37 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   73 │   75 ║   30 │   36 ║   29 │   35 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  122 │  115 ║   43 │   44 ║   41 │   43 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  158 │  111 ║   60 │   44 ║   59 │   43 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  172 │   96 ║   72 │   42 ║   71 │   43 ║
║    │    ║     │     ║        │   sum: ║ 1171 │ 1222 ║  427 │  466 ║  421 │  459 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +174 │ +162 ║ +178 │ +166 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⣄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣤⠤⠤⠖⠒⠒⠒⠒⠒⠒⠲⠶⠶⠶⠒⠒⠋⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠛⠓⠲⠶⠶⠶⠤⠤⣤⣤⣤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠛⠋⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠙⠛⠓⠒⠲⠶⠤⠤⢤⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠑⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
div(int,long)
  13117 in 465ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    14 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     7 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     8 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     9 │     8 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    19 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    23 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    37 │    25 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   231 │   166 ║    11 │    22 ║    22 │    22 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `div(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by `T`_</sup>

```
[runtime performance]
div(T)
  107377 in 594ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  340 │  152 ║  308 │  139 ║  299 │  135 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  265 │  184 ║  243 │  169 ║  233 │  162 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  199 │  202 ║  182 │  195 ║  176 │  182 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  105 │  221 ║   96 │  208 ║   89 │  204 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   43 │  196 ║   44 │  173 ║   44 │  168 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   19 │  146 ║   27 │  114 ║   32 │  114 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   51 │  243 ║   49 │  206 ║   47 │  196 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  121 │  258 ║  101 │  226 ║   99 │  209 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  230 │  231 ║  193 │  203 ║  184 │  194 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  300 │  202 ║  259 │  179 ║  243 │  165 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  380 │  165 ║  327 │  145 ║  308 │  139 ║
║    │    ║      │     ║       │  sum: ║ 2053 │ 2200 ║ 1829 │ 1957 ║ 1754 │ 1868 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +12 │  +12 ║  +17 │  +17 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠒⠋⣉⡠⠤⣀⡈⠙⠢⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡴⠖⠋⢁⡠⠒⠉⠀⠀⠀⠀⠈⠒⢄⠀⠙⢕⡤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢔⠮⠓⠁⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠈⠒⠬⣉⡒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⢒⡩⠕⠊⢁⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠈⠑⠒⠤⣈⡉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠒⠊⣉⠤⠔⠊⢁⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⣉⠑⠒⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⣀⣀⠤⠤⠒⠒⠊⢉⣉⣁⣀⡠⠤⠤⠒⠊⠉⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⡀⠈⠉⠉⠉⠑⠒⠒⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡯⠥⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⣀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
div(T)
  54001 in 4053ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    72 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   583 │   373 ║    11 │    41 ║    41 │    41 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5200 │  +809 ║ +1321 │  +809 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by signed `int`, returning remainder_</sup>

```
[runtime performance]
divRem(int)
  2640441 in 4411ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   77 │   57 ║   30 │   23 ║   32 │   25 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   74 │   58 ║   29 │   24 ║   32 │   26 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   64 │   61 ║   25 │   25 ║   28 │   26 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   42 │   64 ║   16 │   25 ║   17 │   27 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   30 │   62 ║   15 │   26 ║   14 │   27 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   27 │   62 ║   14 │   24 ║   15 │   26 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   33 │   62 ║   13 │   23 ║   14 │   25 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   54 │   59 ║   20 │   23 ║   24 │   25 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   72 │   58 ║   30 │   24 ║   30 │   26 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   74 │   57 ║   30 │   23 ║   31 │   25 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   77 │   52 ║   30 │   22 ║   32 │   24 ║
║    │    ║     │     ║        │   sum: ║  624 │  652 ║  252 │  262 ║  269 │  282 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +147 │ +148 ║ +131 │ +131 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠒⣒⣢⣤⡤⠤⠤⠤⠤⠴⠶⠶⠶⠶⠮⠭⠭⠭⠭⠭⠛⠛⠫⠭⠭⢍⣉⠑⠒⠒⠒⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⣉⣉⣉⠥⠤⠤⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⠤⠤⠬⣉⣒⣒⣒⣒⣒⣒⣒⡒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡏⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠔⠒⠒⠒⠢⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int)
  8361 in 189ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║     9 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     8 │     7 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    91 │    74 ║    11 │    25 ║    25 │    25 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `div(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by signed `long`_</sup>

```
[runtime performance]
div(long)
  946929 in 2215ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  136 │  100 ║   66 │   43 ║   67 │   42 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  115 │  120 ║   51 │   46 ║   51 │   45 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   98 │   86 ║   35 │   38 ║   35 │   38 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   61 │   74 ║   28 │   37 ║   28 │   37 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   29 │   80 ║   12 │   43 ║   12 │   41 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   29 │   80 ║   21 │   43 ║   20 │   45 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   33 │   74 ║   15 │   37 ║   15 │   37 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   74 │   72 ║   29 │   37 ║   29 │   36 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  112 │  110 ║   41 │   43 ║   40 │   43 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  120 │  109 ║   56 │   44 ║   55 │   43 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  140 │  103 ║   68 │   42 ║   67 │   41 ║
║    │    ║     │     ║        │   sum: ║  947 │ 1008 ║  422 │  453 ║  419 │  448 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +124 │ +122 ║ +126 │ +125 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣤⣤⣤⣤⣄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⠤⠤⠤⠤⣤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠒⠒⠒⠛⠓⠒⠛⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠉⠉⠓⠒⠦⠤⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠙⠛⠛⠲⠶⠶⠶⢤⣤⣤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠛⠛⠲⠶⠦⢤⣤⣤⣤⣄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
div(long)
  13117 in 446ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    33 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    18 │    12 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║     8 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     6 │     5 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     7 │     7 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    23 │    17 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    32 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   212 │   148 ║    11 │    22 ║    22 │    22 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1827 │  +572 ║  +863 │  +572 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

#### Remainder ([`BigIntRemainderTest`][BigIntRemainderTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═════════════╗
║                  ║ BigInteger  ║   BigInt    ║    int[]    ║
╠══════════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║        divRem(T) ║   +4 │   +2 ║   +0 │   +0 ║  +16 │  +16 ║
║  divRem(int,int) ║   +0 │   +0 ║  +98 │ +102 ║ +100 │ +104 ║
║     divRem(long) ║   +0 │   +0 ║ +134 │ +132 ║ +134 │ +133 ║
║     rem(int,int) ║   +0 │   +0 ║ +117 │ +119 ║ +110 │ +113 ║
║  rem(int,long):T ║   +0 │   +0 ║ +184 │ +174 ║ +191 │ +183 ║
║       rem(int):T ║   +0 │   +0 ║ +177 │ +178 ║ +160 │ +160 ║
║    rem(int,long) ║   +0 │   +0 ║ +199 │ +188 ║ +195 │ +183 ║
║ divRem(int,long) ║   +0 │   +0 ║ +191 │ +182 ║ +197 │ +187 ║
║   rem(int,int):T ║   +0 │   +0 ║ +108 │ +110 ║ +101 │ +102 ║
║        rem(long) ║   +0 │   +0 ║ +145 │ +144 ║ +137 │ +137 ║
║         rem(int) ║   +0 │   +0 ║ +155 │ +151 ║ +127 │ +125 ║
║      rem(long):T ║   +0 │   +0 ║ +141 │ +139 ║ +147 │ +142 ║
║      divRem(int) ║   +0 │   +0 ║ +131 │ +133 ║ +144 │ +147 ║
║           mod(T) ║   +0 │   +0 ║  +29 │  +30 ║  +48 │  +47 ║
║           rem(T) ║   +0 │   +0 ║  +23 │  +22 ║  +32 │  +31 ║
╚══════════════════╩══════╧══════╩══════╧══════╩══════╧══════╝
```

```
[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║        divRem(T) ║    +0 │    +0 ║ +2554 │  +578 ║  +961 │  +578 ║
║  divRem(int,int) ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
║     divRem(long) ║    +0 │    +0 ║ +1836 │  +825 ║ +1231 │  +825 ║
║     rem(int,int) ║    +0 │    +0 ║  +736 │  +155 ║  +441 │  +335 ║
║  rem(int,long):T ║    +0 │    +0 ║ +2027 │  +514 ║  +766 │  +514 ║
║       rem(int):T ║    +0 │    +0 ║  +736 │  +164 ║  +228 │  +164 ║
║    rem(int,long) ║    +0 │    +0 ║ +2027 │  +514 ║ +1362 │  +937 ║
║ divRem(int,long) ║    +0 │    +0 ║ +2027 │  +937 ║ +1362 │  +937 ║
║   rem(int,int):T ║    +0 │    +0 ║  +736 │  +155 ║  +217 │  +155 ║
║        rem(long) ║    +0 │    +0 ║ +1836 │  +448 ║ +1231 │  +825 ║
║         rem(int) ║    +0 │    +0 ║  +736 │  +155 ║  +441 │  +335 ║
║      rem(long):T ║    +0 │    +0 ║ +1836 │  +448 ║  +688 │  +448 ║
║      divRem(int) ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
║           mod(T) ║    +0 │    +0 ║ +5136 │  +768 ║ +1209 │  +768 ║
║           rem(T) ║    +0 │    +0 ║ +5209 │  +747 ║ +1227 │  +747 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `divRem(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by `T`, returning remainder_</sup>

```
[runtime performance]
divRem(T)
  107377 in 834ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  401 │  225 ║  403 │  288 ║  370 │  243 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  310 │  276 ║  299 │  303 ║  268 │  256 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  312 │  303 ║  300 │  322 ║  248 │  282 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  277 │  321 ║  304 │  331 ║  259 │  304 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  127 │  296 ║  193 │  276 ║  154 │  248 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  182 │  251 ║  272 │  214 ║  214 │  185 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  136 │  361 ║  190 │  328 ║  150 │  275 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  315 │  373 ║  325 │  358 ║  273 │  304 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  335 │  338 ║  304 │  339 ║  249 │  284 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  351 │  289 ║  316 │  311 ║  280 │  262 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  434 │  250 ║  408 │  288 ║  368 │  250 ║
║    │    ║      │     ║       │  sum: ║ 3180 │ 3283 ║ 3314 │ 3358 ║ 2833 │ 2893 ║
║    │    ║      │     ║       │   +%: ║   +4 │   +2 ║   +0 │   +0 ║  +16 │  +16 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠉⠁⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⢀⠔⠊⠉⠉⠉⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⢁⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⢀⠔⠁⠠⠂⠀⠀⡠⠔⠒⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠑⢄⣀⣀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠔⠁⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠢⠤⡈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⡡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠉⠉⠉⠉⠉⠉⠉⣉⣉⣒⣢⠤⣀⡀⠀⠀⠀⠀⠀⣀⣀⠤⠒⠉⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⠤⠒⠉⠀⠀⠀⠀⠉⠉⠚⠛⠭⠭⢍⣉⡀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠒⠉⠉⣉⣑⣒⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢕⢄⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⢀⡠⠔⠊⠉⠀⠀⠀⠀⠈⠉⠓⠶⣒⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠛⠛⠛⠛⠒⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(T)
  54001 in 4462ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    40 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    42 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     2 │     5 ║     5 │     5 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │     ║      │ sum: ║   584 │   373 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +2554 │  +578 ║  +961 │  +578 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `int`, returning remainder_</sup>

```
[runtime performance]
divRem(int,int)
  2640441 in 4611ms
╔═════════╦═══════════╦═════════════════╦═════════════╦════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt   ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   79 │   67 ║  42 │   43 ║   42 │   43 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   74 │   68 ║  41 │   43 ║   41 │   42 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   70 │   69 ║  37 │   45 ║   38 │   44 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   60 │   69 ║  26 │   44 ║   26 │   43 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   78 │   72 ║  31 │   48 ║   30 │   46 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   66 │   82 ║  33 │   32 ║   32 │   30 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   72 │   79 ║  26 │   30 ║   25 │   30 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   57 │   77 ║  32 │   28 ║   32 │   30 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   71 │   72 ║  40 │   27 ║   40 │   28 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   72 │   71 ║  41 │   27 ║   40 │   27 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   77 │   71 ║  42 │   26 ║   41 │   27 ║
║    │    ║     │     ║        │   sum: ║  776 │  797 ║ 391 │  393 ║  387 │  390 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +98 │ +102 ║ +100 │ +104 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⢭⣝⣛⡻⠿⠿⠿⠭⣭⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⢤⡲⠭⠓⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠙⠛⠭⠭⠵⢖⣒⣒⣒⣒⣤⣤⣤⣤⡤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⡲⠮⠛⠛⠫⠭⢖⣒⡤⠤⠤⢤⣔⡲⠝⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠶⢖⣒⣒⣒⣒⣒⣤⣤⣤⣀⣀⣀⠤⠴⠚⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⣀⣀⣀⡀⢀⣀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int,int)
  8361 in 218ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     2 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    17 ║    17 │    17 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `long`, returning remainder_</sup>

```
[runtime performance]
divRem(long)
  946929 in 2146ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  145 │  117 ║   68 │   46 ║   68 │   46 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  126 │  138 ║   55 │   50 ║   56 │   48 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  108 │   94 ║   36 │   41 ║   37 │   42 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   70 │   79 ║   29 │   39 ║   29 │   40 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   41 │   84 ║   15 │   43 ║   15 │   43 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   46 │   87 ║   27 │   43 ║   26 │   44 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   43 │   80 ║   18 │   41 ║   18 │   40 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   82 │   76 ║   30 │   38 ║   30 │   38 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  115 │  125 ║   43 │   47 ║   44 │   46 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  133 │  126 ║   60 │   48 ║   60 │   47 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  147 │  111 ║   69 │   45 ║   67 │   45 ║
║    │    ║     │     ║        │   sum: ║ 1056 │ 1117 ║  450 │  481 ║  450 │  479 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +134 │ +132 ║ +134 │ +133 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣤⠤⠶⠖⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠲⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠛⠛⠒⠲⠶⠶⠶⠦⠤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⢄⣀⣀⡀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠢⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⢄⣀⣀⣀⣀⣀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(long)
  13117 in 481ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    36 │    22 ║     1 │     2 ║     2 │     2 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    34 │    22 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    18 │    12 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     6 │     5 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     7 │     7 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    23 │    17 ║     1 │     2 ║     2 │     2 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    31 │    22 ║     1 │     2 ║     2 │     2 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    34 │    23 ║     1 │     2 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║   213 │   148 ║    11 │    16 ║    16 │    16 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1836 │  +825 ║ +1231 │  +825 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by unsigned `int`_</sup>

```
[runtime performance]
rem(int,int)
  2640441 in 4474ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   81 │   60 ║   36 │   34 ║   38 │   36 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   75 │   61 ║   36 │   35 ║   36 │   37 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   65 │   64 ║   30 │   35 ║   31 │   36 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   53 │   61 ║   22 │   36 ║   22 │   37 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   49 │   62 ║   20 │   38 ║   21 │   39 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   45 │   72 ║   22 │   30 ║   22 │   30 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   45 │   73 ║   19 │   25 ║   20 │   27 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   55 │   71 ║   26 │   24 ║   27 │   25 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   70 │   67 ║   35 │   24 ║   36 │   24 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   71 │   64 ║   34 │   23 ║   36 │   23 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   78 │   63 ║   36 │   23 ║   38 │   23 ║
║    │    ║     │     ║        │   sum: ║  687 │  718 ║  316 │  327 ║  327 │  337 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +117 │ +119 ║ +110 │ +113 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⢍⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣠⠤⠤⠔⠒⠒⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠚⠛⠫⠵⠶⣢⣤⡤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡠⠤⣤⣤⠶⠖⠚⠉⠉⠉⠀⠉⠉⠉⠛⠛⠛⠛⠛⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡯⠭⠭⠭⠭⠭⠭⠛⠛⠛⠓⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(int,int)
  8361 in 202ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     2 │     2 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     3 ║     2 │     2 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     3 ║     2 │     2 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     4 ║     2 │     2 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     2 │     2 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    29 ║    17 │    17 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +155 ║  +441 │  +335 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by unsigned `long`_</sup>

```
[runtime performance]
rem(int,long):T
  946929 in 2264ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  181 │  143 ║   75 │   46 ║   72 │   44 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  156 │  139 ║   56 │   46 ║   56 │   44 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  113 │  143 ║   38 │   49 ║   38 │   46 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   79 │  146 ║   31 │   49 ║   30 │   48 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   63 │  157 ║   10 │   53 ║   10 │   51 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   82 │   94 ║   20 │   45 ║   18 │   44 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   64 │   84 ║   15 │   34 ║   15 │   35 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   93 │   80 ║   32 │   33 ║   33 │   33 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  131 │  130 ║   45 │   47 ║   43 │   44 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  164 │  132 ║   62 │   48 ║   61 │   47 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  181 │  114 ║   76 │   46 ║   73 │   45 ║
║    │    ║     │     ║        │   sum: ║ 1307 │ 1362 ║  460 │  496 ║  449 │  481 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +184 │ +174 ║ +191 │ +183 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⣤⠤⠶⠶⠶⠶⠤⣤⣤⣤⣤⣤⣤⠤⠤⠔⠒⠚⠛⠛⠛⠛⠳⠶⠶⢤⣤⣄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⣀⣀⣀⣠⣤⡤⠤⠴⠶⠖⠒⠒⠚⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠙⠛⠵⠶⣒⣒⣢⣤⣤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡟⠛⠋⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠙⠛⠛⠒⠶⠶⠶⠶⠶⠶⠶⠶⠶⠶⢾
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

##### `rem(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by unsigned `long`_</sup>

```
[heap allocation]
rem(int,long):T
  13117 in 543ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    11 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     8 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    19 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    23 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    36 │    25 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   234 │   166 ║    11 │    27 ║    27 │    27 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2027 │  +514 ║  +766 │  +514 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by signed `int`_</sup>

```
[runtime performance]
rem(int):T
  2640441 in 4491ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   80 │   61 ║   30 │   23 ║   32 │   24 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   75 │   65 ║   29 │   23 ║   31 │   24 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   69 │   67 ║   25 │   24 ║   27 │   26 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   47 │   75 ║   15 │   25 ║   16 │   25 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   39 │   67 ║   14 │   28 ║   14 │   28 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   38 │   67 ║   14 │   27 ║   14 │   28 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   43 │   72 ║   13 │   24 ║   14 │   26 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   62 │   65 ║   21 │   23 ║   23 │   26 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   79 │   62 ║   28 │   22 ║   30 │   25 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   78 │   60 ║   30 │   22 ║   32 │   24 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   82 │   69 ║   30 │   21 ║   33 │   24 ║
║    │    ║     │     ║        │   sum: ║  692 │  730 ║  249 │  262 ║  266 │  280 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +177 │ +178 ║ +160 │ +160 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⢤⣔⠶⠒⠒⠒⠛⠛⠒⠒⠒⠒⠒⠒⠲⠶⠶⠶⠶⠶⢖⣒⣒⡲⠭⠭⠭⠭⠥⠬⠭⣉⣉⣒⠒⠢⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⣏⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠥⠤⠤⠔⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠭⠭⣉⣉⣉⣑⣉⣉⣉⣉⣉⣁⣀⣀⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```


```
[heap allocation]
rem(int):T
  8361 in 205ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +164 ║  +228 │  +164 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `long`, returning remainder_</sup>

```
[runtime performance]
rem(int,long)
  946929 in 2239ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  195 │  149 ║   74 │   45 ║   74 │   46 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  167 │  150 ║   57 │   47 ║   59 │   47 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  126 │  161 ║   40 │   49 ║   41 │   50 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   87 │  156 ║   32 │   49 ║   32 │   50 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   69 │  156 ║   11 │   52 ║   11 │   53 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   80 │   95 ║   17 │   43 ║   16 │   46 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   67 │   85 ║   14 │   35 ║   15 │   37 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   92 │   83 ║   33 │   34 ║   34 │   35 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  141 │  135 ║   45 │   46 ║   46 │   46 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  168 │  138 ║   63 │   49 ║   63 │   49 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  187 │  122 ║   74 │   46 ║   75 │   46 ║
║    │    ║     │     ║        │   sum: ║ 1379 │ 1430 ║  460 │  495 ║  466 │  505 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +199 │ +188 ║ +195 │ +183 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣤⡤⠤⠶⠶⠒⠒⠲⠶⠶⠶⠶⠶⠶⠶⠶⠶⠛⠛⠛⠛⠛⠛⠛⠫⠶⠶⠦⢤⣤⣤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣤⣤⣤⠤⠤⠴⠶⠖⠒⠚⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠙⠛⠒⠒⠶⠤⠤⣄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠊⠉⠉⠉⠁⠀⠈⠉⠉⠉⠒⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⠤⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡧⠤⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(int,long)
  13117 in 506ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    23 ║     1 │     3 ║     2 │     2 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    36 │    23 ║     1 │     3 ║     2 │     2 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    20 │    14 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    11 │     8 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     8 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     7 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     8 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     8 ║     1 │     2 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    19 ║     1 │     3 ║     2 │     2 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    23 ║     1 │     3 ║     2 │     2 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    36 │    25 ║     1 │     3 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║   234 │   166 ║    11 │    27 ║    16 │    16 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2027 │  +514 ║ +1362 │  +937 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by unsigned `long`, returning remainder_</sup>

```
[runtime performance]
divRem(int,long)
  946929 in 2234ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  193 │  148 ║   77 │   46 ║   76 │   46 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  167 │  149 ║   59 │   47 ║   58 │   46 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  126 │  152 ║   40 │   48 ║   40 │   48 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   86 │  153 ║   31 │   50 ║   31 │   49 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   67 │  155 ║   10 │   54 ║    9 │   51 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   66 │   96 ║   13 │   42 ║   13 │   43 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   66 │   86 ║   15 │   39 ║   14 │   39 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   92 │   83 ║   34 │   38 ║   33 │   37 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  139 │  135 ║   47 │   47 ║   46 │   45 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  170 │  148 ║   65 │   48 ║   63 │   47 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  190 │  122 ║   77 │   47 ║   75 │   46 ║
║    │    ║     │     ║        │   sum: ║ 1362 │ 1427 ║  468 │  506 ║  458 │  497 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +191 │ +182 ║ +197 │ +187 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣠⣤⣤⣔⠶⠶⠶⠶⠖⠶⠶⠶⠶⠖⠒⠒⠒⠒⠒⠲⠶⠶⠦⢤⣤⣄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⣀⣀⣀⣤⣤⠤⠤⠴⠖⠒⠒⠒⠒⠒⠚⠛⠛⠋⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠙⠛⠛⠛⠳⠶⠶⠶⠶⢤⣤⣤⣤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠙⠛⠛⠳⠶⠶⠶⠶⠶⠶⠶⠶⢾
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⠤⠔⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⢄⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡧⠤⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int,long)
  13117 in 505ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    23 ║     1 │     2 ║     2 │     2 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    36 │    23 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    20 │    14 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    11 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     8 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     7 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     8 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     8 ║     1 │     1 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    19 ║     1 │     2 ║     2 │     2 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    23 ║     1 │     2 ║     2 │     2 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    36 │    25 ║     1 │     2 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║   234 │   166 ║    11 │    16 ║    16 │    16 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2027 │  +937 ║ +1362 │  +937 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by unsigned `int`_</sup>

```
[runtime performance]
rem(int,int):T
  2640441 in 4643ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   78 │   58 ║   37 │   36 ║   38 │   35 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   71 │   58 ║   36 │   35 ║   37 │   35 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   63 │   61 ║   31 │   36 ║   32 │   35 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   49 │   60 ║   22 │   37 ║   22 │   36 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   43 │   59 ║   20 │   36 ║   19 │   37 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   43 │   70 ║   20 │   30 ║   20 │   31 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   46 │   75 ║   20 │   26 ║   19 │   29 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   57 │   68 ║   28 │   25 ║   28 │   28 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   73 │   64 ║   36 │   24 ║   39 │   26 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   72 │   62 ║   35 │   23 ║   39 │   26 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   78 │   61 ║   38 │   23 ║   41 │   26 ║
║    │    ║     │     ║        │   sum: ║  673 │  696 ║  323 │  331 ║  334 │  344 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +108 │ +110 ║ +101 │ +102 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠛⠛⠛⠛⠭⠭⢍⣑⣒⠒⠒⠢⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣠⠤⠤⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⠬⣉⣉⣉⣉⣉⣉⣉⣁⣀⣀⣀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠶⠶⠶⠶⠶⠶⠶⠶⠭⠭⠛⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⠤⠤⠒⠒⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(int,int):T
  8361 in 208ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +155 ║  +217 │  +155 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by signed `long`_</sup>

```
[runtime performance]
rem(long)
  946929 in 2122ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  151 │  117 ║   67 │   46 ║   68 │   47 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  124 │  156 ║   52 │   48 ║   52 │   49 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  108 │   91 ║   36 │   38 ║   38 │   39 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   69 │   75 ║   28 │   34 ║   30 │   35 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   37 │   82 ║   12 │   41 ║   13 │   43 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   41 │   84 ║   22 │   45 ║   22 │   45 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   41 │   77 ║   15 │   34 ║   16 │   36 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   83 │   74 ║   30 │   34 ║   32 │   35 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  112 │  124 ║   40 │   45 ║   42 │   47 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  127 │  127 ║   56 │   46 ║   58 │   49 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  149 │  110 ║   67 │   46 ║   68 │   46 ║
║    │    ║     │     ║        │   sum: ║ 1042 │ 1117 ║  425 │  457 ║  439 │  471 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +145 │ +144 ║ +137 │ +137 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣠⣤⣤⣔⣒⣒⣒⣤⣤⣀⣀⠀⠀⠀⠀⠀⠀⣀⣠⣔⠶⠮⠭⠭⠶⠶⣒⣤⣤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⣀⣠⠤⠤⠴⠶⠖⠒⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠙⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠙⠛⠛⠫⠶⠶⢖⣤⣤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠶⠖⠒⠚⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠛⠫⠵⠶⠶⠶⠶⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠢⠤⢄⣀⣀⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(long)
  13117 in 463ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    36 │    22 ║     1 │     3 ║     2 │     2 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    34 │    22 ║     1 │     3 ║     2 │     2 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    18 │    12 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║     9 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║     9 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     6 │     5 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     7 │     7 ║     1 │     2 ║     1 │     1 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    23 │    17 ║     1 │     3 ║     2 │     2 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    31 │    22 ║     1 │     3 ║     2 │     2 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    34 │    23 ║     1 │     3 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║   213 │   148 ║    11 │    27 ║    16 │    16 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1836 │  +448 ║ +1231 │  +825 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by signed `int`_</sup>

```
[runtime performance]
rem(int)
  2640441 in 4489ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   83 │   63 ║   33 │   25 ║   36 │   28 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   78 │   63 ║   32 │   26 ║   35 │   28 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   64 │   66 ║   26 │   26 ║   29 │   29 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   46 │   69 ║   15 │   27 ║   18 │   31 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   40 │   67 ║   14 │   30 ║   16 │   32 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   38 │   69 ║   15 │   29 ║   17 │   31 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   41 │   67 ║   14 │   25 ║   16 │   30 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   59 │   65 ║   22 │   24 ║   25 │   28 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   76 │   65 ║   32 │   24 ║   35 │   27 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   78 │   61 ║   32 │   24 ║   37 │   26 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   81 │   59 ║   33 │   24 ║   37 │   26 ║
║    │    ║     │     ║        │   sum: ║  684 │  714 ║  268 │  284 ║  301 │  316 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +155 │ +151 ║ +127 │ +125 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠢⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠊⠉⠉⢁⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⣀⣀⡉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⣀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠤⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(int)
  8361 in 202ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     3 ║     2 │     2 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     3 ║     2 │     2 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     3 ║     2 │     2 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     2 ║     1 │     1 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     4 ║     2 │     2 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     2 │     2 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    29 ║    17 │    17 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +155 ║  +441 │  +335 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by signed `long`_</sup>

```
[runtime performance]
rem(long):T
  946929 in 2239ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  146 │  120 ║   67 │   48 ║   67 │   48 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  135 │  142 ║   57 │   50 ║   56 │   51 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  111 │   95 ║   39 │   41 ║   39 │   39 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   71 │   80 ║   30 │   38 ║   29 │   36 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   41 │   85 ║   15 │   41 ║   13 │   40 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   40 │   87 ║   15 │   42 ║   15 │   44 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   45 │   82 ║   18 │   36 ║   18 │   36 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   85 │   77 ║   31 │   38 ║   31 │   35 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  126 │  130 ║   48 │   48 ║   46 │   47 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  134 │  131 ║   60 │   49 ║   57 │   48 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  148 │  114 ║   68 │   47 ║   66 │   47 ║
║    │    ║     │     ║        │   sum: ║ 1082 │ 1143 ║  448 │  478 ║  437 │  471 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +141 │ +139 ║ +147 │ +142 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⣔⣒⣒⣢⣤⣤⣤⣤⣤⣤⣤⣤⣤⡤⠤⠶⠶⠒⠒⠦⠤⠤⢤⣄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣤⠤⠶⠶⠶⠚⠛⠛⠛⠛⠛⠛⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠛⠛⠳⠶⢦⣤⡤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠚⠛⠫⠭⠵⠶⠶⠶⣒⣤⣤⣤⣤⣄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⡀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⢄⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠈⠉⠑⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(long):T
  13117 in 463ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    36 │    22 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    34 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    18 │    12 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║     9 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║     9 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     6 │     5 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     6 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    23 │    17 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    31 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    34 │    23 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   213 │   148 ║    11 │    27 ║    27 │    27 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1836 │  +448 ║  +688 │  +448 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `divRem(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_division by signed `int`, returning remainder_</sup>

```
[runtime performance]
divRem(int)
  2640441 in 4477ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   81 │   62 ║   35 │   28 ║   33 │   27 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   77 │   66 ║   35 │   28 ║   33 │   27 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   68 │   69 ║   29 │   29 ║   29 │   28 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   50 │   72 ║   20 │   32 ║   18 │   29 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   43 │   70 ║   19 │   32 ║   18 │   28 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   42 │   71 ║   20 │   29 ║   18 │   27 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   50 │   69 ║   18 │   30 ║   18 │   28 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   60 │   68 ║   26 │   28 ║   24 │   27 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   78 │   63 ║   35 │   27 ║   33 │   27 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   77 │   68 ║   34 │   27 ║   32 │   26 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   82 │   61 ║   35 │   27 ║   34 │   25 ║
║    │    ║     │     ║        │   sum: ║  708 │  739 ║  306 │  317 ║  290 │  299 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +131 │ +133 ║ +144 │ +147 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⠭⠭⢍⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡠⠤⠤⠔⠒⠒⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⠥⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠚⠛⠛⠛⠭⠭⠭⣑⣒⡒⠒⠒⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠚⠛⠛⠛⠛⠛⠛⠛⠛⠛⠒⠚⠛⠛⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
divRem(int)
  8361 in 200ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    11 │     8 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     6 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    11 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  408 ║     9 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  429 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     6 │     6 ║     1 │     1 ║     1 │     1 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     2 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    17 ║    17 │    17 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `mod(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_modulus by `T`_</sup>

```
[runtime performance]
mod(T)
  107377 in 630ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │       ║  468 │    0 ║  376 │    0 ║  345 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │       ║  345 │    0 ║  274 │    0 ║  254 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │       ║  287 │    0 ║  213 │    0 ║  190 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │       ║  207 │    0 ║  146 │    0 ║  119 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │       ║  139 │    0 ║   87 │    0 ║   67 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 10779 ║  114 │  187 ║   83 │  135 ║   64 │  121 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 20926 ║  101 │  313 ║   70 │  249 ║   45 │  218 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 20450 ║  194 │  338 ║  128 │  263 ║  112 │  233 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │ 18142 ║  265 │  305 ║  211 │  238 ║  184 │  208 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │ 19246 ║  348 │  271 ║  284 │  208 ║  249 │  185 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │ 17234 ║  472 │  249 ║  394 │  186 ║  356 │  162 ║
║    │    ║      │     ║       │  sum: ║ 2940 │ 1663 ║ 2266 │ 1279 ║ 1985 │ 1127 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +29 │  +30 ║  +48 │  +47 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⢊⣉⣉⡑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⢁⡠⠤⠒⠉⠁⠀⠀⠈⠉⠒⢄⡉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⢉⣉⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⢀⣀⠤⠤⣀⠀⠀⠀⠈⠢⢄⠈⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⡠⠤⠒⢒⣉⠥⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠉⠑⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠒⠊⣉⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠢⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⡠⠤⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠢⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⢄⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
mod(T)
  54001 in 4208ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    41 │    25 ║     1 │     4 ║     4 │     4 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    40 │    24 ║     1 │     4 ║     4 │     4 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    38 │    24 ║     1 │     4 ║     4 │     4 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    34 │    22 ║     1 │     4 ║     4 │     4 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    21 │    15 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │  5427 ║    10 │    11 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 10438 ║    31 │    27 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 10250 ║    83 │    57 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │  9046 ║    91 │    60 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │  9598 ║    99 │    63 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │  8642 ║    88 │    54 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │  sum: ║   576 │   382 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +5136 │  +768 ║ +1209 │  +768 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `rem(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_remainder by `T`_</sup>

```
[runtime performance]
rem(T)
  107377 in 559ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  371 │  173 ║  322 │  146 ║  305 │  132 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  297 │  207 ║  253 │  169 ║  236 │  160 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  219 │  229 ║  179 │  192 ║  173 │  182 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  126 │  255 ║   90 │  221 ║   85 │  207 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   57 │  213 ║   44 │  178 ║   37 │  168 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   31 │  151 ║   34 │  115 ║   28 │  110 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   70 │  268 ║   50 │  226 ║   43 │  202 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  146 │  295 ║  102 │  237 ║   96 │  214 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  257 │  260 ║  199 │  209 ║  183 │  192 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  344 │  231 ║  273 │  179 ║  252 │  165 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  409 │  194 ║  344 │  149 ║  315 │  145 ║
║    │    ║      │     ║       │  sum: ║ 2327 │ 2476 ║ 1890 │ 2021 ║ 1753 │ 1877 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +23 │  +22 ║  +32 │  +31 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⢒⡪⠝⠒⠒⠒⠪⣑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⣉⠤⠊⠁⠀⠀⣀⣀⣀⠀⠀⠑⢄⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⢊⡡⠔⠒⠉⠀⠀⠀⡠⠒⠉⠀⠀⠀⠉⠢⡀⠀⠑⢄⡀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⢊⣉⠤⠒⠊⠁⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠈⠒⠢⠤⣀⣀⡀⠀⠈⠉⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠒⢒⣉⡩⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⣀⡀⠀⠉⠑⠒⠒⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠒⠒⠊⠉⠉⢀⣀⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⣀⡠⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
rem(T)
  54001 in 4162ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   584 │   373 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5209 │  +747 ║ +1227 │  +747 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

#### Binary ([`BigIntBinaryTest`][BigIntBinaryTest])

**Summary**

```
[runtime performance]
╔═══════════╦═════════════╦═════════════╦═════════════╗
║           ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║    and(T) ║   +0 │   +0 ║ +101 │ +106 ║ +207 │ +215 ║
║     not() ║   +0 │   +0 ║ +220 │  +83 ║ +550 │ +201 ║
║    xor(T) ║   +0 │   +0 ║  +89 │ +137 ║ +213 │ +277 ║
║     or(T) ║   +0 │   +0 ║ +117 │  +90 ║ +249 │ +207 ║
║ andNot(T) ║   +0 │      ║  +96 │      ║ +223 │      ║
╚═══════════╩══════╧══════╩══════╧══════╩══════╧══════╝
```

```
[heap allocation]
╔═══════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║  BigInteger   ║    BigInt     ║     int[]     ║
║           ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║    and(T) ║    +0 │    +0 ║ +5290 │  +779 ║ +1247 │  +779 ║
║     not() ║    +0 │    +0 ║ +3118 │  +569 ║  +972 │  +569 ║
║    xor(T) ║    +0 │    +0 ║ +5327 │  +788 ║ +1256 │  +788 ║
║     or(T) ║    +0 │    +0 ║ +5336 │  +793 ║ +1259 │  +793 ║
║ andNot(T) ║    +0 │    +0 ║ +5290 │  +781 ║ +1247 │  +781 ║
╚═══════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `and(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_and with `T`_</sup>

```
[runtime performance]
and(T)
  107377 in 1164ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  144 │  176 ║   54 │   83 ║   33 │   56 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  144 │  172 ║   63 │   88 ║   38 │   57 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  191 │  166 ║   79 │   74 ║   53 │   52 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  186 │  150 ║   87 │   68 ║   55 │   47 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  188 │  144 ║  109 │   64 ║   77 │   40 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  245 │  166 ║  127 │   72 ║   86 │   43 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  162 │  161 ║   87 │   64 ║   59 │   42 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  145 │  140 ║   78 │   76 ║   50 │   47 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  149 │  142 ║   75 │   77 ║   46 │   47 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  103 │  142 ║   60 │   82 ║   42 │   50 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  106 │  153 ║   54 │   81 ║   34 │   62 ║
║    │    ║      │     ║       │  sum: ║ 1763 │ 1712 ║  873 │  829 ║  573 │  543 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +101 │ +106 ║ +207 │ +215 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡯⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠉⠉⠉⠉⠑⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠢⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠤⠒⠒⠒⠒⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠉⠁⠀⠈⠉⠉⠉⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠊⠉⠉⠉⠉⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠔⠒⠢⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⣀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
and(T)
  54001 in 4382ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    45 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    38 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    21 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    11 │    10 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    40 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    44 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   593 │   387 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5290 │  +779 ║ +1247 │  +779 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `not()`<br>&nbsp;&nbsp;&nbsp;<sup>_not_</sup>

```
[runtime performance]
not()
  107377 in 741ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         98 ║     22 ║     6 ║
║     44 ║      -102 ║  9605 ║         82 ║     21 ║     5 ║
║     41 ║       -76 ║  9454 ║         57 ║     20 ║     4 ║
║     39 ║       -51 ║  9166 ║         77 ║     30 ║    18 ║
║     36 ║       -25 ║  8398 ║         55 ║     21 ║    17 ║
║     34 ║         0 ║  8523 ║         99 ║     68 ║    59 ║
║     36 ║        25 ║  8348 ║         80 ║     22 ║    12 ║
║     39 ║        51 ║  9191 ║         98 ║     29 ║    12 ║
║     41 ║        76 ║  9501 ║         71 ║     20 ║     3 ║
║     44 ║       102 ║  9693 ║        111 ║     20 ║     5 ║
║     47 ║       128 ║ 11735 ║        121 ║     23 ║     5 ║
║        ║           ║  sum: ║        949 ║    296 ║   146 ║
║        ║           ║   +%: ║         +0 ║   +220 ║  +550 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠫⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠒⠉⠑⠒⢌⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⣀⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠑⢄⠀⠀⠀⠀⡠⠊⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠉⠒⠒⠉⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⣀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⣀⢀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
not()
  54001 in 2586ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    23 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    21 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    14 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     7 │     6 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    22 │    16 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    34 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    37 │    24 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    39 │    24 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    41 │    25 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   354 │   221 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3118 │  +569 ║  +972 │  +569 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `xor(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_xor with `T`_</sup>

```
[runtime performance]
xor(T)
  107377 in 953ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  144 │  137 ║  59 │  75 ║   35 │   45 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  110 │  134 ║  48 │  68 ║   30 │   41 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  108 │  133 ║  51 │  67 ║   29 │   39 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  130 │  121 ║  75 │  62 ║   42 │   36 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  144 │  113 ║  85 │  57 ║   55 │   35 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  163 │  129 ║  99 │  61 ║   68 │   43 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  143 │  107 ║  81 │  55 ║   54 │   32 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  111 │  112 ║  74 │  63 ║   41 │   38 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   90 │  110 ║  50 │  65 ║   29 │   37 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   89 │  122 ║  52 │  64 ║   30 │   39 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  116 │  127 ║  61 │  71 ║   34 │   44 ║
║    │    ║      │     ║       │  sum: ║ 1348 │ 1345 ║ 735 │ 708 ║  447 │  429 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +83 │ +89 ║ +201 │ +213 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩══════╧══════╝

⣏⣉⣉⠭⠭⠭⠭⠝⠛⠛⠛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠒⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⣀⠤⠤⠒⠒⠒⠊⠉⠉⠒⠒⠒⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠒⠒⠒⠤⠤⣀⣀⣀⣀⣀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠔⠒⠒⠒⠒⠢⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
xor(T)
  54001 in 4383ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    45 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    38 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    21 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    12 │    11 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    59 │    41 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    65 │    43 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    45 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    65 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   597 │   391 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5327 │  +788 ║ +1256 │  +788 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `or(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_or with `T`_</sup>

```
[runtime performance]
or(T)
  107377 in 726ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  134 │  119 ║   42 │   53 ║   24 │   35 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  119 │  125 ║   43 │   57 ║   26 │   40 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  116 │  120 ║   45 │   51 ║   25 │   30 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   99 │  110 ║   38 │   46 ║   23 │   26 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  120 │  114 ║   60 │   43 ║   39 │   30 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  133 │  113 ║   67 │   45 ║   58 │   28 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  238 │   89 ║   70 │   44 ║   43 │   24 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   74 │   86 ║   44 │   43 ║   25 │   26 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   86 │   84 ║   49 │   45 ║   30 │   29 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   85 │   97 ║   46 │   50 ║   24 │   30 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   90 │   92 ║   41 │   51 ║   26 │   31 ║
║    │    ║      │     ║       │  sum: ║ 1294 │ 1149 ║  545 │  528 ║  343 │  329 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +137 │ +117 ║ +277 │ +249 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡟⠛⠛⠛⠭⠭⠭⠭⠭⠭⠭⠝⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠭⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⠭⠭⠝⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠉⠉⠉⠒⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⣀⣀⣀⣀⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
or(T)
  54001 in 4474ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    71 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    45 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    69 │    44 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    39 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    21 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    12 │    11 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    59 │    41 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    43 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    45 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    65 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   598 │   393 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5336 │  +793 ║ +1259 │  +793 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `andNot(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_andNot with `T`_</sup>

```
[runtime performance]
andNot(T)
  107377 in 575ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  111 │   83 ║  38 │  49 ║   23 │   29 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   98 │   90 ║  37 │  44 ║   23 │   26 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  102 │   89 ║  44 │  44 ║   26 │   25 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   81 │   83 ║  49 │  39 ║   33 │   23 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   80 │   88 ║  60 │  37 ║   36 │   23 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   79 │   87 ║  42 │  39 ║   34 │   28 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   65 │   69 ║  44 │  36 ║   27 │   23 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   59 │   74 ║  41 │  38 ║   26 │   23 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   70 │   75 ║  40 │  41 ║   23 │   23 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   68 │   78 ║  35 │  41 ║   19 │   24 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   77 │   80 ║  36 │  47 ║   19 │   30 ║
║    │    ║      │     ║       │  sum: ║  890 │  896 ║ 466 │ 455 ║  289 │  277 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +90 │ +96 ║ +207 │ +223 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩══════╧══════╝

⣏⣉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠛⠛⠛⠛⠛⠛⠋⠉⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠢⠤⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠒⠒⠒⠒⠉⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠉⠑⠢⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠔⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠑⠒⠢⠤⣀⣀⣀⣀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠔⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
andNot(T)
  54001 in 4256ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    74 │    45 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    59 │    38 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    29 │    21 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    11 │    11 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    27 │    22 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    40 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    42 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    44 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   593 │   388 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5290 │  +781 ║ +1247 │  +781 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

#### Bitwise ([`BigIntBitwiseTest`][BigIntBitwiseTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═════════════╗
║                  ║ BigInteger  ║   BigInt    ║    int[]    ║
╠══════════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     testBit(int) ║   +0 │   +0 ║  +34 │  +37 ║  +39 │  +43 ║
║ toByteArray() BE ║   +0 │   +0 ║  +14 │ +449 ║   +9 │ +299 ║
║     flipBit(int) ║   +0 │  +86 ║ +394 │  +77 ║ +300 │   +0 ║
║      bitLength() ║   +0 │   +0 ║  +70 │  +41 ║  +44 │  +20 ║
║ toByteArray() LE ║   +0 │   +0 ║  +33 │ +399 ║  +15 │ +205 ║
║  shiftRight(int) ║   +0 │  +12 ║ +366 │  +13 ║ +194 │   +0 ║
║    clearBit(int) ║  +16 │   +0 ║  +11 │ +327 ║   +0 │ +190 ║
║   shiftLeft(int) ║   +0 │  +62 ║ +315 │  +51 ║ +184 │   +0 ║
╚══════════════════╩══════╧══════╩══════╧══════╩══════╧══════╝
```

```
[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║     testBit(int) ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
║ toByteArray() BE ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║     flipBit(int) ║    +0 │    +0 ║ +3118 │  +569 ║  +972 │  +569 ║
║      bitLength() ║    +0 │    +0 ║ +2954 │  +827 ║ +1427 │  +827 ║
║ toByteArray() LE ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║  shiftRight(int) ║    +0 │    +0 ║ +3118 │  +551 ║  +972 │  +551 ║
║    clearBit(int) ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║   shiftLeft(int) ║    +0 │    +0 ║ +3118 │  +554 ║  +972 │  +554 ║
║      setBit(int) ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║       bitCount() ║    +0 │    +0 ║ +2954 │  +831 ║ +1427 │  +831 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `testBit(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_test bit_</sup>

```
[runtime performance]
testBit(int)
  107377 in 561ms
╔═════════╦════════════╦════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║    8 │    0 ║   6 │   0 ║   6 │   0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   22 │    0 ║  16 │   0 ║  16 │   0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   40 │    0 ║  23 │   0 ║  28 │   0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   33 │    0 ║  20 │   0 ║  19 │   0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   40 │    0 ║  34 │   0 ║  33 │   0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   67 │   33 ║  54 │  24 ║  45 │  23 ║
║ 36 │    ║   25 │     ║  8348 │        ║   27 │    0 ║  26 │   0 ║  23 │   0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   24 │    0 ║  17 │   0 ║  16 │   0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   25 │    0 ║  18 │   0 ║  20 │   0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   16 │    0 ║  13 │   0 ║  12 │   0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║    9 │    0 ║   4 │   0 ║   5 │   0 ║
║    │    ║      │     ║       │   sum: ║  311 │   33 ║ 231 │  24 ║ 223 │  23 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +34 │ +37 ║ +39 │ +43 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡯⣉⠒⠒⠦⠤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⠤⠔⠒⠊⠉⢁⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠉⠒⠢⢄⡀⠀⠉⠙⠶⡒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣠⣤⣒⣒⡢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⢒⡪⠝⠛⠉⠉⠉⠙⠛⠵⢖⣒⠤⠤⠤⣒⡲⠞⠋⠉⣀⡠⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠈⠑⠤⡀⠀⠈⠑⠤⡀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠈⠉⠒⠫⢖⡤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⢁⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠈⠉⠑⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⡔⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠈⠢⠑⢄⠀⠀⠀⠀⠀⠀⠀⢀⠌⢀⡌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⡀⠑⡀⠢⣀⠀⠀⠀⠀⡠⠁⢀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠐⠄⠀⠑⠒⠒⠉⠀⢀⠂⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠈⢄⠀⠀⠀⠀⡠⠁⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠑⠤⠤⠊⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢄⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠒⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
testBit(int)
  54001 in 2770ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    40 │    23 ║     1 │     2 ║     2 │     2 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║    │    ║      │     ║      │  sum: ║   337 │   205 ║    11 │    22 ║    22 │    22 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `toByteArray()`<br>&nbsp;&nbsp;&nbsp;<sup>_to byte array (big endian)_</sup>

```
[runtime performance]
toByteArray()
  107377 in 446ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        103 ║     88 ║    95 ║
║     44 ║      -102 ║  9605 ║         77 ║     63 ║    72 ║
║     41 ║       -76 ║  9454 ║         78 ║     80 ║    72 ║
║     39 ║       -51 ║  9166 ║        107 ║     77 ║    77 ║
║     36 ║       -25 ║  8398 ║         78 ║     56 ║    57 ║
║     34 ║         0 ║  8523 ║         83 ║     77 ║    69 ║
║     36 ║        25 ║  8348 ║         70 ║     54 ║    58 ║
║     39 ║        51 ║  9191 ║         88 ║     81 ║    81 ║
║     41 ║        76 ║  9501 ║         60 ║     69 ║    68 ║
║     44 ║       102 ║  9693 ║         64 ║     56 ║    72 ║
║     47 ║       128 ║ 11735 ║         82 ║     76 ║    92 ║
║        ║           ║  sum: ║        890 ║    777 ║   813 ║
║        ║           ║   +%: ║         +0 ║    +14 ║    +9 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⣏⠭⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠤⠤⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
toByteArray()
  54001 in 2384ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `flipBit(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_flip bit_</sup>

```
[runtime performance]
flipBit(int)
  107377 in 680ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║  106 │    0 ║   12 │    0 ║   20 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║  113 │    0 ║   13 │    0 ║   21 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║  102 │    0 ║   15 │    0 ║   20 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   80 │    0 ║   17 │    0 ║   19 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   86 │    0 ║   17 │    0 ║   18 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   77 │   84 ║   22 │   17 ║   24 │   21 ║
║ 36 │    ║   25 │     ║  8348 │        ║   47 │    0 ║   12 │    0 ║   15 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   49 │    0 ║   11 │    0 ║   15 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   54 │    0 ║   10 │    0 ║   18 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   62 │    0 ║   11 │    0 ║   20 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   54 │    0 ║   11 │    0 ║   18 │    0 ║
║    │    ║      │     ║       │   sum: ║  830 │   84 ║  151 │   17 ║  208 │   21 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +449 │ +394 ║ +299 │ +300 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⡯⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠵⣒⠤⣀⣀⠀⠀⢀⣀⠤⣒⠭⠓⠊⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠭⠭⠥⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠒⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠉⠀⠀⠀⠀⠀⠀⠉⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠤⠤⠤⢄⣀⣀⡠⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠤⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
flipBit(int)
  54001 in 3031ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    42 │    24 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    41 │    24 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    39 │    24 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    22 │    15 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     7 │     6 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │  sum: ║   354 │   221 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +3118 │  +569 ║  +972 │  +569 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `bitLength()`<br>&nbsp;&nbsp;&nbsp;<sup>_bit length_</sup>

```
[runtime performance]
bitLength()
  107377 in 307ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          7 ║      4 ║    13 ║
║     44 ║      -102 ║  9605 ║          6 ║      3 ║    13 ║
║     41 ║       -76 ║  9454 ║          6 ║      3 ║    11 ║
║     39 ║       -51 ║  9166 ║          7 ║      4 ║    11 ║
║     36 ║       -25 ║  8398 ║          7 ║      6 ║    10 ║
║     34 ║         0 ║  8523 ║         45 ║     64 ║    65 ║
║     36 ║        25 ║  8348 ║          5 ║      6 ║     9 ║
║     39 ║        51 ║  9191 ║          2 ║      4 ║    11 ║
║     41 ║        76 ║  9501 ║          2 ║      2 ║    10 ║
║     44 ║       102 ║  9693 ║          4 ║      2 ║    13 ║
║     47 ║       128 ║ 11735 ║          5 ║      3 ║    13 ║
║        ║           ║  sum: ║         96 ║    101 ║   179 ║
║        ║           ║   +%: ║        +86 ║    +77 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡯⠭⠭⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠿⠯⢭⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡙⠩⢭⡭⢭⣭⠭⠭⠭⠭⠭⠝⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢁⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⢊⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠠⡡⠁⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠄⠡⡀⠀⠀⠀⠀⠀⠀⢀⠊⢀⠑⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢑⠀⠐⡀⠀⠀⠀⠀⢀⠂⠀⢄⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢅⠀⠈⠢⣀⣀⠔⠁⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢡⠀⠀⠀⠀⠀⠀⣈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠆⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡀⠀⠀⠀⠎⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⣀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
bitLength()
  54001 in 2411ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    38 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   336 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2954 │  +827 ║ +1427 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `toByteArray()`<br>&nbsp;&nbsp;&nbsp;<sup>_to byte array (little endian)_</sup>

```
[runtime performance]
toByteArray()
  107377 in 327ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        138 ║     72 ║    84 ║
║     44 ║      -102 ║  9605 ║        114 ║     59 ║    74 ║
║     41 ║       -76 ║  9454 ║         91 ║     53 ║    60 ║
║     39 ║       -51 ║  9166 ║         67 ║     39 ║    47 ║
║     36 ║       -25 ║  8398 ║         45 ║     29 ║    35 ║
║     34 ║         0 ║  8523 ║         38 ║     43 ║    43 ║
║     36 ║        25 ║  8348 ║         44 ║     30 ║    31 ║
║     39 ║        51 ║  9191 ║         57 ║     35 ║    45 ║
║     41 ║        76 ║  9501 ║         83 ║     46 ║    55 ║
║     44 ║       102 ║  9693 ║        100 ║     55 ║    68 ║
║     47 ║       128 ║ 11735 ║        122 ║     66 ║    79 ║
║        ║           ║  sum: ║        899 ║    527 ║   621 ║
║        ║           ║   +%: ║         +0 ║    +70 ║   +44 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⣏⣉⠭⠭⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠫⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠭⢍⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠒⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
toByteArray()
  54001 in 2374ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `shiftRight(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_shift right_</sup>

```
[runtime performance]
shiftRight(int)
  107377 in 521ms
╔═════════╦════════════╦════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   57 │    0 ║  41 │   0 ║  54 │   0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   58 │    0 ║  39 │   0 ║  48 │   0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   59 │    0 ║  39 │   0 ║  44 │   0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   56 │    0 ║  32 │   0 ║  34 │   0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   48 │    0 ║  27 │   0 ║  31 │   0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   39 │   52 ║  38 │  39 ║  37 │  45 ║
║ 36 │    ║   25 │     ║  8348 │        ║   33 │    0 ║  22 │   0 ║  26 │   0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   42 │    0 ║  27 │   0 ║  31 │   0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   45 │    0 ║  37 │   0 ║  41 │   0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   43 │    0 ║  33 │   0 ║  42 │   0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   46 │    0 ║  38 │   0 ║  49 │   0 ║
║    │    ║      │     ║       │   sum: ║  526 │   52 ║ 373 │  39 ║ 437 │  45 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +41 │ +33 ║ +20 │ +15 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠒⠉⠑⠢⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⣒⡪⠭⠥⠤⠤⠤⠤⠤⠤⠤⢌⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⡠⠔⠒⠒⠒⠢⠤⠤⠤⣀⣀⡀⠉⠒⠤⣀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠔⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⡐⠀⠀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠂⠀⠀⠀⠀⠀⠀⠀⠀⡐⠐⠀⡐⠉⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢉⠄⠀⠀⠀⠀⠀⠀⠠⠠⠁⡐⠀⠀⠀⠀⠀⠀⠈⠢⢄⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠒⠒⠒⠒⠛⠛⠓⠒⠲⠶⣢⣤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⢀⣀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠄⠀⠀⠀⠀⢀⠁⠂⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⣏⣁⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠉⠉⠢⡀⠀⠀⠈⡈⢄⠀⠀⠠⠂⠌⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠐⡀⠑⠒⠁⡐⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠐⣀⣀⠔⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⢀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
shiftRight(int)
  54001 in 2872ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    42 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    39 │    23 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    35 │    21 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    22 │    14 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     7 │     5 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │  sum: ║   354 │   215 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +3118 │  +551 ║  +972 │  +551 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `clearBit(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_clear bit_</sup>


```
[runtime performance]
clearBit(int)
  107377 in 526ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   85 │    0 ║   10 │    0 ║   19 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   72 │    0 ║   11 │    0 ║   18 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   62 │    0 ║   11 │    0 ║   18 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   54 │    0 ║   11 │    0 ║   16 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   61 │    0 ║   15 │    0 ║   18 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   60 │   56 ║   18 │   12 ║   20 │   19 ║
║ 36 │    ║   25 │     ║  8348 │        ║   36 │    0 ║   10 │    0 ║   15 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   28 │    0 ║    6 │    0 ║   14 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   33 │    0 ║    7 │    0 ║   15 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   39 │    0 ║    8 │    0 ║   17 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   44 │    0 ║    8 │    0 ║   18 │    0 ║
║    │    ║      │     ║       │   sum: ║  574 │   56 ║  115 │   12 ║  188 │   19 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +399 │ +366 ║ +205 │ +194 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⣉⣉⣁⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠢⢄⡉⠉⠑⠉⠉⠉⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
clearBit(int)
  54001 in 2913ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    42 │    25 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    41 │    24 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    39 │    24 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    22 │    15 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     7 │     6 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │  sum: ║   354 │   222 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `shiftLeft(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_shift left_</sup>

```
[runtime performance]
shiftLeft(int)
  107377 in 655ms
╔═════════╦════════════╦════════════════╦═════════════╦═══════════╦══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt   ║  int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤═════╬═════╤════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   53 │    0 ║  55 │   0 ║  61 │  0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   49 │    0 ║  46 │   0 ║  55 │  0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   38 │    0 ║  36 │   0 ║  44 │  0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   39 │    0 ║  38 │   0 ║  43 │  0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   30 │    0 ║  22 │   0 ║  23 │  0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   27 │   42 ║  47 │  44 ║  43 │ 49 ║
║ 36 │    ║   25 │     ║  8348 │        ║   29 │    0 ║  22 │   0 ║  24 │  0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   39 │    0 ║  37 │   0 ║  41 │  0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   39 │    0 ║  35 │   0 ║  44 │  0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   47 │    0 ║  45 │   0 ║  56 │  0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   51 │    0 ║  55 │   0 ║  63 │  0 ║
║    │    ║      │     ║       │   sum: ║  441 │   42 ║ 438 │  44 ║ 497 │ 49 ║
║    │    ║      │     ║       │    +%: ║  +12 │  +16 ║ +13 │ +11 ║  +0 │ +0 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧═════╩═════╧════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋⠁⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⡪⠤⠬⣑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢀⢔⠕⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠍⣀⣀⣀⣀⠑⠌⠢⣀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⣒⠭⠕⠒⠒⠒⠛⠛⠒⠒⠲⠒⠚⡳⠒⠊⠉⠉⠉⠑⠒⢄⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡖⠉⠀⠀⠀⠀⠉⠙⠲⣤⠭⠭⠭⠭⠤⠤⣀⣀⣀⣀⣀⡈⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⣔⣒⡩⠕⠒⠉⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⠢⡀⠀⠀⠀⠀⠀⠀⢀⠔⢑⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠛⠒⠤⣤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡯⠛⠋⠉⠉⠉⠉⠀⠀⠀⠀⢀⡠⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠈⠢⣀⡀⢀⣀⠔⠁⢀⠅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⢀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠀⠀⠀⠈⠁⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠀⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡄⠀⠀⠀⠀⠀⡊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠰⠀⠀⠀⠀⡨⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢣⡀⠀⣐⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠪⠮⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
shiftLeft(int)
  54001 in 3046ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    42 │    24 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    39 │    23 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    35 │    21 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    22 │    14 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     7 │     5 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │  sum: ║   354 │   216 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +3118 │  +554 ║  +972 │  +554 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `setBit(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_set bit_</sup>

```
[runtime performance]
setBit(int)
  107377 in 527ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   85 │    0 ║   12 │    0 ║   19 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   70 │    0 ║   11 │    0 ║   19 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   60 │    0 ║   11 │    0 ║   18 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   50 │    0 ║   11 │    0 ║   19 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   55 │    0 ║   16 │    0 ║   19 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   49 │   54 ║   16 │   13 ║   17 │   19 ║
║ 36 │    ║   25 │     ║  8348 │        ║   34 │    0 ║   12 │    0 ║   14 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   30 │    0 ║    8 │    0 ║   16 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   32 │    0 ║    9 │    0 ║   15 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   37 │    0 ║   10 │    0 ║   15 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   45 │    0 ║   12 │    0 ║   17 │    0 ║
║    │    ║      │     ║       │   sum: ║  547 │   54 ║  128 │   13 ║  188 │   19 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +327 │ +315 ║ +190 │ +184 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝

⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⢍⡩⢍⣉⣉⢍⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⢉⣉⣑⠶⠶⢖⣢⠤⢄⣀⣀⠀⠀⠀⢀⣀⡠⠤⢔⡲⠭⠝⠛⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣈⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠉⠉⠉⠁⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠢⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠢⠤⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⡀⣀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
setBit(int)
  54001 in 3125ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    42 │    25 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║ -102 │     ║ 4765 │       ║    41 │    24 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║  -76 │     ║ 4702 │       ║    39 │    24 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║  -51 │     ║ 4558 │       ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║  -25 │     ║ 4174 │       ║    22 │    15 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 53901 ║     7 │     6 ║     1 │     3 ║     3 │     3 ║
║ 36 │    ║   25 │     ║ 4124 │       ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 39 │    ║   51 │     ║ 4583 │       ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║ 41 │    ║   76 │     ║ 4713 │       ║    36 │    23 ║     1 │     3 ║     3 │     3 ║
║ 44 │    ║  102 │     ║ 4809 │       ║    38 │    23 ║     1 │     3 ║     3 │     3 ║
║ 47 │    ║  128 │     ║ 5855 │       ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │  sum: ║   354 │   222 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `bitCount(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_bit count_</sup>

```
[runtime performance]
bitCount()
  107377 in 300ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         14 ║     14 ║    24 ║
║     44 ║      -102 ║  9605 ║         11 ║     10 ║    20 ║
║     41 ║       -76 ║  9454 ║          9 ║      9 ║    21 ║
║     39 ║       -51 ║  9166 ║          7 ║      9 ║    15 ║
║     36 ║       -25 ║  8398 ║          7 ║      8 ║    11 ║
║     34 ║         0 ║  8523 ║         39 ║     45 ║    32 ║
║     36 ║        25 ║  8348 ║          3 ║      4 ║     9 ║
║     39 ║        51 ║  9191 ║          4 ║      5 ║    13 ║
║     41 ║        76 ║  9501 ║          8 ║      7 ║    18 ║
║     44 ║       102 ║  9693 ║          9 ║      8 ║    17 ║
║     47 ║       128 ║ 11735 ║         13 ║     14 ║    22 ║
║        ║           ║  sum: ║        124 ║    133 ║   202 ║
║        ║           ║   +%: ║        +62 ║    +51 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣩⣭⣭⣝⣛⣋⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⢋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢙⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠭⠭⠭⢍⣉⣉⣉⣉⣛⣛⣭⣍⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⢌⣂⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠅⣀⠤⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠚⠭⢕⡒⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠈⢎⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⡲⠊⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⢀⣜⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⣀⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠡⠢⣀⠀⠀⢀⠔⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⠒⠒⠊⠉⠁⠀⠀⠀⠉⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⢂⠀⠉⠉⠁⠠⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⢄⠀⠀⠠⠂⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠒⠒⠁⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠤⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
bitCount()
  54001 in 2517ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     5 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    38 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   336 │   205 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2954 │  +831 ║ +1427 │  +831 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

#### Predicate ([`BigIntPredicateTest`][BigIntPredicateTest])

**Summary**

```
[runtime performance]
╔═══════════════╦════════════╦════════╦═══════╗
║               ║ BigInteger ║ BigInt ║ int[] ║
╠═══════════════╬════════════╬════════╬═══════╣
║  floatValue() ║         +0 ║   +112 ║  +124 ║
║         abs() ║         +0 ║   +103 ║   +57 ║
║         neg() ║         +0 ║   +375 ║  -258 ║
║    byeValue() ║         +0 ║     +0 ║ -1700 ║
║    toString() ║         +0 ║   +288 ║  +297 ║
║   precision() ║         +0 ║  +2983 ║ +3405 ║
║   longValue() ║         +0 ║   +106 ║  +188 ║
║  shortValue() ║         +0 ║   +120 ║   +65 ║
║    hashCode() ║       +115 ║    +12 ║    +0 ║
║    intValue() ║         +6 ║     +0 ║  +108 ║
║ doubleValue() ║         +0 ║    +78 ║  +118 ║
╚═══════════════╩════════════╩════════╩═══════╝
```

```
[runtime performance]
╔══════════════╦══════════════╦═════════╦═══════════╗
║              ║  BigInteger  ║ BigInt  ║   int[]   ║
╠══════════════╬══════╤═══════╬════╤════╬═════╤═════╣
║       max(T) ║  +81 │   +88 ║ +0 │ +0 ║ +22 │ +20 ║
║       min(T) ║ +136 │  +142 ║ +0 │ +0 ║ +21 │ +21 ║
║ compareTo(T) ║ +160 │  +189 ║ +0 │ +0 ║  +9 │  +9 ║
║    equals(T) ║ +500 │ +2000 ║ +0 │ +0 ║ +10 │ +18 ║
╚══════════════╩══════╧═══════╩════╧════╩═════╧═════╝
```

```
[heap allocation]
╔═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║               ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║  floatValue() ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
║         abs() ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
║        max(T) ║    +0 │    +0 ║ +5181 │  +747 ║ +1220 │  +747 ║
║        min(T) ║    +0 │    +0 ║ +5236 │  +747 ║ +1234 │  +747 ║
║         neg() ║    +0 │    +0 ║ +3118 │  +521 ║  +972 │  +521 ║
║    byeValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║    toString() ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
║  compareTo(T) ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
║   precision() ║    +0 │    +0 ║ +4454 │ +1286 ║ +2177 │ +1286 ║
║   longValue() ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
║  shortValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║    hashCode() ║    +0 │    +0 ║ +2954 │  +827 ║ +1427 │  +827 ║
║    intValue() ║    +0 │    +0 ║ +2963 │  +836 ║ +1431 │  +836 ║
║     equals(T) ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
║ doubleValue() ║    +0 │    +0 ║ +2981 │  +827 ║ +1440 │  +827 ║
╚═══════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `floatValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_float value_</sup>

```
[runtime performance]
floatValue()
  107377 in 487ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          5 ║      5 ║     6 ║
║     44 ║      -102 ║  9605 ║          9 ║      6 ║    10 ║
║     41 ║       -76 ║  9454 ║         16 ║     16 ║    13 ║
║     39 ║       -51 ║  9166 ║         24 ║     20 ║    19 ║
║     36 ║       -25 ║  8398 ║         53 ║     29 ║    28 ║
║     34 ║         0 ║  8523 ║        241 ║     69 ║    60 ║
║     36 ║        25 ║  8348 ║         51 ║     27 ║    24 ║
║     39 ║        51 ║  9191 ║         20 ║     19 ║    17 ║
║     41 ║        76 ║  9501 ║         14 ║     11 ║     9 ║
║     44 ║       102 ║  9693 ║         12 ║      6 ║     9 ║
║     47 ║       128 ║ 11735 ║          6 ║      4 ║     6 ║
║        ║           ║  sum: ║        451 ║    212 ║   201 ║
║        ║           ║   +%: ║         +0 ║   +112 ║  +124 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠭⠭⠿⠿⠿⢿⣿⣿⠯⠭⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣩⣭⠭⠽⠿⠿⢿⣿⣟⠿⠿⠟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⢄⠀⠀⠀⠉⠙⠫⠶⢖⣒⣒⣒⠶⠝⠓⠉⠁⠀⠀⡠⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
floatValue()
  54001 in 2504ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    23 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   205 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `abs()`<br>&nbsp;&nbsp;&nbsp;<sup>_absolute value_</sup>

```
[runtime performance]
abs()
  107377 in 725ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          5 ║      2 ║     3 ║
║     44 ║      -102 ║  9605 ║         11 ║      0 ║     3 ║
║     41 ║       -76 ║  9454 ║          9 ║      1 ║     2 ║
║     39 ║       -51 ║  9166 ║         20 ║      6 ║     8 ║
║     36 ║       -25 ║  8398 ║         26 ║     11 ║    12 ║
║     34 ║         0 ║  8523 ║         18 ║     18 ║    17 ║
║     36 ║        25 ║  8348 ║         13 ║     11 ║    12 ║
║     39 ║        51 ║  9191 ║          7 ║      5 ║     7 ║
║     41 ║        76 ║  9501 ║          1 ║      0 ║     3 ║
║     44 ║       102 ║  9693 ║          0 ║      0 ║     2 ║
║     47 ║       128 ║ 11735 ║          0 ║      0 ║     1 ║
║        ║           ║  sum: ║        110 ║     54 ║    70 ║
║        ║           ║   +%: ║         +0 ║   +103 ║   +57 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⣉⠭⠝⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠝⡻⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡟⠛⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠤⡀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⢀⠔⣉⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠈⠑⠒⠒⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠀⢀⡠⠔⠋⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠤⠤⣀⣀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⢀⡤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠥⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⣀⠴⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠉⠢⢄⡀⠀⣀⠤⠊⠁⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠫⡑⠒⠢⠤⠤⠤⢤⣤⠾⠖⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⣠⡴⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
abs()
  54001 in 2498ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    13 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     6 │     4 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   343 │   204 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `max(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_max with `T`_</sup>

```
[runtime performance]
max(T)
  107377 in 611ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║    6 │   11 ║  12 │  20 ║  11 │  17 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   11 │   11 ║  16 │  22 ║  14 │  18 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   10 │   12 ║  18 │  20 ║  16 │  17 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   14 │   12 ║  22 │  19 ║  17 │  17 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   13 │   11 ║  21 │  21 ║  17 │  16 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   48 │   13 ║  82 │  27 ║  70 │  21 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   11 │    9 ║  20 │  19 ║  15 │  15 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   13 │   11 ║  24 │  21 ║  17 │  18 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    9 │   11 ║  22 │  20 ║  16 │  18 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    8 │   10 ║  20 │  21 ║  15 │  18 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    6 │   12 ║  13 │  22 ║  13 │  18 ║
║    │    ║      │     ║       │  sum: ║  149 │  123 ║ 270 │ 232 ║ 221 │ 193 ║
║    │    ║      │     ║       │   +%: ║  +81 │  +88 ║  +0 │  +0 ║ +22 │ +20 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡟⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠉⠑⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠈⠉⠉⠒⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⣀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣏⡉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠑⡀⠀⠀⠑⢄⡀⠀⠀⢀⡠⠊⠀⠀⢀⠊⠁⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠈⠉⠑⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⠀⠀⠈⠄⠀⠀⠀⠈⠑⠒⠁⠀⠀⠀⠠⠁⠀⡠⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⡀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⢀⠊⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠠⠀⠀⠀⠀⠀⠀⠌⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠡⡀⠀⠀⠀⠌⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠐⠤⠤⠊⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⣀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
max(T)
  54001 in 4087ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    72 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   581 │   373 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5181 │  +747 ║ +1220 │  +747 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `min(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_min with `T`_</sup>

```
[runtime performance]
min(T)
  107377 in 559ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║    5 │    7 ║  12 │  11 ║  10 │  10 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    5 │    6 ║   9 │  14 ║   7 │  10 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    5 │    7 ║  11 │  12 ║   8 │  10 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║    4 │    4 ║   9 │  10 ║   9 │   9 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    6 │    4 ║  14 │   9 ║  11 │   8 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   10 │    3 ║  19 │  11 ║  18 │  10 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    6 │    3 ║  11 │  10 ║   9 │   9 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    2 │    3 ║  11 │  12 ║   9 │   9 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    4 │    5 ║  11 │  12 ║   9 │   9 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    4 │    5 ║  10 │  13 ║   7 │  10 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    4 │    5 ║  13 │  12 ║  10 │  10 ║
║    │    ║      │     ║       │  sum: ║   55 │   52 ║ 130 │ 126 ║ 107 │ 104 ║
║    │    ║      │     ║       │   +%: ║ +136 │ +142 ║  +0 │  +0 ║ +21 │ +21 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠉⠉⠉⠉⠙⠫⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠢⠔⠤⠤⠢⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⣀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⠤⠔⠒⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠤⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢢⣤⣀⣀⡤⠔⠊⠉⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⡠⠁⢀⡠⠒⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠈⢄⠀⠀⠀⠀⢀⠔⠀⡠⠂⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠁⠉⠉⠈⠈⠉⠉⠉⠉⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠑⢄⣀⠤⠂⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
min(T)
  54001 in 4145ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    68 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    11 │     9 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    58 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   587 │   373 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5236 │  +747 ║ +1234 │  +747 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `neg()`<br>&nbsp;&nbsp;&nbsp;<sup>_negate_</sup>

```
[runtime performance]
neg()
  107377 in 485ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          1 ║      1 ║    -2 ║
║     44 ║      -102 ║  9605 ║          1 ║      0 ║    -3 ║
║     41 ║       -76 ║  9454 ║          0 ║      0 ║    -1 ║
║     39 ║       -51 ║  9166 ║          2 ║      0 ║     0 ║
║     36 ║       -25 ║  8398 ║          2 ║      1 ║     0 ║
║     34 ║         0 ║  8523 ║          7 ║      2 ║     1 ║
║     36 ║        25 ║  8348 ║          4 ║      0 ║     0 ║
║     39 ║        51 ║  9191 ║          0 ║     -1 ║     0 ║
║     41 ║        76 ║  9501 ║          0 ║      0 ║    -2 ║
║     44 ║       102 ║  9693 ║          0 ║      0 ║    -3 ║
║     47 ║       128 ║ 11735 ║          2 ║      1 ║    -2 ║
║        ║           ║  sum: ║         19 ║      4 ║   -12 ║
║        ║           ║   +%: ║         +0 ║   +375 ║  -258 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⡤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣤⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠑⠒⢒⡢⠤⢤⣤⡤⢄⣀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⢒⡶⠶⠶⣛⠋⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠈⠉⠒⠛⢓⠶⢖⠶⡊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡲⠋⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⣀⣀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠑⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠈⠪⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠣⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
neg()
  54001 in 2562ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    13 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     7 │     4 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    22 │    14 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    39 │    22 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   354 │   205 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3118 │  +521 ║  +972 │  +521 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `byteValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_byte value_</sup>

```
[runtime performance]
byeValue()
  107377 in 289ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          0 ║      0 ║    -1 ║
║     44 ║      -102 ║  9605 ║          0 ║      0 ║    -2 ║
║     41 ║       -76 ║  9454 ║          1 ║      0 ║    -1 ║
║     39 ║       -51 ║  9166 ║          0 ║      0 ║    -2 ║
║     36 ║       -25 ║  8398 ║          4 ║      5 ║     4 ║
║     34 ║         0 ║  8523 ║          9 ║      9 ║     6 ║
║     36 ║        25 ║  8348 ║          1 ║      2 ║     1 ║
║     39 ║        51 ║  9191 ║          0 ║      0 ║    -2 ║
║     41 ║        76 ║  9501 ║          0 ║      0 ║    -1 ║
║     44 ║       102 ║  9693 ║          0 ║      0 ║    -2 ║
║     47 ║       128 ║ 11735 ║          1 ║      0 ║    -1 ║
║        ║           ║  sum: ║         16 ║     16 ║    -1 ║
║        ║           ║   +%: ║         +0 ║     +0 ║ -1700 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⡩⠛⠉⠉⠙⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠙⠍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡛⠉⠉⠉⢋⠉⠉⠉⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠋⠉⠉⠙⠭⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠢⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⢀⠔⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠂⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠥⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠬⠶⠤⠴⠥⠤⠤⠤⠤⠤⠤⠤⣀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠈⠀⡠⠤⠤⠤⠤⠤⠤⠬⠶⠤⠤⠤⠤⠤⠤⠬⠶⠤⠴⠮⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠵⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠈⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠁⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢔⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠈⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠱⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⢁⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⡢⡀⠀⠀⠀⠀⡠⠁⢀⠑⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡌⠑⠒⠒⠊⠀⠀⠤⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢆⠀⠀⠀⠀⢐⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢣⣀⠀⣠⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
byeValue()
  54001 in 2346ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `toString()`<br>&nbsp;&nbsp;&nbsp;<sup>_to string_</sup>

```
[runtime performance]
toString()
  107377 in 485ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║       2211 ║    528 ║   516 ║
║     44 ║      -102 ║  9605 ║       1609 ║    405 ║   394 ║
║     41 ║       -76 ║  9454 ║       1127 ║    295 ║   283 ║
║     39 ║       -51 ║  9166 ║        661 ║    193 ║   188 ║
║     36 ║       -25 ║  8398 ║        303 ║    112 ║   110 ║
║     34 ║         0 ║  8523 ║        125 ║     57 ║    54 ║
║     36 ║        25 ║  8348 ║        318 ║    117 ║   118 ║
║     39 ║        51 ║  9191 ║        708 ║    205 ║   196 ║
║     41 ║        76 ║  9501 ║       1175 ║    301 ║   295 ║
║     44 ║       102 ║  9693 ║       1723 ║    417 ║   409 ║
║     47 ║       128 ║ 11735 ║       2337 ║    536 ║   529 ║
║        ║           ║  sum: ║      12297 ║   3166 ║  3092 ║
║        ║           ║   +%: ║         +0 ║   +288 ║  +297 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣩⣭⡭⠭⠭⠭⠭⠿⠟⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠭⠝⠛⠛⠛⠛⠫⠭⢍⡉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠿⠯⠭⠭⠭⠭⠭⣭⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
toString()
  54001 in 2515ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    13 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     6 │     4 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   343 │   204 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `compareTo(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_compare to `T`_</sup>

```
[runtime performance]
compareTo(T)
  107377 in 405ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║    4 │    5 ║  14 │  16 ║  13 │  13 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    4 │    7 ║  13 │  15 ║  11 │  13 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    3 │    7 ║  12 │  15 ║  10 │  13 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║    3 │    4 ║  13 │  14 ║  11 │  12 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    7 │    4 ║  14 │  15 ║  12 │  13 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   30 │    5 ║  43 │  16 ║  43 │  15 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    5 │    2 ║  13 │  14 ║  13 │  13 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    2 │    4 ║  14 │  14 ║  13 │  13 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    2 │    4 ║  13 │  14 ║  13 │  14 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    5 │    7 ║  15 │  14 ║  11 │  15 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    4 │    7 ║  16 │  15 ║  14 │  14 ║
║    │    ║      │     ║       │  sum: ║   69 │   56 ║ 180 │ 162 ║ 164 │ 148 ║
║    │    ║      │     ║       │   +%: ║ +160 │ +189 ║  +0 │  +0 ║  +9 │  +9 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡯⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⡠⠤⠤⠤⠤⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠢⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠔⠒⠒⠊⠉⠉⠉⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⡀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠤⠶⠶⠦⠤⣤⠤⠤⠤⠤⠤⠤⠤⣤⣤⠤⠤⠤⠖⠒⠛⠊⠉⠉⠉⠑⠒⠒⠤⢌⣉⠒⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠊⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠁⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡠⠀⠀⠀⠀⠀⠀⠀⠀⠀⡚⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣁⠀⠀⠀⠀⠀⠀⠀⡜⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢦⠀⠀⢀⠎⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
compareTo(T)
  54001 in 3934ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     3 ║     3 │     3 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    72 │    43 ║     1 │     3 ║     3 │     3 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    57 │    37 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    27 │    19 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     3 ║     3 │     3 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     3 ║     3 │     3 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     3 ║     3 │     3 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │ sum: ║   579 │   373 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `precision()`<br>&nbsp;&nbsp;&nbsp;<sup>_decimal precision_</sup>

```
[runtime performance]
precision()
  107377 in 321ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        414 ║      9 ║     8 ║
║     44 ║      -102 ║  9605 ║        386 ║      8 ║     7 ║
║     41 ║       -76 ║  9454 ║        352 ║      9 ║     8 ║
║     39 ║       -51 ║  9166 ║        310 ║      8 ║     8 ║
║     36 ║       -25 ║  8398 ║        175 ║     11 ║    10 ║
║     34 ║         0 ║  8523 ║         43 ║     17 ║    21 ║
║     36 ║        25 ║  8348 ║        191 ║     10 ║     8 ║
║     39 ║        51 ║  9191 ║        308 ║     11 ║     8 ║
║     41 ║        76 ║  9501 ║        351 ║      9 ║     6 ║
║     44 ║       102 ║  9693 ║        389 ║      8 ║     5 ║
║     47 ║       128 ║ 11735 ║        411 ║      8 ║     6 ║
║        ║           ║  sum: ║       3330 ║    108 ║    95 ║
║        ║           ║   +%: ║         +0 ║  +2983 ║ +3405 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠒⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠤⠔⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
precision()
  54001 in 3179ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    60 │    33 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    58 │    33 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    55 │    33 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    50 │    30 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    29 │    19 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     8 │     6 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    30 │    21 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    47 │    30 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    52 │    33 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    55 │    33 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    57 │    34 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   501 │   305 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +4454 │ +1286 ║ +2177 │ +1286 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `longValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_long value_</sup>

```
[runtime performance]
longValue()
  107377 in 290ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         14 ║      4 ║     3 ║
║     44 ║      -102 ║  9605 ║         11 ║      3 ║     1 ║
║     41 ║       -76 ║  9454 ║          8 ║      4 ║     1 ║
║     39 ║       -51 ║  9166 ║          8 ║      4 ║     2 ║
║     36 ║       -25 ║  8398 ║         12 ║      8 ║     6 ║
║     34 ║         0 ║  8523 ║         13 ║     10 ║    12 ║
║     36 ║        25 ║  8348 ║         10 ║      5 ║     5 ║
║     39 ║        51 ║  9191 ║          6 ║      3 ║     1 ║
║     41 ║        76 ║  9501 ║          6 ║      2 ║     0 ║
║     44 ║       102 ║  9693 ║          5 ║      2 ║     2 ║
║     47 ║       128 ║ 11735 ║          8 ║      4 ║     2 ║
║        ║           ║  sum: ║        101 ║     49 ║    35 ║
║        ║           ║   +%: ║         +0 ║   +106 ║  +188 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⣉⠭⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠫⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠔⠊⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⡠⠔⠒⠉⠉⠈⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠲⠚⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠑⠒⠒⠒⠑⠒⠒⠒⠑⠒⠚⠙⠒⠒⠲⢖⡊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⡀⠀⠀⠀⠀⡀⠀⡀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠈⠉⠈⠉⠉⠉⠒⢄⡀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠂⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠤⣀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⡐⠁⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢦⣀⣀⣀⣀⠤⠊⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⢄⣀⣀⣀⣀⡠⠤⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠉⠉⠉⠉⠉⠉⠈⠉⠈⠉⠉⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⡀⣀⢔⠀⠀⠀⠀⢀⠀⢀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⠀⣀⣀⠤⠔⠊⠉⠉⠉⠁⠉⠉⠉⠉⠁⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
longValue()
  54001 in 2361ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     5 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   205 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `shortValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_short value_</sup>

```
[runtime performance]
shortValue()
  107377 in 289ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          3 ║      0 ║     0 ║
║     44 ║      -102 ║  9605 ║          4 ║      0 ║     1 ║
║     41 ║       -76 ║  9454 ║          4 ║      0 ║     1 ║
║     39 ║       -51 ║  9166 ║          2 ║      0 ║     0 ║
║     36 ║       -25 ║  8398 ║          3 ║      2 ║     3 ║
║     34 ║         0 ║  8523 ║          7 ║      9 ║    13 ║
║     36 ║        25 ║  8348 ║          2 ║      3 ║     1 ║
║     39 ║        51 ║  9191 ║          1 ║      0 ║     0 ║
║     41 ║        76 ║  9501 ║          2 ║      1 ║     1 ║
║     44 ║       102 ║  9693 ║          4 ║      0 ║     0 ║
║     47 ║       128 ║ 11735 ║          1 ║      0 ║     0 ║
║        ║           ║  sum: ║         33 ║     15 ║    20 ║
║        ║           ║   +%: ║         +0 ║   +120 ║   +65 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢯⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠍⠉⠉⠉⠉⣩⠽⠋⠉⠉⠉⠙⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠍⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠳⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⢀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠤⠤⠤⠤⠤⠬⠷⣤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⣠⠔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡡⡈⠒⢄⡀⠀⠀⠀⠀⠀⢀⡠⠖⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠈⢄⠀⠈⠑⠒⠒⠒⠊⠁⠰⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠡⡀⠀⠀⠀⠀⢀⠔⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠈⠢⠤⠤⠔⠁⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
shortValue()
  54001 in 2374ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `hashCode()`<br>&nbsp;&nbsp;&nbsp;<sup>_hash code_</sup>

```
[runtime performance]
hashCode()
  107377 in 290ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          5 ║      6 ║     6 ║
║     44 ║      -102 ║  9605 ║          1 ║      4 ║     3 ║
║     41 ║       -76 ║  9454 ║          1 ║      3 ║     2 ║
║     39 ║       -51 ║  9166 ║          0 ║      2 ║     2 ║
║     36 ║       -25 ║  8398 ║          5 ║      7 ║     8 ║
║     34 ║         0 ║  8523 ║          5 ║      9 ║    16 ║
║     36 ║        25 ║  8348 ║          1 ║      4 ║     4 ║
║     39 ║        51 ║  9191 ║          0 ║      2 ║     2 ║
║     41 ║        76 ║  9501 ║          1 ║      3 ║     3 ║
║     44 ║       102 ║  9693 ║          2 ║      4 ║     4 ║
║     47 ║       128 ║ 11735 ║          5 ║      6 ║     6 ║
║        ║           ║  sum: ║         26 ║     50 ║    56 ║
║        ║           ║   +%: ║       +115 ║    +12 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⡩⠝⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⣀⠔⠉⠀⠀⠀⠀⢀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⢀⡠⠒⠉⠁⠀⠀⠀⢈⡩⠝⠛⠓⠒⠒⠒⠒⠒⠒⠒⠑⠒⠲⣤⣀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢔⠶⠖⠒⠒⠒⠒⠒⠒⠑⠒⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⡠⠒⠁⠀⠀⠀⢀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠕⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠛⠒⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠉⠑⠒⠤⠤⣀⣀⣀⣀⣀⣀⠤⠤⠒⠒⡙⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
hashCode()
  54001 in 2454ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    38 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   336 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2954 │  +827 ║ +1427 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `intValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_int value_</sup>

```
[runtime performance]
intValue()
  107377 in 291ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          4 ║      0 ║     0 ║
║     44 ║      -102 ║  9605 ║          2 ║      0 ║    -1 ║
║     41 ║       -76 ║  9454 ║          3 ║      0 ║     0 ║
║     39 ║       -51 ║  9166 ║          5 ║      1 ║     1 ║
║     36 ║       -25 ║  8398 ║          3 ║      4 ║     4 ║
║     34 ║         0 ║  8523 ║         24 ║     44 ║    19 ║
║     36 ║        25 ║  8348 ║          2 ║      3 ║     3 ║
║     39 ║        51 ║  9191 ║          3 ║      0 ║     0 ║
║     41 ║        76 ║  9501 ║          1 ║      0 ║     0 ║
║     44 ║       102 ║  9693 ║          1 ║      0 ║     0 ║
║     47 ║       128 ║ 11735 ║          1 ║      0 ║    -1 ║
║        ║           ║  sum: ║         49 ║     52 ║    25 ║
║        ║           ║   +%: ║         +6 ║     +0 ║  +108 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠙⢫⣍⡿⢭⠭⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣩⣯⣛⣛⣉⣉⣉⣉⣉⣉⠭⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⠤⠒⠊⠁⠀⠈⠑⠳⡬⣡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠊⡑⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠉⢒⠤⣀⠀⠀⠀⣀⡠⠒⡡⠁⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠡⡀⠉⠉⠉⠀⢀⠔⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠈⠢⣀⣀⡠⠂⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
intValue()
  54001 in 2402ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    40 │    23 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    33 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     5 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   337 │   206 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2963 │  +836 ║ +1431 │  +836 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `equals(T)`<br>&nbsp;&nbsp;&nbsp;<sup>_equals `T`_</sup>

```
[runtime performance]
equals(T)
  107377 in 401ms
╔═════════╦════════════╦═══════════════╦══════════════╦═════════╦═══════════╗
║ length  ║ precision  ║     count     ║  BigInteger  ║ BigInt  ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤═══════╬════╤════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║    0 │     0 ║  6 │  5 ║   5 │   4 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    0 │     1 ║  6 │  6 ║   5 │   6 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    0 │     1 ║  6 │  6 ║   5 │   5 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   -1 │     0 ║  4 │  6 ║   3 │   5 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    3 │     0 ║  8 │  5 ║   6 │   4 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║    6 │     0 ║  9 │  8 ║  12 │   6 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    2 │     0 ║  6 │  5 ║   4 │   4 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    0 │     0 ║  4 │  6 ║   3 │   5 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    0 │     0 ║  6 │  6 ║   6 │   5 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    0 │     0 ║  5 │  5 ║   5 │   4 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    1 │     1 ║  6 │  5 ║   6 │   5 ║
║    │    ║      │     ║       │  sum: ║   11 │     3 ║ 66 │ 63 ║  60 │  53 ║
║    │    ║      │     ║       │   +%: ║ +500 │ +2000 ║ +0 │ +0 ║ +10 │ +18 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧═══════╩════╧════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠭⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠝⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠒⠢⠤⣀⣀⣀⡀⡀⠀⣀⠀⠀⠀⠀⢀⣀⣀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠢⠤⣀⣀⣀⣀⣀⠤⠔⠊⠉⠁⠀⠉⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠈⠈⠉⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠢⠤⢄⣀⣀⣀⣀⠀⠀⢀⠀⠀⢀⠀⢀⣀⣀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠢⠤⢄⣀⣀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⢀⣌⠤⠒⠒⠒⠒⠒⠊⠒⠒⠊⠒⠒⠒⠒⠢⠤⢄⣀⣀⣀⣀⣀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠉⠑⠋⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠉⠉⠁⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠖⠤⠤⠤⠤⠒⠊⠍⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
equals(T)
  54001 in 4115ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     3 ║     3 │     3 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    72 │    43 ║     1 │     3 ║     3 │     3 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    57 │    37 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    27 │    19 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     3 ║     3 │     3 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     3 ║     3 │     3 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     3 ║     3 │     3 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     1 │     3 ║     3 │     3 ║
║    │    ║      │     ║      │ sum: ║   579 │   373 ║    11 │    33 ║    33 │    33 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `doubleValue()`<br>&nbsp;&nbsp;&nbsp;<sup>_double value_</sup>

```
[runtime performance]
doubleValue()
  107377 in 307ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         10 ║      2 ║     2 ║
║     44 ║      -102 ║  9605 ║          9 ║      3 ║     2 ║
║     41 ║       -76 ║  9454 ║         11 ║      3 ║     2 ║
║     39 ║       -51 ║  9166 ║         19 ║     10 ║     7 ║
║     36 ║       -25 ║  8398 ║         31 ║     17 ║    17 ║
║     34 ║         0 ║  8523 ║         90 ║     78 ║    65 ║
║     36 ║        25 ║  8348 ║         34 ║     20 ║    15 ║
║     39 ║        51 ║  9191 ║         22 ║      7 ║     5 ║
║     41 ║        76 ║  9501 ║         15 ║      2 ║     2 ║
║     44 ║       102 ║  9693 ║         11 ║      2 ║     1 ║
║     47 ║       128 ║ 11735 ║         10 ║      3 ║     2 ║
║        ║           ║  sum: ║        262 ║    147 ║   120 ║
║        ║           ║   +%: ║         +0 ║    +78 ║  +118 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠫⢍⣉⠛⠛⠛⠫⠭⠭⢍⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠭⠭⠛⠛⣛⣛⡻⠿⠟⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠔⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠤⠤⢄⣀⡀⠀⠀⠉⠉⠉⠉⠉⠉⠑⠒⠛⠒⢤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⡠⠔⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠑⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⡠⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠤⡀⠀⠀⠘⡢⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠔⠀⠀⠀⠀⠀⣀⣀⣀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠠⠑⢄⠀⠀⠀⠀⠠⠊⠀⠌⠀⠀⡠⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠡⠀⠑⠤⠤⠒⠁⠀⠌⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠡⠀⠀⠀⠀⠀⠌⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠡⡀⠀⡠⠊⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠈⠉⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
doubleValue()
  54001 in 2590ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║      -102 ║  4765 ║    39 │    22 ║     1 │     2 ║     2 │     2 ║
║     41 ║       -76 ║  4702 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  4174 ║    20 │    13 ║     1 │     2 ║     2 │     2 ║
║     34 ║         0 ║  4299 ║     5 │     4 ║     1 │     2 ║     2 │     2 ║
║     36 ║        25 ║  4124 ║    20 │    14 ║     1 │     2 ║     2 │     2 ║
║     39 ║        51 ║  4583 ║    32 │    20 ║     1 │     2 ║     2 │     2 ║
║     41 ║        76 ║  4713 ║    35 │    22 ║     1 │     2 ║     2 │     2 ║
║     44 ║       102 ║  4809 ║    37 │    22 ║     1 │     2 ║     2 │     2 ║
║     47 ║       128 ║  5855 ║    39 │    23 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   339 │   204 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2981 │  +827 ║ +1440 │  +827 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

#### Constructor ([`BigIntConstructorTest`][BigIntConstructorTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═══════════╦═══════════╗
║                  ║ BigInteger  ║  BigInt   ║   int[]   ║
╠══════════════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ <init>(int,long) ║   +0 │   +0 ║ +52 │ +39 ║ +79 │ +67 ║
║  <init>(int,int) ║  +63 │  +68 ║  +0 │  +0 ║ +12 │ +12 ║
║      <init>(int) ║ +319 │ +319 ║  +0 │  +0 ║  +8 │  +9 ║
╚══════════════════╩══════╧══════╩═════╧═════╩═════╧═════╝
```

```
[runtime performance]
╔════════════════╦════════════╦════════╦═══════╗
║                ║ BigInteger ║ BigInt ║ int[] ║
╠════════════════╬════════════╬════════╬═══════╣
║   <init>(long) ║       +182 ║     +0 ║    +0 ║
║ <init>(byte[]) ║        +13 ║     +0 ║   +15 ║
║ <init>(String) ║         +0 ║   +200 ║  +231 ║
║ <init>(byte[]) ║        +10 ║     +0 ║    +2 ║
╚════════════════╩════════════╩════════╩═══════╝
```

```
[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ <init>(int,long) ║    +0 │    +0 ║    +∞ │    +0 ║   +25 │    +0 ║
║     <init>(long) ║   +57 │  +144 ║    +∞ │    +0 ║    +0 │    +0 ║
║  <init>(int,int) ║  +300 │    +∞ ║    +∞ │    +0 ║    +0 │    +0 ║
║   <init>(byte[]) ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
║   <init>(String) ║    +0 │    +0 ║    +∞ │  +709 ║ +1109 │  +709 ║
║      <init>(int) ║   +71 │  +166 ║    +∞ │    +0 ║    +0 │    +0 ║
║   <init>(byte[]) ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `<init>(int,long)`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from unsigned `long`_</sup>

```
[runtime performance]
<init>(int,long)
  289329 in 477ms
╔═════════╦═══════════╦════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║     count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║    │ 36 ║     │ -19 ║        │ 37923 ║    0 │   76 ║   0 │  31 ║   0 │  27 ║
║    │ 35 ║     │ -15 ║        │ 22700 ║    0 │   79 ║   0 │  33 ║   0 │  28 ║
║    │ 35 ║     │ -11 ║        │ 30346 ║    0 │   88 ║   0 │  40 ║   0 │  38 ║
║    │ 34 ║     │  -7 ║        │ 22723 ║    0 │   90 ║   0 │  35 ║   0 │  32 ║
║ 34 │ 34 ║  -3 │  -3 ║ 178208 │ 30484 ║   56 │  142 ║  39 │  55 ║  32 │  41 ║
║ 34 │ 34 ║   0 │   0 ║ 110921 │ 15284 ║   66 │   27 ║  41 │  62 ║  36 │  45 ║
║    │ 34 ║     │   3 ║        │ 30346 ║    0 │   22 ║   0 │  33 ║   0 │  29 ║
║    │ 34 ║     │   7 ║        │ 22700 ║    0 │   22 ║   0 │  41 ║   0 │  34 ║
║    │ 35 ║     │  11 ║        │ 30300 ║    0 │   19 ║   0 │  38 ║   0 │  31 ║
║    │ 35 ║     │  15 ║        │ 22700 ║    0 │   17 ║   0 │  34 ║   0 │  24 ║
║    │ 36 ║     │  19 ║        │ 22723 ║    0 │   16 ║   0 │  27 ║   0 │  28 ║
║    │    ║     │     ║        │  sum: ║  122 │  598 ║  80 │ 429 ║  68 │ 357 ║
║    │    ║     │     ║        │   +%: ║   +0 │   +0 ║ +52 │ +39 ║ +79 │ +67 ║
╚════╧════╩═════╧═════╩════════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⣧⣤⣤⣤⣤⣤⣤⣤⣤⣤⠤⠤⣀⣀⠀⠀⠀⠀⠀⢀⣀⣀⣤⣤⣤⣄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠛⠛⠛⠛⠋⠉⠁⠀⠀⠀⠀⠈⠒⢌⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⡠⢒⠭⠭⠭⠵⣒⠢⠤⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠒⠒⠊⠉⠉⠀⠀⠀⣀⣈⡩⠭⠕⠒⠚⠛⠛⠛⠛⠛⠛⠛⢻
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠉⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠕⢉⠔⠁⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠑⠢⣀⡀⠀⠀⣀⠜⠁⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠈⠉⢉⠊⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠠⠂⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠲⠓⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(int,long)
  7749 in 291ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║    │ 36 ║     │ -19 ║      │  873 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │ -15 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │ -11 ║      │  706 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -7 ║      │  493 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 4708 │  844 ║     3 │     2 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 2841 │  464 ║     2 │     2 ║     0 │     2 ║     2 │     2 ║
║    │ 34 ║     │   3 ║      │  706 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   7 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  11 ║      │  660 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  15 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 36 ║     │  19 ║      │  493 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║      │ sum: ║     5 │     4 ║     0 │     4 ║     4 │     4 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║    +∞ │    +0 ║   +25 │    +0 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `<init>(long)`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from signed `long`_</sup>

```
[runtime performance]
<init>(long)
  289329 in 329ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     36 ║       -19 ║ 37923 ║         10 ║     37 ║    34 ║
║     35 ║       -15 ║ 22700 ║         32 ║     88 ║    89 ║
║     35 ║       -11 ║ 30346 ║         19 ║     53 ║    55 ║
║     34 ║        -7 ║ 22723 ║          7 ║     20 ║    21 ║
║     34 ║        -3 ║ 30484 ║         15 ║     37 ║    34 ║
║     34 ║         0 ║ 15284 ║         23 ║     48 ║    50 ║
║     34 ║         3 ║ 30346 ║          7 ║     20 ║    19 ║
║     34 ║         7 ║ 22700 ║          7 ║     25 ║    25 ║
║     35 ║        11 ║ 30300 ║         29 ║     81 ║    84 ║
║     35 ║        15 ║ 22700 ║         23 ║     68 ║    67 ║
║     36 ║        19 ║ 22723 ║          4 ║     20 ║    18 ║
║        ║           ║  sum: ║        176 ║    497 ║   496 ║
║        ║           ║   +%: ║       +182 ║     +0 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡟⠭⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠛⠛⠉⠉⠉⠉⠉⠉⠙⠛⠫⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠉⠒⠢⢄⣀⣀⣀⣀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢔⡲⠭⠝⠛⠛⠶⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠶⠶⠶⠶⠶⠶⠶⢾
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠈⠙⢖⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠋⠁⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠂⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠈⠫⢖⡤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠴⠁⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡯⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠳⢤⣀⠀⠀⠀⠀⣠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡬⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠘⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠭⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠍⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢣⡀⠀⠀⠀⠀⠀⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡡⣀⠀⠀⠀⠀⠀⣠⠞⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠑⢄⣀⣀⡤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣉⣒⡲⠖⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(long)
  7749 in 80ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     36 ║       -19 ║   873 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║     35 ║       -15 ║   470 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║     35 ║       -11 ║   706 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║     34 ║        -7 ║   493 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║     34 ║        -3 ║   844 ║     1 │     0 ║     0 │     2 ║     2 │     2 ║
║     34 ║         0 ║   464 ║     0 │     0 ║     0 │     2 ║     2 │     2 ║
║     34 ║         3 ║   706 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║     34 ║         7 ║   470 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║     35 ║        11 ║   660 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║     35 ║        15 ║   470 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║     36 ║        19 ║   493 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║    14 │     9 ║     0 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║   +57 │  +144 ║    +∞ │    +0 ║    +0 │    +0 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `<init>(int,int)`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from unsigned `int`_</sup>

```
[runtime performance]
<init>(int,int)
  800441 in 817ms
╔═════════╦═══════════╦═════════════════╦═════════════╦══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt  ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬════╤═════╬═════╤═════╣
║    │ 35 ║     │ -10 ║        │ 119921 ║    0 │   16 ║  0 │  28 ║   0 │  26 ║
║    │ 34 ║     │  -8 ║        │  79900 ║    0 │   19 ║  0 │  28 ║   0 │  25 ║
║    │ 34 ║     │  -6 ║        │  79921 ║    0 │   19 ║  0 │  29 ║   0 │  27 ║
║    │ 34 ║     │  -4 ║        │  79984 ║    0 │   19 ║  0 │  31 ║   0 │  28 ║
║ 34 │ 34 ║  -2 │  -2 ║ 396130 │  40005 ║   19 │   24 ║ 31 │  41 ║  28 │  36 ║
║ 34 │ 34 ║   0 │   0 ║ 404111 │  39984 ║   19 │    9 ║ 31 │  39 ║  27 │  35 ║
║    │ 34 ║     │   2 ║        │  79984 ║    0 │   20 ║  0 │  32 ║   0 │  27 ║
║    │ 34 ║     │   4 ║        │  79921 ║    0 │   20 ║  0 │  32 ║   0 │  27 ║
║    │ 34 ║     │   6 ║        │  79900 ║    0 │   20 ║  0 │  27 ║   0 │  25 ║
║    │ 34 ║     │   8 ║        │  79900 ║    0 │   19 ║  0 │  29 ║   0 │  25 ║
║    │ 35 ║     │  10 ║        │  39921 ║    0 │   20 ║  0 │  30 ║   0 │  27 ║
║    │    ║     │     ║        │   sum: ║   38 │  205 ║ 62 │ 346 ║  55 │ 308 ║
║    │    ║     │     ║        │    +%: ║  +63 │  +68 ║ +0 │  +0 ║ +12 │ +12 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩════╧═════╩═════╧═════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⠒⠢⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⠤⠤⠒⠒⠒⠒⠒⠒⠒⠤⠤⣀⣀⠀⠀⠀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠁⠀⢀⡠⠤⠤⠤⣀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣉⣉⣉⣀⣀⣀⡀⠉⠒⠄⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠁⠀⠀⠀⠀⠀⠉⠑⠢⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠪⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠈⢂⡀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠌⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠈⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⣀⣀⣀⠤⠤⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(int,int)
  4441 in 66ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║    │ 35 ║     │ -10 ║      │  521 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -8 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -6 ║      │  321 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -4 ║      │  384 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -2 │  -2 ║ 2318 │  205 ║     1 │     0 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1923 │  184 ║     0 │     0 ║     0 │     2 ║     2 │     2 ║
║    │ 34 ║     │   2 ║      │  384 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   4 ║      │  321 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   6 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   8 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  10 ║      │  121 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║      │ sum: ║     1 │     0 ║     0 │     4 ║     4 │     4 ║
║    │    ║     │     ║      │  +%: ║  +300 │    +∞ ║    +∞ │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `<init>(byte[])`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from `byte` array (little endian)_</sup>

```
[runtime performance]
<init>(byte[])
  3481473 in 6925ms
╔════════╦═══════════╦═════════╦════════════╦════════╦═══════╗
║ length ║ precision ║  count  ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═════════╬════════════╬════════╬═══════╣
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║     34 ║         0 ║ 1145796 ║         37 ║     42 ║    36 ║
║     35 ║        12 ║ 1960925 ║         38 ║     43 ║    37 ║
║     36 ║        25 ║  374452 ║         38 ║     43 ║    38 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║    sum: ║        113 ║    128 ║   111 ║
║        ║           ║     +%: ║        +13 ║     +0 ║   +15 ║
╚════════╩═══════════╩═════════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢯⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢜⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠊⣁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠐⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠡⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡊⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠙⣂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡒⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠙⠫⢍⣉⣉⣉⣉⣉⣉⣉⣒⣤⣀⣀⣀⣀⣠⠖⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(byte[])
  1741049 in 16732ms
╔════════╦═══════════╦════════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║        ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬════════╬═══════════════╬═══════════════╬═══════════════╣
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║     34 ║         0 ║ 572866 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     35 ║        12 ║ 980559 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     36 ║        25 ║ 187324 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║   sum: ║     0 │     6 ║     0 │     6 ║     6 │     6 ║
║        ║           ║    +%: ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
╚════════╩═══════════╩════════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `<init>(String)`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from `String`_</sup>

```
[runtime performance]
<init>(String)
  33393 in 151ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     40 ║       -64 ║  3303 ║        975 ║    269 ║   257 ║
║     39 ║       -51 ║  2997 ║        727 ║    211 ║   202 ║
║     38 ║       -38 ║  2766 ║        518 ║    174 ║   158 ║
║     36 ║       -25 ║  3022 ║        388 ║    150 ║   131 ║
║     35 ║       -12 ║  3047 ║        180 ║    103 ║    90 ║
║     34 ║         0 ║  2610 ║        250 ║    183 ║   124 ║
║     35 ║        12 ║  2972 ║        198 ║    121 ║   108 ║
║     36 ║        25 ║  3047 ║        423 ║    145 ║   129 ║
║     38 ║        38 ║  2741 ║        558 ║    168 ║   157 ║
║     39 ║        51 ║  2997 ║        774 ║    222 ║   210 ║
║     40 ║        64 ║  2791 ║       1074 ║    270 ║   261 ║
║        ║           ║  sum: ║       6065 ║   2016 ║  1827 ║
║        ║           ║   +%: ║         +0 ║   +200 ║  +231 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝

⡿⠿⠿⠟⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⢍⣉⡉⠉⠙⠛⠫⠭⠛⠛⠋⠉⢉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠿⠿⠿⠿⠯⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠈⠉⠒⠤⠤⠤⠤⠔⠒⠉⠁⠀⠀⠈⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(String)
  17009 in 636ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     40 ║       -64 ║  1639 ║    37 │    22 ║     0 │     2 ║     2 │     2 ║
║     39 ║       -51 ║  1461 ║    35 │    22 ║     0 │     2 ║     2 │     2 ║
║     38 ║       -38 ║  1358 ║    34 │    21 ║     0 │     2 ║     2 │     2 ║
║     36 ║       -25 ║  1486 ║    23 │    15 ║     0 │     2 ║     2 │     2 ║
║     35 ║       -12 ║  1511 ║     7 │     5 ║     0 │     2 ║     2 │     2 ║
║     34 ║         0 ║  1330 ║     4 │     4 ║     0 │     2 ║     2 │     2 ║
║     35 ║        12 ║  1436 ║     4 │     5 ║     0 │     2 ║     2 │     2 ║
║     36 ║        25 ║  1511 ║    23 │    18 ║     0 │     2 ║     2 │     2 ║
║     38 ║        38 ║  1333 ║    31 │    21 ║     0 │     2 ║     2 │     2 ║
║     39 ║        51 ║  1461 ║    33 │    22 ║     0 │     2 ║     2 │     2 ║
║     40 ║        64 ║  1383 ║    35 │    23 ║     0 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   266 │   178 ║     0 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║    +∞ │  +709 ║ +1109 │  +709 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝

```

##### `<init>(int)`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from signed `int`_</sup>

```
[runtime performance]
<init>(int)
  800441 in 617ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 35 │ 35 ║ -10 │ -10 ║ 119921 │ 119921 ║    4 │    4 ║  20 │  19 ║  18 │  18 ║
║ 34 │ 34 ║  -8 │  -8 ║  79900 │  79900 ║    4 │    4 ║  19 │  19 ║  17 │  17 ║
║ 34 │ 34 ║  -6 │  -6 ║  79921 │  79921 ║    4 │    4 ║  19 │  20 ║  17 │  18 ║
║ 34 │ 34 ║  -4 │  -4 ║  79984 │  79984 ║    4 │    5 ║  19 │  20 ║  17 │  19 ║
║ 34 │ 34 ║  -2 │  -2 ║  40005 │  40005 ║   11 │   10 ║  31 │  29 ║  31 │  29 ║
║ 34 │ 34 ║   0 │   0 ║  39984 │  39984 ║   11 │    9 ║  31 │  28 ║  30 │  28 ║
║ 34 │ 34 ║   2 │   2 ║  79984 │  79984 ║    4 │    5 ║  19 │  21 ║  18 │  18 ║
║ 34 │ 34 ║   4 │   4 ║  79921 │  79921 ║    4 │    4 ║  19 │  20 ║  17 │  17 ║
║ 34 │ 34 ║   6 │   6 ║  79900 │  79900 ║    4 │    4 ║  19 │  19 ║  18 │  17 ║
║ 34 │ 34 ║   8 │   8 ║  79900 │  79900 ║    3 │    4 ║  19 │  20 ║  17 │  17 ║
║ 35 │ 35 ║  10 │  10 ║  39921 │  39921 ║    3 │    3 ║  20 │  20 ║  17 │  17 ║
║    │    ║     │     ║        │   sum: ║   56 │   56 ║ 235 │ 235 ║ 217 │ 215 ║
║    │    ║     │     ║        │    +%: ║ +319 │ +319 ║  +0 │  +0 ║  +8 │  +9 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝

⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠭⠭⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⡀⠀⠀⠀⠀⠀⢀⠀⣀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠁⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⣀⡠⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡏⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⢁⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢊⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⣀⡤⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⠒⠛⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(int)
  4441 in 54ms
╔═════════╦═══════════╦════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║            ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║   count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬═════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 35 │ 35 ║ -10 │ -10 ║ 521 │  521 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -8 │  -8 ║ 300 │  300 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -6 │  -6 ║ 321 │  321 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -4 ║ 384 │  384 ║     2 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -2 │  -2 ║ 205 │  205 ║     1 │     0 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 184 │  184 ║     0 │     0 ║     0 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   2 │   2 ║ 384 │  384 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   4 ║ 321 │  321 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   6 │   6 ║ 300 │  300 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   8 ║ 300 │  300 ║     1 │     1 ║     0 │     2 ║     2 │     2 ║
║ 35 │ 35 ║  10 │  10 ║ 121 │  121 ║     1 │     1 ║     0 │     3 ║     3 │     3 ║
║    │    ║     │     ║     │ sum: ║    14 │     9 ║     0 │    24 ║    24 │    24 ║
║    │    ║     │     ║     │  +%: ║   +71 │  +166 ║    +∞ │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩═════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝

```

##### `<init>(byte[])`<br>&nbsp;&nbsp;&nbsp;<sup>_construct from `byte` array (big endian)_</sup>

```
[runtime performance]
<init>(byte[])
  3481473 in 6824ms
╔════════╦═══════════╦═════════╦════════════╦════════╦═══════╗
║ length ║ precision ║  count  ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═════════╬════════════╬════════╬═══════╣
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║     34 ║         0 ║ 1146618 ║         39 ║     43 ║    43 ║
║     35 ║        12 ║ 1960595 ║         39 ║     43 ║    42 ║
║     36 ║        25 ║  373960 ║         39 ║     43 ║    41 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║    sum: ║        117 ║    129 ║   126 ║
║        ║           ║     +%: ║        +10 ║     +0 ║    +2 ║
╚════════╩═══════════╩═════════╩════════════╩════════╩═══════╝

⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠜⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠨⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣘⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠓⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡌⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠈⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠴⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡐⢁⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠲⠶⠶⠶⠒⠓⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

```
[heap allocation]
<init>(byte[])
  1741049 in 16668ms
╔════════╦═══════════╦════════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║        ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬════════╬═══════════════╬═══════════════╬═══════════════╣
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║     34 ║         0 ║ 572922 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     35 ║        12 ║ 980331 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     36 ║        25 ║ 187496 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║   sum: ║     0 │     6 ║     0 │     6 ║     6 │     6 ║
║        ║           ║    +%: ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
╚════════╩═══════════╩════════╩═══════════════╩═══════════════╩═══════════════╝
```

## `Decimal`

The `Decimal` is like `BigDecimal`, but <ins>without the overhead of heap allocation</ins>, and the following **pros** (:white_check_mark:) and **cons** (:x:):

| | `BigDecimal` | `Decimal` |
|-|:-------------:|:------------:|
| No heap allocation | :x: | :white_check_mark: |
| Overflow detection | :x: | :white_check_mark: |
| Arbitrary precision | :white_check_mark: |  :x: |

To avoid heap allocation, the `Decimal` represents fixed-point decimals inside a `long` primitive value, which thus limits the precision of a `Decimal`-encoded decimal to 64 bits. The representation of a decimal value in `Decimal` encoding is achieved with the following model:

To represent a value such as `1234567.89`, a `Decimal`-encoded `long` has two numbers inside it:

1. **`value`**<br>
   This is a <ins>variable range</ins> value (maximum of `long`) representing the full unscaled precision (all significant digits), i.e.:<br><br>
   ```
   123456789
   ```
1. **`scale`**<br>
   This is a <ins>variable range</ins> value (maximum of `short`) representing the position of the decimal point in the unscaled `value`, i.e.:<br><br>
   ```
   2
   ```

A decimal represented with `Decimal` encoding can be reconstituted with **`value`** and **`scale`** by multiplying the **`value`** by ten to the power of the negation of the **`scale`** (`value ✕ 10⁻ˢᶜᵃˡᵉ`), i.e.:

```
123456789 ✕ 10⁻² = 1234567.89
```

To maximize precision, the `Decimal` encoding implements a <ins>variable range</ins> for the **`scale`**. The <ins>variable range</ins> is defined by a `bits` variable representing the number of bits inside the 64-bit `long` that are reserved for the signed representation of the **`scale`**. This effectively means that the <ins>variable range</ins> of **`value`** is `64 - bits`.

Since both **`value`** and **`scale`** are signed, one bit is reserved for the sign in each. The following table provides sample **`value`** and **`scale`** ranges for various scale `bits` values:

For example, consider the following table with scale **`bits`** as the variable:

| Scale `bits`<br><sup>[`0`, `16`]</sup> | Scale range<br><sup>[`-2ᵇⁱᵗˢ⁻¹`, `2ᵇⁱᵗˢ⁻¹ - 1`]</sup> | Value bits<br><sup>`64-bits`</sup> | Value range<br><sup>[`-2⁶³⁻ᵇⁱᵗˢ`, `2⁶³⁻ᵇⁱᵗˢ - 1`]</sup> | Example<br>&nbsp; |
|:-------------:|:------------:|:------------:|:------------:|:------------|
| <sup>`0`</sup> | <sup>[`0`, `0`]</sup> | <sup>`64`</sup> | <sup>[`-2⁶³`, `2⁶³ - 1`]</sup> | <sup>`Long.MAX_VALUE`</sup> |
| <sup>`1`</sup> | <sup>[`0`, `0`]</sup> | <sup>`63`</sup> | <sup>[`-2⁶²`, `2⁶² - 1`]</sup> | <sup>`4611686018427387904`</sup> |
| <sup>`2`</sup> | <sup>[`-1`, `0`]</sup> | <sup>`62`</sup> | <sup>[`-2⁶¹`, `2⁶¹ - 1`]</sup> | <sup>`2305843009213693952E1`</sup> |
| <sup>`3`</sup> | <sup>[`-2`, `1`]</sup> | <sup>`61`</sup> | <sup>[`-2⁶⁰`, `2⁶⁰ - 1`]</sup> | <sup>`11529215046068469.76`</sup> |
| <sup>`4`</sup> | <sup>[`-8`, `7`]</sup> | <sup>`60`</sup> | <sup>[`-2⁵⁹`, `2⁵⁹ - 1`]</sup> | <sup>`57646075230.3423488`</sup> |
| <sup>`8`</sup> | <sup>[`-128`, `127`]</sup> | <sup>`56`</sup> | <sup>[`-2⁵⁵`, `2⁵⁵ - 1`]</sup> | <sup>`3.6028797018963968E111`</sup> |
| <sup>`16`</sup> | <sup>[`-32768`, `32767`]</sup> | <sup>`48`</sup> | <sup>[`-2⁴⁷`, `2⁴⁷ - 1`]</sup> | <sup>`1.40737488355328E−32768`</sup> |

The following illustrates the way `Decimal` encodes the **`value`** and **`scale`** inside a `long` primitive:

```
        scale sign bit (for bits > 0)
       /
      1
.---+---+---- // --------+------------------------------------ // -------------------------------------.
|   |   '                |                                                                             |
|   |   '   scale        |                                   value                                     |
|   |   '                |                                                                             |
'---+---+---- // --------+----------------------------------- // --------------------------------------'
  0      [1, bits+1]                                   [bits+1, 63-bits]
   \
    value sign bit
```

The `Decimal` implements the following encoding functions:

| | `BigDecimal` | `Decimal` |
|:-|:-:|:-:|
| doubleValue() | :white_check_mark: | :white_check_mark: |
| fromLong() | :white_check_mark: | :white_check_mark: |
| fromString() | :white_check_mark: | :white_check_mark: |
| precision() | :white_check_mark: | :white_check_mark: |
| scale() | :white_check_mark: | :white_check_mark: |
| toString() | :white_check_mark: | :white_check_mark: |

The `Decimal` implements the following arithmetic functions:

| | `BigDecimal` | `Decimal` |
|:-|:-:|:-:|
| add()       | :white_check_mark: | :white_check_mark: |
| divide() | :white_check_mark: | :white_check_mark: |
| multiply() | :white_check_mark: | :white_check_mark: |
| negate() | :white_check_mark: | :white_check_mark: |
| [pow()](https://github.com/libj/math/issues/8) | :white_check_mark: | :x: |
| [remainder()](https://github.com/libj/math/issues/9) | :white_check_mark: | :x: |
| [round()](https://github.com/libj/math/issues/10) | :white_check_mark: | :x: |
| setScale() | :white_check_mark: | :white_check_mark: |
| subtract() | :white_check_mark: | :white_check_mark: |

The `Decimal` implements the following predicate functions:

| | `BigDecimal` | `Decimal` |
|:-|:-:|:-:|
| abs()       | :white_check_mark: | :white_check_mark: |
| compareTo() | :white_check_mark: | :white_check_mark: |
| equals() | :white_check_mark: | :white_check_mark: |
| gt() | :x: | :white_check_mark: |
| gte() | :x: | :white_check_mark: |
| lt() | :x: | :white_check_mark: |
| lte() | :x: | :white_check_mark: |
| max() | :white_check_mark: | :white_check_mark: |
| min() | :white_check_mark: | :white_check_mark: |

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[BigIntAdditionTest]: src/test/java/org/libj/math/BigIntAdditionTest.java
[BigIntMultiplicationTest]: src/test/java/org/libj/math/BigIntMultiplicationTest.java
[BigIntDivisionTest]: src/test/java/org/libj/math/BigIntDivisionTest.java
[BigIntRemainderTest]: src/test/java/org/libj/math/BigIntRemainderTest.java
[BigIntBinaryTest]: src/test/java/org/libj/math/BigIntBinaryTest.java
[BigIntBitwiseTest]: src/test/java/org/libj/math/BigIntBitwiseTest.java
[BigIntPredicateTest]: src/test/java/org/libj/math/BigIntPredicateTest.java
[BigIntConstructorTest]: src/test/java/org/libj/math/BigIntConstructorTest.java