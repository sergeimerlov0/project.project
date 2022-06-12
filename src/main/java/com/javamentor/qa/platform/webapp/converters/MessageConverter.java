package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.OutcomeMessageDTO;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface MessageConverter {
    MessageConverter INSTANCE = Mappers.getMapper(MessageConverter.class);

    @Mappings({
            @Mapping(source = "user.fullName", target = "nickName"),
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "user.imageLink", target = "image"),
            @Mapping(source = "message.id", target = "id")
    })
    OutcomeMessageDTO toOutcomeMessageDTO(Message message, User user);
}
