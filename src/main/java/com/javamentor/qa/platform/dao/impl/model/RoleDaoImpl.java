package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public boolean existsById(Long id) {
        return null != entityManager.find(Role.class, id);
    }

    @Override
    public Optional<Role> getById(Long id) {
        return Optional.of(entityManager.find(Role.class, id));
    }

    @Override
    public List<Role> getAllByIds(Iterable<Long> ids) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public boolean existsByAllIds(Collection<Long> ids) {
        return !entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList()
                .isEmpty();
    }

    @Override
    public void persist(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void delete(Role role) {
        entityManager.remove(entityManager.contains(role) ? role : entityManager.merge(role));
    }

    @Override
    public void persistAll(Role... entities) {
        for (Role role : entities) {
            entityManager.persist(role);
        }
    }

    @Override
    public void persistAll(Collection<Role> entities) {
        for (Role role : entities) {
            entityManager.persist(role);
        }
    }

    @Override
    public void deleteAll(Collection<Role> entities) {
        for (Role role : entities) {
            entityManager.remove(entityManager.contains(role) ? role : entityManager.merge(role));
        }
    }

    @Override
    public void updateAll(Iterable<? extends Role> entities) {
        for (Role role : entities) {
            entityManager.merge(role);
        }
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("DELETE FROM Role r WHERE r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
