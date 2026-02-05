package com.suchit.rdtracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RD account number (bank already gives)
    private Long accountNo;

    private String name;

    private String mobile;

    // RD opening date (selected manually)
    private LocalDate openingDate;

    // Monthly RD amount
    private Double monthlyAmount;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;
}
