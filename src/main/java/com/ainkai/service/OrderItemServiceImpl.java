package com.ainkai.service;

import com.ainkai.model.OrderItem;
import com.ainkai.repository.OrderItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private OrderItemRepo orderItemRepo;

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepo.save(orderItem);
    }
}
