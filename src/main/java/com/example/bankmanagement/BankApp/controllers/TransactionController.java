package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.entities.Transaction;
import com.example.bankmanagement.BankApp.models.TransactionSubtype;
import com.example.bankmanagement.BankApp.models.TransactionType;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import com.example.bankmanagement.BankApp.repositories.TransactionRepository;
import com.example.bankmanagement.BankApp.utilities.Generator;
import com.example.bankmanagement.BankApp.utilities.TransactionVerification;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/transactions")
    ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        transactionRepository.findAll().forEach(allTransactions::add);

        return new ResponseEntity<List<Transaction>>(allTransactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/recents/{transactionAccountNumber}")
    ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable long transactionAccountNumber) {
        List<Transaction> allTransactions = new ArrayList<>();
        transactionRepository.fetchTransactionListByAcctNumber(transactionAccountNumber).forEach(allTransactions::add);

        int len = allTransactions.size();
        List<Transaction> lastFiveTransactions = new ArrayList<>();
        for(int i = len-1; i > len - 6; i--) {
            lastFiveTransactions.add(allTransactions.get(i));
        }

        return new ResponseEntity<List<Transaction>>(lastFiveTransactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/day/{transactionAccountNumber}/{date}")
    ResponseEntity<List<Transaction>> getDaysTransactions(@PathVariable long transactionAccountNumber,
                                                          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-DD") LocalDate date) {
        List<Transaction> allTransactions = new ArrayList<>();
        transactionRepository.fetchTransactionListByAcctNumber(transactionAccountNumber).forEach(allTransactions::add);

        List<Transaction> userTransactionsToday = new ArrayList<>();

        for (Transaction t:
                allTransactions) {

            LocalDateTime localDateTimeStartOfDay = date.atStartOfDay();

            if(t.getTransactionSubmitTime().isAfter(localDateTimeStartOfDay)) {

                userTransactionsToday.add(t);
            }
        }

        return new ResponseEntity<List<Transaction>>(userTransactionsToday, HttpStatus.OK);
    }


    @PostMapping("/transactions")
    ResponseEntity postNewTransaction(@RequestBody Transaction transaction) {
        transaction.setTransactionRefNumber(Generator.generateTransactionReferenceNumber());
        transaction.setTransactionSubmitTime(Generator.generateCurrentTime());

        //User has exceeded daily limit and/or has insufficient funds
        if(TransactionVerification.verifySufficientFunds(transaction.getTransactionAccountNumberFrom(), transaction.getTransactionAmount())
                && TransactionVerification.verifyThatAccountHasNotExceededDailyLimit(transaction.getTransactionAccountNumberFrom())) {

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        //User is depositing money to own account
        if(transaction.getTransactionType() == TransactionType.DEBIT
            && transaction.getTransactionAccountNumberTo() == 0
            && transaction.getTransactionSubtype() == TransactionSubtype.CASH
            && transaction.getTransactionAmount() > 0) {

            Account account = accountRepository.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            account.setCurrentBalance(account.getCurrentBalance() + transaction.getTransactionAmount());
            accountRepository.save(account);
            transactionRepository.save(transaction);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        //User is withdrawing money from own account
        else if(transaction.getTransactionType() == TransactionType.CREDIT
                && transaction.getTransactionAccountNumberTo() == 0
                && transaction.getTransactionSubtype() == TransactionSubtype.CASH
                && transaction.getTransactionAmount() > 0) {

            Account account = accountRepository.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            account.setCurrentBalance(account.getCurrentBalance() - transaction.getTransactionAmount());
            accountRepository.save(account);
            transactionRepository.save(transaction);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        transactionRepository.save(transaction);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("/transactions/transfers")
    ResponseEntity transferFunds(@RequestBody Transaction transaction) {
        transaction.setTransactionRefNumber(Generator.generateTransactionReferenceNumber());
        transaction.setTransactionSubmitTime(Generator.generateCurrentTime());

        //User has exceeded daily limit and/or has insufficient funds
        if(TransactionVerification.verifySufficientFunds(transaction.getTransactionAccountNumberFrom(), transaction.getTransactionAmount())
                && TransactionVerification.verifyThatAccountHasNotExceededDailyLimit(transaction.getTransactionAccountNumberFrom())) {

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        if(transaction.getTransactionSubtype() == TransactionSubtype.TRANSFER
            && transaction.getTransactionAccountNumberTo() != 0) {

            Account payer = accountRepository.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            Account reciever = accountRepository.queryByAccountNumber(transaction.getTransactionAccountNumberTo());

            payer.setCurrentBalance(payer.getCurrentBalance() - transaction.getTransactionAmount());
            reciever.setCurrentBalance(reciever.getCurrentBalance() + transaction.getTransactionAmount());

            accountRepository.save(payer); accountRepository.save(reciever);
            transactionRepository.save(transaction);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

    }
}
