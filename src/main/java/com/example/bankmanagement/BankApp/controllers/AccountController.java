package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import com.example.bankmanagement.BankApp.utilities.Generator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        accountRepository.findAll().forEach(allAccounts::add);

        return new ResponseEntity<List<Account>>(allAccounts, HttpStatus.OK);
    }

    @PostMapping("/accounts")
    ResponseEntity postNewAccount(@RequestBody Account account) {
        account.setAccountNumber(Generator.generateAccountNumber());
        account.setCurrentBalance(0);

        accountRepository.save(account);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/accounts/{customerID}")
    ResponseEntity<List<Account>> getAllAccountsByID(@PathVariable long customerID) {
        List<Account> accounts = new ArrayList<>();
        accountRepository.queryByCustomerID(customerID).forEach(accounts::add);

        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }
}
