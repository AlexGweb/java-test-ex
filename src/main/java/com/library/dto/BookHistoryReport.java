package com.library.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookHistoryReport {
    private Long bookId;
    private String bookTitle;
    private List<BookMovementAction> movements;
    private int totalBorrowings;
    private int totalDelays;
    private String currentHolder;
    private LocalDateTime currentDueDate;
}