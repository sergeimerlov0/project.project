package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAll() {
        return roleDao.getAll();
    }

    @Override
    public boolean existsById(Long id) {
        return roleDao.existsById(id);
    }

    @Override
    public Optional<Role> getById(Long id) {
        return roleDao.getById(id);
    }

    @Override
    public List<Role> getAllByIds(Iterable<Long> ids) {
        return roleDao.getAllByIds(ids);
    }

    @Override
    public boolean existsByAllIds(Collection<Long> ids) {
        return roleDao.existsByAllIds(ids);
    }

    @Override
    public void persist(Role role) {
        roleDao.persist(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(Role role) {
        roleDao.delete(role);
    }

    @Override
    public void persistAll(Role... entities) {
        roleDao.persistAll(entities);
    }

    @Override
    public void persistAll(Collection<Role> entities) {
        roleDao.persistAll(entities);
    }

    @Override
    public void deleteAll(Collection<Role> entities) {
        roleDao.deleteAll(entities);
    }

    @Override
    public void updateAll(Iterable<? extends Role> entities) {
        roleDao.updateAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        roleDao.deleteById(id);
    }
}
