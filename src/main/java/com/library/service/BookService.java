package com.library.service;

import com.library.model.Book;
import com.library.model.User;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
    }
    
    @Transactional
    public Book borrowBook(Long bookId, User user) {
        Book book = getBookById(bookId);
        if (book.getCurrentHolder() != null) {
            throw new RuntimeException("Book is already borrowed");
        }
        book.setCurrentHolder(user);
        return bookRepository.save(book);
    }
    
    @Transactional
    public Book returnBook(Long bookId) {
        Book book = getBookById(bookId);
        book.setCurrentHolder(null);
        return bookRepository.save(book);
    }
} 