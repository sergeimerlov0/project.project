package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
    @Cacheable(value = "users", key = "#email")
    public Optional<User> getByEmail(String email) {
        log.info("Запрос пользователя из БД в методе getByEmail");
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u " +
                        "FROM User u " +
                        "INNER JOIN FETCH u.role " +
                        "WHERE u.email = :email " +
                        "AND u.isEnabled = true",
                        User.class)
                .setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public Boolean isPresentByEmail(String email) {
        log.info("Запрос пользователя из БД в методе isPresentByEmail");
        return entityManager.createQuery(
                "SELECT COUNT(u)>0 " +
                        "FROM User u " +
                        "WHERE u.email = :email " +
                        "AND u.isEnabled = true",
                        Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery(
                "SELECT u " +
                        "FROM User u " +
                        "INNER JOIN FETCH u.role " +
                        "WHERE u.isEnabled = true",
                        User.class)
                .getResultList();
    }

    @Override
    @CacheEvict(value = "users", key = "#email")
    public void deleteByEmail(String email) {
        entityManager.createQuery(
                "UPDATE User " +
                        "SET isEnabled = false " +
                        "WHERE email = :email")
                .setParameter("email", email)
                .executeUpdate();
    }

    @Override
    @CacheEvict(value = "users", key = "#email")
    public void updatePasswordByEmail (String email, String password) {
        entityManager.createQuery(
                "UPDATE User u " +
                        "SET u.password = :password " +
                        "WHERE u.email = :email")
                .setParameter("password", password)
                .setParameter("email", email).executeUpdate();
    }
}