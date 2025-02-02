package ru.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public class EndpointHitRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReadEndpointHitDto> mapper;

    public EndpointHitRepository(JdbcTemplate jdbcTemplate, RowMapper<ReadEndpointHitDto> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public void save(EndpointHit endpointHit) {
        String sql = "INSERT INTO endpoint_hit (app, uri, ip, timestamp) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                Timestamp.valueOf(endpointHit.getTimestamp()));
    }

    public Collection<ReadEndpointHitDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringBuilder sql = new StringBuilder();

        if (unique) {
            sql.append(" DISTINCT(ip) ");
        }

        sql.append("COUNT(id) as count, uri FROM endpoint_hit GROUP BY uri HEAVING timestamp BETWEEN ? AND ?");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("start", start);
        params.addValue("end", end);

        if (uris != null) {
            StringBuilder urisString = new StringBuilder(uris.getFirst());

            for (int i = 1; i < uris.size(); i++) {
                urisString.append(", ").append(uris.get(i));
            }

            params.addValue("uris", uris);
            sql.append(" AND uri IN (").append(urisString).append(")");
        }

        return jdbcTemplate.query(sql.toString(), mapper, params);
    }
}
