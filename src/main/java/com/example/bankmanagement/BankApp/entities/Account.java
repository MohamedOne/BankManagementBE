package com.example.bankmanagement.BankApp.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _internalReference;
    private long accountNumber;
    private long customerID;
    //Add entity relationships
    private double currentBalance;
}
