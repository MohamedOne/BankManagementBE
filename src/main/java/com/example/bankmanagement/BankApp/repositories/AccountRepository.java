package com.example.bankmanagement.BankApp.repositories;

import com.example.bankmanagement.BankApp.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query(value = "SELECT * FROM account WHERE account_number = ?1", nativeQuery = true)
    Account queryByAccountNumber(long accountNumber);

    @Query(value = "SELECT * FROM account WHERE customerid = ?1", nativeQuery = true)
    List<Account> queryByCustomerID(long customerID);
}
