package com.example.productcatalogservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity(name = "products")
public class Product extends BaseModel implements Serializable {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private String imageUrl;
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "product_category",  // name of the join table
            joinColumns = @JoinColumn(name = "product_id"),  // foreign key for product
            inverseJoinColumns = @JoinColumn(name = "category_id") // foreign key for category
        )
    private Set<Category> categories = new HashSet<>();
    @OneToOne
    @JoinColumn(name ="rating_id")
    private Rating rating ;
}
