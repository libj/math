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

package gnu.java.math;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;
import org.libj.test.TestAide;

public class BasicTest {
  static Random rnd = new Random();

  @Test
  public void testAddInt() {
    final BigInt a = new BigInt(0);
    a.uadd((int)3972540614L);
    assertEquals("3972540614", a.toString());
  }

  @Test
  public void testSubInt() {
    final BigInt a = new BigInt(0);
    a.sub(Integer.MIN_VALUE);
    assertEquals(String.valueOf(-(long)Integer.MIN_VALUE), a.toString());

    final BigInt b = new BigInt(0);
    b.sub(Integer.MAX_VALUE);
    assertEquals(String.valueOf(-Integer.MAX_VALUE), b.toString());
  }

  @Test
  public void testSubLong() {
    final BigInt a = new BigInt(0);
    a.sub(Long.MIN_VALUE);
    assertEquals("9223372036854775808", a.toString());

    final BigInt b = new BigInt(0);
    b.sub(Long.MAX_VALUE);
    assertEquals(String.valueOf(-Long.MAX_VALUE), b.toString());
  }

  @Test
  public void constructorTest() {
    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String ans = new BigInt(s).toString();
    assertEquals("Error in toString()", s, ans);
    assertEquals("Error 4M", "4000000000", new BigInt("4000000000").toString());
    assertEquals("Error", "3928649759", new BigInt("3928649759").toString());
    BigInt me = new BigInt(s);
    me.umul(0);
    assertEquals("Zero string", "0", me.toString());
    me = new BigInt("0");
    assertEquals("Zero string2", "0", me.toString());
    byte[] littleEndian = {35, 47, 32, 45, 93, 0, 1, 0, 0, 0, 0, 0};
    byte[] bigEndian = {1, 0, 93, 45, 32, 47, 35};
    assertEquals("Byte[] constructor", new BigInteger(1, bigEndian).toString(), new BigInt(1, 10, littleEndian).toString());
    assertEquals("Byte[] 0 constructor", "0", new BigInt(1, 3, new byte[] {0, 0, 0}).toString());
    // Add test case covering length-increase due to add in mulAdd().
  }

  @Test
  public void addTest() {
    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String t = "2374283475698324756873245832748";
    BigInteger facit = new BigInteger(s).add(new BigInteger(t));

    BigInt me = new BigInt(s);
    me.add(new BigInt(t));
    assertEquals("Add", facit.toString(), me.toString());

    me = new BigInt(t);
    me.add(new BigInt(s));
    assertEquals("Add2", facit.toString(), me.toString());

    facit = new BigInteger(s);
    facit = facit.add(facit);
    me.assign(s);
    me.add(me);
    assertEquals("Add3", facit.toString(), me.toString());

    facit = new BigInteger(t);
    facit = facit.add(facit);
    me.assign(t);
    me.add(me);
    assertEquals("Add4", facit.toString(), me.toString());

    me = new BigInt("0");
    facit = BigInteger.ZERO;
    for (int i = 0; i < 1337; ++i) {
      long tmp = rnd.nextLong() & ((1L << 32) - 1);
      me.uadd((int)tmp);
      final BigInteger x = facit;
      facit = facit.add(BigInteger.valueOf(tmp));
      assertEquals("For-loop " + i + ": " + me + " " + x + " Added: " + tmp + "\n", facit.toString(), me.toString());
      tmp = rnd.nextLong() >>> 1;
      me.uadd(tmp);
      facit = facit.add(BigInteger.valueOf(tmp));
      assertEquals("For-loop2 " + i + ": " + me + " " + facit + " Added: " + tmp + "\n", facit.toString(), me.toString());
    }
  }

