package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends ReadWriteServiceImpl<User, Long> implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        super(userDao);
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void persist(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.persist(user);
    }

    @Override
    public void update(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.update(user);
    }
}
