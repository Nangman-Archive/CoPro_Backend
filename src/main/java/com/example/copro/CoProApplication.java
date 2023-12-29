package com.example.copro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoProApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoProApplication.class, args);
    }

}
