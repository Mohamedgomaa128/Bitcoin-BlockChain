package com.blockchain.dto;

public record UTXODto(String txHash, int outputIndex, double value) {
}
