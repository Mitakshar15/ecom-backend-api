package com.ainkai.dto;

import com.ainkai.model.Rating;
import com.ainkai.model.Review;
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
public class ReviewResponseDTO {



    private Long id;


    private String review;

    private BasicUserInfoDTO user;

    private LocalDateTime createdAt;

    private Rating rating;

    public  static ReviewResponseDTO fromEntity(Review review){
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReview(review.getReview());
        dto.setId(review.getId());
        //Fetch the User from review, and store it to Data Transfer Layer
        dto.setUser(BasicUserInfoDTO.fromEntity(review.getUser()));
        dto.setCreatedAt(review.getCreatedAt());
        dto.setRating(review.getRating());
        return dto;
    }

    public static List<ReviewResponseDTO> fromEntityToList(List<Review> reviewList){
        List<ReviewResponseDTO> dtoList = new ArrayList<>();
        for(Review review : reviewList){
             dtoList.add(ReviewResponseDTO.fromEntity(review));
        }
        return dtoList;
    }



}
