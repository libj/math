package org.libj.math.survey;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.libj.lang.Classes;

public class Result {
  final Class<?> auditClass;
  private final Class<?>[] trackedClasses;

  private String[] sourceClassNames;
  private final int[] counts;
  final String[] resultClassNames;

  public Result(final Class<?> auditClass, final Class<?>[] trackedClasses) {
    this.auditClass = auditClass;
    this.trackedClasses = trackedClasses;
    this.resultClassNames = classesToNames(trackedClasses, 0, 0);
    this.counts = new int[trackedClasses.length];
  }

  private static String[] classesToNames(final Class<?>[] classes, final int index, final int depth) {
    if (index == classes.length)
      return new String[depth];

    final String name = classes[index] == null ? null : Classes.getProperName(classes[index]);
    if (name == null)
      return classesToNames(classes, index + 1, depth);

    final String[] names = classesToNames(classes, index + 1, depth + 1);
    names[depth] = name;
    return names;
  }

  public void alloc(final String className) {
    for (int i = 0; i < resultClassNames.length; ++i) {
      if (className.equals(resultClassNames[i])) {
        ++counts[i];
        return;
      }
    }

    throw new IllegalArgumentException(className);
  }

  public int[] getCounts() {
    return this.counts;
  }

  public void reset() {
    Arrays.fill(counts, 0);
  }

  public int getAllocations(final Class<?> cls) {
    final String className = Classes.getProperName(cls);
    for (int i = 0; i < resultClassNames.length; ++i)
      if (className.equals(resultClassNames[i]))
        return counts[i];

    return 0;
  }

  public boolean isMatch(final String className) {
    for (int i = 0; i < sourceClassNames.length; ++i)
      if (className.equals(sourceClassNames[i]))
        return true;

    return false;
  }

  public void append(final Set<Rule> rules) {
    if (sourceClassNames != null)
      throw new IllegalStateException();

    sourceClassNames = appendMethods(rules, Classes.getDeclaredMethodsDeep(auditClass), 0, 0);
    for (final Constructor<?> constructor : auditClass.getDeclaredConstructors())
      rules.add(new Rule(constructor, trackedClasses));
  }

  private String[] appendMethods(final Set<Rule> rules, final Method[] methods, final int index, final int depth) {
    if (index == methods.length)
      return new String[depth];

    final Method method = methods[index];
    if (method.isSynthetic())
      return appendMethods(rules, methods, index + 1, depth);

    rules.add(new Rule(method, trackedClasses));
    final String[] result = appendMethods(rules, methods, index + 1, depth + 1);
    result[depth] = method.getDeclaringClass().getName();
    return result;
  }
}