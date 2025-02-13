package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.ReadEndpointHitDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/views")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final RestClient restClient;

    @Autowired
    private StatsServerConfig statsServerConfig;

    @PostMapping
    public ResponseEntity<Void> saveView(@RequestParam String addr, @RequestParam String uri) {
        log.info("\nClientController.saveView addr {}, uri {}", addr, uri);
        CreateEndpointHitDto dto = new CreateEndpointHitDto(
                "ewm-main-service",
                uri,
                addr,
                LocalDateTime.now()
        );

        restClient.post()
                .uri(buildUri("/hit", Map.of()))
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<ReadEndpointHitDto> getHits(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam List<String> uris,
                                            @RequestParam boolean unique) {
        log.info("\nClientController.getHits start {}, end {}, \nuris {}, unique {}", start, end, uris, unique);
        log.info("statsServerConfig: host {}, port {}", statsServerConfig.getHost(), statsServerConfig.getPort());

        Map<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", String.join(",", uris));
        params.put("unique", String.valueOf(unique));
        // Выполняем запрос и получаем коллекцию объектов ReadEndpointHitDto
        ResponseEntity<Collection<ReadEndpointHitDto>> response = restClient.get()
                .uri(buildUri("/stats", params))
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<>() {
                        });

        List<ReadEndpointHitDto> respList = Optional.ofNullable(response.getBody())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
        return respList;
    }

    private URI buildUri(String path, Map<String, String> queryParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(9090)
                .path(path);

        // Добавляем параметры
        queryParams.forEach(uriComponentsBuilder::queryParam);

        return uriComponentsBuilder.build().toUri();
    }

}



