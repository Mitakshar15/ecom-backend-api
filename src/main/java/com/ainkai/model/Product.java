package com.ainkai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name="title", nullable=false)
  private String title;

  @Column(name="description")
  private String description;

  @Column(name="brand")
  private String brand;

  @Column(name="image_url")
  private String imageUrl;

  @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
  @JsonIgnore
  private List<Sku> skus = new ArrayList<>();

  @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Rating> ratings  = new ArrayList<>();

  @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Review> reviews = new ArrayList<>();

  @Column(name="num_ratings")
  private int numRatings;


  @ManyToOne()
  @JoinColumn(name="category_id")
  private Category category;

  @Column(name="created_at", nullable=false, updatable=false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  // Helper methods to manage bidirectional relationship with SKU
  public void addSku(Sku sku) {
    skus.add(sku);
    sku.setProduct(this);
  }

  public void removeSku(Sku sku) {
    skus.remove(sku);
    sku.setProduct(null);
  }
}