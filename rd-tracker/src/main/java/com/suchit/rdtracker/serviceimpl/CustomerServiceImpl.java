package com.suchit.rdtracker.serviceimpl;

import com.suchit.rdtracker.entity.Customer;
import com.suchit.rdtracker.repository.CustomerRepository;
import com.suchit.rdtracker.service.CustomerService;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {

        if (customerRepository.existsByAccountNo(customer.getAccountNo())) {
            throw new RuntimeException("Account number already exists");
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {

        Customer existing = getCustomerById(id);

        existing.setName(customer.getName());
        existing.setMobile(customer.getMobile());
        existing.setOpeningDate(customer.getOpeningDate());
        existing.setMonthlyAmount(customer.getMonthlyAmount());

        return customerRepository.save(existing);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll(
            Sort.by(Sort.Direction.ASC, "name")
        );
    }


    @Override
    public void deleteCustomer(Long id) {
        customerRepository.delete(getCustomerById(id));
    }
}
