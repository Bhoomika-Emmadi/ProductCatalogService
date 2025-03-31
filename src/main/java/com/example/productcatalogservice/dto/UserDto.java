package com.example.productcatalogservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String email;
    //private List<Role> roles;
}