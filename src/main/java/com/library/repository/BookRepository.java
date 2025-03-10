package com.library.repository;

import com.library.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init() {
        books.addAll(Arrays.asList(
            new Book(1L, "Война и мир", "Лев Толстой", "9785171147440", 
                1869, 5, LocalDateTime.now(), LocalDateTime.now(), null),
            new Book(2L, "Преступление и наказание", "Федор Достоевский", "9785171147457", 
                1866, 3, LocalDateTime.now(), LocalDateTime.now(), null),
            new Book(3L, "Мастер и Маргарита", "Михаил Булгаков", "9785171147464", 
                1967, 4, LocalDateTime.now(), LocalDateTime.now(), null),
            new Book(4L, "1984", "Джордж Оруэлл", "9785171147471", 
                1949, 2, LocalDateTime.now(), LocalDateTime.now(), null)
        ));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public Optional<Book> findByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().contains(author))
                .collect(Collectors.toList());
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId((long) (books.size() + 1));
            books.add(book);
        } else {
            books.removeIf(b -> b.getId().equals(book.getId()));
            books.add(book);
        }
        return book;
    }

    public void deleteById(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }
} 