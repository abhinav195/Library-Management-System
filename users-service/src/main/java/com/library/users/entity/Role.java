package com.library.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private RoleName name;
}
