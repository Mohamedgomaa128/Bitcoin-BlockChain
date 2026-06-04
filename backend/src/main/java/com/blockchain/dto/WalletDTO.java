package com.blockchain.dto;

import java.util.List;

public record WalletDTO(String id, String name, String publicKey, double balance, List<UTXODto> utxos) {
}
