package com.sonora.guitars.service;

import com.sonora.guitars.entity.Role;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Numele de utilizator '" + user.getUsername() + "' este deja folosit!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Adresa de email '" + user.getEmail() + "' este deja înregistrată!");
        }

        String password = user.getPassword();
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Parola trebuie să aibă cel puțin 6 caractere!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.VIEWER);

        return userRepository.save(user);
    }
}
