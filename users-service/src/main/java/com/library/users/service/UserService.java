package com.library.users.service;

import com.library.users.dto.*;
import com.library.users.entity.*;
import com.library.users.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public User getByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public User updateProfile(String username, UpdateProfileRequest req) {
        var user = getByUsername(username);
        user.setName(req.getName());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        return user;
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest req) {
        var user = getByUsername(username);
        if (!encoder.matches(req.getOldPassword(), user.getPasswordHash()))
            throw new IllegalArgumentException("Old password incorrect");
        user.setPasswordHash(encoder.encode(req.getNewPassword()));
    }

    @Transactional
    public void promoteToAdmin(Long id) {
        var user = userRepo.findById(id).orElseThrow();
        user.getRoles().add(roleRepo.getReferenceById(RoleName.ADMIN));
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> listAll() {
        return userRepo.findAll();
    }
}
