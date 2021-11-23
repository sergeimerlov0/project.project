package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public boolean existsById(Long id) {
        return userDao.existsById(id);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public List<User> getAllByIds(Iterable<Long> ids) {
        return userDao.getAllByIds(ids);
    }

    @Override
    public boolean existsByAllIds(Collection<Long> ids) {
        return userDao.existsByAllIds(ids);
    }

    @Override
    public void persist(User user) {
        userDao.persist(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public void persistAll(User... entities) {
        userDao.persistAll(entities);
    }

    @Override
    public void persistAll(Collection<User> entities) {
        userDao.persistAll(entities);
    }

    @Override
    public void deleteAll(Collection<User> entities) {
        userDao.deleteAll(entities);
    }

    @Override
    public void updateAll(Iterable<? extends User> entities) {
        userDao.updateAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }
}
