package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.User;
import com.example.bankmanagement.BankApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
    }

    @PostMapping("/users")
    ResponseEntity postAUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
