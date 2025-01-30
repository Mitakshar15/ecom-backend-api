
/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1OrderControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Order;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddressDto;
import com.ainkai.model.dtos.OrderHistoryResponse;
import com.ainkai.model.dtos.OrderResponse;
import com.ainkai.service.OrderService;
import com.ainkai.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserOrderController implements EcomApiV1OrderControllerApi {

    private final OrderService orderService;
    private final UserService userService;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;


   public ResponseEntity<OrderResponse> createOrderHandler(@RequestHeader("Authorization")String jwt, AddressDto AddressRequest) throws UserException, ProductException, OrderException {
       User user  = userService.findUserProfileByJwt(jwt);
       Order order = orderService.createOrder(user, AddressRequest);
       OrderResponse response = mapper.toOrderResponse(builder.buildSuccessApiResponse("Order Created Successfully"));
       response.order(builder.buildOrderDto(order));
       return new ResponseEntity<>(response, HttpStatus.CREATED);
   }


   public ResponseEntity<OrderHistoryResponse> getUserOrderHistoryHandler(@RequestHeader("Authorization")String jwt) throws UserException {
       User user = userService.findUserProfileByJwt(jwt);
       OrderHistoryResponse response = mapper.toOrderHistoryResponse(builder.buildSuccessApiResponse("Order History Retrieved Successfully"));
       List<Order> orders = orderService.usersOrderHistory(user.getId());
       response.orderHistory(mapper.toOrderDtoList(orders));
       return new ResponseEntity<>(response, HttpStatus.OK);
   }

   public ResponseEntity<OrderResponse> getOrderByIdHandler(@PathVariable("orderId")Long orderId) throws OrderException {
         Order order = orderService.findOrderById(orderId);
         OrderResponse response = mapper.toOrderResponse(builder.buildSuccessApiResponse("Order Retrieved Successfully"));
         response.order(builder.buildOrderDto(order));
         return new ResponseEntity<>(response, HttpStatus.OK);
   }

}
