package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.CreateEndpointHitDto;

import java.net.URI;
import java.time.LocalDateTime;


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
                .uri(URI.create("http://stats-server:9090/hit"))
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public Integer countView(@RequestParam String uri) {
        log.info("\nClientController.countView uri {}", uri);
        Integer result = restClient.get()
                .uri("http://stats-server:9090/stats/{uri}", uri)
                .retrieve()
                .toEntity(Integer.class).getBody();

        return (result == null) ? 0 : result;
    }
}



