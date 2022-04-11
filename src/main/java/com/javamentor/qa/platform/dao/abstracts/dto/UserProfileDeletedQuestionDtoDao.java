package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;

import java.util.List;

public interface UserProfileDeletedQuestionDtoDao {
    List<UserProfileQuestionDto> getUserProfileDeletedQuestionDtoByUserId(Long userId);
}
