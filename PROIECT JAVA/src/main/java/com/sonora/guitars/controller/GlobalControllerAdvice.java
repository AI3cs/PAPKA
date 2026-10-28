package com.sonora.guitars.controller;

import com.sonora.guitars.entity.User;
import com.sonora.guitars.service.CartService;
import com.sonora.guitars.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CartService cartService;
    private final UserService userService;

    public GlobalControllerAdvice(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @ModelAttribute("cartItemsCount")
    public int getCartItemsCount(Principal principal) {
        if (principal == null) {
            return 0;
        }
        try {
            return userService.findByUsername(principal.getName())
                    .map(user -> cartService.getCartItemsForUser(user).stream().mapToInt(com.sonora.guitars.entity.CartItem::getQuantity).sum())
                    .orElse(0);
        } catch (Exception e) {
            return 0;
        }
    }
}
