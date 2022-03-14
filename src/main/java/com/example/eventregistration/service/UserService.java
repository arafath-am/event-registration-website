package com.example.eventregistration.service;

import com.example.eventregistration.dto.UserRegistrationDto;
import com.example.eventregistration.exception.BusinessRuleException;
import com.example.eventregistration.exception.ResourceNotFoundException;
import com.example.eventregistration.model.AppUser;
import com.example.eventregistration.model.Role;
import com.example.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AppUser registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("An account already exists with this email.");
        }

        AppUser user = AppUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }
}
