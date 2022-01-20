package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable("users")
    public Optional<User> getByEmail(String email) {
        log.info("Запрос пользователя из БД в методе getByEmails");
        String hql = "SELECT u FROM User u inner join fetch u.role where u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u inner join fetch u.role",
                User.class).getResultList();
    }
}
