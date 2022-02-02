package com.example.bankmanagement.BankApp.services;

import com.example.bankmanagement.BankApp.entities.Transaction;
import com.example.bankmanagement.BankApp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        transactionRepository.findAll().forEach(allTransactions::add);
        return allTransactions;
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }


}
