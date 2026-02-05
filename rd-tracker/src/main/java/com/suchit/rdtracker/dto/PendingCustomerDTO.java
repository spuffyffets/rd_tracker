package com.suchit.rdtracker.dto;

import java.time.YearMonth;

public class PendingCustomerDTO {

    private Long customerId;
    private Long accountNo;
    private String name;
    private Double monthlyAmount;

    private YearMonth pendingSince;
    private int pendingCount;

    // getters & setters
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getMonthlyAmount() {
        return monthlyAmount;
    }
    public void setMonthlyAmount(Double monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public YearMonth getPendingSince() {
        return pendingSince;
    }
    public void setPendingSince(YearMonth pendingSince) {
        this.pendingSince = pendingSince;
    }

    public int getPendingCount() {
        return pendingCount;
    }
    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }
}
