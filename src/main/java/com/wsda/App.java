package com.wsda;

import com.wsda.model.WSDAUser;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    private EntityManager entityManager;
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}