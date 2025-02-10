package ru.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class StatsController {

    private final EndpointHitService endpointHitService;

    @Autowired
    public StatsController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Integer> saveHit(@Valid @RequestBody CreateEndpointHitDto dto) {
        Integer result = endpointHitService.saveHit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<Collection<ReadEndpointHitDto>> getHits(@RequestParam
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                  LocalDateTime start,
                                                                  @RequestParam
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                  LocalDateTime end,
                                                                  @RequestParam(required = false)
                                                                  Optional<List<String>> uris,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                  boolean unique) {
        return ResponseEntity.status(HttpStatus.OK).body(endpointHitService.getHits(start, end, uris, unique));
    }
}