package com.library.users.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateProfileRequest {
    @NotBlank  private String name;
    @NotBlank  private String username;
    @Email     private String email;
}