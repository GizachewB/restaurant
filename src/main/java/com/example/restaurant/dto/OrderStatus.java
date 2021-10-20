package com.example.restaurant.dto;

import com.example.restaurant.entity.Order;
import lombok.Data;

@Data
public class OrderStatus {
    private String id;
    private Order.Status status;
}
