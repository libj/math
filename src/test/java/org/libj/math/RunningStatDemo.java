/* Copyright (c) 2021 LibJ
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

import org.junit.Test;

public class RunningStatDemo {
  @Test
  public void test() {
    RunningStat rs = new RunningStat();
    rs.put(1);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());

    rs.put(1);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());

    rs.put(10);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());

    rs.put(20);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());

    rs.put(50);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());

    rs.put(50);
    System.out.println("ave: " + rs.getAverage());
    System.out.println("std: " + rs.getStandardDeviation());
  }

  public class RunningStat {
    private int count = 0;
    private double average = 0.0;
    private double pwrSumAvg = 0.0;

    /**
     * Incoming new values used to calculate the running statistics
     *
     * @param value
     */
    public void put(final double value) {
      ++count;
      average += (value - average) / count;
      pwrSumAvg += (value * value - pwrSumAvg) / count;
    }

    public double getAverage() {
      return average;
    }

    public double getStandardDeviation() {
      final double stdDev = Math.sqrt((pwrSumAvg * count - count * average * average) / (count - 1));
      return Double.isNaN(stdDev) ? 0 : stdDev;
    }
  }
}