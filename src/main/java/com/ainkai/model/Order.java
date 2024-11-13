package com.ainkai.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="order_id")
    private String orderId;

    @ManyToOne
    private  User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItems> orderItemsList  = new ArrayList<>();


    @Column(name="order_date")
    private LocalDateTime oderDate;

    @Column(name="delivery_date")
    private LocalDateTime deliveryDate;

    @OneToOne
    private Address shippingAddress;

    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

    @Column(name="total_price")
    private double totalPrice;

    private Integer totalDiscountedPrice;

    private Integer discount;

    @Column(name = "order_status")
    private String OrderStatus;

    private int toalItem;

    private  LocalDateTime createdAt;

    public Order(){

    }

    public Order(LocalDateTime createdAt, LocalDateTime deliveryDate, Integer discount, Long id, LocalDateTime oderDate, String orderId, List<OrderItems> orderItemsList, String orderStatus, PaymentDetails paymentDetails, Address shippingAddress, int toalItem, Integer totalDiscountedPrice, double totalPrice, User user) {
        this.createdAt = createdAt;
        this.deliveryDate = deliveryDate;
        this.discount = discount;
        this.id = id;
        this.oderDate = oderDate;
        this.orderId = orderId;
        this.orderItemsList = orderItemsList;
        OrderStatus = orderStatus;
        this.paymentDetails = paymentDetails;
        this.shippingAddress = shippingAddress;
        this.toalItem = toalItem;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOderDate() {
        return oderDate;
    }

    public void setOderDate(LocalDateTime oderDate) {
        this.oderDate = oderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItems> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItems> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getToalItem() {
        return toalItem;
    }

    public void setToalItem(int toalItem) {
        this.toalItem = toalItem;
    }

    public Integer getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public void setTotalDiscountedPrice(Integer totalDiscountedPrice) {
        this.totalDiscountedPrice = totalDiscountedPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
