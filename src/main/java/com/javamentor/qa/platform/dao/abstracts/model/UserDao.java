package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import java.util.Optional;

public interface UserDao extends ReadWriteDao<User, Long>  {
    Optional<User> getByEmail(String email);
    void deleteByEmail(String email);
    void updatePasswordByEmail(String email, String password);
    Boolean isPresentByEmail(String email);
}