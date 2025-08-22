package com.library.users.controller;

import com.library.users.dto.*;
import com.library.users.entity.User;
import com.library.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /* ---------- SELF OPERATIONS ---------- */

    @GetMapping("/me")
    public User me(Authentication auth) {   // simple echo
        return service.getByUsername(auth.getName());
    }

    @PutMapping("/me")
    public User updateMe(Authentication auth,
                         @Valid @RequestBody UpdateProfileRequest req) {
        return service.updateProfile(auth.getName(), req);
    }

    @PostMapping("/me/change-password")
    public String changePassword(Authentication auth,
                                 @Valid @RequestBody ChangePasswordRequest req) {
        service.changePassword(auth.getName(), req);
        return "Password updated";
    }

    /* ---------- ADMIN OPERATIONS ---------- */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listAll() {
        return service.listAll();
    }

    @PutMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public String promote(@PathVariable Long id) {
        service.promoteToAdmin(id);
        return "User promoted";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.deleteUser(id);
        return "User deleted";
    }
}
