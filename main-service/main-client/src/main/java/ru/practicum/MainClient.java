package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainClient {
    public static void main(String[] args) {
            ConfigurableApplicationContext context = SpringApplication.run(MainClient.class, args);

        }
    }