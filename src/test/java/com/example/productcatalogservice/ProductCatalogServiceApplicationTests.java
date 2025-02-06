package com.example.productcatalogservice;


import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductCatalogServiceApplicationTests {

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.configure().load(); // This will load the .env file
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    @Test
    void contextLoads() {
    }


}
