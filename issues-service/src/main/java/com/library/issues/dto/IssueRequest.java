package com.library.issues.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {
    @NotNull private Long bookId;
}