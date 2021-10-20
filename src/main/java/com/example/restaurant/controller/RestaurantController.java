package com.example.restaurant.controller;

import com.example.restaurant.dto.OrderStatus;
import com.example.restaurant.dto.OrderToDriver;
import com.example.restaurant.entity.Order;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    public static final String topic = "toDriverTopic";

    @Autowired
    private RestaurantRepository restaurantRepository;

   @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    ModelMapper modelMapper = new ModelMapper();

    ObjectMapper objectMapper= new ObjectMapper();


    @KafkaListener(topics = "mytopic", groupId = "group_id")
    public void getOrdersFromKafka(String orders)  {

        Order order = null;
        try {
            order = objectMapper.readValue(orders, Order.class);
            restaurantService.persistOrder(order);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(order);
    }
    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return restaurantService.getAllOrders();
    }

    @GetMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("restaurantId") String restaurantId,
                                           @PathVariable("orderId") String rOrderId){
        return ResponseEntity.ok(restaurantService.findOrderById(rOrderId));
    }

    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<?> getOrdersByRestaurantId(@PathVariable("restaurantId") String restaurantId){
        return ResponseEntity.ok(restaurantService.getOrdersByRestaurantId(restaurantId));
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("userId") long userId){
        return ResponseEntity.ok(restaurantService.getOrdersByUserId(userId));
    }

    /*@GetMapping("/orders/{userId}/status")
    public ResponseEntity<?> getOrdersByUserIdAndStatus(@PathVariable("userId") long userId,
                                                        @RequestParam(name = "status") String status){
        if(status.equals("DELIVERED")){
            return ResponseEntity.ok(restaurantService.getOrdersByUserIdAndStatus(userId,Order.Status.DELIVERED));
        }else
            return null;

    }*/
    @GetMapping("/orders/{userId}/delivered")
    public ResponseEntity<?> getDeliveredOrdersByUserIdAndStatus(@PathVariable("userId") long userId){
            return ResponseEntity.ok(restaurantService.getOrdersByUserIdAndStatus(userId,Order.Status.DELIVERED));
    }
    @GetMapping("/orders/{userId}/ready")
    public ResponseEntity<?> getReadyOrdersByUserIdAndStatus(@PathVariable("userId") long userId){
        return ResponseEntity.ok(restaurantService.getOrdersByUserIdAndStatus(userId,Order.Status.READY));
    }
    @GetMapping("/orders/{userId}/shipped")
    public ResponseEntity<?> getShippedOrdersByUserIdAndStatus(@PathVariable("userId") long userId){
        return ResponseEntity.ok(restaurantService.getOrdersByUserIdAndStatus(userId,Order.Status.SHIPPED));
    }
    @GetMapping("/orders/{userId}/ordered")
    public ResponseEntity<?> getOrderedOrdersByUserIdAndStatus(@PathVariable("userId") long userId){
        return ResponseEntity.ok(restaurantService.getOrdersByUserIdAndStatus(userId,Order.Status.ORDERED));
    }
    /**
     * an API used to change the status of the order for a restaurant,
     * but it's also changed by the driver kafka producer
     * @param restaurantId
     * @param orderId
     * @param orderStatus
     * @return
     */
    @PatchMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable("restaurantId") long restaurantId,
                                            @PathVariable("orderId") String orderId,
                                            @RequestBody OrderStatus orderStatus){
        Order order = restaurantRepository.findById(orderId).get();

        if(order.getStatus() == Order.Status.ORDERED && orderStatus.getStatus().equals(Order.Status.READY)){
            Order changedOrder = restaurantService.orderStatus(order,orderStatus.getStatus());

            OrderToDriver orderToDriver = modelMapper.map(changedOrder,OrderToDriver.class);

            kafkaTemplate.send(topic,orderToDriver);

            return ResponseEntity.ok(changedOrder);
        }
        else if(order.getStatus() == Order.Status.READY && orderStatus.getStatus().equals(Order.Status.SHIPPED)){
            return ResponseEntity.ok(restaurantService.orderStatus(order,orderStatus.getStatus()));
        }
        else if(order.getStatus() == Order.Status.SHIPPED && orderStatus.getStatus().equals(Order.Status.DELIVERED)){
            return ResponseEntity.ok(restaurantService.orderStatus(order,orderStatus.getStatus()));
        }
        else{
            return ResponseEntity.badRequest().body("Illegal Expression");
        }
    }

    /**
     * an API for a customer to cancel his order unless delivered
     * @param restaurantId
     * @param orderId
     * @param orderStatus
     * @return
     */
    @DeleteMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("restaurantId") long restaurantId,
                                         @PathVariable("orderId") String orderId,
                                         @RequestBody OrderStatus orderStatus){
        Order order = restaurantRepository.findById(orderId).get();
        if(order.getStatus() != Order.Status.DELIVERED && orderStatus.getStatus().equals(Order.Status.CANCELLED)){
            restaurantService.cancelOrder(order,orderStatus.getStatus());
            return ResponseEntity.ok().body("Your order is cancelled");
        }else
            return ResponseEntity.badRequest().body("either you didn't enter the right word or your order is already delivered");

    }

    /**
     * an API to get the ready orders by using status and restaurantId
     * @param restaurantId
     * @return
     */
   /* @GetMapping("/{restaurantId}/orders/status")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable("restaurantId") String restaurantId,
                                            @RequestParam(name = "status") String status){

        if(status.equals("READY")){
            return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.READY));
        }else if (status.equals("ORDERED")){
            return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.ORDERED));
        }else if(status.equals("CANCELLED")){
            return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.CANCELLED));
        }else if(status.equals("SHIPPED")){
            return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.SHIPPED));
        }else if(status.equals("DELIVERED")){
            return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.DELIVERED));
        }
            return ResponseEntity.badRequest().body("Pass the right Status");

    }*/
    @GetMapping("/{restaurantId}/orders/ready")
    public ResponseEntity<?> getReadyOrdersByRestaurantIdAndStatus(@PathVariable("restaurantId") String restaurantId){
        return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.READY));
    }
    @GetMapping("/{restaurantId}/orders/ordered")
    public ResponseEntity<?> getActiveOrdersByRestaurantIdAndStatus(@PathVariable("restaurantId") String restaurantId){
        return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.ORDERED));
    }
    @GetMapping("/{restaurantId}/orders/shipped")
    public ResponseEntity<?> getShippedOrdersByRestaurantIdAndStatus(@PathVariable("restaurantId") String restaurantId){
        return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.SHIPPED));
    }
    @GetMapping("/{restaurantId}/orders/delivered")
    public ResponseEntity<?> getDeliveredOrdersByRestaurantIdAndStatus(@PathVariable("restaurantId") String restaurantId){
        return ResponseEntity.ok(restaurantService.getOrdersByRestaurantAndStatus(restaurantId,Order.Status.DELIVERED));
    }
}
