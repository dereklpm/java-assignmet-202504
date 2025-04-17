package io.github.dereklpm.javaassignment202504.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.time.LocalDateTime;

import static io.github.dereklpm.javaassignment202504.constants.BookMessage.MUST_BE_ALPHANUMERIC;
import static io.github.dereklpm.javaassignment202504.constants.CommonRegex.ALPHANUMERIC;

@Entity
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required.")
    @Pattern(regexp = ALPHANUMERIC, message = "Title " + MUST_BE_ALPHANUMERIC)
    private String title;

    @NotBlank(message = "Author is required.")
    @Pattern(regexp = ALPHANUMERIC, message = "Author " + MUST_BE_ALPHANUMERIC)
    private String author;

    private Boolean published;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;

    public Book(String title, String author, Boolean published) {
        this.title = title;
        this.author = author;
        this.published = published;
    }
}