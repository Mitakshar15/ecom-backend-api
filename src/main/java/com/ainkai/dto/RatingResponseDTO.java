package com.ainkai.dto;

import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDTO {



    private BasicUserInfoDTO user;

    private double rating;


    private LocalDateTime createdAt;


    public  static RatingResponseDTO fromEntity(Rating rating){
        RatingResponseDTO dto = new RatingResponseDTO();
        dto.setRating(rating.getRating());
        dto.setUser(BasicUserInfoDTO.fromEntity(rating.getUser()));
        dto.setCreatedAt(rating.getCreatedAt());
        return dto;
    }

    public  static List<RatingResponseDTO> fromEntityToList(List<Rating> ratingList){
        List<RatingResponseDTO> dtoList = new ArrayList<>();
        for(Rating rating : ratingList){
             dtoList.add(RatingResponseDTO.fromEntity(rating));
        }
        return dtoList;
    }
}
