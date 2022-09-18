package com.JonasAmme.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@RestController
@EnableJpaRepositories
public class JonasAmmeApplication {
    private static final String[] folders = {"wine_review_photos", "memory_photos", "recipe_photos"};

    public static void main(String[] args) {
        for (String folderName : folders) {
            File folder = new File(folderName);
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    throw new RuntimeException(new IOException("Cannot create folder"));
                }
            }
        }

        SpringApplication.run(JonasAmmeApplication.class, args);
    }


}
