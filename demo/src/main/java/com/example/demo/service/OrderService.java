package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(String name, String email, String address, List<CartItem> cartItems, long totalPrice) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomerName(name);
        order.setCustomerEmail(email);
        order.setCustomerAddress(address);
        order.setTotalPrice(totalPrice);

        List<OrderItem> items = cartItems.stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getId());
            oi.setProductName(ci.getName());
            oi.setProductImage(ci.getImage());
            oi.setPrice(ci.getPrice());
            oi.setQuantity(ci.getQuantity());
            oi.setOrder(order);
            return oi;
        }).toList();

        order.setItems(items);
        return orderRepository.save(order);
    }
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
