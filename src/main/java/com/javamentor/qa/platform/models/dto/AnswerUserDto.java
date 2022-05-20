package com.javamentor.qa.platform.models.dto;

import java.time.LocalDateTime;

public class AnswerUserDto {
    private Long answerId;
    private Long questionId;
    private Long countAnswerVote;
    private LocalDateTime persistDate;
    private String htmlBody;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getCountAnswerVote() {
        return countAnswerVote;
    }

    public void setCountAnswerVote(Long countAnswerVote) {
        this.countAnswerVote = countAnswerVote;
    }

    public LocalDateTime getPersistDate() {
        return persistDate;
    }

    public void setPersistDate(LocalDateTime persistDate) {
        this.persistDate = persistDate;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public AnswerUserDto() {
    }

    public AnswerUserDto(Long answerId, Long questionId, Long countAnswerVote, LocalDateTime persistDate
            , String htmlBody) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.countAnswerVote = countAnswerVote;
        this.persistDate = persistDate;
        this.htmlBody = htmlBody;
    }
}
