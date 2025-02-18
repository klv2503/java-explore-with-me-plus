package ru.practicum.comments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.service.CommentService;

@RestController
@RequestMapping("/comments/{userId}/")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentOutDto> addNewComment(@PathVariable("userId") Long userId,
                                                       @PathVariable("eventId") Long eventId,
                                                       @Valid @RequestBody CommentDto commentDto) {
        log.info("\nRequest for adding new comment {} from author {} to event {}", commentDto, userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addNewComment(userId, eventId, commentDto));
    }
}
