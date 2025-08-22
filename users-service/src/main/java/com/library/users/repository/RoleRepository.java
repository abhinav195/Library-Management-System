package com.library.users.repository;

import com.library.users.entity.Role;
import com.library.users.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleName> {}
