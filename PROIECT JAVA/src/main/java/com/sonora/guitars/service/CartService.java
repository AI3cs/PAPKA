package com.sonora.guitars.service;

import com.sonora.guitars.entity.CartItem;
import com.sonora.guitars.entity.Order;
import com.sonora.guitars.entity.OrderItem;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.repository.CartItemRepository;
import com.sonora.guitars.repository.OrderRepository;
import com.sonora.guitars.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<CartItem> getCartItemsForUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public void addProductToCart(User user, Product product) {
        if (product.getStockQuantity() <= 0) {
            throw new IllegalStateException("Produsul " + product.getName() + " nu mai este în stoc!");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            if (cartItem.getQuantity() >= product.getStockQuantity()) {
                throw new IllegalStateException("Nu poți adăuga mai mult decât cantitatea disponibilă în stoc!");
            }
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(1)
                    .build();
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeProductFromCart(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Articolul nu a fost găsit în coș."));
        
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Nu ai permisiunea să ștergi acest articol.");
        }
        
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void checkout(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        if (items.isEmpty()) {
            throw new IllegalStateException("Coșul de cumpărături este gol!");
        }

        double totalAmount = 0;
        Order order = Order.builder()
                .user(user)
                .orderDate(java.time.LocalDateTime.now())
                .totalAmount(0.0)
                .build();

        for (CartItem item : items) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Stoc insuficient pentru produsul: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
            order.getItems().add(orderItem);
            totalAmount += product.getPrice() * item.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
    }
}
