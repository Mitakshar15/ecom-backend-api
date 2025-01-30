package com.ainkai.service;

import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.ProductException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Category;
import com.ainkai.model.Product;
import com.ainkai.model.Sku;
import com.ainkai.model.dtos.CreateProductRequest;
import com.ainkai.model.dtos.MultipleProductResponse;
import com.ainkai.model.dtos.ProductResponse;
import com.ainkai.model.dtos.UpdateProductRequest;
import com.ainkai.repository.CategoryRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.repository.SkuRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SkuRepository skuRepository;


    @Override
    @Transactional
    public Product createProduct(CreateProductRequest request) throws ProductException {
            Category topLevel = handleTopLevelCategory(request.getTopLevelCategory());
            Category secondLevel = handleSecondLevelCategory(request.getSecondLevelCategory(), topLevel);
            Category thirdLevel = handleThirdLevelCategory(request.getThirdLevelCategory(), secondLevel);
            Product product = getOrCreateProduct(request, thirdLevel);
            Sku sku = createSku(request, product);
            return productRepo.save(product);
    }
    /**
     * Handles top level category creation or retrieval
     */
    private Category handleTopLevelCategory(String categoryName) {
        Category category = categoryRepo.findByName(categoryName);
        if (category == null) {
            category = new Category();
            category.setName(categoryName);
            category.setLevel(1);
            return categoryRepo.save(category);
        }
        return category;
    }
    /**
     * Handles second level category creation or retrieval
     */
    private Category handleSecondLevelCategory(String categoryName, Category parentCategory) {
        Category category = categoryRepo.findByNameAndParent(categoryName, parentCategory.getName());
        if (category == null) {
            category = new Category();
            category.setName(categoryName);
            category.setLevel(2);
            category.setParentCategory(parentCategory);
            return categoryRepo.save(category);
        }
        return category;
    }
    /**
     * Handles third level category creation or retrieval
     */
    private Category handleThirdLevelCategory(String categoryName, Category parentCategory) {
        Category category = categoryRepo.findByNameAndParent(categoryName, parentCategory.getName());
        if (category == null) {
            category = new Category();
            category.setName(categoryName);
            category.setLevel(3);
            category.setParentCategory(parentCategory);
            return categoryRepo.save(category);
        }
        return category;
    }
    /**
     * Gets existing product or creates new one
     */
    private Product getOrCreateProduct(CreateProductRequest request, Category category) {
        Product product;
        if (productRepo.existsProductByTitleAndBrand(request.getTitle(), request.getBrand())) {
            product = productRepo.findProductByTitleAndBrand(request.getTitle(), request.getBrand());
        } else {
            product = new Product();
        }

        updateProduct(product, request, category);
        return product;
    }
    /**
     * Updates product with request data
     */
    private void updateProduct(Product product, CreateProductRequest request, Category category) {
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setImageUrl(request.getImageUrl());
    }
    /**
     * Creates and saves a new SKU
     */
    private Sku createSku(CreateProductRequest request, Product product) throws ProductException {
        Sku sku = new Sku();
        String skuCode = generateSkuCode(request.getBrand(), request.getColor(), request.getTitle());
        if(skuRepository.existsBySkuCode(skuCode)){
            throw new ProductException("400","Error creating sku: Duplicate Sku Found " + skuCode);
        }
        sku.setSkuCode(skuCode);
        sku.setPrice(request.getPrice());
        sku.setQuantity(request.getQuantity());
        sku.setColor(request.getColor());
        sku.setDiscountPercent(request.getDiscountPercent());
        sku.setDiscountedPrice(request.getDiscountedPrice());
        sku.setSize("M");
        sku.setProduct(product);
        product.getSkus().add(sku);
        return skuRepository.save(sku);
    }
    /**
     * Generates SKU code
     */
    private String generateSkuCode(String brand, String color, String title) {
        return String.format("%s %c %s", brand, color.charAt(0), title.charAt(title.length() - 1)).toUpperCase();
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSkus().clear();
        productRepo.delete(product);

        return "::PRODUCT DELETED SUCCESSFULLY";
    }

    @Override
    public Product updateProduct(UpdateProductRequest request) throws ProductException {
        Product product  = findProductById(request.getSkuId());
        if(product==null){
            throw new ProductException("404","Product Not Found");
        }
        if(request.getQuantity()!=0){
            product.getSkus().get(0).setQuantity(request.getQuantity());
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

    // Get initial product list with SKUs
    List<Product> productList = productRepo.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

    // Filter based on SKU attributes
    productList = productList.stream()
            .filter(product -> {
                // Filter SKUs based on color
                if (!colors.isEmpty()) {
                    boolean hasMatchingColor = product.getSkus().stream()
                            .anyMatch(sku -> colors.stream()
                                    .anyMatch(c -> c.equalsIgnoreCase(sku.getColor())));
                    if (!hasMatchingColor) return false;
                }

                // Filter SKUs based on stock
                if (stock != null) {
                    if (stock.equals("in_stock")) {
                        return product.getSkus().stream().anyMatch(sku -> sku.getQuantity() > 0);
                    } else if (stock.equals("out_of_stock")) {
                        return product.getSkus().stream().allMatch(sku -> sku.getQuantity() < 1);
                    }
                }

                return true;
            })
            .collect(Collectors.toList());

    // Apply pagination
    int startIndex = (pageNumber - 1) * pageSize;
    if (startIndex < 0) {
        startIndex = 0;
    }
    int endIndex = Math.min(startIndex + pageSize, productList.size());

    productResponse.setProducts(builder.buildProductDtoList(productList.subList(startIndex, endIndex)));
    productResponse.setCurrentPage(pageNumber);
    productResponse.setTotalItems(productList.size());

    return productResponse;
}


    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> searchProduct(String query) throws ProductException {
        List<Product> productList = productRepo.findProductBySearchParam(query);
        if(productList.isEmpty()){
            throw new ProductException("PRODUCT EXCEPTION ","PRODUCT NOT FOUND");
        }
        return productList;
    }

}
