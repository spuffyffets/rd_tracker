package com.suchit.rdtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDTO {

    private Double totalExpected;
    private Double totalCollected;
    private Double totalPending;
}
