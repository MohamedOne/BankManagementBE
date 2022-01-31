package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Customer;
import com.example.bankmanagement.BankApp.repositories.CustomerRepository;
import com.example.bankmanagement.BankApp.utilities.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        customerRepository.findAll().forEach(allCustomers::add);
        return new ResponseEntity<List<Customer>>(allCustomers, HttpStatus.OK);
    }

    @PostMapping("/customers")
    ResponseEntity postNewCustomer(@RequestBody Customer customer) {
        customer.setCustomerID(Generator.generateCustomerID());
        customerRepository.save(customer);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/customers/{panID}")
    ResponseEntity<Customer> queryByPAN(@PathVariable long panID) {

        Customer customer = customerRepository.queryByPAN(panID);

        if(customer != null) {
            return new ResponseEntity<Customer>(customer, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
