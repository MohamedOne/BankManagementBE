package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.entities.Transaction;
import com.example.bankmanagement.BankApp.exceptions.ExceededDailyLimitException;
import com.example.bankmanagement.BankApp.exceptions.InsufficientFundsException;
import com.example.bankmanagement.BankApp.models.TransactionSubtype;
import com.example.bankmanagement.BankApp.models.TransactionType;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import com.example.bankmanagement.BankApp.repositories.TransactionRepository;
import com.example.bankmanagement.BankApp.services.AccountService;
import com.example.bankmanagement.BankApp.services.TransactionService;
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
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions")
    ResponseEntity<List<Transaction>> getAllTransactions() {

        List<Transaction> allTransactions = transactionService.getAllTransactions();
        return new ResponseEntity<List<Transaction>>(allTransactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/recents/{transactionAccountNumber}")
    ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable long transactionAccountNumber) {
        List<Transaction> allTransactions = transactionService.getAllTransactions();

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
        List<Transaction> allTransactions = transactionService.getAllTransactions();

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
    ResponseEntity postNewTransaction(@RequestBody Transaction transaction) throws ExceededDailyLimitException {
        transaction.setTransactionRefNumber(Generator.generateTransactionReferenceNumber());
        transaction.setTransactionSubmitTime(Generator.generateCurrentTime());

        //User has exceeded daily limit and/or has insufficient funds
        if(TransactionVerification.verifySufficientFunds(transaction.getTransactionAccountNumberFrom(), transaction.getTransactionAmount())
                && TransactionVerification.verifyThatAccountHasNotExceededDailyLimit(transaction.getTransactionAccountNumberFrom())) {

            throw new ExceededDailyLimitException();
        }
        //User is depositing money to own account
        if(transaction.getTransactionType() == TransactionType.DEBIT
            && transaction.getTransactionAccountNumberTo() == 0
            && transaction.getTransactionSubtype() == TransactionSubtype.CASH
            && transaction.getTransactionAmount() > 0) {

            Account account = accountService.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            account.setCurrentBalance(account.getCurrentBalance() + transaction.getTransactionAmount());
            accountService.saveAccount(account);
            transactionService.saveTransaction(transaction);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        //User is withdrawing money from own account
        else if(transaction.getTransactionType() == TransactionType.CREDIT
                && transaction.getTransactionAccountNumberTo() == 0
                && transaction.getTransactionSubtype() == TransactionSubtype.CASH
                && transaction.getTransactionAmount() > 0) {

            Account account = accountService.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            account.setCurrentBalance(account.getCurrentBalance() - transaction.getTransactionAmount());
            accountService.saveAccount(account);
            transactionService.saveTransaction(transaction);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        transactionService.saveTransaction(transaction);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("/transactions/transfers")
    ResponseEntity transferFunds(@RequestBody Transaction transaction) throws InsufficientFundsException {
        transaction.setTransactionRefNumber(Generator.generateTransactionReferenceNumber());
        transaction.setTransactionSubmitTime(Generator.generateCurrentTime());

        //User has exceeded daily limit and/or has insufficient funds
        if(TransactionVerification.verifySufficientFunds(transaction.getTransactionAccountNumberFrom(), transaction.getTransactionAmount())
                && TransactionVerification.verifyThatAccountHasNotExceededDailyLimit(transaction.getTransactionAccountNumberFrom())) {

            throw new InsufficientFundsException();
        }

        if(transaction.getTransactionSubtype() == TransactionSubtype.TRANSFER
            && transaction.getTransactionAccountNumberTo() != 0) {

            Account payer = accountService.queryByAccountNumber(transaction.getTransactionAccountNumberFrom());
            Account reciever = accountService.queryByAccountNumber(transaction.getTransactionAccountNumberTo());

            payer.setCurrentBalance(payer.getCurrentBalance() - transaction.getTransactionAmount());
            reciever.setCurrentBalance(reciever.getCurrentBalance() + transaction.getTransactionAmount());

            accountService.saveAccount(payer); accountService.saveAccount(reciever);
            transactionService.saveTransaction(transaction);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

    }
}
