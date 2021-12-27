package com.javamentor.qa.platform.service.impl.model.reputation;

import com.javamentor.qa.platform.dao.abstracts.model.user.reputation.ReputationDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.user.reputation.ReputationService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {

    @Autowired
    public ReputationServiceImpl(ReputationDao reputationDao) {
        super(reputationDao);
    }
}
