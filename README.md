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
1. **Significantly reduced heap allocation:** `BigInt` was meticulously designed to reduce the number of instances allocated purely for the purpose of transient calculation, and significantly outperforms `BigInteger` with regard to memory and GC load.

##### Bare `int[]` <ins>value-encoded number</ins> arrays

The `BigInt` architecture exposes the underlying `int[]` array, and provides static function equivalents for all of its instance methods. The bare `int[]` array can therefore be used as a feature-equivalent replacement for `BigInt`, with one notable difference: no `BigInt` instance is required.

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
| [`add(int)`](BigIntAddition.md#addintint)<sup>unsigned</sup> | ~0~ | 193% | 188% |
| [`add(int)`](BigIntAddition.md#addint)<sup>signed</sup> | ~0~ | +337% | +359% |
| [`add(long)`](BigIntAddition.md#addintlong)<sup>unsigned</sup> | ~0~ | +299% | +294% |
| [`add(long)`](BigIntAddition.md#addlong)<sup>signed</sup> | ~0~ | +255% | +330% |
| [`add(T)`](BigIntAddition.md#addt) | 0 | +21% | +58% |
| [`sub(int)`](BigIntAddition.md#subintint)<sup>unsigned</sup> | ~0~ | +156% | +195% |
| [`sub(int)`](BigIntAddition.md#subint)<sup>signed</sup> | ~0~ | +251% | +377% |
| [`sub(long)`](BigIntAddition.md#subintlong)<sup>unsigned</sup> | ~0~ | +155% | +324% |
| [`sub(long)`](BigIntAddition.md#sublong)<sup>signed</sup> | ~0~ | +215% | +300 |
| [`sub(T)`](BigIntAddition.md#subt) | 0 | +37% | +97% |
| [`mul(int)`](BigIntMultiplication.md#mulintint)<sup>unsigned</sup> | ~0~ | +79% | +95% |
| [`mul(int)`](BigIntMultiplication.md#mulint)<sup>signed</sup> | ~0~ | +99% | +157% |
| [`mul(long)`](BigIntMultiplication.md#mulintlong)<sup>unsigned</sup> | ~0~ | +193% | +128% |
| [`mul(long)`](BigIntMultiplication.md#mullong)<sup>signed</sup> | ~0~ | +125% | +195% |
| [`mul(T)`](BigIntMultiplication.md#mult) | +75% | 0 | +5% |
| [`div(int)`](BigIntDivision.md#divintint)<sup>unsigned</sup> | ~0~ | +116% | +107% |
| [`div(int)`](BigIntDivision.md#divint)<sup>signed</sup> | ~0~ | +172% | +100% |
| [`div(long)`](BigIntDivision.md#divintlong)<sup>unsigned</sup> | ~0~ | +240% | +255% |
| [`div(long)`](BigIntDivision.md#divlong)<sup>signed</sup> | ~0~ | +146% | +144% |
| [`div(T)`](BigIntDivision.md#divt) | 0 | +29% | +44% |
| [`divRem(int)`](BigIntDivision.md#divremintint)<sup>unsigned</sup> | ~0~ | +57% | +65% |
| [`divRem(int)`](BigIntDivision.md#divremint)<sup>signed</sup> | ~0~ | +144% | +135% |
| [`divRem(long)`](BigIntDivision.md#divremintlong)<sup>unsigned</sup> | ~0~ | +179% | +183% |
| [`divRem(long)`](BigIntDivision.md#divremlong)<sup>signed</sup> | ~0~ | +141% | +142% |
| [`divRem(T)`](BigIntDivision.md#divremt) | ~0~ | +2% | +8% |
| [`rem(int)`](BigIntRemainder.md#remintint)<sup>unsigned</sup> | ~0~ | +140% | +120% |
| [`rem(int)`](BigIntRemainder.md#remint)<sup>signed</sup> | ~0~ | +172% | +128% |
| [`rem(long)`](BigIntRemainder.md#remintlong)<sup>unsigned</sup> | ~0~ | +159% | +253% |
| [`rem(long)`](BigIntRemainder.md#remlong)<sup>signed</sup> | ~0~ | +143% | +150% |
| [`rem(T)`](BigIntRemainder.md#remt) | ~0~ | +47% | +63% |
| [`mod(T)`](BigIntRemainder.md#modt-1) | ~0~ | +55% | +73% |
| [`bitCount()`](BigIntBitwise.md#bitcount) | 0 | +68% | +63% |
| [`bitLength()`](BigIntBitwise.md#bitlength) | 0 | +81% | +205% |
| [`testBit(int)`](BigIntBitwise.md#testbitint) | 0 | +29% | +50% |
| [`setBit(int)`](BigIntBitwise.md#setbitint) | 0 | +800% | +1100% |
| [`clearBit(int)`](BigIntBitwise.md#clearbitint) | 0 | +440% | +590% |
| [`flipBit(int)`](BigIntBitwise.md#flipbitint) | 0 | +508% | +905% |
| [`shiftLeft(int)`](BigIntBitwise.md#shiftleftint) | +23% | 0 | +25% |
| [`shiftRight(int)`](BigIntBitwise.md#shiftrightint) | +4% | 0 | +59% |
| [`and(T)`](BigIntBinary.md#andt) | 0 | +289% | +476% |
| [`or(T)`](BigIntBinary.md#ort) | 0 | +458% | +711% |
| [`xor(T)`](BigIntBinary.md#xort) | 0 | +335% | +440% |
| [`andNot(T)`](BigIntBinary.md#andnott) | 0 | +325% | +505% |
| [`not()`](BigIntBinary.md#not) | 0 | +589% | +2784% |
| [`abs()`](BigIntPredicate.md#abs) | 0 | +100% | +87% |
| [`neg()`](BigIntPredicate.md#neg) | 0 | +50% | +1500% |
| [`max(T)`](BigIntPredicate.md#maxt) | +34% | 0 | +48% |
| [`min(T)`](BigIntPredicate.md#mint) | +30% | 0 | +23% |
| [`signum()`](BigIntPredicate.md#signum) | +293% | 0 | +8% |
| [`precision()`](BigIntPredicate.md#precision) | ~0~ | +2067% | +2477% |
| [`byteValue()`](BigIntPredicate.md#bytevalue) | 0 | +389% | +97% |
| [`shortValue()`](BigIntPredicate.md#shortvalue) | +39% | 0 | +139% |
| [`intValue()`](BigIntPredicate.md#intvalue) | +8% | 0 | +68% |
| [`longValue()`](BigIntPredicate.md#longvalue) | 0 | +208% | +230% |
| [`floatValue()`](BigIntPredicate.md#floatvalue) | 0 | +29% | +96% |
| [`doubleValue()`](BigIntPredicate.md#doublevalue) | 0 | +19% | +123% |
| [`toByteArray()`](BigIntPredicate.md#tobytearray-be) | 0 | +23% | +15% |
| [`compareTo(T)`](BigIntPredicate.md#comparetot) | +215% | +89% | 0 |
| [`equals(T)`](BigIntPredicate.md#equalst) | 0 | +3% | +22% |
| [`hashCode()`](BigIntPredicate.md#hashcode) | +39% | +5% | 0 |
| [`toString()`](BigIntPredicate.md#tostring) | 0 | +349% | +360% |
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

#### Benchmark Results

* All tests were run with Java 1.8.0_231.
* All tests were run with `-Xcomp` argument, in order to ensure JIT optimizations are enabled.
* For `BigInt`, execution with `-Xcomp` causes it to load its <ins>critical native</ins> JNI bindings.

The benchmark results are too lengthy to include in README.md, therefore they have been separated into their own `.md` files:

Benchmark tests are organized in 5 distinct test classes, each responsible for its context of functions:

| Results link | Test code link |
|-|-|
| [Addition and subtraction](BigIntAddition.md) | [`BigIntAdditionTest`][BigIntAdditionTest] |
| [Multiplication](BigIntMultiplication.md) | [`BigIntMultiplicationTest`][BigIntMultiplicationTest] |
| [Division](BigIntDivision.md) | [`BigIntDivisionTest`][BigIntDivisionTest] |
| [Remainder and modulus](BigIntRemainder.md) | [`BigIntRemainderTest`][BigIntRemainderTest] |
| [Binary operations](BigIntBinary.md) | [`BigIntBinaryTest`][BigIntBinaryTest] |
| [Bitwise operations](BigIntBitwise.md) | [`BigIntBitwiseTest`][BigIntBitwiseTest] |
| [Predicate functions](BigIntPredicate.md) | [`BigIntPredicateTest`][BigIntPredicateTest] |
| [Value constructors](BigIntConstructor.md) | [`BigIntConstructorTest`][BigIntConstructorTest] |

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