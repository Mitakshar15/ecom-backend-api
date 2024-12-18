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
public class DeleteOrderHistory {

    UserService userService;
    @Autowired
    OrderService orderService;
    OrderRepo orderRepo;


    public DeleteOrderHistory(OrderRepo orderRepo, OrderService orderService, UserService userService) {
        this.orderRepo = orderRepo;
        this.orderService = orderService;
        this.userService = userService;
    }

    @Scheduled(cron ="0 0 0 * * *")
    public void clearOrderHistoryAfterOneYear()throws OrderException{

        LocalDateTime checkDate = LocalDateTime.now().minusYears(1);
        List<Order> allOrder = orderRepo.findAll();

        for(Order order : allOrder){
            if(order.getCreatedAt().isBefore(checkDate)){
                orderRepo.deleteById(order.getId());
            }
        }
        System.out.println("OLD ORDERS DELETED SUCCESFULLY");

    }
}
