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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

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

  public AuditReport(final Result[] results, final Class<?>[] trackedClasses) {
    if (instance != null)
      throw new IllegalStateException();

    this.results = results;
    this.trackedClasses = trackedClasses;
    instance = this;
  }

  public Class<?>[] getTrackedClasses() {
    return this.trackedClasses;
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

  public int minIterations() {
    return 1;
  }

  public void dump() {
    for (int i = 0; i < results.length; ++i) {
      System.err.println(results[i].auditClass.getName());
      for (int j = 0; j < results[i].resultClassNames.length; ++j) {
        System.err.println("  " + results[i].resultClassNames[j] + ": " + results[i].getCounts()[j]);
      }
    }
  }
}