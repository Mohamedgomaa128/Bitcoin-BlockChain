package com.blockchain.service;

import com.blockchain.core.Transaction;
import com.blockchain.core.TxHandler;
import com.blockchain.core.UTXO;
import com.blockchain.core.UTXOPool;
import com.blockchain.dto.CreateTransactionRequest;
import com.blockchain.dto.TransactionDTO;
import com.blockchain.util.HexUtil;
import org.springframework.stereotype.Service;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {
    private final BlockchainService blockchainService;
    private final WalletService walletService;
    private final BlockchainMapper mapper;

    public TransactionService(BlockchainService blockchainService, WalletService walletService, BlockchainMapper mapper) {
        this.blockchainService = blockchainService;
        this.walletService = walletService;
        this.mapper = mapper;
    }

    public TransactionDTO createTransaction(CreateTransactionRequest request) {
        WalletService.Wallet sender = walletService.getWalletEntity(request.senderWalletId());
        UTXOPool pool = blockchainService.getSpendablePool();
        List<UTXO> selected = selectUtxos(sender, pool, request.amount());
        double selectedValue = selected.stream().mapToDouble(utxo -> pool.getTxOutput(utxo).value).sum();

        Transaction tx = new Transaction();
        for (UTXO utxo : selected) {
            tx.addInput(utxo.getTxHash(), utxo.getIndex());
        }
        tx.addOutput(request.amount(), HexUtil.publicKeyFromHex(request.recipientPublicKey()));
        double change = selectedValue - request.amount();
        if (change > 0.00000001) {
            tx.addOutput(change, sender.publicKey());
        }
        signInputs(tx, request.senderWalletId());
        tx.finalize();

        TxHandler validator = new TxHandler(pool);
        if (!validator.isValidTx(tx)) {
            throw new IllegalArgumentException("Transaction is not valid");
        }

        blockchainService.addTransaction(tx);
        return mapper.toTransactionDto(tx);
    }

    public List<TransactionDTO> getPendingTransactions() {
        return blockchainService.getBlockChain().getTransactionPool().getTransactions().stream()
                .map(mapper::toTransactionDto)
                .toList();
    }

    public TransactionDTO getTransactionByHash(String hex) {
        byte[] hash = HexUtil.fromHex(hex);
        Transaction pending = blockchainService.getBlockChain().getTransactionPool().getTransaction(hash);
        if (pending != null) {
            return mapper.toTransactionDto(pending);
        }
        for (var block : blockchainService.getBlockChain().getAllBlocks()) {
            if (Arrays.equals(block.getCoinbase().getHash(), hash)) {
                return mapper.toTransactionDto(block.getCoinbase());
            }
            for (Transaction tx : block.getTransactions()) {
                if (Arrays.equals(tx.getHash(), hash)) {
                    return mapper.toTransactionDto(tx);
                }
            }
        }
        throw new IllegalArgumentException("Transaction not found: " + hex);
    }

    private List<UTXO> selectUtxos(WalletService.Wallet sender, UTXOPool pool, double amount) {
        List<UTXO> selected = new ArrayList<>();
        double total = 0.0;
        for (UTXO utxo : pool.getAllUTXO()) {
            Transaction.Output output = pool.getTxOutput(utxo);
            if (Arrays.equals(output.address.getEncoded(), sender.publicKey().getEncoded())) {
                selected.add(utxo);
                total += output.value;
                if (total + 0.00000001 >= amount) {
                    return selected;
                }
            }
        }
        throw new IllegalArgumentException("Insufficient funds");
    }

    private void signInputs(Transaction tx, String walletId) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            for (int i = 0; i < tx.numInputs(); i++) {
                signature.initSign(walletService.getPrivateKey(walletId));
                signature.update(tx.getRawDataToSign(i));
                tx.addSignature(signature.sign(), i);
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Could not sign transaction", ex);
        }
    }
}
