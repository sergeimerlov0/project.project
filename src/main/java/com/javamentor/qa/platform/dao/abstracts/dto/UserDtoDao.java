package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import java.util.List;

import java.util.Optional;

public interface UserDtoDao {
    Optional<UserDto> getUserById(Long id);
    List<UserProfileQuestionDto> getUserProfileQuestionDtoAddByUserId(Long userId);
    List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id);
    List<UserDto> getTop10ByAnswerPerWeek();
}