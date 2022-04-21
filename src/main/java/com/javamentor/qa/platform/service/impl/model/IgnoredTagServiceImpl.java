package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {
    private final IgnoredTagDao ignoredTagDao;

    @Autowired
    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao) {
        super(ignoredTagDao);
        this.ignoredTagDao = ignoredTagDao;
    }

    @Override
    public List<Long> ignoredTagsByUserId(Long userId) {
        return ignoredTagDao.ignoredTagsByUserId(userId);
    }

    @Override
    public boolean tagIsPresentInTheListOfUser(Long userId, Long tagId) {
        return ignoredTagDao.tagIsPresentInTheListOfUser(userId, tagId);
    }
}