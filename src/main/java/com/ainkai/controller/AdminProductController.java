package com.ainkai.controller;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.request.CreateProductRequest;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/products/")
    public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest request){

        Product product = productService.createProduct(request);

        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {

     productService.deleteProduct(productId);
     ApiResponse response = new ApiResponse();
     response.setMessage("Product Deleted Succesfully");
     response.setStatus(true);
     return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @GetMapping("/all")
    public  ResponseEntity<List<Product>> findAllProductHandler(){

        List<Product> products = productService.getAllProducts();
        return  new ResponseEntity<>(products,HttpStatus.OK);

    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProductHandler(@RequestBody Product req,@PathVariable Long productId)throws  ProductException{

        Product product   = productService.updateProduct(productId,req);
        return  new ResponseEntity<>(product,HttpStatus.CREATED);
    }






}
