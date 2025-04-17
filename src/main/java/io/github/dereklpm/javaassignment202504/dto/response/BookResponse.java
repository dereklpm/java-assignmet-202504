package io.github.dereklpm.javaassignment202504.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private Boolean published;
}