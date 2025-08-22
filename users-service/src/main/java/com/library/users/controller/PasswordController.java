package com.library.users.controller;

import com.library.users.dto.ResetPasswordRequest;
import com.library.users.entity.User;
import com.library.users.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final UserRepository userRepo;

    @PostMapping("/reset-password")
    public String reset(@Valid @RequestBody ResetPasswordRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        String tmp = UUID.randomUUID().toString().substring(0, 8);
        user.setPasswordHash(tmp);            // *real world: hash & email link*
        System.out.println("TEMP PASSWORD for " + user.getEmail() + ": " + tmp);
        return "Reset link sent (check console for now)";
    }
}
