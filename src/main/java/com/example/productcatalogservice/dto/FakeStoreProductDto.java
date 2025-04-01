package com.example.productcatalogservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter@Getter
public class FakeStoreProductDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private int price;
    private String category;
    private String image;

}
