package com.example.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
//    private long userId;
//    private String userName;
//    private String email;

    private long id;
    private String userName;
    private String email;
    private Address address;
}
