package com.example.bankmanagement.BankApp.entities;

import com.example.bankmanagement.BankApp.models.TransactionSubtype;
import com.example.bankmanagement.BankApp.models.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _internalTransactionReference;
    private long transactionAccountNumberFrom;
    private long transactionAccountNumberTo;
    private long transactionRefNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "account_transactions", nullable = false)
    @ToString.Exclude
    private Account account;
    private LocalDateTime transactionSubmitTime;
    private TransactionType transactionType;
    private TransactionSubtype transactionSubtype;
    private double transactionAmount;
}
