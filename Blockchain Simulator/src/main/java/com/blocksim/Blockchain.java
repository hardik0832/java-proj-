package com.blocksim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Blockchain {
  private final List<Block> chain = new ArrayList<>();
  private final List<Transaction> pendingTransactions = new ArrayList<>();

  private final int difficulty;
  private final double miningReward;

  public Blockchain(int difficulty, double miningReward) {
    if (difficulty < 0) throw new IllegalArgumentException("'difficulty' must be >= 0");
    if (miningReward < 0) throw new IllegalArgumentException("'miningReward' must be >= 0");
    this.difficulty = difficulty;
    this.miningReward = miningReward;

    chain.add(createGenesisBlock());
  }

  private Block createGenesisBlock() {
    Block genesis = new Block(0, "0", List.of(), difficulty);
    genesis.mine();
    return genesis;
  }

  public void addTransaction(Transaction tx) {
    if (tx == null) throw new IllegalArgumentException("'tx' is required");
    if (!tx.isSelfConsistent()) throw new IllegalArgumentException("Transaction txId mismatch");
    pendingTransactions.add(tx);
  }

  public Block minePendingTransactions(String minerAddress) {
    if (minerAddress == null || minerAddress.isBlank()) {
      throw new IllegalArgumentException("'minerAddress' is required");
    }

    if (miningReward > 0) {
      pendingTransactions.add(new Transaction(null, minerAddress, miningReward));
    }

    int nextIndex = chain.size();
    String prevHash = chain.get(chain.size() - 1).getHash();
    Block block = new Block(nextIndex, prevHash, pendingTransactions, difficulty);
    block.mine();

    chain.add(block);
    pendingTransactions.clear();
    return block;
  }

  public List<Block> getChain() {
    return Collections.unmodifiableList(chain);
  }

  public List<Transaction> getPendingTransactions() {
    return Collections.unmodifiableList(pendingTransactions);
  }

  public int getDifficulty() {
    return difficulty;
  }

  public boolean isChainValid() {
    if (chain.isEmpty()) return false;
    if (chain.get(0).getIndex() != 0) return false;
    if (!chain.get(0).hasValidProofOfWork()) return false;
    if (!chain.get(0).hasSelfConsistentTransactions()) return false;

    for (int i = 1; i < chain.size(); i++) {
      Block current = chain.get(i);
      Block previous = chain.get(i - 1);

      if (current.getIndex() != i) return false;
      if (!current.getPreviousHash().equals(previous.getHash())) return false;
      if (!current.hasSelfConsistentTransactions()) return false;
      if (!current.hasValidProofOfWork()) return false;
    }
    return true;
  }
}

