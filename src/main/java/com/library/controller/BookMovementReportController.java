package com.library.controller;

import com.library.dto.BookMovementPeriodReport;
import com.library.service.BookMovementReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports/movements")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
public class BookMovementReportController {
    private final BookMovementReportService reportService;

    @GetMapping("/monthly")
    public BookMovementPeriodReport getMonthlyReport() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return reportService.generateReport(startDate, endDate);
    }

    @GetMapping("/yearly")
    public BookMovementPeriodReport getYearlyReport() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return reportService.generateReport(startDate, endDate);
    }
} 