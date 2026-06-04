package com.blockchain.controller;

import com.blockchain.dto.BlockDTO;
import com.blockchain.dto.BlockchainStatusDTO;
import com.blockchain.dto.MineBlockRequest;
import com.blockchain.service.BlockchainService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {
    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/status")
    public BlockchainStatusDTO getStatus() {
        return blockchainService.getStatus();
    }

    @GetMapping("/blocks")
    public List<BlockDTO> getBlocks() {
        return blockchainService.getBlocks();
    }

    @GetMapping("/blocks/{hash}")
    public BlockDTO getBlock(@PathVariable String hash) {
        return blockchainService.getBlockByHash(hash);
    }

    @PostMapping("/mine")
    public BlockDTO mineBlock(@Valid @RequestBody MineBlockRequest request) {
        return blockchainService.mineBlock(request);
    }
}
