package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/user/question/{questionId}")
public class QuestionResourceController {

    public final QuestionService questionService;
    public final ReputationService reputationService;

    public QuestionResourceController(QuestionService questionService, ReputationService reputationService) {
        this.questionService = questionService;
        this.reputationService = reputationService;
    }

    @PostMapping("/upVote")
    public ResponseEntity<Integer> upVote(@AuthenticationPrincipal User user, @PathVariable Long questionId) {
        Question question = questionService.getById(questionId).get();
        List<VoteQuestion> voteQuestionList = question.getVoteQuestions();
        for (VoteQuestion vQ : voteQuestionList) {
            if (!Objects.equals(vQ.getUser().getId(), user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, VoteType.UP_VOTE);
                voteQuestionList.add(voteQuestion);
                reputationService.setReputationCount(voteQuestion, questionId);
                return new ResponseEntity<>(voteQuestionList.size(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(voteQuestionList.size(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/downVote")
    public ResponseEntity<Integer> downVote(@AuthenticationPrincipal User user, @PathVariable Long questionId) {
        Question question = questionService.getById(questionId).get();
        List<VoteQuestion> voteQuestionList = question.getVoteQuestions();
        for (VoteQuestion vQ : voteQuestionList) {
            if (!Objects.equals(vQ.getUser().getId(), user.getId())) {
                VoteQuestion voteQuestion = new VoteQuestion(user, question, VoteType.DOWN_VOTE);
                voteQuestionList.add(voteQuestion);
                reputationService.setReputationCount(voteQuestion, questionId);
                return new ResponseEntity<>(voteQuestionList.size(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(voteQuestionList.size(), HttpStatus.BAD_REQUEST);
    }
}

