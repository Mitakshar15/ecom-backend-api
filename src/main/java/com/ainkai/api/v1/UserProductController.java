package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1ProductControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Product;
import com.ainkai.model.dtos.MultipleProductResponse;
import com.ainkai.model.dtos.ProductResponse;
import com.ainkai.repository.CartRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.service.CartService;
import com.ainkai.service.ProductService;
import com.ainkai.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserProductController implements EcomApiV1ProductControllerApi {

    private final ProductService productService;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;


    public ResponseEntity<MultipleProductResponse> findProductByCategoryHandler(@RequestParam String category, @RequestParam List<String> color, @RequestParam List<String> size, @RequestParam Integer minPrice, @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort, @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
        MultipleProductResponse response = mapper.toMultipleProductResponse(builder.buildSuccessApiResponse("FIND FILTERED PRODUCTS SUCCESS"));
        response.products(builder.buildProductDtoList(productService.getAllFilteredProducts(category,color,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ProductResponse> getProductByIdHandler(@PathVariable("productId")Long productId){
        Product product = productService.findProductById(productId);
        ProductResponse productResponse = mapper.toProductResponse(builder.buildSuccessApiResponse("GET PRODUCT SUCCESS"));
        productResponse.product(mapper.toProductDto(product));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //TODO: Controller To Handle Search Products



}
