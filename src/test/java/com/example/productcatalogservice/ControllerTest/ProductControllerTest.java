package com.example.productcatalogservice.ControllerTest;

import com.example.productcatalogservice.controllers.ProductController;
import com.example.productcatalogservice.dto.CategoryDto;
import com.example.productcatalogservice.dto.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductCatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ProductControllerTest {


    @Autowired
    ProductController controller;

    @Mock
    IProductCatalogService productService;

    @Captor
    ArgumentCaptor<Long> idCaptor;

    private ProductDto productDto;
    private Product mockProduct;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    void setUp(){
        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(100.0);
        productDto.setDescription("Test Description");
        productDto.setImageUrl("http://testimage.com");
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
        productDto.setCategory(categoryDto);

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(100.0);
        mockProduct.setDescription("Test Description");
        mockProduct.setImageUrl("http://testimage.com");
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        mockProduct.setCategory(category);

    }

    //methodName_when_then
    @Test
    public void TestgetProductById_runsSuccessfully() {

        //arrange
        Long productId = 1L;

        when(productService.getProductById(productId)).thenReturn(mockProduct);

        //act
        ResponseEntity<ProductDto> response = controller.getProductById(productId);

        //assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService,times(1)).getProductById(productId);// productservice is only called once in the function getProductById

    }

    @Test
    public void TestgetProductById_whenProductIdZero_ThrowsIllegalArgumentException() {
        Long productId = 0L;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {controller.getProductById(productId);});
        assertEquals("Please provide correct ID", exception.getMessage());

    }

    @Test
    public void TestgetProductById_whenExceptionInProductService_ThrowsException() {

        Long productId = 1L;
        when(productService.getProductById(productId)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> {controller.getProductById(productId);});

    }

    @Test
    public void TestgetProductById_WhenProductServiceReturnsNull_404(){
        when(productService.getProductById(1L)).thenReturn(null);
        MultiValueMap header = new LinkedMultiValueMap<>();
        header.add("message", "product not available");
        ResponseEntity<ProductDto> response = controller.getProductById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
        assertEquals(header, response.getHeaders());

    }

    @Test
    public void TestgetProducts_runsSuccessfully() {
        //arrange
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("Iphone");
        products.add(product);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Iphone2");
        products.add(product2);
        when(productService.getProducts()).thenReturn(products);

        //act
        List<ProductDto> response = controller.getProducts();

        //assert
        assertNotNull(response);
        assertEquals(products.size(), response.size());
        verify(productService,times(1)).getProducts();// productservice is only called once


    }

    @Test
    public void TestcreateProduct_runsSuccessfully() {

        //arrange
        when(productService.createProduct(any(Product.class))).thenReturn(mockProduct);

        //act
        ProductDto response = controller.createProduct(productDto);

        //assert
        assertNotNull(response);
        verify(productService,times(1)).createProduct(any(Product.class));// productservice is only called once

    }

    @Test
    public void TestreplaceProduct_runsSuccessfully() {

        //arrange
        when(productService.replaceProduct(anyLong(),any(Product.class))).thenReturn(mockProduct);

        //act
        Long productId = 1L;
        ProductDto response = controller.replaceProduct(productId,productDto);

        //assert
        assertNotNull(response);
        verify(productService,times(1)).replaceProduct(anyLong(),any(Product.class));// productservice is only called once

    }

    @Test
    public void TestdeleteProduct_runsSuccessfully() {
        when(productService.deleteProduct(anyLong())).thenReturn(mockProduct);
        Long productId = 1L;
        ResponseEntity<ProductDto> response = controller.deleteProduct(productId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productId, response.getBody().getId());

        verify(productService,times(1)).deleteProduct(anyLong());// productservice is only called once


    }

    @Test
    public void TestdeleteProduct_withInvalidProductId_throwsIllegalArgumentException() {

        Long productId = 0L;
        Exception  exception = assertThrows(IllegalArgumentException.class, () -> {controller.deleteProduct(productId);});
        assertNotNull(exception);
        assertEquals("Please provide correct ID", exception.getMessage());

    }

    @Test
    public void TestdeleteProduct_withresponseFromServiceNullProduct_Return404() {

        //arrange
        when(productService.deleteProduct(anyLong())).thenReturn(null);
        //act
        ResponseEntity<ProductDto> response = controller.deleteProduct(1L);
        //assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    public void TestgetProductById_checkTheProductServiceCalledByCorrectId_runsSuccessfully(){
        //arrange
        when(productService.getProductById(anyLong())).thenReturn(mockProduct);

        //act
        controller.getProductById(mockProduct.getId());

        //assert
        verify(productService).getProductById(idCaptor.capture());
        assertEquals(mockProduct.getId(),idCaptor.getValue());

    }



}
