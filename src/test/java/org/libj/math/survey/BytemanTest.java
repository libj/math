/* Copyright (c) 2020 LibJ
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

package org.libj.math.survey;

import static org.libj.math.survey.AuditRunner.Mode.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.BigInt;

@RunWith(AuditRunner.class)
@AuditRunner.Execution(PHASED)
@AuditRunner.Instrument({BigInt.class, int[].class})
@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BytemanTest {
  @Test
  public void testBigInteger(final AuditReport report) throws Exception {
    final BigInteger v = new BigInteger("432");
    System.out.println(v);
    System.out.println("Total BigInteger: " + report.getAllocations(BigInteger.class));
    System.out.println("Total int[]: " + report.getAllocations(int[].class));
    report.dump();
  }

  @Test
  public void testBigInt(final AuditReport report) throws Exception {
    final BigInt v = new BigInt("432");
    System.out.println(v);
    System.out.println("Total BigInt: " + report.getAllocations(BigInt.class));
    System.out.println("Total int[]: " + report.getAllocations(int[].class));
    report.dump();
  }
}