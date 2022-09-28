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

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;

import org.jaxsb.runtime.BindingList;
import org.jaxsb.runtime.Bindings;
import org.junit.Ignore;
import org.junit.Test;
import org.libj.net.HTTP;
import org.xml.sax.SAXException;

import com.wolframalpha.products.api.explorer.zAA;

public class WolframStudy {
  @Test
  @Ignore
  public void test() throws IOException, SAXException {
    System.out.println(eval("ln(2^(32*67108864))"));
  }

  @Test
  public void testParse1() throws IOException, SAXException {
    System.out.println(parse(ClassLoader.getSystemClassLoader().getResource("wolfram1.xml")));
  }

  @Test
  public void testParse2() throws IOException, SAXException {
    System.out.println(parse(ClassLoader.getSystemClassLoader().getResource("wolfram2.xml")));
  }

  private static zAA.Queryresult parse(final URL url) throws IOException, SAXException {
    return (zAA.Queryresult)Bindings.parse(url, "https://products.wolframalpha.com/api/explorer");
  }

  public static BigDecimal eval(final String expression) throws IOException, SAXException {
    final HashMap<String,String[]> params = new HashMap<>();
    params.put("input", new String[] {expression});
    params.put("format", new String[] {"plaintext"});
    params.put("output", new String[] {"XML"});
    params.put("appid", new String[] {System.getProperty("appid")});
    final zAA.Queryresult result = parse(HTTP.get("https://api.wolframalpha.com/v2/query", params));
    final BindingList<zAA.Pod> waPod = result.getWaPod();
    for (int i = 0, i$ = waPod.size(); i < i$; ++i) { // [RA]
      final zAA.Pod pod = waPod.get(i);
      if ("Result".equals(pod.getId$().text()))
        return new BigDecimal(pod.getWaSubpod().getWaPlaintext().text().replace("... × 10^", "E"));
    }

    return null;
  }
}