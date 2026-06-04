package com.blockchain.dto;

import jakarta.validation.constraints.NotBlank;

public record MineBlockRequest(@NotBlank String minerWalletId) {
}
