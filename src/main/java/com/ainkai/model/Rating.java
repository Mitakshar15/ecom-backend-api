package com.ainkai.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "rating", indexes = {
    @Index(name = "idx_rating_product_id", columnList = "product_id"),
    @Index(name = "idx_rating_user_id", columnList = "user_id"),
    @Index(name = "idx_rating_product_user", columnList = "product_id, user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(name = "rating")
    private double rating;

    private LocalDateTime createdAt;


}
