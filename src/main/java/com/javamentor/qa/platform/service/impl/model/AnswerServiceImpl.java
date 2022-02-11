package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {
    private final AnswerDao answerDao;
@Autowired
    public AnswerServiceImpl (AnswerDao answerDao) {
        super(answerDao);
        this.answerDao = answerDao;
    }


    @Override
    public Answer getAnswerByQuestionIdAndUserIdAndAnswerBody (Long questionId, Long userId, String htmlBody){
//       List<Answer> list =  answerDao.getAll()
//               .stream()
//               .filter(x->x.getHtmlBody().equals(htmlBody)
//                       &&(Objects.equals(x.getQuestion().getId(), questionId))
//                       &&(Objects.equals(x.getUser().getId(), userId)))
//               .collect(Collectors.toList());
//        return list.get(0);
        return answerDao.getAnswerByQuestionIdAndUserIdAndAnswerBody(questionId, userId, htmlBody);
    }
}
