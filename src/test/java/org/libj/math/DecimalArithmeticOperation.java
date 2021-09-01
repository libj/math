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

package org.libj.math;

import static org.libj.math.Decimal.*;
import static org.libj.math.DecimalOperationTest.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.libj.lang.BigDecimals;
import org.libj.lang.BigIntegers;
import org.libj.lang.Numbers;

abstract class DecimalArithmeticOperation extends DecimalOperation<Long,BigDecimal> {
  DecimalArithmeticOperation(final String label, final Class<?> arg, final String operator) {
    super(label, arg, operator);
  }

  abstract BigDecimal epsilon(byte bits);

  @Override
  BigDecimal run(final BigDecimal bd1, final BigDecimal bd2, final BigDecimal expected, final Long actual, final long defaultValue, final BigDecimal[] errors, final boolean[] failures) {
    if (expected == null)
      return actual == defaultValue ? DEFAULT : BigDecimal.ONE;

    if (expected.signum() == 0)
      return compare(0, actual) == 0 ? DEFAULT : BigDecimals.TWO;

    BigInteger unscaled = expected.unscaledValue();
    int pscale = expected.scale() - expected.precision();
    int len;
    if (!lockScale()) {
      len = Numbers.precision(unscaled);
      if (len > 19) {
        final int negOffset = unscaled.signum() < 0 ? 1 : 0;
        unscaled = BigIntegers.intern(unscaled.toString().substring(negOffset, 19 + negOffset));
        pscale -= len - 19;
      }
      else {
        final int maximizeScale = 20 - len;
        unscaled = unscaled.multiply(BigInteger.TEN.pow(maximizeScale));
        pscale += maximizeScale;
      }

      // Turn the significand to be highest precision available for the Decimal of the provided bits
      while (unscaled.signum() < 0 ? unscaled.compareTo(minValue) < 0 : unscaled.compareTo(maxValue) > 0) {
        unscaled = BigIntegers.intern(unscaled.divide(BigInteger.TEN));
        --pscale;
      }
    }

    final boolean expectDefaultValue;
    if (unscaled.signum() < 0 ? unscaled.compareTo(minValue) < 0 : unscaled.compareTo(maxValue) > 0) {
      expectDefaultValue = true;
    }
    else {
      if (Decimal.MIN_PSCALE <= pscale && pscale <= Decimal.MAX_PSCALE) {
        expectDefaultValue = false;
      }
      else {
        len = Numbers.precision(unscaled);
        final int diffScale = pscale - (pscale < 0 ? Decimal.MIN_PSCALE : Decimal.MAX_PSCALE);
        expectDefaultValue = diffScale < 0 || len - diffScale <= 0;
      }
    }

    if (expectDefaultValue)
      return actual == defaultValue ? DEFAULT : EXPECTED_DEFAULT;

    if (actual == defaultValue)
      return UNEXPECTED_DEFAULT;

    final short s = scale(actual);
    final BigDecimal expectedScaled = expected.setScale(s, RoundingMode.HALF_UP);

    final BigDecimal result = toBigDecimal(actual);
    final BigDecimal error = expectedScaled.subtract(result).abs().divide(expected, precision16);
    errors[0] = errors[0] == null ? error : errors[0].max(error);

    final BigDecimal expectedError = epsilon((byte)0);
    final boolean pass = error.signum() == 0 || error.compareTo(expectedError) <= 0;
    failures[0] |= !pass;
    return pass ? null : expectedError;
  }
}