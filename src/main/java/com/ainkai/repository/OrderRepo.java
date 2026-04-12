package com.ainkai.repository;

import com.ainkai.model.Order;
import com.ainkai.user.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.OrderStatus IN :statuses ORDER BY o.createdAt DESC")
    List<Order> getUsersOrders(@Param("userId") Long userId, @Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> getAllUserOrders(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.OrderStatus = :status")
    List<Order> getOrdersByStatus(@Param("status") OrderStatus status);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.OrderStatus = :status")
    void deleteAllByStatus(@Param("status") OrderStatus status);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.createdAt < :cutoffDate")
    void deleteOrdersOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    List<Order> findAllByOrderByCreatedAtDesc();

}
