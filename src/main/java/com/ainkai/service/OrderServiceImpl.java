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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        if(cart.getCartItems().isEmpty()){
            throw new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
        }

        //Build all order items in memory first (avoid saving one-by-one)
        List<OrderItem> orderItems = new ArrayList<>();
        List<Sku> skusToUpdate = new ArrayList<>();

        for(CartItem item : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setSku(item.getSku());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUserId(user.getId());
            orderItem.setPrice(item.getPrice());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            orderItems.add(orderItem);

            if(item.getSku().getQuantity() > orderItem.getQuantity()){
                item.getSku().setQuantity(item.getSku().getQuantity() - orderItem.getQuantity());
                skusToUpdate.add(item.getSku());
            }
        }

        // Batch save SKU quantity updates
        if(!skusToUpdate.isEmpty()){
            skuRepository.saveAll(skusToUpdate);
        }

        // Create and save order (cascade will save order items)
        Order createdOrder = new Order();
        createdOrder.setUser(user);
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

        // Set bidirectional relationship so cascade persist handles order items
        for(OrderItem item : orderItems){
            item.setOrder(createdOrder);
        }
        createdOrder.setOrderItemList(orderItems);

        Order savedOrder = orderRepo.save(createdOrder);

        //Clear the cart after creating the order
        cartItemService.removeAllItems(cart.getId());
        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepo.findById(orderId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw  new OrderException(Constants.DATA_NOT_FOUND_KEY,Constants.ORDER_NOT_FOUND_MESSAGE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> usersOrderHistory(Long userId) {
       return orderRepo.getAllUserOrders(userId);
    }

    @Override
    @Transactional
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.PLACED);
        order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.SHIPPED);
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.DELIVERED);
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        return  orderRepo.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
       return  orderRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepo.deleteById(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isShippingAddressExists(Long userId, Address address) {
        Optional<User> opt = userRepo.findById(userId);
        User user = new User();
        if(opt.isPresent()){
         user = opt.get();
        }
        return addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(), address.getCity(), address.getState(), address.getZipCode(), user);
    }
}
