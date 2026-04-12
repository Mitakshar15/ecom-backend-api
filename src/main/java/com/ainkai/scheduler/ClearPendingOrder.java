package com.ainkai.scheduler;

import com.ainkai.repository.OrderRepo;
import com.ainkai.user.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClearPendingOrder {

    private final OrderRepo orderRepo;

    @Scheduled(cron = "0 0 0 LW * *")
    @Transactional
    public void clearOrders() {
        orderRepo.deleteAllByStatus(OrderStatus.PENDING);
        log.info("Deleted all pending orders");
    }
}
