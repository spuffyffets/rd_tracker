package com.suchit.rdtracker.dto;

import lombok.Data;

@Data
public class PaidCustomerDTO {

    private Long customerId;
    private Long accountNo;
    private String name;
    private Double amountPaid;
    private String paymentMode;
}
