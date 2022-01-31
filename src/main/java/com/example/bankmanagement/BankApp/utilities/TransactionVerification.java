package com.example.bankmanagement.BankApp.utilities;

import com.example.bankmanagement.BankApp.entities.Account;
import com.example.bankmanagement.BankApp.entities.Transaction;
import com.example.bankmanagement.BankApp.repositories.AccountRepository;
import com.example.bankmanagement.BankApp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionVerification {

    private static AccountRepository accountRepo;
    private static TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostConstruct
    private void init() {
        accountRepo = this.accountRepository;
        transactionRepo = this.transactionRepository;
    }

    public static final boolean verifyThatAccountHasNotExceededDailyLimit(long accountNumber) {
        List<Transaction> userTransactions = transactionRepo.fetchTransactionListByAcctNumber(accountNumber);

        List<Transaction> userTransactionsToday = new ArrayList<>();

        for (Transaction t:
             userTransactions) {

            LocalDateTime localDateTime = t.getTransactionSubmitTime();
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDateTime localDateTimeStartOfDay = localDate.atStartOfDay();

            if(t.getTransactionSubmitTime().isAfter(localDateTimeStartOfDay)) {

                userTransactionsToday.add(t);
            }
        }

        double totalTransactionAmtForDay = 0.0;

        for (Transaction t :
                userTransactionsToday) {
            totalTransactionAmtForDay += t.getTransactionAmount();
        }

        if(totalTransactionAmtForDay > 10_000) {
            return false;
        }

        return true;
    }

    public static final boolean verifySufficientFunds(long accountNumber, double withdrawalAmount) {
        Account account = accountRepo.queryByAccountNumber(accountNumber);

        if(account.getCurrentBalance() - withdrawalAmount < 0) {
            return false;
        }

        return true;
    }
}
