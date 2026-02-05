package com.suchit.rdtracker.service;

import com.suchit.rdtracker.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer addCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> getAllCustomers();

    void deleteCustomer(Long id);
}
