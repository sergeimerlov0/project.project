package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private final AnswerDtoDao answerDtoDao;
    private final AnswerService answerService;

    @Override
    public List<AnswerDto> getAnswerByQuestionId(Long id) {
        return answerDtoDao.getAnswerByQuestionId(id);
    }

    @Override
    public AnswerDto getAnswerDtoByAnswerAndReputation(Answer answer, Reputation reputation) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(answer.getId());
        answerDto.setUserId(answer.getUser().getId());
        answerDto.setUserReputation(Long.valueOf(reputation.getCount()));
        answerDto.setBody(answer.getHtmlBody());
        answerDto.setImage(answer.getUser().getImageLink());
        answerDto.setCountValuable((answerService.getUpVoteCountByAnswer(answer) - answerService.getDownVoteCountByAnswer(answer)));
        answerDto.setPersistDate(answer.getPersistDateTime());
        answerDto.setDateAccept(answer.getDateAcceptTime());
        answerDto.setIsHelpful(answer.getIsHelpful());
        answerDto.setNickName(answer.getUser().getNickname());
        answerDto.setQuestionId(answer.getQuestion().getId());

        return answerDto;
    }
}
