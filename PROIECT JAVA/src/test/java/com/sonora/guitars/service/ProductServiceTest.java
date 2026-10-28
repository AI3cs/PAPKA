package com.sonora.guitars.service;

import com.sonora.guitars.entity.Category;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    public void setup() {
        testCategory = Category.builder()
                .id(1L)
                .name("Chitări Electrice")
                .description("Chitări electrice rock")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Player Stratocaster")
                .brand("Fender")
                .price(3500.0)
                .stockQuantity(5)
                .category(testCategory)
                .build();
    }

    @Test
    public void testFindById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> found = productService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Player Stratocaster", found.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product saved = productService.saveProduct(testProduct);

        assertNotNull(saved);
        assertEquals(3500.0, saved.getPrice());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    public void testSaveProduct_NegativePrice_ThrowsException() {
        testProduct.setPrice(-100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(testProduct);
        });

        assertEquals("Prețul produsului trebuie să fie strict pozitiv!", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testSaveProduct_NegativeStock_ThrowsException() {
        testProduct.setStockQuantity(-5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(testProduct);
        });

        assertEquals("Cantitatea din stoc nu poate fi negativă!", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testSearchProducts_Success() {
        when(productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase("Fender", "Fender"))
                .thenReturn(List.of(testProduct));

        List<Product> results = productService.searchProducts("Fender");

        assertEquals(1, results.size());
        assertEquals("Fender", results.get(0).getBrand());
    }
}
