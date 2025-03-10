package com.library.controller;

import com.library.dto.BookHistoryReport;
import com.library.service.BookHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/{bookId}/history")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
public class BookHistoryController {
    private final BookHistoryService bookHistoryService;

    @GetMapping
    public BookHistoryReport getBookHistory(@PathVariable Long bookId) {
        return bookHistoryService.generateBookHistory(bookId);
    }
} 