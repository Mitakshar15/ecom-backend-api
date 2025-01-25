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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @Embedded //->
    @ElementCollection //->
    @CollectionTable(name="payment_information",joinColumns=@JoinColumn(name="user_id")) //->
    private List<PaymentInformation> paymentInformation = new ArrayList<>();

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL) //->
    @JsonIgnore //->
    private List<Rating> ratings = new ArrayList<>();

    @JsonIgnore //->
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL) //->
    private List<Review> reviews = new ArrayList<>();

    private LocalDateTime createdAt;
}
