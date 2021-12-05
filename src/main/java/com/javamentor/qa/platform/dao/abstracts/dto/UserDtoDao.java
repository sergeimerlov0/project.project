package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

public interface UserDtoDao {
    public UserDto getUserById(Long id);
}
