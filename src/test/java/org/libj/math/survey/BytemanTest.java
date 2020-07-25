package org.libj.math.survey;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.BigInt;

@RunWith(AuditRunner.class)
@AuditRunner.Instrument({BigInt.class, int[].class})
@AuditRunner.Instrument({BigInteger.class, int[].class})
public class BytemanTest {
  @Test
  public void test(final AuditReport report) throws Exception {
    new BigInteger("432");
    System.err.println("HELLO");
    report.dump();
    System.err.println("Total int[]: " + report.getAllocations(int[].class));
    System.err.println("Total BigInteger: " + report.getAllocations(BigInteger.class));
  }
}