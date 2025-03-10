package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMovementPeriodReport {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<BookMovementAction> movements;
    private int totalBorrowings;
    private int totalReturns;
    private int totalDelays;
    private List<BookMovementAction> currentlyBorrowed;
    private List<BookMovementAction> overdue;
}