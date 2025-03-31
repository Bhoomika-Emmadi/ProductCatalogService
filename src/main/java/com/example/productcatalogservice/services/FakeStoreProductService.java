package com.example.productcatalogservice.services;

import com.example.productcatalogservice.client.FakeStoreAPIClient;
import com.example.productcatalogservice.dto.FakeStoreProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements IProductCatalogService {

    @Autowired
    RestTemplateBuilder restTemplatebuilder;

    @Autowired
    FakeStoreAPIClient fakeStoreAPIClient;

    @Value("${fakeStore.url}")
    private String fakeStoreURL;

//    RestTemplate restTemplate = restTemplatebuilder.build();
    static List<Product> productList = new ArrayList<>();


    public List<Product> getProducts() {
        FakeStoreProductDto fakeStoreProductDtoList[] = fakeStoreAPIClient.getProducts();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtoList){
            productList.add(fromFakeStoreDto(fakeStoreProductDto));
        }
        return productList;
    }

    public Product getProductById( Long id){
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.getProductById(id);
        return fromFakeStoreDto(fakeStoreProductDto);
    }
    public Product createProduct( Product product){
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.createProduct(fromProduct(product));
        return fromFakeStoreDto(fakeStoreProductDto);
    }

    public Product deleteProduct(Long productId){
        if(fakeStoreAPIClient.deleteProduct(productId) == null) {
            return null;
        }
        return fromFakeStoreDto(fakeStoreAPIClient.deleteProduct(productId));

    }

    public Product replaceProduct(Long productId,  Product product) {
        FakeStoreProductDto fakeStoreProductDto = fromProduct(product);
        return fromFakeStoreDto(fakeStoreAPIClient.replaceProduct(productId, fakeStoreProductDto));
    }

    public <T> ResponseEntity<T> requestForEntity(String url,HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate =restTemplatebuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private Product fromFakeStoreDto(FakeStoreProductDto fakeStoreProductDto){

        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setImageUrl(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setName(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreProductDto fromProduct(Product product){
       FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
       fakeStoreProductDto.setId(product.getId());
       fakeStoreProductDto.setImage(product.getImageUrl());
       fakeStoreProductDto.setDescription(product.getDescription());
       fakeStoreProductDto.setTitle(product.getName());
       if(product.getCategory() != null){
           fakeStoreProductDto.setCategory(product.getCategory().getName());
       }
       return fakeStoreProductDto;
    }

    @Override
    public Product getProductBasedOnUserScope(Long productId, Long userId) {
        return null;
    }


}
