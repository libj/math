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

import java.lang.reflect.Executable;

public class ArrayRule implements Rule {
  private final String className;
  private final String methodSignature;
  private final Class<?>[] classes;
  private final boolean isClone;

  public ArrayRule(final Executable executable, final Class<?>[] classes) {
    this.className = executable.getDeclaringClass().getName();
    final StringBuilder builder = new StringBuilder();
    if (className.equals(executable.getName())) {
      builder.append("<init>");
      isClone = false;
    }
    else {
      builder.append(executable.getName());
      isClone = "clone".equals(executable.getName()) && executable.getParameterCount() == 0;
    }

    if (executable.getParameterCount() != 0) {
      builder.append('(');
      final Class<?>[] parameterTypes = executable.getParameterTypes();
      for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
        if (i > 0)
          builder.append(',');

        builder.append(parameterTypes[i].getCanonicalName());
      }

      builder.append(')');
    }

    this.methodSignature = builder.toString();
    this.classes = classes;
  }

  @Override
  public int hashCode() {
    return className.hashCode() ^ methodSignature.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ArrayRule))
      return false;

    final ArrayRule that = (ArrayRule)obj;
    if (!className.equals(that.className))
      return false;

    if (!methodSignature.equals(that.methodSignature))
      return false;

    return true;
  }

  private void toString(final StringBuilder builder, final Class<?> cls) {
    builder.append("RULE ").append(className).append('.').append(methodSignature).append(" [").append(cls.getName()).append("]\n");
    builder.append("CLASS ").append(className).append('\n');
    builder.append("METHOD ").append(methodSignature).append('\n');
    builder.append("AFTER NEW ").append(cls.isArray() ? cls.getComponentType().getSimpleName() + "[]" : cls.getSimpleName()).append(" ALL\n");
    builder.append("IF true\n");
//    builder.append("DO traceln(\"-----> \" + $CLASS + \" \" + $NEWCLASS);\n");
    builder.append("DO org.libj.math.survey.AuditReport.allocate($CLASS,$NEWCLASS)\n");
    builder.append("ENDRULE\n");
  }

  private void toStringClone(final StringBuilder builder) {
    builder.append("RULE ").append(className).append('.').append(methodSignature).append(" [CLONE]\n");
    builder.append("CLASS ").append(className).append('\n');
    builder.append("METHOD ").append(methodSignature).append('\n');
    builder.append("AT EXIT\n");
    builder.append("IF true\n");
    builder.append("DO org.libj.math.survey.AuditReport.allocate($1,$1)\n");
    builder.append("ENDRULE\n");
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (final Class<?> cls : classes) // [A]
      if (cls != null)
        toString(builder, cls);

    if (isClone)
      toStringClone(builder);

    return builder.toString();
  }
}