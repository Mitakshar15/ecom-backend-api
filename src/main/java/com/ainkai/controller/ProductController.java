package com.ainkai.controller;


import com.ainkai.dto.ProductResponseDTO;
import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.request.CreateProductRequest;
import com.ainkai.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDTO>> findProductByCategoryHandler(@RequestParam String category, @RequestParam List<String> color,@RequestParam List<String> size, @RequestParam Integer minPrice,@RequestParam Integer maxPrice,@RequestParam Integer minDiscount,@RequestParam String sort,@RequestParam String stock,@RequestParam Integer pageNumber, @RequestParam Integer pageSize){

        Page<Product> res = productService.getAllProduct(category,color,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize);
        Page<ProductResponseDTO> responseDTO = res.map(product -> {
            return ProductResponseDTO.fromEntity(product);
        });

        System.out.println("Complete Products");
        return  new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);

    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<ProductResponseDTO> findProductByIdHandler( @PathVariable Long productId)throws ProductException {

        Product product  = productService.findProductById(productId);

        return  new ResponseEntity<>(ProductResponseDTO.fromEntity(product),HttpStatus.ACCEPTED);

    }

    @GetMapping("/products/search/{q}")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){

        return  null;
    }


}
