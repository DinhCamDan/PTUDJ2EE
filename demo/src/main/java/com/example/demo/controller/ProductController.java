package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    // DANH SÁCH + SEARCH + FILTER + SORT + PAGINATION
    // ===============================
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // SORT
        Sort sortOption = sort.equals("desc")
                ? Sort.by("price").descending()
                : Sort.by("price").ascending();

        Pageable pageable = PageRequest.of(page, 6, sortOption);

        Page<Product> productPage;

        // ✅ LOGIC CHUẨN (kết hợp filter)
        if (!keyword.isEmpty() && categoryId != null) {
            productPage = productService.searchByNameAndCategory(keyword, categoryId, pageable);
        } else if (!keyword.isEmpty()) {
            productPage = productService.searchByName(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productService.findByCategory(categoryId, pageable);
        } else {
            productPage = productService.findAll(pageable);
        }

        // DATA RA VIEW
        model.addAttribute("listproduct", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("categories", categoryService.getAll());

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
    public String create(
            @Valid @ModelAttribute("product") Product newProduct,
            BindingResult result,
            @RequestParam("category.id") Long categoryId,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        // IMAGE UPLOAD
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(newProduct, imageProduct);
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            newProduct.setImage(imageUrl);
        }

        // CATEGORY
        Category category = categoryService.get(categoryId);
        newProduct.setCategory(category);

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
    public String edit(
            @Valid @ModelAttribute("product") Product editProduct,
            BindingResult result,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        Product oldProduct = productService.get(editProduct.getId());

        // IMAGE UPDATE
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            editProduct.setImage(imageUrl);
        } else {
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