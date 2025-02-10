package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class EventsRestClientConfig {
    @Bean
    public RestClient eventsRestClient(@Value("${stat-client.url}") String statClientUrl) {
        return RestClient.builder()
                .baseUrl(statClientUrl)
                .build();
    }
}