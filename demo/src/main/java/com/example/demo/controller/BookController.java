package com.example.demo.controller;

import com.example.demo.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1, "Spring boot", "Huy Cuong"));
        books.add(new Book(2, "Spring Boot V2", "Anh"));
    }

    // 📘 Danh sách sách
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", books);
        return "books";
    }

    // ➕ Form thêm
    @GetMapping("/add")
    public String addForm(Model model) {

        int nextId = 1;
        if (!books.isEmpty()) {
            nextId = books.get(books.size() - 1).getId() + 1;
        }

        Book book = new Book();
        book.setId(nextId); // 👈 gán sẵn ID

        model.addAttribute("book", book);
        return "add-book";
    }

    // ➕ Xử lý thêm
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        books.add(book);
        return "redirect:/books";
    }

    // ✏️ Form sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        for (Book b : books) {
            if (b.getId() == id) {
                model.addAttribute("book", b);
                break;
            }
        }
        return "edit-book";
    }

    // ✏️ Xử lý sửa
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute Book book) {
        for (Book b : books) {
            if (b.getId() == book.getId()) {
                b.setTitle(book.getTitle());
                b.setAuthor(book.getAuthor());
                break;
            }
        }
        return "redirect:/books";
    }

    // ❌ Xóa
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        books.removeIf(b -> b.getId() == id);
        return "redirect:/books";
    }
}
