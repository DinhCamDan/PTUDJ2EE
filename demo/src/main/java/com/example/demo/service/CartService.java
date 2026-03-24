package com.example.demo.service;

import com.example.demo.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý giỏ hàng, lưu trong session của người dùng
 */
@Service
@SessionScope
public class CartService {

    private List<CartItem> items = new ArrayList<>();

    // Lấy danh sách sản phẩm trong giỏ
    public List<CartItem> getItems() {
        return items;
    }

    // Thêm sản phẩm vào giỏ
    public void addToCart(CartItem newItem) {
        // Nếu sản phẩm đã có trong giỏ thì tăng số lượng
        for (CartItem item : items) {
            if (item.getId().equals(newItem.getId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        // Nếu chưa có thì thêm mới
        items.add(newItem);
    }

    // Cập nhật số lượng
    public void updateQuantity(int productId, int quantity) {
        for (CartItem item : items) {
            if (item.getId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ
    public void removeFromCart(int productId) {
        items.removeIf(item -> item.getId().equals(productId));
    }

    // Xóa toàn bộ giỏ hàng
    public void clear() {
        items.clear();
    }

    // Tính tổng tiền giỏ hàng
    public long getTotalPrice() {
        return items.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
