package com.library.users.security;

import com.github.benmanes.caffeine.cache.Cache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService uds;     // or UserDetailsService
    private final Cache<String, Boolean> jwtBlacklist;   // ← add this

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        String token  = (StringUtils.hasText(header) && header.startsWith("Bearer "))
                ? header.substring(7) : null;

        // skip if black-listed
        if (token != null && jwtBlacklist.getIfPresent(token) == null) {
            try {
                Claims claims   = jwtUtil.parse(token).getBody();
                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                var auth = new UsernamePasswordAuthenticationToken(
                        uds.loadUserByUsername(username),
                        null,
                        roles.stream().map(r -> "ROLE_" + r)
                                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()));

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ignored) { /* bad or expired token */ }
        }
        chain.doFilter(req, res);
    }
}
