package com.sonora.guitars.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reviews")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele produsului este obligatoriu!")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Brandul este obligatoriu!")
    @Column(nullable = false)
    private String brand;

    @NotNull(message = "Prețul este obligatoriu!")
    @DecimalMin(value = "0.01", message = "Prețul trebuie să fie o valoare pozitivă!")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Cantitatea din stoc este obligatorie!")
    @Min(value = 0, message = "Stocul nu poate fi negativ!")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.startsWith("/images/")) {
            return "/images/fender_stratocaster.png";
        }
        return imageUrl;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Categoria este obligatorie!")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
