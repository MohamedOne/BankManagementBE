package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.User;
import com.example.bankmanagement.BankApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
    }

    @PostMapping("/users")
    ResponseEntity postAUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
