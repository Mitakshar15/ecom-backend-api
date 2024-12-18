package com.ainkai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")

public class User {
    
    //ID Generation
    @Id //->
    @GeneratedValue(strategy = GenerationType.AUTO) //->
    private Long id;
    
     @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;


    @Column(name = "password")
    private String password;
    
    @Column(name = "email")
    private String email;


    @Column(name = "role")
    private String role;


    @Column(name = "mobile")
    private String mobile;
    
    //Address
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    //New Table For Payment_Info
    @Embedded //->
    @ElementCollection //->
    @CollectionTable(name="payment_information",joinColumns=@JoinColumn(name="user_id")) //->
    private List<PaymentInformation> paymentInformation = new ArrayList<>();

    //Ratings
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL) //->
    @JsonIgnore //->
    private List<Rating> ratings = new ArrayList<>();
    

    //New Table for review
    @JsonIgnore //->
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL) //->
    private List<Review> reviews = new ArrayList<>();

    
    //USer Creation Date
    private LocalDateTime createdAt;


    //Constructor
    public User(){

    }

    //Constructor With Params
    public User(List<Address> addresses, LocalDateTime createdAt, String email, String firstName, Long id, String lastName, String mobile, String password, List<PaymentInformation> paymentInformation, List<Rating> ratings, List<Review> reviews, String role) {
        this.addresses = addresses;
        this.createdAt = createdAt;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.mobile = mobile;
        this.password = password;
        this.paymentInformation = paymentInformation;
        this.ratings = ratings;
        this.reviews = reviews;
        this.role = role;
    }



    //getters & Setters
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public List<Address> getAddresses() {
        return addresses;
    }


    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }


    public List<PaymentInformation> getPaymentInformation() {
        return paymentInformation;
    }


    public void setPaymentInformation(List<PaymentInformation> paymentInformation) {
        this.paymentInformation = paymentInformation;
    }


    public List<Rating> getRatings() {
        return ratings;
    }


    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }


    public List<Review> getReviews() {
        return reviews;
    }


    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }




}