  @Test
  public void subTest() {
    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String t = "2374283475698324756873245832748";
    BigInt me = new BigInt(s);
    me.sub(new BigInt(s));
    assertEquals("Sub to zero", "0", me.toString());
    me = new BigInt(t);
    me.sub(new BigInt(t));
    assertEquals("Sub2 to zero", "0", me.toString());
    me = new BigInt("1337");
    me.usub(1337);
    assertEquals("Small sub", "0", me.toString());
    me = new BigInt("4000000000");
    me.sub(new BigInt("2000000000"));
    assertEquals("Small sub", "2000000000", me.toString());

    BigInteger facit = new BigInteger(s).subtract(new BigInteger(t));
    me = new BigInt(s);
    me.sub(new BigInt(t));
    assertEquals("Sub", facit.toString(), me.toString());

    facit = new BigInteger(t).subtract(new BigInteger(s));
    me = new BigInt(t);
    BigInt tmp = new BigInt("-" + s);
    me.add(tmp);
    assertEquals("Sub2", facit.toString(), me.toString());

    me.umul(0);
    me.usub(1);
    assertEquals("From 0 to -1", "-1", me.toString());
    me.mul(-16);
    assertEquals("From -1 to 16", "16", me.toString());
    me.div(-4);
    assertEquals("From 16 to -4", "-4", me.toString());
  }

