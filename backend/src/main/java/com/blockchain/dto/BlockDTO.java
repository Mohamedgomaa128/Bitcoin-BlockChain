package com.blockchain.dto;

import java.util.List;

public record BlockDTO(
        String hash,
        String prevBlockHash,
        int transactionCount,
        double coinbaseValue,
        String coinbaseRecipient,
        int height,
        List<TransactionDTO> transactions
) {
}
