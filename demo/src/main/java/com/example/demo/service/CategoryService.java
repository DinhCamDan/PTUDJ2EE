package com.example.demo.service;

import com.example.demo.model.Category;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final List<Category> listCategory = new ArrayList<>();

    @PostConstruct
    public void initSampleData() {
        // thêm vài category mẫu
        listCategory.add(new Category(1, "Tiểu thuyết"));
        listCategory.add(new Category(2, "Công nghệ"));
        listCategory.add(new Category(3, "Kinh tế"));
    }

    public List<Category> getAll() {
        return listCategory;
    }

    public Category get(int id) {
        return listCategory.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Category newCategory) {
        int maxId = listCategory.stream().mapToInt(Category::getId).max().orElse(0);
        newCategory.setId(maxId + 1); // ID tự động tăng
        listCategory.add(newCategory);
    }

    public void update(Category editCategory) {
        Category find = get(editCategory.getId());
        if (find != null) {
            find.setName(editCategory.getName());
        }
    }

    public void delete(int id) {
        listCategory.removeIf(c -> c.getId() == id);
    }
}
