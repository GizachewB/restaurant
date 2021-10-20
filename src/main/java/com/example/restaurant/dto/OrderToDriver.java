package com.example.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderToDriver {
    private String rOrderId;
    private long orderId;
    private String userName;
    private List<FoodToDriver> foods;
    private String restaurantName;
//    private Driver driver;

}
