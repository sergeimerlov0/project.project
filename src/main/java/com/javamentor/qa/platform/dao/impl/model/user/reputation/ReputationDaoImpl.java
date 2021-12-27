package com.javamentor.qa.platform.dao.impl.model.user.reputation;

import com.javamentor.qa.platform.dao.abstracts.model.user.reputation.ReputationDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {
}
