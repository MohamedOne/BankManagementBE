package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.exceptions.AccountNotFoundException;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import com.example.bankmanagement.BankApp.services.AccountService;
import com.example.bankmanagement.BankApp.utilities.Generator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    ResponseEntity<List<Account>> getAllAccounts() throws AccountNotFoundException{
        List<Account> allAccounts = accountService.getAllAccounts();

        if(allAccounts.size() < 1) { throw new AccountNotFoundException(); }

        return new ResponseEntity<List<Account>>(allAccounts, HttpStatus.OK);
    }

    @PostMapping("/accounts")
    ResponseEntity postNewAccount(@RequestBody Account account) {
        account.setAccountNumber(Generator.generateAccountNumber());
        account.setCurrentBalance(0);

        accountService.saveAccount(account);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/accounts/{customerID}")
    ResponseEntity<List<Account>> getAllAccountsByID(@PathVariable long customerID) throws AccountNotFoundException {
        List<Account> accounts = new ArrayList<>();


            accountService.queryByCustomerID(customerID).forEach(accounts::add);

            if (accounts.size() < 1) {
                throw new AccountNotFoundException();
            }

        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }
}
