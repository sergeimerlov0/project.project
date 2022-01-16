package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class QuestionConverter {
    public abstract Question questionCreateDtoToQuestion(QuestionCreateDto questionCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    public abstract Tag tagDtoToTag(TagDto tagDto);
}
