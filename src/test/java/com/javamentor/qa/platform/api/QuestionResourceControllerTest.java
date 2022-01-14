package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuestionResourceControllerTest extends AbstractApiTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml",
            "datasets/questionDatasets/voteQuestion.yml"
    })
    void getQuestionDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.countAnswer").value(3L))
                .andExpect(jsonPath("$.authorReputation").value(6L))
                .andExpect(jsonPath("$.countValuable").value(1L));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml",
            "datasets/questionDatasets/voteQuestion.yml"
    })
    void getNonExistedQuestionDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postUpVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/100/upVote"))
               .andExpect(status().isOk())
                .andReturn();
                Assertions.assertNotNull(entityManager.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
                                .setParameter("questionId", 100L)
                                .setParameter("userId", 100L));
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postDownVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/101/downVote"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(entityManager.createQuery("FROM VoteQuestion a WHERE a.question.id =:questionId and a.user.id =: userId", VoteQuestion.class)
                .setParameter("questionId", 101L)
                .setParameter("userId", 101L));
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void postUpVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/102/upVote"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/user.yml",
            "datasets/questionDatasets/role.yml"})
    void downUpVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/102/downVote"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void getAllQuestionCommentByQuestionId() throws Exception {
        this.mvc.perform(get("/api/user/question/100/comment")
                .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].text", is("some text 1")))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[0].reputation", is(6)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].questionId", is(100)))
                .andExpect(jsonPath("$[1].text", is("some text 2")))
                .andExpect(jsonPath("$[1].userId", is(101)));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void getEmptyListQuestionCommentByQuestionId() throws Exception {
        mvc.perform(get("/api/user/question/101/comment")
                .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(value = {
            "datasets/questionDatasets/comment.yml",
            "datasets/questionDatasets/commentQuestion.yml",
            "datasets/questionDatasets/answer.yml",
            "datasets/questionDatasets/tag.yml",
            "datasets/questionCommentDatasets/user.yml",
            "datasets/questionDatasets/question.yml",
            "datasets/questionDatasets/questionHasTag.yml",
            "datasets/questionDatasets/reputation.yml"
    })
    public void shouldNotGetQuestionCommentByQuestionId() throws Exception {
        mvc.perform(get("/api/user/question/500/comment")
                .header("Authorization", getJwtToken("test@mail.ru", "password")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Question with id 500 not found!"));
    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/answer.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/question.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/reputation.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/role.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/tag.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/user.yml",
            "datasets/QuestionResourceController/getQuestionDtoByTagIdDatasets/voteQuestion.yml",})
    void getQuestionDtoByTagId() throws Exception {

        //проверяем возвращаемый Response. В датасетах 3 вопроса c id 100, 101, 102, имеющих связь с TagId 100,
        //но вопрос c id 102 имеет поле IsDeleted=true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/tag/100?page=1")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулись нужные QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))
                //Проверяем, что значения полей QuestionDto, например, с id 100 заполнены
                .andExpect(jsonPath("$.items.[0].title").value("title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(6))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description")
                        .value("description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(3))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                //Проверяем, что нужные QuestionDto также выгрузили список всех tags, связанныех с ними
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description")
                        .value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$.items.[0].listTagDto.[2].id").value(102));
    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/answer.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/question.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/role.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/tag.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/voteQuestion.yml",
            "datasets/QuestionResourceController/getQuestionDtoNoAnswerDatasets/user.yml"})
    void getQuestionDtoNoAnswer() throws Exception {

        //В датасетах 4 вопроса c id 100, 102, 103 и 104 на которые нет ответа,
        // но вопрос c id 102 имеет поле IsDeleted=true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулась нужная QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(103))
                .andExpect(jsonPath("$.items.[2].id").value(104))
                //Проверяем, что значения полей QuestionDto, например, с id 100 заполнены
                .andExpect(jsonPath("$.items.[0].title").value("title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description")
                        .value("description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(0))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                //Проверяем, что нужное QuestionDto также выгрузила список всех tags, связанных с ним
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101));

        //Проверяем, что по trackedTags подходит только question с id 100
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулась нужная QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100));

        //Проверяем, что по ignoredTags подходит только question с id 103 и 104
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&ignoredTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулась нужная QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(103))
                .andExpect(jsonPath("$.items.[1].id").value(104));

        //Проверяем, что по trackedTags и по ignoredTags подходит только question с id 100 и 104
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/question/noAnswer?page=1&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулась нужная QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(104));

        //Проверка запроса, на который items не обнаружены, в данном случае по trackedTags
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTags=104")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(0))
                .andExpect(jsonPath("$.totalResultCount").value(0))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getAllQuestionDtoDatasets/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/question.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/role.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/user.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/voteQuestion.yml",})
    void getAllQuestionDto() throws Exception {

        // Проверяем возвращаемый Response.
        // В датасетах 5 вопросов c id 100-105,
        // TagId = 100 относится к вопросам 100, 101, 102
        // TagId = 103 относится к вопросам 104, 105
        // Игнорим тег 101, который имеется у вопросов 100 и 103
        // Вопрос c id 102 имеет поле isDeleted = true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/?currentPageNumber=1&itemsOnPage=2&trackedTags=100,103&ignoredTags=101")
                        .header("Authorization", getJwtToken("test_user100@mail.ru","123")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                //Проверяем, что в pageDto подтянулись нужные QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(104))
                //Проверяем, что значения полей QuestionDto, например, с id 101 заполнены
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 101"))
                .andExpect(jsonPath("$.items.[0].authorId").value(101))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(2))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 101"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image101"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 101"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items.[0].countValuable").value(-1))
                //Проверяем, что нужные QuestionDto также выгрузили список всех tags, связанных с ними
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(102))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(103));
    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getAllQuestionDtoDatasets/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/question.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/role.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/user.yml",
            "datasets/QuestionResourceController/getAllQuestionDtoDatasets/voteQuestion.yml",})
    // Тест для QuestionResourceController::getAllQuestionDto, только без tracked и ignored тегов с фронта и
    // с дефолтным количеством результатов на странице (10)
    void getAllQuestionDtoWithDefaultValuesFromFront() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/?currentPageNumber=1")
                        .header("Authorization", getJwtToken("test_user100@mail.ru","123")))
                .andExpect(status().isOk())
                //Проверяем собранный PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(5))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                //Проверяем, что в pageDto подтянулись нужные QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))
                .andExpect(jsonPath("$.items.[2].id").value(103))
                .andExpect(jsonPath("$.items.[3].id").value(104))
                .andExpect(jsonPath("$.items.[4].id").value(105))
                //Проверяем, что значения полей QuestionDto, например, с id 100 заполнены
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(6))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(3))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                //Проверяем, что нужные QuestionDto также выгрузили список всех tags, связанных с ними
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$.items.[0].listTagDto.[2].id").value(102))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].listTagDto.[1].id").value(102))
                .andExpect(jsonPath("$.items.[2].listTagDto.[0].id").value(101))
                .andExpect(jsonPath("$.items.[3].listTagDto.[0].id").value(103))
                .andExpect(jsonPath("$.items.[4].listTagDto.[0].id").value(103));
    }
}