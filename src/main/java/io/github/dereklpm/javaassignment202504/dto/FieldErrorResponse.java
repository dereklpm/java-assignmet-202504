package io.github.dereklpm.javaassignment202504.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class FieldErrorResponse {
    private String field;
    private String message;
}