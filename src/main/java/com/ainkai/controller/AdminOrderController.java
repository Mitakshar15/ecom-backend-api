package com.ainkai.controller;


import com.ainkai.dto.OrderResponseDTO;
import com.ainkai.emailservice.OrderConfirmationEmail;
import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Order;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.OrderService;
import com.ainkai.service.UserService;
import jakarta.mail.MessagingException;
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
    @Autowired
    private UserService userService;
    @Autowired
    private OrderConfirmationEmail orderConfirmationEmailsender;

    @GetMapping("/")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrderHandler(){
        List<Order> orderList = orderService.getAllOrders();
        return new ResponseEntity<>(OrderResponseDTO.fromEntityToList(orderList), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<OrderResponseDTO> confirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order=orderService.confirmedOrder(orderId);
        return new ResponseEntity<>(OrderResponseDTO.fromEntity(order),HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/shipped")
    public  ResponseEntity<OrderResponseDTO> shippedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
       Order order = orderService.shippedOrder(orderId);
       return new ResponseEntity<>(OrderResponseDTO.fromEntity(order),HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/delivered")
    public  ResponseEntity<OrderResponseDTO> deliveredOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(OrderResponseDTO.fromEntity(order),HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/canceled")
    public  ResponseEntity<OrderResponseDTO> canceledOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order = orderService.cancledOrder(orderId);
        return new ResponseEntity<>(OrderResponseDTO.fromEntity(order),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public  ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
         orderService.deleteOrder(orderId);
         ApiResponse response = new ApiResponse();
         response.setMessage("SUCCESFULLY DELETED THE ORDER : " + orderId);
         response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
    @PutMapping("/{orderId}/placed")
    public  ResponseEntity<OrderResponseDTO> placeOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException, MessagingException, UserException {
        Order order = orderService.placedOrder(orderId);

        return new ResponseEntity<>(OrderResponseDTO.fromEntity(order),HttpStatus.ACCEPTED);
    }






}
