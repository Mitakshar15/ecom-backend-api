/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.service;

import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.*;
import com.ainkai.model.dtos.AddressDto;
import com.ainkai.repository.*;
import com.ainkai.user.domain.Constants;
import com.ainkai.user.domain.OrderStatus;
import com.ainkai.user.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final CartService cartService;
    private final AddressRepo addressRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final UserRepo userRepo;
    private final ProductService productService;
    private final EcomApiUserMapper mapper;
    private final CartItemService cartItemService;
    private final SkuRepository skuRepository;

    @Override
    public Order createOrder(User user, AddressDto request) throws ProductException, UserException, OrderException {
        Address shippingAddress = mapper.toAddressEntity(request);
        shippingAddress.setUser(user);
        Address finalAddress = new Address();
        if(isShippingAddressExists(user.getId(),shippingAddress)){
           Optional<Address> opt=addressRepo.findByStreetAddressAndCityAndStateAndZipCodeAndUser(shippingAddress.getStreetAddress(),shippingAddress.getCity(),shippingAddress.getState(),shippingAddress.getZipCode(),user);
           if(opt.isPresent()){
               finalAddress = opt.get();
           }
        }
        //Now Fetch the cart of the particular user
        Cart cart = cartService.findUserCart(user.getId());
        //now create a List of Order items and fetch each order Item One by One
        List<OrderItem> orderItems = new ArrayList<>();
        if(cart.getCartItems().isEmpty()){
            throw new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        //Loop through Each cart item, and add the cart item to respective orderitem
        for(CartItem item :cart.getCartItems() ){
            OrderItem orderItem = new OrderItem();
            orderItem.setSku(item.getSku());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUserId(user.getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            OrderItem createdOrderItem = orderItemRepo.save(orderItem);
            orderItems.add(createdOrderItem);
            if(item.getSku().getQuantity()>orderItem.getQuantity()){
                item.getSku().setQuantity(item.getSku().getQuantity()-orderItem.getQuantity());
                skuRepository.save(item.getSku());
            }
        }
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItemList(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setOrderId(user.getLastName() + (cart.getId()));
        createdOrder.setShippingAddress(finalAddress);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus(OrderStatus.PENDING);
        createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder.setDeliveryDate(LocalDateTime.now());
        Order saveOrder = orderRepo.save(createdOrder);
        //Update the order items to set the order or (set the ORDER for orderItems)
        for(OrderItem item : orderItems){
            item.setOrder(saveOrder);
            orderItemRepo.save(item);
        }
        //Clear the cart Before creating the order
        cartItemService.removeAllItems(cart.getId());
        return saveOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepo.findById(orderId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
       List<Order> orderList = orderRepo.getUsersOrders(userId);
       return orderList;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order==null){
            throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        order.setOrderStatus(OrderStatus.PLACED);
        order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
        return   orderRepo.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order==null){
            throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        order.setOrderStatus(OrderStatus.CONFIRMED);
        return   orderRepo.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order==null){
            throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        order.setOrderStatus(OrderStatus.SHIPPED);
        return   orderRepo.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order==null){
            throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        return  order;
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order == null){
            throw new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return  orderRepo.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
       return  orderRepo.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        if(order==null){
            throw new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }
        orderRepo.deleteById(orderId);

    }

    @Override
    public boolean isShippingAddressExists(Long userId, Address address) {

        Optional<User> opt = userRepo.findById(userId);
        User user = new User();
        if(opt.isPresent()){
         user = opt.get();
        }
        return addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(), address.getCity(), address.getState(), address.getZipCode(), user);
    }
}
