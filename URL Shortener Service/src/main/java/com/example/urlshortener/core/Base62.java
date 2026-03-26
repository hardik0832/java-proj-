package com.example.urlshortener.core;

public final class Base62 {
  private static final char[] ALPHABET =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
  private static final int BASE = ALPHABET.length;

  private Base62() {}

  public static String encode(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("value must be >= 0");
    }
    if (value == 0) {
      return "0";
    }
    StringBuilder sb = new StringBuilder();
    long v = value;
    while (v > 0) {
      int r = (int) (v % BASE);
      sb.append(ALPHABET[r]);
      v /= BASE;
    }
    return sb.reverse().toString();
  }
}

