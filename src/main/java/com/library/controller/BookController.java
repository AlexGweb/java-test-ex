package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.BookRequest;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public Book addBook(@RequestBody BookRequest request) {
        return bookService.addBook(request);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public Book updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        return bookService.updateBook(id, request);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
    
    @PostMapping("/{id}/borrow")
    public Book borrowBook(@PathVariable Long id, @RequestParam Long userId) {
        return bookService.borrowBook(id, userService.getUserById(userId));
    }
    
    @PostMapping("/{id}/return")
    public Book returnBook(@PathVariable Long id) {
        return bookService.returnBook(id);
    }
} 