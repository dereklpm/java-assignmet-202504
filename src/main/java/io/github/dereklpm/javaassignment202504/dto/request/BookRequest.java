package io.github.dereklpm.javaassignment202504.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static io.github.dereklpm.javaassignment202504.constants.BookMessage.MUST_BE_ALPHANUMERIC;
import static io.github.dereklpm.javaassignment202504.constants.CommonRegex.ALPHANUMERIC;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Title is required.")
    @Pattern(regexp = ALPHANUMERIC, message = "Title " + MUST_BE_ALPHANUMERIC)
    private String title;

    @NotBlank(message = "Author is required.")
    @Pattern(regexp = ALPHANUMERIC, message = "Author " + MUST_BE_ALPHANUMERIC)
    private String author;

    @NotNull(message = "Published status is required.")
    private Boolean published;
}
