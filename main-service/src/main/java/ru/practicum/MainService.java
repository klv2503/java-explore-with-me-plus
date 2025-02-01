package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainService {
    public static void main(String[] args) {
        //ConfigurableApplicationContext context =
        SpringApplication.run(MainService.class, args);
        //StatsClient statsClient = context.getBean(StatsClient.class);
        //statsClient.hit(new ParamHitDto());
    }
}