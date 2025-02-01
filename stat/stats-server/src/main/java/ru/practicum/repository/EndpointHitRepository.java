package ru.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;

import java.sql.Timestamp;

@Repository
public class EndpointHitRepository {

    private final JdbcTemplate jdbcTemplate;

    public EndpointHitRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(EndpointHit endpointHit) {
        String sql = "INSERT INTO endpoint_hit (app, uri, ip, timestamp) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                Timestamp.valueOf(endpointHit.getTimestamp()));
    }
}
