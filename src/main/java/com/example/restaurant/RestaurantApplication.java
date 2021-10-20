package com.example.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

/*    @Bean
    public CommandLineRunner demo(OrderRepository repository, RestaurantRepository restaurantRepository) {
        return (args) -> {
           Restaurant restaurant = restaurantRepository.save(new Restaurant(1L,"HyVee",null));
            repository.save(new Order(1L,"Pizza",120.1,restaurant));
            repository.save(new Order(2L,"Burger",200,restaurant));
            repository.save(new Order(3L,"Sandwich",40,restaurant));
            repository.save(new Order(4L,"Wheat",140,restaurant));
            repository.save(new Order(5L,"Veg",120,restaurant));
        };
    }*/
}
