package com.library.issues.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponse {
    private Long id;
    private Long bookId;
    private Long userId;
    private String issuedAt;
    private String dueAt;
    private String returnedAt;
}