  @Test
  public void mulTest() {
    BigInt me = new BigInt("2000000000");
    me.umul(3);
    assertEquals("Small", "6000000000", me.toString());
    me = new BigInt("4000000000");
    me.mul(me);
    assertEquals("Two", "16000000000000000000", me.toString());

    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String t = "2374283475698324756873245832748";
    BigInteger facit = new BigInteger(s).multiply(new BigInteger(t));

    me = new BigInt(t);
    me.mul(new BigInt(s));
    assertEquals("Mul ", facit.toString(), me.toString());

    me.umul(0);
    me.uadd(1);
    assertEquals("0 to 1", "1", me.toString());
    me.mul(new BigInt(s));
    assertEquals("1 to s", s, me.toString());
    me.mul(new BigInt(t));
    assertEquals("Mul2", facit.toString(), me.toString());

    facit = new BigInteger(1, new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
    me = new BigInt(new int[] {6, 1, -1, -1, -1, -1});
    BigInteger ulong = new BigInteger(1, new byte[] {-1, -1, -1, -1, -1, -1, -1, -1});
    for (int i = 0; i < 256; i++) {
      facit = facit.multiply(ulong);
      me.umul(-1L);
      assertEquals("Long mul " + i, facit.toString(), me.toString());
    }
  }

  @Test
  public void divTest() {
    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String t = "2374283475698324756873245832748";
    BigInteger facit = new BigInteger(s).divide(BigInteger.valueOf(1337));
    BigInt me = new BigInt(s);
    me.udiv(1337);
    assertEquals("Div ", facit.toString(), me.toString());

    facit = new BigInteger(s + t + s).divide(BigInteger.valueOf((1L << 32) - 1));
    me = new BigInt(s + t + s);
    me.udiv(-1);
    assertEquals("Div2 ", facit.toString(), me.toString());

    facit = new BigInteger(s).divide(new BigInteger(t));
    me = new BigInt(s);
    BigInt tmp = new BigInt(t);
    me.div(tmp);
    assertEquals("Div3 ", facit.toString(), me.toString());

    me.div(new BigInt(s));
    assertEquals("Should be 0", "0", me.toString());

    facit = new BigInteger(s).divide(new BigInteger("-" + t + t));
    me.assign(s);
    tmp.assign("-" + t + t);
    me.div(tmp);
    assertEquals("Div4 ", facit.toString(), me.toString());

    s = "253187224242823454860064468797249161593623134834254603067018";
    t = "434771785074759645588146668555";
    facit = new BigInteger(s).divide(new BigInteger(t));
    me = new BigInt(s);
    me.div(new BigInt(t));
    assertEquals("Div5 ", facit.toString(), me.toString());

    final int[] m = {2, 2, 2, 2};
    final int[] n = {1, 2, 2, 2};
    final int[][] u = {{4, 1, 0xfffe0000, 0x8000}, {4, 1, 0x00000003, 0x8000}, {4, 1, 0, 0x7fff8000}, {4, 1, 0xfffe0000, 0x80000000}};
    final int[][] v = {{3, 1, 0x8000ffff}, {4, 1, 0x00000001, 0x2000}, {4, 1, 1, 0x8000}, {4, 1, 0xffff, 0x8000}};
    final int[][] q = {{3, 1, 0xffff}, {3, 1, 0x0003}, {3, 1, 0xfffe}, {3, 1, 0xffff}};
    final int[][] r = {{3, 1, 0x7fffffff}, {4, 1, 0, 0x2000}, {4, 1, 0xffff0002, 0x7fff}, {4, 1, 0xffffffff, 0x7fff}};

    for (int i = 0; i < 4; i++) {
      int[] x = u[i].clone();
      x[0] = m[i] + 2;
      int[] y = v[i].clone();
      y[0] = n[i] + 2;
      BigInt a = new BigInt(x), b = new BigInt(y);
      BigInt rem = a.divRem(b);
      x = q[i].clone();
      y = r[i].clone();
      assertEquals("Hack div " + i, new BigInt(x).toString(), a.toString());
      assertEquals("Hack rem " + i, new BigInt(y).toString(), rem.toString());
    }

    s = "170141183460469231750134047781003722752";
    t = "39614081257132168801066942463";
    me = new BigInt(s); // me = new BigInt(1, new
                        // int[]{0,0xfffffffe,0,0x80000000}, 4);
    tmp = new BigInt(t); // tmp = new BigInt(1, new
                         // int[]{0xffffffff,0,0x80000000}, 3);

    facit = new BigInteger(s).divide(new BigInteger(t));
    BigInt rr = me.divRem(tmp);
    assertEquals("Div shift-32 ", facit.toString(), me.toString());
    facit = new BigInteger(s).remainder(new BigInteger(t));
    assertEquals("Rem shift-32 ", facit.toString(), rr.toString());

    me = new BigInt(new int[] {6, 1, 0, 0, 0x80000000, 0x7fffffff});
    tmp = new BigInt(new int[] {5, 1, 1, 0, 0x80000000});
    BigInteger[] ans = new BigInteger(me.toString()).divideAndRemainder(new BigInteger(tmp.toString()));
    rr = me.divRem(tmp);
    assertEquals("Div addback ", ans[0].toString(), me.toString());
    assertEquals("Rem addback ", ans[1].toString(), rr.toString());

    me = new BigInt(new int[] {5, 1, 0x0003, 0x0000, 0x80000000});
    tmp = new BigInt(new int[] {5, 1, 0x0001, 0x0000, 0x20000000});
    ans = new BigInteger(me.toString()).divideAndRemainder(new BigInteger(tmp.toString()));
    rr = me.divRem(tmp);
    assertEquals("Div addback2 ", ans[0].toString(), me.toString());
    assertEquals("Rem addback2 ", ans[1].toString(), rr.toString());

    me = new BigInt(new int[] {5, 1, 0x0000, 0xfffffffe, 0x80000000});
    tmp = new BigInt(new int[] {4, 1, 0xffffffff, 0x80000000});
    ans = new BigInteger(me.toString()).divideAndRemainder(new BigInteger(tmp.toString()));
    rr = me.divRem(tmp);
    assertEquals("Div qhat=b+1 ", ans[0].toString(), me.toString());
    assertEquals("Rem qhat=b+1 ", ans[1].toString(), rr.toString());
  }

  @Test
  public void remTest() {
    String s = "246313781983713469235139859013498018470170100003957203570275438387";
    String t = "2374283475698324756873245832748";
    BigInteger facit = new BigInteger(s).remainder(BigInteger.valueOf(1337));
    BigInt me = new BigInt(s);
    me.urem(1337);
    assertEquals("Rem ", facit.toString(), me.toString());

    facit = new BigInteger(s + t + s).remainder(BigInteger.valueOf((1L << 32) - 1));
    me = new BigInt(s + t + s);
    me.urem(-1);
    assertEquals("Rem2 ", facit.toString(), me.toString());

    facit = new BigInteger(s).remainder(new BigInteger(t));
    me = new BigInt(s);
    BigInt tmp = new BigInt(t);
    me.rem(tmp);
    assertEquals("Rem3 ", facit.toString(), me.toString());

    me.rem(me);
    assertEquals("Should be 0", "0", me.toString());

    facit = new BigInteger(s).remainder(new BigInteger("-" + t + t));
    me.assign(s);
    tmp.assign("-" + t + t);
    me.rem(tmp);
    assertEquals("Rem4 ", facit.toString(), me.toString());
  }

  @Test
  public void longDivTest() { // Division test using long as parameter.
    for (int i = 0; i < 100; i++) {// System.err.println(i+" divs");
      final char[] s = getRndNumber(1 + i * 10);
      BigInteger facit = new BigInteger(new String(s));
      final BigInt dividend = new BigInt(s);
      while (!dividend.isZero()) {
        final long d = rnd.nextLong();
        if (d == 0)
          continue;
        final byte[] div = new byte[8];
        long tmp = d;
        for (int j = 7; j >= 0; j--, tmp >>>= 8)
          div[j] = (byte)(tmp & 0xFF);
        BigInteger prv = facit;
        facit = facit.divide(new BigInteger(1, div));
        dividend.udiv(d);
        assertEquals("" + prv + "/" + d + "", facit.toString(), dividend.toString());
      }
    }
  }

  private static char[] getRndNumber(final int len) {
    final int sign = rnd.nextInt(2);
    final char[] num = new char[len + sign];
    if (sign > 0)
      num[0] = '-';
    num[sign] = (char)('1' + rnd.nextInt(9));
    for (int i = sign + 1; i < len + sign; i++)
      num[i] = (char)('0' + rnd.nextInt(10));
    return num;
  }

  @Test
  public void testLongCastInMul() {
    BigInt a = new BigInt("1000000000000000");
    BigInt b = new BigInt("1000000000000000");
    a.mul(b);
    assertEquals("10^15 * 10^15", "1000000000000000000000000000000", a.toString());
  }

  @Test
  public void testLongZeroAdd() {
    BigInt a = new BigInt(0);
    a.add(0L);
    assertEquals("add(0L)", true, a.isZero());
    a.uadd(0L);
    assertEquals("uadd(0L)", true, a.isZero());
    a.add(-1L);
    a.add(2L);
    a.add(-1L);
    assertEquals("-1L + 2L + -1L = 0", true, a.isZero());
    a.usub(7L);
    a.sub(-8L);
    assertEquals("-7L - -8L != 0", false, a.isZero());
    a.sub(1L);
    assertEquals("1 - 1L = 0", true, a.isZero());
  }

  @Test
  public void testDivAndRem() {
    // Check divRem
    {
      final long a = 104608886616216589L;
      final long b = 104608886616125069L;
      BigInt p = new BigInt(a), q = new BigInt(b);
      BigInteger m = BigInteger.valueOf(a), n = BigInteger.valueOf(b);
      assertEquals("divRem", m.remainder(n).toString(), p.divRem(q).toString());
      assertEquals("divRem", m.divide(n).toString(), p.toString());
      assertEquals("divRem", String.valueOf(b), q.toString());
    }
    // Check div
    {
      BigInt p = new BigInt(104608886616216589L), q = new BigInt(104608886616125069L);
      p.div(q);
      assertEquals("div", "1", p.toString());
      assertEquals("div", "104608886616125069", q.toString());
    }
    // Check rem
    {
      BigInt p = new BigInt(104608886616216589L), q = new BigInt(104608886616125069L);
      p.rem(q);
      assertEquals("rem", "91520", p.toString());
      assertEquals("rem", "104608886616125069", q.toString());
    }
    // Check udiv
    {
      BigInt p = new BigInt(104608886616216589L);
      long r = p.udiv(104608886616125069L);
      assertEquals("udiv", 91520L, r);
      assertEquals("udiv", "1", p.toString());
    }
    // Check when cumulative remainder overflows signed long.
    {
      String p = "3518084509561074142646556904312376320315226377906768127516", q = "4101587990";
      BigInteger a = new BigInteger(p), b = new BigInteger(q);
      BigInt aa = new BigInt(p), bb = new BigInt(q);
      BigInteger[] ans = a.divideAndRemainder(b);
      bb = aa.divRem(bb);
      assertEquals("udiv handles negative long", ans[0].toString(), aa.toString());
      assertEquals("udiv handles negative long", ans[1].toString(), bb.toString());
    }
  }

  @Test
  public void testBitShifts() {
    BigInt a = new BigInt("45982486592486793248673294867398579368598675986739851099");
    BigInt b = new BigInt("45982486592486793248673294867398579368598675986739851099");
    a.shiftLeft(3673);
    a.shiftRight(3673);
    assertEquals("Left+Right shift", b.toString(), a.toString());
  }

  @Test
  public void testSetClearFlipTestBit() {
    BigInt a = new BigInt(1);
    a.shiftLeft(1337);
    BigInt b = new BigInt(0);
    b.setBit(1337);
    assertEquals("Set bit", a.toString(), b.toString());
    assertEquals("Test bit", true, a.testBit(1337));
    assertEquals("Test bit", false, a.testBit(1336));
    b.clearBit(1337);
    assertEquals("Clear bit", true, b.isZero());
    assertEquals("Test bit", false, b.testBit(1337));
    b.flipBit(1337);
    assertEquals("Flip bit", a.toString(), b.toString());
    b.flipBit(1337);
    assertEquals("Flip bit", true, b.isZero());
    b = new BigInt("24973592847598349867938576938752986459872649249832748");
    BigInteger facit = new BigInteger("24973592847598349867938576938752986459872649249832748");
    b.flipBit(77);
    facit = facit.flipBit(77);
    assertEquals("Flip bit", facit.toString(), b.toString());
    b.flipBit(0);
    facit = facit.flipBit(0);
    assertEquals("Flip bit", facit.toString(), b.toString());
    b.flipBit(31);
    facit = facit.flipBit(31);
    assertEquals("Flip bit", facit.toString(), b.toString());
    b.flipBit(32);
    facit = facit.flipBit(32);
    assertEquals("Flip bit", facit.toString(), b.toString());
    for (int i = 0; i < 2048; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(100));
      int bit = rnd.nextInt(600);
      a.assign(s);
      facit = new BigInteger(new String(s));
      assertEquals("Random test", facit.testBit(bit), a.testBit(bit));
      bit = rnd.nextInt(600);
      facit = facit.setBit(bit);
      a.setBit(bit);
      assertEquals("Random set", facit.toString(), a.toString());
      bit = rnd.nextInt(600);
      facit = facit.clearBit(bit);
      a.clearBit(bit);
      assertEquals("Random clear", facit.toString(), a.toString());
      bit = rnd.nextInt(600);
      facit = facit.flipBit(bit);
      a.flipBit(bit);
      assertEquals("Random flip", facit.toString(), a.toString());
    }

    BigInteger x = BigInteger.valueOf(-1);
    a.assign(-1);

    x = x.shiftLeft(31 + 512);
    a.shiftLeft(31 + 512);

    x = x.flipBit(31 + 512);
    a.flipBit(31 + 512);

    BigInteger y = BigInteger.valueOf(-1);
    b.assign(-1);

    y = y.shiftLeft(32 + 512);
    b.shiftLeft(32 + 512);

    assertEquals("Edge flip", b.toString(), a.toString());
  }

