package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Service;


public interface UserDtoService  {
    UserDto getUserById(Long id);
}
