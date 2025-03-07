package com.example.productcatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class ProductCatalogServiceApplication {

    public static void main(String[] args) {
        // Load the environment variables from the .env file
//        Dotenv dotenv = Dotenv.configure().load();
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        SpringApplication.run(ProductCatalogServiceApplication.class, args);
    }

}
