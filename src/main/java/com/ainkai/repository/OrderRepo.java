package com.ainkai.repository;

import com.ainkai.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {

    /*  NOTE: ADD A METHOD TO RETURN ONLY A LIST OF ORDER_ID INORDER TO HIDE THE USERDETAILS FROM THE CONSOLE */


    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.OrderStatus = PLACED OR o.OrderStatus = CONFIRMED OR o.OrderStatus = SHIPPED OR o.OrderStatus = DELIVERED OR o.OrderStatus = PENDING OR o.OrderStatus = CANCELLED)")
    public List<Order> getUsersOrders(@Param("userId") Long userId);

    List<Order> findAllByOrderByCreatedAtDesc();


}
