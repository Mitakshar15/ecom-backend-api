package com.ainkai.request;

import com.ainkai.model.Product;
import com.ainkai.model.Review;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewRequest {

    private Long productId;
    private Product product;
    private String review;

}
