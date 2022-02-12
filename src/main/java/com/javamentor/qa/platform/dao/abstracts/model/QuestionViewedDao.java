package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

public interface QuestionViewedDao extends ReadWriteDao<QuestionViewed, Long> {

    boolean questionViewCheckByUserIdAndQuestionId (Long questionId, String email);

}
