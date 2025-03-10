package com.library.controller;

import com.library.dto.BookReport;
import com.library.service.BookAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports/books")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
public class BookReportController {
    private final BookAuditService bookAuditService;

    @GetMapping("/monthly")
    public BookReport getMonthlyReport() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return bookAuditService.generateReport(startDate, endDate);
    }

    @GetMapping("/yearly")
    public BookReport getYearlyReport() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return bookAuditService.generateReport(startDate, endDate);
    }
} 