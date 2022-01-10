
package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDtoServiceImpl extends PaginationServiceDtoImpl<UserDto> implements UserDtoService {

    @Autowired
    private UserDtoDao userDtoDao;

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id);
    }

}
