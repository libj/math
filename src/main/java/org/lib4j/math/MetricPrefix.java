/* Copyright (c) 2018 lib4j
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

package org.lib4j.math;

public final class MetricPrefix {
  private static final MetricPrefix[] prefixes = new MetricPrefix[49];

  public static final MetricPrefix YOCTO = new MetricPrefix(-24, "yocto", "y");
  public static final MetricPrefix ZEPRO = new MetricPrefix(-21, "zepto", "z");
  public static final MetricPrefix ATTO = new MetricPrefix(-18, "atto", "a");
  public static final MetricPrefix FEMTO = new MetricPrefix(-15, "femto", "f");
  public static final MetricPrefix PICO = new MetricPrefix(-12, "pico", "p");
  public static final MetricPrefix NANO = new MetricPrefix(-9, "nano", "n");
  public static final MetricPrefix MICRO = new MetricPrefix(-6, "micro", "μ");
  public static final MetricPrefix MILLI = new MetricPrefix(-3, "milli", "m");
  public static final MetricPrefix CENTI = new MetricPrefix(-2, "centi", "c");
  public static final MetricPrefix DECI = new MetricPrefix(-1, "deci", "d");
  public static final MetricPrefix DECA = new MetricPrefix(1, "deca", "da");
  public static final MetricPrefix HECTO = new MetricPrefix(2, "hecto", "h");
  public static final MetricPrefix KILO = new MetricPrefix(3, "kilo", "k");
  public static final MetricPrefix MEGA = new MetricPrefix(6, "mega", "m");
  public static final MetricPrefix GIGA = new MetricPrefix(9, "giga", "G");
  public static final MetricPrefix TERA = new MetricPrefix(12, "tera", "T");
  public static final MetricPrefix PETA = new MetricPrefix(15, "peta", "P");
  public static final MetricPrefix EXA = new MetricPrefix(18, "exa", "E");
  public static final MetricPrefix ZETTA = new MetricPrefix(21, "zetta", "Z");
  public static final MetricPrefix YOTTA = new MetricPrefix(24, "yotta", "Y");

  public static MetricPrefix ofPower(final int power) {
    return prefixes[0].power <= power && power <= prefixes[prefixes.length - 1].power ? prefixes[power - prefixes[0].power] : null;
  }

  private final int power;
  private final String prefix;
  private final String symbol;

  private MetricPrefix(final int power, final String prefix, final String symbol) {
    final int offset = prefixes[0] != null ? prefixes[0].power : power;
    prefixes[power - offset] = this;
    this.power = power;
    this.prefix = prefix;
    this.symbol = symbol;
  }

  public int getPower() {
    return this.power;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getSymbol() {
    return this.symbol;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof MetricPrefix))
      return false;

    final MetricPrefix that = (MetricPrefix)obj;
    return power == that.power;
  }

  @Override
  public int hashCode() {
    return power;
  }

  @Override
  public String toString() {
    return this.prefix;
  }
}