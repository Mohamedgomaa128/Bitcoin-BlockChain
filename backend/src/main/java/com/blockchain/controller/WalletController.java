package com.blockchain.controller;

import com.blockchain.dto.CreateWalletRequest;
import com.blockchain.dto.UTXODto;
import com.blockchain.dto.WalletDTO;
import com.blockchain.service.BlockchainService;
import com.blockchain.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;
    private final BlockchainService blockchainService;

    public WalletController(WalletService walletService, BlockchainService blockchainService) {
        this.walletService = walletService;
        this.blockchainService = blockchainService;
    }

    @PostMapping
    public WalletDTO createWallet(@RequestBody(required = false) CreateWalletRequest request) {
        String name = request == null ? null : request.name();
        return walletService.toDto(walletService.createWallet(name), blockchainService.getSpendablePool());
    }

    @GetMapping
    public List<WalletDTO> getWallets() {
        return walletService.getAllWalletEntities().stream()
                .map(wallet -> walletService.toDto(wallet, blockchainService.getSpendablePool()))
                .toList();
    }

    @GetMapping("/{id}")
    public WalletDTO getWallet(@PathVariable String id) {
        return walletService.toDto(walletService.getWalletEntity(id), blockchainService.getSpendablePool());
    }

    @GetMapping("/{id}/utxos")
    public List<UTXODto> getUtxos(@PathVariable String id) {
        return walletService.getUtxos(walletService.getWalletEntity(id), blockchainService.getSpendablePool());
    }
}
