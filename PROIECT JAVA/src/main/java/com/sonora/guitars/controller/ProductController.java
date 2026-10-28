package com.sonora.guitars.controller;

import com.sonora.guitars.entity.Order;
import com.sonora.guitars.entity.Product;
import com.sonora.guitars.entity.Review;
import com.sonora.guitars.repository.OrderRepository;
import com.sonora.guitars.service.CategoryService;
import com.sonora.guitars.service.ProductService;
import com.sonora.guitars.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final OrderRepository orderRepository;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             ReviewService reviewService,
                             OrderRepository orderRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        long totalProducts = products.size();
        int totalUnits = products.stream().mapToInt(Product::getStockQuantity).sum();

        List<Order> allOrders = orderRepository.findAll();
        double totalRevenue = allOrders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = allOrders.size();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalUnits", totalUnits);
        model.addAttribute("recentProducts", products.stream().limit(3).toList());
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);

        return "dashboard";
    }

    @GetMapping("/products")
    public String showProductsList(@RequestParam(value = "search", required = false) String search,
                                   Model model) {
        List<Product> products = productService.searchProducts(search);
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", search);
        return "products";
    }

    @GetMapping("/products/detail/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produsul cu ID-ul " + id + " nu există!"));

        model.addAttribute("product", product);
        return "product-detail";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product-form";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produsul cu ID-ul " + id + " nu există!"));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
