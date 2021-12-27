package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.UserTestDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoTestService;
import org.springframework.stereotype.Service;

@Service
public class UserDtoTestServiceImpl extends PaginationServiceDtoImpl<UserTestDto> implements UserDtoTestService {
}
