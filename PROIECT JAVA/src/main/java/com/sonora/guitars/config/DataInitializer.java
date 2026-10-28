package com.sonora.guitars.config;

import com.sonora.guitars.entity.*;
import com.sonora.guitars.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           ReviewRepository reviewRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User adminUser;
        User editorUser;
        User viewerUser;

        if (userRepository.count() == 0) {
            adminUser = User.builder()
                    .username("admin")
                    .email("admin@sonoraguitars.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            editorUser = User.builder()
                    .username("editor")
                    .email("editor@sonoraguitars.com")
                    .password(passwordEncoder.encode("editor123"))
                    .role(Role.EDITOR)
                    .build();

            viewerUser = User.builder()
                    .username("viewer")
                    .email("viewer@sonoraguitars.com")
                    .password(passwordEncoder.encode("viewer123"))
                    .role(Role.VIEWER)
                    .build();

            userRepository.saveAll(List.of(adminUser, editorUser, viewerUser));
        } else {
            adminUser = userRepository.findByUsername("admin").orElse(null);
            editorUser = userRepository.findByUsername("editor").orElse(null);
            viewerUser = userRepository.findByUsername("viewer").orElse(null);
        }

        Category acoustic;
        Category electric;
        Category bass;
        Category amps;

        if (categoryRepository.count() == 0) {
            acoustic = Category.builder()
                    .name("Chitari Acustice")
                    .description("Chitari acustice și electro-acustice premium, ideale pentru studio, folk și muzică clasică.")
                    .build();

            electric = Category.builder()
                    .name("Chitari Electrice")
                    .description("Chitari electrice iconice pentru rock, metal, jazz și blues. Tonuri versatile și hardware de ultimă generație.")
                    .build();

            bass = Category.builder()
                    .name("Chitari Bass")
                    .description("Bass-uri cu 4 și 5 corzi, active sau pasive, care oferă frecvențele joase ideale pentru orice trupă.")
                    .build();

            amps = Category.builder()
                    .name("Amplificatoare & Efecte")
                    .description("Amplificatoare pe lămpi, combo-uri de tranzistori și pedale de efecte pentru personalizarea tonului perfect.")
                    .build();

            categoryRepository.saveAll(List.of(acoustic, electric, bass, amps));
        } else {
            acoustic = categoryRepository.findByName("Chitari Acustice").orElse(null);
            electric = categoryRepository.findByName("Chitari Electrice").orElse(null);
            bass = categoryRepository.findByName("Chitari Bass").orElse(null);
            amps = categoryRepository.findByName("Amplificatoare & Efecte").orElse(null);
        }

        Product p1 = null;
        Product p2 = null;
        Product p3 = null;

        if (productRepository.count() == 0 && acoustic != null && electric != null && bass != null && amps != null) {
            p1 = Product.builder()
                    .name("Player Stratocaster Maple")
                    .brand("Fender")
                    .price(3899.99)
                    .stockQuantity(12)
                    .description("Sunetul inspirator al unui Stratocaster este unul dintre elementele de bază ale Fender. Oferind acest sunet clasic - înalte clare, medii puternice și joase robuste - modelul Fender Player Stratocaster este plin de stil și ton original Fender.")
                    .imageUrl("/images/fender_stratocaster.png")
                    .category(electric)
                    .build();

            p2 = Product.builder()
                    .name("Les Paul Standard '60s")
                    .brand("Gibson")
                    .price(11999.00)
                    .stockQuantity(2)
                    .description("Modelul Les Paul Standard 60s se întoarce la designul clasic care a făcut-o celebră în mâinile marilor legende rock. Dispune de un corp din mahon cu top din arțar figurat AA și doze speciale BurstBucker 61.")
                    .imageUrl("/images/gibson_les_paul.png")
                    .category(electric)
                    .build();

            p3 = Product.builder()
                    .name("114ce Grand Auditorium")
                    .brand("Taylor")
                    .price(4799.00)
                    .stockQuantity(8)
                    .description("Taylor 114ce este o chitară electro-acustică cu corp de tip Grand Auditorium, extrem de versatilă. Dispune de un top din molid Sitka masiv și spate/laterale din nuc stratificat.")
                    .imageUrl("/images/taylor_acoustic.png")
                    .category(acoustic)
                    .build();

            Product p4 = Product.builder()
                    .name("SR300E-IPT Metallic")
                    .brand("Ibanez")
                    .price(1649.99)
                    .stockQuantity(15)
                    .description("Bass-urile Ibanez din seria Soundgear sunt cunoscute pentru corpul lor zvelt și gâtul subțire, extrem de comod. Modelul SR300E dispune de doze PowerSpan Dual Coil și egalizator activ cu 3 benzi.")
                    .imageUrl("/images/ibanez_bass.png")
                    .category(bass)
                    .build();

            Product p5 = Product.builder()
                    .name("DSL40CR Tube Combo")
                    .brand("Marshall")
                    .price(3999.00)
                    .stockQuantity(5)
                    .description("Amplificator combo pe lămpi cu o putere de 40W, perfect pentru repetiții, cluburi și studio. Dispune de două canale selectabile, reverb digital integrat și lămpi EL34.")
                    .imageUrl("/images/marshall_amp.png")
                    .category(amps)
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        }

        if (reviewRepository.count() == 0 && viewerUser != null && editorUser != null && p1 != null && p2 != null && p3 != null) {
            Review r1 = Review.builder()
                    .rating(5)
                    .comment("Fender Stratocaster este absolut genial! Gâtul cu finisaj satinat alunecă extrem de ușor, iar dozele single-coil scot exact acel sunet cristalin specific. Recomand cu încredere!")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .product(p1)
                    .user(viewerUser)
                    .build();

            Review r2 = Review.builder()
                    .rating(4)
                    .comment("Gibson Les Paul este un instrument legendar. Sunetul este cald, sustain-ul este excelent, ideal pentru solo-uri de blues rock.")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .product(p2)
                    .user(editorUser)
                    .build();

            Review r3 = Review.builder()
                    .rating(5)
                    .comment("Taylor oferă o claritate acustică incredibilă. O chitară superbă pentru orice chitarist acustic!")
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .product(p3)
                    .user(viewerUser)
                    .build();

            reviewRepository.saveAll(List.of(r1, r2, r3));
        }
    }
}
