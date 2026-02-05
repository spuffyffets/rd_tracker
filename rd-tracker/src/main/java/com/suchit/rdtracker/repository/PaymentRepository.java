package com.suchit.rdtracker.repository;

import com.suchit.rdtracker.entity.Customer;
import com.suchit.rdtracker.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByCustomerAndPaymentMonth(Customer customer, YearMonth paymentMonth);

    List<Payment> findByCustomer(Customer customer);
    
    List<Payment> findByPaymentMonth(YearMonth paymentMonth);

    
    @Query("""
    		SELECT COALESCE(SUM(p.amountPaid), 0)
    		FROM Payment p
    		WHERE p.paymentMonth = :month
    		""")
    		Double getTotalCollectedForMonth(@Param("month") YearMonth month);

}
