package io.github.dereklpm.javaassignment202504.controller;

import io.github.dereklpm.javaassignment202504.constants.BookMessage;
import io.github.dereklpm.javaassignment202504.dto.ApiResponse;
import io.github.dereklpm.javaassignment202504.dto.request.BookRequest;
import io.github.dereklpm.javaassignment202504.dto.response.BookResponse;
import io.github.dereklpm.javaassignment202504.entity.Book;
import io.github.dereklpm.javaassignment202504.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Boolean published) {
        List<Book> books = bookService.getBooks(author, published);
        List<BookResponse> responses = books.stream()
                .map(book -> new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getPublished()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, responses, BookMessage.BOOK_FOUND_SUCCESS));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest bookRequest) {
        Book createdBook = bookService.createBook(bookRequest);
        BookResponse response = new BookResponse(createdBook.getId(), createdBook.getTitle(), createdBook.getAuthor(), createdBook.getPublished());
        return ResponseEntity.ok(new ApiResponse<>(true, response, BookMessage.BOOK_CREATED_SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, BookMessage.BOOK_DELETED_SUCCESS));
    }
}