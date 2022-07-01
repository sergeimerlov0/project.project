package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RelatedTagDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.RelatedTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelatedTagServiceImpl extends ReadWriteServiceImpl<RelatedTag, Long> implements RelatedTagService {
    private final RelatedTagDao relatedTagDao;

    @Autowired
    public RelatedTagServiceImpl(RelatedTagDao relatedTagDao) {
        super(relatedTagDao);
        this.relatedTagDao = relatedTagDao;
    }
}