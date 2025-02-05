package ru.practicum.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.users.model.User;

import java.util.List;

public interface AdminUserRepositoryCustom {
    Page<User> findByIds(List<Long> ids, Pageable pageable);
}