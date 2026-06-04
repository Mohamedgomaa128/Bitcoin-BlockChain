package com.blockchain.dto;

import java.util.List;

public record TransactionDTO(
        String hash,
        boolean coinbase,
        List<InputDTO> inputs,
        List<OutputDTO> outputs
) {
    public record InputDTO(String prevTxHash, int outputIndex, String signature) {
    }

    public record OutputDTO(double value, String address) {
    }
}
