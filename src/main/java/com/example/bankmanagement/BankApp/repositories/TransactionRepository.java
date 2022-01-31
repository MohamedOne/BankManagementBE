package com.example.bankmanagement.BankApp.repositories;

import com.example.bankmanagement.BankApp.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transactions WHERE transaction_account_number_from = ?1", nativeQuery = true)
    List<Transaction> fetchTransactionListByAcctNumber(long accountNumber);
}
