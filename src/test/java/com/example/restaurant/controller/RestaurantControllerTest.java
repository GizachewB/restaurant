package com.example.restaurant.controller;

import com.example.restaurant.dto.Address;
import com.example.restaurant.dto.Food;
import com.example.restaurant.dto.Restaurant;
import com.example.restaurant.dto.User;
import com.example.restaurant.entity.Order;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private KafkaTemplate<String,Object> kafkaTemplate;

    @MockBean
    private RestaurantRepository restaurantRepository;

    Order order = new Order();
    Order order2 = new Order();

    User user = new User(1,"Gizachew","g@gmail.com",new Address());
    User user2 = new User(2,"Meckonnen","m@gmail.com",new Address());

    List<Food> foodOrders = new ArrayList<>();
    Restaurant restaurant = new Restaurant("1","ranch@gmail.com","Pizza Ranch",new Address());
    List<Order> orderList = new ArrayList<>();

    @Test
    void getOrdersFromKafka() {
    }

    @Test
    void getCurrentOrders() throws Exception {

        foodOrders.add(new Food("Pizza","$100"));
        order.setROrderId("1");
        order.setOrderId(10);
        order.setUser(user);
        order.setFoods(foodOrders);
        order.setRestaurant(restaurant);
        order.setStatus(Order.Status.ORDERED);

        order2.setUser(user2);
        order2.setFoods(foodOrders);
        order2.setRestaurant(restaurant);
        order2.setStatus(Order.Status.ORDERED);

        orderList.add(order);
        orderList.add(order2);

        given(restaurantService.getAllOrders()).willReturn(orderList);

        mockMvc.perform(get("/restaurant/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].orderId",is((int)order.getOrderId())));
//                .andExpect(jsonPath("$[0].rOrderId", is(order.getROrderId())));
    }

    @Test
    void orderStatus() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void getOrdersByStatus() throws Exception {

        foodOrders.add(new Food("Pizza","$100"));
        order.setOrderId(11L);
        order.setUser(user);
        order.setROrderId("1");
        order.setFoods(foodOrders);
        order.setRestaurant(restaurant);
        order.setStatus(Order.Status.ORDERED);

        order2.setUser(user2);
        order2.setFoods(foodOrders);
        order2.setRestaurant(restaurant);
        order2.setStatus(Order.Status.ORDERED);

        orderList.add(order);
        orderList.add(order2);

//        given(restaurantService.findOrderById(order.getOrderId())).willReturn(order);
        given(restaurantService.getOrdersByRestaurantAndStatus(restaurant.getId(), Order.Status.ORDERED))
                .willReturn(orderList);

        mockMvc.perform(get("/restaurant/"+ restaurant.getId()+"/orders/status"+"?status=ORDERED")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].rorderId", is(order.getROrderId())));

    }

    @Test
    void findOrderById() throws Exception {
 /*       Order order = new Order();
        User user = new User(1,"Gizachew","g@gmail.com", new Address());
        List<Food> foodOrders = new ArrayList<>();

        Restaurant restaurant = new Restaurant("1","ranch@gmail.com","Pizza Ranch", new Address());
        */
        foodOrders.add(new Food("Pizza","$100"));
        order.setOrderId(1);
        order.setROrderId("10");
        order.setUser(user);
        order.setFoods(foodOrders);
        order.setRestaurant(restaurant);
        order.setStatus(Order.Status.ORDERED);

        given(restaurantService.findOrderById(order.getROrderId())).willReturn(order);

        mockMvc.perform(get("/restaurant/"+ restaurant.getId()+"/orders/"+order.getROrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(order)))
                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("$.user.id", is(1)));
                .andExpect(jsonPath("$.rorderId", is(String.valueOf(10))));
        verify(restaurantService, VerificationModeFactory.times(1))
                .findOrderById(order.getROrderId());

        reset(restaurantService);
    }

    @Test
    void getOrdersByRestaurantId() throws Exception {
        foodOrders.add(new Food("Pizza","$100"));
        order.setOrderId(11L);
        order.setUser(user);
        order.setROrderId("1");
        order.setFoods(foodOrders);
        order.setRestaurant(restaurant);
        order.setStatus(Order.Status.ORDERED);

        order2.setUser(user2);
        order2.setFoods(foodOrders);
        order2.setRestaurant(restaurant);
        order2.setStatus(Order.Status.ORDERED);

        orderList.add(order);
        orderList.add(order2);
        given(restaurantService.getOrdersByRestaurantId(restaurant.getId()))
                .willReturn(orderList);

        mockMvc.perform(get("/restaurant/"+ restaurant.getId()+"/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].restaurant.id", is(restaurant.getId())));

    }

    @Test
    void getOrdersByUserId() throws Exception {
        foodOrders.add(new Food("Pizza","$100"));
        order.setOrderId(11L);
        order.setUser(user);
        order.setROrderId("1");
        order.setFoods(foodOrders);
        order.setRestaurant(restaurant);
        order.setStatus(Order.Status.ORDERED);

        order2.setUser(user);
        order2.setFoods(foodOrders);
        order2.setRestaurant(restaurant);
        order2.setStatus(Order.Status.ORDERED);

        orderList.add(order);
        orderList.add(order2);
        given(restaurantService.getOrdersByUserId(user.getId()))
                .willReturn(orderList);

        mockMvc.perform(get("/restaurant/orders/"+ user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user.id", is((int)user.getId())));
    }
}