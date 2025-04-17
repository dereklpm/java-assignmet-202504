package io.github.dereklpm.javaassignment202504.service;

import io.github.dereklpm.javaassignment202504.dto.request.BookRequest;
import io.github.dereklpm.javaassignment202504.constants.BookMessage;
import io.github.dereklpm.javaassignment202504.entity.Book;
import io.github.dereklpm.javaassignment202504.repository.BookRepository;
import io.github.dereklpm.javaassignment202504.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks(String author, Boolean published) {
        if (author != null && published != null) {
            return bookRepository.findByAuthorAndPublished(author, published);
        } else if (author != null) {
            return bookRepository.findByAuthor(author);
        } else if (published != null) {
            return bookRepository.findByPublished(published);
        } else {
            return bookRepository.findAll();
        }
    }

    public Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublished(bookRequest.getPublished());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(BookMessage.BOOK_NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}
