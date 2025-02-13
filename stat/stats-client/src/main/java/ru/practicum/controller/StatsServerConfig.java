package ru.practicum.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "stats.server")
@Getter
@Setter
public class StatsServerConfig {
    private String url;
    private String host;
    private int port;
}
