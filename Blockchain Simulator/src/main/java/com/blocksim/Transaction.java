package com.blocksim;

import java.time.Instant;
import java.util.Objects;

public final class Transaction {
  private final String from;
  private final String to;
  private final double amount;
  private final long timestamp;
  private final String txId;

  public Transaction(String from, String to, double amount) {
    this(from, to, amount, Instant.now().toEpochMilli());
  }

  public Transaction(String from, String to, double amount, long timestamp) {
    if (to == null || to.isBlank()) throw new IllegalArgumentException("'to' is required");
    if (amount <= 0) throw new IllegalArgumentException("'amount' must be > 0");
    this.from = (from == null || from.isBlank()) ? null : from;
    this.to = to;
    this.amount = amount;
    this.timestamp = timestamp;
    this.txId = computeTxId();
  }

  private String computeTxId() {
    String payload = (from == null ? "COINBASE" : from)
        + "|" + to
        + "|" + amount
        + "|" + timestamp;
    return CryptoUtil.sha256(payload);
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public double getAmount() {
    return amount;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getTxId() {
    return txId;
  }

  public boolean isCoinbase() {
    return from == null;
  }

  public boolean isSelfConsistent() {
    return Objects.equals(txId, computeTxId());
  }
}

