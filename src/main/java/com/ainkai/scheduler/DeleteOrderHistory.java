package com.ainkai.scheduler;

import com.ainkai.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteOrderHistory {

    private final OrderRepo orderRepo;

    @Scheduled(cron = "0 0 0 LW * *")
    @Transactional
    public void clearOrderHistoryAfterOneYear() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusYears(1);
        orderRepo.deleteOrdersOlderThan(cutoffDate);
        log.info("Deleted orders older than {}", cutoffDate);
    }
}
