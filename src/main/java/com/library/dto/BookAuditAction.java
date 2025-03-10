package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAuditAction {
    private Long bookId;
    private String bookTitle;
    private String action;
    private LocalDateTime timestamp;
    private String username;
}