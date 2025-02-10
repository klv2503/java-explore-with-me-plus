package ru.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.practicum.dto.TakeHitsDto;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

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

    public Collection<ReadEndpointHitDto> get(TakeHitsDto takeHitsDto) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sql.append("SELECT ");

        if (takeHitsDto.isUnique()) {
            sql.append(" app, uri, COUNT(distinct ip) as count, ip FROM endpoint_hit WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY app, uri, ip");
        } else {
            sql.append(" app, uri, COUNT(id) as count, ip FROM endpoint_hit WHERE timestamp BETWEEN :start AND :end " +
                    "GROUP BY app, uri, ip");
        }

        params.addValue("start", takeHitsDto.getStart());
        params.addValue("end", takeHitsDto.getEnd());

        if (!CollectionUtils.isEmpty(takeHitsDto.getUris())) {
            List<String> uris = takeHitsDto.getUris();
            StringBuilder urisString = new StringBuilder(uris.getFirst());

            for (int i = 1; i < uris.size(); i++) {
                urisString.append(", ").append(uris.get(i));
            }

            params.addValue("uris", uris);
            sql.append(" HAVING uri IN (:uris)");
        }

        return jdbc.query(sql.toString(), params, mapper);
    }

    public Integer getViews(String uri) {
        String sql = "SELECT COUNT(*) FROM endpoint_hit WHERE uri = :uri";
        //Если надо считать только с разных ip, то "SELECT COUNT(DISTINCT ip) FROM endpoint_hit WHERE uri = :uri";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("uri", uri);
        Integer count = jdbc.queryForObject(sql, params, Integer.class);
        return (count == null) ? 0 : count;
    }

}