package com.suchit.rdtracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which month payment is for (2026-02)
    private YearMonth paymentMonth;

    private Double amountPaid;

    // CASH / ONLINE
    private String paymentMode;

    // Actual payment date & time
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
}
