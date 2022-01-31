package com.example.bankmanagement.BankApp.entities;

import com.example.bankmanagement.BankApp.models.Roles;
import lombok.Data;


import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long _internalID;
    private Long userID;
    private String password;
    private Roles userRole;


}
