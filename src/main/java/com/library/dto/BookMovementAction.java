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
public class BookMovementAction {
    private Long bookId;
    private String bookTitle;
    private String username;
    private String actionType; // BORROWED, RETURNED
    private LocalDateTime actionDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
}