package com.ainkai.api.v1;

import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.InventoryException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.InventoryLog;
import com.ainkai.model.Sku;
import com.ainkai.model.dtos.*;
import com.ainkai.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {

    private final InventoryService inventoryService;
    private final ApiResponseBuilder builder;
    private final EcomApiUserMapper mapper;

    @PutMapping("/restock")
    public ResponseEntity<InventoryResponse> restockSkuHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestBody RestockRequest request) throws InventoryException {
        Sku sku = inventoryService.restockSku(request.getSkuId(), request.getQuantity(), request.getReason());
        InventoryResponse response = mapper.toInventoryResponse(
                builder.buildSuccessApiResponse("SKU restocked successfully"));
        response.setData(builder.buildInventorySkuDto(sku));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/adjust")
    public ResponseEntity<InventoryResponse> adjustStockHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestBody AdjustStockRequest request) throws InventoryException {
        Sku sku = inventoryService.adjustStock(request.getSkuId(), request.getNewQuantity(), request.getReason());
        InventoryResponse response = mapper.toInventoryResponse(
                builder.buildSuccessApiResponse("Stock adjusted successfully"));
        response.setData(builder.buildInventorySkuDto(sku));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<LowStockResponse> getLowStockHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<Sku> lowStockSkus = inventoryService.getLowStockSkus(threshold);
        LowStockResponse response = mapper.toLowStockResponse(
                builder.buildSuccessApiResponse("Low stock SKUs retrieved"));
        response.setData(builder.buildInventorySkuDtoList(lowStockSkus));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<LowStockResponse> getOutOfStockHandler(
            @RequestHeader("Authorization") String jwt) {
        List<Sku> outOfStockSkus = inventoryService.getOutOfStockSkus();
        LowStockResponse response = mapper.toLowStockResponse(
                builder.buildSuccessApiResponse("Out of stock SKUs retrieved"));
        response.setData(builder.buildInventorySkuDtoList(outOfStockSkus));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history/{skuId}")
    public ResponseEntity<InventoryHistoryResponse> getInventoryHistoryHandler(
            @PathVariable Long skuId,
            @RequestHeader("Authorization") String jwt) {
        List<InventoryLog> history = inventoryService.getInventoryHistory(skuId);
        InventoryHistoryResponse response = mapper.toInventoryHistoryResponse(
                builder.buildSuccessApiResponse("Inventory history retrieved"));
        response.setData(builder.buildInventoryLogDtoList(history));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
