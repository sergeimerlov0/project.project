package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagsDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
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
public class RelatedTagsDtoServiceImpl implements RelatedTagsDtoService {

    private final RelatedTagsDtoDao relatedTagsDtoDao;

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return relatedTagsDtoDao.getRelatedTagsDto();
    }
}
