package com.example.bankmanagement.BankApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long _internalCustomerID;
    private long customerID;
    private long permAccountNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private LocalDate customerBirthDate;
}
