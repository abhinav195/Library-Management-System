package com.library.users.dto;

import java.util.Set;

public record JwtResponse(String token,
                          long   expiresInMs,
                          String username,
                          Set<String> roles) {}
