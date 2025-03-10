package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private Integer availableCopies;
}