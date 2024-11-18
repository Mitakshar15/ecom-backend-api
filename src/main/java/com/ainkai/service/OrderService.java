package com.ainkai.service;


import com.ainkai.exceptions.OrderException;
import com.ainkai.model.Address;
import com.ainkai.model.Order;
import com.ainkai.model.User;

import java.util.List;

public interface OrderService  {


    public Order createOrder(User user, Address shippingAddress);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId)throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder(Long orderId) throws OrderException;

    public Order cancledOrder(Long orderId) throws OrderException;

    public List<Order>getAllOrders();

    public void deleteOrder(Long orderId) throws OrderException;



}
