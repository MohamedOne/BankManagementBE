package com.example.bankmanagement.BankApp.services;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Account queryByAccountNumber(long accountNumber) {
        return accountRepository.queryByAccountNumber(accountNumber);
    }

    public List<Account> queryByCustomerID(long customerID) {
        return accountRepository.queryByCustomerID(customerID);
    }

    public List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        accountRepository.findAll().forEach(allAccounts::add);
        return allAccounts;
    }
}
