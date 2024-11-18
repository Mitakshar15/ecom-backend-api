package com.ainkai.service;

import com.ainkai.model.OrderItem;
import com.ainkai.repository.OrderItemRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    private OrderItemRepo orderItemRepo;

    public OrderItemServiceImpl(OrderItemRepo orderItemRepo) {
        this.orderItemRepo = orderItemRepo;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {

        return orderItemRepo.save(orderItem);

    }
}
