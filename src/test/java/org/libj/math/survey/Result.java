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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.libj.lang.Classes;

public class Result {
  final Class<?>[] auditClasses;
  private final Class<?>[] classTypes;
  private final Class<?>[] arrayTypes;

  private final Set<String> sourceClassNames = new HashSet<>();
  private final int[] counts;
  final String[] resultClassNames;

  public Result(final Class<?>[] auditClasses, final Class<?>[] allClasses) {
    this.auditClasses = auditClasses;
    this.classTypes = filterTypes(allClasses, false, 0, 0);
    this.arrayTypes = filterTypes(allClasses, true, 0, 0);
    this.resultClassNames = classesToNames(allClasses, 0, 0);
    this.counts = new int[allClasses.length];
  }

  private static String[] classesToNames(final Class<?>[] classes, final int index, final int depth) {
    if (index == classes.length)
      return new String[depth];

    final String name = classes[index] == null ? null : classes[index].getCanonicalName();
    if (name == null)
      return classesToNames(classes, index + 1, depth);

    final String[] names = classesToNames(classes, index + 1, depth + 1);
    names[depth] = name;
    return names;
  }

  private static Class<?>[] filterTypes(final Class<?>[] classes, final boolean arrayOrClass, final int index, final int depth) {
    if (index == classes.length)
      return new Class<?>[depth];

    if (classes[index] == null)
      System.out.println();
    if (arrayOrClass != classes[index].isArray())
      return filterTypes(classes, arrayOrClass, index + 1, depth);

    final Class<?>[] names = filterTypes(classes, arrayOrClass, index + 1, depth + 1);
    names[depth] = classes[index];
    return names;
  }

  public void alloc(final String className) {
    for (int i = 0, i$ = resultClassNames.length; i < i$; ++i) { // [A]
      if (className.equals(resultClassNames[i])) {
        ++counts[i];
        return;
      }
    }

    System.err.println("ERROR: Unable to alloc " + className);
  }

  public int[] getCounts() {
    return this.counts;
  }

  public void reset() {
    Arrays.fill(counts, 0);
  }

  public int getAllocations(final Class<?> cls) {
    final String className = cls.getCanonicalName();
    for (int i = 0, i$ = resultClassNames.length; i < i$; ++i) // [A]
      if (className.equals(resultClassNames[i]))
        return counts[i];

    return 0;
  }

  public boolean isMatch(final String className) {
    return sourceClassNames.contains(className);
  }

  public void append(final Set<Rule> rules) {
    rules.add(new ClassRule(classTypes));
    for (final Class<?> cls : auditClasses) { // [A]
      for (final Method method : Classes.getDeclaredMethodsDeep(cls)) { // [A]
        if (!method.isSynthetic()) {
          rules.add(new ArrayRule(method, arrayTypes));
          sourceClassNames.add(method.getDeclaringClass().getCanonicalName());
        }
      }

      for (final Constructor<?> constructor : cls.getDeclaredConstructors()) { // [A]
        rules.add(new ArrayRule(constructor, arrayTypes));
      }
    }
  }
}