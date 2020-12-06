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

package org.libj.math;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.libj.console.Tables;
import org.libj.lang.Strings.Align;
import org.libj.math.survey.AuditReport;
import org.libj.util.ArrayUtil;

public class CaseSummaryPrinter {
  private static final File dir = new File("src/test/html");
  private static final Pattern prePattern = Pattern.compile("pre>");
  private static final Pattern linePattern = Pattern.compile("║ +[║a-z]+");
  private static final Pattern cellMatcher = Pattern.compile("[║│]");
  private static final Pattern valuePattern = Pattern.compile(">\\+?([\\d∞]+)<");
  private static final Pattern headingPattern = Pattern.compile(">([^<]+)<");

  private static class SummaryMap extends LinkedHashMap<String,ArrayList<Object[]>> {
    private static final long serialVersionUID = 8003693767129511272L;

    @Override
    @SuppressWarnings("unlikely-arg-type")
    public ArrayList<Object[]> get(final Object key) {
      ArrayList<Object[]> value = super.get(key);
      if (value == null)
        put((String)key, value = new ArrayList<>());

      return value;
    }

    public final HashMap<String,String> methodNameToFileName = new HashMap<>();
  };

  private static double getValue(final String cell) {
    final Matcher valueMatcher = valuePattern.matcher(cell);
    if (!valueMatcher.find())
      return Double.NaN;

    final String group = valueMatcher.group(1).trim();
    return "∞".equals(group) ? Double.POSITIVE_INFINITY : Integer.valueOf(group);
  }

  private static void decompose(final File file, final SummaryMap map) throws IOException {
   final String str = new String(Files.readAllBytes(file.toPath()));
    final Matcher matcher = prePattern.matcher(str);
    matcher.find();
    int start = matcher.end();
    matcher.find();
    int end = matcher.end();
    final String summary = str.substring(start, end);
    final Matcher lineMatcher = linePattern.matcher(summary);
//    System.out.println("---- " + summary.toString() + " ------------------------------------------------------------");
    OUT:
    while (lineMatcher.find()) {
      final Object[] values = new Object[3];
      final String line = summary.substring(lineMatcher.start(), summary.indexOf('\n', lineMatcher.start() + 1)).trim();
//      System.out.println(line);
      final Matcher dataMatcher = cellMatcher.matcher(line);
      dataMatcher.find();
      start = dataMatcher.end();
      dataMatcher.find();
      end = dataMatcher.start();
      final String methodName = line.substring(start, end).trim();
      if (methodName == null)
        continue;

      for (int i = 0; i < 3; ++i) {
        if (!dataMatcher.find())
          continue OUT;

        String cell = line.substring(end + 1, end = dataMatcher.start()).trim();
        double val = getValue(cell);
        if (Double.isNaN(val)) {
          Matcher headingMatcher = headingPattern.matcher(cell);
          if (!headingMatcher.find()) {
            dataMatcher.find();
            cell += " | " + line.substring(end + 1, end = dataMatcher.start()).trim();
            headingMatcher = headingPattern.matcher(cell);
            headingMatcher.find();
          }

          values[i] = headingMatcher.group(1).trim();
        }
        else {
          if (line.charAt(dataMatcher.start()) == '│') {
            dataMatcher.find();
            final double val2 = getValue(line.substring(end + 1, end = dataMatcher.start()).trim());
            if (Double.isFinite(val2))
              val = Double.isInfinite(val) ? val2 : (val + val2) / 2;
          }

          values[i] = val;
        }
      }

      map.get(methodName).add(values);
      map.methodNameToFileName.put(methodName, file.getName());
//      System.out.println(methodName + " -> " + Arrays.toString(values));
//      System.out.println(methodName + " " + Arrays.toString(values));
    }
  }

  private static String toString(final Object obj) {
    if (obj instanceof Double) {
      final double val = ((Double)obj).doubleValue();
      return Double.isInfinite(val) ? "∞" : val == 0 ? "0" : "+" + (int)val + "%";
    }

    return String.valueOf(obj);
  }

  private static String[][] getColumns(final SummaryMap map, final int index, final boolean withKey) {
    final String[][] strings = new String[withKey ? 4 : 3][map.size()];
    final Iterator<Map.Entry<String,ArrayList<Object[]>>> iterator = map.entrySet().iterator();
    for (int j = 0; iterator.hasNext(); ++j) {
      final Map.Entry<String,ArrayList<Object[]>> entry = iterator.next();
      final ArrayList<Object[]> list = entry.getValue();
      if (list.size() <= index)
        continue;

      int i = -1;
      if (withKey)
        strings[++i][j] = entry.getKey();

      final Object[] array = list.get(index);
      strings[++i][j] = String.valueOf(toString(array[0]));
      strings[++i][j] = String.valueOf(toString(array[1]));
      strings[++i][j] = String.valueOf(toString(array[2]));
    }

    return strings;
  }

  @Test
  public void printSummary() throws IOException {
    final SummaryMap bigIntMap = new SummaryMap();
    final SummaryMap decimalMap = new SummaryMap();
    final File[] files = dir.listFiles();
    Arrays.sort(files);
    for (int i = 0; i < files.length; ++i)
      decompose(files[i], files[i].toString().contains("BigInt") ? bigIntMap : decimalMap);

    printMap(bigIntMap);
    printMap(decimalMap);
  }

  private static void printMap(final SummaryMap map) {
    final String[][] cols1 = getColumns(map, 0, true);
    final String[][] cols2 = getColumns(map, 1, false);

    final String[][] tables = new String[2][];
    tables[0] = Tables.printTable(true, Align.CENTER, Align.RIGHT, cols1).split("\n");
    tables[0] = ArrayUtil.concat(new String[] {"Runtime Performance"}, tables[0]);
    if (cols2 != null) {
      tables[1] = Tables.printTable(true, Align.CENTER, Align.RIGHT, cols2).split("\n");
      tables[1] = ArrayUtil.concat(new String[] {"Heap Allocation"}, tables[1]);
    }

    final String str = Tables.printTable(tables);
    final String[] lines = str.split("\n");
    for (int i = 0; i < lines.length; ++i) {
      lines[i] = "<code>" + lines[i].replace(" ", "&nbsp;") + "</code>";
      if (i >= 4 && i - 3 < cols1[0].length) {
        final String methodName = cols1[0][i - 3];
        final String fileName = map.methodNameToFileName.get(methodName);
        lines[i] = "<a href=\"https://htmlpreview.github.io/?https://github.com/libj/math/blob/master/src/test/html/" + fileName + "#" + AuditReport.getAnchor(methodName) + "\">" + lines[i] + "</a><br>";
      }

      System.out.println(lines[i]);
    }
  }
}