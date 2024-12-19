package com.ainkai.dto;

import com.ainkai.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;

    private String title;

    private  String description;

    private int price;

    private int discountedPrice;

    private int discountPercent;

    private int quantity;

    private String brand;

    private String color;

    private Set<Size> sizes = new HashSet<>();

    private String imageUrl;

    private List<Rating> ratings;

    private List<ReviewResponseDTO> reviews;

    private Category category;

    private LocalDateTime createdAt;

    public static ProductResponseDTO fromEntity(Product product){
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setColor(product.getColor());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setDiscountPercent(product.getDiscountPercent());
        dto.setQuantity(product.getQuantity());
        dto.setDescription(product.getDescription());
        dto.setRatings(product.getRatings());
        dto.setReviews(ReviewResponseDTO.fromEntityToList(product.getReviews()));
        dto.setCreatedAt(product.getCreatedAt());
        dto.setImageUrl(product.getImageUrl());
        dto.setTitle(product.getTitle());
        dto.setSizes(product.getSizes());
        return dto;
    }

    public static List<ProductResponseDTO> fromEntityToList(List<Product> productList){
       List<ProductResponseDTO> dtoList = new ArrayList<>();
       for(Product product : productList){
           dtoList.add(ProductResponseDTO.fromEntity(product));
       }

       return dtoList;
    }

}
