package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.service.EndpointHitService;

@RestController
@AllArgsConstructor
public class StatsController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> saveHit(@Valid @RequestBody CreateEndpointHitDto dto) {
        endpointHitService.saveHit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}