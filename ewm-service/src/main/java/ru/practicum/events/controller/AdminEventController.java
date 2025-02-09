package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.AdminEventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Admin requested events with filters: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        List<EventFullDto> response = adminEventService
                .getEvents(users, states, categories, rangeStart, rangeEnd, from, size);

        log.info("Found {} events for the given filters", response.size());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        log.debug("Admin is updating event {} with data: {}", eventId, updateEventAdminRequest);

        try {
            EventFullDto dto = adminEventService.updateEvent(eventId, updateEventAdminRequest);
            log.info("Event {} successfully updated", eventId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Failed to update event {}: {}", eventId, e.getMessage(), e);
            throw e;  // Можно вернуть `ResponseEntity` с ошибкой, если требуется
        }
    }
}
