# Changes by Version

## [v0.6.8-SNAPSHOT](https://github.com/libj/util/compare/417b60d734d427ddf74dafeed3f2c662efbd61ce..HEAD)

## [v0.6.7](https://github.com/libj/math/compare/0c41e394a3b238a3a329ebd80753c0fe58ebae02..417b60d734d427ddf74dafeed3f2c662efbd61ce) (2023-09-20)
* #35 Implement Groups.permute(...)
* #34 Implement SafeMath.gcd(...)
* #33 Refactor SplineInterpolator with generic adapters
* #32 Support M1 arm processor (aarch64)
* #30 Implement SlidingWindowAverage
* #29 Implement SafeMath.factorial(...)
* #28 Support SafeMath.min(...) and SafeMath.max(...) for boxed Number(s)
* #27 Support SafeMath.toRadians() and SafeMath.toDegrees()
* #26 Transition to GitHub Actions
* #25 Move BigDecimals and BitIntegers to lang module
* #24 Add DecimalMath
* #23 Add BitIntMath
* #22 Add BigDecimals.setScale
* #21 Support RoundingMode in SafeMath.round(...) methods
* #18 Implement min(...) and max(...) in SafeMath
* #17 Add Constants.SQRT_2
* #16 Improve tests for MovingNormal
* #15 Improve tests for MovingAverage
* #14 Improve tests for BigDecimals
* #13 Improve tests for BigIntegers
* #11 Implement tests for all functions in SafeMath
* #6 Implement FastMath.e10([0,18])
* #5 Type-safe overloads for SafeMath.log2(...) and SafeMath.log10(...)
* #3 Implement FastMath
* #2 Fix incorrect implementations of SafeMath.abs(...)
* #1 Implement intern(...) for BigIntegers and BigDecimals

## [v0.6.6](https://github.com/libj/math/compare/e4c6262e88a009172d8d700e39871be1575ccc5e..0c41e394a3b238a3a329ebd80753c0fe58ebae02) (2020-05-23)
* Upgrade `ch.obermuhlner:big-math` from `v2.1.0` to `v2.3.0`.
* Apply concurrency considerations to `BigDecimals.instances` and `BigIntegers.instances`.
* Improve javadocs.

## [v0.6.5](https://github.com/libj/math/compare/54e2fb9bed48d6cfdce6708b6bc4b60039230d68..e4c6262e88a009172d8d700e39871be1575ccc5e) (2019-07-21)
* Upgrade `org.libj:test:0.6.9` to `0.7.0`.
* Implement methods in `FastMath`: `digit()`, `isDigit()`, `parseInt()`, and `parseLong()`.

## [v0.6.4](https://github.com/entinae/pom/compare/e33361b9724d068fc9fc93458de44bc1001a2f8f..e4c6262e88a009172d8d700e39871be1575ccc5e) (2019-05-13)
* Initial public release.