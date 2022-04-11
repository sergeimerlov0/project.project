package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import java.util.List;

public interface UserProfileDeletedQuestionDtoService {
    List<UserProfileQuestionDto> getUserProfileDeletedQuestionDtoByUserId(Long userId);
}
