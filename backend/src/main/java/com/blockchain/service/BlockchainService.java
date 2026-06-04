package com.blockchain.service;

import com.blockchain.core.Block;
import com.blockchain.core.BlockChain;
import com.blockchain.core.BlockHandler;
import com.blockchain.core.Transaction;
import com.blockchain.core.UTXOPool;
import com.blockchain.dto.BlockDTO;
import com.blockchain.dto.BlockchainStatusDTO;
import com.blockchain.dto.MineBlockRequest;
import com.blockchain.util.HexUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BlockchainService {
    private final WalletService walletService;
    private final BlockchainMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;
    private BlockChain blockChain;
    private BlockHandler blockHandler;

    public BlockchainService(WalletService walletService, BlockchainMapper mapper, SimpMessagingTemplate messagingTemplate) {
        this.walletService = walletService;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void initialize() {
        WalletService.Wallet systemWallet = walletService.createWallet("System Genesis Wallet");
        Block genesis = new Block(null, systemWallet.publicKey());
        genesis.finalize();
        blockChain = new BlockChain(genesis);
        blockHandler = new BlockHandler(blockChain);
    }

    public BlockchainStatusDTO getStatus() {
        UTXOPool pool = getSpendablePool();
        double coins = pool.getAllUTXO().stream()
                .map(pool::getTxOutput)
                .mapToDouble(output -> output.value)
                .sum();
        return new BlockchainStatusDTO(
                blockChain.getBlockCount(),
                blockChain.getCurrentLevel(),
                blockChain.getTransactionPool().getTransactions().size(),
                walletService.getAllWalletEntities().size(),
                coins
        );
    }

    public BlockDTO mineBlock(MineBlockRequest request) {
        Block block = blockHandler.createBlock(walletService.getPublicKey(request.minerWalletId()));
        if (block == null) {
            throw new IllegalStateException("Block was rejected by the blockchain");
        }
        BlockDTO dto = mapper.toBlockDto(block, blockChain);
        messagingTemplate.convertAndSend("/topic/blocks", dto);
        return dto;
    }

    public List<BlockDTO> getBlocks() {
        return blockChain.getAllBlocks().stream()
                .sorted(Comparator.comparingInt(block -> blockChain.getBlockHeight(block.getHash())))
                .map(block -> mapper.toBlockDto(block, blockChain))
                .toList();
    }

    public BlockDTO getBlockByHash(String hex) {
        Block block = blockChain.getBlockByHash(HexUtil.fromHex(hex));
        if (block == null) {
            throw new IllegalArgumentException("Block not found: " + hex);
        }
        return mapper.toBlockDto(block, blockChain);
    }

    public Block getMaxHeightBlock() {
        return blockChain.getMaxHeightBlock();
    }

    public UTXOPool getSpendablePool() {
        return blockChain.getMaxHeightUTXOPool();
    }

    public BlockChain getBlockChain() {
        return blockChain;
    }

    public BlockHandler getBlockHandler() {
        return blockHandler;
    }

    public void addTransaction(Transaction transaction) {
        blockHandler.processTx(transaction);
        messagingTemplate.convertAndSend("/topic/transactions", mapper.toTransactionDto(transaction));
    }
}
