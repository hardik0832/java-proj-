package com.blocksim;

import java.util.List;

/**
 * Tiny JSON serializer to keep the project runnable with plain Java (no external deps).
 * Output is deterministic for hashing (compact mode) and readable for display (pretty mode).
 */
public final class JsonUtil {
  private JsonUtil() {}

  public static String chainToPrettyJson(List<Block> chain) {
    StringBuilder sb = new StringBuilder(1024);
    appendBlocks(sb, chain, 0, true);
    return sb.toString();
  }

  public static String transactionsToCompactJson(List<Transaction> txs) {
    StringBuilder sb = new StringBuilder(256);
    appendTransactions(sb, txs, 0, false);
    return sb.toString();
  }

  private static void appendBlocks(StringBuilder sb, List<Block> blocks, int indent, boolean pretty) {
    sb.append('[');
    if (pretty && !blocks.isEmpty()) sb.append('\n');

    for (int i = 0; i < blocks.size(); i++) {
      if (pretty) indent(sb, indent + 2);
      appendBlock(sb, blocks.get(i), indent + 2, pretty);
      if (i < blocks.size() - 1) sb.append(',');
      if (pretty) sb.append('\n');
    }

    if (pretty && !blocks.isEmpty()) indent(sb, indent);
    sb.append(']');
  }

  private static void appendBlock(StringBuilder sb, Block b, int indent, boolean pretty) {
    sb.append('{');
    if (pretty) sb.append('\n');

    field(sb, "index", b.getIndex(), indent, pretty, true);
    field(sb, "timestamp", b.getTimestamp(), indent, pretty, true);
    field(sb, "previousHash", b.getPreviousHash(), indent, pretty, true);
    field(sb, "difficulty", b.getDifficulty(), indent, pretty, true);
    field(sb, "nonce", b.getNonce(), indent, pretty, true);
    field(sb, "hash", b.getHash(), indent, pretty, true);

    if (pretty) indent(sb, indent);
    sb.append("\"transactions\":");
    if (pretty) sb.append(' ');
    appendTransactions(sb, b.getTransactions(), indent, pretty);
    if (pretty) sb.append('\n');

    if (pretty) indent(sb, indent - 2);
    sb.append('}');
  }

  private static void appendTransactions(StringBuilder sb, List<Transaction> txs, int indent, boolean pretty) {
    sb.append('[');
    if (pretty && !txs.isEmpty()) sb.append('\n');

    for (int i = 0; i < txs.size(); i++) {
      if (pretty) indent(sb, indent + 2);
      appendTransaction(sb, txs.get(i), indent + 2, pretty);
      if (i < txs.size() - 1) sb.append(',');
      if (pretty) sb.append('\n');
    }

    if (pretty && !txs.isEmpty()) indent(sb, indent);
    sb.append(']');
  }

  private static void appendTransaction(StringBuilder sb, Transaction tx, int indent, boolean pretty) {
    sb.append('{');
    if (pretty) sb.append('\n');

    if (pretty) indent(sb, indent);
    sb.append("\"from\":");
    if (pretty) sb.append(' ');
    if (tx.getFrom() == null) sb.append("null");
    else sb.append('"').append(escape(tx.getFrom())).append('"');
    sb.append(',');
    if (pretty) sb.append('\n');

    field(sb, "to", tx.getTo(), indent, pretty, true);
    field(sb, "amount", tx.getAmount(), indent, pretty, true);
    field(sb, "timestamp", tx.getTimestamp(), indent, pretty, true);
    field(sb, "txId", tx.getTxId(), indent, pretty, false);

    if (pretty) indent(sb, indent - 2);
    sb.append('}');
  }

  private static void field(StringBuilder sb, String key, String value, int indent, boolean pretty, boolean comma) {
    if (pretty) indent(sb, indent);
    sb.append('"').append(key).append("\":");
    if (pretty) sb.append(' ');
    if (value == null) sb.append("null");
    else sb.append('"').append(escape(value)).append('"');
    if (comma) sb.append(',');
    if (pretty) sb.append('\n');
  }

  private static void field(StringBuilder sb, String key, long value, int indent, boolean pretty, boolean comma) {
    if (pretty) indent(sb, indent);
    sb.append('"').append(key).append("\":");
    if (pretty) sb.append(' ');
    sb.append(value);
    if (comma) sb.append(',');
    if (pretty) sb.append('\n');
  }

  private static void field(StringBuilder sb, String key, int value, int indent, boolean pretty, boolean comma) {
    if (pretty) indent(sb, indent);
    sb.append('"').append(key).append("\":");
    if (pretty) sb.append(' ');
    sb.append(value);
    if (comma) sb.append(',');
    if (pretty) sb.append('\n');
  }

  private static void field(StringBuilder sb, String key, double value, int indent, boolean pretty, boolean comma) {
    if (pretty) indent(sb, indent);
    sb.append('"').append(key).append("\":");
    if (pretty) sb.append(' ');
    sb.append(formatDouble(value));
    if (comma) sb.append(',');
    if (pretty) sb.append('\n');
  }

  private static String formatDouble(double value) {
    if (Double.isNaN(value) || Double.isInfinite(value)) return "null";
    if (value == (long) value) return Long.toString((long) value);
    return Double.toString(value);
  }

  private static void indent(StringBuilder sb, int spaces) {
    sb.append(" ".repeat(Math.max(0, spaces)));
  }

  private static String escape(String s) {
    StringBuilder out = new StringBuilder(s.length() + 8);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '"' -> out.append("\\\"");
        case '\\' -> out.append("\\\\");
        case '\b' -> out.append("\\b");
        case '\f' -> out.append("\\f");
        case '\n' -> out.append("\\n");
        case '\r' -> out.append("\\r");
        case '\t' -> out.append("\\t");
        default -> {
          if (c < 0x20) out.append(String.format("\\u%04x", (int) c));
          else out.append(c);
        }
      }
    }
    return out.toString();
  }
}

