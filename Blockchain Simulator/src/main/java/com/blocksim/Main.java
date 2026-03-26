package com.blocksim;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
  public static void main(String[] args) throws Exception {
    int difficulty = 4;       // leading zeros required
    double miningReward = 50; // coinbase reward per mined block

    Blockchain chain = new Blockchain(difficulty, miningReward);

    chain.addTransaction(new Transaction("alice", "bob", 5.25));
    chain.addTransaction(new Transaction("bob", "carol", 2.00));
    chain.minePendingTransactions("miner1");

    chain.addTransaction(new Transaction("carol", "dave", 1.10));
    chain.addTransaction(new Transaction("alice", "carol", 0.75));
    chain.minePendingTransactions("miner1");

    boolean ok = chain.isChainValid();

    String chainJson = JsonUtil.chainToPrettyJson(chain.getChain());
    String output = "Chain valid: " + ok + System.lineSeparator()
        + "Difficulty: " + difficulty + System.lineSeparator()
        + System.lineSeparator()
        + chainJson;

    System.out.println(output);

    Path outPath = Path.of("chain.json");
    Files.writeString(outPath, chainJson, StandardCharsets.UTF_8);
  }
}

