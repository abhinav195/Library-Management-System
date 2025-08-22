package com.library.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank String username,
        @Email String email,
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$",
                message = "Password must be 8+ chars, incl. 1 number & 1 special")
        String password
) {}
