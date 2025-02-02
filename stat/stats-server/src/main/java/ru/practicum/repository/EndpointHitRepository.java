package ru.practicum.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class EndpointHitRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReadEndpointHitDto> mapper;
    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public EndpointHitRepository(JdbcTemplate jdbcTemplate, RowMapper<ReadEndpointHitDto> mapper, NamedParameterJdbcOperations jdbc) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbc = jdbc;
    }

    public void save(EndpointHit endpointHit) {
        String sql = "INSERT INTO endpoint_hit (app, uri, ip, timestamp) VALUES (:app, :uri, :ip, :timestamp)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("app", endpointHit.getApp());
        params.addValue("uri", endpointHit.getUri());
        params.addValue("ip", endpointHit.getIp());
        params.addValue("timestamp", Timestamp.valueOf(endpointHit.getTimestamp()));

//        jdbcTemplate.update(sql,
//                endpointHit.getApp(),
//                endpointHit.getUri(),
//                endpointHit.getIp(),
//                Timestamp.valueOf(endpointHit.getTimestamp()));

       jdbc.update(sql, params);
    }

    public Collection<ReadEndpointHitDto> get(LocalDateTime start, LocalDateTime end, Optional<List<String>> mayBeUris,
                                              boolean unique) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sql.append("SELECT ");

        if (unique) {
            sql.append(" DISTINCT(ip) ");
        }

        params.addValue("start", start);
        params.addValue("end", end);

        sql.append("app, COUNT(id) as count, uri FROM endpoint_hit GROUP BY app, uri, timestamp HAVING timestamp BETWEEN :start AND :end");


        if (mayBeUris.isPresent()) {
            List<String> uris = mayBeUris.get();
            StringBuilder urisString = new StringBuilder(uris.getFirst());

            for (int i = 1; i < uris.size(); i++) {
                urisString.append(", ").append(uris.get(i));
            }

            params.addValue("uris", uris);
            sql.append(" AND uri IN (").append(urisString).append(")");
        }

        return jdbc.query(sql.toString(), params, mapper);
    }
}
