package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final TagService tagService;

    public QuestionServiceImpl(QuestionDao questionDao, TagService tagService) {
        super(questionDao);
        this.tagService = tagService;
    }

    @Override
    @Transactional
    public void persist(Question question) {
        List<Tag> managedTags = question.getTags().stream().map((tag) -> tagService
                .getByName(tag.getName()).orElseGet(() -> {
                    tagService.persist(tag);
                    return tag;
                })).collect(Collectors.toList());
        question.setTags(managedTags);
        super.persist(question);
    }
}
