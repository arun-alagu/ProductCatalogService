package com.example.productcatalogservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "categories")
public class Category extends BaseModel{
    private String name;
    private String description;
    @ManyToMany(mappedBy = "categories", cascade = CascadeType.DETACH)
    private Set<Product> products = new HashSet<>();
}
