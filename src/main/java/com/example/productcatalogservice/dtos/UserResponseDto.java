package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

    @Getter
    @Setter
    public class UserResponseDto {
        private String name;
        private Long phone;
        private String email;
        private Set<String> roles;
    }
