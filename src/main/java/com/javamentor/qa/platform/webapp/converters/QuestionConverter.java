package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class QuestionConverter {
    public abstract Question questionCreateDtoToQuestion(QuestionCreateDto questionCreateDto);

    public Tag mapStringToTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tag;
    }
}