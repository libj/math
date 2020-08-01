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

The following matrix provides a comparison of functions offered by `BigInteger` vs `BigInt` and bare `int[]` array. The values in the matrix have the following rules:

1. A `+###%` represents "percent faster compared to the baseline".
1. A `0` represents the baseline.
1. A ~`0`~ (zero with a line crossing it) represents the baseline, but also says that the function is not inherently available in the particular subject. In these situations, external utility functions were used to bridge the gap.
1. A :x: means the function is missing in the particular subject, and therefore a test was not possible to be performed.
1. A :white_check_mark: means the function is present, but a test was not performed.

It is also important to note the following:

* These numbers are derived from the detailed benchmark tests that are provided further in this document.
* These numbers have relatively high error margin, and should be considered as estimates. It is possible to achieve much better precision in the results, but this would require significant time running these tests in order to increase the sample size. _This will probably be done later._
* These numbers only show the <ins>runtime performance</ins> comparison. In addition to <ins>runtime performance</ins>, the test results further in this document provide <ins>heap memory allocation</ins> comparison as well.

| | `BigInteger` | `BigInt` | `int[]` |
|-|:-------------:|:------------:|:------------:|
| [`abs()`](#abs) | 0 | +100% | +87% |
| [`neg()`](#neg) | 0 | +50% | +1500% |
| [`max(T)`](#maxt) | +34% | 0 | +48% |
| [`min(T)`](#mint) | +30% | 0 | +23% |
| [`signum()`](#signum) | +293% | 0 | +8% |
| [`add(int)`](#addintint)<sup>unsigned</sup> | ~0~ | 193% | 188% |
| [`add(int)`](#addint)<sup>signed</sup> | ~0~ | +337% | +359% |
| [`add(long)`](#addintlong)<sup>unsigned</sup> | ~0~ | +299% | +294% |
| [`add(long)`](#addlong)<sup>signed</sup> | ~0~ | +255% | +330% |
| [`add(T)`](#addt) | 0 | +21% | +58% |
| [`sub(int)`](#subintint)<sup>unsigned</sup> | ~0~ | +156% | +195% |
| [`sub(int)`](#subint)<sup>signed</sup> | ~0~ | +251% | +377% |
| [`sub(long)`](#subintlong)<sup>unsigned</sup> | ~0~ | +155% | +324% |
| [`sub(long)`](#sublong)<sup>signed</sup> | ~0~ | +215% | +300 |
| [`sub(T)`](#subt) | 0 | +37% | +97% |
| [`mul(int)`](#mulintint)<sup>unsigned</sup> | ~0~ | +79% | +95% |
| [`mul(int)`](#mulint)<sup>signed</sup> | ~0~ | +99% | +157% |
| [`mul(long)`](#mulintlong)<sup>unsigned</sup> | ~0~ | +193% | +128% |
| [`mul(long)`](#mullong)<sup>signed</sup> | ~0~ | +125% | +195% |
| [`mul(T)`](#mult) | +75% | 0 | +5% |
| [`div(int)`](#divintint)<sup>unsigned</sup> | ~0~ | +116% | +107% |
| [`div(int)`](#divint)<sup>signed</sup> | ~0~ | +172% | +100% |
| [`div(long)`](#divintlong)<sup>unsigned</sup> | ~0~ | +240% | +255% |
| [`div(long)`](#divlong)<sup>signed</sup> | ~0~ | +146% | +144% |
| [`div(T)`](#divt) | 0 | +29% | +44% |
| [`divRem(int)`](#divremintint)<sup>unsigned</sup> | ~0~ | +57% | +65% |
| [`divRem(int)`](#divremint)<sup>signed</sup> | ~0~ | +144% | +135% |
| [`divRem(long)`](#divremintlong)<sup>unsigned</sup> | ~0~ | +179% | +183% |
| [`divRem(long)`](#divremlong)<sup>signed</sup> | ~0~ | +141% | +142% |
| [`divRem(T)`](#divremt) | ~0~ | +2% | +8% |
| [`rem(int)`](#remintint)<sup>unsigned</sup> | ~0~ | +140% | +120% |
| [`rem(int)`](#remint)<sup>signed</sup> | ~0~ | +172% | +128% |
| [`rem(long)`](#remintlong)<sup>unsigned</sup> | ~0~ | +159% | +253% |
| [`rem(long)`](#remlong)<sup>signed</sup> | ~0~ | +143% | +150% |
| [`rem(T)`](#remt) | ~0~ | +47% | +63% |
| [`mod(T)`](#modt-1) | ~0~ | +55% | +73% |
| [`bitCount()`](#bitcount) | 0 | +68% | +63% |
| [`bitLength()`](#bitlength) | 0 | +81% | +205% |
| [`precision()`](#precision) | ~0~ | +2067% | +2477% |
| [`testBit(int)`](#testbitint) | 0 | +29% | +50% |
| [`setBit(int)`](#setbitint) | 0 | +800% | +1100% |
| [`clearBit(int)`](#clearbitint) | 0 | +440% | +590% |
| [`flipBit(int)`](#flipbitint) | 0 | +508% | +905% |
| [`shiftLeft(int)`](#shiftleftint) | +23% | 0 | +25% |
| [`shiftRight(int)`](#shiftrightint) | +4% | 0 | +59% |
| [`and(T)`](#andt) | 0 | +289% | +476% |
| [`or(T)`](#ort) | 0 | +458% | +711% |
| [`xor(T)`](#xort) | 0 | +335% | +440% |
| [`andNot(T)`](#andnott) | 0 | +325% | +505% |
| [`not()`](#not) | 0 | +589% | +2784% |
| [`byteValue()`](#bytevalue) | 0 | +389% | +97% |
| [`shortValue()`](#shortvalue) | +39% | 0 | +139% |
| [`intValue()`](#intvalue) | +8% | 0 | +68% |
| [`longValue()`](#longvalue) | 0 | +208% | +230% |
| [`floatValue()`](#floatvalue) | 0 | +29% | +96% |
| [`doubleValue()`](#doublevalue) | 0 | +19% | +123% |
| [`toByteArray()`](#tobytearray-be) | 0 | +23% | +15% |
| [`compareTo(T)`](#comparetot) | +215% | +89% | 0 |
| [`equals(T)`](#equalst) | 0 | +3% | +22% |
| [`hashCode()`](#hashcode) | +39% | +5% | 0 |
| [`toString()`](#tostring) | 0 | +349% | +360% |
| `longValueUnsigned()` | :x: | :white_check_mark: | :white_check_mark: |
| `compareToAbs()` | :x: | :white_check_mark: | :white_check_mark: |
| `clone()` | :x: | :white_check_mark: | :white_check_mark: |
| `byteValueExact()` | :white_check_mark: | :x: | :x: |
| `shortValueExact()` | :white_check_mark: | :x: | :x: |
| `intValueExact()` | :white_check_mark: | :x: | :x: |
| `longValueExact()` | :white_check_mark: | :x: | :x: |
| `toString(int)` | :white_check_mark: | :x: | :x: |
| `modInverse(T)` | :white_check_mark: | :x: | :x: |
| `modPow(T)` | :white_check_mark: | :x: | :x: |
| `gcd(T)` | :white_check_mark: | :x: | :x: |
| `getLowestSetBit(T)` | :white_check_mark: | :x: | :x: |
| `isProbablePrime(T)` | :white_check_mark: | :x: | :x: |
| `nextProbablePrime()` | :white_check_mark: | :x: | :x: |
| `pow(T)` | :white_check_mark: | :x: | :x: |

#### Benchmarks

The **`BigInt`** implementation is accompanied by a custom test framework that provides:

1. <ins>Assert correctness of function results</ins><br>The results of each test are asserted to be equal amongst all test subjects.

1. <ins>Compare runtime performance</ins><br>The custom test framework provides a mechanism for the isolation of a specific method to be tested. The actual tests are thus written to compare the feature-equivalent contextually appropriate function that is intended to be benchmarked.

1. <ins>Compare heap memory allocation</ins><br>The custom test framework detects how many instances of a particular type are created during the execution of a test. This is accomplished with bytecode instrumentation via the [Byteman][byteman] and [ByteBuddy][bytebuddy] instrumentation agents. The instrumentation enables a callback to be invoked after the instantiation of the types: `BigInteger`, `BigInt`, and `int[]`.

The custom test framework achieves (2) and (3) with a "phased execution" approach, whereby the <ins>runtime performance</ins> is evaluated first, afterwhich the runtime is instrumented for the evaluation of the <ins>heap memory allocations</ins> in a repeated execution. It is necessary to evaluate the <ins>runtime performance</ins> before instrumentation, because instrumentation adds significant overhead to the runtime.

Benchmark tests are organized in 5 distinct test classes, each responsible for its context of functions:

| Results link | Test code link |
|-|-|
| [Addition and subtraction](#addition-bigintadditiontest) | [`BigIntAdditionTest`][BigIntAdditionTest] |
| [Multiplication](#multiplication-bigintmultiplicationtest) | [`BigIntMultiplicationTest`][BigIntMultiplicationTest] |
| [Division](#division-bigintdivisiontest) | [`BigIntDivisionTest`][BigIntDivisionTest] |
| [Remainder and modulus](#remainder-bigintremaindertest) | [`BigIntRemainderTest`][BigIntRemainderTest] |
| [Binary operations](#binary-bigintbinarytest) | [`BigIntBinaryTest`][BigIntBinaryTest] |
| [Bitwise operations](#bitwise-bigintbitwisetest) | [`BigIntBitwiseTest`][BigIntBitwiseTest] |
| [Predicate functions](#predicate-bigintpredicatetest) | [`BigIntPredicateTest`][BigIntPredicateTest] |
| [Value constructors](#constructors-bigintconstructortest) | [`BigIntConstructorTest`][BigIntConstructorTest] |

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

### Benchmark Results

* All tests were run with `-Xcomp` argument, in order to ensure JIT optimizations are enabled.
* For `BigInt`, execution with `-Xcomp` causes it to load its <ins>critical native</ins> JNI bindings.

#### Addition ([`BigIntAdditionTest`][BigIntAdditionTest])

**Summary**

```
[runtime performance]
╔═══════════════╦═════════════╦═════════════╦═════════════╗
║               ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║  add(int,int) ║   +0 │   +0 ║ +194 │ +193 ║ +190 │ +186 ║
║      add(int) ║   +0 │   +0 ║ +339 │ +336 ║ +359 │ +359 ║
║ add(int,long) ║   +0 │   +0 ║ +305 │ +292 ║ +301 │ +286 ║
║     add(long) ║   +0 │   +0 ║ +259 │ +263 ║ +330 │ +329 ║
║        add(T) ║   +0 │   +0 ║  +21 │  +21 ║  +58 │  +59 ║
║  sub(int,int) ║   +0 │   +0 ║ +158 │ +155 ║ +195 │ +196 ║
║      sub(int) ║   +0 │   +0 ║ +254 │ +248 ║ +377 │ +377 ║
║ sub(int,long) ║   +0 │   +0 ║ +159 │ +150 ║ +328 │ +320 ║
║     sub(long) ║   +0 │   +0 ║ +216 │ +215 ║ +297 │ +304 ║
║        sub(T) ║   +0 │   +0 ║  +36 │  +39 ║  +96 │  +98 ║
╚═══════════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[heap allocation]
╔═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║               ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║  add(int,int) ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
║      add(int) ║    +0 │    +0 ║  +854 │  +196 ║  +262 │  +196 ║
║ add(int,long) ║    +0 │    +0 ║ +2136 │  +535 ║  +778 │  +535 ║
║     add(long) ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
║        add(T) ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
║  sub(int,int) ║    +0 │    +0 ║  +863 │  +206 ║  +265 │  +206 ║
║      sub(int) ║    +0 │    +0 ║  +863 │  +200 ║  +265 │  +200 ║
║ sub(int,long) ║    +0 │    +0 ║ +2127 │  +539 ║  +775 │  +539 ║
║     sub(long) ║    +0 │    +0 ║ +1945 │  +467 ║  +703 │  +467 ║
║        sub(T) ║    +0 │    +0 ║ +5318 │  +772 ║ +1254 │  +772 ║
╚═══════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `add(int,int)`

Addition of unsigned `int`.

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
add(int,int)
  2640441 in 6475ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   60 │   55 ║   20 │   17 ║   21 │   18 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   56 │   52 ║   20 │   21 ║   20 │   21 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   61 │   54 ║   20 │   22 ║   21 │   23 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   53 │   61 ║   21 │   24 ║   21 │   25 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   52 │   64 ║   16 │   27 ║   17 │   27 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   54 │   55 ║   15 │   19 ║   16 │   19 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   55 │   63 ║   19 │   18 ║   20 │   19 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   57 │   58 ║   21 │   18 ║   20 │   18 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   56 │   57 ║   20 │   18 ║   19 │   18 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   61 │   57 ║   20 │   17 ║   20 │   17 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   63 │   63 ║   21 │   17 ║   21 │   18 ║
║    │    ║     │     ║        │   sum: ║  628 │  639 ║  213 │  218 ║  216 │  223 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +194 │ +193 ║ +190 │ +186 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣩⠭⠽⠛⠛⠛⠿⠯⣭⣭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡏⠉⠉⠉⠉⠉⠙⠒⠒⠒⠒⠒⠒⠶⠶⠶⠶⣒⣒⣒⣢⠤⠤⠤⠤⠤⠤⠤⠤⣤⣒⣒⠶⠭⠝⠋⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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
⡇⠀⠀⠀⠀⠀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠔⠒⠉⠉⠀⠀⠀⠀⠈⠉⠑⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡠⠤⠤⠔⠒⠊⠉⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
add(int,int)
  8361 in 1585ms
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

##### `add(int)`

Addition of signed `int`.

```
[runtime performance]
add(int)
  2640441 in 5464ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   61 │   61 ║   14 │   13 ║   14 │   13 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   58 │   59 ║   15 │   14 ║   14 │   13 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   59 │   60 ║   13 │   14 ║   13 │   13 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   55 │   58 ║   11 │   14 ║   11 │   14 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   53 │   51 ║   11 │   12 ║   10 │   11 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   50 │   48 ║   10 │   12 ║   10 │   11 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   56 │   59 ║   11 │   12 ║   11 │   12 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   57 │   58 ║   13 │   13 ║   12 │   12 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   60 │   58 ║   15 │   13 ║   14 │   13 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   59 │   58 ║   15 │   13 ║   14 │   12 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   61 │   59 ║   15 │   14 ║   14 │   13 ║
║    │    ║     │     ║        │   sum: ║  629 │  629 ║  143 │  144 ║  137 │  137 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +339 │ +336 ║ +359 │ +359 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡯⣭⣭⣭⣭⠭⠭⠭⢍⣉⡩⠭⣭⣭⣭⣭⡭⠭⠭⠭⠭⠿⠟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠿⠿⠿⠿⢟⣛⣛⣛⣭⡭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠊⠑⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
add(int)
  8361 in 247ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    11 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     8 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    11 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     7 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     8 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   105 │    86 ║    11 │    29 ║    29 │    29 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +854 │  +196 ║  +262 │  +196 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `add(int,long)`

Addition of unsigned `long`.

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
add(int,long)
  946929 in 3051ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  109 │  144 ║   29 │   29 ║   28 │   25 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  108 │  147 ║   28 │   30 ║   28 │   28 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  105 │  145 ║   26 │   32 ║   25 │   31 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  102 │  147 ║   30 │   32 ║   29 │   29 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  100 │  149 ║   28 │   35 ║   28 │   31 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   99 │   69 ║   24 │   21 ║   25 │   22 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║  100 │   64 ║   23 │   16 ║   24 │   21 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  104 │   65 ║   24 │   16 ║   25 │   21 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  108 │   64 ║   22 │   24 ║   24 │   27 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  109 │   63 ║   26 │   25 ║   27 │   28 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  112 │   63 ║   25 │   25 ║   25 │   27 ║
║    │    ║     │     ║        │   sum: ║ 1156 │ 1120 ║  285 │  285 ║  288 │  290 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +305 │ +292 ║ +301 │ +286 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣭⡭⠭⠿⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣫⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
add(int,long)
  13117 in 849ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    39 │    24 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    37 │    24 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    21 │    15 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    12 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    12 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     9 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     9 │     9 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║    10 │    10 ║     1 │     2 ║     2 │     2 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    26 │    20 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    34 │    24 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    37 │    26 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   246 │   178 ║    11 │    28 ║    28 │    28 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2136 │  +535 ║  +778 │  +535 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `add(long)`

Addition of signed `long`.

```
[runtime performance]
add(long)
  946929 in 2811ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   56 │   54 ║   15 │   21 ║   13 │   16 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   58 │   54 ║   15 │   21 ║   12 │   16 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   56 │   58 ║   15 │   14 ║   13 │   12 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   54 │   55 ║   17 │    9 ║   14 │    9 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   52 │   60 ║   16 │   12 ║   14 │   11 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   50 │   64 ║   17 │   13 ║   14 │   12 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   54 │   55 ║   16 │   10 ║   14 │   10 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   54 │   55 ║   16 │   11 ║   14 │   10 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   58 │   55 ║   13 │   18 ║   11 │   16 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   61 │   53 ║   15 │   20 ║   12 │   16 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   58 │   55 ║   15 │   21 ║   11 │   16 ║
║    │    ║     │     ║        │   sum: ║  611 │  618 ║  170 │  170 ║  142 │  144 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +259 │ +263 ║ +330 │ +329 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⠭⠭⠭⠭⠝⠛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⢽
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⣀⡠⠤⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠢⠔⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠤⠤⠤⣀⣀⡉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⢼
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
⡧⠤⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⠤⠤⠒⠒⠊⠉⠉⠉⠉⠒⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⡠⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
add(long)
  13117 in 573ms
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

##### `add(T)`

Addition of `T`.

```
[runtime performance]
add(T)
  107377 in 982ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   60 │   53 ║  45 │  45 ║  34 │  34 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   55 │   53 ║  46 │  44 ║  31 │  37 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   49 │   53 ║  43 │  43 ║  28 │  36 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   51 │   54 ║  44 │  42 ║  30 │  31 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   47 │   52 ║  42 │  44 ║  31 │  33 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   48 │   52 ║  44 │  45 ║  36 │  29 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   49 │   54 ║  41 │  42 ║  35 │  31 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   49 │   55 ║  40 │  42 ║  36 │  33 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   50 │   52 ║  40 │  43 ║  34 │  32 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   56 │   52 ║  44 │  43 ║  34 │  33 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   62 │   51 ║  45 │  45 ║  35 │  35 ║
║    │    ║      │     ║       │  sum: ║  576 │  581 ║ 474 │ 478 ║ 364 │ 364 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +21 │ +21 ║ +58 │ +59 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⡩⠭⠭⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠉⠉⠉⠀⠀⠀⠀⠈⠉⠉⠑⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠢⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠈⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠤⠤⠤⣀⣀⡠⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⣀⣀⠤⠤⠔⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
add(T)
  54001 in 5257ms
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

##### `sub(int,int)`

Subtraction of unsigned `int`.

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
sub(int,int)
  2640441 in 5795ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   57 │   52 ║   21 │   18 ║   20 │   16 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   54 │   53 ║   22 │   22 ║   20 │   19 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   57 │   53 ║   21 │   24 ║   20 │   22 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   52 │   54 ║   22 │   26 ║   19 │   23 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   48 │   57 ║   18 │   27 ║   15 │   24 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   47 │   55 ║   19 │   23 ║   14 │   18 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   54 │   57 ║   20 │   21 ║   17 │   17 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   56 │   57 ║   22 │   20 ║   19 │   17 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   57 │   56 ║   22 │   19 ║   20 │   17 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   57 │   55 ║   22 │   18 ║   19 │   16 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   58 │   55 ║   22 │   18 ║   19 │   15 ║
║    │    ║     │     ║        │   sum: ║  597 │  604 ║  231 │  236 ║  202 │  204 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +158 │ +155 ║ +195 │ +196 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⠭⠭⠝⠛⠛⠫⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡗⠤⢍⡉⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠔⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠈⠑⠒⠤⠤⣀⣀⡠⠤⠤⠒⠒⠒⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠤⠤⠤⠔⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
sub(int,int)
  8361 in 258ms
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

##### `sub(int)`

Subtraction of signed `int`.

```
[runtime performance]
sub(int)
  2640441 in 5861ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   64 │   63 ║   19 │   17 ║   12 │   13 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   61 │   65 ║   19 │   18 ║   12 │   13 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   62 │   65 ║   19 │   19 ║   12 │   14 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   64 │   65 ║   17 │   19 ║   14 │   14 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   64 │   52 ║   16 │   18 ║   14 │   13 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   62 │   53 ║   17 │   17 ║   15 │   12 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   63 │   67 ║   17 │   17 ║   15 │   13 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   62 │   64 ║   17 │   17 ║   13 │   13 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   60 │   64 ║   17 │   18 ║   12 │   13 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   61 │   63 ║   17 │   17 ║   12 │   12 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   65 │   62 ║   19 │   19 ║   13 │   13 ║
║    │    ║     │     ║        │   sum: ║  688 │  683 ║  194 │  196 ║  144 │  143 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +254 │ +248 ║ +377 │ +377 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠊⠉⠉⠉⠉⠑⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
sub(int)
  8361 in 246ms
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

##### `sub(int,long)`

Subtraction of unsigned `long`.

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
sub(int,long)
  946929 in 3189ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  110 │  152 ║   42 │   42 ║   25 │   26 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  112 │  151 ║   44 │   44 ║   26 │   26 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  107 │  152 ║   40 │   50 ║   24 │   31 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  105 │  154 ║   40 │   55 ║   24 │   30 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  107 │  151 ║   32 │   53 ║   24 │   31 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║  107 │   69 ║   37 │   34 ║   27 │   19 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║  108 │   65 ║   44 │   35 ║   28 │   19 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  106 │   66 ║   48 │   35 ║   28 │   19 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  112 │   65 ║   43 │   38 ║   24 │   25 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  111 │   64 ║   46 │   37 ║   24 │   25 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  110 │   66 ║   45 │   38 ║   25 │   24 ║
║    │    ║     │     ║        │   sum: ║ 1195 │ 1155 ║  461 │  461 ║  279 │  275 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +159 │ +150 ║ +328 │ +320 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡟⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
sub(int,long)
  13117 in 598ms
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

##### `sub(long)`

Subtraction of signed `long`.

```
[runtime performance]
sub(long)
  946929 in 2888ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   71 │   70 ║   22 │   27 ║   16 │   22 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   74 │   69 ║   21 │   26 ║   16 │   20 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   75 │   70 ║   24 │   19 ║   20 │   17 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   66 │   69 ║   21 │   16 ║   18 │   14 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   57 │   67 ║   20 │   17 ║   16 │   15 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   56 │   64 ║   21 │   17 ║   17 │   11 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   60 │   67 ║   20 │   16 ║   17 │   11 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   68 │   69 ║   23 │   16 ║   19 │   13 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   77 │   69 ║   22 │   26 ║   17 │   20 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   75 │   68 ║   22 │   29 ║   17 │   21 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   69 │   70 ║   20 │   29 ║   15 │   22 ║
║    │    ║     │     ║        │   sum: ║  748 │  752 ║  236 │  238 ║  188 │  186 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +216 │ +215 ║ +297 │ +304 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠥⠤⠤⠤⠒⠒⠒⠢⠤⠤⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠔⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
sub(long)
  13117 in 568ms
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

##### `sub(T)`

Subtraction of `T`.

```
[runtime performance]
sub(T)
  107377 in 970ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   66 │   60 ║  42 │  45 ║  28 │  32 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   62 │   55 ║  45 │  41 ║  27 │  28 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   57 │   59 ║  40 │  44 ║  26 │  29 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   51 │   54 ║  40 │  40 ║  27 │  26 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   51 │   55 ║  39 │  40 ║  30 │  28 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   50 │   57 ║  45 │  37 ║  33 │  26 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   50 │   56 ║  40 │  37 ║  30 │  27 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   52 │   60 ║  39 │  42 ║  29 │  27 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   56 │   57 ║  39 │  41 ║  27 │  31 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   61 │   57 ║  41 │  40 ║  29 │  29 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   61 │   56 ║  41 │  42 ║  28 │  32 ║
║    │    ║      │     ║       │  sum: ║  617 │  626 ║ 451 │ 449 ║ 314 │ 315 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +36 │ +39 ║ +96 │ +98 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⣏⣉⣉⡩⠭⠭⠭⠭⠝⠭⠭⠫⠭⠭⠭⠫⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠛⠫⢍⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠉⠉⠑⠒⠢⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡧⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⣀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠈⠉⠑⠒⠒⠢⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠉⠉⠉⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⡀⠀⠀⠀⡀⣀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⢀⡠⠔⠒⠉⠉⠁⠉⠉⠈⠉⠉⠉⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
sub(T)
  54001 in 5092ms
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

#### Multiplication ([`BigIntMultiplicationTest`][BigIntMultiplicationTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════╦═══════════╗
║                  ║   MPN   ║  BigInt   ║
╠══════════════════╬════╤════╬═════╤═════╣
║  umul(int[],int) ║ +0 │ +0 ║  +7 │  +6 ║
║ umul(int[],long) ║ +0 │ +0 ║ +57 │ +56 ║
╚══════════════════╩════╧════╩═════╧═════╝

[runtime performance]
╔═══════════════╦═════════════╦═════════════╦═════════════╗
║               ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     mul(T): 1 ║   +0 │   +0 ║  +24 │  +25 ║  +48 │  +51 ║
║     mul(T): 2 ║   +0 │   +0 ║   +4 │   +5 ║  +14 │  +15 ║
║     mul(T): 4 ║  +41 │  +41 ║   +0 │   +0 ║   +5 │   +5 ║
║     mul(T): 8 ║  +88 │  +88 ║   +0 │   +0 ║   +2 │   +2 ║
║    mul(T): 16 ║ +109 │ +108 ║   +0 │   +0 ║   +4 │   +4 ║
║    mul(T): 32 ║  +84 │  +81 ║   +0 │   +0 ║   +5 │   +5 ║
║    mul(T): 64 ║  +54 │  +54 ║   +0 │   +0 ║   +4 │   +4 ║
║   mul(T): 128 ║  +44 │  +47 ║   +0 │   +0 ║   +3 │   +3 ║
║  mul(int,int) ║   +0 │   +0 ║  +76 │  +81 ║  +93 │  +97 ║
║      mul(int) ║   +0 │   +0 ║ +100 │  +97 ║ +156 │ +159 ║
║ mul(int,long) ║   +0 │   +0 ║ +193 │ +194 ║ +128 │ +128 ║
║     mul(long) ║   +0 │   +0 ║ +124 │ +128 ║ +193 │ +199 ║
╚═══════════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[runtime performance]
╔═══════════════╦════════════╦════════╦═══════╗
║               ║ BigInteger ║ BigInt ║ int[] ║
╠═══════════════╬════════════╬════════╬═══════╣
║   mul(T,T): 1 ║        +29 ║     +0 ║   +16 ║
║   mul(T,T): 2 ║        +31 ║     +0 ║   +10 ║
║   mul(T,T): 4 ║        +47 ║     +0 ║    +8 ║
║   mul(T,T): 8 ║        +82 ║     +0 ║    +4 ║
║  mul(T,T): 16 ║       +107 ║     +0 ║    +0 ║
║  mul(T,T): 32 ║        +95 ║     +0 ║    +1 ║
║  mul(T,T): 64 ║        +99 ║     +0 ║    +0 ║
║ mul(T,T): 128 ║        +71 ║     +0 ║    +2 ║
╚═══════════════╩════════════╩════════╩═══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╗
║                  ║      MPN      ║    BigInt     ║
║                  ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╣
║  umul(int[],int) ║    +0 │    +0 ║    +0 │    +0 ║
║ umul(int[],long) ║    +0 │    +0 ║    +0 │    +0 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╝

[heap allocation]
╔═══════════════╦═══════════════╦═════════════════╦═════════════════╗
║               ║  BigInteger   ║     BigInt      ║      int[]      ║
║               ║     T │ int[] ║      T │ int[]  ║      T │ int[]  ║
╠═══════════════╬═══════╤═══════╬════════╤════════╬════════╤════════╣
║     mul(T): 1 ║    +0 │    +0 ║  +2481 │   +563 ║   +932 │   +563 ║
║     mul(T): 2 ║    +0 │    +0 ║  +3040 │   +658 ║  +1156 │   +658 ║
║     mul(T): 4 ║    +0 │    +0 ║  +3636 │   +758 ║  +1394 │   +758 ║
║     mul(T): 8 ║    +0 │    +0 ║  +4104 │   +821 ║  +1581 │   +821 ║
║    mul(T): 16 ║    +0 │    +0 ║  +4681 │   +905 ║  +1812 │   +905 ║
║    mul(T): 32 ║    +0 │    +0 ║  +6209 │  +1278 ║  +2423 │  +1278 ║
║    mul(T): 64 ║    +0 │    +0 ║ +11800 │  +2990 ║  +4660 │  +2990 ║
║   mul(T): 128 ║    +0 │    +0 ║ +43431 │ +13701 ║ +17312 │ +13701 ║
║  mul(int,int) ║    +0 │    +0 ║   +863 │   +155 ║   +211 │   +155 ║
║      mul(int) ║    +0 │    +0 ║   +863 │   +155 ║   +211 │   +155 ║
║ mul(int,long) ║    +0 │    +0 ║  +2127 │   +458 ║   +690 │   +458 ║
║     mul(long) ║    +0 │    +0 ║  +1972 │   +416 ║   +660 │   +416 ║
║   mul(T,T): 1 ║    +0 │    +0 ║  +2481 │   +442 ║   +760 │   +442 ║
║   mul(T,T): 2 ║    +0 │    +0 ║  +3063 │   +521 ║   +954 │   +521 ║
║   mul(T,T): 4 ║    +0 │    +0 ║  +3636 │   +615 ║  +1145 │   +615 ║
║   mul(T,T): 8 ║    +0 │    +0 ║  +4200 │   +678 ║  +1333 │   +678 ║
║  mul(T,T): 16 ║    +0 │    +0 ║  +4754 │   +739 ║  +1518 │   +739 ║
║  mul(T,T): 32 ║    +0 │    +0 ║  +6436 │  +1072 ║  +2078 │  +1072 ║
║  mul(T,T): 64 ║    +0 │    +0 ║ +14145 │  +3036 ║  +4648 │  +3036 ║
╚═══════════════╩═══════╧═══════╩════════╧════════╩════════╧════════╝
```

##### `umul(int[],int)`

This test validates the correctness of `[BigIntMultiplication.umul(int)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.

```
[runtime performance]
umul(int[],int)
  289329 in 368ms
╔═════════╦═══════════╦═══════════════╦═════════╦═════════╗
║ length  ║ precision ║     count     ║   MPN   ║ BigInt  ║
╠════╤════╬═════╤═════╬═══════╤═══════╬════╤════╬════╤════╣
║ 36 │ 36 ║ -19 │ -19 ║ 37923 │ 37923 ║  7 │  8 ║  7 │  7 ║
║ 35 │ 35 ║ -15 │ -15 ║ 22700 │ 22700 ║ 14 │  8 ║ 11 │  7 ║
║ 35 │ 35 ║ -11 │ -11 ║ 30346 │ 30346 ║  8 │  7 ║  7 │  8 ║
║ 34 │ 34 ║  -7 │  -7 ║ 22723 │ 22723 ║  6 │  7 ║  7 │  6 ║
║ 34 │ 34 ║  -3 │  -3 ║ 30484 │ 30484 ║  7 │  8 ║  8 │  8 ║
║ 34 │ 34 ║   0 │   0 ║ 15284 │ 15284 ║  8 │  8 ║  8 │  8 ║
║ 34 │ 34 ║   3 │   3 ║ 30346 │ 30346 ║  7 │  7 ║  7 │  6 ║
║ 34 │ 34 ║   7 │   7 ║ 22700 │ 22700 ║  7 │  7 ║  6 │  6 ║
║ 35 │ 35 ║  11 │  11 ║ 30300 │ 30300 ║  8 │ 11 ║  8 │  7 ║
║ 35 │ 35 ║  15 │  15 ║ 22700 │ 22700 ║ 10 │  7 ║  8 │  9 ║
║ 36 │ 36 ║  19 │  19 ║ 22723 │ 22723 ║  7 │  7 ║  6 │  8 ║
║    │    ║     │     ║       │  sum: ║ 89 │ 85 ║ 83 │ 80 ║
║    │    ║     │     ║       │   +%: ║ +0 │ +0 ║ +7 │ +6 ║
╚════╧════╩═════╧═════╩═══════╧═══════╩════╧════╩════╧════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡽⠋⠉⠉⠙⡙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠴⠋⠀⠀⠀⠀⠀⠈⠄⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠈⡀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠈⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠪⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⡲⠮⠔⠤⠤⠤⠤⠔⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠤⠤⠤⠤⠤⢼
⡏⡈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⢀⢤⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠀⠀⠑⢄⡀⠀⠀⢀⡠⠊⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⡠⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⢀⠤⡪⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠂⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠨⠛⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢁⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠡⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠡⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
umul(int[],int)
  7749 in 264ms
╔═════════╦═══════════╦════════════╦═══════════════╦═══════════════╗
║         ║           ║            ║      MPN      ║    BigInt     ║
║ length  ║ precision ║   count    ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬═════╤══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 36 ║ -19 │ -19 ║ 873 │  873 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -15 │ -15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -11 │ -11 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -7 │  -7 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 844 │  844 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   0 │   0 ║ 464 │  464 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   3 │   3 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   7 │   7 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  11 │  11 ║ 660 │  660 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  15 │  15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 36 │ 36 ║  19 │  19 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │ sum: ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │  +%: ║    +0 │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩═════╧══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 1`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 1
  33393 in 647ms
╔═════════╦═══════════╦═════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║    count    ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬══════╤══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 40 │ 40 ║ -64 │ -64 ║ 3303 │ 3303 ║  106 │   72 ║  57 │  61 ║  48 │  55 ║
║ 39 │ 39 ║ -51 │ -51 ║ 2997 │ 2997 ║   57 │   62 ║  50 │  56 ║  44 │  50 ║
║ 38 │ 38 ║ -38 │ -38 ║ 2766 │ 2766 ║   54 │   62 ║  48 │  50 ║  43 │  45 ║
║ 36 │ 36 ║ -25 │ -25 ║ 3022 │ 3022 ║   49 │   59 ║  43 │  47 ║  38 │  37 ║
║ 35 │ 35 ║ -12 │ -12 ║ 3047 │ 3047 ║   49 │   50 ║  49 │  32 ║  35 │  25 ║
║ 34 │ 34 ║   0 │   0 ║ 2610 │ 2610 ║   49 │   51 ║  46 │  30 ║  34 │  23 ║
║ 35 │ 35 ║  12 │  12 ║ 2972 │ 2972 ║   50 │   52 ║  48 │  38 ║  36 │  27 ║
║ 36 │ 36 ║  25 │  25 ║ 3047 │ 3047 ║   50 │   68 ║  47 │  51 ║  43 │  40 ║
║ 38 │ 38 ║  38 │  38 ║ 2741 │ 2741 ║   54 │   72 ║  50 │  57 ║  44 │  46 ║
║ 39 │ 39 ║  51 │  51 ║ 2997 │ 2997 ║   56 │   69 ║  51 │  59 ║  45 │  50 ║
║ 40 │ 40 ║  64 │  64 ║ 2791 │ 2791 ║  111 │   72 ║  63 │  67 ║  50 │  56 ║
║    │    ║     │     ║      │ sum: ║  685 │  689 ║ 552 │ 548 ║ 460 │ 454 ║
║    │    ║     │     ║      │  +%: ║   +0 │   +0 ║ +24 │ +25 ║ +48 │ +51 ║
╚════╧════╩═════╧═════╩══════╧══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠉⠉⠉⠉⠑⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠒⠒⠉⠉⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠤⠔⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⠤⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠈⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⠤⢄⣀⣀⣀⡠⠤⠒⠒⠉⠑⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 1
  17009 in 2559ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 40 │ 40 ║ -64 │ -64 ║ 1639 │ 1639 ║    77 │    45 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║ -51 │ -51 ║ 1461 │ 1461 ║    73 │    44 ║     2 │     5 ║     5 │     5 ║
║ 38 │ 38 ║ -38 │ -38 ║ 1358 │ 1358 ║    69 │    43 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║ -25 │ -25 ║ 1486 │ 1486 ║    48 │    31 ║     2 │     5 ║     5 │     5 ║
║ 35 │ 35 ║ -12 │ -12 ║ 1511 │ 1511 ║    16 │    11 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║   0 │   0 ║ 1330 │ 1330 ║    12 │    10 ║     2 │     5 ║     5 │     5 ║
║ 35 │ 35 ║  12 │  12 ║ 1436 │ 1436 ║    13 │    12 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║  25 │  25 ║ 1511 │ 1511 ║    51 │    36 ║     2 │     5 ║     5 │     5 ║
║ 38 │ 38 ║  38 │  38 ║ 1333 │ 1333 ║    66 │    43 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  51 │  51 ║ 1461 │ 1461 ║    69 │    44 ║     2 │     5 ║     5 │     5 ║
║ 40 │ 40 ║  64 │  64 ║ 1383 │ 1383 ║    74 │    46 ║     2 │     5 ║     5 │     5 ║
║    │    ║     │     ║      │ sum: ║   568 │   365 ║    22 │    55 ║    55 │    55 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2481 │  +563 ║  +932 │  +563 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 2`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 2
  131697 in 1971ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 47 ║ -128 │ -128 ║ 13287 │ 13287 ║  146 │  113 ║ 114 │ 130 ║ 110 │ 119 ║
║ 44 │ 44 ║ -102 │ -102 ║ 11189 │ 11189 ║   88 │  103 ║  98 │ 107 ║  95 │ 100 ║
║ 41 │ 41 ║  -76 │  -76 ║ 12238 │ 12238 ║   88 │   97 ║  93 │  90 ║  87 │  85 ║
║ 39 │ 39 ║  -51 │  -51 ║ 12238 │ 12238 ║   92 │   83 ║  79 │  72 ║  73 │  67 ║
║ 36 │ 36 ║  -25 │  -25 ║ 11214 │ 11214 ║   54 │   72 ║  63 │  56 ║  54 │  47 ║
║ 34 │ 34 ║    0 │    0 ║ 11339 │ 11339 ║   53 │   61 ║  42 │  44 ║  36 │  32 ║
║ 36 │ 36 ║   25 │   25 ║ 11164 │ 11164 ║   55 │   82 ║  68 │  61 ║  58 │  52 ║
║ 39 │ 39 ║   51 │   51 ║ 12263 │ 12263 ║   96 │   88 ║  89 │  75 ║  74 │  68 ║
║ 41 │ 41 ║   76 │   76 ║ 12213 │ 12213 ║   93 │   97 ║ 100 │  92 ║  90 │  86 ║
║ 44 │ 44 ║  102 │  102 ║ 11189 │ 11189 ║   92 │  107 ║ 105 │ 113 ║ 100 │ 102 ║
║ 47 │ 47 ║  128 │  128 ║ 12263 │ 12263 ║  157 │  121 ║ 124 │ 131 ║ 108 │ 125 ║
║    │    ║      │      ║       │  sum: ║ 1014 │ 1024 ║ 975 │ 971 ║ 885 │ 883 ║
║    │    ║      │      ║       │   +%: ║   +0 │   +0 ║  +4 │  +5 ║ +14 │ +15 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠝⠛⠉⠙⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⢀⡠⠤⠒⠒⠤⡀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠁⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠀⣀⡠⠔⠒⠉⠁⢀⡠⠔⠒⠒⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⡀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⢀⡠⠔⠉⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⢄⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⢀⡠⠒⠁⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠤⣤⠤⠶⣉⣀⣀⣤⣒⣁⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠮⠒⠊⠉⠀⣀⡠⠒⠉⠀⠀⠀⠀⠉⠉⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⢀⣀⠭⠖⠶⣒⠒⠊⠉⠉⠉⠛⠭⢕⣄⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠔⠁⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⢤⠜⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⡀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⡒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 2
  66161 in 7389ms
╔═════════╦═════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 47 ║ -128 │ -128 ║ 6631 │ 6631 ║    83 │    46 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 5557 │ 5557 ║    79 │    45 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 41 ║  -76 │  -76 ║ 6094 │ 6094 ║    75 │    44 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  -51 │  -51 ║ 6094 │ 6094 ║    68 │    41 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║  -25 │  -25 ║ 5582 │ 5582 ║    41 │    26 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 5707 │ 5707 ║    13 │    10 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║   25 │   25 ║ 5532 │ 5532 ║    42 │    29 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║   51 │   51 ║ 6119 │ 6119 ║    65 │    41 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 41 ║   76 │   76 ║ 6069 │ 6069 ║    71 │    44 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 5557 │ 5557 ║    75 │    45 ║     2 │     5 ║     5 │     5 ║
║ 47 │ 47 ║  128 │  128 ║ 6119 │ 6119 ║    79 │    46 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║      │ sum: ║   691 │   417 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║      │  +%: ║    +0 │    +0 ║ +3040 │  +658 ║ +1156 │  +658 ║
╚════╧════╩══════╧══════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 4`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 4
  231417 in 6125ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 60 │ 60 ║ -256 │ -256 ║ 21899 │ 21611 ║  218 │  210 ║  331 │  357 ║  324 │  342 ║
║ 55 │ 55 ║ -204 │ -204 ║ 22025 │ 21617 ║  194 │  185 ║  283 │  299 ║  273 │  292 ║
║ 49 │ 49 ║ -153 │ -153 ║ 21206 │ 21582 ║  167 │  159 ║  226 │  236 ║  217 │  223 ║
║ 44 │ 44 ║ -102 │ -102 ║ 19018 │ 19742 ║  129 │  127 ║  178 │  173 ║  174 │  165 ║
║ 39 │ 39 ║  -51 │  -51 ║ 22662 │ 21618 ║  105 │  104 ║  135 │  119 ║  125 │  105 ║
║ 34 │ 34 ║    0 │    0 ║ 18815 │ 19803 ║  100 │   87 ║   92 │   85 ║   81 │   63 ║
║ 39 │ 39 ║   51 │   51 ║ 20544 │ 19792 ║  103 │  122 ║  145 │  124 ║  132 │  115 ║
║ 44 │ 44 ║  102 │  102 ║ 21803 │ 21607 ║  136 │  146 ║  198 │  178 ║  179 │  167 ║
║ 49 │ 49 ║  153 │  153 ║ 18421 │ 19717 ║  172 │  175 ║  236 │  238 ║  224 │  225 ║
║ 55 │ 55 ║  204 │  204 ║ 22025 │ 21617 ║  193 │  201 ║  287 │  298 ║  273 │  288 ║
║ 60 │ 60 ║  256 │  256 ║ 21899 │ 21611 ║  222 │  224 ║  344 │  353 ║  329 │  343 ║
║    │    ║      │      ║       │  sum: ║ 1739 │ 1740 ║ 2455 │ 2460 ║ 2331 │ 2328 ║
║    │    ║      │      ║       │   +%: ║  +41 │  +41 ║   +0 │   +0 ║   +5 │   +5 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠭⠭⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⢒⠖⠥⠤⣠⣔⣒⣒⣤⣀⣀⣐⣄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⡠⠒⠁⢀⠔⠉⠀⠀⠀⠀⠀⠀⠑⢄⡀⠑⢄⠀⠈⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠑⢄⡀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⡡⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⡡⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢊⠕⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⡡⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⢁⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⢁⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⡡⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠔⢉⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⠤⠒⢉⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⣁⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 4
  116229 in 15672ms
╔═════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 60 │ 60 ║ -256 │ -256 ║ 10553 │ 10683 ║    92 │    49 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║ -204 │ -204 ║ 10941 │ 10709 ║    87 │    47 ║     2 │     5 ║     5 │     5 ║
║ 49 │ 49 ║ -153 │ -153 ║  9520 │ 10814 ║    89 │    50 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 10142 │  9934 ║    77 │    44 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  -51 │  -51 ║ 11226 │ 10822 ║    65 │    39 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 11787 │ 10189 ║    17 │    13 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║   51 │   51 ║  9804 │  9838 ║    64 │    40 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 11207 │ 10893 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 49 │ 49 ║  153 │  153 ║  8455 │  9855 ║    87 │    51 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║  204 │  204 ║ 10941 │ 10709 ║    83 │    47 ║     2 │     5 ║     5 │     5 ║
║ 60 │ 60 ║  256 │  256 ║ 10553 │ 10683 ║    88 │    49 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║       │  sum: ║   822 │   472 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║       │   +%: ║    +0 │    +0 ║ +3636 │  +758 ║ +1394 │  +758 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 8`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 8
  336513 in 19334ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 87 │ 87 ║ -512 │ -512 ║ 32131 │ 31235 ║  582 │  550 ║ 1162 │ 1168 ║ 1145 │ 1147 ║
║ 76 │ 76 ║ -409 │ -409 ║ 34421 │ 31429 ║  477 │  462 ║  930 │  946 ║  913 │  929 ║
║ 65 │ 65 ║ -307 │ -307 ║ 28834 │ 31486 ║  384 │  367 ║  730 │  728 ║  711 │  707 ║
║ 55 │ 55 ║ -204 │ -204 ║ 26282 │ 28862 ║  277 │  278 ║  493 │  525 ║  476 │  510 ║
║ 44 │ 44 ║ -102 │ -102 ║ 30310 │ 31202 ║  189 │  200 ║  312 │  298 ║  296 │  285 ║
║ 34 │ 34 ║    0 │    0 ║ 31507 │ 27035 ║   88 │  130 ║  143 │  141 ║  132 │  116 ║
║ 44 │ 44 ║  102 │  102 ║ 30260 │ 31152 ║  195 │  220 ║  316 │  305 ║  298 │  284 ║
║ 55 │ 55 ║  204 │  204 ║ 28779 │ 31435 ║  290 │  308 ║  513 │  533 ║  492 │  514 ║
║ 65 │ 65 ║  307 │  307 ║ 26337 │ 28913 ║  396 │  394 ║  744 │  739 ║  734 │  713 ║
║ 76 │ 76 ║  409 │  409 ║ 34421 │ 31429 ║  486 │  489 ║  941 │  950 ║  914 │  933 ║
║ 87 │ 87 ║  512 │  512 ║ 32131 │ 31235 ║  588 │  576 ║ 1167 │ 1167 ║ 1138 │ 1150 ║
║    │    ║      │      ║       │  sum: ║ 3952 │ 3974 ║ 7451 │ 7500 ║ 7249 │ 7288 ║
║    │    ║      │      ║       │   +%: ║  +88 │  +88 ║   +0 │   +0 ║   +2 │   +2 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⠭⠭⢛⣛⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠉⠁⢀⠔⡪⠥⠤⠭⡒⢄⠈⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⢀⢔⠕⠉⠀⠀⠀⠀⠈⠢⡑⢄⠀⠀⠀⠈⠉⠑⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢔⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⡪⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢕⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢔⠕⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠪⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠵⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠭⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠭⡒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠪⣒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣗⠞⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠪⢕⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 8
  173093 in 31403ms
╔═════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 87 │ 87 ║ -512 │ -512 ║ 15601 │ 16065 ║   102 │    52 ║     2 │     5 ║     5 │     5 ║
║ 76 │ 76 ║ -409 │ -409 ║ 16377 │ 16161 ║    96 │    50 ║     2 │     5 ║     5 │     5 ║
║ 65 │ 65 ║ -307 │ -307 ║ 15956 │ 16214 ║    93 │    50 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║ -204 │ -204 ║ 15510 │ 14618 ║    84 │    45 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 15506 │ 15934 ║    77 │    43 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 14143 │ 14059 ║    40 │    26 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 15456 │ 15884 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║  204 │  204 ║ 17199 │ 16047 ║    80 │    45 ║     2 │     5 ║     5 │     5 ║
║ 65 │ 65 ║  307 │  307 ║ 14267 │ 14785 ║    90 │    51 ║     2 │     5 ║     5 │     5 ║
║ 76 │ 76 ║  409 │  409 ║ 16377 │ 16161 ║    92 │    50 ║     2 │     5 ║     5 │     5 ║
║ 87 │ 87 ║  512 │  512 ║ 15601 │ 16065 ║    98 │    52 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║       │  sum: ║   925 │   507 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║       │   +%: ║    +0 │    +0 ║ +4104 │  +821 ║ +1581 │  +821 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 16`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 16
  264681 in 40285ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║  length   ║   precision   ║     count     ║  BigInteger   ║    BigInt     ║     int[]     ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 140 │ 140 ║ -1024 │ -1024 ║ 23875 │ 24835 ║  2242 │  2152 ║  4336 │  4266 ║  4180 │  4052 ║
║ 119 │ 119 ║  -819 │  -819 ║ 24437 │ 24165 ║  1777 │  1667 ║  3681 │  3463 ║  3514 │  3290 ║
║  97 │  97 ║  -614 │  -614 ║ 19313 │ 22785 ║  1241 │  1254 ║  2909 │  2859 ║  2815 │  2797 ║
║  76 │  76 ║  -409 │  -409 ║ 29111 │ 24619 ║   876 │   861 ║  1860 │  1922 ║  1833 │  1901 ║
║  55 │  55 ║  -204 │  -204 ║ 23774 │ 24778 ║   490 │   504 ║   971 │  1013 ║   973 │   994 ║
║  34 │  34 ║     0 │     0 ║ 22611 │ 21267 ║   216 │   319 ║   389 │   369 ║   380 │   338 ║
║  55 │  55 ║   204 │   204 ║ 23724 │ 24728 ║   493 │   536 ║   973 │  1015 ║   967 │  1006 ║
║  76 │  76 ║   409 │   409 ║ 29111 │ 24619 ║   878 │   886 ║  1854 │  1920 ║  1836 │  1895 ║
║  97 │  97 ║   614 │   614 ║ 19313 │ 22785 ║  1255 │  1250 ║  2989 │  3038 ║  2816 │  2813 ║
║ 119 │ 119 ║   819 │   819 ║ 24437 │ 24165 ║  1765 │  1733 ║  3869 │  3591 ║  3517 │  3331 ║
║ 140 │ 140 ║  1024 │  1024 ║ 23875 │ 24835 ║  2292 │  2190 ║  4437 │  4388 ║  4136 │  4127 ║
║     │     ║       │       ║       │  sum: ║ 13525 │ 13352 ║ 28268 │ 27844 ║ 26967 │ 26544 ║
║     │     ║       │       ║       │   +%: ║  +109 │  +108 ║    +0 │    +0 ║    +4 │    +4 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠛⠛⠛⢉⡉⠙⠛⠛⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⡠⠚⠉⠉⠛⠦⡀⠀⠀⠀⠈⠉⠒⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠈⢢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠱⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⣔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⣒⠭⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠈⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⠤⠒⠊⣁⠤⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⡠⠒⠉⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⢁⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 16
  146377 in 43992ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║               ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║  length   ║   precision   ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 140 │ 140 ║ -1024 │ -1024 ║ 14699 │ 13607 ║   111 │    55 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 13057 │ 13703 ║   111 │    56 ║     2 │     5 ║     5 │     5 ║
║  97 │  97 ║  -614 │  -614 ║ 12889 │ 12371 ║   100 │    52 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║  -409 │  -409 ║ 12205 │ 13525 ║   102 │    52 ║     2 │     5 ║     5 │     5 ║
║  55 │  55 ║  -204 │  -204 ║ 13806 │ 13632 ║    85 │    45 ║     2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 12015 │ 11651 ║    56 │    33 ║     2 │     5 ║     5 │     5 ║
║  55 │  55 ║   204 │   204 ║ 13756 │ 13582 ║    81 │    45 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║   409 │   409 ║ 12205 │ 13525 ║    97 │    52 ║     2 │     5 ║     5 │     5 ║
║  97 │  97 ║   614 │   614 ║ 12889 │ 12371 ║    96 │    52 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 13057 │ 13703 ║   106 │    56 ║     2 │     5 ║     5 │     5 ║
║ 140 │ 140 ║  1024 │  1024 ║ 14699 │ 13607 ║   107 │    55 ║     2 │     5 ║     5 │     5 ║
║     │     ║       │       ║       │  sum: ║  1052 │   553 ║    22 │    55 ║    55 │    55 ║
║     │     ║       │       ║       │   +%: ║    +0 │    +0 ║ +4681 │  +905 ║ +1812 │  +905 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 32`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 32
  137393 in 58669ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║  length   ║   precision   ║     count     ║  BigInteger   ║    BigInt     ║     int[]     ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 246 │ 246 ║ -2048 │ -2048 ║ 14851 │ 12815 ║  9146 │  8881 ║ 15484 │ 14274 ║ 14785 │ 13656 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 12157 │ 12625 ║  6916 │  6982 ║ 11440 │ 11558 ║ 10869 │ 11011 ║
║ 161 │ 161 ║ -1228 │ -1228 ║ 10145 │ 11429 ║  5662 │  5499 ║  9834 │  9766 ║  9355 │  9348 ║
║ 119 │ 119 ║  -819 │  -819 ║  9259 │ 13159 ║  3309 │  3670 ║  8297 │  8575 ║  7945 │  8252 ║
║  76 │  76 ║  -409 │  -409 ║ 15938 │ 12522 ║  1489 │  1572 ║  3383 │  3684 ║  3351 │  3631 ║
║  34 │  34 ║     0 │     0 ║ 11643 │ 11243 ║   473 │   621 ║   934 │  1024 ║   905 │   979 ║
║  76 │  76 ║   409 │   409 ║ 15888 │ 12472 ║  1501 │  1608 ║  3391 │  3726 ║  3359 │  3840 ║
║ 119 │ 119 ║   819 │   819 ║  9259 │ 13159 ║  3340 │  3673 ║  8542 │  8869 ║  7977 │  8272 ║
║ 161 │ 161 ║  1228 │  1228 ║ 10145 │ 11429 ║  5883 │  5641 ║ 10067 │ 10013 ║  9371 │  9376 ║
║ 204 │ 204 ║  1638 │  1638 ║ 12157 │ 12625 ║  6697 │  7100 ║ 11670 │ 11765 ║ 10900 │ 11069 ║
║ 246 │ 246 ║  2048 │  2048 ║ 14851 │ 12815 ║  9258 │  8636 ║ 15725 │ 14594 ║ 14905 │ 13718 ║
║     │     ║       │       ║       │  sum: ║ 53674 │ 53883 ║ 98767 │ 97848 ║ 93722 │ 93152 ║
║     │     ║       │       ║       │   +%: ║   +84 │   +81 ║    +0 │    +0 ║    +5 │    +5 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⠭⠝⠛⠋⠉⠉⡉⠉⠉⠛⠫⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⡠⠚⠉⠉⠑⠦⡀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠘⢄⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠣⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠎⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠥⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡬⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢔⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠕⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠒⢊⠥⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠉⠑⠒⠒⠒⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠊⢉⣁⠤⠤⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠤⠤⠤⠤⠤⢄⣀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠒⠉⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⡀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠔⠉⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠊⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 32
  63441 in 41622ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║               ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║  length   ║   precision   ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 246 │ 246 ║ -2048 │ -2048 ║ 8495 │ 5835 ║   141 │    79 ║     2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 3831 │ 5803 ║   175 │    94 ║     2 │     5 ║     5 │     5 ║
║ 161 │ 161 ║ -1228 │ -1228 ║ 6183 │ 5229 ║   125 │    68 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 5621 │ 5927 ║   120 │    62 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║  -409 │  -409 ║ 4888 │ 5652 ║   104 │    53 ║     2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 4355 │ 5499 ║    80 │    46 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║   409 │   409 ║ 4838 │ 5602 ║    99 │    53 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 5621 │ 5927 ║   115 │    62 ║     2 │     5 ║     5 │     5 ║
║ 161 │ 161 ║  1228 │  1228 ║ 6183 │ 5229 ║   121 │    68 ║     2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 3831 │ 5803 ║   170 │    94 ║     2 │     5 ║     5 │     5 ║
║ 246 │ 246 ║  2048 │  2048 ║ 8495 │ 5835 ║   138 │    79 ║     2 │     5 ║     5 │     5 ║
║     │     ║       │       ║      │ sum: ║  1388 │   758 ║    22 │    55 ║    55 │    55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +6209 │ +1278 ║ +2423 │ +1278 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 64`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 64
  63673 in 85017ms
╔═══════════╦═══════════════╦═════════════╦═════════════════╦═════════════════╦═════════════════╗
║  length   ║   precision   ║    count    ║   BigInteger    ║     BigInt      ║      int[]      ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬════════╤════════╬════════╤════════╬════════╤════════╣
║ 459 │ 459 ║ -4096 │ -4096 ║ 6727 │ 5695 ║  32919 │  33061 ║  49065 │  50393 ║  47273 │  48792 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 4613 │ 5757 ║  25661 │  28098 ║  37963 │  39695 ║  36538 │  38029 ║
║ 289 │ 289 ║ -2457 │ -2457 ║ 6741 │ 5609 ║  21506 │  21822 ║  31845 │  31599 ║  30637 │  30297 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 4267 │ 5615 ║  17895 │  17456 ║  27172 │  26247 ║  26132 │  25330 ║
║ 119 │ 119 ║  -819 │  -819 ║ 5638 │ 5850 ║  11844 │   9966 ║  22182 │  21843 ║  21450 │  21328 ║
║  34 │  34 ║     0 │     0 ║ 6651 │ 5571 ║   1649 │   1799 ║   3670 │   3779 ║   3635 │   3675 ║
║ 119 │ 119 ║   819 │   819 ║ 5588 │ 5800 ║  11566 │  10112 ║  22282 │  22501 ║  21661 │  21488 ║
║ 204 │ 204 ║  1638 │  1638 ║ 4267 │ 5615 ║  17759 │  17135 ║  27391 │  26691 ║  26000 │  25274 ║
║ 289 │ 289 ║  2457 │  2457 ║ 6741 │ 5609 ║  21350 │  22095 ║  32192 │  31847 ║  30536 │  30584 ║
║ 374 │ 374 ║  3276 │  3276 ║ 4613 │ 5757 ║  26126 │  28490 ║  38346 │  39955 ║  36722 │  38137 ║
║ 459 │ 459 ║  4096 │  4096 ║ 6727 │ 5695 ║  32235 │  33237 ║  48974 │  50911 ║  47252 │  48947 ║
║     │     ║       │       ║      │ sum: ║ 220510 │ 223271 ║ 341082 │ 345461 ║ 327836 │ 331881 ║
║     │     ║       │       ║      │  +%: ║    +54 │    +54 ║     +0 │     +0 ║     +4 │     +4 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩════════╧════════╩════════╧════════╩════════╧════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠋⠉⠉⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⢀⠔⠲⡄⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠀⠀⠀⢠⠂⠀⠀⠈⢆⠀⠀⠀⠈⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⢀⠃⠀⠀⠀⠀⠈⠆⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠆⠀⠀⠀⠀⠀⠀⠘⡄⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠀⠀⠀⠀⠀⠀⠀⠀⠰⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⢤⠮⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢕⡢⢄⣀⣀⣀⡠⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⣊⡩⠥⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠢⠤⠤⠤⠤⠤⣈⡉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⢊⡡⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⡉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⢉⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠔⠊⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⠤⠊⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠔⢁⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 64
  30301 in 52866ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦════════════════╦═══════════════╗
║           ║               ║             ║  BigInteger   ║     BigInt     ║     int[]     ║
║  length   ║   precision   ║    count    ║     T │ int[] ║     T │ int[]  ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬════════╤═══════╬═══════╤═══════╣
║ 459 │ 459 ║ -4096 │ -4096 ║ 3475 │ 2619 ║   341 │   238 ║      2 │     5 ║     5 │     5 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 1677 │ 2723 ║   339 │   220 ║      2 │     5 ║     5 │     5 ║
║ 289 │ 289 ║ -2457 │ -2457 ║ 2967 │ 2463 ║   223 │   143 ║      2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 2085 │ 2753 ║   224 │   137 ║      2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 2946 │ 2704 ║   149 │    87 ║      2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 2951 │ 2727 ║    88 │    49 ║      2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 2896 │ 2654 ║   145 │    88 ║      2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 2085 │ 2753 ║   219 │   137 ║      2 │     5 ║     5 │     5 ║
║ 289 │ 289 ║  2457 │  2457 ║ 2967 │ 2463 ║   219 │   143 ║      2 │     5 ║     5 │     5 ║
║ 374 │ 374 ║  3276 │  3276 ║ 1677 │ 2723 ║   334 │   220 ║      2 │     5 ║     5 │     5 ║
║ 459 │ 459 ║  4096 │  4096 ║ 3475 │ 2619 ║   337 │   238 ║      2 │     5 ║     5 │     5 ║
║     │     ║       │       ║      │ sum: ║  2618 │  1700 ║     22 │    55 ║    55 │    55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +11800 │ +2990 ║ +4660 │ +2990 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩════════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 128`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 128
  28737 in 117634ms
╔═══════════╦═══════════════╦═════════════╦═════════════════╦═══════════════════╦═══════════════════╗
║  length   ║   precision   ║    count    ║   BigInteger    ║      BigInt       ║       int[]       ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬════════╤════════╬═════════╤═════════╬═════════╤═════════╣
║ 884 │ 884 ║ -8192 │ -8192 ║ 2015 │ 2483 ║ 117724 │ 112059 ║  146429 │  142027 ║  140097 │  142095 ║
║ 714 │ 714 ║ -6553 │ -6553 ║ 2609 │ 2521 ║  83179 │  81935 ║  122844 │  125426 ║  118188 │  120845 ║
║ 544 │ 544 ║ -4915 │ -4915 ║ 1517 │ 2409 ║  77596 │  68110 ║  109916 │  102789 ║  109101 │  100663 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 3379 │ 2607 ║  58804 │  51665 ║   83965 │   75660 ║   81704 │   72452 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 2114 │ 2538 ║  42645 │  39834 ║   74696 │   66007 ║   72037 │   64369 ║
║  34 │  34 ║     0 │     0 ║ 4419 │ 2571 ║   6142 │   5228 ║   29261 │   17846 ║   28452 │   17378 ║
║ 204 │ 204 ║  1638 │  1638 ║ 2064 │ 2488 ║  43371 │  41143 ║   75217 │   68021 ║   72239 │   65304 ║
║ 374 │ 374 ║  3276 │  3276 ║ 3379 │ 2607 ║  57179 │  51926 ║   84445 │   76323 ║   80016 │   73317 ║
║ 544 │ 544 ║  4915 │  4915 ║ 1517 │ 2409 ║  78908 │  65587 ║  113832 │  102386 ║  107544 │   97678 ║
║ 714 │ 714 ║  6553 │  6553 ║ 2609 │ 2521 ║  84990 │  84295 ║  124389 │  125591 ║  121957 │  119237 ║
║ 884 │ 884 ║  8192 │  8192 ║ 2015 │ 2483 ║ 119544 │ 110210 ║  147465 │  149467 ║  141859 │  139400 ║
║     │     ║       │       ║      │ sum: ║ 770082 │ 711992 ║ 1112459 │ 1051543 ║ 1073194 │ 1012738 ║
║     │     ║       │       ║      │  +%: ║    +44 │    +47 ║      +0 │      +0 ║      +3 │      +3 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩════════╧════════╩═════════╧═════════╩═════════╧═════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠛⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⢀⣀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⢀⠜⠉⠉⠳⣄⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠋⠀⠀⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠃⠀⠀⠀⠀⠀⠀⠈⠥⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⡠⠔⠊⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠂⠀⠀⠀⠀⠀⠀⠀⠀⠈⠥⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠴⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢓⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⢄⣀⣀⣀⠤⢔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠪⡒⠤⣀⣀⣀⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢒⠭⠒⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠒⠒⠢⠤⡉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⢤⠶⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⣉⠭⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣈⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠤⠒⢉⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⣊⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠈⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 128
  14401 in 84832ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦═════════════════╦═════════════════╗
║           ║               ║             ║  BigInteger   ║     BigInt      ║      int[]      ║
║  length   ║   precision   ║    count    ║     T │ int[] ║      T │ int[]  ║      T │ int[]  ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬════════╤════════╬════════╤════════╣
║ 884 │ 884 ║ -8192 │ -8192 ║ 1573 │ 1291 ║  1584 │  1312 ║      2 │      5 ║      5 │      5 ║
║ 714 │ 714 ║ -6553 │ -6553 ║ 1027 │ 1227 ║  1359 │  1107 ║      2 │      5 ║      5 │      5 ║
║ 544 │ 544 ║ -4915 │ -4915 ║  621 │ 1043 ║   845 │   636 ║      2 │      5 ║      5 │      5 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 1635 │ 1189 ║   502 │   379 ║      2 │      5 ║      5 │      5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 1082 │ 1192 ║   427 │   310 ║      2 │      5 ║      5 │      5 ║
║  34 │  34 ║     0 │     0 ║ 1475 │ 1467 ║   165 │   101 ║      2 │      5 ║      5 │      5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 1032 │ 1142 ║   424 │   312 ║      2 │      5 ║      5 │      5 ║
║ 374 │ 374 ║  3276 │  3276 ║ 1635 │ 1189 ║   498 │   379 ║      2 │      5 ║      5 │      5 ║
║ 544 │ 544 ║  4915 │  4915 ║  621 │ 1043 ║   839 │   636 ║      2 │      5 ║      5 │      5 ║
║ 714 │ 714 ║  6553 │  6553 ║ 1027 │ 1227 ║  1354 │  1107 ║      2 │      5 ║      5 │      5 ║
║ 884 │ 884 ║  8192 │  8192 ║ 1573 │ 1291 ║  1580 │  1312 ║      2 │      5 ║      5 │      5 ║
║     │     ║       │       ║      │ sum: ║  9577 │  7591 ║     22 │     55 ║     55 │     55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +43431 │ +13701 ║ +17312 │ +13701 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩════════╧════════╩════════╧════════╝
```

##### `mul(int,int)`

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int,int)
  2640441 in 6859ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   55 │   48 ║  24 │  28 ║  22 │  24 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   59 │   49 ║  23 │  30 ║  22 │  26 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   54 │   52 ║  20 │  31 ║  19 │  29 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   48 │   50 ║  39 │  31 ║  36 │  28 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   47 │   48 ║  42 │  30 ║  36 │  28 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   47 │   58 ║  42 │  28 ║  36 │  26 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   49 │   65 ║  41 │  29 ║  36 │  28 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   52 │   61 ║  30 │  30 ║  27 │  29 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   59 │   55 ║  23 │  29 ║  22 │  27 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   58 │   49 ║  23 │  28 ║  23 │  25 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   53 │   49 ║  23 │  27 ║  21 │  25 ║
║    │    ║     │     ║        │   sum: ║  581 │  584 ║ 330 │ 321 ║ 300 │ 295 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +76 │ +81 ║ +93 │ +97 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣹
⡏⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⣒⣒⠶⢖⣒⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⠒⠒⢒⣒⣒⣒⣒⣒⣒⣒⣊⣉⣉⣉⣁⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠉⠢⡉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠒⠉⣁⡠⠤⠔⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡈⠒⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠔⠒⠒⠉⠁⢀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int,int)
  8361 in 454ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     9 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    34 ║    34 │    34 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +155 ║  +211 │  +155 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(int)`

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int)
  2640441 in 6574ms
╔═════════╦═══════════╦═════════════════╦═════════════╦════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt   ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤═════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   60 │   60 ║   29 │  33 ║   17 │   27 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   71 │   64 ║   27 │  32 ║   17 │   24 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   63 │   69 ║   23 │  33 ║   15 │   24 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   55 │   73 ║   36 │  33 ║   34 │   24 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   68 │   60 ║   40 │  32 ║   37 │   23 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   54 │   62 ║   35 │  29 ║   32 │   22 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   66 │   67 ║   42 │  31 ║   44 │   24 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   60 │   62 ║   31 │  31 ║   24 │   23 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   66 │   58 ║   25 │  30 ║   15 │   24 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   68 │   54 ║   27 │  31 ║   17 │   24 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   60 │   52 ║   29 │  30 ║   17 │   23 ║
║    │    ║     │     ║        │   sum: ║  691 │  681 ║  344 │ 345 ║  269 │  262 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +100 │ +97 ║ +156 │ +159 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧═════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠭⠭⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⡩⠭⠭⠭⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠒⠊⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⢀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠒⠒⠤⠤⠤⠤⢄⣈⣁⣀⣀⡠⠤⠤⠤⠤⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣸
⣇⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠈⠉⠒⠢⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int)
  8361 in 251ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     9 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    34 ║    34 │    34 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +155 ║  +211 │  +155 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(int,long)`

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int,long)
  946929 in 3412ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   95 │  125 ║   26 │   30 ║   35 │   41 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   96 │  124 ║   25 │   31 ║   33 │   41 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   88 │  125 ║   25 │   34 ║   38 │   45 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   92 │  125 ║   37 │   34 ║   52 │   47 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   92 │  128 ║   37 │   35 ║   44 │   47 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   93 │   67 ║   45 │   26 ║   47 │   29 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   90 │   60 ║   42 │   25 ║   46 │   29 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   88 │   53 ║   37 │   27 ║   51 │   31 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   89 │   62 ║   21 │   31 ║   32 │   41 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   98 │   61 ║   26 │   32 ║   33 │   42 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   93 │   58 ║   25 │   30 ║   33 │   40 ║
║    │    ║     │     ║        │   sum: ║ 1014 │  988 ║  346 │  335 ║  444 │  433 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +193 │ +194 ║ +128 │ +128 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠔⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⠤⠤⠤⠤⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int,long)
  13117 in 624ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    24 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    36 │    24 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    20 │    14 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    12 │     8 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     9 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    26 │    19 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    35 │    24 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    38 │    25 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   245 │   173 ║    11 │    31 ║    31 │    31 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2127 │  +458 ║  +690 │  +458 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(long)`

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(long)
  946929 in 3268ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   76 │   74 ║   26 │   37 ║   21 │   30 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   73 │   80 ║   22 │   37 ║   19 │   30 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   72 │   71 ║   31 │   30 ║   25 │   23 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   74 │   74 ║   46 │   28 ║   34 │   22 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   68 │   76 ║   34 │   29 ║   25 │   22 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   72 │   76 ║   35 │   34 ║   26 │   19 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   72 │   72 ║   39 │   24 ║   27 │   19 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   74 │   65 ║   49 │   25 ║   36 │   20 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   77 │   75 ║   24 │   34 ║   19 │   27 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   72 │   77 ║   24 │   39 ║   19 │   30 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   75 │   69 ║   28 │   37 ║   23 │   28 ║
║    │    ║     │     ║        │   sum: ║  805 │  809 ║  358 │  354 ║  274 │  270 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +124 │ +128 ║ +193 │ +199 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⠉⠉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠢⠤⠤⠤⠤⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⣀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⢀⣀⣀⣀⣀⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(long)
  13117 in 567ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    36 │    22 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    13 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    18 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    36 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   228 │   155 ║    11 │    30 ║    30 │    30 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1972 │  +416 ║  +660 │  +416 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T,T): 1`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 1
  33393 in 208ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     40 ║       -64 ║  3303 ║         58 ║     77 ║    70 ║
║     39 ║       -51 ║  2997 ║         58 ║     73 ║    61 ║
║     38 ║       -38 ║  2766 ║         41 ║     51 ║    47 ║
║     36 ║       -25 ║  3022 ║         34 ║     43 ║    36 ║
║     35 ║       -12 ║  3047 ║         25 ║     33 ║    30 ║
║     34 ║         0 ║  2610 ║         29 ║     37 ║    29 ║
║     35 ║        12 ║  2972 ║         25 ║     34 ║    30 ║
║     36 ║        25 ║  3047 ║         38 ║     53 ║    40 ║
║     38 ║        38 ║  2741 ║         48 ║     54 ║    48 ║
║     39 ║        51 ║  2997 ║         60 ║     80 ║    64 ║
║     40 ║        64 ║  2791 ║         58 ║     77 ║    70 ║
║        ║           ║  sum: ║        474 ║    612 ║   525 ║
║        ║           ║   +%: ║        +29 ║     +0 ║   +16 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠝⠋⠉⣉⣉⠭⠛⠉⢉⣉⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠻⣭⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⢉⡩⠝⠒⠊⠉⠉⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⢀⡠⠤⠤⣀⠀⠀⠀⠑⠫⡒⠢⠤⠤⠤⠤⠤⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠒⠉⠁⠀⠀⠀⠀⠑⢄⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡠⠊⠁⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠁⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⢀⣀⣀⡠⠤⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⣀⠤⠔⠊⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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

[heap allocation]
mul(T,T): 1
  17009 in 823ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     40 ║       -64 ║  1639 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  1461 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     38 ║       -38 ║  1358 ║    35 │    21 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  1486 ║    24 │    15 ║     1 │     3 ║     3 │     3 ║
║     35 ║       -12 ║  1511 ║     8 │     5 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  1330 ║     6 │     5 ║     1 │     3 ║     3 │     3 ║
║     35 ║        12 ║  1436 ║     6 │     6 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  1511 ║    25 │    17 ║     1 │     3 ║     3 │     3 ║
║     38 ║        38 ║  1333 ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  1461 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║     40 ║        64 ║  1383 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   284 │   179 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2481 │  +442 ║  +760 │  +442 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 2`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 2
  107377 in 1074ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        127 ║    161 ║   158 ║
║     44 ║      -102 ║  9605 ║         94 ║    126 ║   122 ║
║     41 ║       -76 ║  9454 ║         73 ║     97 ║    84 ║
║     39 ║       -51 ║  9166 ║         53 ║     70 ║    57 ║
║     36 ║       -25 ║  8398 ║         38 ║     43 ║    38 ║
║     34 ║         0 ║  8523 ║         28 ║     36 ║    32 ║
║     36 ║        25 ║  8348 ║         40 ║     45 ║    43 ║
║     39 ║        51 ║  9191 ║         55 ║     72 ║    61 ║
║     41 ║        76 ║  9501 ║         73 ║     98 ║    87 ║
║     44 ║       102 ║  9693 ║         97 ║    137 ║   121 ║
║     47 ║       128 ║ 11735 ║        130 ║    174 ║   155 ║
║        ║           ║  sum: ║        808 ║   1059 ║   958 ║
║        ║           ║   +%: ║        +31 ║     +0 ║   +10 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠝⠛⢉⡩⠝⠋⠉⠉⢉⡩⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⡉⠙⠫⠭⣉⠉⠛⠛⠭⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠊⠉⠀⢀⡠⠔⠊⠁⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠉⠒⠢⢄⡀⠀⠀⠈⠉⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⢀⠤⠊⠁⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⢊⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⣀⢤⡪⠕⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣤⡲⠝⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠉⠑⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡷⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⣀⣀⣀⣀⣀⣀⣸
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

[heap allocation]
mul(T,T): 2
  54001 in 3320ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    13 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     6 │     5 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    21 │    14 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    33 │    20 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    36 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    40 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   348 │   205 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3063 │  +521 ║  +954 │  +521 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 4`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 4
  239385 in 5405ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     60 ║      -256 ║ 27655 ║        243 ║    413 ║   396 ║
║     55 ║      -204 ║ 23817 ║        221 ║    315 ║   298 ║
║     49 ║      -153 ║ 22290 ║        163 ║    215 ║   201 ║
║     44 ║      -102 ║ 19226 ║        104 ║    142 ║   127 ║
║     39 ║       -51 ║ 20110 ║         62 ║     81 ║    67 ║
║     34 ║         0 ║ 14411 ║         43 ║     54 ║    45 ║
║     39 ║        51 ║ 17788 ║         62 ║     84 ║    72 ║
║     44 ║       102 ║ 21811 ║        117 ║    155 ║   140 ║
║     49 ║       153 ║ 19705 ║        166 ║    223 ║   203 ║
║     55 ║       204 ║ 23817 ║        219 ║    320 ║   299 ║
║     60 ║       256 ║ 27655 ║        246 ║    425 ║   397 ║
║        ║           ║  sum: ║       1646 ║   2427 ║  2245 ║
║        ║           ║   +%: ║        +47 ║     +0 ║    +8 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠝⠛⢉⡩⠝⢋⡩⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠫⠭⣛⠭⣉⠙⠛⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⣀⡠⠔⢊⡡⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠭⣒⠤⢄⡀⠈⠉⠑⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⣀⠤⢒⣉⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠬⣉⡒⠢⠤⣀⡀⠀⠀⠈⠉⠑⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢒⡩⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢒⠥⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⢒⡡⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠔⡩⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠔⡩⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠔⡪⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡈⠑⠤⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⣀⣀⣀⣀⣀⣀⣀⣸
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

[heap allocation]
mul(T,T): 4
  117781 in 9411ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     60 ║      -256 ║ 13019 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 12005 ║    44 │    24 ║     1 │     3 ║     3 │     3 ║
║     49 ║      -153 ║ 11460 ║    42 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  8748 ║    39 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  9046 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  8989 ║     9 │     7 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  8182 ║    32 │    20 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║ 10029 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     49 ║       153 ║ 10179 ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 12005 ║    42 │    24 ║     1 │     3 ║     3 │     3 ║
║     60 ║       256 ║ 13019 ║    45 │    25 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   411 │   236 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3636 │  +615 ║ +1145 │  +615 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 8`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 8
  374241 in 18199ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     87 ║      -512 ║ 33715 ║        688 ║   1294 ║  1256 ║
║     76 ║      -409 ║ 37557 ║        469 ║    879 ║   855 ║
║     65 ║      -307 ║ 33986 ║        308 ║    554 ║   531 ║
║     55 ║      -204 ║ 30894 ║        202 ║    328 ║   308 ║
║     44 ║      -102 ║ 36174 ║         94 ║    149 ║   130 ║
║     34 ║         0 ║ 28539 ║         47 ║     75 ║    62 ║
║     44 ║       102 ║ 36124 ║         97 ║    151 ║   132 ║
║     55 ║       204 ║ 35039 ║        213 ║    348 ║   322 ║
║     65 ║       307 ║ 29841 ║        316 ║    576 ║   548 ║
║     76 ║       409 ║ 37557 ║        470 ║    885 ║   851 ║
║     87 ║       512 ║ 33715 ║        684 ║   1308 ║  1260 ║
║        ║           ║  sum: ║       3588 ║   6547 ║  6255 ║
║        ║           ║   +%: ║        +82 ║     +0 ║    +4 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠝⠛⠛⣉⣩⣭⠿⠿⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠻⠿⣭⣍⣉⠛⠛⠫⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠒⠊⠉⠉⠁⠀⠀⠀⢀⣠⣔⠶⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠶⣤⣀⡀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⠤⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡴⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠚⠵⣢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡴⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠚⠵⣢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠶⠓⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠫⢖⡤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠮⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠚⠵⡢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣠⠮⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠳⣢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣠⡲⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠵⡢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⡴⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢕⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⢔⠝⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠪⡒⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 8
  186081 in 21146ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     87 ║      -512 ║ 16597 ║    51 │    26 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║ 16539 ║    50 │    26 ║     1 │     3 ║     3 │     3 ║
║     65 ║      -307 ║ 18696 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 16796 ║    44 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║ 17086 ║    38 │    21 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║ 13603 ║    22 │    14 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║ 17036 ║    36 │    21 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 18915 ║    42 │    23 ║     1 │     3 ║     3 │     3 ║
║     65 ║       307 ║ 16577 ║    46 │    26 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║ 16539 ║    48 │    26 ║     1 │     3 ║     3 │     3 ║
║     87 ║       512 ║ 16597 ║    49 │    26 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   473 │   257 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +4200 │  +678 ║ +1333 │  +678 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 16`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 16
  313449 in 37439ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║    140 ║     -1024 ║ 27051 ║       2138 ║   4593 ║  4536 ║
║    119 ║      -819 ║ 26053 ║       1430 ║   2903 ║  2880 ║
║     97 ║      -614 ║ 30285 ║        841 ║   1659 ║  1650 ║
║     76 ║      -409 ║ 30855 ║        436 ║    867 ║   863 ║
║     55 ║      -204 ║ 31622 ║        175 ║    349 ║   343 ║
║     34 ║         0 ║ 20667 ║         55 ║    141 ║   139 ║
║     55 ║       204 ║ 31572 ║        177 ║    353 ║   346 ║
║     76 ║       409 ║ 30855 ║        435 ║    870 ║   866 ║
║     97 ║       614 ║ 30285 ║        842 ║   1651 ║  1653 ║
║    119 ║       819 ║ 26053 ║       1424 ║   2902 ║  2886 ║
║    140 ║      1024 ║ 27051 ║       2135 ║   4609 ║  4549 ║
║        ║           ║  sum: ║      10088 ║  20897 ║ 20711 ║
║        ║           ║   +%: ║       +107 ║     +0 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠭⠭⠝⠛⠛⠛⠋⢉⣉⡭⠭⠝⠛⠛⠛⠋⠉⠉⠙⠛⠛⠛⠫⠭⠭⣉⣉⠉⠛⠛⠛⠛⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠲⠤⣀⡀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⣀⣀⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⠴⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 16
  148941 in 31098ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║    140 ║     -1024 ║ 13343 ║    58 │    28 ║     1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║ 11103 ║    55 │    27 ║     1 │     3 ║     3 │     3 ║
║     97 ║      -614 ║ 11741 ║    52 │    27 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║ 15553 ║    49 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 17022 ║    43 │    23 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║ 10367 ║    30 │    17 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 16972 ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║ 15553 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     97 ║       614 ║ 11741 ║    50 │    27 ║     1 │     3 ║     3 │     3 ║
║    119 ║       819 ║ 11103 ║    53 │    27 ║     1 │     3 ║     3 │     3 ║
║    140 ║      1024 ║ 13343 ║    56 │    28 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   534 │   277 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +4754 │  +739 ║ +1518 │  +739 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 32`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 32
  168969 in 55276ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║    246 ║     -2048 ║ 13111 ║       8337 ║  16554 ║ 16404 ║
║    204 ║     -1638 ║ 14229 ║       5793 ║  10413 ║ 10282 ║
║    161 ║     -1228 ║ 13965 ║       3105 ║   6498 ║  6392 ║
║    119 ║      -819 ║ 17323 ║       1479 ║   3078 ║  3032 ║
║     76 ║      -409 ║ 19062 ║        383 ║    754 ║   755 ║
║     34 ║         0 ║ 12539 ║        109 ║    210 ║   209 ║
║     76 ║       409 ║ 19012 ║        388 ║    761 ║   754 ║
║    119 ║       819 ║ 17323 ║       1478 ║   3084 ║  3049 ║
║    161 ║      1228 ║ 13965 ║       3091 ║   6515 ║  6415 ║
║    204 ║      1638 ║ 14229 ║       5634 ║  10407 ║ 10284 ║
║    246 ║      2048 ║ 13111 ║       8519 ║  16564 ║ 16421 ║
║        ║           ║  sum: ║      38316 ║  74838 ║ 73997 ║
║        ║           ║   +%: ║        +95 ║     +0 ║    +1 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠝⠛⠛⠛⣉⡩⠭⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠫⠭⣉⠛⠛⠛⠫⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⠦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠴⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠲⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⡤⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⢦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 32
  83069 in 42098ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║    246 ║     -2048 ║  7779 ║    97 │    54 ║     1 │     3 ║     3 │     3 ║
║    204 ║     -1638 ║  6779 ║    84 │    46 ║     1 │     3 ║     3 │     3 ║
║    161 ║     -1228 ║  6769 ║    62 │    31 ║     1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║  8565 ║    55 │    27 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║  7726 ║    48 │    25 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  6783 ║    37 │    21 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║  7676 ║    46 │    25 ║     1 │     3 ║     3 │     3 ║
║    119 ║       819 ║  8565 ║    53 │    27 ║     1 │     3 ║     3 │     3 ║
║    161 ║      1228 ║  6769 ║    60 │    31 ║     1 │     3 ║     3 │     3 ║
║    204 ║      1638 ║  6779 ║    82 │    46 ║     1 │     3 ║     3 │     3 ║
║    246 ║      2048 ║  7779 ║    95 │    54 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   719 │   387 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +6436 │ +1072 ║ +2078 │ +1072 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 64`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 64
  79361 in 75043ms
╔════════╦═══════════╦═══════╦════════════╦════════╦════════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[]  ║
╠════════╬═══════════╬═══════╬════════════╬════════╬════════╣
║    459 ║     -4096 ║  4611 ║      30618 ║  64026 ║  63898 ║
║    374 ║     -3276 ║  7573 ║      20108 ║  39956 ║  39611 ║
║    289 ║     -2457 ║  5897 ║      13262 ║  23817 ║  23682 ║
║    204 ║     -1638 ║  7651 ║       4922 ║   9936 ║   9786 ║
║    119 ║      -819 ║  8526 ║       1189 ║   2482 ║   2451 ║
║     34 ║         0 ║  9795 ║        190 ║    355 ║    358 ║
║    119 ║       819 ║  8476 ║       1197 ║   2482 ║   2466 ║
║    204 ║      1638 ║  7651 ║       4939 ║   9950 ║   9841 ║
║    289 ║      2457 ║  5897 ║      13078 ║  23826 ║  23658 ║
║    374 ║      3276 ║  7573 ║      20033 ║  39826 ║  39711 ║
║    459 ║      4096 ║  4611 ║      31007 ║  64158 ║  63942 ║
║        ║           ║  sum: ║     140543 ║ 280814 ║ 279404 ║
║        ║           ║   +%: ║        +99 ║     +0 ║     +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠛⠛⠛⢛⣉⠭⠽⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠫⠭⢍⣙⠛⠛⠛⠭⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⢀⡠⠴⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⠢⢄⣀⠀⠀⠀⠉⠑⠒⠢⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⣀⠴⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⠴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 64
  42393 in 57114ms
╔════════╦═══════════╦═══════╦═══════════════╦════════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║     BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[]  ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬════════════════╬═══════════════╣
║    459 ║     -4096 ║  3395 ║   317 │   232 ║      1 │     3 ║     3 │     3 ║
║    374 ║     -3276 ║  3227 ║   191 │   129 ║      1 │     3 ║     3 │     3 ║
║    289 ║     -2457 ║  3323 ║   123 │    74 ║      1 │     3 ║     3 │     3 ║
║    204 ║     -1638 ║  4465 ║    80 │    43 ║      1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║  3980 ║    56 │    28 ║      1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4563 ║    43 │    23 ║      1 │     3 ║     3 │     3 ║
║    119 ║       819 ║  3930 ║    54 │    28 ║      1 │     3 ║     3 │     3 ║
║    204 ║      1638 ║  4465 ║    78 │    43 ║      1 │     3 ║     3 │     3 ║
║    289 ║      2457 ║  3323 ║   121 │    74 ║      1 │     3 ║     3 │     3 ║
║    374 ║      3276 ║  3227 ║   189 │   129 ║      1 │     3 ║     3 │     3 ║
║    459 ║      4096 ║  3395 ║   315 │   232 ║      1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║  1567 │  1035 ║     11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +14145 │ +3036 ║ +4648 │ +3036 ║
╚════════╩═══════════╩═══════╩═══════════════╩════════════════╩═══════════════╝
```

##### `mul(T,T): 128`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 128
  40921 in 134684ms
╔════════╦═══════════╦═══════╦════════════╦════════╦════════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[]  ║
╠════════╬═══════════╬═══════╬════════════╬════════╬════════╣
║    884 ║     -8192 ║  2919 ║      99812 ║ 140967 ║ 135301 ║
║    714 ║     -6553 ║  3013 ║      75159 ║ 131859 ║ 125228 ║
║    544 ║     -4915 ║  2805 ║      42267 ║  92193 ║  91071 ║
║    374 ║     -3276 ║  3415 ║      24543 ║  48739 ║  48742 ║
║    204 ║     -1638 ║  5018 ║       5945 ║  11658 ║  11556 ║
║     34 ║         0 ║  5531 ║        386 ║    758 ║    766 ║
║    204 ║      1638 ║  4968 ║       5828 ║  11738 ║  11595 ║
║    374 ║      3276 ║  3415 ║      23798 ║  48926 ║  48581 ║
║    544 ║      4915 ║  2805 ║      43389 ║  92691 ║  91011 ║
║    714 ║      6553 ║  3013 ║      76385 ║ 130872 ║ 126977 ║
║    884 ║      8192 ║  2919 ║      97151 ║ 139767 ║ 135915 ║
║        ║           ║  sum: ║     494663 ║ 850168 ║ 826743 ║
║        ║           ║   +%: ║        +71 ║     +0 ║     +2 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠝⠛⣛⠭⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠭⣙⠛⠛⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠉⠁⠀⠀⣠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⠀⠀⠀⠉⠑⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⡲⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠶⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⢒⣉⡡⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠛⠵⣒⡤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡏⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
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

##### `umul(int[],long)`

This test validates the correctness of `[BigIntMultiplication.umul(long)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.

```
[runtime performance]
umul(int[],long)
  289329 in 167ms
╔═════════╦═══════════╦═══════════════╦═══════════╦═══════════╗
║ length  ║ precision ║     count     ║    MPN    ║  BigInt   ║
╠════╤════╬═════╤═════╬═══════╤═══════╬═════╤═════╬═════╤═════╣
║ 36 │ 36 ║ -19 │ -19 ║ 37923 │ 37923 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║ -15 │ -15 ║ 22700 │ 22700 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║ -11 │ -11 ║ 30346 │ 30346 ║  14 │  14 ║   9 │   9 ║
║ 34 │ 34 ║  -7 │  -7 ║ 22723 │ 22723 ║  17 │  15 ║  11 │  10 ║
║ 34 │ 34 ║  -3 │  -3 ║ 30484 │ 30484 ║  16 │  15 ║  11 │  11 ║
║ 34 │ 34 ║   0 │   0 ║ 15284 │ 15284 ║  14 │  15 ║  10 │   9 ║
║ 34 │ 34 ║   3 │   3 ║ 30346 │ 30346 ║  17 │  14 ║  11 │   8 ║
║ 34 │ 34 ║   7 │   7 ║ 22700 │ 22700 ║  14 │  15 ║   8 │   8 ║
║ 35 │ 35 ║  11 │  11 ║ 30300 │ 30300 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║  15 │  15 ║ 22700 │ 22700 ║  13 │  14 ║   8 │   9 ║
║ 36 │ 36 ║  19 │  19 ║ 22723 │ 22723 ║  13 │  14 ║   8 │  10 ║
║    │    ║     │     ║       │  sum: ║ 157 │ 158 ║ 100 │ 101 ║
║    │    ║     │     ║       │   +%: ║  +0 │  +0 ║ +57 │ +56 ║
╚════╧════╩═════╧═════╩═══════╧═══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠭⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⡠⠤⠤⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⣀⣀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
umul(int[],long)
  7749 in 72ms
╔═════════╦═══════════╦════════════╦═══════════════╦═══════════════╗
║         ║           ║            ║      MPN      ║    BigInt     ║
║ length  ║ precision ║   count    ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬═════╤══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 36 ║ -19 │ -19 ║ 873 │  873 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -15 │ -15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -11 │ -11 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -7 │  -7 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 844 │  844 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   0 │   0 ║ 464 │  464 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   3 │   3 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   7 │   7 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  11 │  11 ║ 660 │  660 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  15 │  15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 36 │ 36 ║  19 │  19 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │ sum: ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │  +%: ║    +0 │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩═════╧══════╩═══════╧═══════╩═══════╧═══════╝
```

#### Division ([`BigIntDivisionTest`][BigIntDivisionTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═════════════╗
║                  ║ BigInteger  ║   BigInt    ║    int[]    ║
╠══════════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     div(int,int) ║   +0 │   +0 ║ +114 │ +118 ║ +104 │ +108 ║
║    div(int,long) ║   +0 │   +0 ║ +248 │ +232 ║ +250 │ +261 ║
║         div(int) ║   +0 │   +0 ║ +173 │ +172 ║ +101 │  +98 ║
║        div(long) ║   +0 │   +0 ║ +145 │ +147 ║ +144 │ +143 ║
║           div(T) ║   +0 │   +0 ║  +29 │  +29 ║  +44 │  +44 ║
║  divRem(int,int) ║   +0 │   +0 ║  +52 │  +61 ║  +61 │  +69 ║
║ divRem(int,long) ║   +0 │   +0 ║ +184 │ +175 ║ +186 │ +179 ║
║      divRem(int) ║   +0 │   +0 ║ +144 │ +145 ║ +136 │ +134 ║
║     divRem(long) ║   +0 │   +0 ║ +141 │ +142 ║ +143 │ +142 ║
║        divRem(T) ║   +0 │   +0 ║   +2 │   +3 ║   +7 │   +9 ║
╚══════════════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║     div(int,int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║    div(int,long) ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
║         div(int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║        div(long) ║    +0 │    +0 ║ +1836 │  +572 ║  +868 │  +572 ║
║           div(T) ║    +0 │    +0 ║ +5200 │  +809 ║ +1321 │  +809 ║
║  divRem(int,int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║ divRem(int,long) ║    +0 │    +0 ║ +2000 │  +654 ║  +950 │  +654 ║
║      divRem(int) ║    +0 │    +0 ║  +727 │  +196 ║  +264 │  +196 ║
║     divRem(long) ║    +0 │    +0 ║ +1827 │  +572 ║  +863 │  +572 ║
║        divRem(T) ║    +0 │    +0 ║ +2540 │  +646 ║ +1062 │  +646 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `div(int,int)`

Divide by an unsigned `int`.

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
div(int,int)
  2640441 in 7301ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║  155 │   88 ║   57 │   47 ║   53 │   50 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║  100 │   88 ║   48 │   50 ║   50 │   51 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   91 │   89 ║   43 │   49 ║   46 │   51 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   54 │   95 ║   32 │   51 ║   35 │   53 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   66 │   95 ║   32 │   49 ║   33 │   52 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   80 │   95 ║   33 │   37 ║   34 │   39 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   48 │  128 ║   32 │   38 ║   34 │   41 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   73 │   88 ║   38 │   38 ║   42 │   40 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║  105 │   88 ║   49 │   39 ║   52 │   40 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║  131 │   91 ║   55 │   38 ║   62 │   40 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║  101 │   84 ║   48 │   35 ║   51 │   37 ║
║    │    ║     │     ║        │   sum: ║ 1004 │ 1029 ║  467 │  471 ║  492 │  494 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +114 │ +118 ║ +104 │ +108 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⠭⢭⣭⣝⠿⠿⠿⠿⠿⠿⠿⠿⠿⣛⣛⣛⣛⡛⠛⠭⠭⠭⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⡀⢀⣀⣀⣀⣀⣀⣀⣀⡠⠤⢤⣤⣤⣒⣒⣒⣒⣒⣊⣉⣉⣉⣉⠭⠭⠭⠔⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠒⠒⠫⠭⠭⠭⠶⣒⣒⡒⠒⠒⠒⠒⠒⠒⠒⠉⢉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⠀⠈⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⡠⠤⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
div(int,int)
  8361 in 1319ms
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

##### `div(int,long)`

Divide by an unsigned `long`.

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
div(int,long)
  946929 in 4880ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  328 │  284 ║  104 │   80 ║   98 │   75 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  330 │  495 ║   95 │  122 ║   92 │   72 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  329 │  368 ║   83 │   81 ║   80 │   80 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  222 │  315 ║   68 │   84 ║   65 │   82 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  127 │  300 ║   30 │   81 ║   29 │   77 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║  251 │  162 ║   33 │   72 ║   32 │   69 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║  141 │  153 ║   39 │   75 ║   43 │   70 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  225 │  216 ║   80 │   74 ║   75 │   70 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  301 │  291 ║   89 │   81 ║  125 │   77 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  281 │  221 ║   91 │   75 ║   89 │   78 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  380 │  195 ║  125 │   78 ║  103 │   81 ║
║    │    ║     │     ║        │   sum: ║ 2915 │ 3000 ║  837 │  903 ║  831 │  831 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +248 │ +232 ║ +250 │ +261 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣩⣭⣭⣭⠭⠭⠭⠭⠭⠭⣭⣭⣭⣍⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣧⣒⠒⠒⠒⠒⠒⠉⠉⠉⠉⢉⣉⠵⠶⠒⠒⠒⠒⠒⠒⠒⠒⠛⠛⠛⠛⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠙⠛⠛⠛⠭⠶⠶⠤⢤⣤⠤⠤⠤⠤⠤⠤⠤⠤⣤⣤⠤⠤⠤⠤⢤⣤⣤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠉⠉⠑⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠉⠉⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⠤⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
div(int,long)
  13117 in 959ms
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

##### `div(int)`

Divide by a signed `int`.

```
[runtime performance]
div(int)
  2640441 in 5746ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║   int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   94 │   81 ║   34 │   29 ║   49 │  37 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   95 │   84 ║   34 │   30 ║   48 │  39 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   94 │   86 ║   32 │   30 ║   48 │  40 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   61 │   86 ║   22 │   30 ║   26 │  42 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   46 │   75 ║   20 │   31 ║   23 │  42 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   43 │   74 ║   20 │   30 ║   23 │  43 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   56 │   82 ║   22 │   28 ║   25 │  42 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   81 │   78 ║   28 │   29 ║   37 │  40 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   98 │   79 ║   35 │   29 ║   48 │  39 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   96 │   76 ║   34 │   28 ║   49 │  38 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   96 │   73 ║   34 │   27 ║   50 │  38 ║
║    │    ║     │     ║        │   sum: ║  860 │  874 ║  315 │  321 ║  426 │ 440 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +173 │ +172 ║ +101 │ +98 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠑⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠢⠤⠤⠤⠒⠒⠒⠒⠒⠉⠑⠒⠒⠒⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⡀⠀⣀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠒⠒⠒⠢⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
div(int)
  8361 in 226ms
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

##### `div(long)`

Divide by a signed `long`.

```
[runtime performance]
div(long)
  946929 in 2977ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  186 │  142 ║   76 │   55 ║   77 │   53 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  154 │  169 ║   58 │   55 ║   60 │   55 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  131 │  120 ║   41 │   50 ║   43 │   51 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   86 │  104 ║   42 │   51 ║   41 │   51 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   49 │  103 ║   28 │   49 ║   24 │   51 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   45 │  100 ║   26 │   44 ║   24 │   47 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   55 │  100 ║   29 │   48 ║   29 │   49 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  102 │   97 ║   41 │   47 ║   42 │   49 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  138 │  151 ║   46 │   52 ║   49 │   52 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  163 │  157 ║   63 │   53 ║   65 │   53 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  191 │  133 ║   79 │   53 ║   78 │   53 ║
║    │    ║     │     ║        │   sum: ║ 1300 │ 1376 ║  529 │  557 ║  532 │  564 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +145 │ +147 ║ +144 │ +143 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⢤⣤⣤⣤⣤⣤⣤⣤⣄⣀⣀⣀⣀⣠⠤⠤⠴⠶⠒⠚⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠒⠲⠶⠶⠶⠤⢤⣤⣤⣤⣤⣤⣤⣤⣄⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠶⠒⠒⠛⠋⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠙⠛⠓⠒⠲⠦⠤⠤⢤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠑⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
div(long)
  13117 in 523ms
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
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    24 │    17 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    32 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    35 │    23 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   213 │   148 ║    11 │    22 ║    22 │    22 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1836 │  +572 ║  +868 │  +572 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `div(T)`

Divide by `T`.

```
[runtime performance]
div(T)
  107377 in 967ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  449 │  189 ║  344 │  154 ║  315 │  139 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  344 │  225 ║  263 │  186 ║  239 │  164 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  254 │  257 ║  194 │  207 ║  181 │  190 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  140 │  296 ║  110 │  233 ║   98 │  222 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   66 │  242 ║   65 │  197 ║   47 │  176 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   39 │  163 ║   37 │  127 ║   27 │  118 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   75 │  319 ║   68 │  234 ║   49 │  205 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  153 │  353 ║  118 │  256 ║  103 │  223 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  276 │  306 ║  207 │  224 ║  187 │  203 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  370 │  263 ║  281 │  196 ║  254 │  172 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  471 │  211 ║  356 │  163 ║  328 │  146 ║
║    │    ║      │     ║       │  sum: ║ 2637 │ 2824 ║ 2043 │ 2177 ║ 1828 │ 1958 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +29 │  +29 ║  +44 │  +44 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠉⣀⠤⠤⠤⢄⡈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠁⠀⣀⠔⠊⠀⢀⣀⡀⠀⠈⠑⢄⠀⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠀⢀⣀⠤⠔⠊⠀⢀⠔⠊⠁⠀⠈⠑⢄⠀⠀⠑⠤⡀⠀⠀⠀⠈⠉⠉⠒⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠒⠒⠒⠊⢉⣉⡡⠤⠔⠒⠒⠉⠉⠁⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠈⠑⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠔⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⢀⣀⣀⣀⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
div(T)
  54001 in 5267ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    57 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     3 ║     3 │     3 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   583 │   373 ║    11 │    41 ║    41 │    41 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5200 │  +809 ║ +1321 │  +809 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `divRem(int,int)`

Divide by an unsigned `int`, and return the remainder as an absolute unsigned `int`.

This test validates the value of the division result (not the remainder result).

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
divRem(int,int)
  2640441 in 5690ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   88 │   65 ║  46 │  48 ║  45 │  45 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   81 │   64 ║  45 │  47 ║  45 │  46 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   65 │   64 ║  39 │  47 ║  39 │  46 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   40 │   64 ║  34 │  47 ║  33 │  46 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   36 │   65 ║  38 │  46 ║  33 │  46 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   35 │   76 ║  40 │  35 ║  32 │  34 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   39 │   73 ║  38 │  35 ║  33 │  34 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   56 │   70 ║  36 │  36 ║  35 │  34 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   84 │   67 ║  47 │  37 ║  45 │  34 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   82 │   63 ║  46 │  38 ║  44 │  34 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   87 │   62 ║  46 │  38 ║  46 │  33 ║
║    │    ║     │     ║        │   sum: ║  693 │  733 ║ 455 │ 454 ║ 430 │ 432 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +52 │ +61 ║ +61 │ +69 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠫⠭⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠉⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⠤⠤⠔⠒⠒⠒⠒⠒⠒⠤⢄⡈⠉⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⣀⣀⣀⣀⠀⠀⠀⢀⣀⣀⣀⠤⢤⣔⣒⣒⠮⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⠉⠉⠉⠉⠉⠉⠀⠀⠀⢀⡠⠔⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡧⠤⠤⠤⠤⠭⠭⠛⠓⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠒⠉⠁⠀⠀⠀⠀⠈⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int,int)
  8361 in 230ms
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

##### `divRem(int,long)`

Divide by an unsigned `long`, and return the remainder as an absolute unsigned `long`.

This test validates the value of the division result (not the remainder result).

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
divRem(int,long)
  946929 in 2953ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  231 │  191 ║   83 │   55 ║   82 │   55 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  202 │  193 ║   69 │   56 ║   67 │   54 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  157 │  191 ║   59 │   58 ║   53 │   57 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  106 │  196 ║   50 │   61 ║   47 │   61 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   89 │  194 ║   19 │   61 ║   25 │   59 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   77 │  108 ║   16 │   58 ║   23 │   52 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   91 │  106 ║   28 │   57 ║   32 │   56 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  114 │  102 ║   54 │   56 ║   49 │   55 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  178 │  157 ║   59 │   57 ║   56 │   58 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  214 │  161 ║   74 │   58 ║   72 │   58 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  230 │  143 ║   83 │   56 ║   83 │   58 ║
║    │    ║     │     ║        │   sum: ║ 1689 │ 1742 ║  594 │  633 ║  589 │  623 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +184 │ +175 ║ +186 │ +179 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣤⣤⠴⠶⠶⠶⠶⠶⠤⠤⠤⠤⠤⠤⠤⣤⣤⣤⣤⣄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠴⠶⠶⠶⠖⠒⠚⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠛⠲⠶⢖⣒⣒⣒⡲⠶⠶⠶⠒⠒⠒⠒⠲⠶⠦⠤⠤⢤⣤⣤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⣀⣀⠤⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int,long)
  13117 in 602ms
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

##### `divRem(int)`

Divide by a signed `int`, and return the remainder as a signed `int`.

This test validates the value of the division result (not the remainder result).

```
[runtime performance]
divRem(int)
  2640441 in 5690ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   91 │   76 ║   35 │   32 ║   37 │   32 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   95 │   79 ║   37 │   32 ║   39 │   33 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   87 │   82 ║   33 │   32 ║   34 │   34 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   58 │   86 ║   24 │   32 ║   25 │   35 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   47 │   72 ║   23 │   32 ║   24 │   34 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   46 │   72 ║   27 │   31 ║   24 │   34 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   53 │   80 ║   25 │   32 ║   25 │   34 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   76 │   81 ║   29 │   32 ║   31 │   33 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║  100 │   78 ║   38 │   31 ║   41 │   32 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   94 │   74 ║   37 │   31 ║   38 │   31 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   92 │   73 ║   35 │   31 ║   37 │   31 ║
║    │    ║     │     ║        │   sum: ║  839 │  853 ║  343 │  348 ║  355 │  363 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +144 │ +145 ║ +136 │ +134 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡷⣒⣢⣤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⢒⣒⣒⣒⣉⠭⠭⠥⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠛⠛⠓⠒⠒⠒⠒⠛⠛⠉⠉⠉⠑⠒⠒⠒⠒⠫⠭⠭⠭⣉⣑⣒⡒⠒⠤⠤⠤⠤⠤⠤⢤⣤⣤⡤⠤⠤⠤⠤⠤⠤⠤⠶⠶⠶⠶⠶⠶⠶⠶⠶⢾
⡇⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int)
  8361 in 228ms
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

##### `divRem(long)`

Divide by a signed `long`, and return the remainder as a signed `long`.

This test validates the value of the division result (not the remainder result).

```
[runtime performance]
divRem(long)
  946929 in 2907ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  174 │  133 ║   72 │   52 ║   74 │   51 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  151 │  159 ║   60 │   54 ║   60 │   54 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  131 │  115 ║   44 │   50 ║   45 │   50 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   81 │  102 ║   38 │   49 ║   37 │   51 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   44 │   99 ║   25 │   47 ║   23 │   48 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   41 │   98 ║   23 │   45 ║   21 │   45 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   53 │   99 ║   29 │   46 ║   28 │   47 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   99 │   97 ║   39 │   46 ║   39 │   47 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  139 │  145 ║   49 │   52 ║   49 │   51 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  158 │  145 ║   65 │   52 ║   64 │   51 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  176 │  127 ║   72 │   51 ║   73 │   49 ║
║    │    ║     │     ║        │   sum: ║ 1247 │ 1319 ║  516 │  544 ║  513 │  544 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +141 │ +142 ║ +143 │ +142 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠶⠶⠖⠚⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠒⠒⠒⠒⠲⠤⠤⠤⣤⣤⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠒⠚⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠓⠒⠒⠶⠶⠤⢤⣤⣤⣄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠢⠤⠤⠤⠤⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(long)
  13117 in 527ms
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

##### `divRem(T)`

Divide by `T`, and return the remainder as a new `T`.

This test validates the value of the division result (not the remainder result).

```
[runtime performance]
divRem(T)
  107377 in 916ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  429 │  179 ║  380 │  195 ║  366 │  187 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  334 │  226 ║  304 │  229 ║  290 │  222 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  255 │  253 ║  240 │  253 ║  237 │  246 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  141 │  281 ║  152 │  279 ║  146 │  266 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   59 │  237 ║   89 │  234 ║   79 │  222 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   23 │  157 ║   72 │  164 ║   54 │  144 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   61 │  302 ║   97 │  274 ║   87 │  250 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  153 │  331 ║  167 │  291 ║  155 │  273 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  274 │  292 ║  265 │  264 ║  248 │  253 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  357 │  250 ║  319 │  237 ║  305 │  227 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  447 │  205 ║  397 │  201 ║  379 │  192 ║
║    │    ║      │     ║       │  sum: ║ 2533 │ 2713 ║ 2482 │ 2621 ║ 2346 │ 2482 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║   +2 │   +3 ║   +7 │   +9 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⣊⡡⠤⠬⢕⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡲⠕⠉⠀⠀⠀⠀⠀⠈⠓⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠴⠚⠉⣀⠤⠒⠊⠉⠉⠉⠑⠢⢄⠀⠫⡢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠔⢊⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣐⢄⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⣔⠞⡫⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠦⣀⣀⠀⠈⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⢔⣒⡩⠴⠝⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠫⣑⠒⠢⠤⣀⡀⠉⠑⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⡠⠤⠤⠔⠒⠒⠒⢒⣊⣉⠭⠭⠭⠓⣒⠭⠝⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠈⠑⠒⠤⢄⣀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠔⠒⠒⠊⠉⠉⢁⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠢⠤⠤⢄⣀⡀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⣀⣀⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(T)
  54001 in 5082ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    72 │    43 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    57 │    37 ║     2 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     2 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     2 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     2 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     2 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     2 │     5 ║     5 │     5 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    64 │    39 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │     ║      │ sum: ║   581 │   373 ║    22 │    50 ║    50 │    50 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +2540 │  +646 ║ +1062 │  +646 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

#### Remainder ([`BigIntRemainderTest`][BigIntRemainderTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═════════════╗
║                  ║ BigInteger  ║   BigInt    ║    int[]    ║
╠══════════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     rem(int,int) ║   +0 │   +0 ║ +140 │ +141 ║ +120 │ +121 ║
║   rem(int,int):T ║   +0 │   +0 ║  +67 │  +70 ║  +80 │  +86 ║
║    rem(int,long) ║   +0 │   +0 ║ +161 │ +158 ║ +254 │ +242 ║
║  rem(int,long):T ║   +0 │   +0 ║ +201 │ +189 ║ +255 │ +238 ║
║         rem(int) ║   +0 │   +0 ║ +174 │ +170 ║ +129 │ +128 ║
║       rem(int):T ║   +0 │   +0 ║ +175 │ +175 ║ +102 │ +101 ║
║        rem(long) ║   +0 │   +0 ║ +144 │ +142 ║ +160 │ +158 ║
║      rem(long):T ║   +0 │   +0 ║ +155 │ +152 ║ +105 │ +105 ║
║           rem(T) ║   +0 │   +0 ║  +47 │  +47 ║  +64 │  +63 ║
║  divRem(int,int) ║   +0 │   +0 ║  +97 │ +101 ║  +90 │  +95 ║
║ divRem(int,long) ║   +0 │   +0 ║ +235 │ +220 ║ +233 │ +222 ║
║      divRem(int) ║   +0 │   +0 ║ +184 │ +186 ║ +172 │ +174 ║
║     divRem(long) ║   +0 │   +0 ║ +157 │ +154 ║ +103 │ +102 ║
║        divRem(T) ║   +0 │   +0 ║  +10 │  +11 ║  +25 │  +26 ║
║           mod(T) ║   +0 │   +0 ║  +57 │  +55 ║  +74 │  +72 ║
╚══════════════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║     rem(int,int) ║    +0 │    +0 ║  +736 │  +155 ║  +441 │  +335 ║
║   rem(int,int):T ║    +0 │    +0 ║  +736 │  +164 ║  +228 │  +164 ║
║    rem(int,long) ║    +0 │    +0 ║ +2027 │  +514 ║ +1362 │  +937 ║
║  rem(int,long):T ║    +0 │    +0 ║ +2027 │  +514 ║  +766 │  +514 ║
║         rem(int) ║    +0 │    +0 ║  +736 │  +164 ║  +441 │  +335 ║
║       rem(int):T ║    +0 │    +0 ║  +736 │  +164 ║  +228 │  +164 ║
║        rem(long) ║    +0 │    +0 ║ +1836 │  +448 ║ +1231 │  +825 ║
║      rem(long):T ║    +0 │    +0 ║ +1836 │  +448 ║  +688 │  +448 ║
║           rem(T) ║    +0 │    +0 ║ +5209 │  +747 ║ +1227 │  +747 ║
║  divRem(int,int) ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
║ divRem(int,long) ║    +0 │    +0 ║ +2027 │  +943 ║ +1362 │  +943 ║
║      divRem(int) ║    +0 │    +0 ║  +736 │  +335 ║  +441 │  +335 ║
║     divRem(long) ║    +0 │    +0 ║ +1836 │  +825 ║ +1231 │  +825 ║
║        divRem(T) ║    +0 │    +0 ║ +2554 │  +578 ║  +961 │  +578 ║
║           mod(T) ║    +0 │    +0 ║ +5136 │  +768 ║ +1209 │  +768 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `rem(int,int)`

Remainder from division by an unsigned `int`, setting to `this`, and returning the value as an absolute unsigned `int`.

This test validates the value of the returned `int` remainder result (not the `T` remainder result).

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
rem(int,int)
  2640441 in 6474ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║  111 │   84 ║   44 │   42 ║   48 │   46 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║  104 │   85 ║   44 │   42 ║   47 │   45 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   96 │   90 ║   37 │   42 ║   41 │   46 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   79 │   98 ║   30 │   43 ║   34 │   47 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   91 │   98 ║   33 │   43 ║   35 │   48 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║  101 │   96 ║   34 │   39 ║   35 │   40 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   70 │  109 ║   32 │   36 ║   36 │   40 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   71 │   97 ║   33 │   35 ║   38 │   39 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   80 │   90 ║   42 │   34 ║   45 │   37 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║  101 │   87 ║   44 │   34 ║   48 │   37 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   98 │   86 ║   44 │   33 ║   47 │   36 ║
║    │    ║     │     ║        │   sum: ║ 1002 │ 1020 ║  417 │  423 ║  454 │  461 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +140 │ +141 ║ +120 │ +121 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠔⠒⠒⠊⠉⠉⠉⠀⢀⣀⣀⣀⣀⣀⣀⣀⣈⣉⣉⣉⡩⠥⠔⠒⠊⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣉⣉⣉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠊⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⣇⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠒⠒⠊⠉⠉⠉⠀⠉⠉⠉⠒⠒⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡏⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int,int)
  8361 in 1249ms
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

##### `rem(int,int):T`

Remainder from division by an unsigned `int`, setting to `this`, and returning the value as an absolute unsigned `int`.

This test validates the value of the `T` remainder result (not the returned `int` remainder result).

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
rem(int,int):T
  2640441 in 5551ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   82 │   64 ║  46 │  43 ║  42 │  42 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   74 │   63 ║  43 │  43 ║  40 │  41 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   64 │   63 ║  38 │  43 ║  36 │  41 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   49 │   64 ║  30 │  43 ║  28 │  42 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   47 │   64 ║  30 │  44 ║  28 │  41 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   49 │   73 ║  32 │  38 ║  29 │  34 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   48 │   73 ║  30 │  36 ║  28 │  31 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   58 │   70 ║  35 │  35 ║  32 │  31 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   73 │   68 ║  44 │  35 ║  40 │  31 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   77 │   66 ║  46 │  35 ║  42 │  30 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   81 │   65 ║  46 │  35 ║  43 │  30 ║
║    │    ║     │     ║        │   sum: ║  702 │  733 ║ 420 │ 430 ║ 388 │ 394 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +67 │ +70 ║ +80 │ +86 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⡩⠭⠭⠭⠝⠛⠛⠛⠛⠛⠫⠭⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠔⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⢉⣉⡩⠭⠝⠒⠒⠒⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠔⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠤⠒⠢⠤⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⡠⠤⠤⠔⠒⠒⠒⠒⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int,int):T
  8361 in 251ms
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

##### `rem(int,long)`

Remainder from division by an unsigned `long`, setting to `this`, and returning the value as an absolute unsigned `long`.

This test validates the value of the returned `long` remainder result (not the `T` remainder result).

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
rem(int,long)
  946929 in 3236ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  267 │  229 ║  105 │   83 ║   82 │   53 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  244 │  230 ║   91 │   82 ║   67 │   54 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  182 │  235 ║   67 │   86 ║   53 │   57 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  136 │  236 ║   60 │   89 ║   46 │   59 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  115 │  235 ║   36 │   89 ║   20 │   61 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║  111 │  126 ║   36 │   63 ║   20 │   53 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║  118 │  119 ║   41 │   58 ║   26 │   50 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  142 │  115 ║   59 │   60 ║   46 │   49 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  202 │  187 ║   75 │   65 ║   56 │   56 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  244 │  193 ║   96 │   65 ║   72 │   57 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  261 │  170 ║  106 │   63 ║   82 │   56 ║
║    │    ║     │     ║        │   sum: ║ 2022 │ 2075 ║  772 │  803 ║  570 │  605 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +161 │ +158 ║ +254 │ +242 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠔⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⣀⣀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int,long)
  13117 in 979ms
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

##### `rem(int,long):T`

Remainder from division by an unsigned `long`, setting to `this`, and returning the value as an absolute unsigned `long`.

This test validates the value of the `T` remainder result (not the returned `int` remainder result).

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

```
[runtime performance]
rem(int,long):T
  946929 in 2997ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  249 │  202 ║   92 │   59 ║   78 │   48 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  214 │  203 ║   77 │   60 ║   63 │   48 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  164 │  208 ║   53 │   62 ║   47 │   53 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  124 │  212 ║   42 │   64 ║   38 │   55 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║  107 │  211 ║   19 │   64 ║   16 │   55 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║  109 │  119 ║   21 │   58 ║   18 │   51 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║  107 │  121 ║   26 │   52 ║   22 │   47 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  129 │  110 ║   45 │   53 ║   40 │   45 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  184 │  172 ║   62 │   61 ║   50 │   52 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  219 │  176 ║   82 │   61 ║   68 │   53 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  242 │  154 ║   93 │   59 ║   80 │   51 ║
║    │    ║     │     ║        │   sum: ║ 1848 │ 1888 ║  612 │  653 ║  520 │  558 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +201 │ +189 ║ +255 │ +238 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⢒⣒⣉⠭⠭⠭⠤⠤⠤⠤⠬⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣑⡒⠒⠒⠢⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠁⠀⠀⠀⠀⢀⣀⣀⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠤⠤⠤⠤⠤⣀⣀⣀⣀⡀⠈⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠊⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⣀⠤⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int,long):T
  13117 in 581ms
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

##### `rem(int)`

Remainder from division by a signed `int`, setting to `this`, and returning the value as an `int`.

This test validates the value of the returned `int` remainder result (not the `T` remainder result).

```
[runtime performance]
rem(int)
  2640441 in 5589ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   93 │   79 ║   36 │   30 ║   40 │   34 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   86 │   79 ║   34 │   30 ║   39 │   35 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   73 │   82 ║   29 │   30 ║   34 │   35 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   60 │   83 ║   21 │   32 ║   29 │   37 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   58 │   80 ║   21 │   34 ║   28 │   38 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   61 │   82 ║   22 │   33 ║   27 │   38 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   64 │   87 ║   22 │   29 ║   29 │   36 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   74 │   82 ║   26 │   28 ║   32 │   35 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   91 │   84 ║   33 │   29 ║   39 │   35 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║  103 │   79 ║   36 │   28 ║   41 │   35 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║  103 │   77 ║   36 │   27 ║   40 │   34 ║
║    │    ║     │     ║        │   sum: ║  866 │  894 ║  316 │  330 ║  378 │  392 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +174 │ +170 ║ +129 │ +128 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠊⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡧⢄⣀⣀⣀⣀⣀⡀⢀⣀⣀⣀⡠⠤⠤⠤⠒⠒⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠢⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int)
  8361 in 239ms
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
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     7 │     7 ║     1 │     3 ║     2 │     2 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     7 │     7 ║     1 │     3 ║     2 │     2 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║     9 │     9 ║     1 │     3 ║     2 │     2 ║
║    │    ║     │     ║      │ sum: ║    92 │    74 ║    11 │    28 ║    17 │    17 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +736 │  +164 ║  +441 │  +335 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `rem(int):T`

Remainder from division by a signed `int`, setting to `this`, and returning the value as an `int`.

This test validates the value of the `T` remainder result (not the returned `int` remainder result).

```
[runtime performance]
rem(int):T
  2640441 in 5679ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║  102 │   80 ║   37 │   30 ║   49 │   40 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   92 │   84 ║   35 │   31 ║   45 │   43 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   81 │   86 ║   30 │   31 ║   40 │   42 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   66 │   96 ║   23 │   32 ║   35 │   43 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   61 │   81 ║   22 │   35 ║   33 │   45 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   61 │   80 ║   22 │   32 ║   32 │   44 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   64 │   93 ║   23 │   30 ║   34 │   41 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   81 │   86 ║   27 │   30 ║   37 │   42 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   91 │   83 ║   35 │   29 ║   45 │   42 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   99 │   83 ║   35 │   29 ║   46 │   40 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║  101 │   79 ║   37 │   29 ║   49 │   40 ║
║    │    ║     │     ║        │   sum: ║  899 │  931 ║  326 │  338 ║  445 │  462 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +175 │ +175 ║ +102 │ +101 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠢⠤⠤⠤⠔⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠒⠒⠢⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⠤⢄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠒⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(int):T
  8361 in 242ms
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

##### `rem(long)`

Remainder from division by a signed `long`, setting to `this`, and returning the value as an `long`.

This test validates the value of the returned `long` remainder result (not the `T` remainder result).

```
[runtime performance]
rem(long)
  946929 in 2898ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  189 │  162 ║   84 │   55 ║   76 │   57 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  170 │  187 ║   71 │   57 ║   65 │   59 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  151 │  129 ║   52 │   58 ║   50 │   52 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   96 │  108 ║   43 │   59 ║   41 │   49 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   64 │  112 ║   24 │   60 ║   25 │   53 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   63 │  108 ║   25 │   59 ║   25 │   52 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   66 │  104 ║   29 │   56 ║   28 │   49 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  113 │  103 ║   44 │   55 ║   42 │   48 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  162 │  171 ║   61 │   57 ║   57 │   55 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  173 │  175 ║   74 │   55 ║   68 │   56 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  196 │  154 ║   84 │   54 ║   78 │   55 ║
║    │    ║     │     ║        │   sum: ║ 1443 │ 1513 ║  591 │  625 ║  555 │  585 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +144 │ +142 ║ +160 │ +158 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠒⢒⣒⣒⣒⣊⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣒⠒⠒⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⠤⢤⣤⣤⣤⣤⠶⠶⠶⠶⠭⠭⠝⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠛⠛⠛⠭⠶⠶⠶⠶⢦⣤⣤⣤⣤⣤⣤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡏⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⣀⣀⣀⡀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(long)
  13117 in 544ms
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

##### `rem(long):T`

Remainder from division by a signed `long`, setting to `this`, and returning the value as an `long`.

This test validates the value of the `T` remainder result (not the returned `long` remainder result).

```
[runtime performance]
rem(long):T
  946929 in 2822ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  188 │  163 ║   82 │   55 ║   92 │   70 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  163 │  186 ║   67 │   57 ║   79 │   71 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  145 │  126 ║   48 │   55 ║   60 │   65 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   98 │  106 ║   39 │   54 ║   52 │   64 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   65 │  107 ║   22 │   56 ║   35 │   65 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   62 │  103 ║   21 │   52 ║   34 │   63 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   71 │  102 ║   26 │   48 ║   41 │   61 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  114 │   98 ║   40 │   48 ║   53 │   61 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  152 │  168 ║   55 │   56 ║   66 │   68 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  170 │  174 ║   72 │   55 ║   84 │   68 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  190 │  152 ║   83 │   53 ║   93 │   67 ║
║    │    ║     │     ║        │   sum: ║ 1418 │ 1485 ║  555 │  589 ║  689 │  723 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +155 │ +152 ║ +105 │ +105 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⣀⣀⣀⣀⡠⠤⠤⠤⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⡠⠤⠤⠤⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠢⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠊⠉⠉⠉⠉⠉⠀⠀⠉⠉⠉⠉⠉⠉⠉⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(long):T
  13117 in 539ms
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

##### `rem(T)`

Remainder from division by `T`.

```
[runtime performance]
rem(T)
  107377 in 878ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  451 │  210 ║  313 │  143 ║  290 │  130 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  364 │  248 ║  248 │  170 ║  228 │  157 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  279 │  283 ║  190 │  200 ║  168 │  176 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  157 │  310 ║  103 │  213 ║   89 │  200 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   75 │  255 ║   42 │  178 ║   39 │  163 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   42 │  157 ║   24 │  121 ║   20 │  110 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   83 │  320 ║   49 │  220 ║   40 │  192 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  174 │  356 ║  113 │  231 ║   98 │  205 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  307 │  321 ║  203 │  207 ║  178 │  186 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  388 │  277 ║  266 │  181 ║  242 │  163 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  472 │  236 ║  336 │  154 ║  303 │  137 ║
║    │    ║      │     ║       │  sum: ║ 2792 │ 2973 ║ 1887 │ 2018 ║ 1695 │ 1819 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +47 │  +47 ║  +64 │  +63 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⢒⡪⠕⠒⠊⠉⠑⠒⠬⡉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⢊⡩⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠉⠑⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠔⠒⠒⠉⣉⡠⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠑⠢⡀⠀⠀⠀⠈⠑⠢⠤⣀⣀⠀⠀⠈⠉⠉⠒⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⠤⠤⠤⠤⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠤⠤⠤⢄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠢⠤⠤⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
rem(T)
  54001 in 5199ms
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

##### `divRem(int,int)`

Divide by an unsigned `int`, and return the remainder as an absolute unsigned `int`.

This test validates the value of the remainder result (not the division result).

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

```
[runtime performance]
divRem(int,int)
  2640441 in 5597ms
╔═════════╦═══════════╦═════════════════╦═════════════╦════════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤══════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║  104 │   85 ║  49 │   50 ║  51 │  52 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║  101 │   85 ║  48 │   50 ║  50 │  51 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   97 │   89 ║  47 │   50 ║  49 │  52 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   68 │   87 ║  37 │   52 ║  37 │  53 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   68 │   87 ║  36 │   52 ║  36 │  53 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   69 │   91 ║  37 │   38 ║  37 │  41 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   70 │   93 ║  37 │   40 ║  39 │  41 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   83 │   93 ║  43 │   38 ║  45 │  40 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   94 │   91 ║  49 │   38 ║  51 │  39 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   95 │   86 ║  49 │   38 ║  51 │  39 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║  102 │   86 ║  50 │   37 ║  52 │  37 ║
║    │    ║     │     ║        │   sum: ║  951 │  973 ║ 482 │  483 ║ 498 │ 498 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +97 │ +101 ║ +90 │ +95 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧══════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠭⢛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⠛⠛⠛⠛⠭⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⠤⠤⠤⠔⣒⣒⡲⠶⠶⠶⠶⠶⠖⠒⠒⠒⠛⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠤⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⣏⣉⡩⠭⠶⠶⠶⠶⠮⠭⠭⠭⠭⠭⠭⠤⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int,int)
  8361 in 228ms
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

##### `divRem(int,long)`

Divide by an unsigned `long`, and return the remainder as an absolute unsigned `long`.

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

This test validates the value of the remainder result (not the division result).

```
[runtime performance]
divRem(int,long)
  946929 in 2983ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  242 │  197 ║   84 │   50 ║   82 │   50 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  214 │  198 ║   66 │   50 ║   66 │   49 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  158 │  201 ║   45 │   52 ║   46 │   53 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  117 │  203 ║   36 │   53 ║   39 │   54 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   95 │  213 ║   14 │   54 ║   15 │   55 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   95 │  113 ║   15 │   55 ║   14 │   51 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   97 │  106 ║   19 │   53 ║   21 │   52 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  122 │  103 ║   39 │   53 ║   39 │   51 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  177 │  169 ║   54 │   52 ║   53 │   52 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  215 │  173 ║   71 │   51 ║   70 │   51 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  238 │  156 ║   85 │   49 ║   85 │   50 ║
║    │    ║     │     ║        │   sum: ║ 1770 │ 1832 ║  528 │  572 ║  530 │  568 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +235 │ +220 ║ +233 │ +222 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣠⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⠶⠶⠶⠛⠛⠋⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠛⠓⠒⠒⠒⠒⠦⠤⠤⣄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠖⠒⠒⠒⠛⠛⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠒⠒⠒⠲⠶⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠤⠤⠤⢄⡠⠤⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⣇⣀⣀⠤⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int,long)
  13117 in 582ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    24 ║     1 │     2 ║     2 │     2 ║
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
║    │    ║     │     ║      │ sum: ║   234 │   167 ║    11 │    16 ║    16 │    16 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2027 │  +943 ║ +1362 │  +943 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `divRem(int)`

Divide by a signed `int`, and return the remainder as an `int`.

This test validates the value of the remainder result (not the division result).

```
[runtime performance]
divRem(int)
  2640441 in 5647ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║  109 │   91 ║   38 │   32 ║   39 │   33 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║  103 │   93 ║   39 │   32 ║   39 │   34 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   95 │   95 ║   31 │   32 ║   32 │   34 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   71 │  103 ║   24 │   34 ║   26 │   35 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 139984 ║   69 │   90 ║   24 │   33 ║   25 │   34 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 140005 ║   67 │   86 ║   24 │   34 ║   26 │   34 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   70 │   93 ║   24 │   33 ║   26 │   35 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   83 │   91 ║   28 │   32 ║   29 │   34 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║  102 │   90 ║   38 │   32 ║   40 │   33 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║  104 │   95 ║   38 │   31 ║   39 │   33 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║  112 │   94 ║   38 │   31 ║   41 │   33 ║
║    │    ║     │     ║        │   sum: ║  985 │ 1021 ║  346 │  356 ║  362 │  372 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +184 │ +186 ║ +172 │ +174 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⣩⣭⣭⣭⣭⣭⣭⣭⣭⣭⣭⣭⣭⣭⣭⣉⣉⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣭⣭⣭⣍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡷⠶⠶⠶⠶⠶⠶⠶⠶⠶⠶⠶⠭⠭⠝⠓⠒⠒⠒⠒⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠈⠉⠉⠉⠛⠛⠶⠶⠶⠶⠶⠶⠶⠶⠶⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠒⠒⠒⠊⠉⠑⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(int)
  8361 in 241ms
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

##### `divRem(long)`

Divide by a signed `long`, and return the remainder as an `long`.

This test validates the value of the remainder result (not the division result).

```
[runtime performance]
divRem(long)
  946929 in 2914ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║  194 │  168 ║   86 │   56 ║   99 │   72 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║  167 │  192 ║   66 │   58 ║   85 │   74 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║  156 │  132 ║   51 │   55 ║   67 │   70 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║  106 │  113 ║   42 │   55 ║   54 │   70 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   65 │  111 ║   22 │   56 ║   32 │   67 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   62 │  106 ║   22 │   53 ║   36 │   64 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   73 │  108 ║   27 │   56 ║   39 │   67 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║  128 │  103 ║   44 │   54 ║   57 │   67 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║  158 │  174 ║   55 │   56 ║   71 │   72 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║  173 │  177 ║   70 │   55 ║   89 │   72 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║  196 │  163 ║   88 │   53 ║   99 │   69 ║
║    │    ║     │     ║        │   sum: ║ 1478 │ 1547 ║  573 │  607 ║  728 │  764 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +157 │ +154 ║ +103 │ +102 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⢄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⠤⠤⠤⠤⠤⠒⠒⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠢⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(long)
  13117 in 525ms
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

##### `divRem(T)`

Divide by `T`, and return the remainder as a new `T`.

This test validates the value of the remainder result (not the division result).

```
[runtime performance]
divRem(T)
  107377 in 834ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  478 │  219 ║  399 │  214 ║  363 │  187 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  379 │  265 ║  328 │  242 ║  292 │  224 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  290 │  299 ║  261 │  267 ║  231 │  240 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  160 │  325 ║  164 │  300 ║  140 │  263 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   75 │  266 ║   96 │  256 ║   81 │  222 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   49 │  173 ║   78 │  173 ║   73 │  155 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   85 │  341 ║  113 │  297 ║   85 │  254 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  183 │  375 ║  186 │  314 ║  153 │  269 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  318 │  332 ║  278 │  278 ║  243 │  246 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  406 │  288 ║  342 │  247 ║  298 │  220 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  508 │  244 ║  414 │  215 ║  378 │  190 ║
║    │    ║      │     ║       │  sum: ║ 2931 │ 3127 ║ 2659 │ 2803 ║ 2337 │ 2470 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +10 │  +11 ║  +25 │  +26 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⡲⠋⢉⣀⠤⢄⡈⠫⡑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠀⢀⡠⢊⠤⠊⠁⠀⠀⠀⠈⠑⢔⣄⠀⠀⠈⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⡠⣔⠥⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠔⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⣀⠤⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⠤⠤⠤⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠔⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠔⡲⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢌⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠤⠔⠒⠒⠊⠉⠉⠁⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠉⠉⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠑⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⣀⡠⠤⠔⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
divRem(T)
  54001 in 5138ms
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

##### `mod(T)`

Modulus by `T`.

```
[runtime performance]
mod(T)
  107377 in 914ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │       ║  522 │    0 ║  344 │    0 ║  320 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │       ║  444 │    0 ║  281 │    0 ║  258 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │       ║  373 │    0 ║  229 │    0 ║  204 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │       ║  217 │    0 ║  125 │    0 ║  109 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │       ║  127 │    0 ║   66 │    0 ║   55 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 10779 ║   69 │  181 ║   36 │  138 ║   27 │  118 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 20926 ║   87 │  335 ║   50 │  218 ║   42 │  201 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 20450 ║  186 │  379 ║  111 │  239 ║   96 │  219 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │ 18142 ║  339 │  353 ║  221 │  220 ║  196 │  198 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │ 19246 ║  412 │  306 ║  279 │  194 ║  252 │  175 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │ 17234 ║  492 │  275 ║  338 │  169 ║  314 │  152 ║
║    │    ║      │     ║       │  sum: ║ 3268 │ 1829 ║ 2080 │ 1178 ║ 1873 │ 1063 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║  +57 │  +55 ║  +74 │  +72 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⣒⡲⠭⠛⠒⠒⠒⠒⠪⢍⡒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⢒⣊⠥⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⢊⣉⡩⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠉⠑⠒⠤⣀⠀⠀⠀⠉⠑⠒⠤⢄⣈⡉⠒⠢⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⠒⣒⣉⡩⠥⠤⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⢄⣈⡉⠉⠒⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⣀⡀⠈⠉⠉⠒⠒⠒⠢⠤⠤⠤⠤⢄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⠀⠀⠀⠈⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mod(T)
  54001 in 4975ms
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

#### Binary ([`BigIntBinaryTest`][BigIntBinaryTest])

**Summary**

```
[runtime performance]
╔═══════════╦═════════════╦═════════════╦═════════════╗
║           ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     or(T) ║   +0 │   +0 ║ +497 │ +420 ║ +773 │ +654 ║
║    xor(T) ║   +0 │   +0 ║ +331 │ +339 ║ +436 │ +444 ║
║    and(T) ║   +0 │   +0 ║ +281 │ +290 ║ +468 │ +481 ║
║ andNot(T) ║   +0 │   +0 ║ +322 │ +329 ║ +500 │ +511 ║
╚═══════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[runtime performance]
╔═══════╦════════════╦════════╦═══════╗
║       ║ BigInteger ║ BigInt ║ int[] ║
╠═══════╬════════════╬════════╬═══════╣
║ not() ║         +0 ║   +589 ║ +2784 ║
╚═══════╩════════════╩════════╩═══════╝

[heap allocation]
╔═══════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║  BigInteger   ║    BigInt     ║     int[]     ║
║           ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║     or(T) ║    +0 │    +0 ║ +5336 │  +793 ║ +1259 │  +793 ║
║     not() ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║    xor(T) ║    +0 │    +0 ║ +5318 │  +788 ║ +1254 │  +788 ║
║    and(T) ║    +0 │    +0 ║ +5290 │  +777 ║ +1247 │  +777 ║
║ andNot(T) ║    +0 │    +0 ║ +5290 │  +779 ║ +1247 │  +779 ║
╚═══════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `or(T)`

`or` with `T`.

```
[runtime performance]
or(T)
  107377 in 1531ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  232 │  184 ║   32 │   38 ║   24 │   25 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  192 │  184 ║   31 │   34 ║   24 │   26 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  182 │  185 ║   30 │   34 ║   22 │   24 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  161 │  173 ║   30 │   30 ║   22 │   20 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  134 │  172 ║   31 │   30 ║   19 │   20 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  452 │  163 ║   32 │   26 ║   22 │   17 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  113 │  145 ║   35 │   28 ║   22 │   18 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  141 │  148 ║   33 │   30 ║   21 │   21 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  147 │  152 ║   32 │   31 ║   21 │   22 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  157 │  154 ║   33 │   33 ║   22 │   23 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  186 │  158 ║   32 │   35 ║   21 │   25 ║
║    │    ║      │     ║       │  sum: ║ 2097 │ 1818 ║  351 │  349 ║  240 │  241 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +497 │ +420 ║ +773 │ +654 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠉⠈⠉⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠒⠒⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⣇⡠⠤⠤⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
or(T)
  54001 in 6597ms
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

##### `not()`

`not` with `T`.

```
[runtime performance]
not()
  107377 in 862ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        122 ║     16 ║     5 ║
║     44 ║      -102 ║  9605 ║        103 ║     19 ║     7 ║
║     41 ║       -76 ║  9454 ║         82 ║     13 ║     3 ║
║     39 ║       -51 ║  9166 ║         56 ║     10 ║     1 ║
║     36 ║       -25 ║  8398 ║         48 ║      8 ║     1 ║
║     34 ║         0 ║  8523 ║         41 ║      5 ║     1 ║
║     36 ║        25 ║  8348 ║         59 ║      9 ║     1 ║
║     39 ║        51 ║  9191 ║         71 ║     10 ║     1 ║
║     41 ║        76 ║  9501 ║        100 ║     15 ║     3 ║
║     44 ║       102 ║  9693 ║        128 ║     16 ║     5 ║
║     47 ║       128 ║ 11735 ║        142 ║     17 ║     5 ║
║        ║           ║  sum: ║        952 ║    138 ║    33 ║
║        ║           ║   +%: ║         +0 ║   +589 ║ +2784 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⠭⠭⠭⠭⠛⠛⠛⠛⠫⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠁⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠔⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⡠⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
not()
  54001 in 3027ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    24 ║     1 │     3 ║     3 │     3 ║
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
║        ║           ║  sum: ║   354 │   222 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `xor(T)`

`xor` with `T`.

```
[runtime performance]
xor(T)
  107377 in 1253ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  220 │  198 ║   36 │   49 ║   30 │   39 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  179 │  182 ║   37 │   44 ║   28 │   34 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  160 │  192 ║   36 │   44 ║   28 │   35 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  213 │  183 ║   57 │   39 ║   44 │   29 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  162 │  181 ║   36 │   38 ║   31 │   30 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  133 │  174 ║   30 │   30 ║   25 │   25 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  152 │  160 ║   40 │   32 ║   32 │   29 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  192 │  160 ║   59 │   38 ║   45 │   29 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  138 │  163 ║   36 │   38 ║   31 │   32 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  157 │  160 ║   36 │   39 ║   30 │   33 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  193 │  173 ║   37 │   47 ║   30 │   39 ║
║    │    ║      │     ║       │  sum: ║ 1899 │ 1926 ║  440 │  438 ║  354 │  354 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +331 │ +339 ║ +436 │ +444 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠛⠛⠛⠛⠛⣋⣉⣉⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣙⣛⡛⠛⠛⠛⠭⠭⠭⠭⠭⠭⠭⠭⠝⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⣀⣀⣀⡠⠤⠤⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⢄⣀⣀⣀⠤⠤⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
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
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⠊⠉⠉⠉⠑⠒⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠉⠉⠑⠒⠢⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⣀⣀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
xor(T)
  54001 in 5058ms
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
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    64 │    43 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    69 │    45 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    65 │    40 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   596 │   391 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5318 │  +788 ║ +1254 │  +788 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `and(T)`

`and` with `T`.

```
[runtime performance]
and(T)
  107377 in 930ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  221 │  173 ║   45 │   41 ║   31 │   31 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  189 │  167 ║   37 │   42 ║   28 │   29 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  170 │  162 ║   40 │   43 ║   30 │   28 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  135 │  154 ║   44 │   37 ║   30 │   24 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  121 │  153 ║   42 │   36 ║   28 │   24 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  114 │  155 ║   33 │   30 ║   22 │   22 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  104 │  139 ║   37 │   33 ║   22 │   23 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  116 │  149 ║   41 │   38 ║   25 │   25 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  139 │  142 ║   37 │   44 ║   23 │   27 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  149 │  144 ║   34 │   44 ║   23 │   28 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  178 │  142 ║   39 │   42 ║   26 │   28 ║
║    │    ║      │     ║       │  sum: ║ 1636 │ 1680 ║  429 │  430 ║  288 │  289 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +281 │ +290 ║ +468 │ +481 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡧⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠒⠊⠉⠁⠀⠀⠉⠉⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠔⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠢⠤⠤⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠔⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
and(T)
  54001 in 4922ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    70 │    41 ║     1 │     4 ║     4 │     4 ║
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
║    │    ║      │     ║      │ sum: ║   593 │   386 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5290 │  +777 ║ +1247 │  +777 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `andNot(T)`

`andNot` with `T`.

```
[runtime performance]
andNot(T)
  107377 in 1112ms
╔═════════╦════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║  259 │  173 ║   47 │   43 ║   32 │   32 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║  208 │  189 ║   42 │   43 ║   30 │   29 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║  187 │  180 ║   41 │   45 ║   30 │   27 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║  145 │  171 ║   40 │   36 ║   31 │   24 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║  140 │  176 ║   39 │   29 ║   29 │   29 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║  130 │  169 ║   31 │   30 ║   26 │   25 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║  114 │  163 ║   31 │   38 ║   23 │   25 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║  121 │  157 ║   36 │   38 ║   23 │   25 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║  141 │  163 ║   39 │   43 ║   24 │   27 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║  163 │  166 ║   41 │   46 ║   27 │   33 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║  204 │  163 ║   42 │   44 ║   27 │   30 ║
║    │    ║      │     ║       │  sum: ║ 1812 │ 1870 ║  429 │  435 ║  302 │  306 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +322 │ +329 ║ +500 │ +511 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠙⣛⣛⣛⣛⣛⠿⠯⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡧⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⣀⣀⡠⠤⠤⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
andNot(T)
  54001 in 4930ms
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

#### Bitwise ([`BigIntBitwiseTest`][BigIntBitwiseTest])

**Summary**

```
[runtime performance]
╔══════════════════╦════════════╦════════╦═══════╗
║                  ║ BigInteger ║ BigInt ║ int[] ║
╠══════════════════╬════════════╬════════╬═══════╣
║       bitCount() ║         +0 ║    +67 ║   +63 ║
║      bitLength() ║         +0 ║    +81 ║  +205 ║
║ toByteArray() BE ║         +0 ║    +23 ║   +15 ║
║ toByteArray() LE ║         +0 ║    +71 ║   +64 ║
╚══════════════════╩════════════╩════════╩═══════╝

[runtime performance]
╔═════════════════╦═════════════╦═════════════╦══════════════╗
║                 ║ BigInteger  ║   BigInt    ║    int[]     ║
╠═════════════════╬══════╤══════╬══════╤══════╬═══════╤══════╣
║    testBit(int) ║   +0 │   +0 ║  +30 │  +27 ║   +52 │  +16 ║
║     setBit(int) ║   +0 │   +0 ║ +848 │ +754 ║ +1315 │ +944 ║
║    flipBit(int) ║   +0 │   +0 ║ +557 │ +466 ║ +1035 │ +844 ║
║   clearBit(int) ║   +0 │   +0 ║ +490 │ +420 ║  +636 │ +550 ║
║  shiftLeft(int) ║  +23 │  +52 ║   +0 │   +0 ║   +16 │  +44 ║
║ shiftRight(int) ║   +0 │   +8 ║   +0 │   +0 ║   +49 │  +70 ║
╚═════════════════╩══════╧══════╩══════╧══════╩═══════╧══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║       bitCount() ║    +0 │    +0 ║ +2954 │  +827 ║ +1427 │  +827 ║
║      bitLength() ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
║ toByteArray() BE ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║ toByteArray() LE ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║     testBit(int) ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║      setBit(int) ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║     flipBit(int) ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║    clearBit(int) ║    +0 │    +0 ║ +3118 │  +572 ║  +972 │  +572 ║
║   shiftLeft(int) ║    +0 │    +0 ║ +3118 │  +551 ║  +972 │  +551 ║
║  shiftRight(int) ║    +0 │    +0 ║ +3118 │  +551 ║  +972 │  +551 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `bitCount()`

The number of bits in the two's complement representation that differ from the sign bit.

```
[runtime performance]
bitCount()
  107377 in 937ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         19 ║     11 ║    12 ║
║     44 ║      -102 ║  9605 ║         16 ║      8 ║     9 ║
║     41 ║       -76 ║  9454 ║         17 ║     10 ║     9 ║
║     39 ║       -51 ║  9166 ║         17 ║      8 ║     7 ║
║     36 ║       -25 ║  8398 ║         11 ║      7 ║     6 ║
║     34 ║         0 ║  8523 ║          8 ║      6 ║     5 ║
║     36 ║        25 ║  8348 ║          8 ║      6 ║     4 ║
║     39 ║        51 ║  9191 ║          8 ║      6 ║     7 ║
║     41 ║        76 ║  9501 ║         11 ║      8 ║     8 ║
║     44 ║       102 ║  9693 ║         11 ║      6 ║     7 ║
║     47 ║       128 ║ 11735 ║         15 ║      8 ║    12 ║
║        ║           ║  sum: ║        141 ║     84 ║    86 ║
║        ║           ║   +%: ║         +0 ║    +67 ║   +63 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⣉⠭⡻⠛⠉⠉⠉⠙⠫⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠔⠊⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⡀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠊⠒⠒⠒⠒⠊⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠑⠒⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⡀⠀⠀⠀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⠤⠔⠒⠉⠉⠉⠁⠉⠉⠈⠉⠉⠉⠈⠈⠉⠑⠢⢄⣀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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

[heap allocation]
bitCount()
  54001 in 4047ms
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

##### `bitLength()`

The number of bits in the minimal two's-complement representation excluding the sign bit.

```
[runtime performance]
bitLength()
  107377 in 520ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         15 ║      6 ║     5 ║
║     44 ║      -102 ║  9605 ║         12 ║      3 ║     2 ║
║     41 ║       -76 ║  9454 ║         13 ║      5 ║     4 ║
║     39 ║       -51 ║  9166 ║          9 ║      5 ║     3 ║
║     36 ║       -25 ║  8398 ║          9 ║      4 ║     3 ║
║     34 ║         0 ║  8523 ║          6 ║     10 ║     4 ║
║     36 ║        25 ║  8348 ║          7 ║      4 ║     2 ║
║     39 ║        51 ║  9191 ║          9 ║      6 ║     3 ║
║     41 ║        76 ║  9501 ║          8 ║      6 ║     3 ║
║     44 ║       102 ║  9693 ║          9 ║      4 ║     2 ║
║     47 ║       128 ║ 11735 ║         10 ║      6 ║     4 ║
║        ║           ║  sum: ║        107 ║     59 ║    35 ║
║        ║           ║   +%: ║         +0 ║    +81 ║  +205 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⢉⡩⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠭⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠊⠒⠒⠒⠒⠒⠒⠒⢄⠀⠀⠀⠀⠠⠔⠉⠑⠒⢄⡀⠀⢀⠔⠊⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠒⠒⠊⠒⠒⠒⠒⠒⠊⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠔⠁⠀⠀⠀⠀⠀⠈⡲⣁⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⠎⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠑⠢⣀⠀⠀⠀⠀⠉⠒⠢⠤⠤⠤⠤⠢⠤⠤⠔⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠈⢂⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠊⠀⠀⠀⠀⠑⠤⠤⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⢄⣀⣀⢄⣀⣀⣀⣀⣀⣀⡠⣀⣀⣀⣀⣀⣀⣀⡠⣀⣀⣀⣀⣀⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠀⢀⠀⠀⠀⢀⢀⢀⣀⣀⣀⠤⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠉⠁⠉⠉⠉⠁⠁⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
bitLength()
  54001 in 2647ms
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

##### `toByteArray() BE`

Convert the number to a `byte` array in big-endian order.

```
[runtime performance]
toByteArray() BE
  107377 in 588ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        135 ║     98 ║   112 ║
║     44 ║      -102 ║  9605 ║        118 ║     82 ║    93 ║
║     41 ║       -76 ║  9454 ║        100 ║     70 ║    76 ║
║     39 ║       -51 ║  9166 ║         76 ║     58 ║    60 ║
║     36 ║       -25 ║  8398 ║         50 ║     36 ║    38 ║
║     34 ║         0 ║  8523 ║         28 ║     24 ║    25 ║
║     36 ║        25 ║  8348 ║         38 ║     36 ║    37 ║
║     39 ║        51 ║  9191 ║         59 ║     61 ║    58 ║
║     41 ║        76 ║  9501 ║         75 ║     73 ║    71 ║
║     44 ║       102 ║  9693 ║         96 ║     83 ║    89 ║
║     47 ║       128 ║ 11735 ║        111 ║     96 ║   106 ║
║        ║           ║  sum: ║        886 ║    717 ║   765 ║
║        ║           ║   +%: ║         +0 ║    +23 ║   +15 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⣭⠿⠛⠉⠉⠉⠉⢉⡩⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠻⢯⣍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⣒⡲⠭⠛⠊⠉⠀⠀⠀⠀⢀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠷⢶⣶⣢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⡠⠤⠔⠒⠊⠉⠉⠁⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠙⠫⢞⡓⠲⠶⣤⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠁⠀⠀⠀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠉⠒⠤⢄⡀⠈⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⣀⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠈⠉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠤⠤⢍⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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

[heap allocation]
toByteArray() BE
  54001 in 2666ms
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

##### `toByteArray() LE`

Convert the number to a `byte` array in little-endian order.

The `BigInteger` class does not support little endian `byte[]` encoding. Therefore, for this test, the output array is reversed just for `BigInteger`. The time for the array reversal _is_ included in the runtime measure.

```
[runtime performance]
toByteArray() LE
  107377 in 495ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        191 ║    107 ║   107 ║
║     44 ║      -102 ║  9605 ║        150 ║     75 ║    77 ║
║     41 ║       -76 ║  9454 ║        119 ║     65 ║    72 ║
║     39 ║       -51 ║  9166 ║         88 ║     51 ║    52 ║
║     36 ║       -25 ║  8398 ║         58 ║     32 ║    34 ║
║     34 ║         0 ║  8523 ║         32 ║     21 ║    23 ║
║     36 ║        25 ║  8348 ║         46 ║     30 ║    36 ║
║     39 ║        51 ║  9191 ║         74 ║     49 ║    51 ║
║     41 ║        76 ║  9501 ║        101 ║     65 ║    64 ║
║     44 ║       102 ║  9693 ║        127 ║     72 ║    76 ║
║     47 ║       128 ║ 11735 ║        162 ║    103 ║   105 ║
║        ║           ║  sum: ║       1148 ║    670 ║   697 ║
║        ║           ║   +%: ║         +0 ║    +71 ║   +64 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⣭⡽⠿⠟⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠭⠛⠋⠉⠉⠉⠛⠛⠭⠭⣉⡉⠉⠉⠙⠛⠛⠛⠻⠿⠯⠭⣭⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⢀⣠⡤⠴⠶⠶⠮⠭⠭⢍⣉⣉⣉⡩⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠛⠓⠒⠲⠶⠶⣒⣢⡤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠛⠳⠶⣤⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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

[heap allocation]
toByteArray() LE
  54001 in 2676ms
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

##### `testBit(int)`

Tests whether the provided bit is set.

```
[runtime performance]
testBit(int)
  107377 in 572ms
╔═════════╦════════════╦════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   15 │    0 ║  12 │   0 ║  10 │   0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   17 │    0 ║  11 │   0 ║  10 │   0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   17 │    0 ║  11 │   0 ║  10 │   0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   16 │    0 ║  12 │   0 ║  10 │   0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   15 │    0 ║  10 │   0 ║   9 │   0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   10 │   14 ║   8 │  11 ║   8 │  12 ║
║ 36 │    ║   25 │     ║  8348 │        ║   10 │    0 ║   9 │   0 ║   8 │   0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   10 │    0 ║  10 │   0 ║   8 │   0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   12 │    0 ║   9 │   0 ║   7 │   0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   14 │    0 ║  10 │   0 ║   8 │   0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   12 │    0 ║  11 │   0 ║   9 │   0 ║
║    │    ║      │     ║       │   sum: ║  148 │   14 ║ 113 │  11 ║  97 │  12 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +30 │ +27 ║ +52 │ +16 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠀⢀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠁⠉⠓⢍⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠒⢒⡲⠶⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠶⣒⠒⠒⠒⠒⠒⠒⠉⠀⠀⢀⠤⠒⠒⠢⢄⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠁⠀⠀⢀⠀⠀⡀⠉⠢⣀⡀⠀⢀⣀⣀⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⢀⡠⠊⠁⠀⠀⠀⠀⠀⠡⡄⠀⠀⠀⠀⠀⠀⠀⠀⢌⠁⢀⠊⠉⠁⠉⠉⠈⠉⠉⠉⠈⠉⠣⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⢀⠊⠂⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠀⠀⠀⠀⠐⡑⢄⡀⣀⠔⠁⠌⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⡀⡀⠀⠀⠀⡀⠀⠀⡀⣀⣀⣀⠤⠤⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠑⢄⠀⠀⠀⠐⡀⠈⠀⠀⠌⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠈⠉⠉⠉⠈⠉⠉⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠉⠒⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠐⠤⡠⠊⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
testBit(int)
  54001 in 2947ms
╔═════════╦════════════╦══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║              ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │    ║ -128 │     ║ 6319 │       ║    40 │    22 ║     1 │     2 ║     2 │     2 ║
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
║    │    ║      │     ║      │  sum: ║   337 │   204 ║    11 │    22 ║    22 │    22 ║
║    │    ║      │     ║      │   +%: ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
╚════╧════╩══════╧═════╩══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `setBit(int)`

Sets the provided bit.

```
[runtime performance]
setBit(int)
  107377 in 881ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦══════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]     ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬═══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║  141 │    0 ║   12 │    0 ║     8 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║  127 │    0 ║   13 │    0 ║     8 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║  104 │    0 ║    9 │    0 ║     7 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   79 │    0 ║    9 │    0 ║     6 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   73 │    0 ║    7 │    0 ║     5 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   74 │   94 ║    9 │   11 ║     7 │    9 ║
║ 36 │    ║   25 │     ║  8348 │        ║   43 │    0 ║    5 │    0 ║     4 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   48 │    0 ║    5 │    0 ║     3 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   62 │    0 ║    8 │    0 ║     4 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   80 │    0 ║    9 │    0 ║     6 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   89 │    0 ║   11 │    0 ║     7 │    0 ║
║    │    ║      │     ║       │   sum: ║  920 │   94 ║   97 │   11 ║    65 │    9 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +848 │ +754 ║ +1315 │ +944 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩═══════╧══════╝
⡿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠟⠛⠛⠛⠛⠛⠻⠿⠿⣫⣭⣭⡭⣭⣭⣭⡽⠿⠟⠛⠛⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠭⠭⠭⠭⠭⠭⠭⠭⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⢟⣛⣛⣛⣛⣛⣛⣛⣛⣻
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠉⠉⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠁⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
setBit(int)
  54001 in 3531ms
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

##### `flipBit(int)`

Flips the provided bit.

```
[runtime performance]
flipBit(int)
  107377 in 729ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦══════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]     ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬═══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║  140 │    0 ║   15 │    0 ║     7 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║  118 │    0 ║   14 │    0 ║     8 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║  103 │    0 ║   13 │    0 ║     8 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   84 │    0 ║   13 │    0 ║     8 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   68 │    0 ║   14 │    0 ║     7 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   54 │   85 ║    9 │   15 ║     8 │    9 ║
║ 36 │    ║   25 │     ║  8348 │        ║   38 │    0 ║   12 │    0 ║     6 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   46 │    0 ║   10 │    0 ║     6 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   64 │    0 ║   10 │    0 ║     6 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   76 │    0 ║   12 │    0 ║     7 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   83 │    0 ║   11 │    0 ║     6 │    0 ║
║    │    ║      │     ║       │   sum: ║  874 │   85 ║  133 │   15 ║    77 │    9 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +557 │ +466 ║ +1035 │ +844 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩═══════╧══════╝
⡟⠛⠛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⡛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠝⠛⠛⠛⠛⠛⠛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⠛⠛⠛⠛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣛⣻
⡏⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠉⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⠤⠤⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠤⠤⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
flipBit(int)
  54001 in 3285ms
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

##### `clearBit(int)`

Clears the provided bit.

```
[runtime performance]
clearBit(int)
  107377 in 718ms
╔═════════╦════════════╦════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║  131 │    0 ║   15 │    0 ║   10 │    0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║  115 │    0 ║   17 │    0 ║   10 │    0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   98 │    0 ║   15 │    0 ║   11 │    0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   76 │    0 ║   18 │    0 ║   16 │    0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   60 │    0 ║   12 │    0 ║   10 │    0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   37 │   78 ║    9 │   15 ║    8 │   12 ║
║ 36 │    ║   25 │     ║  8348 │        ║   32 │    0 ║    7 │    0 ║   10 │    0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   43 │    0 ║    8 │    0 ║   12 │    0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   62 │    0 ║   10 │    0 ║    8 │    0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   70 │    0 ║   12 │    0 ║    7 │    0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   79 │    0 ║   13 │    0 ║    7 │    0 ║
║    │    ║      │     ║       │   sum: ║  803 │   78 ║  136 │   15 ║  109 │   12 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +0 ║ +490 │ +420 ║ +636 │ +550 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣯⣭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣭⣭⣭⣭⡭⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⣉⡩⠭⠭⢝⣛⣛⣛⣛⣛⣛⣛⣛⣛⣫⣭⠭⠭⠭⠽⠿⠿⣛⣛⣛⣛⡛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡇⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠚⠛⠛⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
clearBit(int)
  54001 in 3287ms
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

##### `shiftLeft(int)`

Shifts the bits in the number left by the given amount.

```
[runtime performance]
shiftLeft(int)
  107377 in 853ms
╔═════════╦════════════╦════════════════╦═════════════╦══════════╦═══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt  ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤════╬═════╤═════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   42 │    0 ║  58 │  0 ║  49 │   0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   40 │    0 ║  54 │  0 ║  46 │   0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   33 │    0 ║  46 │  0 ║  37 │   0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   29 │    0 ║  32 │  0 ║  28 │   0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   24 │    0 ║  18 │  0 ║  19 │   0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   20 │   36 ║  11 │ 55 ║  13 │  38 ║
║ 36 │    ║   25 │     ║  8348 │        ║   25 │    0 ║  21 │  0 ║  20 │   0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   30 │    0 ║  35 │  0 ║  32 │   0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   34 │    0 ║  51 │  0 ║  38 │   0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   40 │    0 ║  58 │  0 ║  48 │   0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   43 │    0 ║  61 │  0 ║  51 │   0 ║
║    │    ║      │     ║       │   sum: ║  360 │   36 ║ 445 │ 55 ║ 381 │  38 ║
║    │    ║      │     ║       │    +%: ║  +23 │  +52 ║  +0 │ +0 ║ +16 │ +44 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠤⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⢔⠞⠉⠀⠀⠀⠈⠕⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢔⠍⢀⣀⣉⣓⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠊⠉⢉⣉⠭⠭⠛⠋⠉⢉⠝⠉⠉⠉⠉⠑⠒⠤⡈⠌⠢⡀⠀⠀⠀⠀⠀⠀⠀⡠⠁⡢⠊⠁⠀⠀⠀⠉⠙⢶⣒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠤⠒⠒⠒⠒⠉⠉⠀⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⢊⡀⠐⢄⠀⠀⠀⠀⢀⠌⢀⠜⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠭⡉⠒⠒⠒⠒⠒⠤⠤⢄⡀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠱⢄⠀⠑⠤⠤⠒⠁⡠⡒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠑⢄⡀⢀⡠⠊⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠈⠁⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠔⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
shiftLeft(int)
  54001 in 3384ms
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

##### `shiftRight(int)`

Shifts the bits in the number right by the given amount.

```
[runtime performance]
shiftRight(int)
  107377 in 676ms
╔═════════╦════════════╦════════════════╦═════════════╦══════════╦═══════════╗
║ length  ║ precision  ║     count      ║ BigInteger  ║  BigInt  ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤════════╬══════╤══════╬═════╤════╬═════╤═════╣
║ 47 │    ║ -128 │     ║ 12663 │        ║   59 │    0 ║  54 │  0 ║  39 │   0 ║
║ 44 │    ║ -102 │     ║  9605 │        ║   57 │    0 ║  54 │  0 ║  38 │   0 ║
║ 41 │    ║  -76 │     ║  9454 │        ║   48 │    0 ║  51 │  0 ║  32 │   0 ║
║ 39 │    ║  -51 │     ║  9166 │        ║   42 │    0 ║  46 │  0 ║  27 │   0 ║
║ 36 │    ║  -25 │     ║  8398 │        ║   34 │    0 ║  27 │  0 ║  19 │   0 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │ 107277 ║   21 │   49 ║  14 │ 53 ║  12 │  31 ║
║ 36 │    ║   25 │     ║  8348 │        ║   27 │    0 ║  23 │  0 ║  16 │   0 ║
║ 39 │    ║   51 │     ║  9191 │        ║   34 │    0 ║  39 │  0 ║  24 │   0 ║
║ 41 │    ║   76 │     ║  9501 │        ║   39 │    0 ║  48 │  0 ║  28 │   0 ║
║ 44 │    ║  102 │     ║  9693 │        ║   47 │    0 ║  47 │  0 ║  33 │   0 ║
║ 47 │    ║  128 │     ║ 11735 │        ║   46 │    0 ║  47 │  0 ║  36 │   0 ║
║    │    ║      │     ║       │   sum: ║  454 │   49 ║ 450 │ 53 ║ 304 │  31 ║
║    │    ║      │     ║       │    +%: ║   +0 │   +8 ║  +0 │ +0 ║ +49 │ +70 ║
╚════╧════╩══════╧═════╩═══════╧════════╩══════╧══════╩═════╧════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠊⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⣀⣀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠢⠤⠤⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⡀⠀⠀⠀⠑⢄⡀⠀⠀⢀⠤⠊⠀⠀⢀⠊⢀⣀⣉⣢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠑⠒⠒⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠁⠀⠈⠢⠀⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⢀⠂⠔⠁⠀⠀⠀⠉⠲⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⣁⠤⠤⢄⡀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠌⠀⠀⠀⠀⠀⠀⠀⠑⢄⡈⠉⠉⠉⠉⠉⠉⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⢒⠎⠁⠀⠀⠀⠀⠈⢂⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠄⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⢔⣒⡊⠉⠉⠁⠀⢀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⢁⠀⠀⠀⠀⠀⠀⡈⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⡀⠀⢀⣀⡠⠤⠔⠚⠉⠉⠛⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠤⠤⠤⣔⡲⠖⠚⠉⠉⠉⠁⠀⠈⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠢⠀⠀⠀⠀⡐⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠑⢄⣀⠌⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
shiftRight(int)
  54001 in 3421ms
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

#### Predicate ([`BigIntPredicateTest`][BigIntPredicateTest])

**Summary**

```
[runtime performance]
╔═══════════════╦════════════╦════════╦═══════╗
║               ║ BigInteger ║ BigInt ║ int[] ║
╠═══════════════╬════════════╬════════╬═══════╣
║   byteValue() ║         +0 ║   +389 ║   +97 ║
║  shortValue() ║        +39 ║     +0 ║  +139 ║
║    intValue() ║         +8 ║     +0 ║   +68 ║
║   longValue() ║         +0 ║   +208 ║  +230 ║
║  floatValue() ║         +0 ║    +29 ║   +96 ║
║ doubleValue() ║         +0 ║    +19 ║  +123 ║
║    toString() ║         +0 ║   +349 ║  +360 ║
║    hashCode() ║        +39 ║     +5 ║    +0 ║
║         abs() ║         +0 ║   +614 ║  +257 ║
║         neg() ║         +0 ║   +207 ║ -1100 ║
║      signum() ║       +293 ║     +0 ║    +8 ║
║   precision() ║         +0 ║  +2067 ║ +2477 ║
╚═══════════════╩════════════╩════════╩═══════╝

[runtime performance]
╔══════════════╦═════════════╦═══════════╦═══════════╗
║              ║ BigInteger  ║  BigInt   ║   int[]   ║
╠══════════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ compareTo(T) ║ +215 │ +216 ║ +89 │ +90 ║  +0 │  +0 ║
║    equals(T) ║  +33 │   +0 ║  +0 │ +36 ║ +55 │ +23 ║
║       max(T) ║  +50 │  +50 ║  +0 │  +0 ║ +15 │ +17 ║
║       min(T) ║   +0 │   +0 ║ +16 │ +16 ║ +87 │ +87 ║
╚══════════════╩══════╧══════╩═════╧═════╩═════╧═════╝

[heap allocation]
╔═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║               ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═══════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║   byteValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║  shortValue() ║    +0 │    +0 ║ +2963 │  +831 ║ +1431 │  +831 ║
║    intValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║   longValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║  floatValue() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║ doubleValue() ║    +0 │    +0 ║ +2981 │  +831 ║ +1440 │  +831 ║
║    toString() ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
║  compareTo(T) ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
║     equals(T) ║    +0 │    +0 ║ +5163 │ +1030 ║ +1654 │ +1030 ║
║    hashCode() ║    +0 │    +0 ║ +2954 │  +822 ║ +1427 │  +822 ║
║         abs() ║    +0 │    +0 ║ +3018 │  +518 ║  +939 │  +518 ║
║         neg() ║    +0 │    +0 ║ +3118 │  +518 ║  +972 │  +518 ║
║      signum() ║    +0 │    +0 ║ +2963 │  +827 ║ +1431 │  +827 ║
║   precision() ║    +0 │    +0 ║ +4454 │ +1286 ║ +2177 │ +1286 ║
║        max(T) ║    +0 │    +0 ║ +5190 │  +747 ║ +1222 │  +747 ║
║        min(T) ║    +0 │    +0 ║ +5236 │  +747 ║ +1234 │  +747 ║
╚═══════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `byteValue()`

Value of number as a `byte`.

```
[runtime performance]
byteValue()
  107377 in 926ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         15 ║      2 ║     4 ║
║     44 ║      -102 ║  9605 ║         12 ║      3 ║     4 ║
║     41 ║       -76 ║  9454 ║         12 ║      1 ║     4 ║
║     39 ║       -51 ║  9166 ║         13 ║      4 ║     5 ║
║     36 ║       -25 ║  8398 ║         12 ║      3 ║     6 ║
║     34 ║         0 ║  8523 ║         15 ║      4 ║    27 ║
║     36 ║        25 ║  8348 ║         11 ║      3 ║     5 ║
║     39 ║        51 ║  9191 ║         15 ║      3 ║     4 ║
║     41 ║        76 ║  9501 ║         14 ║      1 ║     5 ║
║     44 ║       102 ║  9693 ║         10 ║      2 ║     4 ║
║     47 ║       128 ║ 11735 ║         13 ║      3 ║     4 ║
║        ║           ║  sum: ║        142 ║     29 ║    72 ║
║        ║           ║   +%: ║         +0 ║   +389 ║   +97 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠭⢍⡉⠉⠉⠉⠉⣉⡩⠝⠋⠉⠉⠉⠉⠉⠛⠫⠭⣉⣉⣉⣉⡩⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⣀⡠⠤⠚⠋⠉⠉⠉⠉⠉⠑⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠤⢄⣀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣂⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⡀⣀⣀⣠⣀⣀⣀⣀⠤⠤⠤⠒⠊⠙⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠉⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠔⠒⠊⠉⠉⠉⠉⠉⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠒⠊⠉⠉⠉⠉⠁⠉⠉⠉⠉⠉⠈⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠢⠔⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
byteValue()
  54001 in 4135ms
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

##### `shortValue()`

Value of number as a `short`.

```
[runtime performance]
shortValue()
  107377 in 526ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         11 ║      7 ║     7 ║
║     44 ║      -102 ║  9605 ║         11 ║      4 ║     3 ║
║     41 ║       -76 ║  9454 ║         11 ║      6 ║     4 ║
║     39 ║       -51 ║  9166 ║         10 ║      8 ║     6 ║
║     36 ║       -25 ║  8398 ║         12 ║      8 ║     5 ║
║     34 ║         0 ║  8523 ║         11 ║     88 ║    12 ║
║     36 ║        25 ║  8348 ║          8 ║      5 ║     5 ║
║     39 ║        51 ║  9191 ║          7 ║      7 ║     6 ║
║     41 ║        76 ║  9501 ║          9 ║      7 ║     5 ║
║     44 ║       102 ║  9693 ║          9 ║      4 ║     4 ║
║     47 ║       128 ║ 11735 ║          9 ║      7 ║     6 ║
║        ║           ║  sum: ║        108 ║    151 ║    63 ║
║        ║           ║   +%: ║        +39 ║     +0 ║  +139 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⣏⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠛⠛⠛⣛⣛⣛⣛⣛⣛⠛⠛⠫⠭⠭⢍⡉⠉⠉⠙⣛⠿⠽⠿⠿⠿⠛⠛⠯⠭⢍⣍⣉⡩⠭⠝⠛⠋⠉⠉⠉⠉⠉⠙⠛⠿⠿⢟⣛⣛⣛⣛⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠝⠛⠛⠛⠛⠛⠛⠛⠛⢻
⡇⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⠤⠬⠖⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
shortValue()
  54001 in 2704ms
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

##### `intValue()`

Value of number as an `int`.

```
[runtime performance]
intValue()
  107377 in 590ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         14 ║      6 ║     6 ║
║     44 ║      -102 ║  9605 ║         16 ║      9 ║    10 ║
║     41 ║       -76 ║  9454 ║         15 ║      7 ║     9 ║
║     39 ║       -51 ║  9166 ║         14 ║      9 ║     7 ║
║     36 ║       -25 ║  8398 ║         14 ║      6 ║     6 ║
║     34 ║         0 ║  8523 ║         10 ║     79 ║     9 ║
║     36 ║        25 ║  8348 ║          8 ║      5 ║     7 ║
║     39 ║        51 ║  9191 ║         11 ║      7 ║     7 ║
║     41 ║        76 ║  9501 ║         12 ║      7 ║     9 ║
║     44 ║       102 ║  9693 ║         11 ║      7 ║     9 ║
║     47 ║       128 ║ 11735 ║         11 ║      5 ║     8 ║
║        ║           ║  sum: ║        136 ║    147 ║    87 ║
║        ║           ║   +%: ║         +8 ║     +0 ║   +68 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠋⠉⢉⣉⠭⠝⠛⠛⠛⠛⠛⠛⠛⠻⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠫⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠝⠛⠛⠫⠭⠭⠭⢟⣛⣛⣛⣛⣛⣛⣛⣛⣛⣻
⡗⠒⠤⠤⠤⢄⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠚⡉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
intValue()
  54001 in 2736ms
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

##### `longValue()`

Value of number as a `long`.

```
[runtime performance]
longValue()
  107377 in 475ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         42 ║     13 ║    12 ║
║     44 ║      -102 ║  9605 ║         36 ║     11 ║     9 ║
║     41 ║       -76 ║  9454 ║         34 ║     10 ║     8 ║
║     39 ║       -51 ║  9166 ║         37 ║      9 ║     9 ║
║     36 ║       -25 ║  8398 ║         36 ║      8 ║     8 ║
║     34 ║         0 ║  8523 ║         21 ║      8 ║     8 ║
║     36 ║        25 ║  8348 ║         21 ║     10 ║    10 ║
║     39 ║        51 ║  9191 ║         23 ║      9 ║     9 ║
║     41 ║        76 ║  9501 ║         22 ║      8 ║     8 ║
║     44 ║       102 ║  9693 ║         25 ║     10 ║     9 ║
║     47 ║       128 ║ 11735 ║         33 ║     11 ║    10 ║
║        ║           ║  sum: ║        330 ║    107 ║   100 ║
║        ║           ║   +%: ║         +0 ║   +208 ║  +230 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠢⠤⠤⠤⠤⠢⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⠀⢀⠀⠀⠀⠀⠀⠀⣀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠤⠔⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠁⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
longValue()
  54001 in 2694ms
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

##### `floatValue()`

Value of number as a `float`.

```
[runtime performance]
floatValue()
  107377 in 608ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         29 ║     21 ║    17 ║
║     44 ║      -102 ║  9605 ║         23 ║     11 ║     9 ║
║     41 ║       -76 ║  9454 ║         22 ║     12 ║    10 ║
║     39 ║       -51 ║  9166 ║         31 ║     13 ║    13 ║
║     36 ║       -25 ║  8398 ║         38 ║     21 ║    23 ║
║     34 ║         0 ║  8523 ║         30 ║     82 ║    23 ║
║     36 ║        25 ║  8348 ║         35 ║     20 ║    17 ║
║     39 ║        51 ║  9191 ║         28 ║     18 ║    14 ║
║     41 ║        76 ║  9501 ║         23 ║     12 ║    10 ║
║     44 ║       102 ║  9693 ║         22 ║     10 ║     8 ║
║     47 ║       128 ║ 11735 ║         29 ║     20 ║    14 ║
║        ║           ║  sum: ║        310 ║    240 ║   158 ║
║        ║           ║   +%: ║         +0 ║    +29 ║   +96 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⣏⠭⠝⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠫⠭⣙⣍⣉⣉⣉⣉⣉⣉⠭⠭⠭⠝⠛⠋⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠭⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡧⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠒⠒⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⢀⠤⠒⠉⠉⠉⠒⢄⡀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠒⠒⠑⠒⠒⠒⠑⠒⠒⠑⠒⠒⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⡢⠒⠁⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⣀⣀⣀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠉⠈⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
floatValue()
  54001 in 2720ms
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

##### `doubleValue()`

Value of number as a `double`.

```
[runtime performance]
doubleValue()
  107377 in 480ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         22 ║     11 ║    10 ║
║     44 ║      -102 ║  9605 ║         22 ║     11 ║    10 ║
║     41 ║       -76 ║  9454 ║         20 ║     10 ║     9 ║
║     39 ║       -51 ║  9166 ║         23 ║     13 ║    12 ║
║     36 ║       -25 ║  8398 ║         28 ║     12 ║     9 ║
║     34 ║         0 ║  8523 ║         20 ║     79 ║     8 ║
║     36 ║        25 ║  8348 ║         25 ║     13 ║     9 ║
║     39 ║        51 ║  9191 ║         22 ║     15 ║    12 ║
║     41 ║        76 ║  9501 ║         19 ║     13 ║     9 ║
║     44 ║       102 ║  9693 ║         19 ║     12 ║    10 ║
║     47 ║       128 ║ 11735 ║         21 ║     13 ║    10 ║
║        ║           ║  sum: ║        241 ║    202 ║   108 ║
║        ║           ║   +%: ║         +0 ║    +19 ║  +123 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⢀⠤⠒⠒⠒⠤⢄⡀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⡀⠀⠀⠀⠀⠀⢂⡠⠊⠁⠀⠀⠀⠀⠀⠀⠈⠱⠢⠤⣀⡠⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
doubleValue()
  54001 in 2721ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    23 ║     1 │     2 ║     2 │     2 ║
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
║        ║           ║  sum: ║   339 │   205 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2981 │  +831 ║ +1440 │  +831 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `toString()`

String representation of number in radix 10.

```
[runtime performance]
toString()
  107377 in 769ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║       2749 ║    554 ║   542 ║
║     44 ║      -102 ║  9605 ║       2015 ║    427 ║   417 ║
║     41 ║       -76 ║  9454 ║       1434 ║    317 ║   311 ║
║     39 ║       -51 ║  9166 ║        827 ║    209 ║   210 ║
║     36 ║       -25 ║  8398 ║        354 ║    116 ║   109 ║
║     34 ║         0 ║  8523 ║        151 ║     76 ║    62 ║
║     36 ║        25 ║  8348 ║        382 ║    123 ║   119 ║
║     39 ║        51 ║  9191 ║        864 ║    213 ║   204 ║
║     41 ║        76 ║  9501 ║       1479 ║    332 ║   321 ║
║     44 ║       102 ║  9693 ║       2087 ║    437 ║   434 ║
║     47 ║       128 ║ 11735 ║       2799 ║    563 ║   556 ║
║        ║           ║  sum: ║      15141 ║   3367 ║  3285 ║
║        ║           ║   +%: ║         +0 ║   +349 ║  +360 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣭⣭⠭⠭⠭⠭⠭⠭⠭⠿⠛⠛⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⠭⠝⠛⠛⠛⠛⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠿⠿⠭⠭⠭⠭⠭⠭⠭⣍⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
toString()
  54001 in 3104ms
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

##### `compareTo(T)`

Compare `T`.

```
[runtime performance]
compareTo(T)
  107377 in 601ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║    6 │    7 ║  10 │  11 ║  23 │  22 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    7 │    8 ║  12 │  13 ║  24 │  21 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    7 │    6 ║  11 │  10 ║  22 │  20 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║    6 │    6 ║   9 │   9 ║  19 │  21 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    7 │    7 ║   9 │  12 ║  20 │  22 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║    7 │    5 ║  13 │  12 ║  17 │  21 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    6 │    6 ║  10 │  11 ║  19 │  20 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    5 │    6 ║  10 │  10 ║  18 │  20 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    8 │    6 ║  12 │   9 ║  20 │  19 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    6 │    7 ║  12 │  10 ║  21 │  19 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    6 │    7 ║  10 │  11 ║  21 │  20 ║
║    │    ║      │     ║       │  sum: ║   71 │   71 ║ 118 │ 118 ║ 224 │ 225 ║
║    │    ║      │     ║       │   +%: ║ +215 │ +216 ║ +89 │ +90 ║  +0 │  +0 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠝⠛⠉⠉⠉⠉⠉⠛⠫⢍⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠒⠊⠉⠉⠉⠉⠉⠉⠑⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠉⠁⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠉⠉⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⣀⣀⡠⣀⣀⣀⠤⢄⣀⠤⡠⣀⡠⣀⡠⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠔⠊⠉⠀⠀⠀⠈⠉⠒⠢⠤⣀⣀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
compareTo(T)
  54001 in 4739ms
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

##### `equals(T)`

Equate with `T`.

```
[runtime performance]
equals(T)
  107377 in 609ms
╔═════════╦════════════╦═══════════════╦═════════════╦══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt  ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   11 │    7 ║  5 │   4 ║   5 │   4 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    6 │    4 ║  4 │   4 ║   5 │   5 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    6 │    6 ║  3 │   4 ║   3 │   6 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║    5 │    5 ║  4 │   4 ║   5 │   5 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    2 │    5 ║  5 │   5 ║   6 │   4 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║    3 │    5 ║ 44 │   4 ║   4 │   5 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    3 │    6 ║  5 │   4 ║   5 │   4 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    6 │    6 ║  4 │   4 ║   6 │   6 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    6 │    6 ║  2 │   5 ║   4 │   5 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    6 │    6 ║  4 │   6 ║   5 │   4 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    9 │    8 ║  4 │   3 ║   6 │   4 ║
║    │    ║      │     ║       │  sum: ║   63 │   64 ║ 84 │  47 ║  54 │  52 ║
║    │    ║      │     ║       │   +%: ║  +33 │   +0 ║ +0 │ +36 ║ +55 │ +23 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩════╧═════╩═════╧═════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣹
⡇⠀⠉⠉⠒⠒⠤⣤⠤⠤⣤⡤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣤⡤⢤⣔⡒⠉⠉⠑⠤⡀⠀⠀⠀⣀⡠⠔⠛⠉⠉⠉⠙⠛⠓⠒⠲⠤⠴⢅⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠔⠒⠉⠉⠒⠒⠤⠤⠤⠤⠝⠒⠪⢍⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠖⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠈⠉⠙⠓⠒⠒⠶⠶⢎⣉⣁⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠢⠤⣀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
equals(T)
  54001 in 4620ms
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

##### `hashCode()`

Hash code of number.

```
[runtime performance]
hashCode()
  107377 in 441ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          9 ║     10 ║    12 ║
║     44 ║      -102 ║  9605 ║          7 ║     10 ║     9 ║
║     41 ║       -76 ║  9454 ║          9 ║     10 ║    12 ║
║     39 ║       -51 ║  9166 ║          5 ║      9 ║     9 ║
║     36 ║       -25 ║  8398 ║          3 ║      6 ║     7 ║
║     34 ║         0 ║  8523 ║          4 ║      8 ║     7 ║
║     36 ║        25 ║  8348 ║          3 ║      6 ║     6 ║
║     39 ║        51 ║  9191 ║          6 ║      9 ║     9 ║
║     41 ║        76 ║  9501 ║         10 ║     11 ║    12 ║
║     44 ║       102 ║  9693 ║          8 ║      9 ║     9 ║
║     47 ║       128 ║ 11735 ║         10 ║     10 ║    11 ║
║        ║           ║  sum: ║         74 ║     98 ║   103 ║
║        ║           ║   +%: ║        +39 ║     +5 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⢉⠭⠋⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⣀⣀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⡠⠤⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠤⠴⣒⠊⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⣤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢤⠤⠤⠤⠤⠭⠦⠤⠤⠤⠤⠤⠤⠤⢤⡤⠤⠶⢤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⡠⠊⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠔⠉⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠈⠉⠓⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⣠⡴⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⣀⣀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠑⢒⠬⢄⣀⠀⠀⠀⠀⢀⣀⠴⡒⠉⠉⠉⠁⠉⠉⠉⠉⢉⠝⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠂⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠐⠄⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
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

[heap allocation]
hashCode()
  54001 in 2727ms
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
║     47 ║       128 ║  5855 ║    38 │    22 ║     1 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   336 │   203 ║    11 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2954 │  +822 ║ +1427 │  +822 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `abs()`

Absolute value of number.

```
[runtime performance]
abs()
  107377 in 682ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║         11 ║      2 ║     2 ║
║     44 ║      -102 ║  9605 ║          6 ║      1 ║     1 ║
║     41 ║       -76 ║  9454 ║          4 ║      0 ║     2 ║
║     39 ║       -51 ║  9166 ║          3 ║      2 ║     1 ║
║     36 ║       -25 ║  8398 ║          4 ║      0 ║     1 ║
║     34 ║         0 ║  8523 ║          2 ║      0 ║     1 ║
║     36 ║        25 ║  8348 ║          2 ║      0 ║     1 ║
║     39 ║        51 ║  9191 ║          3 ║      0 ║     2 ║
║     41 ║        76 ║  9501 ║          4 ║      0 ║     1 ║
║     44 ║       102 ║  9693 ║          5 ║      1 ║     0 ║
║     47 ║       128 ║ 11735 ║          6 ║      1 ║     2 ║
║        ║           ║  sum: ║         50 ║      7 ║    14 ║
║        ║           ║   +%: ║         +0 ║   +614 ║  +257 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⡩⠟⠋⠉⠉⠙⠫⡉⠉⠉⠉⠉⠉⠉⠫⢍⠉⢉⠝⠋⠉⠉⠉⠉⠉⠉⡩⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠶⡁⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⣀⣀⠔⠊⠀⠈⠢⢄⣀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⢀⠀⠀⢀⠀⠀⡀⠈⠢⣀⣀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠉⠉⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠉⠉⠁⠈⠉⠉⠉⠁⠉⠉⠁⠉⠉⠈⠉⠉⠉⠈⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⣀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⡀⢀⠀⠀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠉⠉⠈⠈⠉⠉⠉⠉⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
abs()
  54001 in 2945ms
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

##### `neg()`

Negated value of number.

```
[runtime performance]
neg()
  107377 in 673ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          7 ║      2 ║     0 ║
║     44 ║      -102 ║  9605 ║          4 ║      2 ║     0 ║
║     41 ║       -76 ║  9454 ║          3 ║      1 ║    -1 ║
║     39 ║       -51 ║  9166 ║          4 ║      1 ║     0 ║
║     36 ║       -25 ║  8398 ║          1 ║      0 ║    -1 ║
║     34 ║         0 ║  8523 ║          1 ║      0 ║     0 ║
║     36 ║        25 ║  8348 ║          1 ║      0 ║    -1 ║
║     39 ║        51 ║  9191 ║          3 ║      1 ║     0 ║
║     41 ║        76 ║  9501 ║          3 ║      1 ║     0 ║
║     44 ║       102 ║  9693 ║          6 ║      2 ║    -1 ║
║     47 ║       128 ║ 11735 ║          7 ║      3 ║     0 ║
║        ║           ║  sum: ║         40 ║     13 ║    -4 ║
║        ║           ║   +%: ║         +0 ║   +207 ║ -1100 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⢄⡀⠀⠀⠀⠀⠀⠀⢀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠈⠑⠒⠤⠤⠤⠔⠁⠀⠀⠀⠀⢀⡠⠤⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⠤⠤⠤⠢⠤⠤⠤⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
neg()
  54001 in 2976ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     3 ║     3 │     3 ║
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
║        ║           ║  sum: ║   354 │   204 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3118 │  +518 ║  +972 │  +518 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `signum()`

Signum of number.

```
[runtime performance]
signum()
  107377 in 434ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║          1 ║      6 ║     5 ║
║     44 ║      -102 ║  9605 ║          0 ║      5 ║     5 ║
║     41 ║       -76 ║  9454 ║          1 ║      6 ║     4 ║
║     39 ║       -51 ║  9166 ║          0 ║      5 ║     4 ║
║     36 ║       -25 ║  8398 ║          2 ║      5 ║     5 ║
║     34 ║         0 ║  8523 ║          2 ║      7 ║     7 ║
║     36 ║        25 ║  8348 ║          3 ║      5 ║     6 ║
║     39 ║        51 ║  9191 ║          0 ║      5 ║     4 ║
║     41 ║        76 ║  9501 ║          1 ║      6 ║     7 ║
║     44 ║       102 ║  9693 ║          3 ║      5 ║     5 ║
║     47 ║       128 ║ 11735 ║          3 ║      8 ║     6 ║
║        ║           ║  sum: ║         16 ║     63 ║    58 ║
║        ║           ║   +%: ║       +293 ║     +0 ║    +8 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⠤⠤⠤⠔⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⢉⠝⠋⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠉⠉⠉⠉⠉⠉⡩⠛⠉⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠖⠋⠙⠭⡒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠁⠀⠀⠀⠀⠐⢄⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⣠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⣠⣔⣁⣀⣀⣀⣀⣀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
signum()
  54001 in 2705ms
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

##### `precision()`

Precision of number in radix 10.

The `BigInteger` class does not have a `precision()` method. Therefore, for this test, precision is determined with [`Numbers.precision(BigInteger)`][Numbers].

```
[runtime performance]
precision()
  107377 in 473ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        520 ║     17 ║    16 ║
║     44 ║      -102 ║  9605 ║        484 ║     17 ║    15 ║
║     41 ║       -76 ║  9454 ║        444 ║     17 ║    15 ║
║     39 ║       -51 ║  9166 ║        404 ║     17 ║    15 ║
║     36 ║       -25 ║  8398 ║        232 ║     18 ║    15 ║
║     34 ║         0 ║  8523 ║         66 ║     17 ║    13 ║
║     36 ║        25 ║  8348 ║        248 ║     18 ║    15 ║
║     39 ║        51 ║  9191 ║        403 ║     18 ║    15 ║
║     41 ║        76 ║  9501 ║        433 ║     18 ║    15 ║
║     44 ║       102 ║  9693 ║        474 ║     20 ║    14 ║
║     47 ║       128 ║ 11735 ║        519 ║     18 ║    16 ║
║        ║           ║  sum: ║       4227 ║    195 ║   164 ║
║        ║           ║   +%: ║         +0 ║  +2067 ║ +2477 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠤⠔⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠤⠒⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
precision()
  54001 in 3707ms
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

##### `max(T)`

Max with `T`.

```
[runtime performance]
max(T)
  107377 in 817ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   10 │    9 ║  12 │  12 ║  11 │  10 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║    8 │    8 ║  11 │  12 ║  10 │   9 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║    9 │    8 ║  13 │  14 ║   8 │   9 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║    8 │    9 ║  13 │  12 ║  10 │   9 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║    8 │    8 ║  13 │  13 ║  11 │  11 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║    8 │    7 ║  12 │  12 ║  12 │  13 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║    9 │    8 ║  14 │  16 ║  12 │  12 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║    9 │    8 ║  14 │  12 ║  12 │  13 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║    8 │    9 ║  11 │  12 ║   9 │  10 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║    8 │    9 ║  13 │  12 ║  12 │  11 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║    8 │   10 ║  14 │  13 ║  14 │  12 ║
║    │    ║      │     ║       │  sum: ║   93 │   93 ║ 140 │ 140 ║ 121 │ 119 ║
║    │    ║      │     ║       │   +%: ║  +50 │  +50 ║  +0 │  +0 ║ +15 │ +17 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠋⠉⠉⠉⠛⢍⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠀⡀⠀⠀⠀⡀⣀⣀⣀⣀⣀⠤⠤⡤⠤⠤⢤⡤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠉⠈⠉⠉⠉⠈⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠀⡀⠀⠀⠀⡀⣀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠉⠈⠉⠉⠉⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠈⠑⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠀⡀⠀⠀⠀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠉⠈⠉⠉⠉⠈⠈⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢤⣀⣀⣀⠤⠒⠁⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⣀⠌⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠉⠉⠒⠤⢄⡀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠈⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠪⣄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
max(T)
  54001 in 4831ms
╔═════════╦════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║            ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 40 ║ -128 │ -64 ║ 6319 │ 5065 ║    69 │    40 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║ -102 │ -51 ║ 4765 │ 4773 ║    73 │    43 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║  -76 │ -38 ║ 4702 │ 4520 ║    67 │    42 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║  -51 │ -25 ║ 4558 │ 5074 ║    58 │    37 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║  -25 │ -12 ║ 4174 │ 5243 ║    28 │    19 ║     1 │     4 ║     4 │     4 ║
║ 34 │ 34 ║    0 │   0 ║ 4299 │ 4514 ║    10 │     9 ║     1 │     4 ║     4 │     4 ║
║ 36 │ 35 ║   25 │  12 ║ 4124 │ 5144 ║    26 │    21 ║     1 │     4 ║     4 │     4 ║
║ 39 │ 36 ║   51 │  25 ║ 4583 │ 5075 ║    57 │    39 ║     1 │     4 ║     4 │     4 ║
║ 41 │ 38 ║   76 │  38 ║ 4713 │ 4473 ║    63 │    41 ║     1 │     4 ║     4 │     4 ║
║ 44 │ 39 ║  102 │  51 ║ 4809 │ 4749 ║    68 │    43 ║     1 │     4 ║     4 │     4 ║
║ 47 │ 40 ║  128 │  64 ║ 5855 │ 4271 ║    63 │    39 ║     1 │     4 ║     4 │     4 ║
║    │    ║      │     ║      │ sum: ║   582 │   373 ║    11 │    44 ║    44 │    44 ║
║    │    ║      │     ║      │  +%: ║    +0 │    +0 ║ +5190 │  +747 ║ +1222 │  +747 ║
╚════╧════╩══════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `min(T)`

Min with `T`.

```
[runtime performance]
min(T)
  107377 in 813ms
╔═════════╦════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤═════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 40 ║ -128 │ -64 ║ 12663 │ 10155 ║   23 │   21 ║  19 │  13 ║  14 │  11 ║
║ 44 │ 39 ║ -102 │ -51 ║  9605 │  9621 ║   19 │   21 ║  15 │  14 ║  12 │  10 ║
║ 41 │ 38 ║  -76 │ -38 ║  9454 │  9090 ║   19 │   21 ║  13 │  20 ║  10 │  10 ║
║ 39 │ 36 ║  -51 │ -25 ║  9166 │ 10198 ║   20 │   20 ║  16 │  20 ║   9 │  12 ║
║ 36 │ 35 ║  -25 │ -12 ║  8398 │ 10511 ║   17 │   21 ║  19 │  18 ║   9 │  12 ║
║ 34 │ 34 ║    0 │   0 ║  8523 │  8978 ║   12 │   16 ║  12 │  13 ║   8 │  12 ║
║ 36 │ 35 ║   25 │  12 ║  8348 │ 10388 ║   19 │   18 ║  16 │  17 ║   8 │  10 ║
║ 39 │ 36 ║   51 │  25 ║  9191 │ 10175 ║   23 │   19 ║  22 │  22 ║   9 │  11 ║
║ 41 │ 38 ║   76 │  38 ║  9501 │  9021 ║   20 │   19 ║  16 │  17 ║  12 │   9 ║
║ 44 │ 39 ║  102 │  51 ║  9693 │  9573 ║   21 │   20 ║  16 │  15 ║   9 │   9 ║
║ 47 │ 40 ║  128 │  64 ║ 11735 │  8567 ║   23 │   20 ║  22 │  16 ║  15 │   9 ║
║    │    ║      │     ║       │  sum: ║  216 │  216 ║ 186 │ 185 ║ 115 │ 115 ║
║    │    ║      │     ║       │   +%: ║   +0 │   +0 ║ +16 │ +16 ║ +87 │ +87 ║
╚════╧════╩══════╧═════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠔⠤⠤⠤⠤⠤⠤⠤⠤⠔⠤⠤⠤⠤⠤⠢⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⠀⡀⠀⠀⠀⡀⣀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠉⠈⠉⠉⠉⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⡠⠤⠒⠢⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠔⠊⠀⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠤⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⢀⠔⠉⠉⠑⢄⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠉⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⡠⠁⠀⠀⠀⠀⠀⠢⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⡐⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠒⢁⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠈⠄⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⡈⠄⠀⠀⠀⠀⠀⠀⠠⣁⠔⠊⠉⠉⠉⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠢⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⢖⠢⠤⠤⠤⢒⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⣇⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⣀⢀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
min(T)
  54001 in 4844ms
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

#### Constructor ([`BigIntConstructorTest`][BigIntConstructorTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════════╦═════════════╦═══════════╗
║                  ║ BigInteger  ║   BigInt    ║   int[]   ║
╠══════════════════╬══════╤══════╬══════╤══════╬═════╤═════╣
║  <init>(int,int) ║ +203 │ +215 ║   +0 │   +0 ║ +46 │ +47 ║
║      <init>(int) ║ +250 │ +237 ║   +7 │   +6 ║  +0 │  +0 ║
║ <init>(int,long) ║   +0 │   +0 ║ +147 │ +134 ║ +84 │ +87 ║
╚══════════════════╩══════╧══════╩══════╧══════╩═════╧═════╝

[runtime performance]
╔════════════════╦════════════╦════════╦═══════╗
║                ║ BigInteger ║ BigInt ║ int[] ║
╠════════════════╬════════════╬════════╬═══════╣
║   <init>(long) ║       +236 ║     +6 ║    +0 ║
║ <init>(String) ║         +0 ║   +314 ║  +330 ║
║ <init>(byte[]) ║        +27 ║     +0 ║   +20 ║
║ <init>(byte[]) ║        +33 ║     +0 ║   +28 ║
╚════════════════╩════════════╩════════╩═══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╦═══════════════╗
║                  ║  BigInteger   ║    BigInt     ║     int[]     ║
║                  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║  <init>(int,int) ║  +300 │    +∞ ║    +∞ │    +0 ║    +0 │    +0 ║
║      <init>(int) ║   +71 │  +166 ║    +∞ │    +0 ║    +0 │    +0 ║
║ <init>(int,long) ║    +0 │    +0 ║    +∞ │    +0 ║   +25 │    +0 ║
║     <init>(long) ║   +57 │  +144 ║    +∞ │    +0 ║    +0 │    +0 ║
║   <init>(String) ║    +0 │    +0 ║    +∞ │  +704 ║ +1109 │  +704 ║
║   <init>(byte[]) ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
║   <init>(byte[]) ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `<init>(int,int)`

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the [`BigIntegers.valueOf(int)`][BigIntegers] utility method is used to provide this missing function.

```
[runtime performance]
<init>(int,int)
  800441 in 1177ms
╔═════════╦═══════════╦═════════════════╦═════════════╦══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt  ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬════╤═════╬═════╤═════╣
║    │ 35 ║     │ -10 ║        │ 119921 ║    0 │   13 ║  0 │  37 ║   0 │  26 ║
║    │ 34 ║     │  -8 ║        │  79900 ║    0 │   13 ║  0 │  33 ║   0 │  24 ║
║    │ 34 ║     │  -6 ║        │  79921 ║    0 │   15 ║  0 │  40 ║   0 │  30 ║
║    │ 34 ║     │  -4 ║        │  79984 ║    0 │   14 ║  0 │  45 ║   0 │  28 ║
║ 34 │ 34 ║  -2 │  -2 ║ 396050 │  40005 ║   14 │   15 ║ 43 │  60 ║  28 │  30 ║
║ 34 │ 34 ║   0 │   0 ║ 404191 │  39984 ║   13 │    5 ║ 39 │  51 ║  28 │  33 ║
║    │ 34 ║     │   2 ║        │  79984 ║    0 │   13 ║  0 │  39 ║   0 │  28 ║
║    │ 34 ║     │   4 ║        │  79921 ║    0 │   15 ║  0 │  40 ║   0 │  30 ║
║    │ 34 ║     │   6 ║        │  79900 ║    0 │   13 ║  0 │  35 ║   0 │  25 ║
║    │ 34 ║     │   8 ║        │  79900 ║    0 │   13 ║  0 │  36 ║   0 │  26 ║
║    │ 35 ║     │  10 ║        │  39921 ║    0 │   14 ║  0 │  35 ║   0 │  25 ║
║    │    ║     │     ║        │   sum: ║   27 │  143 ║ 82 │ 451 ║  56 │ 305 ║
║    │    ║     │     ║        │    +%: ║ +203 │ +215 ║ +0 │  +0 ║ +46 │ +47 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠔⠒⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠢⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠔⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠢⠤⠤⠤⠤⠔⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⢄⣀⣀⡠⠤⠒⠒⠉⠉⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠒⠒⠒⠢⠤⢄⣀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⣀⠤⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠢⣀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠠⠊⠁⠀⠀⠀⠉⠉⠉⠑⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⣀⠤⠊⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⣀⣀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(int,int)
  4441 in 990ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║    │ 35 ║     │ -10 ║      │  521 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -8 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -6 ║      │  321 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -4 ║      │  384 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -2 │  -2 ║ 2166 │  205 ║     1 │     0 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 2075 │  184 ║     0 │     0 ║     0 │     2 ║     2 │     2 ║
║    │ 34 ║     │   2 ║      │  384 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   4 ║      │  321 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   6 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   8 ║      │  300 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  10 ║      │  121 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║      │ sum: ║     1 │     0 ║     0 │     4 ║     4 │     4 ║
║    │    ║     │     ║      │  +%: ║  +300 │    +∞ ║    +∞ │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `<init>(int)`

```
[runtime performance]
<init>(int)
  800441 in 813ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 35 │ 35 ║ -10 │ -10 ║ 119921 │ 119921 ║    6 │   11 ║  21 │  35 ║  27 │  37 ║
║ 34 │ 34 ║  -8 │  -8 ║  79900 │  79900 ║   28 │   12 ║  73 │  38 ║  74 │  41 ║
║ 34 │ 34 ║  -6 │  -6 ║  79921 │  79921 ║   19 │   13 ║  54 │  41 ║  57 │  42 ║
║ 34 │ 34 ║  -4 │  -4 ║  79984 │  79984 ║    5 │   13 ║  21 │  40 ║  27 │  45 ║
║ 34 │ 34 ║  -2 │  -2 ║  40005 │  40005 ║    4 │   13 ║  24 │  41 ║  30 │  44 ║
║ 34 │ 34 ║   0 │   0 ║  39984 │  39984 ║    4 │   13 ║  26 │  43 ║  29 │  44 ║
║ 34 │ 34 ║   2 │   2 ║  79984 │  79984 ║    5 │   13 ║  21 │  41 ║  25 │  43 ║
║ 34 │ 34 ║   4 │   4 ║  79921 │  79921 ║   19 │   13 ║  56 │  41 ║  54 │  43 ║
║ 34 │ 34 ║   6 │   6 ║  79900 │  79900 ║   25 │   12 ║  74 │  38 ║  71 │  41 ║
║ 34 │ 34 ║   8 │   8 ║  79900 │  79900 ║    5 │   10 ║  20 │  34 ║  24 │  38 ║
║ 35 │ 35 ║  10 │  10 ║  39921 │  39921 ║    6 │   12 ║  21 │  36 ║  24 │  38 ║
║    │    ║     │     ║        │   sum: ║  126 │  135 ║ 411 │ 428 ║ 442 │ 456 ║
║    │    ║     │     ║        │    +%: ║ +250 │ +237 ║  +7 │  +6 ║  +0 │  +0 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⣏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠉⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠈⠑⠒⠤⠤⣀⡠⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠄⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⢒⣉⡡⠤⠔⠒⠒⠒⠢⢄⡑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⢀⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠈⢂⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⢀⠤⠊⠉⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢕⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠡⡐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⡑⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠐⠌⢄⠀⠀⠀⠀⠀⠀⠀⡠⠔⡩⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⢠⠍⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠈⠢⡑⠤⣀⣀⠤⢔⡪⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠲⣤⣀⠀⠀⢀⡰⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(int)
  4441 in 74ms
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

##### `<init>(int,long)`

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the [`BigIntegers.valueOf(long)`][BigIntegers] utility method is used to provide this missing function.

```
[runtime performance]
<init>(int,long)
  289329 in 480ms
╔═════════╦═══════════╦════════════════╦═════════════╦═════════════╦═══════════╗
║ length  ║ precision ║     count      ║ BigInteger  ║   BigInt    ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤═══════╬══════╤══════╬══════╤══════╬═════╤═════╣
║    │ 36 ║     │ -19 ║        │ 37923 ║    0 │   84 ║    0 │   19 ║   0 │  23 ║
║    │ 35 ║     │ -15 ║        │ 22700 ║    0 │   82 ║    0 │   20 ║   0 │  24 ║
║    │ 35 ║     │ -11 ║        │ 30346 ║    0 │   83 ║    0 │   20 ║   0 │  25 ║
║    │ 34 ║     │  -7 ║        │ 22723 ║    0 │   84 ║    0 │   18 ║   0 │  24 ║
║ 34 │ 34 ║  -3 │  -3 ║ 176900 │ 30484 ║   46 │   83 ║   18 │   18 ║  26 │  23 ║
║ 34 │ 34 ║   0 │   0 ║ 112229 │ 15284 ║   48 │    9 ║   20 │   19 ║  25 │  22 ║
║    │ 34 ║     │   3 ║        │ 30346 ║    0 │   11 ║    0 │   17 ║   0 │  22 ║
║    │ 34 ║     │   7 ║        │ 22700 ║    0 │   11 ║    0 │   18 ║   0 │  23 ║
║    │ 35 ║     │  11 ║        │ 30300 ║    0 │   11 ║    0 │   18 ║   0 │  24 ║
║    │ 35 ║     │  15 ║        │ 22700 ║    0 │   10 ║    0 │   17 ║   0 │  23 ║
║    │ 36 ║     │  19 ║        │ 22723 ║    0 │   10 ║    0 │   20 ║   0 │  22 ║
║    │    ║     │     ║        │  sum: ║   94 │  478 ║   38 │  204 ║  51 │ 255 ║
║    │    ║     │     ║        │   +%: ║   +0 │   +0 ║ +147 │ +134 ║ +84 │ +87 ║
╚════╧════╩═════╧═════╩════════╧═══════╩══════╧══════╩══════╧══════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡧⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠔⠒⠒⠒⠢⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠑⠒⠒⠢⢄⣀⠀⠈⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢌⡠⠔⠒⠒⠒⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⠒⠒⠒⠒⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠈⠒⠤⣀⡀⠀⠀⢀⣀⢤⠊⠁⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠭⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠈⠉⠉⠁⢀⡢⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⠔⢒⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠒⠒⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⡐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⣀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(int,long)
  7749 in 347ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║    │ 36 ║     │ -19 ║      │  873 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │ -15 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │ -11 ║      │  706 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │  -7 ║      │  493 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 4820 │  844 ║     3 │     2 ║     0 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 2729 │  464 ║     2 │     2 ║     0 │     2 ║     2 │     2 ║
║    │ 34 ║     │   3 ║      │  706 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 34 ║     │   7 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  11 ║      │  660 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 35 ║     │  15 ║      │  470 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │ 36 ║     │  19 ║      │  493 ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║      │ sum: ║     5 │     4 ║     0 │     4 ║     4 │     4 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║    +∞ │    +0 ║   +25 │    +0 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `<init>(long)`

```
[runtime performance]
<init>(long)
  289329 in 365ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     36 ║       -19 ║ 37923 ║         33 ║     91 ║    93 ║
║     35 ║       -15 ║ 22700 ║         17 ║     44 ║    48 ║
║     35 ║       -11 ║ 30346 ║          6 ║     23 ║    28 ║
║     34 ║        -7 ║ 22723 ║          6 ║     21 ║    27 ║
║     34 ║        -3 ║ 30484 ║          6 ║     22 ║    36 ║
║     34 ║         0 ║ 15284 ║          6 ║     23 ║    26 ║
║     34 ║         3 ║ 30346 ║          6 ║     22 ║    26 ║
║     34 ║         7 ║ 22700 ║          6 ║     24 ║    26 ║
║     35 ║        11 ║ 30300 ║          7 ║     26 ║    28 ║
║     35 ║        15 ║ 22700 ║         30 ║     92 ║    90 ║
║     36 ║        19 ║ 22723 ║         30 ║     94 ║    87 ║
║        ║           ║  sum: ║        153 ║    482 ║   515 ║
║        ║           ║   +%: ║       +236 ║     +6 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⢉⣉⡩⠭⠛⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠉⠉⠉⠉⠉⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⢁⡠⠤⠔⠒⠒⠉⠉⠉⠉⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠛⠛⠫⠭⢦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⢊⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⣀⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⡠⡪⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢔⠕⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠠⡚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⣂⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢕⣄⣀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(long)
  7749 in 83ms
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

##### `<init>(String)`

```
[runtime performance]
<init>(String)
  33393 in 334ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     40 ║       -64 ║  3303 ║       1866 ║    376 ║   394 ║
║     39 ║       -51 ║  2997 ║       1378 ║    306 ║   301 ║
║     38 ║       -38 ║  2766 ║       1035 ║    264 ║   242 ║
║     36 ║       -25 ║  3022 ║        726 ║    190 ║   172 ║
║     35 ║       -12 ║  3047 ║        337 ║    129 ║   125 ║
║     34 ║         0 ║  2610 ║        302 ║    101 ║    89 ║
║     35 ║        12 ║  2972 ║        310 ║    127 ║   130 ║
║     36 ║        25 ║  3047 ║        798 ║    221 ║   184 ║
║     38 ║        38 ║  2741 ║       1108 ║    259 ║   259 ║
║     39 ║        51 ║  2997 ║       1392 ║    332 ║   304 ║
║     40 ║        64 ║  2791 ║       1891 ║    381 ║   389 ║
║        ║           ║  sum: ║      11143 ║   2686 ║  2589 ║
║        ║           ║   +%: ║         +0 ║   +314 ║  +330 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⣯⣭⠭⠭⠭⠭⠭⠭⠿⠿⠟⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠻⠿⠿⠿⠿⠭⠭⠭⠭⠭⠭⠭⢭⣭⣭⣭⣭⣭⣭⣭⣽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(String)
  17009 in 1195ms
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
║     36 ║        25 ║  1511 ║    23 │    17 ║     0 │     2 ║     2 │     2 ║
║     38 ║        38 ║  1333 ║    31 │    21 ║     0 │     2 ║     2 │     2 ║
║     39 ║        51 ║  1461 ║    33 │    22 ║     0 │     2 ║     2 │     2 ║
║     40 ║        64 ║  1383 ║    35 │    23 ║     0 │     2 ║     2 │     2 ║
║        ║           ║  sum: ║   266 │   177 ║     0 │    22 ║    22 │    22 ║
║        ║           ║   +%: ║    +0 │    +0 ║    +∞ │  +704 ║ +1109 │  +704 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `<init>(byte[])`

```
[runtime performance]
<init>(byte[])
  3481473 in 8635ms
╔════════╦═══════════╦═════════╦════════════╦════════╦═══════╗
║ length ║ precision ║  count  ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═════════╬════════════╬════════╬═══════╣
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║     34 ║         0 ║ 1146014 ║         44 ║     55 ║    46 ║
║     35 ║        12 ║ 1960135 ║         45 ║     57 ║    47 ║
║     36 ║        25 ║  375024 ║         44 ║     57 ║    47 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║    sum: ║        133 ║    169 ║   140 ║
║        ║           ║     +%: ║        +27 ║     +0 ║   +20 ║
╚════════╩═══════════╩═════════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢎⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠨⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠒⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠬⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡨⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠬⠁⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠑⢕⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⣒⡡⠁⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠔⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(byte[])
  1741049 in 18747ms
╔════════╦═══════════╦════════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║        ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬════════╬═══════════════╬═══════════════╬═══════════════╣
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║     34 ║         0 ║ 573422 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     35 ║        12 ║ 980195 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     36 ║        25 ║ 187132 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║   sum: ║     0 │     6 ║     0 │     6 ║     6 │     6 ║
║        ║           ║    +%: ║    +∞ │    +0 ║    +∞ │    +0 ║    +0 │    +0 ║
╚════════╩═══════════╩════════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `<init>(byte[])`

The `BigInteger` class does not support little endian `byte[]` encoding. Therefore, for this test, the input array is reversed just for `BigInteger`. The time for the array reversal _is not_ included in the runtime measure.

```
[runtime performance]
<init>(byte[])
  3481473 in 8666ms
╔════════╦═══════════╦═════════╦════════════╦════════╦═══════╗
║ length ║ precision ║  count  ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═════════╬════════════╬════════╬═══════╣
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║     34 ║         0 ║ 1146126 ║         41 ║     54 ║    42 ║
║     35 ║        12 ║ 1959899 ║         41 ║     56 ║    43 ║
║     36 ║        25 ║  375148 ║         41 ║     54 ║    43 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║         ║          0 ║      0 ║     0 ║
║        ║           ║    sum: ║        123 ║    164 ║   128 ║
║        ║           ║     +%: ║        +33 ║     +0 ║   +28 ║
╚════════╩═══════════╩═════════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠯⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡟⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠓⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⢡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⢃⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⢣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠅⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⢣⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠍⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⢒⠤⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⢔⠍⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡀⠀⠑⠒⠒⠒⠒⠒⠒⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠁⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
<init>(byte[])
  1741049 in 18801ms
╔════════╦═══════════╦════════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║        ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count  ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬════════╬═══════════════╬═══════════════╬═══════════════╣
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║        ║           ║        ║     0 │     0 ║     0 │     0 ║     0 │     0 ║
║     34 ║         0 ║ 573462 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     35 ║        12 ║ 980515 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
║     36 ║        25 ║ 186772 ║     0 │     2 ║     0 │     2 ║     2 │     2 ║
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
[BigIntBinaryTest]: src/test/java/org/libj/math/BigIntBinaryTest.java
[BigIntBitwiseTest]: src/test/java/org/libj/math/BigIntBitwiseTest.java
[BigIntConstructorTest]: src/test/java/org/libj/math/BigIntConstructorTest.java
[BigIntDivisionTest]: src/test/java/org/libj/math/BigIntDivisionTest.java
[BigIntMultiplicationTest]: src/test/java/org/libj/math/BigIntMultiplicationTest.java
[BigIntPredicateTest]: src/test/java/org/libj/math/BigIntPredicateTest.java
[BigIntRemainderTest]: src/test/java/org/libj/math/BigIntRemainderTest.java

[BigIntegers]: src/main/java/org/libj/math/BigIntegers.java
[Numbers]: /../../../lang/src/main/java/org/libj/lang/Numbers.java

[bytebuddy]: https://bytebuddy.net/
[byteman]: https://byteman.jboss.org/