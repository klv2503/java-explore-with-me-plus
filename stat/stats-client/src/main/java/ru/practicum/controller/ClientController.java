package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.CreateEndpointHitDto;

import java.net.URI;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
public class ClientController {
    private final RestClient restClient;

    @PostMapping("/hit")
    public void hit(@RequestParam String addr, @RequestParam String uri) {
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
    }
}



