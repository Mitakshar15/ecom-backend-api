package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {


 public Product createProduct(CreateProductRequest request)throws ProductException;

 public  String deleteProduct(Long productId) throws ProductException;

 public Product updateProduct(Long productId, Product request) throws ProductException;

 public  Product findProductById(Long productId)throws  ProductException;

 public List<Product> findProductByCategry(String category);

 public Page<Product> getAllProduct(String category, List<String> colors,List<String> sizes,Integer minPrice, Integer maxPrice,Integer minDiscount,String sort, String stock, Integer pageNumber,Integer pageSize);

 public List<Product> getAllProducts();

 public List<Product> searchProduct(String query);


}
