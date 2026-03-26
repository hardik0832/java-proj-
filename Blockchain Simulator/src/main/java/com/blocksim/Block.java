package com.blocksim;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Block {
  private final int index;
  private final long timestamp;
  private final String previousHash;
  private final List<Transaction> transactions;
  private final int difficulty;

  private long nonce;
  private String hash;

  public Block(int index, String previousHash, List<Transaction> transactions, int difficulty) {
    this(index, Instant.now().toEpochMilli(), previousHash, transactions, difficulty);
  }

  public Block(int index, long timestamp, String previousHash, List<Transaction> transactions, int difficulty) {
    if (index < 0) throw new IllegalArgumentException("'index' must be >= 0");
    if (previousHash == null) throw new IllegalArgumentException("'previousHash' is required");
    if (difficulty < 0) throw new IllegalArgumentException("'difficulty' must be >= 0");
    this.index = index;
    this.timestamp = timestamp;
    this.previousHash = previousHash;
    this.transactions = Collections.unmodifiableList(new ArrayList<>(transactions == null ? List.of() : transactions));
    this.difficulty = difficulty;

    this.nonce = 0L;
    this.hash = calculateHash();
  }

  public String calculateHash() {
    String txJson = JsonUtil.transactionsToCompactJson(transactions);
    String payload = index
        + "|" + timestamp
        + "|" + previousHash
        + "|" + difficulty
        + "|" + nonce
        + "|" + txJson;
    return CryptoUtil.sha256(payload);
  }

  public void mine() {
    String targetPrefix = CryptoUtil.leadingZeros(difficulty);
    while (!hash.startsWith(targetPrefix)) {
      nonce++;
      hash = calculateHash();
    }
  }

  public boolean hasValidProofOfWork() {
    return hash != null
        && hash.equals(calculateHash())
        && hash.startsWith(CryptoUtil.leadingZeros(difficulty));
  }

  public boolean hasSelfConsistentTransactions() {
    for (Transaction tx : transactions) {
      if (tx == null || !tx.isSelfConsistent()) return false;
    }
    return true;
  }

  public int getIndex() {
    return index;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public long getNonce() {
    return nonce;
  }

  public String getHash() {
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Block)) return false;
    Block block = (Block) o;
    return index == block.index && Objects.equals(hash, block.hash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(index, hash);
  }
}

