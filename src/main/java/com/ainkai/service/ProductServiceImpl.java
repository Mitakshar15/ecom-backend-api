package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Category;
import com.ainkai.model.Product;
import com.ainkai.repository.CategoryRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private ProductRepo productRepo;
    private UserService userService;
    private CategoryRepo categoryRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo,CategoryRepo categoryRepo,UserService userService) {
        this.productRepo=productRepo;
        this.userService=userService;
        this.categoryRepo=categoryRepo;
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
         //Create A Product Here
        Category topLevel = categoryRepo.findByName(request.getTopLevelCategory());

        if(topLevel != null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(request.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepo.save(topLevelCategory);
        }

        Category secondLevel = categoryRepo.findByNameAndParent(request.getSecondLevelCategory(),topLevel.getName());

        if(secondLevel !=null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepo.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepo.findByNameAndParent(request.getThirdLevelCategory(),secondLevel.getName());

        if(thirdLevel !=null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getThirdLevelCategory());
            thirdLevelCategory.setLevel(3);

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
        product.setSizes(request.getSize());
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = productRepo.save(product);


        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepo.delete(product);

        return "::PRODUCT DELETED SUCCESSFULLY";
    }

    @Override
    public Product updateProduct(Long productId, Product request) throws ProductException {

        Product product  = findProductById(productId);

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
        throw new ProductException("PRODUCT NOT FOUND WITH ID"+productId);


    }

    @Override
    public List<Product> findProductByCategry(String category) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {



        Pageable pageable = PageRequest.of(pageNumber,pageSize);


        List<Product> productList  = productRepo.filterProducts(category,minPrice,maxPrice,minDiscount,sort);

        if(!colors.isEmpty()){
            productList = productList.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }
        if(stock!=null){
            if(stock.equals("in_stock")){
                productList = productList.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
            }
            else if(stock.equals("out_of_stock")){
                productList = productList.stream().filter(p->p.getQuantity()<1).collect(Collectors.toUnmodifiableList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(),productList.size());

        List<Product> pageContent = productList.subList(startIndex,endIndex);


        Page<Product> filteredProducts = new PageImpl<>(pageContent,pageable,productList.size());

        return filteredProducts;
    }
}
