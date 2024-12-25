package com.ainkai.controller;


import com.ainkai.dto.ProductResponseDTO;
import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.request.CreateProductRequest;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> createProductHandler(@RequestBody CreateProductRequest request)throws ProductException{

        Product product = productService.createProduct(request);

        return new ResponseEntity<ProductResponseDTO>(ProductResponseDTO.fromEntity(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {

     productService.deleteProduct(productId);
     ApiResponse response = new ApiResponse();
     response.setMessage("Product Deleted Succesfully");
     response.setStatus(true);
     return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @GetMapping("/all")
    public  ResponseEntity<List<ProductResponseDTO>> findAllProductHandler(){

        List<Product> products = productService.getAllProducts();
        return  new ResponseEntity<>(ProductResponseDTO.fromEntityToList(products),HttpStatus.OK);

    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ProductResponseDTO> updateProductHandler(@RequestBody Product req,@PathVariable Long productId)throws  ProductException{

        Product product   = productService.updateProduct(productId,req);
        return  new ResponseEntity<>(ProductResponseDTO.fromEntity(product),HttpStatus.CREATED);
    }


    @PostMapping("/create")
    public  ResponseEntity<ApiResponse> createMultipleProductHandler(@RequestBody CreateProductRequest[] requests) throws  ProductException{


        for(CreateProductRequest product : requests){
            productService.createProduct(product);
        }

        ApiResponse response = new ApiResponse("Products Created Succesfully",true);
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);


    }






}
