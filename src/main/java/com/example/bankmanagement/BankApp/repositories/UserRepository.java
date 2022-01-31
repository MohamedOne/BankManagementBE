package com.example.bankmanagement.BankApp.repositories;

import com.example.bankmanagement.BankApp.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
