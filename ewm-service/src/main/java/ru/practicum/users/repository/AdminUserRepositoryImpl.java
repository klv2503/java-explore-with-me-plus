package ru.practicum.users.repository;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ru.practicum.users.model.QUser;
import ru.practicum.users.model.User;

import java.util.List;

public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findByIds(List<Long> ids, Pageable pageable) {
        QUser qUser = QUser.user;  // Сгенерированный Q-класс User

        JPAQuery<User> query = new JPAQuery<>()
                .select(qUser)
                .from(qUser)
                .where(qUser.id.in(ids));

        List<User> users = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return PageableExecutionUtils.getPage(users, pageable, () -> total);
    }
}