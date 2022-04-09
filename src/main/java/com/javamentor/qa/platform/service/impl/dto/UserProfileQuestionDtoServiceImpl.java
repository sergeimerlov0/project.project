package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserProfileQuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserProfileQuestionDtoServiceImpl implements UserProfileQuestionDtoService {
    private final UserProfileQuestionDtoDao userProfileQuestionDtoDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public UserProfileQuestionDtoServiceImpl(UserProfileQuestionDtoDao userProfileQuestionDtoDao, TagDtoDao tagDtoDao) {
        this.userProfileQuestionDtoDao = userProfileQuestionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    @Transactional
    public List<UserProfileQuestionDto> getUserProfileQuestionDtoAddByUserId(Long userId) {
        List<UserProfileQuestionDto> listQuestions = userProfileQuestionDtoDao.getUserProfileQuestionDtoAddByUserId(userId);
        for (UserProfileQuestionDto question : listQuestions) {
            question.setTagList(tagDtoDao.getTagsByQuestionId(question.getQuestionId()));
        }
        return listQuestions;
    }
}