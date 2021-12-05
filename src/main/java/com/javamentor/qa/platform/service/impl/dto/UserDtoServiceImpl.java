package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDtoServiceImpl implements UserDtoService {
    @Autowired
    private UserDtoDao userDtoDao;

    @Override
    public UserDto getUserById(Long id) {
        return userDtoDao.getUserById(id);
    }
}
