package com.sonora.guitars.service;

import com.sonora.guitars.entity.Role;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("StrongPass1!")
                .build();
    }

    @Test
    void testRegisterNewUser_Success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass1!")).thenReturn("encodedPassword");
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        User savedUser = userService.registerNewUser(validUser);

        assertNotNull(savedUser.getId());
        assertEquals(Role.VIEWER, savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(validUser);
    }

    @Test
    void testRegisterNewUser_DuplicateUsername_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(validUser);
        });

        assertTrue(exception.getMessage().contains("este deja folosit"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterNewUser_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(validUser);
        });

        assertTrue(exception.getMessage().contains("este deja înregistrată"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterNewUser_WeakPassword_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        validUser.setPassword("123");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(validUser);
        });

        assertTrue(exception.getMessage().contains("Parola trebuie să aibă cel puțin 6 caractere"));
        verify(userRepository, never()).save(any());
    }
}
