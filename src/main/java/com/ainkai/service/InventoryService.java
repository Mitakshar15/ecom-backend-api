package com.ainkai.service;

import com.ainkai.exceptions.InventoryException;
import com.ainkai.model.InventoryLog;
import com.ainkai.model.Sku;

import java.util.List;

public interface InventoryService {

    void validateStock(Long skuId, int requestedQuantity) throws InventoryException;

    void decrementStock(Long skuId, int quantity, String orderId) throws InventoryException;

    void restoreStock(Long skuId, int quantity, String orderId) throws InventoryException;

    Sku restockSku(Long skuId, int quantity, String reason) throws InventoryException;

    Sku adjustStock(Long skuId, int newQuantity, String reason) throws InventoryException;

    List<Sku> getLowStockSkus(int threshold);

    List<Sku> getOutOfStockSkus();

    List<InventoryLog> getInventoryHistory(Long skuId);

    boolean isInStock(Long skuId, int quantity);
}
