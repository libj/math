/* Copyright (c) 2012 lib4j
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

package org.lib4j.math;

/**
 * https://ccrma.stanford.edu/~jos/parshl/Peak_Detection_Steps_3.html
 * https://ccrma.stanford.edu/~jos/sasp/Quadratic_Interpolation_Spectral_Peaks.html
 */
public final class QuadraticInterpolation {
  public static double calcP(final double ym1, final double y0, final double yp1) {
    return (yp1 - ym1) / (2d * (2d * y0 - yp1 - ym1));
  }

  public static double calcY(final double ym1, final double y0, final double yp1, final double p) {
    return y0 - 0.25d * (ym1 - yp1) * p;
  }

  private QuadraticInterpolation() {
  }
}