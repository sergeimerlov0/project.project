package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {

    @Autowired
    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao) {
        super(ignoredTagDao);
    }
}
