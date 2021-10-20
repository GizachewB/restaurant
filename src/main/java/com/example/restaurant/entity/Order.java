package com.example.restaurant.entity;

/*
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Order {
    @Id //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private String foodName;
    private double price;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonManagedReference
    private Restaurant restaurant;
}
*/

import com.example.restaurant.dto.Food;
import com.example.restaurant.dto.Restaurant;
import com.example.restaurant.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Order {
    public enum Status{
        CANCELLED,ORDERED,READY,SHIPPED,DELIVERED
    }
    @Id
    private String rOrderId;//restaurantOrderId;
    private long orderId;
    private User user;
    private List<Food> foods;
    private Restaurant restaurant;
    private Status status;
}
