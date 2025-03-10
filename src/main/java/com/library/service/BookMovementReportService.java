package com.library.service;

import com.library.dto.BookMovementAction;
import com.library.dto.BookMovementPeriodReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMovementReportService {
    private final BookHistoryService bookHistoryService;

    public BookMovementPeriodReport generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<BookMovementAction> periodMovements = bookHistoryService.getAllMovements().stream()
                .filter(action -> !action.getActionDate().isBefore(startDate) 
                        && !action.getActionDate().isAfter(endDate))
                .sorted(Comparator.comparing(BookMovementAction::getActionDate))
                .collect(Collectors.toList());

        List<BookMovementAction> borrowed = filterByType(periodMovements, "BORROWED");
        List<BookMovementAction> returned = filterByType(periodMovements, "RETURNED");
        
        List<BookMovementAction> currentlyBorrowed = borrowed.stream()
                .filter(action -> returned.stream()
                        .noneMatch(r -> r.getBookId().equals(action.getBookId())))
                .collect(Collectors.toList());

        List<BookMovementAction> overdue = currentlyBorrowed.stream()
                .filter(action -> action.getDueDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        int delays = (int) periodMovements.stream()
                .filter(action -> action.getActionType().equals("RETURNED"))
                .filter(action -> action.getReturnDate().isAfter(action.getDueDate()))
                .count();

        return BookMovementPeriodReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .movements(periodMovements)
                .totalBorrowings(borrowed.size())
                .totalReturns(returned.size())
                .totalDelays(delays)
                .currentlyBorrowed(currentlyBorrowed)
                .overdue(overdue)
                .build();
    }

    private List<BookMovementAction> filterByType(List<BookMovementAction> actions, String type) {
        return actions.stream()
                .filter(action -> action.getActionType().equals(type))
                .collect(Collectors.toList());
    }
}