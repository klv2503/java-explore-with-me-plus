package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
