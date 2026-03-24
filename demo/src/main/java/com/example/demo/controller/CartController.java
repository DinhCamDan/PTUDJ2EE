package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ FIX ở đây
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // =========================
    // HIỂN THỊ GIỎ HÀNG
    // =========================
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart/view";
    }

    // =========================
    // THÊM SẢN PHẨM
    // =========================
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long productId,
                            @RequestParam(defaultValue = "1") int quantity) {

        Product product = productService.get(productId);

        if (product != null) {
            CartItem item = new CartItem();
            item.setId(product.getId().intValue());
            item.setName(product.getName());
            item.setImage(product.getImage());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);

            cartService.addToCart(item);
        }

        return "redirect:/cart";
    }

    // =========================
    // CẬP NHẬT SỐ LƯỢNG
    // =========================
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable("id") int productId,
                                 @RequestParam("quantity") int quantity) {

        if (quantity <= 0) {
            cartService.removeFromCart(productId);
        } else {
            cartService.updateQuantity(productId, quantity);
        }

        return "redirect:/cart";
    }

    // =========================
    // XÓA 1 SẢN PHẨM
    // =========================
    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") int productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    // =========================
    // XÓA TOÀN BỘ
    // =========================
    @PostMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }
}