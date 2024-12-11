package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
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