package com.ainkai.repository;

import com.ainkai.model.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryLogRepo extends JpaRepository<InventoryLog, Long> {

    @Query("SELECT il FROM InventoryLog il WHERE il.sku.id = :skuId ORDER BY il.createdAt DESC")
    List<InventoryLog> findBySkuIdOrderByCreatedAtDesc(@Param("skuId") Long skuId);

    @Query("SELECT il FROM InventoryLog il WHERE il.referenceId = :referenceId ORDER BY il.createdAt DESC")
    List<InventoryLog> findByReferenceId(@Param("referenceId") String referenceId);
}
