package com.library.service;

import com.library.dto.BookHistoryReport;
import com.library.dto.BookMovementAction;
import com.library.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookHistoryService {
    private final List<BookMovementAction> bookMovements = new ArrayList<>();

    public void logMovement(Book book, String username, String actionType, 
            LocalDateTime dueDate, LocalDateTime returnDate) {
        bookMovements.add(BookMovementAction.builder()
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .username(username)
                .actionType(actionType)
                .actionDate(LocalDateTime.now())
                .dueDate(dueDate)
                .returnDate(returnDate)
                .build());
    }

    public BookHistoryReport generateBookHistory(Long bookId) {
        List<BookMovementAction> bookActions = bookMovements.stream()
                .filter(action -> action.getBookId().equals(bookId))
                .sorted(Comparator.comparing(BookMovementAction::getActionDate))
                .collect(Collectors.toList());

        if (bookActions.isEmpty()) {
            throw new RuntimeException("История движения книги не найдена");
        }

        String currentHolder = null;
        LocalDateTime currentDueDate = null;
        int delays = 0;

        for (BookMovementAction action : bookActions) {
            if (action.getActionType().equals("BORROWED")) {
                currentHolder = action.getUsername();
                currentDueDate = action.getDueDate();
                if (action.getReturnDate() != null && 
                    action.getReturnDate().isAfter(action.getDueDate())) {
                    delays++;
                }
            } else if (action.getActionType().equals("RETURNED")) {
                currentHolder = null;
                currentDueDate = null;
            }
        }

        return BookHistoryReport.builder()
                .bookId(bookId)
                .bookTitle(bookActions.get(0).getBookTitle())
                .movements(bookActions)
                .totalBorrowings(countBorrowings(bookActions))
                .totalDelays(delays)
                .currentHolder(currentHolder)
                .currentDueDate(currentDueDate)
                .build();
    }

    private int countBorrowings(List<BookMovementAction> actions) {
        return (int) actions.stream()
                .filter(action -> action.getActionType().equals("BORROWED"))
                .count();
    }

    public List<BookMovementAction> getAllMovements() {
        return new ArrayList<>(bookMovements);
    }
}