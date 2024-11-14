package com.ainkai.service;

import com.ainkai.exceptions.OrderException;
import com.ainkai.model.Address;
import com.ainkai.model.Order;
import com.ainkai.model.User;
import com.ainkai.repository.CartRepo;
import org.springframework.stereotype.Service;
import com.ainkai.service.CartItemService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private  CartRepo cartRepo;
    private  CartItemService cartItemService;
    private  ProductService productService;


    public OrderServiceImpl(CartRepo cartRepo,CartItemService cartItemService,ProductService productService){

        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
        this.productService=productService;
    }

    @Override
    public Order createOrder(User user, Address shippingAdress) {



        return null;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return List.of();
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
