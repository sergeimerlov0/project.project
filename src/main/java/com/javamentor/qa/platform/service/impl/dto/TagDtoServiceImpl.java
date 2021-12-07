package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagDtoServiceImpl implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    @Autowired
    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getTrackedTagByUsername(String username) {
        return convertInDto(tagDtoDao.getTrackedTagByUsername(username));
    }

    @Override
    public List<TagDto> getIgnoreTagByUsername(String username) {
        return convertInDto(tagDtoDao.getIgnoreTagByUsername(username));
    }

    private List<TagDto> convertInDto (List<Tag> tags) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            tagDtos.add(new TagDto(tag.getId(), tag.getName()));
        }
        return tagDtos;
    }
}
