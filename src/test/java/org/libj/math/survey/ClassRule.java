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

import java.util.Collections;

import org.libj.util.IdentityHashSet;

public class ClassRule implements Rule {
  private final IdentityHashSet<Class<?>> classes = new IdentityHashSet<>();

  public ClassRule(final Class<?>[] classes) {
    Collections.addAll(this.classes, classes);
  }

  @Override
  public int hashCode() {
    return classes.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ClassRule))
      return false;

    final ClassRule that = (ClassRule)obj;
    if (!classes.equals(that.classes))
      return false;

    return true;
  }

  private static void toString(final StringBuilder builder, final Class<?> cls, final String method) {
    builder.append("RULE ").append(cls.getName()).append('.').append(method).append('\n');
    builder.append("CLASS ").append(cls.getName()).append('\n');
    builder.append("METHOD ").append(method).append('\n');
    builder.append("AT EXIT\n");
    builder.append("IF true\n");
    builder.append("DO org.libj.math.survey.AuditReport.allocate(\"" + cls.getCanonicalName() + "\",\"" + cls.getCanonicalName() + "\")\n");
    builder.append("ENDRULE\n");
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (final Class<?> cls : classes) {
      toString(builder, cls, "<init>");
      toString(builder, cls, "clone");
    }

    // System.out.println(builder);
    return builder.toString();
  }
}