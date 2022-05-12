package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {
    private final TagService tagService;
    private final QuestionDao questionDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao, TagService tagService) {
        super(questionDao);
        this.tagService = tagService;
        this.questionDao = questionDao;
    }

    @Override
    @Transactional
    public void persist(Question question) {
        List<Tag> tags = question.getTags();
        List<String> tagNames = tags.stream().map(Tag::getName).collect(Collectors.toList());

        List<Tag> existedTags = tagService.findTagsByNames(tagNames);
        List<String> existedTagsNames = existedTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        List<Tag> newTags = tags.stream()
                .filter(tag -> !existedTagsNames.contains(tag.getName()))
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            tagService.persistAll(newTags);
        }

        question.setTags(Stream.of(existedTags, newTags)
                .flatMap(Collection::stream).collect(Collectors.toList()));
        super.persist(question);
    }

    @Override
    @Transactional
    public Integer getCountQuestion() {
        return questionDao.getCountQuestion();
    }
}