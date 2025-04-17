package io.github.dereklpm.javaassignment202504.repository;

import io.github.dereklpm.javaassignment202504.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByPublished(Boolean published);
    List<Book> findByAuthorAndPublished(String author, Boolean published);
}
