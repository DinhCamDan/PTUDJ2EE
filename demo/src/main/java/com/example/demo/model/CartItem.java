package com.example.demo.model;

import lombok.Data;

/**
 * Đại diện cho một sản phẩm trong giỏ hàng
 */
@Data
public class CartItem {

    // Thông tin sản phẩm
    private Integer id;
    private String name;
    private String image;
    private long price;

    // Số lượng
    private int quantity;
}
