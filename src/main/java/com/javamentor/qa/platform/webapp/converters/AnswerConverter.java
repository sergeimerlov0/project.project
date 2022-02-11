package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AnswerConverter {
    @Mapping(target = "userReputation", source = "answer.user.")
    @Mapping(target = "userId", source = "answer.user.id")
    @Mapping(target = "questionId", source = "answer.question")
    @Mapping(target = "nickName", source = "answer.user.nickname")
    @Mapping(target = "image", source = "answer.user.imageLink")
    @Mapping(target = "dateAccept", source = "dateAcceptTime")
    @Mapping(target = "countValuable", source = "")
    @Mapping(target = "body", source = "answer.htmlBody")
    @Mapping(target = "persistDate", source = "persistDateTime")
    public abstract AnswerDto answerToAnswerDto (Answer answer);

}
