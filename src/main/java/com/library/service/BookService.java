package com.library.service;

import com.library.model.Book;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.dto.BookRequest;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuditService bookAuditService;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Книга не найдена"));
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

    public Book addBook(BookRequest request) {
        Book book = Book.builder()
            .title(request.getTitle())
            .author(request.getAuthor())
            .isbn(request.getIsbn())
            .publicationYear(request.getPublicationYear())
            .availableCopies(request.getAvailableCopies())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        Book savedBook = bookRepository.save(book);
        bookAuditService.logAction(savedBook, "ADDED", getCurrentUsername());
        return savedBook;
    }

    public Book updateBook(Long id, BookRequest request) {
        Book existingBook = getBookById(id);
        
        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setPublicationYear(request.getPublicationYear());
        existingBook.setAvailableCopies(request.getAvailableCopies());
        existingBook.setUpdatedAt(LocalDateTime.now());
        
        Book updatedBook = bookRepository.save(existingBook);
        bookAuditService.logAction(updatedBook, "UPDATED", getCurrentUsername());
        return updatedBook;
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.deleteById(id);
        bookAuditService.logAction(book, "DELETED", getCurrentUsername());
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
} 