package com.sonora.guitars.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reviews")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele de utilizator este obligatoriu!")
    @Size(min = 4, max = 20, message = "Numele de utilizator trebuie să aibă între 4 și 20 de caractere!")
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @NotBlank(message = "Adresa de email este obligatorie!")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Adresa de email trebuie să conțină un domeniu valid (ex: .com, .ro)!")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Parola este obligatorie!")
    @Size(min = 6, message = "Parola trebuie să aibă minimum 6 caractere!")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.VIEWER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Review> reviews = new ArrayList<>();
}
