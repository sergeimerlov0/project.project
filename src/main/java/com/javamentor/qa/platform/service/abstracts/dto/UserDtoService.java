
package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

import java.util.Optional;

public interface UserDtoService extends PaginationServiceDto<UserDto> {
    Optional<UserDto> getUserById(Long id);
}

