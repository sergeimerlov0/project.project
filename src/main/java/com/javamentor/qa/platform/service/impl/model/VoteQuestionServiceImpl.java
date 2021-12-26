package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteQuestionService {

    private VoteQuestionDao voteQuestionDao;

    @Autowired
    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao) {
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
    }

    @Override
    public List<VoteQuestion> getAllVoteQuestionsByQuestionId(Long questionId) {
        return voteQuestionDao.getAllVoteQuestionsByQuestionId(questionId);
    }

    @Override
    public boolean userVoteCheck(Long questionId, Long userId) {
        return voteQuestionDao.userVoteCheck(questionId, userId);
    }
}
