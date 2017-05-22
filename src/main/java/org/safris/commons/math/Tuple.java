/* Copyright (c) 2012 lib4j
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

package org.safris.commons.math;

import java.util.Comparator;

public final class Tuple<X extends Number,Y extends Number> {
  public static final Comparator<Tuple<?,?>> comparatorX = new Comparator<Tuple<?,?>>() {
    @Override
    public int compare(final Tuple<?,?> o1, final Tuple<?,?> o2) {
      return Double.compare(o1.x.doubleValue(), o2.x.doubleValue());
    }
  };

  public static final Comparator<Tuple<?,?>> comparatorY = new Comparator<Tuple<?,?>>() {
    @Override
    public int compare(final Tuple<?,?> o1, final Tuple<?,?> o2) {
      return Double.compare(o1.y.doubleValue(), o2.y.doubleValue());
    }
  };

  public final X x;
  public final Y y;

  public Tuple(final X x, final Y y) {
    this.x = x;
    this.y = y;
  }
}