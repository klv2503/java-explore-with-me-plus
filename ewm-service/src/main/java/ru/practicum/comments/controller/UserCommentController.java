package ru.practicum.comments.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.CommentsOrder;
import ru.practicum.comments.model.CommentsStatus;
import ru.practicum.comments.service.CommentService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users/{userId}/comments")
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
    public ResponseEntity<CommentOutputDto> addComment(@PathVariable
                                                       @Min(value = 1, message = "ID must be positive") Long userId,
                                                       //рассматривал вариант с @RequestHeader, пока предлагаю такой, но это уже на выбор
                                                       @PathVariable
                                                       @Min(value = 1, message = "ID must be positive") Long eventId,
                                                       @RequestBody @NotBlank String text) {
        log.info("\nCommentController.addComment accepted userId {}, eventId {}, text {}", userId, eventId, text);
        CommentDto commentDto = CommentDto.builder()
                .userId(userId)
                .eventId(eventId)
                .text(text)
                .created(LocalDateTime.now())
                .status(CommentsStatus.PUBLISHED)
                .build();
        CommentOutputDto createdComment = commentService.addComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);

    }

    @PatchMapping("/commentId")
    public ResponseEntity<CommentOutputDto> updateComment(@PathVariable
                                                          @Min(value = 1, message = "ID must be positive") Long userId,
                                                          @PathVariable
                                                          @Min(value = 1, message = "ID must be positive") Long commentId,
                                                          @RequestBody @NotBlank String text) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(userId, commentId, text));
    }
}
