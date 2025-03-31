package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dto.UserDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repository.ProductRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class ProductCatalogStorageService implements IProductCatalogService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id).get();

    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).get();
        productRepository.deleteById(productId);
        return product;
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        Product product1 = productRepository.findById(productId).get();
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setDescription(product.getDescription());
        product1.setCategory(product.getCategory());
        Product product2 = productRepository.save(product1);
        return product2;
    }

    @Override
    public Product getProductBasedOnUserScope(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()) return null;

        RestTemplate restTemplate = new RestTemplate();
        UserDto userDto = restTemplate
                .getForEntity("http://localhost:9000/users/{userId}", UserDto.class,userId).getBody();

        if(userDto != null) {
            System.out.println(userDto.getEmail());
            return optionalProduct.get();
        }

        return null;
    }
}
