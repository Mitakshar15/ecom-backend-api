package com.ainkai.controller;


import com.ainkai.exceptions.OrderException;
import com.ainkai.model.Order;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrderHandler(){
        List<Order> orderList = orderService.getAllOrders();
        return new ResponseEntity<>(orderList, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> confirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order=orderService.confirmedOrder(orderId);
        return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/shipped")
    public  ResponseEntity<Order> shippedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
       Order order = orderService.shippedOrder(orderId);
       return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/delivered")
    public  ResponseEntity<Order> deliveredOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/canceled")
    public  ResponseEntity<Order> canceledOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order = orderService.cancledOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public  ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
         orderService.deleteOrder(orderId);
         ApiResponse response = new ApiResponse();
         response.setMessage("SUCCESFULLY DELETED THE ORDER : " + orderId);
         response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }






}
