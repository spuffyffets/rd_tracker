package com.suchit.rdtracker.service;

import com.suchit.rdtracker.dto.DashboardSummaryDTO;
import com.suchit.rdtracker.dto.PaidCustomerDTO;
import com.suchit.rdtracker.dto.PendingCustomerDTO;
import com.suchit.rdtracker.entity.Customer;
import com.suchit.rdtracker.entity.Payment;

import java.time.YearMonth;
import java.util.List;

public interface PaymentService {

    Payment addPayment(Long customerId, Payment payment);

    List<Payment> getPaymentsByCustomer(Long customerId);

    boolean isPaymentDoneForMonth(Long customerId, YearMonth month);
    
    List<PaidCustomerDTO> getPaidCustomersForMonth(YearMonth month);


//    List<Long> getPendingCustomersForCurrentMonth();
    List<PendingCustomerDTO> getPendingCustomersForMonth(YearMonth month);
    
    DashboardSummaryDTO getDashboardSummary(YearMonth month);
    
    byte[] generatePaidCustomersPdf(YearMonth month);
    
    Payment updatePayment(Long paymentId, Payment payment);
    void deletePayment(Long paymentId);
    Payment getPaymentById(Long paymentId);






}
