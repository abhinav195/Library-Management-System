package com.library.users.controller;

import com.library.users.entity.User;
import com.library.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserExtrasController {

    private final UserRepository repo;

    /** Return numeric id for Feign client */
    @GetMapping("/{username}/id")
    public Long id(@PathVariable String username) {
        return repo.findByUsername(username)
                .map(u -> u.getId())
                .orElseThrow();
    }
    @GetMapping("/{username}/penalty")
    public int penalty(@PathVariable String username) {
        return repo.findByUsername(username).map(User::getPenaltyPoints).orElse(0);
    }
    @PutMapping("/{username}/penalty/increment")
    public void addPenalty(@PathVariable String username) {
        repo.findByUsername(username).ifPresent(u -> {
            if (u.getPenaltyPoints() < 5)
                u.setPenaltyPoints(u.getPenaltyPoints() + 1);
        });
    }
}
