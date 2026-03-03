package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product get(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void add(Product product) {
        productRepository.save(product);
    }

    public void update(Product product) {
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // Giữ lại phần upload ảnh
    public void updateImage(Product product, MultipartFile imageProduct) {

        if (imageProduct != null && !imageProduct.isEmpty()) {
            try {

                String uploadDir = System.getProperty("user.dir")
                        + "/src/main/resources/static/images/";

                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = UUID.randomUUID() + "_"
                        + imageProduct.getOriginalFilename();

                Path filePath = uploadPath.resolve(fileName);

                Files.copy(imageProduct.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}