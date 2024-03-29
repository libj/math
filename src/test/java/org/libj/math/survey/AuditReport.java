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

package org.libj.math.survey;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.libj.console.Ansi;
import org.libj.console.Tables;
import org.libj.lang.Strings.Align;
import org.openjax.xml.api.CharacterDatas;

public class AuditReport {
  public static boolean isInTest;
  private static AuditReport instance;

  public static void allocate(final String from, final String className) {
    if (isInTest)
      instance.alloc(from, className);
  }

  private static File toFile(final Set<Rule> rules) throws IOException {
    final StringBuilder b = new StringBuilder();
    if (rules.size() > 0) {
      for (final Rule rule : rules) { // [S]
        if (b.length() > 0)
          b.append("\n\n");

        b.append(rule);
      }
    }

    final Path path = Files.createTempFile("rules", "btm");
    Files.write(path, b.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return path.toFile();
  }

  private final Class<?> testClass;
  private final Result[] results;
  private final Class<?>[] trackedClasses;

  public static AuditReport init(final Class<?> testClass, final Result[] results, final Class<?>[] trackedClasses) {
    if (instance != null)
      throw new IllegalStateException();

    return instance = new AuditReport(testClass, results, trackedClasses);
  }

  public static void destroy() {
    instance = null;
  }

  private AuditReport(final Class<?> testClass, final Result[] results, final Class<?>[] trackedClasses) {
    this.testClass = testClass;
    this.results = results;
    this.trackedClasses = trackedClasses;
  }

  public Class<?>[] getTrackedClasses() {
    return mode == 0 ? null : this.trackedClasses;
  }

  public File getRulesFile() throws IOException {
    final Set<Rule> rules = new HashSet<>();
    for (final Result result : results) // [A]
      result.append(rules);

    final File rulesFile = toFile(rules);
    rulesFile.deleteOnExit();
    return rulesFile;
  }

  public void alloc(final String from, final String className) {
    for (int i = 0, i$ = results.length; i < i$; ++i) { // [A]
      final Result result = results[i];
      if (result.isMatch(from)) {
        result.alloc(className);
        return;
      }
    }

    System.err.println("ERROR: Unable to alloc " + from + " -> " + className);
  }

  public void reset() {
    if (results != null)
      for (int i = 0, i$ = results.length; i < i$; ++i) // [A]
        results[i].reset();
  }

  public int getAllocations(final Class<?> auditClass) {
    int count = 0;
    if (results != null)
      for (int i = 0, i$ = results.length; i < i$; ++i) // [A]
        count += results[i].getAllocations(auditClass);

    return count;
  }

  public int minIterations(final int defaultValue) {
    return mode == 1 ? 1 : defaultValue;
  }

  public void dump() {
    if (results != null) {
      for (int i = 0, i$ = results.length; i < i$; ++i) { // [A]
        final Result result = results[i];
        System.err.println(Arrays.toString(result.auditClasses));
        for (int j = 0, j$ = result.resultClassNames.length; j < j$; ++j) { // [A]
          System.err.println("  " + result.resultClassNames[j] + ": " + result.getCounts()[j]);
        }
      }
    }
  }

  private Method method;

  public void setMethod(final Method method) {
    this.method = method;
  }

  private final LinkedHashMap<List<Object>,Map<Integer,String[]>> methodLabelToResults = new LinkedHashMap<>();
  private final HashMap<Integer,LinkedHashMap<List<String>,List<Object>[]>> headersToSummaries = new HashMap<>();
  private final HashMap<Method,LinkedHashMap<Integer,String>> methodToModeToComment = new HashMap<>();

  private LinkedHashMap<List<String>,List<Object>[]> getHeadersToSummaries() {
    LinkedHashMap<List<String>,List<Object>[]> summaries = headersToSummaries.get(mode);
    if (summaries == null)
      headersToSummaries.put(mode, summaries = new LinkedHashMap<>());

    return summaries;
  }

  @SuppressWarnings("unchecked")
  public void submit(final String label, final String result, final String[] summary, final int categories, final List<String> headers) {
    final int headerSize = headers.size();
    headers.add(String.valueOf(categories));
    List<Object>[] columns = getHeadersToSummaries().get(headers);
    if (columns == null) {
      getHeadersToSummaries().put(headers, columns = new List[categories * headerSize + 1]);
      columns[0] = new ArrayList<>();
      columns[0].add("");
    }

    columns[0].add(label);
    for (int c = 0; c < headerSize; ++c) { // [A]
      if (columns[c + 1] == null) {
        columns[c + 1] = new ArrayList<>();
        columns[c + 1].add(headers.get(c));
      }

      for (int i = 0; i < categories; ++i) // [A]
        columns[c + 1].add(String.valueOf(summary[c * categories + i]));
    }

    final List<Object> methodLabel = Arrays.asList(method, label);
    Map<Integer,String[]> map = methodLabelToResults.get(methodLabel);
    if (map == null)
      methodLabelToResults.put(methodLabel, map = new HashMap<>());

    if (map.get(mode) != null)
      throw new IllegalStateException();

    map.put(mode, new String[] {label, result});
  }

  public static String getAnchor(final String methodName) {
    return methodName.replaceAll("[(),:\\[\\]]", "").replace(' ', '-').toLowerCase();
  }

  public void print(final PrintStream out, final Map<String,String> links) {
    if (out != null) {
      final String testClassSimpleName = testClass.getSimpleName();
      final String testClassPath = "src/test/java/" + testClass.getName().replace('.', '/') + ".java";
      final int start = Character.isLowerCase(testClassSimpleName.charAt(6)) ? 7 : 6;
      final int end = testClassSimpleName.length() - 4;
      final String label = start < end ? testClassSimpleName.substring(start, end) : testClassSimpleName;
      out.println("<h4>" + label + " (<a href=\"" + testClassPath + "\"><code>" + testClassSimpleName + "</code></a>)</h4>\n");
      out.println("<p><strong>Summary</strong></p>\n");
      out.println("<pre><code>");
    }

    final StringBuilder b = new StringBuilder();
    // First print the summaries
    final Map<Integer,LinkedHashMap<List<String>,List<Object>[]>> headersToSummaries = this.headersToSummaries;
    if (headersToSummaries.size() > 0) {
      for (final Map.Entry<Integer,LinkedHashMap<List<String>,List<Object>[]>> entry : headersToSummaries.entrySet()) { // [S]
        final int mode = entry.getKey();
        final LinkedHashMap<List<String>,List<Object>[]> headersToSummaryValues = entry.getValue();
        if (headersToSummaryValues.size() > 0) {
          for (final Map.Entry<List<String>,List<Object>[]> entry2 : headersToSummaryValues.entrySet()) { // [S]
            final Object[][] columns = new Object[entry2.getKey().size()][]; // Assumed the "categories" were added (hack)
            for (int c = 0, c$ = columns.length; c < c$; ++c) { // [A]
              columns[c] = entry2.getValue()[c].toArray();
            }

            b.append(getTitle(mode)).append('\n');
            b.append(Tables.printTable(true, Align.CENTER, Align.RIGHT, Integer.valueOf(entry2.getKey().get(entry2.getKey().size() - 1)), true, columns)).append('\n');
          }
        }
      }
    }

    if (b.length() > 1)
      b.setLength(b.length() - 1);

    System.out.println(b.toString());
    if (out != null) {
      out.println(Ansi.toHtml(CharacterDatas.escapeForElem(b.toString())));
      out.println("</code></pre>\n");
    }

    // Next print the detailed results
    if (methodLabelToResults.size() > 0) {
      for (final Map.Entry<List<Object>,Map<Integer,String[]>> resultsEntry : methodLabelToResults.entrySet()) { // [S]
        final List<Object> methodLabel = resultsEntry.getKey();
        final Map<Integer,String[]> results = resultsEntry.getValue();
        if (results.size() > 0) {
          final Iterator<Map.Entry<Integer,String[]>> iterator = results.entrySet().iterator();
          for (int i = 0; iterator.hasNext(); ++i) { // [I]
            final Map.Entry<Integer,String[]> resultEntry = iterator.next();
            final Integer resultKey = resultEntry.getKey();
            String[] resultValue = resultEntry.getValue();
            if (out != null) {
              if (i == 0) {
                final String methodName = resultValue[0];
                out.println("<h5><a name=\"" + getAnchor(methodName) + "\"><code>" + CharacterDatas.escapeForElem(methodName) + "</code></a></h5>\n");

                final Method method = (Method)methodLabel.get(0);
                final Map<Integer,String> modeToComment = methodToModeToComment.get(method);
                if (modeToComment != null) {
                  final String comment = modeToComment.get(resultKey);
                  if (comment != null)
                    out.println("<p>" + mdToHtml(comment, links).replace("\n", "<br>") + "</p>\n");
                }

                out.println("<pre><code>");
              }

              out.println(Ansi.toHtml(CharacterDatas.escapeForElem(getTitle(resultKey))));
              out.println(Ansi.toHtml(CharacterDatas.escapeForElem(resultValue[1])));
            }

            System.out.println(getTitle(resultKey));
            System.out.println(resultValue[1]);
          }
        }

        if (out != null)
          out.println("</code></pre>\n");
      }
    }
  }

  public String getTitle(final int mode) {
    return mode == 0 ? "[runtime performance]" : "[heap allocation]";
  }

  private int mode;

  public void setMode(final int mode) {
    this.mode = mode;
  }

  public int getMode() {
    return mode;
  }

  public void addComment(final int mode, final String comment) {
    if (this.mode != mode)
      return;

    LinkedHashMap<Integer,String> modeToComment = methodToModeToComment.get(method);
    if (modeToComment == null)
      methodToModeToComment.put(method, modeToComment = new LinkedHashMap<>());

    final String existing = modeToComment.get(mode);
    modeToComment.put(mode, existing != null ? existing + "\n\n" + comment : comment);
  }

  static String mdToHtml(final String str, final Map<String,String> links) {
    char ch0, ch1 = Character.MAX_VALUE;
    final StringBuilder b = new StringBuilder();
    boolean code = false;
    int a = -1;
    for (int i = 0, i$ = str.length(); i < i$; ++i, ch1 = ch0) { // [N]
      ch0 = str.charAt(i);
      if (ch0 == '`') {
        b.append(code ? "</code>" : "<code>");
        code = !code;
      }
      else if (ch0 == '[') {
        if (ch1 == ']') {
          final int end = str.indexOf(']', i + 1);
          final String link = str.substring(i + 1, end);
          final String href = links.get(link);
          b.insert(a, href);
          a = -1;
          i = end;
        }
        else {
          b.append("<a href=\"");
          a = b.length();
          b.append("\">");
        }
      }
      else if (ch0 == ']') {
        b.append("</a>");
      }
      else {
        b.append(ch0);
      }
    }

    return b.toString();
  }
}