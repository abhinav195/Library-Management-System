package com.library.books.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @Column(unique = true)
    private String isbn;

    @Positive
    private int totalCopies;
    @PositiveOrZero
    private int copiesAvailable;

    private LocalDate publishedDate;
    private String category;
}
