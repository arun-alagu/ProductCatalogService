package com.example.productcatalogservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "ratings")
public class Rating extends BaseModel{
    private Float rate = 0f;
    private Integer count = 0;
    @OneToOne(mappedBy = "rating")
    private Product product;
}
