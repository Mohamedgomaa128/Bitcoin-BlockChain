package com.blockchain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreateTransactionRequest(
        @NotBlank String senderWalletId,
        @NotBlank String recipientPublicKey,
        @DecimalMin(value = "0.00000001") double amount
) {
}
