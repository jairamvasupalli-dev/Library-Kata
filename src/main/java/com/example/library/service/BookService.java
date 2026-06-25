package com.example.library.service;

import com.example.library.dto.AddBookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse addBook(AddBookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setBorrowed(false);
        book.setBorrowedBy(null);

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public BookResponse borrowBook(Long id, String userName) {
        Book book = findBookOrThrow(id);

        if (book.isBorrowed()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Book is already borrowed with id: " + id);
        }

        book.setBorrowed(true);
        book.setBorrowedBy(userName);

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public BookResponse returnBook(Long id) {
        Book book = findBookOrThrow(id);

        book.setBorrowed(false);
        book.setBorrowedBy(null);

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public List<BookResponse> getBooksBorrowedByUser(String userName) {
        return bookRepository.findByBorrowedBy(userName).stream()
                .map(this::toResponse)
                .toList();
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book not found with id: " + id));
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .borrowed(book.isBorrowed())
                .borrowedBy(book.getBorrowedBy())
                .build();
    }
}
