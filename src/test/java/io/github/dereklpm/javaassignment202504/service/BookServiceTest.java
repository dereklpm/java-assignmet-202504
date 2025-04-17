package io.github.dereklpm.javaassignment202504.service;

import io.github.dereklpm.javaassignment202504.entity.Book;
import io.github.dereklpm.javaassignment202504.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    // --- Helper methods ---

    private Book createBook(Long id, String title, String author, Boolean published) {
        Book book = new Book(title, author, published);
        book.setId(id);
        return book;
    }

    private void verifyBooks(List<Book> actualBooks, List<Book> expectedBooks) {
        assertEquals(expectedBooks.size(), actualBooks.size(), "Book count mismatch");

        for (Book expectedBook : expectedBooks) {
            boolean matchFound = actualBooks.stream().anyMatch(actualBook ->
                    actualBook.getId().equals(expectedBook.getId()) &&
                            actualBook.getTitle().equals(expectedBook.getTitle()) &&
                            actualBook.getAuthor().equals(expectedBook.getAuthor()) &&
                            actualBook.getPublished().equals(expectedBook.getPublished())
            );
            assertTrue(matchFound, "Expected book not found: " + expectedBook);
        }
    }

    // --- GET Test ---

    @Test
    @DisplayName("Get all books - Success")
    void testGetBooksSuccess() {
        List<Book> originalBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false),
                createBook(3L, "Microservices", "John Doe", true)
        );

        when(bookRepository.findAll()).thenReturn(originalBooks);

        List<Book> actualBooks = bookService.getBooks(null, null);

        verifyBooks(actualBooks, originalBooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get books filtered by author - Success")
    void testGetBooksFilteredByAuthor() {
        List<Book> originalBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false),
                createBook(3L, "Microservices", "John Doe", true)
        );

        List<Book> expectedBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(3L, "Microservices", "John Doe", true)
        );

        when(bookRepository.findByAuthor("John Doe")).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getBooks("John Doe", null);

        verifyBooks(actualBooks, expectedBooks);
        verify(bookRepository, times(1)).findByAuthor("John Doe");
    }

    @Test
    @DisplayName("Get books filtered by published status - Success")
    void testGetBooksFilteredByPublished() {
        List<Book> originalBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false),
                createBook(3L, "Microservices", "John Doe", true),
                createBook(4L, "Advanced Java", "Jane Smith", false)
        );

        List<Book> expectedBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(3L, "Microservices", "John Doe", true)
        );

        when(bookRepository.findByPublished(true)).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getBooks(null, true);

        verifyBooks(actualBooks, expectedBooks);
        verify(bookRepository, times(1)).findByPublished(true);
    }

    @Test
    @DisplayName("Get books filtered by author and published status - Success")
    void testGetBooksFilteredByAuthorAndPublished() {
        List<Book> originalBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false),
                createBook(3L, "Microservices", "John Doe", true),
                createBook(4L, "Advanced Java", "Jane Smith", false)
        );

        List<Book> expectedBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(3L, "Microservices", "John Doe", true)
        );

        when(bookRepository.findByAuthorAndPublished("John Doe", true)).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getBooks("John Doe", true);

        verifyBooks(actualBooks, expectedBooks);
        verify(bookRepository, times(1)).findByAuthorAndPublished("John Doe", true);
    }

    @Test
    @DisplayName("Get books - Empty list")
    void testGetBooksEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> actualBooks = bookService.getBooks(null, null);

        assertTrue(actualBooks.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }
}