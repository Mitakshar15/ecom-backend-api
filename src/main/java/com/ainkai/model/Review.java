package com.ainkai.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    private String review;

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonIgnore
    private Product product;
    

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime createdAt;

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="rating",nullable=false)
    private Rating rating;



    
    public Review(){

    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Review(Long id, String review, Product product, User user, LocalDateTime createdAt) {
        this.id = id;
        this.review = review;
        this.product = product;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
