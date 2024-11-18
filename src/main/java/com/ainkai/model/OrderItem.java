package com.ainkai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    public OrderItem(LocalDateTime deliveryDate, Integer discountedPrice, Long id, Order order, Integer price, Product product, int quantity, String size, Long userId) {
        this.deliveryDate = deliveryDate;
        this.discountedPrice = discountedPrice;
        this.id = id;
        this.order = order;
        this.price = price;
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.userId = userId;
    }

    public OrderItem(){

    }
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Integer discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
