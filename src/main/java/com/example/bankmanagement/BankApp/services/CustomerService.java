package com.example.bankmanagement.BankApp.services;

import com.example.bankmanagement.BankApp.entities.Customer;
import com.example.bankmanagement.BankApp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        customerRepository.findAll().forEach(allCustomers::add);
        return allCustomers;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer queryByPAN(long panID) {
        return customerRepository.queryByPAN(panID);
    }
}
