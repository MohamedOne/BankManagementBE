package com.example.bankmanagement.BankApp.services;

import com.example.bankmanagement.BankApp.entities.User;
import com.example.bankmanagement.BankApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return allUsers;
    }
}
