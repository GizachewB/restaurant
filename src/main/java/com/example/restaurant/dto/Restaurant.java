package com.example.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
//    private String restaurantId;
//    private String restaurantName;

    private String id;
    private String email;
    private String restaurantName;
    private Address address;
}
