package com.sonora.guitars.controller;

import com.sonora.guitars.entity.CartItem;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.service.CartService;
import com.sonora.guitars.service.ProductService;
import com.sonora.guitars.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartService cartService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String viewCart(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Utilizatorul nu a fost găsit!"));

        List<CartItem> cartItems = cartService.getCartItemsForUser(user);
        
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String addToCart(@PathVariable Long productId,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilizatorul nu a fost găsit!"));
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Produsul nu există!"));

            cartService.addProductToCart(user, product);
            redirectAttributes.addFlashAttribute("successMessage", "Produs adăugat în coș cu succes!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/remove/{cartItemId}")
    @PreAuthorize("isAuthenticated()")
    public String removeFromCart(@PathVariable Long cartItemId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilizatorul nu a fost găsit!"));
            cartService.removeProductFromCart(cartItemId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Produs eliminat din coș!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public String checkout(Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilizatorul nu a fost găsit!"));
            cartService.checkout(user);
            redirectAttributes.addFlashAttribute("successMessage", "Comanda a fost plasată cu succes! Vă mulțumim pentru achiziție!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }
}
