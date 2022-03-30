package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {
    void updatePasswordByEmail(String email, String password);
    String generateRandomPassword();
    Optional<User> getByEmail(String email);
    void deleteByEmail(String email);
}