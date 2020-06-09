# LibJ Math

[![Build Status](https://travis-ci.org/libj/math.svg?1)](https://travis-ci.org/libj/math)
[![Coverage Status](https://coveralls.io/repos/github/libj/math/badge.svg?branch=master)](https://coveralls.io/github/libj/math?branch=master)
[![Javadocs](https://www.javadoc.io/badge/org.libj/math.svg?1)](https://www.javadoc.io/doc/org.libj/math)
[![Released Version](https://img.shields.io/maven-central/v/org.libj/math.svg?1)](https://mvnrepository.com/artifact/org.libj/math)
![Snapshot Version](https://img.shields.io/nexus/s/org.libj/math?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

LibJ Math is a Java API Extensions to `java.math`.

## `LongDecimal`

The `LongDecimal` is like `BigDecimal`, but <ins>without the overhead of heap allocation</ins>, and the following **pros** (:white_check_mark:) and **cons** (:x:):

| | `BigDecimal` | `LongDecimal` |
|-|:-------------:|:------------:|
| No heap allocation | :x: | :white_check_mark: |
| Overflow detection | :x: | :white_check_mark: |
| Arbitrary precision | :white_check_mark: |  :x: |

To avoid heap allocation, the `LongDecimal` represents fixed-point decimals inside a `long` primitive value, which thus limits the precision of a `LongDecimal`-encoded decimal to 64 bits. The representation of a decimal value in `LongDecimal` encoding is achieved with the following model:

To represent a value such as `1234567.89`, a `LongDecimal`-encoded `long` has two numbers inside it:

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

A decimal represented with `LongDecimal` encoding can be reconstituted with **`value`** and **`scale`** by multiplying the **`value`** by ten to the power of the negation of the **`scale`** (`value ✕ 10⁻ˢᶜᵃˡᵉ`), i.e.:

```
123456789 ✕ 10⁻² = 1234567.89
```

To maximize precision, the `LongDecimal` encoding implements a <ins>variable range</ins> for the **`scale`**. The <ins>variable range</ins> is defined by a `bits` variable representing the number of bits inside the 64-bit `long` that are reserved for the signed representation of the **`scale`**. This effectively means that the <ins>variable range</ins> of **`value`** is `64 - bits`.

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

The following illustrates the way `LongDecimal` encodes the **`value`** and **`scale`** inside a `long` primitive:

```
        . scale sign bit (for bits > 0)
       /
      1
.---+-------- // --------+------------------------------------ // -------------------------------------.
|   |---.----------------|                                                                             |
|   |   |     scale      |                                   value                                     |
|   |---'----------------|                                                                             |
'---+---+---- // --------+----------------------------------- // --------------------------------------'
  0      [1, bits+1]                                   [bits+1, 63-bits]
   \
    ` value sign bit
```

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.