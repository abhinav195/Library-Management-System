package com.library.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Getter @Setter                     // Lombok → all getters & setters
@NoArgsConstructor                  // JPA needs a no-arg ctor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String name;

    @Column(unique = true) @NotBlank
    private String username;

    @Email @Column(unique = true) @NotBlank
    private String email;

    private int penaltyPoints;

    @NotBlank
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();     // JPA
    public User(String name, String username,
                String email, String passwordHash) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    // getters & setters …
}
