package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileDeletedQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserProfileDeletedQuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserProfileDeletedQuestionDtoServiceImpl implements UserProfileDeletedQuestionDtoService {
    private final UserProfileDeletedQuestionDtoDao userProfileDeletedQuestionDtoDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public UserProfileDeletedQuestionDtoServiceImpl(UserProfileDeletedQuestionDtoDao userProfileDeletedQuestionDtoDao, TagDtoDao tagDtoDao) {
        this.userProfileDeletedQuestionDtoDao = userProfileDeletedQuestionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }


    @Override
    @Transactional
    public List<UserProfileQuestionDto> getUserProfileDeletedQuestionDtoByUserId(Long userId) {
        List<UserProfileQuestionDto> listQuestions = userProfileDeletedQuestionDtoDao.getUserProfileDeletedQuestionDtoByUserId(userId);
        for (UserProfileQuestionDto question : listQuestions) {
            question.setTagList(tagDtoDao.getTagsByQuestionId(question.getQuestionId()));
        }
        return listQuestions;

    }
}
