package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "order/checkout"; // form nhập thông tin khách hàng
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String address,
                             Model model) {

        Order order = orderService.placeOrder(name, email, address,
                cartService.getItems(), cartService.getTotalPrice());

        cartService.clear(); // xóa giỏ hàng sau khi đặt

        model.addAttribute("order", order);
        return "order/success"; // trang thông báo đặt hàng thành công
    }
    // Lịch sử đơn hàng của user
    @GetMapping("/history")
    public String orderHistory(@RequestParam String email, Model model) {
        model.addAttribute("orders", orderService.getOrdersByEmail(email));
        return "order/history"; // giao diện hiển thị lịch sử
    }

    // Nếu là admin thì xem tất cả đơn hàng
    @GetMapping("/all")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/all"; // giao diện hiển thị tất cả đơn hàng
    }
}

