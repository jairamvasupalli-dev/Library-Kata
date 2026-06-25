package com.example.library.controller;

import com.example.library.dto.AddBookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.dto.BorrowBookRequest;
import com.example.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponse> addBook(@RequestBody AddBookRequest request) {
        BookResponse response = bookService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/books/{id}/borrow")
    public ResponseEntity<BookResponse> borrowBook(@PathVariable Long id,
                                                   @RequestBody BorrowBookRequest request) {
        BookResponse response = bookService.borrowBook(id, request.getUserName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/books/{id}/return")
    public ResponseEntity<BookResponse> returnBook(@PathVariable Long id) {
        BookResponse response = bookService.returnBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userName}/books")
    public ResponseEntity<List<BookResponse>> getBorrowedBooks(@PathVariable String userName) {
        List<BookResponse> books = bookService.getBooksBorrowedByUser(userName);
        return ResponseEntity.ok(books);
    }
}
