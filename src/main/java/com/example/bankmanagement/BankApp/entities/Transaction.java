package com.example.bankmanagement.BankApp.entities;

import com.example.bankmanagement.BankApp.models.TransactionSubtype;
import com.example.bankmanagement.BankApp.models.TransactionType;
import lombok.Data;
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
    private LocalDateTime transactionSubmitTime;
    private TransactionType transactionType;
    private TransactionSubtype transactionSubtype;
    private double transactionAmount;
}
