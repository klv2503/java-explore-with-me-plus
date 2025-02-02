package ru.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.service.EndpointHitService;

@RestController
@RequestMapping("/")
public class StatsController {

    private final EndpointHitService endpointHitService;

    @Autowired
    public StatsController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@Valid @RequestBody CreateEndpointHitDto dto) {
        endpointHitService.saveHit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}