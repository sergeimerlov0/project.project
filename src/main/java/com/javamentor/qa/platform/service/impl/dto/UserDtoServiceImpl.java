package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.UserTestDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;


//Образец реализации сервиса для работы с объектом UserDto. Возможно добавлять свои методы для работы с UserDto
@Service
public class UserDtoServiceImpl extends PaginationServiceDtoImpl<UserTestDto> implements UserDtoService {

}
