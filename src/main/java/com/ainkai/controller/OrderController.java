package com.ainkai.controller;


import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.Order;
import com.ainkai.model.User;
import com.ainkai.service.OrderService;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
  private OrderService orderService;
  @Autowired
  private UserService userService;


    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


   @PostMapping("/")
   public ResponseEntity<Order> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt)throws UserException, ProductException {

       User user = userService.findUserProfileByJwt(jwt);

       Order order = orderService.createOrder(user,shippingAddress);

       return new ResponseEntity<>(order, HttpStatus.OK);
   }


   @GetMapping("/user")
   public  ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization")String jwt) throws UserException{

        /* Implement A method such that is should return only the orders and user id, instead of returning userdetails */

        User user = userService.findUserProfileByJwt(jwt);

        List<Order> orderList = orderService.usersOrderHistory(user.getId());

        return  new ResponseEntity<>(orderList,HttpStatus.CREATED);
   }

   @GetMapping("/{id}")
   public  ResponseEntity<Order> findOrderById(@PathVariable("id") Long orderId,@RequestHeader("Authorization")String jwt)throws UserException, OrderException{

         User user = userService.findUserProfileByJwt(jwt);
         Order order = orderService.findOrderById(orderId);
         return  new ResponseEntity<>(order,HttpStatus.CREATED);

   }


}
