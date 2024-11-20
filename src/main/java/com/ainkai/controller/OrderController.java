package com.ainkai.controller;


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

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private OrderService orderService;
  @Autowired
  private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


   @PostMapping("/")
   public ResponseEntity<Order> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt)throws UserException {

       User user = userService.findUserProfileByJwt(jwt);

       Order order = orderService.createOrder(user,shippingAddress);

       return new ResponseEntity<>(order, HttpStatus.OK);
   }
}
