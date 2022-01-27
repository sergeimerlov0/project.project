package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {
    User findByIdAndOldPassword(Long id, String oldPassword);
    User save(User user);
    String generateRandomPassword();
    Optional<User> getByEmail(String email);
    void deleteByEmail(String email);
}
