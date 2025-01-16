package com.ainkai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private  Order order;

    @ManyToOne
    private  Product product;

    private String size;

    private int quantity;

    private  Integer price;

    private Integer discountedPrice;

    private  Long userId;

    private LocalDateTime deliveryDate;

}
