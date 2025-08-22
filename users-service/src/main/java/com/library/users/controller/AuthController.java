package com.library.users.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.library.users.dto.*;
import com.library.users.entity.*;
import com.library.users.repository.*;
import com.library.users.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    private final Cache<String, Boolean> jwtBlacklist;

    public AuthController(UserRepository userRepo,
                          RoleRepository roleRepo,
                          PasswordEncoder encoder,
                          AuthenticationManager authManager,
                          JwtUtil jwtUtil, Cache<String, Boolean> jwtBlacklist) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklist = jwtBlacklist;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        if (userRepo.existsByEmail(req.email()))
            return ResponseEntity.badRequest().body("Email already in use");

        var user = new User(
                req.name(),
                req.username(),
                req.email(),
                encoder.encode(req.password()));
        user.getRoles().add(roleRepo.getReferenceById(RoleName.USER));
        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        var user = userRepo.findByUsername(req.username()).orElseThrow();
        var roleNames = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());

        String token = jwtUtil.generate(user.getUsername(), roleNames);
        return new JwtResponse(token,
                Long.parseLong(System.getProperty("security.jwt.expiration-ms",
                        "900000")),
                user.getUsername(),
                roleNames);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            jwtBlacklist.put(token, Boolean.TRUE);   // mark as blacklisted
        }
        return ResponseEntity.ok("Logged out");
    }
}
