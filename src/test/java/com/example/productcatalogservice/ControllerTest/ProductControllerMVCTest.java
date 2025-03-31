package com.example.productcatalogservice.ControllerTest;

import com.example.productcatalogservice.controllers.ProductController;
import com.example.productcatalogservice.dto.ProductDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductCatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@WebMvcTest(ProductController.class)
public class ProductControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IProductCatalogService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;


    @Test
    public void TestGetAllProducts_RunsSuccessfully() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk());

    }

    @Test
    public void TestCreateProducts_RunsSuccessfully() throws Exception {
        Product product1 = new Product();
        product1.setName("Iphone12");
        product1.setId(1L);
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Iphone12");
        when(productService.createProduct(any(Product.class))).thenReturn(product1);
        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productDto)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Iphone12"))
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    public void TestGetProductById_RunsSuccessfully() throws Exception {

        Product product1 = new Product();
        product1.setName("Iphone12");
        product1.setId(1L);
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Iphone12");

        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/products").param("id","1L"))
                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(productDto)));
;

    }

    @Test
    public void TestGetAllProducts_RunsSuccessfully_returnAllProducts() throws Exception {
        //Arrange
        Product product1 = new Product();
        product1.setName("Iphone12");
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("MacBook");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        when(productService.getProducts()).thenReturn(products);

        List<ProductDto> dtos = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Iphone12");
        dtos.add(productDto);
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("MacBook");
        dtos.add(productDto2);


        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper
                        .writeValueAsString(dtos)))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].length()").value(3))
                .andExpect(jsonPath("$[1].name").value("MacBook"));

    }

}
