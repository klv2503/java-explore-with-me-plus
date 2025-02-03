package ru.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class EndpointHitRepository {
    private final RowMapper<ReadEndpointHitDto> mapper;
    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public EndpointHitRepository(RowMapper<ReadEndpointHitDto> mapper, NamedParameterJdbcOperations jdbc) {
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

        jdbc.update(sql, params);
    }

    public Collection<ReadEndpointHitDto> get(LocalDateTime start, LocalDateTime end, Optional<List<String>> mayBeUris,
                                              boolean unique) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sql.append("SELECT ");

        if (unique) {
            sql.append(" app, uri, COUNT(distinct ip) as count, ip FROM endpoint_hit WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY app, uri, ip");
        } else {
            sql.append(" app, uri, COUNT(id) as count, ip FROM endpoint_hit WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY app, uri, ip");
        }

        params.addValue("start", start);
        params.addValue("end", end);

        if (mayBeUris.isPresent()) {
            List<String> uris = mayBeUris.get();
            StringBuilder urisString = new StringBuilder(uris.getFirst());

            for (int i = 1; i < uris.size(); i++) {
                urisString.append(", ").append(uris.get(i));
            }

            params.addValue("uris", uris);
            sql.append(" HAVING uri IN (:uris)");
        }

        return jdbc.query(sql.toString(), params, mapper);
    }
}