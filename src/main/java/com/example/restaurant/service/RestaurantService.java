package com.example.restaurant.service;

import com.example.restaurant.entity.Order;

import java.util.List;

public interface RestaurantService {
    Order persistOrder(Order order);

    List<Order> getAllOrders();
    Order orderStatus(Order order, Order.Status Status);
    List<Order> getOrdersByRestaurantAndStatus(String restaurantId, Order.Status status);
    void cancelOrder(Order order, Order.Status status);
    Order findOrderById(String rOrderId);
    List<Order> getOrdersByRestaurantId(String restaurantId);
    List<Order> getOrdersByUserId(long userId);

    List<Order> getOrdersByUserIdAndStatus(long userId, Order.Status status);
}
