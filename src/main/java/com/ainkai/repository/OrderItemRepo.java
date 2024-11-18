package com.ainkai.repository;

import com.ainkai.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
