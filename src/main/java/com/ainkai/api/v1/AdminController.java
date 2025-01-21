/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1AdminControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.OrderException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Order;
import com.ainkai.model.Product;
import com.ainkai.model.User;
import com.ainkai.model.dtos.*;
import com.ainkai.service.CartService;
import com.ainkai.service.OrderService;
import com.ainkai.service.ProductService;
import com.ainkai.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController implements EcomApiV1AdminControllerApi {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;
    private final ProductService productService;

    public ResponseEntity<BasicOrderResponse> placeOrderHandler(@PathVariable("orderId")Long orderId, @Parameter(name = "Authorization", description = "", required = true, in = ParameterIn.HEADER) @RequestHeader("Authorization")String jwt) {
        orderService.placedOrder(orderId);
        //String email =userService.findUserProfileByJwt(jwt).getEmail();
        //orderConfirmationEmailsender.sendEmail(email,"ORDER PLACED","THANK YOU "+ userService.findUserProfileByJwt(jwt).getFirstName() +" YOUR ORDDER HAS BEEN PLACED " + " ORDER ID : "+order.getOrderId()+" SHIPPING ADDRESS : "+order.getShippingAddress().getStreetAddress()+ " "+order.getShippingAddress().getCity()+" "+order.getShippingAddress().getZipCode());
        BasicOrderResponse response = mapper.toBasicOrderResponse(builder.buildSuccessApiResponse("Order placed successfully"));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public  ResponseEntity<BasicOrderResponse> shipOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException {
        Order order = orderService.shippedOrder(orderId);
        BasicOrderResponse response = mapper.toBasicOrderResponse(builder.buildSuccessApiResponse("Order shipped successfully"));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    public  ResponseEntity<BasicOrderResponse> deliverOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order = orderService.deliveredOrder(orderId);
        BasicOrderResponse response = mapper.toBasicOrderResponse(builder.buildSuccessApiResponse("Order delived successfully"));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    public  ResponseEntity<BasicOrderResponse> cancelOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException {
        Order order = orderService.cancledOrder(orderId);
        BasicOrderResponse response = mapper.toBasicOrderResponse(builder.buildSuccessApiResponse("Order cancelled successfully"));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<BasicOrderResponse> confirmOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)throws OrderException {
        Order order = orderService.confirmedOrder(orderId);
        BasicOrderResponse response = mapper.toBasicOrderResponse(builder.buildSuccessApiResponse("Order confirmed successfully"));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<AllUserResponse> findAllUsersHandler(@RequestHeader("Authorization") String jwt) {
        List<User> users = userService.findAllUsers();
        AllUserResponse response = mapper.toAllUserResponse(builder.buildSuccessApiResponse("All users found successfully"));
        response.data(mapper.toUserResponseDtoList(users));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<EcomApiServiceBaseApiResponse> createProductHandler(@RequestHeader("Authorization")String jwt,@RequestBody CreateProductRequest request){
        Product product = productService.createProduct(request);
        if(product !=null) {
            EcomApiServiceBaseApiResponse response = mapper.toEcomApiServiceBaseApiResponse(builder.buildSuccessApiResponse("Product created successfully"));
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        else{
            EcomApiServiceBaseApiResponse errorResponse = mapper.toEcomApiServiceBaseApiResponse(builder.buildErrorApiResponse("Product creation failed"));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<EcomApiServiceBaseApiResponse> updateProductHandler(@RequestHeader("Authorization")String jwt,@RequestBody UpdateProductRequest request){
        Product product = productService.updateProduct(request);
        if(product !=null) {
            EcomApiServiceBaseApiResponse response = mapper.toEcomApiServiceBaseApiResponse(builder.buildSuccessApiResponse("Product updated successfully"));
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        else {
            EcomApiServiceBaseApiResponse errorResponse = mapper.toEcomApiServiceBaseApiResponse(builder.buildErrorApiResponse("Product update failed"));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AllProductResponse> findAllProductHandler(@RequestHeader("Authorization") String jwt){
        List<Product> products = productService.getAllProducts();
        AllProductResponse response = mapper.toAllProductResponse(builder.buildSuccessApiResponse("All products found successfully"));
        response.products(builder.buildProductDtoList(products));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


}
