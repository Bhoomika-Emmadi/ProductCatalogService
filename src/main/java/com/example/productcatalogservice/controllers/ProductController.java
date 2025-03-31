package com.example.productcatalogservice.controllers;


import com.example.productcatalogservice.dto.CategoryDto;
import com.example.productcatalogservice.dto.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    IProductCatalogService productService;

// we need /getAllProducts, /getProductById, / createProduct, /deleteProduct

    @GetMapping("/products")
    public List<ProductDto> getProducts(){
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> productList = productService.getProducts();
        for (Product product : productList) {
            productDtoList.add(fromProduct(product));
        }

        return productDtoList;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId){ //also try with @QueryParam

        if(productId <=0){
            throw new IllegalArgumentException("Please provide correct ID");
        }
        Product product = productService.getProductById(productId);
        MultiValueMap header = new LinkedMultiValueMap<>();

        if(product == null){
            header.add("message", "product not available");
            return new ResponseEntity<>(null, header, HttpStatus.NOT_FOUND);
        }
        ProductDto productDto =fromProduct(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);

    }

    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody ProductDto productDto){
        Product product = fromProductDto(productDto);
        Product response = productService.createProduct(product);
        ProductDto responseDto = fromProduct(response);
        return responseDto;

    }

    @PutMapping("/products/{id}")
    public ProductDto replaceProduct(@PathVariable("id") Long productId, @RequestBody ProductDto productDto){
        return fromProduct(productService.replaceProduct(productId, fromProductDto(productDto)));
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable("id") Long productId){
        if(productId <=0){
            throw new IllegalArgumentException("Please provide correct ID");
        }
        Product product = productService.deleteProduct(productId);
        if(product == null ){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fromProduct(product), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/{userId}")
    public ProductDto getProductDetailsBasedOnUserScope(@PathVariable Long productId,@PathVariable Long userId) {
        Product product = productService.getProductBasedOnUserScope(productId,userId);
        return fromProduct(product);
    }

    private ProductDto fromProduct(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        CategoryDto categoryDto = new CategoryDto();
        if(product.getCategory() != null){
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }else{
            productDto.setCategory(null);
        }

        return productDto;
    }



    private Product fromProductDto(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        Category category = new Category();
        if(productDto.getCategory() != null){
            category.setId(productDto.getCategory().getId());
            category.setName(productDto.getCategory().getName());
            category.setDescription(productDto.getCategory().getDescription());
            product.setCategory(category);
        }else{
            product.setCategory(null);
        }

        return product;
    }

}
