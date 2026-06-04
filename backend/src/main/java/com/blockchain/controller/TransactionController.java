package com.blockchain.controller;

import com.blockchain.dto.CreateTransactionRequest;
import com.blockchain.dto.TransactionDTO;
import com.blockchain.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionDTO createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/pending")
    public List<TransactionDTO> getPendingTransactions() {
        return transactionService.getPendingTransactions();
    }

    @GetMapping("/{hash}")
    public TransactionDTO getTransaction(@PathVariable String hash) {
        return transactionService.getTransactionByHash(hash);
    }
}
