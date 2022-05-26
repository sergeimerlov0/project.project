package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDtoServiceImpl extends PaginationServiceDtoImpl<UserDto> implements UserDtoService {
    private final UserDtoDao userDtoDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public UserDtoServiceImpl(UserDtoDao userDtoDao, TagDtoDao tagDtoDao) {
        this.userDtoDao = userDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id);
    }

    @Override
    @Transactional
    public List<UserProfileQuestionDto> getUserProfileQuestionDtoAddByUserId(Long userId) {
        List<UserProfileQuestionDto> listQuestions = userDtoDao.getUserProfileQuestionDtoAddByUserId(userId);
        List<Long> listId = listQuestions
                .stream()
                .map(UserProfileQuestionDto::getQuestionId)
                .collect(Collectors.toList());
        Map<Long, List<TagDto>> tags = tagDtoDao.getMapTagsByQuestionIds(listId);

        for (UserProfileQuestionDto userProfileQuestionDto : listQuestions) {
            userProfileQuestionDto.setTagList(
                    tags.get(userProfileQuestionDto.getQuestionId()) != null ?
                            tags.get(userProfileQuestionDto.getQuestionId()) :
                            new ArrayList<>());
        }

        return listQuestions;
    }


    @Override
    @Transactional
    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long userId) {
        List<UserProfileQuestionDto> listQuestions = userDtoDao.getAllDeletedQuestionsByUserId(userId);
        List<Long> listIdQuestion = listQuestions
                .stream()
                .map(UserProfileQuestionDto::getQuestionId)
                .collect(Collectors.toList());
        Map<Long, List<TagDto>> tags = tagDtoDao.getMapTagsByQuestionIds(listIdQuestion);

        for (UserProfileQuestionDto userProfileQuestionDto : listQuestions) {
            userProfileQuestionDto.setTagList(
                    tags.get(userProfileQuestionDto.getQuestionId()) != null ?
                            tags.get(userProfileQuestionDto.getQuestionId()) :
                            new ArrayList<>());
        }
        return listQuestions;

    }



}
