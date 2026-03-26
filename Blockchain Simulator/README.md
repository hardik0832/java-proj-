# Blockchain Simulator (Java)

Simulates a basic blockchain with:

- Blocks linked by hash (`previousHash`)
- SHA-256 hashing of block content
- Simple transactions (`from`, `to`, `amount`)
- Proof-of-work mining (hash must start with N leading zeros)
- Chain integrity verification
- JSON output of the chain

## Run

Requires Java 17+. (Maven is optional.)

```bash
mvn -q clean package
mvn -q exec:java
```

This will also write `chain.json` in the project root.

### Run without Maven (plain Java)

PowerShell:

```powershell
mkdir out -ea 0
javac -d out (Get-ChildItem -Recurse -Filter *.java | % FullName)
java -cp out com.blocksim.Main
```

## Flow diagram

```mermaid
flowchart TD
  start([Start]) --> init[InitializeBlockchain]
  init --> addTx[AddTransactionsToPendingPool]
  addTx --> mine[MinePendingTransactions]
  mine --> buildBlock[BuildBlock(index,previousHash,txs,difficulty)]
  buildBlock --> pow[ProofOfWorkLoop(nonce++)]
  pow --> checkPow{HashHasLeadingZeros?}
  checkPow -- No --> pow
  checkPow -- Yes --> append[AppendBlockToChain]
  append --> validate[VerifyChainIntegrity]
  validate --> output[PrettyPrintChainAsJSON]
  output --> endNode([End])
```

## Notes

- This is a learning-oriented simulator (no signatures, no UTXO/account balances, no networking/consensus).
- Mining reward is modeled as a coinbase transaction (`from = null`).

