package com.sonora.guitars.service;

import com.sonora.guitars.entity.Product;
import com.sonora.guitars.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Prețul produsului trebuie să fie strict pozitiv!");
        }
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Cantitatea din stoc nu poate fi negativă!");
        }
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl("/images/fender_stratocaster.png");
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Produsul cu ID-ul " + id + " nu a fost găsit!");
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String term) {
        if (term == null || term.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(term.trim(), term.trim());
    }
}
