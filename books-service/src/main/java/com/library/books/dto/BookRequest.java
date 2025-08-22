package com.library.books.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    @NotBlank  private String title;
    @NotBlank  private String author;
    @NotBlank  private String isbn;
    @Positive  private int totalCopies;
    private LocalDate publishedDate;
    private String category;
}