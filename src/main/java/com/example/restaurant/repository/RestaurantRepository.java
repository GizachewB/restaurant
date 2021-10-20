package com.example.restaurant.repository;

import com.example.restaurant.entity.Order;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends MongoRepository<Order, String> {
//    List<Order> findByUserUserId(long userId);
    List<Order> findByUserId(long userId);
//    List<Order> findOrdersByRestaurant_RestaurantIdAndStatus(String restaurantId, Order.Status status);
//    List<Order> findByUserUserIdAndStatus(long userId,Order.Status status);
    List<Order> findByUserIdAndStatus(long userId,Order.Status status);
//    List<Order> findByRestaurantRestaurantIdAndStatus(String restaurantId, Order.Status status);
    List<Order> findByRestaurantIdAndStatus(String restaurantId, Order.Status status);
//    List<Order> findByRestaurantRestaurantId(String restaurantId);
    List<Order> findByRestaurantId(String restaurantId);


}
