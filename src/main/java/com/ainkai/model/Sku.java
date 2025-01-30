/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skus", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sku_code"})
})
public class Sku {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="sku_code", nullable=false, unique=true)
    private String skuCode;

    @Column(name="color", nullable=false)
    private String color;

    @Column(name="size", nullable=false)
    private String size;

    @Column(name="price", nullable=false)
    private Integer price;

    @Column(name="discounted_price")
    private Integer discountedPrice;

    @Column(name="discount_percent")
    private int discountPercent;

    @Column(name="quantity", nullable=false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @JsonIgnore
    private Product product;

    // Additional fields or methods can be added here as needed
}