package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {
    private final TrackedTagDao trackedTagDao;

    @Autowired
    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
    }

    @Override
    public List<Long> trackedTagsByUserId(Long userId) {
        return trackedTagDao.trackedTagsByUserId(userId);
    }

    @Override
    public boolean tagIsPresentInTheListOfUser(Long userId, Long tagId) {
        return trackedTagDao.tagIsPresentInTheListOfUser(userId, tagId);
    }
}