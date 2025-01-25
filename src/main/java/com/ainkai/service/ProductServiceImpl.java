package com.ainkai.service;

import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.ProductException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Category;
import com.ainkai.model.Product;
import com.ainkai.model.dtos.CreateProductRequest;
import com.ainkai.model.dtos.MultipleProductResponse;
import com.ainkai.model.dtos.ProductResponse;
import com.ainkai.model.dtos.UpdateProductRequest;
import com.ainkai.repository.CategoryRepo;
import com.ainkai.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ApiResponseBuilder builder;
    private final EcomApiUserMapper mapper;


    @Override
    public Product createProduct(CreateProductRequest request)throws ProductException {
         //Create A Product Here
        Category topLevel = categoryRepo.findByName(request.getTopLevelCategory());
        //Check if any other product exists with same title

        if(topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(request.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepo.save(topLevelCategory);
        }

        Category secondLevel = categoryRepo.findByNameAndParent(request.getSecondLevelCategory(),topLevel.getName());

        if(secondLevel ==null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);
            secondLevelCategory.setParentCategory(topLevel);
            secondLevel = categoryRepo.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepo.findByNameAndParent(request.getThirdLevelCategory(),secondLevel.getName());

        if(thirdLevel ==null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getThirdLevelCategory());
            thirdLevelCategory.setLevel(3);
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevel = categoryRepo.save(thirdLevelCategory);
        }


        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setBrand(request.getBrand());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setCategory(thirdLevel);
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setCreatedAt(LocalDateTime.now());
        product.setSizes(mapper.toSizeEntity(request.getSize()));
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = productRepo.save(product);
        return  savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepo.delete(product);

        return "::PRODUCT DELETED SUCCESSFULLY";
    }

    @Override
    public Product updateProduct(UpdateProductRequest request) throws ProductException {
        Product product  = findProductById(request.getProductId());
        if(request.getQuantity()!=0){
            product.setQuantity(request.getQuantity());
        }
        return productRepo.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        Optional<Product> opt = productRepo.findById(productId);

        if(opt.isPresent()){
            return  opt.get();
        }
        throw new ProductException("PRODUCT EXCEPTION ","PRODUCT NOT FOUND WITH ID " + productId);

    }

    @Override
    public List<Product> findProductByCategry(String category) {
        return List.of();
    }


    @Override
    public MultipleProductResponse getAllFilteredProducts(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        // Todo: Removed the Pagination Since OpenApi Does Not Support Page<>
        MultipleProductResponse productResponse = new MultipleProductResponse();
        List<Product> productList  = productRepo.filterProducts(category,minPrice,maxPrice,minDiscount,sort);
        if(!colors.isEmpty()){
            productList = productList.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }
        if(stock!=null){
            if(stock.equals("in_stock")){
                productList = productList.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
            }
            else if(stock.equals("out_of_stock")){
                productList = productList.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
            }
        }
        int startIndex = (pageNumber-1)*pageSize;
        if(startIndex<0){
            startIndex = 0;
        }
        int endIndex = Math.min(startIndex+pageSize,productList.size());
        productResponse.setProducts(builder.buildProductDtoList(productList.subList(startIndex,endIndex)));
        productResponse.setCurrentPage(pageNumber);
        productResponse.setTotalItems(productList.size());
        return productResponse;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> searchProduct(String query) {
        List<Product> productList = productRepo.findProductBySearchParam(query);
        if(productList.isEmpty()){
            throw new ProductException("PRODUCT EXCEPTION ","PRODUCT NOT FOUND");
        }
        return productList;
    }

}
