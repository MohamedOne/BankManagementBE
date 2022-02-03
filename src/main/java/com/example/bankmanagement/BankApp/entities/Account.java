package com.example.bankmanagement.BankApp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _internalReference;
    private long accountNumber;
    private long customerID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "customer_accounts", nullable = false)
    @ToString.Exclude
    private Customer customer;

    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
    private double currentBalance;
}
