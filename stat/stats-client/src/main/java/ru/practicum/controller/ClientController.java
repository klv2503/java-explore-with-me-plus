package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.ReadEndpointHitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/views")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final RestClient restClient;

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
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("stats-server")
                        .port(9090)
                        .path("/hit")
                        .build())
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
        // Выполняем запрос и получаем коллекцию объектов ReadEndpointHitDto
        ResponseEntity<Collection<ReadEndpointHitDto>> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("stats-server")
                        .port(9090)
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", String.join(",", uris)) // Преобразуем список в строку
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        List<ReadEndpointHitDto> respList = Optional.ofNullable(response.getBody())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
        return respList;
    }
}



