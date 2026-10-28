package com.sonora.guitars.controller;

import com.sonora.guitars.entity.Product;
import com.sonora.guitars.entity.Review;
import com.sonora.guitars.entity.User;
import com.sonora.guitars.service.ProductService;
import com.sonora.guitars.service.ReviewService;
import com.sonora.guitars.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService,
                            ProductService productService,
                            UserService userService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/add/{productId}")
    public String addReviewToProduct(@PathVariable("productId") Long productId,
                                     @Valid @ModelAttribute("newReview") Review review,
                                     BindingResult result,
                                     Principal principal,
                                     Model model) {
        
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produsul cu ID-ul " + productId + " nu există!"));

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu a fost găsit!"));

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("reviews", reviewService.getReviewsForProduct(productId));
            model.addAttribute("averageRating", reviewService.getAverageRatingForProduct(productId));
            model.addAttribute("newReview", review);
            return "product-detail";
        }

        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        
        reviewService.saveReview(review);

        return "redirect:/products/detail/" + productId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{reviewId}/product/{productId}")
    public String deleteReview(@PathVariable("reviewId") Long reviewId,
                               @PathVariable("productId") Long productId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/products/detail/" + productId;
    }
}
