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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.libj.console.Tables;
import org.libj.lang.Strings.Align;

public class AuditReport {
  private static AuditReport instance;

  public static void allocate(final String from, final String className) {
    instance.alloc(from, className);
  }

  private static File toFile(final Set<Rule> rules) throws IOException {
    final StringBuilder builder = new StringBuilder();
    for (final Rule rule : rules) {
      if (builder.length() > 0)
        builder.append("\n\n");

      builder.append(rule);
    }

    final Path path = Files.createTempFile("rules", "btm");
    Files.write(path, builder.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return path.toFile();
  }

  private final Result[] results;
  private final Class<?>[] trackedClasses;

  public static AuditReport init(final Result[] results, final Class<?>[] trackedClasses) {
    if (instance != null)
      throw new IllegalStateException();

    return instance = new AuditReport(results, trackedClasses);
  }

  public static void destroy() {
    instance = null;
  }

  private AuditReport(final Result[] results, final Class<?>[] trackedClasses) {
    this.results = results;
    this.trackedClasses = trackedClasses;
  }

  public Class<?>[] getTrackedClasses() {
    return mode == 0 ? null : this.trackedClasses;
  }

  public File getRulesFile() throws IOException {
    final Set<Rule> rules = new HashSet<>();
    for (final Result result : results)
      result.append(rules);

    final File rulesFile = toFile(rules);
    rulesFile.deleteOnExit();
    return rulesFile;
  }

  public void alloc(final String from, final String className) {
    for (int i = 0; i < results.length; ++i) {
      if (results[i].isMatch(from)) {
        results[i].alloc(className);
        return;
      }
    }

    throw new IllegalArgumentException(from + " -> " + className);
  }

  public void reset() {
    for (int i = 0; i < results.length; ++i)
      results[i].reset();
  }

  public int getAllocations(final Class<?> auditClass) {
    int count = 0;
    for (int i = 0; i < results.length; ++i)
      count += results[i].getAllocations(auditClass);

    return count;
  }

  public int minIterations(final int defaultValue) {
    return mode == 1 ? 1 : defaultValue;
  }

  public void dump() {
    for (int i = 0; i < results.length; ++i) {
      System.err.println(results[i].auditClass.getName());
      for (int j = 0; j < results[i].resultClassNames.length; ++j) {
        System.err.println("  " + results[i].resultClassNames[j] + ": " + results[i].getCounts()[j]);
      }
    }
  }

  private Method method;

  public void setMethod(final Method method) {
    this.method = method;
  }

  private final LinkedHashMap<Method,Map<Integer,String>> methodToResults = new LinkedHashMap<>();
  private final Map<Integer,LinkedHashMap<List<String>,List<Object>[]>> headersToSummaries = new HashMap<>();
  private final Map<Integer,HashMap<List<String>,Integer>> headersToCategories = new HashMap<>();

  private LinkedHashMap<List<String>,List<Object>[]> getHeadersToSummaries() {
    LinkedHashMap<List<String>,List<Object>[]> summaries = headersToSummaries.get(mode);
    if (summaries == null)
      headersToSummaries.put(mode, summaries = new LinkedHashMap<>());

    return summaries;
  }

  private HashMap<List<String>,Integer> getHeadersToCategories() {
    HashMap<List<String>,Integer> categories = headersToCategories.get(mode);
    if (categories == null)
      headersToCategories.put(mode, categories = new LinkedHashMap<>());

    return categories;
  }

  @SuppressWarnings("unchecked")
  public void submit(final String label, final String result, final String[] summary, final int categories, final List<String> headers) {
    List<Object>[] columns = getHeadersToSummaries().get(headers);
    if (columns == null) {
      getHeadersToCategories().put(headers, categories);
      getHeadersToSummaries().put(headers, columns = new List[categories * headers.size() + 1]);
      columns[0] = new ArrayList<>();
      columns[0].add("");
    }

    columns[0].add(label);
    for (int c = 0; c < headers.size(); ++c) {
      if (columns[c + 1] == null) {
        columns[c + 1] = new ArrayList<>();
        columns[c + 1].add(headers.get(c));
      }

      for (int i = 0; i < categories; ++i)
        columns[c + 1].add(String.valueOf(summary[c * categories + i]));
    }

    Map<Integer,String> map = methodToResults.get(method);
    if (map == null)
      methodToResults.put(method, map = new HashMap<>());

    if (map.get(mode) != null)
      throw new IllegalStateException();

    map.put(mode, result);
  }

  public void print() {
    for (final Map.Entry<Integer,LinkedHashMap<List<String>,List<Object>[]>> entry : this.headersToSummaries.entrySet()) {
      final int mode = entry.getKey();
      final LinkedHashMap<List<String>,List<Object>[]> headersToSummaries = entry.getValue();
      final HashMap<List<String>,Integer> headersToCategories = this.headersToCategories.get(mode);
      for (final Map<Integer,String> result : methodToResults.values()) {
        System.out.println(getTitle(mode));
        System.out.println(result.get(mode));
      }

      for (final Map.Entry<List<String>,List<Object>[]> entry2 : headersToSummaries.entrySet()) {
        final Object[][] columns = new Object[entry2.getKey().size() + 1][];
        for (int c = 0; c < columns.length; ++c) {
          columns[c] = entry2.getValue()[c].toArray();
        }

        System.out.println(getTitle(mode));
        System.out.println(Tables.printTable(true, Align.RIGHT, headersToCategories.get(entry2.getKey()), true, columns));
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
}