package com.blockchain.service;

import com.blockchain.core.Transaction;
import com.blockchain.core.UTXO;
import com.blockchain.core.UTXOPool;
import com.blockchain.dto.UTXODto;
import com.blockchain.dto.WalletDTO;
import com.blockchain.util.HexUtil;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WalletService {
    private final Map<String, Wallet> wallets = new ConcurrentHashMap<>();

    public Wallet createWallet(String name) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            String id = UUID.randomUUID().toString();
            Wallet wallet = new Wallet(id, normalizeName(name), keyPair.getPublic(), keyPair.getPrivate());
            wallets.put(id, wallet);
            return wallet;
        } catch (Exception ex) {
            throw new IllegalStateException("Could not create wallet", ex);
        }
    }

    public Wallet getWalletEntity(String id) {
        Wallet wallet = wallets.get(id);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found: " + id);
        }
        return wallet;
    }

    public List<Wallet> getAllWalletEntities() {
        return new ArrayList<>(wallets.values());
    }

    public PrivateKey getPrivateKey(String walletId) {
        return getWalletEntity(walletId).privateKey();
    }

    public PublicKey getPublicKey(String walletId) {
        return getWalletEntity(walletId).publicKey();
    }

    public WalletDTO toDto(Wallet wallet, UTXOPool pool) {
        List<UTXODto> utxos = getUtxos(wallet, pool);
        double balance = utxos.stream().mapToDouble(UTXODto::value).sum();
        return new WalletDTO(wallet.id(), wallet.name(), HexUtil.publicKeyToHex(wallet.publicKey()), balance, utxos);
    }

    public List<UTXODto> getUtxos(Wallet wallet, UTXOPool pool) {
        List<UTXODto> result = new ArrayList<>();
        for (UTXO utxo : pool.getAllUTXO()) {
            Transaction.Output output = pool.getTxOutput(utxo);
            if (Arrays.equals(output.address.getEncoded(), wallet.publicKey().getEncoded())) {
                result.add(new UTXODto(HexUtil.toHex(utxo.getTxHash()), utxo.getIndex(), output.value));
            }
        }
        return result;
    }

    private String normalizeName(String name) {
        return name == null || name.isBlank() ? "Wallet " + (wallets.size() + 1) : name.trim();
    }

    public record Wallet(String id, String name, PublicKey publicKey, PrivateKey privateKey) {
    }
}
