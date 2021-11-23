package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public boolean existsById(Long id) {
        return null != entityManager.find(User.class, id);
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.of(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAllByIds(Iterable<Long> ids) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id IN :ids", User.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public boolean existsByAllIds(Collection<Long> ids) {
        return !entityManager.createQuery("SELECT u FROM User u WHERE u.id IN :ids", User.class)
                .setParameter("ids", ids)
                .getResultList()
                .isEmpty();
    }

    @Override
    public void persist(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }

    @Override
    public void persistAll(User... entities) {
        for (User user : entities) {
            entityManager.persist(user);
        }
    }

    @Override
    public void persistAll(Collection<User> entities) {
        for (User user : entities) {
            entityManager.persist(user);
        }
    }

    @Override
    public void deleteAll(Collection<User> entities) {
        for (User user : entities) {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        }
    }

    @Override
    public void updateAll(Iterable<? extends User> entities) {
        for (User user : entities) {
            entityManager.merge(user);
        }
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("DELETE FROM User u WHERE u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
