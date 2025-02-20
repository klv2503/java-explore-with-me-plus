package ru.practicum.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentEconomDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.mapper.EventMapper;

@Component
public class CommentMapper {

    public static CommentOutputDto commentToOutputDto(Comment comment) {
        return CommentOutputDto.builder()
                .id(comment.getId())
                .user(comment.getUser())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }

    public static CommentEconomDto commentToEconomDto(Comment comment) {
        return CommentEconomDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }
}
