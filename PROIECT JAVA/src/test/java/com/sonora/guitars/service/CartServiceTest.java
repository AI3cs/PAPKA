package com.sonora.guitars.service;

import com.sonora.guitars.entity.CartItem;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.repository.CartItemRepository;
import com.sonora.guitars.repository.OrderRepository;
import com.sonora.guitars.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).username("testuser").build();
        testProduct = Product.builder().id(1L).name("Test Guitar").price(1000.0).stockQuantity(5).build();
    }

    @Test
    void testAddProductToCart_SuccessNewItem() {
        when(cartItemRepository.findByUserAndProduct(testUser, testProduct)).thenReturn(Optional.empty());

        cartService.addProductToCart(testUser, testProduct);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddProductToCart_OutOfStock_ThrowsException() {
        testProduct.setStockQuantity(0);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cartService.addProductToCart(testUser, testProduct);
        });

        assertTrue(exception.getMessage().contains("nu mai este în stoc"));
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void testCheckout_EmptyCart_ThrowsException() {
        when(cartItemRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cartService.checkout(testUser);
        });

        assertTrue(exception.getMessage().contains("Coșul de cumpărături este gol"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCheckout_Success() {
        CartItem item = CartItem.builder().user(testUser).product(testProduct).quantity(2).build();
        when(cartItemRepository.findByUser(testUser)).thenReturn(List.of(item));

        cartService.checkout(testUser);

        // Verify stock is decreased
        assertEquals(3, testProduct.getStockQuantity());
        verify(productRepository, times(1)).save(testProduct);
        verify(orderRepository, times(1)).save(any());
        verify(cartItemRepository, times(1)).deleteByUser(testUser);
    }
}
