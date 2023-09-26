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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.jboss.byteman.agent.Main;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.libj.lang.Classes;
import org.libj.lang.PackageLoader;
import org.libj.lang.Systems;
import org.libj.util.IdentityHashSet;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassInjector;

public class AuditRunner extends BlockJUnit4ClassRunner {
  private static final String classpath = System.getProperty("java.class.path");
  private static final File resultDir = new File("src/test/html");
  private static final boolean skipRTests = Systems.hasProperty("skipRTests");
  private static final Instrumentation instr;
  private static final Map<String,String> links = new HashMap<>();

  static {
    resultDir.mkdirs();

    if (skipRTests) {
      instr = null;
    }
    else {
      try {
        instr = ByteBuddyAgent.install();
        loadJarsInBootstrap(instr, "lang", "slf4j", "logback-core", "logback", "console", "api", "util");
        loadClassesInBootstrap("org.libj.math.survey.Rule", "org.libj.math.survey.ArrayRule", "org.libj.math.survey.ClassRule", "org.libj.math.survey.Result", "org.libj.math.survey.AuditReport");
      }
      catch (final IOException e) {
        throw new ExceptionInInitializerError(e);
      }
    }

    try {
      PackageLoader.getSystemPackageLoader().loadPackage("org.libj.math", c -> {
        links.put(c.getSimpleName(), "src/test/java/" + c.getName().replace('.', '/') + ".java");
        return false;
      });
    }
    catch (final Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Execution {
    AuditMode value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Repeatable(Instruments.class)
  public @interface Instrument {
    Class<?>[] a();

    Class<?>[] b();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Instruments {
    Instrument[] value();
  }

  /**
   * Recursively process each sub-path of the specified directory.
   *
   * @param dir The directory to process.
   * @param predicate The predicate defining the test process.
   * @return {@code true} if the specified predicate returned {@code true} for each sub-path to which it was applied, otherwise
   *         {@code false}.
   */
  public static boolean recurseDir(final File dir, final Predicate<File> predicate) {
    final File[] files = dir.listFiles();
    if (files != null)
      for (final File file : files) // [A]
        if (!recurseDir(file, predicate))
          return false;

    return predicate.test(dir);
  }

  static JarFile createTempJarFile(final File dir) throws IOException {
    final Path dirPath = dir.toPath();
    final Path zipPath = Files.createTempFile("auditrunner", ".jar");
    try (final JarOutputStream jos = new JarOutputStream(new FileOutputStream(zipPath.toFile()))) {
      recurseDir(dir, new Predicate<File>() {
        @Override
        public boolean test(final File t) {
          if (t.isFile()) {
            final Path filePath = t.toPath();
            final String name = dirPath.relativize(filePath).toString();
            try {
              jos.putNextEntry(new ZipEntry(name));
              jos.write(Files.readAllBytes(filePath));
              jos.closeEntry();
            }
            catch (final IOException e) {
              throw new UncheckedIOException(e);
            }
          }

          return true;
        }
      });
    }

    final File file = zipPath.toFile();
    file.deleteOnExit();
    return new JarFile(file);
  }

  private static JarFile createJarFileOfSource(final File file) throws IOException {
    final String path = file.getAbsolutePath();
    if (file.isDirectory()) {
      if ("classes".equals(file.getName()))
        return createTempJarFile(file);

      if ("test-classes".equals(file.getName()))
        return createTempJarFile(new File(file.getParent(), "classes"));
    }
    else {
      if (path.endsWith(".jar"))
        return new JarFile(file);

      if (path.endsWith("-tests.jar"))
        return new JarFile(new File(path.substring(0, path.length() - 10) + ".jar"));
    }

    throw new UnsupportedOperationException("Unsupported source path: " + path);
  }

  private static void loadJarsInBootstrap(final Instrumentation instr, final String ... names) throws IOException {
    for (final String name : names) { // [A]
      int start = classpath.indexOf(File.separator + name);
      start = classpath.lastIndexOf(File.pathSeparatorChar, start) + 1;
      int end = classpath.indexOf(File.pathSeparatorChar, start);
      if (end == -1)
        end = classpath.length();

      final String jarPath = classpath.substring(start, end);
      final JarFile jarFile = createJarFileOfSource(new File(jarPath));
      instr.appendToBootstrapClassLoaderSearch(jarFile);
    }
  }

  private static void loadClassesInBootstrap(final String ... classNames) throws IOException {
    final Map<String,byte[]> nameToBytes = new HashMap<>();
    for (final String className : classNames) { // [A]
      try (final InputStream in = AuditRunner.class.getClassLoader().getResource(className.replace('.', '/').concat(".class")).openStream()) {
        final byte[] bytes = new byte[in.available()];
        in.read(bytes);
        nameToBytes.put(className, bytes);
        ClassInjector.UsingUnsafe.ofBootLoader().injectRaw(nameToBytes);
        nameToBytes.clear();
      }
    }
  }

  private static void loadByteman(final Instrumentation instr, final AuditReport report) {
    try {
      loadJarsInBootstrap(instr, "byteman");
      final File rulesFile = report.getRulesFile();
      Main.premain("script:" + rulesFile.getAbsolutePath(), instr);
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private final AuditMode mode;
  private final AuditReport report;

  public AuditRunner(final Class<?> cls) throws Exception {
    super(cls);
    // System.err.println(Class.forName("org.libj.math.survey.Result").getClassLoader());
    // System.err.println(Class.forName("org.libj.math.survey.AuditReport").getClassLoader());
    // System.err.println(Class.forName("org.libj.math.survey.Rule").getClassLoader());
    final Instruments instrs = cls.getAnnotation(Instruments.class);
    final Execution execution = cls.getAnnotation(Execution.class);
    if (skipRTests || instrs == null || execution != null && execution.value() == AuditMode.UNINSTRUMENTED) {
      this.mode = AuditMode.UNINSTRUMENTED;
      this.report = AuditReport.init(cls, null, null);
    }
    else {
      this.mode = execution == null ? AuditMode.INSTRUMENTED : execution.value();
      final Instrument[] instruments = instrs.value();
      final Result[] results = new Result[instruments.length + 1];
      final Set<Class<?>> allClasses = new IdentityHashSet<>();
      for (int i = 0, i$ = instruments.length; i < i$; ++i) { // [A]
        final Instrument instrument = instruments[i];
        Collections.addAll(allClasses, instrument.a());
        Collections.addAll(allClasses, instrument.b());
      }

      final Class<?>[] allTrackedClasses = allClasses.toArray(new Class[allClasses.size()]);
      for (int i = 0, i$ = instruments.length; i < i$; ++i) { // [A]
        results[i] = new Result(instruments[i].a(), allTrackedClasses);
      }

      results[results.length - 1] = new Result(new Class[] {cls}, allTrackedClasses);
      this.report = AuditReport.init(cls, results, allTrackedClasses);
    }
  }

  @Override
  protected void validateTestMethods(final List<Throwable> errors) {
  }

  private void beforeInvoke() {
    if (report != null)
      report.reset();
  }

  @Override
  public void run(final RunNotifier notifier) {
    final RunNotifier delegateNotifier = new RunNotifier() {
      @Override
      public void addListener(final RunListener listener) {
        notifier.addListener(listener);
      }

      @Override
      public void removeListener(final RunListener listener) {
        notifier.removeListener(listener);
      }

      @Override
      public void fireTestRunStarted(final Description description) {
        if (instr == null || report.getMode() == 0)
          notifier.fireTestRunStarted(description);
      }

      @Override
      public void fireTestRunFinished(final org.junit.runner.Result result) {
        if (instr == null || report.getMode() == 1)
          notifier.fireTestRunFinished(result);
      }

      @Override
      public void fireTestSuiteStarted(final Description description) {
        if (instr == null || report.getMode() == 0)
          notifier.fireTestSuiteStarted(description);
      }

      @Override
      public void fireTestSuiteFinished(final Description description) {
        if (instr == null || report.getMode() == 1)
          notifier.fireTestSuiteFinished(description);
      }

      @Override
      public void fireTestStarted(final Description description) throws StoppedByUserException {
        System.out.println(description.toString());
        if (instr == null || report.getMode() == 0)
          notifier.fireTestStarted(description);
      }

      @Override
      public void fireTestFailure(final Failure failure) {
        notifier.fireTestFailure(failure);
      }

      @Override
      public void fireTestAssumptionFailed(final Failure failure) {
        notifier.fireTestAssumptionFailed(failure);
      }

      @Override
      public void fireTestIgnored(final Description description) {
        if (instr == null || report.getMode() == 0)
          notifier.fireTestIgnored(description);
      }

      @Override
      public void fireTestFinished(final Description description) {
        if (instr == null || report.getMode() == 1)
          notifier.fireTestFinished(description);
      }

      @Override
      public void pleaseStop() {
        notifier.pleaseStop();
      }

      @Override
      public void addFirstListener(final RunListener listener) {
        notifier.addFirstListener(listener);
      }
    };

    if (mode == AuditMode.PHASED || mode == AuditMode.UNINSTRUMENTED) {
      report.setMode(AuditMode.UNINSTRUMENTED.ordinal()); // 0
      super.run(delegateNotifier);
    }

    if (mode == AuditMode.PHASED || mode == AuditMode.INSTRUMENTED) {
      loadByteman(instr, report);
      report.setMode(AuditMode.INSTRUMENTED.ordinal()); // 1
      super.run(delegateNotifier);
    }

    if (report != null) {
      try {
        final String testClassSimpleName = getTestClass().getJavaClass().getSimpleName();
        final File file = new File(resultDir, testClassSimpleName + ".html");
        final OutputStream out = Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        final PrintStream ps = new PrintStream(out);
        ps.println("<!DOCTYPE html>\n<html>\n<head>\n  <title>" + testClassSimpleName + "</title>");
        ps.println("  <link rel=\"stylesheet\" type=\"test/css\" href=\"https://raw.githubusercontent.com/sindresorhus/github-markdown-css/gh-pages/github-markdown.css\">");
        ps.println("  <style>");
        ps.println("    html { font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji; }");
        ps.println("    span, code, pre { font-family: Menlo, monospace; font-size: small; background-color: rgba(27,31,35,.05); mix-blend-mode: hard-light; }");
        ps.println("    code { padding: .2em .4em; }");
        ps.println("    pre { color: #EEE; background-color: #222; padding: 4px; }");
        ps.println("  </style>");
        ps.println("</head>\n<body>");
        report.print(ps, links);
        ps.print("</body>\n</html>");
        AuditReport.destroy();
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  @Override
  @Deprecated
  protected TestClass createTestClass(final Class<?> testClass) {
    return new TestClass(testClass) {
      @Override
      protected void scanAnnotatedMembers(final Map<Class<? extends Annotation>,List<FrameworkMethod>> methodsForAnnotations, final Map<Class<? extends Annotation>,List<FrameworkField>> fieldsForAnnotations) {
        super.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        methodsForAnnotations.clear();

        final Method[] methods = Classes.getDeclaredMethodsDeep(getJavaClass());
        try {
          Classes.sortDeclarativeOrder(methods, true);
        }
        catch (final ClassNotFoundException e) {
          throw new RuntimeException(e);
        }

        for (final Method method : methods) { // [A]
          addToAnnotationLists(new FrameworkMethod(method) {
            @Override
            public Object invokeExplosively(final Object target, final Object ... params) throws Throwable {
              return new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                  beforeInvoke();
                  if (method.getParameterCount() == 0)
                    return method.invoke(target);

                  report.setMethod(method);
                  return method.invoke(target, report);
                }
              }.run();
            }
          }, methodsForAnnotations);
        }
      }
    };
  }
}