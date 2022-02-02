package com.example.bankmanagement.BankApp.controllers;

import com.example.bankmanagement.BankApp.entities.Customer;
import com.example.bankmanagement.BankApp.repositories.CustomerRepository;
import com.example.bankmanagement.BankApp.services.CustomerService;
import com.example.bankmanagement.BankApp.utilities.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return new ResponseEntity<List<Customer>>(allCustomers, HttpStatus.OK);
    }

    @PostMapping(value = "/customers")
    ResponseEntity postNewCustomer(@RequestBody Customer customer) {
        customer.setCustomerID(Generator.generateCustomerID());
        if(customer.getPermAccountNumber() == 0) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        customerService.saveCustomer(customer);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/customers/{panID}")
    ResponseEntity<Customer> queryByPAN(@PathVariable long panID) {

        Customer customer = customerService.queryByPAN(panID);

        if(customer != null) {
            return new ResponseEntity<Customer>(customer, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
