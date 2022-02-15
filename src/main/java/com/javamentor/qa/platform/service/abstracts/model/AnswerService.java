package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;

public interface AnswerService extends ReadWriteService<Answer, Long> {

    Answer getAnswerByQuestionIdAndUserIdAndAnswerBody(Long questionId, Long userId, String htmlBody);

    Long getUpVoteCountByAnswer(Answer answer);

    Long getDownVoteCountByAnswer(Answer answer);
}
