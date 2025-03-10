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
public class BookReport {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<BookAuditAction> added;
    private List<BookAuditAction> updated;
    private List<BookAuditAction> deleted;
    
    public int getTotalChanges() {
        return added.size() + updated.size() + deleted.size();
    }
} 