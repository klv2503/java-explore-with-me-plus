package ru.practicum.comments.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentEconomDto;
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.CommentsOrder;
import ru.practicum.comments.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserCommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentEconomDto> addComment(@PathVariable
                                                       @Min(value = 1, message = "ID must be positive") Long userId,
                                                       //рассматривал вариант с @RequestHeader, пока предлагаю такой, но это уже на выбор
                                                       @RequestBody @Valid CommentDto dto) {
        log.info("\nCommentController.addComment accepted userId {}, dto {}", userId, dto);
        CommentEconomDto createdComment = commentService.addComment(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentEconomDto> updateComment(@PathVariable
                                                          @Min(value = 1, message = "ID must be positive") Long userId,
                                                          @PathVariable
                                                          @Min(value = 1, message = "ID must be positive") Long commentId,
                                                          @RequestBody @NotBlank String text) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(userId, commentId, text));
    }
}
