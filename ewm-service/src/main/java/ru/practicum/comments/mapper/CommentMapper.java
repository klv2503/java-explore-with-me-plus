package ru.practicum.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

@Component
public class CommentMapper {

    public static Comment commentDtoToComment(CommentDto commentDto) {
//Возможно, так надо строить сразу CommentDto - надо подумать
        return Comment.builder()
                .user(User.builder()
                        .id(commentDto.getUserId())
                        .build())
                .event(Event.builder()
                        .id(commentDto.getEventId())
                        .build())
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .status(commentDto.getStatus())
                .build();
    }

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
}
