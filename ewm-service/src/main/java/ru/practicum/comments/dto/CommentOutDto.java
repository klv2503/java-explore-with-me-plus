package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentOutDto {
    private Long id;
    private String text;
    private String eventName;
    private String authorName;
    private String created;
    private String status;
}
