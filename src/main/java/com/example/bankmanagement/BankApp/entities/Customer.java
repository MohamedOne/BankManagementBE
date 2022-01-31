package com.example.bankmanagement.BankApp.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _internalCustomerID;
    private long customerID;
    private long permAccountNumber;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private LocalDate customerBirthDate;
}
