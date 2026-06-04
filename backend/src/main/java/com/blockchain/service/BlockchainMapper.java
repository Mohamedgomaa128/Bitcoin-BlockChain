package com.blockchain.service;

import com.blockchain.core.Block;
import com.blockchain.core.BlockChain;
import com.blockchain.core.Transaction;
import com.blockchain.dto.BlockDTO;
import com.blockchain.dto.TransactionDTO;
import com.blockchain.util.HexUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockchainMapper {
    public BlockDTO toBlockDto(Block block, BlockChain chain) {
        Transaction.Output coinbaseOutput = block.getCoinbase().getOutput(0);
        List<TransactionDTO> transactions = block.getTransactions().stream()
                .map(this::toTransactionDto)
                .toList();
        return new BlockDTO(
                HexUtil.toHex(block.getHash()),
                HexUtil.toHex(block.getPrevBlockHash()),
                transactions.size(),
                coinbaseOutput.value,
                HexUtil.publicKeyToHex(coinbaseOutput.address),
                chain.getBlockHeight(block.getHash()),
                transactions
        );
    }

    public TransactionDTO toTransactionDto(Transaction tx) {
        List<TransactionDTO.InputDTO> inputs = tx.getInputs().stream()
                .map(input -> new TransactionDTO.InputDTO(
                        HexUtil.toHex(input.prevTxHash),
                        input.outputIndex,
                        HexUtil.toHex(input.signature)
                ))
                .toList();
        List<TransactionDTO.OutputDTO> outputs = tx.getOutputs().stream()
                .map(output -> new TransactionDTO.OutputDTO(output.value, HexUtil.publicKeyToHex(output.address)))
                .toList();
        return new TransactionDTO(HexUtil.toHex(tx.getHash()), tx.isCoinbase(), inputs, outputs);
    }
}
