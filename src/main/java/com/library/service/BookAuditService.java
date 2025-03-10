package com.library.service;

import org.springframework.stereotype.Service;

import com.library.dto.BookAuditAction;
import com.library.dto.BookReport;
import com.library.model.Book;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookAuditService {
    private final List<BookAuditAction> auditActions = new ArrayList<>();

    public void logAction(Book book, String action, String username) {
        auditActions.add(BookAuditAction.builder()
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .action(action)
                .timestamp(LocalDateTime.now())
                .username(username)
                .build());
    }

    public BookReport generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<BookAuditAction> periodActions = auditActions.stream()
                .filter(action -> !action.getTimestamp().isBefore(startDate) 
                        && !action.getTimestamp().isAfter(endDate))
                .collect(Collectors.toList());

        return BookReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .added(filterByAction(periodActions, "ADDED"))
                .updated(filterByAction(periodActions, "UPDATED"))
                .deleted(filterByAction(periodActions, "DELETED"))
                .build();
    }

    private List<BookAuditAction> filterByAction(List<BookAuditAction> actions, String actionType) {
        return actions.stream()
                .filter(action -> action.getAction().equals(actionType))
                .collect(Collectors.toList());
    }
} 