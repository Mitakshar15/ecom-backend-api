/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.repository;

import com.ainkai.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {

    boolean existsBySkuCode(String skuCode);

    List<Sku> findByQuantityLessThanEqualAndQuantityGreaterThan(int threshold, int floor);

    List<Sku> findByQuantity(int quantity);

    @Query("SELECT s FROM Sku s WHERE s.product.id = :productId")
    List<Sku> findByProductId(@Param("productId") Long productId);
}
