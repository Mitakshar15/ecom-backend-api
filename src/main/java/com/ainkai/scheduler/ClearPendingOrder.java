package com.ainkai.scheduler;

import com.ainkai.exceptions.OrderException;
import com.ainkai.model.Order;
import com.ainkai.repository.OrderRepo;
import com.ainkai.service.OrderService;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ClearPendingOrder {

    UserService userService;
    @Autowired
    OrderService orderService;
    OrderRepo orderRepo;


    public ClearPendingOrder(OrderRepo orderRepo, OrderService orderService, UserService userService) {
        this.orderRepo = orderRepo;
        this.orderService = orderService;
        this.userService = userService;
    }

    @Scheduled(cron ="0 0/10 * * * *")
    public void clearOrders() throws OrderException {

          List<Order> pendingOrders = orderRepo.getUsersPendingOrders();


        for(Order o : pendingOrders){

         orderService.deleteOrder(o.getId());

        }
        System.out.println("DELETED ALL THE PENDING ORDERS AT - " + LocalDateTime.now());

    }




}
