package com.sonora.guitars.controller;

import com.sonora.guitars.entity.Category;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.service.CategoryService;
import com.sonora.guitars.service.ProductService;
import com.sonora.guitars.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.context.annotation.Import;

@WebMvcTest(ProductController.class)
@Import(com.sonora.guitars.config.SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private com.sonora.guitars.service.CartService cartService;

    @MockBean
    private com.sonora.guitars.service.UserService userService;

    @MockBean
    private com.sonora.guitars.repository.OrderRepository orderRepository;

    @MockBean
    private com.sonora.guitars.config.CustomUserDetailsService customUserDetailsService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    public void setup() {
        testCategory = Category.builder()
                .id(1L)
                .name("Chitări Electrice")
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
    @WithMockUser(roles = "VIEWER")
    public void testGetProducts_AuthorizedForViewer() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(testProduct));
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    public void testAddProductPage_AccessDeniedForViewer() throws Exception {
        mockMvc.perform(get("/products/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    public void testAddProductPage_AccessGrantedForEditor() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory));

        mockMvc.perform(get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeExists("product"));
    }
}
