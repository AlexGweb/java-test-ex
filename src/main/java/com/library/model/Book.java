package com.library.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private String isbn;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User currentHolder;
} 