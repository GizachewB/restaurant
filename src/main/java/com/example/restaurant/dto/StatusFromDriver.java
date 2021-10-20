package com.example.restaurant.dto;

import com.example.restaurant.entity.Order;
import lombok.Data;

@Data
public class StatusFromDriver {
    private String rOrderId;
    private long orderId;
    private Order.Status status;
}