  private static void t(final BigInt a, final char[] s, final char[] t, final int sh1, final int sh2) {
    a.assign(s);
    a.shiftLeft(sh1);
    BigInteger facit = new BigInteger(new String(s)).shiftLeft(sh1);
    final BigInt b = new BigInt(t);
    b.shiftLeft(sh2);
    a.and(b);
    facit = facit.and(new BigInteger(new String(t)).shiftLeft(sh2));
    assertEquals(" Random and", facit.toString(), a.toString());
  }

  @Test
  public void testAnd() {
    BigInt a = new BigInt(1L << 47);
    a.and(new BigInt(0L));
    assertEquals("And with 0", true, a.isZero());
    for (int i = 0; i < 1024; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(64)), t = getRndNumber(1 + rnd.nextInt(64));
      final int sh1 = rnd.nextInt(4) * 32, sh2 = rnd.nextInt(4) * 32;
      final BigInt c = a.clone();
      while (true) {
        try {
          t(a, s, t, sh1, sh2);
          BigInteger x = BigInteger.valueOf(-11);
          a.assign(-11);

          BigInteger y = BigInteger.valueOf(-6);
          BigInt b = new BigInt(-6);

          x = x.and(y);
          a.and(b);
          assertEquals(x.toString(), a.toString());
          assertEquals("-11 & -6 == ", "-16", a.toString());

          x = BigInteger.valueOf(-11);
          a.assign(-11);

          x = x.shiftLeft(28);
          a.shiftLeft(28);
          assertEquals(x.toString(), a.toString());

          y = y.shiftLeft(28);
          b.shiftLeft(28);
          assertEquals(y.toString(), b.toString());

          x = x.and(y);
          a.and(b);
          assertEquals(x.toString(), a.toString());

          b.assign(-1);
          b.shiftLeft(32);
          assertEquals("-11<<28 & -6<<28 == -1<<32", b.toString(), a.toString());
          break;
        }
        catch (final Throwable e) {
          if (!TestAide.isInDebug())
            throw e;

          a = c;
        }
      }
    }
  }

  @Test
  public void testOr() {
    BigInt a = new BigInt(0);
    a.or(new BigInt(1L << 47));
    BigInt b = new BigInt(1);
    b.shiftLeft(47);
    assertEquals("Or with 0", b.toString(), a.toString());
    a.or(new BigInt(-1));
    assertEquals("Or with -1", "-1", a.toString());
    a.assign(-2);
    a.or(new BigInt(1));
    assertEquals("-2 or 1 = ", "-1", a.toString());
    a.assign(1L);
    a.or(new BigInt(-2));
    assertEquals("1 or -2 = ", "-1", a.toString());
    a.or(new BigInt(0));
    assertEquals("-1 or 0 = ", "-1", a.toString());
    for (int i = 0; i < 1024; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(64)), t = getRndNumber(1 + rnd.nextInt(64));
      final int sh1 = rnd.nextInt(4) * 32, sh2 = rnd.nextInt(4) * 32;
      a.assign(s);
      b.assign(t);
      a.shiftLeft(sh1);
      b.shiftLeft(sh2);
      BigInteger facit = new BigInteger(new String(s)).shiftLeft(sh1);
      a.or(b);
      facit = facit.or(new BigInteger(new String(t)).shiftLeft(sh2));
      assertEquals("Random or", facit.toString(), a.toString());
    }
    a = new BigInt(-1);
    a.shiftLeft(2048);
    b.assign(1);
    b.shiftLeft(2048);
    b.sub(1);
    a.or(b);
    assertEquals("-2^2048 or 2^2048-1 = ", "-1", a.toString());
  }

  @Test
  public void testXor() {
    BigInt a = new BigInt(0);
    a.xor(new BigInt(1L << 47));
    BigInt b = new BigInt(1);
    b.shiftLeft(47);
    assertEquals("Xor with 0", b.toString(), a.toString());
    a.xor(b);
    assertEquals("Double xor is zero", true, a.isZero());
    for (int i = 0; i < 1024; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(64)), t = getRndNumber(1 + rnd.nextInt(64));
      final int sh1 = rnd.nextInt(4) * 32, sh2 = rnd.nextInt(4) * 32;
      a.assign(s);
      b.assign(t);
      a.shiftLeft(sh1);
      b.shiftLeft(sh2);
      BigInteger facit = new BigInteger(new String(s)).shiftLeft(sh1);
      a.xor(b);
      facit = facit.xor(new BigInteger(new String(t)).shiftLeft(sh2));
      assertEquals("Random xor", facit.toString(), a.toString());
    }
  }

  @Test
  public void testAndNot() {
    BigInt a = new BigInt(1L << 47);
    a.andNot(new BigInt(0));
    BigInt b = new BigInt(1);
    b.shiftLeft(47);
    assertEquals("AndNot with 0", b.toString(), a.toString());
    a.andNot(b);
    assertEquals("Self andNot is zero", true, a.isZero());
    for (int i = 0; i < 1024; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(64)), t = getRndNumber(1 + rnd.nextInt(64));
      final int sh1 = rnd.nextInt(4) * 32, sh2 = rnd.nextInt(4) * 32;
      a.assign(s);
      a.shiftLeft(sh1);
      BigInteger facit = new BigInteger(new String(s)).shiftLeft(sh1);
      b.assign(t);
      b.shiftLeft(sh2);
      a.andNot(b);
      facit = facit.andNot(new BigInteger(new String(t)).shiftLeft(sh2));
      assertEquals("Random andNot", facit.toString(), a.toString());
    }
    a.assign(-11);
    b = new BigInt(5);
    a.andNot(b);
    assertEquals("-11 & ~5 == ", "-16", a.toString());
    a.assign(-11);
    a.shiftLeft(28);
    b.assign(~(-6 << 28));
    a.andNot(b);
    b.assign(-1);
    b.shiftLeft(32);
    assertEquals("-11<<28 & ~~(-6<<28) == -1<<32", b.toString(), a.toString());
  }

  @Test
  public void testNot() {
    BigInt a = new BigInt(0L);
    a.not();
    assertEquals("~0 = ", "-1", a.toString());
    a.not();
    assertEquals("~~0", true, a.isZero());
    for (int i = 0; i < 1024; i++) {
      final char[] s = getRndNumber(1 + rnd.nextInt(64));
      final int sh1 = rnd.nextInt(4) * 32;
      a.assign(s);
      a.shiftLeft(sh1);
      a.not();
      BigInteger facit = new BigInteger(new String(s)).shiftLeft(sh1).not();
      assertEquals("Random not", facit.toString(), a.toString());
    }
  }

  @Test
  public void testLongAdd() {
    BigInt a = new BigInt(0);
    a.add(-1L);
    assertEquals("Long add", "-1", a.toString());
    a.assign(1L << 40);
    a.assign(0);
    a.add(-1L);
    assertEquals("Long add", "-1", a.toString());
  }

  @Test
  public void testMod() {
    for (int i = 0; i < 1024; i++) {
      char[] s = getRndNumber(1 + rnd.nextInt(64)), t = getRndNumber(1 + rnd.nextInt(64));
      BigInt a = new BigInt(s), b = new BigInt(t);
      BigInteger aa = new BigInteger(new String(s)), bb = new BigInteger(new String(t));
      if (bb.compareTo(BigInteger.ZERO) <= 0) {
        bb = bb.negate().add(BigInteger.ONE);
        b.mul(-1);
        b.add(1);
      }

      a.mod(b);
      assertEquals("Random mod", aa.mod(bb).toString(), a.toString());
    }
  }
}