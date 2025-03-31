package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IProductCatalogService {
    public List<Product> getProducts();
    public Product getProductById( Long id);
    public Product createProduct( Product product);

    Product deleteProduct(Long productId);

    Product replaceProduct(Long productId, Product product);
    Product getProductBasedOnUserScope(Long productId,Long userId);
}
