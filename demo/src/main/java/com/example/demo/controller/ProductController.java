package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ===============================
    // DANH SÁCH
    // ===============================
    @GetMapping
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    // ===============================
    // CREATE FORM
    // ===============================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    // ===============================
    // CREATE
    // ===============================
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") Product newProduct,
                         BindingResult result,
                         @RequestParam("category.id") Long categoryId,
                         @RequestParam("imageProduct") MultipartFile imageProduct,
                         @RequestParam(value = "imageUrl", required = false) String imageUrl,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        // Ưu tiên upload file
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(newProduct, imageProduct);
        }
        // Nếu không có file nhưng có link
        else if (imageUrl != null && !imageUrl.isBlank()) {
            newProduct.setImage(imageUrl);
        }

        Category selectedCategory = categoryService.get(categoryId);
        newProduct.setCategory(selectedCategory);

        productService.add(newProduct);

        return "redirect:/products";
    }

    // ===============================
    // EDIT FORM
    // ===============================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        Product product = productService.get(id);

        if (product == null) {
            return "error/404";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());

        return "product/edit";
    }

    // ===============================
    // EDIT
    // ===============================
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("product") Product editProduct,
                       BindingResult result,
                       @RequestParam("imageProduct") MultipartFile imageProduct,
                       @RequestParam(value = "imageUrl", required = false) String imageUrl,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        Product oldProduct = productService.get(editProduct.getId());

        // Nếu upload file mới
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        }
        // Nếu nhập link mới
        else if (imageUrl != null && !imageUrl.isBlank()) {
            editProduct.setImage(imageUrl);
        }
        // Nếu không nhập gì → giữ ảnh cũ
        else {
            editProduct.setImage(oldProduct.getImage());
        }

        productService.update(editProduct);

        return "redirect:/products";
    }

    // ===============================
    // DELETE
    // ===============================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}