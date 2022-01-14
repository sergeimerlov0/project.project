package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository("AllQuestionDtoSortedByDate")
public class AllQuestionDtoSortedByDate implements PaginationDtoAble<QuestionDto> {

    private TagDtoDao tagDtoDao;

    @Autowired
    public void setTagDtoDao(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    private int size;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<TagDto> ignoredTags = tagDtoDao.getIgnoreTagsByIds((List<Long>) param.get("ignored"));
        List<TagDto> trackedTags = tagDtoDao.getTrackedTagsByIds((List<Long>) param.get("tracked"));
        List<QuestionDto> sortedByDateDto = setTagsForQuestionDto(sortByDateDto(currentPageNumber, itemsOnPage));
        List<QuestionDto> result = new LinkedList<>();

        this.size = sortedByDateDto.size();

        for (TagDto tracked : trackedTags) {
            for (TagDto ignored : ignoredTags) {
                if (Objects.equals(tracked.getId(), ignored.getId())) {
                    trackedTags.remove(tracked);
                }
            }
        }

        for (QuestionDto questionDto : sortedByDateDto) {
            for (TagDto tagDto : questionDto.getListTagDto()) {
                for (TagDto tracked : trackedTags) {
                    if (Objects.equals(tracked.getId(), tagDto.getId()) && !result.contains(questionDto)) {
                        result.add(questionDto);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public List<QuestionDto> sortByDateDto(int currentPageNumber, int itemsOnPage) {
        return entityManager.createQuery("SELECT DISTINCT new com.javamentor.qa.platform.models.dto." +
                        "QuestionDto(question.id, question.title, author.id, " +
                        "(SELECT sum (reputation.count) from Reputation reputation where reputation.author.id = author.id), " +
                        "author.fullName, author.imageLink, " +
                        "question.description, 0L, " +
                        "(SELECT count (*) from Answer answer where answer.question.id = question.id), " +
                        "((SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'UP_VOTE' and voteOnQuestion.question.id = question.id) - " +
                        "(SELECT count (*) from VoteQuestion voteOnQuestion " +
                        "where voteOnQuestion.vote = 'DOWN_VOTE' and voteOnQuestion.question.id = question.id)), " +
                        "question.persistDateTime, question.lastUpdateDateTime) " +
                        "from Question question " +
                        "left outer join question.user as author on (question.user.id=author.id) " +
                        "left outer join question.answers as answer on (question.id=answer.question.id) " +
                        "left outer join question.tags as tags " +
                        "where question.isDeleted=false order by question.persistDateTime asc", QuestionDto.class)
                .getResultStream().skip((long) (currentPageNumber - 1) * itemsOnPage).limit(itemsOnPage).collect(Collectors.toList());
    }

    public List<QuestionDto> setTagsForQuestionDto(List<QuestionDto> questionDtoList) {
        for (QuestionDto questionDto : questionDtoList) {
            questionDto.setListTagDto(entityManager.createQuery("SELECT distinct new com.javamentor.qa.platform.models.dto." +
                            "TagDto(t.id,t.name,t.description) from Tag t left outer join t.questions as q where q.id = :id",
                    TagDto.class).setParameter("id", questionDto.getId()).getResultList());
        }
        return questionDtoList;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return size;
    }
}
