package com.ainkai.service;

import com.ainkai.exceptions.InventoryException;
import com.ainkai.model.InventoryLog;
import com.ainkai.model.Sku;
import com.ainkai.repository.InventoryLogRepo;
import com.ainkai.repository.SkuRepository;
import com.ainkai.user.domain.Constants;
import com.ainkai.user.domain.InventoryAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final SkuRepository skuRepository;
    private final InventoryLogRepo inventoryLogRepo;

    @Override
    @Transactional(readOnly = true)
    public void validateStock(Long skuId, int requestedQuantity) throws InventoryException {
        Sku sku = findSkuOrThrow(skuId);
        if (sku.getQuantity() < requestedQuantity) {
            throw new InventoryException(Constants.INSUFFICIENT_STOCK_KEY,
                    String.format("Insufficient stock for SKU %s: available=%d, requested=%d",
                            sku.getSkuCode(), sku.getQuantity(), requestedQuantity));
        }
    }

    @Override
    @Transactional
    public void decrementStock(Long skuId, int quantity, String orderId) throws InventoryException {
        Sku sku = findSkuOrThrow(skuId);
        if (sku.getQuantity() < quantity) {
            throw new InventoryException(Constants.INSUFFICIENT_STOCK_KEY,
                    String.format("Insufficient stock for SKU %s: available=%d, requested=%d",
                            sku.getSkuCode(), sku.getQuantity(), quantity));
        }

        int quantityBefore = sku.getQuantity();
        sku.setQuantity(quantityBefore - quantity);
        skuRepository.save(sku);

        logInventoryChange(sku, InventoryAction.ORDER_PLACED, -quantity,
                quantityBefore, sku.getQuantity(), orderId, "Stock decremented for order");

        log.info("Stock decremented: SKU={}, orderId={}, before={}, after={}",
                sku.getSkuCode(), orderId, quantityBefore, sku.getQuantity());
    }

    @Override
    @Transactional
    public void restoreStock(Long skuId, int quantity, String orderId) throws InventoryException {
        Sku sku = findSkuOrThrow(skuId);
        int quantityBefore = sku.getQuantity();
        sku.setQuantity(quantityBefore + quantity);
        skuRepository.save(sku);

        logInventoryChange(sku, InventoryAction.ORDER_CANCELLED, quantity,
                quantityBefore, sku.getQuantity(), orderId, "Stock restored from cancelled order");

        log.info("Stock restored: SKU={}, orderId={}, before={}, after={}",
                sku.getSkuCode(), orderId, quantityBefore, sku.getQuantity());
    }

    @Override
    @Transactional
    public Sku restockSku(Long skuId, int quantity, String reason) throws InventoryException {
        if (quantity <= 0) {
            throw new InventoryException(Constants.REQUEST_ERROR_KEY, "Restock quantity must be positive");
        }
        Sku sku = findSkuOrThrow(skuId);
        int quantityBefore = sku.getQuantity();
        sku.setQuantity(quantityBefore + quantity);
        Sku saved = skuRepository.save(sku);

        logInventoryChange(sku, InventoryAction.RESTOCK, quantity,
                quantityBefore, saved.getQuantity(), null, reason);

        log.info("SKU restocked: SKU={}, added={}, before={}, after={}",
                sku.getSkuCode(), quantity, quantityBefore, saved.getQuantity());
        return saved;
    }

    @Override
    @Transactional
    public Sku adjustStock(Long skuId, int newQuantity, String reason) throws InventoryException {
        if (newQuantity < 0) {
            throw new InventoryException(Constants.REQUEST_ERROR_KEY, "Stock quantity cannot be negative");
        }
        Sku sku = findSkuOrThrow(skuId);
        int quantityBefore = sku.getQuantity();
        int change = newQuantity - quantityBefore;
        sku.setQuantity(newQuantity);
        Sku saved = skuRepository.save(sku);

        logInventoryChange(sku, InventoryAction.MANUAL_ADJUSTMENT, change,
                quantityBefore, saved.getQuantity(), null, reason);

        log.info("Stock adjusted: SKU={}, before={}, after={}, reason={}",
                sku.getSkuCode(), quantityBefore, newQuantity, reason);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sku> getLowStockSkus(int threshold) {
        return skuRepository.findByQuantityLessThanEqualAndQuantityGreaterThan(threshold, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sku> getOutOfStockSkus() {
        return skuRepository.findByQuantity(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryLog> getInventoryHistory(Long skuId) {
        return inventoryLogRepo.findBySkuIdOrderByCreatedAtDesc(skuId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(Long skuId, int quantity) {
        return skuRepository.findById(skuId)
                .map(sku -> sku.getQuantity() >= quantity)
                .orElse(false);
    }

    private Sku findSkuOrThrow(Long skuId) throws InventoryException {
        return skuRepository.findById(skuId)
                .orElseThrow(() -> new InventoryException(Constants.DATA_NOT_FOUND_KEY,
                        "SKU not found with id: " + skuId));
    }

    private void logInventoryChange(Sku sku, InventoryAction action, int quantityChange,
                                    int quantityBefore, int quantityAfter,
                                    String referenceId, String reason) {
        InventoryLog logEntry = new InventoryLog();
        logEntry.setSku(sku);
        logEntry.setAction(action);
        logEntry.setQuantityChange(quantityChange);
        logEntry.setQuantityBefore(quantityBefore);
        logEntry.setQuantityAfter(quantityAfter);
        logEntry.setReferenceId(referenceId);
        logEntry.setReason(reason);
        inventoryLogRepo.save(logEntry);
    }
}
