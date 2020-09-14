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

abstract class DecimalMultiplicativeOperation extends DecimalArithmeticOperation {
  DecimalMultiplicativeOperation(final String label, final Class<?> arg, final String operator) {
    super(label, arg, operator);
  }

  @Override
  int maxValuePower(final byte bits) {
    // For multiply and divide, the maxValue should not be so big as to cause the result to overrun max/min value
    return (byte)(bits == 6 ? 55 : bits == 5 ? 43 : bits == 4 ? 36 : bits == 3 ? 33 : bits == 2 ? 31 : 30);
  }

  @Override
  short randomScale(final byte bits) {
    final short scale = super.randomScale(bits);
    final short abs = SafeMath.abs(scale);
    return (short)(abs < 4 ? scale : scale / 2);
  }
}