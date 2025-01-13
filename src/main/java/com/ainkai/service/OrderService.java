package com.ainkai.service;


import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Address;
import com.ainkai.model.Order;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddressDto;
import jakarta.mail.MessagingException;

import java.util.List;

public interface OrderService  {


    public Order createOrder(User user, AddressDto shippingAddress)throws ProductException;

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException, MessagingException;

    public Order confirmedOrder(Long orderId)throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder(Long orderId) throws OrderException;

    public Order cancledOrder(Long orderId) throws OrderException;

    public List<Order>getAllOrders();

    public void deleteOrder(Long orderId) throws OrderException;

    public  boolean isShippingAddressExists(Long userId,Address address);

}
