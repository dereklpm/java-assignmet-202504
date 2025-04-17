package io.github.dereklpm.javaassignment202504.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dereklpm.javaassignment202504.constants.BookMessage;
import io.github.dereklpm.javaassignment202504.dto.request.BookRequest;
import io.github.dereklpm.javaassignment202504.entity.Book;
import io.github.dereklpm.javaassignment202504.exception.BookNotFoundException;
import io.github.dereklpm.javaassignment202504.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private static final String BASE_URL = "/api/books";

    // --- Helper method ---

    private Book createBook(Long id, String title, String author, Boolean published) {
        Book book = new Book(title, author, published);
        book.setId(id);
        return book;
    }

    private void verifyBooksResponse(String responseContent, List<Book> expectedBooks) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> responseMap = objectMapper.readValue(responseContent, Map.class);
        List<Map<String, Object>> actualBooks = (List<Map<String, Object>>) responseMap.get("data");

        assertEquals(expectedBooks.size(), actualBooks.size(), "Book count mismatch");

        for (Book expectedBook : expectedBooks) {
            boolean matchFound = actualBooks.stream().anyMatch(actualBook ->
                    actualBook.get("id").equals(expectedBook.getId().intValue()) &&
                            actualBook.get("title").equals(expectedBook.getTitle()) &&
                            actualBook.get("author").equals(expectedBook.getAuthor()) &&
                            actualBook.get("published").equals(expectedBook.getPublished())
            );

            assertTrue(matchFound, "Expected book not found in response: " + expectedBook);
        }
    }

    // --- GET Test ---

    @Test
    @DisplayName("Get all books - Success")
    void testGetBooks() throws Exception {
        List<Book> expectedBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false)
        );

        when(bookService.getBooks(null, null)).thenReturn(expectedBooks);

        String responseContent = mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_FOUND_SUCCESS))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verifyBooksResponse(responseContent, expectedBooks);

        verify(bookService).getBooks(null, null);
    }

    @Test
    @DisplayName("Get books filtered by author - Success")
    void testGetBooksFilteredByAuthor() throws Exception {

        List<Book> originalBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(2L, "Java Basics", "Jane Smith", false),
                createBook(3L, "Microservices", "John Doe", true)
        );

        List<Book> expectedBooks = Arrays.asList(
                createBook(1L, "Spring Boot", "John Doe", true),
                createBook(3L, "Microservices", "John Doe", true)
        );

        when(bookService.getBooks("John Doe", null))
                .thenAnswer(invocation -> {
                    String author = invocation.getArgument(0);
                    return originalBooks.stream()
                            .filter(book -> book.getAuthor().equals(author))
                            .collect(Collectors.toList());
                }).thenReturn(expectedBooks);

        String responseContent = mockMvc.perform(get(BASE_URL + "?author=John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_FOUND_SUCCESS))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verifyBooksResponse(responseContent, expectedBooks);

        verify(bookService).getBooks("John Doe", null);
    }

    @Test
    @DisplayName("Get books filtered by published status - Success")
    void testGetBooksFilteredByPublished() throws Exception {
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

        when(bookService.getBooks(null, true))
                .thenAnswer(invocation -> {
                    Boolean published = invocation.getArgument(1);
                    return originalBooks.stream()
                            .filter(book -> book.getPublished().equals(published))
                            .collect(Collectors.toList());
                }).thenReturn(expectedBooks);

        String responseContent = mockMvc.perform(get(BASE_URL + "?published=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_FOUND_SUCCESS))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verifyBooksResponse(responseContent, expectedBooks);

        verify(bookService).getBooks(null, true);
    }

    @Test
    @DisplayName("Get books filtered by author and published status - Success")
    void testGetBooksFilteredByAuthorAndPublished() throws Exception {
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

        when(bookService.getBooks("John Doe", true))
                .thenAnswer(invocation -> {
                    String author = invocation.getArgument(0);
                    Boolean published = invocation.getArgument(1);
                    return originalBooks.stream()
                            .filter(book -> book.getAuthor().equals(author) && book.getPublished().equals(published))
                            .collect(Collectors.toList());
                }).thenReturn(expectedBooks);

        String responseContent = mockMvc.perform(get(BASE_URL + "?author=John Doe&published=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_FOUND_SUCCESS))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verifyBooksResponse(responseContent, expectedBooks);

        verify(bookService).getBooks("John Doe", true);
    }

    @Test
    @DisplayName("Get books - Unexpected Error")
    void testGetBooksUnexpectedError() throws Exception {
        when(bookService.getBooks(null, null)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred: Unexpected error"));

        verify(bookService).getBooks(null, null);
    }

    // --- POST Test ---

    @Test
    @DisplayName("Create book - Success")
    void testCreateBook() throws Exception {
        Book createdBook = createBook(1L, "Spring Boot", "John Doe", true);

        when(bookService.createBook(any(BookRequest.class))).thenReturn(createdBook);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Spring Boot\",\"author\":\"John Doe\",\"published\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Spring Boot"))
                .andExpect(jsonPath("$.data.author").value("John Doe"))
                .andExpect(jsonPath("$.data.published").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_CREATED_SUCCESS));

        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    @DisplayName("Create book - Validation Errors")
    void testCreateBookValidationErrors() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"author\":\"\",\"published\":true}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].field").value(org.hamcrest.Matchers.containsInAnyOrder("title", "author")))
                .andExpect(jsonPath("$.data[*].message").value(org.hamcrest.Matchers.containsInAnyOrder(
                        "Title is required.",
                        "Author is required."
                )));
    }

    // --- DELETE Test ---

    @Test
    @DisplayName("Delete book - Success")
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_DELETED_SUCCESS));

        verify(bookService).deleteBook(1L);
    }

    @Test
    @DisplayName("Delete book - Not Found")
    void testDeleteBookNotFound() throws Exception {
        doThrow(new BookNotFoundException(BookMessage.BOOK_NOT_FOUND)).when(bookService).deleteBook(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(BookMessage.BOOK_NOT_FOUND));

        verify(bookService).deleteBook(1L);
    }
}