package com.blockchain.dto;

public record BlockchainStatusDTO(
        int totalBlocks,
        int chainHeight,
        int pendingTransactions,
        int totalWallets,
        double totalCoinsInCirculation
) {
}
