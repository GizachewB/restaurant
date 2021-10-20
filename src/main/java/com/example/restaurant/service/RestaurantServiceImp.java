package com.example.restaurant.service;

import com.example.restaurant.dto.StatusFromDriver;
import com.example.restaurant.entity.Order;
import com.example.restaurant.repository.RestaurantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImp implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    ModelMapper modelMapper = new ModelMapper();

    ObjectMapper objectMapper= new ObjectMapper();

    @KafkaListener(topics = "toRestaurantStatusShipped", groupId = "group_id")
    public void getStatusFromKafka(String status)  {
        System.out.println(status);
        StatusFromDriver statusFromDriver = null;
        try {
            statusFromDriver = objectMapper.readValue(status, StatusFromDriver.class);

            Order order = restaurantRepository.findById(statusFromDriver.getROrderId()).get();
            order.setStatus(statusFromDriver.getStatus());
            restaurantRepository.save(order);

            System.out.println(order);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(statusFromDriver);
    }


    @Override
    public Order persistOrder(Order order) {
        order.setStatus(Order.Status.ORDERED);
        return restaurantRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return restaurantRepository.findAll();
    }

    @Override
    public Order orderStatus(Order order, Order.Status Status) {
        order.setStatus(Status);
        return restaurantRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByRestaurantAndStatus(String restaurantId, Order.Status status) {
        return restaurantRepository.findByRestaurantIdAndStatus(restaurantId,status);
    }

    @Override
    public void cancelOrder(Order order, Order.Status status) {
        order.setStatus(status);
        restaurantRepository.save(order);
    }

    @Override
    public Order findOrderById(String rOrderId) {
        return restaurantRepository.findById(rOrderId).get();
    }

    @Override
    public List<Order> getOrdersByRestaurantId(String restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Order> getOrdersByUserId(long userId) {
        return restaurantRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByUserIdAndStatus(long userId, Order.Status status) {
        return restaurantRepository.findByUserIdAndStatus(userId,status);
    }
}
