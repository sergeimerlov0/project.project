package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.user.reputation.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private final VoteAnswerDao voteAnswerDao;
    private final UserDao userDao;
    private final ReputationDao reputationDao;
    private final AnswerDao answerDao;

    private final static Integer UP_VOTE = 10;
    private final static Integer DOWN_VOTE = -5;

    @Autowired
    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao, UserDao userDao, ReputationDao reputationDao, AnswerDao answerDao) {
        super(voteAnswerDao);
        this.voteAnswerDao = voteAnswerDao;
        this.userDao = userDao;
        this.reputationDao = reputationDao;
        this.answerDao = answerDao;
    }

    @Override
    public Long getTotalVotesByAnswerId(Long id) {
        return voteAnswerDao.getTotalVotesByAnswerId(id);
    }

    @Override
    @Transactional
    public ResponseEntity<Long> postVoteUp(Long answerId, Principal principal) {
        Optional<User> optionalUser = userDao.getByEmail(principal.getName());
        Optional<Answer> optionalAnswer = answerDao.getById(answerId);
        if (optionalUser.isEmpty() || optionalAnswer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User userSender = optionalUser.get();
        Answer answer = optionalAnswer.get();
        voteAnswerDao.persist(new VoteAnswer(userSender, answer, VoteType.UP_VOTE));
        reputationDao.persist(new Reputation(answer.getUser(), userSender, UP_VOTE, ReputationType.VoteAnswer, answer));
        return new ResponseEntity<>(voteAnswerDao.getTotalVotesByAnswerId(answerId), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Long> postVoteDown(Long answerId, Principal principal) {
        Optional<User> optionalUser = userDao.getByEmail(principal.getName());
        Optional<Answer> optionalAnswer = answerDao.getById(answerId);
        if (optionalUser.isEmpty() || optionalAnswer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User userSender = optionalUser.get();
        Answer answer = optionalAnswer.get();
        voteAnswerDao.persist(new VoteAnswer(userSender, answer, VoteType.DOWN_VOTE));
        reputationDao.persist(new Reputation(answer.getUser(), userSender, DOWN_VOTE, ReputationType.VoteAnswer, answer));
        return new ResponseEntity<>(voteAnswerDao.getTotalVotesByAnswerId(answerId), HttpStatus.OK);
    }
}
