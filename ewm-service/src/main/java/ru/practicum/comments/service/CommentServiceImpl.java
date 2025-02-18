package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.service.PublicEventsService;
import ru.practicum.users.service.AdminUserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final AdminUserService adminUserService;

    private final PublicEventsService publicEventsService;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentOutputDto addComment(CommentDto commentDto) {
        Comment comment = CommentMapper.commentDtoToComment(commentDto);
        comment.setUser(adminUserService.getUser(comment.getUser().getId()));
        comment.setEvent(publicEventsService.getEventAnyStatusWithViews(comment.getEvent().getId()));
        return CommentMapper.commentToOutputDto(commentRepository.save(comment));
    }
}
