package com.example.bankmanagement.BankApp.repositories;

import com.example.bankmanagement.BankApp.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query(value = "SELECT * FROM customer WHERE customer.perm_account_number = ?1", nativeQuery = true)
    Customer queryByPAN(long panID);

}
