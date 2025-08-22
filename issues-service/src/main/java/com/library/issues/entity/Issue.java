package com.library.issues.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;   // foreign keys enforced at app-level
    private Long bookId;

    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;   // null = still borrowed
}
