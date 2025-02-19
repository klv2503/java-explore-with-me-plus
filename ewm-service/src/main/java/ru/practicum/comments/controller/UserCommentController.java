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
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.CommentsOrder;
import ru.practicum.comments.service.CommentService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments/{userId}")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserCommentController {

    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public ResponseEntity<CommentPagedDto> getCommentsByEvent(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "NEWEST") CommentsOrder sort) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(eventId, page, size, sort));
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentOutDto> addNewComment(@PathVariable("userId") Long userId,
                                                       @PathVariable("eventId") Long eventId,
                                                       @Valid @RequestBody CommentDto commentDto) {
        log.info("\nRequest for adding new comment {} from author {} to event {}", commentDto, userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addNewComment(userId, eventId, commentDto));
    }
}
