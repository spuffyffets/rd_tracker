package com.suchit.rdtracker.repository;

import com.suchit.rdtracker.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByAccountNo(Long accountNo);
    
    @Query("SELECT COALESCE(SUM(c.monthlyAmount), 0) FROM Customer c")
    Double getTotalExpectedAmount();

}
